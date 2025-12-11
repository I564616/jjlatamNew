/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.daos.impl;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjLAFlexibleSearchBuilder;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
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
import static com.jnj.la.core.model.JnjLAOrderPermutationMatrixModel._TYPECODE;

@Component
public class JnjLAOrderHeaderPermutationMatrixCore {

    private static final Class<JnjLAOrderHeaderPermutationMatrixCore> CURRENT_CLASS = JnjLAOrderHeaderPermutationMatrixCore.class;
    private static final String CALCULATE_ORDER_HEADER_STATUS = "calculateOrderHeaderStatus";

    OrderStatus calculateOrderHeaderStatus(FlexibleSearchService flexibleSearchService, OrderModel order) {
        List<JnjLAOrderPermutationMatrixModel> result = flexibleSearchService.<JnjLAOrderPermutationMatrixModel>search(buildQuery(order)).getResult();

        if (CollectionUtils.isNotEmpty(result)) {
            if (result.size() > 1) {
                JnjGTCoreUtil.logWarnMessage(Logging.UPDATE_ORDER_STATUS, CALCULATE_ORDER_HEADER_STATUS, "More than one status where found for the order " + order.getOrderNumber(), CURRENT_CLASS);
            }
            return result.get(0).getStatus();
        } else {
            JnjGTCoreUtil.logWarnMessage(Logging.UPDATE_ORDER_STATUS, CALCULATE_ORDER_HEADER_STATUS, "No status where found for the order " + order.getOrderNumber(), CURRENT_CLASS);
            return null;
        }
    }

    private static FlexibleSearchQuery buildQuery(OrderModel order) {
        Map queryParams = new JnjLAOrderHeaderPermutationMatrixParams(order).getQueryParams();

        JnjLAFlexibleSearchBuilder builder = new JnjLAFlexibleSearchBuilder()
            .select(ItemModel.PK)
            .from(_TYPECODE)
            .where(HASCREATED)
            .and(HASUNDERANALYSIS)
            .and(HASBEINGPROCESSED)
            .and(HASINPICKING)
            .and(HASPARTIALLYINVOICED)
            .and(HASINVOICED)
            .and(HASPARTIALLYSHIPPED)
            .and(HASSHIPPED)
            .and(HASPARTIALLYDELIVERED)
            .and(HASDELIVERED)
            .and(HASBACKORDERED)
            .and(HASCANCELLED)
            .addQueryParams(queryParams);
        return builder.getQuery();
    }

}
