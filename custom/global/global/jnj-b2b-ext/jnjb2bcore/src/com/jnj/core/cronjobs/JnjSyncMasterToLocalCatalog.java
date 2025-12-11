package com.jnj.core.cronjobs;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjProductDao;
import com.jnj.core.services.JnJProductService;


public class JnjSyncMasterToLocalCatalog extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * 
	 */
	public static final String MEXICO_COUNTRY_CODE = Jnjb2bCoreConstants.UpsertProduct.MEXICO_COUNTRY_CODE;
	public static final String BRAZIL_COUNTRY_CODE = Jnjb2bCoreConstants.UpsertProduct.BRAZIL_COUNTRY_CODE;
	protected static final String CATALOG_VER_STAGED = Jnjb2bCoreConstants.UpsertProduct.CATALOG_VER_STAGED;
	protected static final String BRAZIL_PRODUCT_CATALOG = Jnjb2bCoreConstants.UpsertProduct.BRAZIL_PRODUCT_CATALOG;
	protected static final String MEXICO_PRODUCT_CATALOG = Jnjb2bCoreConstants.UpsertProduct.MEXICO_PRODUCT_CATALOG;

	protected static final Logger LOG = Logger.getLogger(JnjSyncMasterToLocalCatalog.class);

	@Autowired
	private CronJobService cronJobService;

	@Autowired
	private JnJProductService productService;
	@Autowired
	CatalogVersionService catalogVersionService;

	@Autowired
	JnjProductDao productDao;
	
	public CronJobService getCronJobService() {
		return cronJobService;
	}

	public JnJProductService getProductService() {
		return productService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public JnjProductDao getProductDao() {
		return productDao;
	}

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		LOG.debug("Inside perform SyncMasterToLocalCatalog Job");


		//Sync Product Master to Brazil
		final CronJobModel syncBrazilProcutCatalogCronJob = cronJobService
				.getCronJob(Jnjb2bCoreConstants.UpsertProduct.SYNC_CRON_JOB_BR);
		((CatalogVersionSyncCronJobModel) syncBrazilProcutCatalogCronJob).setScheduleMedias(null);
		modelService.save(syncBrazilProcutCatalogCronJob);
		cronJobService.performCronJob(syncBrazilProcutCatalogCronJob, true);

		LOG.info("Sync Status for Master Product to Brazil Product" + syncBrazilProcutCatalogCronJob.getResult());

		final CatalogVersionModel brazilProductCatalog = catalogVersionService.getCatalogVersion(BRAZIL_PRODUCT_CATALOG,
				CATALOG_VER_STAGED);
		//Update Product Status not valid for Brazil
		productService.updateInvalidProduct(brazilProductCatalog, BRAZIL_COUNTRY_CODE);

		//Sync Product Master to Mexico
		final CronJobModel syncMexicoProcutCatalogCronJob = cronJobService
				.getCronJob(Jnjb2bCoreConstants.UpsertProduct.SYNC_CRON_JOB_MX);
		((CatalogVersionSyncCronJobModel) syncMexicoProcutCatalogCronJob).setScheduleMedias(null);
		modelService.save(syncMexicoProcutCatalogCronJob);
		cronJobService.performCronJob(syncMexicoProcutCatalogCronJob, true);
		final CatalogVersionModel mexicoProductCatalog = catalogVersionService.getCatalogVersion(MEXICO_PRODUCT_CATALOG,
				CATALOG_VER_STAGED);
		//Update Product Status not valid for Mexico
		productService.updateInvalidProduct(mexicoProductCatalog, MEXICO_COUNTRY_CODE);
		LOG.info("Sync Status for Master Product to Mexico Product" + syncMexicoProcutCatalogCronJob.getResult());

		return new PerformResult(syncMexicoProcutCatalogCronJob.getResult(), syncMexicoProcutCatalogCronJob.getStatus());
	}
}