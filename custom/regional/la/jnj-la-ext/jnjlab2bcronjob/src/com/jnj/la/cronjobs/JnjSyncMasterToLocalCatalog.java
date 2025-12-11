package com.jnj.la.cronjobs;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjProductDao;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.util.JnjLaCoreUtil;




/**
 * The Class JnjSyncMasterToLocalCatalog.
 */
public class JnjSyncMasterToLocalCatalog extends AbstractJobPerformable<CronJobModel>
{
	/** The Constant JNJ_SYNC_MASTER_TO_LOCAL_FORCE_UPDATE. */
	private static final String JNJ_SYNC_MASTER_TO_LOCAL_FORCE_UPDATE = "jnj.sync.masterToLocal.forceUpdate";

	/** The cron job service. */
	@Autowired
	private CronJobService cronJobService;

	/** The product service. */
	@Autowired
	private JnJLaProductService productService;

	/** The catalog version service. */
	@Autowired
	private CatalogVersionService catalogVersionService;

	/** The product dao. */
	@Autowired
	private JnjProductDao productDao;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final String methodName = "perform()";
		JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName, Logging.BEGIN_OF_METHOD,
				JnjSyncMasterToLocalCatalog.class);

		final String forceUpdate = Config.getParameter(JNJ_SYNC_MASTER_TO_LOCAL_FORCE_UPDATE);

		// Get the list of all stores
		final List<String> storeIsoList = JnjLaCoreUtil.getCountriesList(Jnjlab2bcoreConstants.KEY_VALID_STORES);

		CronJobModel syncProductMasterStgToLocalStg = null;
		// Syncronize products from master stg catalog to local stg catalog
		for (final String storeIso : storeIsoList)
		{
			try
			{
				syncProductMasterStgToLocalStg = syncProductMasterStgToLocalStg(forceUpdate,
						JnjLaCommonUtil.getIdByCountry(storeIso).toUpperCase());
			}
			catch (final Exception e)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SYNC_PRODUCT_MASTER, methodName, e.getMessage(), e,
						JnjSyncMasterToLocalCatalog.class);
			}

		}

		if (syncProductMasterStgToLocalStg != null)
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName, Logging.END_OF_METHOD,
					JnjSyncMasterToLocalCatalog.class);
			return new PerformResult(syncProductMasterStgToLocalStg.getResult(), syncProductMasterStgToLocalStg.getStatus());
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName, Logging.END_OF_METHOD,
					JnjSyncMasterToLocalCatalog.class);
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
		}
	}

	/**
	 * @param forceUpdate
	 * @return CronJobModel
	 *
	 */
	private CronJobModel syncProductMasterStgToLocalStg(final String forceUpdate, final String storeIso)
	{
		final String methodName = "syncProductMasterStgToLocalStg";
		final CronJobModel syncLocalProductCatalogCronJob;

		JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName,
				Logging.BEGIN_OF_METHOD + "Sync Status for Master Product to " + storeIso, JnjSyncMasterToLocalCatalog.class);

		String countryName;
		String storeIsoForInvalidProductSearch = storeIso;
		if (Jnjlab2bcoreConstants.CENCA_SITE_NAME.equals(storeIso)) {
			countryName = StringUtils.capitalize(storeIso.toLowerCase());
			storeIsoForInvalidProductSearch = Jnjb2bCoreConstants.COUNTRY_ISO_PANAMA;
		} else if (Jnjlab2bcoreConstants.COUNTRY_ISO_PUERTORICO.equalsIgnoreCase(storeIso)) {
			countryName = Jnjlab2bcoreConstants.UpsertProduct.PR_SYNC_CATALOG_PREFIX;
		} else {
			final CountryModel country = productService.getCountryModelByIsoOrPk(storeIso);

			// Stops the execution for this specific catalog if no country is found
			if (null == country)
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName, Logging.BEGIN_OF_METHOD
								+ "Could not define sync job for store" + storeIso + ". No country found in Country table.",
						JnjSyncMasterToLocalCatalog.class);
				return null;
			}
			countryName = country.getName();
		}

		// Get cronjob
		syncLocalProductCatalogCronJob = cronJobService.getCronJob(Jnjlab2bcoreConstants.UpsertProduct.SYNC_CRON_JOB_PREFIX
				+ countryName + Jnjb2bCoreConstants.STAGED);
		((CatalogVersionSyncCronJobModel) syncLocalProductCatalogCronJob).setScheduleMedias(null);

		if (forceUpdate != null && StringUtils.equalsIgnoreCase(forceUpdate, "true"))
		{
			((CatalogVersionSyncCronJobModel) syncLocalProductCatalogCronJob).setForceUpdate(Boolean.TRUE);
		}
		else
		{
			((CatalogVersionSyncCronJobModel) syncLocalProductCatalogCronJob).setForceUpdate(Boolean.FALSE);
		}
		modelService.save(syncLocalProductCatalogCronJob);
		cronJobService.performCronJob(syncLocalProductCatalogCronJob, true);

		JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName,
				"Sync Status for Master Product to " + storeIso + " Product" + syncLocalProductCatalogCronJob.getResult(),
				JnjSyncMasterToLocalCatalog.class);

		final String productCatalog = storeIso.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;
		final CatalogVersionModel localProductCatalog = catalogVersionService.getCatalogVersion(productCatalog,
				Jnjb2bCoreConstants.STAGED);

		//Update Product Status not valid for country
		productService.updateInvalidProduct(localProductCatalog, storeIsoForInvalidProductSearch);

		JnjGTCoreUtil.logInfoMessage(Logging.SYNC_PRODUCT_MASTER, methodName,
				Logging.BEGIN_OF_METHOD + "Sync Status for Master Product to" + storeIso, JnjSyncMasterToLocalCatalog.class);
		return syncLocalProductCatalogCronJob;
	}
}