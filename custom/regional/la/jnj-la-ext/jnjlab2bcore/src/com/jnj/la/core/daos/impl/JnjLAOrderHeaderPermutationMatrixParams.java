/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.daos.impl;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.HashMap;
import java.util.Map;

import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASBACKORDERED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASBEINGPROCESSED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASCANCELLED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASCREATED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASDELIVERED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASINPICKING;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASINVOICED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASPARTIALLYDELIVERED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASPARTIALLYINVOICED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASPARTIALLYSHIPPED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASSHIPPED;
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel.HASUNDERANALYSIS;

public class JnjLAOrderHeaderPermutationMatrixParams {

    private OrderModel order;
    private final Map<String, Boolean> queryParams = new HashMap<>();

    JnjLAOrderHeaderPermutationMatrixParams(OrderModel order) {
        this.order = order;
        addParams();
    }

    private void addParams() {
        addParam(HASCREATED, OrderEntryStatus.CREATED);
        addParam(HASUNDERANALYSIS, OrderEntryStatus.UNDER_ANALYSIS);
        addParam(HASBEINGPROCESSED, OrderEntryStatus.BEING_PROCESSED);
        addParam(HASINPICKING, OrderEntryStatus.IN_PICKING);
        addParam(HASPARTIALLYINVOICED, OrderEntryStatus.PARTIALLY_INVOICED);
        addParam(HASINVOICED, OrderEntryStatus.INVOICED);
        addParam(HASPARTIALLYSHIPPED, OrderEntryStatus.PARTIALLY_SHIPPED);
        addParam(HASSHIPPED, OrderEntryStatus.SHIPPED);
        addParam(HASPARTIALLYDELIVERED, OrderEntryStatus.PARTIALLY_DELIVERED);
        addParam(HASDELIVERED, OrderEntryStatus.DELIVERED);
        addParam(HASBACKORDERED, OrderEntryStatus.BACKORDERED);
        addParam(HASCANCELLED, OrderEntryStatus.CANCELLED);
    }

    private void addParam(String queryParam, OrderEntryStatus orderEntryStatus) {
        queryParams.put(queryParam, hasStatusInOrderLines(orderEntryStatus));
    }

    private Boolean hasStatusInOrderLines(OrderEntryStatus orderEntryStatus) {
        if (order != null && order.getEntries() != null) {
            return order.getEntries().stream().anyMatch(entry -> orderEntryStatus.toString().equals(entry.getStatus()));
        }
        return Boolean.FALSE;
    }

    public Map getQueryParams() {
        return queryParams;
    }

}
