/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.pcm.integration.util;



/**
 * This is a util interface which contains logic for connecting to rest API and fetching properties from properties
 * file.
 *
 */
public interface JnjPCMCommonFacadeUtil
{
	/**
	 * This method gets start date from properties.
	 *
	 * @return String
	 */
	public String getStartDate();

	/**
	 * This method gets access token to connect to P360 API
	 *
	 * @return String
	 */
	public String getAccessToken();


}
