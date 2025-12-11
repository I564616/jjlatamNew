/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dto;

import java.util.List;

import com.jnj.core.util.JnjFormGroup;


/**
 * This is the DTO class for the Integral Services Form
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjIntegralServicesDTO extends JnjFormDTO
{
	private String contactEmail;
	private String contactPhone;
	private String soldTo;
	private String shipTo;
	private String specialStockPartner;
	private String orderReason;
	private String periodFrom;
	private String periodTo;
	private String poNumber;
	private List<JnjFormGroup> jnjFormGroup;
	/** Additional Info **/
	private String customerName; // Customer linked to logged in user
	private String hospital; // Hospital linked to logged in user
	private String contactFirstName; // Logged in user first name
	private String contactLastName; // Logged in user last name

	/**
	 * @return the contactEmail
	 */
	public String getContactEmail()
	{
		return contactEmail;
	}

	/**
	 * @param contactEmail
	 *           the contactEmail to set
	 */
	public void setContactEmail(final String contactEmail)
	{
		this.contactEmail = contactEmail;
	}

	/**
	 * @return the contactPhone
	 */
	public String getContactPhone()
	{
		return contactPhone;
	}

	/**
	 * @param contactPhone
	 *           the contactPhone to set
	 */
	public void setContactPhone(final String contactPhone)
	{
		this.contactPhone = contactPhone;
	}

	/**
	 * @return the soldTo
	 */
	public String getSoldTo()
	{
		return soldTo;
	}

	/**
	 * @param soldTo
	 *           the soldTo to set
	 */
	public void setSoldTo(final String soldTo)
	{
		this.soldTo = soldTo;
	}

	/**
	 * @return the shipTo
	 */
	public String getShipTo()
	{
		return shipTo;
	}

	/**
	 * @param shipTo
	 *           the shipTo to set
	 */
	public void setShipTo(final String shipTo)
	{
		this.shipTo = shipTo;
	}

	/**
	 * @return the specialStockPartner
	 */
	public String getSpecialStockPartner()
	{
		return specialStockPartner;
	}

	/**
	 * @param specialStockPartner
	 *           the specialStockPartner to set
	 */
	public void setSpecialStockPartner(final String specialStockPartner)
	{
		this.specialStockPartner = specialStockPartner;
	}

	/**
	 * @return the orderReason
	 */
	public String getOrderReason()
	{
		return orderReason;
	}

	/**
	 * @param orderReason
	 *           the orderReason to set
	 */
	public void setOrderReason(final String orderReason)
	{
		this.orderReason = orderReason;
	}

	/**
	 * @return the periodFrom
	 */
	public String getPeriodFrom()
	{
		return periodFrom;
	}

	/**
	 * @param periodFrom
	 *           the periodFrom to set
	 */
	public void setPeriodFrom(final String periodFrom)
	{
		this.periodFrom = periodFrom;
	}

	/**
	 * @return the periodTo
	 */
	public String getPeriodTo()
	{
		return periodTo;
	}

	/**
	 * @param periodTo
	 *           the periodTo to set
	 */
	public void setPeriodTo(final String periodTo)
	{
		this.periodTo = periodTo;
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

	/**
	 * @return the jnjFormGroup
	 */
	public List<JnjFormGroup> getJnjFormGroup()
	{
		return jnjFormGroup;
	}

	/**
	 * @param jnjFormGroup
	 *           the jnjFormGroup to set
	 */
	public void setJnjFormGroup(final List<JnjFormGroup> jnjFormGroup)
	{
		this.jnjFormGroup = jnjFormGroup;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName()
	{
		return customerName;
	}

	/**
	 * @param customerName
	 *           the customerName to set
	 */
	public void setCustomerName(final String customerName)
	{
		this.customerName = customerName;
	}

	/**
	 * @return the hospital
	 */
	public String getHospital()
	{
		return hospital;
	}

	/**
	 * @param hospital
	 *           the hospital to set
	 */
	public void setHospital(final String hospital)
	{
		this.hospital = hospital;
	}

	/**
	 * @return the contactFirstName
	 */
	public String getContactFirstName()
	{
		return contactFirstName;
	}

	/**
	 * @param contactFirstName
	 *           the contactFirstName to set
	 */
	public void setContactFirstName(final String contactFirstName)
	{
		this.contactFirstName = contactFirstName;
	}

	/**
	 * @return the contactLastName
	 */
	public String getContactLastName()
	{
		return contactLastName;
	}

	/**
	 * @param contactLastName
	 *           the contactLastName to set
	 */
	public void setContactLastName(final String contactLastName)
	{
		this.contactLastName = contactLastName;
	}

}
