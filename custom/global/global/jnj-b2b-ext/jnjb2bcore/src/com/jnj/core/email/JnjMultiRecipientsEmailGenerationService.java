/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.email;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.pcm.constants.JnjPCMCoreConstants.Logging;
import com.jnj.utils.CommonUtil;


/**
 * Extended class responsible for creating attachments (if applicable) and create email using the attachments.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjMultiRecipientsEmailGenerationService extends DefaultEmailGenerationService
{
	@Autowired
	private UserService userService;
	public UserService getUserService() {
		return userService;
	}

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjMultiRecipientsEmailGenerationService.class);

	/**
	 * 
	 * This method can send email to multiple recipients, we just have to enter comma separated email addresses in the
	 * "toEmail" field in context and, correspondingly, comma separated display names in "toDisplayNAme" field. "bcc"
	 * field.
	 */
	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		final String methodName = "createEmailMessage()";
		////CommonUtil.logMethodStartOrEnd("Create Email for multiple recipients", methodName, Logging.BEGIN_OF_METHOD, LOGGER);
		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());

		final String[] emailList = emailContext.getToEmail().split(Jnjb2bCoreConstants.SYMBOl_COMMA);
		final String[] displayNameList = emailContext.getToDisplayName().split(Jnjb2bCoreConstants.SYMBOl_COMMA);
		int emailIndex = 0;
		for (final String email : emailList)
		{
			if (displayNameList.length != 0)
			{
				if (displayNameList.length >= emailIndex + 1)
				{
					final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(email,
							StringUtils.isEmpty(displayNameList[emailIndex]) ? email : displayNameList[emailIndex]);
					toEmails.add(toAddress);
				}
				else
				{
					final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(email, email);
					toEmails.add(toAddress);
				}
				emailIndex++;
			}
			else
			{
				final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(email,
						StringUtils.isEmpty(emailContext.getToDisplayName()) ? email : emailContext.getToDisplayName());
				toEmails.add(toAddress);
			}
		}


		////CommonUtil.logMethodStartOrEnd("Create Email for multiple recipients", methodName, Logging.END_OF_METHOD, LOGGER);
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
				new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);

	}
}
