package com.jnj.core.event;

import java.util.HashMap;
import java.util.Map;

import com.jnj.core.model.JnjGTOrderCancelEmailProcessModel;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

public class JnjGTOrderCancelNotificationEventListener extends AbstractSiteEventListener<JnjGTOrderCancelNotificationEvent> {

	protected  BusinessProcessService businessProcessService;
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";

	/** Model service **/
	protected ModelService modelService;
	
	@Override
	protected void onSiteEvent(JnjGTOrderCancelNotificationEvent event) {
		
		JnjGTOrderCancelEmailProcessModel jnjGTOrderCancelEmailProcessModel = null;
		jnjGTOrderCancelEmailProcessModel = getBusinessProcessService().createProcess("" + "-" + System.currentTimeMillis(), "jnjGTOrderCancelEmailProcess");
		
		Map<String, String> createUserEmailDataMap = null;
		/** Populating the Create User Data Map **/
		createUserEmailDataMap = populateCreateEmailDataMap(event);
		/** Setting the Create User data map in the process model **/
		jnjGTOrderCancelEmailProcessModel.setJnjGTCreateUserEmailDetails(createUserEmailDataMap);
		jnjGTOrderCancelEmailProcessModel.setJnjOrderEntries(event.getOrderEntries());
		populateProcessModel(event, jnjGTOrderCancelEmailProcessModel);
		
	}
	
	public Map<String,String> populateCreateEmailDataMap(JnjGTOrderCancelNotificationEvent event)
	{
		final String METHOD_NAME = "populateCreateEmailDataMap()";
		//Setting user Detail
		final Map<String, String> createdUserEmailDataMap = new HashMap<String, String>();
		if (null != event)
		{
			createdUserEmailDataMap.put("userFullName", event.getCustomer().getName());
			createdUserEmailDataMap.put(USER_EMAIL_ADDRESS, event.getCustomer().getUid());
		}
		return createdUserEmailDataMap;
	}
	
	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTCreateUserEmailProcessModel
	 */
	protected void populateProcessModel(final JnjGTOrderCancelNotificationEvent event, final JnjGTOrderCancelEmailProcessModel jnjGTOrderCancelEmailProcessModel)
	{
		
		jnjGTOrderCancelEmailProcessModel.setSite(event.getSite());
		jnjGTOrderCancelEmailProcessModel.setCustomer(event.getCustomer());
		jnjGTOrderCancelEmailProcessModel.setLanguage(event.getLanguage());
		jnjGTOrderCancelEmailProcessModel.setCurrency(event.getCurrency());
		jnjGTOrderCancelEmailProcessModel.setStore(event.getBaseStore());
		jnjGTOrderCancelEmailProcessModel.setJnjOrderEntries(event.getOrderEntries());
		getModelService().save(jnjGTOrderCancelEmailProcessModel);
		getBusinessProcessService().startProcess(jnjGTOrderCancelEmailProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(JnjGTOrderCancelNotificationEvent event) {
		// TODO Auto-generated method stub
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
