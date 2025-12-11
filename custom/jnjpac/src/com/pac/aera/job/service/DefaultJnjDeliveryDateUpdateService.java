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
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.pac.aera.job.dao.JnjPacAeraDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gt.pac.aera.constants.Jnjgtb2bpacConstants.PAC_AERA_DAILY_EMAIL_CRON_JOBS;

/**
 * The default implementation of {@link JnjDeliveryDateUpdateService} which has been intentionally designed to be
 * customizable so that is can be overridden or configured using properties by different regions if any customizations
 * are required.
 */
public class DefaultJnjDeliveryDateUpdateService implements JnjDeliveryDateUpdateService
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjDeliveryDateUpdateService.class);

	protected JnjPacAeraDao jnjPacAeraDao;
	protected ModelService modelService;
	protected CronJobService cronJobService;
	protected ConfigurationService configurationService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateEstimatedDeliveryDate(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	throws PacHiveException
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");

		final AbstractOrderEntryModel orderEntryModel;
		try
		{
			orderEntryModel = this.jnjPacAeraDao.findCorrespondingOrderEntryModel(
					jnJPacAeraResponse
			);
		} catch (Exception e)
		{
			throw new PacHiveException(String.format(
					"[PAC HIVE] Can not store JnJPacAeraResponse because inside order with sapOrderNumber '%s' no" +
					" order entry was found with catalogCode: '%s' and lineNumber: '%s'." +
					" PAC HIVE data supposed to be saved: schedLineNumber: '%s' recommendedDeliveryDate: '%s'.",
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getCatalogCode(),
					jnJPacAeraResponse.getLineNumber(),
					jnJPacAeraResponse.getSchedLineNumber(),
					jnJPacAeraResponse.getRecommendedDeliveryDate(),
					jnJPacAeraResponse.getConfirmedQuantity(),
					jnJPacAeraResponse.getSubFranchise(),
					jnJPacAeraResponse.getRequestedUnitsTotalQuantity(),
					jnJPacAeraResponse.getAmountPendingDelivery(),
					jnJPacAeraResponse.getQuantityPendingStock()
			), e);
		}

		final JnjPacHiveEntryModel pacHiveEntryModel = this.findOrCreateJnjPacHiveEntryModel(
				jnJPacAeraResponse, orderEntryModel
		);

		this.populateJnjPacHiveEntryModel(jnJPacAeraResponse, pacHiveEntryModel);

		// We do need to save both because cascade save might not work if we only modify entries
		this.modelService.save(orderEntryModel);
		this.modelService.save(pacHiveEntryModel);

		LOG.debug(
				"[PAC HIVE] JnjPacHiveEntryModel with PK '{}' for sapOrderlineNumber '{}' has been successfully" +
				" saved. PAC HIVE data: schedLineNumber: '{}' recommendedDeliveryDate: '{}'.",
				JnjPacHiveEntryModel.PK,
				jnJPacAeraResponse.getOrderNumber(),
				jnJPacAeraResponse.getSchedLineNumber(),
				jnJPacAeraResponse.getRecommendedDeliveryDate()
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateAdditionalFields(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");
		// Override me
	}

	@Nonnull
	protected JnjPacHiveEntryModel findOrCreateJnjPacHiveEntryModel(
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse,
			@Nonnull final AbstractOrderEntryModel orderEntryModel
	)
	{
		Validate.notNull(jnJPacAeraResponse, "jnJPacAeraResponse can not be null.");
		Validate.notNull(orderEntryModel, "orderEntryModel can not be null.");

		JnjPacHiveEntryModel pacHiveEntryModel = null;
		try
		{
			pacHiveEntryModel = this.jnjPacAeraDao.findJnjPacHiveEntryModel(jnJPacAeraResponse, orderEntryModel);
		} catch (Exception e)
		{
			LOG.debug("[PAC HIVE] No JnjPacHiveEntryModel was found.", e);
		}

		if (null == pacHiveEntryModel)
		{
			return this.createJnjPacHiveEntryModel(jnJPacAeraResponse, orderEntryModel);
		}

		return pacHiveEntryModel;
	}

	@Nonnull
	protected JnjPacHiveEntryModel createJnjPacHiveEntryModel(
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse,
			@Nonnull final AbstractOrderEntryModel orderEntryModel
	)
	{
		Validate.notNull(jnJPacAeraResponse, "jnJPacAeraResponse can not be null.");
		Validate.notNull(orderEntryModel, "orderEntryModel can not be null.");

		JnjPacHiveEntryModel pacHiveEntryModel;
		pacHiveEntryModel = this.modelService.create(JnjPacHiveEntryModel.class);

		final Collection<JnjPacHiveEntryModel> jnjPacHiveEntries;
		jnjPacHiveEntries = CollectionUtils.isEmpty(orderEntryModel.getJnjPacHiveEntries())
		                    ? new ArrayList<>()
		                    : new ArrayList<>(orderEntryModel.getJnjPacHiveEntries());

		jnjPacHiveEntries.add(pacHiveEntryModel);
		orderEntryModel.setJnjPacHiveEntries(jnjPacHiveEntries);

		pacHiveEntryModel.setOrderEntry(orderEntryModel);

		LOG.debug(
				"[PAC HIVE] A new JnjPacHiveEntryModel has been created (not saved yet) because an existing one was" +
				" not found for the order with sapOrderNumber '{}' and order entry with catalogCode: '{}'" +
				" and sapOrderlineNumber '{}'. PAC HIVE data: schedLineNumber: '{}' recommendedDeliveryDate: '{}'.",
				jnJPacAeraResponse.getOrderNumber(),
				jnJPacAeraResponse.getCatalogCode(),
				jnJPacAeraResponse.getLineNumber(),
				jnJPacAeraResponse.getSchedLineNumber(),
				jnJPacAeraResponse.getRecommendedDeliveryDate(),
				jnJPacAeraResponse.getConfirmedQuantity(),
				jnJPacAeraResponse.getSubFranchise(),
				jnJPacAeraResponse.getRequestedUnitsTotalQuantity(),
				jnJPacAeraResponse.getAmountPendingDelivery(),
				jnJPacAeraResponse.getQuantityPendingStock()
		);

		return pacHiveEntryModel;
	}

	protected void populateJnjPacHiveEntryModel(
			final JnJPacAeraResponse jnJPacAeraResponse,
			final JnjPacHiveEntryModel pacHiveEntryModel
	)
	{
		pacHiveEntryModel.setCompany(jnJPacAeraResponse.getCompany());
		pacHiveEntryModel.setOrderType(jnJPacAeraResponse.getOrderType());
		pacHiveEntryModel.setOrderNumber(jnJPacAeraResponse.getOrderNumber());
		pacHiveEntryModel.setLineNumber(jnJPacAeraResponse.getLineNumber());
		pacHiveEntryModel.setSchedLineNumber(jnJPacAeraResponse.getSchedLineNumber());
		pacHiveEntryModel.setCatalogCode(jnJPacAeraResponse.getCatalogCode());
		pacHiveEntryModel.setDataSource(jnJPacAeraResponse.getDataSource());
		pacHiveEntryModel.setRecommendedDeliveryDate(jnJPacAeraResponse.getRecommendedDeliveryDate());
		pacHiveEntryModel.setConfirmedQuantity(Double.valueOf(jnJPacAeraResponse.getConfirmedQuantity()));
		pacHiveEntryModel.setSubFranchise(jnJPacAeraResponse.getSubFranchise());
		pacHiveEntryModel.setRequestedUnitsTotalQuantity(Double.valueOf(jnJPacAeraResponse.getRequestedUnitsTotalQuantity()));
		pacHiveEntryModel.setAmountPendingDelivery(Double.valueOf(jnJPacAeraResponse.getAmountPendingDelivery()));
		pacHiveEntryModel.setQuantityPendingStock(Double.valueOf(jnJPacAeraResponse.getQuantityPendingStock()));

		final Date deliveryDate = this.parseRecommendedDeliveryDate(jnJPacAeraResponse);
		pacHiveEntryModel.setConvertedRecommendedDeliveryDate(deliveryDate);

		LOG.debug(
				"[PAC HIVE] JnjPacHiveEntryModel with PK '{}' has been updated (not saved yet)." +
				" sapOrderNumber '{}' catalogCode: '{}' sapOrderlineNumber '{}' schedLineNumber: '{}'" +
				" recommendedDeliveryDate: '{}'.",
				pacHiveEntryModel.getPk(),
				jnJPacAeraResponse.getOrderNumber(),
				jnJPacAeraResponse.getCatalogCode(),
				jnJPacAeraResponse.getLineNumber(),
				jnJPacAeraResponse.getSchedLineNumber(),
				jnJPacAeraResponse.getRecommendedDeliveryDate(),
				jnJPacAeraResponse.getConfirmedQuantity(),
				jnJPacAeraResponse.getSubFranchise(),
				jnJPacAeraResponse.getRequestedUnitsTotalQuantity(),
				jnJPacAeraResponse.getAmountPendingDelivery(),
				jnJPacAeraResponse.getQuantityPendingStock()
		);
	}

	@Nullable
	protected Date parseRecommendedDeliveryDate(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	{
		Validate.notNull(jnJPacAeraResponse, "jnJPacAeraResponse can not be null.");

		Date deliveryDate = null;
		try
		{
			deliveryDate = this.convertAeraDateToJavaDate(jnJPacAeraResponse.getRecommendedDeliveryDate());
		} catch (final ParseException e)
		{
			LOG.error(
					"[PAC HIVE] Can not update OrderEntry pacHiveEstimatedDeliveryDate for sapOrderNumber '{}' and" +
					" sapOrderlineNumber '{}' because of the error in parsing date: '{}'.",
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getLineNumber(),
					jnJPacAeraResponse.getRecommendedDeliveryDate(),
					e
			);
		}

		return deliveryDate;
	}

	/**
	 * Converts a string representing Julian date to Java date.
	 * <p>
	 * This method has been intentionally made protected so that it can be reused or overridden by other regions.
	 *
	 * @param julianDateString String representing Julian date.
	 * @return Java Date converted from the Julian date string.
	 */
	protected Date convertAeraDateToJavaDate(final String julianDateString) throws ParseException
	{
		Date startDate = null;// the julian format to be converted to MM-DD-YY format as required
		final Date date = new SimpleDateFormat("Myydd").parse(julianDateString);
		final String startDateString = new SimpleDateFormat("dd.MM.yyyy").format(date);
		final DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		startDate = df.parse(startDateString);
		LOG.info("[PAC HIVE] Date to be updated is: '{}'.", startDate);
		final String newDateString = df.format(startDate);
		LOG.info("[PAC HIVE] THIS IS IN DATE FORMAT: '{}'.", newDateString);

		return startDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void runEmailCronJob()
	{
		final List<String> cronJobs = this.getCronJobsToPerform();
		LOG.debug("[PAC HIVE] Going to launch the next cron jobs: '{}'.", cronJobs);

		if (CollectionUtils.isEmpty(cronJobs))
		{
			return;
		}

		for (String cronJob : cronJobs)
		{
			this.performCronJob(cronJob);
		}

		LOG.debug("[PAC HIVE] All cron jobs '{}' have been launched.", cronJobs);
	}

	@Nonnull
	protected List<String> getCronJobsToPerform()
	{
		final String cronJobsString = this.configurationService.getConfiguration().getString(
				PAC_AERA_DAILY_EMAIL_CRON_JOBS,
				StringUtils.EMPTY
		);

		if (StringUtils.isBlank(cronJobsString))
		{
			LOG.debug(
					"[PAC HIVE] No cron jobs are required to run according to the property '{}'.",
					PAC_AERA_DAILY_EMAIL_CRON_JOBS
			);

			return Collections.emptyList();
		}

		final String[] cronJobCodes = cronJobsString.split(",");
		return Arrays.asList(cronJobCodes);
	}

	protected void performCronJob(@Nullable final String cronJobCode)
	{
		if (StringUtils.isBlank(cronJobCode))
		{
			LOG.error(
					"[PAC HIVE] Cron Job code can not be blank. Please check your configuration property: '{}'.",
					PAC_AERA_DAILY_EMAIL_CRON_JOBS
			);
			return;
		}

		try
		{
			final CronJobModel cronJobModel = this.cronJobService.getCronJob(cronJobCode);

			LOG.debug("[PAC HIVE] Going to run cron job '{}'. Model: '{}'.", cronJobCode, cronJobModel);
			this.cronJobService.performCronJob(cronJobModel);
			LOG.debug("[PAC HIVE] Cron job '{}' has been launched. Model: '{}'.", cronJobCode, cronJobModel);
		} catch (Exception e)
		{
			LOG.error("[PAC HIVE] Can not launch Cron Job with code '{}'.", cronJobCode, e);
		}
	}

	public void setJnjPacAeraDao(JnjPacAeraDao jnjPacAeraDao)
	{
		this.jnjPacAeraDao = jnjPacAeraDao;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setCronJobService(CronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public JnjPacAeraDao getJnjPacAeraDao()
	{
		return jnjPacAeraDao;
	}

	public ModelService getModelService()
	{
		return this.modelService;
	}

	public CronJobService getCronJobService()
	{
		return this.cronJobService;
	}

	public ConfigurationService getConfigurationService()
	{
		return this.configurationService;
	}
}
