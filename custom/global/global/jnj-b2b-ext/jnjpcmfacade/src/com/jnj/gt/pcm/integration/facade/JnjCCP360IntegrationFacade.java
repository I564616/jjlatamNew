/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.gt.pcm.integration.facade;


import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.core.model.JnjGTPCMIntegrationCronJobModel;
import com.jnj.gt.pcm.integration.data.JnjPCMGTProductDataResponse;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * This interface handles the generation and upload of CSV files in SMTP server of P360
 *
 */
public interface JnjCCP360IntegrationFacade
{
	/**
	 * This method is used to verify if the CSV file is successfully uploaded.
	 *
	 * @return boolean
	 */
	public boolean uploadProductDetailFile();

	/**
	 * This method is used to connect with P360 API for given parameters for Product Update and returns JSON response
	 *
	 * @param url Input parameters to make API call
	 * @param startDate Input parameters to make API call
	 * @param jobModel Input parameters to make API call
	 * @param regionOrCountryParam Input parameters to make API call
	 * @param currentPageNumber Input parameters to make API call
	 *           
	 *
	 * @return String
	 */
	public String getJsonDataForProduct(final String url, final String startDate, final JnjGTPCMIntegrationCronJobModel jobModel,
			final String regionOrCountryParam, final int currentPageNumber);

	/**
	 * This method is used to connect with P360 API for given parameters for Category update and returns JSON response
	 *
	 * @param url Input parameters to make API call
	 * @param startDate Input parameters to make API call
	 * @param jobModel Input parameters to make API call
	 * @param currentPageNumber Input parameters to make API call
	 * @return String
	 */
	public String getJsonDataForCategory(final String url, final String startDate, final JnjGTPCMIntegrationCronJobModel jobModel,
			final int currentPageNumber);
	
	/**
	 * This method is used to get Json Array from Json Object
	 * 
	 * @param productObject Input parameter for extract json array
	 * @param attributeName Specific attribute to be extracted from json object
	 * @return JSONArray
	 */
	public JSONArray getAttributeJsonArray(final JSONObject productObject, final String attributeName);
	
	/**
	 * This method is used to update product attributes from P360 response
	 * 
	 * @param productCode Input parameter to fetch Product Model 
	 * @param jnjProductModel Input parameter to update product attribute
	 * @param response Input parameter which stores P360 response
	 * @param catalogVersion Input parameter to fetch staged version product from master catalog
	 */
	public void populateProductDetails(String productCode, JnJProductModel jnjProductModel,
			JnjPCMGTProductDataResponse response, CatalogVersionModel catalogVersion);

	/**
	 * This method is used to fetch product model
	 * 
	 * @param code Input parameter which stores Product Code 
	 * @param catalogVersion Input parameter which stores catalogversion 
	 * @return JnJProductModel
	 */
	public JnJProductModel getProductModel(String code, CatalogVersionModel catalogVersion);

}
