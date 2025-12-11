/**
 * 
 */
package com.jnj.core.services.impl;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * This class is responsible for Email Generation for the contact us functionality
 * 
 * @author sanchit.a.kumar
 * 
 */
public class ContactUsEmailGenerationService extends DefaultEmailGenerationService
{
	private static final Logger LOG = Logger.getLogger(ContactUsEmailGenerationService.class);

	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody,
			final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		super.createEmailMessage(emailSubject, emailBody, emailContext);
		
		
		
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Creating Contact Us Email Message");
		}
		final List<EmailAddressModel> toEmails = new ArrayList<EmailAddressModel>();
		
		
		Map<String, Object> map = (Map<String, Object>)  emailContext.get("contactUsEmailMap");
		String email = (String) map.get("email");
		String displayName= (String) map.get("displayName");
		
		if(StringUtils.isNotBlank(email) && StringUtils.isNotBlank(displayName)){
			toEmails.add(getEmailService().getOrCreateEmailAddressForEmail(email, displayName));

		}else{
		toEmails.add(getEmailService().getOrCreateEmailAddressForEmail(emailContext.getEmail(), emailContext.getToDisplayName()));
		}

		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(),
				emailContext.getFromDisplayName());

		LOG.info("Contact Us Email Message created by Generation Service");
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(),
				new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
	}
}
