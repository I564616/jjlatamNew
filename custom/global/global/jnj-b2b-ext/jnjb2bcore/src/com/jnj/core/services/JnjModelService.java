/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import java.util.Collection;


/**
 * The Interface JnjModelService.
 * 
 * This interface is designed as a wrapper to ModelService(OOTB), Exception thrown in
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjModelService
{
	/**
	 * Persist given object in the DB.
	 * 
	 * @param Object
	 *           instance of object which need to be persist.
	 * @return true, if successful
	 * @throws ModelSavingException
	 *            the model saving exception
	 */
	public abstract boolean save(Object paramObject) throws ModelSavingException;

	/**
	 * Persist all objects given in the items.
	 * 
	 * @param Collection
	 *           all items to be saved
	 * @return true, if successful
	 * @throws ModelSavingException
	 *            the model saving exception
	 */
	public abstract boolean saveAll(Collection<? extends Object> paramCollection) throws ModelSavingException;

	/**
	 * Removes the given object from DB.
	 * 
	 * @param Object
	 *           item which to be removed
	 * @return true, if successful
	 * @throws ModelRemovalException
	 *            the model removal exception
	 */
	public abstract boolean remove(Object paramObject) throws ModelRemovalException;

	/**
	 * Removes the all item in collection from DB.
	 * 
	 * @param Collection
	 *           all items to be removed
	 * @return true, if successful
	 * @throws ModelRemovalException
	 *            the model removal exception
	 */
	public abstract boolean removeAll(Collection<? extends Object> paramCollection) throws ModelRemovalException;

	/**
	 * Removes the item with given PK from DB.
	 * 
	 * @param PK
	 *           PK object of the item to be removed
	 * @return true, if successful
	 * @throws ModelRemovalException
	 *            the model removal exception
	 */
	public abstract boolean remove(PK paramPK) throws ModelRemovalException;

	/**
	 * Reload given object in the cache.
	 * 
	 * @param Object
	 *           object which needs to be reload in cache.
	 */
	public abstract void refresh(Object paramObject);

}
