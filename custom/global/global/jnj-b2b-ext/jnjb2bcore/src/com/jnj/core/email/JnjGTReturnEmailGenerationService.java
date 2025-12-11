/**
 * 
 */
package com.jnj.core.email;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.activation.MimetypesFileTypeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.model.ModelService;
/**
 * @author nsinha7
 *
 */
public class JnjGTReturnEmailGenerationService extends DefaultEmailGenerationService{

	@Autowired
	protected CatalogService catalogService;
	@Autowired
	protected CatalogVersionService catalogVersionService;
	@Autowired
	FlexibleSearchService flexibleSearchService;
	@Autowired
	MediaService mediaService;
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
		
		List<EmailAttachmentModel> attachments = new ArrayList<EmailAttachmentModel>();
		
//		String mailFor =  (String) emailContext.get(Jnjb2bCoreConstants.MAIL_FOR);
		List<MediaModel> attachmentList = (List<MediaModel>) emailContext.get(Jnjb2bCoreConstants.EMAIL_ATTACHMENT_LIST);
		if(attachmentList!=null && !(attachmentList.isEmpty())){
			attachments = createAttachment(attachmentList);
		}

		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getToEmail(),
				emailContext.getToDisplayName());
		toEmails.add(toAddress);
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
				new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, attachments);
	}

	/**
	 * Method is used to convert images uploaded for return order as  attachment(s) for the email.
	 * @param attachmentList
	 * @param mailFor 
	 * @return
	 */
	protected List<EmailAttachmentModel> createAttachment(final List<MediaModel> attachmentList)
	{
		LOGGER.info(Logging.BEGIN_OF_METHOD + "createAttachment()" + Logging.HYPHEN);
		
		
		final List<EmailAttachmentModel> attachments = new ArrayList<>();
		DataInputStream dataInputStream = null;
		final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion("mstrContCatalog", "Staged");
		if(attachmentList!=null && !(attachmentList.isEmpty())){
			
			for (MediaModel media:attachmentList){
				dataInputStream = (DataInputStream) mediaService.getDataStreamFromMedia(media);

				final MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
				final String mimeType = mimeTypesMap.getContentType(media.getRealFileName());

						
				
				final EmailAttachmentModel attachment = getEmailAttachment(dataInputStream,media.getRealFileName(), mimeType);

				
				attachments.add(attachment);
			}
		}
		
		

		LOGGER.info(Logging.END_OF_METHOD + "createAttachment()" + Logging.HYPHEN);
		return attachments;
	}


	/**
	 * @param dataInputStream
	 * @param realName
	 * @param mimeType
	 */
	protected EmailAttachmentModel getEmailAttachment(final DataInputStream dataInputStream, final String realName,
			final String mimeType)
	{
		final EmailAttachmentModel attachment = getModelService().create(EmailAttachmentModel.class);
		attachment.setCode(realName);
		attachment.setMime(mimeType);
		attachment.setFolder(mediaService.getFolder("returnAttachments"));
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
		return getEmailService().createEmailAttachment(dataInputStream, realName, mimeType);
	}
	
	/**
	 * This method is used to get the catalog version.
	 * @return
	 */
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
