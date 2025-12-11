/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

import java.util.List;


/**
 * TODO:<Ujjwal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjCompanyInfoData
{
	private String sector;
	private String accountName;
	private String gLN;
	private String typeOfBusiness;
	private String subsidiaryOf;
	private String billToCountry;
	private String billToLine1;
	private String billToLine2;
	private String billToCity;
	private String billToState;
	private String billToZipCode;
	private String shipToCountry;
	private String shipToLine1;
	private String shipToLine2;
	private String shipToCity;
	private String shipToState;
	private String shipToZipCode;
	private Boolean salesAndUseTaxFlag;
	private String initialOpeningOrderAmount;
	private String estimatedAmountPerYear;
	private List<String> medicalProductsPurchase;

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName()
	{
		return accountName;
	}

	/**
	 * @param accountName
	 *           the accountName to set
	 */
	public void setAccountName(final String accountName)
	{
		this.accountName = accountName;
	}

	/**
	 * @return the gLN
	 */
	public String getgLN()
	{
		return gLN;
	}

	/**
	 * @param gLN
	 *           the gLN to set
	 */
	public void setgLN(final String gLN)
	{
		this.gLN = gLN;
	}

	/**
	 * @return the typeOfBusiness
	 */
	public String getTypeOfBusiness()
	{
		return typeOfBusiness;
	}

	/**
	 * @param typeOfBusiness
	 *           the typeOfBusiness to set
	 */
	public void setTypeOfBusiness(final String typeOfBusiness)
	{
		this.typeOfBusiness = typeOfBusiness;
	}

	/**
	 * @return the subsidiaryOf
	 */
	public String getSubsidiaryOf()
	{
		return subsidiaryOf;
	}

	/**
	 * @param subsidiaryOf
	 *           the subsidiaryOf to set
	 */
	public void setSubsidiaryOf(final String subsidiaryOf)
	{
		this.subsidiaryOf = subsidiaryOf;
	}

	/**
	 * @return the billToCountry
	 */
	public String getBillToCountry()
	{
		return billToCountry;
	}

	/**
	 * @param billToCountry
	 *           the billToCountry to set
	 */
	public void setBillToCountry(final String billToCountry)
	{
		this.billToCountry = billToCountry;
	}

	/**
	 * @return the billToLine1
	 */
	public String getBillToLine1()
	{
		return billToLine1;
	}

	/**
	 * @param billToLine1
	 *           the billToLine1 to set
	 */
	public void setBillToLine1(final String billToLine1)
	{
		this.billToLine1 = billToLine1;
	}

	/**
	 * @return the billToLine2
	 */
	public String getBillToLine2()
	{
		return billToLine2;
	}

	/**
	 * @param billToLine2
	 *           the billToLine2 to set
	 */
	public void setBillToLine2(final String billToLine2)
	{
		this.billToLine2 = billToLine2;
	}

	/**
	 * @return the billToCity
	 */
	public String getBillToCity()
	{
		return billToCity;
	}

	/**
	 * @param billToCity
	 *           the billToCity to set
	 */
	public void setBillToCity(final String billToCity)
	{
		this.billToCity = billToCity;
	}

	/**
	 * @return the billToState
	 */
	public String getBillToState()
	{
		return billToState;
	}

	/**
	 * @param billToState
	 *           the billToState to set
	 */
	public void setBillToState(final String billToState)
	{
		this.billToState = billToState;
	}

	/**
	 * @return the billToZipCode
	 */
	public String getBillToZipCode()
	{
		return billToZipCode;
	}

	/**
	 * @param billToZipCode
	 *           the billToZipCode to set
	 */
	public void setBillToZipCode(final String billToZipCode)
	{
		this.billToZipCode = billToZipCode;
	}

	/**
	 * @return the shipToCountry
	 */
	public String getShipToCountry()
	{
		return shipToCountry;
	}

	/**
	 * @param shipToCountry
	 *           the shipToCountry to set
	 */
	public void setShipToCountry(final String shipToCountry)
	{
		this.shipToCountry = shipToCountry;
	}

	/**
	 * @return the shipToLine1
	 */
	public String getShipToLine1()
	{
		return shipToLine1;
	}

	/**
	 * @param shipToLine1
	 *           the shipToLine1 to set
	 */
	public void setShipToLine1(final String shipToLine1)
	{
		this.shipToLine1 = shipToLine1;
	}

	/**
	 * @return the shipToLine2
	 */
	public String getShipToLine2()
	{
		return shipToLine2;
	}

	/**
	 * @param shipToLine2
	 *           the shipToLine2 to set
	 */
	public void setShipToLine2(final String shipToLine2)
	{
		this.shipToLine2 = shipToLine2;
	}

	/**
	 * @return the shipToCity
	 */
	public String getShipToCity()
	{
		return shipToCity;
	}

	/**
	 * @param shipToCity
	 *           the shipToCity to set
	 */
	public void setShipToCity(final String shipToCity)
	{
		this.shipToCity = shipToCity;
	}

	/**
	 * @return the shipToState
	 */
	public String getShipToState()
	{
		return shipToState;
	}

	/**
	 * @param shipToState
	 *           the shipToState to set
	 */
	public void setShipToState(final String shipToState)
	{
		this.shipToState = shipToState;
	}

	/**
	 * @return the shipToZipCode
	 */
	public String getShipToZipCode()
	{
		return shipToZipCode;
	}

	/**
	 * @param shipToZipCode
	 *           the shipToZipCode to set
	 */
	public void setShipToZipCode(final String shipToZipCode)
	{
		this.shipToZipCode = shipToZipCode;
	}

	/**
	 * @return the salesAndUseTaxFlag
	 */
	public Boolean getSalesAndUseTaxFlag()
	{
		return salesAndUseTaxFlag;
	}

	/**
	 * @param salesAndUseTaxFlag
	 *           the salesAndUseTaxFlag to set
	 */
	public void setSalesAndUseTaxFlag(final Boolean salesAndUseTaxFlag)
	{
		this.salesAndUseTaxFlag = salesAndUseTaxFlag;
	}

	/**
	 * @return the initialOpeningOrderAmount
	 */
	public String getInitialOpeningOrderAmount()
	{
		return initialOpeningOrderAmount;
	}

	/**
	 * @param initialOpeningOrderAmount
	 *           the initialOpeningOrderAmount to set
	 */
	public void setInitialOpeningOrderAmount(final String initialOpeningOrderAmount)
	{
		this.initialOpeningOrderAmount = initialOpeningOrderAmount;
	}

	/**
	 * @return the estimatedAmountPerYear
	 */
	public String getEstimatedAmountPerYear()
	{
		return estimatedAmountPerYear;
	}

	/**
	 * @param estimatedAmountPerYear
	 *           the estimatedAmountPerYear to set
	 */
	public void setEstimatedAmountPerYear(final String estimatedAmountPerYear)
	{
		this.estimatedAmountPerYear = estimatedAmountPerYear;
	}

	/**
	 * @return the medicalProductsPurchase
	 */
	public List<String> getMedicalProductsPurchase()
	{
		return medicalProductsPurchase;
	}

	/**
	 * @param medicalProductsPurchase
	 *           the medicalProductsPurchase to set
	 */
	public void setMedicalProductsPurchase(final List<String> medicalProductsPurchase)
	{
		this.medicalProductsPurchase = medicalProductsPurchase;
	}
}
