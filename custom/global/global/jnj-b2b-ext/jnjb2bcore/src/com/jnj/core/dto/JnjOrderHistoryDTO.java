/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;

import java.util.Date;
import java.util.Map;


/**
 * TODO:<Sandeep kumar-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjOrderHistoryDTO extends OrderHistoryData
{

	private String clientNumber;
	private String jnJOrderNumber;
	private String contractNumber;
	private Date date;
	private Double totalOrder;
	private String orderStatus;
	private Map TestMap;


	/**
	 * @return the testMap
	 */
	public Map getTestMap()
	{
		return TestMap;
	}

	/**
	 * @param testMap
	 *           the testMap to set
	 */
	public void setTestMap(final Map testMap)
	{
		TestMap = testMap;
	}

	/**
	 * @return the clientNumber
	 */
	public String getClientNumber()
	{
		return clientNumber;
	}

	/**
	 * @param clientNumber
	 *           the clientNumber to set
	 */
	public void setClientNumber(final String clientNumber)
	{
		this.clientNumber = clientNumber;
	}

	/**
	 * @return the jnJOrderNumber
	 */
	public String getJnJOrderNumber()
	{
		return jnJOrderNumber;
	}

	/**
	 * @param jnJOrderNumber
	 *           the jnJOrderNumber to set
	 */
	public void setJnJOrderNumber(final String jnJOrderNumber)
	{
		this.jnJOrderNumber = jnJOrderNumber;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber()
	{
		return contractNumber;
	}

	/**
	 * @param contractNumber
	 *           the contractNumber to set
	 */
	public void setContractNumber(final String contractNumber)
	{
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @param date
	 *           the date to set
	 */
	public void setDate(final Date date)
	{
		this.date = date;
	}

	/**
	 * @return the totalOrder
	 */
	public Double getTotalOrder()
	{
		return totalOrder;
	}

	/**
	 * @param totalOrder
	 *           the totalOrder to set
	 */
	public void setTotalOrder(final Double totalOrder)
	{
		this.totalOrder = totalOrder;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 * @param orderStatus
	 *           the orderStatus to set
	 */
	public void setOrderStatus(final String orderStatus)
	{
		this.orderStatus = orderStatus;
	}

}
