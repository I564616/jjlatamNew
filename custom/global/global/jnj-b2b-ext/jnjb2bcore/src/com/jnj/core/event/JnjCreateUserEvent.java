/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;

import java.util.List;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCreateUserEvent extends AbstractCommerceUserEvent
{
	private String password;
	private List<String> roles;
	private String firstName;
	private String lastName;
	private String emailID;
	private String phone;
	private String mobile;
	private boolean emailNotification;
	private String portalName;
	private String adminUser;
	private String loginUrl;
	private String logoURL;
	private boolean registrationEmailSent;

	/**
	 * @return the roles
	 */
	public List<String> getRoles()
	{
		return roles;
	}

	/**
	 * @param roles
	 *           the roles to set
	 */
	public void setRoles(final List<String> roles)
	{
		this.roles = roles;
	}

	/**
	 * @return the registrationEmailSent
	 */
	public boolean getRistrationEmailSent()
	{
		return registrationEmailSent;
	}

	/**
	 * @param registrationEmailSent
	 *           to set
	 */
	public void setRegistrationEmailSent(final boolean registrationEmailSent)
	{
		this.registrationEmailSent = registrationEmailSent;
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
	 * @return the loginUrl
	 */
	public String getLoginUrl()
	{
		return loginUrl;
	}

	/**
	 * @param loginUrl
	 *           the loginUrl to set
	 */
	public void setLoginUrl(final String loginUrl)
	{
		this.loginUrl = loginUrl;
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
	 * @return the adminUser
	 */
	public String getAdminUser()
	{
		return adminUser;
	}

	/**
	 * @param adminUser
	 *           the adminUser to set
	 */
	public void setAdminUser(final String adminUser)
	{
		this.adminUser = adminUser;
	}

	/**
	 * @return the emailNotification
	 */
	public boolean getEmailNotification()
	{
		return emailNotification;
	}

	/**
	 * @param emailNotification
	 *           the emailNotification to set
	 */
	public void setEmailNotification(final boolean emailNotification)
	{
		this.emailNotification = emailNotification;
	}


	/**
	 * @return the firstName
	 */
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the emailID
	 */
	public String getEmailID()
	{
		return emailID;
	}

	/**
	 * @param emailID
	 *           the emailID to set
	 */
	public void setEmailID(final String emailID)
	{
		this.emailID = emailID;
	}


	public JnjCreateUserEvent()
	{
	}

	public JnjCreateUserEvent(final String password)
	{
		this.setPassword(password);
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password
	 *           the password to set
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}
}
