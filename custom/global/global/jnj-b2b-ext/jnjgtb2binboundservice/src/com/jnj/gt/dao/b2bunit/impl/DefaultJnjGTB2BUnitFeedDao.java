/**
 *
 */
package com.jnj.gt.dao.b2bunit.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.B2BUnit;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.b2bunit.JnjGTB2BUnitFeedDao;
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.model.JnjGTIntPartnerFuncModel;
import com.jnj.gt.model.JnjGTIntSalesOrgModel;



/**
 * The JnjGTB2BUnitFeedDaoImpl class contains all those methods which are dealing with customer related intermediate
 * model and it has definition of all the methods which are defined in the JnjGTB2BUnitFeedDao interface.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTB2BUnitFeedDao extends AbstractItemDao implements JnjGTB2BUnitFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTB2BUnitFeedDao.class);

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntB2BUnitModel> getJnjGTIntB2BUnitModel(final RecordStatus recordStatus, final String indicator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String query = null;
		ServicesUtil.validateParameterNotNull(recordStatus, " processed Indentfier must not be null");
		ServicesUtil.validateParameterNotNull(indicator, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		// On the basis of indicator, particular query is executed.
		if (B2BUnit.SOLD_TO_INDC.equalsIgnoreCase(indicator))
		{
			query = B2BUnit.SOLD_TO_INDC_QUERY;

			/*
			 * queryParams.put("sourceSysId",
			 * JnJCommonUtil.getValues(Jnjnab2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
			 * Jnjnab2binboundserviceConstants.COMMA_STRING));
			 */

			queryParams.put("mddSourceSysId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
					Jnjgtb2binboundserviceConstants.COMMA_STRING));

		}
		else if (B2BUnit.SHIP_TO_INDC.equalsIgnoreCase(indicator))
		{
			query = B2BUnit.SHIP_TO_INDC_QUERY;

			queryParams.put("mddSourceSysId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
					Jnjgtb2binboundserviceConstants.COMMA_STRING));
			/*
			 * queryParams.put("sourceSysId",
			 * JnJCommonUtil.getValues(Jnjnab2binboundserviceConstants.CONS_SOURCE_SYS_ID_FOR_CUST,
			 * Jnjnab2binboundserviceConstants.COMMA_STRING));
			 */
			/*
			 * queryParams.put("sourceSysId",
			 * JnJCommonUtil.getValues(Jnjnab2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
			 * Jnjnab2binboundserviceConstants.COMMA_STRING));
			 */
		}
		/*
		 * else if (B2BUnit.PAY_FROM_INDC.equalsIgnoreCase(indicator)) { query = B2BUnit.PAY_FROM_INDC_QUERY; }
		 */
		else if (B2BUnit.BILL_TO_INDC.equalsIgnoreCase(indicator))
		{
			query = B2BUnit.BILL_TO_INDC_QUERY;

			//queryParams.put("billToInd", "BLL");
			//queryParams.put("primaryBillToInd", "BLP");
			queryParams.put("mddSourceSysId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
					Jnjgtb2binboundserviceConstants.COMMA_STRING));

		}
		queryParams.put("code", recordStatus.getCode());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntB2BUnitModel Query " + fQuery);
		}

		final List<JnjGTIntB2BUnitModel> result = getFlexibleSearchService().<JnjGTIntB2BUnitModel> search(fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelForBillTo(final RecordStatus recordStatus,
			final String indicator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String query = null;
		ServicesUtil.validateParameterNotNull(recordStatus, " processed Indentfier must not be null");
		ServicesUtil.validateParameterNotNull(indicator, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		if (B2BUnit.BILL_TO_INDC.equalsIgnoreCase(indicator))
		{
			query = B2BUnit.BILL_TO_INDC_QUERY_EMEA;

			queryParams.put("billToInd", "BLL");
			queryParams.put("primaryBillToInd", "BLP");
			queryParams.put("mddSourceSysId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
					Jnjgtb2binboundserviceConstants.COMMA_STRING));

		}
		queryParams.put("code", recordStatus.getCode());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntB2BUnitModel Query " + fQuery);
		}

		final List<JnjGTIntPartnerFuncModel> result = getFlexibleSearchService().<JnjGTIntPartnerFuncModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntB2BUnitModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntSalesOrgModel> getJnjGTIntSalesOrgModel(final String customerNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntSalesOrgModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(customerNumber, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		final String query = B2BUnit.SALES_ORG_QUERY;
		queryParams.put("customerNumber", customerNumber);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntSalesOrgModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntSalesOrgModel Query " + fQuery);
		}

		final List<JnjGTIntSalesOrgModel> result = getFlexibleSearchService().<JnjGTIntSalesOrgModel> search(fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntSalesOrgModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModel(final String customerNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(customerNumber, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		final String query = B2BUnit.PARTNER_FUNC_QUERY;
		queryParams.put("customerNumber", customerNumber);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntPartnerFuncModel Query "
					+ fQuery);
		}

		final List<JnjGTIntPartnerFuncModel> result = getFlexibleSearchService().<JnjGTIntPartnerFuncModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}


	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelByPartnerCustomerNumber(final String partnerCustomerNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(partnerCustomerNumber, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		final String query = B2BUnit.PARTNER_FUNC_QUERY_BY_PARTNER_CUST_NO;
		queryParams.put("partCustNo", partnerCustomerNumber);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntPartnerFuncModel Query "
					+ fQuery);
		}

		final List<JnjGTIntPartnerFuncModel> result = getFlexibleSearchService().<JnjGTIntPartnerFuncModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntPartnerFuncModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntAffiliationModel> getJnjGTIntAffiliationModel(final RecordStatus recordStatus, final String customerNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntAffiliationtModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String query = null;
		ServicesUtil.validateParameterNotNull(recordStatus, " processed Indentfier must not be null");
		final Map queryParams = new HashMap();
		if (StringUtils.isNotEmpty(customerNumber))
		{
			query = B2BUnit.AFFIL_QUERY_CUST_NUM;
			queryParams.put("parentCustNo", customerNumber);
		}
		else
		{
			query = B2BUnit.AFFIL_QUERY;
		}

		queryParams.put("code", recordStatus.getCode());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntAffiliationtModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntAffiliationtModel Query "
					+ fQuery);
		}

		final List<JnjGTIntAffiliationModel> result = getFlexibleSearchService().<JnjGTIntAffiliationModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntAffiliationtModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/*
	 * @Override public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelForBillTo(final RecordStatus
	 * recordStatus, final String indicator) { if (LOGGER.isDebugEnabled()) { LOGGER.debug("getJnjGTIntB2BUnitModel()" +
	 * Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME +
	 * JnJCommonUtil.getCurrentDateTime()); } String query = null; ServicesUtil.validateParameterNotNull(recordStatus,
	 * " processed Indentfier must not be null"); ServicesUtil.validateParameterNotNull(indicator,
	 * " processed Indentfier must not be null"); final Map queryParams = new HashMap(); if
	 * (B2BUnit.BILL_TO_INDC.equalsIgnoreCase(indicator)) { query = B2BUnit.BILL_TO_INDC_QUERY_EMEA;
	 * 
	 * queryParams.put("billToInd", "BLL"); queryParams.put("primaryBillToInd", "BLP"); queryParams.put("mddSourceSysId",
	 * JnJCommonUtil.getValues(Jnjnab2binboundserviceConstants.MDD_SOURCE_SYS_ID_FOR_CUST,
	 * Jnjnab2binboundserviceConstants.COMMA_STRING));
	 * 
	 * } queryParams.put("code", recordStatus.getCode()); final FlexibleSearchQuery fQuery = new
	 * FlexibleSearchQuery(query);
	 * 
	 * fQuery.addQueryParameters(queryParams); if (LOGGER.isDebugEnabled()) { LOGGER.debug("getJnjGTIntB2BUnitModel()" +
	 * Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntB2BUnitModel Query " + fQuery); }
	 * 
	 * final List<JnjGTIntPartnerFuncModel> result = getFlexibleSearchService().<JnjGTIntPartnerFuncModel> search(fQuery)
	 * .getResult(); if (LOGGER.isDebugEnabled()) { LOGGER.debug("getJnjGTIntB2BUnitModel()" + Logging.HYPHEN +
	 * Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime()); } return result;
	 * }
	 */
}
