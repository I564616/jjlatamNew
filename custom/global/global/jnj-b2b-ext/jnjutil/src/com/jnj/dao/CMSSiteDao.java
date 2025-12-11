/* * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.dao;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.CountryModel;


/**
 * The Interface CMSSiteDao.
 * 
 * @author Accenture
 * 
 * @version 1.0
 * 
 */
public interface CMSSiteDao extends de.hybris.platform.cms2.servicelayer.daos.CMSSiteDao
{

	/**
	 * This method retrieves the CMS site corresponding to the given iso code by verifying the default country associated
	 * with the site.
	 * 
	 * @param countryModel
	 *           the countryitem
	 * @return cmsSite
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 */
	CMSSiteModel findCMSSiteByCountry(CountryModel countryModel) throws CMSItemNotFoundException;

}
