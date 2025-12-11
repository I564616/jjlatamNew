/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.data;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 *
 */
public class JnjLatamUploadOrderData
{

	private String docName;
	private String date;
	private String customer;
	private String user;
	private String status;
	private String trackingID;
	// Added for EDI changes start
	private Boolean isLinkEnable;
	private List<String> errorMessageList;
	private String poNumber;
	private String fileNameId;

	/**
	 * @return the fileNameId
	 */
	public String getFileNameId()
	{
		return fileNameId;
	}

	/**
	 * @param fileNameId
	 *           the fileNameId to set
	 */
	public void setFileNameId(final String fileNameId)
	{
		this.fileNameId = fileNameId;
	}

	/**
	 * @return the poNumber
	 */
	public String getPoNumber()
	{
		return poNumber;
	}

	/**
	 * @param poNumber
	 *           the poNumber to set
	 */
	public void setPoNumber(final String poNumber)
	{
		this.poNumber = poNumber;
	}

	public Boolean getIsLinkEnable()
	{
		return isLinkEnable;
	}

	public void setIsLinkEnable(final Boolean isLinkEnable)
	{
		this.isLinkEnable = isLinkEnable;
	}

	public List<String> getErrorMessageList()
	{
		return new ArrayList<>(errorMessageList);
	}

	public void setErrorMessageList(final List<String> errorMessageList)
	{
		this.errorMessageList = new ArrayList<>(errorMessageList);
	}

	// Added for EDI changes end

	/**
	 * @return the trackingID
	 */
	public String getTrackingID()
	{
		return trackingID;
	}

	/**
	 * @param trackingID
	 *           the trackingID to set
	 */
	public void setTrackingID(final String trackingID)
	{
		this.trackingID = trackingID;
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

	private CommonsMultipartFile file = null;

	/**
	 * @return the file
	 */
	public CommonsMultipartFile getFile()
	{
		return file;
	}

	/**
	 * @param file
	 *           the file to set
	 */
	public void setFile(final CommonsMultipartFile file)
	{
		this.file = file;
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
