/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.pac.aera.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gt.pac.aera.PacHiveException;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;
import com.pac.aera.job.service.DefaultJnjDeliveryDateUpdateService;

import com.jnj.la.pac.aera.dao.JnjLaPacAeraDao;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;

import com.gt.pac.aera.JnJPacAeraResponse;

import jakarta.annotation.Nonnull;

/**
 * This class is used to override GT logic for Delivery date update in hybris as per PAC date format.
 *  
 */
public class DefaultJnjLaDeliveryDateUpdateService extends DefaultJnjDeliveryDateUpdateService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultJnjLaDeliveryDateUpdateService.class);

	private static final String PAC_ERROR_MESSAGE = "Error in Parsing date";
	private static final String DATE_FORMAT_PAC = "jnj.la.date.format.pac";
	private static final String DATE_FORMAT_HYBRIS = "jnj.la.inbound.order.date.format";
	
	
	protected JnjLaPacAeraDao jnjlaPacAeraDao;
	
   /**
    * This method is used to update Estimated DeliveryDate and save in DB.
    * 
    * @param  jnJPacAeraResponse
    * @throws PacHiveException
    */
	@Override
	public void updateEstimatedDeliveryDate(@Nonnull final JnJPacAeraResponse jnJPacAeraResponse)
	throws PacHiveException
	{
		Validate.notNull(jnJPacAeraResponse, "Parameter 'jnJPacAeraResponse' can not be null.");

		AbstractOrderEntryModel orderEntryModel=null;
		try
		{
			orderEntryModel = this.jnjlaPacAeraDao.findCorrespondingOrderEntryModel(jnJPacAeraResponse);
		
		} catch (Exception e)
		{
			LOG.info("[PAC HIVE] {} " ,e.getMessage());
			LOG.debug(String.format(
					"[PAC HIVE] NO order with sapOrderNumber '%s' no order entry was found with catalogCode: '%s' and lineNumber: '%s'.PAC HIVE data supposed to be saved: schedLineNumber: '%s' recommendedDeliveryDate: '%s'.",
					jnJPacAeraResponse.getOrderNumber(),
					jnJPacAeraResponse.getCatalogCode(),
					jnJPacAeraResponse.getLineNumber(),
					jnJPacAeraResponse.getSchedLineNumber(),
					jnJPacAeraResponse.getRecommendedDeliveryDate()
			), e);
		}

		//we need to create PacHiveEntry irrespective of Order Entry availability
		final JnjPacHiveEntryModel pacHiveEntryModel = this.findOrCreateJnjPacHiveEntryModel(
				jnJPacAeraResponse, orderEntryModel);

		this.populateJnjPacHiveEntryModel(jnJPacAeraResponse, pacHiveEntryModel);

		// We do need to save both because cascade save might not work if we only modify entries
		if(null != orderEntryModel) {
			this.modelService.save(orderEntryModel);
		}
		
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
	
	@Override
	protected JnjPacHiveEntryModel findOrCreateJnjPacHiveEntryModel(
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse,
			 final AbstractOrderEntryModel orderEntryModel)
	{
		Validate.notNull(jnJPacAeraResponse, "jnJPacAeraResponse can not be null.");

		JnjPacHiveEntryModel pacHiveEntryModel = null;
		
		if(null!=orderEntryModel)
		{
			try
			{
				pacHiveEntryModel = this.jnjlaPacAeraDao.findJnjPacHiveEntryModel(jnJPacAeraResponse, orderEntryModel);
			} catch (PacHiveException e)
			{
				LOG.debug("[PAC HIVE] No JnjPacHiveEntryModel was found.", e);
			}
		}

		if (null == pacHiveEntryModel)
		{
			return this.createJnjPacHiveEntryModel(jnJPacAeraResponse, orderEntryModel);
		}

		return pacHiveEntryModel;
	}

    /**
     * this method is used to create JnjPacHiveEntryModel in database.
     * 
     * @param jnJPacAeraResponse
     * @param orderEntryModel
     * @return JnjPacHiveEntryModel
     */
	@Override
	protected JnjPacHiveEntryModel createJnjPacHiveEntryModel(
			@Nonnull final JnJPacAeraResponse jnJPacAeraResponse,
			final AbstractOrderEntryModel orderEntryModel)
	{
		Validate.notNull(jnJPacAeraResponse, "jnJPacAeraResponse can not be null.");

		JnjPacHiveEntryModel pacHiveEntryModel = this.modelService.create(JnjPacHiveEntryModel.class);

		if(null !=orderEntryModel) {
			final Collection<JnjPacHiveEntryModel> jnjPacHiveEntries =  CollectionUtils.isNotEmpty(orderEntryModel.getJnjPacHiveEntries())
					?  new ArrayList<>(orderEntryModel.getJnjPacHiveEntries()) : new ArrayList<>();
					
			jnjPacHiveEntries.add(pacHiveEntryModel);
			orderEntryModel.setJnjPacHiveEntries(jnjPacHiveEntries);
			pacHiveEntryModel.setOrderEntry(orderEntryModel);
		}

		LOG.debug(
				"[PAC HIVE] A new JnjPacHiveEntryModel has been created (not saved yet) because an existing one was" +
				" not found for the order with sapOrderNumber '{}' and order entry with catalogCode: '{}'" +
				" and sapOrderlineNumber '{}'. PAC HIVE data: schedLineNumber: '{}' recommendedDeliveryDate: '{}'.",
				jnJPacAeraResponse.getOrderNumber(),
				jnJPacAeraResponse.getCatalogCode(),
				jnJPacAeraResponse.getLineNumber(),
				jnJPacAeraResponse.getSchedLineNumber(),
				jnJPacAeraResponse.getRecommendedDeliveryDate()
		);

		return pacHiveEntryModel;
	}

	
	/**
	 * Converts a string representing AERA date to Java date.
	 *
	 * @param pacDeliveryDate String .
	 * @return Java Date .
	 */
	@Override
	protected Date convertAeraDateToJavaDate(final String pacDeliveryDate) throws ParseException
	{
		LOG.info("[PAC HIVE] PAC Date received is: '{}'.", pacDeliveryDate);
		
		final SimpleDateFormat receivedPACDateFormat = new SimpleDateFormat(getConfigurationService().getConfiguration().getString(DATE_FORMAT_PAC));
		final SimpleDateFormat scheduledLineDateFormat = new SimpleDateFormat(getConfigurationService().getConfiguration().getString(DATE_FORMAT_HYBRIS));
		Date startDate = null;
		try
		{
			final Date receivedPACDate =receivedPACDateFormat.parse(pacDeliveryDate);
			final String formattedDateString = scheduledLineDateFormat.format(receivedPACDate);
			startDate=scheduledLineDateFormat.parse(formattedDateString);
		}
		catch (final ParseException e)
		{
			LOG.error(PAC_ERROR_MESSAGE, e);
		}	
		
		
		LOG.info("[PAC HIVE] Date to be updated is: '{}'.", startDate);
		return startDate;
	}
	
	
	
	/**
	 * This method is used to find Orphan JnjPacHiveEntryModels.
	 * 
	 * @param orderEntryModel
	 * @return List of JnjPacHiveEntryModel
	 */
	protected List<JnjPacHiveEntryModel> findOrphanJnjPacHiveEntryModels(
			 final AbstractOrderEntryModel orderEntryModel)
	{
		Validate.notNull(orderEntryModel, "orderEntryModel can not be null.");

		List<JnjPacHiveEntryModel> pacHiveEntries = null;
		try
		{
			pacHiveEntries = this.jnjlaPacAeraDao.getOrphanPacHiveEntriesByOrderEntryData(orderEntryModel);
		} 
		catch (Exception e)
		{
			LOG.debug("[PAC HIVE] No JnjPacHiveEntryModel was found. '{}'", e.getMessage());
		}
		
		return pacHiveEntries;
	}
	
	public JnjLaPacAeraDao getJnjlaPacAeraDao() {
		return jnjlaPacAeraDao;
	}

	public void setJnjlaPacAeraDao(final JnjLaPacAeraDao jnjlaPacAeraDao) {
		this.jnjlaPacAeraDao = jnjlaPacAeraDao;
	}

}