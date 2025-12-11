package com.jnj.b2b.cartandcheckoutaddon.download.views;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTOrderConfirmationExcelView extends AbstractXlsView
{
	
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
	protected static final Logger LOG = Logger.getLogger(JnjGTOrderConfirmationExcelView.class);
	
	private static final String PO_NUMBER = "order.confirmation.ponumber"; 
	private static final String JJORDERNUMBER = "order.confirmation.jjordernumber"; 
	private static final String ORDERTYPE = "order.confirmation.ordertype"; 
	private static final String ADDRESS = "order.confirmation.address"; 
	private static final String SALES_FORBIDDEN = "order.confirmation.salesforbidden"; 
	private static final String ORDER = "order.confirmation.order"; 
	private static final String TOTAL_NET = "order.confirmation.total.net";
	private static final String TOTAL_FEES = "order.confirmation.total.fees"; 
	private static final String FREIGHT_FEES = "order.confirmation.total.freightfees"; 
	private static final String EXPEDITED_FEES = "order.confirmation.total.expeditedfees"; 
	private static final String HSA_PROMOTION = "order.confirmation.hsa.promotion"; 
	private static final String DROPSHIP_FEES = "order.confirmation.dropship.fees"; 
	private static final String MINIMUM_FEES = "order.confirmation.minimum.fees"; 
	private static final String TAXES = "order.confirmation.taxes"; 
	private static final String DISCOUNTS = "order.confirmation.discounts"; 
	private static final String TOTALGROSS = "order.confirmation.totalgross.price"; 
	private static final String ENTRY_NUMBER = "order.confirmation.entry.number"; 
	private static final String PRODUCT_NAME = "order.confirmation.product.name"; 
	private static final String PRODUCT_DESC = "order.confirmation.Product.Description"; 
	private static final String PRODUCT_QUANTITY = "order.confirmation.Product.Quantity"; 
	private static final String DELIVERY_DATE = "order.confirmation.Estimated.DeliveryDate"; 
	private static final String INDIRECT_CUST = "order.confirmation.indirectCustomer"; 
	private static final String ITEMPRICE = "order.confirmation.itemPrice"; 
	private static final String TOTAL = "order.confirmation.total"; 
	 

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		try
		{
			response.setHeader("Content-Disposition", "attachment; filename=OrderConfirmation.xls");
			final JnjGTOrderData orderDetails = (JnjGTOrderData) map.get("orderData");
			CellStyle css = createValueCell(workbook);
			final Sheet sheet = workbook.createSheet("Order Confirmation Details");
			final Row header = sheet.createRow(6);
			setHeaderImage(workbook, sheet, (String) map.get("siteLogoPath"));
			header.createCell(0).setCellValue(messageSource.getMessage(PO_NUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(0).setCellStyle(createLabelCell(workbook));
			header.createCell(1).setCellValue(messageSource.getMessage(JJORDERNUMBER, null, i18nService.getCurrentLocale()));
			header.getCell(1).setCellStyle(createLabelCell(workbook));
			header.createCell(2).setCellValue(messageSource.getMessage(ORDERTYPE, null, i18nService.getCurrentLocale()));
			header.getCell(2).setCellStyle(createLabelCell(workbook));
			header.createCell(3).setCellValue(messageSource.getMessage(ADDRESS, null, i18nService.getCurrentLocale()));
			header.getCell(3).setCellStyle(createLabelCell(workbook));
			header.createCell(4).setCellValue(messageSource.getMessage(SALES_FORBIDDEN, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(workbook));
			header.createCell(5).setCellValue(messageSource.getMessage(ORDER, null, i18nService.getCurrentLocale()));
			header.getCell(5).setCellStyle(createLabelCell(workbook));
			header.createCell(6).setCellValue(messageSource.getMessage(TOTAL_NET, null, i18nService.getCurrentLocale()));
			header.getCell(6).setCellStyle(createLabelCell(workbook));
			header.createCell(7).setCellValue(messageSource.getMessage(TOTAL_FEES, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(workbook));
			header.createCell(8).setCellValue(messageSource.getMessage(FREIGHT_FEES, null, i18nService.getCurrentLocale()));
			header.getCell(8).setCellStyle(createLabelCell(workbook));
			header.createCell(9).setCellValue(messageSource.getMessage(EXPEDITED_FEES, null, i18nService.getCurrentLocale()));
			header.getCell(9).setCellStyle(createLabelCell(workbook));
			header.createCell(10).setCellValue(messageSource.getMessage(HSA_PROMOTION, null, i18nService.getCurrentLocale()));
			header.getCell(10).setCellStyle(createLabelCell(workbook));
			header.createCell(11).setCellValue(messageSource.getMessage(DROPSHIP_FEES, null, i18nService.getCurrentLocale()));
			header.getCell(11).setCellStyle(createLabelCell(workbook));
			header.createCell(12).setCellValue(messageSource.getMessage(MINIMUM_FEES, null, i18nService.getCurrentLocale()));
			header.getCell(12).setCellStyle(createLabelCell(workbook));
			header.createCell(13).setCellValue(messageSource.getMessage(TAXES, null, i18nService.getCurrentLocale()));
			header.getCell(13).setCellStyle(createLabelCell(workbook));
			header.createCell(14).setCellValue(messageSource.getMessage(DISCOUNTS, null, i18nService.getCurrentLocale()));
			header.getCell(14).setCellStyle(createLabelCell(workbook));
			header.createCell(15).setCellValue(messageSource.getMessage(TOTALGROSS, null, i18nService.getCurrentLocale()));
			header.getCell(15).setCellStyle(createLabelCell(workbook));

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
			}else{
				row.createCell(1).setCellValue(orderDetails.getOrderNumber());
			}
			if (null != orderDetails.getOrderType())
			{
				row.createCell(2).setCellValue(orderDetails.getOrderType());
			}
			if (null != orderDetails.getDeliveryAddress())
			{
				row.createCell(3).setCellValue(orderDetails.getDeliveryAddress().getFormattedAddress());
			}
			if (null != orderDetails.getSalesForbidden())
			{
				row.createCell(4).setCellValue(orderDetails.getSalesForbidden());
			}
			if (null != orderDetails.getCompleteOrder())
			{
				row.createCell(5).setCellValue(orderDetails.getCompleteOrder());
			}
			if (null != orderDetails.getTotalPrice())
			{
				row.createCell(6).setCellValue(orderDetails.getTotalPrice().getFormattedValue());
			}
			if (null != orderDetails.getTotalNetValue())
			{
				row.createCell(7).setCellValue(orderDetails.getTotalNetValue().getFormattedValue());
			}
			if (null != orderDetails.getTotalFreightFees())
			{
				row.createCell(8).setCellValue(orderDetails.getTotalFreightFees().getFormattedValue());
			}
			if (null != orderDetails.getExpeditedFee())
			{
				row.createCell(9).setCellValue(orderDetails.getExpeditedFee().getFormattedValue());
			}
			if (null != orderDetails.getHsaPromotion())
			{
				row.createCell(10).setCellValue(orderDetails.getHsaPromotion().getFormattedValue());
			}
			if (null != orderDetails.getTotalDropShipFee())
			{
				row.createCell(11).setCellValue(orderDetails.getTotalDropShipFee().getFormattedValue());
			}
			if (null != orderDetails.getTotalminimumOrderFee())
			{
				row.createCell(12).setCellValue(orderDetails.getTotalminimumOrderFee().getFormattedValue());
			}
			if (null != orderDetails.getTotalTax())
			{
				row.createCell(13).setCellValue(orderDetails.getTotalTax().getFormattedValue());
			}
			if (null != orderDetails.getDiscountTotal())
			{
				row.createCell(14).setCellValue(orderDetails.getDiscountTotal().getFormattedValue());
			}
			if (null != orderDetails.getTotalGrossPrice())
			{
				row.createCell(15).setCellValue(orderDetails.getTotalGrossPrice().getFormattedValue());
			}
			final Row secondheader = sheet.createRow(8);
			secondheader.createCell(0).setCellValue(messageSource.getMessage(ENTRY_NUMBER, null, i18nService.getCurrentLocale()));
			secondheader.getCell(0).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_NAME, null, i18nService.getCurrentLocale()));
			secondheader.getCell(1).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(2).setCellValue(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()));
			secondheader.getCell(2).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_QUANTITY, null, i18nService.getCurrentLocale()));
			secondheader.getCell(3).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(4).setCellValue(messageSource.getMessage(DELIVERY_DATE, null, i18nService.getCurrentLocale()));
			secondheader.getCell(4).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(5).setCellValue(messageSource.getMessage(INDIRECT_CUST, null, i18nService.getCurrentLocale()));
			secondheader.getCell(5).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(6).setCellValue(messageSource.getMessage(ITEMPRICE, null, i18nService.getCurrentLocale()));
			secondheader.getCell(6).setCellStyle(createLabelCell(workbook));
			secondheader.createCell(7).setCellValue(messageSource.getMessage(TOTAL, null, i18nService.getCurrentLocale()));
			secondheader.getCell(7).setCellStyle(createLabelCell(workbook));

			final List<OrderEntryData> orderEntryList = (List<OrderEntryData>) map.get("orderEntryList");
			rowNum = 9;
			for (final OrderEntryData orderEntryData : orderEntryList)
			{
				final Row rowStart = sheet.createRow(rowNum++);
				rowStart.createCell(0).setCellValue(String.valueOf(orderEntryData.getEntryNumber()+1));
				rowStart.createCell(1).setCellValue(orderEntryData.getProduct().getName());
				rowStart.createCell(2).setCellValue(orderEntryData.getProduct().getDescription());
				rowStart.createCell(3).setCellValue(String.valueOf(orderEntryData.getQuantity()));
				if (orderEntryData instanceof JnjGTOrderEntryData)
				{
					if (null != ((JnjGTOrderEntryData) orderEntryData).getExpectedDeliveryDate())
					{
						rowStart.createCell(4).setCellValue(simpleDateFormat.format(((JnjGTOrderEntryData) orderEntryData).getExpectedDeliveryDate()).toString());
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getIndirectCustomer())
					{
						rowStart.createCell(5).setCellValue(((JnjGTOrderEntryData) orderEntryData).getIndirectCustomer());
					}
					if (null != ((JnjGTOrderEntryData) orderEntryData).getBasePrice())
					{
						rowStart.createCell(6).setCellValue(((JnjGTOrderEntryData) orderEntryData).getBasePrice().getFormattedValue());
					}

				}
				else
				{
					rowStart.createCell(4).setCellValue(StringUtils.EMPTY);
					rowStart.createCell(5).setCellValue(StringUtils.EMPTY);
					if (null != orderEntryData.getBasePrice())
					{
						rowStart.createCell(6).setCellValue(orderEntryData.getBasePrice().getFormattedValue());
					}
				}
				if (orderEntryData.getTotalPrice() != null)
				{
					rowStart.createCell(7).setCellValue(orderEntryData.getTotalPrice().getFormattedValue());

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
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);


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
