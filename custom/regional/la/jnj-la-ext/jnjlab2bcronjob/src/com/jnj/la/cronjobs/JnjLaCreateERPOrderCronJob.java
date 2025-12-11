/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.la.cronjobs;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.order.JnjLatamCheckoutFacade;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.model.ExportERPOrderCronjobModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * This cronjob class will retrigger ERP order creation for failed orders
 *
 * @author rshukl19
 *
 */
public class JnjLaCreateERPOrderCronJob extends AbstractJobPerformable<ExportERPOrderCronjobModel> {

	@Autowired
	private JnjLatamCheckoutFacade jnjLatamCheckoutFacade;

	@Autowired
	private JnjLaOrderDao jnjLaOrderDao;
	
	private final Class currentClass = JnjLaCreateERPOrderCronJob.class;

	@Override
	public PerformResult perform(final ExportERPOrderCronjobModel cronjob) {

		String methodName = "performJnjLaCreateERPOrderCronJob";
		List<OrderModel> orderCodes = jnjLaOrderDao.getFailedErpOrdersList(cronjob.getOrderCodes(),cronjob.getOrderTypes(),cronjob.getStartDate(),cronjob.getEndDate());
		JnjGTCoreUtil.logInfoMessage(Jnjb2bFacadesConstants.Logging.CREATE_ORDER, methodName,
				"Fetched order codes: " + orderCodes.size(), currentClass);
		jnjLatamCheckoutFacade.createERPOrder(orderCodes);
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

}
