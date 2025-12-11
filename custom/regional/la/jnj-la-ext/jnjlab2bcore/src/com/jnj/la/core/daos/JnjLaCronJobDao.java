/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.la.core.daos;

import de.hybris.platform.cronjob.model.CronJobModel;

import java.util.List;

import com.jnj.la.core.dto.JnjLaCronJobMonitoringEmailDto;

public interface JnjLaCronJobDao
{
    public List<JnjLaCronJobMonitoringEmailDto> fetchJobMonitoringStaticData(final List<String> monitoringCode);

    public List<CronJobModel> getCronJobModelByCode(final List<String> jobCode);
}
