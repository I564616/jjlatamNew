/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2019 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.gt.pcm.integration.util.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jnj.gt.pcm.integration.constants.JnjpcmfacadeConstants;
import com.jnj.gt.pcm.integration.util.JnjPCMCommonFacadeUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;


/**
 * This class handles the util functions and common methods to connect to P360 API
 *
 */
public class DefaultJnjPCMCommonFacadeUtil implements JnjPCMCommonFacadeUtil
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjPCMCommonFacadeUtil.class);

	private ConfigurationService configurationService;
	private CatalogVersionService catalogVersionService;

	@Override
	public String getStartDate()
	{
		String startDate;
		if (configurationService.getConfiguration()
				.getBoolean(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_BACKDATE_FLAG))
		{
			startDate = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_BACKDATE);
		}
		else
		{
			final String dateFrequency = configurationService.getConfiguration()
					.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_FREQUENCY);
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, (-(Integer.parseInt(dateFrequency))));
			final Date dateAndTime = calendar.getTime();
			final DateFormat dateFormat = new SimpleDateFormat(JnjpcmfacadeConstants.PCMIntegration.DATE_FORMAT);
			startDate = dateFormat.format(dateAndTime);
		}

		return startDate;
	}

	@Override
	public String getAccessToken()
	{

		String accessToken = "";
		final String username = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_USERNAME);
		final String password = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_PASSWORD);
		final String clientId = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_CLIENT_ID);
		final String clientSecret = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_CLIENT_SECRET);
		final String authurl = configurationService.getConfiguration()
				.getString(JnjpcmfacadeConstants.PCMIntegration.PCM_INTEGRATION_JOB_ACCESS_TOKEN_URL);

		try
		{

			final TrustStrategy trustStrategy = new TrustStrategy()
			{
				public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException
				{
					return true;
				}
			};
			final SSLConnectionSocketFactory sslsf = createSSLCSocketFactoryObject(trustStrategy);
			final CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			Unirest.setHttpClient(client);

			final HttpResponse<String> tokenResponse = Unirest
					.post(authurl).header("content-type", "application/x-www-form-urlencoded").body("grant_type=password&username="
							+ username + "&password=" + password + "&client_id=" + clientId + "&client_secret=" + clientSecret)
					.asString();

			final JsonElement je = new JsonParser().parse(tokenResponse.getBody());

			accessToken = je.getAsJsonObject().get(JnjpcmfacadeConstants.PCMIntegration.ACCESS_TOKEN).toString().replace("\"",
					JnjpcmfacadeConstants.PCMIntegration.EMPTY_STRING);

		}
		catch (final Exception e)
		{
			LOGGER.error("error while getting access token " + e);
		}
		return accessToken;
	}

	protected SSLConnectionSocketFactory createSSLCSocketFactoryObject(final TrustStrategy trustStrategy)
	{
		SSLConnectionSocketFactory sslsf = null;
		try
		{
			final SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(trustStrategy);
			sslsf = new SSLConnectionSocketFactory(builder.build());
		}
		catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e)
		{
			LOGGER.error(e);
		}
		return sslsf;
	}


	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}


}
