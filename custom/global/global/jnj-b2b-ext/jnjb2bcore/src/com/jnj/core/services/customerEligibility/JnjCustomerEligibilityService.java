/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.customerEligibility;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.Set;


/**
 * TODO:<Akash-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCustomerEligibilityService
{
	/**
	 * Saves item using <code>ModelService</code>
	 * 
	 * @param item
	 */
	void saveItem(ItemModel item) throws ModelSavingException;

	/**
	 * Finds B2b Unit by Id.
	 * 
	 * @param uid
	 * @return B2BUnitModel
	 */
	B2BUnitModel getB2bUnitAccount(final String uid);

	/**
	 * Finds the associated Online <code>CatalogVersion</code> for the B2B Unit based on the associated country.
	 * 
	 * @param categoryCode
	 * @param unitCountryCode
	 * @return CategoryModel
	 */
	CategoryModel getCategoryByCode(final String categoryCode, final String unitCountryCode);

	/**
	 * Finds category using code associated with it.
	 * 
	 * @param b2bUnitUid
	 * @return Set<String>
	 */
	Set<String> getRestrictedCategory(String b2bUnitUid);
}
