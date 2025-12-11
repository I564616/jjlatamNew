/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */


package com.jnj.core.dao;

import java.util.List;

import com.jnj.core.model.JnjConfigModel;


/**
 * The JnjConfigDao interface interacts with the hybris data and retrieves the result set on the basis of requested id.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjConfigDao
{
	/**
	 * The getConfigValueById method retrieves the data from the hybris data on the basis of the id pass in dynamic
	 * query.
	 * 
	 * Note: Please use to avoid this method in EPIC except for the package jnjGTb2boutbound service. If anyone wants to
	 * use then please make sure there is only one id in configuration impex.
	 * 
	 * @param id
	 * @return jnjConfigModel
	 */
	public JnjConfigModel getConfigValueById(final String id);

	/**
	 * This method retrieves data from hybris on the basis of the id pattern passed in the dynamic query.
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
}
