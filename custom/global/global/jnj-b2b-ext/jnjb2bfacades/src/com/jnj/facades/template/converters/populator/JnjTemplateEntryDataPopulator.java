/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.template.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.springframework.util.Assert;

import com.jnj.core.model.JnjTemplateEntryModel;
import com.jnj.facades.data.JnjProductData;
import com.jnj.facades.data.JnjTemplateEntryData;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjTemplateEntryDataPopulator implements Populator<JnjTemplateEntryModel, JnjTemplateEntryData>
{
	@Override
	public void populate(final JnjTemplateEntryModel source, final JnjTemplateEntryData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		final JnjProductData productData = new JnjProductData();
		if (null != source.getProduct())
		{
			productData.setCode(source.getProduct().getCode());
			productData.setName(source.getProduct().getName());
			productData.setDescription(source.getProduct().getDescription());
		}
		target.setProduct(productData);
		target.setQty(source.getQty());
		target.setUnitPrice(source.getUnitPrice());
		target.setTotalPrice(source.getTotalPrice());
	}
}
