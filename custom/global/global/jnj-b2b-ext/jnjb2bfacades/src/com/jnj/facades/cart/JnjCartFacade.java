/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.cart;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjValidateOrderData;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.c2l.CountryModel;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCartFacade extends CartFacade
{

	/**
	 * Adds the to cart.
	 * 
	 * @param productCodeList
	 *           the product code list
	 * @return the list
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	public JnjCartModificationData addToCart(List<String> productCodeList) throws CommerceCartModificationException;


	/**
	 * The saveExpDeliveryDate method saves the expDeliveryDate for all the entry in the hybris data base.
	 * 
	 * @param expDeliveryDate
	 *           the exp delivery date
	 * @param cartEntryId
	 *           the cart entry id
	 * @return true, if successful
	 */
	public boolean updateDeliveryDate(final String expDeliveryDate, final int cartEntryId);

	/**
	 * The getPrice method calls the JnjOrderFacadeImpl class to retrieve the price value from the SAP and then saves
	 * that values in the hybris data base.
	 * 
	 * @param entryNumber
	 *           the entry number
	 * @param deliveryAddressId
	 *           the delivery address id
	 * @return the price
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public Map<String, String> getPrice(String entryNumber, final String deliveryAddressId) throws IntegrationException;



	/**
	 * The placeOrderInSap method passes the orderModel object to JnjOrderFacadeImpl class so that SAP order details is
	 * updated in hybris data base.
	 * 
	 * @param orderCode
	 *           the order code
	 * @return JnjOrderData the jnjorderdata
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public JnjOrderData placeOrderInSap(final String orderCode) throws SystemException, IntegrationException;

	/**
	 * Update shipping adress.
	 * 
	 * @param string
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateShippingAdress(String string);

	/**
	 * The validate order method is used to invoke the JnjOrderFacadeImpl class so that SAP order service validate the
	 * current cart.
	 * 
	 * @return true, if successful
	 * @throws IntegrationException
	 *            the integration exception
	 */
	// Change the return type for CP-002
	public JnjValidateOrderData validateOrder() throws IntegrationException, SystemException, BusinessException;

	/**
	 * The updateChangePrice method updates the change price for the entry in hybris data base with reason.
	 * 
	 * @param changePrice
	 *           the change price
	 * @param reason
	 *           the reason
	 * @param cartEntryId
	 *           the cart entry id
	 * @return String, updatedChanged Price value
	 */
	public String updateChangePrice(final String changePrice, final String reason, final int cartEntryId);

	/**
	 * The getChangeReasonData method is used to call JnjCartServiceImpl class to get the data from the change reason
	 * data.
	 * 
	 * @return the change reason data
	 */
	public List<String> getChangeReasonData();

	/**
	 * Adds the to cart.
	 * 
	 * @param code
	 *           the code
	 * @param quantity
	 *           the quantity
	 * @return the cart modification data
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	public CartModificationData addToCart(final String code, final long quantity) throws CommerceCartModificationException;


	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param orderId
	 */
	public void createCartFromOrder(String orderId);

	/**
	 * This method returns the jnj order entry data on the basis of entry number.
	 * 
	 * 
	 * return jnjOrderEntryData object
	 */
	public OrderEntryData getOrderEntryData(final int entryNumber);



	/**
	 * Adds the to cart.
	 * 
	 * @param productCode
	 *           the product code
	 * @param productQuantity
	 *           the product quantity
	 * @return the jnj cart modification data
	 * @throws CommerceCartModificationException
	 *            the commerce cart modification exception
	 */
	public JnjCartModificationData addToCartQuick(String productCode, String productQuantity,List<CartModificationData> cartModificationList) throws CommerceCartModificationException;

	/**
	 * @param productId
	 * @return
	 */
	
	public JnjCartModificationData addToCart(String productCode, String productQuantity) throws CommerceCartModificationException;
	
	
	public boolean isProductCodeValid(String productId);

	/**
	 * This method is used to get the cart model which will further call calculateCartModel(CartModel)
	 */

	public void calculateSessionCart();

	/**
	 * Checks if is request complete order contains any product other than Pharma.
	 * 
	 * @return true, if is request complete order
	 */
	public boolean isRequestCompleteOrder();
	
	public boolean updateCartModel(JnjGTCartData cart);
}
