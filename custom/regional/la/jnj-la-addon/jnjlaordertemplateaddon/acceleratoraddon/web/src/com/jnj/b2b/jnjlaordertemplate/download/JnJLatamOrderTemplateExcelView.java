/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author plahiri1
 *
 */
public class JnJLatamOrderTemplateExcelView extends AbstractXlsView {

	private JnjCommonFacadeUtil jnjCommonFacadeUtil;

	protected static final String ORDER_TEMPLATE_DATA_LIST = "orderTemplate";
	protected static final String EMPTY_CELL = " ";

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook,
			final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {
		try {
			arg3.setHeader("Content-Disposition",
					"attachment; filename=" + Jnjb2bCoreConstants.TemplateSearch.EXCEL_FILE_NAME + ".xls");
			final List<JnjGTOrderTemplateData> jnjGTOrderTemplateDataList = (List<JnjGTOrderTemplateData>) map
					.get(ORDER_TEMPLATE_DATA_LIST);
			final Sheet sheet = hssfWorkbook.createSheet("Order Template List");
			final Row header = sheet.createRow(6);
			setHeaderImage(hssfWorkbook, sheet, (InputStream) map.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));
			header.createCell(0)
					.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateName"));
			header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(1)
					.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateAuthor"));
			header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(2)
					.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateCreated"));
			header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(3)
					.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateShareStatus"));
			header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
			header.createCell(4)
					.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateLines"));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
			final CellStyle css = createValueCell(hssfWorkbook);

			int rowNum = 7;
			if (null != jnjGTOrderTemplateDataList) {
				for (final JnjGTOrderTemplateData jnjGTOrderTemplateData : jnjGTOrderTemplateDataList) {
					final Row row = sheet.createRow(rowNum++);

					final Cell cell0 = row.createCell(0);
					cell0.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getTemplateName()));
					cell0.setCellStyle(css);

					final Cell cell1 = row.createCell(1);
					cell1.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getAuthor()));
					cell1.setCellStyle(css);
					final Cell cell2 = row.createCell(2);
					if (jnjGTOrderTemplateData.getCreatedOn() != null) {
						cell2.setCellValue(
								new HSSFRichTextString(dateFormatter.format(jnjGTOrderTemplateData.getCreatedOn())));
					} else {
						cell2.setCellValue(new HSSFRichTextString(EMPTY_CELL));
					}
					cell2.setCellStyle(css);
					final String status = jnjGTOrderTemplateData.getShareStatus();
					final Cell cell3 = row.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(getJnjCommonFacadeUtil()
							.getMessageFromImpex("text.template.templateShareStatus." + status)));
					cell3.setCellStyle(css);
					final Cell cell4 = row.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(jnjGTOrderTemplateData.getLines().floatValue() + ""));
					cell4.setCellStyle(css);
					int count = 1;
					count++;
				}
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);
				sheet.autoSizeColumn(3);
				sheet.autoSizeColumn(4);

			}
		}

		catch (final Exception exception) {
			JnjGTCoreUtil.logErrorMessage("Build Excel Document", "buildExcelDocument", "Error while creating Excel - ",
					exception, JnJLatamOrderTemplateExcelView.class);
		}
	}

	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream) {
		int index = 0;
		try {
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		} catch (final IOException ioException) {
			JnjGTCoreUtil.logErrorMessage("IOException", "setHeaderImage()",
					"Exception occured during input output operation in the method setHeaderImage() ", ioException,
					JnJLatamOrderTemplateExcelView.class);

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

	protected static CellStyle createLabelCell(final Workbook workbook) {

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

	protected static CellStyle createValueCell(final Workbook workbook) {

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

	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

}
