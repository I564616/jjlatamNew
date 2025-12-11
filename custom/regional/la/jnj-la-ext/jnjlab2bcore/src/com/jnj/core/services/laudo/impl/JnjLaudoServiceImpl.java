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
package com.jnj.core.services.laudo.impl;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.laudo.JnjLaudoDao;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.core.services.laudo.JnjLaudoService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjLaudoData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Laudo;


/**
 *
 */
public class JnjLaudoServiceImpl implements JnjLaudoService
{

	@Autowired
	private JnjLaudoDao jnjLaudoDao;

	@Autowired
	private ModelService modelService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Override
	public SearchPageData<JnjLaudoModel> getPagedLaudoDetailsWithMedia(final JnjPageableData jnjPageableData)
			throws BusinessException
	{
		final String METHOD_GET_PAGED_LAUDO_DETAILS = "Service - getPagedLaudoDetailsWithMedia()";


		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_PAGED_LAUDO_DETAILS, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);

		final SearchPageData<JnjLaudoModel> pagedDataForLaudo = jnjLaudoDao.findPagedLaudoDetails(jnjPageableData, true);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_PAGED_LAUDO_DETAILS, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);

		return pagedDataForLaudo;
	}
	
	@Override
	public List<JnjLaudoModel> laudoDetailsExpiredDocument(final String countryCode) throws BusinessException{
		return  jnjLaudoDao.findLaudoDetailsExpiredDocument(countryCode);
    }

	@Override
	public JnjLaudoModel getLaudoByFileName(final String fileName) throws BusinessException
	{
		final String METHOD_GET_LAUDO_BY_FILE_NAME = "Service - getLaudoByFileName()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_BY_FILE_NAME, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);

		JnjLaudoModel jnjLaudoModel = null;
		if (StringUtils.isNotEmpty(fileName))
		{
			jnjLaudoModel = jnjLaudoDao.findLaudoByFileName(fileName);
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO_BY_FILE_NAME, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);

		return jnjLaudoModel;
	}

	@Override
	public boolean toggleLock(final JnjLaudoModel jnjLaudoModel, final String laudoLockType, final String laudoLockEntry)
	{
		final String METHOD_TOGGLE_LOCK = "Service - toggleLock()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);
		boolean lockStatus = false;

		if (StringUtils.equalsIgnoreCase(laudoLockType, Laudo.LAUDO_READ_LOCK))
		{
			if (StringUtils.equalsIgnoreCase(laudoLockEntry, Laudo.LAUDO_LOCK_ENTRY))
			{
				jnjLaudoModel.setReadStatus(Boolean.TRUE);
				try
				{
					saveLaudoDetails(jnjLaudoModel);
					lockStatus = true;
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK,
							"BusinessException occured while Toggling Entry Lock. ", businessException, JnjLaudoServiceImpl.class);

				}
			}
			else if (StringUtils.equalsIgnoreCase(laudoLockEntry, Laudo.LAUDO_UNLOCK_ENTRY))
			{
				jnjLaudoModel.setReadStatus(Boolean.FALSE);
				try
				{
					saveLaudoDetails(jnjLaudoModel);
					lockStatus = true;
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK,
							"BusinessException occured while Toggling Entry Lock. ", businessException, JnjLaudoServiceImpl.class);

				}
			}
		}
		else if (StringUtils.equalsIgnoreCase(laudoLockType, Laudo.LAUDO_WRITE_LOCK))
		{
			if (StringUtils.equalsIgnoreCase(laudoLockEntry, Laudo.LAUDO_LOCK_ENTRY))
			{
				jnjLaudoModel.setWriteStatus(Boolean.TRUE);
				try
				{
					saveLaudoDetails(jnjLaudoModel);
					lockStatus = true;
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK,
							"BusinessException occured while Toggling Entry Lock. ", businessException, JnjLaudoServiceImpl.class);
				}
			}
			else if (StringUtils.equalsIgnoreCase(laudoLockEntry, Laudo.LAUDO_UNLOCK_ENTRY))
			{
				jnjLaudoModel.setWriteStatus(Boolean.FALSE);
				try
				{
					saveLaudoDetails(jnjLaudoModel);
					lockStatus = true;
				}
				catch (final BusinessException businessException)
				{
					JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK,
							"BusinessException occured while Toggling Entry Lock. ", businessException, JnjLaudoServiceImpl.class);
				}
			}
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_TOGGLE_LOCK, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);

		return lockStatus;
	}

	@Override
	public void saveLaudoDetails(final JnjLaudoModel jnjLaudoModel) throws BusinessException
	{
		final String METHOD_SAVE_LAUDO_DETAILS = "Service - saveLaudoDetails()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SAVE_LAUDO_DETAILS, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);
		if (null != jnjLaudoModel)
		{
			try
			{
				modelService.saveAll(jnjLaudoModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_SAVE_LAUDO_DETAILS,
						"ModelSavingException occured while saving Laudo Model with Lot No.: " + jnjLaudoModel.getLaudoNumber()
								+ " and JnjProductID: " + jnjLaudoModel.getProductCode() + " and file name: "+jnjLaudoModel.getFileName()+" .Initiating Business Expection: ",
						modelSavingException, JnjLaudoServiceImpl.class);
				throw new BusinessException("ModelSavingException occured while saving Laudo Model.",
						Laudo.LAUDO_GENERAL_EXCEPTION_CODE, Severity.BUSINESS_EXCEPTION);
			}

			JnjGTCoreUtil.logInfoMessage(
					Logging.LAUDO, METHOD_SAVE_LAUDO_DETAILS, "Laudo Model with Lot No.: " + jnjLaudoModel.getLaudoNumber()
							+ " and JnjProductID: " + jnjLaudoModel.getProductCode() + " saved successfully to Hybris. ",
					JnjLaudoServiceImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_SAVE_LAUDO_DETAILS, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);
	}

	@Override
	public SearchPageData<JnjLaudoModel> getPagedLaudoDetails(final JnjPageableData jnjPageableData) throws BusinessException
	{

		final String METHOD_GET_PAGED_LAUDO_DETAILS = "Service - getPagedLaudoDetails()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_PAGED_LAUDO_DETAILS, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);
		final SearchPageData<JnjLaudoModel> pagedDataForLaudo = jnjLaudoDao.findPagedLaudoDetails(jnjPageableData, false);
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_PAGED_LAUDO_DETAILS, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);
		return pagedDataForLaudo;

	}

	@Override
	public void removeLaudoEntry(final JnjLaudoModel jnjLaudoModel) throws BusinessException
	{
		final String METHOD_REMOVE_LAUDO_ENTRY = "Service - removeLaudoEntry()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_REMOVE_LAUDO_ENTRY, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);
		if (null != jnjLaudoModel)
		{
			try
			{
				modelService.remove(jnjLaudoModel);
			}
			catch (final ModelRemovalException modelRemovalException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LAUDO, METHOD_REMOVE_LAUDO_ENTRY,
						"ModelRemovalException occured while REMOVING Laudo entry.", modelRemovalException, JnjLaudoServiceImpl.class);
			}
			JnjGTCoreUtil.logDebugMessage(Logging.LAUDO, METHOD_REMOVE_LAUDO_ENTRY,
					"JnjLaudoModel successfully removed from Hybris.", JnjLaudoServiceImpl.class);
		}
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_REMOVE_LAUDO_ENTRY, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);
	}

	@Override
	public boolean checkFileNameExists(final JnjLaudoData jnjLaudoData, final CountryModel countryModel)
	{
		final String METHOD_NAME = "checkFileNameExists ()";
		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, Logging.BEGIN_OF_METHOD, JnjLaudoServiceImpl.class);
		boolean status = false;
		List<JnjLaudoModel> jnjLaudoModelList = null;
		final JnjLaudoModel jnJLaudoModel = new JnjLaudoModel();

		jnJLaudoModel.setCountry(countryModel);
		jnJLaudoModel.setFileName(jnjLaudoData.getPdfFileName().toLowerCase());
		try
		{
			jnjLaudoModelList = flexibleSearchService.getModelsByExample(jnJLaudoModel);
			if (!jnjLaudoModelList.isEmpty())
			{
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
						"This is an Update on exisitng Laudo Model.Returning [true]", JnjLaudoServiceImpl.class);
				status = true;
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
						"JnjLaudoModels not found in Hybris. Returning [false]", JnjLaudoServiceImpl.class);
				status = false;
			}
		}
		catch (ModelNotFoundException | IllegalArgumentException | AmbiguousIdentifierException exception)
		{
			JnjGTCoreUtil.logWarnMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME,
					"JnjLaudoModels not found in Hybris. Returning true ", exception, JnjLaudoServiceImpl.class);
			status = false;
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_NAME, Logging.END_OF_METHOD, JnjLaudoServiceImpl.class);

		return status;

	}

	@Override
	public JnjLaudoModel getLaudo(final String jnjProductCode, final String laudoNumber) throws BusinessException
	{
		final String METHOD_GET_LAUDO = "Service - getLaudo()";

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO, Logging.BEGIN_OF_METHOD,
				JnjLaudoServiceImpl.class);

		final JnjLaudoModel jnjLaudoModel = jnjLaudoDao.getLaudoFromHybris(jnjProductCode, laudoNumber);

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.LAUDO, METHOD_GET_LAUDO, Logging.END_OF_METHOD,
				JnjLaudoServiceImpl.class);

		return jnjLaudoModel;
	}
}


