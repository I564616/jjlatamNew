package com.jnj.la.core.dao.order.impl;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.dao.order.impl.OrderRowMapper;
import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.order.JnjRSAOrderDao;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import com.jnj.la.core.util.JnJLaCronjobUtil;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;


public class JnjRSAOrderDaoImpl implements JnjRSAOrderDao
{
	
	private static final Logger LOGGER = Logger.getLogger(JnjRSAOrderDaoImpl.class);

	protected JNJRSADBConnector rsaDBConnector;
	
	protected JnJLaCronjobUtil jnjLaCronjobUtil;

	@Autowired
	private JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;
	
	protected ConfigurationService configurationService;

	protected final String ORDER_QUERY = " OH.REC_TIMESTAMP,OH.SRC_SYSTM,OH.BUSINESS_SECTOR,OH.ORDER_NUM,OH.PO_NUM,OH.HYBRIS_ORDER_NUM,OH.ORDER_TYPE,"
		+ "OH.SOLD_TO_CUST_NUM,OH.SHIP_TO_CUST_NUM,OH.PAY_FROM_CUST_NUM,OH.REQ_DELV_DT,OH.NET_VAL_ORD_TOT,OH.ORDER_CREATE_DT,OH.FORBIDDEN_SALES,"
		+ "OH.CONTRACT_REF,OH.COMPLETE_DELV,OH.SALES_ORG,OH.DIST_CHANNEL,OH.DIVISION,OH.OVERALL_STATUS,OH.DELIVERY_STATUS,OH.REJECTION_STATUS,"
		+ "OH.CREDIT_STATUS,OH.HEA_DATA_CM_DT,OH.DELIVERY_BLOCK,OH.ORDER_CHANNEL,OH.INVOICE_STATUS,OH.ALT_CHANNEL_ORDER_NUM,OH.SHIP_TO_CUST_NAME,"
		+ "OH.SHIP_STREET_1,OH.SHIP_STREET_2,OH.SHIP_STREET_3,OH.SHIP_STREET_4,OH.SHIP_CITY,OH.SHIP_STATE,OH.SHIP_COUNTRY,OH.SHIP_POSTAL_CD,"
		+ "OH.SHIP_ATTENTION,OH.BILL_TO_CUST_NUM,OH.CURRENCY,OH.ORD_INCOMPLETE_STATUS,OH.ORD_REASON_CD,OH.SHIP_TO_PO_NUM,OH.DROP_SHIP_IND,OH.BILL_BLOCK,"
		+ "OH.PRICE_DT,OH.CREDIT_REP_CD,OH.SURGEON_NAME,OH.SURGERY_TYPE,OH.SURGERY_NUM,OH.PATIENT_NAME,OH.CASE_DT,OH.RET_DIST_DEBIT_MEMO,"
		+ "OH.CLASS_OF_TRADE,OH.LAST_UPDATED_DATE,OL.ORD_LINE_NUM,OL.ITEM_CATEGORY,OL.HIGH_LVL_ITEM_NUM,"
		+ "OL.ORDERED_MATERIAL_NUM,OL.ORDERED_UOM,OL.SALES_UOM,OL.ORDERED_QTY,OL.UNIT_PRICE,OL.NET_VALUE,OL.TAX_AMT,OL.FREIGHT_FEES,OL.EXPDITED_FEES,"
		+ "OL.DISCOUNTS_PROMO,OL.GROSS_PRICE,OL.IND_CUST_ACCOUNT,OL.OVERALL_STATUS AS LINE_OVERALL_STATUS,OL.DELV_STATUS,OL.RSN_FOR_REJ,OL.INVOICE_STATUS AS LINE_INVOICE_STATUS,OL.LOCAL_TAXES,"
		+ "OL.INTL_FREIGHT,OL.INSURANCE,OL.HANDLING_FEE,OL.DROP_SHIP_FEE,OL.MIN_ORDER_FEE,OL.GTS_HOLD,OL.CURRENCY,OL.SRC_SYSTM,OL.BUSINESS_SECTOR,"
		+ "OL.PLANT,OL.MATERIAL_ENTERED,OL.BATCH_NUM,OL.PRODUCT_DIVISION,OL.SUB_TOTAL_1,OL.SUB_TOTAL_2,OL.SUB_TOTAL_3,OL.SUB_TOTAL_4,OL.SUB_TOTAL_5,"
		+ "OL.SUB_TOTAL_6,OL.ROUTE,OL.RETURN_INV_NUM,OL.GTIN,OL.GTIN_ENTERED,OS.ORD_LINE_NUM,OS.SCHEDULE_LINE_NUM,OS.ROUNDED_QUANTITY,OS.CONFIRMED_QTY,"
		+ "OS.SCHED_DELIVERY_DT,OS.SRC_SYSTM,OS.BUSINESS_SECTOR,OS.SCHED_SHIP_DT, OS.POD, OS.CEDD FROM HYBRIS.STG_ORDER_HEADER AS OH INNER JOIN "
		+ "HYBRIS.STG_ORDER_LINE AS OL ON OH.ORDER_NUM = OL.ORDER_NUM INNER JOIN HYBRIS.STG_ORDER_LINE_SCH AS OS ON OL.ORDER_NUM = OS.ORDER_NUM AND OL.ORD_LINE_NUM = OS.ORD_LINE_NUM";

	
	public JNJRSADBConnector getRsaDBConnector() {
		return rsaDBConnector;
	}
	
