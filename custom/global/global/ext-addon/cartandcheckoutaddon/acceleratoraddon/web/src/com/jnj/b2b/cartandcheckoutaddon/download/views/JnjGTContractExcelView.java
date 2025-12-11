package com.jnj.b2b.cartandcheckoutaddon.download.views;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjContractData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTContractExcelView extends AbstractXlsView {
	
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
	
	protected static final Logger LOG = Logger.getLogger(JnjGTContractExcelView.class);
	
	private static final String CONTRACT_CONTRACTNUMBER = "contract.number.header";
	private static final String CONTRACT_TENDERNUMBER = "contract.tendernumber.header";
	private static final String CONTRACT_INDIRECTCUSTOMER = "contract.indirectcustomer.number";
	private static final String CONTRACT_EXPIRATIONDATE = "contract.expiration.date";
	private static final String CONTRACT_CONTRACTTYPE = "contract.type";
	public static final String CONTRACT_LIST_FILENAME = "MyContracts";
	//Modified by Archana for AAOL-5513
	private static final String DATE_FORMAT = "date.dateformat";
	public static final String CONTRACT_LIST_TITLE = "MyContract List";
	public static final String CONTRACT_DETAIL_FILENAME = "contract.detail.name";
	public static final String CONTRACT_DETAIL_TITLE = "contract.details.header";
	
	protected static final String sheetName = "MyContract Details";
	 
	//protected static final String sheetName = "MyContract List";
	//private static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook, final HttpServletRequest request,
									  final HttpServletResponse response) throws Exception	{
		final String fileName =  messageSource.getMessage(CONTRACT_LIST_FILENAME, null, i18nService.getCurrentLocale()) + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);
		//Modified by Archana for AAOL-5513
		final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		//final String currentSite = (String) arg0.get(Jnjb2bCoreConstants.SITE_NAME);

		final List<JnjContractData> results = (List<JnjContractData>) map.get("contractList");
		final Sheet sheet = workbook.createSheet(sheetName);
		int rowNumber = 6;

		final Row row = sheet.createRow(rowNumber++);

		final Font font = workbook.createFont();
		font.setBold(true);

		final CellStyle style = workbook.createCellStyle();
		style.setFont(font);
	 
		 String[] headers = new String[] {
				 
				 messageSource.getMessage(CONTRACT_CONTRACTNUMBER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_TENDERNUMBER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_INDIRECTCUSTOMER, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_EXPIRATIONDATE, null, i18nService.getCurrentLocale()),
				 messageSource.getMessage(CONTRACT_CONTRACTTYPE, null, i18nService.getCurrentLocale()),
				 
   		}; 
		 setHeaderImage(workbook, sheet, (String) map.get("siteLogoPath"),headers.length);
		 for (int i = 0; i < headers.length; i++) {
         String headerName = headers[i];
         row.createCell(i+1).setCellValue(headerName.toUpperCase());
         row.getCell(i+1).setCellStyle(createLabelCell(workbook));
		 } 
		 
		CellStyle css = createValueCell(workbook);
		int entryRowNumber = rowNumber++;
		final String emptyField = new String();
		if (results != null && !results.isEmpty()) {
			for (final JnjContractData data : results) {
				sheet.autoSizeColumn(entryRowNumber);
				final Row rowData = sheet.createRow(entryRowNumber++);
				Cell cell1 = rowData.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(data.getEccContractNum()));
				cell1.setCellStyle(css);
		      
		      Cell cell2 = rowData.createCell(2);
		      cell2.setCellValue(new HSSFRichTextString(data.getTenderNum()));
		      cell2.setCellStyle(css);
		      
		      Cell cell3 = rowData.createCell(3);
		      cell3.setCellValue(new HSSFRichTextString(data.getIndirectCustomer()));
		      cell3.setCellStyle(css);
		      
		      Cell cell4 = rowData.createCell(4);
		      cell4.setCellValue(new HSSFRichTextString((data.getEndDate() != null) ? dateFormat.format(data.getEndDate()).toString() : emptyField));
		      cell4.setCellStyle(css);
		      
		      Cell cell5 = rowData.createCell(5);
		      cell5.setCellValue(new HSSFRichTextString(data.getContractOrderReason()));
		      cell5.setCellStyle(css);
			}
			 
		// Auto-size the columns.
			 sheet.autoSizeColumn(1);
			 sheet.autoSizeColumn(2);
			 sheet.autoSizeColumn(3);
			 sheet.autoSizeColumn(4);
			 sheet.autoSizeColumn(5);
		}
	}
	
	/**
	 * @param hssfWorkbook
	 * @param sheet
	 * @param logoPath
	 */
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath, int colLen) {
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
		anchor.setCol2(colLen);
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
  