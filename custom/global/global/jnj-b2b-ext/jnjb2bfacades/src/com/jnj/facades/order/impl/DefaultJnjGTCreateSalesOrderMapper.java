package com.jnj.facades.order.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.data.JnjGTCreateOrderResponseData;
import com.jnj.core.data.JnjGTOrderChangeResponseData;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.order.JnjGTCreateSalesOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPInput;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.OrderCreationInSAPOutput;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ScheduledLines3;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_ordercreate_v1.ordercreationinsapwebservice.ObjectFactory;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.mapper.JnjGTReqOrderChangeMapper;
import com.jnj.gt.outbound.services.JnjGTCreateOrderService;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTCreateSalesOrderMapper implements JnjGTCreateSalesOrderMapper {
	
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateSalesOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();
	public static boolean isLotCommentExisted = false;

	
	@Autowired
	protected JnjConfigService jnjConfigService;
	
	@Autowired
	protected JnjCartService jnjCartService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;
	
	@Autowired
	protected JnjGTOrderService jnjGTOrderService;
	
	@Autowired
	JnjOrderUtil orderUtil;
	
	@Autowired
	protected JnjGTCreateOrderService jnjGTCreateOrderService;
	
	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	
	@Autowired
	protected JnjGTReqOrderChangeMapper jnjReqOrderChangeMapper;
	
	@Autowired
	protected JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper;
	
	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;
	
	
	@Override
	public JnjGTOutboundStatusData mapSalesOrderCreationWrapper(OrderModel orderModel, JnjGTSapWsData sapWsData)
			throws IntegrationException, SystemException, BusinessException, ParseException {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderCreationWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		
		boolean isRegional = Boolean.valueOf(Config.getParameter(Jnjb2bCoreConstants.Dropshipment.IS_NON_GLOBAL));
		String holdCodeForLotComment = null;
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
		
		//Dropshipment changes
		List<OutOrderLines3> outOrderLineList = new ArrayList<OutOrderLines3>();
		final Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine = new HashMap<String, OutOrderLines3>();
		double salesOrderTotalNetValue = 0;		

		if(CollectionUtils.isEmpty(orderCreationInSAPOutput.getOutOrderLines())){
			if(!isRegional) {
				outOrderLineList= jnjGTOutOrderLineMapper.createConfirmationOutOrderList(orderModel);
			} else {
				//outOrderLineList =  Regional SAP call
			}
			outOrderLineList= jnjGTOutOrderLineMapper.createConfirmationOutOrderList(orderModel);
		}
		
		//Changes for Bonus Item starts

		final Map<String, JnjGTOutOrderLine> freeGoodsMapfromSession = sessionService.getAttribute("freeGoodsMap");

		Map<String, JnjGTOutOrderLine> freeGoodsMap = null;

		if (freeGoodsMapfromSession == null)
		{
			freeGoodsMap = new HashMap<String, JnjGTOutOrderLine>();

		}
		else
		{
			freeGoodsMap = new HashMap<String, JnjGTOutOrderLine>(freeGoodsMapfromSession);
		}
		
		for (OutOrderLines3 outOrderLine:outOrderLineList)
		{
			if (null != outOrderLine)
			{
				if (mapMaterialNumberWithOutOrderLine.containsKey(outOrderLine.getMaterialEntered())
							&& (outOrderLine.getItemCategory().equals("ZFNT")))
					{
						final JnjGTOutOrderLine jnjOutOrderLine = new JnjGTOutOrderLine();
	
						mapValuesToJnjOutOrderLine(jnjOutOrderLine, outOrderLine);
	
						freeGoodsMap.put(outOrderLine.getMaterialEntered(), jnjOutOrderLine);
					}
					else
					{
						mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
					}			
				}
			sessionService.setAttribute("freeGoodsMap", freeGoodsMap);
		}
		
		for (final AbstractOrderEntryModel entry : orderModel.getEntries()) {
			 for(OutOrderLines3 outOrderLine : outOrderLineList){
				 if(outOrderLine.getMaterialEntered().equalsIgnoreCase(entry.getProduct().getCode())
						 &&outOrderLine.getItemCategory().equalsIgnoreCase(Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY)){
					 	double chargedQuantity = Long.parseLong(outOrderLine.getMaterialQuantity());
						double productValue = chargedQuantity*entry.getBasePrice();
						salesOrderTotalNetValue = salesOrderTotalNetValue + productValue;
				 }

			 }
		 }
		//Changes for Bonus Item ends

		if(outOrderLineList!=null && !(outOrderLineList.isEmpty())){
			final JnjGTOrderChangeResponseData jnjGTOrderChangeResponseData = jnjReqOrderChangeMapper
					.mapChangeOrderRequestResponse(orderModel, orderCreationInSAPOutput.getOutSalesOrderNumber(), true);
			if (jnjGTOrderChangeResponseData.isSapResponseStatus())
			{
				if(orderCreationInSAPOutput.getOutSalesOrderNumber()!=null){
					orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				}
				else{
					orderModel.setSapOrderNumber(orderModel.getCode());
				}
				orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				orderModel.setRiskCategory(orderCreationInSAPOutput.getRiskCategory());
				//Calculate cart for total values and save in data base
				final OrderModel orderModelWithSubmitOrderData=mapOrderModelFromOutOrderLine(orderModel,
						mapMaterialNumberWithOutOrderLine,salesOrderTotalNetValue);
				jnjGTCreateOrderResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModelWithSubmitOrderData, true));
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
			if(orderCreationInSAPOutput.getOutSalesOrderNumber()!=null){
				orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			}
			else{
				orderModel.setSapOrderNumber(orderModel.getCode());
			}
			orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			orderModel.setRiskCategory(orderCreationInSAPOutput.getRiskCategory());
			//Calculate cart for total values and save in data base
			final OrderModel orderModelWithSubmitOrderData=mapOrderModelFromOutOrderLine(orderModel,
					mapMaterialNumberWithOutOrderLine,salesOrderTotalNetValue);
			jnjGTCreateOrderResponseData.setSavedSuccessfully(jnjCartService.calculateValidatedCart(orderModelWithSubmitOrderData));
			jnjGTCreateOrderResponseData.setSapResponseStatus(true);
		}
		else if (null != orderCreationInSAPOutput && null != orderCreationInSAPOutput.getErrorMessage())
		{
			// check for the excluded customer check.
			final OrderModel orderModelWithSubmitOrderData=mapOrderModelFromOutOrderLine(orderModel, mapMaterialNumberWithOutOrderLine,salesOrderTotalNetValue);
			jnjGTCreateOrderResponseData.setSapResponseStatus(false);
			jnjGTCreateOrderResponseData.setErrorMessage(orderCreationInSAPOutput.getErrorMessage().getValue());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderCreationWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//AAOL-2420 changes
		jnjGTCreateOrderResponseData.setCreditLimitCode(Config.getParameter("test.credit.limit.message.code"));
		//end of AAOL-2420 changes
		return jnjGTCreateOrderResponseData;
	}
	
	/**
	 * 
	 * @param orderModel
	 * @param outOrderLineList
	 * @throws SystemException
	 * @throws ParseException
	 * @throws BusinessException
	 */
	protected OrderModel  mapOrderModelFromOutOrderLine(final OrderModel orderModel, final Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine,
			double salesOrderTotalNetValue)throws SystemException, ParseException, BusinessException
	{
		
		double totalTax = 0;
		double totalFreightFees = 0;
		double totalExpeditedFee = 0;
		double handlingFee = 0;
		double dropShipFee = 0;
		double minimumOrderFee = 0;
		double totalInsuarance = 0;
		double totalDiscounts = 0;
		double grossPrice = 0;
		double taxValueForNetPrice = 0;
		JnjObjectComparator jnjObjectComparator = null;
		Set<String> scheduleLineDeliveryDate = null;


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Check the mapMaterialNumberWithOutOrderLine map as null and not empty.
		if (null != mapMaterialNumberWithOutOrderLine && !mapMaterialNumberWithOutOrderLine.isEmpty())
		{
			// Check the cartModel & cartModel Entries as null and not empty.
			if (null != orderModel && null != orderModel.getEntries() && !orderModel.getEntries().isEmpty())
			{
				//Get the Currency Associated with Cart : CP022 Changes
				final CurrencyModel currencyModel = orderModel.getCurrency();

				//Getting the B2bUnit with Cart: CP022 Changes
				final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) orderModel.getUnit();
				if (null == jnJB2bUnitModel)
				{
					throw new SystemException("No B2bUnit associated with Order having order No [" + orderModel.getCode() + "]");
				}

				//Fetch the Country Associated with Cart in Order to get Individual Taxes.: CP022 Changes
				final CountryModel countryModel = jnJB2bUnitModel.getCountry();

				if (null == countryModel)
				{
					throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
							+ "] for Order having order No [" + orderModel.getCode() + "]");
				}

				// List to store values for total calculation on cart split
				final List<OutOrderLines3> outOrderLinesList = new ArrayList<OutOrderLines3>();

				// Iterate the cartModel entries one by one and populates those value in cartModel.
				for (final AbstractOrderEntryModel abstOrdEntMode : orderModel.getEntries())
				{
					double materialQuantity = 0.0;
					double scheduleLineConfirmedQuantity = 0.0;
					double totalLineFee = 0.0;
					if (mapMaterialNumberWithOutOrderLine.containsKey(abstOrdEntMode.getProduct().getCode()))
					{
						final OutOrderLines3 outOrderLine = mapMaterialNumberWithOutOrderLine.get(abstOrdEntMode.getProduct().getCode());
						outOrderLinesList.add(outOrderLine);
						final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
						if (StringUtils.isNotEmpty(outOrderLine.getMaterialQuantity()))
						{
							abstOrdEntMode.setQuantity(Long.valueOf((long) Double.parseDouble(outOrderLine.getMaterialQuantity())));
							materialQuantity = Double.parseDouble(outOrderLine.getMaterialQuantity());
						}
						// changes Start for Defect 30947 Changes base vale to unit value and total price for entry to net value.
						if (StringUtils.isNotEmpty(outOrderLine.getUnitValue()))
						{
							final double basePrice = Double.parseDouble(outOrderLine.getUnitValue());
							// Check the default price and base price is not equal then only base price value is updated in default price
							if (Double.compare(abstOrdEntMode.getBasePrice().doubleValue(), basePrice) != 0)
							{
								abstOrdEntMode.setDefaultPrice(abstOrdEntMode.getBasePrice());
							}
							abstOrdEntMode.setBasePrice(Double.valueOf(basePrice));
						}
						else
						{
							if (LOGGER.isDebugEnabled())
							{
								LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
										+ "Inside else block when we get unit value as null or empty in SAP response ");
							}
						}

						if (StringUtils.isNotEmpty(outOrderLine.getTaxes()))
						{
							abstOrdEntMode.setTaxValues(createTaxValues((Double.valueOf(outOrderLine.getTaxes())), currencyModel));
							totalTax = totalTax + Double.valueOf(outOrderLine.getTaxes()).doubleValue();
							taxValueForNetPrice = totalTax;
						}
						
						if (StringUtils.isNotEmpty(outOrderLine.getNetValue()))
						{
							abstOrdEntMode.setTotalPrice(Double.valueOf(outOrderLine.getNetValue()));
						}
						
						if (StringUtils.isNotEmpty(outOrderLine.getDiscounts()))
						{
							//abstOrdEntMode.setDiscountsOnPrice(Double.valueOf(outOrderLine.getDiscounts()));//CP022 Changes
							abstOrdEntMode.setDiscountValues(createDiscountValues(Double.valueOf(outOrderLine.getDiscounts()),
									currencyModel));//CP022 Changes
							totalDiscounts = totalDiscounts + Double.valueOf(outOrderLine.getDiscounts()).doubleValue();//CP022 Changes
						}
						if (StringUtils.isNotEmpty(outOrderLine.getGrossPrice()))
						{
							abstOrdEntMode.setGrossPrice(Double.valueOf(outOrderLine.getGrossPrice()));
							//grossPrice = grossPrice + abstOrdEntMode.getGrossPrice().doubleValue();//CP022 Changes
						}
						if (StringUtils.isNotEmpty(outOrderLine.getInsurance()))
						{
							abstOrdEntMode.setInsurance(Double.valueOf(outOrderLine.getInsurance()));
							totalInsuarance = totalInsuarance + abstOrdEntMode.getInsurance().doubleValue();
						}
						/*if (StringUtils.isNotEmpty(outOrderLine.getHandlingFee()))
						{
							abstOrdEntMode.setHandlingFee(Double.valueOf(outOrderLine.getHandlingFee()));
							handlingFee = handlingFee + abstOrdEntMode.getHandlingFee().doubleValue();
						}*/
						/*if (StringUtils.isNotEmpty(outOrderLine.getExpeditedFees()))
						{
							abstOrdEntMode.setExpeditedFee(Double.valueOf(outOrderLine.getExpeditedFees()));
							totalExpeditedFee = totalExpeditedFee + abstOrdEntMode.getExpeditedFee().doubleValue();
						}*/
						if (StringUtils.isNotEmpty(outOrderLine.getDropshipFee()))
						{
							abstOrdEntMode.setDropshipFee(Double.valueOf(outOrderLine.getDropshipFee()));
							totalLineFee += abstOrdEntMode.getDropshipFee().doubleValue();
							dropShipFee = dropShipFee + abstOrdEntMode.getDropshipFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getMinimumOrderFee()))
						{
							abstOrdEntMode.setMinimumOrderFee(Double.valueOf(outOrderLine.getMinimumOrderFee()));
							totalLineFee += abstOrdEntMode.getMinimumOrderFee().doubleValue();
							minimumOrderFee = minimumOrderFee + abstOrdEntMode.getMinimumOrderFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getFreightFees()))
						{
							abstOrdEntMode.setFreightFees(Double.valueOf(outOrderLine.getFreightFees()));
							totalLineFee += abstOrdEntMode.getFreightFees().doubleValue();
							totalFreightFees = totalFreightFees + abstOrdEntMode.getFreightFees().doubleValue();
						}
						abstOrdEntMode.setTotalFees(totalLineFee);
						if (CollectionUtils.isNotEmpty(outOrderLine.getScheduledLines()))
						{
							// sorting the schedule line object on the basis of the line number in descending order.


							jnjObjectComparator = new JnjObjectComparator(ScheduledLines3.class, "getLineNumber", false, true);
							Collections.sort(outOrderLine.getScheduledLines(), jnjObjectComparator);

							scheduleLineDeliveryDate = new HashSet<String>();
							// Iterating the schedule line object and setting its value in the JnJ Delivery Schedule Model.

							for (final ScheduledLines3 scheduleLine : outOrderLine.getScheduledLines())
							{
								if (null != scheduleLine && StringUtils.isNotEmpty(scheduleLine.getConfirmedQuantity())
										&& Jnjb2bFacadesConstants.ZERO_IN_DOUBLE != Double.parseDouble(scheduleLine.getConfirmedQuantity())
										&& !scheduleLineDeliveryDate.contains(scheduleLine.getDeliveryDate())
										&& materialQuantity != scheduleLineConfirmedQuantity)
								{
									final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
									jnjDelSchModel.setLineNumber(scheduleLine.getLineNumber());
									jnjDelSchModel.setQty(Long.valueOf(Double.valueOf(scheduleLine.getConfirmedQuantity()).longValue()));
									// set the value in the double variable so that we can compare the requested quantity with schedule confirmed quantity
									scheduleLineConfirmedQuantity = scheduleLineConfirmedQuantity
											+ Double.parseDouble(scheduleLine.getConfirmedQuantity());
									if (StringUtils.isNotEmpty(scheduleLine.getDeliveryDate()))
									{
										try
										{
											final Date deliveryDate = new SimpleDateFormat(
													Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT)).parse(scheduleLine
													.getDeliveryDate());
											jnjDelSchModel.setDeliveryDate(deliveryDate);
											scheduleLineDeliveryDate.add(scheduleLine.getDeliveryDate());
										}
										catch (final ParseException exception)
										{
											LOGGER.error(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()"
													+ Logging.HYPHEN + "Parsing Exception Occured " + exception.getMessage());
											throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
													MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
										}
									} // if block loop


									jnjDelSchModelList.add(jnjDelSchModel);
								}

							} // for loop end
							abstOrdEntMode.setDeliverySchedules(jnjDelSchModelList);
						}
					}
				}
				sessionService.setAttribute("outOrderLinesList", outOrderLinesList);

				grossPrice = salesOrderTotalNetValue + taxValueForNetPrice;//CP022 Changes

				orderModel.setTotalNetValue(salesOrderTotalNetValue);
				orderModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee + totalFreightFees));//CP022 Changes
				orderModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
				orderModel.setTotalExpeditedFees(Double.valueOf(totalExpeditedFee));
				orderModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
				orderModel.setTotalHandlingFee(Double.valueOf(handlingFee));
				orderModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
				orderModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));//CP022 Changes
				orderModel.setTotalTax(Double.valueOf(totalTax));
				orderModel.setTotalInsurance(Double.valueOf(totalInsuarance));
				orderModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
				orderModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));//CP022 Changes
				orderModel.setTotalGrossPrice(Double.valueOf(grossPrice));
				orderModel.setTotalPrice(salesOrderTotalNetValue);
				orderModel.setSapValidated(Boolean.TRUE);
				orderModel.setSubtotal(salesOrderTotalNetValue);
				
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return orderModel;
	}

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
	 * This method is used to map Bonus Item line to OutOrder Line.
	 * @param jnjOutOrderLine
	 * @param outOrderLine
	 */
	protected void mapValuesToJnjOutOrderLine(JnjGTOutOrderLine jnjOutOrderLine, OutOrderLines3 outOrderLine) {

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapValuesToJnjOutOrderLine(JnjOutOrderLine, OutOrderLines3)"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		jnjOutOrderLine.setLineNumber(outOrderLine.getLineNumber());
		jnjOutOrderLine.setMaterialNumber(outOrderLine.getMaterialNumber());
		final String matQantity = outOrderLine.getMaterialQuantity();
		jnjOutOrderLine.setMaterialQuantity(matQantity.substring(0, matQantity.indexOf(".")));
		jnjOutOrderLine.setSalesUOM(outOrderLine.getSalesUOM());
		jnjOutOrderLine.setItemCategory(outOrderLine.getItemCategory());
		jnjOutOrderLine.setMaterialEntered(outOrderLine.getMaterialEntered());
		jnjOutOrderLine.setHigherLevelItemNumber(outOrderLine.getHigherLevelItemNumber());
		/*
		 * jnjOutOrderLine.setReasonForRejection(outOrderLine.getReasonForRejection());
		 * jnjOutOrderLine.setLineOverallStatus(outOrderLine.getLineOverallStatus());
		 */

		jnjOutOrderLine.setFreightFees(outOrderLine.getFreightFees());
		jnjOutOrderLine.setTaxes(outOrderLine.getTaxes());

		jnjOutOrderLine.setUnitValue(outOrderLine.getUnitValue());
		jnjOutOrderLine.setGrossPrice(outOrderLine.getGrossPrice());
		jnjOutOrderLine.setNetValue(outOrderLine.getNetValue());
		jnjOutOrderLine.setDiscounts(outOrderLine.getDiscounts());
		jnjOutOrderLine.setExpeditedFees(outOrderLine.getExpeditedFees());

		jnjOutOrderLine.setInsurance(outOrderLine.getInsurance());
		jnjOutOrderLine.setInternaitonalFreight(outOrderLine.getInternaitonalFreight());
		jnjOutOrderLine.setHandlingFee(outOrderLine.getHandlingFee());
		jnjOutOrderLine.setDropshipFee(outOrderLine.getDropshipFee());
		jnjOutOrderLine.setMinimumOrderFee(outOrderLine.getMinimumOrderFee());
		jnjOutOrderLine.setTaxesLocal(outOrderLine.getTaxes());
		jnjOutOrderLine.setQuantity(Long.valueOf(Double.valueOf(jnjOutOrderLine.getMaterialQuantity()).longValue()));		

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapValuesToJnjOutOrderLine(JnjOutOrderLine, OutOrderLines3)"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}
}
