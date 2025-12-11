/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.b2b.store.setup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.impl.DefaultSetupSolrIndexerService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

@SystemSetup(extension = "jnjb2bstore")
public class JnjB2BStoreSystemSetup extends AbstractSystemSetup
{

	private static final Logger LOG = Logger.getLogger(JnjB2BStoreSystemSetup.class);

	
	@Autowired(required=true)
	private DefaultSetupSolrIndexerService defaultSetupSolrIndexerService;
		
	
	public static final String IMPORT_SITES = "importSites";
	public static final String IMPORT_GT = "importGT";

	public static final String SYNC_CONTENT_CATALOGS = "synContentCatalogs";
	public static final String SYNC_PRODUCT_CATALOGS = "syncProductsCatalogs";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String ACTIVATE_INTERFACE_CRON_JOBS = "activateInterfaceCronJobs";
	public static final String IMPORT_PRIVACY_POLICY = "privacyPolicy";

	protected static final String SYNC_MDD_CONT_STG_TO_ONLINE = "Sync-MDDDEContentStaged->MDDDEContentOnline";
	protected static final String SYNC_MSTR_CONT_STG_TO_MDD_CONT_STG = "Sync-MasterContentStaged->MDDDEContentCatalog";

	protected static final String SYNC_MDD_PROD_STG_TO_ONLINE = "Sync-MDDproductStaged->MDDproductOnline";


	protected static final String EPIC = "master";
	protected static final String MDD = "mdd";
	public static final String JNJ = "jnj";

	@Autowired
	private CronJobService cronJobService;
	@Autowired
	private ModelService modelService;

	public CronJobService getCronJobService() {
		return cronJobService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * This method will be called by system creator during initialization and system update. Be sure that this method can
	 * be called repeatedly.
	 * 
	 * @param context
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/countries.impex");
		importImpexFile(context, "/masterstore/import/common/countries.impex");
		LOG.debug("End jnjb2bstore- /masterstore/import/common/countries.impex");
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/essential-data.impex");
		importImpexFile(context, "/masterstore/import/common/essential-data.impex");
		LOG.debug("End jnjb2bstore-/masterstore/import/common/essential-data.impex");
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/themes.impex");
		//importImpexFile(context, "/masterstore/import/master/delivery-modes.impex");
		importImpexFile(context, "/masterstore/import/common/themes.impex");
		LOG.debug("End jnjb2bstore-/masterstore/import/common/themes.impex");
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/unit.impex");
		importImpexFile(context, "/masterstore/import/common/unit.impex");
		LOG.debug("End jnjb2bstore-/masterstore/import/common/unit.impex");
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/configurations.impex");
		importImpexFile(context, "/masterstore/import/common/configurations.impex");
		LOG.debug("End jnjb2bstore-/masterstore/import/common/configurations.impex");
		LOG.debug("Start jnjb2bstore-/masterstore/import/common/users.impex");
		importImpexFile(context, "/masterstore/import/common/users.impex");
		LOG.debug("End jnjb2bstore-/masterstore/import/common/users.impex");

	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();
		params.add(createBooleanSystemSetupParameter(IMPORT_GT, "Import GT (Please set GT YES if MDD is NO)", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_SITES, "Import Sites", true));
		params.add(createBooleanSystemSetupParameter(SYNC_CONTENT_CATALOGS, "Sync Content Catalogs", true));
		params.add(createBooleanSystemSetupParameter(SYNC_PRODUCT_CATALOGS, "Sync Products Catalogs", true));
		params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", true));
		params.add(createBooleanSystemSetupParameter(ACTIVATE_INTERFACE_CRON_JOBS, "Active interface Cron Jobs", false));
		params.add(createBooleanSystemSetupParameter(IMPORT_PRIVACY_POLICY, "Privacy Policy Update", true));
		return params;
	}

