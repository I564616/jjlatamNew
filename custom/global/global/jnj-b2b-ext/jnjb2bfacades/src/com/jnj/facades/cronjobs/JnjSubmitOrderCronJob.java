/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.cronjobs;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.order.JnjOrderFacade;


/**
 * TODO:<Sumit-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjSubmitOrderCronJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = Logger.getLogger(JnjSubmitOrderCronJob.class);

	@Autowired
	JnjOrderService jnjOrderService;

	@Autowired
	@Qualifier(value = "jnjCustomOrderFacade")
	JnjOrderFacade jnjOrderFacade;

	/**
	 * This method is responsible for calling the JnjOrderServiceImpl class for retrieving those orders for which SAP
	 * order Number doesn't exist in the hybris database.
	 * 
	 * @param cronJobModel
	 * @return PerformResult
	 */
	@Override
	public PerformResult perform(final CronJobModel cronJobModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_CRON_JOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<OrderModel> orderModelList = jnjOrderService.getSubmitOrderDataList();
		if (null != orderModelList && !orderModelList.isEmpty())
		{
			// Iterating the order model and send it to the SAP Submit Order Service to 
			// create the order model at the SAP end.
			for (final OrderModel orderModel : orderModelList)
			{
				//To Create Method of SAP from here.
				//jnjOrderFacade.invokeSalesOrderCreationWrapper(orderModel);
				//Check & Transfer empenho docs to SFTP if exists for order
				if (StringUtils.isNotEmpty(orderModel.getSapOrderNumber()))
				{
					final String destinationDir = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
							+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
							+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SHARED_FOLDER_LOCATION_ORDER);
					final List<String> empenhoDocs = JnJXMLFilePicker.getFilesFromDir(orderModel.getCode(), destinationDir);
					if (CollectionUtils.isNotEmpty(empenhoDocs))
					{
						final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
						jnjSftpFileTransferUtil.uploadEmpenhoDocsToSftp(empenhoDocs, orderModel.getSapOrderNumber(),
								orderModel.getCode());
					}
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("CronJob runs successfully");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_CRON_JOB + Logging.HYPHEN + "perform()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);

	}
}
