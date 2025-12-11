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
package com.jnj.core.services.laudo;
import java.util.List;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjLaudoData;


/**
 *
 */
public interface JnjLaudoService
{
	SearchPageData<JnjLaudoModel> getPagedLaudoDetailsWithMedia(JnjPageableData jnjPageableData) throws BusinessException;

	JnjLaudoModel getLaudoByFileName(String fileName) throws BusinessException;

	boolean toggleLock(JnjLaudoModel jnjLaudoModel, String laudoReadLock, String laudoLockEntry);

	void saveLaudoDetails(JnjLaudoModel jnjLaudoModel) throws BusinessException;

	SearchPageData<JnjLaudoModel> getPagedLaudoDetails(final JnjPageableData jnjPageableData) throws BusinessException;

	void removeLaudoEntry(JnjLaudoModel jnjLaudoModel) throws BusinessException;

	boolean checkFileNameExists(JnjLaudoData jnjLaudoData, CountryModel countryModel);

	JnjLaudoModel getLaudo(final String jnjProductCode, final String laudoNumber) throws BusinessException;
	
	/**
	  * It fetches documents to delete expired document
	  * @param countryCode the countryCode to be used
	  * @throws BusinessException the BusinessException to be thrown
	  * @return List<JnjLaudoModel>
	  * 
	  */
	List<JnjLaudoModel> laudoDetailsExpiredDocument(final String countryCode) throws BusinessException;


}
