/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.order.JnjCheckoutFacade;


/**
 * CartToOrderJob called while placing ReplenishOrder
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjB2bAcceleratorCartToOrderJob extends AbstractJobPerformable<CartToOrderCronJobModel>
{

	private static final Logger LOGGER = Logger.getLogger(JnjB2bAcceleratorCartToOrderJob.class);
	private static final String REPLENISH_ORDER_JOB_METHOD = "perform method of JnjB2bAcceleratorCartToOrderJob";

	@Autowired
	private JnjCheckoutFacade jnjCheckoutFacade;


	@Override
	public PerformResult perform(final CartToOrderCronJobModel cartToOrderCronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.REPLENISH_ORDER_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ REPLENISH_ORDER_JOB_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		try
		{
			jnjCheckoutFacade.placeOrderfromReplenishJob(cartToOrderCronJobModel.getCart());
		}
		catch (final InvalidCartException invalidCartException)
		{
			LOGGER.error(Logging.REPLENISH_ORDER_JOB + Logging.HYPHEN + "InvalidCartException occured. " + Logging.HYPHEN
					+ invalidCartException.getMessage());
			return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);

		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.REPLENISH_ORDER_JOB + Logging.HYPHEN + REPLENISH_ORDER_JOB_METHOD + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}

}
