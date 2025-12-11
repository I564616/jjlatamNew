/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.news;

import java.util.List;

import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjNewsReleasesData;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjNewsReleasesFacade
{

	/**
	 * Gets the news components other than the given JnjNewsBannerComponentModel sorted on NewsPublishDate.
	 * 
	 * @param jnjNewsBannerComponentModel
	 *           the jnj news banner component model
	 * @return the news banners
	 * @throws BusinessException
	 */
	public List<JnjNewsReleasesData> getNewsComponents(final JnjNewsBannerComponentModel jnjNewsBannerComponentModel)
			throws BusinessException;

}
