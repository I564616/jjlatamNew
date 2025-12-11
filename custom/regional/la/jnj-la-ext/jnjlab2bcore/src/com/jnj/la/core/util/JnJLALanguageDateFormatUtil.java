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
package com.jnj.la.core.util;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 */
public class JnJLALanguageDateFormatUtil
{

    private static final String LANGUAGE_ENGLISH_ISO = "en";

    @Autowired
    private static CommonI18NService commonI18NService;

	private static final Logger LOGGER = Logger.getLogger(JnJLALanguageDateFormatUtil.class);

	public static Date getLanguageSpecificDate(final String language, final String expShippingDate)
	{
		Date expShippingDateInDateFormat = null;
		LOGGER.info("Parsing date " + expShippingDate + " for language " + language);
		try
		{
			final DateFormat dateFormat = new SimpleDateFormat(
					Config.getString(language + ".language.date.format", Jnjlab2bcoreConstants.Validation.DATE_FORMAT));
			expShippingDateInDateFormat = dateFormat.parse(expShippingDate);
		}
		catch (final ParseException e)
		{
			// TODO Auto-generated catch block
			LOGGER.error("Invalid date format for requested Shipping date", e);
		}
		return expShippingDateInDateFormat;
	}

	public static String getLanguageSpecificDate(final String language, final Date expShippingDate)
	{
		String expShippingDateInDateFormat = null;
		LOGGER.info("Formatting date " + expShippingDate + " for language " + language);
		final DateFormat dateFormat = new SimpleDateFormat(
				Config.getString(language + ".language.date.format", Jnjlab2bcoreConstants.Validation.DATE_FORMAT));
		expShippingDateInDateFormat = dateFormat.format(expShippingDate);
		return expShippingDateInDateFormat;
	}

	public static String getLanguageSecificDatePattern(final String language)
	{
		LOGGER.info("Pattern for language " + language);

		return Config.getString(language + ".language.date.format", Jnjlab2bcoreConstants.Validation.DATE_FORMAT);

	}


	public static String getLanguageSpecificDateOrderDetail(final String language, final Date expShippingDate)
	{
		String expShippingDateInDateFormat = null;
		JnjGTCoreUtil.logDebugMessage("getLanguageSecificDateOrderDetail", "getLanguageSecificDateOrderDetail",
				"Formatting date " + expShippingDate + " for language " + language, JnJLALanguageDateFormatUtil.class);
		final DateFormat dateFormat = new SimpleDateFormat(
				Config.getString(language + ".language.date.format.latam", Jnjlab2bcoreConstants.Validation.DATE_FORMAT));
		expShippingDateInDateFormat = dateFormat.format(expShippingDate);
		return expShippingDateInDateFormat;
	}

    public static SimpleDateFormat getDateFormatBaseOnUserLanguage(final CommonI18NService commonI18NService) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        if (null != commonI18NService.getCurrentLanguage()
            && !StringUtils.equals(commonI18NService.getCurrentLanguage().getIsocode(), LANGUAGE_ENGLISH_ISO)){
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        }
        return simpleDateFormat;
    }

    public static String convertDateToUSFormat(String date, String isoCode){
        StringBuilder convertedDate = new StringBuilder();

        if(StringUtils.isEmpty(date) || StringUtils.isEmpty(isoCode)) {
            return StringUtils.EMPTY;
        }

        if(Jnjlab2bcoreConstants.LANGUAGE_ENGLISH_ISO.equals(isoCode)){
            return date;
        }

        final String[] splitStartDate = date.split("/");

        if(splitStartDate.length != 3) {
            return StringUtils.EMPTY;
        }

        convertedDate.append(splitStartDate[1])
            .append("/")
            .append(splitStartDate[0])
            .append("/")
            .append(splitStartDate[2]);

        return convertedDate.toString();
    }
}
