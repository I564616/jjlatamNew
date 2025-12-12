/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.dto;

import org.springframework.web.multipart.MultipartFile;


/**
 * This is the data class for Sell Out Reports
 * 
 * @author Manoj.K.Panda
 * @version 1.0
 */
public class JnjSellOutReportData
{
	private String docName;
	private String date;
	private String company;
	private String customer;
	private String user;
	private String hour;

	private MultipartFile file = null;

	/**
	 * @return the file
	 */
	public MultipartFile getFile()
	{
		return file;
	}

	/**
	 * @param file
	 *           the file to set
	 */
	public void setFile(final MultipartFile file)
	{
		this.file = file;
	}

	/**
	 * @return the hour
	 */
	public String getHour()
	{
		return hour;
	}

	/**
	 * @param hour
	 *           the hour to set
	 */
	public void setHour(final String hour)
	{
		this.hour = hour;
	}


	/**
	 * @return the docName
	 */
	public String getDocName()
	{
		return docName;
	}

	/**
	 * @param docName
	 *           the docName to set
	 */
	public void setDocName(final String docName)
	{
		this.docName = docName;
	}

	/**
	 * @return the date
	 */
	public String getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *           the date to set
	 */
	public void setDate(final String date)
	{
		this.date = date;
	}

	/**
	 * @return the company
	 */
	public String getCompany()
	{
		return company;
	}

	/**
	 * @param company
	 *           the company to set
	 */
	public void setCompany(final String company)
	{
		this.company = company;
	}

	/**
	 * @return the customer
	 */
	public String getCustomer()
	{
		return customer;
	}

	/**
	 * @param customer
	 *           the customer to set
	 */
	public void setCustomer(final String customer)
	{
		this.customer = customer;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user
	 *           the user to set
	 */
	public void setUser(final String user)
	{
		this.user = user;
	}

}
