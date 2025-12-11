/* * This code contains copyright information which is the proprietary property
 * of Accenture Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of Accenture Companies Ltd.
 * Copyright (C) Accenture Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.common.common;




import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.ValueExpression;
import jakarta.servlet.jsp.JspApplicationContext;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspFactory;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;


/**
 * The Class JNJMessageTag.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjMessageTag extends TagSupport
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjMessageTag.class);

	/** Message Code for which message text to be rendered. */
	protected String messageCode;

	/** argument list to be replaced in the message text. */
	protected Object arguments;

	/** Value against the Message Label. **/
	protected String labelValue;

	/** Value against the Message Label. **/
	protected String locale;

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
		/* Only display the message label if the 'labelValue' is null and if the value is not not em pty */
		if (labelValue == null || !labelValue.isEmpty())
		{
			if (messageCode == null)
			{
				messageCode = "NONE";
			}
			final WebApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext
					.getServletContext());
			/** MessageFacade to retrieve the message text from message code. */
			final MessageFacadeUtill messageFacade = (MessageFacadeUtill) appContext.getBean("messageFacade");
			final JspWriter out = pageContext.getOut();
			try
			{
				String message = messageFacade.getMessageTextForCode(messageCode, locale);

				final Object[] argumentsArray = resolveArguments(this.arguments);
				if (!(ObjectUtils.isEmpty(argumentsArray)))
				{
					final MessageFormat messageFormat = new MessageFormat(message);
					message = messageFormat.format(argumentsArray);
				}
				out.print(message);
			}
			catch (final IOException exception)
			{
				LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
			}
			catch (final BusinessException exception)
			{
				try
				{
					out.print(messageCode);
				}
				catch (final IOException e)
				{
					LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
				}
				LOGGER.error("Unable to render message text for message code : " + messageCode, exception);
			}
		}
		return SKIP_BODY;
	}

	/**
	 * Resolve arguments.
	 * 
	 * @param argumentsVal
	 *           the arguments val
	 * @return the object[]
	 * @throws JspException
	 *            the jsp exception
	 */
	protected Object[] resolveArguments(final Object argumentsVal) throws JspException
	{
		Object[] objArray = null;
		ELContext elContext =  pageContext.getELContext();
		JspFactory jf = JspFactory.getDefaultFactory();
		JspApplicationContext jac = jf.getJspApplicationContext(pageContext.getServletContext());
		ExpressionFactory ef = jac.getExpressionFactory();
		if (argumentsVal instanceof String)
		{
			final String[] stringArray = StringUtils.delimitedListToStringArray((String) argumentsVal, ",");
			if (stringArray.length == 1)
			{
				//final Object argument = ExpressionEvaluationUtils.evaluate("argument", stringArray[0], this.pageContext);
				ValueExpression val = ef.createValueExpression(elContext, stringArray[0], Object.class);
				final Object argument= val.getValue(elContext);
				if ((argument != null) && (argument.getClass().isArray()))
				{
					return ObjectUtils.toObjectArray(argument);
				}
				objArray = new Object[]
				{ argumentsVal };
			}

			final Object[] argumentsArray = new Object[stringArray.length];
			int counter = 0;
			while (true)
			{
				/*argumentsArray[counter] = ExpressionEvaluationUtils.evaluate("argument[" + counter + "]", stringArray[counter],
						this.pageContext);*/
				ValueExpression val = ef.createValueExpression(elContext, stringArray[counter], Object.class);
				argumentsArray[counter]= val.getValue(elContext);
				++counter;
				if (counter >= stringArray.length)
				{
					objArray = argumentsArray;
					break;
				}
			}
		}
		objArray = checkArgNature(argumentsVal, objArray);

		return objArray;
	}

	/**
	 * Check arg nature.
	 * 
	 * @param argumentsVal
	 *           the arguments val
	 * @param objArray
	 *           the obj array
	 * @return the object[]
	 */
	protected Object[] checkArgNature(final Object argumentsVal, final Object[] objArray)
	{
		Object[] newArray = objArray;
		if (argumentsVal != null)
		{
			newArray = new Object[]
			{ argumentsVal };
		}
		if (argumentsVal instanceof Object[])
		{
			newArray = (Object[]) argumentsVal;
		}
		if (argumentsVal instanceof Collection)
		{
			newArray = ((Collection) argumentsVal).toArray();
		}
		return newArray;
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

	/**
	 * setter method.
	 * 
	 * @param messageCode
	 *           the messageCode to set
	 */
	public void setMessageCode(final String messageCode)
	{
		this.messageCode = messageCode;
	}

	/**
	 * Sets the arguments.
	 * 
	 * @param arguments
	 *           the new arguments
	 */
	public void setArguments(final Object arguments)
	{
		this.arguments = arguments;
	}

	/**
	 * @return the labelValue
	 */
	public String getLabelValue()
	{
		return labelValue;
	}

	/**
	 * @param labelValue
	 *           the labelValue to set
	 */
	public void setLabelValue(final String labelValue)
	{
		this.labelValue = labelValue;
	}

	/**
	 * @return the locale
	 */
	public String getLocale()
	{
		return locale;
	}

	/**
	 * @param locale
	 *           the locale to set
	 */
	public void setLocale(final String locale)
	{
		this.locale = locale;
	}
}
