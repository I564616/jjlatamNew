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
package com.jnj.b2b.store.controller;

import static com.jnj.b2b.store.constants.Jnjb2bsvstoreConstants.PLATFORM_LOGO_CODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.jnj.b2b.store.service.Jnjb2bsvstoreService;


@Controller
public class Jnjb2bsvstoreHelloController
{
	@Autowired
	private Jnjb2bsvstoreService jnjb2bsvstoreService;

	@GetMapping("/")
	public String printWelcome(final ModelMap model)
	{
		model.addAttribute("logoUrl", jnjb2bsvstoreService.getHybrisLogoUrl(PLATFORM_LOGO_CODE));
		return "welcome";
	}
}
