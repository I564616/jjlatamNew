/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjselloutaddon.form;

/**
 * This is the form used for saving attributes on Sell Out reports page.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjSellOutReportsForm
{
	private String sortType;
	private String filterType;
	private String pageSize;



	public String getPageSize()
	{
		return pageSize;
	}


	public void setPageSize(final String pageSize)
	{
		this.pageSize = pageSize;
	}


	public String getSortType()
	{
		return sortType;
	}


	public void setSortType(final String sortType)
	{
		this.sortType = sortType;
	}


	public String getFilterType() {
		return filterType;
	}


	public void setFilterType(final String filterType) {
		this.filterType = filterType;
	}

}
