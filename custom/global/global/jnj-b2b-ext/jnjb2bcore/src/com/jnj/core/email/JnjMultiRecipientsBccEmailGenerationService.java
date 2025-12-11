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
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.utils.CommonUtil;


/**
 * Extended class responsible for creating attachments (if applicable) and create email using the attachments.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjMultiRecipientsBccEmailGenerationService extends DefaultEmailGenerationService
{
	@Autowired
	protected UserService userService;
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjMultiRecipientsBccEmailGenerationService.class);

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
		CommonUtil.logMethodStartOrEnd("Create Email for multiple recipients", methodName, Logging.BEGIN_OF_METHOD, LOGGER);
		final List<EmailAddressModel> bccEmails = new ArrayList<EmailAddressModel>();
		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());
		//Adding a dummy email since system does not accept to email list as null
		toEmails.add(getEmailService().getOrCreateEmailAddressForEmail(
				Config.getParameter(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_TO_EMAIL),
				Config.getParameter(Jnjb2bCoreConstants.Email.COMPREHENSIVE_ORDER_CONFIRMATION_TO_DISPLAY_NAME)));
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
					bccEmails.add(toAddress);
				}
				else
				{
					final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(email, email);
					bccEmails.add(toAddress);
				}
				emailIndex++;
			}
			else
			{
				final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(email,
						StringUtils.isEmpty(emailContext.getToDisplayName()) ? email : emailContext.getToDisplayName());
				bccEmails.add(toAddress);
			}
		}


		CommonUtil.logMethodStartOrEnd("Create Email for multiple recipients", methodName, Logging.END_OF_METHOD, LOGGER);
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(), bccEmails, fromAddress,
				emailContext.getFromEmail(), emailSubject, emailBody, null);

	}
}
