/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.facades.order;

import com.jnj.la.core.data.JnjArchiveOrderData;
import com.jnj.la.core.model.JnjOrderArchivalJobConfigModel;

/**
 * Interface Class responsible for handling archival orders fetching and deletion.
 */
public interface JnjOrderArchivalFacade {

    /**
     * saves the ArchiveResult to model
     *
     * @param jnjArchiveOrderData to save in model
     */
    void saveArchiveResult(final JnjArchiveOrderData jnjArchiveOrderData);

    /**
     * Deletes and archives the orders for a given country code
     *
     * @param countryConfig for which orders needs to be archived
     * @return jnjArchiveOrderData to save in model
     */
    JnjArchiveOrderData archiveOrdersByCountry(final JnjOrderArchivalJobConfigModel countryConfig);
}
