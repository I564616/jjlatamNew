/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services;

import com.jnj.core.dto.JnjContactUsDTO;


/**
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjContactUsService
{
	/**
	 * Method responsible for fetching the email address of the back-office user
	 * 
	 * @param subject
	 * 
	 * @return emailAddress
	 */
	public String fetchEmailAddress(String subject);

	/**
	 * This method is responsible for sending the email for the contact us functionality.
	 * 
	 * @param jnjContactUsDTO
	 * 
	 * @return true/false
	 */
	public boolean sendMessage(final JnjContactUsDTO jnjContactUsDTO);
}
