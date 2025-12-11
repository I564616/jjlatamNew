/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.la.core.daos;

import java.util.List;

import com.jnj.core.dao.JnJCustomerDataDao;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;


/**
 *
 * @author mpanda3
 * @version 1.0
 */
public interface JnJLaCustomerDataDao extends JnJCustomerDataDao
{

	public JnjIndirectPayerModel getIndirectPayerByIdCountry(String customerNumber, String country);

	public List<JnjIndirectPayerModel> getIndirectPayer(String country);

	public List<JnjIndirectPayerModel> getIndirectPayer(final String country, String term);

	public List<JnjIndirectCustomerModel> getIndirectCustomer(final String country, String term);

	public JnjIndirectCustomerModel getIndirectCustomerModel(String country, String indirectCustomer);

	public JnjIndirectPayerModel getIndirectPayerModel(String country, String indirectPayer);
	
	public List<JnJB2bCustomerModel> getActiveUsers();
	
	public List<JnJLaUserAccountPreferenceModel> getActiveUsersNotification();

}
