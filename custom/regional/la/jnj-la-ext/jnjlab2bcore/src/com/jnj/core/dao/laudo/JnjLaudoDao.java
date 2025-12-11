/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.dao.laudo;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import java.util.List;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.exceptions.BusinessException;


/**
 *
 */
public interface JnjLaudoDao
{
	SearchPageData<JnjLaudoModel> findPagedLaudoDetails(final JnjPageableData jnjPageableData, final boolean withMedia)
			throws BusinessException;

	JnjLaudoModel findLaudoByFileName(String fileName) throws BusinessException;

	JnjLaudoModel getLaudoFromHybris(String jnjProductCode, String laudoNumber) throws BusinessException;
	List<JnjLaudoModel> findLaudoDetailsExpiredDocument(final String countryCode) throws BusinessException;
}
