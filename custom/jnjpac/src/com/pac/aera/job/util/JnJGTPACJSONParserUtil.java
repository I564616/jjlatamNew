/*
 * [y] hybris Platform
 *
 * Copyright (c) 2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.pac.aera.job.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.gt.pac.aera.JnJPacAeraResponse;
import com.gt.pac.aera.constants.Jnjgtb2bpacConstants;
import com.pac.aera.job.service.JnjDeliveryDateUpdateService;
import de.hybris.platform.util.Config;
import org.apache.commons.lang3.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class JnJGTPACJSONParserUtil
{
	private static final Logger LOGGER = LoggerFactory.getLogger(JnJGTPACJSONParserUtil.class);
	private static final String PAC_AERA_UPDATE_API_KEY = "pac.aera.update.api.key";

	private JnjDeliveryDateUpdateService jnjDeliveryDateUpdateService;


	/**
	 * @return the jnjDeliveryDateUpdateService
	 */
	public JnjDeliveryDateUpdateService getJnjDeliveryDateUpdateService()
	{
		return jnjDeliveryDateUpdateService;
	}


	/**
	 * @param jnjDeliveryDateUpdateService
	 *           the jnjDeliveryDateUpdateService to set
	 */
	public void setJnjDeliveryDateUpdateService(final JnjDeliveryDateUpdateService jnjDeliveryDateUpdateService)
	{
		this.jnjDeliveryDateUpdateService = jnjDeliveryDateUpdateService;
	}

	public void parseJSONResponse(final InputStream targetStream) throws IOException, ParseException
	{
		final JSONArray jsonArray = (JSONArray) new JSONParser().parse(new InputStreamReader(targetStream));
		Validate.notNull(jsonArray, "JSONArray can not be null.");

		int counter = 0;

		for (Object object : jsonArray)
		{
			try
			{
				counter++;

				this.processAeraResponse((JSONObject) object);

				LOGGER.debug(
						"[PAC HIVE] Processed '{}' out of total '{}' PAC HIVE AERA entries in JSON file.",
						counter, jsonArray.size()
				);
			} catch (Exception e)
			{
				LOGGER.error(
						"[PAC HIVE] Failed to process PAC HIVE AERA entry number '{}' out of total '{}' in JSON file.",
						counter, jsonArray.size(), e
				);
			}
		}
	}

	public void processAeraResponse(final JSONObject valueObject)
	{
		Validate.notNull(valueObject, "Parameter 'valueObject' can not be null.");

		final JnJPacAeraResponse jnJPacAeraResponse = this.convertJsonObjectToJnJPacAeraResponse(valueObject);

		try
		{
			this.getJnjDeliveryDateUpdateService().updateEstimatedDeliveryDate(jnJPacAeraResponse);
		} catch (Exception e)
		{
			LOGGER.error("[PAC HIVE] Failed to process PAC HIVE AERA response.", e);
		}

		try
		{
			this.getJnjDeliveryDateUpdateService().updateAdditionalFields(jnJPacAeraResponse);
		} catch (Exception e)
		{
			LOGGER.error("[PAC HIVE] Failed to process PAC HIVE AERA response.", e);
		}
	}

	protected JnJPacAeraResponse convertJsonObjectToJnJPacAeraResponse(final JSONObject valueObject)
	{
		final JnJPacAeraResponse jnJPacAeraResponse = new JnJPacAeraResponse();

		final String sapOrderNumber = this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.ORDERNUMBER);
		final String orderType = this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.ORDERTYPE);
		final String company = this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.COMPANY);
		jnJPacAeraResponse.setFormattedSapOrderNumber(sapOrderNumber + "-" + orderType + company);

		jnJPacAeraResponse.setSchedLineNumber(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.SCHEDLINENUMBER)
		);

		jnJPacAeraResponse.setLineNumber(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.LINENUMBER)
		);

		jnJPacAeraResponse.setCatalogCode(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.CATALOGCODE)
		);

		jnJPacAeraResponse.setCompany(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.COMPANY)
		);

		jnJPacAeraResponse.setDataSource(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.DATA_SOURCE)
		);

		jnJPacAeraResponse.setOrderType(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.ORDERTYPE)
		);

		jnJPacAeraResponse.setRecommendedDeliveryDate(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.RECOMM_DELIVERY_DATE)
		);

		jnJPacAeraResponse.setOrderNumber(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.ORDERNUMBER)
		);

		jnJPacAeraResponse.setConfirmedQuantity(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.OPEN_CONFIRMED_QUANTITY)
		);
		
		jnJPacAeraResponse.setSubFranchise(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.SUB_FRANCHISE)
		);

		jnJPacAeraResponse.setRequestedUnitsTotalQuantity(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.TOTAL_QUANTITY_OF_REQUESTED_UNITS)
		);

		jnJPacAeraResponse.setAmountPendingDelivery(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.AMOUNT_PENDING_DELIVERY)
		);

		jnJPacAeraResponse.setQuantityPendingStock(
				this.getValueOrEmptyString(valueObject, Jnjgtb2bpacConstants.QUANTITY_PENDING_STOCK)
		);

		return jnJPacAeraResponse;
	}

	protected String getValueOrEmptyString(final JSONObject valueObject, final String key)
	{
		return valueObject.get(key) != null ? valueObject.get(key).toString() : "";
	}

	public void processFile(final File localFile) throws IOException, ParseException
	{
		try (InputStream targetStream = new FileInputStream(localFile))
		{
			this.parseJSONResponse(targetStream);

			LOGGER.info("[PAC HIVE] Finished processing PAC HIVE AERA JSON file: '{}'.", localFile);
		} finally
		{
			if (Files.deleteIfExists(localFile.toPath()))
			{
				LOGGER.info("[PAC HIVE] File was deleted: " + localFile);
			}
			else
			{
				LOGGER.error("[PAC HIVE] File could not be deleted because it did not exist: " + localFile);
			}
		}
	}

	public List<String> getKeyFilePaths(final AmazonS3URI s3URI, final AmazonS3 s3Client)
	{
		final List<String> keys = new ArrayList<>();
		final ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(s3URI.getBucket())
				.withPrefix(s3URI.getKey() + "/");
		ObjectListing objects = s3Client.listObjects(listObjectsRequest);
		for (;;)
		{
			final List<S3ObjectSummary> summaries = objects.getObjectSummaries();
			if (summaries.isEmpty())
			{
				break;
			}
			summaries.forEach(s -> keys.add(s.getKey()));
			objects = s3Client.listNextBatchOfObjects(objects);
		}
		return keys;
	}

	public List<File> downloadToTempFolder(final File file, final int fileListCounter, final AmazonS3URI s3URI,
			final AmazonS3 s3Client, final List<String> keys)
	{
		final List<File> fileList = new ArrayList<>();
		try
		{
			// Create all parent directories because otherwise if they do not exist file creation will fail
			final boolean isMkdirsSuccesful = file.getParentFile().mkdirs();
			if (isMkdirsSuccesful)
			{
				LOGGER.debug("[PAC HIVE] Parent directories have been created for path: '{}'.", file);
			} else
			{
				LOGGER.debug("[PAC HIVE] Parent directories have NOT been created either because of an error or" +
				             " because they already existed for path: '{}'.", file
				);
			}

			if (file.createNewFile())
			{
				LOGGER.info("[PAC HIVE] File is created!");
			}
			else
			{
				LOGGER.info("[PAC HIVE] File already exists.");
			}
			final ObjectMetadata object = s3Client.getObject(new GetObjectRequest(s3URI.getBucket(), keys.get(fileListCounter)),
					file);
			LOGGER.info("[PAC HIVE] A file has been received from AWS S3: '{}'.", object);
			fileList.add(file);
			s3Client.deleteObject(new DeleteObjectRequest(s3URI.getBucket(), keys.get(fileListCounter)));
		}
		catch (final IOException e)
		{
			LOGGER.error("[PAC HIVE] Can not download JSON file from AWS S3 due to the error.", e);
		}
		catch (final AmazonServiceException ase)
		{
			LOGGER.error("[PAC HIVE] Caught an AmazonServiceException, which means your request made it "
					+ "to Amazon S3, but was rejected with an error response for some reason." + ase);

		}
		catch (final AmazonClientException ace)
		{
			LOGGER.error("[PAC HIVE] Caught an AmazonClientException, which means the client encountered "
					+ "an internal error while trying to communicate with S3.", ace);

		}
		return fileList;
	}

	public HttpHeaders addOauthHeaderParameters()
	{
		final HttpHeaders headers = new HttpHeaders();
		headers.set("x-api-key", Config.getParameter(PAC_AERA_UPDATE_API_KEY));
		return headers;
	}


	public ClientHttpRequestFactory clientHttpRequestFactory()
	{
		final int connectionTimeOut = 0;
		final int readTimeOut = 0;
		final SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(connectionTimeOut);
		factory.setConnectTimeout(readTimeOut);
		return factory;
	}
}
