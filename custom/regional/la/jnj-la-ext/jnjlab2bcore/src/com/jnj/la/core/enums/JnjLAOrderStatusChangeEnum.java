/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 */

package com.jnj.la.core.enums;

import de.hybris.platform.core.enums.OrderStatus;

public enum JnjLAOrderStatusChangeEnum {

    CANCELLED(OrderStatus.CANCELLED.getCode(), 0),
    PENDING(OrderStatus.PENDING.getCode(), 1),
    CREATED(OrderStatus.CREATED.getCode(), 1),
    UNDER_ANALYSIS(OrderStatus.UNDER_ANALYSIS.getCode(), 2),
    BACKORDERED(OrderStatus.BACKORDERED.getCode(), 2),
    BEING_PROCESSED(OrderStatus.BEING_PROCESSED.getCode(), 3),
    IN_PICKING(OrderStatus.IN_PICKING.getCode(), 4),
    PARTIALLY_INVOICED(OrderStatus.PARTIALLY_INVOICED.getCode(), 5),
    PARTIALLY_SHIPPED(OrderStatus.PARTIALLY_SHIPPED.getCode(), 7),
    PARTIALLY_DELIVERED(OrderStatus.PARTIALLY_DELIVERED.getCode(), 9),
    INVOICED(OrderStatus.INVOICED.getCode(), 6),
    SHIPPED(OrderStatus.SHIPPED.getCode(), 8),
    DELIVERED(OrderStatus.DELIVERED.getCode(), 10);

    private final String orderStatus;

    private final int statusWeight;

    JnjLAOrderStatusChangeEnum(final String orderStatus, final int statusWeight) {
        this.orderStatus = orderStatus;
        this.statusWeight = statusWeight;
    }

    public static JnjLAOrderStatusChangeEnum getValue(final String orderStatus) {
        for (JnjLAOrderStatusChangeEnum statusChangeEnum : JnjLAOrderStatusChangeEnum.values()) {
            if (statusChangeEnum.getOrderStatus().equalsIgnoreCase(orderStatus)) {
                return statusChangeEnum;
            }
        }
        return null;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public int getStatusWeight() {
        return statusWeight;
    }
}