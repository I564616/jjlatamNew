/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.data;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.util.List;


/**
 * This is the data class for the customer basic data
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjGTCustomerBasicData extends CustomerData
{
	/** This is the User UID **/
	private String uid;

	/** This is the user first name **/
	private String firstName;

	/** This is the user last name **/
	private String lastName;

	/** This is the current b2b unit name **/
	private String currentB2BUnitName;

	/** This is the current b2b unit UID **/
	private String currentB2BUnitID;

	/** This is the current b2b unit GLN **/
	private String currentB2BUnitGLN;

	/** This is the show change account variable **/
	private String showChangeAccount;

	/** This is the site name indicator for MDD or Consumer **/
	private String jnjSiteName;

	/** This is the site name indicator for MDD or Consumer :: Added for CR 12th January **/
	private boolean adminUser;

	private List<String> emailPrefrences;
	
	/** This is the user type indicator :: Added for AAOL-5857 **/
	private String userRole;
	

	/**
	 * This is the WWID associated with the user : used to determine if the user is an internal or external user - Added
	 * as part of the Google Analytics build change
	 **/
	private String wwid;

	/**
	 * @return the UID
	 */
	@Override
	public String getUid()
	{
		return uid;
	}

	/**
	 * @return the emailPrefrences
	 */
	public List<String> getEmailPrefrences()
	{
		return emailPrefrences;
	}

	/**
	 * @param emailPrefrences
	 *           the emailPrefrences to set
	 */
	public void setEmailPrefrences(final List<String> emailPrefrences)
	{
		this.emailPrefrences = emailPrefrences;
	}

	/**
	 * @param uid
	 *           the UID to set
	 */
	@Override
	public void setUid(final String uid)
	{
		this.uid = uid;
	}

	/**
	 * @return the firstName
	 */
	@Override
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	@Override
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@Override
	public String getLastName()
	{
		return lastName;
	}

	/**
	 * @param lastName
	 *           the lastName to set
	 */
	@Override
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the currentB2BUnitName
	 */
	public String getCurrentB2BUnitName()
	{
		return currentB2BUnitName;
	}

	/**
	 * @param currentB2BUnitName
	 *           the currentB2BUnitName to set
	 */
	public void setCurrentB2BUnitName(final String currentB2BUnitName)
	{
		this.currentB2BUnitName = currentB2BUnitName;
	}

	/**
	 * @return the currentB2BUnitID
	 */
	public String getCurrentB2BUnitID()
	{
		return currentB2BUnitID;
	}

	/**
	 * @param currentB2BUnitID
	 *           the currentB2BUnitID to set
	 */
	public void setCurrentB2BUnitID(final String currentB2BUnitID)
	{
		this.currentB2BUnitID = currentB2BUnitID;
	}

	/**
	 * @return the currentB2BUnitGLN
	 */
	public String getCurrentB2BUnitGLN()
	{
		return currentB2BUnitGLN;
	}

	/**
	 * @param currentB2BUnitGLN
	 *           the currentB2BUnitGLN to set
	 */
	public void setCurrentB2BUnitGLN(final String currentB2BUnitGLN)
	{
		this.currentB2BUnitGLN = currentB2BUnitGLN;
	}

	/**
	 * @return the showChangeAccount
	 */
	public String getShowChangeAccount()
	{
		return showChangeAccount;
	}

	/**
	 * @param showChangeAccount
	 *           the showChangeAccount to set
	 */
	public void setShowChangeAccount(final String showChangeAccount)
	{
		this.showChangeAccount = showChangeAccount;
	}

	/**
	 * @return the jnjSiteName
	 */
	public String getJnjSiteName()
	{
		return jnjSiteName;
	}

	/**
	 * @param jnjSiteName
	 *           the jnjSiteName to set
	 */
	public void setJnjSiteName(final String jnjSiteName)
	{
		this.jnjSiteName = jnjSiteName;
	}

	/**
	 * @return the adminUser
	 */
	public boolean isAdminUser()
	{
		return adminUser;
	}

	/**
	 * @param adminUser
	 *           the adminUser to set
	 */
	public void setAdminUser(final boolean adminUser)
	{
		this.adminUser = adminUser;
	}

	/**
	 * @return the wwid
	 */
	public String getWwid()
	{
		return wwid;
	}

	/**
	 * @param wwid
	 *           the wwid to set
	 */
	public void setWwid(final String wwid)
	{
		this.wwid = wwid;
	}
	
	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
