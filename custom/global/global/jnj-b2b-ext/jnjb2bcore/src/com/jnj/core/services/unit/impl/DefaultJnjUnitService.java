/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.unit.impl;

import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.services.unit.JnjUnitService;


/**
 * This Service Class holds the operations related to UOMs
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjUnitService implements JnjUnitService
{

	/** The flexible search service. */
	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}


	protected static final Logger LOG = Logger.getLogger(DefaultJnjUnitService.class);
	protected static final String METHOD_GET_UNIT = "getUnit()";


	@Override
	public UnitModel getUnit(final String unitCode)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.UNIT_SERVICE + Logging.HYPHEN + METHOD_GET_UNIT + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		final UnitModel tempUnitModel = new UnitModel();
		tempUnitModel.setCode(unitCode);
		UnitModel unitModel = null;

		try
		{
			unitModel = flexibleSearchService.getModelByExample(tempUnitModel);
			unitModel.setCode(unitCode);
		}
		catch (final ModelNotFoundException | IllegalArgumentException modelNotFoundException)
		{

			LOG.error(Logging.UNIT_SERVICE + Logging.HYPHEN + METHOD_GET_UNIT + Logging.HYPHEN
					+ "Unit model not found. Returning Null");

		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.UNIT_SERVICE + Logging.HYPHEN + METHOD_GET_UNIT + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return unitModel;
	}

}
