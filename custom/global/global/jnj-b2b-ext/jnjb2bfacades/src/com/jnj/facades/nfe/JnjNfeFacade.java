/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Conermpanies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.nfe;

import java.io.File;

import jakarta.xml.bind.JAXBException;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;



/**
 * Facade for getting NFe XMl File
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjNfeFacade
{

	public File getNfeFile(String invDocNo) throws BusinessException, IntegrationException, IllegalArgumentException,
			JAXBException;


}
