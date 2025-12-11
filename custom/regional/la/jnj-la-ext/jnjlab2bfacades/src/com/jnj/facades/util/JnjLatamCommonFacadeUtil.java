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
package com.jnj.facades.util;

import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;


public interface JnjLatamCommonFacadeUtil
{
	public void checkShowChangeAccountLink(final Model model);

	public List<String> getEffectiveGroups();

	public String getDocType(String messageKey);

	public String getAddress(AddressData addressData);

	public String getPoType(final String messageKey);

	public Object buildShowATPFlagMap(final Model model, final String data);
	
	public void checkoutCurrentSiteNotNull();
	
	public void addPermissionsFlags(HttpServletRequest request, Model model);
		
	public boolean isCommercialUserEnabledForCurrentSite();
}
