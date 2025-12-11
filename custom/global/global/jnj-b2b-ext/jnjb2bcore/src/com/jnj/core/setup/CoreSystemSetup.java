/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.jnj.core.setup;

import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
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
import de.hybris.platform.validation.services.ValidationService;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

//import com.jnj.core.constants.Jnjb2bCoreConstants;


/**
 * This class provides hooks into the system's initialization and update processes.
 * 
 * @see "https://wiki.hybris.com/display/release4/Hooks+for+Initialization+and+Update+Process"
 */
@SystemSetup(extension = Jnjb2bCoreConstants.EXTENSIONNAME)
public class CoreSystemSetup extends AbstractSystemSetup
{

	public static final String IMPORT_SITES = "importSites";
	public static final String SYNC_CONTENT_CATALOGS = "synContentCatalogs";
	public static final String SYNC_PRODUCT_CATALOGS = "syncProductsCatalogs";
	public static final String IMPORT_ACCESS_RIGHTS = "accessRights";
	public static final String ACTIVATE_SOLR_CRON_JOBS = "activateSolrCronJobs";
	public static final String ACTIVATE_INTERFACE_CRON_JOBS = "activateInterfaceCronJobs";
	//public static final String IMPORT_PCM_DATA = "importPcmData";
	//public static final String IMPORT_PCM__FOOTER_DATA = "importPcmFooterData";
	public static final String IMPORT_PRIVACY_POLICY = "privacyPolicy";

	protected static final String SYNC_MDD_CONT_STG_TO_ONLINE = "Sync-MDDContentStaged->MDDContentOnline";
	protected static final String SYNC_CONS_CONT_STG_TO_ONLINE = "SyncCONSContentStaged->CONSContentOnline";
	protected static final String SYNC_PCM_CONT_STG_TO_ONLINE = "Sync-PCMContCatalogStaged->PCMContCatalogOnline";
	protected static final String SYNC_MSTR_CONT_STG_TO_MDD_CONT_STG = "Sync-MasterContentStaged->MDDContentCatalog";
	protected static final String SYNC_MSTR_CONT_STG_TO_CONS_CONT_STG = "Sync-MasterContentStaged->CONSContentCatalog";


	//protected static final String EPIC_PREFIX_PATH = "/jnjb2bcore/import/epic/";
	//protected static final String PCM_PREFIX_PATH = "/jnjb2bcore/import/pcm/";
	//protected static final String EPIC = "epic";
	//protected static final String PCM = "pcm";
	protected static final String MDD = "mdd";
	//protected static final String CONS = "cons";
	//protected static final String CA = "ca";
	//protected static final String US = "us";
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
	 *           the context provides the selected parameters and values
	 */
	@SystemSetup(type = Type.ESSENTIAL, process = Process.ALL)
	public void createEssentialData(final SystemSetupContext context)
	{
		//importImpexFile(context, "/jnjb2bcore/import/common/countries.impex");
		//importImpexFile(context, "/jnjb2bcore/import/common/essential-data.impex");
		//importImpexFile(context, "/jnjb2bcore/import/epic/delivery-modes.impex");
		//importImpexFile(context, "/jnjb2bcore/import/common/themes.impex");
		//importImpexFile(context, "/jnjb2bcore/import/common/unit.impex");
		//importImpexFile(context, "/jnjb2bcore/import/common/configurations.impex");
	}

