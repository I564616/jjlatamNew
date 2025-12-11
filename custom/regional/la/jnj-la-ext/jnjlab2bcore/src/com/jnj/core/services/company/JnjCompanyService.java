/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.company;

import java.util.Collection;

import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnJCompanyModel;


/**
 * Interface for Company Service.
 *
 * @author mpanda3
 * @version 1.0
 */
public interface JnjCompanyService
{

	/**
	 * Gets the master company based on the ID.
	 *
	 * @param companyUid
	 *           the company id
	 * @return the master company
	 * @throws BusinessException
	 *            the business exception
	 */
	JnJCompanyModel getMasterCompanyForUid(String companyUid) throws BusinessException;

	/**
	 * Gets the all master company.
	 *
	 * @return the all master company
	 * @throws BusinessException
	 *            the business exception
	 */
	Collection<JnJCompanyModel> getAllMasterCompanies() throws BusinessException;

}
