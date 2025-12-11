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
package com.jnj.b2b.la.jnjlaawstokenservice.setup;

import static com.jnj.b2b.la.jnjlaawstokenservice.constants.JnjlaawstokenserviceConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.jnj.b2b.la.jnjlaawstokenservice.constants.JnjlaawstokenserviceConstants;
import com.jnj.b2b.la.jnjlaawstokenservice.service.JnjlaawstokenserviceService;


@SystemSetup(extension = JnjlaawstokenserviceConstants.EXTENSIONNAME)
public class JnjlaawstokenserviceSystemSetup
{
	private final JnjlaawstokenserviceService jnjlaawstokenserviceService;

	public JnjlaawstokenserviceSystemSetup(final JnjlaawstokenserviceService jnjlaawstokenserviceService)
	{
		this.jnjlaawstokenserviceService = jnjlaawstokenserviceService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		jnjlaawstokenserviceService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return JnjlaawstokenserviceSystemSetup.class.getResourceAsStream("/jnjlaawstokenservice/sap-hybris-platform.png");
	}
}
