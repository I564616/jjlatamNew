/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.la.jnjlaprofileaddon.constants;

/**
 * Global class for all Jnjlaprofileaddon constants. You can add global constants for your extension into this class.
 */
public final class JnjlaprofileaddonConstants extends GeneratedJnjlaprofileaddonConstants
{
	public static final String EXTENSIONNAME = "jnjlaprofileaddon";

	public static String ADDON_PREFIX = "addon:/jnjlaprofileaddon/";

	private JnjlaprofileaddonConstants()
	{ //empty to avoid instantiating this constant class
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Pages
		{
			interface Account
			{
				String AccountProfilePage = "pages/account/accountProfilePage";
			}
		}
	}
}
