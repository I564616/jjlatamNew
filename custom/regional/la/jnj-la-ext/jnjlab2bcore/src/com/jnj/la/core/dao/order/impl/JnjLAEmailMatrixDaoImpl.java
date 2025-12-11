/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.dao.order.impl;

import com.jnj.la.core.dao.order.JnjLAEmailMatrixDao;
import com.jnj.la.core.model.JnjLAEmailMatrixModel;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class JnjLAEmailMatrixDaoImpl implements JnjLAEmailMatrixDao {

    private static final Logger LOG = Logger.getLogger(JnjLAEmailMatrixDaoImpl.class);
    private static final String NO_COMBINATION_MESSAGE = "No matrix rule found for Previous: %s and Current: %s combination";

    private FlexibleSearchService flexibleSearchService;

    @Override
    public boolean sendEmailByStatuses(OrderEntryStatus previous, final OrderEntryStatus current) {

        if (current == null) {
            LOG.error(String.format(NO_COMBINATION_MESSAGE, previous, null));
            return false;
        }

        if (previous == null) {
            previous = OrderEntryStatus.CREATED;
        }

        final StringBuilder query = new StringBuilder();
        query.append("SELECT {").append(JnjLAEmailMatrixModel.PK).append("} FROM {").append(JnjLAEmailMatrixModel._TYPECODE).append("}");
        query.append(" WHERE {").append(JnjLAEmailMatrixModel.PREVIOUSSTATUS).append("} = ?previous");
        query.append(" AND {").append(JnjLAEmailMatrixModel.CURRENTSTATUS).append("} = ?current");

        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("previous", previous);
        fQuery.addQueryParameter("current", current);

        final List<JnjLAEmailMatrixModel> result = flexibleSearchService.<JnjLAEmailMatrixModel>search(fQuery).getResult();

        if (CollectionUtils.isEmpty(result)) {
            LOG.error(String.format(NO_COMBINATION_MESSAGE, previous, current));
            return false;
        } else {
            return result.get(0).getSendEmail();
        }
    }

    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }

}