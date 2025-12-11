/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.order.impl.DefaultJnjCheckoutFacade;
import com.jnj.facades.orderSplit.JnjGTOrderSplitFacade;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.core.services.JnjGTDropshipmentService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.constants.CartViewMapping;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.dto.JnjGTSplitOrderInfo;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.model.JnjGTIntermediateMasterModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.facades.order.JnjGTCheckoutFacade;
import com.jnj.facades.order.JnjGTCreateSalesOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateConsOrdMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateConsignmentOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateDeliveredOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTCreateOrderMapper;


/**
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCheckoutFacade extends DefaultJnjCheckoutFacade implements JnjGTCheckoutFacade
{
	private static final String PO_TYPE_WEB = "E002";
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCheckoutFacade.class);
	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected JnjGTCreateConsOrdMapper jnjGTCreateConsOrdMapper;

	@Resource(name="GTCreateDeliveredOrderMapper")
	protected JnjGTCreateDeliveredOrderMapper jnjGTCreateDeliveredOrderMapper;
	
	@Resource(name="GTCreateConsignmentOrderMapper")
	protected JnjGTCreateConsignmentOrderMapper jnjGTCreateConsignmentOrderMapper;

	@Autowired
	protected JnjGTCreateOrderMapper jnjCreateOrderMapper;
	
	@Autowired
	protected ModelService modelService;

	@Resource(name = "commerceCartService")
	JnjGTCartService jnjGTCartService;
	@Autowired
	protected SessionService sessionService;
	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjOrderUtil orderUtil;

	@Autowired
	protected JnjGTDropshipmentService jnjGTDropShipmentService;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	protected JnjGTCreateSalesOrderMapper jnjGTCreateSalesOrderMapper;

	@Autowired
	protected Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	protected JnjGTOrderSplitFacade<JnjGTSplitOrderInfo, AbstractOrderEntryModel> jnjGTOrderSplitFacade;

	@Override
	public String placeQuoteOrder() throws InvalidCartException
	{
		String sapOrderNumber = null;
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				final OrderModel orderModel = placeOrder(cartModel);
				if (orderModel != null)
				{
					orderModel.setStatus(OrderStatus.CREATED);
					orderModel.setPoType(PO_TYPE_WEB);
					LOGGER.info("Method: placeQuoteOrder, poType was set to :"+PO_TYPE_WEB);
					//orderModel.setOrderNumber(orderModel.getSapOrderNumber());
					//AAOL-3620 begin
					orderModel.setOrderNumber(orderModel.getCode());
					orderModel.setSapOrderNumber(orderModel.getCode());
					//AAOL-3620 end
					b2bOrderService.saveOrder(orderModel);
				}
				if(cartModel.getSapOrderNumber() == null)
				{
					sapOrderNumber = orderModel.getCode();
				}
				else{
					sapOrderNumber = cartModel.getSapOrderNumber();
				}
				
				cartModel.setSapOrderNumber(sapOrderNumber);
				jnjGTCartService.saveCartModel(cartModel, false);
				afterPlaceOrder(cartModel, orderModel);
			}
		}
		return sapOrderNumber;
	}

	@Override
	public String placeReturnOrder() throws InvalidCartException
	{
		String orderPlaced = null;
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				final OrderModel orderModel = placeOrder(cartModel);
				if (orderModel != null)
				{
					orderModel.setOrderNumber(cartModel.getSapOrderNumber()); //Set SAP order number<Retrived while create order ws call>
					orderModel.setStatus(OrderStatus.CREATED);
					orderModel.setPoType(PO_TYPE_WEB);
					LOGGER.info("Method: placeReturnOrder, poType was set to :"+PO_TYPE_WEB);
					b2bOrderService.saveOrder(orderModel);
					afterPlaceOrder(cartModel, orderModel);
					orderPlaced = orderModel.getCode();
				}
			}
		}
		return orderPlaced;
	}

	@Override
	public JnjGTOutboundStatusData createOrderInSAP(final String orderCode, final JnjGTSapWsData sapWsData)
			throws SystemException, IntegrationException, ParseException, BusinessException
	{
		if (sapWsData.isTimeOutExtended())
		{
			sapWsData
			.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.CREATE_WS_EXTENDED_CONNECTION_TIME_OUT);
			sapWsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.ORDER_WS_EXTENDED_READ_TIME_OUT);
		}
		else
		{
			sapWsData
			.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.CREATE_WS_STANDARD_CONNECTION_TIME_OUT);
			sapWsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.ORDER_WS_STANDARD_READ_TIME_OUT);

		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjGTOutboundStatusData outboundCreateOrderData = null;

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
				|| JnjOrderTypesEnum.ZNC.equals(orderModel.getOrderType()))
		{
			outboundCreateOrderData = jnjCreateOrderMapper.mapCreateOrderRequestResponse(orderModel, sapWsData);
			
		}
		else if (JnjOrderTypesEnum.ZHOR.equals(orderModel.getOrderType())
				|| JnjOrderTypesEnum.ZTOR.equals(orderModel.getOrderType())
				|| JnjOrderTypesEnum.ZIO2.equals(orderModel.getOrderType()))
		{
			outboundCreateOrderData = jnjGTCreateConsOrdMapper.mapCreateConsOrdRequestResponse(orderModel, sapWsData);
		}
		else if (JnjOrderTypesEnum.ZDEL.equals(orderModel.getOrderType())
				|| JnjOrderTypesEnum.ZKB.equals(orderModel.getOrderType()))
		{
			outboundCreateOrderData = jnjGTCreateDeliveredOrderMapper.mapCreateDelOrderRequestResponse(orderModel, sapWsData);
		}
		else if (JnjOrderTypesEnum.KA.equals(orderModel.getOrderType())
				|| JnjOrderTypesEnum.KB.equals(orderModel.getOrderType())|| JnjOrderTypesEnum.KE.equals(orderModel.getOrderType()))
		{
			outboundCreateOrderData = jnjGTCreateConsignmentOrderMapper.mapCreateConsignmentOrderRequestResponse(orderModel, sapWsData);
		}
		
		outboundCreateOrderData.setSavedSuccessfully(true);
		sessionService.setAttribute("outboundCreateOrderData",outboundCreateOrderData);
		if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
		{
			afterPlaceOrder(getCart(), orderModel);
		}
		outboundCreateOrderData.setSavedSuccessfully(true);
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return outboundCreateOrderData;
	}

	public JnjGTOrderData createOrderInSAP(String orderCode,
			JnjGTSapWsData sapWsData, boolean code) throws SystemException,
	IntegrationException, ParseException, BusinessException {

		{
			if (sapWsData.isTimeOutExtended())
			{
				sapWsData
				.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.CREATE_WS_EXTENDED_CONNECTION_TIME_OUT);
				sapWsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.ORDER_WS_EXTENDED_READ_TIME_OUT);
			}
			else
			{
				sapWsData
				.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.CREATE_WS_STANDARD_CONNECTION_TIME_OUT);
				sapWsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.ORDER_WS_STANDARD_READ_TIME_OUT);

			}

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
						+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
			}

			//Change for Dropshipment and Split order

			JnjGTOutboundStatusData outboundCreateOrderData = null;

			boolean split = orderUtil.isOrderSplit();

			/** try to load order model via admin user as unit may be different in case of REPLENISH order **/
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2bOrderService.getOrderForCode(orderCode);
				}
			}, userService.getAdminUser());
			JnjGTOrderData orderData = null;
			/*final OrderModel orderModel = b2bOrderService.getOrderByCode(orderCode);*/
			modelService.save(orderModel);
			if(!split){

				if (JnjOrderTypesEnum.ZOR.equals(orderModel.getOrderType()) || JnjOrderTypesEnum.ZEX.equals(orderModel.getOrderType())
						|| JnjOrderTypesEnum.ZNC.equals(orderModel.getOrderType()))
				{
					outboundCreateOrderData = jnjCreateOrderMapper.mapCreateOrderRequestResponse(orderModel, sapWsData);  //sap call
				}
				else if (JnjOrderTypesEnum.ZHOR.equals(orderModel.getOrderType())
						|| JnjOrderTypesEnum.ZTOR.equals(orderModel.getOrderType())
						|| JnjOrderTypesEnum.ZIO2.equals(orderModel.getOrderType()))
				{
					outboundCreateOrderData = jnjGTCreateConsOrdMapper.mapCreateConsOrdRequestResponse(orderModel, sapWsData);
				}
				else if (JnjOrderTypesEnum.ZDEL.equals(orderModel.getOrderType())
						|| JnjOrderTypesEnum.ZKB.equals(orderModel.getOrderType()))
				{
					outboundCreateOrderData = jnjGTCreateDeliveredOrderMapper.mapCreateDelOrderRequestResponse(orderModel, sapWsData);  //sap call
				}
			}else
			{
				outboundCreateOrderData = jnjGTCreateSalesOrderMapper.mapSalesOrderCreationWrapper(orderModel,sapWsData); //sap call
			}

			if (null != outboundCreateOrderData)
			{
				orderData = new JnjGTOrderData();
				orderConverter.convert(orderModel, orderData);
			}
			outboundCreateOrderData.setSavedSuccessfully(true);
			sessionService.setAttribute("outboundCreateOrderData",outboundCreateOrderData);

			if (null != outboundCreateOrderData && outboundCreateOrderData.isSavedSuccessfully())
			{
				afterPlaceOrder(getCart(), orderModel);
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + "createOrderInSAP()" + Logging.HYPHEN + Logging.END_OF_METHOD
						+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
			}

			return orderData;
		}
	}

	@Override
	public String placeOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException
	{
		String orderNumber = null;
		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			cartModel.getPurchaseOrderNumber();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				beforePlaceOrder(cartModel);
				final OrderModel orderModel = placeOrder(cartModel);
				if (cartCleanUpRequired) //If SAP call is bypass cart needs to be clean
				{
					afterPlaceOrder(cartModel, orderModel);
				}
				if (orderModel != null)
				{
					orderModel.setStatus(OrderStatus.PENDING);
					orderModel.setPoType(PO_TYPE_WEB);
					LOGGER.info("Method: placeOrderInHybris, poType was set to :"+PO_TYPE_WEB);
					orderModel.setOrderNumber(orderModel.getCode());
					// changes for the create order operational architecture.
					final JnjGTIntermediateMasterModel jnjGTIntMasterModel = modelService.create(JnjGTIntermediateMasterModel.class);
					jnjGTIntMasterModel.setRecordStatus(RecordStatus.PENDING);
					jnjGTIntMasterModel.setWriteAttempts(Integer.valueOf(0));
					jnjGTIntMasterModel.setCreationtime(new Date());
					modelService.save(jnjGTIntMasterModel);
					//orderModel.setO
					b2bOrderService.saveOrder(orderModel);
					orderNumber = orderModel.getCode();
				}
			}
		}
		return orderNumber;
	}

	@Override
	protected void afterPlaceOrder(final CartModel cartModel, final OrderModel orderModel)
	{
		if (orderModel != null)
		{
			// Remove cart
			getCartService().removeSessionCart();
			getModelService().refresh(orderModel);
		}
	}

	@Override
	public List<String> placeSplitOrderInHybris(boolean cartCleanUpRequired) throws InvalidCartException 
	{
		final CartModel cartModel = getCart();
		List<String> ordercodeList = new ArrayList<String>();


		//Changes for Bonus Item starts
		if (sessionService != null)
		{
			// setting the actual quantity with which create order needs to be invoked
			final Map<String, JnjGTOutOrderLine> freeGoodsMap = sessionService.getAttribute("freeGoodsMap");
			for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
			{
				if (freeGoodsMap != null && abstOrdEntModel != null
						&& freeGoodsMap.containsKey(abstOrdEntModel.getProduct().getCode()))
				{
					final JnjGTOutOrderLine outOrderLine = freeGoodsMap.get(abstOrdEntModel.getProduct().getCode());
					final String freeItemsQuantity = outOrderLine.getMaterialQuantity();
					final String orderedQuantity = outOrderLine.getOrderedQuantity();
					final long orderedQuant = Long.valueOf(orderedQuantity);

					// Set the final Quantity
					abstOrdEntModel.setQuantity(orderedQuant);
					//					abstOrdEntModel.setfr

				}
			}

			//Clear the free Goods from Session
			if (freeGoodsMap != null)
			{
				sessionService.removeAttribute("freeGoodsMap");
			}
		}
		//Changes for Bonus Item ends

		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			cartModel.getPurchaseOrderNumber();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{			
				beforePlaceOrder(cartModel);

				final List<JnjGTCartData> jnjCartDataList = new ArrayList<JnjGTCartData>();
				Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = null;
				final List<String> productCodesList = new ArrayList<String>();
				final Map<String, AbstractOrderEntryModel> cartEntriesMap = new HashMap<String, AbstractOrderEntryModel>();
				//				final String destinationCountry = cartModel.getDeliveryAddress().getCountry().getIsocode();
				final CountryModel currentBaseStorycountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
				final String baseStoreCountryIsoCode = currentBaseStorycountry.getIsocode();

				for(AbstractOrderEntryModel abstOrderEntModel : cartModel.getEntries())
				{
					if(abstOrderEntModel!=null){

						final String productCode = new String(abstOrderEntModel.getProduct().getCode());
						productCodesList.add(productCode);
						cartEntriesMap.put(productCode, abstOrderEntModel);
					}			

				}

				splitOrderMap = jnjGTOrderSplitFacade.splitOrder(cartModel,baseStoreCountryIsoCode);

				if (splitOrderMap != null && !splitOrderMap.isEmpty())
				{
					if (!splitOrderMap.containsValue(null))
					{
						OrderModel orderModel = null;
						for (final Map.Entry<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> entry : splitOrderMap.entrySet())
						{
							cartModel.setEntries(entry.getValue());
							cartModel.setOrderType(JnjOrderTypesEnum.valueOf(entry.getKey().getDocorderType()));
							orderModel = placeOrder(cartModel);
							ordercodeList.add(orderModel.getCode());
							if (cartCleanUpRequired) //If SAP call is bypass cart needs to be clean
							{
								afterPlaceOrder(cartModel, orderModel);
							}
							if (orderModel != null)
							{
								orderModel.setStatus(OrderStatus.PENDING);
								orderModel.setPoType(PO_TYPE_WEB);
								LOGGER.info("Method: placeSplitOrderInHybris, poType was set to :"+PO_TYPE_WEB);
								orderModel.setOrderNumber(orderModel.getCode());
								// changes for the create order operational architecture.
								final JnjGTIntermediateMasterModel jnjGTIntMasterModel = modelService.create(JnjGTIntermediateMasterModel.class);
								jnjGTIntMasterModel.setRecordStatus(RecordStatus.PENDING);
								jnjGTIntMasterModel.setWriteAttempts(Integer.valueOf(0));
								jnjGTIntMasterModel.setCreationtime(new Date());
								modelService.save(jnjGTIntMasterModel);
								//orderModel.setO
								b2bOrderService.saveOrder(orderModel);
							}
						}
					}
					else
					{
						// In case splitOrderMap returns null or empty
						ordercodeList.add("dropshipment.unavailable");
						return ordercodeList;
					}
				}				
			}
		}
		return ordercodeList;	

	}

	@Override
	public String getPathForView(String checkoutconfirmationpage, String orderType) {
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		
		final String siteDir = CartViewMapping.cmsSiteCartDirMap.get(currentSite);
		final String dirForOrderType = CartViewMapping.orderTypeCartDirMap.get(orderType);
		return new StringBuilder(CartViewMapping.CartPageBaseDirPath).append(siteDir).append(dirForOrderType).append(checkoutconfirmationpage)
				.toString();

	}

}