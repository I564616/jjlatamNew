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
package com.jnj.core.services;

import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.Collection;
import java.util.Locale;

import com.jnj.core.model.JnJB2bCustomerModel;


public interface JnjLaMessageService
{
	String getMessageFromImpex(String messageCode, final Locale locale, final Collection<CatalogVersionModel> catalogVersions);

	String getMessageFromImpex(final String messageCode, final JnJB2bCustomerModel jnJB2bCustomerModel);
}