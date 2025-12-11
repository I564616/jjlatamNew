/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.core.dao.order.impl;

import com.jnj.la.core.dao.order.JnjOrderArchivalDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJnjOrderArchivalDao implements JnjOrderArchivalDao {

    private static final Logger LOG = Logger.getLogger(DefaultJnjOrderArchivalDao.class);
    private static final int REMOVE_QUERY_SUBSTRING_CONSTANT = 3;
    private static final String ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE = "jnj.split.symbol.used.pipe";
    private static final String PIPE = "|";
    private static final String QUERY="select {pk} from {Order as o join JnJB2BUnit  as j  on {o:"
                                        +"unit}={j:pk }  join Country as c on {c.pk}={j.country} "
                                        +"join OrderEntry as e on {e:order}={o:pk} join JnJProduct as p on {e:product}={p:pk} ";
    private static final String ORDER_ARCHIVAL_PO_TYPE_LIST = "jnj.order.archival.ignoring.potype";
    private static final String ORDER_ARCHIVAL_ORDER_TYPE_LIST = "jnj.order.archival.ignoring.ordertype";
    private static final String ORDER_ARCHIVAL_ORDER_STATUS = "jnj.order.archival.ignoring.orderStatus";
    private static final String AND = "AND ";
    private FlexibleSearchService flexibleSearchService;
    private ConfigurationService configurationService;

    /**
     * fetch orders for archival
     *
     * @param param Map with Key sourcesystemId and value with order creationtime
     * @return List<OrderModel>
     */
    @Override
    public List<OrderModel> getOrdersWithSectorForArchival(final Map<String, String> param) {
        final StringBuilder queryString = new StringBuilder();
        queryString.append(QUERY);
        final String poTypeList = getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_PO_TYPE_LIST);
        final String orderTypeList = getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_ORDER_TYPE_LIST);
        final String orderStatusIgnore = getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_ORDER_STATUS);
        addFilterOptions(queryString, orderTypeList,orderStatusIgnore);
        queryString.append("} where (");
        final Map<String, String> searchParams = new HashMap<>();
        int increment = 0;
        String country=StringUtils.EMPTY;
        for (final Map.Entry<String, String> entry : param.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue())) {
                final String[] sourceSystemIdSplitValues = entry.getKey().split(
                        getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE, PIPE));
                final String sourceSystemId = sourceSystemIdSplitValues[1];
                country = sourceSystemIdSplitValues[0];
                final String orderCreationTime = entry.getValue();
                if (sourceSystemId != null && orderCreationTime != null) {
                    queryString.append("({o:creationtime}<CONVERT(DATETIME,?creationtime").append(increment)
                            .append(")").append(" AND {p:sector}=?sourceSystem");
                    queryString.append(increment).append(") OR ");
                    searchParams.put("creationtime" + increment, orderCreationTime);
                    searchParams.put("sourceSystem" + increment, sourceSystemId);
                    increment++;
                }
            }
        }
        queryString.delete(queryString.length() - REMOVE_QUERY_SUBSTRING_CONSTANT, queryString.length());
        queryString.append(") AND {c:isocode}=?country ");
        searchParams.put("country", country);
        addFilterConditions(queryString,orderTypeList,orderStatusIgnore,poTypeList);
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryString);
        fQuery.addQueryParameters(searchParams);
        LOG.info("Query formed : " + fQuery);
        return getFlexibleSearchService().<OrderModel>search(fQuery).getResult();
    }

    private static void addFilterOptions(final StringBuilder queryString, final String orderTypeList,final String orderStatusIgnore ){
        if(StringUtils.isNotEmpty(orderStatusIgnore)){
            queryString.append("join OrderStatus as os on {o:status}={os:pk}");
        }
        if(StringUtils.isNotEmpty(orderTypeList)){
            queryString.append("join JnjOrderTypesEnum as ot on {o:orderType}={ot:pk}");
        }
    }

    private static void addFilterConditions(final StringBuilder queryString, final String orderTypeList,final String orderStatusIgnore,final String poTypeList){
        if(StringUtils.isNotEmpty(poTypeList)){
            final List<String> poTypes = Arrays.asList(poTypeList.split(",", -1));
            queryString.append(AND).append("( {o:poType} NOT IN ('");
            queryString.append(StringUtils.join(poTypes,"','"));
            queryString.append("') OR {o:poType} IS NULL )");
        }
        if(StringUtils.isNotEmpty(orderTypeList)){
            final List<String> orderTypes = Arrays.asList(orderTypeList.split(",", -1));
            queryString.append(AND).append(" {ot:code} NOT IN ('");
            queryString.append(StringUtils.join(orderTypes,"','"));
            queryString.append("') ");
        }
        if(StringUtils.isNotEmpty(orderStatusIgnore)){
            final List<String> orderStatuses = Arrays.asList(orderStatusIgnore.split(",", -1));
            queryString.append(AND).append(" {os:code} NOT IN ('");
            queryString.append(StringUtils.join(orderStatuses,"','"));
            queryString.append("') ");
        }
    }

    /**
     * @return the configurationService
     */
    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    /**
     * @param configurationService the configurationService to set
     */
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    /**
     * @return the flexibleSearchService
     */
    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    /**
     * @param flexibleSearchService the flexibleSearchService to set
     */
    public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
