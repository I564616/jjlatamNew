/**
 *
 */
package com.jnj.gt.dao.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJGTProductSalesOrgModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductPlantModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.product.JnjGTProductFeedDao;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.model.JnJGTIntProductPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;
import com.jnj.utils.CommonUtil;


/**
 * @author akash.rawat
 * 
 */

public class DefaultJnjGTProductFeedDao implements JnjGTProductFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductFeedDao.class);
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	

	private static final String PROD_SKU_CODE = "productSkuCode";

	private static final String SOURCE_SYS_ID = "srcSystem";

	private static final String BASE_MOD_CODE = "00";

	private static final String SEARCH_INT_PRODUCTS_QUERY = "SELECT {PK} FROM {JnjGTIntProduct as INTPROD JOIN RecordStatus as status ON {INTPROD:recordStatus} = "
			+ "{status:pk}} WHERE {status:code} = ?recordStatus  ";

	private static final String AND_SELECT_BASE_PRODUCTS = "AND (({INTPROD:MATERIALNUM} = {INTPROD:MATERIALBASENUM}  AND {INTPROD:SRCSYSTEM} IN (?consumerSrcSystems)) OR ({INTPROD:MATERIALNUM} = {INTPROD:PRODUCTCODE} AND {INTPROD:SRCSYSTEM} IN (?mddSrcSystems)))";

	//TODO private static final String AND_SELECT_BASE_PRODUCTS = "AND (({INTPROD:MATERIALNUM} = {INTPROD:PRODUCTCODE} AND {INTPROD:SRCSYSTEM} IN (?mddSrcSystems)))";

	private static final String AND_SELECT_MOD_PRODUCTS = "AND (({INTPROD:MATERIALNUM} != {INTPROD:MATERIALBASENUM}  AND {INTPROD:SRCSYSTEM} IN (?consumerSrcSystems)) OR ({INTPROD:MATERIALNUM} != {INTPROD:PRODUCTCODE} AND {INTPROD:SRCSYSTEM} IN (?mddSrcSystems)))";

	//TODO private static final String AND_SELECT_MOD_PRODUCTS = "AND (({INTPROD:MATERIALNUM} != {INTPROD:PRODUCTCODE} AND {INTPROD:SRCSYSTEM} IN (?mddSrcSystems)))";

	// excludes the base products.
	private static final String SEARCH_PRODUCTS_WITH_BASE_MAT_NO_PK = "SELECT {PK} FROM {JnJProduct} where ({materialBaseProduct}=?materialBaseProduct) and {catalogVersion}=?catalogVersion ORDER BY {firstShipEffectDate} DESC";

	// all those products for which product status code is inactive and the first effective date is less than 365 days and mod status is not equal to Not applicable
	private static final String GET_PRODUCT_MODEL_WITH_INACTIVE_STATUS_AND_EFFECTIVE_DATE = "SELECT {PK} FROM {JnJProduct} where {productStatusCode} IN (?productStatusCode) and (({materialStatusEffectDate}<CONVERT(DATETIME,?materialStatusEffectDate) and {modStatus} Not IN ({{select {pk} from {JnjGTModStatus} where {code}=?code}}) ) or ({materialStatusEffectDate}>=CONVERT(DATETIME,?materialStatusEffectDate)) ) and {catalogVersion}=?catalogVersion";
	// all those products for which product status code is inactive and the first effective date is less than 365 days and mod status is not equal to Not applicable
	private static final String GET_PRODUCT_MODEL_WITH_INACTIVE_STATUS_AND_EFFECTIVE_DATE_FOR_PCM = "SELECT {PK} FROM {JnJProduct} where {productStatusCode} IN (?productStatusCode) and (({materialStatusEffectDate}<CONVERT(DATETIME,?materialStatusEffectDate) and {pcmModStatus} Not IN ({{select {pk} from {JnjGTModStatus} where {code}=?code}}) ) or ({materialStatusEffectDate}>=CONVERT(DATETIME,?materialStatusEffectDate)) ) and {catalogVersion}=?catalogVersion";

	private static final String SELECT_PRODUCT_PLANT_LOCAL = "SELECT {PK} FROM {JnJGTIntProductLocalPlant} WHERE {PRODUCTSKUCODE}= ?productSkuCode AND {SRCSYSTEM}=?srcSystem";

	private static final String SELECT_EXISTING_PRODUCT_PLANT = "SELECT {PK} FROM {JnjGTProductPlant AS PLANT} WHERE {PLANT:CODE} = ?productPlantCode";

	private static final String SELECT_EXISTING_PRODUCT_SALES_ORG = "SELECT {PK} FROM {JnJGTProductSalesOrg AS SORG} WHERE {SORG:CODE} = ?productSalesOrgtCode";

	private static final String GET_PRD_STATUS_USING_BASE_MAT_NUM_QUERY = "select {productStatus} from {JnjGTIntProduct} where {materialBaseNum}=?materialBaseNum and {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}})";

	private static final String GET_INTERMEIDATE_CANADA_PRODUCTS = "select {pk} from {JnjGTIntProduct} where {srcSystem} in (?caSourceSysId) and {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}})";

	private static final String SELECT_CATEGORY_BY_NAME = "SELECT {CAT:PK} FROM {CATEGORY AS CAT} WHERE {CAT:JNJCUSTOMNAME} = ?jnjCustomName AND {CAT:CATALOGVERSION} = (?catalogVersion) ";

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public Collection<? extends ItemModel> getIntRecordsByProductSkuCode(final String typeCode, final String productCode,
			final String sourceSysId)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(typeCode).append("} WHERE {")
				.append(PROD_SKU_CODE).append("} = ?").append(PROD_SKU_CODE).append(" AND {").append(SOURCE_SYS_ID).append("} = ?")
				.append(SOURCE_SYS_ID);

		queryParams.put(PROD_SKU_CODE, productCode);
		queryParams.put(SOURCE_SYS_ID, sourceSysId);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Query for fetching intermediate table : " + flexibleSearchQuery);
		}

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();

		return result;
	}

	@Override
	public Collection<JnjGTIntProductUnitModel> getIntProductUnitRecords(final String productCode, final String regionCode,
			final boolean isMddProduct)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();

		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntProductUnitModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntProductUnitModel.PRODUCTSKUCODE).append("} = ?productSkuCode");
		queryParams.put(JnjGTIntProductUnitModel.PRODUCTSKUCODE, productCode);

		if (isMddProduct)
		{
			searchQuery.append(" AND {").append(JnjGTIntProductUnitModel.REGIONCODE).append("} = ?regionCode");
			queryParams.put(JnjGTIntProductUnitModel.REGIONCODE, regionCode);
		}

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Query for fetching Product Unit Intermediate records : " + flexibleSearchQuery.getQuery());
		}

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();
		return result;
	}

	@Override
	public Collection<JnjGTIntProductModel> getIntMaterialBaseProducts(final boolean isBaseProductSearch)
	{
		final List<String> mddSourceSystemIds = JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> consumerSourceSystemIds = JnJCommonUtil.getValues(
				Jnjgtb2binboundserviceConstants.CONSUMER_USA_SOURCE_SYS_ID, Jnjb2bCoreConstants.SYMBOl_COMMA);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SEARCH_INT_PRODUCTS_QUERY);
		searchQuery.append((isBaseProductSearch) ? AND_SELECT_BASE_PRODUCTS : AND_SELECT_MOD_PRODUCTS);

		final Map queryParams = new HashMap();
		queryParams.put("consumerSrcSystems", consumerSourceSystemIds);
		queryParams.put("mddSrcSystems", mddSourceSystemIds);
		queryParams.put(JnjGTIntProductModel.RECORDSTATUS, RecordStatus.PENDING.getCode());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIntMaterialBaseProducts()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();
		return result;
	}

	@Override
	public Collection<JnjGTIntProductModel> getIntCanadaProducts()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIntCanadaProducts()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final List<String> consumerSourceSystemIds = JnJCommonUtil.getValues(
				Jnjgtb2binboundserviceConstants.CONSUMER_CANADA_SOURCE_SYS_ID, Jnjb2bCoreConstants.SYMBOl_COMMA);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(GET_INTERMEIDATE_CANADA_PRODUCTS);

		final Map queryParams = new HashMap();
		queryParams.put("caSourceSysId", consumerSourceSystemIds);
		queryParams.put("code", RecordStatus.PENDING.getCode());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIntCanadaProducts()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getIntCanadaProducts()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public List<JnjGTIntProductRegModel> getIntProductRegionRecords(final String productCode, final String sourceSysId)
	{
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntProductRegModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntProductRegModel.PRODUCTSKUCODE).append("} = ?productSkuCode AND {")
				.append(JnjGTIntProductRegModel.REGIONCODE).append("} = upper(?regionCode) AND {")
				.append(JnjGTIntProductRegModel.SRCSYSTEM).append("} = ?srcSystem");

		final Map queryParams = new HashMap();
		queryParams.put(JnjGTIntProductRegModel.REGIONCODE, Jnjgtb2binboundserviceConstants.Product.NA_REGION_CODE);
		queryParams.put(JnjGTIntProductRegModel.PRODUCTSKUCODE, productCode);
		queryParams.put(JnjGTIntProductRegModel.SRCSYSTEM, sourceSysId);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();
		return result;
	}

	@Override
	public List<JnjGTIntProductDescModel> getIntProductDescRecords(final String productCode, final String sourceSysId)
	{
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjGTIntProductDescModel._TYPECODE)
				.append("} WHERE {").append(JnjGTIntProductDescModel.PRODUCTSKUCODE).append("} = ?productSkuCode AND {")
				.append(JnjGTIntProductDescModel.SRCSYSTEM).append("} = ?srcSystem");

		final Map queryParams = new HashMap();
		queryParams.put(JnjGTIntProductDescModel.PRODUCTSKUCODE, productCode);
		queryParams.put(JnjGTIntProductDescModel.SRCSYSTEM, sourceSysId);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getJnJProductModelsUsingMaterialBaseProductPK(final String materialBaseNoPk,
			final CatalogVersionModel catalogVersionModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnJProductModelsUsingMaterialBaseProductPK()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SEARCH_PRODUCTS_WITH_BASE_MAT_NO_PK);
		final Map queryParams = new HashMap();
		queryParams.put(JnJProductModel.MATERIALBASEPRODUCT, materialBaseNoPk);
		queryParams.put(JnJProductModel.CATALOGVERSION, catalogVersionModel.getPk());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnJProductModelsUsingMaterialBaseProductPK()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query "
					+ flexibleSearchQuery);
		}
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(flexibleSearchQuery).getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnJProductModelsUsingMaterialBaseProductPK()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnJProductModel> getProductModelForExclusionFeed(final CatalogVersionModel catalogVersionModel,
			final boolean isCallForPCM)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForExclusionFeed()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final StringBuilder searchQuery = new StringBuilder();
		if (isCallForPCM)
		{
			searchQuery.append(GET_PRODUCT_MODEL_WITH_INACTIVE_STATUS_AND_EFFECTIVE_DATE_FOR_PCM);
		}
		else
		{
			searchQuery.append(GET_PRODUCT_MODEL_WITH_INACTIVE_STATUS_AND_EFFECTIVE_DATE);
		}


		final Map queryParams = new HashMap();
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		final String materialStatusEffectDate = new SimpleDateFormat(jnjCommonUtil.getDBDateFormat())
				.format(cal.getTime());
		queryParams.put("code", JnjGTModStatus.NOTAPPLICABLE.getCode());
		queryParams.put(JnJProductModel.MATERIALSTATUSEFFECTDATE, materialStatusEffectDate);
		queryParams.put(JnJProductModel.CATALOGVERSION, catalogVersionModel.getPk());
		queryParams.put("productStatusCode", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.Product.INACTIVE_STATUS_CODE,
				Jnjgtb2binboundserviceConstants.COMMA_STRING));
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForExclusionFeed()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List<JnJProductModel> result = getFlexibleSearchService().<JnJProductModel> search(flexibleSearchQuery).getResult();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductModelForExclusionFeed()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;

	}

	@Override
	public Collection<JnJGTIntProductLocalPlantModel> getIntProdPlantLocalRecords(final String productCode,
			final String srcSystemId)
	{
		CommonUtil.logMethodStartOrEnd("Product Local Feed", "getIntProdPlantLocalRecords()", Logging.START_TIME, LOGGER);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT_PRODUCT_PLANT_LOCAL);

		final Map queryParams = new HashMap();
		queryParams.put(JnJGTIntProductPlantModel.PRODUCTSKUCODE, productCode);
		queryParams.put(JnJGTIntProductPlantModel.SRCSYSTEM, srcSystemId);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		CommonUtil.logDebugMessage("Product Local Feed", "getIntProdPlantLocalRecords()", "QUERY :: " + searchQuery.toString(),
				LOGGER);

		final List result = getFlexibleSearchService().search(flexibleSearchQuery).getResult();

		CommonUtil.logMethodStartOrEnd("Product Local Feed", "getIntProdPlantLocalRecords()", Logging.END_OF_METHOD, LOGGER);
		return result;
	}

	@Override
	public List<String> getProductCodesUsingIntProductModel(final String materialBaseNum)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductCodesUsingIntProductModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final String query = GET_PRD_STATUS_USING_BASE_MAT_NUM_QUERY;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		fQuery.setResultClassList(resultClassList);
		final Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("code", RecordStatus.PENDING.getCode());
		queryParams.put("materialBaseNum", materialBaseNum);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductCodesUsingIntProductModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + fQuery);
		}
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		final List<String> resultList = result.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProductCodesUsingIntProductModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return resultList;
	}

	/**
	 *
	 */
	@Override
	public JnjGTProductPlantModel getExistingProductPlantRecord(final String productPlantCode)
	{
		CommonUtil.logMethodStartOrEnd("Product Feed", "getExistingProductPlantRecords()", Logging.START_TIME, LOGGER);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT_EXISTING_PRODUCT_PLANT);

		final Map queryParams = new HashMap();
		queryParams.put("productPlantCode", productPlantCode);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		CommonUtil
				.logDebugMessage("Product Feed", "getExistingProductPlantRecords()", "QUERY :: " + searchQuery.toString(), LOGGER);
		final List<JnjGTProductPlantModel> result = getFlexibleSearchService().<JnjGTProductPlantModel> search(flexibleSearchQuery)
				.getResult();

		CommonUtil.logMethodStartOrEnd("Product Feed", "getExistingProductPlantRecords()", Logging.END_OF_METHOD, LOGGER);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 *
	 */
	@Override
	public JnJGTProductSalesOrgModel getExistingProductSalesOrgRecord(final String productSalesOrgCode)
	{
		CommonUtil.logMethodStartOrEnd("Product Feed", "getExistingProductSalesOrgRecord()", Logging.START_TIME, LOGGER);

		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT_EXISTING_PRODUCT_SALES_ORG);

		final Map queryParams = new HashMap();
		queryParams.put("productSalesOrgtCode", productSalesOrgCode);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);

		CommonUtil.logDebugMessage("Product Feed", "getExistingProductSalesOrgRecord()", "QUERY :: " + searchQuery.toString(),
				LOGGER);
		final List<JnJGTProductSalesOrgModel> result = getFlexibleSearchService().<JnJGTProductSalesOrgModel> search(
				flexibleSearchQuery).getResult();

		CommonUtil.logMethodStartOrEnd("Product Feed", "getExistingProductSalesOrgRecord()", Logging.END_OF_METHOD, LOGGER);
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CategoryModel getcategoryByCustomName(final CatalogVersionModel catalogVersion, final String customName)
	{
		CommonUtil.logMethodStartOrEnd("Product Feed", "getcategoryByName()", Logging.START_TIME, LOGGER);
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append(SELECT_CATEGORY_BY_NAME);

		final Map queryParams = new HashMap();
		queryParams.put(CategoryModel.JNJCUSTOMNAME, customName);
		queryParams.put(CategoryModel.CATALOGVERSION, catalogVersion);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<CategoryModel> result = getFlexibleSearchService().<CategoryModel> search(flexibleSearchQuery).getResult();
		return result.size() > 0 ? result.get(0) : null;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}
}
