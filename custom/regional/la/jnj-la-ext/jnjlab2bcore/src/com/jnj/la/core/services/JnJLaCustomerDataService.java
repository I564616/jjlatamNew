/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services;

import java.util.List;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;

/**
 * This interface is used get customer data.
 * @author mpanda3
 * @version 1.0
 */
public interface JnJLaCustomerDataService extends JnJCustomerDataService
{


	/**
	 * @param country
	 * @return List<JnjIndirectCustomerModel>
	 */
	public List<JnjIndirectPayerModel> getJnjIndirectPayers(String country);

	/**
	 * @param customerNumber
	 * @param country
	 * @return JnjIndirectCustomerModel
	 */
	public JnjIndirectCustomerModel getJnjIndirectCustomerByIDCountry(String customerNumber, String country);

	public JnjIndirectPayerModel getJnjIndirectPayerByIDCountry(String customerNumber, String country);

	public List<String> getIndirectCustomer(final String country);

	public List<String> getIndirectCustomer(final String siteDefaultCountry, final String term);

	public List<String> getIndirectPayer(String country);

	public List<String> getIndirectPayer(String siteDefaultCountry, final String term);

	public JnjIndirectCustomerModel getIndirectCustomerModel(String country, String indirectCustomer);

	public JnjIndirectPayerModel getIndirectPayerModel(String country, String indirectPayer);
	
	public List<JnJLaUserAccountPreferenceModel> getActiveUserNotificationPreference();
	
	public List<JnJB2bCustomerModel> getActiveUsersReport();
}