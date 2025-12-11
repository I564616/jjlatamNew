/* * This code contains copyright information which is the proprietary property
 * of Accenture Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of Accenture Companies Ltd.
 * Copyright (C) Accenture Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.dao;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.accenture.model.MessageItemModel;


/**
 * The Interface MessageDao.
 * 
 * @author Accenture
 * 
 * @version 1.0
 * 
 */
public interface MessageDao extends Dao
{

	/**
	 * Find CO messages from the session catalog version.
	 * 
	 * @param messageCode
	 *           the message code
	 * @param catalogVersions
	 *           the catalog versions
	 * @return the list
	 */
	List<MessageItemModel> findJNJMessages(final String messageCode, Collection<CatalogVersionModel> catalogVersions);

	/**
	 * Find all CO messages from the session catalog version.
	 * 
	 * @param catalogVersions
	 *           the catalog versions
	 * @return the list
	 */
	List<MessageItemModel> findAllJNJMessages(Set<CatalogVersionModel> catalogVersions);
}
