/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalreports.forms;

/**
 * This class holds the data for the multiple PA report search form
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTMultiPurchaseAnalysisReportForm extends JnjGTPurchaseAnalysisReportForm
{
	private String territory;
	private String operatingCompany;
	private String franchiseDivCode;
	private String productsToDisplay;
	private String analysisVariable;

	/**
	 * @return the territory
	 */
	public String getTerritory()
	{
		return territory;
	}

	/**
	 * @param territory
	 *           the territory to set
	 */
	public void setTerritory(final String territory)
	{
		this.territory = territory;
	}

	/**
	 * @return the operatingCompany
	 */
	public String getOperatingCompany()
	{
		return operatingCompany;
	}

	/**
	 * @param operatingCompany
	 *           the operatingCompany to set
	 */
	public void setOperatingCompany(final String operatingCompany)
	{
		this.operatingCompany = operatingCompany;
	}

	/**
	 * @return the franchiseDivCode
	 */
	public String getFranchiseDivCode()
	{
		return franchiseDivCode;
	}

	/**
	 * @param franchiseDivCode
	 *           the franchiseDivCode to set
	 */
	public void setFranchiseDivCode(final String franchiseDivCode)
	{
		this.franchiseDivCode = franchiseDivCode;
	}

	/**
	 * @return the productsToDisplay
	 */
	public String getProductsToDisplay()
	{
		return productsToDisplay;
	}

	/**
	 * @param productsToDisplay
	 *           the productsToDisplay to set
	 */
	public void setProductsToDisplay(final String productsToDisplay)
	{
		this.productsToDisplay = productsToDisplay;
	}

	/**
	 * @return the analysisVariable
	 */
	public String getAnalysisVariable()
	{
		return analysisVariable;
	}

	/**
	 * @param analysisVariable
	 *           the analysisVariable to set
	 */
	public void setAnalysisVariable(final String analysisVariable)
	{
		this.analysisVariable = analysisVariable;
	}
}
