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

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.enums.JnjCountryEnum;
import com.jnj.la.core.model.JnJLaB2BUnitModel;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.io.File;

public class JnjLaCommonUtil extends JnJCommonUtil
{
	private static final String JNJ_COMMON_UTIL = "JnJCommonUtil";
	private static final String JNJ_CENCA_COUNTRIES = "jnj.valid.cenca.countries.isoCode";
	private static final String CENCA = "cenca";
	private static final String DEFAULT_LOCALE = "es";
	private static final Logger LOG = Logger.getLogger(JnjLaCommonUtil.class);

	private static final String PROPERTY_KEY_FOR_ES = "jnj.countries.es.language";
	private static final String PROPERTY_KEY_FOR_EN = "jnj.countries.en.language";
	private static final String PROPERTY_KEY_FOR_PT = "jnj.countries.pt.language";
	protected static final String SYS_MASTER_PROPERTY = "sys_master";

	private static final String ES = "es";
	private static final String EN = "en";
	private static final String PT = "pt";
	
	private static final String PAC_HIVE_DISABLED_COUNTRIES = "pac.aera.disabled.for.base.stores";
	private static final String SYMBOL_COMMA = ",";
	private static final String PAC_HIVE_ENABLED_SECTORS = "pac.aera.enabled.for.product.sectors";

	/**
	 * This method is used to get base store country for Master Companies
	 */
	public static String getBaseStoreCountryMaster(final String customerCountry)
	{
		String baseStoreCountry = StringUtils.EMPTY;

		if (checkValidCountry(customerCountry))
		{
			baseStoreCountry = getIdByCountry(customerCountry).toUpperCase();
		}
		return baseStoreCountry;
	}

	/**
	 * Gets the Date Object for the given date string and date format.
	 *
	 * @param rsaDateString
	 *           the date string
	 * @param finalDateFormat
	 *           the format string
	 * @return the date
	 * @throws BusinessException
	 *            the business exception
	 */
	public static Date getRsaDate(final String rsaDateString, final String finalDateFormat) throws BusinessException
	{
		final String methodName = "getRsaDate()";


		JnjGTCoreUtil.logDebugMessage(JNJ_COMMON_UTIL, methodName, Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD,
				JnJCommonUtil.class);

		Date finalDate = null;
		try
		{
			final SimpleDateFormat inputFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.RSA_DATE_FORMAT);
			final Date rsaDate = inputFormat.parse(rsaDateString);

			final SimpleDateFormat outputFormat = new SimpleDateFormat(finalDateFormat);
			final String outputDate = outputFormat.format(rsaDate);
			finalDate = outputFormat.parse(outputDate);
		}
		catch (final ParseException parseException)
		{
			JnjGTCoreUtil.logWarnMessage(JNJ_COMMON_UTIL, methodName,
					"Exception Occured while parsing the date String[" + rsaDateString + "] into format [" + finalDateFormat + "]",
					JnJCommonUtil.class);
			throw new BusinessException(
					"Exception Occurred while parsing the date String[" + rsaDateString + "] into format [" + finalDateFormat + "]");
		}

