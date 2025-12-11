/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dataload.mapper;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * The Items class is user defined class using for storing the xml data.
 * 
 * @author Accenture sumit.y.kumar
 * @version 1.0
 */
@XmlRootElement(name = "Items")
public class Items
{
	private List<LineItem> lineItem;

	/**
	 * @return the lineItem
	 */
	@XmlElement(name = "LineItem")
	public List<LineItem> getLineItem()
	{
		return lineItem;
	}

	/**
	 * @param lineItem
	 *           the lineItem to set
	 */
	public void setLineItem(final List<LineItem> lineItem)
	{
		this.lineItem = lineItem;
	}

}