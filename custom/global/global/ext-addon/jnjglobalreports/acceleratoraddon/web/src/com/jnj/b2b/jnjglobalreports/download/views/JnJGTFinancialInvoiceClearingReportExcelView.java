package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.hybris.platform.servicelayer.i18n.I18NService;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;

import com.jnj.b2b.jnjglobalreports.forms.JnjGTBackorderReportForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceClearingForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGTInvoiceDueReportDueForm;
import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTBackorderReportResponseData;
import com.jnj.facades.data.JnjGTInvoiceClearingReportResponseData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import com.jnj.facades.data.JnjGTInvoicePastDueReportResponseData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnJGTFinancialInvoiceClearingReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTBackorderReportExcelView.class);
	private static final String FINANCIAL_RESPONSE_DATA_LIST = "invoiceClearingResponse";
	private static final String FINANCIAL_INVOICE_DUE_FORM_NAME = "jnjGTInvoiceClearingForm";
	private static final String sheetName = "INVOICE CLEARING REPORT RESULTS";
	private static final String HEADER = "invoice.clearing.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	
	private static final String CUST_PO_NUM = "reports.financial.table.product.customerPoNo"; 
	private static final String SALES_DOC_NUM = "reports.financial.table.product.salesDocumentNumber";
	private static final String RECEIPT_NUM = "invoice.receipt.num";
	private static final String INV_NUM = "invoiceDetailPage.invoiceNumber";
	private static final String INV_DATE = "reports.invoicedue.invoicedate";
	private static final String SOLD_TO_ACC = "reports.invoicedue.soldtoaccount";
	private static final String SOLD_TO_NAME = "reports.invoicedue.soldtoname";
	private static final String PAYMNET_DATE = "reports.financial.paymentdate";
	private static final String INV_STATUS = "reports.financial.invoicestatus";
	private static final String CURRENCY = "reports.financial.currency";
	private static final String TOTAL_AMT = "reports.invoicedue.totalamount";
	private static final String PAID_AMT = "reports.financial.paidAmount";
	
	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";

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
	}//Modified by Archana for AAOL-5513
	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		 
			arg3.setHeader("Content-Disposition", "attachment; filename=Invoice_Clearing.xls");
			String emptyField = new String();
		    emptyField="";
			final List<JnjGTInvoiceClearingReportResponseData> jnjGTInvoiceClearingReportResponseDataList = (List<JnjGTInvoiceClearingReportResponseData>) arg0
					.get(FINANCIAL_RESPONSE_DATA_LIST);
			final JnjGTInvoiceClearingForm searchCriteria = (JnjGTInvoiceClearingForm) arg0.get(FINANCIAL_INVOICE_DUE_FORM_NAME);

			final String accountsSelectedName = (String) arg0.get("accountreportname");
			final Sheet searchSheet = arg1.createSheet(sheetName);
			setHeaderImage(arg1, searchSheet, (String) arg0.get("siteLogoPath"));
			final Row header0 = searchSheet.createRow(6);
			final Row header3 = searchSheet.createRow(9);
			final Row header4 = searchSheet.createRow(10);
			final Row header5 = searchSheet.createRow(11);
			
			final Font font = arg1.createFont();
			font.setBold(true);
			
			final CellStyle style = arg1.createCellStyle();
			style.setFont(font);
			
			final CellStyle styleCell = arg1.createCellStyle();
			styleCell.setWrapText(true); 
			final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final Date date = new Date();
			
			header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
			header0.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
			header3.getCell(0).setCellStyle(createLabelCell(arg1));
			header3.createCell(1).setCellValue(dateFormat.format(date));
			header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header4.getCell(0).setCellStyle(createLabelCell(arg1));
			header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
			header5.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header4.createCell(1).setCellValue((searchCriteria.getAccountIds() != null) ? searchCriteria.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);
			
			
			header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);
			
			/*final SimpleDateFormat dateFormat = new SimpleDateFormat();
			dateFormat.applyPattern("dd/MM/yyyy; HH:mm zzz");
			final Date date = new Date();


			header3.createCell(0).setCellValue("Download Date:");
			header3.getCell(0).setCellStyle(createLabelCell(arg1));

			header3.createCell(1).setCellValue(dateFormat.format(date));

			header4.createCell(0).setCellValue("Account Number:");
			header4.getCell(0).setCellStyle(createLabelCell(arg1));
			
			
			header4.createCell(1).setCellValue("00080888");
			header4.createCell(1).setCellValue((searchCriteria.getAccountIds() != null) ? searchCriteria.getAccountIds() : emptyField);
			header4.getCell(1).setCellStyle(style);*/
			//Modified by Archana for AAOL-5513
			final SimpleDateFormat dateFormat1 = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			final SimpleDateFormat dateFormat2 = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
			final Row header = searchSheet.createRow(17);
			
			header.createCell(0).setCellValue(messageSource.getMessage(RECEIPT_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header.createCell(1).setCellValue(messageSource.getMessage(PAYMNET_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));
			
			header.createCell(2).setCellValue(messageSource.getMessage(INV_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));
			
			header.createCell(3).setCellValue(messageSource.getMessage(INV_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));

			header.createCell(4).setCellValue(messageSource.getMessage(SOLD_TO_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));
			
			header.createCell(5).setCellValue(messageSource.getMessage(SOLD_TO_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));
			
			header.createCell(6).setCellValue(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));
			
			header.createCell(7).setCellValue(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));
			
			header.createCell(8).setCellValue(messageSource.getMessage(INV_STATUS, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));
			
			header.createCell(9).setCellValue(messageSource.getMessage(CURRENCY, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(arg1));
			
			header.createCell(10).setCellValue(messageSource.getMessage(TOTAL_AMT, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(arg1));
			
			header.createCell(11).setCellValue(messageSource.getMessage(PAID_AMT, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(arg1));

			int rowNum = 18;
			if (null != jnjGTInvoiceClearingReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTInvoiceClearingReportResponseData jnjGTInvoiceClearingReportResponseData : jnjGTInvoiceClearingReportResponseDataList)
				{
					searchSheet.autoSizeColumn(rowNum);
					final Row row = searchSheet.createRow(rowNum++);
					row.setRowStyle(styleCell);
					row.createCell(0).setCellValue(jnjGTInvoiceClearingReportResponseData.getReceiptNumber());
					row.createCell(1).setCellValue(jnjGTInvoiceClearingReportResponseData.getPaymentDate());
					row.createCell(2).setCellValue(jnjGTInvoiceClearingReportResponseData.getInvoiceNum());
					row.createCell(3).setCellValue(jnjGTInvoiceClearingReportResponseData.getBillingDate());
					row.createCell(4).setCellValue(jnjGTInvoiceClearingReportResponseData.getSoldToAccNum());
					row.createCell(5).setCellValue(jnjGTInvoiceClearingReportResponseData.getSoldToAccName());
					row.createCell(6).setCellValue(jnjGTInvoiceClearingReportResponseData.getOrderNum());
					row.createCell(7).setCellValue(jnjGTInvoiceClearingReportResponseData.getCustomerPoNum());
					row.createCell(8).setCellValue(jnjGTInvoiceClearingReportResponseData.getStatus());
					row.createCell(9).setCellValue(jnjGTInvoiceClearingReportResponseData.getCurrency());
					row.createCell(10).setCellValue(jnjGTInvoiceClearingReportResponseData.getInvoiceTotalAmount());
					row.createCell(11).setCellValue(jnjGTInvoiceClearingReportResponseData.getOpenAmount());
				}
				final Row row = searchSheet.createRow(rowNum++);
				//row.createCell(0).setCellValue("");
				/*row.createCell(1).setCellValue("");
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
				row.createCell(12).setCellValue("");*/
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