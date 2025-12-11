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
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.activation.MimetypesFileTypeMap;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

/**
 * Extended class responsible for creating attachments (if applicable) and create email using the attachments.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTEmailGenerationService extends DefaultEmailGenerationService
{
	@Autowired
	protected CatalogService catalogService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	@Autowired
	FlexibleSearchService flexibleSearchService;
	@Autowired
	ModelService modelService;
	
	public CatalogService getCatalogService() {
		return catalogService;
	}


	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}


	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	protected static final Logger LOGGER = Logger.getLogger(JnjGTEmailGenerationService.class);

	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		final List<EmailAttachmentModel> attachments = createAttachment(
				String.valueOf(emailContext.get(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_PATH)),
				String.valueOf(emailContext.get(Jnjb2bCoreConstants.EXPORT_EMAIL_ATTACHMENT_FILE_NAME)),
				String.valueOf(emailContext.get(Jnjb2bCoreConstants.USE_DIRECT_PATH)),
				String.valueOf(emailContext.get("orderNumber")), String.valueOf(emailContext.get(Jnjb2bCoreConstants.DELETE_FILE)));

		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
				emailContext.getToDisplayName());
		toEmails.add(toAddress);
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
				new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, attachments);
	}


	protected List<EmailAttachmentModel> createAttachment(final String pathName, final String createdfinleName,
			final String useDirectPath, final String realFileName, final String deleteFile)
	{
		LOGGER.info(Logging.BEGIN_OF_METHOD + "createAttachment()" + Logging.HYPHEN);
		LOGGER.info("createAttachment(): pathName:" + pathName);
		LOGGER.info("createAttachment(): createdfinleName:" + createdfinleName);
		LOGGER.info("createAttachment(): useDirectPath:" + useDirectPath);

		File taxExemptFile = null;
		String filePath = null;
		if (null != useDirectPath && "TRUE".equalsIgnoreCase(useDirectPath))
		{
			filePath = pathName;
		}
		else if (pathName != null && !"null".equalsIgnoreCase(pathName))
		{
			filePath = Config.getParameter(pathName);
		}
		else
		{
			filePath = Config.getParameter(Jnjb2bCoreConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FILE_PATH_KEY);
		}

		LOGGER.info("createAttachment(): filePath:" + filePath);
		final File[] files = new File(filePath).listFiles();

		if (files != null)
		{
			// Iterating over the File List
			for (final File file : files)
			{
				if (file.isFile())
				{
					// Getting file name
					final String fileName = file.getName().toLowerCase();

					// Fetching only those files that contain the fileNamePrefix
					if (createdfinleName.equalsIgnoreCase(fileName)
							|| fileName.startsWith(Jnjb2bCoreConstants.Order.DisputeInquiry.DISPUTE_INQUIRY_FILE_PREFIX))
					{
						taxExemptFile = file;
					}
				}
			}
		}

		if (taxExemptFile != null)
		{
			DataInputStream dataInputStream = null;
			try
			{
				dataInputStream = new DataInputStream(new FileInputStream(taxExemptFile));
			}
			catch (final FileNotFoundException e)
			{
				//LOG error
				return null;
			}

			final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			final String mimeType = mimeTypesMap.getContentType(taxExemptFile.getName());

			String realName;

			if (null != useDirectPath && "TRUE".equalsIgnoreCase(useDirectPath) && StringUtils.isNotEmpty(realFileName) && !"null".equalsIgnoreCase(realFileName) )
			{
				if(createdfinleName.endsWith(".pdf") && !realFileName.contains(".pdf")) {
					realName = realFileName + ".pdf";
				} else {
					realName = realFileName;
				}
				
			}
			else
			{
				realName = taxExemptFile.getName();
			}

			final EmailAttachmentModel attachment = getEmailAttachment(dataInputStream, realName, mimeType);
			if ("null".equalsIgnoreCase(deleteFile) || "TRUE".equalsIgnoreCase(deleteFile))
			{
				if (taxExemptFile.delete())
				{
					LOGGER.info("Attachment created and original file has been deleted successfuly.");
				}
			}
			final List<EmailAttachmentModel> attachments = new ArrayList<>();
			attachments.add(attachment);

			LOGGER.info(Logging.END_OF_METHOD + "createAttachment()" + Logging.HYPHEN);
			return attachments;
		}
		else
		{
			LOGGER.info("createAttachment()" + Logging.HYPHEN + "Couldn't find the tax exempt certificate No Attachment created.");
			return null;
		}
	}


	/**
	 * @param dataInputStream
	 * @param realName
	 * @param mimeType
	 */
	protected EmailAttachmentModel getEmailAttachment(final DataInputStream dataInputStream, final String realName,
			final String mimeType)
	{
		if (realName.startsWith("MDD_Product_Catalog"))
		{

			final EmailAttachmentModel attachment = getModelService().create(EmailAttachmentModel.class);
			attachment.setCode(realName);
			attachment.setMime(mimeType);
			attachment.setRealFileName(realName);
			attachment.setCatalogVersion(getCatalogVersion());
			try
			{
				final EmailAttachmentModel emailAttachment = flexibleSearchService.getModelByExample(attachment);
				if (null != emailAttachment)
				{
					return emailAttachment;
				}
			}
			catch (ModelNotFoundException modelNotFoundException)
			{
				LOGGER.info("Email Attachment model Not Found");
			}
		}
		return getEmailService().createEmailAttachment(dataInputStream, realName, mimeType);
	}

	protected CatalogVersionModel getCatalogVersion()
	{
		CatalogVersionModel catalogVersion = catalogService.getDefaultCatalog() == null ? null : catalogService.getDefaultCatalog()
				.getActiveCatalogVersion();
		if (catalogVersion == null)
		{
			final Collection<CatalogVersionModel> catalogs = catalogVersionService.getSessionCatalogVersions();
			for (final CatalogVersionModel cvm : catalogs)
			{
				if (cvm.getCatalog() instanceof ContentCatalogModel)
				{
					catalogVersion = cvm;
					break;
				}
			}
		}

		return catalogVersion;
	}


	public ModelService getModelService() {
		return modelService;
	}


	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

}
