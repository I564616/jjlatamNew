/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.cronjobs;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;

import com.jnj.la.core.dto.JnJLaProductDTO;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.dataload.services.JnjLAProductDataService;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import java.util.Set;

public class JnjLaUpsertProductCronJob extends AbstractJobPerformable<JnjIntegrationRSACronJobModel>
{
	private static final Logger LOG = Logger.getLogger(JnjLaUpsertProductCronJob.class);
	private static final int BATCH_SIZE=Jnjb2bCoreConstants.BATCH_SIZE;
	@Autowired
	private JnjLAProductDataService jnjLAProductDataService;
	
	@Override
	public PerformResult perform(final JnjIntegrationRSACronJobModel cronJob)
	{
        List<String> failedProducts = new ArrayList<>();
		final String METHOD_NAME = "perform()";
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD,
				JnjLaUpsertProductCronJob.class);
		// Get the Set of business sectors
		Set<String> sectors = cronJob.getBusinessSectors();

		Date lowerDate = cronJob.getLastSuccessFulRecordProcessTime();
		Date upperDate = jnjLAProductDataService.getLastUpdatedDateForLatestRecord();
		LOG.debug("Lower Date: " + lowerDate + " And Upper Date: " + upperDate);
				
		for (String sector: sectors) {
			try {
				Date startTime = Calendar.getInstance().getTime();

				final List<JnJLaProductDTO> products = jnjLAProductDataService.pullProductsFromRSA(lowerDate, upperDate,
						cronJob, sector);
				
				Date endTime = Calendar.getInstance().getTime();
				long difference = endTime.getTime() - startTime.getTime();
				long differenceMinutes = difference / (60 * 1000) % 60;
				LOG.debug("Time Difference In Minutes of pullProductsFromRSA: " + differenceMinutes);

				processRecords(failedProducts, METHOD_NAME, products);

			} catch (final Exception exception) {
				JnjGTCoreUtil.logErrorMessage(
						Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, "Exception occured while executing upsert product cron job" + "-"
								+ exception.getLocalizedMessage() + ". Exception: " + exception.getMessage(),
						exception, JnjLaUpsertProductCronJob.class);
				return new PerformResult(CronJobResult.FAILURE, CronJobStatus.ABORTED);
			}
		}
		
		setLastSuccessfulDate(cronJob, upperDate);
		
		if (CollectionUtils.isNotEmpty(failedProducts)) {
			LOG.info("Failed records count: " + failedProducts.size());
			LOG.info("Failed products: " + failedProducts);

		}
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME, Logging.END_OF_METHOD,
				JnjLaUpsertProductCronJob.class);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void setLastSuccessfulDate(
			final JnjIntegrationRSACronJobModel cronJob, Date upperDate) {
		cronJob.setLastSuccessFulRecordProcessTime(upperDate);
		
		try {
			modelService.save(cronJob);
		} catch(ModelSavingException exe) {
			LOG.error("Error while saving the last successful date",exe);
		}
	}

	private void processRecords(List<String> failedProducts,
			final String METHOD_NAME, final List<JnJLaProductDTO> products) {
		if (CollectionUtils.isNotEmpty(products)) {
			Date stTime = Calendar.getInstance().getTime();
			LOG.info("Start Time saveProductToHybris : " + stTime);

			// Applying batch logic here
			processInBatch(products, failedProducts);

			Date enTime = Calendar.getInstance().getTime();
			LOG.debug("End Time saveProductToHybris : " + enTime);
			long diffInTime = enTime.getTime() - stTime.getTime();
			long diffInMinutes = diffInTime / (60 * 1000) % 60;
			LOG.debug("Time Difference In Minutes of saveProductToHybris : " + diffInMinutes);
		} else {
			JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_PRODUCT_NAME, METHOD_NAME,
					"There is no new data to be saved into hybris. Ending cronjob.", JnjLaUpsertProductCronJob.class);
		}
	}

	private void processInBatch(final List<JnJLaProductDTO> products, List<String> failedProducts) {
		List<JnJLaProductDTO> productList = new ArrayList<>(products);
		for(int index = 0; index < productList.size(); index += BATCH_SIZE)
		{
			int endIndex = Math.min(index + BATCH_SIZE, productList.size());
			List<JnJLaProductDTO> batchSubList = productList.subList(index,endIndex);
			LOG.info("Batch SubList Size: " + batchSubList.size());
			jnjLAProductDataService.saveProductToHybris(batchSubList, failedProducts);
		}
	}

}
