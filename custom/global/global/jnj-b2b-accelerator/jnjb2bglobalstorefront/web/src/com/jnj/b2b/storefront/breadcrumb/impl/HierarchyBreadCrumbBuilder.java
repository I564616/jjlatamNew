/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.b2b.storefront.breadcrumb.impl;

import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.url.UrlResolver;

public class HierarchyBreadCrumbBuilder extends SearchBreadcrumbBuilder {

    protected UrlResolver<CategoryModel> hierarchyModelUrlResolver;

    @Override
    protected Breadcrumb getCategoryBreadcrumb(final CategoryModel category, final String linkClass)
    {
        String categoryUrl = getHierarchyModelUrlResolver().resolve(category);
        return new Breadcrumb(categoryUrl, category.getName(), linkClass);
    }

    public UrlResolver<CategoryModel> getHierarchyModelUrlResolver() {
        return hierarchyModelUrlResolver;
    }

    public void setHierarchyModelUrlResolver(UrlResolver<CategoryModel> hierarchyModelUrlResolver) {
        this.hierarchyModelUrlResolver = hierarchyModelUrlResolver;
    }


}