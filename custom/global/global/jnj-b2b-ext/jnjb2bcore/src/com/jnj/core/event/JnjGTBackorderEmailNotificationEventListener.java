/**
 * 
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTBackorderEmailDto;
import com.jnj.core.model.JnjGTBackOrderEmailBusinessProcessModel;


/**
 * Event Listener class responsible publish the <code>JnjGTBackorderEmailNotificationEvent</code> along with setting
 * required data from event to the business process model.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTBackorderEmailNotificationEventListener extends AbstractSiteEventListener<JnjGTBackorderEmailNotificationEvent>
{
	/**
	 * Constant instance of <codeLogger></code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTBackorderEmailNotificationEventListener.class);

	/**
	 * Instance of <code>ModelService</code>.
	 */
	protected ModelService modelService;

	/**
	 * Instance of <code>BusinessProcessService</code>.
	 */
	protected BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final JnjGTBackorderEmailNotificationEvent event)
	{
		final JnjGTBackOrderEmailBusinessProcessModel backorderEmailBusinessProcessModel = (JnjGTBackOrderEmailBusinessProcessModel) businessProcessService
				.createProcess("JnjGTBackOrderEmailProcess" + "-" + System.currentTimeMillis(), "JnjGTBackOrderEmailProcess");

		/*** Populate BPM with all necessary required values. ***/
		backorderEmailBusinessProcessModel.setCustomer(event.getCustomer());
		backorderEmailBusinessProcessModel.setStore(event.getBaseStore());
		backorderEmailBusinessProcessModel.setSite(event.getSite());
		backorderEmailBusinessProcessModel.setCurrency(event.getCurrency());
		backorderEmailBusinessProcessModel.setLanguage(event.getLanguage());
		backorderEmailBusinessProcessModel.setOpenlineBackorderCheck(event.getEmailPreferenceOpenlinebackorder());
		/*** Populate BPM with backorder email data by iterating and concatenating the email data. ***/
		final List<String> backorderEmailData = new ArrayList<>();

		if (event.getEmailPreferenceOpenlinebackorder().equals("true"))
		{
			backorderEmailBusinessProcessModel.setOpenlineBackorderCheck("true");
			final StringBuilder stringBuilder = new StringBuilder();
			for (final JnjGTBackorderEmailDto dto : event.getEmailData())
			{
				/* fields appended for JJEPIC -825 backorder email notification */
				stringBuilder.append(dto.getOrderNumber()).append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getProductName())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getProductCode())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getAvailabilityDate())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getAccountNumber())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getOperatingCompany())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getCustomerPO())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getQuantity()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
						.append(dto.getUnit()).append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getItemPrice())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getExtendedPrice())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getShipDate()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
						.append(dto.getDeliveryDate()).append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getStatus())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getAdditionComments())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getOrderDate());

				backorderEmailData.add(stringBuilder.toString());
				stringBuilder.setLength(0);
			}
		}
		else
		{
			backorderEmailBusinessProcessModel.setOpenlineBackorderCheck("false");
			final StringBuilder stringBuilder = new StringBuilder();
			for (final JnjGTBackorderEmailDto dto : event.getEmailData())
			{
				/* fields appended for JJEPIC -825 backorder email notification */
				stringBuilder.append(dto.getOrderNumber()).append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getProductName())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getProductCode())
						.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getAvailabilityDate());

				backorderEmailData.add(stringBuilder.toString());
				stringBuilder.setLength(0);
			}
		}
		backorderEmailBusinessProcessModel.setBackOrderEmailData(backorderEmailData);

		try
		{
			modelService.save(backorderEmailBusinessProcessModel);
		}
		catch (final ModelSavingException e)
		{
			LOGGER.error("Saving 'JnjGTOrderShipmentEmailBusinessProcessModel' has caused an exception: " + e.getMessage());
		}
		businessProcessService.startProcess(backorderEmailBusinessProcessModel);
	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	/**
	 * Overridden to handle the event publish request.
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTBackorderEmailNotificationEvent arg0)
	{
		return true;
	}

}
