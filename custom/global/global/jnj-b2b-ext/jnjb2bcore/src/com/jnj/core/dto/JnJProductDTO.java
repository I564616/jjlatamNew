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


public class JnJProductDTO
{	
	private String catalogId;
	
	private String code;
	private String status;
	private String typeCode;
	private String unit;
	private String baseUnit;
	private String originCountry;
	private String sector;
	private String actionCode;
	private String franchise;
	private String ean;
	private String category;
	private List<JnjUomDTO> jnJUomDTO;
	private List<JnjProductDetailsDTO> jnJProductDetailsDTO;
	private List<JnJProductDescriptionDTO> jnJProductDescriptionDTO;
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
	 * @return the actionCode
	 */
	public String getActionCode()
	{
		return actionCode;
	}

	/**
	 * @param actionCode
	 *           the actionCode to set
	 */
	public void setActionCode(final String actionCode)
	{
		this.actionCode = actionCode;
	}

	/**
	 * @return the sector
	 */
	public String getSector()
	{
		return sector;
	}

	/**
	 * @param sector
	 *           the sector to set
	 */
	public void setSector(final String sector)
	{
		this.sector = sector;
	}

	/**
	 * @return the originCountry
	 */
	public String getOriginCountry()
	{
		return originCountry;
	}

	/**
	 * @param originCountry
	 *           the originCountry to set
	 */
	public void setOriginCountry(final String originCountry)
	{
		this.originCountry = originCountry;
	}

	/**
	 * @return the baseUnit
	 */
	public String getBaseUnit()
	{
		return baseUnit;
	}

	/**
	 * @param baseUnit
	 *           the baseUnit to set
	 */
	public void setBaseUnit(final String baseUnit)
	{
		this.baseUnit = baseUnit;
	}

	/**
	 * @return the category
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * @param category
	 *           the category to set
	 */
	public void setCategory(final String category)
	{
		this.category = category;
	}

	/**
	 * @return the jnJProductDescriptionDTO
	 */
	public List<JnJProductDescriptionDTO> getJnJProductDescriptionDTO()
	{
		return jnJProductDescriptionDTO;
	}

	/**
	 * @param jnJProductDescriptionDTO
	 *           the jnJProductDescriptionDTO to set
	 */
	public void setJnJProductDescriptionDTO(final List<JnJProductDescriptionDTO> jnJProductDescriptionDTO)
	{
		this.jnJProductDescriptionDTO = jnJProductDescriptionDTO;
	}

	/**
	 * @return the jnJUomDTO
	 */
	public List<JnjUomDTO> getJnJUomDTO()
	{
		return jnJUomDTO;
	}

	/**
	 * @return the jnJProductDetailsDTO
	 */
	public List<JnjProductDetailsDTO> getJnJProductDetailsDTO()
	{
		return jnJProductDetailsDTO;
	}

	/**
	 * @param jnJProductDetailsDTO
	 *           the jnJProductDetailsDTO to set
	 */
	public void setJnJProductDetailsDTO(final List<JnjProductDetailsDTO> jnJProductDetailsDTO)
	{
		this.jnJProductDetailsDTO = jnJProductDetailsDTO;
	}

	/**
	 * @param jnJUomDTO
	 *           the jnJUomDTO to set
	 */
	public void setJnJUomDTO(final List<JnjUomDTO> jnJUomDTO)
	{
		this.jnJUomDTO = jnJUomDTO;
	}

	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * @param code
	 *           the code to set
	 */
	public void setCode(final String code)
	{
		this.code = code;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final String status)
	{
		this.status = status;
	}

	/**
	 * @return the typeCode
	 */
	public String getTypeCode()
	{
		return typeCode;
	}

	/**
	 * @param typeCode
	 *           the typeCode to set
	 */
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}

	/**
	 * @return the unit
	 */
	public String getUnit()
	{
		return unit;
	}

	/**
	 * @param unit
	 *           the unit to set
	 */
	public void setUnit(final String unit)
	{
		this.unit = unit;
	}

	/**
	 * @return the franchise
	 */
	public String getFranchise()
	{
		return franchise;
	}

	/**
	 * @param franchise
	 *           the franchise to set
	 */
	public void setFranchise(final String franchise)
	{
		this.franchise = franchise;
	}

	/**
	 * @return the ean
	 */
	public String getEan()
	{
		return ean;
	}

	/**
	 * @param ean
	 *           the ean to set
	 */
	public void setEan(final String ean)
	{
		this.ean = ean;
	}



}
