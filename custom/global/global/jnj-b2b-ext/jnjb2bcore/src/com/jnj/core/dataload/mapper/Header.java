/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataload.mapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * The Header class is user defined class using for storing the xml data.
 * 
 * @author Accenture sumit.y.kumar
 * @version 1.0
 */
@XmlRootElement(name = "Header")
public class Header implements Cloneable
{
    protected static final Logger LOGGER = Logger.getLogger(Header.class);

	protected String pODate;
	protected String pONumber;
	protected String vendorID;
	protected String shipToNumber;
	protected String soldToNumber;
	protected String requestedDeliveryDate = Jnjb2bCoreConstants.Order.EMPTY_STRING;
	protected String notes = Jnjb2bCoreConstants.Order.EMPTY_STRING;

	public Header() {
		this.pODate = new SimpleDateFormat(Jnjb2bCoreConstants.Order.PO_DATE_FORMAT).format(new Date());
	}

    public Header(final String dateFormat) {
	    this.pODate = new SimpleDateFormat(dateFormat).format(new Date());
    }

    /**
	 * The clone method is used to avoid the same reference.
	 */
	@Override
	public Header clone()
	{
		Header header = null;

		try
		{
			header = (Header) super.clone();
		}
		catch (final CloneNotSupportedException exception)
		{
			LOGGER.error("Error Occured in clone method of Header class " + exception);
		}

		return header;
	}

	/**
	 * @return the pODate
	 */
	@XmlElement(name = "PODate")
	public String getpODate()
	{
		return pODate;
	}

	/**
	 * @param pODate
	 *           the pODate to set
	 */
	public void setpODate(final String pODate)
	{
		this.pODate = pODate;
	}

	/**
	 * @return the pONumber
	 */
	@XmlElement(name = "PONumber")
	public String getpONumber()
	{
		return pONumber;
	}

	/**
	 * @param pONumber
	 *           the pONumber to set
	 */
	public void setpONumber(final String pONumber)
	{
		this.pONumber = pONumber;
	}

	/**
	 * @return the vendorID
	 */
	@XmlElement(name = "VendorID")
	public String getVendorID()
	{
		return vendorID;
	}

	/**
	 * @param vendorID
	 *           the vendorID to set
	 */
	public void setVendorID(final String vendorID)
	{
		this.vendorID = vendorID;
	}

	/**
	 * @return the shipToNumber
	 */
	@XmlElement(name = "ShipToNumber")
	public String getShipToNumber()
	{
		return shipToNumber;
	}

	/**
	 * @param shipToNumber
	 *           the shipToNumber to set
	 */
	public void setShipToNumber(final String shipToNumber)
	{
		this.shipToNumber = shipToNumber;
	}

	/**
	 * @return the soldToNumber
	 */
	@XmlElement(name = "SoldToNumber")
	public String getSoldToNumber()
	{
		return soldToNumber;
	}

	/**
	 * @param soldToNumber
	 *           the soldToNumber to set
	 */
	public void setSoldToNumber(final String soldToNumber)
	{
		this.soldToNumber = soldToNumber;
	}

	/**
	 * @return the requestedDeliveryDate
	 */
	@XmlElement(name = "RequestedDeliveryDate")
	public String getRequestedDeliveryDate()
	{
		return requestedDeliveryDate;
	}

	/**
	 * @param requestedDeliveryDate
	 *           the requestedDeliveryDate to set
	 */
	public void setRequestedDeliveryDate(final String requestedDeliveryDate)
	{
		this.requestedDeliveryDate = requestedDeliveryDate;
	}

	/**
	 * @return the notes
	 */
	@XmlElement(name = "Notes")
	public String getNotes()
	{
		return notes;
	}

	/**
	 * @param notes
	 *           the notes to set
	 */
	public void setNotes(final String notes)
	{
		this.notes = notes;
	}

}
