/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.outboundservice.services.invoice;

import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0504_electronicbilling_hybris_source_v1_webservices.receiveelectronicbillingws.ElectronicBillingResponse;


/**
 * The JnjInvoiceDoc Interface is used to interact with Get Inovice Document Service to get the invoice url.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjInvoiceDoc
{
	public ElectronicBillingResponse receiveElectronicBillingFromHybrisWrapper(
			final ElectronicBillingRequest electronicBillingRequest) throws IntegrationException;
}
