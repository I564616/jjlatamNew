/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.media.impl;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.media.JnjMediaService;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjMediaService extends DefaultMediaService implements JnjMediaService
{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjMediaService.class);

	/** The Constant MEDIA_SERVICE. */
	protected static final String MEDIA_SERVICE = "Media Service";

	@Override
	public void saveMedia(final MediaModel mediaModel) throws BusinessException
	{

		final String METHOD_SAVE_MEDIA = "saveMedia()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(MEDIA_SERVICE + Logging.HYPHEN + METHOD_SAVE_MEDIA + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		if (null != mediaModel)
		{
			try
			{
				getModelService().save(mediaModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				LOGGER.error(MEDIA_SERVICE + Logging.HYPHEN + METHOD_SAVE_MEDIA + Logging.HYPHEN
						+ "ModelSavingException occured while saving Media. Initiating Business Expection: " + modelSavingException);
				throw new BusinessException("ModelSavingException occured while saving Media.");
			}
		}
		LOGGER.info(MEDIA_SERVICE + Logging.HYPHEN + METHOD_SAVE_MEDIA + Logging.HYPHEN + "Media successfully saved to Hybris.");

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(MEDIA_SERVICE + Logging.HYPHEN + METHOD_SAVE_MEDIA + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

	}

	@Override
	public void removeMedia(final MediaModel mediaModel) throws BusinessException
	{
		final String METHOD_REMOVE_MEDIA = "removeMedia()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(MEDIA_SERVICE + Logging.HYPHEN + METHOD_REMOVE_MEDIA + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		if (null != mediaModel)
		{
			try
			{
				getModelService().remove(mediaModel);
			}
			catch (final ModelRemovalException modelRemovalException)
			{
				LOGGER.error(MEDIA_SERVICE + Logging.HYPHEN + METHOD_REMOVE_MEDIA + Logging.HYPHEN
						+ "ModelRemovalException occured while removing Media. Initiating Business Expection: " + modelRemovalException);
				throw new BusinessException("ModelRemovalException occured while removing Media. Initiating Business Expection: ");
			}

			LOGGER.info(MEDIA_SERVICE + Logging.HYPHEN + METHOD_REMOVE_MEDIA + Logging.HYPHEN
					+ "Media successfully removed from Hybris.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(MEDIA_SERVICE + Logging.HYPHEN + METHOD_REMOVE_MEDIA + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}

}
