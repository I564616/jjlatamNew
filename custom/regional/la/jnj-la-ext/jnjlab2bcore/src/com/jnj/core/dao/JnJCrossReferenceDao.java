/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.la.core.model.JnjIntLoadTranslationModel;
import com.jnj.la.core.model.LoadTranslationModel;


/**
 * This class contains the abstract methods for getting the Cross Reference Table data and also for specific search
 * feature.
 *
 * @author mpanda3
 */
public interface JnJCrossReferenceDao
{
	/**
	 *
	 * This method invokes the service class in order to obtain the data for all records of Cross Reference Table
	 *
	 * @return List<LoadTranslationModel>
	 */
	public SearchPageData<LoadTranslationModel> getCrossReferenceTable(final PageableData pageableData);

	/**
	 * This method invokes the service class in order to obtain the data for the record searched by the user on the front
	 * end.
	 *
	 * @param jnjIdflag
	 * @param searchTerm
	 * @param pageableData
	 * @return List<LoadTranslationModel>
	 */
	public SearchPageData<LoadTranslationModel> getCrossReferenceSearch(String searchTerm, PageableData pageableData);

	/**
	 * @param recordStatus
	 * @return List<JnjIntLoadTranslationModel>
	 */
	public List<JnjIntLoadTranslationModel> getJnjIntLoadTranslatiomModelForRemove(RecordStatus recordStatus);

}
