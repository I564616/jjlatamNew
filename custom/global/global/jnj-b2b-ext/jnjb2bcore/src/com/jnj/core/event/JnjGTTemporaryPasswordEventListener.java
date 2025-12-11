package com.jnj.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.model.JnjGTCreateUserEmailProcessModel;
import com.jnj.core.model.JnjGTTemporaryPwdEmailProcessModel;
//import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;

/**
 * @author pmunuswa
 *
 */
public class JnjGTTemporaryPasswordEventListener extends AbstractSiteEventListener<JnjGTTemporaryPasswordEvent> {

	protected static final Logger LOG = Logger.getLogger(JnjGTTemporaryPasswordEventListener.class);
	
	protected static final String CUSTOMER_TEMPORARY_PASSWORD = "customerTemporaryPassword";
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	protected static final String USER_FULL_NAME  = "userFullName";
	protected static final String TEMP_PWD_EMAILBODY  = "tempPwdEmailBody";
	protected static final String TEMP_EMAILBODY_CONT  = "email.temporaryPassword.EmailBody";
	
	
	protected BusinessProcessService businessProcessService;
	
	/** Model service **/
	protected ModelService modelService;
	
	@Autowired
	private MessageFacadeUtill messageFacade;
	
	/** I18NService to retrieve the current locale. */
	@Autowired
	private I18NService i18nService;
	
	/**
	 * @return the messageFacade
	 */
	public MessageFacadeUtill getMessageFacade()
	{
		return messageFacade;
	}

	
	/**
	 * @return the i18nService
	 */
	public I18NService getI18nService()
	{
		return i18nService;
	}


	@Override
	protected void onSiteEvent(JnjGTTemporaryPasswordEvent event) {

		JnjGTTemporaryPwdEmailProcessModel jnjGTTemporaryPwdEmailProcessModel = null;
		
		jnjGTTemporaryPwdEmailProcessModel = (JnjGTTemporaryPwdEmailProcessModel) getBusinessProcessService().createProcess(
				"" + "-" + System.currentTimeMillis(), "jnjGTTemporaryPwdEmailProcess");
		
		Map<String, String> temporaryPasswordEmailDataMap = null;
		/** Populating the Temporary Password Data Map **/
		temporaryPasswordEmailDataMap = populateTemporaryPwdEmailDataMap(event);
		/** Setting the Temporary Password map in the process model **/
		jnjGTTemporaryPwdEmailProcessModel.setJnjGTTemporaryPwdEmailDetails(temporaryPasswordEmailDataMap);
		populateProcessModel(event, jnjGTTemporaryPwdEmailProcessModel);
		
	}
	
	/**
	 * This method populates the ApprovedUser Email Data Map
	 *
	 * @param JnjGTTemporaryPasswordEvent
	 * @return approvedUserEmailDataMap
	 */
	protected Map<String, String> populateTemporaryPwdEmailDataMap(final JnjGTTemporaryPasswordEvent event)
	{
		final String METHOD_NAME = "populateTemporaryPwdEmailDataMap()";
		String languageIsoCode = null;
		String mailContent = null;
		Locale currentLocale = null;
		ArrayList<String> inpValues= new ArrayList<String>();
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, "Setting Essential data", LOG);
		final Map<String, String> temporaryPasswrodEmailDataMap = new HashMap<String, String>();
		if (null != event.getCustomer())
		{
			temporaryPasswrodEmailDataMap.put(USER_FULL_NAME, event.getCustomer().getName());
			inpValues.add(event.getCustomer().getName());
			if(event.getCustomer().getSessionLanguage() != null){
				languageIsoCode = event.getCustomer().getSessionLanguage().getIsocode();
			}else{
				languageIsoCode = event.getLanguage().getIsocode();
			}
		
			if (StringUtils.isNotBlank(languageIsoCode)) {
				currentLocale = LocaleUtils.toLocale(languageIsoCode);
			} else {
				currentLocale = i18nService.getCurrentLocale();
			}
		}
		temporaryPasswrodEmailDataMap.put(CUSTOMER_TEMPORARY_PASSWORD, event.getTemporaryPassword());
		inpValues.add(event.getTemporaryPassword());
		temporaryPasswrodEmailDataMap.put(USER_EMAIL_ADDRESS, event.getBuisnessEmail());
		LOG.info("populateCreateEmailDataMap currentLocale ************************************************* : "+currentLocale);
		/*ArrayList to Array Conversion */
		String dynamicFields[]=inpValues.toArray(new String[inpValues.size()]);
		mailContent = messageFacade.getMessageForCode(TEMP_EMAILBODY_CONT, currentLocale, dynamicFields);
		LOG.info("populateCreateEmailDataMap mailContent ************************************************* : "+mailContent);
		temporaryPasswrodEmailDataMap.put(TEMP_PWD_EMAILBODY,mailContent); 
		
		CommonUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, "Essential data set!", LOG);
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.TEMPORARY_PWD_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return temporaryPasswrodEmailDataMap;
	}
	
	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTCreateUserEmailProcessModel
	 */
	protected void populateProcessModel(final JnjGTTemporaryPasswordEvent event, final JnjGTTemporaryPwdEmailProcessModel processModel)
	{
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logDebugMessage(Logging.TEMPORARY_PWD_EMAIL, "sentTemporaryPwdEmailToUser()", "Populating process model", LOG);
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		CommonUtil.logDebugMessage(Logging.TEMPORARY_PWD_EMAIL, "sentTemporaryPwdEmailToUser()", "Starting the process", LOG);
		getBusinessProcessService().startProcess(processModel);
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD, LOG);
	}



	@Override
	protected boolean shouldHandleEvent(JnjGTTemporaryPasswordEvent event) {
		
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD, LOG);
		CommonUtil.logMethodStartOrEnd(Logging.TEMPORARY_PWD_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD, LOG);
		return true;
	}

	/**
	 * @return businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	/**
	 * @param businessProcessService
	 */
	public void setBusinessProcessService(BusinessProcessService businessProcessService) {
		this.businessProcessService = businessProcessService;
	}

	/**
	 * @return modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}
