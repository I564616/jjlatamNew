/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.data;

import java.sql.Date;


/**
 * TODO:<Balinder-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPasswordExpiryData
{
	private String userID;
	private Integer lastEmailSentFor;
	private Date passwordChangeDate;

	/**
	 * @return the userID
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * @param userID
	 *           the userID to set
	 */
	public void setUserID(final String userID)
	{
		this.userID = userID;
	}

	/**
	 * @return the lastEmailSentFor
	 */
	public Integer getLastEmailSentFor()
	{
		return lastEmailSentFor;
	}

	/**
	 * @param lastEmailSentFor
	 *           the lastEmailSentFor to set
	 */
	public void setLastEmailSentFor(final Integer lastEmailSentFor)
	{
		this.lastEmailSentFor = lastEmailSentFor;
	}

	/**
	 * @return the passwordChangeDate
	 */
	public Date getPasswordChangeDate()
	{
		return passwordChangeDate;
	}

	/**
	 * @param passwordChangeDate
	 *           the passwordChangeDate to set
	 */
	public void setPasswordChangeDate(final Date passwordChangeDate)
	{
		this.passwordChangeDate = passwordChangeDate;
	}
}
