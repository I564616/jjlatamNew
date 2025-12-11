/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.data;

import java.util.List;
import java.util.Map;


public class JnjPCMGTProductDataResponse
{
	protected String code;
	protected String relatedProductCodes;
	protected String submissionItemNumber;
	protected String copyExpirationDate;
	protected String regions;
	protected String brands;
	protected List<String> countries;
	protected List<JnjPCMGTProductDataLocalAttributes> aboutBrand;
	protected List<JnjPCMGTProductDataLocalAttributes> longDescription;
	protected List<JnjPCMGTProductDataLocalAttributes> shortOverview;
	protected Map<String, String> productDocumentsList;
	protected List<JnjGTClassificationData> classificationData;

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
	 * @return the relatedProductCodes
	 */
	public String getRelatedProductCodes()
	{
		return relatedProductCodes;
	}

	/**
	 * @param relatedProductCodes
	 *           the relatedProductCodes to set
	 */
	public void setRelatedProductCodes(final String relatedProductCodes)
	{
		this.relatedProductCodes = relatedProductCodes;
	}

	/**
	 * @return the submissionItemNumber
	 */
	public String getSubmissionItemNumber()
	{
		return submissionItemNumber;
	}

	/**
	 * @param submissionItemNumber
	 *           the submissionItemNumber to set
	 */
	public void setSubmissionItemNumber(final String submissionItemNumber)
	{
		this.submissionItemNumber = submissionItemNumber;
	}

	/**
	 * @return the copyExpirationDate
	 */
	public String getCopyExpirationDate()
	{
		return copyExpirationDate;
	}

	/**
	 * @param copyExpirationDate
	 *           the copyExpirationDate to set
	 */
	public void setCopyExpirationDate(final String copyExpirationDate)
	{
		this.copyExpirationDate = copyExpirationDate;
	}

	/**
	 * @return the regions
	 */
	public String getRegions()
	{
		return regions;
	}

	/**
	 * @param regions
	 *           the regions to set
	 */
	public void setRegions(final String regions)
	{
		this.regions = regions;
	}

	/**
	 * @return the brands
	 */
	public String getbrands()
	{
		return brands;
	}

	/**
	 * @param brands
	 *           the brands to set
	 */
	public void setbrands(final String brands)
	{
		this.brands = brands;
	}

	/**
	 * @return the countries
	 */
	public List<String> getCountries()
	{
		return countries;
	}

	/**
	 * @param countries
	 *           the countries to set
	 */
	public void setCountries(final List<String> countries)
	{
		this.countries = countries;
	}

	/**
	 * @return the aboutBrand
	 */
	public List<JnjPCMGTProductDataLocalAttributes> getAboutBrand()
	{
		return aboutBrand;
	}

	/**
	 * @param aboutBrand
	 *           the aboutBrand to set
	 */
	public void setAboutBrand(final List<JnjPCMGTProductDataLocalAttributes> aboutBrand)
	{
		this.aboutBrand = aboutBrand;
	}

	/**
	 * @return the longDescription
	 */
	public List<JnjPCMGTProductDataLocalAttributes> getLongDescription()
	{
		return longDescription;
	}

	/**
	 * @param longDescription
	 *           the longDescription to set
	 */
	public void setLongDescription(final List<JnjPCMGTProductDataLocalAttributes> longDescription)
	{
		this.longDescription = longDescription;
	}

	/**
	 * @return the shortOverview
	 */
	public List<JnjPCMGTProductDataLocalAttributes> getShortOverview()
	{
		return shortOverview;
	}

	/**
	 * @param shortOverview
	 *           the shortOverview to set
	 */
	public void setShortOverview(final List<JnjPCMGTProductDataLocalAttributes> shortOverview)
	{
		this.shortOverview = shortOverview;
	}

	/**
	 * @return the productDocumentsList
	 */
	public Map<String, String> getProductDocumentsList()
	{
		return productDocumentsList;
	}

	/**
	 * @param productDocumentsList
	 *           the productDocumentsList to set
	 */
	public void setProductDocumentsList(final Map<String, String> productDocumentsList)
	{
		this.productDocumentsList = productDocumentsList;
	}

	/**
	 * @return the classificationData
	 */
	public List<JnjGTClassificationData> getClassificationData()
	{
		return classificationData;
	}

	/**
	 * @param classificationData
	 *           the classificationData to set
	 */
	public void setClassificationData(final List<JnjGTClassificationData> classificationData)
	{
		this.classificationData = classificationData;
	}


}

