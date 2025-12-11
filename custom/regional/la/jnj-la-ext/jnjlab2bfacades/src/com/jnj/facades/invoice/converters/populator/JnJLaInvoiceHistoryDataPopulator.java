/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.facades.invoice.converters.populator;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class JnJLaInvoiceHistoryDataPopulator implements Populator<JnJInvoiceOrderModel, JnJLaInvoiceHistoryData> {

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil b2bUnitUtil;

    @Autowired
    private DefaultCommerceCommonI18NService defaultCommerceCommonI18NService;

    @Autowired
    private PriceDataFactory priceDataFactory;

    @Autowired
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;

    @Override
    public void populate(final JnJInvoiceOrderModel source, final JnJLaInvoiceHistoryData target) throws ConversionException {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setInvDocNo(source.getInvDocNo());
        target.setCreationDate(source.getCreationDate());
        target.setNetValue(getNetValue(source));
        populateOrderInfo(source, target);
        target.setInvoiceNumber(getInvoiceNumber(source));
        target.setCarrierConfirmedDeliveryDate(source.getCarrierConfirmedDeliveryDate());
        target.setCarrierEstimateDeliveryDate(source.getCarrierEstimateDeliveryDate());
    }

    private void populateOrderInfo(final JnJInvoiceOrderModel source, final JnJLaInvoiceHistoryData target) {
        if (source.getOrder() != null) {
            target.setOrderNumber(source.getOrder().getCode());
            target.setSapOrderNumber(source.getOrder().getSapOrderNumber());
            target.setOrderLoaded(Boolean.TRUE);
        } else {
            target.setOrderLoaded(Boolean.FALSE);
            target.setSapOrderNumber(jnjCommonFacadeUtil.getMessageFromImpex(Jnjlab2bcoreConstants.INVOICE_HISTORY_ORDER_NOT_LOADED));
        }
    }

    private PriceData getNetValue(final JnJInvoiceOrderModel source) {
        final Double netValue;
        if (source.getNetValue() != null) {
            netValue = source.getNetValue();
        } else {
            netValue = 0.0;
        }

        final CurrencyModel currentCurrency = defaultCommerceCommonI18NService.getCurrentCurrency();
        return priceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(netValue), currentCurrency);
    }

    private String getInvoiceNumber(final JnJInvoiceOrderModel source) {
        final CountryModel country = b2bUnitUtil.getCurrentCountryForSite();
        if (country != null && Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(country.getIsocode())) {
            return source.getNfNumber();
        } else {
            return source.getInvDocNo();
        }
    }

}