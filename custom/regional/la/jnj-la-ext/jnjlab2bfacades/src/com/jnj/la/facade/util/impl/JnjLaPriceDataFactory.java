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
package com.jnj.la.facade.util.impl;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
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
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.services.CMSSiteService;

public class JnjLaPriceDataFactory extends JnjPriceDataFactory {
	private CommonI18NService commonI18NService;
	private I18NService i18NService;
	private CommerceCommonI18NService commerceCommonI18NService;
	@Autowired
	private BaseStoreService baseStoreService;
	@Autowired
	private CMSSiteService cmsSiteService;

	protected final Map<String, NumberFormat> currencyFormats = new HashMap<String, NumberFormat>();


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
	@Override
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
	public final NumberFormat createCurrencyFormat(final Locale locale, final CurrencyModel currency)
	{
		final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
		if (cmsSiteService.getCurrentSite() != null)
		{

			final String currentCountry = cmsSiteService.getCurrentSite().getDefaultCountry().getName();
			final String currentLanguage = cmsSiteService.getCurrentSite().getDefaultLanguage().getIsocode();

			final String key = currentCountry + currentLanguage + "_" + currency.getIsocode();

			if (currencyFormats.containsKey(key))
			{
				return currencyFormats.get(key);
			}

			adjustDigits((DecimalFormat) currencyFormat, currency);
			adjustSymbol((DecimalFormat) currencyFormat, currency);
			currencyFormats.put(key, currencyFormat);

		}
		return currencyFormat;

	}

	/**
	 * Adjusts {@link DecimalFormat}'s fraction digits according to given {@link CurrencyModel}.
	 */
	@Override
	public final DecimalFormat adjustDigits(final DecimalFormat format, final CurrencyModel currencyModel)
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
		if (currencyModel.getIsocode().equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.BRAZIL_COUNTRY_CURRENCY_ISO)) //to change the decimal seperator, group seperator
		{
			decimalFormatSymbols.setDecimalSeparator(Jnjlab2bcoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setMonetaryDecimalSeparator(Jnjlab2bcoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setGroupingSeparator(Jnjlab2bcoreConstants.CONST_DOT.toCharArray()[0]);
		}
		if (cmsSiteService.getCurrentSite().getDefaultCountry() != null && (cmsSiteService.getCurrentSite().getDefaultCountry()
				.getName().equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.SITE_NAME_ARGENTINA)
				|| cmsSiteService.getCurrentSite().getDefaultCountry().getName()
						.equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.SITE_NAME_URUGUAY)
				|| cmsSiteService.getCurrentSite().getDefaultCountry().getName()
						.equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.SITE_NAME_BRAZIL))) //to change the decimal seperator, group seperator
		{
			decimalFormatSymbols.setDecimalSeparator(Jnjlab2bcoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setMonetaryDecimalSeparator(Jnjlab2bcoreConstants.CONST_COMMA.toCharArray()[0]);
			decimalFormatSymbols.setGroupingSeparator(Jnjlab2bcoreConstants.CONST_DOT.toCharArray()[0]);
		}
		else if (cmsSiteService.getCurrentSite().getDefaultCountry() != null && (cmsSiteService.getCurrentSite().getDefaultCountry()
				.getName().equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.SITE_NAME_CHILE)
				|| cmsSiteService.getCurrentSite().getDefaultCountry().getName()
						.equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.SITE_NAME_MEXICO)))//to change the decimal seperator, group seperator
		{
			decimalFormatSymbols.setDecimalSeparator(Jnjlab2bcoreConstants.CONST_DOT.toCharArray()[0]);
			decimalFormatSymbols.setMonetaryDecimalSeparator(Jnjlab2bcoreConstants.CONST_DOT.toCharArray()[0]);
			decimalFormatSymbols.setGroupingSeparator(Jnjlab2bcoreConstants.CONST_COMMA.toCharArray()[0]);
		}

		format.setDecimalFormatSymbols(decimalFormatSymbols);

		return format;
	}

	@Override
	public final PriceData create(final PriceDataType priceType, final BigDecimal value, final CurrencyModel currency)
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

	public Double parsePrice(final String value, final CurrencyModel currency) throws ParseException {
		final LanguageModel currentLanguage = getCommonI18NService().getCurrentLanguage();
		Locale locale = getCommerceCommonI18NService().getLocaleForLanguage(currentLanguage);
		if (locale == null) {
			// Fallback to session locale
			locale = getI18NService().getCurrentLocale();
		}

		final NumberFormat currencyFormat = createCurrencyFormat(locale, currency);

		return currencyFormat.parse(value).doubleValue();
	}



}
