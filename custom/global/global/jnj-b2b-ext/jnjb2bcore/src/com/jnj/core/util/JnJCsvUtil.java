/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
 
package com.jnj.core.util;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JnJCsvUtil {

    private static final String SINGLE_QUOTE = "'";

    /**
     * Transform a csv to a list
     * @param csv
     * @return
     */
    public static List<String> toList(final String csv) {
        if (StringUtils.isNotEmpty(csv)) {
            return Arrays.asList(csv.split(Jnjb2bCoreConstants.SYMBOL_COMMA));
        }
        return new ArrayList<>();
    }

    /**
     * Transform a csv to a list trimming the quotes
     * @param csv
     * @return
     */
    public static List<String> toListRemovingQuotes(final String csv) {
        if (csv != null) {
            return toList(csv.replaceAll(SINGLE_QUOTE, StringUtils.EMPTY));
        }
        return new ArrayList<>();
    }

}
