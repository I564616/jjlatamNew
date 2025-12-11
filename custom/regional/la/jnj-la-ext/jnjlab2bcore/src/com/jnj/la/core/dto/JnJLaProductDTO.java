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

import com.jnj.core.dto.JnJProductDTO;
import com.jnj.core.dto.JnjUomConversionDTO;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class JnJLaProductDTO extends JnJProductDTO
{
	private String catalogId;
	private String materialType;
	private List<JnjUomConversionDTO> JnjUomConversionDTO;
	private List<JnjProductSalesOrgDTO> jnjProductSalesOrgDto;
	private String portalVisibility;
	private String lastUpdateDate;
	private String division;

	/**
	 * @return the materialType
	 */
	public String getMaterialType()
	{
		return materialType;
	}

	/**
	 * @param materialType
	 *           the materialType to set
	 */
	public void setMaterialType(final String materialType)
	{
		this.materialType = materialType;
	}

	public List<JnjUomConversionDTO> getJnjUomConversionDTO() {
	    if (JnjUomConversionDTO != null) {
		    return new ArrayList<>(JnjUomConversionDTO);
        }
        return new ArrayList<>();
	}

	/**
	 * @param jnjUomConversionDTO
	 *           the jnjUomConversionDTO to set
	 */
	public void setJnjUomConversionDTO(final List<JnjUomConversionDTO> jnjUomConversionDTO)
	{
		JnjUomConversionDTO = new ArrayList<>(jnjUomConversionDTO);
	}

	/**
	 * @return the jnjProductSalesOrgDto
	 */
	public List<JnjProductSalesOrgDTO> getJnjProductSalesOrgDto()
	{
		if (jnjProductSalesOrgDto != null) {
			return new ArrayList<>(jnjProductSalesOrgDto);
		}
		return new ArrayList<>();
	}

	/**
	 * @param jnjProductSalesOrgDto
	 *           the jnjProductSalesOrgDto to set
	 */
	public void setJnjProductSalesOrgDto(final List<JnjProductSalesOrgDTO> jnjProductSalesOrgDto)
	{
		this.jnjProductSalesOrgDto = new ArrayList<>(jnjProductSalesOrgDto);
	}

	/**
	 * @return the portalVisibility
	 */
	public String getPortalVisibility()
	{
		return portalVisibility;
	}

	/**
	 * @param portalVisibility
	 *           the portalVisibility to set
	 */
	public void setPortalVisibility(final String portalVisibility)
	{
		this.portalVisibility = portalVisibility;
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
	 * @return the catalogId
	 */
	public String getCatalogId()
	{
		return catalogId;
	}

	/**
	 * @param catalogId
	 *           the catalogId to set
	 */
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}

	public String getDivision()
	{
		return division;
	}

	public void setDivision(final String division)
	{
		this.division = division;
	}

}
