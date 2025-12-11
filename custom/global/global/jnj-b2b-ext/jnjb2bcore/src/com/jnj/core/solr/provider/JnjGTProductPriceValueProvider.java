/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;



//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * TODO:<class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */

public class JnjGTProductPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider,
		Serializable
{
	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;
	@Autowired
	protected CommonI18NService commonI18NService;

	public JnJGTProductService getJnjGTProductService() {
		return jnjGTProductService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	protected FieldNameProvider fieldNameProvider;

	@Override
	public Collection<FieldValue> getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty,
			final Object model) throws FieldValueProviderException
	{
		final JnJProductModel naProductModel = getGTProductModel(model);
		if (naProductModel == null)
		{
			return Collections.emptyList();
		}

		// Get Delivery GTIN
		final JnjGTVariantProductModel variantModel = jnjGTProductService.getDeliveryGTIN(naProductModel);

		if (variantModel != null && CollectionUtils.isNotEmpty(variantModel.getEurope1Prices()))
		{

			final Collection languages = indexConfig.getLanguages();
			LanguageModel languageModel = null;
			if (CollectionUtils.isNotEmpty(languages))
			{
				languageModel = (LanguageModel) languages.iterator().next();
			}
			// For each price row present in the variant, add value in the index
			final Collection<FieldValue> fieldValues = new ArrayList<FieldValue>();
			final Collection<PriceRowModel> priceRows = variantModel.getEurope1Prices();

			// If current indexing is being done for MDD
			//	if (indexConfig.getBaseSite().getJnjWebSiteType().getCode().equals(Jnjb2bCoreConstants.MDD_SITE_ID)) - fix jjepic-747
			if (indexConfig.getBaseSite().getUid().equals(Jnjb2bCoreConstants.MDD_SITE_ID))
			{
				for (final PriceRowModel priceRow : priceRows)
				{
					fieldValues.addAll(createFieldValue(priceRow, indexedProperty, naProductModel, languageModel));
				}
			}
			// If current indexing is being done for CONS
			//else if (indexConfig.getBaseSite().getJnjWebSiteType().getCode().equals(Jnjb2bCoreConstants.CONSUMER_SITE_ID))  - fix jjepic-747
			else if (indexConfig.getBaseSite().getUid().equals(Jnjb2bCoreConstants.CONSUMER_SITE_ID))
			{
				for (final PriceRowModel priceRow : priceRows)
				{
					fieldValues.addAll(createFieldValue(priceRow, indexedProperty, null, languageModel));
				}
			}

			return fieldValues;
		}
		else
		{
			return Collections.emptyList();
		}
	}

	protected List<FieldValue> createFieldValue(final PriceRowModel priceRow, final IndexedProperty indexedProperty,
			final JnJProductModel naProductModel, final LanguageModel languageModel)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		final StringBuilder value = new StringBuilder();

		// If UG is present
		if (priceRow.getUg() != null && priceRow.getUg().getCode() != null)
		{
			value.append(priceRow.getUg().getCode());
		}
		else
		{
			value.append(Jnjb2bCoreConstants.Solr.DEFAULTUG);
		}

		// For Mdd, this model will not be null
		// For Cons, this will be null
		if (naProductModel != null)
		{
			value.append(Jnjb2bCoreConstants.Solr.COLON).append(naProductModel.getSalesOrgCode());
		}



		//Append Division and price
		value.append(Jnjb2bCoreConstants.Solr.COLON).append(getFormattedPrice(priceRow, languageModel));

		final Collection<String> fieldNames = fieldNameProvider.getFieldNames(indexedProperty, priceRow.getCurrency().getIsocode());
		for (final String fieldName : fieldNames)
		{
			fieldValues.add(new FieldValue(fieldName, value));
		}
		return fieldValues;
	}

	protected JnJProductModel getGTProductModel(final Object model)
	{
		if (model instanceof JnJProductModel)
		{
			return (JnJProductModel) model;
		}

		else
		{
			return null;
		}
	}

	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}

	protected String getFormattedPrice(final PriceRowModel priceRow, final LanguageModel languageModel)
	{
		final Locale locale = commonI18NService.getLocaleForLanguage(languageModel);

		//	final String key = locale.getISO3Country() + "_" + priceRow.getCurrency().getIsocode();

		final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		final DecimalFormatSymbols decimalFormatSymbols = currencyFormat.getDecimalFormatSymbols();
		final int tempDigits = priceRow.getCurrency().getDigits() == null ? 0 : priceRow.getCurrency().getDigits().intValue();
		final int digits = Math.max(0, tempDigits);

		currencyFormat.setMaximumFractionDigits(digits);
		currencyFormat.setMinimumFractionDigits(digits);
		if (digits == 0)
		{
			currencyFormat.setDecimalSeparatorAlwaysShown(false);
		}
		currencyFormat.setDecimalFormatSymbols(decimalFormatSymbols);
		final String symbol = priceRow.getCurrency().getSymbol();
		if (symbol != null)
		{
			final DecimalFormatSymbols symbols = currencyFormat.getDecimalFormatSymbols(); // does cloning
			final String iso = priceRow.getCurrency().getIsocode();
			boolean changed = false;
			if (!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
			{
				symbols.setInternationalCurrencySymbol(iso);
				changed = true;
			}
			if (!symbol.equals(symbols.getCurrencySymbol()))
			{
				symbols.setCurrencySymbol(symbol);
				changed = true;
			}
			if (changed)
			{
				currencyFormat.setDecimalFormatSymbols(symbols);
			}
		}
		return currencyFormat.format(priceRow.getPrice());

	}
}
