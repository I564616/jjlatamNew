/**
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderEntryCloneHelper {

    private static final Logger LOG = Logger.getLogger(OrderEntryCloneHelper.class);
    private static final String KEY_SEPARATOR = "-";

    Map<String, OrderEntryModel> cloneEntries(List<AbstractOrderEntryModel> entries) {
        if (entries == null) {
            return new HashMap<>();
        }

        try {
            return removeNulls(entries).stream().collect(Collectors.toMap(this::createCloneKey, this::cloneEntry));
        } catch (Exception e) {
            LOG.error("Could not clone order entries.", e);
            return new HashMap<>();
        }
    }

    private List<AbstractOrderEntryModel> removeNulls(List<AbstractOrderEntryModel> entries) {
        return entries.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String createCloneKey(final AbstractOrderEntryModel entry) {
        if (StringUtils.isNotBlank(entry.getSapOrderlineNumber())) {
            return entry.getSapOrderlineNumber() + KEY_SEPARATOR + entry.getProduct().getCode();
        }
        return createSimplifiedCloneKey(entry);
    }

    private String createSimplifiedCloneKey(final AbstractOrderEntryModel entry) {
        return entry.getProduct().getCode();
    }

    void copyClonedEntryValues(final Map<String, OrderEntryModel> clonedOrderEntries, final OrderEntryModel newOrderEntry) {
        if (newOrderEntry != null && newOrderEntry.getProduct() != null) {
            OrderEntryModel clonedOrderEntry = clonedOrderEntries.get(createCloneKey(newOrderEntry));
            if (clonedOrderEntry == null) {
                clonedOrderEntry = clonedOrderEntries.get(createSimplifiedCloneKey(newOrderEntry));
            }

            if (clonedOrderEntry != null){
                copyValues(clonedOrderEntry, newOrderEntry);
            }
        }
    }

    private OrderEntryModel cloneEntry(AbstractOrderEntryModel originalEntry) {
        final OrderEntryModel clonedEntry = new OrderEntryModel();
        copyValues(originalEntry, clonedEntry);
        return clonedEntry;
    }

    protected void copyValues(AbstractOrderEntryModel source, AbstractOrderEntryModel target) {
        target.setPreviousStatus(source.getPreviousStatus());
        target.setQuantityStatus(source.getQuantityStatus());
        target.setStatus(source.getStatus());
        target.setPendingDailyEmail(source.getPendingDailyEmail());
        target.setPendingImmediateEmail(source.getPendingImmediateEmail());
        target.setPendingConsolidatedEmail(source.getPendingConsolidatedEmail());
    }

}
