/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.converters.populator;

import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.facades.util.JnjLaEmailPopulatorUtils;
import com.jnj.la.core.data.order.JnjLAEmailOrderData;
import com.jnj.la.core.data.order.JnjLAEmailOrderEntryData;
import com.jnj.la.core.data.order.JnjLAEmailScheduleLineData;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.facade.util.impl.JnjLaPriceDataFactory;

import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.store.BaseStoreModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Optional;
import org.apache.log4j.Logger;



import static com.jnj.facades.util.JnjLaEmailPopulatorUtils.formatDate;
import static com.jnj.facades.util.JnjLaEmailPopulatorUtils.formatQuantity;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

public class JnjLAEmailOrderPopulator extends AbstractResultPopulator<OrderModel, JnjLAEmailOrderData> {

    public static final String ORDER_TYPE_PATH = "order.doctype.";
    public static final Long LONG_DEFAULT_VALUE = 0L;
    public static final String ORDER_STATUS_PATH = "order.status.email.";
    public static final String ADDRESS_PATTERN = "%s %s - %s - %s - %s - %s";
    public static final String N_A = "n/a";
    public static final int ZERO_QUANTITY = 0;
	private static final String PAC_HIVE_ENABLED = "pac.aera.enabled";
	public static final String DUMMY_SAP_USER_ID = "dummy@sapuser.com";
	private static final Logger LOG = Logger.getLogger(JnjLAEmailOrderPopulator.class);

    private JnjLaPriceDataFactory priceDataFactory;

    private JnjCommonFacadeUtil jnjCommonFacadeUtil;

    private JnjLatamInvoiceFacade jnjLatamInvoiceFacade;
    
    private static ConfigurationService configurationService;

    protected static JnjGTB2BUnitService jnjGTB2BUnitService;

    @Override
    public void populate(final OrderModel model, final JnjLAEmailOrderData data) throws ConversionException {
        data.setCode(model.getCode());
        
        LOG.info("SAP order number########################: "+model.getSapOrderNumber());
        data.setSapOrderNumber(model.getSapOrderNumber());
        data.setCustomerOrderNumber(model.getPurchaseOrderNumber());

        populateShipTo(model, data);
        Locale locale;
        
		if (model.getUser().getUid().equalsIgnoreCase(DUMMY_SAP_USER_ID)) {
			locale = JnjLaCommonUtil.getLocale(model.getSite().getDefaultCountry().getIsocode(),
					getStore(model).getDefaultLanguage());
		}
		else {
				try 
				{
					locale = JnjLaCommonUtil.getLocale(model.getSite().getDefaultCountry().getIsocode(),
							model.getUser().getSessionLanguage());
				} 
				catch (IndexOutOfBoundsException exception) {
					locale = JnjLaCommonUtil.getLocale(model.getSite().getDefaultCountry().getIsocode(),
							getStore(model).getDefaultLanguage());
					LOG.warn("IndexOutOfBoundsException exception occurred######## " +exception.getMessage() + " Locale value####### " + locale);
				}
		}
        data.setOrderTypeDescription(jnjCommonFacadeUtil.getMessageFromImpex(ORDER_TYPE_PATH + model.getOrderType().getCode(), locale));
        data.setStatus(jnjCommonFacadeUtil.getMessageFromImpex(ORDER_STATUS_PATH + model.getStatus().getCode(), locale));

        data.setOrderEntries(getOrderEntriesData(model, locale));
        data.setCustomer(model.getUser().getDisplayName());

        final Optional<Date> closestEstimatedDeliveredDate = getClosestEstimatedDeliveredDate(model);

        if (closestEstimatedDeliveredDate.isPresent()) {
            data.setNextDeliveryDate(formatDate(closestEstimatedDeliveredDate.get()));
        }

        final Optional<Date> closestCarrierActualDeliveryDate = getClosestCarrierActualDeliveryDate(model);

        if (closestCarrierActualDeliveryDate.isPresent()) {
            data.setDeliveryDate(formatDate(closestCarrierActualDeliveryDate.get()));
        }
    }
     
    private static BaseStoreModel getStore(final OrderModel orderModel) {
        return orderModel.getUnit().getCountry().getBaseStores().iterator().next();
    }
    
    private Optional<Date> getClosestEstimatedDeliveredDate(final OrderModel model) {
        Optional<Date> closestScheduleLineDate = Optional.empty();

        if (CollectionUtils.isNotEmpty(model.getEntries())) {
            closestScheduleLineDate = getClosestScheduleLineDateInOrder(model);
        }

        final Optional<Date> closestCarrierEstimatedDate = getClosestCarrierEstimatedDate(findInvoiceByOrder(model));

        return getClosestDateBetweenScheduleLinesAndCarrierEstimatedDates(closestScheduleLineDate, closestCarrierEstimatedDate);
    }

