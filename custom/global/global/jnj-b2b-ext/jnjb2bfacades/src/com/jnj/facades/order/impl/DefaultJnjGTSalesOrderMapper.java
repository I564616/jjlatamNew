package com.jnj.facades.order.impl;

/**
 * 
 */

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeoutException;

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
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.JnjGTSimulateOrderResponseData;
import com.jnj.core.dto.JnjGTSplitOrderInfo;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjGTDropshipmentService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.cart.JnjGTCartFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnjCartData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTOutOrderLine;
import com.jnj.facades.data.JnjOrderEntryData;
import com.jnj.facades.impl.DefaultMessageFacade;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.facades.orderSplit.JnjGTOrderSplitFacade;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.OrderLinesOutput;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.ScheduledLines;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_ordersimulate_v1.ordersimulatewebservice.TestPricingFromGatewayOutput;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ScheduledLines3;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.variants.model.VariantProductModel;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTSalesOrderMapper implements com.jnj.facades.order.JnjGTSalesOrderMapper{
	
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSalesOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();
	
	@Autowired
	protected JnjConfigService jnjConfigService;
	
	@Autowired
	protected JnjCartService jnjCartService;
	
	@Autowired
	protected ModelService modelService;
	
	@Autowired
	SessionService sessionService;
	
	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;
	
	@Autowired
	protected JnjGTOrderService jnjGTOrderService;
	
	@Autowired
	JnjOrderUtil orderUtil;
	
	@Autowired
	protected JnjGTDropshipmentService jnjGTDropShipmentService;
	
	@Autowired
	protected JnjGTProductFacade jnjGTProductFacade;
	
	@Autowired
	protected JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper;
	
	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;
	
	@Autowired
	protected DefaultMessageFacade messageFacade;
	
	@Resource(name = "cartFacade")
	protected CartFacade cartFacade;
	
	@Resource(name="GTCartFacade")
	JnjGTCartFacade jnjGTCartFacade;
	
	@Autowired
	protected JnjGTOrderSplitFacade<JnjGTSplitOrderInfo, AbstractOrderEntryModel> jnjGTOrderSplitFacade;
	
	public JnjGTCartFacade getJnjGTCartFacade() {
		return jnjGTCartFacade;
	}
	
	@Override
	public JnjGTOutboundStatusData mapSalesOrderSimulationWrapper(CartModel cartModel,boolean isCallFromGetPrice)
			throws IntegrationException, SystemException, TimeoutException, BusinessException 
	{
		boolean isRegional = Boolean.valueOf(Config.getParameter(Jnjb2bCoreConstants.Dropshipment.IS_NON_GLOBAL));
	  
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationWrapper()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		
		final JnjGTSimulateOrderResponseData jnjGTSimulateOrderResponseData = new JnjGTSimulateOrderResponseData();
		final TestPricingFromGatewayInput testPricingFromGatewayInput = new TestPricingFromGatewayInput();
		final CartData cartData = cartFacade.getSessionCart();
		if (null != cartModel.getOrderType())
		{
			testPricingFromGatewayInput.setInOrderType(cartModel.getOrderType().getCode());
		}
		else
		{
			testPricingFromGatewayInput.setInOrderType(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		if (null != cartModel.getUnit())
		{
			testPricingFromGatewayInput.setInSoldToCustomerAddress(cartModel.getUnit().getUid());
		}
		if (null != cartModel.getDeliveryAddress())
		{
			testPricingFromGatewayInput.setInShipToCustomerAddress(cartModel.getDeliveryAddress().getJnJAddressId());
		}
		else
		{
			testPricingFromGatewayInput.setInShipToCustomerAddress(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		// check for the not empty or not null
		if (BooleanUtils.isTrue(cartModel.getThirdpartyBilling()))
		{
			final JAXBElement<String> dropShipIndicator = objectFactory
					.createTestPricingFromGatewayInputInDropShipIndicator(Jnjgtb2boutboundserviceConstants.Y_STRING);
			testPricingFromGatewayInput.setInDropShipIndicator(dropShipIndicator);
		}//else set empty in it.
		else
		{
			final JAXBElement<String> dropShipIndicator = objectFactory
					.createTestPricingFromGatewayInputInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			testPricingFromGatewayInput.setInDropShipIndicator(dropShipIndicator);
		}

		// check for the empty value or null.
		if (StringUtils.isNotEmpty(cartModel.getDealerPONum()))
		{
			final JAXBElement<String> dealerPO = objectFactory.createTestPricingFromGatewayInputInDealerPONumber(cartModel
					.getDealerPONum());
			testPricingFromGatewayInput.setInDealerPONumber(dealerPO);
		}
		else
		{
			final JAXBElement<String> dealerPO = objectFactory
					.createTestPricingFromGatewayInputInDealerPONumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			testPricingFromGatewayInput.setInDealerPONumber(dealerPO);
		}
		// changes as per the CR63 in which we are supposed to pass purchase order number in dealer po number.
		if (StringUtils.equals(JnjOrderTypesEnum.ZNC.getCode(), cartModel.getOrderType().getCode()))
		{
			testPricingFromGatewayInput.setInDealerPONumber(objectFactory
					.createTestPricingFromGatewayInputInDealerPONumber(cartModel.getPurchaseOrderNumber()));
		}
		// Fetch value from the config table.
		testPricingFromGatewayInput.setInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));
		// check for the entries are not empty or are not null.
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			testPricingFromGatewayInput.getInOrderLines().addAll(mapOrderLinesFromAbstOrderEntries(cartModel.getEntries()));
		}
		
		final TestPricingFromGatewayOutput testPricingFromGatewayOutput = null;
		
		/*Code changes for Order Split Start*/
		  
		final List<JnjGTCartData> jnjCartDataList = new ArrayList<JnjGTCartData>();
		Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = null;
		final List<String> productCodesList = new ArrayList<String>();
		final Map<String, AbstractOrderEntryModel> cartEntriesMap = new HashMap<String, AbstractOrderEntryModel>();
//		final String destinationCountry = cartModel.getDeliveryAddress().getCountry().getIsocode();
		
		for(AbstractOrderEntryModel abstOrderEntModel : cartModel.getEntries())
		{
			if(abstOrderEntModel!=null){
				
				String materialId   = abstOrderEntModel.getProduct().getCode();
				
				final String productCode = new String(materialId);
				productCodesList.add(productCode);
				cartEntriesMap.put(productCode, abstOrderEntModel);
			}			
			
		}
		final CountryModel currentBaseStorycountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
		final String baseStoreCountryIsoCode = currentBaseStorycountry.getIsocode();
		// Getting DropshipmentDetails 
//		final List<JnjDropShipmentDetailsModel> dropShipmentDetails = jnjGTDropShipmentService
//				.getDropShipmentDetails(productCodesList);
		
//		if(dropShipmentDetails!=null && !dropShipmentDetails.isEmpty()){
			
//			splitOrderMap = orderUtil.getSplitOrderMap(cartEntriesMap,dropShipmentDetails,destinationCountry);
			splitOrderMap = jnjGTOrderSplitFacade.splitOrder(cartModel,baseStoreCountryIsoCode);
//		}
		
		Map<String, String> codesNotFound = new HashMap<String, String>();
		
		if(splitOrderMap!=null && !(splitOrderMap.isEmpty())){
			codesNotFound = orderUtil.validateSplitOrderMap(splitOrderMap, cartModel);
		}
		else{
			for(String productId :productCodesList){
				codesNotFound.put(productId, messageFacade.getMessageTextForCode(Jnjb2bCoreConstants.Dropshipment.NOT_FOUND_ERROR));
			}
			
		}
		sessionService.setAttribute("codesNotFound", codesNotFound);
		
		
		if(codesNotFound!=null && !codesNotFound.isEmpty()){
			jnjGTSimulateOrderResponseData.setErrorMessage("Validation error Occurred. Please contact administrator.");
			return jnjGTSimulateOrderResponseData;
		}
		
		for (final Entry<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry : splitOrderMap.entrySet())
		{
			addEntriesToCartSplit(mapEntry,jnjCartDataList,cartModel.getCode());
		}
		
		if (!jnjCartDataList.isEmpty())
		{
			sessionService.setAttribute("jnjCartDataList", jnjCartDataList);
			sessionService.setAttribute("populateSpliCart", true);
		}
		
		//Create Outorder Line List
		List<OutOrderLines3> outOrderLineList = new ArrayList<OutOrderLines3>();
		
		if(!isRegional) {
			outOrderLineList = jnjGTOutOrderLineMapper.createValidationOutOrderList(cartModel);
		} else {
			//outOrderLineList =  Regional SAP call
		}
		
		//Changes for Bonus Item starts
		final Map<String, String> quantiyMap = new HashMap<String, String>();
		
		for(AbstractOrderEntryModel orderEntry : cartModel.getEntries()){
			quantiyMap.put(orderEntry.getProduct().getCode(), orderEntry.getQuantity().toString());
		}
		
		final Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine = new HashMap<String, OutOrderLines3>();
		final Map<String, JnjGTOutOrderLine> freeGoodsMap = new HashMap<String, JnjGTOutOrderLine>();
				
		if(outOrderLineList!=null){
			
			String orderedQuantity = null;
			String materialEnterd = null;
			
			for (final OutOrderLines3 outOrderLine : outOrderLineList)
			{
				if (null != outOrderLine)
				{
					if (quantiyMap.containsKey(outOrderLine.getMaterialEntered())
							&& (outOrderLine.getItemCategory().equals("ZTAN") || outOrderLine.getItemCategory().equals("ZTAS")))
					{
						orderedQuantity = quantiyMap.get(outOrderLine.getMaterialEntered());
						materialEnterd = outOrderLine.getMaterialEntered();
					}
					
					if (mapMaterialNumberWithOutOrderLine.containsKey(outOrderLine.getMaterialEntered())
							&& (outOrderLine.getItemCategory().equals("ZFNT")))
					{
						final JnjGTOutOrderLine jnjOutOrderLine = new JnjGTOutOrderLine();

						if (outOrderLine.getMaterialEntered().equalsIgnoreCase(materialEnterd))
						{
							jnjOutOrderLine.setOrderedQuantity(orderedQuantity);
						}

						mapValuesToJnjOutOrderLine(jnjOutOrderLine, outOrderLine);

						freeGoodsMap.put(outOrderLine.getMaterialEntered(), jnjOutOrderLine);
					}
					else
					{
						mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
					}
				}
			}
		}
		
		sessionService.setAttribute("freeGoodsMap", freeGoodsMap);

		jnjCartService.calculateValidateEntries(cartModel); // Update the basePrice ,total price and Net Price according to the contract Price 
		double salesOrderTotalNetValue = 0;
				
		for (AbstractOrderEntryModel entry : cartModel.getEntries()) {
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


		if(!mapMaterialNumberWithOutOrderLine.isEmpty()){
			final CartModel cartModelResponse = mapSalesOrderSimulationResponse(mapMaterialNumberWithOutOrderLine, cartModel,salesOrderTotalNetValue);
			// To invoke the JnjOrderServiceImpl class to persist the simulate order data in hybris database.
			jnjCartService.calculateValidatedCart(cartModelResponse);
		}
		 /*Code changes for Order Split End*/
				

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationWrapper()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return jnjGTSimulateOrderResponseData;
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

	/**
	 * 
	 * @param mapMaterialNumberWithOutOrderLine
	 * @param cartModel
	 * @param salesOrderTotalNetValue 
	 * @return
	 * @throws SystemException 
	 */
	protected CartModel mapSalesOrderSimulationResponse(Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine,
				CartModel cartModel, double salesOrderTotalNetValue) throws SystemException {
		
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
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Check the mapMaterialNumberWithOutOrderLine map as null and not empty.
		if (null != mapMaterialNumberWithOutOrderLine && !mapMaterialNumberWithOutOrderLine.isEmpty())
		{
			// Check the cartModel & cartModel Entries as null and not empty.
			if (null != cartModel && null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				//Get the Currency Associated with Cart : CP022 Changes
				final CurrencyModel currencyModel = cartModel.getCurrency();

				//Getting the B2bUnit with Cart: CP022 Changes
				final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) cartModel.getUnit();
				if (null == jnJB2bUnitModel)
				{
					throw new SystemException("No B2bUnit associated with Order having order No [" + cartModel.getCode() + "]");
				}

				//Fetch the Country Associated with Cart in Order to get Individual Taxes.: CP022 Changes
				final CountryModel countryModel = jnJB2bUnitModel.getCountry();

				if (null == countryModel)
				{
					throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
							+ "] for Order having order No [" + cartModel.getCode() + "]");
				}

				// List to store values for total calculation on cart split
				final List<OutOrderLines3> outOrderLinesList = new ArrayList<OutOrderLines3>();

				// Iterate the cartModel entries one by one and populates those value in cartModel.
				for (final AbstractOrderEntryModel abstOrdEntMode : cartModel.getEntries())
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
								LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
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
							LOGGER.info("outOrderLine.getNetValue() : "+Double.valueOf(outOrderLine.getNetValue()));
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
							LOGGER.info("outOrderLine.getGrossPrice() : "+Double.valueOf(outOrderLine.getGrossPrice()));
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
											LOGGER.error(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()"
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

				cartModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee + totalFreightFees));//CP022 Changes
				cartModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
				cartModel.setTotalExpeditedFees(Double.valueOf(totalExpeditedFee));
				cartModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
				cartModel.setTotalHandlingFee(Double.valueOf(handlingFee));
				cartModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
				cartModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));//CP022 Changes
				cartModel.setTotalTax(Double.valueOf(totalTax));
				cartModel.setTotalInsurance(Double.valueOf(totalInsuarance));
				cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
				cartModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));//CP022 Changes
				cartModel.setTotalGrossPrice(Double.valueOf(grossPrice));
				cartModel.setSubtotal(Double.valueOf(salesOrderTotalNetValue));
				cartModel.setSapValidated(Boolean.TRUE);
				
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return cartModel;
	}

	/**
	 * 
	 * @param mapEntry
	 * @param jnjCartDataList
	 * @param cartCode
	 */
	protected void addEntriesToCartSplit(Entry<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry,
			List<JnjGTCartData> jnjCartDataList, String cartCode) {

		//Entries for order
		final List<OrderEntryData> orderEntryDataList = new ArrayList<OrderEntryData>();

		// Mapping of the Optional Fields End
		// Get the value from the map which contains the List of AbstractOrderEntryModel Object and iterate them one by one to populate the orde entries in the request object.
		for (final AbstractOrderEntryModel abstOrderEntryModel : mapEntry.getValue())
		{

			final JnjGTOrderEntryData jnjOrderEntryData = new JnjGTOrderEntryData();
			final ProductData productData = jnjGTProductFacade.getProductForCodeAndOptions(abstOrderEntryModel.getProduct().getCode(),
					Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
			jnjOrderEntryData.setProduct(productData);
			orderEntryDataList.add(jnjOrderEntryData);
		}
		//Initialize cart data with product entry for order split display
		final JnjGTCartData jnjCartData = new JnjGTCartData();
		jnjCartData.setCode(cartCode);
		jnjCartData.setEntries(orderEntryDataList);
		jnjCartData.setOrderType(mapEntry.getKey().getDocorderType());
		jnjCartDataList.add(jnjCartData);	
		
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
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<InOrderLines> inOrderLineList = new ArrayList<InOrderLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InOrderLines inOrderLines = new InOrderLines();
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getProduct().getCode());
			inOrderLines.setQuantity(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inOrderLines.setSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			final JAXBElement<String> modCode = objectFactory
					.createInOrderLinesModCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inOrderLines.setModCode(modCode);
			// add the object in the list
			inOrderLineList.add(inOrderLines);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return inOrderLineList;
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
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return discountValues;
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
		final TaxValue tax = new TaxValue(Order.TAX_VALUE, 0.0D, false, taxValue.doubleValue(), (currencyModel == null) ? null
				: currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<TaxValue>();
		taxValues.add(tax);
		return taxValues;
	}
}

