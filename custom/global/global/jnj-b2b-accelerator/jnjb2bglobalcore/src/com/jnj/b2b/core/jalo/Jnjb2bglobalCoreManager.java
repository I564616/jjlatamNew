/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *  
 */
package com.jnj.b2b.core.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.jnj.b2b.core.constants.Jnjb2bglobalCoreConstants;
import com.jnj.b2b.core.setup.CoreSystemSetup;

import org.apache.log4j.Logger;


/**
 * Don't use. User {@link CoreSystemSetup} instead.
 */
@SuppressWarnings("PMD")
public class Jnjb2bglobalCoreManager extends GeneratedJnjb2bglobalCoreManager
{
	@SuppressWarnings("unused")
	private static Logger LOG = Logger.getLogger(Jnjb2bglobalCoreManager.class.getName());

	public static final Jnjb2bglobalCoreManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Jnjb2bglobalCoreManager) em.getExtension(Jnjb2bglobalCoreConstants.EXTENSIONNAME);
	}
}
