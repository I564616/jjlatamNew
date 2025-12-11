/**
 *
 */
package com.jnj.b2b.jnjglobalreports.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPicture;
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

import com.jnj.facades.data.JnjGTCutReportOrderData;
import com.jnj.facades.data.JnjGTCutReportOrderEntryData;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.core.util.JnJCommonUtil;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author ujjwal.negi
 *
 */
public class JnjGTCutReportExcelView extends AbstractXlsView
{
	private static final Logger LOG = Logger.getLogger(JnjGTCutReportExcelView.class);
	private static final String CUTORDER_RESPONSE_DATA_LIST = "cutReportOrders";
	public static final String DATE_FORMAT = "date.dateformat";//Modified by Archana for AAOL-5513
	private static final String CUT_REPORT_PICTURE_RESIZING_FACTOR = "cutReport.picture.resizeFactor";
	
	private static final String ACCOUNT_NUMBER = "cutreports.account.number";
	private static final String PRODUCT_NAME = "cutreports.product.name";
	private static final String PRODUCT_CODE = "cutreports.product.code";
	private static final String GTIN_UPC = "cutreports.gtin.upc";
	private static final String PO_NUMBER = "cutreports.po.number";
	private static final String ORDER_NUMBER = "cutreports.order.number";
	private static final String ORDER_DATE = "cutreports.order.date";
	private static final String CUT_REASON = "cutreports.cut.reason";
	private static final String CUT_QTY = "cutreports.cut.qty";
	private static final String ORDER_QTY = "cutreports.order.qty";
	private static final String NEXT_AVAILABLEDATE = "cutreports.next.available.date";
	
	
	@Autowired
	protected I18NService i18nService;
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

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		try
		{
			arg3.setHeader("Content-Disposition", "attachment; filename=Cut_Report.xls");
			final List<JnjGTCutReportOrderData> jnjGTCutReportResponseDataList = (List<JnjGTCutReportOrderData>) arg0
					.get(CUTORDER_RESPONSE_DATA_LIST);
					
			/*final Map<String, JnjGTCutReportOrderData> jnjGTCutReportResponseDataList = (Map<String, JnjGTCutReportOrderData>) arg0
					.get(CUTORDER_RESPONSE_DATA_LIST);*/

			final Sheet sheet = arg1.createSheet("Cut Report");
			setHeaderImage(arg1, sheet, (String) arg0.get("siteLogoPath"));
			final Row header = sheet.createRow(6);
			//final String siteName = (String) arg0.get(Jnjnab2bcoreConstants.SITE_NAME);
			
			
			header.createCell(0).setCellValue(messageSource.getMessage(ACCOUNT_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(arg1));

			header.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(arg1));

			header.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(arg1));

			header.createCell(3).setCellValue(messageSource.getMessage(GTIN_UPC, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(arg1));

			header.createCell(4).setCellValue(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(arg1));

			header.createCell(5).setCellValue(messageSource.getMessage(ORDER_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(arg1));

			header.createCell(6).setCellValue(messageSource.getMessage(ORDER_DATE, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(arg1));

			header.createCell(7).setCellValue(messageSource.getMessage(CUT_REASON, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(arg1));

			header.createCell(8).setCellValue(messageSource.getMessage(CUT_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(arg1));

			header.createCell(9).setCellValue(messageSource.getMessage(ORDER_QTY, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(arg1));

			header.createCell(10).setCellValue(messageSource.getMessage(NEXT_AVAILABLEDATE, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(arg1));

			CellStyle css = createValueCell(arg1);
			int rowNum = 7;
			if (null != jnjGTCutReportResponseDataList)
			{
				for (final JnjGTCutReportOrderData jnjGTCutReportOrderDataSetEntry : jnjGTCutReportResponseDataList)
				{
					for(final JnjGTCutReportOrderEntryData jnjGTCutReportOrderEntryDataSetEntries:jnjGTCutReportOrderDataSetEntry.getCutReportEntries() )
					{
						
					
					
					Row row = sheet.createRow(rowNum++);

					Cell cell0 = row.createCell(0);
					cell0.setCellStyle(css);
					cell0.setCellValue(jnjGTCutReportOrderDataSetEntry.getAccountNumber().replaceAll("^0+", StringUtils.EMPTY));
					Cell cell1 = row.createCell(1);
					cell1.setCellStyle(css);
					cell1.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getProductName());
					Cell cell2 = row.createCell(2);
					cell2.setCellStyle(css);
					cell2.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getProductCode());
					Cell cell3 = row.createCell(3);
					cell3.setCellStyle(css);
					
					if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getGtin())){
						if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getUpc())){
							cell3.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getGtin()+" / "+ jnjGTCutReportOrderEntryDataSetEntries.getUpc());

						}else{
							cell3.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getGtin()+" / ");
						}
					}else{
						if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getUpc())){
							cell3.setCellValue(" / "+ jnjGTCutReportOrderEntryDataSetEntries.getUpc());

						}else{
							cell3.setCellValue("");
						}
					}
					
