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
 * Created by aadrian2 on 10/01/2017.
 */
public class JnjSyncCOProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
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
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CO_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync CO Prod Stage >>>> Online Job", JnjSyncCOProdStagedToOnline.class);

		final CronJobModel syncCOProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CO_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncCOProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncCOProdStgToOnline);
		cronJobService.performCronJob(syncCOProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CO_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for CO Product Staged >>>> Online: " + syncCOProdStgToOnline.getResult(),
				JnjSyncCOProdStagedToOnline.class);
		return new PerformResult(syncCOProdStgToOnline.getResult(), syncCOProdStgToOnline.getStatus());
	}
}
