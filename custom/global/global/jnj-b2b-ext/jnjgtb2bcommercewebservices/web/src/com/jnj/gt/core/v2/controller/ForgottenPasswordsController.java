/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.jnj.gt.core.v2.controller;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;



@Controller
@RequestMapping(value = "/{baseSiteId}/forgottenpasswordtokens")
@CacheControl(directive = CacheControlDirective.NO_STORE)
@Tag(name = "Forgotten Passwords")
public class ForgottenPasswordsController extends BaseController
{
	private static final Logger LOG = Logger.getLogger(ForgottenPasswordsController.class);
	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;


	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT" })
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.ACCEPTED)
	@Operation(summary = "Generates a token to restore a customer's forgotten password.", description = "Generates a token to restore a customer's forgotten password.")
	@ApiBaseSiteIdParam
	public void restorePassword(
			@Parameter(description = "Customer's user id. Customer user id is case insensitive.", required = true) @RequestParam final String userId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("restorePassword: uid=" + sanitize(userId));
		}
		customerFacade.forgottenPassword(userId);
	}
}
