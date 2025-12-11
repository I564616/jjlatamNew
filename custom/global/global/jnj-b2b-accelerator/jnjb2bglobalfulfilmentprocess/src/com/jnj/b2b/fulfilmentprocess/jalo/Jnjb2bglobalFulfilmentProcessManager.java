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
package com.jnj.b2b.fulfilmentprocess.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;
import com.jnj.b2b.fulfilmentprocess.constants.Jnjb2bglobalFulfilmentProcessConstants;

import org.apache.log4j.Logger;

@SuppressWarnings("PMD")
public class Jnjb2bglobalFulfilmentProcessManager extends GeneratedJnjb2bglobalFulfilmentProcessManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( Jnjb2bglobalFulfilmentProcessManager.class.getName() );
	
	public static final Jnjb2bglobalFulfilmentProcessManager getInstance()
	{
		ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Jnjb2bglobalFulfilmentProcessManager) em.getExtension(Jnjb2bglobalFulfilmentProcessConstants.EXTENSIONNAME);
	}
	
}
