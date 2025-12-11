/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.pcm.integration.core.job.service.strategy.impl;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.pcm.integration.core.job.service.strategy.JnjGTP360CountryNameStrategy;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Implementation class for JnjGTP360CountryNameStrategy.
 */
public class DefaultJnjGTP360CountryNameStrategy implements JnjGTP360CountryNameStrategy {


	@Override
	public StringBuilder getCountriesName(final JnJProductModel productModel) {

		final StringBuilder countriesName = new StringBuilder();
		final Collection<BaseStoreModel> baseStores = productModel.getCatalogVersion().getCatalog().getBaseStores();
		for (final BaseStoreModel baseStore : baseStores)
		{
			final Collection<CountryModel> countries = baseStore.getDeliveryCountries();
			for (final CountryModel country : countries)
	        {
	            if (StringUtils.isNotEmpty(countriesName.toString()))
	            {
	                countriesName.append(",");
	            }
	            countriesName.append(country.getIsocode());
	        }

		}
		return countriesName;
	}

}
