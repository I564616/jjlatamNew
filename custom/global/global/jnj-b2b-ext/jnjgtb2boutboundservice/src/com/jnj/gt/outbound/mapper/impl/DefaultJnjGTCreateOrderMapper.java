/**
 *
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPOutput;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OutOrderLines;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.ScheduledLines;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTCreateOrderResponseData;
import com.jnj.core.data.JnjGTOrderChangeResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTCreateOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTReqOrderChangeMapper;
import com.jnj.gt.outbound.services.JnjGTCreateOrderService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * The jnjGTCreateOrderMapperImpl class contains the definition of all the method of the jnjGTCreateOrderMapper
 * interface.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTCreateOrderMapper implements JnjGTCreateOrderMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();
	public static boolean isLotCommentExisted = false;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjGTCreateOrderService jnjGTCreateOrderService;

	@Autowired
	private JnjCartService jnjCartService;

	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	@Autowired
	private JnjGTReqOrderChangeMapper jnjGTReqOrderChangeMapper;

	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	@Autowired
	private ModelService modelService;

	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjGTCreateOrderService getjnjGTCreateOrderService() {
		return jnjGTCreateOrderService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public JnjGTReqOrderChangeMapper getJnjGTReqOrderChangeMapper() {
		return jnjGTReqOrderChangeMapper;
	}

	public JnjGTOrderService getJnjGTOrderService() {
		return jnjGTOrderService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws ParseException
	 * @throws BusinessException
	 */
	@Override
	public JnjGTCreateOrderResponseData mapCreateOrderRequestResponse(final OrderModel orderModel, final JnjGTSapWsData sapWsData)
			throws IntegrationException, SystemException, ParseException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapCreateOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String holdCodeForLotComment = null;
		isLotCommentExisted = false;
		final JnjGTCreateOrderResponseData jnjGTCreateOrderResponseData = new JnjGTCreateOrderResponseData();

		final OrderCreationInSAPInput orderCreationInSAPInput = new OrderCreationInSAPInput();
		if (null != orderModel.getOrderType())
		{
			orderCreationInSAPInput.setInOrderType(orderModel.getOrderType().getCode());
			if (StringUtils.equals(JnjOrderTypesEnum.ZEX.getCode(), orderModel.getOrderType().getCode()))
			{
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.ORDER_REASON, Jnjgtb2boutboundserviceConstants.ORDER_REASON_FOR_INTERNATIONAL);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					orderCreationInSAPInput.setInOrderReason(objectFactory.createOrderCreationInSAPInputInOrderReason(jnjConfigModels
							.get(0).getValue()));
				}
				// check for the empty value.
				if (StringUtils.isNotEmpty(orderModel.getDealerPONum()))
				{
					final JAXBElement<String> dealerPO = objectFactory.createOrderCreationInSAPInputInDealerPONumber(orderModel
							.getDealerPONum());
					orderCreationInSAPInput.setInDealerPONumber(dealerPO);
				}
			}
			else if (StringUtils.equals(JnjOrderTypesEnum.ZNC.getCode(), orderModel.getOrderType().getCode()))
			{
				orderCreationInSAPInput.setInOrderReason(objectFactory.createOrderCreationInSAPInputInOrderReason(orderModel
						.getReasonCode()));
				orderCreationInSAPInput.setInDealerPONumber(objectFactory.createOrderCreationInSAPInputInDealerPONumber(orderModel
						.getPurchaseOrderNumber()));
			}
			else
			{
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.ORDER_REASON, Jnjgtb2boutboundserviceConstants.ORDER_REASON_FOR_STANDARD);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					orderCreationInSAPInput.setInOrderReason(objectFactory.createOrderCreationInSAPInputInOrderReason(jnjConfigModels
							.get(0).getValue()));
				}
				// check for the empty value.
				if (StringUtils.isNotEmpty(orderModel.getDealerPONum()))
				{
					final JAXBElement<String> dealerPO = objectFactory.createOrderCreationInSAPInputInDealerPONumber(orderModel
							.getDealerPONum());
					orderCreationInSAPInput.setInDealerPONumber(dealerPO);
				}
			}
		}
		if (null != orderModel.getUnit())
		{
			orderCreationInSAPInput.setInSoldToNumber(orderModel.getUnit().getUid());
		}
		if (null != orderModel.getDeliveryAddress())
		{
			orderCreationInSAPInput.setInShipToNumber(orderModel.getDeliveryAddress().getJnJAddressId());
		}
		if (null != orderModel.getPaymentAddress())
		{
			final JAXBElement<String> billToNumber = objectFactory.createOrderCreationInSAPInputInBillToNumber(orderModel
					.getPaymentAddress().getJnJAddressId());
			orderCreationInSAPInput.setInBillToNumber(billToNumber);
		}
		// check for the not empty or not null
		if (BooleanUtils.isTrue(orderModel.getThirdpartyBilling()))
		{
			orderCreationInSAPInput.setInDropShipIndicator(Jnjgtb2boutboundserviceConstants.Y_STRING);
		}
		else
		{
			orderCreationInSAPInput.setInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		final JAXBElement<String> gatewayRelatedOrderNo = objectFactory
				.createOrderCreationInSAPInputInGatewayRelatedOrdernumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		orderCreationInSAPInput.setInGatewayRelatedOrdernumber(gatewayRelatedOrderNo);

		if (StringUtils.isNotEmpty(orderModel.getCordisHouseAccount()))
		{
			final JAXBElement<String> houseAccount = objectFactory.createOrderCreationInSAPInputInHouseAccount(orderModel
					.getCordisHouseAccount());
			orderCreationInSAPInput.setInHouseAccount(houseAccount);
		}
		orderCreationInSAPInput.setInGatewayOrdernumber(orderModel.getCode());
		orderCreationInSAPInput.setInPONumber(orderModel.getPurchaseOrderNumber());
		// Fetch value from the config table.
		orderCreationInSAPInput.setInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));

		final JAXBElement<String> holdCode = objectFactory.createOrderCreationInSAPInputInHoldCode(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.HOLD_CODE));
		orderCreationInSAPInput.setInHoldCode(holdCode);
		orderCreationInSAPInput.setInOrderSource(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_SOURCE));

		if (StringUtils.isNotEmpty(orderModel.getAttention()))
		{
			orderCreationInSAPInput.setInCustomerNotes(objectFactory.createOrderCreationInSAPInputInCustomerNotes(orderModel
					.getAttention()));
		}
		else
		{
			orderCreationInSAPInput.setInCustomerNotes(objectFactory
					.createOrderCreationInSAPInputInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}

		final JAXBElement<String> deliveryDate = objectFactory
				.createOrderCreationInSAPInputInRequestedDeliveryDate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		orderCreationInSAPInput.setInRequestedDeliveryDate(deliveryDate);
		if (null != orderModel.getUser())
		{
			// check for the non empty value if its empty or null then set empty value.
			orderCreationInSAPInput.setInContactName(StringUtils.isNotEmpty(orderModel.getUser().getName()) ? orderModel.getUser()
					.getName() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);

			// Iterates the Address Model to find the contact address and get phone1 value from it.
			for (final AddressModel addressModel : orderModel.getUser().getAddresses())
			{
				if (null != addressModel && null != addressModel.getContactAddress()
						&& addressModel.getContactAddress().booleanValue())
				{
					orderCreationInSAPInput.setInContactphonenumber(addressModel.getPhone1());
					break;
				}
			}
		}
		// Set info related to Credit Card
		if (null != orderModel.getPaymentInfo())
		{
			final JnjGTCreditCardModel jnjGTCreditCardModel = (JnjGTCreditCardModel) orderModel.getPaymentInfo();

			final JAXBElement<String> cardSequenceNumber = objectFactory
					.createOrderCreationInSAPInputInCreditCardSequenceNumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			orderCreationInSAPInput.setInCreditCardSequenceNumber(cardSequenceNumber);

			if (null != jnjGTCreditCardModel.getType())
			{
				final String crediCardType = StringUtils.substring(jnjGTCreditCardModel.getType().getCode(), 2);
				final JAXBElement<String> cardType = objectFactory.createOrderCreationInSAPInputInCreditCardType(crediCardType);
				orderCreationInSAPInput.setInCreditCardType(cardType);
			}
			if (null != jnjGTCreditCardModel.getCreditCardtoken())
			{
				final JAXBElement<String> cardAccountNo = objectFactory
						.createOrderCreationInSAPInputInCreditCardAccountNumber(jnjGTCreditCardModel.getCreditCardtoken());
				orderCreationInSAPInput.setInCreditCardAccountNumber(cardAccountNo);
			}
			if (StringUtils.isNotEmpty(jnjGTCreditCardModel.getValidToMonth())
					&& StringUtils.isNotEmpty(jnjGTCreditCardModel.getValidToYear()))
			{
				final String expirationYear = Config.getParameter(Jnjgtb2boutboundserviceConstants.CREDIT_CARD_YEAR_INITIAL).concat(
						jnjGTCreditCardModel.getValidToYear());
				final Calendar calendar = new GregorianCalendar(Integer.parseInt(expirationYear),
						Integer.parseInt(jnjGTCreditCardModel.getValidToMonth()), 0);
				final Date formattedDate = calendar.getTime();

				// Format the Date in requested date format and set it in the expire field.
				final JAXBElement<String> expireDate = objectFactory
						.createOrderCreationInSAPInputInCreditCardExpirationCard(new SimpleDateFormat(Config
								.getParameter(Jnjgtb2boutboundserviceConstants.REQUEST_DATE_FORMAT)).format(formattedDate));
				orderCreationInSAPInput.setInCreditCardExpirationCard(expireDate);
			}
		}
		if (CollectionUtils.isNotEmpty(orderModel.getEntries()))
		{
			orderCreationInSAPInput.getInOrderLines().addAll(mapOrderLinesFromAbstOrderEntries(orderModel.getEntries()));
			if (isLotCommentExisted)
			{
				holdCodeForLotComment = jnjConfigService
						.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.HOLD_CODE_WITH_LOT_NO);
				orderCreationInSAPInput.setInHoldCode(objectFactory.createOrderCreationInSAPInputInHoldCode(holdCodeForLotComment));
			}
		}
		final OrderCreationInSAPOutput orderCreationInSAPOutput = jnjGTCreateOrderService.orderCreationInSAP(
				orderCreationInSAPInput, sapWsData);
		if (null != orderCreationInSAPOutput
				&& CollectionUtils.isNotEmpty(orderCreationInSAPOutput.getOutOrderLines())
				&& CollectionUtils.isNotEmpty(orderModel.getEntries())
				&& (null == orderCreationInSAPOutput.getErrorMessage() || StringUtils.isEmpty(orderCreationInSAPOutput
						.getErrorMessage().getValue())) && !isLotCommentExisted)
		{
			final JnjGTOrderChangeResponseData jnjGTOrderChangeResponseData = jnjGTReqOrderChangeMapper
					.mapChangeOrderRequestResponse(orderModel, orderCreationInSAPOutput.getOutSalesOrderNumber(), true);
			if (jnjGTOrderChangeResponseData.isSapResponseStatus())
			{
				orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				orderModel.setRiskCategory(orderCreationInSAPOutput.getRiskCategory());
				mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
				//Calculate cart for total values and save in data base
				jnjGTCreateOrderResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModel, true));
				jnjGTCreateOrderResponseData.setSapResponseStatus(true);
				//AAOL-2420 changes
				jnjGTCreateOrderResponseData.setCreditLimitCode(Config.getParameter("test.credit.limit.message.code"));
				//end of AAOL-2420 changes
			}
			else
			{
				jnjGTCreateOrderResponseData.setSapResponseStatus(false);
				jnjGTCreateOrderResponseData.setErrorMessage(jnjGTOrderChangeResponseData.getErrorMessage());
			}
		}
		else if (null != orderCreationInSAPOutput
				&& CollectionUtils.isNotEmpty(orderCreationInSAPOutput.getOutOrderLines())
				&& CollectionUtils.isNotEmpty(orderModel.getEntries())
				&& (null == orderCreationInSAPOutput.getErrorMessage() || StringUtils.isEmpty(orderCreationInSAPOutput
						.getErrorMessage().getValue())))
		{
			orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			orderModel.setRiskCategory(orderCreationInSAPOutput.getRiskCategory());
			mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
			//Calculate cart for total values and save in data base
			jnjGTCreateOrderResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModel, true));
			jnjGTCreateOrderResponseData.setSapResponseStatus(true);
		}
		else if (null != orderCreationInSAPOutput && null != orderCreationInSAPOutput.getErrorMessage())
		{
			// check for the excluded customer check.
			mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
			jnjGTCreateOrderResponseData.setSapResponseStatus(false);
			jnjGTCreateOrderResponseData.setErrorMessage(orderCreationInSAPOutput.getErrorMessage().getValue());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapCreateOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//AAOL-2420 changes
		jnjGTCreateOrderResponseData.setCreditLimitCode(Config.getParameter("test.credit.limit.message.code"));
		//end of AAOL-2420 changes
		return jnjGTCreateOrderResponseData;
	}

	/**
	 * Map in order lines fields from abstract order entries model.
	 * 
	 * @param abstOrdEntModelList
	 *           the abstract order entry model list
	 * @return the array of in order lines
	 */
	protected List<InOrderLines> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<InOrderLines> inOrderLinesList = new ArrayList<InOrderLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InOrderLines inOrderLines = new InOrderLines();
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getProduct().getCode());
			inOrderLines.setQuantityRequested(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inOrderLines.setSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			final JAXBElement<String> customerLineNumber = objectFactory.createInOrderLinesCustomerLineNumber(String
					.valueOf(abstOrderEntryModel.getEntryNumber()));
			inOrderLines.setCustomerLineNumber(customerLineNumber);
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getLot()))
			{
				final JAXBElement<String> lot = objectFactory.createInOrderLinesLot(abstOrderEntryModel.getLot());
				inOrderLines.setLot(lot);
				isLotCommentExisted = true;
			}
			else
			{
				final JAXBElement<String> lot = objectFactory.createInOrderLinesLot(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setLot(lot);
			}

			// if the selected route is null or empty and it's not equal to route which comes from sap then set the route, shipping point and plant else set it empty.
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getSelectedRoute()))
			{
				final JAXBElement<String> route = objectFactory.createInOrderLinesRoute(abstOrderEntryModel.getSelectedRoute());
				inOrderLines.setRoute(route);
				if (StringUtils.isNotEmpty(abstOrderEntryModel.getShippingPoint()))
				{
					final JAXBElement<String> shippingPoint = objectFactory.createInOrderLinesShippingPoint(abstOrderEntryModel
							.getShippingPoint());
					inOrderLines.setShippingPoint(shippingPoint);
				}
				if (StringUtils.isNotEmpty(abstOrderEntryModel.getPlant()))
				{
					final JAXBElement<String> plant = objectFactory.createInOrderLinesPlant(abstOrderEntryModel.getPlant());
					inOrderLines.setPlant(plant);
				}
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.UNLOADING_POINT_ID, Jnjgtb2boutboundserviceConstants.UNLOADING_POINT_KEY);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					final JAXBElement<String> unloadingPoint = objectFactory.createInOrderLinesUnloadingPoint(jnjConfigModels.get(0)
							.getValue());
					inOrderLines.setUnloadingPoint(unloadingPoint);
				}
			}
			else
			{
				final JAXBElement<String> route = objectFactory
						.createInOrderLinesRoute(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setRoute(route);
				final JAXBElement<String> shippingPoint = objectFactory
						.createInOrderLinesShippingPoint(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setShippingPoint(shippingPoint);
				final JAXBElement<String> plant = objectFactory
						.createInOrderLinesPlant(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPlant(plant);
				final JAXBElement<String> unloadingPoint = objectFactory
						.createInOrderLinesUnloadingPoint(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setUnloadingPoint(unloadingPoint);
			}

			// add the object in the list
			inOrderLinesList.add(inOrderLines);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return inOrderLinesList;
	}


	/**
	 * Map order model from out order line.
	 * 
	 * @param orderModel
	 *           the order model
	 * @param orderCreationInSAPOutput
	 *           the order creation in sap output
	 * @throws SystemException
	 *            the system exception
	 * @throws ParseException
	 * @throws BusinessException
	 */
	protected void mapOrderModelFromOutOrderLine(final OrderModel orderModel, final OrderCreationInSAPOutput orderCreationInSAPOutput)
			throws SystemException, ParseException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapOrderModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalNetValue = 0.0;
		double totalTax = 0.0;
		double dropShipFee = 0.0;
		double totalFreightFees = 0.0;
		double minimumOrderFee = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		CatalogVersionModel catalogVersionModel = null;
		final CurrencyModel currencyModel = orderModel.getCurrency();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : orderModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}
		final List<String> itemCategories = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.ITEM_CATEGORY_FOR_MDD,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> lineNumberExcluded = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.LINE_NUMBER_EXCLUDED,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// To fetch active Catalog Version Model.
		if (null != orderModel.getSite() && CollectionUtils.isNotEmpty(orderModel.getSite().getStores()))
		{
			if (CollectionUtils.isNotEmpty(orderModel.getSite().getStores().get(0).getCatalogs()))
			{
				catalogVersionModel = orderModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}

		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (final OutOrderLines outOrderLine : orderCreationInSAPOutput.getOutOrderLines())
		{
			// In case of excluded customer, enter inside if block.
			if (null != outOrderLine && null != outOrderLine.getLineNumber()
					&& !lineNumberExcluded.contains(outOrderLine.getLineNumber().getValue()))
			{
				try
				{
					if (null != outOrderLine.getItemCategory()
							&& !(itemCategories.contains(outOrderLine.getItemCategory().getValue()))
							&& null != outOrderLine.getMaterialNumber() && null != outOrderLine.getMaterialEntered())
					{
						AbstractOrderEntryModel abstOrdEntryModel = null;
						JnJProductModel product = null;
						JnJProductModel productEntered = null;
						ProductModel baseProduct = null;
						ProductModel baseProductEntered = null;
						if (StringUtils.equals(outOrderLine.getMaterialNumber().getValue(), outOrderLine.getMaterialEntered()
								.getValue()))
						{
							product = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							if (null != product)
							{
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
								baseProductEntered = baseProduct;
							}
						}
						else
						{
							product = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							productEntered = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialEntered().getValue(),
									catalogVersionModel);
							if (null != product)
							{
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
							}
							if (null != productEntered)
							{
								baseProductEntered = productEntered.getMaterialBaseProduct() == null ? productEntered : productEntered
										.getMaterialBaseProduct();
							}
						}
						// Check if the base product entered is not null.
						if (null != baseProductEntered && null != baseProduct)
						{
							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProductEntered.getCode()))
							{
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProductEntered.getCode());
							abstOrdEntryModel.setProduct(baseProduct);
							// check for the null value and set it in Sap Order Line Number.
							if (null != outOrderLine.getLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(outOrderLine.getLineNumber().getValue());
							}
							if (null != outOrderLine.getBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outOrderLine.getBaseUOM().getValue());
								abstOrdEntryModel.setBaseUOM(unitModel);
							}
							if (null != outOrderLine.getMessage())
							{
								abstOrdEntryModel.setMessage(outOrderLine.getMessage().getValue());
							}
							if (null != outOrderLine.getSubstitutionReason())
							{
								abstOrdEntryModel.setSubstitutionReason(outOrderLine.getSubstitutionReason().getValue());
							}
							if (null != outOrderLine.getSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outOrderLine.getSalesUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
								if (CollectionUtils.isNotEmpty(baseProduct.getVariants()))
								{
									// Get the Variant Product Models of the base product.
									final List<VariantProductModel> variantProductModels = (List) baseProduct.getVariants();
									// Iterate them one by one.
									for (final VariantProductModel variantProductModel : variantProductModels)
									{
										// Check it for not null value for the models and equate the unit model code of the Variant model with the incoming response sales uom.
										if (null != variantProductModel
												&& null != variantProductModel.getUnit()
												&& variantProductModel.getUnit().getCode()
														.equalsIgnoreCase(outOrderLine.getSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}

							if (null != outOrderLine.getDiscounts() && StringUtils.isNotEmpty(outOrderLine.getDiscounts().getValue()))
							{
								abstOrdEntryModel.setDiscountValues(createDiscountValues(
										(Double.valueOf(outOrderLine.getDiscounts().getValue())), currencyModel));
								totalDiscounts = totalDiscounts + Double.valueOf(outOrderLine.getDiscounts().getValue()).doubleValue();
							}
							if (null != outOrderLine.getTax() && StringUtils.isNotEmpty(outOrderLine.getTax().getValue()))
							{
								abstOrdEntryModel.setTaxValues(createTaxValues((Double.valueOf(outOrderLine.getTax().getValue())),
										currencyModel));

								totalTax = totalTax + Double.valueOf(outOrderLine.getTax().getValue()).doubleValue();
							}
							if (null != outOrderLine.getHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(outOrderLine.getHigherLevelItemNumber().getValue());
							}

							abstOrdEntryModel.setItemCategory(outOrderLine.getItemCategory().getValue());
							if (null != outOrderLine.getRoute())
							{
								abstOrdEntryModel.setRoute(outOrderLine.getRoute().getValue());
							}
							if (null != outOrderLine.getShippingPoint())
							{
								abstOrdEntryModel.setShippingPoint(outOrderLine.getShippingPoint().getValue());
							}
							if (null != outOrderLine.getPriceType())
							{
								abstOrdEntryModel.setPriceType(outOrderLine.getPriceType().getValue());
							}
							if (null != outOrderLine.getHSAPromotion()
									&& StringUtils.isNotEmpty(outOrderLine.getHSAPromotion().getValue()))
							{
								abstOrdEntryModel.setHsaPromotion(Double.valueOf(outOrderLine.getHSAPromotion().getValue()));
								hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
							}
							if (null != outOrderLine.getFreightAndHandling()
									&& StringUtils.isNotEmpty(outOrderLine.getFreightAndHandling().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outOrderLine.getFreightAndHandling().getValue()));
								totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
							}
							if (null != outOrderLine.getDropShipFee()
									&& StringUtils.isNotEmpty(outOrderLine.getDropShipFee().getValue()))
							{
								abstOrdEntryModel.setDropshipFee(Double.valueOf(outOrderLine.getDropShipFee().getValue()));
								dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
							}
							if (null != outOrderLine.getFeeMin() && StringUtils.isNotEmpty(outOrderLine.getFeeMin().getValue()))
							{
								abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(outOrderLine.getFeeMin().getValue()));
								minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
							}
							if (null != outOrderLine.getNetValue() && StringUtils.isNotEmpty(outOrderLine.getNetValue().getValue()))
							{
								abstOrdEntryModel.setNetPrice(Double.valueOf(outOrderLine.getNetValue().getValue()));
								totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
							}
							// To set the schedule lines information in order model.
							if (CollectionUtils.isNotEmpty(outOrderLine.getScheduledLines()))
							{
								long quantity = 0;

								if (CollectionUtils.isNotEmpty(abstOrdEntryModel.getDeliverySchedules()))
								{
									try
									{
										modelService.removeAll(abstOrdEntryModel.getDeliverySchedules());
									}
									catch (final ModelRemovalException exception)
									{
										LOGGER.error(exception.getMessage());
										abstOrdEntryModel.setDeliverySchedules(null);
									}
								}

								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues(
										Jnjgtb2boutboundserviceConstants.EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								for (final ScheduledLines scheduledLines : outOrderLine.getScheduledLines())
								{
									// Check for the not null object
									if (null != scheduledLines)
									{
										final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
										jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
										if (null != scheduledLines.getLineNumber())
										{
											jnjDelSchModel.setLineNumber(scheduledLines.getLineNumber().getValue());
										}
										if (null != scheduledLines.getScheduledLineNumber())
										{
											jnjDelSchModel.setScheduledLineNumber(scheduledLines.getScheduledLineNumber().getValue());
										}
										if (null != scheduledLines.getLineStatus())
										{
											jnjDelSchModel.setLineStatus(scheduledLines.getLineStatus().getValue());
										}
										if (null != scheduledLines.getQuantity()
												&& StringUtils.isNotEmpty(scheduledLines.getQuantity().getValue()))
										{
											jnjDelSchModel
													.setQty(JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity().getValue()));
											quantity = quantity
													+ (JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity().getValue()).longValue());
										}
										if (null != scheduledLines.getDeliveryDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getDeliveryDate().getValue()))
										{
											jnjDelSchModel.setDeliveryDate(formatResponseDate(scheduledLines.getDeliveryDate().getValue()));
										}// if block loop
										if (null != scheduledLines.getMaterialAvailabilityDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getMaterialAvailabilityDate().getValue()))
										{
											jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(scheduledLines
													.getMaterialAvailabilityDate().getValue()));

										}// if block loop
										if (null != scheduledLines.getShipDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getShipDate().getValue()))
										{
											jnjDelSchModel.setShipDate(formatResponseDate(scheduledLines.getShipDate().getValue()));
										}// if block loop
										jnjDelSchModelList.add(jnjDelSchModel);
									}
								}
								abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
								abstOrdEntryModel.setQuantity(Long.valueOf(quantity));
							}
							jnjGTOrderService.populateMddOrderEntryStatus(abstOrdEntryModel);
							if (null != outOrderLine.getGrossPrice() && StringUtils.isNotEmpty(outOrderLine.getGrossPrice().getValue())
									&& null != outOrderLine.getAvailableQuantity()
									&& StringUtils.isNotEmpty(outOrderLine.getAvailableQuantity().getValue()))
							{
								abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double.parseDouble(outOrderLine
										.getGrossPrice().getValue()) + Double.parseDouble(outOrderLine.getDiscounts().getValue()))
										/ (abstOrdEntryModel.getQuantity().doubleValue()))));
								abstOrdEntryModel.setTotalPrice(Double.valueOf(Double
										.parseDouble(outOrderLine.getGrossPrice().getValue())
										+ Double.parseDouble(outOrderLine.getDiscounts().getValue())));
								grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
							}
						}

					}
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CREATE_ORDER + Logging.HYPHEN + "mapOrderModelFromOutOrderLine()" + Logging.HYPHEN
							+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
					throw exception;
				}
			}
			else
			{
				throw new BusinessException();
			}
		}
		// Populates the order level value in order model.
		orderModel.setTotalPrice(Double.valueOf(totalNetValue + totalTax));
		orderModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
		orderModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		orderModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		orderModel.setDeliveryCost(Double.valueOf(totalFreightFees - minimumOrderFee));
		orderModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		orderModel.setSubtotal(Double.valueOf(grossPrice));
		orderModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		orderModel.setTotalOtherCharge(Double.valueOf(totalNetValue - grossPrice - minimumOrderFee - dropShipFee + hsaPromotion));
		orderModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee));
		orderModel.setTotalTax(Double.valueOf(totalTax));
		orderModel.setStatus(OrderStatus.CREATED);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapOrderModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	/**
	 * Sets Discount Values for Order Entry.
	 * 
	 * @param discountValue
	 *           the discount value
	 * @param currencyModel
	 *           the currency model
	 * @return the list
	 */
	protected List<DiscountValue> createDiscountValues(final Double discountValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return discountValues;
	}

	/**
	 * Format response date and return the formatted date.
	 * 
	 * @param date
	 *           the date
	 * @return the date
	 * @throws SystemException
	 *            the system exception
	 */
	protected Date formatResponseDate(final String date) throws ParseException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Date formattedDate = null;
		try
		{
			if (StringUtils.isNotEmpty(date))
			{
				formattedDate = new SimpleDateFormat(Config.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT))
						.parse(date);
			}
		}
		catch (final ParseException exception)
		{
			LOGGER.error(Logging.CREATE_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ "Parsing Exception Occured " + exception.getMessage(), exception);
			throw exception;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return formattedDate;
	}

	/**
	 * Sets Tax values for Order Entry.
	 * 
	 * @param taxValue
	 *           the tax value
	 * @param currencyModel
	 *           the currency model
	 * @return the collection
	 */
	protected Collection<TaxValue> createTaxValues(final Double taxValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final TaxValue tax = new TaxValue(Jnjgtb2boutboundserviceConstants.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(currencyModel == null) ? null

				: currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<TaxValue>();
		taxValues.add(tax);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return taxValues;
	}
}