	/**
	 * Generates the Dropdown and Multi-select boxes for the project data import
	 */
	@Override
	@SystemSetupParameterMethod
	public List<SystemSetupParameter> getInitializationOptions()
	{
		final List<SystemSetupParameter> params = new ArrayList<SystemSetupParameter>();

		//params.add(createBooleanSystemSetupParameter(IMPORT_SITES, "Import Sites", false));
		//params.add(createBooleanSystemSetupParameter(SYNC_CONTENT_CATALOGS, "Sync Content Catalogs", false));
		//params.add(createBooleanSystemSetupParameter(SYNC_PRODUCT_CATALOGS, "Sync Products Catalogs", false));
		//params.add(createBooleanSystemSetupParameter(IMPORT_ACCESS_RIGHTS, "Import Users & Groups", false));
		//params.add(createBooleanSystemSetupParameter(ACTIVATE_SOLR_CRON_JOBS, "Activate Solr Cron Jobs", false));
		//params.add(createBooleanSystemSetupParameter(ACTIVATE_INTERFACE_CRON_JOBS, "Active interface Cron Jobs", false));
		//params.add(createBooleanSystemSetupParameter(IMPORT_PRIVACY_POLICY, "Privacy Policy Update", false));
		//params.add(createBooleanSystemSetupParameter(IMPORT_PCM_DATA, "Import PCM Data", false));
		//.add(createBooleanSystemSetupParameter(IMPORT_PCM__FOOTER_DATA, "Import PCM Footer Data", false));


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
		/*final boolean importSites = getBooleanSystemSetupParameter(context, IMPORT_SITES);
		final boolean syncContCatalogs = getBooleanSystemSetupParameter(context, SYNC_CONTENT_CATALOGS);
		final boolean syncProdCatalogs = getBooleanSystemSetupParameter(context, SYNC_PRODUCT_CATALOGS);

		//final boolean importPcmData = getBooleanSystemSetupParameter(context, IMPORT_PCM_DATA);

		final boolean importAccessRights = getBooleanSystemSetupParameter(context, IMPORT_ACCESS_RIGHTS);
		final List<String> extensionNames = Registry.getCurrentTenant().getTenantSpecificExtensionNames();

		if (importAccessRights && extensionNames.contains("jnjb2bcore"))
		{
			importImpexFile(context, "/jnjb2bcore/import/common/users.impex");
		}

		if (importSites)
		{
			//importProductCatalog(context, JNJ, EPIC);

			//importContentCatalog(context, JNJ, EPIC);

			//importStore(context, JNJ, EPIC);

			//importOrderStatus(context);

			//createAndActivateSolrIndex(context, EPIC, MDD);
			//createAndActivateSolrIndex(context, EPIC, CONS);

			//importShippingMethods(context);
			//importShipmentTrackingURLs(context);

			((ValidationService) Registry.getApplicationContext().getBean("validationService")).reloadValidationEngine();
		}
		if (importPcmData)
		{
			importProductCatalog(context, JNJ, PCM);
			importContentCatalog(context, JNJ, PCM);
			importStore(context, JNJ, PCM);
			createAndActivateSolrIndex(context, PCM, CA);
			createAndActivateSolrIndex(context, PCM, US);
		}

		//Run sync job if any of the catalog is chosen for sync
		if (syncContCatalogs || syncProdCatalogs)
		{
			executeCatalogSyncJob(context, "mstrContentCatalog");
		}

		if (importAccessRights && extensionNames.contains("cmscockpit"))
		{
			// Import cms cockpit impexes specific to PCM
			if (importPcmData)
			{
				importImpexFile(context, "/jnjb2bcore/import/pcm/cockpits/cmscockpit/cmscockpit-users.impex");
				importImpexFile(context, "/jnjb2bcore/import/pcm/cockpits/cmscockpit/cmscockpit-access-rights.impex");
			}
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cmscockpit/cmscockpit-users.impex");
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cmscockpit/cmscockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("btgcockpit"))
		{
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cmscockpit/btgcockpit-users.impex");
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cmscockpit/btgcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("productcockpit"))
		{
			// Import product cockpit impexes specific to PCM
			if (importPcmData)
			{
				importImpexFile(context, "/jnjb2bcore/import/pcm/cockpits/productcockpit/productcockpit-users.impex");
				importImpexFile(context, "/jnjb2bcore/import/pcm/cockpits/productcockpit/productcockpit-access-rights.impex");
				//importPCMWorkflows(context, JNJ, PCM);
			}
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/productcockpit/productcockpit-users.impex");
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/productcockpit/productcockpit-access-rights.impex");
		}

		if (importAccessRights && extensionNames.contains("cscockpit"))
		{
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cscockpit/cscockpit-users.impex");
			importImpexFile(context, "/jnjb2bcore/import/epic/cockpits/cscockpit/cscockpit-access-rights.impex");
		}

		if (extensionNames.contains("mcc"))
		{
			importImpexFile(context, "/jnjb2bcore/import/common/mcc-sites-links.impex");
		}

		//Importing the CronJobs
		importCronJobs(context, JNJ, getBooleanSystemSetupParameter(context, ACTIVATE_INTERFACE_CRON_JOBS));
		importTestData(context, JNJ);*/

	}
/*
	private void importPCMWorkflows(final SystemSetupContext context, final String storeName, final String projectType)
	{
		logInfo(context, "Begin importing PCM Workflows for store [" + storeName + "]");
		String prefix = EPIC_PREFIX_PATH;

		if (projectType.equals(PCM))
		{
			prefix = PCM_PREFIX_PATH;
		}
		importImpexFile(context, prefix + "workflows/Jnj_CA_PCM_NewProduct_WorkFlow.impex");
		importImpexFile(context, prefix + "workflows/Jnj_CA_PCM_UpdateProduct_WorkFlow.impex");
		importImpexFile(context, prefix + "workflows/Jnj_US_PCM_NewProduct_WorkFlow.impex");
		importImpexFile(context, prefix + "workflows/Jnj_US_PCM_UpdateProduct_WorkFlow.impex");
		logInfo(context, "Done importing PCM Workflows for store [" + storeName + "]");

	}
*/
	@Override
	public PerformResult executeCatalogSyncJob(final SystemSetupContext context, final String catalogName)
	{
		//final boolean syncContCatalogs = getBooleanSystemSetupParameter(context, SYNC_CONTENT_CATALOGS);
		//final boolean syncProdCatalogs = getBooleanSystemSetupParameter(context, SYNC_PRODUCT_CATALOGS);

		final PerformResult syncCronJobResult = null;
		/*if (syncContCatalogs || StringUtils.equals(catalogName, JNJ))
		{

			*//** MasterContent to ConsumerContentStaged *//*
			logInfo(context, "Executing catalogs sync job  [MasterContent to ConsumerContentStaged]");
			final CronJobModel syncMasterToConsContentCronJob = cronJobService.getCronJob(SYNC_MSTR_CONT_STG_TO_CONS_CONT_STG);
			((CatalogVersionSyncCronJobModel) syncMasterToConsContentCronJob).setScheduleMedias(null);
			modelService.save(syncMasterToConsContentCronJob);
			cronJobService.performCronJob(syncMasterToConsContentCronJob, true);
			logInfo(context, "Executed catalogs sync job  [MasterContent to ConsumerContentStaged] Status:- "
					+ syncMasterToConsContentCronJob.getResult());

			*//** MasterContent to MDDContentStaged *//*
			logInfo(context, "Executing catalogs sync job  [MasterContent to MDDContentStaged]");
			final CronJobModel syncMasterToMDDContentCronJob = cronJobService.getCronJob(SYNC_MSTR_CONT_STG_TO_MDD_CONT_STG);
			((CatalogVersionSyncCronJobModel) syncMasterToMDDContentCronJob).setScheduleMedias(null);
			modelService.save(syncMasterToMDDContentCronJob);
			cronJobService.performCronJob(syncMasterToMDDContentCronJob, true);
			logInfo(context, "Executed catalogs sync job  [MasterContent to MDDContentStaged] Status:- "
					+ syncMasterToMDDContentCronJob.getResult());

			if (StringUtils.equals(catalogName, JNJ))
			{
				importImpexFile(context, "/jnjb2bcore/import/epic/contentCatalogs/" + catalogName
						+ "ContentCatalog/cms-content-mdd.impex", false);
				importImpexFile(context, "/jnjb2bcore/import/epic/contentCatalogs/" + catalogName
						+ "ContentCatalog/cms-content-cons.impex", false);
			}
			if (!StringUtils.equals(catalogName, JNJ))
			{
				*//** ConsumerContentStaged to ConsumerContentOnline *//*
				logInfo(context, "Executing catalogs sync job  [ConsumerContentStaged to ConsumerContentOnline]");
				final CronJobModel syncConsumerContentStagedToOnlineCronJob = cronJobService.getCronJob(SYNC_CONS_CONT_STG_TO_ONLINE);
				((CatalogVersionSyncCronJobModel) syncConsumerContentStagedToOnlineCronJob).setScheduleMedias(null);
				modelService.save(syncConsumerContentStagedToOnlineCronJob);
				cronJobService.performCronJob(syncConsumerContentStagedToOnlineCronJob, true);
				logInfo(context, "Executed catalogs sync job  [MasterContent to MDDContentStaged] Status:- "
						+ syncConsumerContentStagedToOnlineCronJob.getResult());

				*//** MDDContentStaged to MDDContentOnline *//*
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
			*//** MasterProductStaged to brProductStaged *//*
		}*/
		return syncCronJobResult;
	}

