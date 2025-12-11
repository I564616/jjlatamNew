/*
 * Copyright: Copyright © 2023
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.converters.populator;

import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjInvoiceService;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.util.JnjLaEmailPopulatorUtils;
import com.jnj.la.core.data.order.JnjLAConsolidatedReportRowData;

import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.model.LoadTranslationModel;
import com.jnj.la.core.services.JnjLoadTranslationService;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.facade.util.impl.JnjLaPriceDataFactory;
import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.jnj.facades.converters.populator.JnjLAEmailOrderPopulator.ADDRESS_PATTERN;
import static com.jnj.facades.converters.populator.JnjLAEmailOrderPopulator.ORDER_STATUS_PATH;
import static com.jnj.facades.converters.populator.JnjLAEmailOrderPopulator.ORDER_TYPE_PATH;
import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;

import java.text.DecimalFormat;

public class JnjLaConsolidatedReportRowDataPopulator extends AbstractResultPopulator<AbstractOrderEntryModel, JnjLAConsolidatedReportRowData> {

    private static final Class<JnjLaConsolidatedReportRowDataPopulator> currentClass = JnjLaConsolidatedReportRowDataPopulator.class;

    private static final String SLASH_SEPARATOR = " / ";
    final DecimalFormat decimalFormat = new DecimalFormat("#");
	private static final String PAC_HIVE_ENABLED = "pac.aera.enabled";
    private static final double DEFAULT_VALUE = 0d;
    private JnjLaPriceDataFactory priceDataFactory;
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;
    private JnjLoadTranslationService loadTranslationService;
    private JnjInvoiceService invoiceService;

    @Autowired
    private JnjLAOrderService jnjLAOrderService;
    private static ConfigurationService configurationService;

    @Override
    public void populate(final AbstractOrderEntryModel entry, final JnjLAConsolidatedReportRowData data)
            throws ConversionException {
        final Locale locale = JnjLaCommonUtil.getLocaleForCountryIsoCode(getCountryIsoCode(entry));

        populateWithOrderHeader(entry.getOrder(), data, locale);
        populateWithProduct(entry.getProduct(), entry.getOrder().getUnit(), data, locale);
        populateWithOrderEntry(entry, data, locale);
        populateWithInvoices(entry, data);
    }

    private static String getCountryIsoCode(AbstractOrderEntryModel entry) {
        AbstractOrderModel order = entry.getOrder();
        if (order != null && order.getUnit() != null && order.getUnit().getCountry() != null) {
            return order.getUnit().getCountry().getIsocode();
        }
        return "AR"; // fallback to spanish
    }

    private void populateWithInvoices(final AbstractOrderEntryModel entry, final JnjLAConsolidatedReportRowData data) {
        final List<JnJInvoiceOrderModel> invoices = findInvoicesByOrderEntry(entry);

        data.setInvoiceNumber(StringUtils.join(findInvoiceNumbers(entry, invoices), SLASH_SEPARATOR));
    }

    private Set<String> findInvoiceNumbers(final AbstractOrderEntryModel entry, final List<JnJInvoiceOrderModel> invoices) {
        if (isFromBrazil(entry)) {
            return invoices.stream().map(JnJInvoiceOrderModel::getNfNumber).collect(Collectors.toSet());
        }
        return invoices.stream().map(JnJInvoiceOrderModel::getInvDocNo).collect(Collectors.toSet());
    }

    private List<JnJInvoiceOrderModel> findInvoicesByOrderEntry(final AbstractOrderEntryModel entry) {
        final List<JnJInvoiceOrderModel> invoices = invoiceService.getInvoicebyOrderCode(
                entry.getOrder().getSapOrderNumber());
        return invoices.stream().filter(hasAnyEntry(entry)).collect(Collectors.toList());
    }

    private boolean isFromBrazil(final AbstractOrderEntryModel entry) {
        return Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL.equals(getCountryIsoCode(entry));
    }

    private Predicate<JnJInvoiceOrderModel> hasAnyEntry(AbstractOrderEntryModel entry) {
        return i -> i.getEntries().stream().anyMatch(e ->
            e.getMaterial() != null &&
            e.getMaterial().getCode() != null &&
            entry.getProduct() != null &&
            e.getMaterial().getCode().equals(entry.getProduct().getCode())
        );
    }

    private void populateWithOrderEntry(AbstractOrderEntryModel entry, JnjLAConsolidatedReportRowData data,
                                        Locale locale) {
        defineQuantities(entry, data);
        inspectQuantities(data);

        data.setSalesUnit(entry.getUnit().getCode());
        data.setUnitPriceString(priceDataFactory.formatPrice(BigDecimal.valueOf(entry.getTotalPrice()),
                entry.getOrder().getCurrency()));
        data.setSapLineNumber(entry.getSapOrderlineNumber());
        data.setStatus(jnjCommonFacadeUtil.getMessageFromImpex(
                ORDER_STATUS_PATH + entry.getStatus(), locale));
        data.setRequestedDeliveryDate(entry.getExpectedDeliveryDate());
        populateEDDInDummyScheduleLines(entry,data);
        data.setEstimatedDeliveryDate(findEarliestScheduleDeliveryDate(entry, data));
        data.setIndirectCustomer(entry.getIndirectCustomer());
        
        data.setUnitPrice(entry.getTotalPrice());
        data.setHasChanged(entry.getPendingConsolidatedEmail());
        data.setCurrency(entry.getOrder().getCurrency().getSymbol());
    }

    private void populateWithOrderHeader(final AbstractOrderModel order, final JnjLAConsolidatedReportRowData data,
                                         final Locale locale) {
        final String methodName = "populateWithOrderHeader()";

        data.setSapOrderNumber(order.getSapOrderNumber());
        data.setCustomerOrderNumber(order.getPurchaseOrderNumber());
        data.setOrderType(jnjCommonFacadeUtil.getMessageFromImpex(
                ORDER_TYPE_PATH + order.getOrderType().getCode(), locale));

        populateShipTo(order, data);

        data.setCreationDate(order.getDate());
        final String orderChannelCode = ((OrderModel) order).getPoType();
        try {
            data.setSalesOrderChannel(jnjLAOrderService.getOrderChannel(orderChannelCode).getName());
        } catch (final BusinessException businessException) {
            JnjGTCoreUtil.logWarnMessage("Consolidated open order report", methodName,
                    "Cannot fetch order channel for order [" + order.getSapOrderNumber() + "]."
                + Jnjb2bCoreConstants.Logging.HYPHEN + businessException.getLocalizedMessage(), businessException, currentClass);
        }

        data.setOrderStatus(jnjCommonFacadeUtil.getMessageFromImpex(
                ORDER_STATUS_PATH + order.getStatus().getCode(), locale));
        data.setCreatedBy(order.getUser().getName());
        data.setContractReference(order.getContractNumber());
    }

    private void populateWithProduct(final ProductModel product, final B2BUnitModel unit,
                                     JnjLAConsolidatedReportRowData data, Locale locale) {
        if (product == null) {
            return;
        }

        if (!CollectionUtils.isEmpty(product.getSupercategories())) {
            data.setFirstHierarchy(getFirstHierarchy(product.getSupercategories().iterator().next()));
        }

        data.setProductCode(product.getCode());
        data.setProductName(product.getName(locale));
        data.setEanNumber(product.getEan());

        if (product instanceof JnJLaProductModel) {
            final LoadTranslationModel loadTranslation = loadTranslationService.getLoadTranslationModelByProductNumber(
                    (JnJLaProductModel) product, (JnJB2BUnitModel) unit);
            if (loadTranslation != null) {
                data.setCustomerMaterialCode(loadTranslation.getCustMaterialNum());
            }
        }
    }

    private Date findEarliestScheduleDeliveryDate(final AbstractOrderEntryModel entry,
                                                  final JnjLAConsolidatedReportRowData data) {
        if (ObjectUtils.isEmpty(entry)) {
            return null;
        }
        final List<JnjDeliveryScheduleModel> scheduleLinesModel = new ArrayList<>(entry.getDeliverySchedules());
        scheduleLinesModel.removeIf(Objects::isNull);
       
        boolean pacHiveEnabled = configurationService.getConfiguration().getBoolean(PAC_HIVE_ENABLED);
        boolean podFlag = podIsNullOrEmpty(scheduleLinesModel) ||
                !OrderEntryStatus.DELIVERED.getCode().equalsIgnoreCase(entry.getStatus());
        boolean pacHiveEntryFlag = CollectionUtils.isNotEmpty(entry.getJnjPacHiveEntries()) && pacHiveEnabled;
        
        if (podFlag && pacHiveEntryFlag && !JnjLaCommonUtil.isDisableCountries(entry)
                && JnjLaCommonUtil.isEnabledForSectors(entry)) {
            List<JnjPacHiveEntryModel> pacHiveScheduleLine = entry.getJnjPacHiveEntries().stream()
                    .sorted(Comparator.comparing(JnjPacHiveEntryModel::getConvertedRecommendedDeliveryDate))
                    .collect(Collectors.toList());
            Optional<JnjPacHiveEntryModel> pacHiveEntryModelOptional = pacHiveScheduleLine.stream().findFirst();
            settingPacHiveQuantity(data, pacHiveEntryModelOptional);
            return pacHiveScheduleLine.stream().findFirst().map(
                    JnjLaConsolidatedReportRowDataPopulator::getPacHiveDeliveryDate).orElse(null);
        }
        List<JnjDeliveryScheduleModel> collectScheduleLines = scheduleLinesModel.stream().
            filter(s -> s.getQty() != null && s.getQty().compareTo(0L) > 0).
            filter(this::isDateInFuture).collect(Collectors.toList());

        collectScheduleLines.sort(comparing(JnjDeliveryScheduleModel::getDeliveryDate, nullsLast(naturalOrder())));

        if(OrderEntryStatus.DELIVERED.getCode().equalsIgnoreCase(entry.getStatus())) {
            List<JnjDeliveryScheduleModel> scheduleLine = scheduleLinesModel.stream().sorted(
                    Comparator.comparing(JnjDeliveryScheduleModel::getProofOfDeliveryDate)).collect(Collectors.toList());
            return scheduleLine.stream().findFirst().map(
                    JnjLaConsolidatedReportRowDataPopulator::getProofOfDeliveryDate).orElse(null);
        }
        return collectScheduleLines.stream().findFirst().map(s -> s.getDeliveryDate()).orElse(null);
    }

    /**
     *  Setting Pac Hive Quantity data
     * @param data
     * @param pacHiveEntryModelOptional
     */
    private void settingPacHiveQuantity(JnjLAConsolidatedReportRowData data, Optional<JnjPacHiveEntryModel> pacHiveEntryModelOptional) {
        if (pacHiveEntryModelOptional.isPresent()) {
            Double qty = pacHiveEntryModelOptional.get().getConfirmedQuantity();
            final double qtyValue = qty != null && qty.doubleValue() > DEFAULT_VALUE ? qty.doubleValue() : DEFAULT_VALUE;
            data.setQuantity(decimalFormat.format(qtyValue));
        }
    }

    private static Date getPacHiveDeliveryDate(JnjPacHiveEntryModel jnjPacHiveEntryModel) {
    	return jnjPacHiveEntryModel.getConvertedRecommendedDeliveryDate();
    }
    private static Date getProofOfDeliveryDate(JnjDeliveryScheduleModel scheduleLine){
    	return scheduleLine.getProofOfDeliveryDate();
    }

    private static boolean podIsNullOrEmpty(List<JnjDeliveryScheduleModel> deliverySchedules) {
        return deliverySchedules.stream().findAny().map(JnjDeliveryScheduleModel::getProofOfDeliveryDate).isEmpty();
    }
   
    /**
   	 * @param sourceEntryModel order entry
     * @param data consolidated report data
   	 * @return
   	 */
	private static void populateEDDInDummyScheduleLines(final AbstractOrderEntryModel sourceEntryModel,
			JnjLAConsolidatedReportRowData data) {
        List<JnjLAConsolidatedReportRowData> dataList = new ArrayList<>();
		final boolean pacHiveEnabled = configurationService.getConfiguration().getBoolean(PAC_HIVE_ENABLED, false);
		if (pacHiveEnabled && !JnjLaCommonUtil.isDisableCountries(sourceEntryModel)
				&& JnjLaCommonUtil.isEnabledForSectors(sourceEntryModel)
				&& CollectionUtils.isNotEmpty(sourceEntryModel
						.getJnjPacHiveEntries())) {
			sourceEntryModel.getJnjPacHiveEntries().forEach(
					l -> dataList.add(getPacHiveLineData(l)));
			if (CollectionUtils.isNotEmpty(dataList) && !dataList.isEmpty()) {
				data.setEstimatedDeliveryDate(dataList.iterator().next()
						.getEstimatedDeliveryDate());
			}

		}
	}

    private static JnjLAConsolidatedReportRowData getPacHiveLineData(final JnjPacHiveEntryModel pacHiveEntryModel) {
        final JnjLAConsolidatedReportRowData line = new JnjLAConsolidatedReportRowData();
        line.setEstimatedDeliveryDate(pacHiveEntryModel.getConvertedRecommendedDeliveryDate());
        line.setQuantity(String.valueOf(ObjectUtils.defaultIfNull(
                pacHiveEntryModel.getConfirmedQuantity(), StringUtils.EMPTY)));
        return line;
    }
	
    
    private boolean isDateInFuture(JnjDeliveryScheduleModel deliverySchedule) {
        Date date = deliverySchedule.getDeliveryDate();
        if (date == null) {
            return false;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().isAfter(LocalDateTime.now());
    }

    private static void populateShipTo(final AbstractOrderModel order, final JnjLAConsolidatedReportRowData data) {
        data.setIssuerCode(order.getUnit().getUid());
        data.setIssuerName(order.getUnit().getDisplayName());

        if (order.getDeliveryAddress() != null) {
            final AddressModel address = order.getDeliveryAddress();
            final String region = address.getRegion() != null ? address.getRegion().getName() : null;
            final String country = address.getCountry() != null ? address.getCountry().getName() : null;
            data.setDeliveryAddress(String.format(ADDRESS_PATTERN,
                defaultIfBlank(address.getStreetname(), StringUtils.EMPTY),
                defaultIfBlank(address.getStreetnumber(), StringUtils.EMPTY),
                defaultIfBlank(address.getDistrict(), StringUtils.EMPTY),
                defaultIfBlank(address.getTown(), StringUtils.EMPTY),
                defaultIfBlank(region, StringUtils.EMPTY),
                defaultIfBlank(country, StringUtils.EMPTY)));
        }
    }

    private String getFirstHierarchy(final CategoryModel hierarchy) {
        if (BooleanUtils.isFalse(hierarchy.getDisplayProducts())
                || CollectionUtils.isEmpty(hierarchy.getSupercategories())) {
            return hierarchy.getName();
        }
        return getFirstHierarchy(hierarchy.getSupercategories().iterator().next());
    }

    private static void defineQuantities(final AbstractOrderEntryModel entry, final JnjLAConsolidatedReportRowData data) {
        final Long orderedQuantity = JnjLaEmailPopulatorUtils.readQuantity(entry.getQuantity());
        final Long invoicedQuantity = JnjLaEmailPopulatorUtils.readQuantity(entry.getInvoicedQuantity());
    	
    	data.setQuantity(String.valueOf(orderedQuantity));
    	data.setInvoicedQuantity(String.valueOf(invoicedQuantity));
    	data.setOpenedQuantity(String.valueOf(orderedQuantity-invoicedQuantity));
    }

    private static void inspectQuantities(final JnjLAConsolidatedReportRowData data) {
        inspectValue(data.getQuantity(), data);
        inspectValue(data.getInvoicedQuantity(), data);
        inspectValue(data.getOpenedQuantity(), data);
    }

    private static void inspectValue(final String value, final JnjLAConsolidatedReportRowData data){
        if (value != null && value.contains("-")){
            final StringBuilder message = new StringBuilder();
            message.append("Ordered: ").append(data.getQuantity()).append(", ");
            message.append("invoiced: ").append(data.getInvoicedQuantity()).append(", ");
            message.append("opened: ").append(data.getOpenedQuantity()).append(".");

            JnjGTCoreUtil.logErrorMessage("Negative quantity found", "inspectValue()",
                    message.toString(), currentClass);
        }
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

    public void setPriceDataFactory(final JnjLaPriceDataFactory priceDataFactory) {
        this.priceDataFactory = priceDataFactory;
    }

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setLoadTranslationService(JnjLoadTranslationService loadTranslationService) {
        this.loadTranslationService = loadTranslationService;
    }

    public void setInvoiceService(JnjInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

	public static ConfigurationService getConfigurationService() {
		return configurationService;
	}
    
	public static void setConfigurationService(final ConfigurationService configurationService) {
		JnjLaConsolidatedReportRowDataPopulator.configurationService = configurationService;
	}
}