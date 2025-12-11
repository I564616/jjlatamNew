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
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnJCustomerDTO
{
	/**
	 * @param args
	 */
	private String accountType = "";
	private Boolean centralOrderBlock;
	private String city = "";
	private String region = "";
	private String postalCode = "";
	private String country = "";
	private String customerNumer = "";
	private String indicator = "";
	private String district = "";
	private String industryCode1 = "";
	private String industryCode2 = "";
	private String customerType = "";
	private String name1 = "";
	private String name2 = "";
	private String street = "";
	private String telephone = "";
	private List<JnJSalesOrgDTO> jnJSalesOrgDTOList;
	private String fileName;
	private String idocNumber;

	/**
	 * @return the fileName
	 */
	public String getFileName()
	{
		return fileName;
	}

	/**
	 * @param fileName
	 *           the fileName to set
	 */
	public void setFileName(final String fileName)
	{
		this.fileName = fileName;
	}

	/**
	 * @return the idocNumber
	 */
	public String getIdocNumber()
	{
		return idocNumber;
	}

	/**
	 * @param idocNumber
	 *           the idocNumber to set
	 */
	public void setIdocNumber(final String idocNumber)
	{
		this.idocNumber = idocNumber;
	}

	/**
	 * @return the region
	 */
	public String getRegion()
	{
		return region;
	}

	/**
	 * @param region
	 *           the region to set
	 */
	public void setRegion(final String region)
	{
		this.region = region;
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
	 * @return the jnJSalesOrgDTOList
	 */
	public List<JnJSalesOrgDTO> getJnJSalesOrgDTOList()
	{
		return jnJSalesOrgDTOList;
	}

	/**
	 * @param jnJSalesOrgDTOList
	 *           the jnJSalesOrgDTOList to set
	 */
	public void setJnJSalesOrgDTOList(final List<JnJSalesOrgDTO> jnJSalesOrgDTOList)
	{
		this.jnJSalesOrgDTOList = jnJSalesOrgDTOList;
	}

	/**
	 * @return the accountType
	 */
	public String getAccountType()
	{
		return accountType;
	}

	/**
	 * @param accountType
	 *           the accountType to set
	 */
	public void setAccountType(final String accountType)
	{
		this.accountType = accountType;
	}

	/**
	 * @return the centralOrderBlock
	 */
	public Boolean getCentralOrderBlock()
	{
		return centralOrderBlock;
	}

	/**
	 * @param centralOrderBlock
	 *           the centralOrderBlock to set
	 */
	public void setCentralOrderBlock(final Boolean centralOrderBlock)
	{
		this.centralOrderBlock = centralOrderBlock;
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

	/**
	 * @return the postalCode
	 */
	public String getPostalCode()
	{
		return postalCode;
	}

	/**
	 * @param postalCode
	 *           the postalCode to set
	 */
	public void setPostalCode(final String postalCode)
	{
		this.postalCode = postalCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry()
	{
		return country;
	}

	/**
	 * @param country
	 *           the country to set
	 */
	public void setCountry(final String country)
	{
		this.country = country;
	}


	/**
	 * @return the customerNumer
	 */
	public String getCustomerNumer()
	{
		return customerNumer;
	}

	/**
	 * @param customerNumer
	 *           the customerNumer to set
	 */
	public void setCustomerNumer(final String customerNumer)
	{
		this.customerNumer = customerNumer;
	}

	/**
	 * @return the indicator
	 */
	public String getIndicator()
	{
		return indicator;
	}

	/**
	 * @param indicator
	 *           the indicator to set
	 */
	public void setIndicator(final String indicator)
	{
		this.indicator = indicator;
	}

	/**
	 * @return the district
	 */
	public String getDistrict()
	{
		return district;
	}

	/**
	 * @param district
	 *           the district to set
	 */
	public void setDistrict(final String district)
	{
		this.district = district;
	}

	/**
	 * @return the industryCode1
	 */
	public String getIndustryCode1()
	{
		return industryCode1;
	}

	/**
	 * @param industryCode1
	 *           the industryCode1 to set
	 */
	public void setIndustryCode1(final String industryCode1)
	{
		this.industryCode1 = industryCode1;
	}

	/**
	 * @return the industryCode2
	 */
	public String getIndustryCode2()
	{
		return industryCode2;
	}

	/**
	 * @param industryCode2
	 *           the industryCode2 to set
	 */
	public void setIndustryCode2(final String industryCode2)
	{
		this.industryCode2 = industryCode2;
	}

	/**
	 * @return the name1
	 */
	public String getName1()
	{
		return name1;
	}

	/**
	 * @param name1
	 *           the name1 to set
	 */
	public void setName1(final String name1)
	{
		this.name1 = name1;
	}

	/**
	 * @return the name2
	 */
	public String getName2()
	{
		return name2;
	}

	/**
	 * @param name2
	 *           the name2 to set
	 */
	public void setName2(final String name2)
	{
		this.name2 = name2;
	}

	/**
	 * @return the street
	 */
	public String getStreet()
	{
		return street;
	}

	/**
	 * @param street
	 *           the street to set
	 */
	public void setStreet(final String street)
	{
		this.street = street;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone()
	{
		return telephone;
	}

	/**
	 * @param telephone
	 *           the telephone to set
	 */
	public void setTelephone(final String telephone)
	{
		this.telephone = telephone;
	}



}
