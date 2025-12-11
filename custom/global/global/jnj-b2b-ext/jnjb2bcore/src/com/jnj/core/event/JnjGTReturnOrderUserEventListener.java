/**
 * 
 */
package com.jnj.core.event;

import jakarta.annotation.Resource;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjReturnOrderUserProcessModel;
import com.jnj.core.services.synchronizeOrders.impl.DefaultJnjSAPOrdersService;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import com.jnj.core.event.JnjGTReturnOrderUserEvent;
/**
 * @author nsinha7
 *
 */
public class JnjGTReturnOrderUserEventListener extends AbstractSiteEventListener<JnjGTReturnOrderUserEvent> {
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
	protected void onSiteEvent(final JnjGTReturnOrderUserEvent returnOrderUserNotificationEvent)
	{

		final JnjReturnOrderUserProcessModel returnOrderUserProcessModel = (JnjReturnOrderUserProcessModel) businessProcessService
				.createProcess("" + "-" + System.currentTimeMillis(), "jnjReturnOrderUserProcess");

		returnOrderUserProcessModel.setSite(returnOrderUserNotificationEvent.getSite());
		returnOrderUserProcessModel.setCustomer(returnOrderUserNotificationEvent.getCustomer());
		returnOrderUserProcessModel.setLanguage(returnOrderUserNotificationEvent.getLanguage());
		returnOrderUserProcessModel.setCurrency(returnOrderUserNotificationEvent.getCurrency());
		returnOrderUserProcessModel.setStore(returnOrderUserNotificationEvent.getBaseStore());
		returnOrderUserProcessModel.setCustomer(returnOrderUserNotificationEvent.getCustomer());
		returnOrderUserProcessModel.setBaseUrl(returnOrderUserNotificationEvent.getBaseUrl());
		/*START AAOL 4911*/
		returnOrderUserProcessModel.setToEmail(returnOrderUserNotificationEvent.getToEmail());
		/*END AAOL 4911*/
		
		if (null != returnOrderUserNotificationEvent.getOrderCode())
		{
			//userService.getAdminUser();
			//final OrderModel orderModel = b2boOrderService.getOrderForCode(returnOrderUserNotificationEvent.getOrderCode());
			//final OrderModel orderModel = b2boOrderService.getOrderForCode("00002000");
			
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2boOrderService.getOrderForCode(returnOrderUserNotificationEvent.getOrderCode());
				}
			}, userService.getAdminUser());
			
		
			if (null != orderModel)
			{
				returnOrderUserProcessModel.setOrder(orderModel);
				CommonUtil.logDebugMessage("Order Status Notification", "onSiteEvent()",
						"Order Has been set in the returnOrderUserProcessModel!", LOGGER);
			}
		}
		else
		{
			returnOrderUserProcessModel.setSapOrderNumber(returnOrderUserNotificationEvent.getSapOrderNumber());
			returnOrderUserProcessModel.setJnjOrderNumber(returnOrderUserNotificationEvent.getJnjOrderNumber());

		}
		
		try
		{
			modelService.save(returnOrderUserProcessModel);
			LOGGER.debug("Saving returnOrderUserProcessModel.. " + returnOrderUserProcessModel);
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error("Saving 'JnjInterfaceNotificationProcessModel' has caused exception: " + modelSavingException.getMessage());
		}

		try{
		LOGGER.debug("Start Processing mail for Order Status Notification.. " + returnOrderUserNotificationEvent.getJnjOrderNumber());
		businessProcessService.startProcess(returnOrderUserProcessModel);
		LOGGER.debug("End Processed mail successfully for Order Status Notification.. " + returnOrderUserNotificationEvent.getJnjOrderNumber());
		}catch(Exception e){
			LOGGER.error("Exception occured while processing mail for the order .." + returnOrderUserNotificationEvent.getJnjOrderNumber());
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
	protected boolean shouldHandleEvent(final JnjGTReturnOrderUserEvent arg0)
	{
		return true;
	}

	

}
