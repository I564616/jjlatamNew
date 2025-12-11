/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.util;


import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JnjLaCoreUtil
{
	private JnjLaCoreUtil()
	{
	}

	@Autowired
	private MediaService mediaService;

	/**
	 * Get a list of countries of a propertie
	 *
	 * @param countryListPropertie
	 *           propertie
	 * @return countriesIso list of countries
	 */
	public static List<String> getCountriesList(final String countryListPropertie)
	{
		final String methodName = "getCountriesList()";
		List<String> countriesIso = new ArrayList<>();
		if (countryListPropertie == null)
		{
			JnjGTCoreUtil.logInfoMessage("Find country list", methodName, "Could not find the property " + countryListPropertie,
					JnjLaCoreUtil.class);
		}
		else
		{
			countriesIso = JnJCommonUtil.getValues(countryListPropertie, Jnjb2bCoreConstants.CONST_COMMA);
		}
		return countriesIso;
	}

	public static String getCommaSeparatedValuesFromLists(final List<String> values)
	{
		String commaSeparatedValues = null;

		for (final String str : values)
		{
			if (commaSeparatedValues == null)
			{
				commaSeparatedValues = "'" + str + "'";
			}
			else
			{
				final StringBuilder bld = new StringBuilder();
				for (int i = 0; i < commaSeparatedValues.length(); ++i)
				{
					bld.append(commaSeparatedValues);
				}


			}
		}
		return commaSeparatedValues;
	}

	public static String getStringValue(final String value)
	{
		return (value != null) ? value : Jnjlab2bcoreConstants.EMPTY_STRING;
	}

	public static String getStringTwoDigitsValue(final String value)
	{
		return (value != null) ? value.substring(0, 2) : Jnjlab2bcoreConstants.EMPTY_STRING;
	}

	public static Double getDoubleValue(final String value)
	{
		return (value != null) ? Double.valueOf(Double.parseDouble(value)) : null;
	}

	/**
	 * Check if a country belongs to a list
	 *
	 * @param countryIso
	 *           country iso code
	 * @param countriesList
	 *           property id from properties file
	 * @return Boolean
	 */
	public static Boolean isCountryValidForACountriesList(final String countryIso, final List<String> countriesList)
	{
		Boolean isCountryInTheList = Boolean.FALSE;
		if (countriesList.contains(countryIso))
		{
			isCountryInTheList = Boolean.TRUE;
		}
		return isCountryInTheList;
	}

	public static String getCommaSeparatedValuesForQueryConditions(final String configProperty)
	{
		final String valuesFromConfig[] = Config.getParameter(configProperty).split(",");

		final StringBuilder bld = new StringBuilder();

		if (valuesFromConfig != null && valuesFromConfig.length > 0)
		{
			for (final String str : valuesFromConfig)
			{
				if (bld.length() == 0)
				{
					bld.append("'" + str + "'");
				}
				else
				{
					bld.append(",'" + str + "'");
				}
			}
		}
		return bld.toString();
	}

	public String createMediaLogoURL(final OrderModel order)
	{
		CatalogVersionModel currentCatalog = null;
		currentCatalog = ((CMSSiteModel) order.getSite()).getContentCatalogs().get(0).getActiveCatalogVersion();
		return mediaService.getMedia(currentCatalog, "siteLogoImage").getURL();
	}

	public static long getLongValueFromDecimalString(final String decimalString)
	{
		if (decimalString != null)
		{
			return (long) Double.parseDouble(decimalString);
		}
		return 0;
	}

	/**
	 * Updated the quantity of all order entries which are part of free goods in order send the quantity correctly upon
	 * order validation and order creation.
	 *
	 * @param cartModel
	 */
	public static void updateQuantityByFreeGoodLogic(final CartModel cartModel, final Map<String, JnjOutOrderLine> freeGoodsMap)
	{
		final String methodName = "updateQuantityByFreeGoodLogic()";
		// setting the actual quantity with which create order needs to be invoked
		if (freeGoodsMap == null || cartModel == null)
		{
			return;
		}

		for (final AbstractOrderEntryModel abstOrdEntModel : cartModel.getEntries())
		{
			if (freeGoodsMap.containsKey(abstOrdEntModel.getMaterialEntered()))
			{
				final String freeItemsQuantity = freeGoodsMap.get(abstOrdEntModel.getMaterialEntered()).getMaterialQuantity();
				final long chargedQuantity = abstOrdEntModel.getQuantity().longValue();
				final long totalQuantity = JnjLaCoreUtil.getLongValueFromDecimalString(freeItemsQuantity) + chargedQuantity;
				
				abstOrdEntModel.setQuantity(Long.valueOf(totalQuantity)); // Inclusive logic
					JnjGTCoreUtil.logInfoMessage("FreeGood Quantity", methodName,
							"Total Quantity for FreeGood Product " + totalQuantity, JnjLaCoreUtil.class);
				
			}
		}
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

}
