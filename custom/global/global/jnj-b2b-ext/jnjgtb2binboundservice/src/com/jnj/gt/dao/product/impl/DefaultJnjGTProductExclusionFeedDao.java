package com.jnj.gt.dao.product.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.JnjProductExclusionClassType;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.product.JnjGTProductExclusionFeedDao;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;


/**
 * The JnjGTProductExclusionFeedDaoImpl class contains all those methods which are dealing with product exclusion
 * related intermediate model and it has definition of all the methods which are defined in the
 * JnjGTProductExclusionFeedDao interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTProductExclusionFeedDao extends AbstractItemDao implements JnjGTProductExclusionFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductExclusionFeedDao.class);

	private static final String GET_B2B_UNIT_CODES_USING_MATERIAL_NUMBER = "Select {characteristic} from {JnjGTIntProductExclusion} where {systemId} in (?systemId) and {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}}) and {materialCustNumber}=?materialCustNumber and {materialCustInd}='MAT' and {classificationType}=?classificationType";

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntProductExclusionModel> getProdExclusionRecords(final JnjProductExclusionClassType classType,
			final String characteristic, final String materialNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProdExclusionRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery
				.append("SELECT {")
				.append(ItemModel.PK)
				.append("} FROM {")
				.append(JnjGTIntProductExclusionModel._TYPECODE)
				.append(
						"} where {classificationType}=?classificationType and {systemId} in (?systemId) and {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}})");

		final Map queryParams = new HashMap();
		if (StringUtils.isNotEmpty(characteristic) && StringUtils.isEmpty(materialNumber))
		{
			searchQuery.append(" and {characteristic}=?characteristic");
			queryParams.put(JnjGTIntProductExclusionModel.CHARACTERISTIC, characteristic);
		}
		if (StringUtils.isNotEmpty(materialNumber))
		{
			searchQuery
					.append(" and (({materialCustNumber}=?materialCustNumberForB2B and {materialCustInd}='CUST') or ({materialCustNumber}=?materialCustNumber and {materialCustInd}='MAT'))");
			queryParams.put(JnjGTIntProductExclusionModel.MATERIALCUSTNUMBER, materialNumber);
			queryParams.put("materialCustNumberForB2B", characteristic);
		}
		queryParams.put(JnjGTIntProductExclusionModel.CLASSIFICATIONTYPE, classType.getCode());
		queryParams.put("code", RecordStatus.PENDING.getCode());
		queryParams.put("systemId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.CONSUMER_USA_SOURCE_SYS_ID,
				Jnjgtb2binboundserviceConstants.COMMA_STRING));
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProdExclusionRecords()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List<JnjGTIntProductExclusionModel> result = getFlexibleSearchService().<JnjGTIntProductExclusionModel> search(
				flexibleSearchQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getProdExclusionRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public Collection<JnJB2BUnitModel> getCustomersForGroup(final String groupNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustomersForGroup()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime());
		}
		final StringBuilder searchQuery = new StringBuilder();
		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnJB2BUnitModel._TYPECODE)
				.append("} WHERE {").append(JnJB2BUnitModel.CUSTOMERGROUP).append("} = ").append("?customerGroup")
				.append(" and {sourceSysId}=?sourceSysId and {active}=1");

		final Map queryParams = new HashMap();
		queryParams.put(JnJB2BUnitModel.CUSTOMERGROUP, groupNumber);
		queryParams.put(JnJB2BUnitModel.SOURCESYSID, JnjGTSourceSysId.CONSUMER.getCode());
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustomersForGroup()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + flexibleSearchQuery);
		}
		final List<JnJB2BUnitModel> result = getFlexibleSearchService().<JnJB2BUnitModel> search(flexibleSearchQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCustomersForGroup()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}

	@Override
	public List<String> getB2BUnitCodesUsingMaterialNumber(final String materialNumber)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getB2BUnitCodesUsingMaterialNumber()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final String query = GET_B2B_UNIT_CODES_USING_MATERIAL_NUMBER;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		final List resultClassList = new ArrayList();
		resultClassList.add(String.class);
		fQuery.setResultClassList(resultClassList);
		final Map queryParams = new HashMap<>();
		queryParams.put("code", RecordStatus.PENDING.getCode());
		queryParams.put("materialCustNumber", materialNumber);
		queryParams.put("systemId", JnJCommonUtil.getValues(Jnjgtb2binboundserviceConstants.CONSUMER_USA_SOURCE_SYS_ID,
				Jnjgtb2binboundserviceConstants.COMMA_STRING));
		queryParams.put(JnjGTIntProductExclusionModel.CLASSIFICATIONTYPE, JnjProductExclusionClassType.ALLOWED_CUSTOMER.getCode());
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getB2BUnitCodesUsingMaterialNumber()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Query " + fQuery);
		}
		final SearchResult result = getFlexibleSearchService().search(fQuery);
		final List<String> resultList = result.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getB2BUnitCodesUsingMaterialNumber()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return resultList;
	}

}
