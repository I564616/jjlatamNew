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
package com.jnj.facades.util.impl;

import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.impl.DefaultJnjCommonFacadeUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.services.JnjLatamCommonService;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.cms2.model.site.CMSSiteModel;

public class JnjLatamCommonFacadeUtilImpl extends DefaultJnjCommonFacadeUtil implements JnjLatamCommonFacadeUtil
{
	private static final Class currentClass = JnjLatamCommonFacadeUtilImpl.class;

	private static String coreUtil = "Jnj Latam Common Facade Util";

	public static final String DOC_TYPE_PREFIX = "order.doctype.";
	public static final String PO_TYPE_PREFIX = "order.potype.";
	private static final String COMMERCIAL_USER_ENABLED = "commercial.user.enabled.";

	@Autowired
	private SessionService sessionService;

	@Autowired
	private JnjLatamCommonService jnjLatamCommonService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private CMSSiteService cmsSiteService;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	private Converter<CountryModel, CountryData> countryConverter;

	@Override
	public void checkShowChangeAccountLink(final Model model)
	{
		final String methodName = "checkShowChangeAccountLink";
		JnjGTCoreUtil.logDebugMessage(coreUtil, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		final Object showChangeAccountLink = sessionService.getAttribute(Jnjlab2bcoreConstants.SHOW_CHANGE_ACCOUNT);
		if (null != showChangeAccountLink && ((Boolean) showChangeAccountLink).booleanValue())
		{
			model.addAttribute("showChangeAccountLink", Boolean.TRUE);
		}
		JnjGTCoreUtil.logDebugMessage(coreUtil, methodName, Logging.END_OF_METHOD, currentClass);
	}

	@Override
	public List<String> getEffectiveGroups()
	{
		final String methodName = "getEffectiveGroups()";
		JnjGTCoreUtil.logDebugMessage(coreUtil, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		final String effectiveGroups = Config.getParameter(Jnjlab2bcoreConstants.KEY_LATAM_EFFECTIVE_GROUPS);
		JnjGTCoreUtil.logDebugMessage(coreUtil, methodName, Logging.END_OF_METHOD, currentClass);
		return Arrays.asList(effectiveGroups.split(Jnjb2bCoreConstants.CONST_COMMA));
	}

	@Override
	public String getDocType(final String messageKey)
	{
		return getSpecificDocOrPoType(messageKey, DOC_TYPE_PREFIX);
	}

	public String getSpecificDocOrPoType(final String messageKey, final String preFix)
	{
		String returnValue = messageKey;
		if (!StringUtils.isEmpty(messageKey))
		{
			final String type = getMessageFromImpex(preFix + messageKey.toUpperCase());
			if (null != type)
			{
				returnValue = type;
			}
		}
		return returnValue;
	}

	@Override
	public String getAddress(final AddressData addressData)
	{
		String returnValue = null;
		StringBuilder tempAddress;

		if (null != addressData)
		{
			tempAddress = new StringBuilder("");
			if (null != addressData.getLine1()) //  Street Name
			{
				tempAddress.append(addressData.getLine1() + Jnjb2bCoreConstants.CONST_COMMA);
			}
			if (null != addressData.getLine2()) //  Street Number
			{
				tempAddress.append(addressData.getLine2() + Jnjb2bCoreConstants.CONST_COMMA);
			}
			if (null != addressData.getTown()) //  Town
			{
				tempAddress.append(addressData.getTown() + Jnjb2bCoreConstants.CONST_COMMA);
			}
			if (null != addressData.getPostalCode()) //  Postal Code
			{
				tempAddress.append(addressData.getPostalCode() + Jnjb2bCoreConstants.CONST_COMMA);
			}
			if (null != addressData.getCountry()) //  Country
			{
				tempAddress.append(addressData.getCountry().getName() + Jnjb2bCoreConstants.CONST_COMMA);
			}
			if (tempAddress.toString().length() > 0)
			{
				returnValue = tempAddress.toString();
				returnValue = returnValue.substring(0, returnValue.lastIndexOf(Jnjb2bCoreConstants.CONST_COMMA) - 1); // removing last comma
			}
		}
		return returnValue;
	}

	@Override
	public String getPoType(final String messageKey)
	{
		return getSpecificDocOrPoType(messageKey, PO_TYPE_PREFIX);
	}

	@Override
	public Object buildShowATPFlagMap(final Model model, final String data)
	{
		return jnjLatamCommonService.buildShowATPFlagMap(model, data);
	}
	

	@Override
	public void checkoutCurrentSiteNotNull()
	{
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (currentSite == null)
		{
			sessionService.setAttribute(Jnjb2bCoreConstants.SITE_NAME, Jnjlab2bcoreConstants.DEFAULT_SITE_NAME);
		}
	}

	
	@Override
	public void addPermissionsFlags(final HttpServletRequest request, final Model model)
	{
		boolean placeOrderGroupFlag = false;
		boolean orderHystoryGroupFlag = false;
		boolean catalogGroupFlag = false;

		final List<String> permissions = getPermissions();
		if (permissions != null)
		{
			if (permissions.contains("placeOrderGroup") || permissions.contains("placeOrderBuyerGroup"))
			{
				placeOrderGroupFlag = true;
			}
			if (permissions.contains("orderHistoryGroup"))
			{
				orderHystoryGroupFlag = true;
			}
			if (permissions.contains("catalogGroup") || permissions.contains("viewCatalogGroup"))
			{
				catalogGroupFlag = true;
			}

		}
		model.addAttribute("placeOrderGroupFlag", Boolean.valueOf(placeOrderGroupFlag));
		model.addAttribute("orderHystoryGroupFlag", Boolean.valueOf(orderHystoryGroupFlag));
		model.addAttribute("catalogGroupFlag", Boolean.valueOf(catalogGroupFlag));

	}
	
	@Override
	public boolean isCommercialUserEnabledForCurrentSite() {
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		return configurationService.getConfiguration().getBoolean(COMMERCIAL_USER_ENABLED + cmsSiteModel.getUid(),
				false);
	}
}