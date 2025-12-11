package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceDueReportDueForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;

import de.hybris.platform.servicelayer.i18n.I18NService;

import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnJGTFinancialInvoiceDueReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTBackorderReportExcelView.class);
	private static final String FINANCIAL_RESPONSE_DATA_LIST = "invoicePastDueResponse";
	private static final String FINANCIAL_INVOICE_DUE_FORM_NAME = "jnjGTInvoiceDueReportDueForm";
	private static final String sheetName = "INVOICE DUE REPORT RESULTS";
	private static final String ACC_NAME = "backorder.reports.accountname";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String HEADER = "invoicedue.reports.header";
	private static final String INVOICE_NUM = "reports.financial.invoicenumber";
	private static final String INVOICE_DATE = "reports.invoicedue.invoicedate";
	private static final String SOLD_TO_ACC = "reports.invoicedue.soldtoaccount";
	private static final String SOLD_TO_ACC_NAME = "reports.invoicedue.soldtoname";
	private static final String SALE_DOC_NUM = "reports.invoicedue.salesdocnum";
	private static final String CUSTOMER_PO = "reports.financial.table.product.customerPoNo";
	private static final String STATUS = "reports.purchase.analysis.popup.status";
	private static final String INVOICE_DUE_DATE = "reports.financial.invoiceduedate";
	private static final String CURRENCY = "reports.financial.table.product.currency";
	private static final String TOTAL_AMOUNT = "reports.invoicedue.totalamount";
	private static final String OUTSTANDING_AMOUNT = "reports.invoicedue.outstandingamount";
	

	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";
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
	@Autowired
	private I18NService i18nService;
	private MessageSource messageSource;
	
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
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		 
			arg3.setHeader("Content-Disposition", "attachment; filename=Invoice Past Due.xls");
			final String emptyField = new String();
			final List<JnjGTInvoicePastDueReportResponseData> jnjGTInvoicePastDueReportResponseDataList = (List<JnjGTInvoicePastDueReportResponseData>) arg0
					.get(FINANCIAL_RESPONSE_DATA_LIST);
			final JnjGTInvoiceDueReportDueForm searchCriteria = (JnjGTInvoiceDueReportDueForm) arg0.get(FINANCIAL_INVOICE_DUE_FORM_NAME);

			final String accountsSelectedName = (String) arg0.get("accountreportname");
			final Sheet searchSheet = arg1.createSheet(sheetName);
			setHeaderImage(arg1, searchSheet, (String) arg0.get("siteLogoPath"));
			final Row header0 = searchSheet.createRow(6);
			final Row header3 = searchSheet.createRow(9);
			final Row header4 = searchSheet.createRow(10);
			final Row header5 = searchSheet.createRow(11);
			//final Row header9 = searchSheet.createRow(15);
			final Row header = searchSheet.createRow(17);
			
			final Font font = arg1.createFont();
			font.setBold(true);
			
			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);
			
			final CellStyle styleCell = arg1.createCellStyle();
			styleCell.setWrapText(true);
			
			header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			header0.getCell(0).setCellStyle(createLabelCell(arg1));
			
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Date date = new Date();

			header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			header3.getCell(0).setCellStyle(createLabelCell(arg1));
			header3.createCell(1).setCellValue(dateFormat.format(date));

			header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header4.getCell(0).setCellStyle(createLabelCell(arg1));
			
			//header4.createCell(1).setCellValue("00080888");
			header4.createCell(1).setCellValue((searchCriteria.getAccountIds() != null) ? searchCriteria.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);
			
			
			header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			header5.getCell(0).setCellStyle(createLabelCell(arg1));
			header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);
			//Modified by Archana for AAOL-5513
			final SimpleDateFormat dateFormat1 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final SimpleDateFormat dateFormat2 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
					
			header.createCell(1).setCellValue(messageSource.getMessage(INVOICE_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));
			
			header.createCell(2).setCellValue(messageSource.getMessage(INVOICE_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));

			header.createCell(3).setCellValue(messageSource.getMessage(SOLD_TO_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));
			
			header.createCell(4).setCellValue(messageSource.getMessage(SOLD_TO_ACC_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));
			
			header.createCell(5).setCellValue(messageSource.getMessage(SALE_DOC_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));
			
			header.createCell(6).setCellValue(messageSource.getMessage(CUSTOMER_PO, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));
			
			header.createCell(7).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));

			header.createCell(8).setCellValue(messageSource.getMessage(INVOICE_DUE_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));
			
			header.createCell(9).setCellValue(messageSource.getMessage(CURRENCY, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(arg1));

			header.createCell(10).setCellValue(messageSource.getMessage(TOTAL_AMOUNT, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(arg1));
			
			header.createCell(11).setCellValue(messageSource.getMessage(OUTSTANDING_AMOUNT, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(arg1));

			int rowNum = 18;
			if (null != jnjGTInvoicePastDueReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTInvoicePastDueReportResponseData jnjGTInvoicePastDueReportResponseData : jnjGTInvoicePastDueReportResponseDataList)
				{
					searchSheet.autoSizeColumn(rowNum);
					final Row row = searchSheet.createRow(rowNum++);
					row.setRowStyle(styleCell);
					row.createCell(1).setCellValue(jnjGTInvoicePastDueReportResponseData.getInvoiceNum());
					row.createCell(2).setCellValue(jnjGTInvoicePastDueReportResponseData.getBillingDate());//Modified by Archana for AAOL-5513
					row.createCell(3).setCellValue(jnjGTInvoicePastDueReportResponseData.getSoldToAccNum());
					row.createCell(4).setCellValue(jnjGTInvoicePastDueReportResponseData.getSoldToAccName());
					row.createCell(5).setCellValue(jnjGTInvoicePastDueReportResponseData.getOrderNum());
					row.createCell(6).setCellValue(jnjGTInvoicePastDueReportResponseData.getCustomerPoNum());
					/*row.createCell(5).setCellValue(jnjGTInvoicePastDueReportResponseData.getReceiptNumber());*/
					row.createCell(7).setCellValue(jnjGTInvoicePastDueReportResponseData.getStatus());
					row.createCell(8).setCellValue(jnjGTInvoicePastDueReportResponseData.getInvoiceDueDate());//Modified by Archana for AAOL-5513
					row.createCell(9).setCellValue(jnjGTInvoicePastDueReportResponseData.getCurrency());
					row.createCell(10).setCellValue(jnjGTInvoicePastDueReportResponseData.getInvoiceTotalAmount());
					row.createCell(11).setCellValue(jnjGTInvoicePastDueReportResponseData.getOpenAmount());
					/*row.createCell(10).setCellValue(jnjGTInvoicePastDueReportResponseData.getCurrency());
					row.createCell(11).setCellValue(jnjGTInvoicePastDueReportResponseData.getOrderNum());
					row.createCell(12).setCellValue(jnjGTInvoicePastDueReportResponseData.getCustomerPoNum());*/
				}
				final Row row = searchSheet.createRow(rowNum++);
				//row.createCell(0).setCellValue("");
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("");
				row.createCell(3).setCellValue("");
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue("");
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue("");
				row.createCell(9).setCellValue("");
				row.createCell(10).setCellValue("");
				row.createCell(11).setCellValue("");
				row.createCell(12).setCellValue("");
				//searchSheet.setAutoFilter(CellRangeAddress.valueOf("A18:J18"));
				
				searchSheet.addMergedRegion(new CellRangeAddress(15,15,0,6));
				searchSheet.addMergedRegion(new CellRangeAddress(9,9,2,4));
				for(int i=0;i<=13;i++){
					if(i==1){
						searchSheet.setColumnWidth(i,4500);
					}else{
						searchSheet.autoSizeColumn(i);
					}
				
				}
			}
			
		}
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath)
	{
		final CellStyle styleImgCell = hssfWorkbook.createCellStyle();
		styleImgCell.setAlignment(HorizontalAlignment.CENTER);
		//final Row header0 = sheet.createRow(0);
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
		sheet.addMergedRegion(new CellRangeAddress(0,4,0,9));
		//header0.getCell(0).setCellStyle(styleImgCell);
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
	}