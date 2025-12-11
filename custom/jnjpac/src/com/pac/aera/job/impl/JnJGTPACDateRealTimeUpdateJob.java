/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.pac.aera.job.impl;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.pac.aera.job.service.JnjDeliveryDateUpdateService;
import com.pac.aera.job.service.JnjGTPacHiveConfigurationService;
import com.pac.aera.job.util.JnJGTPACJSONParserUtil;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.gt.pac.aera.constants.Jnjgtb2bpacConstants.*;


/**
 * Cronjob responsible for Fetchind Estimated Delivery Date from AERA in realtime.
 */
public class JnJGTPACDateRealTimeUpdateJob extends AbstractJobPerformable<CronJobModel>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(JnJGTPACDateRealTimeUpdateJob.class);

	private JnJGTPACJSONParserUtil jnJGTPACJSONParserUtil;
	private JnjDeliveryDateUpdateService jnjDeliveryDateUpdateService;
	private ConfigurationService configurationService;
	private JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		CronJobResult cronJobResult = CronJobResult.SUCCESS;
		CronJobStatus cronJobStatus = CronJobStatus.FINISHED;

		if (this.jnjGTPacHiveConfigurationService.isPacHiveDisabledGlobally())
		{
			LOGGER.info(
					"[PAC HIVE] PAC HIVE is disabled. AERA real-time job will be stopped without processing any data.");
			return new PerformResult(cronJobResult, cronJobStatus);
		}

		final boolean isUpdatedSuccesfully = parsePacAeraResponse();
		LOGGER.info("[PAC HIVE] Delivery dates updated succesfully: '{}'.", isUpdatedSuccesfully);
		if (!isUpdatedSuccesfully)
		{
			cronJobResult = CronJobResult.ERROR;
			cronJobStatus = CronJobStatus.ABORTED;
		}
		LOGGER.info("[PAC HIVE] Job completed successfully");
		return new PerformResult(cronJobResult, cronJobStatus);
	}


	public boolean parsePacAeraResponse()
	{
		ResponseEntity<String> response = null;
		boolean isUpdatedSuccessfully = false;
		final URI fileToBeDownloaded = null;
		org.json.JSONObject responseObject = null;
		try
		{
			final RestTemplate restTemplate = new RestTemplate(jnJGTPACJSONParserUtil.clientHttpRequestFactory());
			final HttpHeaders headers = jnJGTPACJSONParserUtil.addOauthHeaderParameters();
			final HttpEntity<String> entity = new HttpEntity<>("paramters", headers);
			response = restTemplate.exchange(Config.getParameter(PAC_AERA_REALTIME_UPDATE_URL), HttpMethod.GET, entity,
			                                 String.class
			);
			LOGGER.info("[PAC HIVE] Response from AERA with S3 file path: '{}'.", response.getBody());
			responseObject = new org.json.JSONObject(response.getBody());


			final Regions region = this.getAwsRegion();
			if (null == region)
			{
				LOGGER.error(
						"[PAC HIVE] Failed download AERA JSON file because AWS region specified in property '{}'" +
						" is invalid.", PAC_AMAZON_S3_REGION
				);
				return false;
			}

			final List<File> downloadedlocalFileList = downloadFilesFromBucket(
					getAmazonS3URI(responseObject, fileToBeDownloaded),
					getAmazonS3Client(region)
			);
			if (CollectionUtils.isNotEmpty(downloadedlocalFileList))
			{
				isUpdatedSuccessfully = processLocalFiles(downloadedlocalFileList);
			}
			else 
			{
				isUpdatedSuccessfully = true;
			}
		} catch (final JSONException e)
		{
			LOGGER.error("[PAC HIVE] Failed to parse AERA response body.", e);
		} catch (final HttpClientErrorException e)
		{
			LOGGER.info("[PAC HIVE] Failed to construct AWS S3 URI.", e);
		}

		return isUpdatedSuccessfully;

	}

	/**
	 * @return the jnJGTPACJSONParserUtil
	 */
	public JnJGTPACJSONParserUtil getJnJGTPACJSONParserUtil()
	{
		return jnJGTPACJSONParserUtil;
	}

	private static AmazonS3 getAmazonS3Client(final Regions region)
	{
		final BasicAWSCredentials awsCreds = new BasicAWSCredentials(
				Config.getParameter(PAC_AMAZON_S3_ACCESS_KEY),
				Config.getParameter(PAC_AMAZON_S3_SECRET_KEY)
		);
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                            .withRegion(region).build();
	}

	@Nullable
	protected Regions getAwsRegion()
	{
		final String regionName = this.configurationService.getConfiguration().getString(
				PAC_AMAZON_S3_REGION,
				PAC_AMAZON_S3_REGION_DEFAULT
		);

		try
		{
			return Regions.fromName(regionName);
		} catch (Exception e)
		{
			LOGGER.error(
					"[PAC HIVE] Failed to find AWS region by name specified in property: " + PAC_AMAZON_S3_REGION,
					e
			);
			return null;
		}
	}

	private static AmazonS3URI getAmazonS3URI(final org.json.JSONObject responseObject, URI fileToBeDownloaded)
	{
		try
		{
			if (responseObject.get("dataUrl") != null)
			{
				fileToBeDownloaded = new URI(responseObject.get("dataUrl").toString());
			}
		} catch (final JSONException e)
		{
			LOGGER.error("[PAC HIVE] Failed to parse AERA response body.", e);
		} catch (final URISyntaxException e)
		{
			LOGGER.info("[PAC HIVE] Failed to construct AWS S3 URI.", e);
		}
		return new AmazonS3URI(fileToBeDownloaded);
	}

	/**
	 * @param jnjDeliveryDateUpdateService the jnjDeliveryDateUpdateService to set
	 */
	public void setJnjDeliveryDateUpdateService(final JnjDeliveryDateUpdateService jnjDeliveryDateUpdateService)
	{
		this.jnjDeliveryDateUpdateService = jnjDeliveryDateUpdateService;
	}


	private List<File> downloadFilesFromBucket(final AmazonS3URI s3URI, final AmazonS3 s3Client)
	{
		final StopWatch stopWatch = StopWatch.createStarted();

		List<File> fileList = new ArrayList<>();
		final List<String> keys = jnJGTPACJSONParserUtil.getKeyFilePaths(s3URI, s3Client);

		if (CollectionUtils.isNotEmpty(keys))
		{
			final int fileListSize = keys.size();
			for (int fileListCounter = 0; fileListCounter < fileListSize; fileListCounter++)
			{
				final String filePath = this.configurationService.getConfiguration().getString(
						PAC_PROCESSING_REALTIME_FILE_PATH,
						PAC_PROCESSING_REALTIME_FILE_PATH_DEFAULT
				);

				final String fileNamePrefix = this.configurationService.getConfiguration().getString(
						PAC_PROCESSING_REALTIME_FILE_NAME_PREFIX,
						PAC_PROCESSING_REALTIME_FILE_NAME_PREFIX_DEFAULT
				);

				final File file = new File(filePath + fileNamePrefix + fileListCounter + ".txt");

				LOGGER.debug("[PAC HIVE] Starting downloading from '{}' to '{}'.", s3URI, file);
				fileList.addAll(jnJGTPACJSONParserUtil.downloadToTempFolder(
						file,
						fileListCounter,
						s3URI,
						s3Client,
						keys
				));
				LOGGER.debug("[PAC HIVE] Finished downloading from '{}' to '{}'.", s3URI, file);
			}
		}

		stopWatch.stop();
		LOGGER.info(
				"[PAC HIVE] Finished downloading '{}' PAC HIVE AERA JSON file(s) from AWS S3 in '{}'.",
				fileList.size(),
				stopWatch.toString()
		);

		return fileList;
	}


	/**
	 * @param jnJGTPACJSONParserUtil the jnJGTPACJSONParserUtil to set
	 */
	public void setJnJGTPACJSONParserUtil(final JnJGTPACJSONParserUtil jnJGTPACJSONParserUtil)
	{
		this.jnJGTPACJSONParserUtil = jnJGTPACJSONParserUtil;
	}

	@Override
	public boolean isAbortable()
	{
		return true;
	}

	private boolean processLocalFiles(final List<File> downloadedlocalFileList)
	{
		boolean isUpdated = false;
		int numberOfProcessedFiles = 0;

		if (CollectionUtils.isEmpty(downloadedlocalFileList))
		{
			LOGGER.info("[PAC HIVE] No new files are available to process.");
		}
		else
		{
			final StopWatch stopWatch = StopWatch.createStarted();

			for (final File localFile : downloadedlocalFileList)
			{
				numberOfProcessedFiles++;

				try
				{
					this.jnJGTPACJSONParserUtil.processFile(localFile);
				} catch (Exception e)
				{
					LOGGER.error("[PAC HIVE] Some error happened during processing AERA JSON file: " + localFile, e);
				}

				stopWatch.split();

				LOGGER.info(
						"[PAC HIVE] Finished processing '{}' out of total '{}' AERA JSON files in '{}'." +
						" Current file: '{}'.",
						numberOfProcessedFiles,
						downloadedlocalFileList.size(),
						stopWatch.toSplitString(),
						localFile
				);
			}

			stopWatch.stop();

			LOGGER.info(
					"[PAC HIVE] Finished processing all '{}' AERA JSON file(s) in '{}'.",
					downloadedlocalFileList.size(),
					stopWatch.toString()
			);
		}
		isUpdated = true;
		return isUpdated;
	}

	/**
	 * @return the jnjDeliveryDateUpdateService
	 */
	public JnjDeliveryDateUpdateService getJnjDeliveryDateUpdateService()
	{
		return jnjDeliveryDateUpdateService;
	}

	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public JnjGTPacHiveConfigurationService getJnjGTPacHiveConfigurationService()
	{
		return jnjGTPacHiveConfigurationService;
	}

	public void setJnjGTPacHiveConfigurationService(JnjGTPacHiveConfigurationService jnjGTPacHiveConfigurationService)
	{
		this.jnjGTPacHiveConfigurationService = jnjGTPacHiveConfigurationService;
	}
}