	protected void importProductCatalog(final SystemSetupContext context, final String catalogName, final String projectType)
	{
		//logInfo(context, "Begin importing catalog [" + catalogName + "]");
		//String prefix = EPIC_PREFIX_PATH;

		/*if (projectType.equals(PCM))
		{
			prefix = PCM_PREFIX_PATH;
		}*/

		/*importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/catalog.impex", true);
		importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/catalog_en.impex", true);

		importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/cons_rootCategories.impex", true);*/
		/*if (projectType.equals(EPIC))
		{
			importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/mdd_rootCategories.impex", true);
		}*/

		//importImpexFile(context, prefix + "productCatalogs/" + catalogName + "ProductCatalog/productCatalogSyncJob.impex", true);
		//createProductCatalogSyncJob(context, catalogName + "ProductCatalog");

/*
		if (projectType.equals(PCM))
		{
			importImpexFile(context, prefix + "dm/ca_Catagories.impex", true);
		}*/

	}

	protected void createAndActivateSolrIndex(final SystemSetupContext context, final String storeName, final String projectType)
	{
		/*logInfo(context, "Begin SOLR index setup [" + storeName + " " + projectType + "]");

		importImpexFile(context, "/jnjb2bcore/import/" + storeName + "/stores/" + projectType + "/solr.impex");
		importImpexFile(context, "/jnjb2bcore/import/" + storeName + "/stores/" + projectType + "/solr_en.impex");

		logInfo(context, "Done SOLR index setup [" + storeName + " " + projectType + "]");*/
	}

