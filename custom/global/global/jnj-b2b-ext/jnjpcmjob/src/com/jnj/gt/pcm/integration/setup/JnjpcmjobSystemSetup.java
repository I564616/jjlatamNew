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
package com.jnj.gt.pcm.integration.setup;

import static com.jnj.gt.pcm.integration.constants.JnjpcmjobConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import com.jnj.gt.pcm.integration.constants.JnjpcmjobConstants;
import com.jnj.gt.pcm.integration.service.JnjpcmjobService;


@SystemSetup(extension = JnjpcmjobConstants.EXTENSIONNAME)
public class JnjpcmjobSystemSetup
{
	private final JnjpcmjobService jnjpcmjobService;

	public JnjpcmjobSystemSetup(final JnjpcmjobService jnjpcmjobService)
	{
		this.jnjpcmjobService = jnjpcmjobService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		jnjpcmjobService.createLogo(PLATFORM_LOGO_CODE);
	}

	
}
