/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.util;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public final class JnjLaEmailPopulatorUtils {

    private static final String QUANTITY_PATTERN = "%d %s";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private JnjLaEmailPopulatorUtils() {
    }

    public static String formatDate(final Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static String readOrderedQuantity(final AbstractOrderEntryModel entry) {
        final Long quantity = readQuantity(entry.getQuantity());
        return formatQuantity(quantity, entry);
    }

    public static String readInvoicedQuantity(final AbstractOrderEntryModel entry) {
        final Long quantity = readQuantity(entry.getInvoicedQuantity());
        return formatQuantity(quantity, entry);
    }

    public static String readOpenedQuantity(final AbstractOrderEntryModel entry) {
        final Long orderedQuantity = readQuantity(entry.getQuantity());
        final Long invoicedQuantity = readQuantity(entry.getInvoicedQuantity());

        return formatQuantity(orderedQuantity - invoicedQuantity, entry);
    }

    public static Long readQuantity(Long quantity) {
        return Optional.ofNullable(quantity).orElse(0L);
    }

    public static String formatQuantity(final Long quantity, final AbstractOrderEntryModel entry) {
        return formatQuantity(quantity, entry.getUnit().getCode());
    }

    public static String formatQuantity(final Long quantity, final String unit) {
        return String.format(QUANTITY_PATTERN, quantity, unit);
    }

}
