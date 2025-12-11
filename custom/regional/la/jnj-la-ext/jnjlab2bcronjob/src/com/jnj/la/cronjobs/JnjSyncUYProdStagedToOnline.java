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


public class JnjSyncUYProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_UY_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync UY Prod Stage >>>> Online Job", JnjSyncUYProdStagedToOnline.class);

		final CronJobModel syncUYProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_UY_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncUYProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncUYProdStgToOnline);
		cronJobService.performCronJob(syncUYProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_UY_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for UY Product Staged >>>> Online: " + syncUYProdStgToOnline.getResult(),
				JnjSyncUYProdStagedToOnline.class);
		return new PerformResult(syncUYProdStgToOnline.getResult(), syncUYProdStgToOnline.getStatus());
	}
}