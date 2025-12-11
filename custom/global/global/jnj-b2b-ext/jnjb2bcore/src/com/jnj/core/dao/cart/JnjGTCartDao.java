/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.cart;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.List;

import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTShippingMethodModel;



/**
 * TODO:<class level comments are missing>.
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCartDao
{

	/**
	 * Gets the shipping methods.
	 *
	 * @return the shipping methods
	 */
	List<JnjGTShippingMethodModel> getShippingMethods();

	/**
	 * Checks if is valid lot number.
	 *
	 * @param invoiceNum
	 *           the invoice num
	 * @param lotNumber
	 *           the lot number
	 * @return true, if is valid lot number
	 */
	boolean isValidLotNumber(final String invoiceNum, final String lotNumber);


	/**
	 * Validate invoice number.
	 *
	 * @param product
	 *           the product
	 * @param returnInvNumber
	 *           the return inv number
	 * @return true, if successful
	 */
	boolean validateInvoiceNumber(final JnJProductModel product, final String returnInvNumber);


	/**
	 * Validate lot number.
	 *
	 * @param product
	 *           the product
	 * @param batchNumber
	 *           the batch number
	 * @return true, if successful
	 */
	boolean validateLotNumber(final JnJProductModel product, final String batchNumber);

	/**
	 * Gets the cart for site and user.
	 *
	 * @param site
	 *           the site
	 * @param user
	 *           the user
	 * @param b2BUnit
	 *           the b2 b unit
	 * @return the cart for site and user
	 */
	public CartModel getCartForUserAndUnit(final BaseSiteModel site, final UserModel user, final B2BUnitModel b2BUnit);

	/**
	 * @param cOMPATIBLE_SHIPPING_METHODS_ALL_DIVISONS
	 * @return
	 */
	public List<JnjGTShippingMethodModel> getFasterShippingMethods(int rank, List<String> routeList);

	/**
	 * @param requiredIds
	 * @return
	 */
	public List<JnjGTShippingMethodModel> getRequiredShippingMethod(List<String> requiredIds);

	/**
	 * Find invoice entry for given Product in given invoice number.
	 *
	 * @param product
	 *           the product
	 * @param invoiceNumber
	 *           the invoice number
	 * @return JnjGTInvoiceEntryModel invoice entry model
	 */
	public JnjGTInvoiceEntryModel findInvoiceEntryforProduct(final JnJProductModel product, final String invoiceNumber);

	boolean isValidLotNumForProduct(String lotNumber, String pcode);
}
