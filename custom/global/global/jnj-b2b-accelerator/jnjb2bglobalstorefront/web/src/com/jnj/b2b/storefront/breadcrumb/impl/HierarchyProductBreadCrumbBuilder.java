/**
 *
 */
package com.jnj.b2b.storefront.breadcrumb.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.url.UrlResolver;

/**
 * @author nsinha7
 *
 */
public class HierarchyProductBreadCrumbBuilder extends ProductBreadcrumbBuilder {

    protected UrlResolver<CategoryModel> hierarchyModelUrlResolver;

    @Override
    public List<Breadcrumb> getBreadcrumbs(final ProductData baseProductData)
    {
        final List<Breadcrumb> breadcrumbs = new ArrayList<>();

        addCategoryBreadCrumbs(breadcrumbs, baseProductData.getCode());
        Collections.reverse(breadcrumbs);
        return breadcrumbs;
    }

    @Override
    protected Breadcrumb getCategoryBreadcrumb(final CategoryModel category)
    {
        final String categoryUrl = getHierarchyModelUrlResolver().resolve(category);
        return new Breadcrumb(categoryUrl, category.getName(), null);
    }

    public UrlResolver<CategoryModel> getHierarchyModelUrlResolver() {
        return hierarchyModelUrlResolver;
    }

    public void setHierarchyModelUrlResolver(UrlResolver<CategoryModel> hierarchyModelUrlResolver) {
        this.hierarchyModelUrlResolver = hierarchyModelUrlResolver;
    }


}