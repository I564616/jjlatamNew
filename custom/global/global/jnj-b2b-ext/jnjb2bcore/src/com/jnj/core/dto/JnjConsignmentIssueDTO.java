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
 * This is the DTO Class for the consignment issue form
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjConsignmentIssueDTO extends JnjFormDTO
{
	private String contactEmail;
	private String contactPhone;
	private String soldTo;
	private String shipTo;
	private String specialStockPartner;
	private String orderReason;
	private String date;
	private String poNumber;
	private String replenishmentOrFillUpDoc;
	private String replenishmentOrFillUpNFE;
	private String patient;
	private String doctor;
	private List<String> item;
	private List<String> qty;
	private List<String> uom;
	private List<String> batchNumber;
	private List<String> folio;
	private List<String> price;
	private List<String> currency;
	/** Additional Info **/
	private String customerName; // Customer linked to logged in user
	private String hospital; // Hospital linked to logged in user
	private String contactFirstName; // Logged in user first name
	private String contactLastName; // Logged in user last name
	private String cityOrState; // City /State of the logged in user

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
	 * @return the replenishmentOrFillUpDoc
	 */
	public String getReplenishmentOrFillUpDoc()
	{
		return replenishmentOrFillUpDoc;
	}

	/**
	 * @param replenishmentOrFillUpDoc
	 *           the replenishmentOrFillUpDoc to set
	 */
	public void setReplenishmentOrFillUpDoc(final String replenishmentOrFillUpDoc)
	{
		this.replenishmentOrFillUpDoc = replenishmentOrFillUpDoc;
	}

	/**
	 * @return the replenishmentOrFillUpNFE
	 */
	public String getReplenishmentOrFillUpNFE()
	{
		return replenishmentOrFillUpNFE;
	}

	/**
	 * @param replenishmentOrFillUpNFE
	 *           the replenishmentOrFillUpNFE to set
	 */
	public void setReplenishmentOrFillUpNFE(final String replenishmentOrFillUpNFE)
	{
		this.replenishmentOrFillUpNFE = replenishmentOrFillUpNFE;
	}

	/**
	 * @return the patient
	 */
	public String getPatient()
	{
		return patient;
	}

	/**
	 * @param patient
	 *           the patient to set
	 */
	public void setPatient(final String patient)
	{
		this.patient = patient;
	}

	/**
	 * @return the doctor
	 */
	public String getDoctor()
	{
		return doctor;
	}

	/**
	 * @param doctor
	 *           the doctor to set
	 */
	public void setDoctor(final String doctor)
	{
		this.doctor = doctor;
	}

	/**
	 * @return the item
	 */
	public List<String> getItem()
	{
		return item;
	}

	/**
	 * @param item
	 *           the item to set
	 */
	public void setItem(final List<String> item)
	{
		this.item = item;
	}

	/**
	 * @return the qty
	 */
	public List<String> getQty()
	{
		return qty;
	}

	/**
	 * @param qty
	 *           the qty to set
	 */
	public void setQty(final List<String> qty)
	{
		this.qty = qty;
	}

	/**
	 * @return the uom
	 */
	public List<String> getUom()
	{
		return uom;
	}

	/**
	 * @param uom
	 *           the uom to set
	 */
	public void setUom(final List<String> uom)
	{
		this.uom = uom;
	}

	/**
	 * @return the batchNumber
	 */
	public List<String> getBatchNumber()
	{
		return batchNumber;
	}

	/**
	 * @param batchNumber
	 *           the batchNumber to set
	 */
	public void setBatchNumber(final List<String> batchNumber)
	{
		this.batchNumber = batchNumber;
	}

	/**
	 * @return the folio
	 */
	public List<String> getFolio()
	{
		return folio;
	}

	/**
	 * @param folio
	 *           the folio to set
	 */
	public void setFolio(final List<String> folio)
	{
		this.folio = folio;
	}

	/**
	 * @return the price
	 */
	public List<String> getPrice()
	{
		return price;
	}

	/**
	 * @param price
	 *           the price to set
	 */
	public void setPrice(final List<String> price)
	{
		this.price = price;
	}

	/**
	 * @return the currency
	 */
	public List<String> getCurrency()
	{
		return currency;
	}

	/**
	 * @param currency
	 *           the currency to set
	 */
	public void setCurrency(final List<String> currency)
	{
		this.currency = currency;
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

	/**
	 * @return the cityOrState
	 */
	public String getCityOrState()
	{
		return cityOrState;
	}

	/**
	 * @param cityOrState
	 *           the cityOrState to set
	 */
	public void setCityOrState(final String cityOrState)
	{
		this.cityOrState = cityOrState;
	}
}
