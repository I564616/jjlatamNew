/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.versioning.util;

import com.jnj.la.versioning.enumeration.JnjLAVersionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.jnj.la.versioning.constants.JnjlaversioningConstants.PATH;
import static com.jnj.la.versioning.constants.JnjlaversioningConstants.PATH_PATTERN;

public final class JnjLAVersionFolderUtils {

    private static final String DOT = ".";
    private static final String UNDERSCORE = "_";

    @Autowired
    private ResourceLoader resourceLoader;

    public List<String> getImpexFilesForVersion(final JnjLAVersionEnum version) throws IOException {
        final List<String> files = new ArrayList<>();
        final String label = version.getLabel().replace(DOT, UNDERSCORE);
        final String versionPath = String.format(PATH, label);
        final Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(String.format(PATH_PATTERN, label));
        Arrays.stream(resources).forEach(r -> files.add(versionPath + r.getFilename()));
        return files;
    }

}