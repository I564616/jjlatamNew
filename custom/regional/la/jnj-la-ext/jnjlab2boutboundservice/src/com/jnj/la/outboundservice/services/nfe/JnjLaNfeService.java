/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.outboundservice.services.nfe;



import com.jnj.exceptions.IntegrationException;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalResponse;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapper;



/**
 * The Serivce Interface to get NFE data from external systems.
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjLaNfeService
{

	/**
	 * Gets the nfe wrapper.
	 *
	 * @param nFERequestData
	 *           the n fe request data
	 * @return the nfe wrapper
	 * @throws IntegrationException
	 */
	ElectronicNotaFiscalResponse getNfeWrapper(
			ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper) throws IntegrationException;
}
