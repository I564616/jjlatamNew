/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.component;

import com.jnj.core.model.JnjLinkComponentModel;
import com.jnj.exceptions.BusinessException;


/**
 * Interface for the Jnj CMS Component Service.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCmsComponentService
{

	/**
	 * Gets the jnj link component for id.
	 * 
	 * @param componentId
	 *           the component id
	 * @return the jnj link component for id
	 * @throws BusinessException
	 *            the business exception
	 */
	public JnjLinkComponentModel getJnjLinkComponentForId(final String componentId) throws BusinessException;


}
