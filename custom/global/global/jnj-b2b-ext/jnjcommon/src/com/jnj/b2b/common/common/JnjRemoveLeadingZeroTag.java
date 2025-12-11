/* * This code contains copyright information which is the proprietary property
 * of Accenture Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of Accenture Companies Ltd.
 * Copyright (C) Accenture Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.common.common;




import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * The Class JNJMessageTag.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjRemoveLeadingZeroTag extends TagSupport
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjRemoveLeadingZeroTag.class);

	/** Message Code for which message text to be rendered. */
	protected String value;

	/**
	 * Do start tag.
	 *
	 * @return the int
	 * @throws JspException
	 *            the jsp exception
	 * @see jakarta.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException
	{
		if (StringUtils.isNotEmpty(value))
		{
			final JspWriter out = pageContext.getOut();
			try
			{
				final String trimmedValue = value.replaceAll("^0+", StringUtils.EMPTY);
				out.print(trimmedValue);
			}
			catch (final Exception exception)
			{
				LOGGER.error("Unable to remove zeroes form the String :" + value, exception);
			}
		}
		return SKIP_BODY;
	}


	public String getValue()
	{
		return value;
	}

	public void setValue(final String value)
	{
		this.value = value;
	}


	/**
	 * Do end tag.
	 *
	 * @return the int
	 * @throws JspException
	 *            the jsp exception
	 * @see jakarta.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException
	{
		return EVAL_PAGE;
	}
}
