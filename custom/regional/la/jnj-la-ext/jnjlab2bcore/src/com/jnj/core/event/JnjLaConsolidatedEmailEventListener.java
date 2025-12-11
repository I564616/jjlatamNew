/*
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.model.JnjLaConsolidatedEmailProcessModel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.hybris.platform.core.model.order.OrderModel;
import org.apache.commons.collections4.CollectionUtils;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Optional;


public class JnjLaConsolidatedEmailEventListener extends AbstractSiteEventListener<JnjLaConsolidatedEmailEvent> {

    private static final Logger LOG = Logger.getLogger(JnjLaConsolidatedEmailEventListener.class);

    private ModelService modelService;
    private BusinessProcessService businessProcessService;

    @Override
    protected void onSiteEvent(final JnjLaConsolidatedEmailEvent event) {

        final JnJB2BUnitModel b2bUnit = event.getUnit();
        final List<OrderModel> allB2bUnitOrders = event.getOrders();
        final List<String> defaultRecipients = event.getDefaultRecipients();
        final List<JnJB2bCustomerModel> allB2bCustomers = event.getUsers();
        List<JnJB2bCustomerModel> b2bCustomersWithAllOrderTypes = new ArrayList<>();
        List<JnJB2bCustomerModel> otherB2bCustomers = new ArrayList<>();

        //Get all order types from available orders
        Set<String> allOrderTypeCodesFromOrders = getAllOrderTypesFromOrders(allB2bUnitOrders);
        LOG.info("All order types - " + allOrderTypeCodesFromOrders);

        // Group all b2bCustomers with all order types
        groupCustomersByOrderTypes(b2bUnit, allB2bCustomers, b2bCustomersWithAllOrderTypes, otherB2bCustomers,
                allOrderTypeCodesFromOrders);

        LOG.debug("Default customers size " + b2bCustomersWithAllOrderTypes.size());
        LOG.debug("Other customers size " + otherB2bCustomers.size());

        // Customers who can receive all orders
        createAndTriggerBusinessProcess(event, event.getOrders(), b2bCustomersWithAllOrderTypes, defaultRecipients);

        //Other customers
        processOtherCustomers(event, allB2bUnitOrders, otherB2bCustomers);
    }

    private static void groupCustomersByOrderTypes(final JnJB2BUnitModel b2bUnit,
                                            final List<JnJB2bCustomerModel> allB2bCustomers,
                                            List<JnJB2bCustomerModel> b2bCustomersWithAllOrderTypes,
                                            List<JnJB2bCustomerModel> otherB2bCustomers,
                                            final Set<String> allOrderTypeCodesFromOrders) {

        allB2bCustomers.forEach(jnJB2bCustomerModel -> {
            JnJLaUserAccountPreferenceModel preferenceModel = getAccountPreferenceByUnit(b2bUnit,
                    jnJB2bCustomerModel.getAccountPreferences());
            Set<String> customerOrderTypes = getOrderTypesFromAccountPreference(preferenceModel);

            LOG.info("Customer " + jnJB2bCustomerModel.getUid() + " has order types " + customerOrderTypes);

            if(customerOrderTypes.containsAll(allOrderTypeCodesFromOrders)) {
                b2bCustomersWithAllOrderTypes.add(jnJB2bCustomerModel);
            } else {
                otherB2bCustomers.add(jnJB2bCustomerModel);
            }
        });
    }

    private void processOtherCustomers(final JnjLaConsolidatedEmailEvent event, final List<OrderModel> allB2bUnitOrders,
                                       final List<JnJB2bCustomerModel> otherB2bCustomers) {
        if(CollectionUtils.isNotEmpty(otherB2bCustomers)) {
            otherB2bCustomers.forEach(jnJB2bCustomerModel -> {
                JnJLaUserAccountPreferenceModel preferenceModel = getAccountPreferenceByUnit(event.getUnit(),
                        jnJB2bCustomerModel.getAccountPreferences());
                Set<String> customerOrderTypes = getOrderTypesFromAccountPreference(preferenceModel);
                List<OrderModel> filteredOrders = filterOrdersByType(allB2bUnitOrders, customerOrderTypes);
                createAndTriggerBusinessProcess(event, filteredOrders, Collections.singletonList(jnJB2bCustomerModel),
                        new ArrayList<>());
            });
        }
    }

    private static List<OrderModel> filterOrdersByType(final List<OrderModel> allB2bUnitOrders,
                                                final Set<String> customerOrderTypes) {
        List<OrderModel> filteredOrders = new ArrayList<>();
        allB2bUnitOrders.forEach(order -> {
            if (customerOrderTypes.contains(getOrderTypeFromOrder(order))) {
                filteredOrders.add(order);
            }
        });

        return filteredOrders;
    }

    private static Set<String> getAllOrderTypesFromOrders(final List<OrderModel> b2bUnitOrders) {
        Set<String> allOrderTypeCodesFromOrders = new HashSet<>();
        b2bUnitOrders.forEach(order -> {
            if (order.getJnjOrderType() != null) {
                allOrderTypeCodesFromOrders.add(order.getJnjOrderType().getCode());
            } else if (order.getOrderType() != null) {
                allOrderTypeCodesFromOrders.add(order.getOrderType().getCode());
            }
        });

        return allOrderTypeCodesFromOrders;
    }

    private static String getOrderTypeFromOrder(final OrderModel order) {
        String orderTypeCode = StringUtils.EMPTY;
        if (order.getJnjOrderType() != null) {
            orderTypeCode = order.getJnjOrderType().getCode();
        } else if (order.getOrderType() != null) {
            orderTypeCode = order.getOrderType().getCode();
        }
        return orderTypeCode;
    }

    private void createAndTriggerBusinessProcess(final JnjLaConsolidatedEmailEvent event,
            final List<OrderModel> reportOrders, final List<JnJB2bCustomerModel> b2bCustomers,
                                                 final List<String> defaultRecipients) {

        final String processId = "JnjLaConsolidatedEmailProcess" + "-" + event.getUnit().getPk() + "-"
                + System.currentTimeMillis();
        final JnjLaConsolidatedEmailProcessModel processModel = businessProcessService.createProcess(processId,
                "JnjLaConsolidatedEmailProcess");
        processModel.setUnit(event.getUnit());
        processModel.setUsers(b2bCustomers);
        processModel.setDefaultRecipients(defaultRecipients);
        final BaseStoreModel store = getStore(event);

        processModel.setStore(store);
        processModel.setSite(store.getCmsSites().iterator().next());
        processModel.setCurrency(store.getDefaultCurrency());
        processModel.setLanguage(store.getDefaultLanguage());
        processModel.setReportOrders(reportOrders);

        LOG.info("Creating process " + processId + " for store " + store.getUid() + ", B2BUnit UID " + event.getUnit().getUid()
                + " with " +  reportOrders.size() + " order(s)");
        modelService.save(processModel);
        businessProcessService.startProcess(processModel);
    }

    private static JnJLaUserAccountPreferenceModel getAccountPreferenceByUnit (final JnJB2BUnitModel b2bUnit,
            final List<JnJLaUserAccountPreferenceModel> customerAccountPreferences) {

        Optional<JnJLaUserAccountPreferenceModel> prefModelOptional = customerAccountPreferences.stream().filter(
                accountPreferenceModel -> b2bUnit.getUid().equalsIgnoreCase(
                        accountPreferenceModel.getAccount().getUid())).findFirst();

        return prefModelOptional.orElse(null);
    }

    private static Set<String> getOrderTypesFromAccountPreference(
            final JnJLaUserAccountPreferenceModel accountPreferenceModel) {
        Set<String> orderTypes = new HashSet<>();
        if (accountPreferenceModel != null && CollectionUtils.isNotEmpty(accountPreferenceModel.getOrderTypes())) {
            accountPreferenceModel.getOrderTypes().forEach(jnjOrderTypesEnum ->
                    orderTypes.add(jnjOrderTypesEnum.getCode()));
        }
        return orderTypes;
    }

    private static BaseStoreModel getStore(final JnjLaConsolidatedEmailEvent event) {
        return event.getUnit().getCountry().getBaseStores().iterator().next();
    }

    @Override
    protected boolean shouldHandleEvent(final JnjLaConsolidatedEmailEvent event) {
        return true;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    public void setBusinessProcessService(final BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }
}