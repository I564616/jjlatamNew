/**
 *
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Arrays;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTAddExistingAccountEmailProcessModel;


/**
 * @author sakshi.kashiva
 *
 */
public class JnjGTAddExistingAccountEventListener extends AbstractSiteEventListener<JnjGTAddExistingAccountEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTAddAccountEventListener.class);
	@Autowired
	protected CommonI18NService commonI18NService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected MessageFacadeUtill messageFacade;


	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public UserService getUserService() {
		return userService;
	}

	public MessageFacadeUtill getMessageFacade() {
		return messageFacade;
	}


	protected ModelService modelService;

	protected BusinessProcessService businessProcessService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#onSiteEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected void onSiteEvent(final JnjGTAddExistingAccountEvent event)
	{

		final JnjGTAddExistingAccountEmailProcessModel JnjGTAddExistingAccountEmailProcessModelCSR = (JnjGTAddExistingAccountEmailProcessModel) getBusinessProcessService()
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjGTAddExistingAccountCSREmailProcess");
		final JnjGTAddExistingAccountEmailProcessModel JnjGTAddExistingAccountEmailProcessModelUser = (JnjGTAddExistingAccountEmailProcessModel) getBusinessProcessService()
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjGTAddExistingAccountUSEREmailProcess");
		JnjGTAddExistingAccountEmailProcessModelCSR.setEmail(event.getCustomer().getUid());
		JnjGTAddExistingAccountEmailProcessModelCSR.setFirstName(event.getCustomer().getName());
		JnjGTAddExistingAccountEmailProcessModelCSR.setAccountNumbers(Arrays.asList(event.getAccountNumbers()));
		JnjGTAddExistingAccountEmailProcessModelCSR.setEmailFlag(Boolean.valueOf(true));
		JnjGTAddExistingAccountEmailProcessModelUser.setEmail(event.getCustomer().getUid());
		JnjGTAddExistingAccountEmailProcessModelUser.setFirstName(event.getCustomer().getName());
		JnjGTAddExistingAccountEmailProcessModelUser.setAccountNumbers(Arrays.asList(event.getAccountNumbers()));
		JnjGTAddExistingAccountEmailProcessModelUser.setEmailFlag(Boolean.valueOf(false));
		populateProcessModel(event, JnjGTAddExistingAccountEmailProcessModelCSR);
		populateProcessModel(event, JnjGTAddExistingAccountEmailProcessModelUser);

	}

	protected void populateProcessModel(final JnjGTAddExistingAccountEvent event,
			final JnjGTAddExistingAccountEmailProcessModel processModel)
	{
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "sendEmailToUser()", "Populating process model");
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		logDebugMessage(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "sendEmailToUser()", "Starting the process");
		getBusinessProcessService().startProcess(processModel);
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.commerceservices.event.AbstractSiteEventListener#shouldHandleEvent(de.hybris.platform.servicelayer
	 * .event.events.AbstractEvent)
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTAddExistingAccountEvent paramT)
	{
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Logging.ADD_EXISTING_ACCOUNT_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD);
		return true;
	}

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


	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


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

}
