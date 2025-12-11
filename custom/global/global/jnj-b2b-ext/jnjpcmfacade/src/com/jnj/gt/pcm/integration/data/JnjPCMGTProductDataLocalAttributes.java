/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.data;

public class JnjPCMGTProductDataLocalAttributes
{
	protected String locale;
	protected String attrValue;

	public JnjPCMGTProductDataLocalAttributes(final String locale, final String attrValue)
	{
		super();
		this.locale = locale;
		this.attrValue = attrValue;
	}

	public JnjPCMGTProductDataLocalAttributes()
	{

	}

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(final String locale)
	{
		this.locale = locale;
	}

	public String getAttrValue()
	{
		return attrValue;
	}

	public void setAttrValue(final String attrValue)
	{
		this.attrValue = attrValue;
	}

}

