/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dto;

/**
 * This is the DTO Class for the add indirect customer form
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjAddIndirectCustomerDTO extends JnjFormDTO
{
	private String customerName;
	private String cnpjOrCpf;
	private String cnpjOrCpfText;
	private String cpfPatientOrPhysicianName;
	private String customerType;
	private String publicOrPrivate;
	private String state;
	private String city;
	private String address;
	private String neighborhood;
	private String zipCode;

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
	 * @return the cnpjOrCpf
	 */
	public String getCnpjOrCpf()
	{
		return cnpjOrCpf;
	}

	/**
	 * @param cnpjOrCpf
	 *           the cnpjOrCpf to set
	 */
	public void setCnpjOrCpf(final String cnpjOrCpf)
	{
		this.cnpjOrCpf = cnpjOrCpf;
	}

	/**
	 * @return the cnpjOrCpfText
	 */
	public String getCnpjOrCpfText()
	{
		return cnpjOrCpfText;
	}

	/**
	 * @param cnpjOrCpfText
	 *           the cnpjOrCpfText to set
	 */
	public void setCnpjOrCpfText(final String cnpjOrCpfText)
	{
		this.cnpjOrCpfText = cnpjOrCpfText;
	}

	/**
	 * @return the cpfPatientOrPhysicianName
	 */
	public String getCpfPatientOrPhysicianName()
	{
		return cpfPatientOrPhysicianName;
	}

	/**
	 * @param cpfPatientOrPhysicianName
	 *           the cpfPatientOrPhysicianName to set
	 */
	public void setCpfPatientOrPhysicianName(final String cpfPatientOrPhysicianName)
	{
		this.cpfPatientOrPhysicianName = cpfPatientOrPhysicianName;
	}

	/**
	 * @return the customerType
	 */
	public String getCustomerType()
	{
		return customerType;
	}

	/**
	 * @param customerType
	 *           the customerType to set
	 */
	public void setCustomerType(final String customerType)
	{
		this.customerType = customerType;
	}

	/**
	 * @return the publicOrPrivate
	 */
	public String getPublicOrPrivate()
	{
		return publicOrPrivate;
	}

	/**
	 * @param publicOrPrivate
	 *           the publicOrPrivate to set
	 */
	public void setPublicOrPrivate(final String publicOrPrivate)
	{
		this.publicOrPrivate = publicOrPrivate;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *           the state to set
	 */
	public void setState(final String state)
	{
		this.state = state;
	}

	/**
	 * @return the address
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * @param address
	 *           the address to set
	 */
	public void setAddress(final String address)
	{
		this.address = address;
	}

	/**
	 * @return the neighborhood
	 */
	public String getNeighborhood()
	{
		return neighborhood;
	}

	/**
	 * @param neighborhood
	 *           the neighborhood to set
	 */
	public void setNeighborhood(final String neighborhood)
	{
		this.neighborhood = neighborhood;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode()
	{
		return zipCode;
	}

	/**
	 * @param zipCode
	 *           the zipCode to set
	 */
	public void setZipCode(final String zipCode)
	{
		this.zipCode = zipCode;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *           the city to set
	 */
	public void setCity(final String city)
	{
		this.city = city;
	}
}
