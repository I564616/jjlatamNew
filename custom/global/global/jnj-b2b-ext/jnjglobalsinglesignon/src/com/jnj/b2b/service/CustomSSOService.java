/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.b2b.service;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.samlsinglesignon.DefaultSSOService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2bCustomerModel;


/**
 *
 */
public class CustomSSOService extends DefaultSSOService
{
	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
	{
		if (CollectionUtils.isEmpty(roles))
		{
			roles.add("ascustomer");
		}
		final SSOUserMapping userMapping = findMapping(roles);

		if (StringUtils.isEmpty(id) || StringUtils.isEmpty(name))
		{
			throw new IllegalArgumentException("User info must not be empty");
		}

		if (userMapping != null)
		{

			UserModel user = lookupExisting(id.toLowerCase(), userMapping);
			if (user == null)
			{
				user = createNewUser(id, name, userMapping);
			}
			adjustUserAttributes(user, userMapping);
			((JnJB2bCustomerModel) user).setSsoLogin(Boolean.TRUE);
			modelService.save(user);

			return user;
		}
		else
		{
			throw new IllegalArgumentException("No SSO user mapping available for roles " + roles + " - cannot accept user " + id);
		}
	}

	/**
	 * create a new user
	 *
	 * @param id
	 *           to be used as the user Id
	 * @param name
	 *           name of the user
	 * @param userMapping
	 *           user mappings (groups and user type)
	 * @return a new user model
	 */
	@Override
	protected UserModel createNewUser(final String id, final String name, final SSOUserMapping userMapping)
	{
		final UserModel user = super.createNewUser(id, name, userMapping);
		((JnJB2bCustomerModel) user).setEmail(id);
		return user;
	}
}
