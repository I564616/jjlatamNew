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


public class JnjSyncECProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_EC_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync EC Prod Stage >>>> Online Job", JnjSyncECProdStagedToOnline.class);

		final CronJobModel syncECProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_EC_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncECProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncECProdStgToOnline);
		cronJobService.performCronJob(syncECProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_EC_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for EC Product Staged >>>> Online: " + syncECProdStgToOnline.getResult(),
				JnjSyncECProdStagedToOnline.class);
		return new PerformResult(syncECProdStgToOnline.getResult(), syncECProdStgToOnline.getStatus());
	}
}