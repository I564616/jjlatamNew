package com.jnj.la.browseandsearch.wrapper;

import de.hybris.platform.commercefacades.product.data.CategoryData;

/**
 * Created by aadrian2 on 29/05/2017.
 */
public class JnjLatamCategoryDataWrapper implements java.io.Serializable  {

    private CategoryData categoryData;

    private boolean displayProducts;

    public JnjLatamCategoryDataWrapper(CategoryData categoryData, boolean displayProducts) {
        this.categoryData = categoryData;
        this.displayProducts = displayProducts;
    }

    public CategoryData getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(CategoryData categoryData) {
        this.categoryData = categoryData;
    }

    public boolean isDisplayProducts() {
        return displayProducts;
    }

    public void setDisplayProducts(boolean displayProducts) {
        this.displayProducts = displayProducts;
    }
}
