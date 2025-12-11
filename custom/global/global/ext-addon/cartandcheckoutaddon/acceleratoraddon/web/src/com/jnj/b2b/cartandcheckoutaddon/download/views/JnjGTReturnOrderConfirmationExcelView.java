package com.jnj.b2b.cartandcheckoutaddon.download.views;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
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

import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTReturnOrderConfirmationExcelView extends AbstractXlsView
{
	
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
	
	protected static final Logger LOG = Logger.getLogger(JnjGTOrderConfirmationExcelView.class);
 
	private static final String PO_NUMBER = "cart.return.entries.poNumber";
	private static final String JNJ_ORDERNUMBER = "return.confirmation.table.jnjorderNumber";
	private static final String ORDER_TYPE = "return.confirmation.order.type";
	private static final String ADDRESS = "return.confirmation.address";
	private static final String REASON_FOR_RETURN = "return.confirmation.reasonfor.return";
	private static final String CUST_PO_REFERENCE = "cart.return.returnInfo.customerReferencePO";
	private static final String ENTRYNUMBER = "return.confirmation.entry.number";
	private static final String PRODUCTNAME = "return.confirmation.productName";
	private static final String PRODUCT_DESC = "return.confirmation.productDescription";
	private static final String JNJID = "return.confirmation.jnjid";
	private static final String QUANTITY = "return.confirmation.product.qty";
	private static final String GTIN = "return.confirmation.gtin";
	private static final String LOT = "return.confirmation.lot";
	private static final String INVOICE_NUMBER = "cart.return.entries.invoiceNumber";
		

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		try
		{
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.xls");
			final JnjGTOrderData orderDetails = (JnjGTOrderData) map.get("orderData");
			final Sheet sheet = workbook.createSheet("Order Confirmation Details");
			CellStyle css = createValueCell(workbook);
			final Row header = sheet.createRow(6);
			setHeaderImage(workbook, sheet, (String) map.get("siteLogoPath"));
			header.createCell(0).setCellValue(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(workbook));
			header.createCell(1).setCellValue(messageSource.getMessage(JNJ_ORDERNUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(workbook));
			header.createCell(2).setCellValue(messageSource.getMessage(ORDER_TYPE, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(workbook));
			header.createCell(3).setCellValue(messageSource.getMessage(ADDRESS, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(workbook));
			header.createCell(4).setCellValue(messageSource.getMessage(REASON_FOR_RETURN, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(workbook));
			header.createCell(5).setCellValue(messageSource.getMessage(CUST_PO_REFERENCE, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(workbook));
	
			int rowNum = 7;
		
			
			final Row row = sheet.createRow(rowNum++);
			
			Cell cell1 = row.createCell(1);
			cell1.setCellStyle(css);
			if (null != orderDetails.getPurchaseOrderNumber())
			{
				row.createCell(0).setCellValue(orderDetails.getPurchaseOrderNumber());
			}
			if (null != orderDetails.getSapOrderNumber())
			{
				row.createCell(1).setCellValue(orderDetails.getSapOrderNumber());
			}
			if (null != orderDetails.getOrderType())
			{
				row.createCell(2).setCellValue(orderDetails.getOrderType());
			}
			if (null != orderDetails.getDeliveryAddress())
			{
				row.createCell(3).setCellValue(orderDetails.getDeliveryAddress().getFormattedAddress());
			}
			if (null != orderDetails.getReasonCodeReturn())
			{
				row.createCell(4).setCellValue(orderDetails.getReasonCodeReturn());
			}
			if (null != orderDetails.getCustomerPoNumber())
			{
				row.createCell(5).setCellValue(orderDetails.getCustomerPoNumber());
			}


			final Row secondheader = sheet.createRow(8);
			secondheader.createCell(0).setCellValue(messageSource.getMessage(ENTRYNUMBER, null, i18nService.getCurrentLocale()));
			secondheader.getCell(0).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(1).setCellValue(messageSource.getMessage(PRODUCTNAME, null, i18nService.getCurrentLocale()));
			secondheader.getCell(1).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()));
			secondheader.getCell(2).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(3).setCellValue(messageSource.getMessage(JNJID, null, i18nService.getCurrentLocale()));
			secondheader.getCell(3).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(4).setCellValue(messageSource.getMessage(QUANTITY, null, i18nService.getCurrentLocale()));
			secondheader.getCell(4).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(5).setCellValue(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale()));
			secondheader.getCell(5).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(6).setCellValue(messageSource.getMessage(LOT, null, i18nService.getCurrentLocale()));
			secondheader.getCell(6).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(7).setCellValue(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale()));
			secondheader.getCell(7).setCellStyle(createLabelCell(workbook));

			secondheader.createCell(8).setCellValue(messageSource.getMessage(INVOICE_NUMBER, null, i18nService.getCurrentLocale()));
			secondheader.getCell(8).setCellStyle(createLabelCell(workbook));


			final List<OrderEntryData> orderEntryList = (List<OrderEntryData>) map.get("orderEntryList");
			rowNum = 9;
			for (final OrderEntryData orderEntryData : orderEntryList)
			{
				final JnjGTProductData productData = (JnjGTProductData) orderEntryData.getProduct();
				final Row rowStart = sheet.createRow(rowNum++);
				rowStart.createCell(0).setCellValue(String.valueOf(orderEntryData.getEntryNumber()));
				rowStart.createCell(1).setCellValue(productData.getName());
				rowStart.createCell(2).setCellValue(productData.getDescription());
				rowStart.createCell(3).setCellValue(productData.getCode());
				rowStart.createCell(4).setCellValue(String.valueOf(orderEntryData.getQuantity()));
				rowStart.createCell(5).setCellValue(productData.getGtin());

				if (orderEntryData instanceof JnjGTOrderEntryData)
				{
					if (null != ((JnjGTOrderEntryData) orderEntryData).getLotNumber())
					{
						rowStart.createCell(6).setCellValue(((JnjGTOrderEntryData) orderEntryData).getLotNumber());
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getPoNumber())
					{
						rowStart.createCell(7).setCellValue(((JnjGTOrderEntryData) orderEntryData).getPoNumber());
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getReturnInvNumber())
					{
						rowStart.createCell(8).setCellValue(((JnjGTOrderEntryData) orderEntryData).getReturnInvNumber());
					}
				}
				else
				{
					rowStart.createCell(6).setCellValue(StringUtils.EMPTY);
					rowStart.createCell(7).setCellValue(StringUtils.EMPTY);
					rowStart.createCell(8).setCellValue(StringUtils.EMPTY);
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
			
		}

		catch (final Exception exp)
		{
			LOG.error("error while setting table - " + exp.getMessage() + "---" + exp.getLocalizedMessage());
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
