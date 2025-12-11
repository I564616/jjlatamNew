/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.service.product;

import com.jnj.exceptions.BusinessException;


/**
 * The JnjGTUsPcmPrdExclService interface contains the declaration of all the methods of the
 * JnjGTUsPcmPrdExclServiceImpl class.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public interface JnjGTUsPcmPrdExclService
{
	/**
	 * Gets the Exclusions product attribute models.
	 *
	 * @return boolean.
	 */
	public boolean getExProductAttributeModels() throws BusinessException;
}
