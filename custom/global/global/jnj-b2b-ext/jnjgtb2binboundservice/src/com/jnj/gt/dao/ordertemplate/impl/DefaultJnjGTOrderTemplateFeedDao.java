/**
 * 
 */
package com.jnj.gt.dao.ordertemplate.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.dao.ordertemplate.JnjGTOrderTemplateFeedDao;
import com.jnj.gt.model.JnjGTIntOrderTemplateEntryModel;


/**
 * @author komal.sehgal
 * 
 */
public class DefaultJnjGTOrderTemplateFeedDao extends AbstractItemDao implements JnjGTOrderTemplateFeedDao
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderTemplateFeedDao.class);

	@Override
	public List<JnjGTIntOrderTemplateEntryModel> getJnjGTIntOrderTemplateEntry(final String templateId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderTemplateEntry()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		ServicesUtil.validateParameterNotNull(templateId, " template Indentfier must not be null");
		final Map queryParams = new HashMap();
		final String query = "select {pk} from {JnjGTIntOrderTemplateEntry} where {orderId}=?templateId and {recordStatus} IN ({{select {pk} from {RecordStatus} where {code}=?code}})";
		queryParams.put("templateId", templateId);
		queryParams.put("code", RecordStatus.PENDING.getCode());
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderTemplateEntry()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ "getJnjGTIntOrderTemplateEntry Query " + fQuery);
		}
		final List<JnjGTIntOrderTemplateEntryModel> result = getFlexibleSearchService().<JnjGTIntOrderTemplateEntryModel> search(
				fQuery).getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntOrderTemplateEntry()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}
}
