/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.commons;

/**
 * The Enum Severity.
 * 
 * @author Accenture
 * 
 * @version 1.0
 * 
 */
public enum Severity
{
	/** The INFO. */
	INFO(1),
	/** The WARN. */
	WARN(2),
	/** The BUSINES s_ exception. */
	BUSINESS_EXCEPTION(3),
	/** The SYSTE m_ exception. */
	SYSTEM_EXCEPTION(4),
	/** The FATAL. */
	FATAL(5),
	/** The LABEL. */
	LABEL(6),
	/** The NONE. */
	NONE(0);
	/** The seviarity code. */
	private final int seviarityCode;

	/**
	 * Instantiates a new severity.
	 * 
	 * @param seviarityCode
	 *           the seviarity code
	 */
	private Severity(final int seviarityCode)
	{
		this.seviarityCode = seviarityCode;
	}

	/**
	 * Gets the message seviarity.
	 * 
	 * @return the messageSeviarity
	 */
	public int getMessageSeviarity()
	{
		return seviarityCode;
	}

	/**
	 * Gets the for code.
	 * 
	 * @param code
	 *           the code
	 * @return the for code
	 */
	public static Severity getForCode(final int code)
	{
		for (final Severity seviarity : Severity.values())
		{
			if (seviarity.seviarityCode == code)
			{
				return seviarity;
			}
		}
		return null;
	}
}
