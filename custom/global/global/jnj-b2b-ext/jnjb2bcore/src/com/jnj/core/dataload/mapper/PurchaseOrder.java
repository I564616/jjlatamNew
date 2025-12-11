/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataload.mapper;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * The PurchaseOrder class is user defined class using for storing the xml data.
 * 
 * @author Accenture sumit.y.kumar
 * @version 1.0
 */
@XmlRootElement(name = "PurchaseOrder")
public class PurchaseOrder
{
	protected String eChannel = "HybrisLATAM";
	protected Header header;
	protected Items items;

	/**
	 * @return the header
	 */
	@XmlElement(name = "Header")
	public Header getHeader()
	{
		return header;
	}

	/**
	 * @param header
	 *           the header to set
	 */
	public void setHeader(final Header header)
	{
		this.header = header;
	}

	/**
	 * @return the items
	 */
	@XmlElement(name = "Items")
	public Items getItems()
	{
		return items;
	}

	/**
	 * @param items
	 *           the items to set
	 */
	public void setItems(final Items items)
	{
		this.items = items;
	}

	/**
	 * @return the eChannel
	 */
	public String geteChannel()
	{
		return eChannel;
	}

	/**
	 * @param eChannel
	 *           the eChannel to set
	 */
	public void seteChannel(final String eChannel)
	{
		this.eChannel = eChannel;
	}

}
