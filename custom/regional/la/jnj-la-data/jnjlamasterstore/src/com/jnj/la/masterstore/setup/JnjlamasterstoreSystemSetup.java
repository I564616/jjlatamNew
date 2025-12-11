/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.masterstore.setup;

import com.jnj.la.masterstore.services.JnJLaSyncCatalogService;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.commerceservices.setup.impl.DefaultSetupSolrIndexerService;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetup.Process;
import de.hybris.platform.core.initialization.SystemSetup.Type;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.List;

import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.COUNTRIES;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.COUNTRIES_LABEL;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.DEPLOYMENT_ENV;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.IMPORT_SITE;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.IMPORT_SOLR_BY_COUNTRY;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.LOAD_CONTENT;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.LOAD_CONTENT_LABEL;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.LOAD_PRODUCT;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.LOAD_PRODUCT_LABEL;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.LOCAL;
import static com.jnj.la.masterstore.constants.JnjlamasterstoreConstants.MAP_OF_COUNTRIES_ISO;

@SystemSetup(extension = "jnjlamasterstore")
public class JnjlamasterstoreSystemSetup extends AbstractSystemSetup {

    private CommonI18NService commonI18NService;

    private DefaultSetupSolrIndexerService defaultSetupSolrIndexerService;

    private JnJLaSyncCatalogService syncCatalogService;

    @Override
    @SystemSetupParameterMethod
    public List<SystemSetupParameter> getInitializationOptions() {
        final ArrayList<SystemSetupParameter> parameters = new ArrayList<>();
        parameters.add(createCountriesParameter());
        parameters.add(createBooleanSystemSetupParameter(LOAD_PRODUCT, LOAD_PRODUCT_LABEL, true));
        parameters.add(createBooleanSystemSetupParameter(LOAD_CONTENT, LOAD_CONTENT_LABEL, true));
        return parameters;
    }

    private SystemSetupParameter createCountriesParameter() {
        final SystemSetupParameter countries = new SystemSetupParameter(COUNTRIES);
        countries.setLabel(COUNTRIES_LABEL);
        countries.addValues(MAP_OF_COUNTRIES_ISO.keySet().toArray(new String[MAP_OF_COUNTRIES_ISO.size()]));
        countries.setMultiSelect(true);
        return countries;
    }

    @SystemSetup(type = Type.ESSENTIAL, process = Process.INIT)
    public void createEssentialData(final SystemSetupContext context) {
        importImpexFile(context, "/jnjlamasterstore/import/common/userGroups.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/countries.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/essential-data.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/configurations.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/SearchRestrictionUpdate.impex");

        importImpexFile(context, "/jnjlamasterstore/import/common/themes.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/unit.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/ComercialUomConversion.impex");


        importImpexFile(context, "/jnjlamasterstore/import/common/JnjCronJobs.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/JnjLatamCronJobs.impex");
        importImpexFile(context, "/jnjlamasterstore/import/order/JnjOrderChannel.impex");
        importImpexFile(context, "/jnjlamasterstore/import/order/JnjOrderType.impex");
        importImpexFile(context, "/jnjlamasterstore/import/order/JnjSAPErrorTranslationTable.impex");
        importImpexFile(context, "/jnjlamasterstore/import/order/orderEntryStatusMapping.impex");
        importImpexFile(context, "/jnjlamasterstore/import/order/orderStatusMapping.impex");

        final String environment = Config.getParameter(DEPLOYMENT_ENV);
        if (LOCAL.equalsIgnoreCase(environment)) {
            importImpexFile(context, "/jnjlamasterstore/import/common/JnjLatamCronJobsTriggerDeactivation.impex");
        }
    }

    @SystemSetup(type = Type.PROJECT, process = Process.ALL)
    public void createProjectData(final SystemSetupContext context) {
        if (getBooleanSystemSetupParameter(context, LOAD_PRODUCT)) {
            importProductCatalog(context);
        }

        if (getBooleanSystemSetupParameter(context, LOAD_CONTENT)) {
            importContentCatalog(context);
        }

        importStore(context);
        createAndActivateSolrIndex(context);

        if (getBooleanSystemSetupParameter(context, LOAD_PRODUCT)) {
            syncCatalogService.synchProductCatalogsForCountries(getSetupCountries(context));
        }

        if (getBooleanSystemSetupParameter(context, LOAD_CONTENT)) {
            syncCatalogService.synchContentCatalogsForCountries(getSetupCountries(context));
        }
    }

