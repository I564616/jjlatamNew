/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.impl;

import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.la.core.enums.JnJOrderColumnStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JnjUpdateCarrierStatusHelper {

    void processOrderEntry(final AbstractOrderEntryModel orderEntry, final List<JnJInvoiceOrderModel> ordersInvoices) {
        if (orderEntry == null) {
            return;
        }

        initializeQuantities(orderEntry);
        updateQuantities(orderEntry, ordersInvoices);
        updateCarrierStatus(orderEntry);
    }

    private static void initializeQuantities(final AbstractOrderEntryModel entry) {
        entry.setInvoicedQuantity(0L);
        entry.setShippedQuantity(0L);
        entry.setDeliveredQuantity(0L);
    }

    private static void updateQuantities(final AbstractOrderEntryModel orderEntry, final List<JnJInvoiceOrderModel> ordersInvoices) {
        for (JnJInvoiceOrderModel invoice : ordersInvoices) {
            updateQuantities(orderEntry, invoice);
        }
    }

    private static void updateQuantities(final AbstractOrderEntryModel orderEntry, final JnJInvoiceOrderModel invoice) {
        final List<JnJInvoiceEntryModel> invoiceEntries = findInvoiceEntriesByOrderEntry(orderEntry, invoice);
        for (JnJInvoiceEntryModel invoiceEntry : invoiceEntries) {
            final Integer qty = invoiceEntry.getQty();

            if (StringUtils.isNotBlank(invoice.getCancelledDocNo())){
                orderEntry.setInvoicedQuantity(orderEntry.getInvoicedQuantity() - qty);
            } else {
                orderEntry.setInvoicedQuantity(orderEntry.getInvoicedQuantity() + qty);
            }


            if (invoice.getCarrierEstimateDeliveryDate() != null) {
                orderEntry.setShippedQuantity(orderEntry.getShippedQuantity() + qty);
            }

            if (invoice.getCarrierConfirmedDeliveryDate() != null) {
                orderEntry.setDeliveredQuantity(orderEntry.getDeliveredQuantity() + qty);
            }
        }
    }

    private static List<JnJInvoiceEntryModel> findInvoiceEntriesByOrderEntry(final AbstractOrderEntryModel orderEntry, final JnJInvoiceOrderModel invoice) {
        if (orderEntry.getProduct() == null) {
            return new ArrayList<>();
        }

        final List<JnJInvoiceEntryModel> invoiceEntries = invoice.getEntries();
        final String productCode = orderEntry.getProduct().getCode();
        final String salesOrderItemNum = orderEntry.getSapOrderlineNumber();

        return invoiceEntries.stream().filter(entry -> {
            return entry.getMaterial() != null && entry.getMaterial().getCode() != null && entry.getMaterial().getCode().equals(productCode) && entry.getSalesOrderItemNo().equals(salesOrderItemNum);
        }).collect(Collectors.toList());
    }

    private static void updateCarrierStatus(final AbstractOrderEntryModel entry) {
        entry.setCarrierEstDeliveryDateStatus(calculateStatus(entry.getShippedQuantity(), entry.getQuantity()));
        entry.setCarrierActualDeliveryDateStatus(calculateStatus(entry.getDeliveredQuantity(), entry.getQuantity()));
    }

    private static JnJOrderColumnStatus calculateStatus(final Long actualQuantity, final Long requiredQuantity){
        if (actualQuantity >= requiredQuantity) {
            return JnJOrderColumnStatus.C;
        } else if (actualQuantity > 0) {
            return JnJOrderColumnStatus.B;
        }
        return JnJOrderColumnStatus.A;
    }


}