	protected void importContentCatalog(final SystemSetupContext context, final String catalogName, final String projectType)
	{
		//logInfo(context, "Begin importing catalog [" + catalogName + "]for :" + projectType);
		//String prefix = EPIC_PREFIX_PATH;
		//final boolean updatePrivacyPolicy = getBooleanSystemSetupParameter(context, IMPORT_PRIVACY_POLICY);
		//final boolean importPcmFooterData = getBooleanSystemSetupParameter(context, IMPORT_PCM__FOOTER_DATA);
		/*if (projectType.equals(PCM))
		{
			prefix = PCM_PREFIX_PATH;
		}*/
		/*importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/catalog.impex", true);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/catalog_en.impex", true);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content.impex", false);
		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content_en.impex", false);
*/
		/*if (updatePrivacyPolicy)
		{
			importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-content_privacyPolicy.impex",
					false);
		}
		if (importPcmFooterData && projectType.equals(PCM))
		{
			importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/cms-footer-content.impex", false);
		}


		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/messages.impex", false);

		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/emails/email-content.impex", false);

		importImpexFile(context, prefix + "contentCatalogs/" + catalogName + "ContentCatalog/contentCatalogSyncJob.impex", false);

		if (projectType.equals(EPIC))
		{
			executeCatalogSyncJob(context, JNJ);
		}
		else if (projectType.equals(PCM))
		{
			if (getBooleanSystemSetupParameter(context, SYNC_CONTENT_CATALOGS))
			{
				*//** PCMContentStaged to PCMContentOnline *//*
				logInfo(context, "Executing catalogs sync job  [PCMContentStaged to PCMContentOnline]");
				final CronJobModel syncPCMContentStagedToOnlineCronJob = cronJobService.getCronJob(SYNC_PCM_CONT_STG_TO_ONLINE);
				((CatalogVersionSyncCronJobModel) syncPCMContentStagedToOnlineCronJob).setScheduleMedias(null);
				modelService.save(syncPCMContentStagedToOnlineCronJob);
				cronJobService.performCronJob(syncPCMContentStagedToOnlineCronJob, true);
				logInfo(context, "Executed catalogs sync job  [PCMContentStaged to PCMContentOnline] Status:- "
						+ syncPCMContentStagedToOnlineCronJob.getResult());
			}
		}*/
		//logInfo(context, "Done importing catalog [" + catalogName + "] for :" + projectType);
	}