		JnjGTCoreUtil.logDebugMessage(JNJ_COMMON_UTIL, methodName, Jnjlab2bcoreConstants.Logging.END_OF_METHOD,
				JnJCommonUtil.class);
		return finalDate;
	}

	public static String getIdByCountry(final String countryIso)
	{
		if (StringUtils.isEmpty(countryIso))
		{
			JnjGTCoreUtil.logErrorMessage("getIdByCountry", "getIdByCountry", "No country ISO Code passed!", JnjLaCommonUtil.class);
			return StringUtils.EMPTY;
		}

		final JnjCountryEnum countryFound = JnjCountryEnum.getValue(countryIso);

		if (null != countryFound && countryFound != JnjCountryEnum.PANAMA && countryFound != JnjCountryEnum.COSTA_RICA)
		{
			return countryFound.getCountryIso().toLowerCase();
		}
		else
		{
			for (final JnjCountryEnum countryEnum : JnjCountryEnum.getEnumWithMultipleCountries())
			{
				final List<String> countriesList = Arrays
						.asList(Config.getParameter(countryEnum.getIsoCodeParameter()).split(Jnjb2bCoreConstants.CONST_COMMA));
				if (countriesList.contains(countryIso))
				{
					return countryEnum.getCountryIso().toLowerCase();
				}
			}
		}

		JnjGTCoreUtil.logErrorMessage("getIdByCountry", "getIdByCountry", "Country ISO Code not found!", JnjLaCommonUtil.class);
		return StringUtils.EMPTY;
	}

    public static List<String> getCountriesBySite(String site) {
        List<String> countries = getCountriesBySiteWithDuplications(site);

        if (countries.size() > 1) {
            List<String> editableCountries = new ArrayList<>(countries);
            editableCountries.removeAll(getAvoidDuplicatedEmailsCountries());
            return editableCountries;
        }

        return countries;
    }

    private static List<String> getCountriesBySiteWithDuplications(String siteOrStore){
        if (StringUtils.isNotBlank(siteOrStore)) {
            String isoCode = siteOrStore.replaceAll("BaseStore", "").replaceAll("CMSite", "").replaceAll("CMSsite", "").toUpperCase();
            JnjCountryEnum jnjCountryEnum = JnjCountryEnum.getValue(isoCode);
            if (jnjCountryEnum != null && StringUtils.isNotBlank(jnjCountryEnum.getIsoCodeParameter())) {
                String paramValue = Config.getParameter(jnjCountryEnum.getIsoCodeParameter());
                if (StringUtils.isNotBlank(paramValue)) {
                    return Arrays.asList(paramValue.split(Jnjb2bCoreConstants.CONST_COMMA));
                }
            }
            return Collections.singletonList(isoCode);
        }
        return new ArrayList<>();
    }

    private static List<String> getAvoidDuplicatedEmailsCountries() {
        String value = Config.getParameter(Jnjlab2bcoreConstants.KEY_AVOID_DUPLICATED_EMAILS);
        if (StringUtils.isNotBlank(value)) {
            return Arrays.asList(value.split(Jnjb2bCoreConstants.CONST_COMMA));
        }
        return new ArrayList<>();
    }

    public static Locale getLocaleForCountryIsoCode(String countryIsoCode) {
        if (StringUtils.isBlank(countryIsoCode)) {
            return getDefaultLocale();
        }

        String language = getDefaultLanguageForCountryIsoCode(countryIsoCode);
        return getLocale(language);
    }

	private static String getDefaultLanguageForCountryIsoCode(String code){
        if (getCountriesByProperty(PROPERTY_KEY_FOR_ES).contains((code.toUpperCase(Locale.ENGLISH)))) {
            return ES;
        }
        if (getCountriesByProperty(PROPERTY_KEY_FOR_PT).contains((code.toUpperCase(Locale.ENGLISH)))) {
            return PT;
        }
        if (getCountriesByProperty(PROPERTY_KEY_FOR_EN).contains((code.toUpperCase(Locale.ENGLISH)))) {
            return EN;
        }
        return ES;
    }

	private static List<String> getCountriesByProperty(String property){
        String paramValue = Config.getParameter(property);
        if (StringUtils.isNotBlank(paramValue)) {
            return Arrays.asList(paramValue.split(Jnjb2bCoreConstants.CONST_COMMA));
        }
        return new ArrayList<>();
	}

    private static Locale getLocale(final String language) {
        try {
            return LocaleUtils.toLocale(language);
        } catch (Exception e) {
            LOG.error("Error getting locale for language " + language, e);
            return getDefaultLocale();
        }
    }

	public static Locale getLocale(final String isoCode, final LanguageModel languageModel) {
		final String language = languageModel.getIsocode();
		final Optional<Locale> locale = LocaleUtils.languagesByCountry(isoCode).stream().filter(l -> l.getLanguage().equalsIgnoreCase(language)).findAny();
		return locale.orElse(LocaleUtils.countriesByLanguage(language).get(0));
	}

    private static Locale getDefaultLocale() {
        return LocaleUtils.toLocale(DEFAULT_LOCALE);
    }

	/**
	 *
	 * This is used to get a configuration int valuefrom properties file for given key
	 *
	 * @param key
	 *
	 * @return String
	 */
	public static int getIntConfigValue(final String key)
	{
		if (StringUtils.isNotEmpty(key))
		{
			return Integer.parseInt(Config.getParameter(key));
		}
		return 0;
	}


	public static String getSalesOrgCustomerForProductSector(final String productSector, final JnJB2BUnitModel jnjB2BUnit)
	{
		String salesOrgCustomer = null;
		if (productSector != null && jnjB2BUnit != null && jnjB2BUnit instanceof JnJLaB2BUnitModel)
		{
			final List<JnJSalesOrgCustomerModel> salesOrgs = ((JnJLaB2BUnitModel) jnjB2BUnit).getSalesOrg();
			if (salesOrgs != null && !salesOrgs.isEmpty())
			{
				salesOrgCustomer = findSalesOrgForSectorInList(productSector, salesOrgs);
			}
		}

		if (salesOrgCustomer != null && !salesOrgCustomer.isEmpty())
		{
			return salesOrgCustomer.substring(0, 2);
		}
		return null;
	}

	private static String findSalesOrgForSectorInList(final String productSector, final List<JnJSalesOrgCustomerModel> salesOrgs)
	{
		for (final JnJSalesOrgCustomerModel salesOrg : salesOrgs)
		{
			if (productSector.equals(salesOrg.getSector()))
			{
				return salesOrg.getSalesOrg();
			}
		}
		return null;
	}

	public static String getBaseStorePrefix(final String country)
	{
		// Verify if the country is a default country for a store
		for (final JnjCountryEnum jnjCountryEnum : JnjCountryEnum.values())
		{
			if (StringUtils.equals(jnjCountryEnum.getCountryIso(), country))
			{
				return findBaseStorePrefixForDefaultCountry(jnjCountryEnum, country);
			}
		}
		return StringUtils.EMPTY;
	}

	private static String findBaseStorePrefixForDefaultCountry(final JnjCountryEnum jnjCountryEnum, final String country)
	{
		String baseStorePrefix;
		if (jnjCountryEnum.getCountryIso().equals(JnjCountryEnum.PANAMA.getCountryIso()) || jnjCountryEnum.getCountryIso().equals(JnjCountryEnum.COSTA_RICA.getCountryIso()))
		{
			baseStorePrefix = JnjCountryEnum.CENCA.getCountryIso().toLowerCase();
		}
		else
		{
			baseStorePrefix = country.toLowerCase();
		}
		return baseStorePrefix;
	}

	public static boolean isStatusValueProvided(final String status)
	{
		if (status != null && !status.isEmpty() && !status.equals(Jnjlab2bcoreConstants.SPACE))
		{
			return true;
		}
		return false;
	}

	public static String getValidStringOrNull(final String value)
	{
		if (value != null && !value.isEmpty() && !value.equals(Jnjlab2bcoreConstants.SPACE))
		{
			return value;
		}
		return null;
	}

	public static String getDefaultCountry(final String b2bUnitCountryIsoCode){
		String defaultCountry = b2bUnitCountryIsoCode;
		if(StringUtils.equalsIgnoreCase(Jnjlab2bcoreConstants.COUNTRY_ISO_CENCA, b2bUnitCountryIsoCode)){
			defaultCountry = Jnjlab2bcoreConstants.COUNTRY_ISO_PANAMA;
		}
		return defaultCountry;
	}

	public static Object getMapValue (final String mapKey, final Map<String, Object> map){
		return (map != null && map.containsKey(mapKey)) ? map.get(mapKey) : null;
	}

    /**
     * Returns "cenca" if the isocode belongs to cenca, or the own isocode
     * @param countryIsoCode - country iso code
     * @return country isocode or cenca
     */
	public static String getCountryOrCenca(final String countryIsoCode) {
		if (JnJCommonUtil.getValues(JNJ_CENCA_COUNTRIES, Jnjb2bFacadesConstants.COMMA_IN_STRING).stream().anyMatch(countryIsoCode::equalsIgnoreCase)) {
            return CENCA;
		} else {
            return countryIsoCode;
		}
	}
	/**
	 * checks if sectors are enable to show aera dates
	 * @param entryModel
	 * @return
	 */
	public static boolean isEnabledForSectors(final AbstractOrderEntryModel entryModel) {
		boolean enableForSectors = false;
		final List<String> sectors = JnJCommonUtil.getValues(PAC_HIVE_ENABLED_SECTORS, SYMBOL_COMMA);
		final B2BUnitModel b2BUnitModel =  entryModel.getOrder().getUnit();
		if(b2BUnitModel instanceof  JnJLaB2BUnitModel) {
			final List<JnJSalesOrgCustomerModel> listJnJSalesOrgCustomerModel=((JnJLaB2BUnitModel) b2BUnitModel).getSalesOrg();
			if(CollectionUtils.isNotEmpty(listJnJSalesOrgCustomerModel) && CollectionUtils.isNotEmpty(sectors)) {
					enableForSectors=  listJnJSalesOrgCustomerModel.stream().anyMatch(s -> sectors.contains(s.getSector()));
				}
		}
		return enableForSectors;
	}
	/**
	 * to check for stores for whom pac hive is disabled
	 * @param entryModel
	 * @return
	 */
	public static boolean isDisableCountries(final AbstractOrderEntryModel entryModel) {
		BaseStoreModel baseStore = entryModel.getOrder().getStore();
		boolean disableCountries=false;
		if (null!=baseStore) {
			final List<String> baseStores = JnJCommonUtil.getValues(PAC_HIVE_DISABLED_COUNTRIES, SYMBOL_COMMA);
			if(CollectionUtils.isNotEmpty(baseStores)) {
			disableCountries = baseStores.stream().anyMatch(b->b.equals(baseStore.getUid()));
			}
		}
		return disableCountries;
	}

	public static String getMediaPath(final ConfigurationService configurationService, final MediaModel model) {
		final String mediaDirectoryBase = configurationService.getConfiguration().getString(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
		return mediaDirectoryBase + File.separator + SYS_MASTER_PROPERTY + File.separator + model.getLocation();
	}
}
