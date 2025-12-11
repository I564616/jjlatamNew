/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.solr.provider;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;

import java.io.Serial;
import java.io.Serializable;


/**
 * This is used to get content identity
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjContentIdentityProvider implements IdentityProvider<ContentPageModel>, Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.solrfacetsearch.provider.IdentityProvider#getIdentifier(de.hybris.platform.solrfacetsearch.
	 * config.IndexConfig, java.lang.Object)
	 */
	@Override
	public String getIdentifier(final IndexConfig paramIndexConfig, final ContentPageModel cmsComponent)
	{

		//To enable fall back 
		//	JaloSession.getCurrentSession().setAttribute(LocalizableItem.LANGUAGE_FALLBACK_ENABLED, Boolean.TRUE);
		//JaloSession.getCurrentSession().setAttribute(AbstractItemModel.LANGUAGE_FALLBACK_ENABLED_SERVICE_LAYER, Boolean.TRUE);

		final CatalogVersionModel catalogVersion = cmsComponent.getCatalogVersion();
		return catalogVersion.getCatalog().getId() + "/" + catalogVersion.getVersion() + "/" + cmsComponent.getUid();
	}

}
