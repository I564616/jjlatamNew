/**
 * 
 */
package com.jnj.gt.dao.common.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnjGTIntermediateMasterModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.dao.common.JnjGTFeedDao;


/**
 * @author akash.rawat
 * 
 */
public class DefaultJnjGTFeedDao implements JnjGTFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTFeedDao.class);

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}


	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus)
	{
		return fetchIntRecords(intermediateModel, recordStatus, null, null);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, final Date selectionDate)
	{
		return fetchIntRecords(intermediateModel, recordStatus, null, selectionDate);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, final List<String> sourceSysId)
	{
		return fetchIntRecords(intermediateModel, recordStatus, sourceSysId, null);
	}

	@Override
	public Collection<? extends JnjGTIntermediateMasterModel> fetchIntRecords(final String intermediateModel,
			final RecordStatus recordStatus, final List<String> sourceSysId, final Date selectionDate)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();


		/* If recordStatus is null then select all records */
		if (recordStatus == null)
		{
			searchQuery.append("select {pk} from {" + intermediateModel + " as int} ");
			if (selectionDate != null)
			{
				searchQuery.append(" where {creationtime} <= to_timestamp(?formattedDate,'yyyy-mm-dd')");
				final String formattedDate = new SimpleDateFormat(jnjCommonUtil.getDBDateFormat()).format(selectionDate);
				queryParams.put("formattedDate", formattedDate);
			}
		}
		else
		{
			searchQuery.append("select {pk} from {" + intermediateModel
					+ " as int JOIN RecordStatus as status ON {int:recordstatus}={status:pk}} where {status:code}=?recordStatus");

			if (selectionDate != null)
			{
				searchQuery.append(" and {int:creationtime} <= to_timestamp(?formattedDate,'yyyy-mm-dd')");
				final String formattedDate = new SimpleDateFormat(jnjCommonUtil.getDBDateFormat()).format(selectionDate);
				queryParams.put("formattedDate", formattedDate);
			}
		}

		if (recordStatus != null)
		{
			queryParams.put(JnjGTIntermediateMasterModel.RECORDSTATUS, recordStatus.getCode());
		}
		if (CollectionUtils.isNotEmpty(sourceSysId))
		{
			searchQuery.append(" and {int:sourceSystemId} in (?sourceSystemIds)");
			queryParams.put("sourceSystemIds", sourceSysId);
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);
		final List<JnjGTIntermediateMasterModel> result = getFlexibleSearchService().<JnjGTIntermediateMasterModel> search(fQuery)
				.getResult();
		return result;

	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}



}
