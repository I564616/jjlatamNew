package com.jnj.la.core.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_ARGENTINA;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_CENCA;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_CHILE;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_COLOMBIA;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_ECUADOR;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_MEXICO;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_PANAMA;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_PERU;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_PUERTORICO;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_URUGUAY;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.COUNTRY_ISO_COSTA_RICA;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.KEY_AR_VALID_COUNTRIES;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.KEY_CENCA_VALID_COUNTRIES;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.KEY_PE_VALID_COUNTRIES;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.KEY_PR_VALID_COUNTRIES;
import static com.jnj.la.core.constants.Jnjlab2bcoreConstants.KEY_UY_VALID_COUNTRIES;


/**
 * This enum contains only countries which must to be processed Created by aadrian2 on 02/02/2017.
 */
public enum JnjCountryEnum
{

	ARGENTINA(KEY_AR_VALID_COUNTRIES, COUNTRY_ISO_ARGENTINA),
	BRAZIL(StringUtils.EMPTY, COUNTRY_ISO_BRAZIL),
	CENCA(KEY_CENCA_VALID_COUNTRIES, COUNTRY_ISO_CENCA),
	CHILE(StringUtils.EMPTY, COUNTRY_ISO_CHILE),
	COLOMBIA(StringUtils.EMPTY, COUNTRY_ISO_COLOMBIA),
	ECUADOR(StringUtils.EMPTY, COUNTRY_ISO_ECUADOR),
	MEXICO(StringUtils.EMPTY, COUNTRY_ISO_MEXICO),
	PANAMA(StringUtils.EMPTY, COUNTRY_ISO_PANAMA),
	PERU(KEY_PE_VALID_COUNTRIES, COUNTRY_ISO_PERU),
	PUERTO_RICO(KEY_PR_VALID_COUNTRIES, COUNTRY_ISO_PUERTORICO),
	URUGUAY(KEY_UY_VALID_COUNTRIES, COUNTRY_ISO_URUGUAY),
	COSTA_RICA(StringUtils.EMPTY, COUNTRY_ISO_COSTA_RICA);

	private final String isoCodeParameter;

	private final String countryIso;

	JnjCountryEnum(final String isoCodeParameter, final String countryIso)
	{
		this.isoCodeParameter = isoCodeParameter;
		this.countryIso = countryIso;
	}

	public String getIsoCodeParameter()
	{
		return isoCodeParameter;
	}

	public String getCountryIso()
	{
		return countryIso;
	}

    public static JnjCountryEnum getValue(String isoCode){
        for (JnjCountryEnum countryEnum : JnjCountryEnum.values()) {
            if (countryEnum.getCountryIso().equalsIgnoreCase(isoCode)) {
                return countryEnum;
            }
        }
        return null;
    }

    public static List<JnjCountryEnum> getEnumWithMultipleCountries(){
        List<JnjCountryEnum> countries = new ArrayList<>();
        for (JnjCountryEnum countryEnum : JnjCountryEnum.values()) {
            if (!StringUtils.EMPTY.equals(countryEnum.getIsoCodeParameter())) {
                countries.add(countryEnum);
            }
        }
        return countries;
    }

}
