/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.core.model.ItemModel;

import java.util.HashMap;
import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;


/**
 * TODO:<Sandeep kumar-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjSalesOrgCustService
{
	public boolean saveItemModel(ItemModel itemModel);

	/**
	 * This Method is used to check in the hybris data base that sales org customer exist for this b2bUnit and Sector
	 * 
	 * @param customerId
	 * @param sector
	 * @return JnJSalesOrgCustomerModel
	 */
	public JnJSalesOrgCustomerModel getSalesOrgModel(String customerId, String sector);

	/**
	 * Gets the sales org model for current user.
	 * 
	 * @return the sales org model for current user
	 */
	public List<JnJSalesOrgCustomerModel> getSalesOrgsForCurrentUser();

	/**
	 * Check cold chain storage.
	 * 
	 * @param salesOrgId
	 *           the sales org id
	 * @return true, if successful
	 */
	public boolean checkColdChainStorage(final String salesOrgId);


	/**
	 * This method used to get the mapping of product Sector related sales Org.
	 * 
	 * @return Map the sector sales org mapping
	 */
	public HashMap<String, String> getSectorAndSalesOrgMapping();


	/**
	 * This method return the Sales Org and Sap Order type on the basis on Jnj Product Model.
	 * 
	 * @param jnjProductModel
	 *           the jnj product model
	 * @return the sales org and sap order type
	 */
	public String getSalesOrgAndSapOrderType(final JnJProductModel jnjProductModel);

}
