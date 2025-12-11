/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.process.email.context;

import java.util.List;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnjCreateUserProcessModel;
import com.jnj.core.model.JnjPasswordExpiryEmailProcessModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.MessageFacadeUtill;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPasswordExpiryEmailContext extends CustomerEmailContext
{
	private static final Logger LOG = Logger.getLogger(JnjPasswordExpiryEmailContext.class);
	private static final String PASSWORD_EXPIRY_SECURITY_WINDOW_KEY = "user.password.expirationPeriod";
	private static final String PASSWORD_EXPIRY_SUBJECT_KEY = "email.password.expiry.subject";
	private static final String MSG_DETAIL1_KEY = "email.password.expiry.msg1";
	private static final String MSG_DETAIL2_KEY = "email.password.expiry.msg2";
	private static final String MSG_DETAIL3_KEY = "email.password.expiry.msg3";
	private static final String MSG_DETAIL4_KEY = "email.password.expiry.msg4";
	private static final String PASSWORD_EXPIRY_DAYS = "email.password.expiry.";
	private static final String MSG_GREETING_KEY = "user.email.newUser.dearuser";
	private static final String USER_FIRST_NAME_KEY = "profile.firstName";
	private static final String USER_LAST_NAME_KEY = "profile.lastName";
	private static final String USER_ROLE_KEY = "profile.role";
	private static final String USER_ID_KEY = "profile.email";
	private static final String EMAIL_NOTIFICATIONS_KEY = "text.register.email.notification";
	
	private String emailSubject;
	private String userId;
	private String userIdLabel;
	private String securityWindow;
	private Integer daysBeforePasswordExpiry;
	private String userLanguage;
	private String userEmailGreetingMsg;
	private String userEmailInitialDescriptiveMsg;
	private String portalName;
	private String logoURL;
	private String customerName;
	private String userFirstName;
	private String userFirstNameLabel;
	private String userLastName;
	private String userLastNameLabel;
	private String userRoles;
	private String userRolesLabel;
	private String emailNotifications;	
	private String emailNotificationsLabel;

	@Autowired
	private MessageFacadeUtill messageFacade;

	@Override
	public void init(final StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(storeFrontCustomerProcessModel, emailPageModel);
		LOG.debug("Start processing Password Expiry Email context..........");
		if (storeFrontCustomerProcessModel instanceof JnjPasswordExpiryEmailProcessModel)
		{
			setUserId(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getEmailID());
			setUserLanguage(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getLanguage().getName());
			setPortalName(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getPortalName());
			setLogoURL(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getLogoURL());
			setDaysBeforePasswordExpiry(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel)
					.getDaysBeforePasswordExpiry());
			setCustomerName(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getCustomerName());
			setUserFirstName(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getUserFirstName());
			setUserLastName(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getUserLastName());
			setUserRoles(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getUserRoles());
			setEmailNotifications(((JnjPasswordExpiryEmailProcessModel) storeFrontCustomerProcessModel).getEmailNotifications()?"checked":"");
			try
			{
				setUserIdLabel(messageFacade.getMessageTextForCode(USER_ID_KEY));
				setEmailSubject(messageFacade.getMessageTextForCode(PASSWORD_EXPIRY_SUBJECT_KEY)); // Action Required to Retain Access
				setUserEmailInitialDescriptiveMsg(getCommonEmailMessage(getPortalName(), getDaysBeforePasswordExpiry()));
				setUserEmailGreetingMsg(messageFacade.getMessageTextForCode(MSG_GREETING_KEY));				
				setUserFirstNameLabel(messageFacade.getMessageTextForCode(USER_FIRST_NAME_KEY));
				setUserLastNameLabel(messageFacade.getMessageTextForCode(USER_LAST_NAME_KEY));
				setUserRolesLabel(messageFacade.getMessageTextForCode(USER_ROLE_KEY));
				setEmailNotificationsLabel(messageFacade.getMessageTextForCode(EMAIL_NOTIFICATIONS_KEY));				
			}			
			catch (final BusinessException exp)
			{
				LOG.error("Unable to fetch subject for the create user email::" + exp.getMessage());
			}
			LOG.debug(getUserIdLabel() + ":" + getUserId());
			LOG.debug(getUserFirstNameLabel() + ":" + getUserFirstName());
			LOG.debug(getUserLastNameLabel() + ":" + getUserLastName());
			LOG.debug(getUserRolesLabel() + ":" + getUserRoles());
			LOG.debug(getEmailNotificationsLabel() + ":" + getEmailNotifications());
			LOG.debug("Portal :" + getPortalName());
		}
		LOG.debug("End processing Password Expiry Email context..........");
	}



	/**
	 * @return the emailSubject
	 */
	public String getEmailSubject()
	{
		return emailSubject;
	}


	/**
	 * @param emailSubject
	 *           the emailSubject to set
	 */
	public void setEmailSubject(final String emailSubject)
	{
		this.emailSubject = emailSubject;
	}


	/**
	 * @return the userId
	 */
	public String getUserId()
	{
		return userId;
	}


	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(final String userId)
	{
		this.userId = userId;
	}


	/**
	 * @return the securityWindow
	 */
	public String getSecurityWindow()
	{
		return securityWindow;
	}


	/**
	 * @param securityWindow
	 *           the securityWindow to set
	 */
	public void setSecurityWindow(final String securityWindow)
	{
		this.securityWindow = securityWindow;
	}


	/**
	 * @return the daysBeforePasswordExpiry
	 */
	public Integer getDaysBeforePasswordExpiry()
	{
		return daysBeforePasswordExpiry;
	}


	/**
	 * @param daysBeforePasswordExpiry
	 *           the daysBeforePasswordExpiry to set
	 */
	public void setDaysBeforePasswordExpiry(final Integer daysBeforePasswordExpiry)
	{
		this.daysBeforePasswordExpiry = daysBeforePasswordExpiry;
	}


	/**
	 * @return the userLanguage
	 */
	public String getUserLanguage()
	{
		return userLanguage;
	}


	/**
	 * @param userLanguage
	 *           the userLanguage to set
	 */
	public void setUserLanguage(final String userLanguage)
	{
		this.userLanguage = userLanguage;
	}


	/**
	 * @return the userEmailGreetingMsg
	 */
	public String getUserEmailGreetingMsg()
	{
		return userEmailGreetingMsg;
	}


	/**
	 * @param userEmailGreetingMsg
	 *           the userEmailGreetingMsg to set
	 */
	public void setUserEmailGreetingMsg(final String userEmailGreetingMsg)
	{
		this.userEmailGreetingMsg = userEmailGreetingMsg;
	}


	/**
	 * @return the userEmailInitialDescriptiveMsg
	 */
	public String getUserEmailInitialDescriptiveMsg()
	{
		return userEmailInitialDescriptiveMsg;
	}


	/**
	 * @param userEmailInitialDescriptiveMsg
	 *           the userEmailInitialDescriptiveMsg to set
	 */
	public void setUserEmailInitialDescriptiveMsg(final String userEmailInitialDescriptiveMsg)
	{
		this.userEmailInitialDescriptiveMsg = userEmailInitialDescriptiveMsg;
	}


	/**
	 * @return the portalName
	 */
	public String getPortalName()
	{
		return portalName;
	}


	/**
	 * @param portalName
	 *           the portalName to set
	 */
	public void setPortalName(final String portalName)
	{
		this.portalName = portalName;
	}


	/**
	 * @return the messageFacade
	 */
	public MessageFacadeUtill getMessageFacade()
	{
		return messageFacade;
	}


	/**
	 * @param messageFacade
	 *           the messageFacade to set
	 */
	public void setMessageFacade(final MessageFacadeUtill messageFacade)
	{
		this.messageFacade = messageFacade;
	}


	private String getCommonEmailMessage(final String portalName, final Integer daysBeforeEmailSent) throws BusinessException
	{
		final StringBuffer emailMessage = new StringBuffer();
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL1_KEY) + " "); //For security reasons, your 
		emailMessage.append(portalName + " "); //[PORTALNAME]
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL2_KEY) + " "); //password expires every
		emailMessage.append(messageFacade.getMessageTextForCode(PASSWORD_EXPIRY_SECURITY_WINDOW_KEY) + " "); //90 days
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL3_KEY) + " "); //You are receiving this reminder because it your password will expire in 
		emailMessage.append(daysBeforeEmailSent + " "); //[15/10/5/1]
		if(daysBeforeEmailSent==1){
			emailMessage.append(messageFacade.getMessageTextForCode(PASSWORD_EXPIRY_DAYS + daysBeforeEmailSent) + ". "); //day(s)
		}else{
			emailMessage.append(messageFacade.getMessageTextForCode(PASSWORD_EXPIRY_DAYS) + ". "); //day(s)	
		}
		emailMessage.append(messageFacade.getMessageTextForCode(MSG_DETAIL4_KEY) + " "); //The password should be 8 characters or longer with a combination of .......
		return emailMessage.toString();
	}

	/**
	 * @return the logoURL
	 */
	public String getLogoURL()
	{
		return logoURL;
	}

	/**
	 * @param logoURL
	 *           the logoURL to set
	 */
	public void setLogoURL(final String logoURL)
	{
		this.logoURL = logoURL;
	}


	/**
	 * @return the userFirstName
	 */
	public String getUserFirstName() {
		return userFirstName;
	}


	/**
	 * @param userFirstName the userFirstName to set
	 */
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}


	/**
	 * @return the userFirstNameLabel
	 */
	public String getUserFirstNameLabel() {
		return userFirstNameLabel;
	}


	/**
	 * @param userFirstNameLabel the userFirstNameLabel to set
	 */
	public void setUserFirstNameLabel(String userFirstNameLabel) {
		this.userFirstNameLabel = userFirstNameLabel;
	}


	/**
	 * @return the userLastName
	 */
	public String getUserLastName() {
		return userLastName;
	}


	/**
	 * @param userLastName the userLastName to set
	 */
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}


	/**
	 * @return the userLastNameLabel
	 */
	public String getUserLastNameLabel() {
		return userLastNameLabel;
	}


	/**
	 * @param userLastNameLabel the userLastNameLabel to set
	 */
	public void setUserLastNameLabel(String userLastNameLabel) {
		this.userLastNameLabel = userLastNameLabel;
	}

	/**
	 * @return the userRoles
	 */
	public String getUserRoles() {
		return userRoles;
	}


	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}


	/**
	 * @return the userRolesLabel
	 */
	public String getUserRolesLabel() {
		return userRolesLabel;
	}


	/**
	 * @param userRolesLabel the userRolesLabel to set
	 */
	public void setUserRolesLabel(String userRolesLabel) {
		this.userRolesLabel = userRolesLabel;
	}



	/**
	 * @return the emailNotifications
	 */
	public String getEmailNotifications() {
		return emailNotifications;
	}



	/**
	 * @param emailNotifications the emailNotifications to set
	 */
	public void setEmailNotifications(String emailNotifications) {
		this.emailNotifications = emailNotifications;
	}



	/**
	 * @return the emailNotificationsLabel
	 */
	public String getEmailNotificationsLabel() {
		return emailNotificationsLabel;
	}


	/**
	 * @param emailNotificationsLabel the emailNotificationsLabel to set
	 */
	public void setEmailNotificationsLabel(String emailNotificationsLabel) {
		this.emailNotificationsLabel = emailNotificationsLabel;
	}


	/**
	 * @return the userIdLabel
	 */
	public String getUserIdLabel() {
		return userIdLabel;
	}


	/**
	 * @param userIdLabel the userIdLabel to set
	 */
	public void setUserIdLabel(String userIdLabel) {
		this.userIdLabel = userIdLabel;
	}



	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}



	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	
}
