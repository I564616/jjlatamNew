/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;

/**
 * This class holds the data for the single PA report search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSinglePurchaseAnalysisReportForm extends JnjGTPurchaseAnalysisReportForm
{
	private String productCode;
	private String periodBreakdown;
	private String lotNumber;

	/**
	 * @return the productCode
	 */
	public String getProductCode()
	{
		return productCode;
	}

	/**
	 * @param productCode
	 *           the productCode to set
	 */
	public void setProductCode(final String productCode)
	{
		this.productCode = productCode;
	}

	/**
	 * @return the periodBreakdown
	 */
	public String getPeriodBreakdown()
	{
		return periodBreakdown;
	}

	/**
	 * @param periodBreakdown
	 *           the periodBreakdown to set
	 */
	public void setPeriodBreakdown(final String periodBreakdown)
	{
		this.periodBreakdown = periodBreakdown;
	}

	/**
	 * @return the lotNumber
	 */
	public String getLotNumber()
	{
		return lotNumber;
	}

	/**
	 * @param lotNumber
	 *           the lotNumber to set
	 */
	public void setLotNumber(final String lotNumber)
	{
		this.lotNumber = lotNumber;
	}

}
