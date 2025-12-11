package com.jnj.core.event;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTCreateUserEmailProcessModel;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

public class JnjGTCreateUserEventListener extends AbstractSiteEventListener<JnjGTCreateUserEvent> {

	protected static final Logger LOG = Logger.getLogger(JnjGTApprovedUserEventListener.class);
	
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	protected static final String SITE_URL = "siteUrl";
	
	protected BusinessProcessService businessProcessService;
	
	/** Model service **/
	protected ModelService modelService;
	
	@Override
	protected void onSiteEvent(JnjGTCreateUserEvent event) {

		JnjGTCreateUserEmailProcessModel jnjGTCreateUserEmailProcessModel = null;
		/** create Process **/
		jnjGTCreateUserEmailProcessModel = (JnjGTCreateUserEmailProcessModel) getBusinessProcessService().createProcess(
					"" + "-" + System.currentTimeMillis(), "jnjGTCreateUserProfile");
		
		Map<String, String> createUserEmailDataMap = null;
		/** Populating the Create User Data Map **/
		createUserEmailDataMap = populateCreateEmailDataMap(event);
		/** Setting the Create User data map in the process model **/
		jnjGTCreateUserEmailProcessModel.setJnjGTCreateUserEmailDetails(createUserEmailDataMap);
		populateProcessModel(event, jnjGTCreateUserEmailProcessModel);
		
	}
	
	/**
	 * This method populates the ApprovedUser Email Data Map
	 *
	 * @param JnjGTApprovedCompleteEvent
	 * @return approvedUserEmailDataMap
	 */
	protected Map<String, String> populateCreateEmailDataMap(final JnjGTCreateUserEvent event)
	{
		final String METHOD_NAME = "populateCreateEmailDataMap()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		final Map<String, String> createdUserEmailDataMap = new HashMap<String, String>();
		/** Setting essential data in the map **/
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME, "Setting Essential data", LOG);
		/** Setting Business Email in the map **/
		if (null != event.getCustomer())
		{
			createdUserEmailDataMap.put("userFullName", event.getCustomer().getName());
		}
		createdUserEmailDataMap.put(USER_EMAIL_ADDRESS, event.getBuisnessEmail());
		/** Setting Site Url in the map **/
		createdUserEmailDataMap.put(SITE_URL, event.getBaseUrl());
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME, "Essential data set!", LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.CREATE_USER_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return createdUserEmailDataMap;
	}
	
	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTCreateUserEmailProcessModel
	 */
	protected void populateProcessModel(final JnjGTCreateUserEvent event, final JnjGTCreateUserEmailProcessModel processModel)
	{
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.CREATE_USER_EMAIL, "sendCreatedEmailToUser()", "Populating process model", LOG);
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		CommonUtil.logDebugMessage(Logging.CREATE_USER_EMAIL, "sendCreatedEmailToUser()", "Starting the process", LOG);
		getBusinessProcessService().startProcess(processModel);
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD, LOG);
	}



	@Override
	protected boolean shouldHandleEvent(JnjGTCreateUserEvent event) {
		
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Logging.CREATE_USER_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD, LOG);
		return true;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}
