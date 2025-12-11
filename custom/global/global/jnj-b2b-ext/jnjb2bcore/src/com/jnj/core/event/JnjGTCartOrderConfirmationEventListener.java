/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.MessageFacadeUtill;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.core.model.JnjGTCartOrderEmailProcessModel;


/**
 * The listener interface for receiving JnjGTCartOrderConfirmationEvent events. The class that is interested in
 * processing a JnjGTCartOrderConfirmationEvent event implements this interface, and the object created with that class
 * is registered with a component using the component's
 * <code>addJnjGTCartOrderConfirmationEventListener<code> method. When
 * the JnjGTCartOrderConfirmationEvent event occurs, that object's appropriate
 * method is invoked.
 * 
 * @author sakshi.kashiva
 */
public class JnjGTCartOrderConfirmationEventListener extends AbstractSiteEventListener<JnjGTCartOrderConfirmationEvent>
{

	/** The Constant LOG. */
	protected static final Logger LOG = Logger.getLogger(JnjGTAddAccountEventListener.class);

	/** The common i18 n service. */
	@Autowired
	protected CommonI18NService commonI18NService;

	/** The user service. */
	@Autowired
	protected UserService userService;

	/** The message facade. */
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


	/** The model service. */
	protected ModelService modelService;

	/** The business process service. */
	protected BusinessProcessService businessProcessService;



	@Override
	protected void onSiteEvent(final JnjGTCartOrderConfirmationEvent event)
	{
		final JnjGTOrderData orderData = new JnjGTOrderData();
		final JnjGTCartOrderEmailProcessModel JnjGTCartOrderEmailProcessModelUser = getBusinessProcessService().createProcess(
				"" + "-" + System.currentTimeMillis(), "jnjGTCartOrderConfirmationUSEREmailProcess");
		JnjGTCartOrderEmailProcessModelUser.setFirstName(event.getCustomer().getName());
		JnjGTCartOrderEmailProcessModelUser.setOrderStatus(orderData.getOrderStatus());
		JnjGTCartOrderEmailProcessModelUser.setLastName(event.getCustomer().getName());

		populateProcessModel(event, JnjGTCartOrderEmailProcessModelUser);

	}

	/**
	 * Populate process model.
	 * 
	 * @param event
	 *           the event
	 * @param processModel
	 *           the process model
	 */
	protected void populateProcessModel(final JnjGTCartOrderConfirmationEvent event,
			final JnjGTCartOrderEmailProcessModel processModel)
	{
		logMethodStartOrEnd(Logging.ORDER_CONFIRMATION_EMAIL, "populateProcessModel()", Logging.BEGIN_OF_METHOD);
		logDebugMessage(Logging.ORDER_CONFIRMATION_EMAIL, "sendEmailToUser()", "Populating process model");
		processModel.setSite(event.getSite());
		processModel.setCustomer(event.getCustomer());
		processModel.setLanguage(event.getLanguage());
		processModel.setCurrency(event.getCurrency());
		processModel.setStore(event.getBaseStore());
		getModelService().save(processModel);
		logDebugMessage(Logging.ORDER_CONFIRMATION_EMAIL, "sendEmailToUser()", "Starting the process");
		getBusinessProcessService().startProcess(processModel);
		logMethodStartOrEnd(Logging.ORDER_CONFIRMATION_EMAIL, "populateProcessModel()", Logging.END_OF_METHOD);
	}



	@Override
	protected boolean shouldHandleEvent(final JnjGTCartOrderConfirmationEvent paramT)
	{
		logMethodStartOrEnd(Logging.ORDER_CONFIRMATION_EMAIL, "shouldHandleEvent()", Logging.BEGIN_OF_METHOD);
		logMethodStartOrEnd(Logging.ORDER_CONFIRMATION_EMAIL, "shouldHandleEvent()", Logging.END_OF_METHOD);
		return true;
	}

	/**
	 * Log method start or end.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param entryOrExit
	 *           the entry or exit
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
	 * Utility method used for logging custom messages.
	 * 
	 * @param functionalityName
	 *           the functionality name
	 * @param methodName
	 *           the method name
	 * @param message
	 *           the message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}


	/**
	 * Gets the model service.
	 * 
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}


	/**
	 * Sets the model service.
	 * 
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/**
	 * Gets the business process service.
	 * 
	 * @return the businessProcessService
	 */
	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}


	/**
	 * Sets the business process service.
	 * 
	 * @param businessProcessService
	 *           the businessProcessService to set
	 */
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
