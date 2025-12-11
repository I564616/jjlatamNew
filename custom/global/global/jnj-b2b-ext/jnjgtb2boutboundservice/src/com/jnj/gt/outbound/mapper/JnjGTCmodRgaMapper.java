/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper;

import de.hybris.platform.acceleratorservices.dataexport.googlelocal.model.Business;

import java.util.Map;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;



/**
 * The JnjNACmodRgaMapper interface contains the declaration of all the method of the JnjNACmodRgaMapperImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTCmodRgaMapper
{
	/**
	 * Map cmod/rga request and response by using incoming argument and fetching some value from the properties file.
	 * 
	 * @param orderNumber
	 *           the order number
	 * @param isCmodCall
	 *           the is cmod call
	 * @return byte array in map
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws Business
	 *            Exception the business exception
	 */
	public Map<String, byte[]> mapCmodRgaRequestResponse(final String orderNumber, final boolean isCmodCall)
			throws IntegrationException, BusinessException;
}
