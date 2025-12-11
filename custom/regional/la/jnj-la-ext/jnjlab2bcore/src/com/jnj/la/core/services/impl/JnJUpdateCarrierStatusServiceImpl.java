/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.impl;

import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.daos.JnjLaInvoiceDao;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.services.JnJUpdateCarrierStatusService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JnJUpdateCarrierStatusServiceImpl implements JnJUpdateCarrierStatusService {

    private ModelService modelService;

    private JnjLaInvoiceDao invoiceDao;

    @Autowired
    private JnjLaOrderDao orderDao;

    @Autowired
    private JnjUpdateCarrierStatusHelper updateCarrierStatusHelper;

    private static final Logger LOG = Logger.getLogger(JnJUpdateCarrierStatusServiceImpl.class);

    @Override
    public void processPendingOrderEntries(final JnjIntegrationRSACronJobModel model) {
        final Timestamp startDate = Timestamp.valueOf(LocalDateTime.now());
        processPendingOrderEntries(model.getLastSuccessFulRecordProcessTime());
        updateJobDate(model, startDate);
    }

    private void processPendingOrderEntries(Date lastTime) {
        final Set<OrderModel> pendingOrders = findPendingOrders(lastTime);
        final int size = pendingOrders.size();
        LOG.info("Found " + size + " pending orders to be processed");

        processPendingOrders(pendingOrders);
    }

    private void processPendingOrders(Set<OrderModel> orders) {
        final int batchSize = Config.getInt(Jnjlab2bcoreConstants.UPDATE_CARRIER_STATUS_BATCH_SIZE, 200);
        final int lastBatchNumber = orders.size() / batchSize;
        int batchNumber = 1;
        LOG.info("Processing orders using batch size " + batchSize + ". Pending batches: " + lastBatchNumber);

        final List<OrderModel> batch = new ArrayList<>();
        for (OrderModel order : orders) {
            batch.add(order);
            if (batch.size() >= batchSize) {
                processPendingOrdersBatch(batch, batchNumber, lastBatchNumber);
                batch.clear();
                batchNumber++;
            }
        }

        if (batch.size() > 0) {
            // process incomplete batch
            processPendingOrdersBatch(batch, batchNumber, lastBatchNumber + 1);
        }
    }

    private void processPendingOrdersBatch(List<OrderModel> orders, final int batchNumber, final int lastBatchNumber) {
        LOG.info("Processing batch " + batchNumber + "/" + lastBatchNumber + ", with " + orders.size() + " elements.");
        LOG.info("Searching invoices of batch.");
        final List<JnJInvoiceOrderModel> batchInvoices = invoiceDao.getInvoicesByOrders(orders);
        LOG.info("Found " + batchInvoices.size() + " invoices for the batch.");

        for (OrderModel order : orders) {
            try {
                LOG.info("Processing order " + order.getSapOrderNumber());
                final List<JnJInvoiceOrderModel> ordersInvoices = filterInvoices(batchInvoices, order);
                for (AbstractOrderEntryModel orderEntry : order.getEntries()) {
                    updateCarrierStatusHelper.processOrderEntry(orderEntry, ordersInvoices);
                }
            } catch (Exception e) {
                LOG.error("Error processing order " + order.getSapOrderNumber(), e);
            }
        }

        LOG.info("Saving batch " + batchNumber + "/" + lastBatchNumber);
        modelService.saveAll(extractOrderEntries(orders));
    }

    private List<AbstractOrderEntryModel> extractOrderEntries(List<OrderModel> orders) {
        final List<AbstractOrderEntryModel> entries = new ArrayList<>();

        for (OrderModel order : orders) {
            entries.addAll(order.getEntries());
        }

        return entries;
    }

    private List<JnJInvoiceOrderModel> filterInvoices(List<JnJInvoiceOrderModel> batchInvoices, OrderModel order) {
        return batchInvoices.stream().filter( i -> i.getSalesOrder().equals(order.getSapOrderNumber())).collect(Collectors.toList());
    }

    private Set<OrderModel> findPendingOrders(Date lastTime) {
        if (lastTime == null) {
            LOG.error("Field lastSuccessFulRecordProcessTime cannot be blank, otherwise all orders will be loaded!");
            return new HashSet<>();
        }

        return createPendingOrdersSet(orderDao.getUpdatedOrders(lastTime), orderDao.getUpdatedInvoiceOrders(lastTime));
    }

    private Set<OrderModel> createPendingOrdersSet(List<OrderModel> pendingOrders, List<OrderModel> pendingInvoiceOrders) {
        Set<OrderModel> mergedOrders = new LinkedHashSet<>();
        mergedOrders.addAll(pendingOrders);
        mergedOrders.addAll(pendingInvoiceOrders);

        LOG.info("Pending orders: " + pendingOrders.size());
        LOG.info("Pending invoice orders: " + pendingInvoiceOrders.size());
        LOG.info("Total pending orders: " + mergedOrders.size());

        return mergedOrders;
    }

    private void updateJobDate(final JnjIntegrationRSACronJobModel model, final Timestamp startDate) {
        model.setLastSuccessFulRecordProcessTime(startDate);
        modelService.save(model);
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    public void setInvoiceDao(final JnjLaInvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }
}
