/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;


/**
 * This class is use to extend OOTB PageableData
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPageableData extends PageableData
{
	private String searchBy;
	private String searchText;
	private String status;
	private String toDate; //Added for CP014: Laudo
	private String fromDate; //Added for CP014: Laudo
	private String additionalSearchText; //Added for CP014: Laudo

	public String getSearchBy()
	{
		return searchBy;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public void setSearchBy(final String searchBy)
	{
		this.searchBy = searchBy;
	}


	public String getSearchText()
	{
		return searchText;
	}

	public void setSearchText(final String searchText)
	{
		this.searchText = searchText;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate()
	{
		return toDate;
	}

	/**
	 * @param toDate
	 *           the toDate to set
	 */
	public void setToDate(final String toDate)
	{
		this.toDate = toDate;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate()
	{
		return fromDate;
	}

	/**
	 * @param fromDate
	 *           the fromDate to set
	 */
	public void setFromDate(final String fromDate)
	{
		this.fromDate = fromDate;
	}

	/**
	 * @return the additionalSearchText
	 */
	public String getAdditionalSearchText()
	{
		return additionalSearchText;
	}

	/**
	 * @param additionalSearchText
	 *           the additionalSearchText to set
	 */
	public void setAdditionalSearchText(final String additionalSearchText)
	{
		this.additionalSearchText = additionalSearchText;
	}


}
