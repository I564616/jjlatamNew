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
 * This is the DTO class for the Synthes form
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjSynthesDTO extends JnjFormDTO
{
	private String name;
	private String email;
	private String phone;
	private String surgeryDate;
	private String surgeryHour;
	private String physicianName;
	private String patientName;
	private String patientId;
	private String billToName;
	private String billToId;
	private String billToRequestedBy;
	private String billToReserveDate;
	private String billToReserveHour;
	private String billToCustomerSalesOrder;
	private String billToSurgeryClassification;
	private String billToSurgeryEquipment;
	private String billToExpectedDeliveryDate;
	private String billToExpectedDeliveryHour;
	private String notes;
	private List<String> material;
	private List<String> description;
	private List<String> reference;
	private List<String> tabName;
	private List<String> qty;
	private List<String> uom;
	/** Additional Info **/
	private String customerId;
	private String customerName;
	private String customerAddress;
	private String customerCity;
	private String customerCountry;

	/**
	 * @return the customerId
	 */
	public String getCustomerId()
	{
		return customerId;
	}

	/**
	 * @param customerId
	 *           the customerId to set
	 */
	public void setCustomerId(final String customerId)
	{
		this.customerId = customerId;
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
	 * @return the customerAddress
	 */
	public String getCustomerAddress()
	{
		return customerAddress;
	}

	/**
	 * @param customerAddress
	 *           the customerAddress to set
	 */
	public void setCustomerAddress(final String customerAddress)
	{
		this.customerAddress = customerAddress;
	}

	/**
	 * @return the customerCity
	 */
	public String getCustomerCity()
	{
		return customerCity;
	}

	/**
	 * @param customerCity
	 *           the customerCity to set
	 */
	public void setCustomerCity(final String customerCity)
	{
		this.customerCity = customerCity;
	}

	/**
	 * @return the customerCountry
	 */
	public String getCustomerCountry()
	{
		return customerCountry;
	}

	/**
	 * @param customerCountry
	 *           the customerCountry to set
	 */
	public void setCustomerCountry(final String customerCountry)
	{
		this.customerCountry = customerCountry;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *           the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the surgeryDate
	 */
	public String getSurgeryDate()
	{
		return surgeryDate;
	}

	/**
	 * @param surgeryDate
	 *           the surgeryDate to set
	 */
	public void setSurgeryDate(final String surgeryDate)
	{
		this.surgeryDate = surgeryDate;
	}

	/**
	 * @return the surgeryHour
	 */
	public String getSurgeryHour()
	{
		return surgeryHour;
	}

	/**
	 * @param surgeryHour
	 *           the surgeryHour to set
	 */
	public void setSurgeryHour(final String surgeryHour)
	{
		this.surgeryHour = surgeryHour;
	}

	/**
	 * @return the physicianName
	 */
	public String getPhysicianName()
	{
		return physicianName;
	}

	/**
	 * @param physicianName
	 *           the physicianName to set
	 */
	public void setPhysicianName(final String physicianName)
	{
		this.physicianName = physicianName;
	}

	/**
	 * @return the patientName
	 */
	public String getPatientName()
	{
		return patientName;
	}

	/**
	 * @param patientName
	 *           the patientName to set
	 */
	public void setPatientName(final String patientName)
	{
		this.patientName = patientName;
	}

	/**
	 * @return the patientId
	 */
	public String getPatientId()
	{
		return patientId;
	}

	/**
	 * @param patientId
	 *           the patientId to set
	 */
	public void setPatientId(final String patientId)
	{
		this.patientId = patientId;
	}

	/**
	 * @return the billToName
	 */
	public String getBillToName()
	{
		return billToName;
	}

	/**
	 * @param billToName
	 *           the billToName to set
	 */
	public void setBillToName(final String billToName)
	{
		this.billToName = billToName;
	}

	/**
	 * @return the billToId
	 */
	public String getBillToId()
	{
		return billToId;
	}

	/**
	 * @param billToId
	 *           the billToId to set
	 */
	public void setBillToId(final String billToId)
	{
		this.billToId = billToId;
	}

	/**
	 * @return the billToRequestedBy
	 */
	public String getBillToRequestedBy()
	{
		return billToRequestedBy;
	}

	/**
	 * @param billToRequestedBy
	 *           the billToRequestedBy to set
	 */
	public void setBillToRequestedBy(final String billToRequestedBy)
	{
		this.billToRequestedBy = billToRequestedBy;
	}

	/**
	 * @return the billToReserveDate
	 */
	public String getBillToReserveDate()
	{
		return billToReserveDate;
	}

	/**
	 * @param billToReserveDate
	 *           the billToReserveDate to set
	 */
	public void setBillToReserveDate(final String billToReserveDate)
	{
		this.billToReserveDate = billToReserveDate;
	}

	/**
	 * @return the billToReserveHour
	 */
	public String getBillToReserveHour()
	{
		return billToReserveHour;
	}

	/**
	 * @param billToReserveHour
	 *           the billToReserveHour to set
	 */
	public void setBillToReserveHour(final String billToReserveHour)
	{
		this.billToReserveHour = billToReserveHour;
	}

	/**
	 * @return the billToCustomerSalesOrder
	 */
	public String getBillToCustomerSalesOrder()
	{
		return billToCustomerSalesOrder;
	}

	/**
	 * @param billToCustomerSalesOrder
	 *           the billToCustomerSalesOrder to set
	 */
	public void setBillToCustomerSalesOrder(final String billToCustomerSalesOrder)
	{
		this.billToCustomerSalesOrder = billToCustomerSalesOrder;
	}

	/**
	 * @return the billToSurgeryClassification
	 */
	public String getBillToSurgeryClassification()
	{
		return billToSurgeryClassification;
	}

	/**
	 * @param billToSurgeryClassification
	 *           the billToSurgeryClassification to set
	 */
	public void setBillToSurgeryClassification(final String billToSurgeryClassification)
	{
		this.billToSurgeryClassification = billToSurgeryClassification;
	}

	/**
	 * @return the billToSurgeryEquipment
	 */
	public String getBillToSurgeryEquipment()
	{
		return billToSurgeryEquipment;
	}

	/**
	 * @param billToSurgeryEquipment
	 *           the billToSurgeryEquipment to set
	 */
	public void setBillToSurgeryEquipment(final String billToSurgeryEquipment)
	{
		this.billToSurgeryEquipment = billToSurgeryEquipment;
	}

	/**
	 * @return the billToExpectedDeliveryDate
	 */
	public String getBillToExpectedDeliveryDate()
	{
		return billToExpectedDeliveryDate;
	}

	/**
	 * @param billToExpectedDeliveryDate
	 *           the billToExpectedDeliveryDate to set
	 */
	public void setBillToExpectedDeliveryDate(final String billToExpectedDeliveryDate)
	{
		this.billToExpectedDeliveryDate = billToExpectedDeliveryDate;
	}

	/**
	 * @return the billToExpectedDeliveryHour
	 */
	public String getBillToExpectedDeliveryHour()
	{
		return billToExpectedDeliveryHour;
	}

	/**
	 * @param billToExpectedDeliveryHour
	 *           the billToExpectedDeliveryHour to set
	 */
	public void setBillToExpectedDeliveryHour(final String billToExpectedDeliveryHour)
	{
		this.billToExpectedDeliveryHour = billToExpectedDeliveryHour;
	}

	/**
	 * @return the notes
	 */
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

	/**
	 * @return the material
	 */
	public List<String> getMaterial()
	{
		return material;
	}

	/**
	 * @param material
	 *           the material to set
	 */
	public void setMaterial(final List<String> material)
	{
		this.material = material;
	}

	/**
	 * @return the description
	 */
	public List<String> getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(final List<String> description)
	{
		this.description = description;
	}

	/**
	 * @return the reference
	 */
	public List<String> getReference()
	{
		return reference;
	}

	/**
	 * @param reference
	 *           the reference to set
	 */
	public void setReference(final List<String> reference)
	{
		this.reference = reference;
	}

	/**
	 * @return the tabName
	 */
	public List<String> getTabName()
	{
		return tabName;
	}

	/**
	 * @param tabName
	 *           the tabName to set
	 */
	public void setTabName(final List<String> tabName)
	{
		this.tabName = tabName;
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

}
