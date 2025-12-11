/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.order.impl;

import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTIntermediateMasterModel;
import com.jnj.core.services.JnjGTDropshipmentService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.cart.JnjLatamCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.order.JnjLatamCheckoutFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import com.jnj.outboundservice.facades.orderSplit.impl.DefaultJnjLatamOrderSplitFacade;
import com.jnj.outboundservice.services.order.mapper.JnjLatamSalesOrderMapper;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.la.core.util.JnjLaSftpFileTransferUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.la.core.services.order.JnjLAOrderService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class JnjLatamCheckoutFacadeImpl extends DefaultJnjCheckoutFacade implements JnjLatamCheckoutFacade
{
	private static final String PO_TYPE_WEB = "E004";
	private static final Logger LOGGER = Logger.getLogger(JnjLatamCheckoutFacadeImpl.class);
	private final Class currentClass = JnjLatamCheckoutFacadeImpl.class;
	
	private static final String RETRY_COUNT = "jnj.la.send.erp.order.retrycount";
	private final int RETRY_DEFAULT_VAL = 3;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected ModelService modelService;
	
	@Autowired
    private JnjLAOrderService jnjLAOrderService;

	@Resource(name = "commerceCartService")
	protected JnjGTCartService jnjGTCartService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjLatamSalesOrderMapper jnjCreateOrderMapper;

	@Autowired
	protected JnjLatamCartFacade jnJLatamCartFacade;

	@Autowired
	protected JnjlatamOrderUtil orderUtil;

	@Autowired
	protected JnjGTDropshipmentService jnjGTDropShipmentService;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private DefaultJnjLatamOrderSplitFacade<JnjLatamSplitOrderInfo, AbstractOrderEntryModel> jnjLatamOrderSplitFacade;

	private static final String CHECK_PHARMA_PRODUCT = "isAtLeastOnePharamaProduct";

	private static final String CHECKOUT_TRUE_FLAG = "X";

	@Override
	public String placeOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException
	{
		final String methodName = "placeOrderInHybris()";
		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCheckoutFacadeImpl.class);

		String orderNumber = null;
		final CartModel cartModel = getCart();
		final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
		JnjLaCoreUtil.updateQuantityByFreeGoodLogic(cartModel, freeGoodsMap);
		populateFreeItemInHybris(cartModel);
		boolean orderPlaced = false;
		OrderModel orderModel = null;
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			cartModel.getPurchaseOrderNumber();
			cartModel.getComplementaryInfo();
			
			JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
					"Po number###########:" + cartModel.getPurchaseOrderNumber(), JnjLatamCheckoutFacadeImpl.class);
			JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
					"User ID###########:" + cartModel.getUser().getUid(), JnjLatamCheckoutFacadeImpl.class);
			
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				beforePlaceOrder(cartModel);
				orderModel = placeOrder(cartModel);
				orderPlaced = true;

				if (orderModel != null)
				{
					orderModel.setStatus(OrderStatus.PENDING);
					orderModel.setPoType(PO_TYPE_WEB);
					orderModel.setOrderNumber(orderModel.getCode());

					if (null == orderModel.getPurchaseOrderNumber())
					{
						orderModel.setPurchaseOrderNumber(orderModel.getOrderNumber());
					}

					if (null != cartModel.getComplementaryInfo())
					{
						orderModel.setComplementaryInfo(cartModel.getComplementaryInfo());
					}
					else
					{
						orderModel.setComplementaryInfo(StringUtils.EMPTY);
					}

					setCustomerFreightTypeInOrderModel(cartModel, orderModel);

					if (CollectionUtils.isNotEmpty(orderModel.getEntries()) && orderModel.getEntries().get(0) != null)
					{
						orderModel.setSalesOrganizationCode(orderModel.getEntries().get(0).getSalesOrg());
						final JnjOrderTypesEnum orderType = JnjOrderTypesEnum.valueOf(cartModel.getOrderType().getCode());
						orderModel.setOrderType(orderType);
						try
						{
							orderModel.setJnjOrderType(jnjLAOrderService.getOrderType(orderType.getCode()));
						}
						catch (final BusinessException e)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.PLACE_ORDER, methodName,
									"Exception Occurred while updating order type"+ " Exception:" + e.getMessage(), e,
									JnjLatamCheckoutFacadeImpl.class);
						}
					}

					b2bOrderService.saveOrder(orderModel);
					orderNumber = orderModel.getCode();
				}
			}
			populateFreeItemInHybris(cartModel);

			if (cartCleanUpRequired && orderPlaced) { //If SAP call is bypass cart needs to be clean
				afterPlaceOrder(cartModel, orderModel);
			}
		}

		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "Hybris order placed successfully#####"+orderNumber+Logging.BEGIN_OF_METHOD, JnjLatamCheckoutFacadeImpl.class);
		return orderNumber;
	}

	private static void setCustomerFreightTypeInOrderModel(CartModel cartModel, OrderModel orderModel) {
		if (StringUtils.isNotEmpty(cartModel.getCustomerFreightType())) {
			orderModel.setCustomerFreightType(cartModel.getCustomerFreightType());
		}
		else
		{
			orderModel.setCustomerFreightType(StringUtils.EMPTY);
		}
	}

	@Override
	protected CartModel getCart()
	{
		if (hasCheckoutCart())
		{
			return getCartService().getSessionCart();
		}

		return null;
	}

	@Override
	public boolean hasCheckoutCart()
	{
		return jnJLatamCartFacade.hasSessionCart();
	}

	@Override
	public SalesOrderCreationResponse createOrderInSAP(final String orderCode, final JnjGTSapWsData sapWsData)
			throws SystemException, IntegrationException, ParseException, BusinessException
	{
		final String methodName = "createOrderInSAP()";

		JnjGTCoreUtil.logDebugMessage(
				Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME, methodName,
				JnJCommonUtil.getCurrentDateTime(), JnjLatamCheckoutFacadeImpl.class);

		SalesOrderCreationResponse outboundCreateOrderData = null;

		/** try to load order model via admin user as unit may be different in case of REPLENISH order **/
		final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public OrderModel execute()
			{
				return b2bOrderService.getOrderForCode(orderCode);
			}
		}, userService.getAdminUser());

		if (JnjOrderTypesEnum.ZOR.equals(orderModel.getOrderType()) || JnjOrderTypesEnum.ZEX.equals(orderModel.getOrderType())
				|| JnjOrderTypesEnum.ZORD.equals(orderModel.getOrderType()) || JnjOrderTypesEnum.ZIND.equals(orderModel.getOrderType()) ||
				JnjOrderTypesEnum.ZINS.equals(orderModel.getOrderType()))

		{
			if (LOGGER.isDebugEnabled())
			{
				JnjGTCoreUtil.logInfoMessage("Create order", methodName, "Order type: " + orderModel.getOrderType(),
						JnjLatamCheckoutFacadeImpl.class);
			}
			outboundCreateOrderData = jnjCreateOrderMapper.mapSalesOrderCreationWrapper(orderModel);
		}

		JnjGTCoreUtil.logDebugMessage(
				Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME, methodName,
				JnJCommonUtil.getCurrentDateTime(), JnjLatamCheckoutFacadeImpl.class);
		return outboundCreateOrderData;
	}

	@Override
	protected void afterPlaceOrder(final CartModel cartModel, final OrderModel orderModel)
	{
		if (orderModel != null)
		{
			sessionService.removeAttribute("contractEntryList");
			getCartService().removeSessionCart();
            getModelService().refresh(orderModel);
			sessionService.removeAttribute("splitMap");
			sessionService.removeAttribute("jnjCartDataList");
		}
	}


	@Override
	public List<String> placeSplitOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException
	{
		final String methodName = "placeSplitOrderInHybris()";
		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, Logging.BEGIN_OF_METHOD, JnjLatamCheckoutFacadeImpl.class);

		final CartModel cartModel = getCart();
		final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
		JnjLaCoreUtil.updateQuantityByFreeGoodLogic(cartModel, freeGoodsMap);
		final List<String> ordercodeList = new ArrayList<>();

		OrderModel orderModel = null;
		boolean orderPlaced = false;
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			cartModel.getPurchaseOrderNumber();
			cartModel.getComplementaryInfo();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				beforePlaceOrder(cartModel);

				Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap;

				final CountryModel currentBaseStoreCountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
				final String baseStoreCountryIsoCode = JnjLaCommonUtil.getIdByCountry(currentBaseStoreCountry.getIsocode());


					JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
							"Current logged in Site:" + baseStoreCountryIsoCode, JnjLatamCheckoutFacadeImpl.class);
					JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
							"Po number###########:" + cartModel.getPurchaseOrderNumber(), JnjLatamCheckoutFacadeImpl.class);
					JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
							"User ID###########:" + cartModel.getPurchaseOrderNumber(), JnjLatamCheckoutFacadeImpl.class);
				
				splitOrderMap = jnjLatamOrderSplitFacade.splitOrder(cartModel, baseStoreCountryIsoCode);
				sessionService.setAttribute("splitMap", splitOrderMap);

				if (splitOrderMap != null && !splitOrderMap.isEmpty())
				{
					if (!splitOrderMap.containsValue(null))
					{
						for (final Map.Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> entry : splitOrderMap.entrySet())
						{
							final JnJLaB2BUnitModel jnJB2bUnitModel = (JnJLaB2BUnitModel) cartModel.getUnit();
							final String salesOrg = findSalesOrgForOrderCreation(jnJB2bUnitModel, entry.getValue());

							if (LOGGER.isDebugEnabled())
							{
								JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
										"JnjLatamCheckoutFacadeImpl salesOrg ::: " + salesOrg, JnjLatamCheckoutFacadeImpl.class);
								JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
										"Cart model PO number " + cartModel.getPurchaseOrderNumber(), JnjLatamCheckoutFacadeImpl.class);
								JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
										"Cart model Complementary Info " + cartModel.getComplementaryInfo(), JnjLatamCheckoutFacadeImpl.class);
							}
							cartModel.setEntries(entry.getValue());
							final JnjOrderTypesEnum orderType = JnjOrderTypesEnum.valueOf(entry.getKey().getDocOrderType());
							cartModel.setOrderType(orderType);
							cartModel.setCalculated(Boolean.TRUE);
							orderModel = placeOrder(cartModel);
							JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
									"Hybris Order number#### " + orderModel.getCode(), JnjLatamCheckoutFacadeImpl.class);
							if (orderModel != null)
							{
								ordercodeList.add(orderModel.getCode());
								orderModel.setStatus(OrderStatus.PENDING);
								orderModel.setPoType(PO_TYPE_WEB);
								orderModel.setOrderNumber(orderModel.getCode());
								try
								{
									orderModel.setJnjOrderType(jnjLAOrderService.getOrderType(orderType.getCode()));
								}
								catch (final BusinessException e)
								{
						            JnjGTCoreUtil.logErrorMessage(Logging.PLACE_ORDER, methodName,
											"Exception Occured while updating order type"+ " Exception: "
													+ e.getMessage(), e, JnjLatamCheckoutFacadeImpl.class);
								}

								if (null != cartModel.getPurchaseOrderNumber())
								{
									orderModel.setPurchaseOrderNumber(cartModel.getPurchaseOrderNumber());
								}
								if (null == orderModel.getPurchaseOrderNumber())
								{
									orderModel.setPurchaseOrderNumber(orderModel.getOrderNumber());
								}

								if (null != cartModel.getComplementaryInfo())
								{
									orderModel.setComplementaryInfo(cartModel.getComplementaryInfo());
								} else {
									orderModel.setComplementaryInfo(StringUtils.EMPTY);
								}

								if(null != cartModel.getEmpenhoFilesFullPath()) {
									orderModel.setEmpenhoFilesFullPath(cartModel.getEmpenhoFilesFullPath());
								}

								if (LOGGER.isDebugEnabled())
								{
									JnjGTCoreUtil.logInfoMessage("Place order in Hybris", methodName,
											"JnjLatamCheckoutFacadeImpl orderCode ::: " + orderModel.getCode() + " salesOrg ::: " + salesOrg,
											JnjLatamCheckoutFacadeImpl.class);
								}
								orderModel.setSalesOrganizationCode(salesOrg);

								final String b2bUnitCountryCode = jnJB2bUnitModel.getCountry().getIsocode();

								if (null != b2bUnitCountryCode
										&& (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(b2bUnitCountryCode)
												|| Jnjlab2bcoreConstants.COUNTRY_ISO_MEXICO.equalsIgnoreCase(b2bUnitCountryCode)))
								{
									final Boolean isAtLeastOnePharamaProduct = (Boolean) sessionService.getAttribute(CHECK_PHARMA_PRODUCT);
									if (null != isAtLeastOnePharamaProduct && isAtLeastOnePharamaProduct.equals(Boolean.TRUE)
											&& orderModel.getContractNumber() != null && !orderModel.getContractNumber().isEmpty())
									{
										orderModel.setForbiddenSales(CHECKOUT_TRUE_FLAG);
									}
								}
								else
								{
									if (entry.getKey().getForbiddenFlag() != null
											&& !entry.getKey().getForbiddenFlag().equals(StringUtils.EMPTY))
									{
										orderModel.setForbiddenSales(entry.getKey().getForbiddenFlag());
									}
								}
								for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
								{
									orderEntry.setStatus(OrderEntryStatus.PENDING.getCode());
									modelService.save(orderEntry);
								}

								// changes for the create order operational architecture.
								final JnjGTIntermediateMasterModel jnjNAIntMasterModel = modelService.create(JnjGTIntermediateMasterModel.class);
								jnjNAIntMasterModel.setRecordStatus(RecordStatus.PENDING);
								jnjNAIntMasterModel.setWriteAttempts(0);
								jnjNAIntMasterModel.setCreationtime(new Date());
								modelService.save(jnjNAIntMasterModel);
								b2bOrderService.saveOrder(orderModel);
							}
						}
						orderPlaced = true;
					}
					else
					{
						// In case splitOrderMap returns null or empty
						ordercodeList.add("dropshipment.unavailable");
						return ordercodeList;
					}
				}
			}

			populateFreeItemInHybris(cartModel);

			if (cartCleanUpRequired && orderPlaced) {
				afterPlaceOrder(cartModel, orderModel);
			}
		}

		JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "Hybris order placed successfully########## "
				+ ordercodeList.toString()+Logging.BEGIN_OF_METHOD, JnjLatamCheckoutFacadeImpl.class);
		return ordercodeList;

	}

	private void populateFreeItemInHybris(final CartModel cartModel)
	{
		if (sessionService != null)
		{
			// setting the actual quantity with which create order needs to be invoked
			final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (freeGoodsMap != null && abstOrdEntModel != null && freeGoodsMap.containsKey(abstOrdEntModel.getMaterialEntered()))
				{
					final JnjOutOrderLine outOrderLine = freeGoodsMap.get(abstOrdEntModel.getMaterialEntered());
					final String freeItemsQuantity = outOrderLine.getMaterialQuantity();
					final double orderedQuantity = Double.parseDouble(outOrderLine.getOrderedQuantity());
					final long orderedQty = (long) orderedQuantity;

					// Set the final Quantity
					abstOrdEntModel.setQuantity(orderedQty);
					abstOrdEntModel.setFreeItemsAvailabilityStatus(outOrderLine.getAvailabilityStatus());
					abstOrdEntModel.setFreeItemsQuanity(freeItemsQuantity);
					setFreeGoodsScheduledLines(outOrderLine, abstOrdEntModel);
				}
			}

			//Clear the free Goods from Session
			if (freeGoodsMap != null)
			{
				sessionService.removeAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			}
		}
	}

	private String findSalesOrgForOrderCreation(final JnJLaB2BUnitModel jnjB2BUnit,
			final List<AbstractOrderEntryModel> orderEntriesList){

		String sector = null;
		JnJLaProductModel jnjLaProductModel;
		String salesOrgValue = Strings.EMPTY;
		if (orderEntriesList != null && !orderEntriesList.isEmpty())
		{
			final ProductModel product = orderEntriesList.get(0).getProduct();
			if (product instanceof JnJLaProductModel)
			{
				jnjLaProductModel = (JnJLaProductModel) product;
				sector = jnjLaProductModel.getSector();
			}
		}

		final List<JnJSalesOrgCustomerModel> salesOrgList = jnjB2BUnit.getSalesOrg();

		for (final JnJSalesOrgCustomerModel salesOrg : salesOrgList)
		{
			if (salesOrg.getSector() != null && sector != null && salesOrg.getSector().equals(sector))
			{
				salesOrgValue = salesOrg.getSalesOrg();
			}
		}
		return salesOrgValue;
	}

	@Override
	public String placeQuoteOrder() throws InvalidCartException
	{
		return null;
	}

	@Override
	public String placeReturnOrder() throws InvalidCartException
	{
		return null;
	}

	private void setFreeGoodsScheduledLines(final JnjOutOrderLine jnjOutOrderLine,
			final AbstractOrderEntryModel abstractOrderEntryModel)
	{
		final List<JnjDeliveryScheduleData> scheduledLines = jnjOutOrderLine.getScheduleLines();
		List<JnjDeliveryScheduleModel> freeGoodsScheduleLines = null;
		if (scheduledLines != null && scheduledLines.size() > 0)
		{
			freeGoodsScheduleLines = new ArrayList<JnjDeliveryScheduleModel>();
			for (final JnjDeliveryScheduleData jnjDeliveryScheduleData : scheduledLines)
			{
				final JnjDeliveryScheduleModel jnjDeliveryScheduleModel = new JnjDeliveryScheduleModel();
				jnjDeliveryScheduleModel.setLineNumber(jnjDeliveryScheduleData.getLineNumber());
				jnjDeliveryScheduleModel.setDeliveryDate(jnjDeliveryScheduleData.getDeliveryDate());
				jnjDeliveryScheduleModel.setQty(jnjDeliveryScheduleData.getQuantity());
				jnjDeliveryScheduleModel.setRoundedQuantity(jnjDeliveryScheduleData.getRoundedQuantity());
				jnjDeliveryScheduleModel.setOwnerEntry(abstractOrderEntryModel);

				freeGoodsScheduleLines.add(jnjDeliveryScheduleModel);
			}
			abstractOrderEntryModel.setFreeGoodScheduleLines(freeGoodsScheduleLines);
		}
	}
	
	public void setEmpenhoFilesFullPath(final List<String> empenhoFilesFullPath) {
		final CartModel cartModel = getCart();
		cartModel.setEmpenhoFilesFullPath(empenhoFilesFullPath);
		modelService.save(cartModel);	
	}
	
	@Override
	public void createERPOrder(final List<OrderModel> orderList) {

		final String methodName = "createERPOrder() jnjLatamCheckoutFacade";
		int allowedRetryAttempts = Config.getParameter(RETRY_COUNT)==null ? RETRY_DEFAULT_VAL
				: Integer.valueOf(Config.getParameter(RETRY_COUNT));

		SalesOrderCreationResponse outboundCreateOrderData;
		for (final OrderModel order : orderList)
		{
			int retryAttempts = (order.getRetryAttempts()!=null)?order.getRetryAttempts():0;
			if(retryAttempts < allowedRetryAttempts) {
				final List<String> empenhoFilesFullPath = order.getEmpenhoFilesFullPath();
				String orderCode = order.getCode();
				List<String> errorMsg = new ArrayList<>();
				errorMsg.addAll(order.getSapErrorMessages());		
				try
				{
					boolean isRemoteUploadSuccess = false;
					// To fetch information and create order in sap.
					JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName, "START: Placing order in SAP #" + orderCode,
							currentClass);
	
					outboundCreateOrderData = createOrderInSAP(orderCode, null);
	
					if (null != empenhoFilesFullPath && outboundCreateOrderData != null
							&& outboundCreateOrderData.getSalesOrderOut() != null
							&& StringUtils.isNotEmpty(outboundCreateOrderData.getSalesOrderOut().getOutSalesOrderNumber()))
					{
						JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
								"File Upload to SFTP remote folder transfering: " + empenhoFilesFullPath.size() + "files.", currentClass);
						final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjLaSftpFileTransferUtil();
						isRemoteUploadSuccess = jnjSftpFileTransferUtil.uploadEmpenhoDocsToSftp(empenhoFilesFullPath,
								outboundCreateOrderData.getSalesOrderOut().getOutSalesOrderNumber(), orderCode);
					}
	
					JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
							"File Upload to SFTP remote folder status :" + isRemoteUploadSuccess, currentClass);
					JnjGTCoreUtil.logInfoMessage(Logging.PLACE_ORDER, methodName,
							"END: Placing order in SAP | SAP create Order Status" + outboundCreateOrderData, currentClass);
				}
				catch (final SystemException systemException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
							Logging.HYPHEN + "System Exception occurred " + systemException.getMessage(), systemException, currentClass);
					errorMsg.add("System Exception occurred :" + systemException.getMessage());
				}
				catch (final IntegrationException integrationException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
							Logging.HYPHEN + "Integration Exception occurred " + integrationException.getMessage(), integrationException,
							currentClass);
					errorMsg.add("Integration Exception occurred :" + integrationException.getMessage());
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
							Logging.HYPHEN + "Business Exception occured " + businessException.getMessage(), businessException,
							currentClass);
					errorMsg.add("Business Exception occurred :" + businessException.getMessage());
				}
				catch (final Exception throwable)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER + Logging.HYPHEN, methodName,
							Logging.HYPHEN + "Exception occured other then integration exception " + throwable.getMessage(), throwable,
							currentClass);
					errorMsg.add("Exception occurred :" + throwable.getMessage());
				}finally {
					retryAttempts = retryAttempts+1;
					order.setRetryAttempts(retryAttempts);
					if(retryAttempts >= allowedRetryAttempts && StringUtils.isEmpty(order.getSapOrderNumber()) ) {
						order.setStatus(OrderStatus.ERP_EXPORT_ERROR);
					}
					order.setSapErrorMessages(errorMsg);
					modelService.save(order);
				}
			}
		}
	}
}
