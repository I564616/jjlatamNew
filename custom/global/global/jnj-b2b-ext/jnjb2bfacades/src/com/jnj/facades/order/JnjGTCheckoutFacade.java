/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order;

import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;

import java.text.ParseException;
import java.util.List;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.order.JnjCheckoutFacade;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCheckoutFacade extends JnjCheckoutFacade
{

	/**
	 * Place order in hybris.
	 * 
	 * @param cartCleanUpRequired
	 *           the cart clean up required
	 * @return OrderCode the string
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public String placeOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException;

	/**
	 * Place Quote order.
	 * 
	 * @return boolean representing the order Model created or not
	 * @throws InvalidCartException
	 *            is thrown by underlying {@link CartValidator}
	 */
	public String placeQuoteOrder() throws InvalidCartException;

	/**
	 * Place Return order.
	 * 
	 * @return true, if successful
	 * @throws InvalidCartException
	 *            the invalid cart exception
	 */
	public String placeReturnOrder() throws InvalidCartException;

	/**
	 * Creates the order in sap.
	 * 
	 * @param orderCode
	 *           the order code
	 * @param sapWsData
	 *           YTODO
	 * @return true, if successful
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public JnjGTOutboundStatusData createOrderInSAP(final String orderCode, JnjGTSapWsData sapWsData) throws SystemException,
			IntegrationException, ParseException, BusinessException;
	
	

	/**
	 * Creates the order in sap.
	 * 
	 * @param orderCode
	 *           the order code
	 * @param sapWsData
	 *           YTODO
	 * @return true, if successful
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public JnjGTOrderData createOrderInSAP(final String orderCode, JnjGTSapWsData sapWsData, boolean code) throws SystemException,
			IntegrationException, ParseException, BusinessException;
	
	
	/**
	 * 
	 * @param cartCleanUpRequired
	 * @return
	 * @throws InvalidCartException
	 */
	public List<String> placeSplitOrderInHybris(final boolean cartCleanUpRequired) throws InvalidCartException;
	
	/**
	 * Returns directory for Order type.
	 * @param checkoutconfirmationpage
	 * @param orderType
	 * @return
	 */
	public String getPathForView(String checkoutconfirmationpage, String orderType);
	
}
