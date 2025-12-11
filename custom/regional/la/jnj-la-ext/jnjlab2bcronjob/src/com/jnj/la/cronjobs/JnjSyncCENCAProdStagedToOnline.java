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


public class JnjSyncCENCAProdStagedToOnline extends AbstractJobPerformable<CronJobModel>
{
	@Autowired
	private CronJobService cronJobService;
	@Autowired
	private JnJProductService productService;
	@Autowired
	protected CatalogVersionService catalogVersionService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final String METHOD_NAME = "perform";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CENCA_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Inside perform Sync CENCA Prod Stage >>>> Online Job", JnjSyncCENCAProdStagedToOnline.class);

		final CronJobModel syncCENCAProdStgToOnline = cronJobService
				.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CENCA_PROD_STG_TO_ONLINE);
		((CatalogVersionSyncCronJobModel) syncCENCAProdStgToOnline).setScheduleMedias(null);
		modelService.save(syncCENCAProdStgToOnline);
		cronJobService.performCronJob(syncCENCAProdStgToOnline, true);

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CENCA_PROD_STG_TO_ONLINE, METHOD_NAME,
				"Sync Status for CENCA Product Staged >>>> Online: " + syncCENCAProdStgToOnline.getResult(),
				JnjSyncCENCAProdStagedToOnline.class);
		return new PerformResult(syncCENCAProdStgToOnline.getResult(), syncCENCAProdStgToOnline.getStatus());
	}
}