	public void setRsaDBConnector(JNJRSADBConnector rsaDBConnector) {
		this.rsaDBConnector = rsaDBConnector;
	}

	public JnJLaCronjobUtil getJnjLaCronjobUtil() {
		return jnjLaCronjobUtil;
	}

	public void setJnjLaCronjobUtil(JnJLaCronjobUtil jnjLaCronjobUtil) {
		this.jnjLaCronjobUtil = jnjLaCronjobUtil;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@Override
	public List<JnjOrderDTO> getOrders(final JnjIntegrationRSACronJobModel cronJob)
	{
		final String methodName = "getOrders()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, JnjRSAOrderDaoImpl.class);

		List<JnjOrderDTO> jnjOrders = null;
		final String SELECT = getSelectStatement() + ORDER_QUERY;
		final String query = buildRSASQLQuery(SELECT, cronJob);

		Map<String,List<?>> resultMap= new HashMap<>();
		
		int retryAttempt = 0;
		int connectionWaitPeriod = 0;
		try {
			retryAttempt = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.RETRY_ATTEMPT));
			connectionWaitPeriod = Integer.parseInt(configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CONNECTION_WAIT_PERIOD_TIME));
			
		} catch (final NumberFormatException e) {
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, Logging.BEGIN_OF_METHOD,
					"Can not convert string to integer: " + e.getMessage(), e, JnjRSAOrderDaoImpl.class);
		}
		
		
		try {
			
			for (int count = 0; count < retryAttempt; count++) {

				if(count != 0)
				{
					TimeUnit.SECONDS.sleep(connectionWaitPeriod); 
				}
				final boolean reconnectDB = jnjLaCronjobUtil.getResultsFromDB(query, resultMap,JnjOrderDTO.class);

				LOGGER.info("ReconnectDB value: "+reconnectDB);
				
				if (!reconnectDB) {
					break;
				}
			}
			
			LOGGER.info("List of Order records: " + resultMap.get(JnjOrderDTO.class.getSimpleName()));
			
			if(resultMap.get(JnjOrderDTO.class.getSimpleName()) instanceof List<?>){
				jnjOrders=(List<JnjOrderDTO>)resultMap.get(JnjOrderDTO.class.getSimpleName());
			}
			
			
		} catch (final EmptyResultDataAccessException e) {
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "No orders returned from RSA.", JnjRSAOrderDaoImpl.class);
		} catch (final Exception e) {
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Connecting to RSA order details view to perform query error. Message: " + e.getMessage(), e, JnjRSAOrderDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, JnjRSAOrderDaoImpl.class);
		return jnjOrders;
	}

	private String buildRSASQLQuery(final String syncOrderQuery, final JnjIntegrationRSACronJobModel jobModel) {
		final String methodName = "buildRSASQLQuery";

		if (Boolean.parseBoolean(Config.getParameter(Jnjlab2bcoreConstants.Order.SYNC_ORDER_TEST_SETUP_REQUIRED))) {
			final String testDataCriteria = JnjLaCoreUtil.getCommaSeparatedValuesForQueryConditions(Jnjlab2bcoreConstants.Order.SYNC_ORDER_TEST_DATA);

			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Order Set up for testing: " + testDataCriteria, JnjRSAOrderDaoImpl.class);
			final String finalQuery = syncOrderQuery + " WHERE OH.ORDER_NUM IN (" + testDataCriteria + ")";
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Final Query formed: " + finalQuery, JnjRSAOrderDaoImpl.class);

			return finalQuery;
		}

		String queryConstraints = interfaceOperationArchUtility.buildRSAQueryOrderLastUpdatedDate(jobModel, "WHERE (OH.LAST_UPDATED_DATE");
		if (!queryConstraints.isEmpty()) {
			queryConstraints = interfaceOperationArchUtility.buildRSAQueryLastUpdatedEndDate(queryConstraints, Jnjlab2bcoreConstants.Order.SYNC_ORDER_LAST_UPDATED_END_DATE, "OH.LAST_UPDATED_DATE");
			if(Config.getBoolean(Jnjlab2bcoreConstants.Order.USE_CREATED_DATE, false))
			{
				queryConstraints = interfaceOperationArchUtility.buildRSAQueryOrderCreationDate(queryConstraints, "OH.ORDER_CREATE_DT");
			}
			queryConstraints = interfaceOperationArchUtility.buildRSAQuerySalesOrgList(queryConstraints, Jnjlab2bcoreConstants.Order.SYNC_ORDER_SALES_ORGS_LIST, "OH.SALES_ORG");
		}

		final String finalQuery = syncOrderQuery + queryConstraints;

		JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Final Query: " + finalQuery, JnjRSAOrderDaoImpl.class);

		return finalQuery;
	}

	private String getSelectStatement()
	{

		final int maxResult = Config.getInt(Jnjlab2bcoreConstants.Order.SYNC_ORDER_MAX_RESULT_SET, 1000);
		if (maxResult > 0)
		{
			return "SELECT TOP " + maxResult + " ";
		}
		return "SELECT ";
	}
}
