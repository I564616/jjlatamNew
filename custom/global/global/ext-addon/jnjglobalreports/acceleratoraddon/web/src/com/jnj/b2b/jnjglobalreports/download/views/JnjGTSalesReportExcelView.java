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

import com.jnj.b2b.jnjglobalreports.forms.JnjGTSalesReportAnalysisForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTSalesReportResponseData;
import com.jnj.facades.data.JnjGTSalesReportResponseOrderEntryData;



import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnjGTSalesReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTSalesReportExcelView.class);
	private static final String SALES_REPORT_ANALYSIS_RESPONSE_DATA_LIST = "jnjGTSalesReportResponseDataList";
	private static final String SALES_REPORT_ANALYSIS_FORM_NAME = "JnjGTSalesReportAnalysisForm";
	private static final String sheetName = "SALES REPORT RESULTS";
	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";
	private static final String HEADER = "sales.reports.header";
	private static final String DOWNLOADDATE = "backorder.reports.download.date";
	private static final String ACC_NUMBER = "backorder.reports.account.number";
	private static final String ACC_NAME = "backorder.reports.accountname";

	private static final String ORDER_TYPE = "reports.financial.label.product.orderType";
	private static final String SALES_DOC_NUM = "reports.financial.table.product.salesDocumentNumber";
	private static final String LINE_ITEM = "reports.financial.table.product.lineItem";
	private static final String ORDER_DATE = "reports.backorder.table.orderDate";
	private static final String STATUS = "reports.backorder.table.status";
	private static final String CUST_PO_NUM = "reports.order.analysis.customerPo.no";
	private static final String FRANCHISE_DESC = "reports.order.analysis.franchise.description";
	private static final String PRODUCT_CODE = "reports.order.analysis.productcode";
	private static final String PRODUCT_DESC = "reports.financial.productCode.desc";
	private static final String REIN_CODE = "invoiceDetailPage.reim.code";
	private static final String ORDER_QTY = "reports.order.analysis.order.quantity";
	private static final String BACK_QTY = "sales.report.line.backorderqty";
	private static final String UOM = "backorder.reports.unit";
	private static final String DELIVERY_QTY = "reports.order.analysis.delivery.qty";
	private static final String BILLED_QTY = "reports.order.analysis.billed.qty";
	private static final String UNIT_PRICE = "gt.price.inquiry.pdf.unit.price";
	private static final String TOTAL_AMOUNT = "reports.invoicedue.totalamount";
	private static final String CURRENCY = "reports.financial.currency";
	private static final String DELIVERY_BLOCK = "reports.order.analysis.order.delivery.block";
	private static final String BILLING_BLOCK = "reports.order.analysis.order.billing.block";
	private static final String CREDIT_BLOCK = "reports.order.analysis.order.credit.block";
	private static final String SOLD_TO_ACC = "reports.invoicedue.soldtoaccount";
	private static final String SOLD_TO_NAME = "reports.invoicedue.soldtoname";
	private static final String SHIP_TO_ACC = "cart.deliver.shipToAcc";
	private static final String SHIP_TO_NAME = "reports.backorder.table.shipTo";
	private static final String END_USER = "cart.common.end.user";
	private static final String END_USER_NAME = "cart.common.end.user.name";
	private static final String STOCK_ACC = "cart.common.stock";
	private static final String STOCK_NAME = "cart.common.stock.user.name";
	private static final String INV_NUM = "reports.financial.table.product.invoiceNumber";
	private static final String DEL_NUM = "reports.order.analysis.delivery.No";
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
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		 
			arg3.setHeader("Content-Disposition", "attachment; filename=SalesReport.xls");
			final String emptyField = new String();
			final List<JnjGTSalesReportResponseData> JnjGTSalesReportResponseDataList = (List<JnjGTSalesReportResponseData>) arg0.get(SALES_REPORT_ANALYSIS_RESPONSE_DATA_LIST);
			final JnjGTSalesReportAnalysisForm searchCriteria = (JnjGTSalesReportAnalysisForm) arg0.get(SALES_REPORT_ANALYSIS_FORM_NAME);

			final String accountsSelectedName = (String) arg0.get("accountreportname");
			final Sheet searchSheet = arg1.createSheet(sheetName);
						
			final Row searchHeader = searchSheet.createRow(6);//TODO - 6 row??
			setHeaderImage(arg1, searchSheet, (String) arg0.get("siteLogoPath"));
			final Row header0 = searchSheet.createRow(6);
			final Row header3 = searchSheet.createRow(9);
			final Row header4 = searchSheet.createRow(10);
			final Row header5 = searchSheet.createRow(11);
			final Row header = searchSheet.createRow(17);
			
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
			header.createCell(0).setCellValue(messageSource.getMessage(ORDER_TYPE, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(arg1));
			
			header.createCell(1).setCellValue(messageSource.getMessage(SALES_DOC_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));

			header.createCell(2).setCellValue(messageSource.getMessage(LINE_ITEM, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));
			
			header.createCell(3).setCellValue(messageSource.getMessage(ORDER_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));
			
			header.createCell(4).setCellValue(messageSource.getMessage(STATUS, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));
			
			header.createCell(5).setCellValue(messageSource.getMessage(CUST_PO_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));

			header.createCell(6).setCellValue(messageSource.getMessage(FRANCHISE_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));

			header.createCell(7).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));
			
			header.createCell(8).setCellValue(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));
			
			header.createCell(9).setCellValue(messageSource.getMessage(REIN_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(arg1));

			header.createCell(10).setCellValue(messageSource.getMessage(ORDER_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(arg1));
			
			header.createCell(11).setCellValue(messageSource.getMessage(BACK_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(arg1));
			
			header.createCell(12).setCellValue(messageSource.getMessage(UOM, null, i18nService.getCurrentLocale()));
			header.getCell(12).setCellStyle(createLabelCell(arg1));
			
			header.createCell(13).setCellValue(messageSource.getMessage(DELIVERY_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(13).setCellStyle(createLabelCell(arg1));
			
			header.createCell(14).setCellValue(messageSource.getMessage(BILLED_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(14).setCellStyle(createLabelCell(arg1));

			header.createCell(15).setCellValue(messageSource.getMessage(UNIT_PRICE, null, i18nService.getCurrentLocale()));
			header.getCell(15).setCellStyle(createLabelCell(arg1));
			
			header.createCell(16).setCellValue(messageSource.getMessage(TOTAL_AMOUNT, null, i18nService.getCurrentLocale()));
			header.getCell(16).setCellStyle(createLabelCell(arg1));
			
			header.createCell(17).setCellValue(messageSource.getMessage(CURRENCY, null, i18nService.getCurrentLocale()));
			header.getCell(17).setCellStyle(createLabelCell(arg1));

			header.createCell(18).setCellValue(messageSource.getMessage(DELIVERY_BLOCK, null, i18nService.getCurrentLocale()));
			header.getCell(18).setCellStyle(createLabelCell(arg1));
			
			header.createCell(19).setCellValue(messageSource.getMessage(BILLING_BLOCK, null, i18nService.getCurrentLocale()));
			header.getCell(19).setCellStyle(createLabelCell(arg1));

			header.createCell(20).setCellValue(messageSource.getMessage(CREDIT_BLOCK, null, i18nService.getCurrentLocale()));
			header.getCell(20).setCellStyle(createLabelCell(arg1));
			
			header.createCell(21).setCellValue(messageSource.getMessage(SOLD_TO_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(21).setCellStyle(createLabelCell(arg1));
			
			header.createCell(22).setCellValue(messageSource.getMessage(SOLD_TO_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(22).setCellStyle(createLabelCell(arg1));

			header.createCell(23).setCellValue(messageSource.getMessage(SHIP_TO_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(23).setCellStyle(createLabelCell(arg1));
			
			header.createCell(24).setCellValue(messageSource.getMessage(SHIP_TO_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(24).setCellStyle(createLabelCell(arg1));

			header.createCell(25).setCellValue(messageSource.getMessage(END_USER, null, i18nService.getCurrentLocale()));
			header.getCell(25).setCellStyle(createLabelCell(arg1));
			
			header.createCell(26).setCellValue(messageSource.getMessage(END_USER_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(26).setCellStyle(createLabelCell(arg1));
			
			header.createCell(27).setCellValue(messageSource.getMessage(STOCK_ACC, null, i18nService.getCurrentLocale()));
			header.getCell(27).setCellStyle(createLabelCell(arg1));
			
			header.createCell(28).setCellValue(messageSource.getMessage(STOCK_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(28).setCellStyle(createLabelCell(arg1));
			
			header.createCell(29).setCellValue(messageSource.getMessage(INV_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(29).setCellStyle(createLabelCell(arg1));

			header.createCell(30).setCellValue(messageSource.getMessage(DEL_NUM, null, i18nService.getCurrentLocale()));
			header.getCell(30).setCellStyle(createLabelCell(arg1));
			
		
		
			int rowNum = 18;
			if (null != JnjGTSalesReportResponseDataList)
			{
				double totals = 0;
				for (final JnjGTSalesReportResponseData jnjGTSalesReportResponseData : JnjGTSalesReportResponseDataList)
				{
					searchSheet.autoSizeColumn(rowNum);
					for(JnjGTSalesReportResponseOrderEntryData orderEntry:jnjGTSalesReportResponseData.getOrderEntryList()){
						final Row row = searchSheet.createRow(rowNum++);
						row.setRowStyle(styleCell);
						row.createCell(0).setCellValue(jnjGTSalesReportResponseData.getOrderType());
						row.createCell(1).setCellValue(jnjGTSalesReportResponseData.getSalesDocNo());
						row.createCell(2).setCellValue(orderEntry.getLineItem());
						row.createCell(3).setCellValue(jnjGTSalesReportResponseData.getOrderDate());
						row.createCell(4).setCellValue(jnjGTSalesReportResponseData.getStatus());
						row.createCell(5).setCellValue(jnjGTSalesReportResponseData.getCustomerPONo());
						row.createCell(6).setCellValue(jnjGTSalesReportResponseData.getFranchiseDescription());
						row.createCell(7).setCellValue(orderEntry.getProductCode());
						row.createCell(8).setCellValue(orderEntry.getDescription());
						row.createCell(9).setCellValue(jnjGTSalesReportResponseData.getReimbursementCode());
						row.createCell(10).setCellValue(orderEntry.getQuantity());
						row.createCell(11).setCellValue(jnjGTSalesReportResponseData.getBackOrderQty());
						row.createCell(12).setCellValue(orderEntry.getUOM());
						row.createCell(13).setCellValue(jnjGTSalesReportResponseData.getDeliveryQty());
						row.createCell(14).setCellValue(jnjGTSalesReportResponseData.getBillingQty());
						row.createCell(15).setCellValue(orderEntry.getUnitPrice());
						row.createCell(16).setCellValue((Double.valueOf(orderEntry.getUnitPrice()) * Long.valueOf(orderEntry.getQuantity())));
						row.createCell(17).setCellValue(jnjGTSalesReportResponseData.getCurr());
						//row.createCell(18).setCellValue(jnjGTSalesReportResponseData.getInvoicedQty());
						row.createCell(18).setCellValue(jnjGTSalesReportResponseData.getDeliveryBlock());
						row.createCell(19).setCellValue(jnjGTSalesReportResponseData.getBillingBlock());
						row.createCell(20).setCellValue(jnjGTSalesReportResponseData.getCreditBlock());
						row.createCell(21).setCellValue(jnjGTSalesReportResponseData.getSoldToAccount());
						row.createCell(22).setCellValue(jnjGTSalesReportResponseData.getSoldToName());
						row.createCell(23).setCellValue(jnjGTSalesReportResponseData.getShiptoAccount());
						row.createCell(24).setCellValue(jnjGTSalesReportResponseData.getShipToName());
						row.createCell(25).setCellValue(jnjGTSalesReportResponseData.getEndUserAccount());
						row.createCell(26).setCellValue(jnjGTSalesReportResponseData.getEndUserName());
						row.createCell(27).setCellValue(jnjGTSalesReportResponseData.getStockLocationAccount());
						row.createCell(28).setCellValue(jnjGTSalesReportResponseData.getStockLocationName());
						row.createCell(29).setCellValue(jnjGTSalesReportResponseData.getInvoiceNumber());
						row.createCell(30).setCellValue(jnjGTSalesReportResponseData.getDeliveryNumber());
						
					}
					
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
				row.createCell(13).setCellValue("");
				row.createCell(14).setCellValue("");
				row.createCell(15).setCellValue("");
				row.createCell(16).setCellValue("");
				row.createCell(17).setCellValue("");
				row.createCell(18).setCellValue("");
				row.createCell(19).setCellValue("");
				row.createCell(20).setCellValue("");
				row.createCell(21).setCellValue("");
				row.createCell(22).setCellValue("");
				row.createCell(23).setCellValue("");
				row.createCell(24).setCellValue("");
				row.createCell(25).setCellValue("");
				row.createCell(26).setCellValue("");
				row.createCell(27).setCellValue("");
				row.createCell(28).setCellValue("");
				row.createCell(29).setCellValue("");
				row.createCell(30).setCellValue("");
				
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