    private void importProductCatalog(final SystemSetupContext context) {
        logInfo(context, "Begin importing product catalog");
        final String prefix = "/jnjlamasterstore/import/productCatalogs/jnjProductCatalog";

        importImpexFile(context, prefix + "/catalog.impex");
        importImpexFile(context, prefix + "/catalog_en.impex");
        importImpexFile(context, prefix + "/categories-L1.impex");
        importImpexFile(context, prefix + "/categories-L1_en.impex");
        importImpexFile(context, prefix + "/categories-L2.impex");
        importImpexFile(context, prefix + "/categories-L2_en.impex");
        importImpexFile(context, prefix + "/categories-L3.impex");
        importImpexFile(context, prefix + "/categories-L3_en.impex");
        importImpexFile(context, prefix + "/categories-L4.impex");
        importImpexFile(context, prefix + "/categories-L4_en.impex");
        importImpexFile(context, prefix + "/productCatalogSyncJob.impex");

        logInfo(context, "Done importing product catalog");
    }

    private void importContentCatalog(final SystemSetupContext context) {
        logInfo(context, "Begin importing content catalog");

        final String prefix = "/jnjlamasterstore/import/contentCatalogs/jnjContentCatalog";

        importImpexFile(context, prefix + "/catalog.impex");
        importImpexFile(context, prefix + "/catalog_en.impex");
        importImpexFile(context, prefix + "/cms-content.impex");
        importImpexFile(context, prefix + "/messages.impex");
        importImpexFile(context, prefix + "/emails/email-content.impex");
        importImpexFile(context, prefix + "/contentCatalogSyncJob.impex");

        logInfo(context, "Done importing content catalog");
    }

    private void importStore(final SystemSetupContext context) {
        logInfo(context, "Begin importing store");

        importImpexFile(context, "/jnjlamasterstore/import/stores/jnj/store.impex");

        for (final String country : getSetupCountries(context)) {
            importImpexFile(context, String.format(IMPORT_SITE, country));
        }

        logInfo(context, "Done importing store");
    }

    protected void createAndActivateSolrIndex(final SystemSetupContext context) {
        final String prefix = "/jnjlamasterstore/import/stores/solr";

        importImpexFile(context, prefix +  "/solr_content.impex");
        importImpexFile(context, prefix +  "/solr_content_en.impex");
        importImpexFile(context, prefix +  "/solr_content_es.impex");
        importImpexFile(context, prefix +  "/solr_content_pt.impex");
        importImpexFile(context, prefix +  "/solr_indirectCust.impex");

        for (final String country : getSetupCountries(context)) {
            importImpexFile(context, String.format(IMPORT_SOLR_BY_COUNTRY, country));
        }

        importImpexFile(context, prefix +  "/solr_en.impex");
        importImpexFile(context, "/jnjlamasterstore/import/common/JnjLatamSolrCronJobs.impex");
    }

    private String[] getSetupCountries(final SystemSetupContext context) {
        var countries = context.getParameters(context.getExtensionName() + "_" + COUNTRIES);
        return countries == null || countries.length == 0 ? MAP_OF_COUNTRIES_ISO.keySet().
                toArray(new String[MAP_OF_COUNTRIES_ISO.size()]) : countries;
    }

    public void setCommonI18NService(CommonI18NService commonI18NService) {
        this.commonI18NService = commonI18NService;
    }

    public void setDefaultSetupSolrIndexerService(DefaultSetupSolrIndexerService defaultSetupSolrIndexerService) {
        this.defaultSetupSolrIndexerService = defaultSetupSolrIndexerService;
    }

    public void setSyncCatalogService(JnJLaSyncCatalogService syncCatalogService) {
        this.syncCatalogService = syncCatalogService;
    }
}
