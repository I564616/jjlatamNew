/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.download.views;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjContractData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author plahiri1
 *
 */
public class JnjLatamContractExcelView extends AbstractXlsView
{
	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	/**
	 * @return the jnjCommonFacadeUtil
	 */
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
	{
		return jnjCommonFacadeUtil;
	}

	/**
	 * @param jnjCommonFacadeUtil
	 *           the jnjCommonFacadeUtil to set
	 */
	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
	{
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}



	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook workbook, final HttpServletRequest request,
									  final HttpServletResponse response) throws Exception
	{

		try
		{
			final String sheetName = getJnjCommonFacadeUtil().getMessageFromImpex("text.contract.pdfexcel.list");
			final String fileName = getJnjCommonFacadeUtil().getMessageFromImpex("contract.myContracts") + ".xls";
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			final List<JnjContractData> results = (List<JnjContractData>) map.get("contractList");
			final Sheet sheet = workbook.createSheet(sheetName);
			int rowNumber = 6;

			final Row row = sheet.createRow(rowNumber++);

			final Font font = workbook.createFont();
			font.setBold(true);

			final CellStyle style = workbook.createCellStyle();
			style.setFont(font);

			final String[] headers = new String[]
			{ getJnjCommonFacadeUtil().getMessageFromImpex("download.excelOrdersLabel.contractNumber"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.tenderNumber"),
					getJnjCommonFacadeUtil().getMessageFromImpex("checkOut.orderConfirmation.indirectCustomer"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.expirationDate"),
					getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractType") };
			setHeaderImage(workbook, sheet, (InputStream) map.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY), headers.length);
			for (int i = 0; i < headers.length; i++)
			{
				final String headerName = headers[i];
				row.createCell(i + 1).setCellValue(headerName.toUpperCase());
				row.getCell(i + 1).setCellStyle(createLabelCell(workbook));
			}

			final CellStyle css = createValueCell(workbook);
			int entryRowNumber = rowNumber++;
			final String emptyField = StringUtils.EMPTY;
			if (results != null && !results.isEmpty())
			{
				for (final JnjContractData data : results)
				{
					sheet.autoSizeColumn(entryRowNumber);
					final Row rowData = sheet.createRow(entryRowNumber++);
					final Cell cell1 = rowData.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(data.getEccContractNum()));
					cell1.setCellStyle(css);

					final Cell cell2 = rowData.createCell(2);
					cell2.setCellValue(new HSSFRichTextString(data.getTenderNum()));
					cell2.setCellStyle(css);

					final Cell cell3 = rowData.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(data.getIndirectCustomer()));
					cell3.setCellStyle(css);

					final Cell cell4 = rowData.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(
							(data.getEndDate() != null) ? dateFormat.format(data.getEndDate()) : emptyField));
					cell4.setCellStyle(css);

					final Cell cell5 = rowData.createCell(5);
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
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage("Latam Contract EXCEL Download", "buildExcelDocument()", "Error While downloading EXCEL",
					e, JnjLatamContractExcelView.class);
		}
	}

	/**
	 * @param hssfWorkbook
	 * @param sheet
	 * @param logoPath
	 */
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream, final int colLen)
	{
		int index = 0;
		try
		{
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		}
		catch (final IOException ioException)
		{
			JnjGTCoreUtil.logErrorMessage("Set Excel Header", "setHeaderImage()",
					"Exception occured during input output operation in the method setHeaderImage()", ioException,
					JnjLatamContractExcelView.class);

		}
		final CreationHelper helper = hssfWorkbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setCol2(colLen);
		anchor.setRow1(1);
		anchor.setRow2(5);
		anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
		drawing.createPicture(anchor, index);
	}

	protected static CellStyle createLabelCell(final Workbook workbook)
	{
		final CellStyle cellStyle = workbook.createCellStyle();
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

	protected static CellStyle createValueCell(final Workbook workbook)
	{
		final CellStyle cellStyle = workbook.createCellStyle();
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
