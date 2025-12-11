/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.util;

import java.util.HashMap;
import java.util.Map;


/**
 * This class holds the secret question attempts information for users
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjForgotPasswordErrorCounter
{
	Map<String, String> secretQuestionAttackCounter = new HashMap<String, String>();

	/**
	 * @return the secretQuestionAttackCounter
	 */
	public Map<String, String> getSecretQuestionAttackCounter()
	{
		return secretQuestionAttackCounter;
	}

	/**
	 * @param secretQuestionAttackCounter
	 *           the secretQuestionAttackCounter to set
	 */
	public void setSecretQuestionAttackCounter(final Map<String, String> secretQuestionAttackCounter)
	{
		this.secretQuestionAttackCounter = secretQuestionAttackCounter;
	}
}
