/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.core.dao.order;

import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface JnjOrderArchivalDao {
    /**
     * fetch orders for archival
     *
     * @param param Map with Key country pipe seperated with sector and value with order creationtime
     * @return List<OrderModel>
     */
    List<OrderModel> getOrdersWithSectorForArchival(final Map<String, String> param);
}
