/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.pac.aera.service.impl;

import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.jnj.la.core.services.order.impl.OrderEntryCloneHelper;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * During order data synchronization from SAP to Hybris when we process an order we remove all its order entries and
 * clone them using {@link com.jnj.la.core.services.order.impl.OrderEntryCloneHelper OrderEntryCloneHelper} so we
 * need ot make sure we do not loose PAC HIVE data. This class overrides
 * {@link com.jnj.la.core.services.order.impl.OrderEntryCloneHelper OrderEntryCloneHelper} and copies PAC HIVE data.
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class PacHiveOrderEntryCloneHelper extends OrderEntryCloneHelper
{
	private static final Logger LOG = LoggerFactory.getLogger(PacHiveOrderEntryCloneHelper.class);
	
	public static final String PAC_ORPHAN_SYNC_ENABLED = "pac.aera.orphan.pacentry.sync.enabled";
	public static final boolean PAC_ORPHAN_SYNC_ENABLED_DEFAULT = false;
	
	private DefaultJnjLaDeliveryDateUpdateService jnjLaDeliveryDateUpdateService;
	protected ConfigurationService configurationService;

	@Override
	protected void copyValues(final AbstractOrderEntryModel source, final AbstractOrderEntryModel target)
	{
		super.copyValues(source, target);

		if (null == source || null == target)
		{
			LOG.warn("[PAC HIVE] Source or target parameter was null. Exiting from the populator without any changes." +
			         " Source: '{}', target: '{}'.", source, target);
			return;
		}

		if(CollectionUtils.isNotEmpty(source.getJnjPacHiveEntries())) {
			target.setJnjPacHiveEntries(source.getJnjPacHiveEntries());
		}else {
			final boolean isPACOrphanSyncEnabled = this.configurationService.getConfiguration().getBoolean(
					PAC_ORPHAN_SYNC_ENABLED, PAC_ORPHAN_SYNC_ENABLED_DEFAULT);
			
			LOG.debug("[PAC HIVE]  Orphan PAC HIVE Entry sync is enabled: ''{}'", isPACOrphanSyncEnabled);
			
			if(isPACOrphanSyncEnabled) {
				//making DB call to fetch and map Orphan(unlinked) PAC-Entries if available for given Order Entry.
				LOG.info("[PAC HIVE] inside PacHiveOrderEntryCloneHelper, Entry PacHive entries are found as null, Going to find and map any Orphan PacEntry available for given order entry: '{}',", source.getPk());
				
				final List<JnjPacHiveEntryModel> orphanPacEntries=jnjLaDeliveryDateUpdateService.findOrphanJnjPacHiveEntryModels(source);
				if(CollectionUtils.isNotEmpty(orphanPacEntries)) {
					target.setJnjPacHiveEntries(orphanPacEntries);
				}
			}
		}
		
		
		if (CollectionUtils.isNotEmpty(target.getJnjPacHiveEntries()))
		{
			for (JnjPacHiveEntryModel jnjPacHiveEntry : target.getJnjPacHiveEntries())
			{
				jnjPacHiveEntry.setOrderEntry(target);
			}

			LOG.debug("[PAC HIVE] Cloned PAC HIVE entries for the source order entry: '{}',", source.getPk());
		}
		else
		{
			LOG.debug(
					"[PAC HIVE] There is no PAC HIVE entries to clone for the source order entry: '{}',",
					source.getPk()
			);
		}
	}
	
	public void setJnjLaDeliveryDateUpdateService(final DefaultJnjLaDeliveryDateUpdateService jnjLaDeliveryDateUpdateService) {
		this.jnjLaDeliveryDateUpdateService = jnjLaDeliveryDateUpdateService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
