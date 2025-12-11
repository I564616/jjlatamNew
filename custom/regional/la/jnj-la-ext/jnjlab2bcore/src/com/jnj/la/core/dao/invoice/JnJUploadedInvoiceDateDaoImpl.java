/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.dao.invoice;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.enums.JnJUploadedFileStatus;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JnJUploadedInvoiceDateDaoImpl implements JnJUploadedInvoiceDateDao {

    private static final String SELECT = "SELECT {";
    private static final String FROM = "} FROM {";
    private static final String WHERE = " WHERE {";
    protected FlexibleSearchService flexibleSearchService;

    @Override
    public List<JnJUploadedInvoiceDateModel> getPendingFiles() {

        final StringBuilder searchQuery = new StringBuilder();
        final Map<String, Object> queryParams = new HashMap<>();
        searchQuery.append(SELECT).append(JnJUploadedInvoiceDateModel.PK)
            .append(FROM).append(JnJUploadedInvoiceDateModel._TYPECODE).append("}")
            .append(WHERE).append(JnJUploadedInvoiceDateModel.CURRENTSTATUS).append("} = ?status");
        queryParams.put("status", JnJUploadedFileStatus.PENDING);

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);

        flexibleSearchQuery.addQueryParameters(queryParams);

        return flexibleSearchService.<JnJUploadedInvoiceDateModel>search(flexibleSearchQuery).getResult();
    }

    @Override
    public List<JnJUploadedInvoiceDateModel> getUploadedOlderThan(final Long ageInDays) {

        final StringBuilder searchQuery = new StringBuilder();
        final Map<String, Object> queryParams = new HashMap<>();
        searchQuery.append(SELECT).append(JnJUploadedInvoiceDateModel.PK)
            .append(FROM).append(JnJUploadedInvoiceDateModel._TYPECODE).append("}")
            .append(WHERE).append(JnJUploadedInvoiceDateModel.CURRENTSTATUS).append("} != ?status")
            .append(" AND {").append(JnJUploadedInvoiceDateModel.ERASED).append("} = ?erased");
        queryParams.put("status", JnJUploadedFileStatus.PENDING);
        queryParams.put("erased", Boolean.FALSE);

        if (ageInDays != null) {
            final Date fromDate = Date.from(LocalDate.now().minus(ageInDays, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault()).toInstant());
            queryParams.put("fromDate", fromDate);
            searchQuery.append(" AND {").append(JnJUploadedInvoiceDateModel.CREATIONTIME).append("} < ?fromDate");
        }

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);

        flexibleSearchQuery.addQueryParameters(queryParams);

        return flexibleSearchService.<JnJUploadedInvoiceDateModel>search(flexibleSearchQuery).getResult();
    }

    @Override
    public List<JnJUploadedInvoiceDateModel> getUploadedByUser(final JnJB2bCustomerModel user) {

        final StringBuilder searchQuery = new StringBuilder();
        searchQuery.append(SELECT).append(JnJUploadedInvoiceDateModel.PK)
            .append(FROM).append(JnJUploadedInvoiceDateModel._TYPECODE).append("}")
            .append(WHERE).append(JnJUploadedInvoiceDateModel.USER).append("} = ?user")
            .append(" ORDER BY {").append(JnJUploadedInvoiceDateModel.CREATIONTIME).append("} DESC");

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
        flexibleSearchQuery.addQueryParameter("user", user);

        return flexibleSearchService.<JnJUploadedInvoiceDateModel>search(flexibleSearchQuery).getResult();
    }

    @Override
    public JnJUploadedInvoiceDateModel getFileByUserAndFilename(final JnJB2bCustomerModel user, final String filename) {
        final JnJUploadedInvoiceDateModel model = new JnJUploadedInvoiceDateModel();
        model.setUser(user);
        model.setFilename(filename);
        return getSingleResult(flexibleSearchService.getModelsByExample(model));
    }

    @Override
    public JnJUploadedInvoiceDateModel getById(final String id) {
        final StringBuilder searchQuery = new StringBuilder();
        searchQuery.append(SELECT).append(JnJUploadedInvoiceDateModel.PK)
            .append(FROM).append(JnJUploadedInvoiceDateModel._TYPECODE).append("}")
            .append(WHERE).append(JnJUploadedInvoiceDateModel.PK).append("} = ?id");

        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
        flexibleSearchQuery.addQueryParameter("id", id);

        return getSingleResult(flexibleSearchService.<JnJUploadedInvoiceDateModel>search(flexibleSearchQuery).getResult());
    }

    private static JnJUploadedInvoiceDateModel getSingleResult(final List<JnJUploadedInvoiceDateModel> result) {
        return CollectionUtils.isNotEmpty(result) ? result.get(0) : null;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
