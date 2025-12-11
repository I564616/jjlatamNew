/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.dto;

import java.util.ArrayList;
import java.util.List;

import com.jnj.core.dto.JnJCustomerDTO;


/**
 *
 */
public class JnJLaCustomerDTO extends JnJCustomerDTO
{

	private String cnpj;
	private String bothIndicator;
	private List<LaPartnerFunction> laPartnerFunction;
	private String lastUpdateDate;
	private List<JnJLaSalesOrgDTO> jnjLaSalesOrgDTOList;
	private String customerFreightType;

	/**
	 * @return the cnpj
	 */
	public String getCnpj()
	{
		return cnpj;
	}

	/**
	 * @param cnpj
	 *           the cnpj to set
	 */
	public void setCnpj(final String cnpj)
	{
		this.cnpj = cnpj;
	}

	/**
	 * @return the bothIndicator
	 */
	public String getBothIndicator()
	{
		return bothIndicator;
	}

	/**
	 * @param bothIndicator
	 *           the bothIndicator to set
	 */
	public void setBothIndicator(final String bothIndicator)
	{
		this.bothIndicator = bothIndicator;
	}

	/**
	 * @return the partnerFunction
	 */
	public List<LaPartnerFunction> getLaPartnerFunction()
	{
		return new ArrayList<>(laPartnerFunction);
	}

	/**
	 * @param laPartnerFunction
	 *           the partnerFunction to set
	 */
	public void setLaPartnerFunction(final List<LaPartnerFunction> laPartnerFunction)
	{
		this.laPartnerFunction = new ArrayList<>(laPartnerFunction);
	}

	/**
	 * @return the lastUpdateDate
	 */
	public String getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate
	 *           the lastUpdateDate to set
	 */
	public void setLastUpdateDate(final String lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * @return the jnjLaSalesOrgDTOList
	 */
	public List<JnJLaSalesOrgDTO> getJnjLaSalesOrgDTOList()
	{
		return new ArrayList<>(jnjLaSalesOrgDTOList);
	}

	/**
	 * @param jnjLaSalesOrgDTOList
	 *           the jnjLaSalesOrgDTOList to set
	 */
	public void setJnjLaSalesOrgDTOList(final List<JnJLaSalesOrgDTO> jnjLaSalesOrgDTOList)
	{
		this.jnjLaSalesOrgDTOList = new ArrayList<>(jnjLaSalesOrgDTOList);
	}

	/**
	 * @return the customerFreightType
	 */
	public String getCustomerFreightType() {
		return customerFreightType;
	}

	/**
	 * @param customerFreightType
	 *           the customerFreightType to set
	 */
	public void setCustomerFreightType(String customerFreightType) {
		this.customerFreightType = customerFreightType;
	}

	@Override
	public String toString()
	{

		return getAccountType() + " | " + getCity() + " | " + getRegion() + " | " + getPostalCode() + " | " + getDistrict() + " | "
				+ getCountry() + " | " + getCustomerNumer() + " | " + getCustomerType() + " | " + getIndustryCode1() + " | "
				+ getIndustryCode2() + " | " + getName1() + " | " + getName2() + " | " + getLastUpdateDate() + "\n\n";
	}
}
