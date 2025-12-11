/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.util;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JnjLAFlexibleSearchBuilder {

    private static final String SELECT = "SELECT { ";
    private static final String FROM = " FROM { ";
    private static final String WHERE = " WHERE { ";
    private static final String AND = " AND { ";
    private static final String END_BLOCK = " }";
    private static final String END_WITH_COMPARE = " } = ?";

    private StringBuilder query = new StringBuilder();
    private String queryTemplate;
    private List<String> templateParams = new ArrayList<>();
    private Map queryParams = new HashMap<>();

    public JnjLAFlexibleSearchBuilder() {
    }

    public JnjLAFlexibleSearchBuilder(String queryTemplate){
        this.queryTemplate = queryTemplate;
    }

    public JnjLAFlexibleSearchBuilder addVar(String var){
        templateParams.add(var);
        return this;
    }

    public JnjLAFlexibleSearchBuilder select(final String field) {
        query.append(SELECT).append(field).append(END_BLOCK);
        return this;
    }

    public JnjLAFlexibleSearchBuilder from(final String table) {
        query.append(FROM).append(table).append(END_BLOCK);
        return this;
    }

    public JnjLAFlexibleSearchBuilder where(final String field) {
        query.append(WHERE).append(field).append(END_WITH_COMPARE).append(field);
        return this;
    }

    public JnjLAFlexibleSearchBuilder and(final String field) {
        query.append(AND).append(field).append(END_WITH_COMPARE).append(field);
        return this;
    }

    @SuppressWarnings("unchecked")
    public JnjLAFlexibleSearchBuilder addQueryParam(String key, Object value) {
        queryParams.put(key, value);
        return this;
    }

    public JnjLAFlexibleSearchBuilder addQueryParams(Map queryParams) {
        this.queryParams = queryParams;
        return this;
    }

    @SuppressWarnings("unchecked")
    public FlexibleSearchQuery getQuery(){
        final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(buildQuery());
        if (queryParams.size() > 0) {
            flexibleSearchQuery.addQueryParameters(queryParams);
        }
        return flexibleSearchQuery;
    }

    private String buildQuery() {
        if (queryTemplate != null) {
            return String.format(queryTemplate, templateParams.toArray());
        }
        return query.toString();
    }

}
