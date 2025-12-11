package com.jnj.b2b.loginaddon.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSerialResponseData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author Cognizant
 * This class creates excel view for serialization search results
 *
 */
public class JnJGTSerializationResultsExcelView extends AbstractXlsView {

	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	
	protected static final Logger LOG = Logger
			.getLogger(JnJGTSerializationResultsExcelView.class);
	private static final String SHEETNAME = "serialization.download.excel.sheetName";
	private static final String SERIALIZATION_RESULTS_NAME = "SerializationResults";
	
	private static final String DOWNLOAD_DATE = "serialization.download.downloadDate";
	private static final String DOWNLOAD_TIME = "serialization.download.downloadTime";
	private static final String SEARCH_CRITERIA = "serialization.download.searchCeriteria";
	private static final String GTIN = "serialization.gtin";
	private static final String SERIAL_NUMBER = "serialization.serialNumber";
	private static final String BATCH_NUMBER = "serialization.batchNumber";
	private static final String EXPIRY_YEAR = "serialization.expiryYear";
	private static final String EXPIRY_MONTH = "serialization.expiryMonth";
	private static final String EXPIRY_DAY = "serialization.expiryDay";
	private static final String SERIAL_NUMBER_RESULT = "serialization.result.serialNumber";
	private static final String STATUS = "serialization.result.status";
	private static final String REASON = "serialization.result.reason";
	private static final String RESULTS = "serialization.result";

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map,
			final Workbook workbook,
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse) throws Exception {
		try {
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=Product_Serial_details.xls");
			
			JnjGTSerialResponseData searchResults = (JnjGTSerialResponseData) map.get(SERIALIZATION_RESULTS_NAME); 

			final Sheet searchSheet = workbook.createSheet(messageSource.getMessage(SHEETNAME, null, i18nService.getCurrentLocale()));
			setHeaderImage(workbook, searchSheet,
					(String) map.get("siteLogoPath"));

			// START Font size needs to display
			final Font font = workbook.createFont();
			font.setBold(true);

			final CellStyle style = workbook.createCellStyle();
			style.setFont(font);
			// END Font size needs to display

			final Row searchRow6 = searchSheet.createRow(6);

			final Row searchRow8 = searchSheet.createRow(8);
			final Row searchRow9 = searchSheet.createRow(9);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"dd/MM/yyyy");
			String date = simpleDateFormat.format(new Date());
			SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss z");
			String time = simpleTimeFormat.format(new Date());

			searchRow8.createCell(1).setCellValue(messageSource.getMessage(DOWNLOAD_DATE, null, i18nService.getCurrentLocale()));
			searchRow8.getCell(1).setCellStyle(style);
			searchRow9.createCell(1).setCellValue(messageSource.getMessage(DOWNLOAD_TIME, null, i18nService.getCurrentLocale()));
			searchRow9.getCell(1).setCellStyle(style);

			searchRow8.createCell(2).setCellValue(date);
			searchRow9.createCell(2).setCellValue(time);

			final Row searchRow11 = searchSheet.createRow(11);
			searchRow11.createCell(1).setCellValue(messageSource.getMessage(SEARCH_CRITERIA, null, i18nService.getCurrentLocale()));
			searchRow11.getCell(1).setCellStyle(style);

			final Row searchRow13 = searchSheet.createRow(13);
			final Row searchRow14 = searchSheet.createRow(14);
			final Row searchRow15 = searchSheet.createRow(15);
			final Row searchRow16 = searchSheet.createRow(16);
			final Row searchRow17 = searchSheet.createRow(17);
			final Row searchRow18 = searchSheet.createRow(18);

			searchRow13.createCell(1).setCellValue(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale())+":");
			searchRow13.getCell(1).setCellStyle(style);
			searchRow14.createCell(1).setCellValue(messageSource.getMessage(SERIAL_NUMBER, null, i18nService.getCurrentLocale())+":");
			searchRow14.getCell(1).setCellStyle(style);
			searchRow15.createCell(1).setCellValue(messageSource.getMessage(BATCH_NUMBER, null, i18nService.getCurrentLocale())+":");
			searchRow15.getCell(1).setCellStyle(style);
			searchRow16.createCell(1).setCellValue(messageSource.getMessage(EXPIRY_DAY, null, i18nService.getCurrentLocale())+":");
			searchRow16.getCell(1).setCellStyle(style);
			searchRow17.createCell(1).setCellValue(messageSource.getMessage(EXPIRY_MONTH, null, i18nService.getCurrentLocale())+":");
			searchRow17.getCell(1).setCellStyle(style);
			searchRow18.createCell(1).setCellValue(messageSource.getMessage(EXPIRY_YEAR, null, i18nService.getCurrentLocale())+":");
			searchRow18.getCell(1).setCellStyle(style);

			searchRow13.createCell(2)
					.setCellValue(searchResults.getGtin());
			searchRow14.createCell(2).setCellValue(
					searchResults.getInputSerialNumber());
			searchRow15.createCell(2).setCellValue(
					searchResults.getBatchNumber());
			searchRow16.createCell(2).setCellValue(
					searchResults.getExpiryDay());
			searchRow17.createCell(2).setCellValue(searchResults.getExpiryMonth());
			searchRow18.createCell(2).setCellValue(searchResults.getExpiryYear());

			final Row searchRow21 = searchSheet.createRow(21);
			searchRow21.createCell(1).setCellValue(messageSource.getMessage(RESULTS, null, i18nService.getCurrentLocale())+" :");
			searchRow21.getCell(1).setCellStyle(style);
			
			final Row resultRow23 = searchSheet.createRow(23);
			final Row resultRow24 = searchSheet.createRow(24);
			final Row resultRow25 = searchSheet.createRow(25);

			resultRow23.createCell(1).setCellValue(messageSource.getMessage(SERIAL_NUMBER_RESULT, null, i18nService.getCurrentLocale())+":");
			resultRow23.getCell(1).setCellStyle(style);
			resultRow24.createCell(1).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale())+":");
			resultRow24.getCell(1).setCellStyle(style);
			//Changes for AAOL-6197
			if(!searchResults.getStatus().equalsIgnoreCase(Jnjb2bCoreConstants.Serialization.KNOWN)){
				
				resultRow25.createCell(1).setCellValue(messageSource.getMessage(REASON, null, i18nService.getCurrentLocale())+":");
				resultRow25.getCell(1).setCellStyle(style);
			}

			resultRow23.createCell(2)
					.setCellValue(searchResults.getSerialNumber());
			resultRow24.createCell(2).setCellValue(
					searchResults.getStatus());
			if(!searchResults.getStatus().equalsIgnoreCase(Jnjb2bCoreConstants.Serialization.KNOWN)){
				if(null!=searchResults.getReason()){					
					resultRow25.createCell(2).setCellValue(
							searchResults.getReason());
				}
			}


		} catch (final Exception exception) {
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
	}

	public void setHeaderImage(final Workbook hssfWorkbook,
			final Sheet sheet, final String logoPath) {
		final CellStyle styleImgCell = hssfWorkbook.createCellStyle();
		styleImgCell.setAlignment(HorizontalAlignment.CENTER);
		// final Row header0 = sheet.createRow(0);
		InputStream inputStream = null;
		int index = 0;
		try {
			inputStream = new FileInputStream(logoPath);
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes,
					HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		} catch (final IOException ioException) {
			logger.error("Exception occured during input output operation in the method setHeaderImage()");
		}
		final CreationHelper helper = hssfWorkbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setCol2(10);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		final Picture pict = drawing.createPicture(anchor, index);
		sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 9));
		// header0.getCell(0).setCellStyle(styleImgCell);
	}

	protected static CellStyle createLabelCell(HSSFWorkbook workbook) {

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());

		cellStyle.setBorderBottom(BorderStyle.THIN);

		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderLeft(BorderStyle.THIN);

		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderRight(BorderStyle.THIN);

		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderTop(BorderStyle.THIN);

		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

		return cellStyle;

	}

	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
