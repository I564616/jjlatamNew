package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJGTProductKitModel;
import com.jnj.core.model.JnJGTProductSalesOrgModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductPlantModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Product;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnJGTIntProductPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductKitModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductSalesOrgModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * The Mapper class responsible to process all <code>JnjGTIntProductModel</code> records, and its assoacited
 * intermediate records to create or Update MDD or Consumer specific <code>JnJProductModel</code>. Logic includes
 * processing, mapping and establishing relationships of <code>JnjGTIntProductModel</code> with associations.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTProductMasterMapper extends JnjAbstractMapper
{
	/**  */
	private static final String CONSUMER_PRODUCT_WORKFLOW_DISABLE_SWTICH = "consumer.product.workflow.disable.swtich";

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTProductMasterMapper.class);

	/**
	 * Private instance of <code>jnjGTFeedService</code>
	 */
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * Private instance of <code>jnjGTProductFeedService</code>
	 */
	private JnjGTProductFeedService jnjGTProductFeedService;

	/**
	 * Private instance of <code>CatalogVersionService</code>
	 */
	private CatalogVersionService catalogVersionService;

	/**
	 * Private instance of <code>CategoryService</code>
	 */
	private CategoryService categoryService;

	/**
	 * <code>CatalogVersionModel</code> instance for MDD catalog.
	 */
	private CatalogVersionModel mddCatalogVersion;

	/**
	 * <code>CatalogVersionModel</code> instance for Consumer USA catalog.
	 */
	private CatalogVersionModel consumerUsCatalogVersion;

	/**
	 * <code>CatalogVersionModel</code> instance for Consumer CANADA catalog.
	 */
	private CatalogVersionModel consumerCaCatalogVersion;

	/**
	 * <code>CategoryModel</code> instance for MDD Root Category.
	 */
	private CategoryModel mddRootCategory;

	/**
	 * Allowed Principal groups for MDD categories.
	 */
	private List<PrincipalModel> mddAllowedPrincipals;

	/**
	 * <code>CategoryModel</code> instance for MDD Default Category.
	 */
	private CategoryModel mddDefaultCategory;

	/**
	 * Allowed Principal groups for Consumer categories.
	 */
	private List<PrincipalModel> consumerAllowedPrincipals;

	/**
	 * <code>CategoryModel</code> instance for Root Category of Consumer USA.
	 */
	private CategoryModel consumerUSRootCategory;

	/**
	 * <code>CategoryModel</code> instance for Root Category of Consumer Canada Catalog.
	 */
	private CategoryModel consumerCaRootCategory;

	/**
	 * <code>CategoryModel</code> instance for Default Category of Consumer USA catalog.
	 */
	private CategoryModel consumerUSDefaultCategory;

	/**
	 * <code>CategoryModel</code> instance for Default Category of Consumer Canada catalog.
	 */
	private CategoryModel consumerCaDefaultCategory;

	/**
	 * Holds greatest packaging level code value used for determining Delivery GTIN.
	 */
	private int delGtinGreatestPkglvlCode = 0;

	/**
	 * Holds greatest packaging level code value used for determining Sales GTIN.
	 */
	private int salesGtinGreatestPkglvlCode = 0;

	/**
	 *
	 */
	private boolean upcCodeFound = false;

	/**
	 *
	 */
	private String upcCode = null;

	/**
	 * Constant Error message script for no Catalog found exception.
	 */
	private static final String NO_CATALOG_FOUND_ERROR_MESSAGE = "No Catalog found for the Stage version with the Catalog ID: ";

	// Fetching all the status code value from the config files.
	private static final List<String> notApplicableCodeList = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.NOT_APPLICABLE_STATUS_CODE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> inActiveCodeList = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.INACTIVE_STATUS_CODE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> activeCodeList = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	/**
	 * Flag to drive whether MDD INtermediate records to be filtered based on USA OTC Ready Indicator or not.
	 */
	private boolean filterUsaOtcEnabledRecords = true;

	@Resource(name = "productService")
	private JnJGTProductService jnjGTProductService;

	/**
	 * Sets the catalog versions and Root category, and Processes over all product intermediate records in two phase:
	 * 
	 * <ul>
	 * <li>1. Process all Base Products, i.e. having MOD code NULL (for MDD) or '00' (for Consumer).</li>
	 * <li>2. Process all Mod based products, i.e. having MOD value.</li>
	 * </ul>
	 */
	@Override
	public void processIntermediateRecords()
	{
		try
		{
			filterUsaOtcEnabledRecords = (Config.getParameter(Product.MDD_FILTER_USA_OTC_READY_RECORDS_FLAG_KEY) != null) ? Boolean
					.valueOf(Config.getParameter(Product.MDD_FILTER_USA_OTC_READY_RECORDS_FLAG_KEY)).booleanValue() : false;

			LOGGER.debug("START - MDD - set cat version & Root cat");
			getMddCatalogVersionAndRootCategory();
			LOGGER.debug("END - MDD - set cat version & Root cat");

			LOGGER.debug("START - usa - set cat version & Root cat");
			getConsumerUsaCatalogVersionAndRootCategory();
			LOGGER.debug("END - usa - set cat version & Root cat");
			//getConsumerCanadaCatalogVersionAndRootCategory();

			//To get Products with mod_code = 00 (For consumer) and mod_code=null (in case of MDD)
			final Collection<JnjGTIntProductModel> intBaseProductRecords = getjnjGTProductFeedService().getIntMaterialBaseProducts(
					true);
			LOGGER.debug("get all int BaseProductRecords..." + intBaseProductRecords);
			LOGGER.debug("and its size..." + intBaseProductRecords.size());

			LOGGER.debug("START - processIntermediateProductRecords for BASE");
			processIntermediateProductRecords(intBaseProductRecords);
			LOGGER.debug("END - processIntermediateProductRecords for BASE");

			//To get Products with mod_code != 00 (For consumer) and mod_code !=null (in case of MDD)
			final Collection<JnjGTIntProductModel> intModProductRecords = getjnjGTProductFeedService().getIntMaterialBaseProducts(
					false);
			LOGGER.debug("get all int BaseProductRecords..." + intBaseProductRecords);
			LOGGER.debug("and its size..." + intBaseProductRecords.size());

			LOGGER.debug("START - processIntermediateProductRecords for MOD");
			processIntermediateProductRecords(intModProductRecords);
			LOGGER.debug("END - processIntermediateProductRecords for MOD");

			//final Collection<JnjGTIntProductModel> intCanadaProductRecords = getjnjGTProductFeedService().getIntCanadaProducts();
			//processIntermediateProductRecords(intCanadaProductRecords);
		}
		catch (final BusinessException exception)
		{
			LOGGER.error(exception.getMessage());
		}
	}

	/**
	 * Fetches the MDD product Catalog version from the active store front along with the root Category.
	 * 
	 * @throws BusinessException
	 *            : If either MDD and Consumer Catalog versions are not found in the active storefront, or Multiple Root
	 *            category is found.
	 */
	private void getMddCatalogVersionAndRootCategory() throws BusinessException
	{
		mddCatalogVersion = getCatalogVersionService().getCatalogVersion(Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
		LOGGER.debug("mddCatalogVersion....." + mddCatalogVersion);
		try
		{
			mddRootCategory = getCategoryService().getCategoryForCode(mddCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
			LOGGER.debug("mddRootCategory......" + mddRootCategory);

			mddDefaultCategory = getCategoryService().getCategoryForCode(mddCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.DEFAULT_CATEGORY_ID);
			LOGGER.debug("mddDefaultCategory.." + mddDefaultCategory);

			mddAllowedPrincipals = mddRootCategory.getAllowedPrincipals();
			LOGGER.debug("consumerAllowedPrincipals....." + mddAllowedPrincipals);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			exception.printStackTrace();
			throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
	}

	/**
	 * Fetches the Consumer USA product Catalog version from the active store front along with the root Category.
	 * 
	 * @throws BusinessException
	 *            : If either MDD and Consumer Catalog versions are found in the active storefront, or Multiple Root
	 *            category is found.
	 */
	private void getConsumerUsaCatalogVersionAndRootCategory() throws BusinessException
	{
		consumerUsCatalogVersion = getCatalogVersionService().getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
		LOGGER.debug("consumerUsCatalogVersion....." + consumerUsCatalogVersion);
		try
		{
			consumerUSRootCategory = getCategoryService().getCategoryForCode(consumerUsCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
			LOGGER.debug("consumerUSRootCategory......" + consumerUSRootCategory);

			consumerUSDefaultCategory = getCategoryService().getCategoryForCode(consumerUsCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.DEFAULT_CATEGORY_ID);
			LOGGER.debug("consumerUSDefaultCategory.." + consumerUSDefaultCategory);

			consumerAllowedPrincipals = consumerUSRootCategory.getAllowedPrincipals();
			LOGGER.debug("consumerAllowedPrincipals....." + consumerAllowedPrincipals);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			exception.printStackTrace();
			throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
	}

	/**
	 * Fetches the Consumer Canada product Catalog version from the active store front along with the root Category.
	 * 
	 * @throws BusinessException
	 *            : If either MDD and Consumer Catalog versions are found in the active storefront, or Multiple Root
	 *            category is found.
	 */
	private void getConsumerCanadaCatalogVersionAndRootCategory() throws BusinessException
	{
		consumerCaCatalogVersion = getCatalogVersionService().getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.CONSUMER_CANADA_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
		try
		{
			consumerCaRootCategory = getCategoryService().getCategoryForCode(consumerCaCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);

			consumerCaDefaultCategory = getCategoryService().getCategoryForCode(consumerCaCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.DEFAULT_CATEGORY_ID);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			throw new BusinessException(NO_CATALOG_FOUND_ERROR_MESSAGE + Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
	}

	/**
	 * Processes <code>JnjGTIntProductModel</code> records and persist them after processing as new or updated
	 * <code>JnJProductModel</code>.
	 */
	private void processIntermediateProductRecords(final Collection<JnjGTIntProductModel> records)
	{
		for (final JnjGTIntProductModel intProductModel : records)
		{
			try
			{
				final String sourceSystemId = JnjGTInboundUtil.fetchValidSourceSysId(intProductModel.getSrcSystem());
				LOGGER.debug("sourceSystemId..............." + sourceSystemId);
				final CatalogVersionModel catalogVersionModel = JnjGTSourceSysId.MDD.toString().equals(sourceSystemId) ? mddCatalogVersion
						: (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSystemId) ? consumerUsCatalogVersion
								: consumerCaCatalogVersion);
				JnJProductModel productModel = getjnjGTProductFeedService().getProductByCode(intProductModel.getMaterialNum(),
						catalogVersionModel);
				LOGGER.info("PRODUCT Intermediate Record for the Material number: " + intProductModel.getMaterialNum()
						+ " is going to process.");

				/***
				 * Update approval status if: 1. It's an existing MDD based record, 2. USA OTC ready filter flag is not
				 * setFilter. 3. doesn't have USA_OTC_READY_IND field set.
				 ***/
				LOGGER.debug("Product -> Source system id is " + sourceSystemId);
				LOGGER.debug("USA OTC Enabled?" + intProductModel.getUsaOtcReadyInd());
				if (JnjGTSourceSysId.MDD.toString().equals(sourceSystemId)
						&& (intProductModel.getUsaOtcReadyInd() == null || !intProductModel.getUsaOtcReadyInd().booleanValue()))
				{
					LOGGER.debug("filterUsaOtcEnabledRecords.............." + filterUsaOtcEnabledRecords);
					if (!filterUsaOtcEnabledRecords && productModel != null
							&& !ArticleApprovalStatus.UNAPPROVED.equals(productModel.getApprovalStatus()))
					{
						LOGGER.info("PRODUCT CODE: " + productModel.getCode()
								+ " | Setting Approval Status to UNAPPROVED for product and its variants");
						setProductAndVariantsUnApproved(productModel);
					}
					LOGGER.debug("EXITING PRODUCT....." + intProductModel.getMaterialNum());
					continue;
				}
				//Check if product does not exist for given material number , create new product
				if (productModel == null)
				{
					LOGGER.debug("Product does not exist.. Creating new product for " + intProductModel.getMaterialNum());
					productModel = getjnjGTProductFeedService().createNewItem(JnJProductModel.class);
					productModel.setCode(intProductModel.getMaterialNum());
				}
				processProductDetails(productModel, intProductModel, sourceSystemId);
				LOGGER.debug("JnJCommonUtil.getValue(CONSUMER_PRODUCT_WORKFLOW_DISABLE_SWTICH)........."
						+ JnJCommonUtil.getValue(CONSUMER_PRODUCT_WORKFLOW_DISABLE_SWTICH));
				if (!Boolean.valueOf(JnJCommonUtil.getValue(CONSUMER_PRODUCT_WORKFLOW_DISABLE_SWTICH)))
				{
					final String productCatalog = null;
					boolean processProduct = true;
					/*
					 * if (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSystemId)) { productCatalog =
					 * JnJCommonUtil.getValue(JnjPCMCoreConstants.ZONZA.PRODUCTS_CATALOG_US); } else if
					 * (JnjGTSourceSysId.CONSUMER_CAN.toString().equals(sourceSystemId)) { productCatalog =
					 * JnJCommonUtil.getValue(JnjPCMCoreConstants.ZONZA.PRODUCTS_CATALOG_CA); }
					 */
					final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(productCatalog, "Online");
					List<JnJProductModel> JnJProductModels = null;
					try
					{
						JnJProductModels = jnjGTProductService.getProductModelByExactUpcCode(productModel.getUpcCode(), catalogVersion);
						LOGGER.debug("prod " + productModel.getCode() + " in online cat......" + JnJProductModels);
					}
					catch (final UnknownIdentifierException exception)
					{
						LOGGER.info("No product found in the Online catalog for the Product UPC Code: "
								+ intProductModel.getMaterialNum() + " in the Catalog." + catalogVersion.getCatalog().getId());
						processProduct = true;
					}
					catch (final IllegalArgumentException exception)
					{
						LOGGER.info("Illegal argument passed for the method  getProductModelByExactUpcCode()"
								+ intProductModel.getMaterialNum() + " in the Catalog." + catalogVersion.getCatalog().getId());
						processProduct = false;
					}
					LOGGER.debug("processProduct........" + processProduct);
					if (processProduct)
					{
						final boolean newProductWorkflow = CollectionUtils.isEmpty(JnJProductModels);
						jnjGTProductService.createProductWorkflow(productModel, newProductWorkflow);
						LOGGER.debug("CREATE PROD WORK FLOW");
					}
				}
				LOGGER.info("SUCCESS: PRODUCT Intermediate Record for the Product SKU Code: " + intProductModel.getMaterialNum()
						+ " has been persisted successfully.");
				getjnjGTFeedService().updateIntermediateRecord(intProductModel, RecordStatus.SUCCESS, false, null);
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Exception occured while processing PRODUCT Intermediate Records for the Product SKU Code: "
						+ intProductModel.getMaterialNum() + ". Exception Message: " + exception + "\n Updating WRITE Dashboard.",
						exception);
				getjnjGTFeedService().updateIntermediateRecord(intProductModel, RecordStatus.PENDING, true, exception.getMessage());
			}
			catch (final Exception exception)
			{
				LOGGER.error("Exception occured while processing PRODUCT Intermediate Records for the Product SKU Code: "
						+ intProductModel.getMaterialNum() + ". Exception Message: " + exception + "\n Updating WRITE Dashboard.",
						exception);
				getjnjGTFeedService().updateIntermediateRecord(intProductModel, RecordStatus.PENDING, true, exception.getMessage());
			}
		}
	}

	/**
	 * Processes CONSUMER and MDD products common details:
	 * 
	 * <ul>
	 * <li>Base Code</li>
	 * <li>Base UoM</li>
	 * <li>Description</li>
	 * <li>Region Details (MDD)</li>
	 * <li>Plant Details (CONS)</li>
	 * <li>Kit Details (MDD)</li>
	 * <li>Region Details</li>
	 * <li>Units of Measurement Variants</li>
	 * </ul>
	 * 
	 * @param productModel
	 * @param intProductModel
	 * @throws BusinessException
	 */
	private void processProductDetails(final JnJProductModel productModel, final JnjGTIntProductModel intProductModel,
			final String sourceSystemId) throws BusinessException
	{
		String errorMessage = null;
		productModel.setSalesOrgCode(intProductModel.getSalesOrgCode());
		if (JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.Product.JNJ_SALES_ORG_CODE,
				Jnjgtb2binboundserviceConstants.COMMA_STRING).contains(productModel.getSalesOrgCode()))
		{
			productModel.setJnjPortalInd(Boolean.TRUE);
		}
		else
		{
			productModel.setJnjPortalInd(intProductModel.getJnjPortalInd());
		}
		productModel.setProductCode(intProductModel.getProductCode());
		productModel.setSourceSystemId(intProductModel.getSrcSystem());
		productModel.setRecordTimeStamp(parseDate(intProductModel.getRecordTimeStamp()));

		if (JnjGTSourceSysId.MDD.toString().equals(sourceSystemId))
		{
			LOGGER.debug("START - Processing MDD flow......");
			productModel.setApprovalStatus(ArticleApprovalStatus.APPROVED);
			LOGGER.debug("START - Processing MDD - Prod Details flow......");
			processMddProductDetails(productModel, intProductModel);
			LOGGER.debug("END - Processing MDD - Prod Details flow......");
			LOGGER.debug("START - Processing MDD - Prod Kit flow......");
			populateMddProductKitDetails(productModel);
			LOGGER.debug("END - Processing MDD - Prod Kit flow......");
			LOGGER.debug("START - Processing MDD - Prod Region flow......");
			populateMddProductRegDetails(productModel);
			LOGGER.debug("END - Processing MDD - Prod Region flow......");
			LOGGER.debug("START - Processing MDD - Prod Desc flow......");
			populateMddProductDescDetails(productModel);
			LOGGER.debug("END - Processing MDD - Prod Desc flow......");
			LOGGER.debug("START - Processing MDD - Prod Unit flow......");
			processProductUnitsOfMeasurement(productModel, intProductModel.getSrcSystem(), null, true, false);
			LOGGER.debug("END - Processing MDD - Prod Unit flow......");
			LOGGER.debug("END - Processing MDD flow......");
		}
		else if (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSystemId)
				|| JnjGTSourceSysId.CONSUMER_CAN.toString().equals(sourceSystemId))
		{
			LOGGER.debug("START - Processing Consumer flow......");
			if (!Boolean.valueOf(JnJCommonUtil.getValue(CONSUMER_PRODUCT_WORKFLOW_DISABLE_SWTICH)).booleanValue())
			{
				productModel.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
			}
			else
			{
				productModel.setApprovalStatus(ArticleApprovalStatus.APPROVED);
			}
			final boolean isConsumerUSProduct = (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSystemId)) ? true : false;
			LOGGER.debug("START - Processing Consumer - Prod details flow......");
			processConsumerProductDetails(productModel, intProductModel, isConsumerUSProduct);
			LOGGER.debug("END - Processing Consumer - Prod details flow......");
			LOGGER.debug("START - Processing Consumer - Prod Desc flow......");
			populateConsumerProductDescDetails(productModel);
			LOGGER.debug("END - Processing Consumer - Prod Desc flow......");
			LOGGER.debug("START - Processing Consumer - Plant details flow......");
			setConsumerProductPlantDetails(productModel);
			LOGGER.debug("END - Processing Consumer - Plant details flow......");
			LOGGER.debug("START - Processing Consumer - Salesorg flow......");
			setConsumerProductSalesOrg(productModel);
			LOGGER.debug("END - Processing Consumer - Salesorg flow......");
			LOGGER.debug("START - Processing Consumer -UOM flow......");
			processProductUnitsOfMeasurement(productModel, intProductModel.getSrcSystem(), intProductModel.getBaseUom(), false,
					isConsumerUSProduct);
			LOGGER.debug("END - Processing Consumer -UOM flow......");
			/*
			 * productModel.setSourceSystemId((isConsumerUSProduct) ?
			 * Config.getParameter(Product.SOURCE_SYS_NAME_MAPPING_US_KEY) :
			 * Config.getParameter(Product.SOURCE_SYS_NAME_MAPPING_CA_KEY));
			 */
			LOGGER.debug("END - Processing Consumer flow......");
		}
		else
		{
			errorMessage = "There exists NO such SOURCE SYSTEM ID with value: " + intProductModel.getSrcSystem()
					+ ". Intermediate record for product with SKU code: " + intProductModel.getMaterialNum() + " cannot be processed."
					+ productModel.getCode();

			throw new BusinessException(errorMessage);
		}

		getjnjGTProductFeedService().saveItem(productModel);
	}

	/**
	 * Parses the date based on the configurable Format scpecified.
	 * 
	 * @param date
	 */
	private Date parseDate(final String date)
	{
		Date parsedDate = null;
		if (date != null)
		{
			final String dateFormat = Config.getParameter(Product.DATE_FORMAT_KEY);
			final DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
			try
			{
				parsedDate = dateFormatter.parse(date);
			}
			catch (final ParseException exception)
			{
				LOGGER.error("Exception while parsing Record timestamp: " + exception.getMessage());
			}
		}
		return parsedDate;
	}

	/**
	 * Sets the consumer product status by using the product model, intermediate product model and call from base
	 * product.
	 * 
	 * @param productModel
	 *           the product model
	 * @param jnjGTIntProductModel
	 *           the jnj na int product model
	 * @throws BusinessException
	 * 
	 */
	private void setConsumerProductStatus(final JnJProductModel productModel, final JnjGTIntProductModel jnjGTIntProductModel)
			throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setConsumerProductStatus()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final String productStatus = jnjGTIntProductModel.getProductStatus();
		if (StringUtils.isNotEmpty(productStatus))
		{

			if (notApplicableCodeList.contains(productStatus))
			{
				productModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
				productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
			else if (activeCodeList.contains(productStatus) && null != jnjGTIntProductModel.getFirstShipEffectDate())
			{
				productModel.setModStatus(JnjGTModStatus.ACTIVE);
				productModel.setPcmModStatus(JnjGTModStatus.ACTIVE);
			}
			else if (inActiveCodeList.contains(productStatus) && null != jnjGTIntProductModel.getMaterialStatusEffectDate())
			{
				final Calendar cal = Calendar.getInstance();
				final Calendar inComingCalendar = Calendar.getInstance();
				inComingCalendar.setTime(jnjGTIntProductModel.getMaterialStatusEffectDate());
				cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1));
				// If the first ship effective date lies after 365 days then the product has 'not applicable' status.
				if (inComingCalendar.compareTo(cal) < 0)
				{
					productModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
					productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				}
				else
				{
					productModel.setModStatus(JnjGTModStatus.DISCONTINUED);
					productModel.setPcmModStatus(JnjGTModStatus.DISCONTINUED);
				}
			}
			else
			{
				productModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
				productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
			if (BooleanUtils.isTrue(productModel.getKitInd()))
			{
				productModel.setPcmInd(Boolean.FALSE);
				productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
			//CR changes Start.
			if (null == productModel.getMaterialBaseProduct())
			{
				if (BooleanUtils.isTrue(productModel.getPublishInd()))
				{
					productModel.setTempPublisInd(Boolean.TRUE);
				}
			}
			else
			{
				if (BooleanUtils.isTrue(productModel.getPublishInd())
						&& BooleanUtils.isFalse(productModel.getMaterialBaseProduct().getTempPublisInd()))
				{
					final JnJProductModel JnJProductModel = productModel.getMaterialBaseProduct();
					JnJProductModel.setTempPublisInd(Boolean.TRUE);
					getjnjGTProductFeedService().saveItem(JnJProductModel);
				}
			}
			//CR changes End.
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setConsumerProductStatus()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Sets the consumer pcm product status in product model by checking the active or inactive code list.
	 * 
	 * @param productModel
	 *           the product model
	 * @param jnjGTIntProductModel
	 *           the jnj na int product model
	 * 
	 */
	private void setConsumerProductStatusForCanadaPrd(final JnJProductModel productModel,
			final JnjGTIntProductModel jnjGTIntProductModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setConsumerProductStatusForCanadaPrd()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final String productStatus = jnjGTIntProductModel.getProductStatus();
		if (StringUtils.isNotEmpty(productStatus))
		{
			if (inActiveCodeList.contains(productStatus))
			{
				productModel.setPcmModStatus(JnjGTModStatus.DISCONTINUED);
				productModel.setProductStatus(JnjGTModStatus.DISCONTINUED);
			}
			else if (activeCodeList.contains(productStatus))
			{
				productModel.setPcmModStatus(JnjGTModStatus.ACTIVE);
				productModel.setProductStatus(JnjGTModStatus.ACTIVE);
			}
			else
			{
				productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				productModel.setProductStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
			// if the publish indicator is false or the kit indicator is true then set the portal indicator as false else set it as true.
			if (BooleanUtils.isFalse(jnjGTIntProductModel.getPublishInd()) || BooleanUtils.isTrue(jnjGTIntProductModel.getKitInd()))
			{
				productModel.setPcmInd(Boolean.FALSE);
				productModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setConsumerProductStatusForCanadaPrd()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Processes MDD specific product details:
	 * 
	 * <ul>
	 * <li>Product Status</li>
	 * <li>Kit Details</li>
	 * <li>Region Details</li>
	 * <li>Description</li>
	 * </ul>
	 * 
	 * @param productModel
	 * @param intProductModel
	 * @throws BusinessException
	 */
	private void processMddProductDetails(final JnJProductModel productModel, final JnjGTIntProductModel intProductModel)
			throws BusinessException
	{
		productModel.setCatalogVersion(mddCatalogVersion);
		productModel.setGhexInd(intProductModel.getGhexInd());
		productModel.setHazmatCode(intProductModel.getHazmatCode());
		productModel.setFrancMjrPrdGrpCd(intProductModel.getFrancMjrPrdGrpCd());
		productModel.setDescription(intProductModel.getECatalogLongDesciption());
		productModel.setSummary(intProductModel.getECatalogDesciption());
		//following properties has been set for EMEA requirement.
		productModel.setMaterialBaseNum(intProductModel.getMaterialBaseNum());
		//productModel.setMajorGroupCode(intProductModel.getMajorGroupCode());
		//productModel.setMajorGroupName(intProductModel.getMajorGroupName());
		//productModel.setSubFranchiseCode(intProductModel.getSubFranchiseCode());
		//productModel.setSubFranchiseName(intProductModel.getSubFranchiseName());
		//productModel.setProductDesc2(intProductModel.getProductDesc2());

		// Set Material Base Product ONLY for those products which themselves are NOT entitled to be a MATERIAL BASE
		// PRODUCT, i.e. for them Material Num does NOT match with Product code.
		if (!intProductModel.getMaterialNum().equals(intProductModel.getProductCode()))
		{
			final JnJProductModel mddBaseProduct = getMDDMaterialBaseProduct(intProductModel.getProductCode());
			if (mddBaseProduct == null)
			{
				LOGGER.error("MDD PRODUCT WITH MATERIAL NUM: " + productModel.getCode()
						+ ". COULDN'T FIND BASE PRODUCT WITH PRODUCT CODE: " + intProductModel.getProductCode()
						+ " AND CATALOG VERSION: " + mddCatalogVersion);
			}
			else
			{
				productModel.setMaterialBaseProduct(mddBaseProduct);
			}
		}
		getjnjGTProductFeedService().saveItem(productModel);
	}

	/**
	 * Provides Material Base Product for a MDD product based on the <code>JnjGTIntProductModel.PRODUCTCODE</code>.
	 * 
	 * @param productCode
	 * @return JnJProductModel
	 * @throws BusinessException
	 */
	private JnJProductModel getMDDMaterialBaseProduct(final String productCode) throws BusinessException
	{
		return getjnjGTProductFeedService().getProductByCode(productCode, mddCatalogVersion);
	}

	/**
	 * Sets and assign <code>JnJProductModel.supercategories</code> to the product based on the two levels of category
	 * codes received.
	 * 
	 * @param rootCategory
	 * @param levelOneCategoryCode
	 * @param levelTwoCategoryCode
	 * @param catalogVersion
	 * @param jnjGTProduct
	 * @throws BusinessException
	 */
	private void setConsProductCategory(final CategoryModel rootCategory, final String levelOneCategoryCode,
			final String levelTwoCategoryCode, final String levelOneCategoryName, final String levelTwoCategoryName,
			final CatalogVersionModel catalogVersion, final JnJProductModel jnjGTProduct) throws BusinessException
	{
		CategoryModel levelOneCategoryModel = null;
		CategoryModel levelTwoCategoryModel = null;
		final Set<CategoryModel> productSuperCategory = new HashSet();

		try
		{
			/*** If EPIC/PCM CONS Product: Fetch Level 1 category by Custom Name i.e. brand name. ***/
			levelOneCategoryModel = getjnjGTProductFeedService().getcategoryByCustomName(catalogVersion, levelOneCategoryName);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level one Category model is already existed having code value: " + levelOneCategoryModel.getCode()
						+ " and name is " + levelOneCategoryModel.getName());
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			final List<CategoryModel> rootSuperCategory = new ArrayList<>();
			rootSuperCategory.add(rootCategory);
			levelOneCategoryModel = getjnjGTProductFeedService().createNewItem(CategoryModel.class);
			levelOneCategoryModel.setCode(Product.CONSUMER_LEVEL_1_CATEG_PREFIX + levelOneCategoryCode);
			levelOneCategoryModel.setCatalogVersion(catalogVersion);
			levelOneCategoryModel.setSupercategories(rootSuperCategory);
			levelOneCategoryModel.setName(levelOneCategoryName);
			levelOneCategoryModel.setAllowedPrincipals(consumerAllowedPrincipals);
			levelOneCategoryModel.setDisplayProducts(Boolean.TRUE);
			levelOneCategoryModel.setJnjCustomName(levelOneCategoryName);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level One Category model is not existed in hybris.");
			}
		}
		catch (final AmbiguousIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}
		try
		{
			/***
			 * If EPIC/PCM CONS Product: Fetch level 2 category by code based on sub-brand and brand name i.e.
			 * 'brandName_sub-brandName'
			 ***/
			levelTwoCategoryModel = getjnjGTProductFeedService().getcategoryByCustomName(catalogVersion,
					levelOneCategoryName + Jnjb2bCoreConstants.SYMBOL_UNDERSCORE + levelTwoCategoryName);

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level two Category model is already existed having code value: " + levelTwoCategoryModel.getCode()
						+ " and name is " + levelTwoCategoryModel.getName());
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			final List<CategoryModel> superCategory = new ArrayList<>();
			superCategory.add(levelOneCategoryModel);
			levelTwoCategoryModel = getjnjGTProductFeedService().createNewItem(CategoryModel.class);
			levelTwoCategoryModel.setCode(Product.CONSUMER_LEVEL_2_CATEG_PREFIX + levelTwoCategoryCode);
			levelTwoCategoryModel.setCatalogVersion(catalogVersion);
			levelTwoCategoryModel.setName(levelTwoCategoryName);
			levelTwoCategoryModel.setAllowedPrincipals(consumerAllowedPrincipals);
			levelTwoCategoryModel.setSupercategories(superCategory);
			levelTwoCategoryModel.setDisplayProducts(Boolean.TRUE);
			levelTwoCategoryModel.setJnjCustomName(levelOneCategoryName + Jnjb2bCoreConstants.SYMBOL_UNDERSCORE
					+ levelTwoCategoryName);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level Two Category model is not existed in hybris.");
			}
		}
		catch (final AmbiguousIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}
		jnjGTProduct.setFirstLevelCategory(levelOneCategoryModel.getCode());
		jnjGTProduct.setSecondLevelCategory(levelTwoCategoryModel.getCode());


		if (jnjGTProduct.getSupercategories() != null)
		{
			productSuperCategory.addAll(jnjGTProduct.getSupercategories());
		}
		productSuperCategory.add(levelTwoCategoryModel);
		jnjGTProduct.setSupercategories(productSuperCategory);

		getjnjGTProductFeedService().saveItem(levelOneCategoryModel);
		getjnjGTProductFeedService().saveItem(levelTwoCategoryModel);
	}

	/**
	 * Sets and assign <code>JnJProductModel.supercategories</code> to the product based on the two levels of category
	 * codes received.
	 * 
	 * @param rootCategory
	 * @param levelOneCategoryCode
	 * @param levelTwoCategoryCode
	 * @param catalogVersion
	 * @param jnjGTProduct
	 * @throws BusinessException
	 */
	private void setMddProductCategory(final CategoryModel rootCategory, final String levelOneCategoryCode,
			final String levelTwoCategoryCode, final String levelOneCategoryName, final String levelTwoCategoryName,
			final JnJProductModel jnjGTProduct) throws BusinessException
	{
		CategoryModel levelOneCategoryModel = null;
		CategoryModel levelTwoCategoryModel = null;
		final Set<CategoryModel> productSuperCategory = new HashSet();

		/***
		 * Provide level 1/2 prefix while building the category system for the product to avoid discrepancies and issues
		 * when category codes are similar in MDD/CONS.
		 ***/
		try
		{
			levelOneCategoryModel = getCategoryService().getCategoryForCode(mddCatalogVersion,
					Product.MDD_LEVEL_1_CATEG_PREFIX + levelOneCategoryCode);

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level one Category model is already existed having code value: " + levelOneCategoryModel.getCode()
						+ " and name is " + levelOneCategoryModel.getName());
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			final List<CategoryModel> rootSuperCategory = new ArrayList<>();
			rootSuperCategory.add(rootCategory);
			levelOneCategoryModel = getjnjGTProductFeedService().createNewItem(CategoryModel.class);
			levelOneCategoryModel.setCode(Product.MDD_LEVEL_1_CATEG_PREFIX + levelOneCategoryCode);
			levelOneCategoryModel.setCatalogVersion(mddCatalogVersion);
			levelOneCategoryModel.setSupercategories(rootSuperCategory);
			levelOneCategoryModel.setName(levelOneCategoryName);
			levelOneCategoryModel.setAllowedPrincipals(mddAllowedPrincipals);
			levelOneCategoryModel.setDisplayProducts(Boolean.TRUE);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level One Category model is not existed in hybris.");
			}
		}
		catch (final AmbiguousIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}

		try
		{
			/*** If EPIC/PCM CONS Product: fetch category based on name i.e. sub-brand name. ***/
			levelTwoCategoryModel = getCategoryService().getCategoryForCode(mddCatalogVersion,
					Product.MDD_LEVEL_2_CATEG_PREFIX + levelTwoCategoryCode);

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level two Category model is already existed having code value: " + levelTwoCategoryModel.getCode()
						+ " and name is " + levelTwoCategoryModel.getName());
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			final List<CategoryModel> superCategory = new ArrayList<>();
			superCategory.add(levelOneCategoryModel);
			levelTwoCategoryModel = getjnjGTProductFeedService().createNewItem(CategoryModel.class);
			levelTwoCategoryModel.setCode(Product.MDD_LEVEL_2_CATEG_PREFIX + levelTwoCategoryCode);
			levelTwoCategoryModel.setCatalogVersion(mddCatalogVersion);
			levelTwoCategoryModel.setName(levelTwoCategoryName);
			levelTwoCategoryModel.setAllowedPrincipals(mddAllowedPrincipals);
			levelTwoCategoryModel.setSupercategories(superCategory);
			levelTwoCategoryModel.setDisplayProducts(Boolean.TRUE);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductCategory()" + Logging.HYPHEN
						+ "level Two Category model is not existed in hybris.");
			}
		}
		catch (final AmbiguousIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}

		if (jnjGTProduct.getSupercategories() != null)
		{
			productSuperCategory.addAll(jnjGTProduct.getSupercategories());
		}
		productSuperCategory.add(levelTwoCategoryModel);
		jnjGTProduct.setSupercategories(productSuperCategory);

		getjnjGTProductFeedService().saveItem(levelOneCategoryModel);
		getjnjGTProductFeedService().saveItem(levelTwoCategoryModel);
	}

	/**
	 * Sets default category for a product when either level 1 or level 2 categories is absent.
	 * 
	 * @param productModel
	 * @param defaultCategory
	 */
	private void setDefaultCategory(final JnJProductModel productModel, final CategoryModel defaultCategory)
	{
		final Set<CategoryModel> productSuperCategory = new HashSet();
		if (productModel.getSupercategories() != null)
		{
			productSuperCategory.addAll(productModel.getSupercategories());
		}
		productSuperCategory.add(defaultCategory);
		productModel.setSupercategories(productSuperCategory);

		try
		{
			getjnjGTProductFeedService().saveItem(productModel);
		}
		catch (final BusinessException exception)
		{
			LOGGER.error("EXCEPTION WHILE SETTING DEFAULT CATEGORY FOR PRODUCT CODE: " + productModel.getCode() + ". Exception: "
					+ exception.getMessage());
		}
	}

	/**
	 * Populates MDD Product description details.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	private void populateMddProductDescDetails(final JnJProductModel productModel) throws BusinessException
	{
		final Collection<JnjGTIntProductDescModel> jnaIntProductDescModels = getjnjGTProductFeedService()
				.getIntProductDescRecordsByProductSkuCode(JnjGTIntProductDescModel._TYPECODE, productModel.getCode(),
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(jnaIntProductDescModels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT DESCRIPTION, WITH PRODUCT SKU CODE: " + productModel.getCode());
		}
		else
		{
			final JnjGTIntProductDescModel jnjGTIntProductDescModel = (JnjGTIntProductDescModel) jnaIntProductDescModels.toArray()[0];
			final String description = jnjGTIntProductDescModel.getProductDesc();
			final String languageCode = jnjGTIntProductDescModel.getLanguageCode();
			if (languageCode != null)
			{
				final Locale local = Locale.of(languageCode);
				productModel.setName(description, local);
				//added for EMEA integration
				//productModel.setProductDesc2(jnjGTIntProductDescModel.getProdDesc2());
			}
			else
			{
				productModel.setName(description);
			}
		}
	}


	/**
	 * Populates MDD Product KIT details.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	public void populateMddProductKitDetails(final JnJProductModel productModel) throws BusinessException
	{
		final String productCode = productModel.getCode();
		String productKitCode = null;

		final Collection<JnjGTIntProductKitModel> jnaIntproductKitDetails = getjnjGTProductFeedService()
				.getIntProductKitRecordsByProductSkuCode(JnjGTIntProductKitModel._TYPECODE, productCode,
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(jnaIntproductKitDetails))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT KIT, FOR PRODUCT WITH SKU CODE: " + productModel.getCode());
		}
		else
		{
			final Set<JnJGTProductKitModel> existingProductKits = productModel.getProductKits();
			final Set<JnJGTProductKitModel> updatedProductKits = new HashSet<>();
			final Set<JnJGTProductKitModel> newProductKits = new HashSet<>();
			final Set<JnJGTProductKitModel> obsoleteProductKits = new HashSet<>();

			for (final JnjGTIntProductKitModel jnjGTIntProductKitModel : jnaIntproductKitDetails)
			{
				productKitCode = productCode + "|" + jnjGTIntProductKitModel.getComponentCode();
				boolean found = false;
				for (final JnJGTProductKitModel jnjGTProductKitModel : existingProductKits)
				{
					if (jnjGTProductKitModel.getCode().equals(productKitCode))
					{
						found = true;
						// Updating existing product kit model
						populateProductKitModel(jnjGTIntProductKitModel, jnjGTProductKitModel);
						// adding existing which comes with feed to product kit list
						updatedProductKits.add(jnjGTProductKitModel);
						break;
					}
				}
				if (!found)
				{
					final JnJGTProductKitModel newProductKitModel = getjnjGTProductFeedService().createNewItem(
							JnJGTProductKitModel.class);

					newProductKitModel.setCode(productKitCode);
					newProductKitModel.setComponentCode(jnjGTIntProductKitModel.getComponentCode());
					// populating new product kit model
					populateProductKitModel(jnjGTIntProductKitModel, newProductKitModel);
					// adding new product kit model to list.
					newProductKits.add(newProductKitModel);
				}
			}

			//Remove all updated records from existing collection, add remaining i.e. not being used in the Obsolete collection and combine updated
			//and new records in a single collection to be saved.
			//TODO ERROR UNSUPPORTED OPERATION - existingProductKits.removeAll(updatedProductKits);
			obsoleteProductKits.addAll(existingProductKits);
			updatedProductKits.addAll(newProductKits);

			try
			{
				getjnjGTProductFeedService().saveItems(updatedProductKits);
				productModel.setProductKits(updatedProductKits);
				getjnjGTProductFeedService().saveItem(productModel);
				getjnjGTProductFeedService().removeItems(obsoleteProductKits);
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Saving/Removal of Product Sales Records have caused an exception.");
				throw exception;
			}
			finally
			{
				//TODO UNSUPPORT ERROR
				/*
				 * existingProductKits.clear(); newProductKits.clear(); obsoleteProductKits.clear();
				 */
			}
		}
	}

	/**
	 * Populates MDD Product Region details.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	public void populateMddProductRegDetails(final JnJProductModel productModel) throws BusinessException
	{
		final Collection<JnjGTIntProductRegModel> jnjGTIntProductRegModels = getjnjGTProductFeedService()
				.getIntProductRegRecordsByProductSkuCode(JnjGTIntProductRegModel._TYPECODE, productModel.getCode(),
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(jnjGTIntProductRegModels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT REGION, FOR PRODUCT WITH SKU CODE: " + productModel.getCode());
		}
		else
		{
			final JnjGTIntProductRegModel intProductRegModel = (JnjGTIntProductRegModel) jnjGTIntProductRegModels.toArray()[0];
			productModel.setRegionCode(intProductRegModel.getRegionCode());

			if (intProductRegModel.getSalesOrgCompCde() != null && intProductRegModel.getSalesOrgDivision() != null)
			{
				/*** Send level 1 & 2 category names same as that of code value in case of MDD ***/
				productModel.setFirstLevelCategory(Product.MDD_LEVEL_1_CATEG_PREFIX + intProductRegModel.getSalesOrgCompCde());
				productModel.setSecondLevelCategory(Product.MDD_LEVEL_2_CATEG_PREFIX + intProductRegModel.getSalesOrgDivision());
				setMddProductCategory(mddRootCategory, intProductRegModel.getSalesOrgCompCde(),
						intProductRegModel.getSalesOrgDivision(), intProductRegModel.getSalesOrgCompCde(),
						intProductRegModel.getSalesOrgDivision(), productModel);
			}
			else
			{
				LOGGER.info("EITHER LEVEL 1 OR LEVEL 2 CATEGORY CODE FROM REGION IS NULL- SalesOrgCompCode: "
						+ intProductRegModel.getSalesOrgCompCde() + " | SalesOrgDivision: " + intProductRegModel.getSalesOrgDivision()
						+ ". PLACING PRODUCT UNDER DEFAULT CATEGORY");

				setDefaultCategory(productModel, mddDefaultCategory);
			}
		}
	}

	/**
	 * Processes Consumer product specific details:
	 * 
	 * <ul>
	 * <li>Product Status</li>
	 * <li>Manufacturer Code</li>
	 * <li>Approval Status</li>
	 * <li>Plant Details</li>
	 * <li>Sales Org Details</li>
	 * </ul>
	 * 
	 * @param productModel
	 * @param intProductModel
	 * @throws BusinessException
	 */
	public void processConsumerProductDetails(final JnJProductModel productModel, final JnjGTIntProductModel intProductModel,
			final boolean isConsumerUSProduct) throws BusinessException
	{
		final String statusCode = (intProductModel.getProductStatus() != null) ? intProductModel.getProductStatus().trim() : null;
		final CatalogVersionModel catalogVersion = isConsumerUSProduct ? consumerUsCatalogVersion : consumerCaCatalogVersion;
		final CategoryModel rootCategory = isConsumerUSProduct ? consumerUSRootCategory : consumerCaRootCategory;
		final CategoryModel defaultCategory = isConsumerUSProduct ? consumerUSDefaultCategory : consumerCaDefaultCategory;

		productModel.setCatalogVersion(catalogVersion);
		productModel.setProductStatusCode(statusCode);
		productModel.setManufacturerAID(intProductModel.getMfgCompCode());
		productModel.setDeaRegInd(Product.DEA_REG_IND_VALUE.equals(intProductModel.getDeaRegInd()) ? Boolean.TRUE : Boolean.FALSE);
		//productModel.setOnlineDate(intProductModel.getFirstShipEffectDate());
		productModel.setFranchiseName(intProductModel.getFranchiseName());
		productModel.setPublishInd(intProductModel.getPublishInd());
		productModel.setFirstShipEffectDate(intProductModel.getFirstShipEffectDate());

		productModel.setKitInd(intProductModel.getKitInd());
		productModel.setGlobalBusinessUnit(intProductModel.getGlobalBusinessUnit());
		productModel.setGlobalProductCode(intProductModel.getGlobalProductCode());
		productModel.setKitInd(intProductModel.getKitInd());
		productModel.setNewProductStartDate(intProductModel.getNewProductStartDate());
		productModel.setMaterialStatusEffectDate(intProductModel.getMaterialStatusEffectDate());
		productModel.setNetContent(intProductModel.getNetContent());
		productModel.setMaterialBaseNum(intProductModel.getMaterialBaseNum());

		if (isConsumerUSProduct && intProductModel.getBrandCode() != null && intProductModel.getSubBrandCode() != null)
		{


			setConsProductCategory(rootCategory, intProductModel.getBrandCode(), intProductModel.getSubBrandCode(),
					intProductModel.getBrandName(), intProductModel.getSubBrandName(), catalogVersion, productModel);
		}
		else if (!isConsumerUSProduct && CollectionUtils.isEmpty(productModel.getSupercategories()))
		{
			LOGGER.info("PLACING PRODUCT UNDER DEFAULT CATEGORY BECAUSE EITHER: - IT IS A CANADA PRODUCT | OR, CONSUMER US PRODUCT - LEVEL 1/LEVEL 2"
					+ " CATEGORY CODE IS NULL- Brand Code: "
					+ intProductModel.getBrandCode()
					+ " | Sub Brand Code: "
					+ intProductModel.getSubBrandCode());
			setDefaultCategory(productModel, defaultCategory);
		}

		/**
		 * Set Material Base Product ONLY for those products which themselves are NOT entitled to be a MATERIAL BASE
		 * PRODUCT, i.e. Material Num is NOT equal to Material Base Num.
		 */
		if (!intProductModel.getMaterialNum().equals(intProductModel.getMaterialBaseNum()))
		{
			final JnJProductModel baseProduct = getConsumerMaterialBaseProduct(intProductModel.getMaterialBaseNum(), catalogVersion);

			if (baseProduct == null)
			{
				LOGGER.error("CONSUMER PRODUCT WITH MATERIAL NUM: " + productModel.getCode()
						+ ". COULDN'T FIND BASE PRODUCT WITH MATERIAL BASE NUM: " + intProductModel.getMaterialBaseNum()
						+ " AND CATALOG VERSION: " + catalogVersion);
			}
			else
			{
				productModel.setMaterialBaseProduct(baseProduct);
			}
		}

		if (isConsumerUSProduct)
		{
			// set the product status of all the consumer products.
			setProductStatus(productModel);
			setConsumerProductStatus(productModel, intProductModel);
		}
		else
		{
			setConsumerProductStatusForCanadaPrd(productModel, intProductModel);
		}

		getjnjGTProductFeedService().saveItem(productModel);
	}

	/**
	 * Sets the product status of the products and their siblings.
	 * 
	 */
	private void setProductStatus(final JnJProductModel productModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isProductStatusSet = false;
		boolean setInactiveStatus = true;

		// Enter inside if block if the material base product is not null
		if (null != productModel.getMaterialBaseProduct())
		{
			// Product Status is set in product model.
			productModel.setProductStatus(productModel.getMaterialBaseProduct().getProductStatus());
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
						+ "Inside if block when the material base product is not null");
			}
		}
		else if (null == productModel.getMaterialBaseProduct() && !productModel.getMaterialBaseNum().equals(productModel.getCode()))
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
						+ "Inside if block when the material base product is null && material base number is not equal to product code");
			}
			if (activeCodeList.contains(productModel.getProductStatusCode()))
			{
				productModel.setProductStatus(JnjGTModStatus.ACTIVE);
			}
			else if (inActiveCodeList.contains(productModel.getProductStatusCode()))
			{
				productModel.setProductStatus(JnjGTModStatus.DISCONTINUED);
			}
			else
			{
				productModel.setProductStatus(JnjGTModStatus.NOTAPPLICABLE);
			}
		}
		else
		{

			final List<String> productStatusCodeList = jnjGTProductFeedService.getProductCodesUsingIntProductModel(productModel
					.getMaterialBaseNum());
			// Iterate on active code list and check if the product status code is existed in this list if yes then set the material status active.
			for (final String activeCode : activeCodeList)
			{
				if (productStatusCodeList.contains(activeCode))
				{
					productModel.setProductStatus(JnjGTModStatus.ACTIVE);
					isProductStatusSet = true;
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
								+ "Inside if active code list block.");
					}
					break;
				}
			}
			// if the active status is not status the enter inside if block.
			if (!isProductStatusSet)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
							+ "Inside if InActive code list block.");
				}
				for (final String productStatus : productStatusCodeList)
				{
					if (!inActiveCodeList.contains(productStatus))
					{
						setInactiveStatus = false;
						break;
					}
				}
			}
			if (!isProductStatusSet && setInactiveStatus)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
							+ "Inside if block to set status as discontinued.");
				}
				productModel.setProductStatus(JnjGTModStatus.DISCONTINUED);
			}
			if (!isProductStatusSet && !setInactiveStatus)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
							+ "Inside if block to set status as not applicable.");
				}
				productModel.setProductStatus(JnjGTModStatus.NOTAPPLICABLE);
			}

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN
						+ "Inside if block when the material base product is null");
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "setProductStatus()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Provides Material Base Product for a CONSUMER product based on the
	 * <code>JnjGTIntProductModel.MATERIALBASENUM</code>.
	 * 
	 * @param materialBaseNum
	 * @return JnJProductModel
	 * @throws BusinessException
	 */
	private JnJProductModel getConsumerMaterialBaseProduct(final String materialBaseNum, final CatalogVersionModel catalogVersion)
			throws BusinessException
	{
		return getjnjGTProductFeedService().getProductByCode(materialBaseNum, catalogVersion);
	}


	/**
	 * Populates Plant details for a Consumer product.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	public void setConsumerProductPlantDetails(final JnJProductModel productModel) throws BusinessException
	{
		final Collection<JnJGTIntProductPlantModel> intProductPlantNodels = getjnjGTProductFeedService()
				.getIntProductPlantRecordsByProductSkuCode(JnJGTIntProductPlantModel._TYPECODE, productModel.getCode(),
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(intProductPlantNodels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT PLANT, FOR PRODUCT WITH SKU CODE: " + productModel.getCode());
		}
		else
		{
			final String productCode = productModel.getCode();
			boolean productPlantExists = false;
			String productPlantCode = null;

			final Set<JnjGTProductPlantModel> existingPlantRecords = new HashSet<JnjGTProductPlantModel>();
			existingPlantRecords.addAll(productModel.getProductPlants());

			final Set<JnjGTProductPlantModel> updatedPlantRecords = new HashSet<JnjGTProductPlantModel>();
			final Set<JnjGTProductPlantModel> newPlantRecords = new HashSet<JnjGTProductPlantModel>();
			final Set<JnjGTProductPlantModel> obsoletePlantRecords = new HashSet<JnjGTProductPlantModel>();

			for (final JnJGTIntProductPlantModel intProductPlantModel : intProductPlantNodels)
			{
				productPlantExists = false;
				productPlantCode = productCode + Jnjgtb2binboundserviceConstants.PIPE_STRING + intProductPlantModel.getProdPlnt();

				/*** Iterate and update existing plant records associated with product. ***/
				for (final JnjGTProductPlantModel existingProductPlantModel : existingPlantRecords)
				{
					if (existingProductPlantModel.getCode() != null && existingProductPlantModel.getCode().equals(productPlantCode))
					{
						productPlantExists = true;
						populateProductPlantDetails(intProductPlantModel, existingProductPlantModel);
						updatedPlantRecords.add(existingProductPlantModel);
						break;
					}
				}

				/*** If not found, update existing plant record which is NOT associated with product. ***/
				if (!productPlantExists && getjnjGTProductFeedService().getExistingProductPlantRecord(productPlantCode) != null)
				{
					final JnjGTProductPlantModel existingIndependentProdPlantModel = getjnjGTProductFeedService()
							.getExistingProductPlantRecord(productPlantCode);
					productPlantExists = true;
					populateProductPlantDetails(intProductPlantModel, existingIndependentProdPlantModel);
					updatedPlantRecords.add(existingIndependentProdPlantModel);
				}

				/*** If none of the product plant records exists with the productPlantCode, create new one. ***/
				if (!productPlantExists)
				{
					final JnjGTProductPlantModel newProductPlantModel = getjnjGTProductFeedService().createNewItem(
							JnjGTProductPlantModel.class);
					newProductPlantModel.setCode(productPlantCode);
					populateProductPlantDetails(intProductPlantModel, newProductPlantModel);
					newPlantRecords.add(newProductPlantModel);
				}
			}
			existingPlantRecords.removeAll(updatedPlantRecords);
			obsoletePlantRecords.addAll(existingPlantRecords);
			updatedPlantRecords.addAll(newPlantRecords);
			try
			{
				getjnjGTProductFeedService().removeItems(obsoletePlantRecords);
				LOGGER.info("OBSOLETE PLANT RECORDS: Have been REMOVED successfully for product code: " + productCode);

				getjnjGTProductFeedService().saveItems(updatedPlantRecords);
				LOGGER.info("NEW/UPDATED PLANT RECORDS: Have been SAVED successfully for product code: " + productCode);

				productModel.setProductPlants(updatedPlantRecords);
				getjnjGTProductFeedService().saveItem(productModel);
				LOGGER.info("NEW/UPDATED PLANT RECORDS: Have been SAVED successfully in the product with code: " + productCode);
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("EXCEPTION WHILE PROCESSING PLANT RECORDS FOR PRODUCT WITH SKU CODE: " + productModel.getCode()
						+ ". EXCEPTION: " + exception.getMessage());
			}
			finally
			{
				existingPlantRecords.clear();
				newPlantRecords.clear();
				obsoletePlantRecords.clear();
			}
		}
	}

	/**
	 * Populate Product Plant details.
	 * 
	 * @param intProductPlantModel
	 * @param productPlantModel
	 */
	private void populateProductPlantDetails(final JnJGTIntProductPlantModel intProductPlantModel,
			final JnjGTProductPlantModel productPlantModel)
	{
		productPlantModel.setProdPlnt(intProductPlantModel.getProdPlnt());
		if (intProductPlantModel.getCountryOriginCode() != null)
		{
			productPlantModel.setCountry(getCountryModel(intProductPlantModel.getCountryOriginCode(),
					intProductPlantModel.getCountryOriginName()));
		}
	}

	/**
	 * Populates Consumer Product description details.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	private void populateConsumerProductDescDetails(final JnJProductModel productModel) throws BusinessException
	{
		final Collection<JnjGTIntProductDescModel> jnaIntProductDescModels = getjnjGTProductFeedService()
				.getIntProductDescRecordsByProductSkuCode(JnjGTIntProductDescModel._TYPECODE, productModel.getCode(),
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(jnaIntProductDescModels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT DESCRIPTION, WITH PRODUCT SKU CODE: " + productModel.getCode());
		}
		else
		{
			//final JnjGTIntProductDescModel jnjGTIntProductDescModel = (JnjGTIntProductDescModel) jnaIntProductDescModels;
			String description = null;
			String languageCode = null;

			for (final JnjGTIntProductDescModel jnjGTIntProductDescModel : jnaIntProductDescModels)
			{
				description = jnjGTIntProductDescModel.getProductDesc();
				languageCode = jnjGTIntProductDescModel.getLanguageCode();

				if (languageCode != null)
				{
					final Locale local = Locale.of(languageCode);
					productModel.setMdmDescription(description, local);
				}
				else
				{
					productModel.setMdmDescription(description);
				}
			}
		}
	}

	/**
	 * Populates Sales Org details for a Consumer product.
	 * 
	 * @param productModel
	 * @throws BusinessException
	 */
	public void setConsumerProductSalesOrg(final JnJProductModel productModel) throws BusinessException
	{
		final Set<JnJGTProductSalesOrgModel> existingSalesOrgs = new HashSet<JnJGTProductSalesOrgModel>();
		existingSalesOrgs.addAll(productModel.getProductSalesOrg());

		final Set<JnJGTProductSalesOrgModel> updatedSalesOrgs = new HashSet<JnJGTProductSalesOrgModel>();
		final Set<JnJGTProductSalesOrgModel> newSalesOrgs = new HashSet<JnJGTProductSalesOrgModel>();
		final Set<JnJGTProductSalesOrgModel> obsoleteSalesOrg = new HashSet<JnJGTProductSalesOrgModel>();

		final String productCode = productModel.getCode();
		String productSalesOrgCode = null;

		boolean salesOrgExists = false;
		final Collection<JnjGTIntProductSalesOrgModel> intProductSalesOrgModels = getjnjGTProductFeedService()
				.getIntProductSalesOrgRecordsByProductSkuCode(JnjGTIntProductSalesOrgModel._TYPECODE, productCode,
						productModel.getSourceSystemId());

		if (CollectionUtils.isEmpty(intProductSalesOrgModels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT SALES ORG, FOR PRODUCT WITH SKU CODE: " + productCode);
		}
		else
		{
			for (final JnjGTIntProductSalesOrgModel intProductSalesOrgModel : intProductSalesOrgModels)
			{
				salesOrgExists = false;
				productSalesOrgCode = (productCode + Jnjgtb2binboundserviceConstants.PIPE_STRING
						+ intProductSalesOrgModel.getSalesOrg() + Jnjgtb2binboundserviceConstants.PIPE_STRING + intProductSalesOrgModel
						.getDistChannel());

				/*** Iterate and update existing sales org records associated with product. ***/
				for (final JnJGTProductSalesOrgModel existingProductSalesOrgModel : existingSalesOrgs)
				{
					if (existingProductSalesOrgModel.getCode() != null
							&& existingProductSalesOrgModel.getCode().equalsIgnoreCase(productSalesOrgCode))
					{
						salesOrgExists = true;
						populateProductSalesOrg(intProductSalesOrgModel, existingProductSalesOrgModel);
						updatedSalesOrgs.add(existingProductSalesOrgModel);
						break;
					}
				}

				/*** If not found, update existing sales org record which is NOT associated with product. ***/
				if (!salesOrgExists && getjnjGTProductFeedService().getExistingProductSalesOrgRecord(productSalesOrgCode) != null)
				{
					final JnJGTProductSalesOrgModel existingIndependentSalesOrgModel = getjnjGTProductFeedService()
							.getExistingProductSalesOrgRecord(productSalesOrgCode);
					if (null != existingIndependentSalesOrgModel)
					{
						salesOrgExists = true;
						populateProductSalesOrg(intProductSalesOrgModel, existingIndependentSalesOrgModel);
						updatedSalesOrgs.add(existingIndependentSalesOrgModel);
					}
				}

				/*** If none of the product sales org records exists with the productPlantCode, create new one. ***/
				if (!salesOrgExists)
				{
					final JnJGTProductSalesOrgModel newProductSalesOrgModel = getjnjGTProductFeedService().createNewItem(
							JnJGTProductSalesOrgModel.class);
					newProductSalesOrgModel.setCode(productSalesOrgCode);
					populateProductSalesOrg(intProductSalesOrgModel, newProductSalesOrgModel);
					newSalesOrgs.add(newProductSalesOrgModel);
				}
			}

			existingSalesOrgs.removeAll(updatedSalesOrgs);
			obsoleteSalesOrg.addAll(existingSalesOrgs);
			updatedSalesOrgs.addAll(newSalesOrgs);

			try
			{
				getjnjGTProductFeedService().removeItems(obsoleteSalesOrg);
				LOGGER.info("OBSOLETE SALES-ORG RECORDS: Have been REMOVED successfully for product code: " + productCode);

				getjnjGTProductFeedService().saveItems(updatedSalesOrgs);
				LOGGER.info("NEW/UPDATED SALES-ORG RECORDS: Have been SAVED successfully for product code: " + productCode);

				productModel.setProductSalesOrg(updatedSalesOrgs);
				getjnjGTProductFeedService().saveItem(productModel);
				LOGGER.info("NEW/UPDATED SALES-ORG RECORDS: Have been SAVED successfully in the product with code: " + productCode);
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Exception while processing Sales org records, Saving/Removal of Product Sales Records have caused an exception: "
						+ exception.getMessage());
			}
			finally
			{
				existingSalesOrgs.clear();
				newSalesOrgs.clear();
				obsoleteSalesOrg.clear();
			}
		}
	}

	/**
	 * 
	 * @param intProductSalesOrgModel
	 * @param productSalesOrgModel
	 */
	public void populateProductSalesOrg(final JnjGTIntProductSalesOrgModel intProductSalesOrgModel,
			final JnJGTProductSalesOrgModel productSalesOrgModel)
	{
		productSalesOrgModel.setSalesOrgCode(intProductSalesOrgModel.getSalesOrg());
		productSalesOrgModel.setDistChannel(intProductSalesOrgModel.getDistChannel());
		productSalesOrgModel.setMaterialNum(intProductSalesOrgModel.getProductSkuCode());
	}


	/**
	 * Populates KIT details for a MDD product.
	 * 
	 * @param intProductKitModel
	 * @param productKitModel
	 * @throws BusinessException
	 */
	private void populateProductKitModel(final JnjGTIntProductKitModel intProductKitModel,
			final JnJGTProductKitModel productKitModel) throws BusinessException
	{
		try
		{
			productKitModel.setComponentQty((intProductKitModel.getComponentQty() != null) ? Double.valueOf(intProductKitModel
					.getComponentQty()) : Double.valueOf(0));
			productKitModel.setFeatureQty(Integer.valueOf(intProductKitModel.getKitFeatureQty()));

		}
		catch (final NumberFormatException e)
		{
			throw new BusinessException(e.getMessage());
		}

		productKitModel.setFeatureCode(intProductKitModel.getKitFeatureCode());
		productKitModel.setFeatureName(intProductKitModel.getKitFeatureName());
	}


	/**
	 * Processes Unit of Measurements details and converts them in form of Product Variants for a base product.
	 * 
	 * @param productModel
	 * @param sourceSysId
	 * @throws BusinessException
	 */
	public void processProductUnitsOfMeasurement(final JnJProductModel productModel, final String sourceSysId,
			final String baseUomCode, final boolean isMddProduct, final boolean isConsumerUSProduct) throws BusinessException
	{
		final Collection<JnjGTIntProductUnitModel> intProductUnitModels = getjnjGTProductFeedService()
				.getIntProductUnitsRecordsByProductSkuCode(productModel.getCode(), productModel.getRegionCode(), isMddProduct,
						sourceSysId);

		if (CollectionUtils.isEmpty(intProductUnitModels))
		{
			LOGGER.info("NO INTERMEDIATE RECORDS PRESENT FOR PRODUCT DESCRIPTION, WITH PRODUCT SKU CODE: " + productModel.getCode());
		}
		else
		{
			final Collection<VariantProductModel> existingVariants = new ArrayList<>();
			existingVariants.addAll(productModel.getVariants());
			final Collection<VariantProductModel> updatedVariants = new HashSet<VariantProductModel>();
			final Collection<JnjGTVariantProductModel> newVariants = new HashSet<JnjGTVariantProductModel>();
			final Collection<VariantProductModel> obsoleteVariants = new HashSet<VariantProductModel>();
			String variantProductCode = null;
			upcCode = null;
			upcCodeFound = false;
			delGtinGreatestPkglvlCode = 0;
			salesGtinGreatestPkglvlCode = 0;
			final List<JnjGTVariantProductModel> deliveryGtinVariant = new ArrayList<>();
			final List<JnjGTVariantProductModel> salesGtinVariant = new ArrayList<>();
			boolean variantExists = false;
			final Map<String, JnjGTVariantProductModel> newVariantsMap = new HashMap<String, JnjGTVariantProductModel>();

			getjnjGTProductFeedService().setJnjGTVariantType(productModel);
			for (final JnjGTIntProductUnitModel intProductUnitModel : intProductUnitModels)
			{
				for (final VariantProductModel existingVariant : existingVariants)
				{
					variantProductCode = (productModel.getCode() + Jnjgtb2binboundserviceConstants.PIPE_STRING + (isMddProduct ? intProductUnitModel
							.getPackagingLvlCode() : intProductUnitModel.getUnitCode()));

					if ((existingVariant instanceof JnjGTVariantProductModel)
							&& (existingVariant.getCode().equals(variantProductCode)))
					{
						variantExists = true;
						convertProductUnitIntoVariant(intProductUnitModel, (JnjGTVariantProductModel) existingVariant, isMddProduct,
								deliveryGtinVariant, salesGtinVariant, baseUomCode);
						updatedVariants.add(existingVariant);
						break;
					}
				}

				if (!variantExists)
				{
					/**
					 * Below check confirms if no new variant should get formed with same code (i.e. <base prod. code>|<unit
					 * code>) and catalog version. If intermediate record with similar unique attributes occur again, rather
					 * than forming a new variant the new variant formed earlier in current iteration is returned back from
					 * the map for the update of other attributes.
					 */
					JnjGTVariantProductModel newVariantProductModel = null;
					variantProductCode = (productModel.getCode() + Jnjgtb2binboundserviceConstants.PIPE_STRING + (isMddProduct ? intProductUnitModel
							.getPackagingLvlCode() : intProductUnitModel.getUnitCode()));

					if (newVariantsMap.keySet().contains(variantProductCode))
					{
						newVariantProductModel = newVariantsMap.get(variantProductCode);
					}
					else
					{
						newVariantProductModel = getjnjGTProductFeedService().createNewItem(JnjGTVariantProductModel.class);
						newVariantProductModel.setCode(variantProductCode);
						newVariantProductModel.setCatalogVersion(isMddProduct ? mddCatalogVersion
								: (isConsumerUSProduct ? consumerUsCatalogVersion : consumerCaCatalogVersion));
						newVariantProductModel.setBaseProduct(productModel);
						getjnjGTProductFeedService().saveItem(newVariantProductModel);
						newVariantsMap.put(variantProductCode, newVariantProductModel);
					}

					convertProductUnitIntoVariant(intProductUnitModel, newVariantProductModel, isMddProduct, deliveryGtinVariant,
							salesGtinVariant, baseUomCode);
					newVariants.add(newVariantProductModel);
				}
			}

			if (!isMddProduct)
			{
				if (upcCodeFound && upcCode != null)
				{
					productModel.setUpcCode(upcCode);
					setUpcCodeForVariants(updatedVariants, newVariants, upcCode);
				}
				else
				{
					LOGGER.info("COULD NOT FIND ANY UPC CODE FROM ANY OF THE INTERMEDIATE TABLE FOR THE PRODUCT SKU CODE: "
							+ productModel.getCode());
				}
			}

			if (deliveryGtinVariant.size() > 0)
			{
				final JnjGTVariantProductModel deliveryGtinVariantModel = deliveryGtinVariant.get(0);
				deliveryGtinVariantModel.setDeliveryGtinInd(Boolean.TRUE);
			}
			else
			{
				LOGGER.info("No delivery GTIN update available for the product with code: " + productModel.getCode());
			}

			if (salesGtinVariant.size() > 0)
			{
				final JnjGTVariantProductModel salesGtinVariantModel = salesGtinVariant.get(0);
				salesGtinVariantModel.setSalesGtinInd(Boolean.TRUE);
			}
			else
			{
				LOGGER.info("No Sales GTIN update available for the product with code: " + productModel.getCode());
			}

			existingVariants.removeAll(updatedVariants);
			//existingVariants.removeAll(newVariants);
			obsoleteVariants.addAll(existingVariants);


			final Date currentDate = new Date();
			for (final VariantProductModel obsoleteVariant : obsoleteVariants)
			{
				if ((obsoleteVariant.getOfflineDate() == null || obsoleteVariant.getOfflineDate().after(currentDate))
						&& obsoleteVariant instanceof JnjGTVariantProductModel)
				{
					final Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DATE, -1);
					((JnjGTVariantProductModel) obsoleteVariant).setOfflineDate(calendar.getTime());
				}
			}

			updatedVariants.addAll(newVariants);
			updatedVariants.addAll(obsoleteVariants);

			try
			{
				getjnjGTProductFeedService().saveItems(updatedVariants);
				productModel.setVariants(updatedVariants);
				getjnjGTProductFeedService().saveItem(productModel);
			}
			catch (final BusinessException exception)
			{
				throw exception;
			}
			finally
			{
				existingVariants.clear();
				newVariants.clear();
			}
		}
	}

	/**
	 * Maps and Converts a <code>JnjGTIntProductUnitModel</code> into a <code>JnjGTVariantProductModel</code>.
	 * 
	 * @param intProductUnitModel
	 * @param variantProductModel
	 * @param isMddProduct
	 */
	private void convertProductUnitIntoVariant(final JnjGTIntProductUnitModel intProductUnitModel,
			final JnjGTVariantProductModel variantProductModel, final boolean isMddProduct,
			final List<JnjGTVariantProductModel> deliveryGtinVariant, final List<JnjGTVariantProductModel> salesGtinVariant,
			final String baseUomCode)
	{
		final String unitcode = intProductUnitModel.getUnitCode();
		int packagingLvlCodeValue = Integer.MIN_VALUE;

		variantProductModel.setApprovalStatus(ArticleApprovalStatus.APPROVED);
		variantProductModel.setUnit(getUnitOfMeasurement(unitcode, intProductUnitModel.getUnitName()));

		variantProductModel.setLinearUom(getUnitOfMeasurement(intProductUnitModel.getLineUomCode(),
				intProductUnitModel.getLineUomName()));

		variantProductModel.setVolumeUom(getUnitOfMeasurement(intProductUnitModel.getVolumeUomCode(), null));
		variantProductModel.setWeightUom(getUnitOfMeasurement(intProductUnitModel.getWeightUomCode(),
				intProductUnitModel.getWeightUomName()));

		variantProductModel.setEan(intProductUnitModel.getGtin());

		variantProductModel.setCasesPerTier((intProductUnitModel.getCasesPerTier() != null) ? Double.valueOf(intProductUnitModel
				.getCasesPerTier()) : null);
		variantProductModel.setTiersPerPallet((intProductUnitModel.getTiersPerPallet() != null) ? Double
				.valueOf(intProductUnitModel.getTiersPerPallet()) : null);

		variantProductModel.setInnerPackPerCase((intProductUnitModel.getInnerPackPerCase() != null) ? Double
				.valueOf(intProductUnitModel.getInnerPackPerCase()) : null);

		try
		{
			if (intProductUnitModel.getWeightQty() != null)
			{
				variantProductModel.setWeightQty(Double.valueOf(intProductUnitModel.getWeightQty()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.WEIGHTQTY
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getDepth() != null)
			{
				variantProductModel.setDepth(Double.valueOf(intProductUnitModel.getDepth()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.DEPTH
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getWidth() != null)
			{
				variantProductModel.setWidth(Double.valueOf(intProductUnitModel.getWidth()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.WIDTH
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getHeight() != null)
			{
				variantProductModel.setHeight(Double.valueOf(intProductUnitModel.getHeight()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.HEIGHT
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getVolumeQty() != null)
			{
				variantProductModel.setVolumeQty(Double.valueOf(intProductUnitModel.getVolumeQty()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.VOLUMEQTY
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getNetWeightQty() != null)
			{
				variantProductModel.setNetWeightQty(Double.valueOf(intProductUnitModel.getNetWeightQty()));
			}
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.NETWEIGHTQTY
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		try
		{
			if (intProductUnitModel.getPackagingLvlCode() != null)
			{
				packagingLvlCodeValue = Integer.valueOf(intProductUnitModel.getPackagingLvlCode()).intValue();
				variantProductModel.setPackagingLvlCode(Integer.valueOf(packagingLvlCodeValue));
			}

			variantProductModel.setPackagingLevelQty((intProductUnitModel.getPackagingLevelQty() != null) ? Integer
					.valueOf(intProductUnitModel.getPackagingLevelQty()) : null);
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception occured while processing attribute: " + JnjGTIntProductUnitModel.PACKAGINGLVLCODE
					+ ", for the variant with Product Sku code: " + intProductUnitModel.getProductSkuCode() + ". Error Message: "
					+ exception.getMessage());
		}

		variantProductModel.setRegionCode(intProductUnitModel.getRegionCode());

		variantProductModel.setOnlineDate(intProductUnitModel.getStartDate());
		variantProductModel.setOfflineDate(intProductUnitModel.getEndDate());

		variantProductModel.setShipUnitInd((intProductUnitModel.getShipUnitInd() != null) ? intProductUnitModel.getShipUnitInd()
				: Boolean.FALSE);
		variantProductModel.setSellUnitInd((intProductUnitModel.getSalesUomInd() != null) ? intProductUnitModel.getSalesUomInd()
				: Boolean.FALSE);

		/*
		 * Set UPC code, Sell Indicator and default GTIN Indicator.
		 */
		if (!isMddProduct)
		{
			variantProductModel.setNumerator(intProductUnitModel.getNumerator());
			/** Set Base UOM from Product as Lower Packaging Level UOM (for EA) for Consumer product. **/
			variantProductModel.setLwrPackagingLvlUom(getUnitOfMeasurement(baseUomCode, null));

			variantProductModel.setDeliveryGtinInd((intProductUnitModel.getShipUnitInd() == null) ? Boolean.FALSE
					: intProductUnitModel.getShipUnitInd());

			variantProductModel.setSalesGtinInd((intProductUnitModel.getSalesUomInd() == null) ? Boolean.FALSE : intProductUnitModel
					.getSalesUomInd());

			variantProductModel.setOuterCaseCode(intProductUnitModel.getOuterCaseCode());

			if (upcCodeFound)
			{
				variantProductModel.setUpc(upcCode);
			}
			else if (Jnjgtb2binboundserviceConstants.Product.EACH.equals(unitcode))
			{
				upcCode = intProductUnitModel.getGtin();
				upcCodeFound = true;
				variantProductModel.setUpc(upcCode);
			}
		}
		else
		{
			final Date currentDate = new Date();
			variantProductModel.setNumerator((intProductUnitModel.getLwrPackagingLvlQty() != null ? Integer
					.valueOf(intProductUnitModel.getLwrPackagingLvlQty()) : null));
			variantProductModel.setLwrPackagingLvlUom(getUnitOfMeasurement(intProductUnitModel.getLwrPackagingLvlUomCode(), null));

			/*** Set greatest Package level code only for enabled ship unit with valid start and end date. ***/
			if (intProductUnitModel.getShipUnitInd() != null
					&& Boolean.TRUE.equals(intProductUnitModel.getShipUnitInd())
					&& (packagingLvlCodeValue >= delGtinGreatestPkglvlCode)
					&& ((intProductUnitModel.getStartDate() == null || (intProductUnitModel.getStartDate() != null && intProductUnitModel
							.getStartDate().before(currentDate))) && (intProductUnitModel.getEndDate() == null || (intProductUnitModel
							.getEndDate() != null && intProductUnitModel.getEndDate().after(currentDate)))))
			{
				delGtinGreatestPkglvlCode = packagingLvlCodeValue;
				deliveryGtinVariant.clear();
				deliveryGtinVariant.add(variantProductModel);
			}

			if (intProductUnitModel.getSalesUomInd() != null
					&& Boolean.TRUE.equals(intProductUnitModel.getSalesUomInd())
					&& (packagingLvlCodeValue >= salesGtinGreatestPkglvlCode)
					&& ((intProductUnitModel.getStartDate() == null || (intProductUnitModel.getStartDate() != null && intProductUnitModel
							.getStartDate().before(currentDate))) && (intProductUnitModel.getEndDate() == null || (intProductUnitModel
							.getEndDate() != null && intProductUnitModel.getEndDate().after(currentDate)))))
			{
				salesGtinGreatestPkglvlCode = packagingLvlCodeValue;
				salesGtinVariant.clear();
				salesGtinVariant.add(variantProductModel);
			}
		}
	}

	/**
	 * Finds existing UoM corresponding to the code, and if not found creates new <code>UnitModel</code> with the code.
	 * 
	 * @param code
	 * @param name
	 * @return UnitModel
	 */
	private UnitModel getUnitOfMeasurement(final String code, final String name)
	{
		UnitModel unitModel = null;
		if (code == null)
		{
			return null;
		}
		else
		{
			try
			{
				unitModel = getjnjGTProductFeedService().getUnitByCode(code);
				if (unitModel == null)
				{
					LOGGER.info("Could not find any exiting Unit corresponding to the code: " + code + ", creating a new unit.");
					unitModel = getjnjGTProductFeedService().createNewItem(UnitModel.class);
					unitModel.setCode(code);
					unitModel.setName(name);
					unitModel.setUnitType(Jnjgtb2binboundserviceConstants.Product.JNJ_UNIT_TYPE);
					getjnjGTProductFeedService().saveItem(unitModel);
				}
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Error while creating unit of measurement for unit code: " + code + ". Exception: "
						+ exception.getMessage());
			}
		}
		return unitModel;
	}

	/**
	 * Finds existing Country corresponding to the iso code, and if not found creates new <code>CountryModel</code> with
	 * the code.
	 * 
	 * @param isoCode
	 * @param name
	 * @return
	 */
	private CountryModel getCountryModel(final String isoCode, final String name)
	{
		CountryModel countryModel = null;
		if (isoCode == null)
		{
			return null;
		}
		else
		{
			try
			{
				countryModel = getjnjGTProductFeedService().getCountryByIso(isoCode);
				if (countryModel == null)
				{
					LOGGER.info("Could not find any exiting Country corresponding to the iso code: " + isoCode
							+ ", creating a new Country indeed.");
					countryModel = getjnjGTProductFeedService().createNewItem(CountryModel.class);
					countryModel.setIsocode(isoCode);
					countryModel.setName(name);
					getjnjGTProductFeedService().saveItem(countryModel);
				}
			}
			catch (final BusinessException exception)
			{
				LOGGER.error("Error while creating New Country Model sor iso code: " + isoCode + ". Exception: "
						+ exception.getMessage());
			}
		}
		return countryModel;
	}


	/**
	 * Sets the found UPC code to all other existing and new Variants processed till the instance UPC has been found.
	 * 
	 * @param updatedVariants
	 * @param newVariants
	 * @param upcCode
	 */
	private void setUpcCodeForVariants(final Collection<VariantProductModel> updatedVariants,
			final Collection<JnjGTVariantProductModel> newVariants, final String upcCode)
	{
		for (final VariantProductModel variant : updatedVariants)
		{
			((JnjGTVariantProductModel) variant).setUpc(upcCode);
		}

		for (final JnjGTVariantProductModel variant : newVariants)
		{
			variant.setUpc(upcCode);
		}

	}

	/**
	 * Sets un-approved status for the product and all its associated variants.
	 * 
	 * @param productModel
	 */
	private void setProductAndVariantsUnApproved(final JnJProductModel productModel)
	{
		productModel.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
		try
		{
			getjnjGTProductFeedService().saveItem(productModel);
			LOGGER.debug("SAVE....... approval status for Products........." + productModel.getApprovalStatus());
		}
		catch (final BusinessException e1)
		{
			LOGGER.error("Exception while saving Unapproved status for product with code: " + productModel.getCode());
		}

		if (!CollectionUtils.isEmpty(productModel.getVariants()))
		{
			for (final VariantProductModel variant : productModel.getVariants())
			{
				variant.setApprovalStatus(ArticleApprovalStatus.UNAPPROVED);
				try
				{
					getjnjGTProductFeedService().saveItem(variant);
					LOGGER.debug("SAVE....... approval status for Variant........." + productModel.getApprovalStatus());
				}
				catch (final BusinessException e)
				{
					LOGGER.error("Exception while saving Unapproved status for variant with code: " + variant.getCode());
				}
			}
		}
	}

	/**
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	public void setjnjGTFeedService(final JnjGTFeedService jnjGTFeedService)
	{
		this.jnjGTFeedService = jnjGTFeedService;
	}

	/**
	 * @return the jnjGTProductFeedService
	 */
	public JnjGTProductFeedService getjnjGTProductFeedService()
	{
		return jnjGTProductFeedService;
	}

	public void setjnjGTProductFeedService(final JnjGTProductFeedService jnjGTProductFeedService)
	{
		this.jnjGTProductFeedService = jnjGTProductFeedService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	public CategoryService getCategoryService()
	{
		return categoryService;
	}

	public void setCategoryService(final CategoryService categoryService)
	{
		this.categoryService = categoryService;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
