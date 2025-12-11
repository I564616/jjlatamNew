/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.masterstore.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Global class for all Jnjlamasterstore constants. You can add global constants for your extension into this class.
 */
public final class JnjlamasterstoreConstants extends GeneratedJnjlamasterstoreConstants
{
	public static final String EXTENSIONNAME = "jnjlamasterstore";

	public static final String IMPORT_SITE = "/jnjlamasterstore/import/stores/jnj/site_%s.impex";
	public static final String IMPORT_SOLR_BY_COUNTRY = "/jnjlamasterstore/import/stores/solr/solr_%s.impex";

	public static final String SYNC_MASTER_CONT_STG_TO_COUNTRY_STG = "Sync-MasterContentStaged-%sContentStaged";
	public static final String SYNC_COUNTRY_CONT_STG_TO_ONLINE = "Sync-%1$sContentStaged-%1$sContentOnline";
	public static final String SYNC_MASTER_PROD_STG_TO_COUNTRY_STG = "Sync-MasterStaged-%sStaged";
	public static final String SYNC_COUNTRY_PROD_STG_TO_ONLINE = "Sync-%1$sStaged->%1$sOnline";

	public static final String DEPLOYMENT_ENV = "deployment.env";
	public static final String LOCAL = "local";
	public static final String COUNTRIES = "COUNTRIES";
	public static final String LOAD_PRODUCT = "LOAD_PRODUCT";
	public static final String LOAD_CONTENT = "LOAD_CONTENT";
	public static final String LOAD_PRODUCT_LABEL = "Load Product Catalog?";
	public static final String LOAD_CONTENT_LABEL = "Load Content Catalog?";
	public static final String COUNTRIES_LABEL = "Which stores should be created?";

	private static final Map<String, String> COUNTRIES_MAP = new HashMap<>();
	static {
		COUNTRIES_MAP.put("AR", "Argentina");
		COUNTRIES_MAP.put("BR", "Brazil");
		COUNTRIES_MAP.put("CENCA", "Cenca");
		COUNTRIES_MAP.put("CL", "Chile");
		COUNTRIES_MAP.put("CO", "Colombia");
		COUNTRIES_MAP.put("EC", "Ecuador");
		COUNTRIES_MAP.put("MX", "Mexico");
		COUNTRIES_MAP.put("PE", "Peru");
		COUNTRIES_MAP.put("PR", "Puerto");
		COUNTRIES_MAP.put("UY", "Uruguay");
	}

	public static final Map<String, String> MAP_OF_COUNTRIES_ISO = Collections.unmodifiableMap(COUNTRIES_MAP);

	private JnjlamasterstoreConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
