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
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjMediaService
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

}
