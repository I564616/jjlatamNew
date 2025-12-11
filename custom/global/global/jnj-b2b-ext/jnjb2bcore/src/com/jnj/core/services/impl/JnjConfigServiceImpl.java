/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.JnjConfigDao;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.services.MessageService;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;


/**
 * The JnjConfigServiceImpl class invokes the JnjConfigDao by passes the requested id to method of DAO.
 * 
 * @author Cognizant
 * @version 1.0
 * 
 */
public class JnjConfigServiceImpl implements JnjConfigService
{
	protected static final Logger LOGGER = Logger.getLogger(JnjConfigServiceImpl.class);
	protected JnjConfigDao jnjConfigDao;
	@Autowired
	protected ModelService modelService;
	/** The message service. */
	@Autowired
	protected MessageService messageService;

	/** I18NService to retrieve the current locale. */
	@Autowired
	protected I18NService i18nService;


	public ModelService getModelService() {
		return modelService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public I18NService getI18nService() {
		return i18nService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getConfigValueById(final String id)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String configValue = null;
		if (StringUtils.isNotEmpty(id))
		{
			final JnjConfigModel configModel = jnjConfigDao.getConfigValueById(id);
			if (null != configModel)
			{
				configValue = configModel.getValue();
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValueById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return configValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getConfigValuesById(final String id, final String seperator)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		List<String> values = null;
		final String configValue = getConfigValueById(id);
		if (StringUtils.isNotEmpty(seperator) && StringUtils.isNotEmpty(configValue))
		{
			values = new ArrayList<String>();
			values = Arrays.asList(configValue.split(seperator));
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesById()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveConfigValues(final String key, final String value)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveConfigValues()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.isNotEmpty(key))
		{
			final JnjConfigModel configModel = jnjConfigDao.getConfigValueById(key);
			if (null != configModel)
			{
				configModel.setValue(value);
				modelService.save(configModel);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveConfigValues()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method fetches the values from the CONFIG table where the id begins with the particular pattern passed.
	 * 
	 * @param idLike
	 * @return values
	 */
	@Override
	public List<String> getConfigValuesWhereIdLike(final String idLike)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<String> configValues = jnjConfigDao.getConfigValuesWhereIdLike(idLike);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN + "Config values retrieved :: "
					+ configValues);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getConfigValuesWhereIdLike()" + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.END_OF_METHOD + Jnjb2bCoreConstants.Logging.HYPHEN
					+ Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return configValues;
	}

	/**
	 * @return the jnjConfigDao
	 */
	public JnjConfigDao getJnjConfigDao()
	{
		return jnjConfigDao;
	}


	/**
	 * @param jnjConfigDao
	 *           the jnjConfigDao to set
	 */
	public void setJnjConfigDao(final JnjConfigDao jnjConfigDao)
	{
		this.jnjConfigDao = jnjConfigDao;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjConfigModel> getConfigModelsByIdAndKey(final String id, final String key)
	{
		return jnjConfigDao.getConfigModelsByIdAndKey(id, key);
	}

	/**
	 * This will return Map to populate dropdown
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public Map<String, String> getDropdownValuesInMap(final String id)
	{
		final Map<String, String> outputMap = new LinkedHashMap<String, String>();
		final List<JnjConfigModel> configModels = getConfigModelsByIdAndKey(id, null);
		for (final JnjConfigModel jnjConfigModel : configModels)
		{
			try
			{
				outputMap.put(jnjConfigModel.getKey(),
						messageService.getMessageForCode(jnjConfigModel.getKey(), i18nService.getCurrentLocale()));
			}
			catch (final BusinessException exception)
			{

				LOGGER.error("Unable to render message text for message code : " + jnjConfigModel.getKey(), exception);
			}
		}
		return outputMap;
	}

}
