/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.laudo.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjLaudoModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnjLaudoData;


/**
 *
 */
public class JnjLaudoDataPopulator implements Populator<JnjLaudoModel, JnjLaudoData>
{



	/**
	 * Method to populate JnjLaudoData from JnjLaudoModel
	 */

	/** The Constant LOG. */
	private static final String METHOD_POPULATE = "Laudo Data Populator - populate()";
	public static final String LAUDO = "Laudo";

	@Override
	public void populate(final JnjLaudoModel source, final JnjLaudoData target) throws ConversionException
	{

		JnjGTCoreUtil.logDebugMessage(LAUDO, METHOD_POPULATE, Logging.BEGIN_OF_METHOD, JnjLaudoDataPopulator.class);


		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setLaudoNumber(source.getLaudoNumber());
		target.setProductCode(source.getProductCode());
		target.setExpirationDate(source.getExpirationDate());
		target.setPdfFileName(source.getFileName());
		target.setReadStatus(source.getReadStatus());
		target.setWriteStatus(source.getWriteStatus());
		if (null != source.getFileMedia())
		{
			target.setActive(Boolean.TRUE);
		}
		else
		{
			target.setActive(Boolean.FALSE);
		}
		JnjGTCoreUtil.logDebugMessage(LAUDO, METHOD_POPULATE, Logging.END_OF_METHOD, JnjLaudoDataPopulator.class);

	}
}
