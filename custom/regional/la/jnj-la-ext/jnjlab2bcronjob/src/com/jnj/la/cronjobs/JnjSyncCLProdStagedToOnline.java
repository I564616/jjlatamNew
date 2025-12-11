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


public class JnjSyncCLProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CL_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync CL Prod Stage >>>> Online Job", JnjSyncCLProdStagedToOnline.class);

		final CronJobModel syncCLProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CL_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncCLProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncCLProdStgToOnline);
		cronJobService.performCronJob(syncCLProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CL_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for CL Product Staged >>>> Online: " + syncCLProdStgToOnline.getResult(),
				JnjSyncCLProdStagedToOnline.class);
		return new PerformResult(syncCLProdStgToOnline.getResult(), syncCLProdStgToOnline.getStatus());
	}
}