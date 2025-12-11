/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.InQuotationLines;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.OutQuotationLines;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.ScheduledLines;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayInput;
import com.jnj.hcswmd01.mu007_epic_pricequote_v1.getpricequotewebservice.TestPricingFromGatewayOutput;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTGetPriceQuoteResponseData;
import com.jnj.core.data.JnjGTSimulateOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTGetPriceQuoteMapper;
import com.jnj.gt.outbound.mapper.JnjGTSimulateOrderMapper;
import com.jnj.gt.outbound.services.JnjGTGetPriceQuoteService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * The JnjGTGetPriceQuoteMapperImpl class contains the definition of all the method of the JnjGTGetPriceQuoteMapper
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTGetPriceQuoteMapper implements JnjGTGetPriceQuoteMapper
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTGetPriceQuoteMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected JnjCartService jnjCartService;

	@Autowired
	protected JnjGTGetPriceQuoteService jnjGTGetPriceQuoteService;

	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	@Autowired
	private JnjGTSimulateOrderMapper jnjGTSimulateOrderMapper;

	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	@Autowired
	private ModelService modelService;

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnjGTGetPriceQuoteService getJnjGTGetPriceQuoteService() {
		return jnjGTGetPriceQuoteService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public JnjGTSimulateOrderMapper getjnjGTSimulateOrderMapper() {
		return jnjGTSimulateOrderMapper;
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
	 * @throws SystemException
	 * @throws ParseException
	 * @throws BusinessException
	 */
	@Override
	public JnjGTGetPriceQuoteResponseData mapGetPriceQuoteRequestResponse(final CartModel cartModel) throws IntegrationException,
			SystemException, ParseException, BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapGetPriceQuoteRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String excludedProductCode = null;
		JnjGTSimulateOrderResponseData jnjGTSimulateOrderResponseData = null;
		final JnjGTGetPriceQuoteResponseData jnjGTGetPriceQuoteResponseData = new JnjGTGetPriceQuoteResponseData();
		try
		{
			final JnjGTSapWsData wsData = new JnjGTSapWsData();
			wsData.setConnectionTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.SIMULATE_WS_EXTENDED_CONNECTION_TIME_OUT);
			wsData.setReadTimeOutKey(Jnjgtb2boutboundserviceConstants.WebServiceConnection.ORDER_WS_EXTENDED_READ_TIME_OUT);
			jnjGTSimulateOrderResponseData = jnjGTSimulateOrderMapper.mapSimulateOrderRequestResponse(cartModel, true, wsData);
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapGetPriceQuoteRequestResponse()" + Logging.HYPHEN
					+ "Business Exception occured " + businessException.getMessage(), businessException);
			if (StringUtils.isNotEmpty(businessException.getMessage()))
			{
				excludedProductCode = businessException.getMessage();
			}
			// if there is no entry in the cart then through business exception and show the error message to user.
			if (cartModel.getEntries().size() < 1)
			{
				throw new BusinessException(excludedProductCode, MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
			}
		}
		/* Code Fixes For JJEPIC-811 Start * */
		if (null != jnjGTSimulateOrderResponseData && jnjGTSimulateOrderResponseData.isCartEmpty())
		{
			jnjGTGetPriceQuoteResponseData.setCartEmpty(jnjGTSimulateOrderResponseData.isCartEmpty());
		}/* Code Fixes For JJEPIC-811 End * */
		else
		{
			final TestPricingFromGatewayInput testPricingFromGatewayInput = new TestPricingFromGatewayInput();
			if (null != cartModel.getOrderType())
			{
				testPricingFromGatewayInput.setInQuoteType(cartModel.getOrderType().getCode());
			}
			if (null != cartModel.getUnit())
			{
				testPricingFromGatewayInput.setInSoldToCustomerAddress(cartModel.getUnit().getUid());
			}
			if (null != cartModel.getDeliveryAddress())
			{
				testPricingFromGatewayInput.setInShipToCustomerAddress(cartModel.getDeliveryAddress().getJnJAddressId());
			}
			// check for the not empty or not null
			if (null != cartModel.getCode())
			{
				testPricingFromGatewayInput.setInPortalOrderNumber(cartModel.getCode());
			}
			if (StringUtils.isNotEmpty(cartModel.getDealerPONum()))
			{
				final JAXBElement<String> poNumber = objectFactory.createTestPricingFromGatewayInputInPONumber(cartModel
						.getDealerPONum());
				testPricingFromGatewayInput.setInPONumber(poNumber);
			}
			testPricingFromGatewayInput.setInOrderReason(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			testPricingFromGatewayInput.setInOrderSource(jnjConfigService
					.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_SOURCE));
	
			testPricingFromGatewayInput.setInOrderChannel(jnjConfigService
					.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));
			// check for the entries are not empty or are not null.
			if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
			{
				testPricingFromGatewayInput.getInQuotationLines().addAll(mapOrderLinesFromAbstOrderEntries(cartModel.getEntries()));
			}
	
			final TestPricingFromGatewayOutput testPricingFromGatewayOutput = jnjGTGetPriceQuoteService
					.getPriceQuoteInSAP(testPricingFromGatewayInput);
			if (null != testPricingFromGatewayOutput
					&& CollectionUtils.isNotEmpty(testPricingFromGatewayOutput.getOutQuotationLines())
					&& CollectionUtils.isNotEmpty(cartModel.getEntries()))
				//Temporary Changes to enable Get Price Quote
				//Comment Out if working on other regions
//					&& (null == testPricingFromGatewayOutput.getErrorMessage() || Jnjgtb2boutboundserviceConstants.EMPTY_STRING
//							.equals(testPricingFromGatewayOutput.getErrorMessage().getValue())))
			{
	
				cartModel.setSapOrderNumber(testPricingFromGatewayOutput.getOutSalesOrderNumber());
				if (null != testPricingFromGatewayOutput.getValidFrom())
				{
					cartModel.setDate(new SimpleDateFormat(Config.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT))
							.parse(testPricingFromGatewayOutput.getValidFrom()));
				}
				if (null != testPricingFromGatewayOutput.getValidTo())
				{
					cartModel.setQuoteExpirationDate(new SimpleDateFormat(Config
							.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT)).parse(testPricingFromGatewayOutput
							.getValidTo()));
				}
				mapCartModelFromOutOrderLine(cartModel, testPricingFromGatewayOutput);
				//Calculate cart for total values and save in data base
				jnjGTGetPriceQuoteResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(cartModel, true));
				jnjGTGetPriceQuoteResponseData.setSapResponseStatus(true);
				// if some of the products got excluded but get price quote create get successful then show the error message with excluded products.
				if (StringUtils.isNotEmpty(excludedProductCode))
				{
					jnjGTGetPriceQuoteResponseData.setExcludedProductCodes(excludedProductCode);
				}
			}
			else if (null != testPricingFromGatewayOutput && null != testPricingFromGatewayOutput.getErrorMessage())
			{
				// To handle the excluded customer scenario.
				mapCartModelFromOutOrderLine(cartModel, testPricingFromGatewayOutput);
				jnjGTGetPriceQuoteResponseData.setSapResponseStatus(false);
				jnjGTGetPriceQuoteResponseData.setErrorMessage(testPricingFromGatewayOutput.getErrorMessage().getValue());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapGetPriceQuoteRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTGetPriceQuoteResponseData;
	}

	/**
	 * Map in order lines fields from abstract order entries model.
	 * 
	 * @param abstOrdEntModelList
	 *           the abstract order entry model list
	 * @return the array of in order lines
	 */
	protected List<InQuotationLines> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<InQuotationLines> inQuotationLines = new ArrayList<InQuotationLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InQuotationLines inQuotationLine = new InQuotationLines();
			inQuotationLine.setMaterialNumber(abstOrderEntryModel.getProduct().getCode());
			inQuotationLine.setQuantity(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inQuotationLine.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inQuotationLine.setSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			final JAXBElement<String> itemNumber = objectFactory.createInQuotationLinesItemNumber(String.valueOf(abstOrderEntryModel
					.getEntryNumber()));
			inQuotationLine.setItemNumber(itemNumber);

			// add the object in the list
			inQuotationLines.add(inQuotationLine);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return inQuotationLines;
	}

	/**
	 * Map cart model from out order line.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param testPricingFromGatewayOutput
	 *           the test pricing from gateway output
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */
	protected void mapCartModelFromOutOrderLine(final CartModel cartModel,
			final TestPricingFromGatewayOutput testPricingFromGatewayOutput) throws SystemException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalNetValue = 0.0;
		double totalTax = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		final CurrencyModel currencyModel = cartModel.getCurrency();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		CatalogVersionModel catalogVersionModel = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : cartModel.getEntries())
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
		if (null != cartModel.getSite() && CollectionUtils.isNotEmpty(cartModel.getSite().getStores()))
		{
			if (CollectionUtils.isNotEmpty(cartModel.getSite().getStores().get(0).getCatalogs()))
			{
				catalogVersionModel = cartModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}
		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (final OutQuotationLines outQuotationLine : testPricingFromGatewayOutput.getOutQuotationLines())
		{
			// In case of excluded customer, enter inside if block.
			if (null != outQuotationLine && null != outQuotationLine.getLineNumber()
					&& !lineNumberExcluded.contains(outQuotationLine.getLineNumber().getValue()))
			{
				try
				{
					if (null != outQuotationLine.getItemCategory()
							&& !(itemCategories.contains(outQuotationLine.getItemCategory().getValue()))
							&& null != outQuotationLine.getMaterialNumber() && null != outQuotationLine.getMaterialEntered())
					{
						AbstractOrderEntryModel abstOrdEntryModel = null;
						JnJProductModel product = null;
						JnJProductModel productEntered = null;
						ProductModel baseProduct = null;
						ProductModel baseProductEntered = null;
						if (StringUtils.equals(outQuotationLine.getMaterialNumber().getValue(), outQuotationLine.getMaterialEntered()
								.getValue()))
						{
							product = jnJGTProductService.getProductModelByCode(outQuotationLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							if (null != product)
							{
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
								baseProductEntered = baseProduct;
							}
						}
						else
						{
							product = jnJGTProductService.getProductModelByCode(outQuotationLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							productEntered = jnJGTProductService.getProductModelByCode(outQuotationLine.getMaterialEntered().getValue(),
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
							if (null != outQuotationLine.getLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(outQuotationLine.getLineNumber().getValue());
							}
							if (null != outQuotationLine.getBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outQuotationLine.getBaseUOM().getValue());
								abstOrdEntryModel.setBaseUOM(unitModel);
							}
							if (null != outQuotationLine.getMessage())
							{
								abstOrdEntryModel.setMessage(outQuotationLine.getMessage().getValue());
							}
							if (null != outQuotationLine.getSubstitutionReason())
							{
								abstOrdEntryModel.setSubstitutionReason(outQuotationLine.getSubstitutionReason().getValue());
							}
							if (null != outQuotationLine.getSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outQuotationLine.getSalesUOM().getValue());
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
														.equalsIgnoreCase(outQuotationLine.getSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}
							if (null != outQuotationLine.getDiscounts()
									&& StringUtils.isNotEmpty(outQuotationLine.getDiscounts().getValue()))
							{
								abstOrdEntryModel.setDiscountValues(createDiscountValues(
										(Double.valueOf(outQuotationLine.getDiscounts().getValue())), currencyModel));
								totalDiscounts = totalDiscounts
										+ Double.valueOf(outQuotationLine.getDiscounts().getValue()).doubleValue();
							}
							if (null != outQuotationLine.getHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(outQuotationLine.getHigherLevelItemNumber().getValue());
							}

							abstOrdEntryModel.setItemCategory(outQuotationLine.getItemCategory().getValue());
							if (null != outQuotationLine.getRoute())
							{
								abstOrdEntryModel.setRoute(outQuotationLine.getRoute().getValue());
							}
							if (null != outQuotationLine.getShippingPoint())
							{
								abstOrdEntryModel.setShippingPoint(outQuotationLine.getShippingPoint().getValue());
							}
							if (null != outQuotationLine.getPlant())
							{
								abstOrdEntryModel.setPlant(outQuotationLine.getPlant().getValue());
							}
							if (null != outQuotationLine.getHSAPromotion()
									&& StringUtils.isNotEmpty(outQuotationLine.getHSAPromotion().getValue()))
							{
								abstOrdEntryModel.setHsaPromotion(Double.valueOf(outQuotationLine.getHSAPromotion().getValue()));
								hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
							}
							if (null != outQuotationLine.getNetValue()
									&& StringUtils.isNotEmpty(outQuotationLine.getNetValue().getValue()))
							{
								abstOrdEntryModel.setNetPrice(Double.valueOf(outQuotationLine.getNetValue().getValue()));
								totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
							}
							if (null != outQuotationLine.getTax() && StringUtils.isNotEmpty(outQuotationLine.getTax().getValue()))
							{
								abstOrdEntryModel.setTaxValues(createTaxValues((Double.valueOf(outQuotationLine.getTax().getValue())),
										currencyModel));

								totalTax = totalTax + Double.valueOf(outQuotationLine.getTax().getValue()).doubleValue();
							}
							// To Set the Schedule Lines information in CartModel
							if (null != outQuotationLine.getScheduledLines())
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
								for (final ScheduledLines scheduledLines : outQuotationLine.getScheduledLines())
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
												&& !Jnjgtb2boutboundserviceConstants.EMPTY_STRING.equals(scheduledLines.getQuantity()
														.getValue()))
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
							if (null != outQuotationLine.getGrossPrice()
									&& StringUtils.isNotEmpty(outQuotationLine.getGrossPrice().getValue())
									&& null != outQuotationLine.getAvailableQuantity()
									&& StringUtils.isNotEmpty(outQuotationLine.getAvailableQuantity().getValue()))
							{
								abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double.parseDouble(outQuotationLine
										.getGrossPrice().getValue()) + Double.parseDouble(outQuotationLine.getDiscounts().getValue()))
										/ (abstOrdEntryModel.getQuantity().doubleValue()))));
								abstOrdEntryModel.setTotalPrice(Double.valueOf(Double.parseDouble(outQuotationLine.getGrossPrice()
										.getValue()) + Double.parseDouble(outQuotationLine.getDiscounts().getValue())));
								grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
							}
							jnjGTOrderService.populateMddOrderEntryStatus(abstOrdEntryModel);
						}
					}
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
							+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
					throw new SystemException("System Exception throw from the JnjGTGetPriceQuoteMapperImpl class",
							MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
				}
			}
			else
			{
				throw new BusinessException();
			}
		}

		cartModel.setTotalPrice(Double.valueOf(totalNetValue + totalTax));
		cartModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
		cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		cartModel.setSubtotal(Double.valueOf(grossPrice));
		cartModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		cartModel.setTotalOtherCharge(Double.valueOf(totalNetValue - grossPrice + hsaPromotion));
		cartModel.setTotalTax(Double.valueOf(totalTax));
		cartModel.setSapValidated(Boolean.TRUE);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
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
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
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
	protected Date formatResponseDate(final String date) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
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
			LOGGER.error(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ "Parsing Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the jnjGTSimulateOrderMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN + Logging.END_OF_METHOD
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
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final TaxValue tax = new TaxValue(Jnjgtb2boutboundserviceConstants.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(currencyModel == null) ? null

				: currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<TaxValue>();
		taxValues.add(tax);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_PRICE_QUOTE + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return taxValues;
	}

}
