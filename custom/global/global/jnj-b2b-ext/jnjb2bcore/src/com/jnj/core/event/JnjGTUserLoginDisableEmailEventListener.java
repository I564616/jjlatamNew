/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.event.JnjGTSuccessfulRegistrationEmailEventListener;
import com.jnj.core.model.JnjGTUserDisableLoginEmailProcessModel;


/**
 * @author labanya.saha
 * 
 */
public class JnjGTUserLoginDisableEmailEventListener extends AbstractSiteEventListener<JnjGTUserLoginDisableEmailEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTSuccessfulRegistrationEmailEventListener.class);

	/** Model service **/
	@Autowired
	protected ModelService modelService;

	public ModelService getModelService() {
		return modelService;
	}

	/** Business process services required to create process **/

	protected BusinessProcessService businessProcessService;



	/**
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}


	/**
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}


	@Override
	protected void onSiteEvent(final JnjGTUserLoginDisableEmailEvent jnjGTUserLoginDisableEmailEvent)
	{
		final String METHOD_NAME = "onSiteEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating the process **/



		final JnjGTUserDisableLoginEmailProcessModel jnjGTUserDisableLoginEmailProcessModel = (JnjGTUserDisableLoginEmailProcessModel) getBusinessProcessService()
				.createProcess("jnjGTUserDisableLoginEmailProcess" + "-" + System.currentTimeMillis(),
						"jnjGTUserDisableLoginEmailProcess");



		/** Populating the process model and then starting the process for the successful registration email **/

		populateProcessModel(jnjGTUserLoginDisableEmailEvent, jnjGTUserDisableLoginEmailProcessModel);



	}



	@Override
	protected boolean shouldHandleEvent(final JnjGTUserLoginDisableEmailEvent jnjGTUserLoginDisableEmailEvent)
	{
		final String METHOD_NAME = "shouldHandleEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return true;
	}




	protected void populateProcessModel(final JnjGTUserLoginDisableEmailEvent event,
			final JnjGTUserDisableLoginEmailProcessModel jnjGTUserDisableLoginEmailProcessModel)
	{

		final String METHOD_NAME = "populateProcessModel()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		logDebugMessage(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME,
				"Populating the jnjGTUserDisableLoginEmailProcessModel");

		jnjGTUserDisableLoginEmailProcessModel.setCustomer(event.getCustomer());
		//jnjGTUserDisableLoginEmailProcessModel.setFirstName(event.getFirstName());
		jnjGTUserDisableLoginEmailProcessModel.setSite(event.getSite());
		jnjGTUserDisableLoginEmailProcessModel.setLanguage(event.getLanguage());
		jnjGTUserDisableLoginEmailProcessModel.setCurrency(event.getCurrency());
		jnjGTUserDisableLoginEmailProcessModel.setStore(event.getBaseStore());
		jnjGTUserDisableLoginEmailProcessModel.setDaysBeforeDisable(event.getDaysBeforeDisable());
		LOG.info("Inside Listener populateProcessModel method before saving model");
		modelService.save(jnjGTUserDisableLoginEmailProcessModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME,
				"jnjGTSuccessfulRegistrationProcessModel saved! Now starting the process - jnjGTSuccessfulRegistrationProcess");
		businessProcessService.startProcess(jnjGTUserDisableLoginEmailProcessModel);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.WARNING_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}



	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
