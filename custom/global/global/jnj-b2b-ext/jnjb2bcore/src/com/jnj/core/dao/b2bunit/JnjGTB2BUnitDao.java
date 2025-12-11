/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.b2bunit;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTSalesOrgCustomerModel;



/**
 * The JnjGTB2BUnitDao interface is an interface of JnjGTB2BUnitDaoImpl class which contains methods declaration of all
 * the methods of the aforementioned class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTB2BUnitDao
{

	/**
	 * Gets the sales org customer model.
	 * 
	 * @param salesOrg
	 *           the sales org
	 * @param distribChannel
	 *           the distrib channel
	 * @param division
	 *           the division
	 * @param sourceSysId
	 * 
	 * @return the sales org customer model
	 */
	public JnjGTSalesOrgCustomerModel getSalesOrgCustomerModel(final String salesOrg, final String distribChannel,
			final String division, String sourceSysId);

	/**
	 * @param uid
	 * @return
	 */
	public JnJB2BUnitModel getB2BUnitByUid(String uid);
	
	public JnJB2BUnitModel getJnJB2BUnitByUid(String uid);
}
