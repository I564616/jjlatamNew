/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */
package com.jnj.la.core.services;

import com.jnj.la.core.data.JnjArchiveOrderData;
import com.jnj.la.core.model.JnjOrderArchivalJobConfigModel;

/**
 * Interface Class responsible for handling archival orders fetching and deletion.
 */
public interface JnjOrderArchivalService {

    /**
     * Deletes and archives the Orders for a given country code
     *
     * @param countryConfig for which orders needs to be archived
     * @return JnjArchiveOrderData archive data to save in model
     */
    JnjArchiveOrderData deleteOrdersForCountry(final JnjOrderArchivalJobConfigModel countryConfig);
}
