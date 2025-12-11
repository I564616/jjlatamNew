/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.Filter;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportReply;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportRequest;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsReply;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.ReportsRequest;
import com.jnj.hcswmd01.mu007_epic_reports_cmod_v1.wsd.getreports.Topics;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.gt.outbound.mapper.JnjGTCmodRgaMapper;
import com.jnj.gt.outbound.services.JnjGTCmodRgaService;




/**
 * The JnjNACmodRgaMapperImpl class contains the definition of all the method of the JnjNACmodRgaMapper interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCmodRgaMapper implements JnjGTCmodRgaMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCmodRgaMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private JnjGTCmodRgaService jnjGTCmodRgaService;
	
	public JnjGTCmodRgaService getJnjGTCmodRgaService() {
		return jnjGTCmodRgaService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws BusinessException
	 */
	@Override
	public Map<String, byte[]> mapCmodRgaRequestResponse(final String orderNumber, final boolean isCmodCall)
			throws IntegrationException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "mapCmodRgaRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Map<String, byte[]> byteArrayInMap = null;

		final int oneDay = Integer.parseInt(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.DATE_TO_VALUE));
		final int ninetyDays = Integer.parseInt(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.DATE_FROM_VALUE));
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.DATE_FROMAT));
		final ReportsRequest request = new ReportsRequest();
		final ReportRequest reportRequest = new ReportRequest();
		final Filter filter = new Filter();
		final Topics topics = new Topics();
		// If the call is for Cmod
		if (isCmodCall)
		{
			filter.setReports(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.REPORTS_CMOD));
			topics.setTName(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.TNAME_CMOD));
		}
		else
		{
			filter.setReports(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.REPORTS_RGA));
			topics.setTName(Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.TNAME_RGA));
		}

		if (StringUtils.isNotEmpty(orderNumber))
		{
			topics.setValue(orderNumber.replaceFirst(
					Config.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.KEY_TO_REPLACE_LEADING_ZEROS),
					Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}
		filter.getTopics().add(topics);
		final Calendar calendarWithPlusOneDay = Calendar.getInstance();
		calendarWithPlusOneDay.set(Calendar.DAY_OF_MONTH, (calendarWithPlusOneDay.get(Calendar.DAY_OF_MONTH) + oneDay));
		final Calendar calendarWithMinusNinetyDays = Calendar.getInstance();
		calendarWithMinusNinetyDays.set(Calendar.DAY_OF_MONTH,
				(calendarWithMinusNinetyDays.get(Calendar.DAY_OF_MONTH) - ninetyDays));
		filter.setDateTo(simpleDateFormat.format(calendarWithPlusOneDay.getTime()));
		filter.setDateFrom(objectFactory.createFilterDateFrom(simpleDateFormat.format(calendarWithMinusNinetyDays.getTime())));
		filter.setFormats(objectFactory.createFilterFormats(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		reportRequest.setFilter(filter);
		reportRequest.setOrderBy(objectFactory.createReportRequestOrderBy(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.ORDER_BY)));
		reportRequest.setPageIndex(objectFactory.createReportRequestOrderBy(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.PAGE_INDEX)));
		reportRequest.setPageSize(objectFactory.createReportRequestOrderBy(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.CmodRga.PAGE_SIZE)));
		request.setReportRequest(reportRequest);

		// Call the service class to make interaction with the CMOD Interface.

		final ReportsReply reply = jnjGTCmodRgaService.getReports(request);
		if (null != reply && null != reply.getReportReply() && null != reply.getReportReply().getValue())
		{
			final ReportReply reportReply = reply.getReportReply().getValue();
			if (null != reportReply.getPDFAttachment() && null != reportReply.getPDFAttachment().getValue()
					&& null != reportReply.getPDFReferenceName()
					&& StringUtils.isNotEmpty(reportReply.getPDFReferenceName().getValue()))
			{
				byteArrayInMap = new HashMap<String, byte[]>();
				byteArrayInMap.put(reportReply.getPDFReferenceName().getValue(), reportReply.getPDFAttachment().getValue());
			}
			else if (null != reportReply.getErrorMessage() && null != reportReply.getErrorMessage().getValue())
			{
				throw new BusinessException();
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CMOD_RGA_CALL + Logging.HYPHEN + "mapCmodRgaRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return byteArrayInMap;
	}

}
