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


public class JnjSyncARProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_AR_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync AR Prod Stage >>>> Online Job", JnjSyncARProdStagedToOnline.class);

		final CronJobModel syncARProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_AR_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncARProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncARProdStgToOnline);
		cronJobService.performCronJob(syncARProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_AR_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for AR Product Staged >>>> Online: " + syncARProdStgToOnline.getResult(),
				JnjSyncARProdStagedToOnline.class);
		return new PerformResult(syncARProdStgToOnline.getResult(), syncARProdStgToOnline.getStatus());
	}
}