/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.daos.impl;

import com.jnj.core.model.JnjOrdEntStsMappingModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JnjLAOrderEntryPermutationMatrixCore {

    private static final String IS_NULL = "} IS NULL";
    private static final String AND = " AND {";

    List<JnjOrdEntStsMappingModel> getOrderEntryStatuses(FlexibleSearchService flexibleSearchService, final AbstractOrderEntryModel orderLine) {
        final StringBuilder searchQuery = buildQuery(orderLine);
        final Map<String, String> queryParams = populateQueryParams(orderLine);

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
        fQuery.addQueryParameters(queryParams);

        return flexibleSearchService.<JnjOrdEntStsMappingModel> search(fQuery).getResult();
    }

    private static StringBuilder buildQuery(final AbstractOrderEntryModel orderLine) {
        final StringBuilder searchQuery = new StringBuilder();

        searchQuery.append("SELECT {").append(ItemModel.PK)
            .append("} FROM {").append(JnjOrdEntStsMappingModel._TYPECODE).append("} WHERE {")
            .append(JnjOrdEntStsMappingModel.OVERALLSTATUS)
            .append(getClauseIfNotNull(orderLine.getLineOverallStatus(), "} = ?overAllStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.REJECTIONSTATUS)
            .append(getClauseIfNotNull(orderLine.getReasonForRejection(), "} = ?rejectionStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.DELIVERYSTATUS)
            .append(getClauseIfNotNull(orderLine.getDeliveryStatus(), "} = ?deliveryStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.INVOICESTATUS)
            .append(getClauseIfNotNull(orderLine.getLineInvoiceStatus(), "} = ?invoiceStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.GTSHOLD)
            .append(getClauseIfNotNull(orderLine.getGTSHold(), "} = ?GTSHold"))
            .append(AND).append(JnjOrdEntStsMappingModel.BACKORDERSTATUS)
            .append(getClauseIfNotNull(orderLine.getBackorderStatus(), "} = ?backorderStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.CARRIERESTDELIVERYDATESTATUS)
            .append(getClauseIfNotNull(orderLine.getCarrierEstDeliveryDateStatus(), "} = ?carrierEstDeliveryDateStatus"))
            .append(AND).append(JnjOrdEntStsMappingModel.CARRIERACTUALDELIVERYDATESTATUS)
            .append(getClauseIfNotNull(orderLine.getCarrierActualDeliveryDateStatus(), "} = ?carrierActualDeliveryDateStatus"));

        return searchQuery;
    }

    private static Map<String, String> populateQueryParams(final AbstractOrderEntryModel orderLine) {
        final Map<String, String> queryParams = new HashMap<>();
        if (StringUtils.isNotBlank(orderLine.getLineOverallStatus())){
            queryParams.put(JnjOrdEntStsMappingModel.OVERALLSTATUS, orderLine.getLineOverallStatus());
        }

        if (StringUtils.isNotBlank(orderLine.getReasonForRejection())){
            queryParams.put(JnjOrdEntStsMappingModel.REJECTIONSTATUS, orderLine.getReasonForRejection());
        }

        if (StringUtils.isNotBlank(orderLine.getDeliveryStatus())){
            queryParams.put(JnjOrdEntStsMappingModel.DELIVERYSTATUS, orderLine.getDeliveryStatus());
        }
        if (StringUtils.isNotBlank(orderLine.getLineInvoiceStatus())){
            queryParams.put(JnjOrdEntStsMappingModel.INVOICESTATUS, orderLine.getLineInvoiceStatus());
        }
        if (StringUtils.isNotBlank(orderLine.getGTSHold())){
            queryParams.put(JnjOrdEntStsMappingModel.GTSHOLD, orderLine.getGTSHold());
        }

        if (orderLine.getBackorderStatus() != null){
            queryParams.put(JnjOrdEntStsMappingModel.BACKORDERSTATUS, orderLine.getBackorderStatus().getCode());
        }

        if (orderLine.getCarrierEstDeliveryDateStatus() != null){
            queryParams.put(JnjOrdEntStsMappingModel.CARRIERESTDELIVERYDATESTATUS, orderLine.getCarrierEstDeliveryDateStatus().getCode());
        }

        if (orderLine.getCarrierActualDeliveryDateStatus() != null){
            queryParams.put(JnjOrdEntStsMappingModel.CARRIERACTUALDELIVERYDATESTATUS, orderLine.getCarrierActualDeliveryDateStatus().getCode());
        }
        return queryParams;
    }

    private static String getClauseIfNotNull(final Object o, final String valueToReturn) {
        if (o == null || (o instanceof String && StringUtils.isBlank((String) o)) ) {
            return IS_NULL;
        }
        return valueToReturn;
    }

}
