/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.facades.product;

import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.JnjProductCarouselData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;

/**
 * This class provides methods for retrieving product data for product model and
 * products bought together with a specific product code.
 */
public interface JnjLatamProductFacade extends JnjGTProductFacade{

    /**
     * This method returns JnjLaProductData based on product model passed.
     * @param productModel productModel for which productData is retrieved
     * @return JnjLaProductData object
     */
    public JnjLaProductData getLatamProductData(final ProductModel productModel);

    /**
     * This method returns a list of all products bought together with the product provided.
     * @param productCode productCode for which list of products bought together are retrieved
     * @return List of JnjProductCarouselData objects
     */
    public List<JnjProductCarouselData> getProductsBoughtTogether(final String productCode);

    /**
     * This method returns list of ProductReferenceData based on the given parameters.
     * @param code will be pass to get its substitute products.
     * @param referenceTypes Substitute Product reference type will be passed.
     * @param options will ba passed as parameter.
     * @param limit this is the max number of references to be fetched.
     * @return list of ProductReferenceData objects.
     */
    public List<ProductReferenceData> getSubstituteProductReferences(final String code,
           final List<ProductReferenceTypeEnum> referenceTypes, final List<ProductOption> options, final Integer limit);
}
