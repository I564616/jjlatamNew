package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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

import com.jnj.b2b.jnjglobalreports.forms.JnjGlobalFinancialAnalysisReportForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTFinancePurchaseOrderReportResponseData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnJGTFnancialInvoiceReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTBackorderReportExcelView.class);
	private static final String FINANCIAL_RESPONSE_DATA_LIST = "jnjGTFinancialAnalysisOrderReportResponseDataMap";
	private static final String FINANCIAL_INVOICE_FORM_NAME = "JnjGlobalFinancialAnalysisReportForm";
	private static final String sheetName = "INVOICE REPORT RESULTS";
	
	private static final String HEADER = "invoice.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";
	
	private static final String ORDERTYPE_DESC = "reports.financial.orderType.desc"; 
	private static final String INV_NUM = "reports.financial.table.product.invoiceNumber"; 
	private static final String LINE_ITEM = "reports.financial.table.product.lineItem"; 
	private static final String BILLING_DATE = "invoiceDetailPage.billingDate"; 
	private static final String SALES_DOC_NUM = "reports.financial.table.product.salesDocumentNumber"; 
	private static final String CUST_PO_NUM = "reports.financial.table.product.customerPONumber"; 
	private static final String FRANCHISE_DESC = "reports.financial.table.product.franchiseDescription"; 
	private static final String PROD_CODE = "reports.financial.table.product.productCode"; 
	private static final String PROD_DESC = "reports.financial.productCode.desc"; 
	private static final String UOM = "reports.backorder.table.unitRpt"; 
	private static final String INV_QTY = "invoiceDetailPage.invoicedQuantity"; 
	private static final String REIMBURSEMENT_CODE = "invoiceDetailPage.reim.code"; 
	private static final String STATUS = "userSearch.status"; 
	private static final String RECP_NUM = "invoice.receipt.num"; 
	private static final String UNIT_PRICE = "gt.price.inquiry.pdf.unit.price"; 
	private static final String TOTAL_AMT = "reports.invoicedue.totalamount"; 
	private static final String CURRENCY = "reports.financial.currency"; 
	private static final String SOLD_TO_ACC = "reports.invoicedue.soldtoaccount"; 
	private static final String SOLD_TO_NAME = "reports.invoicedue.soldtoname"; 
	private static final String SHIP_TO_ACC = "backorder.reports.shiptoaccount"; 
	private static final String SHIP_TO_NAME = "reports.backorder.table.shipTo"; 
	private static final String END_USER = "cart.common.end.user"; 
	private static final String END_USER_NAME = "cart.common.end.user.name"; 
	private static final String STOCK_LOC_ACC = "stock.user"; 
	private static final String STOCK_LOC_ACC_NAME = "cart.common.stock.user.name"; 
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	
	/** I18NService to retrieve the current locale. */
	@Autowired
	private I18NService i18nService;
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

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
			 try{
				 String emptyField = new String();
			     emptyField="";
				arg3.setHeader("Content-Disposition", "attachment; filename=Invoice Report.xls");
	
				final JnjGlobalFinancialAnalysisReportForm formData = (JnjGlobalFinancialAnalysisReportForm) map
						.get(FINANCIAL_INVOICE_FORM_NAME);
				final TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData> dataMap = (TreeMap<String, JnjGTFinancePurchaseOrderReportResponseData>) map.get(FINANCIAL_RESPONSE_DATA_LIST);
				final String accountsSelectedName = (String) map.get("accountreportname");
	
				final Sheet searchSheet = hssfWorkbook.createSheet((messageSource.getMessage(sheetName, null, i18nService.getCurrentLocale())).toUpperCase());
				
				setHeaderImage(hssfWorkbook, searchSheet, (String) map.get("siteLogoPath"));
				final Row header0 = searchSheet.createRow(6);
				final Row header3 = searchSheet.createRow(9);
				final Row header4 = searchSheet.createRow(10);
				final Row header5 = searchSheet.createRow(11);
				final Font font = hssfWorkbook.createFont();
				font.setBold(true);
				
				final CellStyle style = hssfWorkbook.createCellStyle();
				style.setFont(font);
				
				final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
				
				final Date date = new Date();
				
				header0.createCell(0).setCellValue(messageSource.getMessage(HEADER, null, i18nService.getCurrentLocale()));
				header0.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
				
				header3.createCell(0).setCellValue(messageSource.getMessage(DOWNLOADDATE, null, i18nService.getCurrentLocale()));
				header3.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
				header3.createCell(1).setCellValue(dateFormat.format(date));
				header4.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
				header4.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
				header5.createCell(0).setCellValue(messageSource.getMessage(ACC_NAME, null, i18nService.getCurrentLocale()));
				header5.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
				
				header4.createCell(1).setCellValue((formData.getAccountIds() != null) ? formData.getAccountIds() : emptyField);
				header4.getCell(1).setCellStyle(style);
				
				
				header5.createCell(1).setCellValue((accountsSelectedName != null) ? accountsSelectedName : emptyField);
				
				
				//final HSSFSheet sheet = hssfWorkbook.createSheet("Invoice Report Results");
				
				final Row header = searchSheet.createRow(17);
				//setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
				
				/*header.createCell(0).setCellValue("Accounts");
				header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));		*/		
				header.createCell(0).setCellValue(messageSource.getMessage(ORDERTYPE_DESC, null, i18nService.getCurrentLocale()));
				header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(1).setCellValue(messageSource.getMessage(INV_NUM, null, i18nService.getCurrentLocale()));
				header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));	
				header.createCell(2).setCellValue(messageSource.getMessage(LINE_ITEM, null, i18nService.getCurrentLocale()));
				header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(3).setCellValue(messageSource.getMessage(BILLING_DATE, null, i18nService.getCurrentLocale()));
				header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(4).setCellValue(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()));
				header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(5).setCellValue(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()));
				header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));	
				header.createCell(6).setCellValue(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()));
				header.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));	
				header.createCell(7).setCellValue(messageSource.getMessage(PROD_CODE, null, i18nService.getCurrentLocale()));
				header.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(8).setCellValue(messageSource.getMessage(PROD_DESC, null, i18nService.getCurrentLocale()));
				header.getCell(8).setCellStyle(createLabelCell(hssfWorkbook));	
				header.createCell(9).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
				header.getCell(9).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(10).setCellValue(messageSource.getMessage(INV_QTY, null, i18nService.getCurrentLocale()));
				header.getCell(10).setCellStyle(createLabelCell(hssfWorkbook));				
				header.createCell(11).setCellValue(messageSource.getMessage(REIMBURSEMENT_CODE, null, i18nService.getCurrentLocale()));
				header.getCell(11).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(12).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
				header.getCell(12).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(13).setCellValue(messageSource.getMessage(RECP_NUM, null, i18nService.getCurrentLocale()));
				header.getCell(13).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(14).setCellValue(messageSource.getMessage(UNIT_PRICE, null, i18nService.getCurrentLocale()));
				header.getCell(14).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(15).setCellValue(messageSource.getMessage(TOTAL_AMT, null, i18nService.getCurrentLocale()));
				header.getCell(15).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(16).setCellValue(messageSource.getMessage(CURRENCY, null, i18nService.getCurrentLocale()));
				header.getCell(16).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(17).setCellValue(messageSource.getMessage(SOLD_TO_ACC, null, i18nService.getCurrentLocale()));
				header.getCell(17).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(18).setCellValue(messageSource.getMessage(SOLD_TO_NAME, null, i18nService.getCurrentLocale()));
				header.getCell(18).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(19).setCellValue(messageSource.getMessage(SHIP_TO_ACC, null, i18nService.getCurrentLocale()));
				header.getCell(19).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(20).setCellValue(messageSource.getMessage(SHIP_TO_NAME, null, i18nService.getCurrentLocale()));
				header.getCell(20).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(21).setCellValue(messageSource.getMessage(END_USER, null, i18nService.getCurrentLocale()));
				header.getCell(21).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(22).setCellValue(messageSource.getMessage(END_USER_NAME, null, i18nService.getCurrentLocale()));
				header.getCell(22).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(23).setCellValue(messageSource.getMessage(STOCK_LOC_ACC, null, i18nService.getCurrentLocale()));
				header.getCell(23).setCellStyle(createLabelCell(hssfWorkbook));
				header.createCell(24).setCellValue(messageSource.getMessage(STOCK_LOC_ACC_NAME, null, i18nService.getCurrentLocale()));
				header.getCell(24).setCellStyle(createLabelCell(hssfWorkbook));
				
				
			
				int rowNum = 18;
				if (null != dataMap)
				{
					Collection<JnjGTFinancePurchaseOrderReportResponseData> listJnjGTFinancePurchaseOrderReportResponseData= dataMap.values();
					for (final JnjGTFinancePurchaseOrderReportResponseData jnjGTFinancePurchaseOrderReportResponseData : listJnjGTFinancePurchaseOrderReportResponseData)
					{
						
						final Row row = searchSheet.createRow(rowNum++);
						row.createCell(0).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getOrderType());
						row.createCell(1).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getInvoiceNumber());
						row.createCell(2).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getLineItem());
						row.createCell(3).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getBillingDate());
						row.createCell(4).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getOrderNumber());
						row.createCell(5).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getCustomerPONumber());
						row.createCell(6).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getFranchiseDescription());
						row.createCell(7).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getProductCode());
						row.createCell(8).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getProductDescription());
						row.createCell(9).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getUom());
						row.createCell(10).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getInvoiceQty());
						row.createCell(11).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getReimbursementCode());
						row.createCell(12).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getStatus());
						row.createCell(13).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getReceiptNumber());
						row.createCell(14).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getUnitPrice());
						row.createCell(15).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getTotalPrice());
						row.createCell(16).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getCurrency());
						row.createCell(17).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getSoldToAccount());
						row.createCell(18).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getSoldToName());
						row.createCell(19).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getShiptoAccount());
						row.createCell(20).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getShipToName());
						row.createCell(21).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getEndUserAccount());
						row.createCell(22).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getEndUserName());
						row.createCell(23).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getStockLocationAccount());
						row.createCell(24).setCellValue(jnjGTFinancePurchaseOrderReportResponseData.getStockLocationName());
					}	
				}
				searchSheet.autoSizeColumn(0);
				searchSheet.autoSizeColumn(1);
				searchSheet.autoSizeColumn(2);
				searchSheet.autoSizeColumn(3);
				searchSheet.autoSizeColumn(4);
				searchSheet.autoSizeColumn(5);
				searchSheet.autoSizeColumn(6);
				searchSheet.autoSizeColumn(7);
				searchSheet.autoSizeColumn(8);
				searchSheet.autoSizeColumn(9);
				searchSheet.autoSizeColumn(10);
				searchSheet.autoSizeColumn(11);
				searchSheet.autoSizeColumn(12);
				searchSheet.autoSizeColumn(13);
				searchSheet.autoSizeColumn(14);
				searchSheet.autoSizeColumn(15);
				searchSheet.autoSizeColumn(16);
				searchSheet.autoSizeColumn(17);
				searchSheet.autoSizeColumn(18);
				searchSheet.autoSizeColumn(19);
				searchSheet.autoSizeColumn(20);
				searchSheet.autoSizeColumn(21);
				searchSheet.autoSizeColumn(22);
				searchSheet.autoSizeColumn(23);
				searchSheet.autoSizeColumn(24);
				
				
			 }catch (final Exception exception)
			{
				LOG.error("Error while creating excel - " + exception.getMessage());
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
	
	protected static CellStyle createValueCell(HSSFWorkbook workbook){

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