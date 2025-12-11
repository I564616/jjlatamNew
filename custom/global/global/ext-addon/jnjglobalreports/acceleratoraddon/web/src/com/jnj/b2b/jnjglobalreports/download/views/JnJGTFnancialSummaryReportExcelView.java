package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialSummaryReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTFinancialAccountAgingReportData;
import com.jnj.facades.data.JnjGTFinancialBalanceSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialCreditSummaryReportData;
import com.jnj.facades.data.JnjGTFinancialPaymentSummaryReportData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnJGTFnancialSummaryReportExcelView extends AbstractXlsView {

	private static final Logger LOG = Logger.getLogger(JnJGTFnancialSummaryReportExcelView.class);
	
	private static final String FINANCIAL_SUMMARY_ACCOUNT_AGINING_DATA_LIST = "accountAgingReportDatasList";
	private static final String FINANCIAL_BALANCE_SUMMARY_DATA_LIST = "balanceSummaryReportDatasList";
	private static final String FINANCIAL_PAYMENT_SUMMARY_DATA_LIST = "paymentSummaryReportDatasList";
	private static final String FINANCIAL_CREDIT_SUMMARY_DATA_LIST = "creditSummaryReportDatasList";
	private static final String FINANCIAL_SUMMARY_FORM_NAME = "JnjGlobalFinancialSummaryReportForm";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	@Autowired
	protected I18NService i18nService;
	private MessageSource messageSource;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	
	/**
	 * @return the jnjCommonUtil
	 */
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	/**
	 * @param jnjCommonUtil the jnjCommonUtil to set
	 */
	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	public MessageSource getMessageSource()
	{
		return messageSource;
	}

	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	public I18NService getI18nService()
	{
		return i18nService;
	}

	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}
	//Modified by Archana for AAOL-5513
	/**
	 * This method generates the Excel doc
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest httpServletRequest,
									  final HttpServletResponse httpServletResponse) throws Exception
	{
		try
		{
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename = Financial_Summary_Report.xls");
			final List<JnjGTFinancialAccountAgingReportData> accountAgingReportDatasList = (List<JnjGTFinancialAccountAgingReportData>) map
					.get(FINANCIAL_SUMMARY_ACCOUNT_AGINING_DATA_LIST);
			final List<JnjGTFinancialBalanceSummaryReportData> balanceSummaryReportDatasList = (List<JnjGTFinancialBalanceSummaryReportData>) map
					.get(FINANCIAL_BALANCE_SUMMARY_DATA_LIST);
			final List<JnjGTFinancialPaymentSummaryReportData>  paymentSummaryReportDatasList = (List<JnjGTFinancialPaymentSummaryReportData>) map
					.get(FINANCIAL_PAYMENT_SUMMARY_DATA_LIST);
			final List<JnjGTFinancialCreditSummaryReportData> creditSummaryReportDatasList = (List<JnjGTFinancialCreditSummaryReportData>) map
					.get(FINANCIAL_CREDIT_SUMMARY_DATA_LIST);
			
			final JnjGlobalFinancialSummaryReportForm financialSummaryForm = (JnjGlobalFinancialSummaryReportForm) map.get(FINANCIAL_SUMMARY_FORM_NAME);
			//Modified by Archana for AAOL-5513
			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Sheet searchSheet = hssfWorkbook.createSheet("Financial Summary Search Criteria");
			final Row searchHeader = searchSheet.createRow(6);
			setHeaderImage(hssfWorkbook, searchSheet, (String) map.get("siteLogoPath"));
			
			
			final Row searchRow = searchSheet.createRow(7);

			final Sheet sheet = hssfWorkbook.createSheet("Financial Summary Report");
			final Row header = sheet.createRow(0);
			CellStyle css = createValueCell(hssfWorkbook);
			
			int rowNum = 1;
			if (null != accountAgingReportDatasList)
			{
				searchHeader.createCell(0).setCellValue("Account Aging");
				searchHeader.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
				Cell cell0 = searchRow.createCell(0);
				cell0.setCellValue(new HSSFRichTextString(financialSummaryForm.getAccountAgingPayerID()));
				cell0.setCellStyle(css);
				
				header.createCell(0).setCellValue("Account Aging");
				header.createCell(1).setCellValue("Days in Arrears");
				header.createCell(2).setCellValue("Due Item");
				header.createCell(3).setCellValue("Not Due");
				for (final JnjGTFinancialAccountAgingReportData jnjgtacAccountAgingReportData : accountAgingReportDatasList)
				{
					final Row row = sheet.createRow(rowNum++);
					row.createCell(0).setCellValue("");
					row.createCell(1).setCellValue(jnjgtacAccountAgingReportData.getDaysinArrears());
					row.createCell(2).setCellValue(jnjgtacAccountAgingReportData.getDueItem());
					row.createCell(3).setCellValue(jnjgtacAccountAgingReportData.getNotDue());
				}
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
			}
			
			if (null != balanceSummaryReportDatasList)
			{
				searchHeader.createCell(1).setCellValue("AR Balance Summary");
				searchHeader.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
				Cell cell0 = searchRow.createCell(1);
				cell0.setCellValue(new HSSFRichTextString(financialSummaryForm.getBalanceSummaryPayerID()));
				cell0.setCellStyle(css);
				
				header.createCell(4).setCellValue("AR Balance Summary");
				header.createCell(5).setCellValue("Due Date");
				header.createCell(6).setCellValue("Open Amount");
				header.createCell(7).setCellValue("Total");
		    	
				for (final JnjGTFinancialBalanceSummaryReportData jnjgtBalanceSummaryReportData : balanceSummaryReportDatasList)
				{
					final Row row = sheet.createRow(rowNum++);
					row.createCell(4).setCellValue("");
					row.createCell(5).setCellValue(jnjgtBalanceSummaryReportData.getAmountPaid());
					row.createCell(6).setCellValue(jnjgtBalanceSummaryReportData.getDueDate());//Modified by Archana for AAOL-5513
					row.createCell(7).setCellValue(jnjgtBalanceSummaryReportData.getTotal());
				}
				sheet.autoSizeColumn(4);
				sheet.autoSizeColumn(5);
				sheet.autoSizeColumn(6);
				sheet.autoSizeColumn(7);
				
			}
			
			if (null != paymentSummaryReportDatasList)
			{
				searchHeader.createCell(2).setCellValue("Payment Summary");
				searchHeader.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
				Cell cell0 = searchRow.createCell(2);
				cell0.setCellValue(new HSSFRichTextString(financialSummaryForm.getPaymentSummaryPayerID()));
				cell0.setCellStyle(css);
				
				header.createCell(8).setCellValue("Payment Summary");
				header.createCell(9).setCellValue("Amount Invoiced MTD");
				header.createCell(10).setCellValue("Net Amount Paid MTD");
				header.createCell(11).setCellValue("Amount Invoiced Prior Month");
				header.createCell(12).setCellValue("Net Amount Paid Prior Month");
				header.createCell(13).setCellValue("Amount Invoiced This Year");
				header.createCell(14).setCellValue("Net Amount Paid This Year");
				header.createCell(15).setCellValue("Amount Invoiced Prior Year");
				header.createCell(16).setCellValue("Net Amount Paid Prior Year");
				header.createCell(17).setCellValue("Last Payment Amount");
				
				for (final JnjGTFinancialPaymentSummaryReportData jnjGTFinancialPaymentSummaryReportData : paymentSummaryReportDatasList)
				{
					final Row row = sheet.createRow(rowNum++);
					row.createCell(8).setCellValue("");
					row.createCell(9).setCellValue(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedMTD());
					row.createCell(10).setCellValue(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidMTD());
					row.createCell(11).setCellValue(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorMonth());
					row.createCell(12).setCellValue(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPriorMonth());
					row.createCell(13).setCellValue(jnjGTFinancialPaymentSummaryReportData.getAmountInvoiceThisYear());
					row.createCell(14).setCellValue(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidThisYear());
					row.createCell(15).setCellValue(jnjGTFinancialPaymentSummaryReportData.getAmountInvoicedPriorYear());
					row.createCell(16).setCellValue(jnjGTFinancialPaymentSummaryReportData.getNetAmountPaidPrioryear());
					row.createCell(17).setCellValue(jnjGTFinancialPaymentSummaryReportData.getLastPaymentAmount());
				}
				
				sheet.autoSizeColumn(8);
				sheet.autoSizeColumn(9);
				sheet.autoSizeColumn(10);
				sheet.autoSizeColumn(11);
				sheet.autoSizeColumn(12);
				sheet.autoSizeColumn(13);
				sheet.autoSizeColumn(14);
				sheet.autoSizeColumn(15);
				sheet.autoSizeColumn(16);
				sheet.autoSizeColumn(17);
			}
			
			if (null != creditSummaryReportDatasList)
			{
				searchHeader.createCell(3).setCellValue("Credit Summary");
				searchHeader.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
				Cell cell0 = searchRow.createCell(3);
				cell0.setCellValue(new HSSFRichTextString(financialSummaryForm.getCreditSummaryPayerID()));
				cell0.setCellStyle(css);
				
				header.createCell(18).setCellValue("Credit Summary");
				header.createCell(19).setCellValue("Open Order Value");
				header.createCell(20).setCellValue("Open Delivery Value");
				header.createCell(21).setCellValue("Amount Due");
				header.createCell(22).setCellValue("Credit Used");
				header.createCell(23).setCellValue("Credit Limit");
				header.createCell(24).setCellValue("Over/Under Value");
				header.createCell(25).setCellValue("Credit Limit Used (%)");
		    	
				for (final JnjGTFinancialCreditSummaryReportData jnjGTFinancialCreditSummaryReportData : creditSummaryReportDatasList)
				{
					final Row row = sheet.createRow(rowNum++);
					row.createCell(18).setCellValue("");
					row.createCell(19).setCellValue(jnjGTFinancialCreditSummaryReportData.getOpenOrderValue());
					row.createCell(20).setCellValue(jnjGTFinancialCreditSummaryReportData.getOpenDeliveryValue());
					row.createCell(21).setCellValue(jnjGTFinancialCreditSummaryReportData.getAmountDue());
					row.createCell(22).setCellValue(jnjGTFinancialCreditSummaryReportData.getCreditUsed());
					row.createCell(23).setCellValue(jnjGTFinancialCreditSummaryReportData.getCreditLimit());
					row.createCell(24).setCellValue(jnjGTFinancialCreditSummaryReportData.getOverUnderValue());
					row.createCell(25).setCellValue(jnjGTFinancialCreditSummaryReportData.getCreditLimitUsed());
				}
				
				sheet.autoSizeColumn(18);
				sheet.autoSizeColumn(19);
				sheet.autoSizeColumn(20);
				sheet.autoSizeColumn(21);
				sheet.autoSizeColumn(22);
				sheet.autoSizeColumn(23);
				sheet.autoSizeColumn(24);
			}
		}
		catch (final Exception exception)
		{
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
	}
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath)
	{
		InputStream inputStream = null;
		int index = 0;
		try
		{
			inputStream = new FileInputStream(logoPath);
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		}
		catch (final IOException ioException)
		{
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
			

	}
	
	protected static CellStyle createLabelCell(Workbook workbook){

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
	protected static CellStyle createValueCell(Workbook workbook){

	    CellStyle cellStyle = workbook.createCellStyle();

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

}
