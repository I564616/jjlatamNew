/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.facades.order.impl;

import com.jnj.la.core.data.JnjArchiveOrderData;
import com.jnj.la.core.model.JnjOrderArchivalJobConfigModel;
import com.jnj.la.core.model.JnjOrderArchivalResultModel;
import com.jnj.la.core.services.JnjOrderArchivalService;
import com.jnj.la.facades.order.JnjOrderArchivalFacade;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.lang3.StringUtils;

/**
 * Class responsible for handling archival orders fetching and deletion.
 */
public class DefaultJnjOrderArchivalFacade implements JnjOrderArchivalFacade {

    private ModelService modelService;
    private JnjOrderArchivalService jnjOrderArchivalService;

    /**
     * saves the ArchiveResult to model
     *
     * @param jnjArchiveOrderData to save in jnjOrderArchivalResultModel
     */
    @Override
    public void saveArchiveResult(final JnjArchiveOrderData jnjArchiveOrderData) {
        JnjOrderArchivalResultModel jnjOrderArchivalResultModel = getModelService().create(JnjOrderArchivalResultModel.class);
        String orderCode = jnjArchiveOrderData.getOrderCode();
        if (StringUtils.isNotEmpty(orderCode)) {
            jnjOrderArchivalResultModel.setOrderList(orderCode);
        }
        jnjOrderArchivalResultModel.setRecordsDeletedCount(jnjArchiveOrderData.getRecordsDeleted());
        jnjOrderArchivalResultModel.setRecordsFetchedCount(jnjArchiveOrderData.getTotalRecords());
        getModelService().save(jnjOrderArchivalResultModel);
    }

    /**
     * fetch the list of Orders for deletion
     *
     * @return OrderModel returns list of order respective to the date and source Id
     */
    @Override
    public JnjArchiveOrderData archiveOrdersByCountry(final JnjOrderArchivalJobConfigModel countryConfig) {
        return getJnjOrderArchivalService().deleteOrdersForCountry(countryConfig);
    }

    /**
     * @return the modelService
     */
    public ModelService getModelService() {
        return modelService;
    }

    /**
     * @param modelService the modelService to set
     */
    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * @return the jnjOrderArchivalService
     */
    public JnjOrderArchivalService getJnjOrderArchivalService() {
        return jnjOrderArchivalService;
    }

    /**
     * @param jnjOrderArchivalService the jnjOrderArchivalService to set
     */
    public void setJnjOrderArchivalService(final JnjOrderArchivalService jnjOrderArchivalService) {
        this.jnjOrderArchivalService = jnjOrderArchivalService;
    }

}
