/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services.impl;

import com.jnj.facades.services.JnjLAOrderEmailMatrixService;
import com.jnj.la.core.dao.order.JnjLAEmailMatrixDao;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;

public class JnjLAOrderEmailMatrixServiceImpl implements JnjLAOrderEmailMatrixService {

    private static final Logger LOG = Logger.getLogger(JnjLAOrderEmailMatrixServiceImpl.class);
    private static final String ENTRY_ERROR_MESSAGE = "Entry: %s must not trigger e-mail according to Previous: %s and Current: %s status combination";

    private JnjLAEmailMatrixDao jnjLAEmailMatrixDao;

    @Override
    public boolean mustSendEmail(final OrderModel order, final JnJEmailPeriodicity periodicity) {
        if (JnJEmailPeriodicity.IMMEDIATE.equals(periodicity)) {
            return order.getEntries().stream().filter(e -> BooleanUtils.isTrue(e.getPendingImmediateEmail())).anyMatch(this::mustSendEmail);
        } else if (JnJEmailPeriodicity.DAILY.equals(periodicity)) {
            return order.getEntries().stream().filter(e -> BooleanUtils.isTrue(e.getPendingDailyEmail())).anyMatch(this::mustSendEmail);
        }
        return false;
    }

    private boolean mustSendEmail(final AbstractOrderEntryModel entry) {
        final boolean result = jnjLAEmailMatrixDao.sendEmailByStatuses(entry.getPreviousStatus(), entry.getQuantityStatus());

        if (!result) {
            LOG.warn(String.format(ENTRY_ERROR_MESSAGE, entry.getProduct().getCode(), entry.getPreviousStatus(), entry.getQuantityStatus()));
        }

        return result;
    }

    public void setJnjLAEmailMatrixDao(final JnjLAEmailMatrixDao jnjLAEmailMatrixDao) {
        this.jnjLAEmailMatrixDao = jnjLAEmailMatrixDao;
    }
}