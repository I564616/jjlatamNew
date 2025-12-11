package com.jnj.la.core.dao.product.impl;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.util.Config;

import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.impl.JnjRSAOrderDaoImpl;
import com.jnj.la.core.dao.product.JnjRSAProductDao;
import com.jnj.la.core.dto.product.JnjRSAProductDTO;
import com.jnj.la.core.dto.product.JnjRSAProductDescriptionDTO;
import com.jnj.la.core.dto.product.JnjRSAProductSalesOrgDTO;
import com.jnj.la.core.dto.product.JnjRSAProductUnitDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.core.util.JnJLaCronjobUtil;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.text.SimpleDateFormat;

public class JnjRSAProductDaoImpl implements JnjRSAProductDao
{

	private static final Logger LOGGER = Logger.getLogger(JnjRSAProductDaoImpl.class);
	protected static final String RSA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private JNJRSADBConnector rsaDBConnector;
	
	private JnJLaCronjobUtil jnjLaCronjobUtil;
	
	protected ConfigurationService configurationService;
	

	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;
	
	
	protected static final String PRODUCT_QUERY = "SELECT MATERIAL_NUM,PROD_CD,PROD_MOD_CD,MATERIAL_STATUS,MIN_PROD_GRP_CD,ECATALOG_DESC,ECATALOG_LONG_DESC,GHEX_IND,HAZMAT_CD,"
			+ "FRANC_MJR_PRD_GRP_CD,DIVISION,DIVISION_NAME,LEGCY_PROD_CD,JNJ_PORTAL_IND,USA_OTC_READY_IND,GPC_CD,NET_CONTENT,MFG_COMP_CD,DEA_REG_IND,FIRST_SHIP_EFFECT_DT,"
			+ "PUBLISH_IND,MATERIAL_BASE_NUM,BASE_UNIT_CD,FRANCHISE_CD,FRANCHISE_NAME,CATEGORY_CD,CATEGORY_NAME,BRAND_CD,BRAND_NAME,SUB_BRAND_CD,SUB_BRAND_NAME,FUNC_NAME_CD,"
			+ "FUNC_NAME_NAME,SOURCE_SYS_ID,BUSINESS_SECTOR,REC_TIMESTAMP,NEW_PROD_START_DT,MATERIAL_STATUS_EFFECT_DT,KIT_IND,GDS_IND,SUBFRANCHISE_CD,SUBFRANCHISE_NAME,"
			+ "MAJORGROUP_CD,MAJORGROUP_NAME,PROD_DESC2,MATERIAL_TYPE,ORIGIN_COUNTRY, LAST_UPDATED_DATE FROM HYBRIS.STG_PRODUCT";


	protected static final String PRODUCT_DESCRIPTION_QUERY = "SELECT MATERIAL_NUM,PROD_DESC,PROD_NAME,PROD_DESC_LANG_CD,PROD_DESC_LANG_NAME,SOURCE_SYS_ID,BUSINESS_SECTOR,"
			+ "REC_TIMESTAMP,INSERTIONTIMESTAMP,MIGRATIONSTATUS,PROD_DESC2 FROM HYBRIS.STG_PRODUCT_DESC WHERE MATERIAL_NUM IN (%s)";

	protected static final String PRODUCT_SALESORG_QUERY = "SELECT SALES_ORG,DIST_CHANNEL,COLD_CHAIN_PRODUCT,DELV_UNIT,STATUS,MATERIAL_NUM,SALES_UNIT,BASE_UNIT,SOURCE_SYS_ID,"
			+ "BUSINESS_SECTOR,ECOMMERCE_FLAG FROM HYBRIS.STG_PRODUCT_SORG WHERE MATERIAL_NUM IN (%s)";

	protected static final String PRODUCT_UNIT_QUERY = "SELECT MATERIAL_NUM,ALT_UNIT_CD,EANUPC_TYPE_CD,GTIN,UOM_CONV_DENOM,UOM_CONV_NUMERATOR,EAN_UPC_NF,ALT_UNIT_NAME,"
			+ "SELL_UOM_IND,SHIP_UOM_IND,UOM_TYPE_CD,LIN_UOM_CD,LIN_UOM_NAME,DEPTH,HEIGHT,WIDTH,VOL_UOM_CD,VOL_QTY,WEIGHT_UOM_CD,WEIGHT_UOM_NAME,GROSS_WEIGHT_QTY,"
			+ "ALT_UOM_NAME,SOURCE_SYS_ID,BUSINESS_SECTOR,REC_TIMESTAMP,UNIT_DIMENSION,UNIT_MEASURE FROM HYBRIS.STG_PRODUCT_UNIT WHERE MATERIAL_NUM IN (%s)";

