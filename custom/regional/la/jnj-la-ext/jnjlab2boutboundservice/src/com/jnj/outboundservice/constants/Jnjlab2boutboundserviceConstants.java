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
package com.jnj.outboundservice.constants;

/**
 * Global class for all Jnjlab2boutboundservice constants. You can add global constants for your extension into this
 * class.
 */
public final class Jnjlab2boutboundserviceConstants extends GeneratedJnjlab2boutboundserviceConstants
{
	public static final String EXTENSIONNAME = "jnjlab2boutboundservice";

	private Jnjlab2boutboundserviceConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension

	public static final String SOAP_ACTION_INVOICE_DOC = "SG910_BtB_IN0504_ElectronicBilling_Hybris_Source_v1_webservices_receiveElectronicBillingWS_Binder_receiveElectronicBillingFromHybrisWrapper";

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

}
