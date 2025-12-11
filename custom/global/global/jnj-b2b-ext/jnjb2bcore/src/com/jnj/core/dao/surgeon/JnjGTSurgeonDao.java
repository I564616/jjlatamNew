/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.dao.surgeon;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Collection;

import com.jnj.core.model.JnjGTSurgeonModel;


// TODO: Auto-generated Javadoc
/**
 * The Interface JnjGTSurgeonDao.
 *
 * @author sakshi.kashiva
 */
public interface JnjGTSurgeonDao
{
	/**
	 * Returns the jnj surgeon model by id.
	 *
	 * @param id
	 *           the id
	 * @return the jnj surgeon model by id
	 */
	public JnjGTSurgeonModel getJnjSurgeonModelById(String id);

	/**
	 * Retrieves all <code>JnjGTSurgeonModel</code> available in the system.
	 *
	 * @return Collection<JnjGTSurgeonModel>
	 */
	public Collection<JnjGTSurgeonModel> getAllSurgeonRecords();

	/**
	 * Gets the surgeon records.
	 *
	 * @param pageableData
	 *           the pageable data
	 * @param searchPattern
	 *           the search pattern
	 * @return the surgeon records
	 */
	public SearchPageData<JnjGTSurgeonModel> getSurgeonRecords(final PageableData pageableData, final String searchPattern);
}
