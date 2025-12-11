/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao;

import com.jnj.core.model.JnJSalesOrgCustomerModel;


/**
 * This interface is used to call JnjSalesOrgCustomerDaoImpl class for.
 * 
 * TODO:<Sandeep Kumar - class level comments are incomplete>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjSalesOrgCustomerDao
{

	/**
	 * Gets the jn j sales org customer mode by id.
	 * 
	 * @param customerId
	 *           the customer id
	 * @param sector
	 *           the sector
	 * @return the jn j sales org customer mode by id
	 */
	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeById(String customerId, String sector);
}
