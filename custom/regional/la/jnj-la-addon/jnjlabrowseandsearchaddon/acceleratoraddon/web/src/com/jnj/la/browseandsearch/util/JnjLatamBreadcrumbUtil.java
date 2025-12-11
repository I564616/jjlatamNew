/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.la.browseandsearch.util;

import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;

import java.util.ArrayList;
import java.util.List;

public class JnjLatamBreadcrumbUtil {

    private JnjLatamBreadcrumbUtil(){

    }
    /**
     * This method prepares breadcrumb data
     *
     * @param breadCrumbList
     * @return
     */
    public static List<Breadcrumb> customizeBreadCrumb(final List<Breadcrumb> breadCrumbList) {

        final List<Breadcrumb> custombreadCrumbList = new ArrayList<>();
        for (int i = 0; i < breadCrumbList.size(); i++) {
            final Breadcrumb breadCrumnb = breadCrumbList.get(i);
            if (i == 1) {
                breadCrumnb.setUrl(breadCrumbList.get(0).getUrl());
            }  else {
                breadCrumnb.setUrl(breadCrumbList.get(i).getUrl());
            }
            custombreadCrumbList.add(breadCrumnb);
        }
        return custombreadCrumbList;
    }
}
