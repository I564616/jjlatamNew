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
package com.jnj.b2b.jnjglobalprofile.constants;

/**
 * Global class for all Jnjglobalprofile constants. You can add global constants for your extension into this class.
 */
public final class JnjglobalprofileConstants extends GeneratedJnjglobalprofileConstants
{


	public static final String EXTENSIONNAME = "jnjglobalprofile";
	public static final String SYMBOl_PIPE = "|";
	public static final String US = "US";

	private JnjglobalprofileConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public interface Profile
	{
		public static final String PERSONAL_INFORMATION_LINK_COMPONENT_ID = "jnjGTPersonalinformation";
		public static final String EMAIL_PREFRENCES_LINK_COMPONENT_ID = "jnjGTEmailpreferences";
		public static final String Change_Password_LINK_COMPONENT_ID = "jnjGTChangepassword";
		public static final String ADD_ACCOUNT_LINK_COMPONENT_ID = "jnjGTAddaccount";
		public static final String CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID = "jnjGTChangesecurityquestion";
		//Changes for AAOL-4659,4660
		public static final String DEFAULT_ACCOUNT_ORDER_LINK_COMPONENT_ID = "jnjGTChangesecurityquestion";
		
		/*public static final String PERSONAL_INFORMATION_LINK_COMPONENT_ID = "jnjNAPersonalinformation";
		public static final String EMAIL_PREFRENCES_LINK_COMPONENT_ID = "jnjNAEmailpreferences";
		public static final String Change_Password_LINK_COMPONENT_ID = "jnjNAChangepassword";
		public static final String ADD_ACCOUNT_LINK_COMPONENT_ID = "jnjNAAddaccount";
		public static final String CHANGE_SECURITY_QUESTION_LINK_COMPONENT_ID = "jnjNAChangesecurityquestion";
		*/
		public static final String Add_NEW_ACCOUNT_EMAIL_SUBJECT = "label.addNewAccountEmail.subject";
		public static final String YES = "YES";
		public static final String NO = "NO";
	}

	public interface SalesAlignment
	{
		public static final String CONSUMER = "CONSUMER";
		public static final String MDD = "MDD";
		public static final String ACCESS_BY_WWID = "WWID";
	}
}
