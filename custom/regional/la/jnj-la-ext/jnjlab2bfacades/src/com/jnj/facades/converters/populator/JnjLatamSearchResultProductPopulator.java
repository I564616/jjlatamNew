/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.facades.converters.populator;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.customer.JnjGTCustomerFacade;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.services.MessageService;

public class JnjLatamSearchResultProductPopulator extends JnjGTSearchResultProductPopulator {

    private final Class<JnjLatamSearchResultProductPopulator> currentClass = JnjLatamSearchResultProductPopulator.class;

    @Override
    public void populate(final SearchResultValueData source, final ProductData target) {
        final String METHOD_NAME = "JnjLatamSearchResultProductPopulator populate()";
        JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SOLR_SEARCH, METHOD_NAME, Logging.BEGIN_OF_METHOD, currentClass);
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        // Pull the values directly from the SearchResult object
        final JnjLaProductData jnjLaProductData = (JnjLaProductData) target;
        jnjLaProductData.setCode(getValue(source, "code"));
        jnjLaProductData.setCatalogId(getValue(source, "catalogId"));
        jnjLaProductData.setName(getValue(source, "name"));
        jnjLaProductData.setDescription(getValue(source, "description"));
        jnjLaProductData.setManufacturer(getValue(source, "manufacturerName"));
        jnjLaProductData.setSummary(getValue(source, "summary"));
        jnjLaProductData.setAverageRating(getValue(source, "reviewAvgRating"));

        populatePrices(source, target);

        getProductFeatureListPopulator().populate(getFeaturesList(source), target);

        final List<ImageData> images = createImageData(source);
        if (CollectionUtils.isNotEmpty(images)) {
            jnjLaProductData.setImages(images);
        }
        populateUrl(source, target);

        final Boolean discontinue = getValue(source, "disContinue");
        final Boolean displayPrice = getValue(source, "showPrice");
        if (null != discontinue) {
            jnjLaProductData.setDiscontinue(discontinue);
        }
        if (null != displayPrice) {
            jnjLaProductData.setDisplayPrice(displayPrice);
        }

        setUnits(source, jnjLaProductData);
        final String productStatusCode = getValue(source, "productStatusCode");
        JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SOLR_SEARCH, METHOD_NAME, "productStatusCode :: " + productStatusCode, currentClass);

        final String modStatus = getValue(source, "modStatus");
        JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SOLR_SEARCH, METHOD_NAME, "modStatus :: " + modStatus, currentClass);
        jnjLaProductData.setStatus(modStatus);
        jnjLaProductData.setBaseMaterialNumber(getValue(source, "baseMaterialNumber"));
        jnjLaProductData.setProductVolume(getValue(source, "volume"));
        jnjLaProductData.setProductWeight(getValue(source, "weight"));
        JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.Logging.SOLR_SEARCH, METHOD_NAME, Logging.END_OF_METHOD, currentClass);
    }

    protected void setUnits(final SearchResultValueData source, final JnjLaProductData jnjGTProductData) {
        final String unitOfMeasure = getValue(source, "uom");
        if (StringUtils.isNotEmpty(unitOfMeasure)) {
            final String[] units = unitOfMeasure.split(Jnjlab2bcoreConstants.UNDERSCORE_SYMBOL);
            for (int pos = 0; pos < units.length; pos++) {
                final String unit = units[pos];
                switch (pos) {
                    case 0:
                        jnjGTProductData.setSalesUnit(unit);
                        break;
                    case 1:
                        jnjGTProductData.setQuantity(Integer.valueOf(unit));
                        jnjGTProductData.setNumerator(unit);
                        break;
                    case 2:
                        jnjGTProductData.setDeliveryUnit(unit);
                        break;
                    case 3:
                        jnjGTProductData.setBaseUnit(unit);
                        break;
                    default:
                        break;
                }
            }
        }
    }

}