/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.la.b2b.occ.helper;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Objects;

import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderRequestWsDTO;


/**
 * Helper Class JnjLatamOrderHelper
 *
 */
public class JnjLatamOrderHelper
{

	private UserService userService;

	/**
	 * Sets the current user to request.
	 *
	 * @param orderRequestWsDto
	 *           the new current user to request
	 */
	public void setCurrentUserToRequest(final JnjLatamOrderRequestWsDTO orderRequestWsDto)
	{
		final UserModel currentUser = userService.getCurrentUser();
		if (Objects.nonNull(currentUser))
		{
//			orderRequestWsDto.setUser(currentUser.getUid());
		}
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
