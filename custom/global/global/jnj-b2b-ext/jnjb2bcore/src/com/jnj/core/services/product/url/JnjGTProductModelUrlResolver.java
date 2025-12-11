/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.product.url;

import jakarta.annotation.Resource;

import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;



//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjGTProductDao;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;


/**
 * Extended Implementation of <code>DefaultProductModelUrlResolver</code> to populate url based on the correct mod/base
 * product. For Consumer Product url is populated from its 'ACTIVE' base product otherwise mod product itself.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTProductModelUrlResolver extends DefaultProductModelUrlResolver
{

	/**
	 * The Instance of <code>JnjGTProductDao</code>.
	 */
	@Resource(name = "productDao")
	JnjGTProductDao jnjGTProductDao;

	/**
	 * The Instance of <code>SessionService</code>.
	 */
	@Autowired
	protected SessionService sessionService;

	public JnjGTProductDao getJnjGTProductDao() {
		return jnjGTProductDao;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	
	/**
	 * Resolves and return the URL for a product, if it's a CONSUMER product url resolved through the MOD product else
	 * for MDD product itself is used.
	 */
	@Override
	public String resolve(final ProductModel source)
	{
		String url = StringUtils.EMPTY;
		final String DEFAULT_CATEGORY_ID = "DefaultCategory";
		final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		if (Jnjb2bCoreConstants.CONS.equals(currentSiteName))
		{
			final JnJProductModel modProduct = jnjGTProductDao.getModProductByBase(source.getCode(), source.getPk().toString());
			if (null != modProduct)
			{
				url = super.resolve(modProduct);
			}
		}
		/*else if (JnjPCMCoreConstants.PCM.equals(currentSiteName))
		{
			if (!source.getSupercategories().isEmpty())
			{
				if (!StringUtils.equalsIgnoreCase(source.getSupercategories().iterator().next().getCode(), DEFAULT_CATEGORY_ID))
				{
					final JnJProductModel modProduct = jnjGTProductDao.getModProductByBase(source.getCode(), source.getPk()
							.toString());
					if (null != modProduct)
					{
						url = super.resolve(modProduct);
					}
				}
			}
		}*/
		else
		{
			url = super.resolve(source);
		}
		return url;
	}
}
