/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.laudo;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjLaudoData;
import com.jnj.facades.data.JnjLaudoFileStatusData;


/**
 * The interface class for operations related to Laudo.
 *
 * @author plahiri1
 * @version 1.0
 */
public interface JnjLaudoFacade
{
	SearchPageData<JnjLaudoData> getLaudoDetailsWithMedia(JnjPageableData jnjPageableData) throws BusinessException;

	/**
	 *
	 */
	SearchPageData<JnjLaudoData> convertPageData(SearchPageData<JnjLaudoModel> source,
			Converter<JnjLaudoModel, JnjLaudoData> converter);

	Map<File, String> getLaudoFile(String laudoFileName) throws BusinessException, IOException;

	SearchPageData<JnjLaudoData> getLaudoDetails(final JnjPageableData jnjPageableData) throws BusinessException;

	List<JnjLaudoFileStatusData> deleteLaudos(final List<String> fileNameList) throws BusinessException;

	File convertMultipartFile(MultipartFile multipartFile) throws BusinessException;

	JnjLaudoFileStatusData processSingleLaudoEntry(final JnjLaudoData jnjLaudoData);

	void deleteLaudoTempFile(File file);

	List<JnjLaudoFileStatusData> processUploadedFiles(CommonsMultipartFile[] files);
	
	/**
	  * call the service method to get laudo to delete after expiration.
	  * @param countryCode the countryCode is the code for country
	  * @throws BusinessException the BusinessException is the exception which can be thrown
	  * @return List<JnjLaudoModel>
	  */
	List<JnjLaudoModel> laudoDetailsExpiredDelete(final String countryCode) throws BusinessException;


}