    private Optional<Date> getClosestCarrierActualDeliveryDate(final OrderModel model) {
        return getClosestCarrierActualDate(findInvoiceByOrder(model));
    }

    private static Optional<Date> getClosestDateBetweenScheduleLinesAndCarrierEstimatedDates(final Optional<Date> closestScheduleLineDate,
                                                                                      final Optional<Date> closestCarrierEstimatedDate) {
        if(closestCarrierEstimatedDate.isPresent() && closestScheduleLineDate.isPresent()) {
            return getClosestEstimatedDeliveryDate(closestScheduleLineDate, closestCarrierEstimatedDate);
        }
        if(closestCarrierEstimatedDate.isPresent()) {
            return Optional.of(closestCarrierEstimatedDate.get());
        }
        if (closestScheduleLineDate.isPresent()) {
            return Optional.of(closestScheduleLineDate.get());
        }
        return Optional.empty();
    }

    private static Optional<Date> getClosestEstimatedDeliveryDate(final Optional<Date> closestScheduleLineDate,
                                                           final Optional<Date> closestCarrierEstimatedDate) {
        final boolean isClosestCarrierEstimatedDate=closestCarrierEstimatedDate.isPresent();
    	final boolean isClosestScheduleLineDate=closestScheduleLineDate.isPresent();
    	if(isClosestCarrierEstimatedDate && isClosestScheduleLineDate && (closestScheduleLineDate.get().before(closestCarrierEstimatedDate.get()))) {
    		return Optional.of(closestScheduleLineDate.get());
    	} else if(isClosestCarrierEstimatedDate){
            return Optional.of(closestCarrierEstimatedDate.get());
        }
    	return Optional.empty();
    }

    private static Optional<Date> getClosestScheduleLineDateInOrder(final OrderModel model) {
        return model.getEntries().stream()
            .map(JnjLAEmailOrderPopulator::getClosestScheduleLinesData)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted()
            .findFirst();
    }

    private static void populateShipTo(final OrderModel model, final JnjLAEmailOrderData data) {
        if (model.getDeliveryAddress() == null) {
            return;
        }
        final JnJB2BUnitModel jnJB2BUnitModel = (JnJB2BUnitModel) jnjGTB2BUnitService.getUnitForUid(model.getDeliveryAddress().getJnJAddressId());
        final String displayName = jnJB2BUnitModel !=null ? jnJB2BUnitModel.getDisplayName() : model.getUnit().getDisplayName();
        data.setShipToName(displayName);

        final AddressModel address = model.getDeliveryAddress();
        final String region = address.getRegion() != null ? address.getRegion().getName() : null;
        final String country = address.getCountry() != null ? address.getCountry().getName() : null;
        LOG.info("Country#######################: "+country);
        data.setShipToAddress(String.format(ADDRESS_PATTERN,
            defaultIfBlank(address.getStreetname(), StringUtils.EMPTY),
            defaultIfBlank(address.getStreetnumber(), StringUtils.EMPTY),
            defaultIfBlank(address.getDistrict(), StringUtils.EMPTY),
            defaultIfBlank(address.getTown(), StringUtils.EMPTY),
            defaultIfBlank(region, StringUtils.EMPTY),
            defaultIfBlank(country, StringUtils.EMPTY)));
    }

    private List<JnjLAEmailOrderEntryData> getOrderEntriesData(final OrderModel model, final Locale locale) {

        final List<JnjLAEmailOrderEntryData> entries = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(model.getEntries())) {
            model.getEntries().forEach(e -> entries.add(getOrderEntryData(model, e, locale)));
        }

