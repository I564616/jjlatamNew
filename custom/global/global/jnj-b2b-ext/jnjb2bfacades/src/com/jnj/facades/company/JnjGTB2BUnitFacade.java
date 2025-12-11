/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.company;

import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jnj.facades.company.JnjB2BUnitFacade;
import com.jnj.core.model.JnJB2BUnitModel;


/**
 * @author neeraj.m.kumar
 * 
 */
public interface JnjGTB2BUnitFacade extends JnjB2BUnitFacade
{

	/**
	 * This method is used for get type of the account MDD/CONS
	 * 
	 * @return the soruce system id for unit
	 */
	String getSourceSystemIdForUnit();

	/**
	 * Gets the account type for given jnjb2bunit, if the parameter is null value will be returned on the basis of
	 * CurrentB2Bunit of logged in user.
	 * 
	 * @param unitModel
	 *           JnJB2BUnitModel
	 * @return the account Type String
	 */
	String getAccountType(JnJB2BUnitModel unitModel);

	/**
	 * Gets the available order type for the current Unit and logged in user.
	 * 
	 * @return orderTypes List
	 */
	public Set<String> getOrderTypesForAccount();

	/**
	 * Gets the contact address for current User's B2BUnuit
	 * 
	 * @return the contact address for current B2BUnit
	 */
	public AddressData getContactAddress();

	/**
	 * Gets the all drop ship accounts.
	 * 
	 * @return the all drop ship accounts
	 */
	public Collection<AddressData> getAllDropShipAccounts();

	/**
	 * Gets the shipping address for current User's B2BUnuit
	 * 
	 * @return the shipping address for current B2BUnit
	 */
	public AddressData getShippingAddress();

	/**
	 * Evaluates if the current customer in session is International Affiliate or not.
	 * 
	 * @return boolean
	 */
	public boolean isCustomerInternationalAff();
	
	/**
	 * This method returns list of available order types for the user
	 * @param b2bUnitList
	 * @return
	 */
	public Set<String> getAvailableOrderTypes(List<B2BUnitData> b2bUnitList);
	//3088
	public List<AddressData> getSearchShippingAddress(final String searchItem);
	
	public List<AddressData> getSearchBillingAddress(final String searchItem);
}
