/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjModelService;


/**
 * TODO:<Neeraj-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjModelService implements JnjModelService
{

	private static final Logger LOGGER = Logger.getLogger(DefaultJnjModelService.class);

	@Autowired
	ModelService modelService;

	public ModelService getModelService() {
		return modelService;
	}


	@Override
	public boolean save(final Object paramObject) throws ModelSavingException
	{
		boolean success = Boolean.FALSE;
		try
		{
			modelService.save(paramObject);
			success = Boolean.TRUE;
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("Model Saving Exceptin while Saving " + paramObject.getClass() + " ", exception);
		}
		return success;
	}


	@Override
	public boolean saveAll(final Collection<? extends Object> paramCollection) throws ModelSavingException
	{
		boolean success = Boolean.FALSE;
		try
		{
			modelService.saveAll(paramCollection);
			success = Boolean.TRUE;
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("Model Saving Exceptin while call saveAll fron JnjModelServiceImpl", exception);
		}
		return success;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnjModelService#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(final Object paramObject) throws ModelRemovalException
	{
		boolean removed = Boolean.FALSE;
		try
		{
			modelService.remove(paramObject);
			removed = Boolean.TRUE;
		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("ModelRemovalException while call remove fron JnjModelServiceImpl", exception);
		}
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnjModelService#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(final Collection<? extends Object> paramCollection) throws ModelRemovalException
	{
		boolean removed = Boolean.FALSE;
		try
		{
			modelService.removeAll(paramCollection);
			removed = Boolean.TRUE;
		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("ModelRemovalException while call removeAll fron JnjModelServiceImpl", exception);
		}
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnjModelService#remove(de.hybris.platform.core.PK)
	 */
	@Override
	public boolean remove(final PK paramPK) throws ModelRemovalException
	{
		boolean removed = Boolean.FALSE;
		try
		{
			modelService.remove(paramPK);
			removed = Boolean.TRUE;
		}
		catch (final ModelRemovalException exception)
		{
			LOGGER.error("ModelRemovalException while call remove(pk) fron JnjModelServiceImpl", exception);
		}
		return removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.JnjModelService#refresh(java.lang.Object)
	 */
	@Override
	public void refresh(final Object paramObject)
	{
		modelService.refresh(paramObject);
	}

}
