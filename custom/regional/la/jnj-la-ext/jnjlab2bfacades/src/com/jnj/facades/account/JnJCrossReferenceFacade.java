/*

 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.account;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import com.jnj.core.dto.JnjCrossReferenceTableDTO;





/**
 * This class contains the abstract methods for getting the Cross Reference Table data and also for specific search
 * feature.
 *
 * @author skant3
 *
 */
public interface JnJCrossReferenceFacade
{
	/**
	 *
	 * This method invokes the service class in order to obtain the data for all records of Cross Reference Table
	 *
	 * @return List<JnjCrossReferenceTableDTO>
	 */
	public SearchPageData<JnjCrossReferenceTableDTO> getCrossReferenceTable(final PageableData pageableData);

	/**
	 * This method invokes the service class in order to obtain the data for the record searched by the user on the front
	 * end.
	 *
	 * @param string
	 * @param searchTerm
	 * @return List<JnjCrossReferenceTableDTO>
	 */
	public SearchPageData<JnjCrossReferenceTableDTO> getCrossReferenceSearch(String searchTerm, final PageableData pageableData);

}