					Cell cell4 = row.createCell(4);
					cell4.setCellStyle(css);
					cell4.setCellValue(jnjGTCutReportOrderDataSetEntry.getPONumber());
					Cell cell5 = row.createCell(5);
					cell5.setCellStyle(css);
					cell5.setCellValue(jnjGTCutReportOrderDataSetEntry.getOrderNumber());
					Cell cell6 = row.createCell(6);
					cell6.setCellStyle(css);
					cell6.setCellValue(JnJCommonUtil.formatDate(jnjGTCutReportOrderDataSetEntry.getOrderDate(),
							messageSource.getMessage(DATE_FORMAT, null, i18nService.getCurrentLocale())));//Modified by Archana for AAOL-5513
					Cell cell7 = row.createCell(7);
					cell7.setCellStyle(css);
					cell7.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getCutReason());
					Cell cell8 = row.createCell(8);
					cell8.setCellStyle(css);
					cell8.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getCutQuantity());
					Cell cell9 = row.createCell(9);
					cell9.setCellStyle(css);
					cell9.setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getOrderQuantity());
					Cell cell10 = row.createCell(10);
					cell10.setCellStyle(css);
					
					if(jnjGTCutReportOrderEntryDataSetEntries.getAvailabilityDate()!=null){
						cell10.setCellValue(JnJCommonUtil.formatDate(jnjGTCutReportOrderEntryDataSetEntries.getAvailabilityDate(),
								messageSource.getMessage(DATE_FORMAT, null, i18nService.getCurrentLocale())));//Modified by Archana for AAOL-5513
	
					}else{
						cell10.setCellValue("");

					}
					
					
					
					/*row.createCell(0).setCellValue(
							jnjGTCutReportOrderDataSetEntry.getAccountNumber().replaceAll("^0+", StringUtils.EMPTY));
					cell0.setCellStyle(css);
					row.createCell(1).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getProductName());
					row.createCell(2).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getProductCode());
					if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getGtin())){
						if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getUpc())){
							row.createCell(3).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getGtin()+" / "+ jnjGTCutReportOrderEntryDataSetEntries.getUpc());

						}else{
							row.createCell(3).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getGtin()+" / ");
						}
					}else{
						if(StringUtils.isNotEmpty(jnjGTCutReportOrderEntryDataSetEntries.getUpc())){
							row.createCell(3).setCellValue(" / "+ jnjGTCutReportOrderEntryDataSetEntries.getUpc());

						}else{
							row.createCell(3).setCellValue("");
						}
					}
					row.createCell(4).setCellValue(jnjGTCutReportOrderDataSetEntry.getPONumber());
					row.createCell(5).setCellValue(jnjGTCutReportOrderDataSetEntry.getOrderNumber());

					row.createCell(6).setCellValue(JnJCommonUtil.formatDate(jnjGTCutReportOrderDataSetEntry.getOrderDate(),
							DATE_FORMAT));
					
					row.createCell(7).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getCutReason());
					row.createCell(8).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getCutQuantity());
					row.createCell(9).setCellValue(jnjGTCutReportOrderEntryDataSetEntries.getOrderQuantity());
					if(jnjGTCutReportOrderEntryDataSetEntries.getAvailabilityDate()!=null){
						row.createCell(10).setCellValue(JnJCommonUtil.formatDate(jnjGTCutReportOrderEntryDataSetEntries.getAvailabilityDate(),
								DATE_FORMAT));
	
					}else{
						row.createCell(10).setCellValue("");

					}
*/					
					

				}
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
		anchor.setCol2(11);
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
