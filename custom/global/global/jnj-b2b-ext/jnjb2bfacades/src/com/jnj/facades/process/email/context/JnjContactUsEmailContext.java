/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.log4j.Logger;

import com.jnj.core.model.ContactUsProcessModel;



/**
 * TODO:<Sanchit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjContactUsEmailContext extends AbstractEmailContext<ContactUsProcessModel>
{
	private CustomerData customerData;
	private Converter<UserModel, CustomerData> customerConverter;
	private String contactUsEmailSubject;
	private String contactUsEmailBody;
	private String contactUsEmailOrderId;
	private static final Logger LOG = Logger.getLogger(JnjContactUsEmailContext.class);

	/**
	 * This method is responsible for initializing address and content
	 * 
	 * @param businessProcessModel
	 * @param emailPageModel
	 */
	@Override
	public void init(final ContactUsProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Contact Us Email Context - Initializing");
		}
		super.init(businessProcessModel, emailPageModel);
		customerData = getCustomerConverter().convert(businessProcessModel.getCustomer());
		initializeAddresses(businessProcessModel);
		initializeEmailContent(businessProcessModel);
		LOG.info("Contact Us Email Context Initialized");
	}

	/**
	 * This method initializes the From Email, From Display Name, Recipient email address
	 * 
	 * @param businessProcessModel
	 */
	private void initializeAddresses(final ContactUsProcessModel businessProcessModel)
	{
		// Setting the to email address
		if (null != businessProcessModel.getToEmailAddress())
		{
			put(EMAIL, businessProcessModel.getToEmailAddress());
		}
		// Setting the from email address (Logged in user)
		put(FROM_EMAIL, businessProcessModel.getCustomer().getUid());

		//Setting the from display name (Logged in user)
		put(FROM_DISPLAY_NAME, businessProcessModel.getCustomer().getName());
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Contact Us : Addresses Initialized :: FROM " + businessProcessModel.getCustomer().getUid() + "; FROM NAME "
					+ businessProcessModel.getCustomer().getName() + "; TO EMAIL " + businessProcessModel.getToEmailAddress());
		}
	}

	/**
	 * This method initializes the Email Content - Subject and Body
	 * 
	 * @param businessProcessModel
	 */
	private void initializeEmailContent(final ContactUsProcessModel businessProcessModel)
	{
		// Setting the Email Subject and Body
		setContactUsEmailSubject(businessProcessModel.getEmailSubject());
		setContactUsEmailOrderId(businessProcessModel.getEmailOrderId());
		setContactUsEmailBody(businessProcessModel.getEmailBody());
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Contact Us : Content Initialized :: Subject " + businessProcessModel.getEmailSubject() + "; BODY "
					+ businessProcessModel.getEmailBody());
		}
	}

	@Override
	protected BaseSiteModel getSite(final ContactUsProcessModel businessProcessModel)
	{
		return businessProcessModel.getSite();
	}


	@Override
	protected CustomerModel getCustomer(final ContactUsProcessModel businessProcessModel)
	{
		return businessProcessModel.getCustomer();
	}

	@Override
	protected LanguageModel getEmailLanguage(final ContactUsProcessModel businessProcessModel)
	{
		return businessProcessModel.getLanguage();
	}

	/**
	 * @return the customerData
	 */
	public CustomerData getCustomerData()
	{
		return customerData;
	}

	/**
	 * @param customerData
	 *           the customerData to set
	 */
	public void setCustomerData(final CustomerData customerData)
	{
		this.customerData = customerData;
	}

	/**
	 * @return the customerConverter
	 */
	public Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	/**
	 * @param customerConverter
	 *           the customerConverter to set
	 */
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}

	/**
	 * @return the contactUsEmailSubject
	 */
	public String getContactUsEmailSubject()
	{
		return contactUsEmailSubject;
	}

	/**
	 * @param contactUsEmailSubject
	 *           the contactUsEmailSubject to set
	 */
	public void setContactUsEmailSubject(final String contactUsEmailSubject)
	{
		this.contactUsEmailSubject = contactUsEmailSubject;
	}

	/**
	 * @return the contactUsEmailBody
	 */
	public String getContactUsEmailBody()
	{
		return contactUsEmailBody;
	}

	/**
	 * @param contactUsEmailBody
	 *           the contactUsEmailBody to set
	 */
	public void setContactUsEmailBody(final String contactUsEmailBody)
	{
		this.contactUsEmailBody = contactUsEmailBody;
	}

	/**
	 * @return the contactUsEmailOrderId
	 */
	public String getContactUsEmailOrderId()
	{
		return contactUsEmailOrderId;
	}

	/**
	 * @param contactUsEmailOrderId
	 *           the contactUsEmailOrderId to set
	 */
	public void setContactUsEmailOrderId(final String contactUsEmailOrderId)
	{
		this.contactUsEmailOrderId = contactUsEmailOrderId;
	}
}
