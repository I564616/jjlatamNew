/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.core.services.impl;

import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTShippingLineDetailsModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTInvoicePriceModel;
import com.jnj.core.model.JnjGTInvoiceEntryLotModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dao.order.JnjOrderArchivalDao;
import com.jnj.la.core.data.JnjArchiveOrderData;
import com.jnj.la.core.model.JnjOrderArchivalJobConfigModel;
import com.jnj.la.core.services.JnjOrderArchivalService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Collections;

/**
 * Service Class responsible for handling archival orders fetching and deletion.
 */
public class DefaultJnjOrderArchivalService implements JnjOrderArchivalService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjOrderArchivalService.class);
    private static final String ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE = "jnj.split.symbol.used.pipe";
    private static final String ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_COLON = "jnj.split.symbol.used.colon";
    private static final String ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE_SEPARATED = "jnj.split.symbol.used.pipe.seperated";
    public static final String HYBRIS_DATE_FORMAT = "hybris.date.format";

    private JnjOrderArchivalDao jnjOrderArchivalDao;
    private ModelService modelService;
    private ConfigurationService configurationService;

    public JnjArchiveOrderData deleteOrdersForCountry(final JnjOrderArchivalJobConfigModel countryConfig) {
        LOG.info("Started deleting orders for country: {}", countryConfig.getCountryCode());
        int ordersDeletedCount = 0;
        int totalInvoiceCount = 0;
        long invoiceDeletionTime = 0L;
        int totalEntriesCount = 0;
        int totalDelSchdlCount = 0;
        long entriesDeletionTime = 0L;
        int totalAddressCount = 0;
        long addressDeletionTime = 0L;
        int totalShippingDtlsCount = 0;
        long shippingDtlsDeletionTime = 0L;
        final JnjArchiveOrderData jnjArchiveOrderData = new JnjArchiveOrderData();
        final StringBuilder orderCodeList = new StringBuilder();
        final StringBuilder errorOrderList= new StringBuilder();
        final List<OrderModel> orderModelList = fetchOrdersByCountryCode(countryConfig);

        if(CollectionUtils.isNotEmpty(orderModelList)) {
            LOG.info("Started deleting {} orders for country {} ", orderModelList.size(), countryConfig.getCountryCode());
            final long startTime = System.currentTimeMillis();
            for (final OrderModel orderModel : orderModelList) {
                final String sapOrderNumber = orderModel.getSapOrderNumber();
                try {
                    // Delete invoices
                    long startingTime = System.currentTimeMillis();
                    removeInvoices(orderModel.getInvoices());
                    totalInvoiceCount = totalInvoiceCount + orderModel.getInvoices().size();
                    invoiceDeletionTime = invoiceDeletionTime + System.currentTimeMillis() - startingTime;

                    // Delete order entries
                    startingTime = System.currentTimeMillis();
                    totalDelSchdlCount = totalDelSchdlCount + removeOrderEntries(orderModel.getEntries());
                    totalEntriesCount = totalEntriesCount + orderModel.getEntries().size();
                    entriesDeletionTime = entriesDeletionTime + System.currentTimeMillis() - startingTime;

                    // Delete payment info
                    removePaymentInfo(orderModel);

                    // Delete addresses
                    startingTime = System.currentTimeMillis();
                    totalAddressCount = totalAddressCount + removeOrderAddress(orderModel);
                    addressDeletionTime = addressDeletionTime + System.currentTimeMillis() - startingTime;

                    // Delete shipping details
                    startingTime = System.currentTimeMillis();
                    removeShippingDetails(orderModel.getShippingDetails());
                    totalShippingDtlsCount = totalShippingDtlsCount + orderModel.getShippingDetails().size();
                    shippingDtlsDeletionTime = shippingDtlsDeletionTime + System.currentTimeMillis() - startingTime;

                    getModelService().remove(orderModel);
                    ordersDeletedCount++;
                    orderCodeList.append(sapOrderNumber).append(Jnjlab2bcoreConstants.CONST_COMMA);
                }
                catch (Exception ex){
                    LOG.error("Exception occurred while deleting order with sapOrderNumber {}. " +
                            "Details of error {}", sapOrderNumber, ex.getMessage());
                    errorOrderList.append(sapOrderNumber).append(Jnjlab2bcoreConstants.CONST_COMMA);
                }
            }
            long deleteTime = System.currentTimeMillis() - startTime;
            LOG.info("Time taken to delete {} orders is {} ms", ordersDeletedCount, deleteTime);
            LOG.info("Time taken to delete {} invoices is {} ms", totalInvoiceCount, invoiceDeletionTime);
            LOG.info("Time taken to delete {} order entries  and {} delivery schedule is {} ms", totalEntriesCount,
                    totalDelSchdlCount, entriesDeletionTime);
            LOG.info("Time taken to delete {} addresses is {} ms", totalAddressCount, addressDeletionTime);
            LOG.info("Time taken to delete {} shipping details is {} ms", totalShippingDtlsCount, shippingDtlsDeletionTime);
            if(StringUtils.isNotEmpty(errorOrderList.toString())){
                LOG.info("There was some error while deleting {} orders for country {}",errorOrderList,countryConfig.getCountryCode());
            }
        }

        if(ordersDeletedCount > 0) {
            jnjArchiveOrderData.setOrderCode(orderCodeList.toString());
            jnjArchiveOrderData.setTotalRecords(orderModelList.size());
            jnjArchiveOrderData.setRecordsDeleted(Integer.valueOf(ordersDeletedCount));
        }

        return jnjArchiveOrderData;
    }

    /**
     * Returns list of orders for given country code
     *
     * @param countryConfig country specific config for which orders needs to be fetched
     * @return list of order models
     */
    private List<OrderModel> fetchOrdersByCountryCode(final JnjOrderArchivalJobConfigModel countryConfig) {
        final Map<String, String> paramsMap = setCountryAndSectorsFromConfiguration(countryConfig);
        final long startTime = System.currentTimeMillis();
        List<OrderModel> orderModelList = getOrdersByCountryConfig(paramsMap);
        int orderCount = CollectionUtils.isNotEmpty(orderModelList) ? orderModelList.size() : 0;
        final long deletionTime = System.currentTimeMillis() - startTime;
        LOG.info("Time taken to fetch {} orders for country {} is {} ms", orderCount, countryConfig.getCountryCode(), deletionTime);
        return orderModelList;
    }

    /**
     * validate address is belong to OrderModel
     *
     * @param address of shipping or payment
     * @return validToDelete
     */
    private static boolean checkAddressIsOfOrder(final AddressModel address) {
        return address != null && address.getOwner() instanceof OrderModel;
    }

    /**
     * validate address is belong to Invoice
     *
     * @param address of shipping of invoice
     * @return validToDelete
     */
    private static boolean checkAddressIsOfInvoice(final AddressModel address) {
        return address != null && address.getOwner() instanceof JnjGTInvoiceModel;
    }

    /**
     * validate paymentinfo is belong to OrderModel
     *
     * @param paymentInfo of OrderModel
     * @return validToDelete
     */
    private static boolean checkPaymentInfoIsOfOrder(final PaymentInfoModel paymentInfo) {
        return paymentInfo != null && paymentInfo.getOwner() instanceof OrderModel;
    }

    /**
     * delete the invoice details
     *
     * @param invoices list
     */
    private void removeInvoices(final Collection<JnjGTInvoiceModel> invoices) {
        for (final JnjGTInvoiceModel invoice : invoices) {
            final Collection<JnjGTInvoicePriceModel> invoicePrices = invoice.getPrices();
            if (CollectionUtils.isNotEmpty(invoicePrices)) {
                getModelService().removeAll(invoicePrices);
            }
            if (checkAddressIsOfInvoice(invoice.getShipToAddress())) {
                getModelService().remove(invoice.getShipToAddress());
            }
            removeInvoiceEntries(invoice);
            getModelService().remove(invoice);
        }
    }

    /**
     * delete the invoice entries of given invoice
     *
     * @param invoice invoice for which entries to be deleted.
     */
    private void removeInvoiceEntries(final JnjGTInvoiceModel invoice) {
        final Collection<JnjGTInvoiceEntryModel> jnjGtInvoiceEntries = invoice.getEntries();
        if (CollectionUtils.isNotEmpty(jnjGtInvoiceEntries)) {
            for (final JnjGTInvoiceEntryModel jnjGtInvoiceEntry : jnjGtInvoiceEntries) {
                final Collection<JnjGTInvoiceEntryLotModel> lots = jnjGtInvoiceEntry.getLots();
                if (CollectionUtils.isNotEmpty(lots)) {
                    getModelService().removeAll(lots);
                }
            }
            getModelService().removeAll(jnjGtInvoiceEntries);
        }
    }

    /**
     * Deleted the orderEntries and delivery schedules.
     *
     * @param orderEntries list of order entries
     */
    private int removeOrderEntries(final List<AbstractOrderEntryModel> orderEntries) {
        int totalDelSchdlCount = 0;
        if(CollectionUtils.isNotEmpty(orderEntries)) {
            for (final AbstractOrderEntryModel orderEntry : orderEntries) {
                final List<JnjDeliveryScheduleModel> jnjDeliveryScheduleModel = orderEntry.getDeliverySchedules();
                if (CollectionUtils.isNotEmpty(jnjDeliveryScheduleModel)) {
                    totalDelSchdlCount = totalDelSchdlCount + jnjDeliveryScheduleModel.size();
                    getModelService().removeAll(jnjDeliveryScheduleModel);
                }
                AddressModel deliveryAddress = orderEntry.getDeliveryAddress();
                if (checkAddressIsOfInvoice(deliveryAddress) && BooleanUtils.isTrue(deliveryAddress.getDuplicate())) {
                    getModelService().remove(deliveryAddress);
                }
            }
            getModelService().removeAll(orderEntries);
        }
        return totalDelSchdlCount;
    }

    /**
     * delete the order payment and delivery address
     *
     * @param orderModel for Address deletion
     */
    private int removeOrderAddress(final OrderModel orderModel) {
        int totalAddressCount= 0;
        final AddressModel paymentAddress = orderModel.getPaymentAddress();
        final AddressModel deliveryAddress = orderModel.getDeliveryAddress();
        final boolean isSameAddress = paymentAddress != null && deliveryAddress != null
                && paymentAddress.equals(deliveryAddress);
        if (paymentAddress != null && BooleanUtils.isTrue(paymentAddress.getDuplicate())
                && checkAddressIsOfOrder(paymentAddress)) {
            getModelService().remove(paymentAddress);
            totalAddressCount++;
        }
        if (!isSameAddress && deliveryAddress != null && BooleanUtils.isTrue(deliveryAddress.getDuplicate())
                && checkAddressIsOfOrder(paymentAddress)) {
            getModelService().remove(deliveryAddress);
            totalAddressCount++;
        }
        return totalAddressCount;
    }

    /**
     * delete the all Jnj  GT ShippingDetails
     *
     * @param shippingDetails set of JnjGTShippingDetailsModel
     */
    private void removeShippingDetails(final Set<JnjGTShippingDetailsModel> shippingDetails) {
        if(CollectionUtils.isNotEmpty(shippingDetails)) {
            for (final JnjGTShippingDetailsModel shippingDetailsModel : shippingDetails) {
                final Collection<JnjGTShippingLineDetailsModel> jnjGTShippingLineDetails
                        = shippingDetailsModel.getShippingLineDetails();
                if (CollectionUtils.isNotEmpty(jnjGTShippingLineDetails)) {
                    getModelService().removeAll(jnjGTShippingLineDetails);
                }
            }
            getModelService().removeAll(shippingDetails);
        }
    }

    /**
     * Deletes all the paymentinfo models for a given order
     *
     * @param orderModel orderModel for which payment info data needs to be removed
     */
    private void removePaymentInfo(final OrderModel orderModel) {
        final PaymentInfoModel paymentInfo = orderModel.getPaymentInfo();
        if (paymentInfo != null && BooleanUtils.isTrue(paymentInfo.getDuplicate())
                && checkPaymentInfoIsOfOrder(paymentInfo)) {
            getModelService().remove(paymentInfo);
        }
    }

    /**
     * gets Date with Number of months
     *
     * @param months number of months
     * @return date with months older with hybris dateformat
     */
    public String getDateWithMonths(final String months) {
        int currentDate = 1;
        Date referenceDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(referenceDate);
        calendar.add(Calendar.MONTH, -Integer.parseInt(months));
        calendar.add(Calendar.DATE, +currentDate);
        final String pattern = getConfigurationService().getConfiguration().getString(HYBRIS_DATE_FORMAT);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Retrieves orders for a given country configuration
     *
     * @param paramsMap country configuration parameters
     * @return list of orders
     */
    private List<OrderModel> getOrdersByCountryConfig(final Map<String, String> paramsMap) {
        if (MapUtils.isNotEmpty(paramsMap)) {
            final List<OrderModel> orderList = getJnjOrderArchivalDao().getOrdersWithSectorForArchival(paramsMap);
            if (CollectionUtils.isNotEmpty(orderList)) {
                return orderList.stream()
                        .filter(distinctByKey(AbstractOrderModel::getCode))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    protected static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * set countries And sectors from configuration
     *
     * @param countryConfig the country specific config fetched of JnjOrderArchivalJobConfigModel type
     * @return queryParameters map
     */
    private Map<String, String> setCountryAndSectorsFromConfiguration(final JnjOrderArchivalJobConfigModel countryConfig) {
        String strSector = StringUtils.EMPTY;
        if(StringUtils.isNotEmpty(countryConfig.getBusinessSectorConfig())) {
            strSector = countryConfig.getBusinessSectorConfig();
        }
        return setQueryParametersForSectors(strSector, countryConfig.getCountryCode());
    }

    /**
     * set countries And sectors from configuration
     *
     * @param sectorConfig the sector of String type
     * @param countryCode   the country fetched of String type
     * @return queryParameters map
     */
    private Map<String, String> setQueryParametersForSectors(final String sectorConfig, final String countryCode) {
        Map param = new HashMap();
        if (StringUtils.isNotEmpty(sectorConfig)) {
            final String[] sectorList = sectorConfig.split(
                    getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE));
            if (ArrayUtils.isNotEmpty(sectorList)) {
                for (final String sector : sectorList) {
                    splitSectors(param, countryCode, sector);
                }
            }
        }
        return param;
    }

    /**
     * Split the sector and update the param
     *
     * @param param   the param to be updated
     * @param country the country to be used
     * @param sector  the sector to be splitted
     */
    private void splitSectors(final Map<String, String> param, final String country, final String sector) {
        LOG.info("Sectors configured : {}", sector);
        final String[] splitForDateAndSector = sector.split(
                getConfigurationService().getConfiguration().getString(ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_COLON));
        if (splitForDateAndSector.length == 2) {
            param.put(country + getConfigurationService().getConfiguration()
                            .getString(ORDER_ARCHIVAL_SPLIT_SYMBOL_USED_PIPE_SEPARATED) + splitForDateAndSector[0],
                    getDateWithMonths(splitForDateAndSector[1]));
        }
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
     * @return the jnjOrderArchivalDao
     */
    public JnjOrderArchivalDao getJnjOrderArchivalDao() {
        return jnjOrderArchivalDao;
    }

    /**
     * @param jnjOrderArchivalDao the jnjOrderArchivalDao to set
     */
    public void setJnjOrderArchivalDao(JnjOrderArchivalDao jnjOrderArchivalDao) {
        this.jnjOrderArchivalDao = jnjOrderArchivalDao;
    }
}
