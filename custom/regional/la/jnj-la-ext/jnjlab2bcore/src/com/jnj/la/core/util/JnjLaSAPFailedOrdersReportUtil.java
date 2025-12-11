package com.jnj.la.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import de.hybris.platform.core.model.order.OrderModel;

/**
 * This class is used to create a excel report sap failed order email.
 *
 */
public class JnjLaSAPFailedOrdersReportUtil {

	private static final Logger LOGGER = Logger.getLogger(JnjLaSAPFailedOrdersReportUtil.class);

	private JnjLaSAPFailedOrdersReportUtil() {
	}

	/**
	 * This method is used to create report for sap failed orders
	 * 
	 * @param sapFailedOrders
	 *
	 */
	public static boolean createSAPFailedOrderReport(final List<OrderModel> sapFailedOrders, final String filepath,
			final String fileName, final String sheetName) {

		// Create and populate Excel for SAP failed order report
		final HSSFWorkbook workbook = new HSSFWorkbook();
		populateSAPFailedOrdersReport(workbook, sapFailedOrders, sheetName);
		try {
			writeExcel(workbook, filepath + File.separator + fileName);
			workbook.close();
		} catch (final IOException ioException) {
			LOGGER.error("Error occurred while writing to excel", ioException);
			return false;
		}
		return true;
	}

	/**
	 * Populates the value for header and rows of the report.
	 * 
	 * @param workbook        - holds HSSFWorkbook
	 * @param sapFailedOrders - sap failed orders even after maximum retry
	 */
	private static void populateSAPFailedOrdersReport(final HSSFWorkbook workbook,
			final List<OrderModel> sapFailedOrders, final String sheetName) {

		HSSFSheet reportSheet = workbook.createSheet(sheetName);

		final HSSFRow header = reportSheet.createRow(0);

		populateReportHeader(header);

		populateReportValue(reportSheet, sapFailedOrders);
	}

	/**
	 * Populates values for rows
	 * 
	 * @param sheet           - holds HSSFSheet
	 * @param sapFailedOrders - sap failed orders even after maximum retry
	 */
	private static void populateReportValue(final HSSFSheet sheet, final List<OrderModel> sapFailedOrders) {
		int rowNumber = 1;
		for (OrderModel sapFailedOrder : sapFailedOrders) {
			final HSSFRow row = sheet.createRow(rowNumber++);
			final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.SapFailedOrderReport.DATE_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone(Jnjlab2bcoreConstants.SapFailedOrderReport.TIME_ZONE));

			final String creationAttemptDate = dateFormat.format(sapFailedOrder.getCreationtime());

			createCell(creationAttemptDate, row, 0, sheet);

			createCell(sapFailedOrder.getCode(), row, 1, sheet);

			if (null != sapFailedOrder.getUnit()) {
				createCell(sapFailedOrder.getUnit().getUid(), row, 2, sheet);
				createCell(sapFailedOrder.getUnit().getName(), row, 3, sheet);
			} else {
				createCell(StringUtils.EMPTY, row, 2, sheet);
				createCell(StringUtils.EMPTY, row, 3, sheet);
			}

			if (null != sapFailedOrder.getDeliveryAddress()) {
				createCell(sapFailedOrder.getDeliveryAddress().getJnJAddressId(), row, 4, sheet);
			} else {
				createCell(StringUtils.EMPTY, row, 4, sheet);
			}

			// Get the list of sap error messages and populate the recent error message got
			// from SAP ERP
			if (CollectionUtils.isNotEmpty(sapFailedOrder.getSapErrorMessages())) {
				List<String> sapErrorMessage = sapFailedOrder.getSapErrorMessages();
				createCell(sapErrorMessage.get(sapErrorMessage.size() - 1), row, 5, sheet);
			} else {
				createCell(StringUtils.EMPTY, row, 5, sheet);
			}

		}

	}

	/**
	 * Create headers for the report
	 * 
	 * @param header - holds HSSFRow for header
	 */
	private static void populateReportHeader(final HSSFRow header) {

		header.createCell(0).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.CREATION_ATTEMPT_DATE);

		header.createCell(1).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.ORDER_NUMBER);

		header.createCell(2).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.ACCOUNT_NUMBER);

		header.createCell(3).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.ACCOUNT_DESCRIPTION);

		header.createCell(4).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.SHIP_TO);

		header.createCell(5).setCellValue(Jnjlab2bcoreConstants.SapFailedOrderReport.ERROR);

	}

	/**
	 * Creates a cell in the excel sheet.
	 * 
	 * @param object     - value of the cell.
	 * @param row        - holds HSSFRow.
	 * @param cellNumber - index of the cell.
	 * @param sheet      - holds HSSFSheet.
	 */
	private static void createCell(final Object object, final HSSFRow row, final int cellNumber,
			final HSSFSheet sheet) {
		if (object != null) {
			if (object instanceof String && !StringUtils.startsWithIgnoreCase(((String) object).trim(), "null")) {
				row.createCell(cellNumber).setCellValue((String) object);
			} else if (object instanceof Date) {
				row.createCell(cellNumber).setCellValue((Date) object);
			} else if (object instanceof Double) {
				row.createCell(cellNumber).setCellValue(((Double) object).doubleValue());
			} else if (object instanceof Integer) {
				row.createCell(cellNumber).setCellValue(((Integer) object).intValue());
			} else if (object instanceof List<?>) {
				row.createCell(cellNumber)
						.setCellValue(!((List) object).isEmpty() ? ((List<Object>) object).toString() : "");
			} else {
				row.createCell(cellNumber).setCellValue(" ");
			}
		} else {
			row.createCell(cellNumber).setCellValue(" ");
		}
		sheet.autoSizeColumn(cellNumber);
	}

	/**
	 * This method will write a report in temp location
	 * 
	 * @param workbook - holds HSSFWorkbook
	 * @param filePath - holds filePath of the report
	 * @throws IOException
	 */
	private static void writeExcel(final HSSFWorkbook workbook, final String filePath) throws IOException {

		final File file = new File(filePath);

		try (FileOutputStream out = new FileOutputStream(file)) {
			workbook.write(out);
			out.flush();
		} catch (final IOException exception) {
			LOGGER.error("There was an error while trying to preform input output operations on the file", exception);
			throw exception;
		}
	}

}