        return entries;
    }

    private JnjLAEmailOrderEntryData getOrderEntryData(final OrderModel order, final AbstractOrderEntryModel entryModel, final Locale locale) {
        final JnjLAEmailOrderEntryData entryData = new JnjLAEmailOrderEntryData();
        entryData.setProductCode(entryModel.getProduct().getCode());
        entryData.setProductName(entryModel.getProduct().getName(locale));
        LOG.info("EntryModel###################: "+entryModel.getPk());
        defineQuantities(entryModel, entryData);

        entryData.setPrice(priceDataFactory.formatPrice(BigDecimal.valueOf(entryModel.getTotalPrice()), order.getCurrency()));
        entryData.setRequestedDeliveryDate(formatDate(order.getNamedDeliveryDate()));
        entryData.setScheduleLines(getScheduleLinesData(entryModel));
        entryData.setStatus(jnjCommonFacadeUtil.getMessageFromImpex(ORDER_STATUS_PATH + entryModel.getStatus(), locale));
        return entryData;
    }

    private static void defineQuantities(final AbstractOrderEntryModel entry, final JnjLAEmailOrderEntryData data) {
        data.setOrderedQuantity(JnjLaEmailPopulatorUtils.readOrderedQuantity(entry));
        data.setInvoicedQuantity(JnjLaEmailPopulatorUtils.readInvoicedQuantity(entry));
        data.setOpenedQuantity(JnjLaEmailPopulatorUtils.readOpenedQuantity(entry));
    }

    private static List<JnjLAEmailScheduleLineData> getScheduleLinesData(final AbstractOrderEntryModel entryModel) {
    	final List<JnjLAEmailScheduleLineData> pacHivelines = new ArrayList<>();
        final List<JnjLAEmailScheduleLineData> lines = new ArrayList<>();
        boolean pacHiveEntries = CollectionUtils.isNotEmpty(entryModel.getJnjPacHiveEntries());
        boolean pacHiveEnabled = configurationService.getConfiguration().getBoolean(PAC_HIVE_ENABLED, false) && pacHiveEntries;
        boolean podFlag = CollectionUtils.isEmpty(entryModel.getDeliverySchedules()) || podIsNullOrEmpty(entryModel.getDeliverySchedules());
        if (podFlag && pacHiveEnabled
                && !JnjLaCommonUtil.isDisableCountries(entryModel) && JnjLaCommonUtil.isEnabledForSectors(entryModel)) {
            
                entryModel.getJnjPacHiveEntries().forEach(pl -> pacHivelines.add(getPacHiveLineData(entryModel, pl)));
                return CollectionUtils.isNotEmpty(pacHivelines) ? pacHivelines : Collections.emptyList();
            
        }
        if (CollectionUtils.isNotEmpty(entryModel.getDeliverySchedules())) {
            entryModel.getDeliverySchedules().forEach(l -> lines.add(getScheduleLineData(entryModel, l)));
            return CollectionUtils.isNotEmpty(lines) ? lines : Collections.emptyList();
        }
        return Collections.emptyList();
    }

    private static boolean podIsNullOrEmpty(List<JnjDeliveryScheduleModel> deliverySchedules) {
        return deliverySchedules.stream().findAny().map(JnjDeliveryScheduleModel::getProofOfDeliveryDate).isEmpty();
    }

    private static JnjLAEmailScheduleLineData getPacHiveLineData(final AbstractOrderEntryModel entryModel, final JnjPacHiveEntryModel pacHiveEntryModel) {
        final JnjLAEmailScheduleLineData line = new JnjLAEmailScheduleLineData();
        line.setExpectedDeliveryDate(formatDate(pacHiveEntryModel.getConvertedRecommendedDeliveryDate()));
        line.setScheduledLineNumber(stripLeadingZeros(pacHiveEntryModel.getSchedLineNumber()));        
        line.setQuantity(formatQuantity(ObjectUtils.defaultIfNull(pacHiveEntryModel.getConfirmedQuantity(), LONG_DEFAULT_VALUE).longValue(), entryModel.getUnit().getCode()));
        return line;
    }

	private static JnjLAEmailScheduleLineData getScheduleLineData(final AbstractOrderEntryModel entryModel, final JnjDeliveryScheduleModel lineModel) {
        final JnjLAEmailScheduleLineData line = new JnjLAEmailScheduleLineData();
        line.setExpectedDeliveryDate(formatDate((lineModel.getProofOfDeliveryDate() != null) ? lineModel.getProofOfDeliveryDate()
                        : lineModel.getDeliveryDate()));
        line.setQuantity(formatQuantity(lineModel.getQty(), entryModel.getUnit().getCode()));
        line.setScheduledLineNumber(lineModel.getLineNumber());
        return line;
	}


    private static Optional<Date> getClosestScheduleLinesData(final AbstractOrderEntryModel entryModel) {
        if (CollectionUtils.isNotEmpty(entryModel.getDeliverySchedules())) {
            return entryModel.getDeliverySchedules().stream()
                .map(JnjLAEmailOrderPopulator::getScheduleLineDataWithConfirmedQty)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .findFirst();
        }
        return Optional.empty();
    }

   
	private static Optional<Date> getScheduleLineDataWithConfirmedQty(final JnjDeliveryScheduleModel lineModel) {
        if (lineModel.getDeliveryDate() != null) {
            final Date deliveryDate = lineModel.getDeliveryDate();
            if (hasConfirmedQuantity(lineModel) && isItATodayOrAFutureDate(deliveryDate)) {
                return Optional.of(deliveryDate);
            }
        }
        return Optional.empty();
    }

    private List<JnJInvoiceOrderModel> findInvoiceByOrder(final OrderModel order) {
        if (order.getSapOrderNumber() == null) {
            return new ArrayList<>();
        }
        return jnjLatamInvoiceFacade.getInvoicesByOrders(Arrays.asList(order));
    }

    private static Optional<Date> getClosestCarrierEstimatedDate(final List<JnJInvoiceOrderModel> invoices) {

        if (CollectionUtils.isNotEmpty(invoices)) {
            return invoices.stream()
                .map(JnjLAEmailOrderPopulator::getInvoiceCarrierEstimatedDate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .findFirst();
        }
        return Optional.empty();
    }

    private static Optional<Date> getClosestCarrierActualDate(final List<JnJInvoiceOrderModel> invoices) {

        if (CollectionUtils.isNotEmpty(invoices)) {
            return invoices.stream()
                .map(JnjLAEmailOrderPopulator::getInvoiceCarrierActualDate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .findFirst();
        }
        return Optional.empty();
    }

    private static Optional<Date> getInvoiceCarrierEstimatedDate(final JnJInvoiceOrderModel invoiceOrderModel) {
        if (hasCarrierEstimatedDeliveryDate(invoiceOrderModel) && doesntHasCarrierActualDeliveryDate(invoiceOrderModel)){
            final Date carrierEstimateDeliveryDate = invoiceOrderModel.getCarrierEstimateDeliveryDate();

            if (isItATodayOrAFutureDate(carrierEstimateDeliveryDate)){
                return Optional.of(carrierEstimateDeliveryDate);
            }
        }
        return Optional.empty();
    }

    private static Optional<Date> getInvoiceCarrierActualDate(final JnJInvoiceOrderModel invoiceOrderModel) {
        if (hasCarrierActualDeliveryDate(invoiceOrderModel)){
            final Date carrierConfirmedDeliveryDate = invoiceOrderModel.getCarrierConfirmedDeliveryDate();

            if (isItAPastOrATodayDate(carrierConfirmedDeliveryDate)){
                return Optional.of(carrierConfirmedDeliveryDate);
            }
        }
        return Optional.empty();
    }

    
    /**
	 * to remove leading zeroes
	 * @param sapOrderlineNumber
	 * @return
	 */
	protected static String stripLeadingZeros(final String sapOrderlineNumber)
	{
		return StringUtils.stripStart(sapOrderlineNumber, "0");
	}
    private static boolean doesntHasCarrierActualDeliveryDate(final JnJInvoiceOrderModel invoiceOrderModel) {
        return invoiceOrderModel.getCarrierConfirmedDeliveryDate() == null;
    }

    private static boolean hasCarrierActualDeliveryDate(final JnJInvoiceOrderModel invoiceOrderModel) {
        return invoiceOrderModel.getCarrierConfirmedDeliveryDate() != null;
    }

    private static boolean hasCarrierEstimatedDeliveryDate(final JnJInvoiceOrderModel invoiceOrderModel) {
        return invoiceOrderModel.getCarrierEstimateDeliveryDate() != null;
    }


    private static boolean isItATodayOrAFutureDate(final Date date) {
        final Date today = new Date();
        return date.equals(today) || date.after(today);
    }

    private static boolean isItAPastOrATodayDate(final Date date) {
        final Date today = new Date();
        return date.before(today) || date.equals(today);
    }

    private static boolean hasConfirmedQuantity(final JnjDeliveryScheduleModel lineModel) {
        if (lineModel.getQty() != null) {
            return scheduleLineHasConfirmedQuantity(lineModel);
        } else {
            return false;
        }
    }

    private static boolean scheduleLineHasConfirmedQuantity(final JnjDeliveryScheduleModel lineModel) {
        return lineModel.getQty().compareTo(0L) > ZERO_QUANTITY;
    }

    public void setPriceDataFactory(final JnjLaPriceDataFactory priceDataFactory) {
        this.priceDataFactory = priceDataFactory;
    }

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setJnjLatamInvoiceFacade(final JnjLatamInvoiceFacade jnjLatamInvoiceFacade) {
        this.jnjLatamInvoiceFacade = jnjLatamInvoiceFacade;
    }

	public static ConfigurationService getConfigurationService() {
		return configurationService;
	}
    
	public static void setConfigurationService(final ConfigurationService configurationService) {
		JnjLAEmailOrderPopulator.configurationService = configurationService;
	}

    public static JnjGTB2BUnitService getJnjGTB2BUnitService() {
        return jnjGTB2BUnitService;
    }
    public void setJnjGTB2BUnitService(final JnjGTB2BUnitService jnjGTB2BUnitService) {
        this.jnjGTB2BUnitService = jnjGTB2BUnitService;
    }
}
