/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.versioning.setup;

import com.jnj.la.masterstore.services.JnJLaSyncCatalogService;
import com.jnj.la.versioning.enumeration.JnjLAVersionEnum;
import com.jnj.la.versioning.services.JnJLaVersionService;
import com.jnj.la.versioning.util.JnjLAVersionFolderUtils;
import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;
import de.hybris.platform.core.initialization.SystemSetupParameterMethod;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jnj.la.versioning.constants.JnjlaversioningConstants.SYNCHRONIZE_CONTENT_CATALOGS;
import static com.jnj.la.versioning.constants.JnjlaversioningConstants.SYNCHRONIZE_CONTENT_CATALOGS_LABEL;
import static com.jnj.la.versioning.constants.JnjlaversioningConstants.VERSION;
import static com.jnj.la.versioning.constants.JnjlaversioningConstants.VERSION_LABEL;

@SystemSetup(extension = "jnjlaversioning")
public class JnjLAVersioningSystemSetup extends AbstractSystemSetup {

    private JnJLaVersionService versionService;

    private JnjLAVersionFolderUtils jnjLAVersionFolderUtils;

    private JnJLaSyncCatalogService syncCatalogService;

    @Override
    @SystemSetupParameterMethod
    public List<SystemSetupParameter> getInitializationOptions() {
        final List<SystemSetupParameter> parameters = new ArrayList<>();

        final JnjLAVersionEnum currentVersion = versionService.getCurrentVersion();
        final String[] versions = JnjLAVersionEnum.getVersionsGreaterThan(currentVersion);
        if (ArrayUtils.isNotEmpty(versions)) {
            final SystemSetupParameter parameter = new SystemSetupParameter(VERSION);
            parameter.setLabel(VERSION_LABEL);
            parameter.addValues(versions);
            parameters.add(parameter);
        }

        parameters.add(createBooleanSystemSetupParameter(SYNCHRONIZE_CONTENT_CATALOGS, SYNCHRONIZE_CONTENT_CATALOGS_LABEL, Boolean.TRUE));

        return parameters;
    }

    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.INIT)
    public void executeAllVersions(final SystemSetupContext context) {
        logInfo(context, "All system update version will be performed!");
        final JnjLAVersionEnum currentVersion = versionService.getCurrentVersion();
        logInfo(context, "Current version of System: " + currentVersion.getLabel());
        processVersionsGreaterThanCurrent(context, currentVersion);
        updateSearchRestrictions(context);

        synchronizeCatalogs(context);
    }

    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.UPDATE)
    public void updateVersion(final SystemSetupContext context) {
        final JnjLAVersionEnum versionEnum = getSelectedVersion(context);
        logInfo(context, "System update version selected: " + versionEnum.getLabel());
        final JnjLAVersionEnum currentVersion = versionService.getCurrentVersion();
        logInfo(context, "Current version of System: " + currentVersion.getLabel());
        processVersionsBetweenCurrentAndSelected(context, versionEnum, currentVersion);
        updateSearchRestrictions(context);

        synchronizeCatalogs(context);
    }

    private void synchronizeCatalogs(final SystemSetupContext context) {
        if (getBooleanSystemSetupParameter(context, SYNCHRONIZE_CONTENT_CATALOGS)) {
            logInfo(context, "Synchronizing content catalogs");
            syncCatalogService.synchContentCatalogs();
            logInfo(context, "Content catalogs synchronized");
        }
    }

    private void processVersionsGreaterThanCurrent(final SystemSetupContext context, final JnjLAVersionEnum currentVersion) {
        Arrays.stream(JnjLAVersionEnum.values()).
            filter(v -> greaterThan(v, currentVersion)).
            forEach(v -> importVersionImpexes(context, v));
    }

    private void processVersionsBetweenCurrentAndSelected(final SystemSetupContext context, final JnjLAVersionEnum selectedVersion, final JnjLAVersionEnum currentVersion) {
        Arrays.stream(JnjLAVersionEnum.values()).
            filter(v -> lessOrEqualThan(v, selectedVersion) && greaterThan(v, currentVersion)).
            forEach(v -> importVersionImpexes(context, v));
    }

    private void updateSearchRestrictions(final SystemSetupContext context) {
        importImpexFile(context, "/jnjlaversioning/import/SearchRestrictionUpdate.impex");
    }

    private void importVersionImpexes(final SystemSetupContext context, final JnjLAVersionEnum version) {
        logInfo(context, "Running impexes for version: " + version.getLabel());
        try {
            jnjLAVersionFolderUtils.getImpexFilesForVersion(version).forEach(f -> importImpexFile(context, f));
            logInfo(context, "System version updated");
        } catch (IOException e) {
            logError(context, "Unable to process files from " + version.getLabel(), e);
        }
    }

    private static JnjLAVersionEnum getSelectedVersion(final SystemSetupContext context) {
        return JnjLAVersionEnum.get(context.getParameter(context.getExtensionName() + "_" + VERSION));
    }

    private static boolean lessOrEqualThan(final JnjLAVersionEnum left, final JnjLAVersionEnum right) {
        return left.getCode() <= right.getCode();
    }

    private static boolean greaterThan(final JnjLAVersionEnum left, final JnjLAVersionEnum right) {
        return left.getCode() > right.getCode();
    }

    @Required
    public void setVersionService(JnJLaVersionService versionService) {
        this.versionService = versionService;
    }

    @Required
    public void setJnjLAVersionFolderUtils(JnjLAVersionFolderUtils jnjLAVersionFolderUtils) {
        this.jnjLAVersionFolderUtils = jnjLAVersionFolderUtils;
    }

    @Required
    public void setSyncCatalogService(JnJLaSyncCatalogService syncCatalogService) {
        this.syncCatalogService = syncCatalogService;
    }
}
