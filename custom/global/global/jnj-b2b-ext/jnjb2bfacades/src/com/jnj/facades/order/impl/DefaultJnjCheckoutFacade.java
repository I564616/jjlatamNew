/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCheckoutDTO;
import com.jnj.core.dto.SplitOrderData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.synchronizeOrders.JnjSAPOrdersService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.order.JnjCheckoutFacade;



/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjCheckoutFacade extends DefaultCheckoutFacade implements JnjCheckoutFacade
{
	private static final Logger LOGGER = Logger.getLogger(JnjOrderUtil.class);

	@Autowired
	B2BOrderService b2bOrderService;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil getCurrentDefaultB2BUnitUtil;

	@Autowired
	JnjSAPOrdersService jnjSAPOrderService;

	@Autowired
	JnjCommonFacadeUtil jnjCommonFacadeUtil;
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	@Autowired
	protected UserService userService;

	/**
	 * Place order is override for adding additional sap specific attributes.
	 * 
	 * @return the order data
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	@Override
	public OrderData placeOrder() throws InvalidCartException
	{

		final CartModel cartModel = getCart();
		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();
			cartModel.getPurchaseOrderNumber();
			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				beforePlaceOrder(cartModel);

				final OrderModel orderModel = placeOrder(cartModel);
				afterPlaceOrder(cartModel, orderModel);
				// Convert the order to an order data
				if (orderModel != null)
				{
					orderModel.setStatus(OrderStatus.PENDING);
					b2bOrderService.saveOrder(orderModel);
					return getOrderConverter().convert(orderModel);
				}
			}
		}

		return null;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public List<String> placeOrder(final JnjCheckoutDTO jnjCheckoutDTO) throws InvalidCartException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PLACE_ORDER + Logging.HYPHEN + "placeOrder()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final CartModel cartModel = getCart();
		List<String> orderDatas = null;

		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();

			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				cartModel.setUnit(((B2BCustomerModel) currentUser).getDefaultB2BUnit());

				final Map<SplitOrderData, List<AbstractOrderEntryModel>> salesOrgAndCartEntriesMap = JnjOrderUtil
						.getSalesOrgFromAbstOrdEntModel(cartModel);
				if (!salesOrgAndCartEntriesMap.isEmpty())
				{
					orderDatas = new ArrayList<String>();
					OrderModel orderModel = null;
					// Iterating the map entry one by one.
					for (final Map.Entry<SplitOrderData, List<AbstractOrderEntryModel>> entry : salesOrgAndCartEntriesMap.entrySet())
					{
						cartModel.setEntries(entry.getValue());
						orderModel = placeOrder(cartModel);
						orderDatas.add(orderModel.getCode());
						orderModel.setStatus(OrderStatus.CREATED);
						orderModel.setSalesOrganizationCode(entry.getValue().get(0).getSalesOrg());
					}
					afterPlaceOrder(cartModel, orderModel);
				}
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PLACE_ORDER + Logging.HYPHEN + "placeOrder()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderDatas;
	}




	@Override
	public void placeOrderfromReplenishJob(final CartModel cartModel) throws InvalidCartException
	{

		if (cartModel != null)
		{
			final UserModel currentUser = getCurrentUserForCheckout();

			if (cartModel.getUser().equals(currentUser) || getCheckoutCustomerStrategy().isAnonymousCheckout())
			{
				cartModel.setUnit(((B2BCustomerModel) currentUser).getDefaultB2BUnit());

				final Map<SplitOrderData, List<AbstractOrderEntryModel>> salesOrgAndCartEntriesMap = JnjOrderUtil
						.getSalesOrgFromAbstOrdEntModel(cartModel);
				if (!salesOrgAndCartEntriesMap.isEmpty())
				{
					OrderModel orderModel = null;
					for (final Map.Entry<SplitOrderData, List<AbstractOrderEntryModel>> salesOrgAndCartEntriesObject : salesOrgAndCartEntriesMap
							.entrySet())
					{
						cartModel.setEntries(salesOrgAndCartEntriesObject.getValue());
						orderModel = placeOrder(cartModel);
						if (StringUtils.isEmpty(orderModel.getPurchaseOrderNumber()))
						{
							orderModel.setPurchaseOrderNumber(orderModel.getCode());
						}
						orderModel.setStatus(OrderStatus.CREATED);
						b2bOrderService.saveOrder(orderModel);
					}
				}
			}
		}
	}

	@Override
	public boolean sendOrderStatusEmail(final String orderCode, final String baseUrl, final Boolean isSyncOrder)
	{
		try
		{
			/*START AAOL 4911*/
			JnJB2bCustomerModel currentUser = null;
			final UserModel user = userService.getCurrentUser();
			List<String> useremailpreferencelist = new ArrayList<String>(); 
			if(user instanceof JnJB2bCustomerModel) {
				currentUser = (JnJB2bCustomerModel) user;
				Set<PrincipalModel> setofMembersforAccount = currentUser.getCurrentB2BUnit().getMembers();
				for (PrincipalModel principalModel : setofMembersforAccount) {
					JnJB2bCustomerModel jnjB2bCustomer = (JnJB2bCustomerModel) principalModel;
					List<String> emailPreferencesofuser = jnjB2bCustomer.getEmailPreferences();
					if(emailPreferencesofuser.contains("emailPreference15") || emailPreferencesofuser.contains("emailPreference16")){
						LOGGER.debug("START - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
						jnjSAPOrderService.sendStatusChangeNotification(getCurrentUserForCheckout(), orderCode, baseUrl,
								jnjCommonFacadeUtil.createMediaLogoURL(),jnjB2bCustomer.getEmail());
						LOGGER.debug("END - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
					} else if(((JnJB2bCustomerModel)userService.getCurrentUser()).equals(jnjB2bCustomer)) {
						jnjSAPOrderService.sendStatusChangeNotification(getCurrentUserForCheckout(), orderCode, baseUrl,
								jnjCommonFacadeUtil.createMediaLogoURL(),jnjB2bCustomer.getEmail());
						LOGGER.debug("END - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
					}
				}
			}
			/*END AAOL 4911*/
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	}


	@Override
	public boolean sendOrderStatusEmail(final String sapOrderNumber, final String clientOrderNumber, final String jnjOrderNumber,
			final OrderStatus currentStatus, final OrderStatus previousStatus, final String baseUrl, final Boolean isSyncOrder)
	{
		try
		{
			/*START AAOL 4911*/
			JnJB2bCustomerModel currentUser = null;
			final UserModel user = userService.getCurrentUser();
			List<String> useremailpreferencelist = new ArrayList<String>(); 
			if(user instanceof JnJB2bCustomerModel) {
				currentUser = (JnJB2bCustomerModel) user;
				Set<PrincipalModel> setofMembersforAccount = currentUser.getCurrentB2BUnit().getMembers();
				for (PrincipalModel principalModel : setofMembersforAccount) {
					JnJB2bCustomerModel jnjB2bCustomer = (JnJB2bCustomerModel) principalModel;
					List<String> emailPreferencesofuser = jnjB2bCustomer.getEmailPreferences();
					if(emailPreferencesofuser.contains("emailPreference15") || emailPreferencesofuser.contains("emailPreference16")){
						jnjSAPOrderService.sendStatusChangeNotification(getCurrentUserForCheckout(), sapOrderNumber, clientOrderNumber,
								jnjOrderNumber, currentStatus, previousStatus, baseUrl, isSyncOrder, jnjCommonFacadeUtil.createMediaLogoURL(), jnjB2bCustomer.getEmail());
					} else if(((JnJB2bCustomerModel)userService.getCurrentUser()).equals(jnjB2bCustomer)) {
						jnjSAPOrderService.sendStatusChangeNotification(getCurrentUserForCheckout(), sapOrderNumber, clientOrderNumber,
								jnjOrderNumber, currentStatus, previousStatus, baseUrl, isSyncOrder, jnjCommonFacadeUtil.createMediaLogoURL(), jnjB2bCustomer.getEmail());
					}
				}
			}
			/*END AAOL 4911*/
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.facades.order.JnjCheckoutFacade#getCurrentUserForCheckoutForEmail()
	 */
	@Override
	public CustomerModel getCurrentUserForCheckoutForEmail()
	{
		return getCurrentUserForCheckout();
	}
	
	@Override
	public boolean sendReturnOrderUserEmail(String orderCode, String baseUrl){

		try
		{
			JnJB2bCustomerModel currentUser = null;
			final UserModel user = userService.getCurrentUser();

			if(user instanceof JnJB2bCustomerModel) {
				currentUser = (JnJB2bCustomerModel) user;
				/*Set<PrincipalModel> setofMembersforAccount = currentUser.getCurrentB2BUnit().getMembers();
				for (PrincipalModel principalModel : setofMembersforAccount) {
					JnJB2bCustomerModel jnjB2bCustomer = (JnJB2bCustomerModel) principalModel;
					List<String> emailPreferencesofuser = jnjB2bCustomer.getEmailPreferences();
					if(emailPreferencesofuser.contains("emailPreference15") || emailPreferencesofuser.contains("emailPreference16")){
						LOGGER.debug("START - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
						jnjSAPOrderService.sendReturnOrderUserEmail(getCurrentUserForCheckout(), orderCode, baseUrl,
								jnjCommonFacadeUtil.createMediaLogoURL(),jnjB2bCustomer.getEmail());
						LOGGER.debug("END - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
					} else if(((JnJB2bCustomerModel)userService.getCurrentUser()).equals(jnjB2bCustomer)) {
						jnjSAPOrderService.sendReturnOrderUserEmail(getCurrentUserForCheckout(), orderCode, baseUrl,
								jnjCommonFacadeUtil.createMediaLogoURL(),jnjB2bCustomer.getEmail());
						LOGGER.debug("END - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
					}*/
					LOGGER.debug("START - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
					jnjSAPOrderService.sendReturnOrderUserEmail(getCurrentUserForCheckout(), orderCode, baseUrl,
							jnjCommonFacadeUtil.createMediaLogoURL(),currentUser.getEmail());
					LOGGER.debug("END - jnjSAPOrderService.sendStatusChangeNotification() for the order.. " + orderCode);
				
				}
			
			
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	
	}
	
	@Override
	public boolean sendReturnOrderCSREmail(String orderCode, String baseUrl){

		try
		{			
			String csrEmail  = Config.getParameter(Jnjb2bCoreConstants.CUSTOMER_SERVICE_EMAIL);		
			LOGGER.debug("START - jnjSAPOrderService.sendReturnOrderCSREmail() for the order.. " + orderCode);
			jnjSAPOrderService.sendReturnOrderCSREmail(getCurrentUserForCheckout(), orderCode, baseUrl,
					jnjCommonFacadeUtil.createMediaLogoURL(),csrEmail);
			LOGGER.debug("END - jnjSAPOrderService.sendReturnOrderCSREmail() for the order.. " + orderCode);
			
				
		}
		catch (final Exception e)
		{
			return false;
		}
		return true;
	
	}
}
