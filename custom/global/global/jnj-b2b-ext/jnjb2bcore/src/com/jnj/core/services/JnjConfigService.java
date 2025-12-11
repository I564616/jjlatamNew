/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import java.util.List;
import java.util.Map;

import com.jnj.core.model.JnjConfigModel;


/**
 * The JnjConfigService interface invoke the JnjConfigDao by passes the requested id to method of DAO.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjConfigService
{
	/**
	 * The getConfigValueById method passes the id to the dao class and return the value string which is taken out from
	 * the Config Model object.
	 * 
	 * @param id
	 * @return string
	 */

	public String getConfigValueById(final String id);

	/**
	 * The getConfigValueById method passes the id to the dao class and return the list of string which contains the
	 * different values corresponding to the requested id.
	 * 
	 * @param id
	 * @param separator
	 * @return List of String
	 */
	public List<String> getConfigValuesById(final String id, final String seperator);

	/**
	 * This method is used to save values to the congif. entries w.r.t the key.
	 * 
	 * @param id
	 * @param value
	 */
	void saveConfigValues(String key, String value);

	/**
	 * This method fetches the values from the CONFIG table where the id begins with the particular pattern passed.
	 * 
	 * @param idLike
	 * @return values
	 */
	public List<String> getConfigValuesWhereIdLike(String idLike);

	/**
	 * Gets the config models by id and key.
	 * 
	 * @param id
	 *           the id
	 * @param key
	 *           the key
	 * @return the config models by id and key
	 */
	public List<JnjConfigModel> getConfigModelsByIdAndKey(final String id, final String key);

	/**
	 * Gets map for drop down population
	 * 
	 * @param id
	 * @return
	 */
	Map<String, String> getDropdownValuesInMap(String id);
}
