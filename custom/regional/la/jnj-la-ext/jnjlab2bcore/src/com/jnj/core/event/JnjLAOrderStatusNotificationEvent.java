/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;

public class JnjLAOrderStatusNotificationEvent extends AbstractCommerceUserEvent {

    private OrderModel order;

    private JnJEmailPeriodicity periodicity;

    public JnjLAOrderStatusNotificationEvent(final OrderModel order, final JnJEmailPeriodicity periodicity) {
        this.order = order;
        this.periodicity = periodicity;
    }

    public OrderModel getOrder() {
        return order;
    }

    public JnJEmailPeriodicity getPeriodicity() {
        return periodicity;
    }

    @Override
    public String toString() {
        return "JnjLAOrderStatusNotificationEvent{order="+ order.toString() + ", periodicity=" + periodicity.getCode() + "}";
    }
}