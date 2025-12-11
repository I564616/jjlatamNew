package com.jnj.b2b.cartandcheckoutaddon.download.views;

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
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractPriceData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTContractDetailExcelView extends AbstractXlsView {
	
		@Autowired
		protected I18NService i18nService;
		private MessageSource messageSource;
		
		@Autowired
		protected JnJCommonUtil jnjCommonUtil;
		
		public JnJCommonUtil getJnjCommonUtil() {
			return jnjCommonUtil;
		}

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
		
	protected static final Logger LOG = Logger.getLogger(JnjGTContractDetailExcelView.class);
	
	private static final String CONTRACT_MYCONTRACTS = "contract.mycontracts";
	private static final String CONTRACT_CONTRACTNUMBER = "contract.number.header";
	private static final String CONTRACT_TENDERNUMBER = "contract.tendernumber.header";
	private static final String CONTRACT_INDIRECTCUSTOMER = "contract.indirectcustomer.number";
	private static final String CONTRACT_INDIRECTCUSTOMER_NAME = "contract.indirectcustomer.name";
	private static final String CONTRACT_EXPIRATIONDATE = "contract.expiration.date";
	private static final String CONTRACT_CONTRACTTYPE = "contract.type";
		
	private static final String CONTRACT_STATUS = "contract.status";
	private static final String CONTRACT_CREATION_DATE = "contract.creation.date";
	private static final String CONTRACT_LAST_TIME_UPDATED = "contract.last.updated";
	private static final String CONTRACT_PRODUCT_NAME = "contract.product.name";
	private static final String CONTRACT_UNIT_PRICE = "contract.unit.price";
		
	private static final String CONTRACT_BAL = "contract.balance";
	private static final String CONTRACT_CONSUMED = "contract.consumed";
	private static final String CONTRACT_QTY = "contract.quantity";
	private static final String CONTRACT_UNIT_MEASURE = "contract.unit.measurement";
	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";
	private static final String CONTRACT_TOTAL_CONTRACT_VALUE = "contract.total.value";
	private static final String CONTRACT_CONSUMED_VALUE = "contract.consumed.value";
	private static final String CONTRACT_BALANCE = "contract.balance";
	private static final String CONTRACT_ZCQ = "ZCQ";
	private static final String CONTRACT_ZCV = "ZCV";
	public static final String CONTRACT_LIST_TITLE = "MyContract List";
	public static final String CONTRACT_DETAIL_FILENAME = "contract.detail.name";
	public static final String CONTRACT_DETAIL_TITLE = "contract.details.header";
	 
	protected static final String sheetName = "MyContract Details";
	//protected static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook, final HttpServletRequest request,
									  final HttpServletResponse response) throws Exception	{
		final String fileName = CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_DETAIL_FILENAME + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		//Modified by Archana for AAOL-5513
        final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat dateFormatHHMMSS = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());

		final JnjContractData contractData = (JnjContractData) map.get("contractData");
		final List<JnjContractEntryData> jnjContractEntryDataList = (List<JnjContractEntryData>) map.get("contractEntryList");
		final Map<String, JnjContractPriceData> cntrctEntryMap = (Map<String, JnjContractPriceData>) map.get("cntrctEntryMap");
		final JnjContractPriceData  cntrctPriceData = (JnjContractPriceData) map.get("cntrctPriceData");
		
		final Sheet sheet = workbook.createSheet(sheetName);
		setHeaderImage(workbook, sheet, (String) map.get("siteLogoPath"));
		int rowNumber = 6;

		final Row headerPart1 = sheet.createRow((short) rowNumber++); //1-5 is image part and 6 is header part
		final Row rowPart1 = sheet.createRow(rowNumber++);
		final Row rowPart2 = sheet.createRow(rowNumber++);
		final Row rowPart3 = sheet.createRow(rowNumber++);
		final Row rowPart4 = sheet.createRow(rowNumber++);
		final Row rowPart5 = sheet.createRow(rowNumber++);
		final Row rowPart6 = sheet.createRow(rowNumber++);
		final Row rowPart7 = sheet.createRow(rowNumber++);
		final Row header = sheet.createRow(rowNumber++);

		final Font font = workbook.createFont();
		font.setBold(true);

		final CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		headerPart1.createCell(1).setCellValue(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_DETAIL_TITLE); 
		headerPart1.getCell(1).setCellStyle(style);
		
		 String[] headers_CONTRACT_ZCQ = new String[] {
				 messageSource.getMessage(CONTRACT_PRODUCT_NAME, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_UNIT_PRICE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_UNIT_MEASURE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_QTY, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_CONSUMED, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_BAL, null, i18nService.getCurrentLocale()),
		 };
        		
        
        String[] headers_CONTRACT_ZCV = new String[] {
        		messageSource.getMessage(CONTRACT_PRODUCT_NAME, null, i18nService.getCurrentLocale()),
        		messageSource.getMessage(CONTRACT_UNIT_PRICE, null, i18nService.getCurrentLocale()),
        		messageSource.getMessage(CONTRACT_TOTAL_CONTRACT_VALUE, null, i18nService.getCurrentLocale()),
        		messageSource.getMessage(CONTRACT_CONSUMED_VALUE, null, i18nService.getCurrentLocale()),
        		messageSource.getMessage(CONTRACT_BALANCE, null, i18nService.getCurrentLocale()),
        		
        		 }; 
		
		if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCQ, null, i18nService.getCurrentLocale()))){
			for (int i = 0; i < headers_CONTRACT_ZCQ.length; i++) {
	         String headerName = headers_CONTRACT_ZCQ[i];
	         header.createCell(i+1).setCellValue(headerName.toUpperCase());
	         header.getCell(i+1).setCellStyle(createLabelCell(workbook));
			 } 
		}
		
		if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
			for (int i = 0; i < headers_CONTRACT_ZCV.length; i++) {
	         String headerName = headers_CONTRACT_ZCV[i];
	         header.createCell(i+1).setCellValue(headerName.toUpperCase());
	         header.getCell(i+1).setCellStyle(createLabelCell(workbook));
			 } 
		}
		 CellStyle css = createValueCell(workbook);
		int entryRowNumber = rowNumber++;
		final String emptyField = new String();
		if (jnjContractEntryDataList != null && !jnjContractEntryDataList.isEmpty()) {
			for (final JnjContractEntryData jnjContractEntryData : jnjContractEntryDataList) {
				sheet.autoSizeColumn(entryRowNumber);
				final Row rowData = sheet.createRow(entryRowNumber++);
				Cell cell1 = rowData.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(jnjContractEntryData.getProductCode() +"\n"+jnjContractEntryData.getProductName()));
				cell1.setCellStyle(css);
				
				Cell cell2 = rowData.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractProductPrice()));
				cell2.setCellStyle(css);
				
				if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCQ, null, i18nService.getCurrentLocale()))){
					Cell cell3 = rowData.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(jnjContractEntryData.getProdSalesUnit()));
					cell3.setCellStyle(css);
					
					Cell cell4 = rowData.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractQty().toString()));
					cell4.setCellStyle(css);
					
					Cell cell5 = rowData.createCell(5);
					cell5.setCellValue(new HSSFRichTextString(jnjContractEntryData.getConsumedQty().toString()));
					cell5.setCellStyle(css);
					
					Cell cell6 = rowData.createCell(6);
					cell6.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractBalanceQty().toString()));
					cell6.setCellStyle(css);
				}
				
				if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
					String prodCode = jnjContractEntryData.getProductCode();
					JnjContractPriceData jnjContractPriceData =  cntrctEntryMap.get(prodCode);
					if(jnjContractPriceData != null){
   					
						Cell cell3 = rowData.createCell(3);
						cell3.setCellValue(new HSSFRichTextString(jnjContractPriceData.getTotalAmount().getValue()+""));
						cell3.setCellStyle(css);
						
						Cell cell4 = rowData.createCell(4);
						cell4.setCellValue(new HSSFRichTextString(jnjContractPriceData.getConsumedAmount().getValue()+""));
						cell4.setCellStyle(css);
						
						Cell cell5 = rowData.createCell(5);
						cell5.setCellValue(new HSSFRichTextString(jnjContractPriceData.getBalanceAmount().getValue()+""));
						cell5.setCellStyle(css);
					 }
				}
			}
			 sheet.autoSizeColumn(1);
			 sheet.autoSizeColumn(2);
			 sheet.autoSizeColumn(3);
			 sheet.autoSizeColumn(4);
			 sheet.autoSizeColumn(5);
			 sheet.autoSizeColumn(6);
			 sheet.autoSizeColumn(7);
		}
		
		///start
		Cell cell11 = rowPart1.createCell(1);
		cell11.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_CONTRACTNUMBER, null, i18nService.getCurrentLocale())));
		cell11.setCellStyle(createLabelCell(workbook));
		
		Cell cell12 = rowPart1.createCell(2);
		cell12.setCellValue(new HSSFRichTextString(contractData.getEccContractNum()));
		cell12.setCellStyle(css);
		
		Cell cell13 = rowPart1.createCell(3);
		cell13.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_STATUS, null, i18nService.getCurrentLocale())));
		cell13.setCellStyle(createLabelCell(workbook));
		
		Cell cell14 = rowPart1.createCell(4);
		cell14.setCellValue(new HSSFRichTextString(contractData.getStatus() != null ? contractData.getStatus() : emptyField));
		cell14.setCellStyle(css);
		
		Cell cell15 = rowPart1.createCell(5);
		cell15.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_CREATION_DATE, null, i18nService.getCurrentLocale())));
		cell15.setCellStyle(createLabelCell(workbook));
		
		Cell cell16 = rowPart1.createCell(6);
		cell16.setCellValue(new HSSFRichTextString(contractData.getStartDate() != null ? dateFormat.format(contractData.getStartDate()).toString() :  emptyField));
		cell16.setCellStyle(css);
		
      /////
		Cell cell21 = rowPart2.createCell(1);
		cell21.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER, null, i18nService.getCurrentLocale())));
		cell21.setCellStyle(createLabelCell(workbook));
		
		Cell cell22 = rowPart2.createCell(2);
		cell22.setCellValue(new HSSFRichTextString(contractData.getIndirectCustomer() != null ? contractData.getIndirectCustomer() : emptyField));
		cell22.setCellStyle(css);
		
		Cell cell23 = rowPart2.createCell(3);
		cell23.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_TENDERNUMBER, null, i18nService.getCurrentLocale())));
		cell23.setCellStyle(createLabelCell(workbook));
		
		Cell cell24 = rowPart2.createCell(4);
		cell24.setCellValue(new HSSFRichTextString(contractData.getTenderNum() != null ? contractData.getTenderNum() : emptyField));
		cell24.setCellStyle(css);
		
		Cell cell25 = rowPart2.createCell(5);
		cell25.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_EXPIRATIONDATE, null, i18nService.getCurrentLocale())));
		cell25.setCellStyle(createLabelCell(workbook));
		
		Cell cell26 = rowPart2.createCell(6);
		cell26.setCellValue(new HSSFRichTextString(contractData.getEndDate() != null ? dateFormat.format(contractData.getEndDate()).toString() :  emptyField));
		cell26.setCellStyle(css);
		 
      /////
		Cell cell31 = rowPart3.createCell(1);
		cell31.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER_NAME, null, i18nService.getCurrentLocale())));
		cell31.setCellStyle(createLabelCell(workbook));
		
		Cell cell32 = rowPart3.createCell(2);
		cell32.setCellValue(new HSSFRichTextString(contractData.getIndirectCustomerName() != null ? contractData.getIndirectCustomerName() : emptyField));
		cell32.setCellStyle(css);
      
		Cell cell33 = rowPart3.createCell(3);
		cell33.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_CONTRACTTYPE, null, i18nService.getCurrentLocale())));
		cell33.setCellStyle(createLabelCell(workbook));
		
		Cell cell34 = rowPart3.createCell(4);
		cell34.setCellValue(new HSSFRichTextString(contractData.getContractOrderReason() != null ? contractData.getContractOrderReason() : emptyField));
		cell34.setCellStyle(css);
      
		Cell cell35 = rowPart3.createCell(5);
		cell35.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_LAST_TIME_UPDATED, null, i18nService.getCurrentLocale())));
		cell35.setCellStyle(createLabelCell(workbook));
		
		Cell cell36 = rowPart3.createCell(6);
		cell36.setCellValue(new HSSFRichTextString(contractData.getLastUpdatedTime() != null ? dateFormatHHMMSS.format(contractData.getLastUpdatedTime()).toString() :  emptyField));
		cell36.setCellStyle(css);
		 
      ////
		rowPart4.createCell(1).setCellValue(emptyField);
		rowPart4.createCell(2).setCellValue(emptyField);
		rowPart4.createCell(3).setCellValue(emptyField);
		rowPart4.createCell(4).setCellValue(emptyField);
   
      if(contractData.getDocumentType().equalsIgnoreCase(messageSource.getMessage(CONTRACT_ZCV, null, i18nService.getCurrentLocale()))){
      	if (cntrctPriceData != null) {
      		rowPart5.createCell(1).setCellValue(emptyField);
      		rowPart5.createCell(2).setCellValue(emptyField);
      		rowPart5.createCell(3).setCellValue(emptyField);
      		rowPart5.createCell(4).setCellValue(emptyField);
      		
      		Cell cell45 = rowPart4.createCell(5);
      		cell45.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_TOTAL_CONTRACT_VALUE, null, i18nService.getCurrentLocale())));
      		cell45.setCellStyle(createLabelCell(workbook));
      		
      		Cell cell46 = rowPart4.createCell(6);
      		cell46.setCellValue(new HSSFRichTextString(cntrctPriceData.getTotalAmount().getValue() != null ? cntrctPriceData.getTotalAmount().getValue().toString() : emptyField));
      		cell46.setCellStyle(css);
      		
      		Cell cell55 = rowPart5.createCell(5);
      		cell55.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_CONSUMED_VALUE, null, i18nService.getCurrentLocale())));
      		cell55.setCellStyle(createLabelCell(workbook));
      		
      		Cell cell56 = rowPart5.createCell(6);
      		cell56.setCellValue(new HSSFRichTextString(cntrctPriceData.getConsumedAmount().getValue() != null ? cntrctPriceData.getConsumedAmount().getValue().toString() : emptyField));
      		cell56.setCellStyle(css);
            ////
      		rowPart6.createCell(1).setCellValue(emptyField);
      		rowPart6.createCell(2).setCellValue(emptyField);
      		rowPart6.createCell(3).setCellValue(emptyField);
      		rowPart6.createCell(4).setCellValue(emptyField);
      		
      		Cell cell65 = rowPart6.createCell(5);
      		cell65.setCellValue(new HSSFRichTextString(messageSource.getMessage(CONTRACT_BALANCE, null, i18nService.getCurrentLocale())));
      		cell65.setCellStyle(createLabelCell(workbook));
      		
      		Cell cell66 = rowPart6.createCell(6);
      		cell66.setCellValue(new HSSFRichTextString(cntrctPriceData.getBalanceAmount().getValue() != null ? cntrctPriceData.getBalanceAmount().getValue().toString() : emptyField));
      		cell66.setCellStyle(css);
             ////
      		rowPart7.createCell(1).setCellValue(emptyField);
      		rowPart7.createCell(2).setCellValue(emptyField);
      		rowPart7.createCell(3).setCellValue(emptyField);
      		rowPart7.createCell(4).setCellValue(emptyField); 
      		rowPart7.createCell(5).setCellValue(emptyField); 
      		rowPart7.createCell(6).setCellValue(emptyField); 
      		}
      	}
		///end
		
	}
	
	/**
	 * @param hssfWorkbook
	 * @param sheet
	 * @param logoPath
	 */
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath) {
		InputStream inputStream = null;
		int index = 0;
		try {
			inputStream = new FileInputStream(logoPath);
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
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
	}

	protected static CellStyle  createLabelCell(Workbook workbook){
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

	protected static CellStyle  createValueCell(Workbook workbook){
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
  