/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */

package com.jnj.core.services.impl;

import com.jnj.la.core.services.JnJLaProductService;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.commerceservices.product.data.ReferenceData;
import de.hybris.platform.commerceservices.product.impl.DefaultCommerceProductReferenceService;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

/**
 * This class overrides getProductReferencesForCode methods from OOTB to add latam specific logic
 * to check if the reference product is available for the logged-in customer
 */
public class DefaultLatamCommerceProductReferenceService extends DefaultCommerceProductReferenceService {

    private static final Logger LOG = Logger.getLogger(DefaultLatamCommerceProductReferenceService.class);

    @Autowired
    protected JnJLaProductService jnjLaProductService;

    @Override
    public List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> getProductReferencesForCode(final String productCode,
                                                                                                   final List<ProductReferenceTypeEnum> referenceTypes,
                                                                                                   final Integer limit)
    {
        validateParameterNotNull(productCode, "Parameter productCode must not be null");
        validateParameterNotNull(referenceTypes, "Parameter referenceType must not be null");
        validateParameterNotNull(limit, "Parameter limit must not be null");

        final List<ReferenceData<ProductReferenceTypeEnum, ProductModel>> result = new ArrayList<>();

        final ProductModel product = getProductService().getProductForCode(productCode);
        final List<ProductReferenceModel> references = getAllActiveProductReferencesFromSourceOfType(product,
                referenceTypes);
        if (CollectionUtils.isNotEmpty(references))
        {
            for (final ProductReferenceModel reference : references)
            {
                final ProductModel targetProduct = resolveTarget(product, reference);
                try
                {
                    ProductModel refProduct = jnjLaProductService.getProductForCatalogId(targetProduct.getCode());
                    populateData(refProduct, targetProduct, reference, result);
                }
                catch (final Exception exception)
                {
                    String referenceType = reference.getReferenceType() != null
                            ? reference.getReferenceType().getCode() : StringUtils.EMPTY;

                    LOG.error("Exception occurred to get the references product data with source product code:"
                            + productCode + "and reference type:" + referenceType + "with message:" , exception);
                }

                if (result.size() >= limit.intValue())
                {
                    break;
                }
            }
        }

        return result;
    }

    private void populateData(final ProductModel relatedProduct, final ProductModel targetProduct,
                              final ProductReferenceModel reference, final List<ReferenceData<ProductReferenceTypeEnum,
            ProductModel>> result) {

        if (null != relatedProduct) {
            final ReferenceData<ProductReferenceTypeEnum, ProductModel> referenceData = createReferenceData();
            referenceData.setTarget(targetProduct);
            referenceData.setDescription(reference.getDescription());
            referenceData.setQuantity(reference.getQuantity());
            referenceData.setReferenceType(reference.getReferenceType());
            result.add(referenceData);
        }
    }
}
