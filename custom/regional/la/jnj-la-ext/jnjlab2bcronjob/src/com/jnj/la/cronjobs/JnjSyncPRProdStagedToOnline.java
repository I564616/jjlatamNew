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
 * Created by ccaetano on 25/01/2017.
 */

public class JnjSyncPRProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_PR_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync PR Prod Stage >>>> Online Job", JnjSyncPRProdStagedToOnline.class);

		final CronJobModel syncPRProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_PR_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncPRProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncPRProdStgToOnline);
		cronJobService.performCronJob(syncPRProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_PR_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for PR Product Staged >>>> Online: " + syncPRProdStgToOnline.getResult(),
				JnjSyncPRProdStagedToOnline.class);
		return new PerformResult(syncPRProdStgToOnline.getResult(), syncPRProdStgToOnline.getStatus());
	}
}
