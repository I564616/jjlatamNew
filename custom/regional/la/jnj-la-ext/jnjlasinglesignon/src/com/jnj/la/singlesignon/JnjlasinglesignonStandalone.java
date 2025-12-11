/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.singlesignon;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import org.apache.log4j.Logger;

/**
 * Demonstration of how to write a standalone application that can be run directly from within eclipse or from the
 * commandline.<br>
 * To run this from commandline, just use the following command:<br>
 * <code>
 * java -jar bootstrap/bin/ybootstrap.jar "new com.jnj.la.singlesignon.JnjlasinglesignonStandalone().run();"
 * </code> From eclipse, just run as Java Application. Note that you maybe need to add all other projects like
 * ext-commerce, ext-pim to the Launch configuration classpath.
 */
public class JnjlasinglesignonStandalone {

	private static final Logger LOG = Logger.getLogger(JnjlasinglesignonStandalone.class);

	public static void main(final String[] args)
	{
		new JnjlasinglesignonStandalone().run();
	}

	public void run() {
		Registry.activateStandaloneMode();
		Registry.activateMasterTenant();

		final JaloSession jaloSession = JaloSession.getCurrentSession();
		LOG.debug("Session ID: " + jaloSession.getSessionID());
		LOG.debug("User: " + jaloSession.getUser());
		Utilities.printAppInfo();

		RedeployUtilities.shutdown();
	}
}
