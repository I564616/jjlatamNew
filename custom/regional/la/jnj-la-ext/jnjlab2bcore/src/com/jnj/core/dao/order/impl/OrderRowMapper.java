/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.dao.order.impl;

import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.dto.order.JnjOrderLineDTO;
import com.jnj.core.dto.order.JnjOrderSchLineDTO;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.enums.JnJOrderColumnStatus;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRowMapper implements RowMapper<List<JnjOrderDTO>> {

    private static final String BLANK = "";
    private static final String REASON_BLANK = "jnj.orderLine.rejection.reason.blank";
    private static final String REASON_A_OPTIONS = "jnj.orderLine.rejection.reason.A.options";
    private static final String REASON_B_OPTIONS = "jnj.orderLine.rejection.reason.B.options";
    private static final String REASON_DEFAULT = "jnj.orderLine.rejection.reason.default";
    private static final String ORDER_NUM = "ORDER_NUM";
    private static final String ORD_LINE_NUM = "ORD_LINE_NUM";

    @Override
    public List<JnjOrderDTO> mapRow(final ResultSet orderRecords, final int rowNum) throws SQLException {
        final String METHOD_NAME = "mapRow";
        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.BEGIN_OF_METHOD, OrderRowMapper.class);
        final List<JnjOrderDTO> jnjOrderList = new ArrayList<>();

        final Map<String, JnjOrderDTO> mapOrderNumAndOrderDto = new HashMap<>();

        final Map<String, List<JnjOrderLineDTO>> mapOrderNumAndOrderLineDTO = new HashMap<>();

        final Map<String, List<JnjOrderSchLineDTO>> mapOrderNumAndOrderSchLine = new HashMap<>();

        do {
            // Maps Order Header
            final String orderNumber = orderRecords.getString(ORDER_NUM);
            if (!mapOrderNumAndOrderDto.containsKey(orderNumber)) {
                mapOrderNumAndOrderDto.put(orderNumber, mapOrderHeader(orderRecords));
            }

            // Maps Order Lines
            if (!mapOrderNumAndOrderLineDTO.containsKey(orderNumber)) {
                mapOrderNumAndOrderLineDTO.put(orderNumber, mapOrderLine(orderRecords, mapOrderNumAndOrderLineDTO, true));
            } else {
                mapOrderNumAndOrderLineDTO.put(orderNumber, mapOrderLine(orderRecords, mapOrderNumAndOrderLineDTO, false));
            }

            // Maps Order Schedule Lines

            final String orderLineNumber = orderRecords.getString(ORD_LINE_NUM);

            final String schLineKey = orderNumber + orderLineNumber;

            // Add Schedule line if not added already
            if (!mapOrderNumAndOrderSchLine.containsKey(schLineKey)) {
                mapOrderNumAndOrderSchLine.put(schLineKey, mapScheduleLine(orderRecords, mapOrderNumAndOrderSchLine, true));
            } else {
                mapOrderNumAndOrderSchLine.put(schLineKey, mapScheduleLine(orderRecords, mapOrderNumAndOrderSchLine, false));
            }

        } while (orderRecords.next());

        for (final Map.Entry<String, JnjOrderDTO> entry : mapOrderNumAndOrderDto.entrySet()) {
            final String orderNumber = entry.getKey();
            final JnjOrderDTO order = mapOrderNumAndOrderDto.get(orderNumber);

            final List<JnjOrderLineDTO> orderLines = mapOrderNumAndOrderLineDTO.get(orderNumber);
            if (orderLines != null) {
                for (final JnjOrderLineDTO jnjOrderLine : orderLines) {
                    final String schLineKey = jnjOrderLine.getOrderNumber() + jnjOrderLine.getOrderLineNumber();
                    final List<JnjOrderSchLineDTO> orderSchLines = mapOrderNumAndOrderSchLine.get(schLineKey);
                    // Sets Order Schedule Line to Order Line
                    if (orderSchLines != null) {
                        jnjOrderLine.setJnjOrderSchLines(orderSchLines);
                    }
                }
                // Sets Order Line to Order
                order.setJnjOrderLines(orderLines);
            }

            if (order != null) {
                jnjOrderList.add(order);
            }
        }

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.END_OF_METHOD, OrderRowMapper.class);
        return jnjOrderList;
    }

    /**
     * @throws SQLException
     */
    private JnjOrderDTO mapOrderHeader(final ResultSet orderRecords) throws SQLException {
        final String METHOD_NAME = "mapOrderHeader";
        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.BEGIN_OF_METHOD, OrderRowMapper.class);

        final JnjOrderDTO jnjOrder = new JnjOrderDTO();
        // Order numbers
        jnjOrder.setSAPOrderNumber(orderRecords.getString(ORDER_NUM));
        jnjOrder.setOrderNumber(orderRecords.getString("HYBRIS_ORDER_NUM"));
        jnjOrder.setCustomerPONumber(orderRecords.getString("PO_NUM"));
        // Fields used to map order entries statuses
        jnjOrder.setInvoiceStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("INVOICE_STATUS")));
        jnjOrder.setSalesOrderCreditStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("CREDIT_STATUS")));
        jnjOrder.setSalesOrderDeliveryStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("DELIVERY_STATUS")));
        jnjOrder.setSalesOrderOverallStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("OVERALL_STATUS")));
        jnjOrder.setSalesOrderRejectionStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("REJECTION_STATUS")));

        jnjOrder.setCompleteDelivery(orderRecords.getString("COMPLETE_DELV"));
        jnjOrder.setContractNumber(orderRecords.getString("CONTRACT_REF"));
        jnjOrder.setDeliveryMode(orderRecords.getString("ORDER_TYPE"));
        jnjOrder.setDistributionChannel(orderRecords.getString("DIST_CHANNEL"));
        jnjOrder.setDivision(orderRecords.getString("DIVISION"));
        jnjOrder.setForbiddenSales(orderRecords.getString("FORBIDDEN_SALES"));
        jnjOrder.setHeaderDeliveryBlock(orderRecords.getString("DELIVERY_BLOCK"));
        jnjOrder.setNamedDeliveryDate(orderRecords.getString("REQ_DELV_DT"));
        jnjOrder.setPayFromNumber(orderRecords.getString("PAY_FROM_CUST_NUM"));
        jnjOrder.setPoType(orderRecords.getString("ORDER_CHANNEL"));
        jnjOrder.setSalesOrderDataCompleteness(orderRecords.getString("HEA_DATA_CM_DT"));
        jnjOrder.setSalesOrganizationCode(orderRecords.getString("SALES_ORG"));
        jnjOrder.setShipToNumber(orderRecords.getString("SHIP_TO_CUST_NUM"));
        jnjOrder.setSoldToNumber(orderRecords.getString("SOLD_TO_CUST_NUM"));
        jnjOrder.setStartDate(orderRecords.getString("ORDER_CREATE_DT"));
        jnjOrder.setTotalNetPrice(orderRecords.getString("NET_VAL_ORD_TOT"));
        jnjOrder.setLastUpdatedDate(orderRecords.getString("LAST_UPDATED_DATE"));

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.END_OF_METHOD, OrderRowMapper.class);
        return jnjOrder;
    }

    /**
     * @param mapOrderNumAndOrderLineDTO
     * @param newOrderLine
     * @throws SQLException
     */
    private List<JnjOrderLineDTO> mapOrderLine(final ResultSet orderRecords, final Map<String, List<JnjOrderLineDTO>> mapOrderNumAndOrderLineDTO, final boolean newOrderLine) throws SQLException {
        final String METHOD_NAME = "mapOrderLine";
        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.BEGIN_OF_METHOD, OrderRowMapper.class);
        final String orderNumber = orderRecords.getString(ORDER_NUM);
        List<JnjOrderLineDTO> jnjOrderLineList;
        if (newOrderLine) {
            jnjOrderLineList = new ArrayList<>();
        } else {
            jnjOrderLineList = mapOrderNumAndOrderLineDTO.get(orderNumber);
        }

        final JnjOrderLineDTO jnjOrderLine = new JnjOrderLineDTO();
        jnjOrderLine.setEntryNumber(orderRecords.getString(ORD_LINE_NUM));
        jnjOrderLine.setOrderLineNumber(orderRecords.getString(ORD_LINE_NUM));
        jnjOrderLine.setOrderNumber(orderRecords.getString(ORDER_NUM));
        jnjOrderLine.setCode(orderRecords.getString("ORDERED_MATERIAL_NUM"));
        jnjOrderLine.setMaterialEntered(orderRecords.getString("MATERIAL_ENTERED"));

		// Fields used to map order entries statuses
		jnjOrderLine.setLineOverallStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("LINE_OVERALL_STATUS")));
		final String orderLineRejectionStatus = JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("RSN_FOR_REJ"));
		jnjOrderLine.setReasonForRejection(getMappedOrderLineRejectionStatus(orderLineRejectionStatus));
		jnjOrderLine.setDeliveryStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("DELV_STATUS")));
		jnjOrderLine.setLineInvoiceStatus(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("LINE_INVOICE_STATUS")));
		jnjOrderLine.setGTSHold(JnjLaCommonUtil.getValidStringOrNull(orderRecords.getString("GTS_HOLD")));

        // Fees and taxes
        jnjOrderLine.setMinimumOrderFee(orderRecords.getString("MIN_ORDER_FEE"));
        jnjOrderLine.setDropShipFee(orderRecords.getString("DROP_SHIP_FEE"));
        jnjOrderLine.setExpeditedFee(orderRecords.getString("EXPDITED_FEES"));
        jnjOrderLine.setFreightcharges(orderRecords.getString("FREIGHT_FEES"));
        jnjOrderLine.setGrossPrice(orderRecords.getString("GROSS_PRICE"));
        jnjOrderLine.setHandlingFee(orderRecords.getString("HANDLING_FEE"));
        // Discounts
        jnjOrderLine.setDiscount(orderRecords.getString("DISCOUNTS_PROMO"));

        jnjOrderLine.setHigherLevelItemNumber(orderRecords.getString("HIGH_LVL_ITEM_NUM"));
        jnjOrderLine.setIndirectCustomer(orderRecords.getString("IND_CUST_ACCOUNT"));
        jnjOrderLine.setInsurance(orderRecords.getString("INSURANCE"));
        jnjOrderLine.setInternationalFreight(orderRecords.getString("INTL_FREIGHT"));
        jnjOrderLine.setItemCategory(orderRecords.getString("ITEM_CATEGORY"));
        jnjOrderLine.setNetPrice(orderRecords.getString("NET_VALUE"));

        jnjOrderLine.setQuantity(orderRecords.getString("ORDERED_QTY"));

        jnjOrderLine.setSalesUOM(orderRecords.getString("SALES_UOM"));
        jnjOrderLine.setTax(orderRecords.getString("TAX_AMT"));
        jnjOrderLine.setTaxesLocal(orderRecords.getString("LOCAL_TAXES"));
        jnjOrderLine.setUnitPrice(orderRecords.getString("UNIT_PRICE"));

        // Prevents duplicated lines from being added.
        if (null != jnjOrderLineList && !contains(jnjOrderLineList, jnjOrderLine)) {
            jnjOrderLineList.add(jnjOrderLine);
        }

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.END_OF_METHOD, OrderRowMapper.class);
        return jnjOrderLineList;
    }

    private String getMappedOrderLineRejectionStatus(final String orderLineRejectionStatus) {
        final String status;
        if (StringUtils.isBlank(orderLineRejectionStatus)) {
            status = statusFromString(Config.getString(REASON_BLANK, BLANK)).toString();
        } else if (Config.getString(REASON_A_OPTIONS, BLANK).contains(orderLineRejectionStatus)) {
            status = JnJOrderColumnStatus.A.toString();
        } else if (Config.getString(REASON_B_OPTIONS, BLANK).contains(orderLineRejectionStatus)) {
            status = JnJOrderColumnStatus.B.toString();
        } else {
            status = statusFromString(Config.getString(REASON_DEFAULT, BLANK)).toString();
        }
        return status;
    }

    private JnJOrderColumnStatus statusFromString(final String status) {
        return JnJOrderColumnStatus.valueOf(status.trim());
    }

    private boolean contains(final List<JnjOrderLineDTO> jnjOrderLineList, final JnjOrderLineDTO jnjOrderLine) {
        if (jnjOrderLineList == null) {
            return false;
        }

        for (final JnjOrderLineDTO ol : jnjOrderLineList) {
            if (ol.getOrderNumber().equals(jnjOrderLine.getOrderNumber()) && ol.getOrderLineNumber().equals(jnjOrderLine.getOrderLineNumber())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param newSchLine
     * @param mapOrderNumAndOrderSchLine
     * @throws SQLException
     */
    private List<JnjOrderSchLineDTO> mapScheduleLine(final ResultSet orderRecords, final Map<String, List<JnjOrderSchLineDTO>> mapOrderNumAndOrderSchLine, final boolean newSchLine) throws SQLException {
        final String METHOD_NAME = "mapScheduleLine";
        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.BEGIN_OF_METHOD, OrderRowMapper.class);
        List<JnjOrderSchLineDTO> jnjOrderSchLineList;
        final String orderNumber = orderRecords.getString(ORDER_NUM);
        final String OrderLineNumber = orderRecords.getString(ORD_LINE_NUM);
        if (newSchLine) {
            jnjOrderSchLineList = new ArrayList<>();
        } else {
            final String key = orderNumber + OrderLineNumber;
            jnjOrderSchLineList = mapOrderNumAndOrderSchLine.get(key);
        }

        final JnjOrderSchLineDTO jnjOrderSchLine = new JnjOrderSchLineDTO();
        jnjOrderSchLine.setLineNumber(orderRecords.getString("SCHEDULE_LINE_NUM"));
        jnjOrderSchLine.setOrderLineNumber(orderRecords.getString(ORD_LINE_NUM));
        jnjOrderSchLine.setOrderNumber(orderRecords.getString(ORDER_NUM));

        jnjOrderSchLine.setDeliveryDate(orderRecords.getString("SCHED_DELIVERY_DT"));
        jnjOrderSchLine.setRoundedQuantity(orderRecords.getString("ROUNDED_QUANTITY"));
        jnjOrderSchLine.setConfirmedQuantity(orderRecords.getString("CONFIRMED_QTY"));
        jnjOrderSchLine.setCarrierExpectedDeliveryDate(orderRecords.getString("CEDD"));
        jnjOrderSchLine.setProofOfDeliveryDate(orderRecords.getString("POD"));
        jnjOrderSchLineList.add(jnjOrderSchLine);

        JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, METHOD_NAME, Logging.END_OF_METHOD, OrderRowMapper.class);
        return jnjOrderSchLineList;
    }

}
