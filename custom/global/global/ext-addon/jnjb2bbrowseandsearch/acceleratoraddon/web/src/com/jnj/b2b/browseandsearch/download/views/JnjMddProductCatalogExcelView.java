/**
 * 
 */
package com.jnj.b2b.browseandsearch.download.views;

import java.util.Collection;
import java.util.Date;
import java.util.List;
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

import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTUomData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author t.e.sharma
 * 
 */
public class JnjMddProductCatalogExcelView extends AbstractXlsView
{

	protected static final String sheetName = "MDD_Product-Catalog(MDD)";

	protected static final String NEW_LINE_CAHARACTER = "\n";

	@Override
	protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		arg3.setHeader("Content-Disposition", "attachment; filename=MDD_Product_Catalog.xls");

		final Collection<JnjGTProductData> catalogProductsData = (Collection<JnjGTProductData>) arg0.get("catalogProductsData");
		final String currentAccount = (String) arg0.get("currentAccount");

		final Font font = arg1.createFont();
		font.setBold(true);

		final CellStyle style = arg1.createCellStyle();
		style.setFont(font);

		final Sheet sheet = arg1.createSheet(sheetName);

		sheet.autoSizeColumn(0);
		final Row downloadDateHeader = sheet.createRow(0);
		downloadDateHeader.createCell(0).setCellValue("Download date");
		downloadDateHeader.getCell(0).setCellStyle(style);

		final String currentTime = new Date().toString();
		downloadDateHeader.createCell(1).setCellValue(currentTime);

		final Row globalAccounHeader = sheet.createRow(1);
		globalAccounHeader.createCell(0).setCellValue("Global Account Name");
		globalAccounHeader.getCell(0).setCellStyle(style);
		globalAccounHeader.createCell(1).setCellValue(currentAccount);

		final Row emptyRow = sheet.createRow(2);

		final Row header = sheet.createRow(3);
		header.createCell(0).setCellValue("Download date");
		header.getCell(0).setCellStyle(style);


		header.createCell(0).setCellValue("Name");
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue("Franchise");
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue("Division");
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue("Product Code");
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue("Each GTIN");
		header.getCell(4).setCellStyle(style);

		header.createCell(5).setCellValue("Case GTIN");
		header.getCell(5).setCellStyle(style);

		header.createCell(6).setCellValue("Delivery UOM");
		header.getCell(6).setCellStyle(style);

		header.createCell(7).setCellValue("Description");
		header.getCell(7).setCellStyle(style);

		header.createCell(8).setCellValue("UOM of Eaches");
		header.getCell(8).setCellStyle(style);

		header.createCell(9).setCellValue("Status");
		header.getCell(9).setCellStyle(style);

		header.createCell(10).setCellValue("Kit Component Name");
		header.getCell(10).setCellStyle(style);

		header.createCell(11).setCellValue("Kit Component Description");
		header.getCell(11).setCellStyle(style);

		header.createCell(12).setCellValue("Kit Component Unit");
		header.getCell(12).setCellStyle(style);

		header.createCell(13).setCellValue("Depth");
		header.getCell(13).setCellStyle(style);

		header.createCell(14).setCellValue("Height");
		header.getCell(14).setCellStyle(style);

		header.createCell(15).setCellValue("Width");
		header.getCell(15).setCellStyle(style);

		header.createCell(16).setCellValue("Volume");
		header.getCell(16).setCellStyle(style);

		header.createCell(17).setCellValue("Weight");
		header.getCell(17).setCellStyle(style);

		int columnNumber = 4;
		JnjGTUomData deliveryUom = null;
		JnjGTUomData salesUom = null;
		final StringBuilder stringBuilder = new StringBuilder();

		for (final JnjGTProductData data : catalogProductsData)
		{
			sheet.autoSizeColumn(columnNumber);

			final Row row = sheet.createRow(columnNumber++);
			row.createCell(0).setCellValue(data.getName());
			row.createCell(1).setCellValue(data.getMddSpecification().getFranchise());
			row.createCell(2).setCellValue(data.getMddSpecification().getDivision());
			row.createCell(3).setCellValue(data.getBaseMaterialNumber());
			row.createCell(4).setCellValue(data.getMddSpecification().getEachGtin());
			row.createCell(5).setCellValue(data.getMddSpecification().getCaseGtin());


			/** Set Delivery UOM ONLY when Delivery and Sales UOM are NOT null **/
			if (data.getMddSpecification().getDeliveryUom() != null && data.getMddSpecification().getSalesUom() != null)
			{
				deliveryUom = data.getMddSpecification().getDeliveryUom();
				salesUom = data.getMddSpecification().getSalesUom();

				final int quantity = (deliveryUom.getPackagingLevelQty() != null && salesUom.getPackagingLevelQty() != null) ? deliveryUom
						.getPackagingLevelQty() / salesUom.getPackagingLevelQty()
						: 0;

				final String quantityText = (quantity == 0) ? "" : Integer.toString(quantity);

				final int numeratorFactor = (salesUom.getNumerator() != null) ? (quantity * salesUom.getNumerator()) : 0;
				final String numeratorFactorText = (numeratorFactor == 0) ? "" : Integer.toString(numeratorFactor);

				stringBuilder.append(quantityText);
				stringBuilder.append(salesUom.getName());
				stringBuilder.append(" (");
				stringBuilder.append(numeratorFactorText + "");
				stringBuilder.append(salesUom.getLwrPackagingLvlUomCode());
				stringBuilder.append(")");
			}
			row.createCell(6).setCellValue(stringBuilder.toString());
			stringBuilder.setLength(0);

			row.createCell(7).setCellValue(data.getDescription());

			//TODO [AR]: Yet to be confirmed with Uom of Eaches, yet to get confirmation on the value of it.
			row.createCell(8).setCellValue("");
			row.createCell(9).setCellValue(data.getStatus());

			final List<String> kitNames = data.getMddSpecification().getKitComponentNames();
			final List<String> kitDescriptions = data.getMddSpecification().getKitComponentDesc();
			final List<String> kitUnits = data.getMddSpecification().getKitComponentUnits();


			if (kitNames != null && !kitNames.isEmpty())
			{
				for (final String kitName : kitNames)
				{
					stringBuilder.append(kitName);
					stringBuilder.append("NEW_LINE_CAHARACTER");
				}
			}
			row.createCell(10).setCellValue(stringBuilder.toString());
			stringBuilder.setLength(0);

			if (kitDescriptions != null && !kitDescriptions.isEmpty())
			{
				for (final String kitDescription : kitDescriptions)
				{
					stringBuilder.append(kitDescription);
					stringBuilder.append("NEW_LINE_CAHARACTER");
				}
			}
			row.createCell(11).setCellValue(stringBuilder.toString());
			stringBuilder.setLength(0);

			if (kitUnits != null && !kitUnits.isEmpty())
			{
				for (final String kitUnit : kitUnits)
				{
					stringBuilder.append(kitUnit);
					stringBuilder.append("NEW_LINE_CAHARACTER");
				}
			}
			row.createCell(12).setCellValue(stringBuilder.toString());
			stringBuilder.setLength(0);

			row.createCell(13).setCellValue(data.getMddSpecification().getDeliveryUom().getDepth());
			row.createCell(14).setCellValue(data.getMddSpecification().getDeliveryUom().getHeight());
			row.createCell(15).setCellValue(data.getMddSpecification().getDeliveryUom().getWidth());
			row.createCell(16).setCellValue(data.getMddSpecification().getDeliveryUom().getVolume());
			row.createCell(17).setCellValue(data.getMddSpecification().getDeliveryUom().getWeight());
		}
	}
}
