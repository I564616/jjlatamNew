/**
 * 
 */
package com.jnj.core.event;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjReturnOrderCSRProcessModel;
import com.jnj.core.services.synchronizeOrders.impl.DefaultJnjSAPOrdersService;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author nsinha7
 *
 */
public class JnjGTReturnOrderCSREventListener extends AbstractSiteEventListener<JnjGTReturnOrderCSREvent> {
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjSAPOrdersService.class);

	@Resource(name = "userService")
	UserService userService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected B2BOrderService b2boOrderService;

	@Autowired
	protected BusinessProcessService businessProcessService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected I18NService i18nService;



	public B2BOrderService getB2boOrderService() {
		return b2boOrderService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	@Override
	protected void onSiteEvent(final JnjGTReturnOrderCSREvent returnOrderCSRNotificationEvent)
	{

		final JnjReturnOrderCSRProcessModel returnOrderCSRProcessModel = (JnjReturnOrderCSRProcessModel) businessProcessService
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjReturnOrderCSRProcess");

		returnOrderCSRProcessModel.setSite(returnOrderCSRNotificationEvent.getSite());
		returnOrderCSRProcessModel.setCustomer(returnOrderCSRNotificationEvent.getCustomer());
		returnOrderCSRProcessModel.setLanguage(returnOrderCSRNotificationEvent.getLanguage());
		returnOrderCSRProcessModel.setCurrency(returnOrderCSRNotificationEvent.getCurrency());
		returnOrderCSRProcessModel.setStore(returnOrderCSRNotificationEvent.getBaseStore());
		returnOrderCSRProcessModel.setCustomer(returnOrderCSRNotificationEvent.getCustomer());
		returnOrderCSRProcessModel.setBaseUrl(returnOrderCSRNotificationEvent.getBaseUrl());
		returnOrderCSRProcessModel.setToEmail(returnOrderCSRNotificationEvent.getToEmail());
		
		if (null != returnOrderCSRNotificationEvent.getOrderCode())
		{
						
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2boOrderService.getOrderForCode(returnOrderCSRNotificationEvent.getOrderCode());
				}
			}, userService.getAdminUser());
			
		
			if (null != orderModel)
			{
				returnOrderCSRProcessModel.setOrder(orderModel);
				CommonUtil.logDebugMessage("Order Status Notification", "onSiteEvent()",
						"Order Has been set in the returnOrderCSRProcessModel!", LOGGER);
			}
		}
		else
		{
			returnOrderCSRProcessModel.setSapOrderNumber(returnOrderCSRNotificationEvent.getSapOrderNumber());
			returnOrderCSRProcessModel.setJnjOrderNumber(returnOrderCSRNotificationEvent.getJnjOrderNumber());

		}

		try{
			modelService.save(returnOrderCSRProcessModel);
			LOGGER.debug("Saving returnOrderCSRProcessModel.. " + returnOrderCSRProcessModel);
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error("Saving 'JnjInterfaceNotificationProcessModel' has caused exception: " + modelSavingException.getMessage());
		}

		try{
		LOGGER.debug("Start Processing mail for Order Status Notification.. " + returnOrderCSRNotificationEvent.getJnjOrderNumber());
		businessProcessService.startProcess(returnOrderCSRProcessModel);
		LOGGER.debug("End Processed mail successfully for Order Status Notification.. " + returnOrderCSRNotificationEvent.getJnjOrderNumber());
		}catch(Exception e){
			LOGGER.error("Exception occured while processing mail for the order .." + returnOrderCSRNotificationEvent.getJnjOrderNumber());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method generates the address string
	 * 
	 * @param addressData
	 * @return addressString
	 */
	protected String generateAddressString(final JnjGTAddressData addressData)
	{
		final StringBuilder addressBuilder = new StringBuilder();
		if (StringUtils.isNotEmpty(addressData.getCompanyName()))
		{
			addressBuilder.append(addressData.getCompanyName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getLine1()))
		{
			addressBuilder.append(addressData.getLine1());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getLine2()))
		{
			addressBuilder.append(addressData.getLine2());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getTown()))
		{
			addressBuilder.append(addressData.getTown());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (null != addressData.getRegion() && StringUtils.isNotEmpty(addressData.getRegion().getName()))
		{
			addressBuilder.append(addressData.getRegion().getName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (null != addressData.getCountry() && StringUtils.isNotEmpty(addressData.getCountry().getName()))
		{
			addressBuilder.append(addressData.getCountry().getName());
			addressBuilder.append(Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE);
		}
		if (StringUtils.isNotEmpty(addressData.getPostalCode()))
		{
			addressBuilder.append(addressData.getPostalCode());
		}
		String addressString;
		if (StringUtils.endsWith(String.valueOf(addressBuilder), Jnjb2bCoreConstants.CONST_COMMA + Jnjb2bCoreConstants.SPACE))
		{
			addressString = addressBuilder.substring(0, addressBuilder.length() - 2);
		}
		else
		{
			addressString = String.valueOf(addressBuilder);
		}
		return addressString;
	}

	@Override
	protected boolean shouldHandleEvent(final JnjGTReturnOrderCSREvent arg0)
	{
		return true;
	}

	

}
