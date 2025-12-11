/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import com.jnj.facades.data.JnjProductCarouselData;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Product;
import com.jnj.core.data.JnjGTGetContractPriceResponseData;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.event.JnjGTExportCatalogEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.outbound.mapper.JnjGTGetContractPriceMapper;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTCpsiaData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.product.JnjGTProductFacade;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.converters.populator.LanguagePopulator;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;


/**
 * The Facade class responsible to fetch all products from the active catalog version and convert them into
 * <code>JnjGTProductData</code> to be used for Full Catalog download.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTProductFacade extends DefaultJnjProductFacade implements JnjGTProductFacade
{


	/** The Constant RESOURCES_CPSIA. */
	protected static final String RESOURCES_CPSIA = "Resources - CPSIA";

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(DefaultJnjGTProductFacade.class);

	/** The jnj na product data converter. */
	protected Converter<JnJProductModel, JnjGTProductData> jnjGtProductDataConverter;

	/** The jnj na cpsia data converter. */
	protected Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> jnjGTCpsiaDataConverter;

	/** The jnj na cpsia certificate data converter. */
	protected Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> jnjGTCpsiaCertificateDataConverter;

	/** The jnj na product service. */
	@Resource(name = "productService")
	protected JnJGTProductService jnjGTProductService;

	@Autowired
	protected LanguagePopulator<LanguageModel, LanguageData> defaultLanguagePopulator;

	/** The jnj na get contract price mapper. */
	@Autowired
	JnjGTGetContractPriceMapper jnjGTGetContractPriceMapper;

	/** The jnj na cart service. */
	@Resource(name = "commerceCartService")
	JnjGTCartService jnjGTCartService;

	@Autowired
	protected JnjConfigService jnjConfigService;

	/** The event service required for the email flow **/
	@Autowired
	protected EventService eventService;

	/** Internationalization Service for Email Event Population **/
	@Autowired
	protected CommonI18NService commonI18NService;

	/** Base Store Service for Email Event Population **/
	@Autowired
	protected BaseStoreService baseStoreService;

	/** Base Site Service for Email Event Population **/
	@Autowired
	protected BaseSiteService baseSiteService;

	@Autowired
	protected UserService userService;
	@Autowired
	protected ModelService modelService;
	@Resource(name="GTCustomerFacade")
	protected JnjGTCustomerFacade jnjGTCustomerFacade;
	@Resource(name = "b2bProductFacade")
	protected ProductFacade productFacade;

	@Autowired
	B2BCartService b2bCartService;

	@Autowired
	protected SessionService sessionService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	protected Populator<ProductModel, ProductData> productPricePopulator;

	protected Converter<JnJProductModel, JnjProductCarouselData> jnjGTProductCarouselDataConverter;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.product.JnjGTProductFacade#getAllProductsForCatalog()
	 */
	@Override
	public boolean sendEmailForAllProductsOfCatalog()
	{
		final Map<String, String> exportDataMap = new HashMap<String, String>();
		boolean isSuccess = true;
		final JnJB2BUnitModel currentB2bUnit = jnjGTCustomerFacade.getCurrentB2bUnit();
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		try
		{
			eventService.publishEvent(initializeEmailEvent(new JnjGTExportCatalogEvent(exportDataMap, currentB2bUnit, currentSite),
					exportDataMap));
		}
		catch (final Exception exception)
		{
			LOG.error("An error occured while sending email of catalog export", exception);
			isSuccess = false;
		}
		return isSuccess;
	}

	/**
	 * this method is used to send the catalog download excel email to the user.
	 * 
	 * @param event
	 * @param exportDataMap
	 * @return
	 */
	protected AbstractEvent initializeEmailEvent(final JnjGTExportCatalogEvent event, final Map<String, String> exportDataMap)
	{
		/** populating the event with the basic required values **/
		exportDataMap.put("toEmail", jnjGTCustomerService.getCurrentUser().getEmail());
		exportDataMap.put("firstName", jnjGTCustomerService.getCurrentUser().getName());
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer(jnjGTCustomerService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setExportCatalogData(exportDataMap);

		return event;
	}

	/**
	 * Gets the jnj na product data converter.
	 * 
	 * @return the jnj na product data converter
	 */
	public Converter<JnJProductModel, JnjGTProductData> getJnjGTProductDataConverter()
	{
		return jnjGtProductDataConverter;
	}

	/**
	 * Sets the jnj na product data converter.
	 * 
	 * @param jnjGtProductDataConverter
	 *           the jnj na product data converter
	 */
	public void setJnjGtProductDataConverter(final Converter<JnJProductModel, JnjGTProductData> jnjGtProductDataConverter)
	{
		this.jnjGtProductDataConverter = jnjGtProductDataConverter;
	}

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 * 
	 * @param sortBy
	 *           the sort by
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	@Override
	public List<JnjGTCpsiaData> getConsumerProductsCpsia(final String sortBy)
	{
		final String METHOD_NAME = "getConsumerProductsCpsia";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOG);

		final List<JnjGTCpsiaData> cpsiaData = new ArrayList<>();

		/** Calling the service layer to fetch the CPSIA Details **/
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling service layer to fetch CPSIA data ...", LOG);
		final Collection<JnjGTProductCpscDetailModel> cpsiaDetailModels = jnjGTProductService.getConsumerProductsCpsia(sortBy);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA data retrieved", LOG);

		/** Iterating over the cpsiaDetailModels **/
		for (final JnjGTProductCpscDetailModel cpsiaDetail : cpsiaDetailModels)
		{
			/** Converting and populating the data **/
			cpsiaData.add(getJnjGTCpsiaDataConverter().convert(cpsiaDetail));
		}
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA data populated :: " + cpsiaData, LOG);

		/** Returning the data **/
		return cpsiaData;
	}

	/**
	 * This method fetches the CPSIA data for the supplied product.
	 * 
	 * @param productId
	 *           the product id
	 * @return cpsiaData
	 */
	@Override
	public JnjGTCpsiaData getCpsiaDataForProduct(final String productId)
	{
		final String METHOD_NAME = "getCpsiaDataForProduct";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOG);
		JnjGTCpsiaData cpsiaData = null;

		/** Calling the service layer to fetch the CPSIA Details **/
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling service layer to fetch CPSIA data ...", LOG);
		final JnjGTProductCpscDetailModel cpsiaDetailModel = jnjGTProductService.getCpsiaDataForProduct(productId);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA data retrieved", LOG);

		/** Converting and populating the data **/
		cpsiaData = getJnjGTCpsiaCertificateDataConverter().convert(cpsiaDetailModel);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "CPSIA data populated :: " + cpsiaData, LOG);

		/** Returning the data **/
		return cpsiaData;
	}

	/**
	 * Gets the jnj na cpsia data converter.
	 * 
	 * @return the jnj na cpsia data converter
	 */
	public Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> getJnjGTCpsiaDataConverter()
	{
		return jnjGTCpsiaDataConverter;
	}

	/**
	 * Sets the jnj na cpsia data converter.
	 * 
	 * @param jnjGTCpsiaDataConverter
	 *           the jnj na cpsia data converter
	 */
	public void setJnjGTCpsiaDataConverter(final Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> jnjGTCpsiaDataConverter)
	{
		this.jnjGTCpsiaDataConverter = jnjGTCpsiaDataConverter;
	}

	/**
	 * Gets the jnj na cpsia certificate data converter.
	 * 
	 * @return the jnjGTCpsiaCertificateDataConverter
	 */
	public Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> getJnjGTCpsiaCertificateDataConverter()
	{
		return jnjGTCpsiaCertificateDataConverter;
	}

	/**
	 * Sets the jnj na cpsia certificate data converter.
	 * 
	 * @param jnjGTCpsiaCertificateDataConverter
	 *           the jnjGTCpsiaCertificateDataConverter to set
	 */
	public void setJnjGTCpsiaCertificateDataConverter(
			final Converter<JnjGTProductCpscDetailModel, JnjGTCpsiaData> jnjGTCpsiaCertificateDataConverter)
	{
		this.jnjGTCpsiaCertificateDataConverter = jnjGTCpsiaCertificateDataConverter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.product.JnjGTProductFacade#getProductContractPrice(java.lang.String)
	 */
	@Override
	public String getProductContractPrice(final String productCode) throws SystemException, IntegrationException
	{
		JnjGTVariantProductModel deliveryGtinVariant = null;
		JnjGTGetContractPriceResponseData response = null;

		if (jnjGTProductService.getProductByCodeOrEAN(productCode) instanceof JnJProductModel)
		{
			final JnJProductModel productModel = (JnJProductModel) jnjGTProductService.getProductByCodeOrEAN(productCode);
			deliveryGtinVariant = jnjGTProductService.getDeliveryGTIN(productModel);
		}
		else if (jnjGTProductService.getProductByCodeOrEAN(productCode) instanceof JnjGTVariantProductModel)
		{
			deliveryGtinVariant = (JnjGTVariantProductModel) jnjGTProductService.getProductByCodeOrEAN(productCode);
		}
		else
		{
			LOG.error("No product/variant  found with product code: " + productCode);
		}

		JnjOrderTypesEnum orderType = null;
		if (b2bCartService.hasSessionCart() && null != b2bCartService.getSessionCart().getOrderType())
		{
			orderType = b2bCartService.getSessionCart().getOrderType();
		}
		else
		{
			orderType = jnjGTCartService.getDefaultOrderType();
		}


		Long qty = null;
		try
		{
			qty = Long.valueOf(Config.getParameter(Product.PRODUCT_GET_CONTRACT_PRICE_QUANTITY));
		}
		catch (final NumberFormatException exception)
		{
			LOG.error("Exception while parsing quantity: " + exception);
		}
		
		/** Added the or condition if the contract price is coming as null with success response **/
		if (null == response || (response.getErrorMessage() != null && !response.isSapResponseStatus())
				|| (0.0 == response.getContractPrice()))
		{
			final ProductData productData = new ProductData();
			productPricePopulator.populate(deliveryGtinVariant, productData);
			if (null != productData.getPrice() && null != productData.getPrice().getValue())
			{
				return productData.getPrice().getFormattedValue();
			}

		}
		/***
		 * Fetch currency based on the ISO code received from Get Contract Price response, if not found set site default
		 * currency.
		 ***/
		final CurrencyModel currency = response.getCurrency() != null ? commonI18NService.getCurrency(response.getCurrency())
				: commonI18NService.getCurrentCurrency();

		final StringBuilder formattedPrice = new StringBuilder();
		formattedPrice.append(currency.getSymbol()).append(Double.valueOf(response.getContractPrice()).toString());
		return formattedPrice.toString();
	}

	@Override
	public boolean sendProductDetailsEmail(final ProductData productData, final String contactDetails)
	{
		final String METHOD_NAME = "sendProductDetailsEmail()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		boolean sendStatus = false;
		logDebugMessage(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to publish event JnjPCMProductDetailsEmailEvent");
		try
		{
			final String[] emailAddresses = contactDetails.split(Jnjb2bCoreConstants.SYMBOl_COMMA);
			for (final String emailAddress : emailAddresses)
			{
				/** Publishing event responsible for product details email flow **/
				/*eventService.publishEvent(initializeEmailEvent(new JnjPCMProductDetailsEmailEvent(null, productData, emailAddress),
						jnjGTCustomerFacade.getJnjGTcustomerModel()));*/
				sendStatus = true;
				logDebugMessage(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL + Logging.HYPHEN, METHOD_NAME,
						"JnjPCMProductDetailsEmailEvent has been published, for email:" + emailAddress);
			}
		}
		catch (final Exception exception)
		{
			LOG.error(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Unable to publish JnjPCMProductDetailsEmailEvent : " + exception.getMessage());
		}

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return sendStatus;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}

	/**
	 * This method populates the JnjPCMProductDetailsEmailEvent object.
	 * 
	 * @return populated event object
	 */
	protected <T extends AbstractCommerceUserEvent> T initializeEmailEvent(final T event,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final String METHOD_NAME = "initializeEmailEvent()";
		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"Going to populate the JnjPCMProductDetailsEmailEvent object");

		/** populating the event with the basic required values **/
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer(JnJB2bCustomerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());

		logDebugMessage(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL + Logging.HYPHEN, METHOD_NAME,
				"event object is populated");

		logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.PRODUCT_DETAILS_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return event;
	}

	@Override
	public ProductData getProductAsAdmin(final String code, final Collection<ProductOption> options)

	{
		final ProductModel productModel = jnjGTProductService.getProductModelByCode(code, null);
		return productFacade.getProductForOptions(productModel, options);
	}

	@Override
	public LanguageData getLanguageData(final LanguageModel languageModel)
	{
		final LanguageData languageData = new LanguageData();
		defaultLanguagePopulator.populate(languageModel, languageData);
		return languageData;
	}

	public StringBuilder getObsoleteProductList(String[] selectedProductIds) {
		return jnjGTProductService.getObsoleteProductList(selectedProductIds);
	}

	@Override
	public List<JnjProductCarouselData> getProductsBoughtTogether(final String productCode, CatalogVersionModel catalogVersion) {
		final List<JnJProductModel> productList = jnjGTProductService.getProductsBoughtTogether(productCode, catalogVersion);

		final List<JnjProductCarouselData> carouselProductList = new ArrayList<>();
		for (final JnJProductModel carouselProduct : productList) {
			carouselProductList.add(jnjGTProductCarouselDataConverter.convert(carouselProduct));
		}

		return carouselProductList;
	}

	public Converter<JnJProductModel, JnjProductCarouselData> getJnjGTProductCarouselDataConverter() {
		return jnjGTProductCarouselDataConverter;
	}
	public void setJnjGTProductCarouselDataConverter(
			Converter<JnJProductModel, JnjProductCarouselData> jnjGTProductCarouselDataConverter) {
		this.jnjGTProductCarouselDataConverter = jnjGTProductCarouselDataConverter;
	}
}
