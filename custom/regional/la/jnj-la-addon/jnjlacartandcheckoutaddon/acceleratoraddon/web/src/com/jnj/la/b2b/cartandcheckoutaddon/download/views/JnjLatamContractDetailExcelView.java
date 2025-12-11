/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.b2b.cartandcheckoutaddon.constants.CartandcheckoutaddonWebConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjContractData;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjContractPriceData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author plahiri1
 *
 */
public class JnjLatamContractDetailExcelView extends AbstractXlsView
{

	private static final String CONTRACT_PDFEXCEL_DETAILS="text.contract.pdfexcel.details";
	private static final String TEXT_PRODUCT_NAME="text.product.name";
	private static final String CONTRACT_ITEM_PRICE="contract.itemPrice";
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
		final String sheetName = getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_PDFEXCEL_DETAILS);
		final String fileName = getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_PDFEXCEL_DETAILS) + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		final SimpleDateFormat dateFormatHHMMSS = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");

		final JnjContractData contractData = (JnjContractData) map.get("contractData");
		final List<JnjContractEntryData> jnjContractEntryDataList = (List<JnjContractEntryData>) map.get("contractEntryList");
		final Map<String, JnjContractPriceData> cntrctEntryMap = (Map<String, JnjContractPriceData>) map.get("cntrctEntryMap");
		final JnjContractPriceData cntrctPriceData = (JnjContractPriceData) map.get("cntrctPriceData");

		final Sheet sheet = workbook.createSheet(sheetName);
		setHeaderImage(workbook, sheet, (InputStream) map.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));
		int rowNumber = 6;

		final Row headerPart1 = sheet.createRow((short) rowNumber++); //1-5 is image part and 6 is header part
		final Row rowPart1 = sheet.createRow(rowNumber++);
		final Row rowPart2 = sheet.createRow(rowNumber++);
		final Row rowPart3 = sheet.createRow(rowNumber++);
		final Row rowPart4 = sheet.createRow(rowNumber++);
		final Row rowPart5 = sheet.createRow(rowNumber++);
		final Row rowPart6 = sheet.createRow(rowNumber++);
		final Row rowPart7 = sheet.createRow(rowNumber++);
		final Row header = sheet.createRow(rowNumber++);

		final Font font = workbook.createFont();
		font.setBold(true);

		final CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setWrapText(true);
		headerPart1.createCell(1).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_PDFEXCEL_DETAILS));
		headerPart1.getCell(1).setCellStyle(style);

		final String[] headersContractZCQ = new String[]
		{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
				getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.unitOfMeasure"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractQty"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumed"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.balance") };

		final String[] headersContractZCV = new String[]
		{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
				getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.totalValue"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumedValue"),
				getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractBalance") };

		final String[] headersContractZCI = new String[]
				{ getJnjCommonFacadeUtil().getMessageFromImpex(TEXT_PRODUCT_NAME),
						getJnjCommonFacadeUtil().getMessageFromImpex(CONTRACT_ITEM_PRICE),
						getJnjCommonFacadeUtil().getMessageFromImpex("contract.unitOfMeasure"),
						getJnjCommonFacadeUtil().getMessageFromImpex("contract.contractQty"),
						getJnjCommonFacadeUtil().getMessageFromImpex("contract.consumed"),
						getJnjCommonFacadeUtil().getMessageFromImpex("contract.balance") };

		extractedContractZCQ(workbook, contractData, header, headersContractZCQ);

		extractedContractZCV(workbook, contractData, header, headersContractZCV);
		extractedContractZCI(workbook, contractData, header, headersContractZCI);
		final CellStyle css = createValueCell(workbook);
		int entryRowNumber = rowNumber++;
		final String emptyField = StringUtils.EMPTY;
	    extractedContractEntryData(contractData, jnjContractEntryDataList, cntrctEntryMap, sheet, css, entryRowNumber);

		///start
		final Cell cell11 = rowPart1.createCell(1);
		cell11.setCellValue(new HSSFRichTextString(
				getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONTRACTNUMBER)));
		cell11.setCellStyle(createLabelCell(workbook));

		final Cell cell12 = rowPart1.createCell(2);
		cell12.setCellValue(new HSSFRichTextString(contractData.getEccContractNum()));
		cell12.setCellStyle(css);

		final Cell cell13 = rowPart1.createCell(3);
		cell13.setCellValue(
				new HSSFRichTextString(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_STATUS)));
		cell13.setCellStyle(createLabelCell(workbook));

		final Cell cell14 = rowPart1.createCell(4);
		cell14.setCellValue(new HSSFRichTextString(contractData.getStatus() != null ? contractData.getStatus() : emptyField));
		cell14.setCellStyle(css);

		final Cell cell15 = rowPart1.createCell(5);
		cell15.setCellValue(
				new HSSFRichTextString(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CREATION_DATE)));
		cell15.setCellStyle(createLabelCell(workbook));

		final Cell cell16 = rowPart1.createCell(6);
		cell16.setCellValue(new HSSFRichTextString(
				contractData.getStartDate() != null ? dateFormat.format(contractData.getStartDate()).toString() : emptyField));
		cell16.setCellStyle(css);

		/////
		final Cell cell21 = rowPart2.createCell(1);
		cell21.setCellValue(new HSSFRichTextString(
				getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_INDIRECTCUSTOMER)));
		cell21.setCellStyle(createLabelCell(workbook));

		final Cell cell22 = rowPart2.createCell(2);
		cell22.setCellValue(
				new HSSFRichTextString(contractData.getIndirectCustomer() != null ? contractData.getIndirectCustomer() : emptyField));
		cell22.setCellStyle(css);

		final Cell cell23 = rowPart2.createCell(3);
		cell23.setCellValue(
				new HSSFRichTextString(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_TENDERNUMBER)));
		cell23.setCellStyle(createLabelCell(workbook));

		final Cell cell24 = rowPart2.createCell(4);
		cell24.setCellValue(new HSSFRichTextString(contractData.getTenderNum() != null ? contractData.getTenderNum() : emptyField));
		cell24.setCellStyle(css);

		final Cell cell25 = rowPart2.createCell(5);
		cell25.setCellValue(new HSSFRichTextString(
				getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_EXPIRATIONDATE)));
		cell25.setCellStyle(createLabelCell(workbook));

		final Cell cell26 = rowPart2.createCell(6);
		cell26.setCellValue(new HSSFRichTextString(
				contractData.getEndDate() != null ? dateFormat.format(contractData.getEndDate()).toString() : emptyField));
		cell26.setCellStyle(css);

		final Cell cell31 = rowPart3.createCell(1);
		cell31.setCellValue(new HSSFRichTextString(
				getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_INDIRECTCUSTOMER_NAME)));
		cell31.setCellStyle(createLabelCell(workbook));

		final Cell cell32 = rowPart3.createCell(2);
		cell32.setCellValue(new HSSFRichTextString(
				contractData.getIndirectCustomerName() != null ? contractData.getIndirectCustomerName() : emptyField));
		cell32.setCellStyle(css);

		final Cell cell33 = rowPart3.createCell(3);
		cell33.setCellValue(
				new HSSFRichTextString(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONTRACTTYPE)));
		cell33.setCellStyle(createLabelCell(workbook));

		final Cell cell34 = rowPart3.createCell(4);
		cell34.setCellValue(new HSSFRichTextString(
				contractData.getContractOrderReason() != null ? contractData.getContractOrderReason() : emptyField));
		cell34.setCellStyle(css);

		final Cell cell35 = rowPart3.createCell(5);
		cell35.setCellValue(new HSSFRichTextString(
				getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_LAST_TIME_UPDATED)));
		cell35.setCellStyle(createLabelCell(workbook));

		final Cell cell36 = rowPart3.createCell(6);
		cell36.setCellValue(new HSSFRichTextString(contractData.getLastUpdatedTime() != null
				? dateFormatHHMMSS.format(contractData.getLastUpdatedTime()).toString() : emptyField));
		cell36.setCellStyle(css);

		rowPart4.createCell(1).setCellValue(emptyField);
		rowPart4.createCell(2).setCellValue(emptyField);
		rowPart4.createCell(3).setCellValue(emptyField);
		rowPart4.createCell(4).setCellValue(emptyField);
		
		if (contractData.getDocumentType()
				.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV) && cntrctPriceData != null)
		{
			rowPart5.createCell(1).setCellValue(emptyField);
			rowPart5.createCell(2).setCellValue(emptyField);
			rowPart5.createCell(3).setCellValue(emptyField);
			rowPart5.createCell(4).setCellValue(emptyField);

			final Cell cell45 = rowPart4.createCell(5);
			cell45.setCellValue(new HSSFRichTextString(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_TOTAL_CONTRACT_VALUE)));
			cell45.setCellStyle(createLabelCell(workbook));

			final Cell cell46 = rowPart4.createCell(6);
			cell46.setCellValue(new HSSFRichTextString(cntrctPriceData.getTotalAmount().getValue() != null
					? cntrctPriceData.getTotalAmount().getValue().toString() : emptyField));
			cell46.setCellStyle(css);

			final Cell cell55 = rowPart5.createCell(5);
			cell55.setCellValue(new HSSFRichTextString(
					getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_CONSUMED_VALUE)));
			cell55.setCellStyle(createLabelCell(workbook));

			final Cell cell56 = rowPart5.createCell(6);
			cell56.setCellValue(new HSSFRichTextString(cntrctPriceData.getConsumedAmount().getValue() != null
					? cntrctPriceData.getConsumedAmount().getValue().toString() : emptyField));
			cell56.setCellStyle(css);
			////
			rowPart6.createCell(1).setCellValue(emptyField);
			rowPart6.createCell(2).setCellValue(emptyField);
			rowPart6.createCell(3).setCellValue(emptyField);
			rowPart6.createCell(4).setCellValue(emptyField);

			final Cell cell65 = rowPart6.createCell(5);
			cell65.setCellValue(
					new HSSFRichTextString(getJnjCommonFacadeUtil().getMessageFromImpex(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_BALANCE)));
			cell65.setCellStyle(createLabelCell(workbook));

			final Cell cell66 = rowPart6.createCell(6);
			cell66.setCellValue(getRichTextStringBalanceAmount(cntrctPriceData, emptyField));
			cell66.setCellStyle(css);
			////
			rowPart7.createCell(1).setCellValue(emptyField);
			rowPart7.createCell(2).setCellValue(emptyField);
			rowPart7.createCell(3).setCellValue(emptyField);
			rowPart7.createCell(4).setCellValue(emptyField);
			rowPart7.createCell(5).setCellValue(emptyField);
			rowPart7.createCell(6).setCellValue(emptyField);
		}
		///end
	}

	private static HSSFRichTextString getRichTextStringBalanceAmount(JnjContractPriceData cntrctPriceData, String emptyField) {
		return new HSSFRichTextString(cntrctPriceData.getBalanceAmount().getValue() != null
				? cntrctPriceData.getBalanceAmount().getValue().toString() : emptyField);
	}

	private static void extractedContractZCV(Workbook workbook, JnjContractData contractData, Row header, String[] headersContractZCV) {
		if (contractData.getDocumentType()
				.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV))
		{
			for (int i = 0; i < headersContractZCV.length; i++)
			{
				final String headerName = headersContractZCV[i];
				header.createCell(i + 1).setCellValue(headerName.toUpperCase());
				header.getCell(i + 1).setCellStyle(createLabelCell(workbook));
			}
		}
	}

	private static void extractedContractZCQ(Workbook workbook, JnjContractData contractData, Row header, String[] headersContractZCQ) {
		if (contractData.getDocumentType()
				.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCQ))
		{
			for (int i = 0; i < headersContractZCQ.length; i++)
			{
				final String headerName = headersContractZCQ[i];
				header.createCell(i + 1).setCellValue(headerName.toUpperCase());
				header.getCell(i + 1).setCellStyle(createLabelCell(workbook));
			}
		}
	}

	private static void extractedContractZCI(Workbook workbook, JnjContractData contractData, Row header, String[] headersContractZCI) {
		if (Jnjlab2bcoreConstants.Contract.CONTRACT_ZCI.equalsIgnoreCase(contractData.getDocumentType()))
		{
			for (int i = 0; i < headersContractZCI.length; i++)
			{
				final String headerName = headersContractZCI[i];
				header.createCell(i + 1).setCellValue(headerName.toUpperCase());
				header.getCell(i + 1).setCellStyle(createLabelCell(workbook));
			}
		}
	}

	private static void extractedContractEntryData(JnjContractData contractData, List<JnjContractEntryData> jnjContractEntryDataList, Map<String, JnjContractPriceData> cntrctEntryMap, Sheet sheet, CellStyle css, int entryRowNumber) {
		if (jnjContractEntryDataList != null && !jnjContractEntryDataList.isEmpty())
		{
			for (final JnjContractEntryData jnjContractEntryData : jnjContractEntryDataList)
			{
				sheet.autoSizeColumn(entryRowNumber);
				final Row rowData = sheet.createRow(entryRowNumber++);
				final Cell cell1 = rowData.createCell(1);
				cell1.setCellValue(
						new HSSFRichTextString(jnjContractEntryData.getProductCode() + "\n" + jnjContractEntryData.getProductName()));
				cell1.setCellStyle(css);

				final Cell cell2 = rowData.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractProductPrice()));
				cell2.setCellStyle(css);

				if (contractData.getDocumentType()
						.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCQ))
				{
					final Cell cell3 = rowData.createCell(3);
					cell3.setCellValue(new HSSFRichTextString(jnjContractEntryData.getProdSalesUnit()));
					cell3.setCellStyle(css);

					final Cell cell4 = rowData.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractQty().toString()));
					cell4.setCellStyle(css);

					final Cell cell5 = rowData.createCell(5);
					cell5.setCellValue(new HSSFRichTextString(jnjContractEntryData.getConsumedQty().toString()));
					cell5.setCellStyle(css);

					final Cell cell6 = rowData.createCell(6);
					cell6.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractBalanceQty().toString()));
					cell6.setCellStyle(css);
				}

				checkContractZCI(contractData, css, jnjContractEntryData, rowData);

				checkContractZCV(contractData, cntrctEntryMap, css, jnjContractEntryData, rowData);
			}
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
		}
	}

	/**
	 * Check If Contract ZCV
	 * @param contractData
	 * @param cntrctEntryMap
	 * @param css
	 * @param jnjContractEntryData
	 * @param rowData
	 */
	private static void checkContractZCV(JnjContractData contractData, Map<String, JnjContractPriceData> cntrctEntryMap, CellStyle css, JnjContractEntryData jnjContractEntryData, Row rowData) {
		if (contractData.getDocumentType()
				.equalsIgnoreCase(CartandcheckoutaddonWebConstants.JnjGTExcelPdfViewLabels.ContractList.CONTRACT_ZCV))
		{
			final String prodCode = jnjContractEntryData.getProductCode();
			final JnjContractPriceData jnjContractPriceData = cntrctEntryMap.get(prodCode);
			if (jnjContractPriceData != null)
			{

				final Cell cell3 = rowData.createCell(3);
				cell3.setCellValue(new HSSFRichTextString(jnjContractPriceData.getTotalAmount().getValue() + ""));
				cell3.setCellStyle(css);

				final Cell cell4 = rowData.createCell(4);
				cell4.setCellValue(new HSSFRichTextString(jnjContractPriceData.getConsumedAmount().getValue() + ""));
				cell4.setCellStyle(css);

				final Cell cell5 = rowData.createCell(5);
				cell5.setCellValue(new HSSFRichTextString(jnjContractPriceData.getBalanceAmount().getValue() + ""));
				cell5.setCellStyle(css);
			}
		}
	}

	/**
	 * Check If Contract ZCI
	 * @param contractData
	 * @param css
	 * @param jnjContractEntryData
	 * @param rowData
	 */
	private static void checkContractZCI(JnjContractData contractData, CellStyle css, JnjContractEntryData jnjContractEntryData, Row rowData) {
		if (Jnjlab2bcoreConstants.Contract.CONTRACT_ZCI.equalsIgnoreCase(contractData.getDocumentType()))
		{
			final Cell cell3 = rowData.createCell(3);
			cell3.setCellValue(new HSSFRichTextString(jnjContractEntryData.getProdSalesUnit()));
			cell3.setCellStyle(css);

			final Cell cell4 = rowData.createCell(4);
			cell4.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractQty().toString()));
			cell4.setCellStyle(css);

			final Cell cell5 = rowData.createCell(5);
			cell5.setCellValue(new HSSFRichTextString(jnjContractEntryData.getConsumedQty().toString()));
			cell5.setCellStyle(css);

			final Cell cell6 = rowData.createCell(6);
			cell6.setCellValue(new HSSFRichTextString(jnjContractEntryData.getContractBalanceQty().toString()));
			cell6.setCellStyle(css);
		}
	}

	/**
	 * @param hssfWorkbook
	 * @param sheet
	 * @param inputStream
	 */
	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream)
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
					JnjLatamContractDetailExcelView.class);
		}
		final CreationHelper helper = hssfWorkbook.getCreationHelper();
		final Drawing drawing = sheet.createDrawingPatriarch();
		final ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(0);
		anchor.setCol2(10);
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
		cellStyle.setWrapText(true);
		return cellStyle;
	}
}
