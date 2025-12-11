/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.storefront.forms;

import org.springframework.web.multipart.MultipartFile;


/**
 * This form is used for storing all attributes of the file that is being uploaded.
 * 
 * @author Accenture
 * @version 1.0
 */
public class FileUploadForm
{
	private String name = null;
	private CommonsMultipartFile file = null;
	private String date;
	private String hour;
	private String company;

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

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public CommonsMultipartFile getFile()
	{
		return file;
	}

	public void setFile(final CommonsMultipartFile file)
	{
		this.file = file;
		this.name = file.getOriginalFilename();
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(final String date)
	{
		this.date = date;
	}

	public String getHour()
	{
		return hour;
	}

	public void setHour(final String hour)
	{
		this.hour = hour;
	}
}
