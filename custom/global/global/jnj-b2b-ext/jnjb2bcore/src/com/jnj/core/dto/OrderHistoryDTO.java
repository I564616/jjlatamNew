/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;



/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class OrderHistoryDTO
{
	private String code;
	private String fieldName;
	private String downLoadType;
	private String sortbynumber;
	private String fromDate;
	private String toDate;
	private String status;

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName()
	{
		return fieldName;
	}

	/**
	 * @param fieldName
	 *           the fieldName to set
	 */
	public void setFieldName(final String fieldName)
	{
		this.fieldName = fieldName;
	}

	/**
	 * @return the downLoadType
	 */
	public String getDownLoadType()
	{
		return downLoadType;
	}

	/**
	 * @param downLoadType
	 *           the downLoadType to set
	 */
	public void setDownLoadType(final String downLoadType)
	{
		this.downLoadType = downLoadType;
	}

	/**
	 * @return the sortbynumber
	 */
	public String getSortbynumber()
	{
		return sortbynumber;
	}

	/**
	 * @param sortbynumber
	 *           the sortbynumber to set
	 */
	public void setSortbynumber(final String sortbynumber)
	{
		this.sortbynumber = sortbynumber;
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
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}



}
