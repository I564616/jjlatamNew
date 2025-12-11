/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.media.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.media.impl.DefaultMediaService;
import de.hybris.platform.util.Config;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.media.JnjLatamMediaService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.services.CMSSiteService;


/**
 * Implementation of Media Service.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLatamMediaServiceImpl extends DefaultMediaService implements JnjLatamMediaService
{

	/** The Constant MEDIA_SERVICE. */
	private static final String MEDIA_SERVICE = "Media Service";

	@Autowired
	private MediaService mediaService;

	@Autowired
	private CMSSiteService cMSSiteService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Override
	public void saveMedia(final MediaModel mediaModel) throws BusinessException
	{

		final String METHOD_SAVE_MEDIA = "saveMedia()";

		if (null != mediaModel)
		{
			try
			{
				getModelService().save(mediaModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				JnjGTCoreUtil.logErrorMessage(MEDIA_SERVICE, METHOD_SAVE_MEDIA,
						"ModelSavingException occured while saving Media. Initiating Business Expection: ", modelSavingException,
						JnjLatamMediaServiceImpl.class);
				throw new BusinessException("ModelSavingException occured while saving Media.");
			}
		}
		JnjGTCoreUtil.logDebugMessage(MEDIA_SERVICE, METHOD_SAVE_MEDIA, "Media successfully saved to Hybris.",
				JnjLatamMediaServiceImpl.class);

	}

	@Override
	public void removeMedia(final MediaModel mediaModel) throws BusinessException
	{
		final String METHOD_REMOVE_MEDIA = "removeMedia()";

		if (null != mediaModel)
		{
			try
			{
				getModelService().remove(mediaModel);
			}
			catch (final ModelRemovalException modelRemovalException)
			{
				JnjGTCoreUtil.logDebugMessage(MEDIA_SERVICE, METHOD_REMOVE_MEDIA,
						"ModelRemovalException occured while removing Media. Initiating Business Expection: " + modelRemovalException,
						JnjLatamMediaServiceImpl.class);
				throw new BusinessException("ModelRemovalException occured while removing Media. Initiating Business Expection: ");
			}
			JnjGTCoreUtil.logDebugMessage(MEDIA_SERVICE, METHOD_REMOVE_MEDIA, "Media successfully removed from Hybris.",
					JnjLatamMediaServiceImpl.class);
		}


	}

	@Override
	public String getMediaLocation(final String mediaUid)
	{


		CatalogVersionModel currentCatalog = null;
		if (cMSSiteService != null && cMSSiteService.getCurrentSite() != null)
		{
			currentCatalog = cMSSiteService.getCurrentSite().getContentCatalogs().get(0).getActiveCatalogVersion();
		}
		else
		{
			currentCatalog = catalogVersionService.getCatalogVersion(Jnjlab2bcoreConstants.MASTER_CONTENT_CATALOG_ID,
					Jnjb2bCoreConstants.CATALOG_VERSION_STAGED);
		}
		final MediaModel mediaModel = mediaService.getMedia(currentCatalog, mediaUid);
		final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		final String mediaPath = mediaDirBase + File.separator
				+ Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_ROOT_FOLDER_KEY) + mediaModel.getLocation();


		return mediaPath;
	}

}
