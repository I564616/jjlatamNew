/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */

package com.jnj.facades.product.converters.populator;

import com.jnj.facades.data.JnjLaProductData;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commercefacades.product.converters.populator.ReferenceDataProductReferencePopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class JnJLatamSubstituteProductReferencePopulator extends ReferenceDataProductReferencePopulator {

    /**
     * Populates the ProductModel to ProductReferenceData.
     *
     * @param source
     *           the product to take the data from.
     * @param target
     *           the data to put the data in.
     * @throws ConversionException
     */
    @Override
    public void populate(final ReferenceData<ProductReferenceTypeEnum, ProductModel> source, final ProductReferenceData target) 
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        populateTargetData(source, target);
    }

    private static void populateTargetData(final ReferenceData<ProductReferenceTypeEnum, ProductModel> source, final ProductReferenceData target) {
        final JnjLaProductData productData = new JnjLaProductData();
        if (productData instanceof ProductData) {
            productData.setDescription(StringUtils.isNotBlank(source.getDescription()) ? source.getDescription() : StringUtils.EMPTY);
            productData.setQuantity(source.getQuantity() != null ? source.getQuantity() : 0);
            productData.setCode(source.getTarget().getCode());
        }
        target.setTarget(productData);
        target.setReferenceType(source.getReferenceType());
    }
}