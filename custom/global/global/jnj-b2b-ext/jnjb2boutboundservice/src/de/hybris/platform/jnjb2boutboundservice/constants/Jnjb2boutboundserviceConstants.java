/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package de.hybris.platform.jnjb2boutboundservice.constants;

/**
 * Global class for all Jnjb2boutboundservice constants. You can add global constants for your extension into this
 * class.
 */
public final class Jnjb2boutboundserviceConstants extends GeneratedJnjb2boutboundserviceConstants
{
	public static final String EXTENSIONNAME = "jnjb2boutboundservice";

	private Jnjb2boutboundserviceConstants()
	{
		//empty to avoid instantiating this constant class

	}

	// Added for logging purpose
	public interface Logging
	{
		public static final String BEGIN_OF_METHOD = "Begin of method";
		public static final String END_OF_METHOD = "End of method";
		public static final String HYPHEN = "-";
		public static final String START_TIME = "startTime:";
		public static final String END_TIME = "endTime:";
		public static final String SUBMIT_ORDER = "Submit Order";
		public static final String GET_INVOICE_DOCUMENT = "Get Invoice Document";
	}

	public interface WebServiceConnection
	{
		public static final String WEBSERVICE_INVOICE_DOC_URL = "jnj.webservice.invoice.doc.endpoint.url";
		public static final String WEBSERVICE_INVOICE_DOC_USER = "jnj.webservice.invoice.doc.user";
		public static final String WEBSERVICE_INVOICE_DOC_PWD = "jnj.webservice.invoice.doc.password";
		public static final String WEBSERVICE_QUERY_NFE_URL = "jnj.webservice.query.nfe.endpoint.url";
		public static final String WEBSERVICE_QUERY_NFE_USER = "jnj.webservice.query.nfe.user";
		public static final String WEBSERVICE_QUERY_NFE_PWD = "jnj.webservice.query.nfe.password";
	}
	// implement here constants used by this extension
}