	/**
	 * This method will be called during the system initialization.
	 *
	 * @param context
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.PROJECT, process = Process.ALL)
	public void createProjectData(final SystemSetupContext context)
	{
		
		final boolean importGT = getBooleanSystemSetupParameter(context, IMPORT_GT);
		
		if(!importGT){
			LOG.debug("Start -importContentCatalog");
			importContentCatalog(context, JNJ, EPIC);
			LOG.debug("End -importContentCatalog");
			LOG.debug("Start -importOrderStatus");
			importOrderStatus(context);
			LOG.debug("End -importOrderStatus");
			LOG.debug("Start -importShippingMethods");
			importShippingMethods(context);
			LOG.debug("End -importShippingMethods");
			LOG.debug("Start -importShipmentTrackingURLs");
			importShipmentTrackingURLs(context);
			LOG.debug("End -importShipmentTrackingURLs");
		}
		
		if(importGT){
		final boolean importSites = getBooleanSystemSetupParameter(context, IMPORT_SITES);
		final boolean syncContCatalogs = getBooleanSystemSetupParameter(context, SYNC_CONTENT_CATALOGS);
		final boolean syncProdCatalogs = getBooleanSystemSetupParameter(context, SYNC_PRODUCT_CATALOGS);

		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();

		if (importAccessRights && extensionNames.contains("jnjb2bstore"))
		{
			LOG.debug("Start -/masterstore/import/common/users.impex");
			importImpexFile(context, "/masterstore/import/common/users.impex");
			LOG.debug("End -/masterstore/import/common/users.impex");
		}

		if (importSites)
		{
			LOG.debug("Start -importProductCatalog");
			importProductCatalog(context, JNJ, EPIC);
			LOG.debug("End -importProductCatalog");
			LOG.debug("Start - importProductTestData");
			importTestData(context, JNJ);
			LOG.debug("End - importProductTestData");
			LOG.debug("Start -importContentCatalog");
			importContentCatalog(context, JNJ, EPIC);
			LOG.debug("End -importContentCatalog");
			LOG.debug("Start -importStore");
			importStore(context, JNJ, EPIC);
			LOG.debug("End -importStore");
			LOG.debug("Start -createAndActivateSolrIndex");
			createAndActivateSolrIndex(context, EPIC, MDD);
			LOG.debug("End -createAndActivateSolrIndex");
			
		}
	
		//Run sync job if any of the catalog is chosen for sync
		if (syncContCatalogs || syncProdCatalogs)
		{
			executeCatalogSyncJob(context, "mstrContentCatalog");
		}

		if (importAccessRights && extensionNames.contains("cmscockpit"))
		{
			importImpexFile(context, "/masterstore/import/master/cockpits/cmscockpit/cmscockpit-users.impex");
			importImpexFile(context, "/masterstore/import/master/cockpits/cmscockpit/cmscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("btgcockpit"))
		{
			importImpexFile(context, "/masterstore/import/master/cockpits/cmscockpit/btgcockpit-users.impex");
			importImpexFile(context, "/masterstore/import/master/cockpits/cmscockpit/btgcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("productcockpit"))
		{
			importImpexFile(context, "/masterstore/import/master/cockpits/productcockpit/productcockpit-users.impex");
			importImpexFile(context, "/masterstore/import/master/cockpits/productcockpit/productcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/masterstore/import/master/cockpits/cscockpit/cscockpit-users.impex");
			importImpexFile(context, "/masterstore/import/master/cockpits/cscockpit/cscockpit-access-rights.impex");
		}

		if (extensionNames.contains("mcc"))
		{
			//importImpexFile(context, "/masterstore/import/common/mcc-sites-links.impex");
		}

		//Importing the CronJobs
		importCronJobs(context, JNJ, getBooleanSystemSetupParameter(context, ACTIVATE_INTERFACE_CRON_JOBS));
		//importTestData(context, JNJ);
		}
	}


	@Override
	public PerformResult executeCatalogSyncJob(final SystemSetupContext context, final String catalogName)
	{

		final boolean syncContCatalogs = getBooleanSystemSetupParameter(context, SYNC_CONTENT_CATALOGS);
		final boolean syncProdCatalogs = getBooleanSystemSetupParameter(context, SYNC_PRODUCT_CATALOGS);

		final PerformResult syncCronJobResult = null;
		if (syncContCatalogs || StringUtils.equals(catalogName, JNJ))
		{
			


			/** MasterContent to MDDContentStaged */
			logInfo(context, "Executing catalogs sync job  [MasterContent to MDDContentStaged]");
			final CronJobModel syncMasterToMDDContentCronJob = cronJobService.getCronJob(SYNC_MSTR_CONT_STG_TO_MDD_CONT_STG);
			((CatalogVersionSyncCronJobModel) syncMasterToMDDContentCronJob).setScheduleMedias(null);
			modelService.save(syncMasterToMDDContentCronJob);
			cronJobService.performCronJob(syncMasterToMDDContentCronJob, true);
			logInfo(context, "Executed catalogs sync job  [MasterContent to MDDContentStaged] Status:- "
					+ syncMasterToMDDContentCronJob.getResult());

