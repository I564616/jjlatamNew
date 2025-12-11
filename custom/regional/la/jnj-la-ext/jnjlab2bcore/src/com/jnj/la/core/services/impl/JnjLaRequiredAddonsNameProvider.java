package com.jnj.la.core.services.impl;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.acceleratorservices.addonsupport.RequiredAddOnsNameProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.*;

public class JnjLaRequiredAddonsNameProvider extends RequiredAddOnsNameProvider {

    private static final Logger LOG = Logger.getLogger(JnjLaRequiredAddonsNameProvider.class);

    @Override
    protected List<String> getDependantAddOns(final String extensionName)
    {
        LOG.info( "fetching dependent addons for extension "+ extensionName);
        if (StringUtils.isEmpty(extensionName))
        {
            LOG.info( "no dependent addons for extension "+ extensionName);
            return Collections.emptyList();
        }
        final ExtensionInfo extensionInfo = getExtensionInfo(extensionName);
        final Set<ExtensionInfo> allRequiredExtensionInfos = extensionInfo.getAllRequiredExtensionInfos();

        // Check if each required extension is an addon
        final Set<String> allAddOns = new HashSet<String>();
        for (final ExtensionInfo extension : allRequiredExtensionInfos)
        {
            if (isAddOnExtension(extension))
            {
                allAddOns.add(extension.getName());
            }
        }

        // Get the addon names in the correct order
        final List<String> extNames = new ArrayList<>(getExtensionNames());

        final List<String> addOnsInOrder = new ArrayList<String>();
        for (final String extName : extNames)
        {
            if (allAddOns.contains(extName))
            {
                addOnsInOrder.add(extName);
            }
        }
        LOG.info( " addons installed "+ addOnsInOrder.size());
        return addOnsInOrder;
    }
}
