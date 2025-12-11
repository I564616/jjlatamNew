package com.jnj.b2b.cartandcheckoutaddon.download.views;

import de.hybris.platform.b2bacceleratorfacades.product.data.CartEntryData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.util.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTCartEntriesExportToExcelView extends AbstractXlsView
{
	protected static final Logger LOG = Logger.getLogger(JnjGTCartEntriesExportToExcelView.class);
	 

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook,
			final HttpServletRequest request, final HttpServletResponse response) throws Exception
	{

		try
		{
			response.setHeader("Content-Disposition", "attachment; filename="+Config.getParameter("cart.export.excel.filename"));
//			final JnjGTOrderData orderDetails = (JnjGTOrderData) map.get("orderData");
			final JnjGTCartData cartData = (JnjGTCartData) map.get("cartData");
			if(cartData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KA.getCode()) || 
					cartData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KB.getCode()) || 
					cartData.getOrderType().equalsIgnoreCase(JnjOrderTypesEnum.KE.getCode()))
			{
				response.setHeader("Content-Disposition", "attachment; filename="+Config.getParameter("cart.consignment.export.excel.filename"));
			}
		
			final Sheet sheet = workbook.createSheet("Cart Details");
			final Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("Line Item");
			header.createCell(1).setCellValue("Product Code");
			header.createCell(2).setCellValue("Quantity");
			header.createCell(3).setCellValue("UOM");
			header.createCell(4).setCellValue("Product Description");
			header.createCell(5).setCellValue("Unit Price");
			header.createCell(6).setCellValue("Total Price");
			header.createCell(7).setCellValue("Deliver To ID");
			header.createCell(8).setCellValue("Deliver To Name");
			int rowNum = 1;
		
			for(OrderEntryData entry : cartData.getEntries()){
				
			final Row row = sheet.createRow(rowNum++);
			if (null != entry.getEntryNumber())
			{
				row.createCell(0).setCellValue((entry.getEntryNumber()+1));
			}
			if (null != entry.getProduct().getCode())
			{
				row.createCell(1).setCellValue(entry.getProduct().getCode());
			}
			if (null != entry.getQuantity())
			{
				row.createCell(2).setCellValue(entry.getQuantity());
			}
			/*if (null != entry.getProduct().get)
			{
				row.createCell(3).setCellValue(orderDetails.getDeliveryAddress().getFormattedAddress());
			}*/
			if (null !=entry.getProduct().getDescription())
			{
				row.createCell(4).setCellValue(entry.getProduct().getDescription());
			}
			if (null != entry.getBasePrice().getFormattedValue())
			{
				row.createCell(5).setCellValue(entry.getBasePrice().getFormattedValue());
			}
			if (null != entry.getTotalPrice())
			{
				row.createCell(6).setCellValue(entry.getTotalPrice().getFormattedValue());
			}
			if (null != cartData.getB2bCustomerData().getUid())
			{
				row.createCell(7).setCellValue(cartData.getB2bCustomerData().getUid());
			}
			if (null !=  cartData.getB2bCustomerData().getName())
			{
				row.createCell(8).setCellValue(cartData.getB2bCustomerData().getName());
			}
			//rowNum++;
			}
			
			
			
		}

		catch (final Exception exp)
		{
			LOG.error("error while setting table - " + exp.getMessage() + "---" + exp.getLocalizedMessage());
		}
	}
	public void setHeaderImage(final HSSFWorkbook hssfWorkbook, final HSSFSheet sheet, final String logoPath)
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

	protected static CellStyle createLabelCell(HSSFWorkbook workbook){

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
