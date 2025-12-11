/**
 * Excel view class for CONSUMER PRODUCTS
 */
package com.jnj.b2b.browseandsearch.download.views;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;


public class JnjConsProductCatalogExcelView extends AbstractXlsView
{

	protected static final String sheetName = "CONS_Product-Catalog(MDD)";

	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		arg3.setHeader("Content-Disposition", "attachment; filename=Consumer_Product_Catalog.xls");

		//TO-DO [Tushar]: Bread crumb and Search oage data will be enables ONLY after complete Solr configuration.
		/*
		 * final ProductSearchPageData<SearchStateData, ProductData> searchPageData =
		 * (ProductSearchPageData<SearchStateData, ProductData>) arg0 .get("searchPageData"); final List<ProductData>
		 * results = searchPageData.getResults();
		 */

		final Sheet sheet = arg1.createSheet(sheetName);

		final Row header = sheet.createRow(0);

		final Font font = arg1.createFont();
		font.setBold(true);

		final CellStyle style = arg1.createCellStyle();
		style.setFont(font);

		header.createCell(0).setCellValue("Name");
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue("Franchise");
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue("Brand");
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue("Product Code");
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue("UPC");
		header.getCell(4).setCellStyle(style);

		header.createCell(5).setCellValue("Bracket Price");
		header.getCell(5).setCellStyle(style);

		header.createCell(6).setCellValue("Quantity (editable)");
		header.getCell(6).setCellStyle(style);

		header.createCell(7).setCellValue("Status, if applicable");
		header.getCell(7).setCellStyle(style);

		header.createCell(8).setCellValue("Description");
		header.getCell(8).setCellStyle(style);

		header.createCell(9).setCellValue("Shipping UOM");
		header.getCell(9).setCellStyle(style);

		header.createCell(10).setCellValue("Eaches per Case");
		header.getCell(10).setCellStyle(style);

		header.createCell(11).setCellValue("Inner Packs per Case");
		header.getCell(11).setCellStyle(style);

		header.createCell(12).setCellValue("Eaches per Inner Pack");
		header.getCell(12).setCellStyle(style);

		header.createCell(13).setCellValue("Tiers per Pallet");
		header.getCell(13).setCellStyle(style);

		header.createCell(14).setCellValue("Cases per Pallet");
		header.getCell(14).setCellStyle(style);

		header.createCell(15).setCellValue("First Effective Ship Date");
		header.getCell(15).setCellStyle(style);

		header.createCell(16).setCellValue("Case GTIN");
		header.getCell(16).setCellStyle(style);

		header.createCell(17).setCellValue("Case Weight");
		header.getCell(17).setCellStyle(style);

		header.createCell(18).setCellValue("Case Height");
		header.getCell(18).setCellStyle(style);

		header.createCell(19).setCellValue("Case Depth");
		header.getCell(19).setCellStyle(style);

		header.createCell(20).setCellValue("Case Width");
		header.getCell(20).setCellStyle(style);

		header.createCell(21).setCellValue("Case Price");
		header.getCell(21).setCellStyle(style);

		header.createCell(22).setCellValue("Eaches GTIN");
		header.getCell(22).setCellStyle(style);

		header.createCell(23).setCellValue("Eaches Weight");
		header.getCell(23).setCellStyle(style);

		header.createCell(24).setCellValue("Eaches Volume");
		header.getCell(24).setCellStyle(style);

		header.createCell(25).setCellValue("Eaches Height");
		header.getCell(25).setCellStyle(style);

		header.createCell(26).setCellValue("Eaches Depth");
		header.getCell(26).setCellStyle(style);

		header.createCell(27).setCellValue("Eaches Width");
		header.getCell(27).setCellStyle(style);

		header.createCell(28).setCellValue("Eaches Price");
		header.getCell(28).setCellStyle(style);

		header.createCell(29).setCellValue("Country of Origin");
		header.getCell(29).setCellStyle(style);

		header.createCell(30).setCellValue("Country of Assembly");
		header.getCell(30).setCellStyle(style);

		for (int columnNumber = 0; columnNumber <= header.getLastCellNum(); columnNumber++)
		{
			sheet.autoSizeColumn(columnNumber);
		}
		/*
		 * int rowNum = 1; //create the row data final HSSFRow row = sheet.createRow(rowNum++);
		 * row.createCell(0).setCellValue("1234"); row.createCell(1).setCellValue("Categiry Name");
		 */

		/*
		 * int rowNum = 1;// for (final ProductData productData : results)// {// //create the row data final HSSFRow row =
		 * sheet.createRow(rowNum++);// row.createCell(0).setCellValue(productData.getCode());//
		 * row.createCell(1).setCellValue("");// row.createCell(2).setCellValue(productData.getName());//
		 * //row.createCell(3).setCellValue((productData.getPrice() == null) ? null :
		 * productData.getPrice().getFormattedValue()); }
		 */

	}
}
