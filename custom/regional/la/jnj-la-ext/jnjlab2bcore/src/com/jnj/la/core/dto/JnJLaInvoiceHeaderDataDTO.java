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

import com.jnj.core.dto.JnJInvoiceHeaderDataDTO;


/**
 *
 */
public class JnJLaInvoiceHeaderDataDTO extends JnJInvoiceHeaderDataDTO
{

	private String lastUpdateDate;


	public String getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	public void setLastUpdateDate(final String lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}

}
