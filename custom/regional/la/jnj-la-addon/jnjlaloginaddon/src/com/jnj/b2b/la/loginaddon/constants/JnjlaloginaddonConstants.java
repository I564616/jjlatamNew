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
package com.jnj.b2b.la.loginaddon.constants;

/**
 * Global class for all Jnjlaloginaddon constants. You can add global constants for your extension into this class.
 */
public final class JnjlaloginaddonConstants extends GeneratedJnjlaloginaddonConstants
{
	public static final String EXTENSIONNAME = "jnjlaloginaddon";

	private JnjlaloginaddonConstants()
	{
		//empty to avoid instantiating this constant class
	}

	public interface Login {

		public final static String USER_GROUP = "UserGroup";
		public final static String RANDOM_GROUP = "57e3cbbf-f603-48a3-a226-be86d2a4efac";
		public final static String REGION_ERROR = "login.error.invalid.country";
		public final static String USER_ERROR = "login.error.invalid.user";
		public final static String SUCCESS = "SUCCESS";
		public final static String CENCA_USER_GROUP = "cencaUserGroup";
        String ACCOUNT_ERROR = "login.error.user.account";
		String LOGOUT_REASON = "logoutReasonCookie";
		int LOGOUT_REASON_COOKIE_MAXAGE = 300000;
    }

	public interface Logging {
		public final static String PRIVACY_POLICY_CHECK = "Privacy Policy Check";
		public final static String BEGIN_OF_METHOD = "Begin of Method";
		public final static String END_OF_METHOD = "End of Method";
	}
}
