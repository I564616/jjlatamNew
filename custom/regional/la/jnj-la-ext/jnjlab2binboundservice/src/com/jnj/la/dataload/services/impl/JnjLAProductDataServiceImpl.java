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
package com.jnj.la.dataload.services.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnJProductDTO;
import com.jnj.core.dto.JnjUomConversionDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnJLaProductDTO;
import com.jnj.la.core.dto.JnjProductSalesOrgDTO;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.JnJProductSalesOrgModel;
import com.jnj.la.core.model.JnjLaSalesOrgDivisionMappingModel;
import com.jnj.la.core.model.JnjCommercialUomConversionModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.product.JnjRSAProductDataService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.services.JnjLAProductDataService;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Objects;

public class JnjLAProductDataServiceImpl implements JnjLAProductDataService
{
	private static final Logger LOG = Logger.getLogger(JnjLAProductDataServiceImpl.class);
	private static final String UNIT_OF_MEASURE="Unit of measure ";
	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private CommerceCommonI18NService commerceCommonI18NService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private JnJLaProductService jnjLaProductService;

	@Autowired
	private JnjRSAProductDataService jnjRSAProductDataService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private CatalogService catalogService;

	@Autowired
	protected UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	@Override
	public List<JnJLaProductDTO> pullProductsFromRSA(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel cronJob, final String sector)
	{
		final String methodName = "pullProductsFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		final List<JnJLaProductDTO> jnjProductDtoList = jnjRSAProductDataService.pullProductsFromRSA(lowerDate, upperDate, cronJob, sector);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		return jnjProductDtoList;
	}

	
	private List<JnJProductModel> getAvailableProducts(final List<String> codes) {
		final String methodName = "getAvailableProducts()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjLAProductDataServiceImpl.class);
		List<JnJProductModel> products = new ArrayList<>();
		final CatalogVersionModel versionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.UpsertProduct.CATALOG_MASTER, Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STAGED);
		try {
			products = jnjLaProductService.getProductsForCatalogVersionId(versionModel, codes);
		} catch (final UnknownIdentifierException exception) {
			String message = String.format("model not found for given code. Detail: '%s'. Creating a new product for code: '%s'. ", exception.getMessage(), codes);
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, message, JnjLAProductDataServiceImpl.class);
		} catch (final Exception exception) {
			String message = "unexpected exception. Creating a new product for code: " + codes;
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName, message, exception, JnjLAProductDataServiceImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjLAProductDataServiceImpl.class);
		return products;
	}

	@Override
	public void saveProductToHybris(final List<JnJLaProductDTO> products, List<String> failedProducts)
	{
		final String methodName = "saveProductToHybris()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		boolean recordStatus = false;

		final Map<String, String> salesOrgSectorMap = getSalesOrgSectorMap();

		// Create a map to get the product model as value, based on product code as key
		final Map<String, ProductModel> productCodeAndProductModelMap = new LinkedHashMap<>();

		// Get the list of product codes from the List<JnJLaProductDTO>
		final List<String> productCodes = new ArrayList<>(); 
		getProductCodeList(products, productCodes);		

		// Passing the list of product codes to getAvailableProducts method to get the list of product models
		final List<ProductModel> listOfProductModels = new ArrayList<>();
		final List<JnJLaProductModel> finalProductList = new ArrayList<>();
		
		listOfProductModels.addAll(getAvailableProducts(productCodes));
		LOG.debug("Products size in batch: " + listOfProductModels.size());
		
		getMapofExistingProducts(productCodeAndProductModelMap, listOfProductModels);
		LOG.debug("Final Products List: " + productCodeAndProductModelMap.keySet().size());

		List<String> notAvailableProductList = new ArrayList<>();
		LOG.info("products in JnJLaProductDTO size: " +products.size());
		final CatalogVersionModel versionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.UpsertProduct.CATALOG_MASTER, Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STAGED);
		for (final JnJLaProductDTO product : products)
		{
			try {
			ProductModel productmodel = null;
			if(productCodeAndProductModelMap.containsKey(product.getCode())) {
				productmodel = productCodeAndProductModelMap.get(product.getCode());
			} else {
				productmodel = jnjLaProductService.createModel();
				productmodel.setCatalogVersion(versionModel);
				notAvailableProductList.add(product.getCode());
			}
			productmodel.setApprovalStatus(ArticleApprovalStatus.APPROVED);

			if (productmodel instanceof JnJLaProductModel)
			{  
				recordStatus = loadProduct((JnJLaProductModel) productmodel, product, salesOrgSectorMap, finalProductList);
			} else {
				recordStatus = false;
			}

			if (recordStatus)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						" Record Processed Successfully, update timestamp in job", JnjLAProductDataServiceImpl.class);
				LOG.info("LastUpdatedDate Of The Product: " + product.getCode() + " IS: " + product.getLastUpdateDate());
				
			}
			else
			{
				JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"The Record with Product ID: " + product.getCode() + " was not processed successfully.",
						JnjLAProductDataServiceImpl.class);
				failedProducts.add(product.getCode());
			}
			} catch (Exception exe) {
				JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"Error while loading the product " + product.getCode(),
						JnjLAProductDataServiceImpl.class);
				failedProducts.add(product.getCode());
			}
		}
		
		try {
			if (CollectionUtils.isNotEmpty(finalProductList)) {
				modelService.saveAll(finalProductList);
				LOG.debug("TotalProducts saved successfully" + finalProductList.size());
			}
		} catch (Exception exe) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"Exception occurred while saving totalproducts ", exe,
					JnjLAProductDataServiceImpl.class);			
		}
		LOG.debug("un-available products list size: " + notAvailableProductList.size());

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}


	private void getMapofExistingProducts(final Map<String, ProductModel> productCodeAndProductModelMap,
			final List<ProductModel> listOfProductModels) {
		if(CollectionUtils.isNotEmpty(listOfProductModels)) {
			for (ProductModel product: listOfProductModels) {
				productCodeAndProductModelMap.put(product.getCode(), product);
			}
		}
	}


	private void getProductCodeList(final List<JnJLaProductDTO> products, final List<String> productCodes) {
		products.stream().forEach(product -> productCodes.add(product.getCode()));
	}

	@Override
	public List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector) {
		final String methodName = "getUniqueDivisionsFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		final List<String> jnjProductDivisionsList = jnjRSAProductDataService.getUniqueDivisionsFromRSA(lowerDate, upperDate, sector);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		return jnjProductDivisionsList;
	}

	@Override
	public Date getLastUpdatedDateForLatestRecord() {
		return jnjRSAProductDataService.getLastUpdatedDateForLatestRecord();
	}

	private Map<String, String> getSalesOrgSectorMap() {

		Map<String, String> salesOrgMap = new HashMap<>();
		try {
			final String query = "Select {sd.PK} from {JnjLaSalesOrgDivisionMapping AS sd}";
			final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query);
			final SearchResult<JnjLaSalesOrgDivisionMappingModel> searchResult = flexibleSearchService.search(searchQuery);

			if (searchResult != null && CollectionUtils.isNotEmpty(searchResult.getResult())) {
				for (JnjLaSalesOrgDivisionMappingModel mapping: searchResult.getResult()) {
					salesOrgMap.put(mapping.getSalesOrg()+"_"+mapping.getDivision(), mapping.getSector());
				}
			}

			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, "getSalesOrgSectorMap",
					"Map size: " + salesOrgMap.size(), JnjLAProductDataServiceImpl.class);
		} catch (Exception ex) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, "getSalesOrgSectorMap",
					"Error while preparing salesOrg division map ", ex, JnjLAProductDataServiceImpl.class);
		}

		return salesOrgMap;
	}

	/**
	 * This method is used to set the JnjProductModel from our DTO.
	 *
	 * @param product
	 *           the product
	 * @param productDto
	 *           -JnJIntProductModel
	 * @param salesOrgSectorMap salesOrg and Division map
	 * @return true, if successful
	 */
	public boolean loadProduct(final JnJLaProductModel product, final JnJLaProductDTO productDto,
							   final Map<String, String> salesOrgSectorMap, final List<JnJLaProductModel> finalProductList)
	{

		final String methodName = "loadProduct()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		boolean recordStatus = false;

		try
		{
			if (productDto.getJnjUomConversionDTO() == null)
			{
				throw new BusinessException(
						"No uom conversion found for product code: " + productDto.getCode() + ".  Dropping the record.");
			}
			loadProductUom(product, productDto);

			if (productDto.getJnjProductSalesOrgDto() == null)
			{
				throw new BusinessException(
						"No product salesOrg found for product code: " + productDto.getCode() + ". Dropping the record.");
			}

			removeAllCurrentSaleOrgSets(product);
			// initialize the map setting all a product to be inactive in all stores
			initializeProductInactiveInStoreMap();
			// Set default value for e-commerce flag
			product.setEcommerceFlag(Boolean.FALSE);
			final List<JnJProductSalesOrgModel> jnJProductSalesOrgModelList = new ArrayList<>();
			// Updates new salesOrgs to  a product and sets productStoreActivationList boolean value for a product at a given store
			updateProductSalesOrgModel(product, productDto, jnJProductSalesOrgModelList, salesOrgSectorMap);
			/* Setting Final JnJProductSalesOrgModelList on Product */
			product.setSalesOrgList(jnJProductSalesOrgModelList);

			if (productDto.getJnJProductDescriptionDTO() == null
					|| CollectionUtils.isEmpty(productDto.getJnJProductDescriptionDTO()))
			{
				throw new BusinessException(
						"No product description found for product code: " + productDto.getCode() + ". Dropping the record.");
			}
			else
			{
				loadProducDescription(product, productDto);
			}

			// Populate: catalogId, ean, origin country, sector, material type and categody
			productPopulateFields(product, productDto);

			setCountriesAndSalesOrg(product, productDto);

			logProductInfo(productDto, product, methodName);
			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Division " + productDto.getDivision(),
					JnjLAProductDataServiceImpl.class);

			/* Make Existing Products Inactive in Catalog ID Pool. */
		
			if (null != productDto.getBaseUnit())
			{
				if (product.getUnit() == null)
				{
					product.setUnit(getUnit(productDto.getBaseUnit()));
				}
				if (product.getDeliveryUnitOfMeasure() == null)
				{
					product.setDeliveryUnitOfMeasure(getUnit(productDto.getBaseUnit()));
				}				
				
			}
			
			finalProductList.add(product);
			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, " - Product added to the list",
					JnjLAProductDataServiceImpl.class);
			recordStatus = true;
		}
		catch (final BusinessException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"BusinessException occurred while saving the material " + productDto.getCode() + " - " + exception.getLocalizedMessage(), exception,
					JnjLAProductDataServiceImpl.class);
			recordStatus = false;
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"ModelSavingException occurred while saving material " + productDto.getCode() + " to Hybris database " + " - " + modelSavingException.getLocalizedMessage(), modelSavingException,
					JnjLAProductDataServiceImpl.class);
			recordStatus = false;
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"IllegalArgumentException occurred while saving material " + productDto.getCode() + " which has a unit of measure " + productDto.getBaseUnit() + " not defined",
					illegalArgumentException, JnjLAProductDataServiceImpl.class);
			recordStatus = false;
		}
		catch (final Exception ex)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					" General exception occurred while saving the material " + productDto.getCode() + " - " + ex.getLocalizedMessage(), ex, JnjLAProductDataServiceImpl.class);
			recordStatus = false;
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		return recordStatus;
	}

	/**
	 *
	 */
	private void productPopulateFields(final JnJProductModel product, final JnJLaProductDTO productDto)
	{
		//Setting CatalogID on Product

		if (null != productDto.getCatalogId())
		{
			product.setCatalogId(productDto.getCatalogId().toUpperCase());
		}
		product.setMaterialType(productDto.getMaterialType());

		product.setEan(productDto.getEan());
		if (null != productDto.getOriginCountry() && !productDto.getOriginCountry().isEmpty())
		{
			product.setOriginCountry(commonI18NService.getCountry(productDto.getOriginCountry()));
		}

		final Collection<CategoryModel> categoryModelCollection = new ArrayList<>();
		categoryModelCollection.add(getCategoryModel(productDto.getCategory()));
		product.setSupercategories(categoryModelCollection);
		product.setSector(productDto.getSector());
		if (product instanceof JnJLaProductModel) {
			((JnJLaProductModel) product).setDivision(productDto.getDivision());
		}
	}


	/**
	 *
	 */
	private Map<String, Boolean> initializeProductInactiveInStoreMap()
	{
		final List<String> storeList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_VALID_STORES);
		final Map<String, Boolean> productInactiveInStoreMap = new HashMap<>();

		for (final String store : storeList)
		{
			productInactiveInStoreMap.put(store, Boolean.FALSE);
		}

		return productInactiveInStoreMap;
	}

	/**
	 * @param product
	 * @param methodName
	 *
	 */
	private void logProductInfo(final JnJProductDTO productDto, final JnJLaProductModel product, final String methodName)
	{
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Product info for: code: " + productDto.getCode(),
				JnjLAProductDataServiceImpl.class);
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
				" - Product info: getCategory: " + productDto.getCategory(), JnjLAProductDataServiceImpl.class);
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, " - Product Description: " + product.getDescription(),
				JnjLAProductDataServiceImpl.class);
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, " - product EAN: " + product.getEan(),
				JnjLAProductDataServiceImpl.class);
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Base Unit" + productDto.getBaseUnit(),
				JnjLAProductDataServiceImpl.class);
	}

	private void loadProducDescription(final JnJLaProductModel product, final JnJProductDTO productDto)
	{
		final String methodName = "loadProducDescription";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		for (int productDescription = 0; productDescription < productDto.getJnJProductDescriptionDTO().size(); productDescription++)
		{
			/* Check if Language Exists in HYBRIS */
			final String languageName = productDto.getJnJProductDescriptionDTO().get(productDescription).getLanguage();

			final Locale locale = getLanguageLocale(languageName);

			if (null != locale)
			{
				try
				{
					/* Language exist in HYBRIS. Setting Material Desc. */
					product.setName(productDto.getJnJProductDescriptionDTO().get(productDescription).getDescription(), locale);
					product.setDescription(productDto.getJnJProductDescriptionDTO().get(productDescription).getDescription(), locale);
				}
				catch (final IllegalArgumentException illegalArgumentException)
				{
					throw new IllegalArgumentException(
							"Material " + productDescription + " has a description in a language not defined " + locale,
							illegalArgumentException);
				}
			}
			else
			{
				/* Language does not exist in HYBRIS. Skipping the Desc. */
				JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"Language with ISO CODE[" + languageName + "] does not exist in Hybris.", JnjLAProductDataServiceImpl.class);
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"Skipping Product Descrption for Language [" + languageName + "]", JnjLAProductDataServiceImpl.class);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}

	private Locale getLanguageLocale(final String languageName)
	{
		/* Getting all available languages in HYBRIS */
		final List<LanguageModel> availableLanguageList = (List<LanguageModel>) commerceCommonI18NService.getAllLanguages();
		for (final LanguageModel languageModel : availableLanguageList)
		{
			if (StringUtils.equalsIgnoreCase(languageModel.getIsocode(), languageName))
			{
				return Locale.of(languageName);
			}
		}
		return null;
	}


	/**
	 * @param product
	 * @param productDto
	 * @param jnJProductSalesOrgModelList
	 * @param salesOrgSectorMap
	 * @throws ParseException
	 *
	 */
	private void updateProductSalesOrgModel(final JnJLaProductModel product, final JnJLaProductDTO productDto,
			final List<JnJProductSalesOrgModel> jnJProductSalesOrgModelList, final Map<String, String> salesOrgSectorMap)
			throws ParseException
	{
		final String methodName = "updateProductSalesOrgs";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		String vrkmeUnitOfMeasure;
		String deliveryUnitMeasure;

		if (productDto.getJnjProductSalesOrgDto() != null && !productDto.getJnjProductSalesOrgDto().isEmpty())
		{
			for (final JnjProductSalesOrgDTO jnjProductSalesOrgDto : productDto.getJnjProductSalesOrgDto())
			{
				// Fetch Existing JnJProductSalesOrgModel if any
				JnJProductSalesOrgModel jnJProductSalesOrgModel = jnjLaProductService.getJnjProductSalesOrgModel(productDto.getCode(),
						jnjProductSalesOrgDto.getSalesOrganization(), false);

				// If no JnJProductSalesOrgModel is fetched then create new
				if (Objects.isNull(jnJProductSalesOrgModel))
				{
					jnJProductSalesOrgModel = modelService.create(JnJProductSalesOrgModel.class);
				}

				// Populating Values from JnjProductSalesOrgDto to jnJProductSalesOrgModel
				populateProductSalesOrgModel(jnJProductSalesOrgModel, jnjProductSalesOrgDto, productDto,
						salesOrgSectorMap);

				vrkmeUnitOfMeasure = jnjProductSalesOrgDto.getUnit();
				deliveryUnitMeasure = jnjProductSalesOrgDto.getDeliveryUnit();

				/* Setting Numerator UOM in Current JnJProductSalesOrgModel */
				setNumeratorUOM(product.getUomDetails(), vrkmeUnitOfMeasure, deliveryUnitMeasure, jnJProductSalesOrgModel);
				

				// Prevents duplicated salesOrgs from being added
				if (!jnJProductSalesOrgModelList.contains(jnJProductSalesOrgModel))
				{
					jnJProductSalesOrgModelList.add(jnJProductSalesOrgModel);
				}


				/* Setting Discontinue 'Flag' and Offline Date based on Status flag. */
				setDiscontinueFlagAndOfflineDate(jnjProductSalesOrgDto.getStatus(), jnJProductSalesOrgModel);

				/* Setting Modified Time of Updated ProductSalesOrg on Existing JnJProduct */
				product.setProductSalesOrgModifiedTime(jnJProductSalesOrgModel.getModifiedtime());

			} //End of JnjIntProductSalesOrgModel Loop

			// Store iso code, sales orgs list
			final Map<String, List<JnJProductSalesOrgModel>> salesOrgSplitModelList = new HashMap<>();
			splitSalesOrgModel(jnJProductSalesOrgModelList, salesOrgSplitModelList);
			
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}

	/**
	 * Split the list of sales org from each store in a map. i.e: BR=BR02,BR01 | Cenca=PA02,PA01,PR01
	 */
	private void splitSalesOrgModel(final List<JnJProductSalesOrgModel> jnJProductSalesOrgModelList,
			final Map<String, List<JnJProductSalesOrgModel>> salesOrgSplitModelList)
	{
		final List<String> cencaCountryList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_CENCA_VALID_COUNTRIES);

		final List<String> storeList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_VALID_STORES);
		for (final String store : storeList)
		{
			final List<JnJProductSalesOrgModel> productSalesOrgList = new ArrayList<>();

			for (final JnJProductSalesOrgModel prodSalesOrg : jnJProductSalesOrgModelList)
			{
				final String salesOrgCountry = prodSalesOrg.getSalesOrg().substring(0, 2);
				if (store.equalsIgnoreCase(salesOrgCountry) || (store.equalsIgnoreCase(Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA)
						&& cencaCountryList.contains(salesOrgCountry)))
				{
					productSalesOrgList.add(prodSalesOrg);
				}
			}
			if (!productSalesOrgList.isEmpty())
			{
				salesOrgSplitModelList.put(store, productSalesOrgList);
			}

		}
	}


	/**
	 * @param productDto
	 *
	 */
	private void populateProductSalesOrgModel(final JnJProductSalesOrgModel jnJProductSalesOrgModel,
			final JnjProductSalesOrgDTO jnjProductSalesOrgDto, final JnJProductDTO productDto,
											  final Map<String, String> salesOrgSectorMap)
	{
		final String methodName = "populateProductSalesOrgModel";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		
		jnJProductSalesOrgModel.setSalesOrg(jnjProductSalesOrgDto.getSalesOrganization());
		jnJProductSalesOrgModel.setStatus(jnjProductSalesOrgDto.getStatus());
		jnJProductSalesOrgModel.setEcommerceFlag(jnjProductSalesOrgDto.getEcommerceFlag());
		jnJProductSalesOrgModel.setProductCode(productDto.getCode());		
		jnJProductSalesOrgModel.setActive(true);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "SalesOrg: "
						+ jnjProductSalesOrgDto.getSalesOrganization() + " && " + "Division: "
						+ ((JnJLaProductDTO)productDto).getDivision() , JnjLAProductDataServiceImpl.class);
		String sector = salesOrgSectorMap.get(
				jnjProductSalesOrgDto.getSalesOrganization()+"_"+((JnJLaProductDTO)productDto).getDivision());
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Derived sector: " + sector,
				JnjLAProductDataServiceImpl.class);
		jnJProductSalesOrgModel.setSector(sector);
		// Setting Cold Chain Flag in Current JnJProductSalesOrgModel
		final String coldChainProduct = jnjProductSalesOrgDto.getColdChainProduct();
		if (StringUtils.isNotEmpty(coldChainProduct)
				&& Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_COLD_CHAIN_VALUE.equalsIgnoreCase(coldChainProduct))
		{
			jnJProductSalesOrgModel.setColdChainProduct(Boolean.TRUE);
		}
		else
		{
			jnJProductSalesOrgModel.setColdChainProduct(Boolean.FALSE);
		}
		try
		{
			jnJProductSalesOrgModel.setDeliveryUnitOfMeasure(getUnit(jnjProductSalesOrgDto.getDeliveryUnit()));
			jnJProductSalesOrgModel.setSalesUnitOfMeasure(getUnit(jnjProductSalesOrgDto.getSalesUnitOfMeasure()));
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{

			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					UNIT_OF_MEASURE + jnjProductSalesOrgDto.getUnit() + " is not in the platform."
							+ illegalArgumentException.getLocalizedMessage(),
					illegalArgumentException, JnjLAProductDataServiceImpl.class);
			throw illegalArgumentException;
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}

	private String getSalesOrgSector(final String salesOrg, final String division) {

		try {
			JnjLaSalesOrgDivisionMappingModel salesOrgDivisionMappingModel = modelService.create(JnjLaSalesOrgDivisionMappingModel.class);
			salesOrgDivisionMappingModel.setSalesOrg(salesOrg);
			salesOrgDivisionMappingModel.setDivision(division);
			JnjLaSalesOrgDivisionMappingModel salesOrgDivisionMapping = flexibleSearchService.getModelByExample(salesOrgDivisionMappingModel);
			return salesOrgDivisionMapping.getSector();
		} catch (Exception ex) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, "getSalesOrgSector",
					"SalesOrg and Division mapping doesn't exist for SalesOrg code "+ salesOrg
							+ " and division " + division, ex, JnjLAProductDataServiceImpl.class);
		}
		return StringUtils.EMPTY;
	}

	/**
	 *
	 */
	private void removeAllCurrentSaleOrgSets(final JnJLaProductModel product)
	{
		final String methodName = "removeAllCurrentSaleOrgSets";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		final List<JnJProductSalesOrgModel> jnJProductSalesOrgModelExistingList = product.getSalesOrgList();
		if (null != jnJProductSalesOrgModelExistingList && !jnJProductSalesOrgModelExistingList.isEmpty())
		{
			// Remove all attached to the product
			modelService.removeAll(jnJProductSalesOrgModelExistingList);
		}
		// Remove all related  to the product which may be attached to it
		jnjLaProductService.removeAllProductSalesOrgForProduct(product.getCode());
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}


	/**
	 * This method updates the existing products in Catalog ID pool to make them [Inactive] if current Product is
	 * [active].
	 *
	 * @param product
	 *           the product
	 * @param makeBrProductsInactive
	 *           the make br products inactive
	 * @param makeMxProductsInactive
	 *           the make mx products inactive
	 * @param makePaProductsInactive
	 */
	@SuppressWarnings("unused")
	private void updateExistingProductsInCatalogIdPool(final JnJProductModel product, final boolean makeBrProductsInactive,
			final boolean makeMxProductsInactive, final boolean makePaProductsInactive, final boolean makeEcProductsInactive)
	{
		final String methodName = "updateExistingProductsInCatalogIdPool()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		List<JnJProductModel> activeProducts;
		try
		{
			/* Fetching MasterProduct Catalog Version */
			final CatalogModel catalogModel = jnjLaProductService.getJnJCatalogForId();
			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion(catalogModel.getId(),
					Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STAGED);

			/* Fetch all active Products in the CatalogID pool */
			activeProducts = jnjLaProductService.getAllActiveProducts((JnJLaProductModel) product, catalogVersionModel);
			if (CollectionUtils.isNotEmpty(activeProducts))
			{
				activeProductsLoop: for (final JnJProductModel jnJProductModel : activeProducts)
				{
					final JnJLaProductModel jnjLaProductModel = (JnJLaProductModel) jnJProductModel;
					/* Getting ProductSalesOrg for each Active Product */
					final List<JnJProductSalesOrgModel> productSalesOrgList = jnjLaProductModel.getSalesOrgList();

					/* We have to make sure that we don't toggle the 'e-commerce' flag of the current incoming product. */
					if (CollectionUtils.isNotEmpty(productSalesOrgList)
							&& !StringUtils.equalsIgnoreCase(jnjLaProductModel.getCode(), product.getCode()))
					{
						productSalesOrgLoop: for (final JnJProductSalesOrgModel jnJProductSalesOrgModel : productSalesOrgList)
						{
							final String salesOrgName = jnJProductSalesOrgModel.getSalesOrg();
							if (StringUtils.isEmpty(salesOrgName))
							{
								continue productSalesOrgLoop;
							}

							final String salesOrgCountry = salesOrgName.substring(0, 2);

							/* The active ProductSalesOrg is the Master */
							if (null != jnJProductSalesOrgModel.getActive() && jnJProductSalesOrgModel.getActive())
							{
								/* Setting 'E-commerce' Flag to [FALSE] for masterProductSalesOrg of BR */
								if (makeBrProductsInactive
										&& StringUtils.equalsIgnoreCase(salesOrgCountry, Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL))
								{
									jnJProductSalesOrgModel.setEcommerceFlag(Boolean.FALSE);

									/* Saving ProductSalesOrg */
									jnjLaProductService.saveProductSalesOrgModel(jnJProductSalesOrgModel);

									/* Setting Modified Time of Updated ProductSalesOrg on Existing JnJProduct */
									jnjLaProductModel.setProductSalesOrgModifiedTime(jnJProductSalesOrgModel.getModifiedtime());
									continue productSalesOrgLoop;
								}

								/* Setting 'E-commerce' Flag to [FALSE] for masterProductSalesOrg of MX */
								if (makeMxProductsInactive
										&& StringUtils.equalsIgnoreCase(salesOrgCountry, Jnjb2bCoreConstants.COUNTRY_ISO_MEXICO))
								{
									jnJProductSalesOrgModel.setEcommerceFlag(Boolean.FALSE);

									/* Saving ProductSalesOrg */
									jnjLaProductService.saveProductSalesOrgModel(jnJProductSalesOrgModel);

									/* Setting Modified Time of Updated ProductSalesOrg on Existing JnJProduct */
									jnjLaProductModel.setProductSalesOrgModifiedTime(jnJProductSalesOrgModel.getModifiedtime());
									continue productSalesOrgLoop;
								}

								/* Setting 'E-commerce' Flag to [FALSE] for masterProductSalesOrg of PA */
								if (makePaProductsInactive && (StringUtils.equalsIgnoreCase(salesOrgCountry,
										Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA)
										|| StringUtils.equalsIgnoreCase(salesOrgCountry, Jnjlab2bcoreConstants.COUNTRY_ISO_DOMINICAN)
										|| StringUtils.equalsIgnoreCase(salesOrgCountry, Jnjlab2bcoreConstants.COUNTRY_ISO_PUERTORICO)))
								{

									jnJProductSalesOrgModel.setEcommerceFlag(Boolean.FALSE);

									/* Saving ProductSalesOrg */
									jnjLaProductService.saveProductSalesOrgModel(jnJProductSalesOrgModel);

									/* Setting Modified Time of Updated ProductSalesOrg on Existing JnJProduct */
									jnjLaProductModel.setProductSalesOrgModifiedTime(jnJProductSalesOrgModel.getModifiedtime());
									continue productSalesOrgLoop;
								}

								/* Setting 'E-commerce' Flag to [FALSE] for masterProductSalesOrg of EC */
								if (makeEcProductsInactive
										&& StringUtils.equalsIgnoreCase(salesOrgCountry, Jnjb2bCoreConstants.COUNTRY_ISO_EQUADOR))
								{
									jnJProductSalesOrgModel.setEcommerceFlag(Boolean.FALSE);

									/* Saving ProductSalesOrg */
									jnjLaProductService.saveProductSalesOrgModel(jnJProductSalesOrgModel);

									/* Setting Modified Time of Updated ProductSalesOrg on Existing JnJProduct */
									jnjLaProductModel.setProductSalesOrgModifiedTime(jnJProductSalesOrgModel.getModifiedtime());
									continue productSalesOrgLoop;
								}
							}
						} //End of productSalesOrgLoop

						/* Saving Existing JnJProductModel */
						jnjLaProductService.saveProduct(jnJProductModel);
					}
				} //End of activeProductsLoop
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"BusinessException occurred while getting Active Products of of the CatalogID pool with ID["
							+ product.getCatalogId() + "]. " + Logging.HYPHEN
							+ businessException.getLocalizedMessage(),
					businessException, JnjLAProductDataServiceImpl.class);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"exception occurred while making  Active Products Inactive in the CatalogID pool with ID["
							+ product.getCatalogId() + "]. ",
					exception, JnjLAProductDataServiceImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}


	/**
	 * This method is used to set countries and salesOrg.
	 *
	 * @param jnJProduct JnJProductModel
	 * @param jnjProductDTO JnJIntProductModel
	 */
	private void setCountriesAndSalesOrg(final JnJProductModel jnJProduct, final JnJLaProductDTO jnjProductDTO) {
		final String methodName = "setCountriesAndSalesOrg";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		final Set<String> salesOrg = new HashSet<>();
		final Set<String> countrySet = new HashSet<>();
		final Collection<CountryModel> countries = new ArrayList<>();

		for (final JnjProductSalesOrgDTO jnjProductSalesOrgDto : jnjProductDTO.getJnjProductSalesOrgDto())
		{
			salesOrg.add(jnjProductSalesOrgDto.getSalesOrganization());
			final String countryIso = jnjProductSalesOrgDto.getSalesOrganization().substring(0, 2);
			CountryModel country;
			if (!countrySet.contains(countryIso))
			{
				country = getCountry(countryIso);
				if (null != country)
				{
					countries.add(getCountry(countryIso));
				}
				else
				{
					JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Country not found",
							JnjLAProductDataServiceImpl.class);
				}
			}
			countrySet.add(countryIso);
		}

		jnJProduct.setCountries(countries);
		jnJProduct.setSalesOrg(salesOrg);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}


	/**
	 * This method is used to get country on the basis of iso code which we are getting from XML.
	 *
	 * @param isoCode
	 *           -String
	 * @return the country
	 */
	private CountryModel getCountry(final String isoCode)
	{
		final String methodName = "getCountry";
		CountryModel countryModel = null;
		try
		{
			countryModel = commonI18NService.getCountry(isoCode);
		}
		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"Country model for ISO code not found" + "-" + exception, JnjLAProductDataServiceImpl.class);
		}
		return countryModel;
	}


	/**
	 * This method is used to get unit on the basis of code.
	 *
	 * @param code
	 *           -String
	 * @return the category model
	 */
	private CategoryModel getCategoryModel(final String code)
	{
		final String methodName = "getCategoryModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		CategoryModel category;
		final CatalogModel catalogModel = catalogService
				.getCatalogForId(Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_MASTER_CATALOG_ID);
		final CatalogVersionModel versionModel = catalogVersionService.getCatalogVersion(catalogModel.getId(),
				Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STAGED);
		try
		{
			userService.setCurrentUser(userService.getAdminUser());
			category = categoryService.getCategoryForCode(versionModel, code);
			if (null == category)
			{
				JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						"Category is not present.Hence associating with 'Default Category' with code["
								+ Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_DEFAULT_CATEGORY + "]",
						JnjLAProductDataServiceImpl.class);
				category = categoryService.getCategoryForCode(versionModel,
						Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_DEFAULT_CATEGORY);
			}
		}
		catch (final UnknownIdentifierException | IllegalArgumentException exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"category is not present.Hence associating with 'Default Category'" + "-" + exception.getLocalizedMessage(),
					exception, JnjLAProductDataServiceImpl.class);
			category = categoryService.getCategoryForCode(versionModel,
					Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_DEFAULT_CATEGORY);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		return category;
	}


	

	/**
	 * This method sets the 'Offline Date' and 'Discontinue' Flag on the JnJProductSalesOrgModel.
	 *
	 * @param status
	 *           the status
	 * @param jnJProductSalesOrgModel
	 *           the jn j product sales org model
	 * @throws ParseException
	 *            the parse exception
	 */
	private void setDiscontinueFlagAndOfflineDate(final String status, final JnJProductSalesOrgModel jnJProductSalesOrgModel)
			throws ParseException
	{
		if (Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D2.equalsIgnoreCase(status)
				|| Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D3.equalsIgnoreCase(status))
		{
			jnJProductSalesOrgModel.setDisContinue(Boolean.FALSE);
			jnJProductSalesOrgModel.setOfflineDate(null);
		}
		else if (Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_QA.equalsIgnoreCase(status)
				|| Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D4.equalsIgnoreCase(status)
				|| Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D5.equalsIgnoreCase(status))
		{
			jnJProductSalesOrgModel.setDisContinue(Boolean.TRUE);
			jnJProductSalesOrgModel.setOfflineDate(null);
		}
		else if (Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D0.equalsIgnoreCase(status)
				|| Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_D1.equalsIgnoreCase(status)
				|| Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_STATUS_CT.equalsIgnoreCase(status))
		{
			jnJProductSalesOrgModel.setDisContinue(Boolean.TRUE);
			final String dateInString = Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_OFFLINE_DATE;
			final SimpleDateFormat formatter = new SimpleDateFormat(
					Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_OFFLINE_DATE_FORMATTER);
			final Date date = formatter.parse(dateInString);
			jnJProductSalesOrgModel.setOfflineDate(date);
		}
	}


	/**
	 * This method is used to get the numerator sales UOM vale and numerator delivery value.
	 *
	 * @param uomCoversionList
	 *           the jn j int product model
	 * @param vrkmeUnitOfMeasure
	 *           the vrkme unit of measure list
	 * @param deliveryUnitMeasure
	 *           the delivery unit measure list
	 * @param jnJProductSalesOrgModel
	 *           the jn j product sales org model
	 */
	private void setNumeratorUOM(final List<JnjUomConversionModel> uomCoversionList, final String vrkmeUnitOfMeasure,
			final String deliveryUnitMeasure, final JnJProductSalesOrgModel jnJProductSalesOrgModel)
	{
		boolean salesUOM = false;
		boolean deliveryUOM = false;
		if (uomCoversionList != null && !uomCoversionList.isEmpty())
		{
			uomCoversionListLoop: for (final JnjUomConversionModel jnjUomConversionModel : uomCoversionList)
			{
				final String alternateUnitMeasure = !Objects.isNull(jnjUomConversionModel.getUOM()) ? jnjUomConversionModel.getUOM().getCode() : StringUtils.EMPTY;
				/* Setting Numerator of Sales UOM */
				if (StringUtils.endsWithIgnoreCase(alternateUnitMeasure, vrkmeUnitOfMeasure))
				{
					if (null != jnjUomConversionModel.getNumerator())
					{
						jnJProductSalesOrgModel.setNumeratorSUOM(String.valueOf(jnjUomConversionModel.getDenominator()));
						salesUOM = true;
					}
				}

				/* Setting Numerator of Delivery UOM */
				if (StringUtils.endsWithIgnoreCase(alternateUnitMeasure, deliveryUnitMeasure))
				{
					if (null != jnjUomConversionModel.getDenominator())
					{
						jnJProductSalesOrgModel.setNumeratorDUOM(String.valueOf(jnjUomConversionModel.getNumerator()));
						deliveryUOM = true;
					}
				}

				if (salesUOM && deliveryUOM)
				{
					break uomCoversionListLoop;
				}

			} // End of intUomCoversionListLoop
		}
		/* Setting Default Values if nothing is fetched from UOM Conversion Models */
		if (!deliveryUOM)
		{
			jnJProductSalesOrgModel.setNumeratorDUOM(Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_ONE);
		}
		if (!salesUOM)
		{
			jnJProductSalesOrgModel.setNumeratorSUOM(Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_ONE);
		}
	}


	/**
	 * Updates a product's UOMs
	 * @throws BusinessException
	 * @throws NumberFormatException
	 */
	private void loadProductUom(final JnJLaProductModel product, final JnJLaProductDTO productDto)
			throws NumberFormatException, BusinessException
	{
		final String methodName = "loadProductUom";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
				" - Product info: getBaseUnit: " + productDto.getBaseUnit(), JnjLAProductDataServiceImpl.class);
		product.setBaseUnitOfMeasure(getUnit(productDto.getBaseUnit()));
		product.setCode(productDto.getCode());

		//Setting Product UOM Conversion Details.
		final List<JnjUomConversionDTO> jnjUomConversionDtoList = productDto.getJnjUomConversionDTO();
		final List<JnjUomConversionModel> jnjUomConversionModelOldList = new ArrayList<>();
		//Fetch Existing UOM Conversion Details
		List<JnjUomConversionModel> jnjUomConversionList = product.getUomDetails();
		//For Null or Empty Details Create New List
		if (null == jnjUomConversionList || jnjUomConversionList.isEmpty())
		{
			jnjUomConversionList = new ArrayList<>();
			for (final JnjUomConversionDTO jnjUomConversionDto : jnjUomConversionDtoList)
			{
				if (StringUtils.isNotEmpty(jnjUomConversionDto.getUnitOfMeasure()))
				{
					jnjUomConversionList.add(loadProductUomDetails(jnjUomConversionDto));
				}
			}
			product.setUomDetails(jnjUomConversionList);
		}
		//For Existing UOM Details
		else
		{
			jnjUomConversionModelOldList.addAll(jnjUomConversionList);
			final List<JnjUomConversionModel> jnjUomConversionModelUpdatedList = new ArrayList<>();
			for (final JnjUomConversionDTO jnjUomConversionDto : jnjUomConversionDtoList)
			{
				if (StringUtils.isNotEmpty(jnjUomConversionDto.getUnitOfMeasure()))
				{
					checkAndUpdateExistingUomConversionDetails(jnjUomConversionDto, jnjUomConversionModelOldList,
							jnjUomConversionModelUpdatedList);
				}
			}
			product.setUomDetails(jnjUomConversionModelUpdatedList);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}



	/**
	 * Check and update existing uom conversion details.
	 *
	 * @param injUomConversionDto
	 *           the JnjIntUomConversionModel
	 * @param jnjUomConversionModelOldList
	 *           the JnjUomConversionModel list
	 * @param jnjUomConversionModelUpdatedList
	 *           the jnj uom conversion model updated list
	 * @throws NumberFormatException
	 *            the number format exception
	 * @throws BusinessException
	 *            the business exception
	 */
	private void checkAndUpdateExistingUomConversionDetails(final JnjUomConversionDTO injUomConversionDto,
			final List<JnjUomConversionModel> jnjUomConversionModelOldList,
			final List<JnjUomConversionModel> jnjUomConversionModelUpdatedList) throws NumberFormatException, BusinessException
	{
		final String methodName = "checkAndUpdateExistingUomConversionDetails ()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);

		boolean existingUpdated = false;
		for (final JnjUomConversionModel jnjUomConversionModel : jnjUomConversionModelOldList)
		{
			// Check whether the unit exists in the jnjUomConversionModelList
			/* :: 35488: Changing the comparison to ISO Code */
			if (StringUtils.equals(jnjUomConversionModel.getIsoCode(), injUomConversionDto.getUnitOfMeasure()))
			{
				//Unit exists in the jnjUomConversionModelList, update the same
				existingUpdated = true;
				if (StringUtils.isNotEmpty(injUomConversionDto.getNumeratorUom()))
				{
					jnjUomConversionModel.setNumerator(Integer.valueOf(injUomConversionDto.getNumeratorUom()));
				}
				if (StringUtils.isNotEmpty(injUomConversionDto.getDenominatorUom()))
				{
					jnjUomConversionModel.setDenominator(Integer.valueOf(injUomConversionDto.getDenominatorUom()));
				}
				if (injUomConversionDto.getLength() != null)
				{
					jnjUomConversionModel.setLength(injUomConversionDto.getLength());
				}
				if (injUomConversionDto.getWidth() != null)
				{
					jnjUomConversionModel.setWidth(injUomConversionDto.getWidth());
				}
				if (injUomConversionDto.getHeight() != null)
				{
					jnjUomConversionModel.setHeight(injUomConversionDto.getHeight());
				}
				if (injUomConversionDto.getShippingWeight() != null)
				{
					jnjUomConversionModel.setShippingWeight(injUomConversionDto.getShippingWeight());
				}
				if (StringUtils.isNotEmpty(injUomConversionDto.getShippingUnit()))
				{
					jnjUomConversionModel.setShippingUnit(injUomConversionDto.getShippingUnit());
				}
				if (StringUtils.isNotEmpty(injUomConversionDto.getUnitOfDimensionForVol()))
				{
					jnjUomConversionModel.setVolumeUnit(getUnit(injUomConversionDto.getUnitOfDimensionForVol()));
				}
				jnjLaProductService.saveUomConversionModel(jnjUomConversionModel);
				jnjUomConversionModelUpdatedList.add(jnjUomConversionModel);
				break;
			}
		}
		// Unit not found in jnjUomConversionModelList. Add new entry.
		if (!existingUpdated)
		{
			jnjUomConversionModelUpdatedList.add(loadProductUomDetails(injUomConversionDto));
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
	}


	/**
	 * Load product uom details.
	 *
	 * @param jnjUomConversionDto
	 *           the JnjIntUomConversionModel
	 * @return the JnjUomConversionModel
	 * @throws NumberFormatException
	 *            the number format exception
	 * @throws BusinessException
	 *            the business exception
	 */
	private JnjUomConversionModel loadProductUomDetails(final JnjUomConversionDTO jnjUomConversionDto)
			throws NumberFormatException, BusinessException
	{
		final String methodName = "loadProductUomDetails ()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		final JnjUomConversionModel jnjUomConversionModel = modelService.create(JnjUomConversionModel.class);

		final UnitModel unit = getUnit(jnjUomConversionDto.getUnitOfMeasure());
		if (null != unit)
		{
			jnjUomConversionModel.setUOM(unit);

			jnjUomConversionModel.setIsoCode(jnjUomConversionDto.getUnitOfMeasure());

			if (StringUtils.isNotEmpty(jnjUomConversionDto.getNumeratorUom()))
			{
				jnjUomConversionModel.setNumerator(Integer.valueOf(jnjUomConversionDto.getNumeratorUom()));
			}
			if (StringUtils.isNotEmpty(jnjUomConversionDto.getDenominatorUom()))
			{
				jnjUomConversionModel.setDenominator(Integer.valueOf(jnjUomConversionDto.getDenominatorUom()));
			}
			if (jnjUomConversionDto.getLength() != null)
			{
				jnjUomConversionModel.setLength(jnjUomConversionDto.getLength());
			}
			if (jnjUomConversionDto.getWidth() != null)
			{
				jnjUomConversionModel.setWidth(jnjUomConversionDto.getWidth());
			}
			if (jnjUomConversionDto.getHeight() != null)
			{
				jnjUomConversionModel.setHeight(jnjUomConversionDto.getHeight());
			}
			if (StringUtils.isNotEmpty(jnjUomConversionDto.getShippingWeight()))
			{
				jnjUomConversionModel.setShippingWeight(jnjUomConversionDto.getShippingWeight());
			}
			if (StringUtils.isNotEmpty(jnjUomConversionDto.getShippingUnit()))
			{
				jnjUomConversionModel.setShippingUnit(jnjUomConversionDto.getShippingUnit());
			}
			if (StringUtils.isNotEmpty(jnjUomConversionDto.getUnitDimension()))
			{
				jnjUomConversionModel.setVolumeUnit(getUnit(jnjUomConversionDto.getUnitDimension()));
			}
			jnjLaProductService.saveUomConversionModel(jnjUomConversionModel);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					Logging.BEGIN_OF_METHOD + Logging.HYPHEN + "No unit found in Hybirs for Unit with code: "
							+ jnjUomConversionDto.getUnitOfMeasure() + " .Throwing Business Exception.",
					JnjLAProductDataServiceImpl.class);
			throw new BusinessException("Dropping the record as Unit not found in Hybris.");
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		return jnjUomConversionModel;
	}


	/**
	 * This method is used to get unit on the basis of code.
	 *
	 * @param code
	 *           -String
	 * @return the unit
	 */
	private UnitModel getUnit(final String code)
	{
		final String errorMsg = " is not in the platform. ";
		final String methodName = "getUnit";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		UnitModel unit;
		if (StringUtils.isEmpty(code))
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Unit of measure is empty",
					JnjLAProductDataServiceImpl.class);
			return null;
		}
		try
		{
			String commercialCode = null;
			boolean primaryCodefound = false;
			final List<JnjCommercialUomConversionModel> jnJCommercialUomConversionModelList = jnjLaProductService
					.getCommercialUom(code);

			if (null == jnJCommercialUomConversionModelList || CollectionUtils.isEmpty(jnJCommercialUomConversionModelList))
			{
				JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
						UNIT_OF_MEASURE + code + errorMsg, JnjLAProductDataServiceImpl.class);
				throw new IllegalArgumentException(UNIT_OF_MEASURE + code + errorMsg);
			}

			if (jnJCommercialUomConversionModelList.size() > 1)
			{
				for (final JnjCommercialUomConversionModel jnjCommercialUomConversionModel : jnJCommercialUomConversionModelList)
				{
					if (StringUtils.equalsIgnoreCase(jnjCommercialUomConversionModel.getPrimaryCode(),
							Jnjlab2bcoreConstants.UpsertProduct.COMMERCIAL_PRIMARY_CODE))
					{
						commercialCode = jnjCommercialUomConversionModel.getCommercialCode();
						primaryCodefound = true;
						break;
					}
				}
				if (!primaryCodefound)
				{
					commercialCode = jnJCommercialUomConversionModelList.get(0).getCommercialCode();
				}
			}
			else if (jnJCommercialUomConversionModelList.size() == 1)
			{
				commercialCode = jnJCommercialUomConversionModelList.get(0).getCommercialCode();
			}
			final UnitModel unitModel = modelService.create(UnitModel.class);
			unitModel.setCode(commercialCode);
			unit = flexibleSearchService.getModelByExample(unitModel);
		}
		catch (final ModelNotFoundException | IllegalArgumentException exception)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					UNIT_OF_MEASURE + code + errorMsg + exception.getLocalizedMessage(), exception,
					JnjLAProductDataServiceImpl.class);
			throw new IllegalArgumentException(UNIT_OF_MEASURE + code + errorMsg);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD,
				JnjLAProductDataServiceImpl.class);
		return unit;

	}
}