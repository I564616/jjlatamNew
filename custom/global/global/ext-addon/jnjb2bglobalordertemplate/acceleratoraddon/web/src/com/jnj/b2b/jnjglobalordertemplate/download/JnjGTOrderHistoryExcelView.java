package com.jnj.b2b.jnjglobalordertemplate.download;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;



import com.jnj.b2b.jnjglobalordertemplate.controllers.Jnjb2bglobalordertemplateControllerConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderHistoryData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * Excel view class for populating Order History data.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTOrderHistoryExcelView extends AbstractXlsView
{


	private static final String sheetName = "Order History";
	private static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";
	
	protected static final String BILL_TO_ACC_NUMBER = "orderhistory.billto.accountnumber";
	protected static final String BILL_TO_ADDRESS = "orderhistory.billto.address";
	protected static final String SOLD_TO_ACC_NUMBER = "orderhistory.soldto.accountnumber";
	protected static final String SOLD_TO_ADDRESS = "orderhistory.soldto.address";
	protected static final String SHIP_TO_ACC_NUMBER = "orderhistory.shipto.accountnumber";
	protected static final String SHIP_TO_ACC_NAME = "orderhistory.shipto.accountname";
	protected static final String CUSTOMER_PO = "orderhistory.customer.po";
	protected static final String ORDER_NUMBER = "orderhistory.order.number";
	private static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	protected static final String ORDER_DATE = "orderhistory.order.date";
	protected static final String ORDER_STATUS = "orderhistory.order.status";
	protected static final String ORDER_TOTAL = "orderhistory.order.total";
	protected static final String ORDER_CHANNEL = "orderhistory.order.channel";
	protected static final String DELIVERY_DATE = "orderhistory.order.deliverydate";
	
	//FIX FOR AAOL-5475
	protected static final String ORDER_CHANNEL_CODE = "order.channel.";
	
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

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workBook, final HttpServletRequest request,
									  final HttpServletResponse response) throws Exception
	{
		final String fileName = Jnjb2bglobalordertemplateControllerConstants.JnjGTExcelPdfViewLabels.OrderHistory.ORDER_HISTORY+".xls";
		response.setHeader("Content-Disposition", "attachment; filename="+fileName);

		final String currentSite = (String) map.get(Jnjb2bCoreConstants.SITE_NAME);

		final SearchPageData<JnjGTOrderHistoryData> searchPageData = (SearchPageData<JnjGTOrderHistoryData>) map.get("searchPageData");
		final List<JnjGTOrderHistoryData> results = searchPageData.getResults();

		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");

		final Sheet sheet = workBook.createSheet(sheetName);
		setHeaderImage(workBook, sheet, (String) map.get("siteLogoPath"),5);
		int rowNumber = 6;
		if (resultLimitExceeded.booleanValue())
		{
			final Row note = sheet.createRow(rowNumber++);

			note.createCell(0).setCellValue(RESULTS_EXCEEDED_MESSAGE);
		}//Modified by Archana for AAOL-5513
		final SimpleDateFormat dateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		final SimpleDateFormat dateFormatRequestedDeliveryDate= new SimpleDateFormat(Jnjb2bCoreConstants.REQUESTDELIVERYDATE_FORMAT);
		final Row header = sheet.createRow(rowNumber++);

		final Font font = workBook.createFont();
		font.setBold(true);

		final CellStyle style = workBook.createCellStyle();
		style.setFont(font);
		
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			header.createCell(0).setCellValue(messageSource.getMessage(BILL_TO_ACC_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(style);

			header.createCell(1).setCellValue(messageSource.getMessage(BILL_TO_ADDRESS,null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(style);
		}
		else
		{
			header.createCell(0).setCellValue(messageSource.getMessage(SOLD_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(style);

			header.createCell(1).setCellValue(messageSource.getMessage(SOLD_TO_ADDRESS,null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(style);
		}

		header.createCell(2).setCellValue(messageSource.getMessage(SHIP_TO_ACC_NUMBER,null, i18nService.getCurrentLocale()));
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue(messageSource.getMessage(SHIP_TO_ACC_NAME,null, i18nService.getCurrentLocale()));
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue(messageSource.getMessage(CUSTOMER_PO,null, i18nService.getCurrentLocale()));
		header.getCell(4).setCellStyle(style);

		header.createCell(5).setCellValue(messageSource.getMessage(ORDER_NUMBER,null, i18nService.getCurrentLocale()));
		header.getCell(5).setCellStyle(style);

		header.createCell(6).setCellValue(messageSource.getMessage(ORDER_DATE,null, i18nService.getCurrentLocale()));
		header.getCell(6).setCellStyle(style);

		header.createCell(7).setCellValue(messageSource.getMessage(ORDER_STATUS,null, i18nService.getCurrentLocale()));
		header.getCell(7).setCellStyle(style);

		header.createCell(8).setCellValue(messageSource.getMessage(ORDER_TOTAL,null, i18nService.getCurrentLocale()));
		header.getCell(8).setCellStyle(style);

		header.createCell(9).setCellValue(messageSource.getMessage(ORDER_CHANNEL,null, i18nService.getCurrentLocale()));
		header.getCell(9).setCellStyle(style);

		header.createCell(10).setCellValue(messageSource.getMessage(DELIVERY_DATE,null, i18nService.getCurrentLocale()));
		header.getCell(10).setCellStyle(style);
		
	

		int entryRowNumber = rowNumber++;
		final String emptyField = new String();
		if (results != null && !results.isEmpty())
		{
			for (final JnjGTOrderHistoryData data : results)
			{
				sheet.autoSizeColumn(entryRowNumber);
				final Row row = sheet.createRow(entryRowNumber++);
				row.createCell(0).setCellValue(data.getBillToNumber());
				row.createCell(1).setCellValue(data.getBillToName());
				row.createCell(2).setCellValue(data.getShipToNumber());
				row.createCell(3).setCellValue(data.getShipToName());
				row.createCell(4).setCellValue(data.getPurchaseOrderNumber());
				row.createCell(5).setCellValue(data.getSapOrderNumber());
				row.createCell(6).setCellValue((data.getPlaced() != null) ? dateFormat.format(data.getPlaced()).toString() : emptyField);//Modified by Archana for AAOL-5513
				row.createCell(7).setCellValue((data.getStatus() != null) ? data.getStatusDisplay() : emptyField);
				row.createCell(8).setCellValue((data.getTotal() != null) ? data.getTotal().getFormattedValue() : emptyField);
				//FIX FOR AAOL-5475
				row.createCell(9).setCellValue(messageSource.getMessage(ORDER_CHANNEL_CODE+(StringUtils.isNotEmpty(data.getChannel()) ? data.getChannel() : emptyField),null, i18nService.getCurrentLocale()));
						
				row.createCell(10).setCellValue((data.getDeliveryDate() != null) ? dateFormat.format(dateFormatRequestedDeliveryDate.parse(data.getDeliveryDate())).toString() : emptyField);
			
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			
		}
	}
	
	
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final String logoPath,int colLen)
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
		anchor.setCol2(colLen);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		final Picture pict = drawing.createPicture(anchor, index);
	}
}
