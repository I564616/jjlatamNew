/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.dao.news;

import java.util.List;

import com.jnj.core.enums.BusinessCenter;
import com.jnj.core.model.JnjNewsBannerComponentModel;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjNewsReleasesDao
{


	/**
	 * Gets the news components for the given BusinessCenter sorted on NewsPublishDate.
	 * 
	 * @param businessCenter
	 *           the business center
	 * @return the news components
	 * @throws BusinessException
	 */
	public List<JnjNewsBannerComponentModel> getNewsComponents(final BusinessCenter businessCenter) throws BusinessException;
}
