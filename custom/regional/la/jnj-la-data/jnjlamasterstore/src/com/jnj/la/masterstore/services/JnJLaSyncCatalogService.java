/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.masterstore.services;

import de.hybris.platform.catalog.model.synchronization.CatalogVersionSyncCronJobModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;

import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.MAP_OF_COUNTRIES_ISO;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.SYNC_COUNTRY_CONT_STG_TO_ONLINE;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.SYNC_COUNTRY_PROD_STG_TO_ONLINE;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.SYNC_MASTER_CONT_STG_TO_COUNTRY_STG;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.SYNC_MASTER_PROD_STG_TO_COUNTRY_STG;

public class JnJLaSyncCatalogService {

    private CronJobService cronJobService;

    private ModelService modelService;

    public void synchContentCatalogs() {
        synchContentCatalogsForCountries(getAllCountries());
    }

    public void synchProductCatalogs() {
        synchProductCatalogsForCountries(getAllCountries());
    }

    public void synchContentCatalogsForCountries(final String[] countries) {
        synchCatalogs(countries, SYNC_MASTER_CONT_STG_TO_COUNTRY_STG, SYNC_COUNTRY_CONT_STG_TO_ONLINE);
    }

    public void synchProductCatalogsForCountries(final String[] countries) {
        synchCatalogs(countries, SYNC_MASTER_PROD_STG_TO_COUNTRY_STG, SYNC_COUNTRY_PROD_STG_TO_ONLINE);
    }

    private void synchCatalogs(final String[] countries, final String masterStagedPattern, final String stagedOnlinePattern) {
        for (final String country : countries) {
            final String countryName = MAP_OF_COUNTRIES_ISO.get(country);
            synchCountry(masterStagedPattern, stagedOnlinePattern, countryName);
        }
    }

    private void synchCountry(final String masterStagedPattern, final String stagedOnlinePattern, final String countryName) {
        syncCronJobs(String.format(masterStagedPattern, countryName));
        syncCronJobs(String.format(stagedOnlinePattern, countryName));
    }

    private void syncCronJobs(final String stgToOnline) {
        final CronJobModel syncMDDContentStagedToOnlineCronJob = cronJobService.getCronJob(stgToOnline);
        ((CatalogVersionSyncCronJobModel) syncMDDContentStagedToOnlineCronJob).setScheduleMedias(null);
        modelService.save(syncMDDContentStagedToOnlineCronJob);
        cronJobService.performCronJob(syncMDDContentStagedToOnlineCronJob, true);
    }

    private static String[] getAllCountries() {
        return MAP_OF_COUNTRIES_ISO.keySet().toArray(new String[0]);
    }

    public void setCronJobService(final CronJobService cronJobService) {
        this.cronJobService = cronJobService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }
}
