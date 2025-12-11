package com.jnj.la.cronjobs;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnJProductService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;


/**
 * This class is used to sync Mexico product catalog staged to online.
 */
public class JnjSyncMXProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
{
	@Autowired
	private CronJobService cronJobService;
	@Autowired
	private JnJProductService productService;
	@Autowired
	private CatalogVersionService catalogVersionService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final String METHOD_NAME = "perform";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_MX_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync MX Prod Stage >>>> Online Job", JnjSyncMXProdStagedToOnline.class);

		final CronJobModel syncMXProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_MX_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncMXProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncMXProdStgToOnline);
		cronJobService.performCronJob(syncMXProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_MX_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for MX Product Staged >>>> Online: " + syncMXProdStgToOnline.getResult(),
				JnjSyncMXProdStagedToOnline.class);
		return new PerformResult(syncMXProdStgToOnline.getResult(), syncMXProdStgToOnline.getStatus());
	}
}