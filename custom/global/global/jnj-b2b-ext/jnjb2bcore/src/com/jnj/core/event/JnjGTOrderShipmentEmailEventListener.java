/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;

import com.jnj.core.model.JnjGTOrderShipmentEmailBusinessProcessModel;


/**
 * Event Listener class responsible publish the <code>JnjGTOrderShipmentEmailEvent</code> along with setting required
 * data from event to the business process model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderShipmentEmailEventListener extends AbstractSiteEventListener<JnjGTOrderShipmentEmailEvent>
{
	/**
	 * Constant instance of <codeLogger></code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTOrderShipmentEmailEventListener.class);

	/**
	 * Instance of <code>ModelService</code>.
	 */
	protected ModelService modelService;

	/**
	 * Instance of <code>BusinessProcessService</code>.
	 */
	protected BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final JnjGTOrderShipmentEmailEvent event)
	{
		final JnjGTOrderShipmentEmailBusinessProcessModel orderShipmentEmailBusinessProcessModel = (JnjGTOrderShipmentEmailBusinessProcessModel) businessProcessService
				.createProcess("JnjGTOrderShipmentEmailProcess" + "-" + System.currentTimeMillis(), "JnjGTOrderShipmentEmailProcess");

		/*** Populate BPM with all necessary required values. ***/
		orderShipmentEmailBusinessProcessModel.setCustomer(event.getCustomer());
		orderShipmentEmailBusinessProcessModel.setStore(event.getBaseStore());
		orderShipmentEmailBusinessProcessModel.setSite(event.getSite());
		orderShipmentEmailBusinessProcessModel.setCurrency(event.getCurrency());
		orderShipmentEmailBusinessProcessModel.setLanguage(event.getLanguage());
		orderShipmentEmailBusinessProcessModel.setOrderNumber(event.getSapOrderNumber());
		orderShipmentEmailBusinessProcessModel.setOrderCode(event.getOrderCode());
		try
		{
			modelService.save(orderShipmentEmailBusinessProcessModel);
		}
		catch (final ModelSavingException e)
		{
			LOGGER.error("Saving 'JnjGTOrderShipmentEmailBusinessProcessModel' has caused an exception: " + e.getMessage());
		}
		businessProcessService.startProcess(orderShipmentEmailBusinessProcessModel);
	}

	/**
	 * Overridden to handle the event publish request.
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTOrderShipmentEmailEvent arg0)
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
