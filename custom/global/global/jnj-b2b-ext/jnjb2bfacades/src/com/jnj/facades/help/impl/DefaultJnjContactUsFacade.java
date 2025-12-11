/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.help.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjContactUsDTO;
import com.jnj.core.services.impl.DefaultJnjContactUsService;
import com.jnj.facades.help.JnjContactUsFacade;


/**
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjContactUsFacade implements JnjContactUsFacade
{
	@Autowired
	private DefaultJnjContactUsService defaultjnjContactUsService;
	private static final Logger LOG = Logger.getLogger(DefaultJnjContactUsFacade.class);

	/**
	 * This method is responsible for sending the email for the contact us functionality.
	 * 
	 * @param jnjContactUsDTO
	 * 
	 * @return true/false
	 */
	@Override
	public boolean sendMessage(final JnjContactUsDTO jnjContactUsDTO)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Calling Contact Us Service Layer");
		}
		return defaultjnjContactUsService.sendMessage(jnjContactUsDTO);
	}
}
