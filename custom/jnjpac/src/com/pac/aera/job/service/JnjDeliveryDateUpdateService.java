/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.pac.aera.job.service;

import com.gt.pac.aera.JnJPacAeraResponse;
import com.gt.pac.aera.PacHiveException;

import jakarta.annotation.Nonnull;


/**
 * The JnjDeliveryDateUpdateService is the interface which allows to update delivery date or any other data
 * accordingly to the data received from PAC.
 *
 * @author Cognizant
 */
public interface JnjDeliveryDateUpdateService
{

	/**
	 * This method is used to update PAC delivery date on {@link com.jnj.core.model.JnjDeliveryScheduleModel}.
	 * corresponding to the received {@link JnJPacAeraResponse}.
	 *
	 * @param jnJPacAeraResponse POJO with the data from the JSON file received from AERA.
	 */
	void updateEstimatedDeliveryDate(@Nonnull JnJPacAeraResponse jnJPacAeraResponse) throws PacHiveException;

	/**
	 * If some region requires custom logic to be executed while updating delivery date they can create an empty
	 * region-specific extension, override this method and put all their custom code into it.
	 *
	 * @param jnJPacAeraResponse POJO with the data from the JSON file received from AERA.
	 */
	void updateAdditionalFields(@Nonnull JnJPacAeraResponse jnJPacAeraResponse);

	/**
	 * Runs cron jobs configured by {@link com.gt.pac.aera.constants.Jnjgtb2bpacConstants#PAC_AERA_DAILY_EMAIL_CRON_JOBS}
	 * This property allows different regions to configure which jobs they want to run.
	 * If the property is empty no cron jobs will be run at all.
	 */
	void runEmailCronJob();
}
