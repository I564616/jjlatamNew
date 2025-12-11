/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.event;

import java.util.List;

import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPasswordExpiryEvent extends AbstractCommerceUserEvent
{
	private String emailID;
	private String logoURL;
	private String portalName;
	private String securityWindow;
	private Integer daysBeforePasswordExpiry;
	private String userFirstName;
	private String userLastName;
	private String customerName;
	private List<String> userRoles;
	private Boolean emailNotifications;

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
	 * @return the userRoles
	 */
	public List<String> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return the emailNotifications
	 */
	public Boolean getEmailNotifications() {
		return emailNotifications;
	}

	/**
	 * @param emailNotifications the emailNotifications to set
	 */
	public void setEmailNotifications(Boolean emailNotifications) {
		this.emailNotifications = emailNotifications;
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
