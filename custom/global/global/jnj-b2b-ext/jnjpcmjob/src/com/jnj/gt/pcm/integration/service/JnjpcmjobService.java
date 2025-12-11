/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.jnj.gt.pcm.integration.service;

/**
 * Interface 
 */
public interface JnjpcmjobService
{
	/**
	 * Fetches Hybris Logo Url
	 * @param logoCode the code to be used
	 * @return String
	 */
	String getHybrisLogoUrl(String logoCode);

	/**
	 * Creates Hybris Logo
	 * @param logoCode the code to be used
	 */
	void createLogo(String logoCode);
}
