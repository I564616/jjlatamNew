/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facade.util.impl;


import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.impl.DefaultPriceDataFactory;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.store.services.BaseStoreService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * This class is used to format the price according to the current currency.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjPriceDataFactory extends DefaultPriceDataFactory
{

	private CommonI18NService commonI18NService;
	private I18NService i18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	@Autowired
	private BaseStoreService baseStoreService;

	private final Map<String, NumberFormat> currencyFormats = new HashMap<String, NumberFormat>();


	/**
	 * Formats the Price and returns the Formatted Price as String
	 */
	@Override
	public String formatPrice(final BigDecimal value, final CurrencyModel currency)
	{
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);
		if (locale == null)
		{
			// Fallback to session locale
			locale = getI18NService().getCurrentLocale();
		}

		final NumberFormat currencyFormat = createCurrencyFormat(locale, currency);
		return currencyFormat.format(value);

	}


	/**
	 * Formats the Price and returns the Formatted Price as String without given Curreny. The Curreny is taken out from
	 * the default base store. This method should be used only when the user is logged in from front end.
	 * 
	 * @param value
	 * @return
	 */
	public String formatPrice(final BigDecimal value)
	{
		final CurrencyModel currencyModel = baseStoreService.getCurrentBaseStore().getDefaultCurrency();
		final String formatedPrice = formatPrice(value, currencyModel);
		return formatedPrice;
	}


	/**
	 * @return the commonI18NService
	 */
	@Override
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Override
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @return the i18NService
	 */
	@Override
	public I18NService getI18NService()
	{
		return i18NService;
	}

	/**
	 * @param i18nService
	 *           the i18NService to set
	 */
	@Override
	public void setI18NService(final I18NService i18nService)
	{
		i18NService = i18nService;
	}

	/**
	 * @return the commerceCommonI18NService
	 */
	@Override
	public CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	/**
	 * @param commerceCommonI18NService
	 *           the commerceCommonI18NService to set
	 */
	@Override
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}


	@Override
	public NumberFormat createCurrencyFormat(final Locale locale, final CurrencyModel currency)
	{
		final String key = locale.getISO3Country() + "_" + currency.getIsocode();
		if (currencyFormats.containsKey(key))
		{
			return currencyFormats.get(key);
		}
		else
		{
			final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
			adjustDigits((DecimalFormat) currencyFormat, currency);
			adjustSymbol((DecimalFormat) currencyFormat, currency);
			currencyFormats.put(key, currencyFormat);
			return currencyFormat;
		}
	}

	/**
	 * Adjusts {@link DecimalFormat}'s fraction digits according to given {@link CurrencyModel}.
	 */
	@Override
	public DecimalFormat adjustDigits(final DecimalFormat format, final CurrencyModel currencyModel)
	{
		final DecimalFormatSymbols decimalFormatSymbols = format.getDecimalFormatSymbols();
		final int tempDigits = currencyModel.getDigits() == null ? 0 : currencyModel.getDigits().intValue();
		final int digits = Math.max(0, tempDigits);

		format.setMaximumFractionDigits(digits);
		format.setMinimumFractionDigits(digits);
		if (digits == 0)
		{
			format.setDecimalSeparatorAlwaysShown(false);
		}
		if (currencyModel.getIsocode().equalsIgnoreCase(Jnjb2bCoreConstants.UpsertCustomer.BRAZIL_COUNTRY_CURRENCY_ISO)) //to change the decimal seperator, group seperator
		{
			decimalFormatSymbols.setDecimalSeparator(Jnjb2bCoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setMonetaryDecimalSeparator(Jnjb2bCoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setGroupingSeparator(Jnjb2bCoreConstants.CONST_DOT.toCharArray()[0]);
		}
		format.setDecimalFormatSymbols(decimalFormatSymbols);

		return format;
	}

	@Override
	public PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
	{
		Assert.notNull(priceType, "Parameter priceType cannot be null.");
		Assert.notNull(value, "Parameter value cannot be null.");
		Assert.notNull(currency, "Parameter currency cannot be null.");

		final PriceData priceData = createPriceData();

		priceData.setPriceType(priceType);
		priceData.setValue(value);
		priceData.setCurrencyIso(currency.getIsocode());
		priceData.setFormattedValue(formatPrice(value, currency));

		return priceData;
	}
}
