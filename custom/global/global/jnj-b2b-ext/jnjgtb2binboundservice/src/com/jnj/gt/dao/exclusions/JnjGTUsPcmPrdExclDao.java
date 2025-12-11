/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.dao.exclusions;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.List;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTExProductAttributeModel;



/**
 * The JnjGTUsPcmPrdExclDao interface is an interface of JnjGTUsPcmPrdExclDaoImpl class which contains methods
 * declaration of all the methods of the aforementioned class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTUsPcmPrdExclDao
{

	/**
	 * Gets the Exclusions product attribute models.
	 * 
	 * @return the List of JnjGTExProductAttributeModel.
	 */
	public List<JnjGTExProductAttributeModel> getExProductAttributeModels();

	/**
	 * Gets the product models for US products which has portal indicator is null.
	 * 
	 * @return the List of JnJProductModel.
	 */
	public List<JnJProductModel> getProductModelForUSProducts(final CatalogVersionModel catalogVersionModel);

	/**
	 * Gets the product models for US products which have publish indicator as false by using the temp publish indicator
	 * as true set in Product master feed.
	 * 
	 * @return the List of JnJProductModel.
	 */
	public List<JnJProductModel> getProductModelHavingPublishIndFalse(final CatalogVersionModel catalogVersionModel);
}
