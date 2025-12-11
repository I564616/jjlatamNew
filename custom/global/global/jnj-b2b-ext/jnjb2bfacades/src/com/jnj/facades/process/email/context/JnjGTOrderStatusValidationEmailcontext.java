/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.util.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.process.email.context.CustomerEmailContext;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.OrderStatusInbound;
import com.jnj.core.model.JnjGTOrdInboundValidationEmailProcessModel;


/**
 * This class represents the email context for the dispute inquiry Email process.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderStatusValidationEmailcontext extends CustomerEmailContext
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTOrderStatusValidationEmailcontext.class);

	/**
	 * List of all file names validates against the lines count.
	 */
	private List<String> fileNames;

	/**
	 * Map stores file name as key and corresponding available record count as value.
	 */
	private Map<String, String> fileNameAndAvailableCount;

	/**
	 * Map stores file name as key and corresponding mentioned record count as value.
	 */
	private Map<String, String> fileNameAndMentionedCount;

	/**
	 * Constant for method name.
	 */
	private final String METHOD_INIT = "Context - init()";

	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		super.init(businessProcessModel, emailPageModel);

		if (businessProcessModel instanceof JnjGTOrdInboundValidationEmailProcessModel)
		{
			final JnjGTOrdInboundValidationEmailProcessModel ordInboundValidationEmailProcessModel = (JnjGTOrdInboundValidationEmailProcessModel) businessProcessModel;

			final Map<String, String> fileNameAndAvailableCount = new HashMap<String, String>();
			final Map<String, String> fileNameAndMentionedCount = new HashMap<>();
			final Map<String, String> fileNamesAndCounts = ordInboundValidationEmailProcessModel.getFileNameAndCount();
			for (final String fileName : fileNamesAndCounts.keySet())
			{
				if (fileNamesAndCounts.get(fileName) != null)
				{
					final String[] recordCounts = fileNamesAndCounts.get(fileName).split(OrderStatusInbound.RECORD_COUNT_DELIMETER);
					try
					{
						fileNameAndAvailableCount.put(fileName, recordCounts[0]);
						fileNameAndMentionedCount.put(fileName, recordCounts[1]);
					}
					catch (final ArrayIndexOutOfBoundsException exception)
					{
						LOGGER.error("Exception while parsing counts for conetxt: ", exception);
					}
				}
			}
			setFileNames(ordInboundValidationEmailProcessModel.getFileNames());
			setFileNameAndAvailableCount(fileNameAndAvailableCount);
			setFileNameAndMentionedCount(fileNameAndMentionedCount);

			/** Set the To Email address i.e. company CSR email **/
			put(EMAIL, Config.getParameter(OrderStatusInbound.VALIDATION_CSR_EMAIL_KEY));

			/** Set the From Email address i.e. customer email **/
			put(FROM_EMAIL, getCustomerEmailResolutionService().getEmailForCustomer(getCustomer(businessProcessModel)));

			//TODO [AR]: To be confirmed. 
			/** Set FROM_DISPLAY_NAME value based on the customer name, if not present then customer email id. **/
			put(FROM_DISPLAY_NAME, (getCustomer().getName() != null) ? getCustomer().getName() : getCustomerEmailResolutionService()
					.getEmailForCustomer(getCustomer(businessProcessModel)));
		}
	}

	/**
	 * @return the fileNames
	 */
	public List<String> getFileNames()
	{
		return fileNames;
	}

	/**
	 * @param fileNames
	 *           the fileNames to set
	 */
	public void setFileNames(final List<String> fileNames)
	{
		this.fileNames = fileNames;
	}

	/**
	 * @return the fileNameAndAvailableCount
	 */
	public Map<String, String> getFileNameAndAvailableCount()
	{
		return fileNameAndAvailableCount;
	}

	/**
	 * @param fileNameAndAvailableCount
	 *           the fileNameAndAvailableCount to set
	 */
	public void setFileNameAndAvailableCount(final Map<String, String> fileNameAndAvailableCount)
	{
		this.fileNameAndAvailableCount = fileNameAndAvailableCount;
	}

	/**
	 * @return the fileNameAndMentionedCount
	 */
	public Map<String, String> getFileNameAndMentionedCount()
	{
		return fileNameAndMentionedCount;
	}

	/**
	 * @param fileNameAndMentionedCount
	 *           the fileNameAndMentionedCount to set
	 */
	public void setFileNameAndMentionedCount(final Map<String, String> fileNameAndMentionedCount)
	{
		this.fileNameAndMentionedCount = fileNameAndMentionedCount;
	}

}
