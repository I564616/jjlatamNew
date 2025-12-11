/**
 *
 */
package com.jnj.core.services.operations.impl;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.interceptor.JnjGTUserGroupPrepareInterceptor;
import com.jnj.core.dao.operations.JnjGTOperationsDao;
import com.jnj.core.model.BroadcastMessageModel;
import com.jnj.core.model.JnJGTAuditTrailModel;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.services.CMSSiteService;


/**
 * * This class Handles Business Logic For Operations Module
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOperationsService implements JnjGTOperationsService
{
	/** Auto-wired Model Service **/
	@Autowired
	protected ModelService modelService;

	@Resource(name = "GTOperationsDao")
	protected JnjGTOperationsDao jnjGTOperationsDao;

	@Autowired
	protected UserService userService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	/** The catalog service. */
	@Autowired
	protected CatalogService catalogService;
	@Autowired
	protected CMSSiteService cmsSiteService;

	@Autowired
	protected SessionService sessionService;

	public ModelService getModelService() {
		return modelService;
	}

	public JnjGTOperationsDao getJnjGTOperationsDao() {
		return jnjGTOperationsDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public CatalogService getCatalogService() {
		return catalogService;
	}

	public CMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	protected static final Logger LOG = Logger.getLogger(JnjGTUserGroupPrepareInterceptor.class);

	//protected static final String query = "SELECT {pk} " + " FROM {JnJGTAuditTrailModel AS aditTrail} ";

	@Override
	public boolean logAuditData(final String systemOrProcess, final String descriptionOfEvent, final String ipAddress,
			final boolean isAuthorised, final boolean isSuccess, final Date timeOfEvent, final String userId)
	{
		final UserModel loggedInUser = userService.getCurrentUser();
		String currentSystem = null;
		if (loggedInUser != null)
		{
			if (loggedInUser instanceof CustomerModel)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for current system as Portal");
				}
				currentSystem = "Portal";
			}
			else if (loggedInUser instanceof EmployeeModel)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug("going to log event for current system as Backend");
				}
				currentSystem = "Backend";
			}
		}
		final JnJGTAuditTrailModel JnjGTAuditTrailModel = modelService.create(JnJGTAuditTrailModel.class);
		JnjGTAuditTrailModel.setAuthorised(Boolean.valueOf(isAuthorised));
		JnjGTAuditTrailModel.setDateTime(timeOfEvent);
		if (StringUtils.isNotEmpty(userId))
		{
			JnjGTAuditTrailModel.setUser(userId);
		}
		else
		{
			JnjGTAuditTrailModel.setUser(loggedInUser.getUid());
		}
		if (systemOrProcess == null)
		{
			JnjGTAuditTrailModel.setSystemOrProcess(currentSystem);
		}
		else
		{
			JnjGTAuditTrailModel.setSystemOrProcess(systemOrProcess);
		}
		JnjGTAuditTrailModel.setDescription(descriptionOfEvent);
		JnjGTAuditTrailModel.setIpAddress(ipAddress);
		JnjGTAuditTrailModel.setSuccess(Boolean.valueOf(isSuccess));
		modelService.save(JnjGTAuditTrailModel);
		return false;
	}

	@Override
	public boolean cleanAllRecords()
	{
		final int start = 0;
		final int range = 1000;
		int total;
		final Calendar today = Calendar.getInstance();
		today.add(Calendar.MONTH, -3);
		final Date dateForCleanUp = today.getTime();
		final String query = "SELECT {" + JnJGTAuditTrailModel.PK + "} FROM {" + JnJGTAuditTrailModel._TYPECODE + "} where {"
				+ JnJGTAuditTrailModel.CREATIONTIME + "} < ?dateForCleanUp";
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
		fQuery.setCount(range);
		fQuery.setNeedTotal(true);
		fQuery.addQueryParameter("dateForCleanUp", dateForCleanUp);
		try
		{
			do
			{
				fQuery.setStart(start);
				final List<JnJGTAuditTrailModel> searchResult = flexibleSearchService.<JnJGTAuditTrailModel> search(fQuery)
						.getResult();
				for (final JnJGTAuditTrailModel oldJnjGTAuditTrailModel : searchResult)
				{
					modelService.remove(oldJnjGTAuditTrailModel);
				}
				total = searchResult.size();
				// start += range;
			}
			while (total > 0);

			return true;
		}
		catch (final Exception exception)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("going to log event for data older than 90 days");
			}
			return false;
		}
	}

	@Override
	public List<BroadcastMessageModel> getBroadCastMessages()
	{
		return jnjGTOperationsDao.getBroadCastMessages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.operations.JnjGTOperationsService#getBroadCastDataForId(java.lang.String)
	 */
	@Override
	public BroadcastMessageModel getBroadCastDataForId(final String broadCastId)
	{
		BroadcastMessageModel broadCastModel = new BroadcastMessageModel();
		broadCastModel.setUid(broadCastId);
		BroadcastMessageModel result = new BroadcastMessageModel();
		try
		{
			
			
			 result = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public BroadcastMessageModel execute()
				{
					final BroadcastMessageModel results = getFlexibleSearchService().<BroadcastMessageModel> getModelByExample(broadCastModel);
					return results;
				}
			}, userService.getAdminUser());
			
			
			/*broadCastModel = flexibleSearchService.getModelByExample(broadCastModel);*/
		}
		catch (final ModelNotFoundException | ModelLoadingException exception)
		{
			LOG.debug("Model Not Found" + exception.getMessage());
		}
		return result;
	}
}
