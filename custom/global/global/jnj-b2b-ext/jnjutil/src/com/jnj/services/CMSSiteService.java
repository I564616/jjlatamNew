/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.services;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;


/**
 * This extension of DefaultCMSSiteService.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface CMSSiteService extends de.hybris.platform.cms2.servicelayer.services.CMSSiteService
{

	/**
	 * This method retrieves the CMSSite with the default country corresponding to the ISO code. The default channel type
	 * is used from the configuration to retrieve the site.If there are more than one site for this combination,
	 * CMSItemNotFoundException is thrown.
	 * 
	 * @param countryIsoCode
	 *           the country iso code
	 * @return cmsSiteModel
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	CMSSiteModel getSiteForCountry(String countryIsoCode) throws CMSItemNotFoundException;


	/**
	 * This method retrieves the CMSSite with the default country corresponding to the ISO code. The default channel type
	 * is used from the configuration to retrieve the site.If there are more than one site for this combination,
	 * CMSItemNotFoundException is thrown.
	 * 
	 * @param countryModel
	 * @return cmsSiteModel
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */

	CMSSiteModel getSiteForCountry(final CountryModel countryModel) throws CMSItemNotFoundException;



}
