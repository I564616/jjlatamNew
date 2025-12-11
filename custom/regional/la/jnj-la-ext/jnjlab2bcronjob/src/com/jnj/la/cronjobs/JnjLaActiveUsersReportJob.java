/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2020
 * All rights reserved.
 */

package com.jnj.la.cronjobs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.nio.file.Files;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.commons.lang3.StringUtils;

import com.jnj.core.event.JnjLaActiveUsersReportEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.services.JnJLaCustomerDataService;
import com.jnj.la.core.util.JnjLaCommonUtil;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;

public class JnjLaActiveUsersReportJob extends
		AbstractJobPerformable<CronJobModel> {

	private static final Logger LOGGER = Logger
			.getLogger(JnjLaActiveUsersReportJob.class);

	private static final String TRUE = "true";
	private static final String FALSE = "false";
	private static final String XLS = ".xls";
	private static final String ZIP = ".zip";
	private static final String ATTACHMENT_FILE_NAME = "attachmentFileName";
	private static final String ATTACHMENT_FILE_PATH = "attachmentPath";
	private static final String FIRST_NAME = "firstName";
	private static final String FROM_EMAIL = "fromEmail";
	private static final String TO_EMAIL = "toEmail";
	private static final String ACTIVE_USER_EMAIL = "activeUserEmail";
	private static final String TIME_ZONE = "EST5EDT";
	private static final String FILE_NAME = "jnj.la.active.users.report.fileName";
	private static final String DATE_FORMAT = "ddMMMyyyy";
	private static final String DATE_FORMAT_DATA = "MM/dd/yyyy";
	private static final String ERROR_FLAG = "ErrorFlag";
	private static final String COURIER_NEW = "Courier New";

	private static final String SOLD_TO = "SoldTo";
	private static final String MAIN = "Main";
	private static final String NOTIFICATION = "Notification";
	private static final String SERIAL_NO = "Serial number";
	private static final String EMAIL_ID = "Email ID";
	private static final String CUSTOMER_NAME = "Customer Name";
	private static final String CUSTOMER_EMAIL = "Customer Email";
	private static final String SOLD_TO_NAME = "Sold To Name";
	private static final String SOLD_TO_NUMBER = "Sold To Number";
	private static final String SECTOR = "Sector";
	private static final String COUNTRY = "Country";
	private static final String LAST_LOGIN_DATE = "Last Login Date";
	private static final String CREATION_DATE = "Creation Date";
	private static final String CUSTOMER_LOGIN_DATE = "Customer Login Id";
	private static final String SALESORG_COUNTRY = "SalesOrg/Country";
	private static final String PREFERENCE = "Preference";
	private static final String DATE_CREATED = "Date Created";

	private static final String EMAIL_LIST_KEY = "jnj.la.active.users.report.to.emails";
	private static final String FROM_EMAIL_KEY = "jnj.la.active.users.report.fromEmail";
	private static final String FROM_EMAIL_NAME_KEY = "jnj.la.active.users.report.fromEmail.name";
	private static final String FILE_PATH = "jnj.la.active.users.report.file.path.temp";

	private JnJLaCustomerDataService jnjCustomerDataService;
	private static ConfigurationService configurationService;
	private EventService eventService;
	private CommonI18NService commonI18NService;
	private UserService userService;
	private BaseSiteService baseSiteService;

	@Override
	public PerformResult perform(final CronJobModel cronjob) {
		PerformResult result = null;
		final String emailIdsList = configurationService.getConfiguration()
				.getString(EMAIL_LIST_KEY);
		try {

			if (StringUtils.isNotEmpty(emailIdsList)) {
				sendEmailNotification(emailIdsList);

			} else {
				LOGGER.info("Email list not configured to send active b2b unit user report");
			}
			result = new PerformResult(CronJobResult.SUCCESS,
					CronJobStatus.FINISHED);
		} catch (final Exception ex) {
			LOGGER.error("Exception while sending Notification mail, error message: "
					+ ex);
			sendErrorEmail(emailIdsList);
			result = new PerformResult(CronJobResult.ERROR,
					CronJobStatus.ABORTED);
		}
		return result;
	}

	public void sendErrorEmail(final String emailIdsList) {
		String toEmailName = null;
		final Map<String, String> activeUserReportData = new HashMap<>();
		final String fromEmail = configurationService.getConfiguration()
				.getString(FROM_EMAIL_KEY);
		toEmailName = configurationService.getConfiguration().getString(
				FROM_EMAIL_NAME_KEY);

		activeUserReportData.put(TO_EMAIL, emailIdsList);
		activeUserReportData.put(FROM_EMAIL, fromEmail);
		activeUserReportData.put(FIRST_NAME, toEmailName);
		activeUserReportData.put(ACTIVE_USER_EMAIL, TRUE);
		activeUserReportData.put(ERROR_FLAG, TRUE);
		final JnjLaActiveUsersReportEvent jnjLaActiveUsersReportEvent = new JnjLaActiveUsersReportEvent();
		jnjLaActiveUsersReportEvent
				.setActiveUserReportData(activeUserReportData);
		jnjLaActiveUsersReportEvent.setCustomer(null);
		jnjLaActiveUsersReportEvent.setLanguage(commonI18NService
				.getCurrentLanguage());
		jnjLaActiveUsersReportEvent.setCurrency(commonI18NService
				.getCurrentCurrency());
		jnjLaActiveUsersReportEvent.setSite(baseSiteService
				.getBaseSiteForUID(JnjLaCommonUtil
						.getValue(Jnjlab2bcoreConstants.CURRENT_BASE_SITE)));

		eventService.publishEvent(jnjLaActiveUsersReportEvent);

	}

	public void sendEmailNotification(final String emailIdsList)
			throws IOException {
		List<JnJB2bCustomerModel> b2bCustomerList = jnjCustomerDataService
				.getActiveUsersReport();
		List<JnJLaUserAccountPreferenceModel> userNotificationPref = jnjCustomerDataService
				.getActiveUserNotificationPreference();

		final HSSFWorkbook workbook = new HSSFWorkbook();
		generateActiveUserReport(b2bCustomerList, userNotificationPref,
				workbook);

		final String filepath = Config.getParameter(FILE_PATH);
		final DateFormat df = new SimpleDateFormat(DATE_FORMAT);
		df.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		final String date = df.format(new Date());
		final String fileName = configurationService.getConfiguration()
				.getString(FILE_NAME) + date + XLS;
		final String compressfileName = configurationService.getConfiguration()
				.getString(FILE_NAME) + date + ZIP;

		try {
			writeExcel(workbook, filepath + File.separator + fileName);
			workbook.close();
			compressFile(filepath + File.separator + fileName, filepath + File.separator+ compressfileName);			
			Files.delete(Path.of(filepath + File.separator + fileName));			
		} catch (final IOException e) {
			LOGGER.error("Error occurred while writing to excel", e);
			throw e;
		}
		String toEmailName = null;
		final Map<String, String> activeUserReportData = new HashMap<>();
		final String fromEmail = configurationService.getConfiguration()
				.getString(FROM_EMAIL_KEY);

		toEmailName = configurationService.getConfiguration().getString(
				FROM_EMAIL_NAME_KEY);

		activeUserReportData.put(TO_EMAIL, emailIdsList);
		activeUserReportData.put(FROM_EMAIL, fromEmail);
		activeUserReportData.put(FIRST_NAME, toEmailName);
		activeUserReportData.put(ATTACHMENT_FILE_NAME, compressfileName);
		activeUserReportData.put(ATTACHMENT_FILE_PATH, filepath);
		activeUserReportData.put(ACTIVE_USER_EMAIL, TRUE);
		activeUserReportData.put(ERROR_FLAG, FALSE);
		final JnjLaActiveUsersReportEvent jnjLaActiveUsersReportEvent = new JnjLaActiveUsersReportEvent();
		jnjLaActiveUsersReportEvent
				.setActiveUserReportData(activeUserReportData);
		jnjLaActiveUsersReportEvent.setCustomer(null);
		jnjLaActiveUsersReportEvent.setLanguage(commonI18NService
				.getCurrentLanguage());
		jnjLaActiveUsersReportEvent.setCurrency(commonI18NService
				.getCurrentCurrency());
		jnjLaActiveUsersReportEvent.setSite(baseSiteService
				.getBaseSiteForUID(JnjLaCommonUtil
						.getValue(Jnjlab2bcoreConstants.CURRENT_BASE_SITE)));

		eventService.publishEvent(jnjLaActiveUsersReportEvent);

	}

	private static void writeExcel(final HSSFWorkbook workbook,
			final String filePath) throws IOException {
		final File file = new File(filePath);

		try (FileOutputStream out = new FileOutputStream(file)) {
			workbook.write(out);
			out.flush();
		} catch (final IOException exception) {
			LOGGER.error(
					"There was an error while trying to preform input output operations on the file",
					exception);
			throw exception;
		}
	}

	private static void compressFile(final String filePath, final String compressFile)
 throws IOException {
		File file = new File(filePath);
		try(FileOutputStream fos = new FileOutputStream(compressFile)) {
			compressFileIntoZip(fos, file.getName(), filePath);
		} catch (IOException exe) {
			LOGGER.error(
					"There was an error while trying to compress the report",
					exe);
			throw exe;
		}
	}
	
	private static void compressFileIntoZip(final FileOutputStream fos, final String fileName, final String filePath) throws IOException{
		try (ZipOutputStream zos = new ZipOutputStream(fos)){
			zos.putNextEntry(new ZipEntry(fileName));
			byte[] bytes = Files.readAllBytes(Path.of(filePath));
			zos.write(bytes, 0, bytes.length);
			zos.closeEntry();
			} catch (IOException exe) {
				LOGGER.error(
						"There was an error while trying to compress the report",
						exe);
				throw exe;
			}
	}
	

	private static void generateActiveUserReport(
			List<JnJB2bCustomerModel> b2bCustomerList,
			List<JnJLaUserAccountPreferenceModel> userNotificationPref,
			HSSFWorkbook workbook) {

		final int DEFAULT_WIDTH = 16;
		final int HEADER_ROW1 = 0;

		HSSFSheet searchSheet1 = workbook.createSheet(MAIN);
		HSSFSheet searchSheet2 = workbook.createSheet(NOTIFICATION);
		HSSFSheet searchSheet3 = workbook.createSheet(SOLD_TO);
		searchSheet1.setDefaultColumnWidth(DEFAULT_WIDTH);
		searchSheet2.setDefaultColumnWidth(DEFAULT_WIDTH);
		searchSheet3.setDefaultColumnWidth(DEFAULT_WIDTH);

		final HSSFRow header1 = searchSheet1.createRow(HEADER_ROW1);
		final HSSFRow header2 = searchSheet2.createRow(HEADER_ROW1);
		final HSSFRow header3 = searchSheet3.createRow(HEADER_ROW1);
		final HSSFFont font = workbook.createFont();
		font.setBold(true);

		final HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		final HSSFCellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		headerStyle.setFont(font);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		final HSSFFont regularBlueHyperlinkFont = workbook.createFont();
		final HSSFCellStyle regularBlueHyperlinkStyle = workbook
				.createCellStyle();
		regularBlueHyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
		regularBlueHyperlinkFont.setUnderline(Font.U_SINGLE);
		regularBlueHyperlinkFont.setFontName(COURIER_NEW);
		regularBlueHyperlinkStyle.setFont(regularBlueHyperlinkFont);
		final HSSFCellStyle styleCell = workbook.createCellStyle();
		styleCell.setWrapText(true);

		createColumnsForMainSheet(header1, style);
		createColumnsForNotificationSheet(header2, style);
		createColumnsForSoldToSheet(header3, style);

		final int rowNumber = 1;
		if (null != b2bCustomerList) {
			populateColumnsForMainSheet(b2bCustomerList, searchSheet1,
					rowNumber);
		}
		if (null != userNotificationPref) {
			populateColumnsForNotificationSheet(userNotificationPref,
					searchSheet2, rowNumber);
		}
		if (null != b2bCustomerList) {
			populateColumnsForSoldToSheet(b2bCustomerList, searchSheet3,
					rowNumber);
		}

	}

	private static int populateCustomerData(JnJB2bCustomerModel b2bCustomer,
			int columnNumber, HSSFSheet sheet, HSSFRow row, boolean countryFlag) {

		if (null != b2bCustomer.getUid()) {
			createCell(b2bCustomer.getUid(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		if (null != b2bCustomer.getName()) {
			createCell(b2bCustomer.getName(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		if (countryFlag) {
			if (null != b2bCustomer.getCurrentCountry()) {
				populateCellData(b2bCustomer.getCurrentCountry().getIsocode(),
						columnNumber++, sheet, row);
			} else {
				createCell("", row, columnNumber++, sheet);
			}
		}

		return columnNumber;

	}

	private static int populateUnitSalesOrgData(JnJB2BUnitModel jnJB2BUnit,
			JnJSalesOrgCustomerModel salesOrg, int columnNumber,
			HSSFSheet sheet, HSSFRow row) {
		if (null != jnJB2BUnit.getName()) {
			createCell(jnJB2BUnit.getName(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		if (null != jnJB2BUnit.getUid()) {
			createCell(jnJB2BUnit.getUid(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		if (null != salesOrg) {

			if (StringUtils.isNotEmpty(salesOrg.getSalesOrg())) {
				createCell(salesOrg.getSalesOrg(), row, columnNumber++, sheet);
			} else {
				createCell("", row, columnNumber++, sheet);
			}
			if (StringUtils.isNotEmpty(salesOrg.getSector())) {
				createCell(salesOrg.getSector(), row, columnNumber++, sheet);
			} else {
				createCell("", row, columnNumber++, sheet);
			}
		}

		return columnNumber;

	}

	private static void populateColumnsForNotificationSheet(
			List<JnJLaUserAccountPreferenceModel> userNotificationPreference,
			HSSFSheet sheet, int rowNumber) {
		int i = 0;
		for (JnJLaUserAccountPreferenceModel notification : userNotificationPreference) {
			try {
				rowNumber = populateNotificationData(sheet, rowNumber, i,
						notification);

			} catch (Exception exe) {
				LOGGER.error("Error while writing notification to excell ", exe);
				throw exe;
			}
			i++;
		}

	}

	private static int populateNotificationData(HSSFSheet sheet, int rowNumber,
			int i, JnJLaUserAccountPreferenceModel notification) {
		JnJB2bCustomerModel b2bCustomer = notification.getUser();
		JnJB2BUnitModel jnJB2BUnit = notification.getAccount();
		if (jnJB2BUnit instanceof JnJLaB2BUnitModel) {
			List<JnJSalesOrgCustomerModel> salesOrgList = ((JnJLaB2BUnitModel) jnJB2BUnit)
					.getSalesOrg();
			for (JnJSalesOrgCustomerModel salesOrg : salesOrgList) {
				int columnNumber = 0;
				final HSSFRow row = sheet.createRow(rowNumber++);
				createCell(String.valueOf(i + 1), row, columnNumber++,
						sheet);
				if (null != b2bCustomer) {
					columnNumber = populateCustomerData(b2bCustomer,
							columnNumber, sheet, row, false);
					populateCellData(b2bCustomer.getUid(),
							columnNumber++, sheet, row);
				}

				columnNumber = populateUnitSalesOrgData(jnJB2BUnit,
						salesOrg, columnNumber, sheet, row);

				populatePeriodicity(notification, columnNumber++,
						sheet, row);
				populateDateValues(notification.getCreationtime(),
						columnNumber, sheet, row);
			}
		}
		return rowNumber;
	}
	
	private static void populatePeriodicity(
			JnJLaUserAccountPreferenceModel notification, int columnNumber,
			HSSFSheet sheet, HSSFRow row) {
		if (null != notification.getPeriodicity()
				&& null != notification.getPeriodicity().getCode()) {
			createCell(notification.getPeriodicity().getCode(), row,
					columnNumber, sheet);
		} else {
			createCell("", row, columnNumber, sheet);
		}
	}
	
	private static void populateCellData(String str, int columnNumber,
			HSSFSheet sheet, HSSFRow row) {
		if (null != str) {
			createCell(str, row,
					columnNumber, sheet);
		} else {
			createCell("", row, columnNumber, sheet);
		}
	}

	private static void populateDateValues(Object obj, int columnNumber,
			HSSFSheet sheet, HSSFRow row) {
		if (null != obj) {
			createCell(getFormattedDate((Date) obj), row, columnNumber, sheet);
		} else {
			createCell("", row, columnNumber, sheet);
		}
	}

	private static void populateColumnsForSoldToSheet(
			List<JnJB2bCustomerModel> b2bCustomerList, HSSFSheet sheet,
			int rowNumber) {
		int i = 0;
		for (JnJB2bCustomerModel b2bCustomer : b2bCustomerList) {
			Collection<PrincipalGroupModel> groups = b2bCustomer.getGroups();

			for (PrincipalGroupModel group : groups) {
				int columnNumber = 0;
				if (group instanceof JnJLaB2BUnitModel) {
					final HSSFRow row = sheet.createRow(rowNumber++);
					createCell(String.valueOf(i + 1), row, columnNumber++,
							sheet);
					columnNumber = populateCustomerData(b2bCustomer,
							columnNumber, sheet, row, true);
					populateDateValues(b2bCustomer.getLastLogin(),
							columnNumber++, sheet, row);
					populateDateValues(b2bCustomer.getCreationtime(),
							columnNumber++, sheet, row);

					JnJLaB2BUnitModel unit = (JnJLaB2BUnitModel) group;
					columnNumber = populateB2BUnitData(unit, columnNumber,
							sheet, row);
					String sector = getSoldToSectors(unit);
					populateCellData(sector, columnNumber, sheet, row);
					i++;
				}

			}

		}
	}

	private static int populateB2BUnitData(JnJB2BUnitModel unit, int columnNumber,
			HSSFSheet sheet, HSSFRow row) {
		if (null != unit && null != unit.getUid()) {
			createCell(unit.getUid(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		if (null != unit && null != unit.getName()) {
			createCell(unit.getName(), row, columnNumber++, sheet);
		} else {
			createCell("", row, columnNumber++, sheet);
		}
		return columnNumber;

	}

	private static String getSoldToSectors(final JnJLaB2BUnitModel unit) {
		StringBuilder buffer = new StringBuilder();
		List<JnJSalesOrgCustomerModel> salesOrgList = unit.getSalesOrg();

		if (CollectionUtils.isNotEmpty(salesOrgList)) {
			int i = 0;
			for (JnJSalesOrgCustomerModel salesOrg : salesOrgList) {
				if (i == 1) {
					buffer.append(" AND ");
				}
				buffer.append(salesOrg.getSector());
				i++;

			}
			return buffer.toString();
		}
		return null;
	}

	private static void populateColumnsForMainSheet(
			List<JnJB2bCustomerModel> b2bCustomerList, HSSFSheet sheet,
			int rowNumber) {
		int i = 0;
		for (JnJB2bCustomerModel b2bCustomer : b2bCustomerList) {
			int columnNumber = 0;

			if (b2bCustomer != null) {
				final HSSFRow row = sheet.createRow(rowNumber++);
				createCell(String.valueOf(i + 1), row, columnNumber++, sheet);

				columnNumber = populateCustomerData(b2bCustomer, columnNumber,
						sheet, row, true);
				
				populateDateValues(b2bCustomer.getLastLogin(), columnNumber++,
						sheet, row);
				populateDateValues(b2bCustomer.getCreationtime(),
						columnNumber++, sheet, row);

				columnNumber = populateB2BUnitData(
						b2bCustomer.getCurrentB2BUnit(), columnNumber, sheet,
						row);

				String sector = getSectors(b2bCustomer);
				if (null != sector) {
					createCell(sector, row, columnNumber, sheet);
				} else {
					createCell("", row, columnNumber, sheet);
				}

			}
			i++;
		}
	}

	private static String getSectors(final JnJB2bCustomerModel b2bCustomer) {
		JnJB2BUnitModel unit = b2bCustomer.getCurrentB2BUnit();

		if (unit instanceof JnJLaB2BUnitModel) {
			JnJLaB2BUnitModel b2bUnit = (JnJLaB2BUnitModel) unit;
			return getSoldToSectors(b2bUnit);
		}
		return null;
	}

	private static void createCell(final Object object, final HSSFRow row,
			final int cellNumber, final HSSFSheet sheet) {
		if (object != null) {
			if (object instanceof String
					&& !StringUtils.startsWithIgnoreCase(
							((String) object).trim(), "null")) {
				row.createCell(cellNumber).setCellValue((String) object);
			} else if (object instanceof Date) {
				row.createCell(cellNumber).setCellValue((Date) object);
			} else if (object instanceof Double) {
				row.createCell(cellNumber).setCellValue(
						((Double) object).doubleValue());
			} else if (object instanceof Integer) {
				row.createCell(cellNumber).setCellValue(
						((Integer) object).intValue());
			} else if (object instanceof List<?>) {
				row.createCell(cellNumber).setCellValue(
						!((List) object).isEmpty() ? ((List<Object>) object)
								.toString() : "");
			} else {
				row.createCell(cellNumber).setCellValue(" ");
			}
		} else {
			row.createCell(cellNumber).setCellValue(" ");
		}
		sheet.autoSizeColumn(cellNumber);
	}

	private static void createColumnsForSoldToSheet(HSSFRow header0,
			HSSFCellStyle style) {

		header0.createCell(0).setCellValue(SERIAL_NO);
		header0.getCell(0).setCellStyle(style);

		header0.createCell(1).setCellValue(EMAIL_ID);
		header0.getCell(1).setCellStyle(style);

		header0.createCell(2).setCellValue(CUSTOMER_NAME);
		header0.getCell(2).setCellStyle(style);

		header0.createCell(3).setCellValue(COUNTRY);
		header0.getCell(3).setCellStyle(style);

		header0.createCell(4).setCellValue(LAST_LOGIN_DATE);
		header0.getCell(4).setCellStyle(style);

		header0.createCell(5).setCellValue(CREATION_DATE);
		header0.getCell(5).setCellStyle(style);

		header0.createCell(6).setCellValue(SOLD_TO_NUMBER);
		header0.getCell(6).setCellStyle(style);

		header0.createCell(7).setCellValue(SOLD_TO_NAME);
		header0.getCell(7).setCellStyle(style);

		header0.createCell(8).setCellValue(SECTOR);
		header0.getCell(8).setCellStyle(style);
	}

	private static void createColumnsForNotificationSheet(HSSFRow header1,
			HSSFCellStyle style) {

		header1.createCell(0).setCellValue(SERIAL_NO);
		header1.getCell(0).setCellStyle(style);

		header1.createCell(1).setCellValue(CUSTOMER_LOGIN_DATE);
		header1.getCell(1).setCellStyle(style);

		header1.createCell(2).setCellValue(CUSTOMER_NAME);
		header1.getCell(2).setCellStyle(style);

		header1.createCell(3).setCellValue(CUSTOMER_EMAIL);
		header1.getCell(3).setCellStyle(style);

		header1.createCell(4).setCellValue(SOLD_TO_NAME);
		header1.getCell(4).setCellStyle(style);

		header1.createCell(5).setCellValue(SOLD_TO_NUMBER);
		header1.getCell(5).setCellStyle(style);

		header1.createCell(6).setCellValue(SALESORG_COUNTRY);
		header1.getCell(6).setCellStyle(style);

		header1.createCell(7).setCellValue(SECTOR);
		header1.getCell(7).setCellStyle(style);

		header1.createCell(8).setCellValue(PREFERENCE);
		header1.getCell(8).setCellStyle(style);

		header1.createCell(9).setCellValue(DATE_CREATED);
		header1.getCell(9).setCellStyle(style);
	}

	private static void createColumnsForMainSheet(HSSFRow header2, HSSFCellStyle style) {
		header2.createCell(0).setCellValue(SERIAL_NO);
		header2.getCell(0).setCellStyle(style);

		header2.createCell(1).setCellValue(EMAIL_ID);
		header2.getCell(1).setCellStyle(style);

		header2.createCell(2).setCellValue(CUSTOMER_NAME);
		header2.getCell(2).setCellStyle(style);

		header2.createCell(3).setCellValue(COUNTRY);
		header2.getCell(3).setCellStyle(style);

		header2.createCell(4).setCellValue(LAST_LOGIN_DATE);
		header2.getCell(4).setCellStyle(style);

		header2.createCell(5).setCellValue(CREATION_DATE);
		header2.getCell(5).setCellStyle(style);

		header2.createCell(6).setCellValue(SOLD_TO_NUMBER);
		header2.getCell(6).setCellStyle(style);

		header2.createCell(7).setCellValue(SOLD_TO_NAME);
		header2.getCell(7).setCellStyle(style);

		header2.createCell(8).setCellValue(SECTOR);
		header2.getCell(8).setCellStyle(style);
	}

	private static String getFormattedDate(final Date date) {
		final DateFormat df = new SimpleDateFormat(DATE_FORMAT_DATA);
		df.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		return df.format(date);
	}

	@Override
	public boolean isAbortable() {
		return true;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService) {
		this.baseSiteService = baseSiteService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}

	public JnJLaCustomerDataService getJnjCustomerDataService() {
		return jnjCustomerDataService;  
	}

	public void setJnjCustomerDataService(
			final JnJLaCustomerDataService jnjCustomerDataService) {
		this.jnjCustomerDataService = jnjCustomerDataService;
	}

	public static void setConfigurationService(
			final ConfigurationService configurationService) {
		JnjLaActiveUsersReportJob.configurationService = configurationService;
	}

}
