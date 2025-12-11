/*
] * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.services.impl;

import com.jnj.core.util.CalendarBlockUtils;
import com.jnj.core.util.Interval;
import com.jnj.core.util.JnJCsvUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;

import jakarta.annotation.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.impl.DefaultJnJProductService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.core.constants.GeneratedJnjb2bCoreConstants.Enumerations.JnjOrderTypesEnum;
import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjGTProductDao;
//import com.jnj.core.services.CompletenessStatusData;
import com.jnj.core.services.JnJGTMDDProductExportService;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.services.customer.JnjGTCustomerService;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;
//import com.jnj.pcm.core.dto.CompletenessStatusData;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import com.jnj.core.enums.AccessBy;

/**
 * This is implementation class of JnJGTProductService , is used to get information related to JnjGTProduct specific to
 * JNJ NA requirement.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJGTProductService extends DefaultJnJProductService implements JnJGTProductService
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnJGTProductService.class);
	/** The Constant ROOT_CATEGORY. */
	protected static final String ROOT_CATEGORY = "Categories";
	protected static final String JNJ_GT_PRODUCT_FACADE = "JnjGTProductFacade";
	protected static final String RESOURCES_CPSIA = "Resources - CPSIA";
	protected static final String COMPLETENESS_IND = "Completeness Indicator";
	protected JnJCommonUtil jnjCommonUtil;
	protected static final int PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY_DEFAULT = 16;
	protected static final int PRODUCTS_BOUGHT_TOGETHER_CALENDAR_PERIOD_DEFAULT = 4;
	protected static final String ALLOWED_FRANCHISE = "allowedFranchise";
	public static final String PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY = "products.bought.together.carousel.quantity";
	public static final String PRODUCTS_BOUGHT_TOGETHER_CALENDAR_PERIOD = "products.bought.together.calendar.period";
	/**
	 * Constant collection to store active status codes for CONSUMER products.
	 */
	protected static final String CONSUMER_ACTIVE_PRODUCT_STATUS = Config
			.getParameter(Jnjb2bCoreConstants.Product.CONSUMER_ACTIVE_PRODUCT_STATUS_KEY);

	/**
	 * Constant collection to store Obsolete status codes for MDD products.
	 */
	protected static final String CONSUMER_INACTIVE_PRODUCT_STATUS = Config
			.getParameter(Jnjb2bCoreConstants.Product.CONSUMER_INACTIVE_PRODUCT_STATUS_KEY);
	protected static final String EACH = "EA";
	protected static final String EMAIL_ATTACHMENT = "email-attachments";
	@Autowired
	protected UserService userService;
	/** The Catalog Version service. */
	@Autowired
	protected CatalogVersionService catalogVersionService;
	@Resource(name = "productDao")
	protected JnjGTProductDao jnjGTProductDao;
	@Autowired
	protected JnJGTMDDProductExportService jnJGTMDDProductExportService;

	/**
	 * The Instance of <code>SessionService</code>.
	 */
	@Autowired
	protected CartService cartService;
	
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;

	@Autowired
	protected MessageService messageService;

	@Resource(name = "newestWorkflowService")
	protected WorkflowService workflowService;

	@Autowired
	protected WorkflowTemplateService workflowTemplateService;

	@Autowired
	protected WorkflowProcessingService workflowProcessingService;

	@Resource(name = "jnjGTUserService")
	protected JnjGTCustomerService jnjGTCustomerService;

	public void setJnjGTCustomerService(JnjGTCustomerService jnjGTCustomerService) {
		this.jnjGTCustomerService = jnjGTCustomerService;
	}

	@Autowired
	protected ModelService modelService;
	
	
	protected MediaService mediaService;

	public MediaService getMediaService() {
		return mediaService;
	}

	public void setMediaService(MediaService mediaService) {
		this.mediaService = mediaService;
	}

	public UserService getUserService() {
		return userService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public JnJGTMDDProductExportService getJnJGTMDDProductExportService() {
		return jnJGTMDDProductExportService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjGTB2BCommerceUserService getJnjGTB2BCommerceUserService() {
		return jnjGTB2BCommerceUserService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public WorkflowService getWorkflowService() {
		return workflowService;
	}

	public WorkflowTemplateService getWorkflowTemplateService() {
		return workflowTemplateService;
	}

	public WorkflowProcessingService getWorkflowProcessingService() {
		return workflowProcessingService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	protected UrlResolver<ProductModel> productModelUrlResolver;

	protected UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}

	@Override
	public boolean isProductDivisionSameAsUserDivision(final JnJProductModel product)
	{
		// validate division
		if (null != product && userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = ((JnJB2bCustomerModel) userService.getCurrentUser());
			final List<String> userDivisions = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
			return userDivisions.contains(product.getSalesOrgCode());
		}
		return false;
	}

	@Override
	public String getProductUrl(final ProductModel product)
	{
		// As for each variant product we need to need to show base product url
		if (product instanceof JnjGTVariantProductModel)
		{
			return getProductModelUrlResolver().resolve(((JnjGTVariantProductModel) product).getBaseProduct());
		}
		else
		{
			return getProductModelUrlResolver().resolve(product);
		}
	}

	@Override
	public ProductModel getProductByValue(final String code, final boolean ignoreUpc)
	{
		// From session get the current site i.e. MDD/CONS
				final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
				ProductModel product = null;
				/*if (currentSite.equals(JnjPCMCoreConstants.PCM))
				{
					product = jnjGTProductDao.getProductByPartialValue(code, ignoreUpc, currentSite, false);
				}*/
				/*else
				{*/
					final String checkUpc = sessionService.getAttribute(Jnjb2bCoreConstants.Cart.IGNORE_UPC);
					
					if (checkUpc == null || checkUpc.equals("false"))
					{
					 
						product = jnjGTProductDao.getProductByPartialValue(code, false, currentSite, false);
						if (!currentSite.equals(Jnjb2bCoreConstants.MDD))
						{
							product = getDeliveryGTIN(product);
						}
					}
					else
					{
						final List<JnjGTVariantProductModel> productList = jnjGTProductDao.getProductByValue(code.trim().toUpperCase(), true);
						if (productList.isEmpty())
						{
							LOGGER.info("For Product Code-" + code + "no variant found");
							return null;
						}
						else
						{
							if (productList.size() == 1)
							{
								LOGGER.info("For Product Code-" + code + ", " + productList.size() + "variant found, returning that variant");
								return productList.get(0);
							}
							else
							{
								LOGGER.info("For Product Code-" + code + ", " + productList.size() + "variants found");
								return getDeliveryGTIN(productList);
							}

						}
					}
				/*}*/
				return product;
	}

	protected JnjGTVariantProductModel getDeliveryGTIN(final Collection<JnjGTVariantProductModel> productList)
	{
		JnjGTVariantProductModel JnjGTVariantProductModel = null;
		for (final VariantProductModel variant : productList)
		{
			if (variant instanceof JnjGTVariantProductModel && (((JnjGTVariantProductModel) variant).getDeliveryGtinInd() != null)
					&& ((JnjGTVariantProductModel) variant).getDeliveryGtinInd().booleanValue())
			{
				JnjGTVariantProductModel = (JnjGTVariantProductModel) variant;
				break;
			}
		}
		// For Canada Catalog, if no variant has GtinInd, any variant can be selected
		if (JnjGTVariantProductModel == null && CollectionUtils.isNotEmpty(productList))
		{
			final JnjGTVariantProductModel variantMod = productList.iterator().next();
			if (variantMod.getCatalogVersion().getCatalog().getId().equals(Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID))
			{
				JnjGTVariantProductModel = variantMod;
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Returning- variant having PK: "
					+ (null != JnjGTVariantProductModel ? JnjGTVariantProductModel.getPk() : null));
		}
		return JnjGTVariantProductModel;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String isProductSaleable(final JnJProductModel product, final String currentSite)
	{
		String errorCode = null;
		/** MDD SALEABLE checks **/
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			/** Checking if active / available **/
			if (!(BooleanUtils.isTrue(product.getAvailableInd()) && (JnjGTModStatus.ACTIVE.equals(product.getModStatus()))))
			{
				//System.out.println("Available Ind"+product.getAvailableInd()+"              "+JnjGTModStatus.ACTIVE.equals(product.getModStatus()));
				//LOGGER.info("the product is not sealable...."+product.getCode());
//				errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.NOT_SALEABLE, null, product.getCode()+"#Nayan#");
				if(JnjGTModStatus.OBSOLETE.equals(product.getModStatus())){
					errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.OBSOLETE, null, product.getCode());
				}
				else if(JnjGTModStatus.DISCONTINUED.equals(product.getModStatus())){
					errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DISCONTINUED, null, product.getCode());
				}
				else if(JnjGTModStatus.NOTAPPLICABLE.equals(product.getModStatus())){
					errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.NOT_APPLICABLE, null, product.getCode());
				}
			}
			/*else if((Jnjb2bCoreConstants.Cart.VALID_DCHAIN_CODE_FOR_ZDEL09.equals(product.getDChainSpecStatusCode())||Jnjb2bCoreConstants.Cart.VALID_DCHAIN_CODE_FOR_ZDEL04.equals(product.getDChainSpecStatusCode()))&&(!(JnjOrderTypesEnum.ZDEL.equals(cartService.getSessionCart().getOrderType().getCode()) ))){
				LOGGER.info("Its only for delivery order, so the product is not sealable...."+product.getCode());
				errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.NOT_SALEABLE, null, product.getCode());
			}*/
			/** Checking if international affiliate or portal indicator is true **/
			else if (!(jnjGTB2BUnitService.isCustomerInternationalAff() || (BooleanUtils.isTrue(product.getJnjPortalInd()))))
			{
				//System.out.println("else if ");
				//System.out.println("Available Ind"+jnjGTB2BUnitService.isCustomerInternationalAff()+"              "+BooleanUtils.isTrue(product.getJnjPortalInd()));
				errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null, product.getCode());
			}
		}
		/** CONSUMER SALEABLE checks **/
		else
		{
			final String productStatusCode = product.getProductStatusCode();
			/** if the product status code is not null and the status code is within the active list **/
			if (!((productStatusCode != null) && (CONSUMER_ACTIVE_PRODUCT_STATUS.contains(productStatusCode))))
			{
				errorCode = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.NOT_SALEABLE, null, product.getCode());
			}
		}
		return errorCode;
	}

	@Override
	public boolean isProductSaleable(final JnJProductModel product, final boolean isIndexing)
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			// If the call is from indexer don't check for international affiliate
			if (isIndexing)
			{
				return (product.getAvailableInd() != null) && (product.getAvailableInd().booleanValue())
						&& (JnjGTModStatus.ACTIVE.equals(product.getModStatus()))
						&& (product.getJnjPortalInd() != null && product.getJnjPortalInd().booleanValue());
			}
			else
			{
				return (product.getAvailableInd() != null)
						&& (product.getAvailableInd().booleanValue())
						&& (JnjGTModStatus.ACTIVE.equals(product.getModStatus()))
						&& (jnjGTB2BUnitService.isCustomerInternationalAff() || (product.getJnjPortalInd() != null && product
								.getJnjPortalInd().booleanValue()));
			}
		}
		else if (Jnjb2bCoreConstants.CONS.equals(currentSite))
		{
			final String productStatusCode = product.getProductStatusCode();
			return ((productStatusCode != null) && (CONSUMER_ACTIVE_PRODUCT_STATUS.contains(productStatusCode))) ? true : false;
		}
		return false;
	}

	@Override
	public boolean isProductBrowsable(final JnJProductModel product)
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			return ((product.getJnjPortalInd() == null) ? false : product.getJnjPortalInd().booleanValue());
		}
		else if (Jnjb2bCoreConstants.CONS.equals(currentSite))
		{
			final String productStatusCode = product.getProductStatusCode();
			return (productStatusCode != null)
					&& (CONSUMER_ACTIVE_PRODUCT_STATUS.contains(productStatusCode) || CONSUMER_INACTIVE_PRODUCT_STATUS
							.contains(productStatusCode)) ? true : false;
		}
		return false;
	}

	@Override
	public boolean isProductSearchable(final JnJProductModel product)
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			return ((product.getJnjPortalInd() == null) ? false : product.getJnjPortalInd().booleanValue());
		}
		else if (Jnjb2bCoreConstants.CONS.equals(currentSite))
		{
			final String productStatusCode = product.getProductStatusCode();
			return (productStatusCode != null)
					&& (CONSUMER_ACTIVE_PRODUCT_STATUS.contains(productStatusCode) || CONSUMER_INACTIVE_PRODUCT_STATUS
							.contains(productStatusCode)) ? true : false;
		}
		return false;
	}

	@Override
	public boolean isPDPViewable(final JnJProductModel product)
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			return ((product.getJnjPortalInd() == null) ? false : product.getJnjPortalInd().booleanValue());
		}
		else if (Jnjb2bCoreConstants.CONS.equals(currentSite))
		{
			final String productStatusCode = product.getProductStatusCode();
			return (productStatusCode != null)
					&& (CONSUMER_ACTIVE_PRODUCT_STATUS.contains(productStatusCode) || CONSUMER_INACTIVE_PRODUCT_STATUS
							.contains(productStatusCode)) ? true : false;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjGTVariantProductModel getDeliveryGTIN(final ProductModel product)
	{
		JnjGTVariantProductModel JnjGTVariantProductModel = null;

		if (null != product && CollectionUtils.isNotEmpty(product.getVariants()))
		{
			final List<JnjGTVariantProductModel> list = new ArrayList<JnjGTVariantProductModel>();
			for (final VariantProductModel variant : product.getVariants())
			{
				list.add((JnjGTVariantProductModel) variant);
			}
			JnjGTVariantProductModel = getDeliveryGTIN(list);
		}
		return JnjGTVariantProductModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnjGTVariantProductModel getSalesGTIN(final JnJProductModel product)
	{
		for (final VariantProductModel variant : product.getVariants())
		{
			if (variant instanceof JnjGTVariantProductModel && (((JnjGTVariantProductModel) variant).getSalesGtinInd() != null)
					&& ((JnjGTVariantProductModel) variant).getSalesGtinInd().booleanValue())
			{
				return (JnjGTVariantProductModel) variant;
			}
		}

		return null;
	}

	@Override
	public ProductModel getProductByCodeOrEAN(final String code)
	{
		return getProductByValue(code, false);
	}


	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public JnJProductModel getProductModelByCode(final String code, final CatalogVersionModel catalogVersionModel)
	{
		return jnjGTProductDao.getProductModelByCode(code, catalogVersionModel);
	}

	@Override
	public List<JnJProductModel> getProductModelByExactUpcCode(final String upcCode,
			final CatalogVersionModel catalogVersionModel)
	{
		return jnjGTProductDao.getProductModelByUpcCode(upcCode, catalogVersionModel);
	}

	@Override
	public UnitModel getUnitByCode(final String code)
	{
		final UnitModel unit = new UnitModel();
		unit.setCode(code);
		return flexibleSearchService.getModelByExample(unit);
	}

	@Override
	public Date getProductLotExpirationDate(final JnJProductModel productModel, final String batchNumber)
	{
		final List<JnjGTProductLotModel> lotInformation = getProductLotInfo(productModel.getCode(), batchNumber);
		if (lotInformation != null)
		{
			for (final JnjGTProductLotModel productLot : lotInformation)
			{
				return productLot.getExpirationDate();
			}
		}
		return null;
	}

	@Override
	public List<String> getEligibleUrlAndCodeForOrderHistoryAndTemplate(final JnJProductModel product,
			final JnjGTVariantProductModel refrenceVariant)
	{
		final List<String> values = new ArrayList<String>();
		final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		final String productCode = product.getCode();
		final String refProductCode = refrenceVariant.getBaseProduct().getCode();
		final String EanNumber = refrenceVariant.getEan();
		String value = null;
		String url = null;
		if (Jnjb2bCoreConstants.CONS.equals(currentSiteName))
		{
			final JnJProductModel modProduct = jnjGTProductDao.getModProductByBase(product.getCode(), product.getPk().toString());
			value = modProduct.getCode();
			url = getProductUrl(modProduct);
		}
		else
		{
			if (StringUtils.equals(productCode, refProductCode))
			{
				if (StringUtils.isNotEmpty(EanNumber))
				{
					value = EanNumber;
				}
				else
				{
					value = productCode;
				}
			}
			else
			{
				value = productCode;
			}
			url = getProductUrl(product);
		}
		values.add(value);
		values.add(url);
		return values;
	}

	/**
	 * Gets the jnj na product model by code by querying the db with admin for Order Template
	 * 
	 * @param code
	 *           the code
	 * @return the product model by code
	 */
	@Override
	public JnJProductModel getProductForOrderTemplate(final String code, final String sourceId)
	{
		return jnjGTProductDao.getProductForOrderTemplate(code, sourceId);
	}

	/**
	 * Retrieves all CPSIA details for the products present in current session catalog.
	 * 
	 * @param sortBy
	 * @return Collection<JnjGTProductCpscDetailModel>
	 */
	@Override
	public Collection<JnjGTProductCpscDetailModel> getConsumerProductsCpsia(final String sortBy)
	{
		final String METHOD_NAME = "getConsumerProductsCpsia";
		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOGGER);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling DAO layer to fetch CPSIA data ...", LOGGER);
		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		/** Calling DAO layer to fetch CPSIA DATA **/
		return jnjGTProductDao.getConsumerProductsCpsia(sortBy);
	}

	/**
	 * This method fetches the CPSIA data for the supplied product
	 * 
	 * @param productId
	 * @return JnjGTProductCpscDetailModel
	 */
	@Override
	public JnjGTProductCpscDetailModel getCpsiaDataForProduct(final String productId)
	{
		final String METHOD_NAME = "getCpsiaDataForProduct";
		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.START_TIME, LOGGER);

		/** Populating the product Id in the model **/
		JnjGTProductCpscDetailModel JnjGTProductCpscDetailModel = new JnjGTProductCpscDetailModel();
		JnjGTProductCpscDetailModel.setUid(productId);

		/** Using get model by example of flexibleSearchService to get CPSIA data **/
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling Flexible Search Service to get CPSIA Data ... ", LOGGER);
		JnjGTProductCpscDetailModel = flexibleSearchService.getModelByExample(JnjGTProductCpscDetailModel);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Calling Flexible Search Service to get CPSIA Data ... ", LOGGER);

		//CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		/** Calling DAO layer to fetch CPSIA DATA **/
		return JnjGTProductCpscDetailModel;
	}

	@Override
	public Collection<JnjGTVariantProductModel> getVariantsOrderedByPkgLvlCode(final String productPk)
	{
		return jnjGTProductDao.getVariantsOrderedByPkgLvlCode(productPk);
	}

	/*@Override
	public String getLaunchStatus(final JnJProductModel product, final Boolean onlyActive)
	{
		String launchStatus = null;
		final JnjGTModStatus modStatus = product.getPcmModStatus();

		if (modStatus != null)
		{
			// Based on the catalog decide which date to be used for computation
			final Date prodDate = (product.getCatalogVersion().getCatalog().getId()
					.equals(Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID)) ? product.getFirstShipEffectDate() : product
					.getNewProductStartDate();

			if (modStatus.equals(JnjGTModStatus.ACTIVE) && prodDate != null)
			{
				final Date today = new Date();
				final Date todayPlusOne = DateUtils.addDays(today, 1);
				final Date todayPlusThirty = DateUtils.addDays(today, 30);
				final Date todayMinusSixM = DateUtils.addDays(today, -180);
				if (prodDate.after(todayPlusOne) && prodDate.before(todayPlusThirty))
				{
					launchStatus = Jnjgtb2bCONSConstants.COMING_SOON;
				}
				else if (prodDate.before(today) && prodDate.after(todayMinusSixM))
				{
					launchStatus = Jnjgtb2bCONSConstants.NEW;
				}
			}
			else if (onlyActive != null && !onlyActive.booleanValue() && modStatus.equals(JnjGTModStatus.DISCONTINUED))
			{
				launchStatus = modStatus.getCode();
			}
		}
		return launchStatus;
	}*/

	/*@Override
	public CompletenessStatusData getCompletenessValueIndicator(final JnJProductModel productModel)
	{
		final Double completenessInd;
		double totalNumOfAttr = 0;
		final double totalRichContentAttr = 9;
		final double totalLabelingAttr = 7;
		final double totalImageryAttr = 3;
		int numberOfCompleteAttr = 0;
		int richContentCompAttr = 0;
		int labelingCompAttr = 0;
		int imageryCompAttr = 0;

		final String METHOD_NAME = "getCompletenessValueIndicator";
		//CommonUtil.logMethodStartOrEnd(COMPLETENESS_IND, METHOD_NAME, Logging.START_TIME, LOGGER);
		// Check for Labeling Attributes (7)
		if (StringUtils.isNotBlank(productModel.getIngredient()))
		{
			labelingCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getIndication()))
		{
			labelingCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getWarning()))
		{
			labelingCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getDirection()))
		{
			labelingCompAttr++;
		}
		if (productModel.getArtworkApprovalDate() != null)
		{
			labelingCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getArtworkApprovalCrr()))
		{
			labelingCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getRmcNo()))
		{
			labelingCompAttr++;
		}

		// Check for Rich Content Attributes (9)
		if (StringUtils.isNotBlank(productModel.getName()))
		{
			richContentCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getSummary()))
		{
			richContentCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getDescription()))
		{
			richContentCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getLongBullet()))
		{
			richContentCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getShortBullets()))
		{
			richContentCompAttr++;
		}
		if (StringUtils.isNotBlank(productModel.getCopyApprovalReference()))
		{
			richContentCompAttr++;
		}
		if (productModel.getCopyApprovalDate() != null)
		{
			richContentCompAttr++;
		}
		if (CollectionUtils.isNotEmpty(productModel.getKeywords()))
		{
			richContentCompAttr++;
		}

		// Calculate total number of cross sell products
		final Collection<ProductReferenceModel> productReferences = productModel.getProductReferences();
		for (final ProductReferenceModel productReferenceModel : productReferences)
		{
			if (productReferenceModel.getReferenceType().equals(ProductReferenceTypeEnum.CROSSELLING))
			{
				richContentCompAttr++;
				break;
			}
		}

		// Check for imagery Attributes (3)
		if (productModel.getPicture() != null)
		{
			imageryCompAttr++;
		}
		if (CollectionUtils.isNotEmpty(productModel.getOthers()))
		{
			imageryCompAttr++;
		}
		if (productModel.getImageLastUpdateDate() != null)
		{
			imageryCompAttr++;
		}
		CommonUtil.logDebugMessage(COMPLETENESS_IND, METHOD_NAME, "Rich Content Attrs: " + richContentCompAttr
				+ " complete out of " + totalRichContentAttr, LOGGER);
		CommonUtil.logDebugMessage(COMPLETENESS_IND, METHOD_NAME, "Labeling Attrs: " + labelingCompAttr + " complete out of "
				+ totalLabelingAttr, LOGGER);
		CommonUtil.logDebugMessage(COMPLETENESS_IND, METHOD_NAME, "Imagery Attrs: " + imageryCompAttr + " complete out of "
				+ totalImageryAttr, LOGGER);
		totalNumOfAttr = totalRichContentAttr + totalLabelingAttr + totalImageryAttr;
		numberOfCompleteAttr = richContentCompAttr + labelingCompAttr + imageryCompAttr;
		completenessInd = Double.valueOf(numberOfCompleteAttr / totalNumOfAttr * 100);
		final CompletenessStatusData statusData = new CompletenessStatusData();
		final List<String> missingArea = new ArrayList<String>();
		if (richContentCompAttr / totalRichContentAttr != 1)
		{
			missingArea.add("Rich Marketing");
		}
		if (labelingCompAttr / totalLabelingAttr != 1)
		{
			missingArea.add("Labelling");
		}
		if (imageryCompAttr / totalImageryAttr != 1)
		{
			missingArea.add("Imagery");
		}
		statusData.setCompletenessInd((Math.round(completenessInd.doubleValue() * 100.0) / 100.0) + "%");
		statusData.setMissingAreaValue(missingArea);
		//CommonUtil.logMethodStartOrEnd(COMPLETENESS_IND, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
		return statusData;
	}*/

	/**
	 * This method is used to check if the mandatory attributes are completely filled for a product
	 * 
	 * @param productModel
	 * @return
	 */
	/*@Override
	public boolean mandatoryAttributesCheck(final JnJProductModel productModel)
	{
		int compMDMAttributes = 0;
		int totalMDMAttributes = 19; // Should be changed everytime mdm attributes increase/decrease.
		totalMDMAttributes += 11;
		compMDMAttributes = getMandatoryAttributes(productModel, compMDMAttributes);
		return (compMDMAttributes / totalMDMAttributes >= 1) ? true : false;
	}*/

	/**
	 * This method is used to return the total number of mandatory attributes are completely filled for a product
	 * 
	 * @param productModel
	 * @param mdmCompleteAttr
	 * @return
	 */
	/*protected int getMandatoryAttributes(final JnJProductModel productModel, int mdmCompleteAttr)
	{
		String unitCode = null;
		// Check for MDM attributes (19)
		mdmCompleteAttr = checkStringValue(productModel, productModel.getSourceSystemId(), JnJProductModel.SOURCESYSTEMID,
				mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getRecordTimeStamp(), JnJProductModel.RECORDTIMESTAMP,
				mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getCode(), JnJProductModel.CODE, mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getKitInd(), JnJProductModel.KITIND, mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getGlobalBusinessUnit(),
				JnJProductModel.GLOBALBUSINESSUNIT, mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getPcmBrand(), JnJProductModel.PCMBRAND, mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getPcmSubBrand(), JnJProductModel.PCMSUBBRAND,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getGlobalProductCode(), JnJProductModel.GLOBALPRODUCTCODE,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getUpcCode(), JnJProductModel.UPCCODE, mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getMaterialBaseNum(), JnJProductModel.MATERIALBASENUM,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getProductStatusCode(), JnJProductModel.PRODUCTSTATUSCODE,
				mdmCompleteAttr);

		mdmCompleteAttr = checkObjectValue(productModel, productModel.getMaterialStatusEffectDate(),
				JnJProductModel.MATERIALSTATUSEFFECTDATE, mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getFirstShipEffectDate(),
				JnJProductModel.FIRSTSHIPEFFECTDATE, mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getPublishInd(), JnJProductModel.PUBLISHIND,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getNetContent(), JnJProductModel.NETCONTENT,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getMdmDescription(), JnJProductModel.MDMDESCRIPTION,
				mdmCompleteAttr);
		mdmCompleteAttr = checkStringValue(productModel, productModel.getSalesOrgCode(), JnJProductModel.SALESORGCODE,
				mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getProductStatus(), JnJProductModel.PRODUCTSTATUS,
				mdmCompleteAttr);
		mdmCompleteAttr = checkObjectValue(productModel, productModel.getPcmInd(), JnJProductModel.PCMIND, mdmCompleteAttr);
		//commenting out distChannel attribute for the time being as the validation for the same.
		//mdmCompleteAttr = checkStringValue(productModel, productModel.getDistChannel(), JnJProductModel.DISTCHANNEL,
		//mdmCompleteAttr);
		// Variant attributes

		final Collection<VariantProductModel> productVariants = productModel.getVariants();
		for (final VariantProductModel variantProductModel : productVariants)
		{
			if (variantProductModel instanceof JnjGTVariantProductModel)
			{
				final JnjGTVariantProductModel jnjGTvariant = (JnjGTVariantProductModel) variantProductModel;
				if (jnjGTvariant.getUnit() != null)
				{
					unitCode = jnjGTvariant.getUnit().getCode();
				}

				//11 attributes
				switch (unitCode)
				{
					case EACH:
						// DEPTH
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getDepth(), JnjGTVariantProductModel.DEPTH,
								mdmCompleteAttr);

						// WIDTH
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getWidth(), JnjGTVariantProductModel.WIDTH,
								mdmCompleteAttr);

						// HEIGHT
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getHeight(), JnjGTVariantProductModel.HEIGHT,
								mdmCompleteAttr);

						// STG_PRODUCT_UNIT-VOL_QTY
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getVolumeQty(),
								JnjGTVariantProductModel.VOLUMEQTY, mdmCompleteAttr);

						// VOL_UOM_CD
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getVolumeUom(),
								JnjGTVariantProductModel.VOLUMEUOM, mdmCompleteAttr);

						// LIN_UOM_CD
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getLinearUom(),
								JnjGTVariantProductModel.LINEARUOM, mdmCompleteAttr);

						// GROSS_WEIGHT_QTY
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getWeightQty(),
								JnjGTVariantProductModel.WEIGHTQTY, mdmCompleteAttr);

						// WEIGHT_UOM_CD
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getWeightUom(),
								JnjGTVariantProductModel.WEIGHTUOM, mdmCompleteAttr);

						// ALT_UNIT_CD and ALT_UNIT_NAME both derive Unit
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getUnit(), JnjGTVariantProductModel.UNIT,
								mdmCompleteAttr);

						// BASE_UNIT_CD
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getLwrPackagingLvlUom(),
								JnjGTVariantProductModel.LWRPACKAGINGLVLUOM, mdmCompleteAttr);

						// UOM_CONV_NUMERATOR
						mdmCompleteAttr = checkObjectValue(jnjGTvariant, jnjGTvariant.getNumerator(),
								JnjGTVariantProductModel.NUMERATOR, mdmCompleteAttr);
						break;
					default:
						break;
				}
			}
		}

		return mdmCompleteAttr;
	}*/

	/**
	 * This method is used to check if a mandatory attribute is null or not.
	 * 
	 * @param productModel
	 * @param mdmCompleteAttr
	 * @return
	 */
	protected int checkObjectValue(final ProductModel productModel, final Object attribute, final String attributeName,
			int mdmCompleteAttr)
	{
		if (attribute != null)
		{
			mdmCompleteAttr++;
		}
		else
		{
			LOGGER.info("The product with code " + productModel.getCode() + " has the missing attribute " + attributeName
					+ " due to which the mandatory check will fail for the product.");
		}
		return mdmCompleteAttr;
	}

	/**
	 * This method is used to check if a mandatory string value is null or not.
	 * 
	 * @param productModel
	 * @param mdmCompleteAttr
	 * @return
	 */
	protected int checkStringValue(final ProductModel productModel, final String attribute, final String attributeName,
			int mdmCompleteAttr)
	{
		if (StringUtils.isNotBlank(attribute))
		{
			mdmCompleteAttr++;
		}
		else
		{
			LOGGER.info("The product with code " + productModel.getCode() + " has the missing attribute " + attributeName
					+ " due to which the mandatory check will fail for the proudct.");
		}
		return mdmCompleteAttr;
	}

	/**
	 * This method is used to fetch the newly activated products, So as to send the details of these products to the
	 * sales team via. Email.
	 */
	@Override
	public List<JnJProductModel> getNewlyActivatedProducts(final CatalogVersionModel catalogVersionModel)
	{
		final Date today = new Date();
		/* this date will used to compute the firstShipEffectDate or the new product start date */
		final Date endDate = DateUtils.addDays(today, 30);
		/* this date will used to compute which products that were activated in the last 7 days */
		final Date startDate = DateUtils.addDays(endDate, -7);
		final SimpleDateFormat formatTo = new SimpleDateFormat("YYYY-MM-DD");
		final Calendar endDateCalendar = Calendar.getInstance();
		endDateCalendar.setTime(endDate);
		final String formatedEndDate = endDateCalendar.get(Calendar.YEAR) + "-" + (endDateCalendar.get(Calendar.MONTH) + 1) + "-"
				+ endDateCalendar.get(Calendar.DATE);
		final Calendar startDateCalendar = Calendar.getInstance();
		startDateCalendar.setTime(startDate);
		formatTo.setCalendar(startDateCalendar);
		final String formatedStartDate = startDateCalendar.get(Calendar.YEAR) + "-" + (startDateCalendar.get(Calendar.MONTH) + 1)
				+ "-" + startDateCalendar.get(Calendar.DATE);
		final List<JnJProductModel> products = jnjGTProductDao.getNewActiveProducts(catalogVersionModel, formatedEndDate,
				formatedStartDate);

		return products;
	}

	@Override
	public void createProductWorkflow(final JnJProductModel JnJProductModel, final boolean newProductWorkflow)
			throws BusinessException
	{
		final String METHOD_NAME = "createProductWorkflow";
		//CommonUtil.logMethodStartOrEnd(JNJ_NA_PRODUCT_FACADE, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOGGER);

		if (null == JnJProductModel)
		{
			LOGGER.error(JNJ_GT_PRODUCT_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Incomming Product is null. Throwing Business Exception.");
			throw new BusinessException("Incomming Product is null.");
		}

		/* Fetching CatalogVersion of Incoming JnjGTProduct */
		final CatalogVersionModel catalogVersionModel = JnJProductModel.getCatalogVersion();

		/* Throwing Exception if the Incoming JnjGTProduct is not from [STAGED] CatalogVersion */
		if (!StringUtils.equalsIgnoreCase(catalogVersionModel.getVersion(), Jnjb2bCoreConstants.STAGED))
		{
			LOGGER.error(JNJ_GT_PRODUCT_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Incomming Product with code ["
					+ JnJProductModel.getCode() + "] is not From [STAGED] Catalog Version. Throwing Business Exception.");
			throw new BusinessException("Incomming Product with code [" + JnJProductModel.getCode()
					+ "] is not From [STAGED] Catalog Version.");
		}

		final CatalogModel catalogModel = catalogVersionModel.getCatalog();
		final String catalogID = catalogModel.getId();
		/* Fetching ProductManagerGroup based on Catalog. */
		final UserGroupModel productManagerGroup = jnjGTCustomerService.getProductManagerGroupForCatalog(catalogModel);

		if (null == productManagerGroup)
		{
			LOGGER.error(JNJ_GT_PRODUCT_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "No PrductManagerGroup can be found for Catalog with ID[" + catalogModel.getId()
					+ "]. Throwing Business Exception.");
			throw new BusinessException("No PrductManagerGroup can be found for Catalog with ID[" + catalogModel.getId() + "].");
		}

		WorkflowModel workflow = null;
		String workFlowTemplateCode = null;
		String workFlowName = null;
		try
		{
			/* Getting the workflowTemplateCode and workFlowName based on flag 'newProductWorkflow' */
			if (newProductWorkflow)
			{
				if (StringUtils.equalsIgnoreCase(catalogID, Jnjb2bCoreConstants.CONSUMER_US_PRODUCT_CATALOG_ID))
				{
					/* Getting New Product WorkFlow Template Code for naProdCatalog */
					//workFlowTemplateCode = JnjPCMCoreConstants.ProductWorkflows.JNJ_US_PCM_NEW_PRODUCT_WORK_FLOW_TEMPLATE_CODE;
					//workFlowName = JnjPCMCoreConstants.ProductWorkflows.JNJ_US_PCM_NEW_PRODUCT_WORK_FLOW;
				}
				else if (StringUtils.equalsIgnoreCase(catalogID, Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID))
				{
					/* Getting New Product WorkFlow Template Code for consCAProdCatalog */
					//workFlowTemplateCode = JnjPCMCoreConstants.ProductWorkflows.JNJ_CA_PCM_NEW_PRODUCT_WORK_FLOW_TEMPLATE_CODE;
					//workFlowName = JnjPCMCoreConstants.ProductWorkflows.JNJ_CA_PCM_NEW_PRODUCT_WORK_FLOW;
				}
			}
			else
			{
				if (StringUtils.equalsIgnoreCase(catalogID, Jnjb2bCoreConstants.CONSUMER_US_PRODUCT_CATALOG_ID))
				{
					/* Getting Update Product WorkFlow Template Code for naProdCatalog */
					//workFlowTemplateCode = JnjPCMCoreConstants.ProductWorkflows.JNJ_US_PCM_UPDATE_PRODUCT_WORK_FLOW_TEMPLATE_CODE;
					//workFlowName = JnjPCMCoreConstants.ProductWorkflows.JNJ_US_PCM_UPDATE_PRODUCT_WORK_FLOW;
				}
				else if (StringUtils.equalsIgnoreCase(catalogID, Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID))
				{
					/* Getting Update Product WorkFlow Template Code for consCAProdCatalog */
					//workFlowTemplateCode = JnjPCMCoreConstants.ProductWorkflows.JNJ_CA_PCM_UPDATE_PRODUCT_WORK_FLOW_TEMPLATE_CODE;
					//workFlowName = JnjPCMCoreConstants.ProductWorkflows.JNJ_CA_PCM_UPDATE_PRODUCT_WORK_FLOW;
				}
			}

			/* Getting the First Member of returned ProductManagerGroup */
			final UserModel productManager = (UserModel) productManagerGroup.getMembers().iterator().next();

			/* Fetching the WorkFlow Template */
			final WorkflowTemplateModel workflowTemplate = this.workflowTemplateService
					.getWorkflowTemplateForCode(workFlowTemplateCode);

			/* Creating the WorkFlow from the Template */
			workflow = workflowService.createWorkflow(workflowTemplate, JnJProductModel, productManager);
			workflow.setName(workFlowName + Jnjb2bCoreConstants.SYMBOL_UNDERSCORE + JnJProductModel.getUpcCode());
			modelService.save(workflow);

			/* Iterating and saving WORKFLOW actions */
			for (final WorkflowActionModel action : workflow.getActions())
			{
				modelService.save(action);
			}
			workflowProcessingService.startWorkflow(workflow);
		}
		catch (final Exception exception)
		{
			LOGGER.error(JNJ_GT_PRODUCT_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Exception Occured while Creating the workflow for Product with Code[" + JnJProductModel.getCode() + "].",
					exception);
			throw new BusinessException("Exception Occured while Creating the workflow for Product with Code ["
					+ JnJProductModel.getCode() + "].");
		}

		LOGGER.info(JNJ_GT_PRODUCT_FACADE + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Workflow with Code["
				+ workflow.getCode() + "] and Name [" + workflow.getName() + "] for Product with Code[" + JnJProductModel.getCode()
				+ "] successfully created.");

		//CommonUtil.logMethodStartOrEnd(JNJ_NA_PRODUCT_FACADE, METHOD_NAME, Logging.END_OF_METHOD, LOGGER);
	}

	@Override
	public List<JnjGTProductLotModel> getProductLotInfo(final String prouctCode, final String batchNumber)
	{
		List<JnjGTProductLotModel> productLotList = null;
		final JnjGTProductLotModel JnjGTProductLotModel = new JnjGTProductLotModel();
		JnjGTProductLotModel.setProductCode(prouctCode);
		if (StringUtils.isNotEmpty(batchNumber))
		{
			JnjGTProductLotModel.setLotNumber(batchNumber);
			try
			{
				productLotList = flexibleSearchService.getModelsByExample(JnjGTProductLotModel);
			}
			catch (final ModelNotFoundException exception)
			{
				// In case of Lot master feed this message is req for info only
				LOGGER.info("Lot Master not Found,Creating a New Model" + exception.getMessage());
			}
		}
		return productLotList;
	}

	@Override
	public List<JnJProductModel> getProductsForCategory(final String currentSite, final String accountPK)
	{
		return jnjGTProductDao.getAllProductsForSite(currentSite, accountPK);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTProductService#createMDDExportFile(java.lang.String, java.util.List,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public HSSFWorkbook createMDDExportFile(final List<JnJProductModel> products, final String fileName)
	{
		final String METHOD_NAME = "createMDDExportFile()";
		LOGGER.info("JnJGTProductService" + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Start of the method");
		catalogVersionService.setSessionCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE);
		final String sheetName = "MDD_Products_Sheet_0";
		final HSSFWorkbook excelWorkBook = new HSSFWorkbook();

		final HSSFFont font = excelWorkBook.createFont();
		font.setBold(true);

		final HSSFCellStyle style = excelWorkBook.createCellStyle();
		style.setFont(font);
		final HSSFSheet sheet = excelWorkBook.createSheet(sheetName);

		sheet.autoSizeColumn(0);
		final HSSFRow downloadDateHeader = sheet.createRow(0);
		downloadDateHeader.createCell(0).setCellValue("Download date");
		downloadDateHeader.getCell(0).setCellStyle(style);

		final String currentTime = new Date().toString();
		downloadDateHeader.createCell(1).setCellValue(currentTime);

		/*
		 * final HSSFRow globalAccounHeader = sheet.createRow(1);
		 * globalAccounHeader.createCell(0).setCellValue("Global Account Name");
		 * globalAccounHeader.getCell(0).setCellStyle(style);
		 * globalAccounHeader.createCell(1).setCellValue(currentAccount);
		 */
		try
		{
			final String filepath = Config.getParameter(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY) + File.separator
					+ fileName;
			createMDDExcelFile(products, sheet, excelWorkBook, style, filepath);
			final File file = new File(filepath);
			createMedia(file);
		}
		catch (final Exception exception)
		{
			LOGGER.error("There was an error while trying to create the excel file for the catalog export", exception);
		}
		LOGGER.info("JnJGTProductService" + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "End of the method");
		return excelWorkBook;
	}

	protected void createMedia(final File file) throws BusinessException
	{
		MediaModel mediaModel = null;
		InputStream inputStream = null;
		mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
		mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID,
				Jnjb2bCoreConstants.ONLINE));
		mediaModel.setCode(Jnjb2bCoreConstants.MDD_EXPORT_FILE_MEDIA_KEY);

		//mediaModel.setRealFileName(mainImage.getName());
		try
		{
			final List<MediaModel> existingMediaList = flexibleSearchService.getModelsByExample(mediaModel);
			if (existingMediaList != null)
			{
				for (final MediaModel media : existingMediaList)
				{
					modelService.remove(media);
					LOGGER.info("Media Found and deleted");
					modelService.refresh(media);
				}
			}
		}
		catch (final Exception exception)
		{
			LOGGER.info("Media Not Found");
		}
		try
		{
			inputStream = new FileInputStream(file);
			mediaModel.setFolder(mediaService.getFolder(EMAIL_ATTACHMENT)); //Setting the Media folder
			modelService.saveAll(mediaModel);
			LOGGER.info("Media saved with the code:" + mediaModel.getCode());
			mediaService.setStreamForMedia(mediaModel, inputStream, file.getName(),
					Files.probeContentType(Path.of(file.getAbsolutePath()))); //Setting the File into the Media Model
			modelService.save(mediaModel);
			LOGGER.info("Media saved with the code:" + mediaModel.getCode() + " with the Pk for File: " + mediaModel.getDataPK()
					+ " with the location:" + mediaModel.getLocation());

		}
		catch (final Exception exception)
		{
			LOGGER.error("Exception occured during creation of media!!!");
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (final IOException iOException)
			{
				LOGGER.error("Exception occured during closing file in the method createMedia()" + iOException);
			}
		}
	}


	
	
	
	@Override
	public HSSFWorkbook createCONSExportFile(final List<JnjGTProductData> catalogProductsData, final String currentAccount,
			final String fileName)
	{
		final String METHOD_NAME = "createCONSExportFile()";
		LOGGER.info("JnJGTProductService" + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Start of the method");
		final String sheetName = "CONS_Product-Catalog(Consumer)";
		final HSSFWorkbook excelFile = new HSSFWorkbook();

		final HSSFFont font = excelFile.createFont();
		//font.setBold(true);

		final HSSFCellStyle style = excelFile.createCellStyle();
		style.setFont(font);
		final HSSFSheet sheet = excelFile.createSheet(sheetName);

		sheet.autoSizeColumn(0);
		final HSSFRow downloadDateHeader = sheet.createRow(0);
		downloadDateHeader.createCell(0).setCellValue("Download date");
		downloadDateHeader.getCell(0).setCellStyle(style);

		final String currentTime = new Date().toString();
		downloadDateHeader.createCell(1).setCellValue(currentTime);

		final HSSFRow globalAccounHeader = sheet.createRow(1);
		globalAccounHeader.createCell(0).setCellValue("Global Account Name");
		globalAccounHeader.getCell(0).setCellStyle(style);
		globalAccounHeader.createCell(1).setCellValue(currentAccount);
		FileOutputStream outputStream = null;
		try
		{
			final String filepath = Config.getParameter(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_PATH_KEY) + File.separator
					+ fileName;
			outputStream = new FileOutputStream(filepath);
			excelFile.write(outputStream);

			createCONSExcelFile(catalogProductsData, currentAccount, sheet, excelFile, style, filepath);

		}
		catch (final IOException exception)
		{
			LOGGER.error(
					"There was an error while trying to perform I/O operations while creating the excel file for the catalog export",
					exception);
		}
		catch (final Exception exception)
		{
			LOGGER.error("There was an error while trying to create the excel file for the catalog export", exception);
		}
		finally
		{
			try
			{
				outputStream.flush();
				outputStream.close();
			}
			catch (final IOException exception)
			{
				LOGGER.error(
						"There was an error while trying to perform I/O operations while creating the excel file for the catalog export",
						exception);
			}
		}
		LOGGER.info("JnJGTProductService" + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "End of the method");
		return excelFile;
	}

	/**
	 * This method is used to create the excel file which will be downloaded in case of Consumer catalog.
	 * 
	 * @param catalogProductsData
	 * @param currentAccount
	 * @param sheet
	 * @param excelFile
	 * @param style
	 */
	protected void createCONSExcelFile(final List<JnjGTProductData> catalogProductsData, final String currentAccount,
			final HSSFSheet sheet, final HSSFWorkbook excelFile, final HSSFCellStyle style, final String filepath)
	{

		final HSSFRow header = sheet.createRow(3);

		header.createCell(0).setCellValue("Name");

		header.createCell(1).setCellValue("Franchise");

		header.createCell(2).setCellValue("Brand");

		header.createCell(3).setCellValue("Product Code");

		header.createCell(4).setCellValue("UPC");

		header.createCell(5).setCellValue("Description");

		header.createCell(6).setCellValue("Shipping UOM");

		header.createCell(7).setCellValue("Eaches per Case");

		header.createCell(8).setCellValue("Inner Packs per Case");

		header.createCell(9).setCellValue("Eaches per Inner Pack");

		header.createCell(10).setCellValue("Tiers per Pallet");

		header.createCell(11).setCellValue("Cases per Pallet");

		header.createCell(12).setCellValue("First Effective Ship Date");

		header.createCell(13).setCellValue("Case GTIN");

		header.createCell(14).setCellValue("Case Weight");

		header.createCell(15).setCellValue("Case Height");

		header.createCell(16).setCellValue("Case Depth");

		header.createCell(17).setCellValue("Case Width");

		header.createCell(18).setCellValue("Case Price");

		header.createCell(19).setCellValue("Eaches GTIN");

		header.createCell(20).setCellValue("Eaches Weight");

		header.createCell(21).setCellValue("Eaches Volume");

		header.createCell(22).setCellValue("Eaches Height");

		header.createCell(23).setCellValue("Eaches Depth");

		header.createCell(24).setCellValue("Eaches Width");

		header.createCell(25).setCellValue("Eaches Price");

		header.createCell(26).setCellValue("Country of Origin");

		header.createCell(27).setCellValue("Country of Assembly");
		//Apply the specified style to all the cells in the header
		for (final Iterator iterator = header.cellIterator(); iterator.hasNext();)
		{
			final HSSFCell cell = (HSSFCell) iterator.next();
			cell.setCellStyle(style);
		}

		int rowNumber = 4;
		FileOutputStream outputStream = null;
		for (final JnjGTProductData data : catalogProductsData)
		{
			int columnNumber = 0;
			if (data.getConsumerSpecification() != null)
			{
				final HSSFRow row = sheet.createRow(rowNumber++);
				createCell(data.getName(), row, columnNumber++, sheet);
				createCell(data.getConsumerSpecification().getFranchise(), row, columnNumber++, sheet);
				createCell(data.getConsumerSpecification().getBrand(), row, columnNumber++, sheet);
				createCell(data.getBaseMaterialNumber(), row, columnNumber++, sheet);
				createCell(data.getUpc(), row, columnNumber++, sheet);
				createCell(data.getDescription(), row, columnNumber++, sheet);
				/** Set the shipping info details only if the shippingInfo is not null **/
				if (data.getConsumerSpecification().getShippingInfo() != null)
				{
					//Shipping info for the product
					createCell(data.getConsumerSpecification().getShippingInfo().getShippingUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getEaPerCase(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getIpPerCAse(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getEaPerInnerPacks(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getTrPerPallet(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getCsPerTier(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getShippingInfo().getShipEffectiveDate(), row, columnNumber++, sheet);
				}
				else
				{
					columnNumber = +7;
				}

				if (data.getConsumerSpecification().getCaseInfo() != null)
				{
					createCell(data.getConsumerSpecification().getCaseInfo().getGtin(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getCaseInfo().getWeight() + " "
							+ data.getConsumerSpecification().getCaseInfo().getWeightLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getCaseInfo().getHeight() + " "
							+ data.getConsumerSpecification().getCaseInfo().getHeightLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getCaseInfo().getDepth() + " "
							+ data.getConsumerSpecification().getCaseInfo().getDepthLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getCaseInfo().getWidth() + " "
							+ data.getConsumerSpecification().getCaseInfo().getWidthLinearUom(), row, columnNumber++, sheet);
					if (data.getPrice() != null)
					{
						createCell(data.getConsumerSpecification().getCasePrice().getFormattedValue(), row, columnNumber++, sheet);
					}
					else
					{
						columnNumber++;
					}
				}
				else
				{
					columnNumber = +6;
				}

				if (data.getConsumerSpecification().getEachesInfo() != null)
				{
					createCell(data.getConsumerSpecification().getEachesInfo().getGtin(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getEachesInfo().getWeight() + " "
							+ data.getConsumerSpecification().getEachesInfo().getWeightLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getEachesInfo().getVolume() + " "
							+ data.getConsumerSpecification().getEachesInfo().getVolumeLinearUom() + " "
							+ data.getConsumerSpecification().getEachesInfo().getVolumeLUomSuperscript(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getEachesInfo().getHeight() + " "
							+ data.getConsumerSpecification().getEachesInfo().getHeightLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getEachesInfo().getDepth() + " "
							+ data.getConsumerSpecification().getEachesInfo().getDepthLinearUom(), row, columnNumber++, sheet);
					createCell(data.getConsumerSpecification().getEachesInfo().getWidth() + " "
							+ data.getConsumerSpecification().getEachesInfo().getWidthLinearUom(), row, columnNumber++, sheet);
					if (data.getPrice() != null)
					{
						createCell(data.getConsumerSpecification().getEachPrice().getFormattedValue(), row, columnNumber++, sheet);
					}
					else
					{
						columnNumber++;
					}
				}
				else
				{
					columnNumber = +7;
				}

				createCell(data.getConsumerSpecification().getOriginCountry(), row, columnNumber++, sheet);
				createCell(data.getConsumerSpecification().getAssemblyCountry(), row, columnNumber++, sheet);

			}
		}
		try
		{
			outputStream = new FileOutputStream(filepath);
			excelFile.write(outputStream);
		}
		catch (final FileNotFoundException exception)
		{
			LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
		}
		catch (final IOException exception)
		{
			LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
		}
		finally
		{
			try
			{
				outputStream.flush();
				outputStream.close();
			}
			catch (final IOException exception)
			{
				LOGGER.error(
						"There was an error while trying to perform I/O operations while creating the excel file for the catalog export",
						exception);
			}
		}
	}



	/**
	 * This method is used to create the excel file which will be download in case of MDD catalog.
	 * 
	 * @param products
	 * @param currentAccount
	 * @param sheet
	 * @param excelFile
	 * @param style
	 */
	protected void createMDDExcelFile(final List<JnJProductModel> products, HSSFSheet sheet, final HSSFWorkbook excelWorkBook,
			final HSSFCellStyle style, final String filepath)
	{
		final String seperator = ";";
		final String METHOD_NAME = "createMDDExcelFile()";
		final int productsPerSheet = Integer.parseInt(Config.getParameter("export.MDD.productsPerSheets"));
		int productIndex = 0;
		int sheetIndex = 0;
		int rowNumber = 3;
		createHeader(rowNumber, sheet, style);
		final StringBuilder stringBuilder = new StringBuilder();
		for (final JnJProductModel productModel : products)
		{
			int columnNumber = 0;
			final JnjGTVariantProductModel deliveryVariant = getDeliveryGTIN(productModel);
			final JnjGTVariantProductModel salesVariant = getSalesGTIN(productModel);
			if (productIndex == productsPerSheet)
			{
				if (excelWorkBook.getSheetAt(sheetIndex) != null)
				{
					productIndex = 0;
					sheetIndex = sheetIndex + 1;
					sheet = excelWorkBook.createSheet("MDD_Products_Sheet_" + sheetIndex);
					rowNumber = 0;
					createHeader(rowNumber, sheet, style);
				}
			}
			final HSSFRow row = sheet.createRow(++rowNumber);
			if (Integer.valueOf(Config.getParameter("test.limit.for.MDD.catalog.export")).intValue() > 0)
			{
				LOGGER.info("Total number of records processed: " + rowNumber + Logging.HYPHEN + "for the method" + METHOD_NAME
						+ " out of " + Logging.HYPHEN + products.size());
			}
			createCell(jnJGTMDDProductExportService.getName(productModel), row, columnNumber++, sheet);
			createCell(jnJGTMDDProductExportService.getFranchiseName(productModel), row, columnNumber++, sheet);
			createCell(jnJGTMDDProductExportService.getDivisionName(productModel), row, columnNumber++, sheet);
			createCell(jnJGTMDDProductExportService.getProductCode(productModel), row, columnNumber++, sheet);
			String eachGtin = null;
			String caseGtin = null;
			if (null != deliveryVariant)
			{
				caseGtin = deliveryVariant.getEan();
			}
			if (null != salesVariant)
			{
				eachGtin = salesVariant.getEan();
			}
			createCell(eachGtin, row, columnNumber++, sheet);
			createCell(caseGtin, row, columnNumber++, sheet);


			/** Set Delivery UOM ONLY when Delivery and Sales UOM are NOT null **/
			if (null != deliveryVariant && null != salesVariant)
			{
				jnJGTMDDProductExportService.setDeliveryUOM(deliveryVariant, salesVariant, stringBuilder);
				createCell(stringBuilder.toString(), row, columnNumber++, sheet);
				stringBuilder.setLength(0);
				jnJGTMDDProductExportService.setUOMOfEaches(deliveryVariant, salesVariant, stringBuilder);
				createCell(stringBuilder.toString(), row, columnNumber++, sheet);//not sure about the UOM of eaches, need confirmation
				stringBuilder.setLength(0);
			}
			else
			{
				columnNumber = +2;
			}

			createCell(jnJGTMDDProductExportService.getProductDescription(productModel), row, columnNumber++, sheet);

			final String downloadKit = JnJCommonUtil.getValue("catalog.export.mdd.kit");
			Map<String, List<String>> kitMap = new HashMap<String, List<String>>();
			if (StringUtils.equals("true", downloadKit))
			{
				kitMap = jnJGTMDDProductExportService.getKitDetails(productModel);
			}
			final List<String> kitNames = kitMap.get("kitComponentNames");
			final List<String> kitDescriptions = kitMap.get("kitComponentDesc");
			final List<String> kitUnits = kitMap.get("kitComponentUnits");

			if (kitNames != null && !kitNames.isEmpty())
			{
				for (final String kitName : kitNames)
				{
					if (stringBuilder.length() > 32767)
					{
						break;
					}
					stringBuilder.append(kitName);
					stringBuilder.append(seperator);

				}
			}
			createCell(stringBuilder.toString(), row, columnNumber++, sheet);
			stringBuilder.setLength(0);

			if (kitDescriptions != null && !kitDescriptions.isEmpty())
			{
				for (final String kitDescription : kitDescriptions)
				{
					if (stringBuilder.length() > 32767)
					{
						break;
					}
					stringBuilder.append(kitDescription);
					stringBuilder.append(seperator);
				}
			}
			createCell(stringBuilder.toString(), row, columnNumber++, sheet);
			stringBuilder.setLength(0);

			if (kitUnits != null && !kitUnits.isEmpty())
			{
				for (final String kitUnit : kitUnits)
				{
					if (stringBuilder.length() > 32767)
					{
						break;
					}
					stringBuilder.append(kitUnit);
					stringBuilder.append(seperator);
				}
			}
			createCell(stringBuilder.toString(), row, columnNumber++, sheet);
			stringBuilder.setLength(0);
			if (null != deliveryVariant)
			{

				final String linearUomName = (deliveryVariant.getLinearUom() != null) ? ((deliveryVariant.getLinearUom().getName() != null) ? deliveryVariant
						.getLinearUom().getName() : deliveryVariant.getLinearUom().getCode())
						: "";

				final String weightLinearUomName = (deliveryVariant.getWeightUom() != null) ? deliveryVariant.getWeightUom()
						.getCode() : "";
				String volumeLinerUom = "";
				String volumeLUomSuperscript = "";
				createCell((deliveryVariant.getDepth() == null) ? "" : deliveryVariant.getDepth().toString() + " " + linearUomName,
						row, columnNumber++, sheet);
				createCell((deliveryVariant.getHeight() == null) ? "" : deliveryVariant.getHeight().toString() + " " + linearUomName,
						row, columnNumber++, sheet);
				createCell((deliveryVariant.getWidth() == null) ? "" : deliveryVariant.getWidth().toString() + " " + linearUomName,
						row, columnNumber++, sheet);
				if (deliveryVariant.getVolumeUom() != null)
				{
					if (StringUtils.length(deliveryVariant.getVolumeUom().getCode()) > 1)
					{
						try
						{
							volumeLUomSuperscript = deliveryVariant.getVolumeUom().getCode()
									.substring(deliveryVariant.getVolumeUom().getCode().length() - 1);
							volumeLinerUom = deliveryVariant.getVolumeUom().getCode()
									.substring(0, deliveryVariant.getVolumeUom().getCode().length() - 1);
						}
						catch (final NumberFormatException numberFormatException)
						{
							volumeLUomSuperscript = "";
							volumeLinerUom = deliveryVariant.getVolumeUom().getCode();
						}
					}
					else
					{
						volumeLUomSuperscript = "";
						volumeLinerUom = deliveryVariant.getVolumeUom().getCode();
					}

				}

				createCell((deliveryVariant.getVolumeQty() == null) ? null : deliveryVariant.getVolumeQty().toString() + " "
						+ volumeLinerUom + " " + volumeLUomSuperscript, row, columnNumber++, sheet);

				createCell((deliveryVariant.getWeightQty() == null) ? null : deliveryVariant.getWeightQty().toString() + " "
						+ weightLinearUomName, row, columnNumber++, sheet);


			}
			productIndex++;
		}
		writeExcelfile(filepath, excelWorkBook);

	}


	protected void createHeader(final int rowIndxOfHeader, final HSSFSheet sheet, final HSSFCellStyle style)
	{
		final HSSFRow header = sheet.createRow(rowIndxOfHeader);

		header.createCell(0).setCellValue("Name");

		header.createCell(1).setCellValue("Franchise");

		header.createCell(2).setCellValue("Division");

		header.createCell(3).setCellValue("Product Code");

		header.createCell(4).setCellValue("Each GTIN");

		header.createCell(5).setCellValue("Case GTIN");

		header.createCell(6).setCellValue("Delivery UOM");

		header.createCell(7).setCellValue("UOM of Eaches");

		header.createCell(8).setCellValue("Description");

		header.createCell(9).setCellValue("Kit Component Name");

		header.createCell(10).setCellValue("Kit Component Description");

		header.createCell(11).setCellValue("Kit Component Unit");

		header.createCell(12).setCellValue("Depth");

		header.createCell(13).setCellValue("Height");

		header.createCell(14).setCellValue("Width");

		header.createCell(15).setCellValue("Volume");

		header.createCell(16).setCellValue("Weight");
		//Apply the specified style to all the cells in the header
		for (final Iterator iterator = header.cellIterator(); iterator.hasNext();)
		{
			final HSSFCell cell = (HSSFCell) iterator.next();
			cell.setCellStyle(style);
		}
	}

	protected void writeExcelfile(final String filePath, final HSSFWorkbook excelWorkBook)
	{
		final File excelFile = new File(filePath);
		FileOutputStream outputStream = null;
		try
		{
			outputStream = new FileOutputStream(excelFile);
			excelWorkBook.write(outputStream);
		}
		catch (final FileNotFoundException exception)
		{
			LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
		}
		catch (final IOException exception)
		{
			LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
		}
		finally
		{
			try
			{
				outputStream.flush();
				outputStream.close();
			}
			catch (final IOException exception)
			{
				LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
			}
		}
	}

	/**
	 * This method is used to write the value in the cells by resolving there type.
	 * 
	 * @param object
	 * @param row
	 * @param cellNumber
	 */
	protected void createCell(final Object object, final HSSFRow row, final int cellNumber, final HSSFSheet sheet)
	{

		if (object != null)
		{
			if (object instanceof String && !StringUtils.equalsIgnoreCase(((String) object).trim(), "null"))
			{
				row.createCell(cellNumber).setCellValue((String) object);
			}
			else if (object instanceof Date)
			{
				row.createCell(cellNumber).setCellValue((Date) object);
			}
			else if (object instanceof Double)
			{
				row.createCell(cellNumber).setCellValue(((Double) object).doubleValue());
			}
			else if (object instanceof Integer)
			{
				row.createCell(cellNumber).setCellValue(((Integer) object).intValue());
			}
			else
			{
				row.createCell(cellNumber).setCellValue(" ");
			}
		}
		else
		{
			row.createCell(cellNumber).setCellValue(" ");
		}
		sheet.autoSizeColumn(cellNumber);
	}


	@Override
	public void deleteOldMDDExportFile(final String filePath)
	{
		final File[] files = (new File(filePath)).listFiles();
		Arrays.sort(files, new Comparator<File>()
		{
			@Override
			public int compare(final File f1, final File f2)
			{
				return Long.valueOf(f2.lastModified()).compareTo(Long.valueOf(f1.lastModified()));
			}
		});
		int fileCount = 0;
		for (int index = 0; index < files.length; index++)
		{
			if (files[index].getName().contains(Jnjb2bCoreConstants.MDD))
			{
				fileCount++;
				if (fileCount > 2)
				{
					if (files[index].delete())
					{
						LOGGER.info("Old MDD Export File deleted Successfully");
					}
				}
			}
		}
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public List<JnJProductModel> getAllProductsForPCM(final CatalogModel catalogModel)
	{
		return jnjGTProductDao.getAllProductsForPCM(catalogModel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnJGTProductService#getMDDProductByValue(java.lang.String)
	 */
	@Override
	public ProductModel getMDDDeliveryVariantByProdCode(final String code)
	{
		return jnjGTProductDao.getProductByPartialValue(code, true, "MDD", false);

	}

	/**
	 * To get the MDD description in case the name is empty.
	 */

	@Override
	public String getProductName(final JnJProductModel JnJProductModel)
	{
		String name = null;
		if (sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME).toString().equalsIgnoreCase(Jnjb2bCoreConstants.CONS))
		{
			if (StringUtils.isNotEmpty(JnJProductModel.getName()))
			{
				name = JnJProductModel.getName();
			}
			else if (StringUtils.isNotEmpty(JnJProductModel.getMdmDescription()))
			{
				name = JnJProductModel.getMdmDescription();
			}
		}
		else
		{
			name = JnJProductModel.getName();
		}
		return name;
	}

	@Override
	public String getLaunchStatus(JnJProductModel product, Boolean onlyActive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mandatoryAttributesCheck(JnJProductModel productModel) {
		// TODO Auto-generated method stub
		return false;
	}

	/*@Override
	public CompletenessStatusData getCompletenessValueIndicator(
			JnJProductModel productModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean mandatoryAttributesCheck(JnJProductModel productModel) {
		// TODO Auto-generated method stub
		return false;
	}*/

	
	/**
	 * 
	 * This is to get the media url for mdd catalog export file.
	 * 
	 * @return mediaUrl
	 * 
	 */
	@Override
	public String getMediaURLForMDDExport()
	{
		MediaModel mediaModel = modelService.create(MediaModel.class);
		mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
		if (sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME).toString().equalsIgnoreCase(Jnjb2bCoreConstants.CONS))
		{
			mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CANADA_CONSUMER_CATALOG_ID,
					Jnjb2bCoreConstants.ONLINE));
		}
		else
		{
		mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID,
				Jnjb2bCoreConstants.ONLINE));
		}
		mediaModel.setCode(Jnjb2bCoreConstants.MDD_EXPORT_FILE_MEDIA_KEY);
		try
		{
			final MediaModel existingMedia = flexibleSearchService.getModelByExample(mediaModel);
			if (existingMedia != null)
			{
				return existingMedia.getURL();
			}
		}
		catch (final Exception exception)
		{
			LOGGER.info("Media Not Found");
		}
		return null;
	}
	
	@Override
	public String getMediaURLForPDFMDDExport()
	{
		MediaModel mediaModel = modelService.create(MediaModel.class);
		mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
		if (sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME).toString().equalsIgnoreCase(Jnjb2bCoreConstants.CONS))
		{
			mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CANADA_CONSUMER_CATALOG_ID,
					Jnjb2bCoreConstants.ONLINE));
		}
	
		else
		{
		mediaModel.setCatalogVersion(catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID,
				Jnjb2bCoreConstants.ONLINE));
		}
		mediaModel.setCode(Jnjb2bCoreConstants.MDD_PDF_EXPORT_FILE_MEDIA_KEY);
		try
		{
			final MediaModel existingMedia = flexibleSearchService.getModelByExample(mediaModel);
			if (existingMedia != null)
			{
				return existingMedia.getURL();
			}
		}
		catch (final Exception exception)
		{
			LOGGER.info("Media Not Found");
		}
		return null;
	}
	
	@Override
	public boolean validateProductMfgForHouseOrder(final JnJProductModel product)
	{
		 
		return false;
	}

	public StringBuilder getObsoleteProductList(String[] selectedProductIds) {
		StringBuilder strProductList = new StringBuilder();

		try {
			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.MDD_CATALOG_ID, Jnjb2bCoreConstants.ONLINE);
			for (String productId : selectedProductIds) {
				boolean flag =  isObsoleteProduct(productId, catalogVersionModel);
				if(flag){
					strProductList.append(productId).append(",");
				}
			}
		} catch (UnknownIdentifierException e) {
			LOGGER.error("Unable to find catalogVersionModel", e);
		}

		return strProductList;
	}

	public boolean isObsoleteProduct(String code, CatalogVersionModel catalogVersionModel) {
		boolean isObsoleteProduct = false;
		final JnJProductModel jnjProductModel = getProductModelByCode(code, catalogVersionModel);
		if(jnjProductModel!= null && JnjGTModStatus.OBSOLETE.equals(jnjProductModel.getModStatus()))
		{
			isObsoleteProduct = true;
		}
		return isObsoleteProduct;
	}

	
	
	/*@Override
	public boolean validateProductMfgForHouseOrder(final JnJProductModel product)
	{
		boolean isValidProductForHouse = true;
		// validate division
		if (null != product && userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = ((JnJB2bCustomerModel) userService.getCurrentUser());
			if (AccessBy.WWID.equals(currentUser.getAccessBy()) && StringUtils.isNotEmpty(currentUser.getConsumerDivison()))
			{
				final List<String> userDivisions = Arrays.asList(currentUser.getConsumerDivison().split(","));
				if (StringUtils.isNotEmpty(product.getManufacturerAID()) && userDivisions.contains(product.getManufacturerAID()))
				{
					isValidProductForHouse = true;
				}
				else
				{
					isValidProductForHouse = false;
				}
			}
		}
		return isValidProductForHouse;
	}*/

	@Override
	public List<JnJProductModel> getProductsBoughtTogether(final String productCode, final CatalogVersionModel catalogVersion) {

		final int carouselQuantity = jnjCommonUtil.getInt(PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY, PRODUCTS_BOUGHT_TOGETHER_CAROUSEL_QUANTITY_DEFAULT);
		final int calendarBlocksQuantity = jnjCommonUtil.getInt(PRODUCTS_BOUGHT_TOGETHER_CALENDAR_PERIOD, PRODUCTS_BOUGHT_TOGETHER_CALENDAR_PERIOD_DEFAULT);

		final Interval<Calendar> dates = new CalendarBlockUtils(calendarBlocksQuantity).getPreviousBlockFromNow();
		final List<String> allowedFranchisesCode = getAllowedFranchiseCodeList();

		String productPK = findProductPK(productCode, catalogVersion);
		if (StringUtils.isEmpty(productPK)) {
			return new ArrayList<>();
		}

		return jnjGTProductDao.getProductsBoughtTogether(allowedFranchisesCode, productPK, dates, carouselQuantity);
	}
	protected List<String> getAllowedFranchiseCodeList() {
		final String allowedFranchises = sessionService.getAttribute(ALLOWED_FRANCHISE);
		LOGGER.debug("Allowed Franchises: " + allowedFranchises);
		return JnJCsvUtil.toListRemovingQuotes(allowedFranchises);
	}
	@Override
	public String findProductPK(final String productCode, final CatalogVersionModel catalogVersion) {
		final JnJProductModel product = getProductModelByCode(productCode, catalogVersion);

		if (product == null || product.getPk() == null) {
			return null;
		}

		return product.getPk().toString();
	}

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}
	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
}