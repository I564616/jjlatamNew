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
import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnjGTShipmentConfirmationEmailBusinessProcessModel;


/**
 * Event Listener class responsible publish the <code>JnjGTBackorderEmailNotificationEvent</code> along with setting
 * required data from event to the business process model.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class JnjLaConfirmShipmentEmailEventListener extends AbstractSiteEventListener<JnjLaConfirmShipmentEmailEvent>
{
	/**
	 * Constant instance of <codeLogger></code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjLaConfirmShipmentEmailEventListener.class);

	/**
	 * Instance of <code>ModelService</code>.
	 */
	protected ModelService modelService;

	/**
	 * Instance of <code>BusinessProcessService</code>.
	 */
	protected BusinessProcessService businessProcessService;

	@Override
	protected void onSiteEvent(final JnjLaConfirmShipmentEmailEvent event)
	{
		final JnjGTShipmentConfirmationEmailBusinessProcessModel emailBusinessProcessModel = (JnjGTShipmentConfirmationEmailBusinessProcessModel) businessProcessService
				.createProcess("JnjGTShipmentConfirmationEmailBusinessProcess" + "-" + System.currentTimeMillis(),
						"JnjGTShipmentConfirmationEmailBusinessProcess");

		/*** Populate BPM with all necessary required values. ***/
		emailBusinessProcessModel.setCustomer(event.getCustomer());
		emailBusinessProcessModel.setStore(event.getBaseStore());
		emailBusinessProcessModel.setSite(event.getSite());
		emailBusinessProcessModel.setCurrency(event.getCurrency());
		emailBusinessProcessModel.setLanguage(event.getLanguage());
		//	backorderemailBusinessProcessModel.setShipmentConfirmationEmailData(value);OpenlineBackorderCheck(event.getEmailPreferenceOpenlinebackorder());
		/*** Populate BPM with backorder email data by iterating and concatenating the email data. ***/
		final List<String> backorderEmailData = new ArrayList<>();
		final List<String> b2bUserList = new ArrayList<String>();
		b2bUserList.add(event.getCustomer().getContactEmail());
		emailBusinessProcessModel.setB2bUsersList(b2bUserList);


		final StringBuilder stringBuilder = new StringBuilder();
		for (final JnJInvoiceOrderModel dto : event.getOrder().getJnjlaInvoices())
		{
			/* fields appended for JJEPIC -825 backorder email notification */
			stringBuilder.append(dto.getSalesOrder()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(dto.getEntries().get(0).getMaterial().getName()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(dto.getEntries().get(0).getMaterial().getCode()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(dto.getOrder().getOrderType()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(event.getOrder().getUser().getName()).append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(event.getOrder().getPaymentAddress().getAppartment() + ""
							+ event.getOrder().getPaymentAddress().getCountyName())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(event.getOrder().getDeliveryAddress().getAppartment() + ""
							+ event.getOrder().getDeliveryAddress().getCountyName())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(event.getOrder().getOrderChannel())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(dto.getEntries().get(0).getQty())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(event.getOrder().getNamedDeliveryDate())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE).append(event.getOrder().getOrderNumber());

			backorderEmailData.add(stringBuilder.toString());
			emailBusinessProcessModel.setShipmentConfirmationEmailData(backorderEmailData);
			stringBuilder.setLength(0);
		}

		//emailBusinessProcessModel.setBackOrderEmailData(backorderEmailData);

		try
		{
			modelService.save(emailBusinessProcessModel);
		}
		catch (final ModelSavingException e)
		{
			JnjGTCoreUtil.logErrorMessage("Send Invoice Email", "onSiteEvent()",
					"Saving 'JnjGTOrderShipmentemailBusinessProcessModel' has caused an exception: " + e.getMessage(), e,
					JnjLaConfirmShipmentEmailEventListener.class);
		}
		businessProcessService.startProcess(emailBusinessProcessModel);
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
	protected boolean shouldHandleEvent(final JnjLaConfirmShipmentEmailEvent event)
	{
		// YTODO Auto-generated method stub
		return true;
	}

}
