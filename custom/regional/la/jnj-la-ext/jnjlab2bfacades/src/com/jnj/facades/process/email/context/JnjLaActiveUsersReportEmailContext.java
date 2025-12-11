/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.process.email.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnjLaActiveUserReportEmailProcessModel;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;

/**
 * This class is used to generate active users report email.
 * 
 * @author RChauh21
 *
 */
public class JnjLaActiveUsersReportEmailContext extends
		AbstractEmailContext<JnjLaActiveUserReportEmailProcessModel> {

	private static final Logger LOGGER = Logger
			.getLogger(JnjLaActiveUsersReportEmailContext.class);
	private static final String FROM_EMAIL_ADDRESS = "fromEmail";
	public static final String TO_DISPLAY_NAME = "toDisplayName";
	public static final String EMAIL_ATTACHMENT_PATH = "attachmentPath";
	public static final String EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	public static final String USE_DIRECT_PATH = "useDirectPath";
	public static final String USE_REALFILE_NAME = "orderNumber";
	public static final String DELETE_FILE = "deleteFile";
	private static final String TRUE = "true";
	private static final String ACTIVE_USER_REPORT = "activeUserReport";
	private static final String ATTACHMENT_FILE_NAME = "attachmentFileName";
	private static final String ATTACHMENT_FILE_PATH = "attachmentPath";
	private static final String FIRST_NAME = "firstName";
	private static final String FROM_EMAIL = "fromEmail";
	private static final String TO_EMAIL = "toEmail";
	private static final String BASE_SITE = "site";
	private static final String CURRENCY = "currency";
	private static final String LANGUAGE = "language";
	private static final String ERROR_FLAG = "ErrorFlag";
	private Map<String, String> activeUserReportData = new HashMap<>();

	/**
	 * This method is used to generate active user report
	 * 
	 * @param businessProcessModel
	 * @param emailPageModel
	 */
	@Override
	public void init(final JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel,
			final EmailPageModel emailPageModel) {
		final String METHOD_NAME = "init()";
		super.init(jnjLaActiveUserReportEmailProcessModel, emailPageModel);
			try {
				logMethodStartOrEnd(
						Jnjlab2bcoreConstants.Logging.ACTIVE_USER_REPORT_EMAIL,
						METHOD_NAME,
						Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD);
				
				setActiveUserReportData(jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData());
				put(EMAIL, jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData().get(TO_EMAIL));
				put(DISPLAY_NAME, jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData().get(FIRST_NAME));
				put(FROM_EMAIL, jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData().get(FROM_EMAIL_ADDRESS));
				put(FROM_DISPLAY_NAME, jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData().get(FIRST_NAME));
				put(EMAIL_ATTACHMENT_PATH,
						getActiveUserReportData().get(ATTACHMENT_FILE_PATH));
				put(EMAIL_ATTACHMENT_FILE_NAME,
						getActiveUserReportData().get(ATTACHMENT_FILE_NAME));
				put(DELETE_FILE, Boolean.TRUE);
				put(USE_DIRECT_PATH, TRUE);
				put(ERROR_FLAG, jnjLaActiveUserReportEmailProcessModel
						.getActiveUserReportData().get(ERROR_FLAG));

				put(BASE_SITE, jnjLaActiveUserReportEmailProcessModel.getSite());
				put(CURRENCY,
						jnjLaActiveUserReportEmailProcessModel.getCurrency());
				put(LANGUAGE,
						jnjLaActiveUserReportEmailProcessModel.getLanguage());

				if ("true".equalsIgnoreCase(getActiveUserReportData().get("activeUserEmail"))) {
					put(ACTIVE_USER_REPORT, TRUE);
				} else {
					put(ACTIVE_USER_REPORT, null);
				}

				logMethodStartOrEnd(
						Jnjlab2bcoreConstants.Logging.ACTIVE_USER_REPORT_EMAIL,
						METHOD_NAME,
						Jnjlab2bcoreConstants.Logging.END_OF_METHOD);
			} catch (final Exception ex) {
				LOGGER.error("Error while sending Active User email excel sheet."
						+ ex);

			}
		
	}

	private static void logMethodStartOrEnd(final String functionalityName,
			final String methodName, final String entryOrExit) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(functionalityName
					+ Jnjlab2bcoreConstants.Logging.HYPHEN + methodName
					+ Jnjlab2bcoreConstants.Logging.HYPHEN + entryOrExit
					+ Jnjlab2bcoreConstants.Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	@Override
	protected BaseSiteModel getSite(
			final JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel) {

		return null;
	}

	@Override
	protected CustomerModel getCustomer(
			final JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel) {

		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(
			final JnjLaActiveUserReportEmailProcessModel jnjLaActiveUserReportEmailProcessModel) {

		return jnjLaActiveUserReportEmailProcessModel.getLanguage();
	}

	public Map<String, String> getActiveUserReportData() {
		return activeUserReportData;
	}

	public void setActiveUserReportData(final Map<String, String> activeUserReportData) {
		this.activeUserReportData = activeUserReportData;
	}

}
