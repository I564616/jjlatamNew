/*
 * Copyright: Copyright ©  2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.facades.services.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.process.email.context.AbstractJnjLaEmailContext;
import com.jnj.facades.process.email.context.JnjFormEmailContext;
import com.jnj.facades.process.email.context.JnjLaActiveUsersReportEmailContext;
import com.jnj.facades.process.email.context.JnjLaCronJobMonitoringEmailContext;
import com.jnj.facades.process.email.context.JnjLaSAPFailedOrdersReportEmailContext;
import de.hybris.platform.acceleratorservices.email.EmailGenerationService;
import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.File;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.activation.MimetypesFileTypeMap;

public class JnjLatamEmailGenerationService extends DefaultEmailGenerationService implements EmailGenerationService{

    public static final String EMAIL_ADDRESS_SEPARATOR = ";";
    public static final String TRUE = "true";
    protected ConfigurationService configurationService;
    private static final Logger LOGGER = Logger.getLogger(JnjLatamEmailGenerationService.class);
    private static final String CREATE_ATTACHMENT="createAttachment()";
    private ModelService modelService;
    private static final String ALWAYS_NEW_EMAIL_ADDRESS_CREATION = "jnj.latam.email.always.newaddressmodel";
    
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
	
	public ModelService getModelService() {
		return modelService;
	}
	

    @Override
    protected EmailMessageModel createEmailMessage(final String subject, final String body, final AbstractEmailContext<BusinessProcessModel> context) {
        final EmailAddressModel from = createFrom(context);
        final List<EmailAddressModel> to = createTo(context);
        final List<EmailAddressModel> cc = createCc(context);
        final List<EmailAddressModel> bcc = createBcc(context);
        final List<EmailAttachmentModel> attachments = getAttachments(context);

        return getEmailService().createEmailMessage(to, cc, bcc, from, context.getFromEmail(), subject, body, attachments);
    }

    private EmailAddressModel createFrom(AbstractEmailContext<BusinessProcessModel> context) {
        return createEmailAddress(context.getFromEmail(), context.getFromDisplayName());
    }

    private List<EmailAddressModel> createTo(AbstractEmailContext<BusinessProcessModel> context) {
        return convertStringToEmailList(context.getToEmail());
    }

    private List<EmailAddressModel> convertStringToEmailList(String rawEmails) {
        final List<EmailAddressModel> emails = new ArrayList<>();
        if (StringUtils.isNotBlank(rawEmails)) {
            String[] split = rawEmails.split(EMAIL_ADDRESS_SEPARATOR);
            Arrays.stream(split).forEach(address -> emails.add(createEmailAddress(address, address)));
        }
        return emails;
    }

    private List<EmailAddressModel> createCc(AbstractEmailContext context) {
        final List<EmailAddressModel> emails = new ArrayList<>();
        if (context instanceof JnjFormEmailContext) {
            final EmailAddressModel ccAddress = createCcEmailAddress((JnjFormEmailContext) context);
            emails.add(ccAddress);
        }
        return emails;
    }

    private List<EmailAddressModel> createBcc(AbstractEmailContext<BusinessProcessModel> context) {
        if (isLaContext(context)) {
            return convertStringToEmailList(((AbstractJnjLaEmailContext) context).getBcc());
        }
        return Collections.emptyList();
    }

    private boolean isLaContext(AbstractEmailContext<BusinessProcessModel> context) {
        return context instanceof AbstractJnjLaEmailContext;
    }

    private List<EmailAttachmentModel> getAttachments(AbstractEmailContext context) {
        if (isLaContext(context)) {
            return ((AbstractJnjLaEmailContext) context).getAttachments();
        }
        if(context instanceof JnjLaCronJobMonitoringEmailContext || context instanceof JnjLaActiveUsersReportEmailContext || context instanceof JnjLaSAPFailedOrdersReportEmailContext) {
            return createAttachment(
                    String.valueOf(context.get(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH)),
                    String.valueOf(context.get(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME)),
                    String.valueOf(context.get(Jnjb2bCoreConstants.USE_DIRECT_PATH)),
                    String.valueOf(context.get(Jnjb2bCoreConstants.DELETE_FILE)), context);
        }
        return Collections.emptyList();
    }

    private EmailAddressModel createCcEmailAddress(JnjFormEmailContext context) {
        return createEmailAddress(context.getJnjCustomerFormMap().get("contactEmail").toString(), context.getToDisplayName());
    }

    private EmailAddressModel createEmailAddress(final String emailAddressParam, final String displayNameParam) {
        final String emailAddress = emailAddressParam != null ? emailAddressParam.trim() : null;
        final String displayName = displayNameParam != null ? displayNameParam.trim() : null;
		
        final boolean isAlwaysNewEmailAddress = configurationService.getConfiguration().getBoolean(ALWAYS_NEW_EMAIL_ADDRESS_CREATION);
        LOGGER.info("Always new email address creation value: "+ isAlwaysNewEmailAddress);
        if(isAlwaysNewEmailAddress) 
        {
        	
        	return createNewEmailAddressAlways(emailAddressParam,displayNameParam);
        }
        else
        {
            return getEmailService().getOrCreateEmailAddressForEmail(emailAddress, displayName);
        }

    }

    private EmailAddressModel createNewEmailAddressAlways(final String emailAddressParam, final String displayNameParam) {
        final EmailAddressModel emailaddress = modelService.create(EmailAddressModel.class);
        emailaddress.setEmailAddress(emailAddressParam);
        emailaddress.setDisplayName(displayNameParam);
        modelService.save(emailaddress);
        return emailaddress;
    }


    protected List<EmailAttachmentModel> createAttachment(final String pathName,  final String createdfileName, final String useDirectPath,  final String deleteFile,final AbstractEmailContext<BusinessProcessModel> emailContext) {
        LOGGER.info(Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + CREATE_ATTACHMENT + Jnjb2bCoreConstants.Logging.HYPHEN);
        LOGGER.info("createAttachment(): pathName:" + pathName);
        LOGGER.info("createAttachment(): createdfileName:" + createdfileName);
        LOGGER.info("createAttachment(): useDirectPath:" + useDirectPath);

        List<EmailAttachmentModel> attachments = new ArrayList<>();

        File attachedFile = null;
        String filePath = null;
        try {
            if (StringUtils.isNotBlank(useDirectPath) && "TRUE".equalsIgnoreCase(useDirectPath)) {
                filePath = pathName;
            } else if (StringUtils.isNotBlank(pathName)) {
                filePath = getConfigurationService().getConfiguration().getString(pathName);
            } else {
                LOGGER.error("filepath not found");
            }

            LOGGER.info("createAttachment(): filePath:" + filePath);
            final File[] files = new File(filePath).listFiles();

            if (files != null) {
                // Iterating over the File List
                for (final File file : files) {
                    if (file.isFile()) {
                        // Getting file name
                        final String fileName = file.getName().toLowerCase();

                        // Fetching only those files that contain the fileNamePrefix
                        if (createdfileName.equalsIgnoreCase(fileName))
                        {
                            attachedFile = file;
                        }

                    }
                }
            }

            if (attachedFile != null) {
                DataInputStream dataInputStream = null;
                try {
                    dataInputStream = new DataInputStream(new FileInputStream(attachedFile));
                } catch (final FileNotFoundException e) {
                    LOGGER.error("Error in createAttachment()", e);
                    return Collections.emptyList();
                }

                final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
                final String mimeType = mimeTypesMap.getContentType(attachedFile.getName());

                final String realName = attachedFile.getName();


                final EmailAttachmentModel attachment = getEmailService().createEmailAttachment(dataInputStream, realName, mimeType);
                if(dataInputStream != null)
                {
                	dataInputStream.close();
                }
                
                if (StringUtils.isNotBlank(deleteFile) && TRUE.equalsIgnoreCase(deleteFile) && attachedFile.delete()) {
                    LOGGER.info("Attachment created and original file has been deleted successfuly.");
                }

                attachments.add(attachment);


                LOGGER.info(Jnjb2bCoreConstants.Logging.END_OF_METHOD + CREATE_ATTACHMENT + Jnjb2bCoreConstants.Logging.HYPHEN);
                return attachments;
            }

        } catch (Exception FileNotFoundException) {
            LOGGER.error(CREATE_ATTACHMENT + Jnjb2bCoreConstants.Logging.HYPHEN + "Couldn't find the monitoring report No Attachment created." + FileNotFoundException);
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

}
