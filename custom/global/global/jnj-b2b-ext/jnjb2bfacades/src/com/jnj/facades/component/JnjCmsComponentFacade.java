/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.component;

import com.jnj.core.model.JnjLinkComponentModel;
import com.jnj.exceptions.BusinessException;



/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjCmsComponentFacade
{

	/**
	 * Gets the jnj link component for the particular ID.
	 * 
	 * @param componentId
	 *           the component id
	 * @return the jnj link component
	 * @throws BusinessException
	 *            the business exception
	 */
	public JnjLinkComponentModel getJnjLinkComponent(final String componentId) throws BusinessException;
}
