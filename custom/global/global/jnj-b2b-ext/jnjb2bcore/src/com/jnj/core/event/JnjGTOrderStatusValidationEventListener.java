/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.jnj.core.model.JnjGTOrdInboundValidationEmailProcessModel;


/**
 * Event Listener class responsible publish the <code>JnjGTOrderStatusValidationEvent</code> along with setting order
 * status in-bound file validation data from event to the business process model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderStatusValidationEventListener extends AbstractSiteEventListener<JnjGTOrderStatusValidationEvent>
{
	/**
	 * Constant for Logger.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTOrderStatusValidationEventListener.class);

	/**
	 * protected instance of <code>ModelService</code>.
	 */
	protected ModelService modelService;

	/**
	 * protected instance of <code>BusinessProcessService</code>.
	 */
	protected BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final JnjGTOrderStatusValidationEvent event)
	{
		final JnjGTOrdInboundValidationEmailProcessModel ordInboundValidationEmailProcessModel = (JnjGTOrdInboundValidationEmailProcessModel) businessProcessService
				.createProcess("jnjGTOrderStatusValidationEmailProcess" + "-" + System.currentTimeMillis(),
						"jnjGTOrderStatusValidationEmailProcess");

		ordInboundValidationEmailProcessModel.setCustomer(event.getCustomer());
		ordInboundValidationEmailProcessModel.setStore(event.getBaseStore());
		ordInboundValidationEmailProcessModel.setSite(event.getSite());
		ordInboundValidationEmailProcessModel.setCurrency(event.getCurrency());
		ordInboundValidationEmailProcessModel.setLanguage(event.getLanguage());

		ordInboundValidationEmailProcessModel.setFileNames(event.getOrderStatusInboundFileNames());
		ordInboundValidationEmailProcessModel.setFileNameAndCount(event.getFileNameAndCounts());

		try
		{
			modelService.save(ordInboundValidationEmailProcessModel);
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("Saving Business process model for Order Status Inbound Validation Email has caused an exception: "
					+ exception.getMessage());
		}

		businessProcessService.startProcess(ordInboundValidationEmailProcessModel);
	}

	@Override
	protected boolean shouldHandleEvent(final JnjGTOrderStatusValidationEvent arg0)
	{
		return true;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
