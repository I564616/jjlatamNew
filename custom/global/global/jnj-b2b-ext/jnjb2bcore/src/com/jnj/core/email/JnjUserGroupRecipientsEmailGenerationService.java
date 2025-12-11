/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.model.JnJB2bCustomerModel;
//import com.jnj.pcm.constants.JnjPCMCoreConstants.Logging;
//import com.jnj.pcm.constants.JnjPCMCoreConstants.ProductWorkflows;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;


/**
 * Extended class responsible for creating attachments (if applicable) and create email using the attachments.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjUserGroupRecipientsEmailGenerationService extends DefaultEmailGenerationService
{
    @Autowired
    private UserService userService;
	public UserService getUserService() {
		return userService;
	}

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjUserGroupRecipientsEmailGenerationService.class);

	/**
	 * 
	 * This method can send email to a single user where the recipient will be present in the "TO" field. It can also send email to multiple users,
	 * who have to be members of a hybris usergroup, these users will be placed in the "bcc" field.
	 */
	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
	    String methodName = "createEmailMessage()";
	    ////CommonUtil.logMethodStartOrEnd("Create new product Email for multiple recipients", methodName, Logging.BEGIN_OF_METHOD, LOGGER);
        final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
        final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
                emailContext.getFromDisplayName());
        UserGroupModel userGroup = null;
        try
        {
            userGroup = userService.getUserGroupForUID(emailContext.getToEmail());
        } catch (UnknownIdentifierException exception)
        {
            LOGGER.error(exception.getMessage(), exception);
            
        }
        if(userGroup != null)
        {
        Set<PrincipalModel> principalModel = userGroup.getMembers();
        for (PrincipalModel userModel : principalModel)
        {
            if(userModel instanceof JnJB2bCustomerModel)
            {
                JnJB2bCustomerModel user = (JnJB2bCustomerModel)userModel;
                final EmailAddressModel bccAddress = getEmailService().getOrCreateEmailAddressForEmail(user.getContactEmail(),
                        user.getDisplayName());
                toEmails.add(bccAddress);
            }
        }
        final EmailAddressModel toAddress = new EmailAddressModel();
       // //CommonUtil.logMethodStartOrEnd("Create new product Email for multiple recipients", methodName, Logging.END_OF_METHOD, LOGGER);
        /*final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(JnJCommonUtil.getValue(ProductWorkflows.PCM_NEW_PRODUCT_DUMMY_ID),
                JnJCommonUtil.getValue(ProductWorkflows.PCM_NEW_PRODUCT_USER_GROUP_NAME));*/
        return getEmailService().createEmailMessage(Arrays.asList(toAddress),
                new ArrayList<EmailAddressModel>(),toEmails, fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
        }
        else
        {
        final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
                emailContext.getToDisplayName());
        toEmails.add(toAddress);
        ////CommonUtil.logMethodStartOrEnd("Create new product Email for multiple recipients", methodName, Logging.END_OF_METHOD, LOGGER);
        return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
                new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
        }
    }

}