	/*protected void importStore(final SystemSetupContext context, final String storeName, final String projectType)
	{
		logInfo(context, "Begin importing store [" + storeName + "]");
		String prefix = EPIC_PREFIX_PATH;

		if (projectType.equals(PCM))
		{
			prefix = PCM_PREFIX_PATH;
		}
		importImpexFile(context, prefix + "stores/store.impex");
		importImpexFile(context, prefix + "stores/site.impex");

		logInfo(context, "Done importing store [" + storeName + "]");
	}*/

	/*protected void importOrderStatus(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of orderStatusMapping & orderEntryStatusMapping");

		importImpexFile(context, "/jnjb2bcore/import/epic/order/orderStatusMapping.impex");
		importImpexFile(context, "/jnjb2bcore/import/epic/order/orderEntryStatusMapping.impex");
		importImpexFile(context, "/jnjb2bcore/import/epic/order/tanOrderStatusMapping.impex");

		logInfo(context, "Done importing impex of orderStatusMapping & orderEntryStatusMapping & tanOrderStatusMapping");
	}

	protected void importShippingMethods(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of Shipping methods");
		importImpexFile(context, "/jnjb2bcore/import/epic/order/shippingMethods.impex");
		logInfo(context, "Done importing impex of Shipping methods");
	}

	protected void importShipmentTrackingURLs(final SystemSetupContext context)
	{
		logInfo(context, "Begin importing impex of Shipment Tracking URLs");
		importImpexFile(context, "/jnjb2bcore/import/epic/externaldata/shipmenttracking/shipmenttracking.impex");
		logInfo(context, "Done importing impex of Shipment Tracking URLs");
	}


	protected void importCronJobs(final SystemSetupContext context, final String catalogName, final boolean active)
	{
		logInfo(context, "Begin importing CronJobs [" + catalogName + "]");
		importImpexFile(context, "/jnjb2bcore/import/common/JnjCronJobs.impex", true);
		if (active)
		{
			importImpexFile(context, "/jnjb2bcore/import/common/JnjCronJobsATriger.impex", true);
		}
		else
		{
			importImpexFile(context, "/jnjb2bcore/import/common/JnjCronJobsDTriger.impex", true);
		}

	}

	protected void importTestData(final SystemSetupContext context, final String catalogName)
	{
		logInfo(context, "Begin importing TestData [" + catalogName + "]");

		//importImpexFile(context, "/jnjb2bcore/import/epic/testData/products.impex", true);
		//importImpexFile(context, "/jnjb2bcore/import/epic/testData/newsReleases.impex", true);

	}
*/}