			if (StringUtils.equals(catalogName, JNJ))
			{
				importImpexFile(context, "/masterstore/import/master/contentCatalogs/" + catalogName
						+ "ContentCatalog/cms-content-mdd.impex", false);
				/*importImpexFile(context, "/masterstore/import/master/contentCatalogs/" + catalogName
						+ "ContentCatalog/cms-content-cons.impex", false);*/
			}
			if (!StringUtils.equals(catalogName, JNJ))
			{
				/** MDDContentStaged to MDDContentOnline */
				logInfo(context, "Executing catalogs sync job  [MDDContentStaged to MDDContentOnline]");
				final CronJobModel syncMDDContentStagedToOnlineCronJob = cronJobService.getCronJob(SYNC_MDD_CONT_STG_TO_ONLINE);
				((CatalogVersionSyncCronJobModel) syncMDDContentStagedToOnlineCronJob).setScheduleMedias(null);
				modelService.save(syncMDDContentStagedToOnlineCronJob);
				cronJobService.performCronJob(syncMDDContentStagedToOnlineCronJob, true);
				logInfo(context, "Executed catalogs sync job  [MasterContent to MDDContentStaged] Status:- "
						+ syncMDDContentStagedToOnlineCronJob.getResult());
			}

		}

		if (syncProdCatalogs)
		{
			/** mddProductStaged to mddProductOnline */
			logInfo(context, "Executing catalogs sync job  [mddProductStaged to mddProductOnline]");
			final CronJobModel syncMDDProductStagedToOnlineCronJob = cronJobService.getCronJob(SYNC_MDD_PROD_STG_TO_ONLINE);
			((CatalogVersionSyncCronJobModel) syncMDDProductStagedToOnlineCronJob).setScheduleMedias(null);
			modelService.save(syncMDDProductStagedToOnlineCronJob);
			cronJobService.performCronJob(syncMDDProductStagedToOnlineCronJob, true);
			logInfo(context, "Executed catalogs sync job  [mddProductStaged to mddProductOnline] Status:- "
					+ syncMDDProductStagedToOnlineCronJob.getResult());
		}
		
		return syncCronJobResult;
	}

	protected void importProductCatalog(final SystemSetupContext context, final String catalogName, final String projectType)
	{
		logInfo(context, "Begin importing catalog [" + catalogName + "]");
		String prefix =  "/masterstore/import/master/";

		importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/catalog.impex", true);
		importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/catalog_en.impex", true);

		//importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/cons_rootCategories.impex", true);
		if (projectType.equals(EPIC))
		{
			importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/mdd_rootCategories.impex", true);
		}

		importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/productCatalogSyncJob.impex", true);
	}

	protected void createAndActivateSolrIndex(final SystemSetupContext context, final String storeName, final String projectType)
	{
		logInfo(context, "Begin SOLR index setup [" + storeName + " " + projectType + "]");

		importImpexFile(context, "/masterstore/import/" + storeName + "/stores/" + projectType + "/solr.impex");
		importImpexFile(context, "/masterstore/import/" + storeName + "/stores/" + projectType + "/solr_en.impex");

		logInfo(context, "Done SOLR index setup [" + storeName + " " + projectType + "]");
		
		logInfo(context, "Begin SOLR indexing --- [" + storeName + " " + projectType + "]");
		defaultSetupSolrIndexerService.executeSolrIndexerCronJob("jnjMddIndex", true);

		logInfo(context, "Done SOLR indexing --- [" + storeName + " " + projectType + "]");

	}

	protected void importContentCatalog(final SystemSetupContext context, final String catalogName, final String projectType)
	{
		logInfo(context, "Begin importing catalog [" + catalogName + "]for :" + projectType);
		String prefix =  "/masterstore/import/master/";
		final boolean updatePrivacyPolicy = getBooleanSystemSetupParameter(context, IMPORT_PRIVACY_POLICY);
		
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/catalog.impex", true);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/catalog_en.impex", true);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content.impex", false);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content_en.impex", false);

		if (updatePrivacyPolicy)
		{
			importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content_privacyPolicy.impex",
					false);
		}


		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/messages.impex", false);

		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/emails/email-content.impex", false);

		//if (projectType.equals(EPIC))
		//{
		//	executeCatalogSyncJob(context, JNJ);
		//}
		final boolean importGT = getBooleanSystemSetupParameter(context, IMPORT_GT);
		if(importGT){
		 logInfo(context, "Importing catalogs sync job  [MasterContent to MDDContentStaged]");
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/contentCatalogSyncJob.impex", false);
		}
		logInfo(context, "Done importing catalog [" + catalogName + "] for :" + projectType);
	}

	protected void importStore(final SystemSetupContext context, final String storeName, final String projectType)
	{
		logInfo(context, "Begin importing store [" + storeName + "]");
		String prefix =  "/masterstore/import/master/";

		
		importImpexFile(context, prefix + "stores/store.impex");
		importImpexFile(context, prefix + "stores/site.impex");

		logInfo(context, "Done importing store [" + storeName + "]");
	}

	protected void importOrderStatus(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of orderStatusMapping & orderEntryStatusMapping");

		importImpexFile(context, "/masterstore/import/master/order/orderStatusMapping.impex");
		importImpexFile(context, "/masterstore/import/master/order/orderEntryStatusMapping.impex");

		logInfo(context, "Done importing impex of orderStatusMapping & orderEntryStatusMapping");
	}

	protected void importShippingMethods(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of Shipping methods");
		importImpexFile(context, "/masterstore/import/master/order/shippingMethods.impex");
		logInfo(context, "Done importing impex of Shipping methods");
	}

	protected void importShipmentTrackingURLs(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of Shipment Tracking URLs");
		importImpexFile(context, "/masterstore/import/master/externaldata/shipmenttracking/shipmenttracking.impex");
		logInfo(context, "Done importing impex of Shipment Tracking URLs");
	}


	protected void importCronJobs(final SystemSetupContext context, final String catalogName, final boolean active)
	{
		logInfo(context, "Begin importing CronJobs [" + catalogName + "]");
		//importImpexFile(context, "/masterstore/import/common/JnjCronJobs.impex", true);
		if (active)
		{
			//importImpexFile(context, "/masterstore/import/common/JnjCronJobsATriger.impex", true);
		}
		else
		{
			//importImpexFile(context, "/masterstore/import/common/JnjCronJobsDTriger.impex", true);
		}

	}

	protected void importTestData(final SystemSetupContext context, final String catalogName)
	{
		logInfo(context, "Begin jnjb2bstore importing TestData [" + catalogName + "]");

		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-bonecement.impex");
		importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-disposable.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-extremities.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-hips.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-instdepuyspecific.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-instgeneralpurpose.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-knees.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-lps.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-ormedenvironmental.impex");
		//importImpexFile(context, "/masterstore/import/master/externaldata/depuy/depuy-shoulder.impex");

		logInfo(context, "Done jnjb2bstore importing TestData [" + catalogName + "]");
	}

	public DefaultSetupSolrIndexerService getDefaultSetupSolrIndexerService() {
		return defaultSetupSolrIndexerService;
	}

	public void setDefaultSetupSolrIndexerService(
			DefaultSetupSolrIndexerService defaultSetupSolrIndexerService) {
		this.defaultSetupSolrIndexerService = defaultSetupSolrIndexerService;
	}

	
}


