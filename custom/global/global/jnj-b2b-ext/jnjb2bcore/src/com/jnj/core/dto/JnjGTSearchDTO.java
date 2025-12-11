/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSearchDTO
{

	private String searchBy;
	private String searchValue;
	private String searchType;

	/**
	 * @return the searchBy
	 */
	public String getSearchBy()
	{
		return searchBy;
	}

	/**
	 * @param searchBy
	 *           the searchBy to set
	 */
	public void setSearchBy(final String searchBy)
	{
		this.searchBy = searchBy;
	}

	/**
	 * @return the searchValue
	 */
	public String getSearchValue()
	{
		return searchValue;
	}

	/**
	 * @param searchValue
	 *           the searchValue to set
	 */
	public void setSearchValue(final String searchValue)
	{
		this.searchValue = searchValue;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType()
	{
		return searchType;
	}

	/**
	 * @param searchType
	 *           the searchType to set
	 */
	public void setSearchType(final String searchType)
	{
		this.searchType = searchType;
	}


}
