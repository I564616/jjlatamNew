/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.media;

import de.hybris.platform.core.model.media.MediaModel;

import com.jnj.exceptions.BusinessException;


/**
 * Interface for Media Serivce.
 *
 * @author Accenture
 * @version 1.0
 */
public interface JnjLatamMediaService
{

	/**
	 * Saves the media.
	 *
	 * @param mediaModel
	 *           the media model
	 * @throws BusinessException
	 *            the business exception
	 */
	void saveMedia(final MediaModel mediaModel) throws BusinessException;

	/**
	 * Removes the media.
	 *
	 * @param mediaModel
	 *           the media model
	 * @throws BusinessException
	 *            the business exception
	 */
	void removeMedia(MediaModel mediaModel) throws BusinessException;

	/**
	 * Gets the media location for the given Media UID.
	 *
	 * @param mediaUid
	 *           the media uid
	 * @return the media location
	 */
	String getMediaLocation(String mediaUid);

}