	protected static final String PRODUCT_UNIQUE_DIVISIONS_QUERY = "SELECT DISTINCT DIVISION FROM HYBRIS.STG_PRODUCT";

	protected static final String UPPER_TIME_LIMIT_QUERY ="SELECT MAX (LAST_UPDATED_DATE) FROM HYBRIS.STG_PRODUCT";

	public JNJRSADBConnector getRsaDBConnector() {
		return rsaDBConnector;
	}

	public void setRsaDBConnector(final JNJRSADBConnector rsaDBConnector) {
		this.rsaDBConnector = rsaDBConnector;
	}

	public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}

	public void setJnjLaCronjobUtil(final JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public List<JnjRSAProductDTO> getProducts(final Date lowerDate, final Date upperDate, final JnjIntegrationRSACronJobModel cronJob, final String sector)
	{

		final String methodName = "getProducts()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjRSAProductDaoImpl.class);
		List<JnjRSAProductDTO> products = null;
		Map<String,List<?>> resultMap= new HashMap<>();
		
		final String query = buildRSASQLQuery(PRODUCT_QUERY, cronJob, lowerDate, upperDate);
		String finalQuery = query + " AND BUSINESS_SECTOR="+"'"+sector+"'" + " ORDER BY DIVISION";
		
		int retryAttempt=0;
		int connectionWaitPeriod=0;

		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.RETRY_ATTEMPT));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
		} catch (final NumberFormatException e) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"Can not convert string to integer: " + e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}
		
		try
		{
			for (int count = 0; count < retryAttempt; count++) {
				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				final boolean reconnectDB = jnjLaCronjobUtil.getResultsFromDB(finalQuery, resultMap,JnjRSAProductDTO.class);

				LOGGER.info("ReconnectDB value: "+reconnectDB);
				
				if (!reconnectDB) {
					break;
				}
			}
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_PRODUCT_NAME, methodName,
					"Connect to RSA material view to perform query. Message: " + e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}
				
		if(resultMap.get(JnjRSAProductDTO.class.getSimpleName()) instanceof List<?>)
		{
			LOGGER.info("&&&&&&&&&&&&&&&&&&&&&: Instance of Product DTO");
			products=(List<JnjRSAProductDTO>)resultMap.get(JnjRSAProductDTO.class.getSimpleName());
		}
		
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjRSAProductDaoImpl.class);
		return products;
	}

	@Override
	public List<JnjRSAProductSalesOrgDTO> getProductSalesOrg(final List<String> materialNum)
	{
		final String methodName = "getProductSalesOrg()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjRSAProductDaoImpl.class);
		List<JnjRSAProductSalesOrgDTO> jnjProductSalesOrgs = null;
	    
		String inSql = String.join(",", Collections.nCopies(materialNum.size(), "?"));
		
		try
		{
			jnjProductSalesOrgs = rsaDBConnector.getJdbcTemplate().query(String.format(PRODUCT_SALESORG_QUERY, inSql), new BeanPropertyRowMapper<JnjRSAProductSalesOrgDTO>(JnjRSAProductSalesOrgDTO.class), 
					materialNum.toArray());
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjRSAProductDaoImpl.class);
		return jnjProductSalesOrgs;
	}

	@Override
	public List<JnjRSAProductDescriptionDTO> getProductDescription(final List<String> materialNum)
	{
		final String methodName = "getProductDescription()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjRSAProductDaoImpl.class);
		List<JnjRSAProductDescriptionDTO> jnjProductDescriptions = null;

		String inSql = String.join(",", Collections.nCopies(materialNum.size(), "?"));
		
		try
		{
			jnjProductDescriptions = rsaDBConnector.getJdbcTemplate().query(String.format(PRODUCT_DESCRIPTION_QUERY, inSql), new BeanPropertyRowMapper<JnjRSAProductDescriptionDTO>(JnjRSAProductDescriptionDTO.class),
					materialNum.toArray());
			
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjRSAProductDaoImpl.class);
		return jnjProductDescriptions;
	}

	@Override
	public List<JnjRSAProductUnitDTO> getProductUomConversion(final List<String> materialNum)
	{
		final String methodName = "getProductUomConversion()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjRSAProductDaoImpl.class);
		List<JnjRSAProductUnitDTO> jnjProductUnits = null;

		String inSql = String.join(",", Collections.nCopies(materialNum.size(), "?"));
		
		try
		{
			jnjProductUnits = rsaDBConnector.getJdbcTemplate().query(String.format(PRODUCT_UNIT_QUERY, inSql), new BeanPropertyRowMapper<JnjRSAProductUnitDTO>(JnjRSAProductUnitDTO.class), 
					materialNum.toArray());
			
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjRSAProductDaoImpl.class);
		return jnjProductUnits;
	}

	@Override
	public List<String> getUniqueDivisionsFromRSA(final Date lowerDate, final Date upperDate, final String sector) {
		final String methodName = "getUniqueDivisionsFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.BEGIN_OF_METHOD, JnjRSAProductDaoImpl.class);

		List<String> divisionsList = null;
		StringBuilder query = new StringBuilder(PRODUCT_UNIQUE_DIVISIONS_QUERY + " WHERE BUSINESS_SECTOR=" + "'"+sector+"'");
		String finalQuery = query.append(interfaceOperationArchUtility.buildRSAQueryForLastUpdatedDate(lowerDate, upperDate," AND LAST_UPDATED_DATE")).toString();

		LOGGER.info("Final query for pullProductDivisionsFromRSA: " + finalQuery);
		try
		{
			divisionsList = rsaDBConnector.getJdbcTemplate().queryForList(finalQuery, String.class);
			LOGGER.info("List of unique divisions available for the given sector : " + sector + " are: " + divisionsList);
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_PRODUCT_NAME, methodName, Logging.END_OF_METHOD, JnjRSAProductDaoImpl.class);
		return divisionsList;
	}

	@Override
	public Date getLastUpdatedDateForLatestRecord() {
		final String methodName = "getLastUpdatedDateForLatestRecord";
		final SimpleDateFormat sdf = new SimpleDateFormat(RSA_DATE_FORMAT);
		Date upperDate = null;
		try {
			String upperTime =  rsaDBConnector.getJdbcTemplate().queryForObject(UPPER_TIME_LIMIT_QUERY, String.class);
			upperDate = sdf.parse(upperTime);
		} catch (final Exception e) {
			JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_PRODUCT_NAME, methodName, e.getMessage(), e, JnjRSAProductDaoImpl.class);
		}
		return upperDate;
	}

	private String buildRSASQLQuery(final String productQuery, final JnjIntegrationRSACronJobModel jobModel, final Date lowerDate, final Date upperDate)
	{
		final String methodName = "buildRSASQLQuery";
		String finalQuery;
		final String isTestSetupRequired = Config.getParameter(Jnjlab2bcoreConstants.UpsertProduct.IS_PRODUCT_TEST_SETUP_REQUIRED);
		if (Boolean.parseBoolean(isTestSetupRequired))
		{
			final String testDataCriteria = JnjLaCoreUtil
					.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.UpsertProduct.PRODUCT_TEST_DATA);

			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Product Set up for testing: " + testDataCriteria,
					JnjRSAProductDaoImpl.class);

			finalQuery = productQuery + " WHERE MATERIAL_NUM IN (" + testDataCriteria + ")";
			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Final Query formed: " + finalQuery,
					JnjRSAOrderDaoImpl.class);

			return finalQuery;
		}

		String queryConstraints = interfaceOperationArchUtility.buildRSAQueryLastUpdatedDateForProduct(lowerDate, upperDate, jobModel, "WHERE LAST_UPDATED_DATE");
		// Verify whether there is a initial date in order to set a end date.
		if (!queryConstraints.isEmpty())
		{
			queryConstraints = interfaceOperationArchUtility.buildRSAQueryLastUpdatedEndDate(queryConstraints,
					Jnjlab2bcoreConstants.UpsertProduct.PRODUCT_LAST_UPDATED_END_DATE, "LAST_UPDATED_DATE");
		}

		finalQuery = productQuery + queryConstraints;

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, methodName, "Final Query formed: " + finalQuery,
				JnjRSAProductDaoImpl.class);

		return finalQuery;
	}

}
