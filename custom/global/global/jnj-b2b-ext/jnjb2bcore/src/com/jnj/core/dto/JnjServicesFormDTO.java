/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * This is the DTO class for the Service OCD form
 * 
 * @author Accenture
 * @version 1.0
 * 
 * 
 */
public class JnjServicesFormDTO extends JnjFormDTO
{
	private String phone;
	private String email;
	private String additionalContact;
	private String model;
	private String serialNumber;
	private String eventDate;
	private String errorCode;
	private String eventDescription;
	/** Additional Info **/
	private String contactFirstName; // Logged in user first name
	private String contactLastName; // Logged in user last name
	private String loggedInUserPhone; // Logged in user phone
	private String customerName;
	private String customerCity;
	private String customerState;
	private String customerProvince;

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
	 * @return the additionalContact
	 */
	public String getAdditionalContact()
	{
		return additionalContact;
	}

	/**
	 * @param additionalContact
	 *           the additionalContact to set
	 */
	public void setAdditionalContact(final String additionalContact)
	{
		this.additionalContact = additionalContact;
	}

	/**
	 * @return the model
	 */
	public String getModel()
	{
		return model;
	}

	/**
	 * @param model
	 *           the model to set
	 */
	public void setModel(final String model)
	{
		this.model = model;
	}

	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber()
	{
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *           the serialNumber to set
	 */
	public void setSerialNumber(final String serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	/**
	 * @return the eventDate
	 */
	public String getEventDate()
	{
		return eventDate;
	}

	/**
	 * @param eventDate
	 *           the eventDate to set
	 */
	public void setEventDate(final String eventDate)
	{
		this.eventDate = eventDate;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode
	 *           the errorCode to set
	 */
	public void setErrorCode(final String errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return the eventDescription
	 */
	public String getEventDescription()
	{
		return eventDescription;
	}

	/**
	 * @param eventDescription
	 *           the eventDescription to set
	 */
	public void setEventDescription(final String eventDescription)
	{
		this.eventDescription = eventDescription;
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
	 * @return the loggedInUserPhone
	 */
	public String getLoggedInUserPhone()
	{
		return loggedInUserPhone;
	}

	/**
	 * @param loggedInUserPhone
	 *           the loggedInUserPhone to set
	 */
	public void setLoggedInUserPhone(final String loggedInUserPhone)
	{
		this.loggedInUserPhone = loggedInUserPhone;
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
	 * @return the customerState
	 */
	public String getCustomerState()
	{
		return customerState;
	}

	/**
	 * @param customerState
	 *           the customerState to set
	 */
	public void setCustomerState(final String customerState)
	{
		this.customerState = customerState;
	}

	/**
	 * @return the customerProvince
	 */
	public String getCustomerProvince()
	{
		return customerProvince;
	}

	/**
	 * @param customerProvince
	 *           the customerProvince to set
	 */
	public void setCustomerProvince(final String customerProvince)
	{
		this.customerProvince = customerProvince;
	}

}
