package com.jnj.la.dataload.dao.impl;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;

import java.util.List;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.jnj.core.connector.JNJRSADBConnector;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.dao.JnjRSASalesOrgCustDao;


public class JnjRSASalesOrgCustDaoImpl implements JnjRSASalesOrgCustDao
{

	@Autowired
	protected JNJRSADBConnector rsaDBConnector;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	private static final String SQL_QUERY_SELECT_PART = "SELECT CUSTOMER_NUM, SALES_ORG, SECTOR, LAST_UPDATED_DATE FROM ";
	private static final String SQL_QUERY_ORDERBY_PART = " ORDER BY LAST_UPDATED_DATE";
	private static final Logger LOGGER = Logger.getLogger(JnjRSASalesOrgCustDaoImpl.class);



	@Override
	public List<JnjSalesOrgCustDTO> getSalesOrgDetails(final JnjIntegrationRSACronJobModel cronJobModel)
	{

		final String METHOD_NAME = "getSalesOrgDetails()";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjRSASalesOrgCustDaoImpl.class);
		List<JnjSalesOrgCustDTO> salesorgs = null;

		String query = SQL_QUERY_SELECT_PART + "HYBRIS.STG_SALES_ORG_ORDER_TYPE";

		query = buildRSASQLQuery(query, cronJobModel);

		query = query + SQL_QUERY_ORDERBY_PART;

		try
		{
			salesorgs = rsaDBConnector.getJdbcTemplate().query(query,
					new BeanPropertyRowMapper<JnjSalesOrgCustDTO>(JnjSalesOrgCustDTO.class));

		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SALES_ORG_CUST, METHOD_NAME,
					"Connect to RSA sales org view to perform query. Message: " + e.getMessage(), e, JnjRSASalesOrgCustDaoImpl.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, METHOD_NAME, Logging.END_OF_METHOD, JnjRSASalesOrgCustDaoImpl.class);
		return salesorgs;
	}



	private String buildRSASQLQuery(final String query, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String lastLogDateTime = interfaceOperationArchUtility.getLastSuccesfulStartTimeForJob(jobModel);

		LOGGER.info("Last Succesful record process time  ::: " + lastLogDateTime);

		String appendedQuery = query;

		if (lastLogDateTime != null)
		{
			appendedQuery = query + " where LAST_UPDATED_DATE > '" + lastLogDateTime + "'";
		}

		LOGGER.info("Final Query formed ::: " + appendedQuery);
		return appendedQuery;
	}


}
