/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.sso.service;

import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.SSOUserService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class JnJLABackofficeSSOService implements SSOUserService
{
	private static final Logger LOG = Logger.getLogger(JnJLABackofficeSSOService.class);

	@Resource
	private UserService userService;

	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
	{
		LOG.info("getOrCreateSSOUser - id: " + id.toLowerCase() + " getOrCreateSSOUser - name: " + name + " getOrCreateSSOUser - roles: " + roles);
		if (StringUtils.isEmpty(id))
		{
			throw new IllegalArgumentException("User info must not be empty");
		}
		try
		{
			final String userId = id.substring(0, id.indexOf("@")).toLowerCase();
			LOG.info("getOrCreateSSOUser - userId: " + userId);
			LOG.info("getOrCreateSSOUser - USerforuid " + this.userService.getUserForUID(userId, EmployeeModel.class));
			return this.userService.getUserForUID(userId, EmployeeModel.class);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Customer not found for UID: " + id, e);
			return null;
		}
	}
}