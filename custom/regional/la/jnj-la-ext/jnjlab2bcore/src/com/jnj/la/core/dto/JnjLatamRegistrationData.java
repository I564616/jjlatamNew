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

import com.jnj.core.dto.JnjRegistrationData;


/**
 *
 */
public class JnjLatamRegistrationData extends JnjRegistrationData
{
	private Boolean commercialUserFlag;
	
	private String commercialUserSector;
	
	private JnjLatamCompanyInfoData companyInfoData;	
	
	
	public Boolean getCommercialUserFlag() {
		return commercialUserFlag;
	}

	public void setCommercialUserFlag(Boolean commercialUserFlag) {
		this.commercialUserFlag = commercialUserFlag;
	}

	public String getCommercialUserSector() {
		return commercialUserSector;
	}

	public void setCommercialUserSector(String commercialUserSector) {
		this.commercialUserSector = commercialUserSector;
	}

	/**
	 * @return the companyInfoData
	 */
	@Override
	public JnjLatamCompanyInfoData getCompanyInfoData()
	{
		return companyInfoData;
	}

	/**
	 * @param companyInfoData
	 *           the companyInfoData to set
	 */
	public void setCompanyInfoData(final JnjLatamCompanyInfoData companyInfoData)
	{
		this.companyInfoData = companyInfoData;
	}


}
