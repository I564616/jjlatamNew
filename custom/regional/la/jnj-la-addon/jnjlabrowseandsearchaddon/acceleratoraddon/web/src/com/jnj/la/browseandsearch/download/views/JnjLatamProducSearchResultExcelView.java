/**
 *
 */
package com.jnj.la.browseandsearch.download.views;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTProductData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author plahiri1
 *
 */
public class JnjLatamProducSearchResultExcelView extends AbstractXlsView {

	/**
	 * Private instance of <code>JnjCommonFacadeUtil</code>
	 */
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook,
			final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception {
		arg3.setHeader("Content-Disposition", "attachment; filename=Product_Catalog.xls");
		final String RESULT_LIMIT_EXCEEDED_MESSAGE = getJnjCommonFacadeUtil()
				.getMessageFromImpex("search.result.limit.exceededmessage");
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = (ProductSearchPageData<SearchStateData, ProductData>) map
				.get("searchPageData");

		final String sheetName = getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.excelsheetname");
		final List<ProductData> results = searchPageData.getResults();
		final String currentAccount = (String) map.get("currentAccount");
		final String currentAccountName = (String) map.get("currentAccountName");
		boolean displayPrice = false;
		if (searchPageData.getPagination() != null && searchPageData.getPagination().getTotalNumberOfResults() > 200)
		{
			displayPrice = true;
		}
		final String pricingDisclaimer = displayPrice
				? getJnjCommonFacadeUtil().getMessageFromImpex(
						Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.PRICING_DISCLAIMER_MSG)
				: getJnjCommonFacadeUtil().getMessageFromImpex(
						Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.NO_PRICING_DISCLAIMER_MSG);

		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");

		final Sheet sheet = hssfWorkbook.createSheet(sheetName);

		final Font font = hssfWorkbook.createFont();
		font.setBold(true);
		final CellStyle style = hssfWorkbook.createCellStyle();
		style.setFont(font);

		int rowNumber = 6;
		if (resultLimitExceeded.booleanValue()) {
			final Row note = sheet.createRow(rowNumber++);

			note.createCell(0).setCellValue(RESULT_LIMIT_EXCEEDED_MESSAGE);
		}
		final Row firstHeader = sheet.createRow(rowNumber++);

		setHeaderImage(hssfWorkbook, sheet, (InputStream) map.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));

		firstHeader.createCell(0).setCellValue(
				getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.pricedeclaimer.header"));
		firstHeader.getCell(0).setCellStyle(style);

		firstHeader.createCell(1).setCellValue(
				getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.excelresults.header"));
		firstHeader.getCell(1).setCellStyle(style);

		firstHeader.createCell(2)
				.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.downloaddate"));
		firstHeader.getCell(2).setCellStyle(style);

		final Date currentDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		final Row secondHeader = sheet.createRow(rowNumber++);
		secondHeader.createCell(0).setCellValue(pricingDisclaimer);
		if (!StringUtils.isEmpty(searchPageData.getFreeTextSearch())) {
			secondHeader.createCell(1).setCellValue(searchPageData.getFreeTextSearch());
		} else {
			secondHeader.createCell(1).setCellValue(searchPageData.getCategoryCode());
		}
		secondHeader.createCell(2).setCellValue(sdf.format(currentDate));

		final Row header = sheet.createRow(rowNumber++);
		header.createCell(0)
				.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.accountnumber"));
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("cart.review.productCodes"));
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("product.pdfexcel.uom"));
		header.getCell(2).setCellStyle(style);

		header.createCell(3)
				.setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("product.search.download.shiptoname"));
		header.getCell(3).setCellStyle(style);

		for (int columnNumber = 0; columnNumber <= header.getLastCellNum(); columnNumber++) {
			sheet.autoSizeColumn(columnNumber);
		}

		int entryRowNumber = rowNumber++;
		final String emptyField = StringUtils.EMPTY;
		extractedProductSearchResults(results, currentAccount, currentAccountName, sheet, entryRowNumber, emptyField);
	}

	private static void extractedProductSearchResults(List<ProductData> results, String currentAccount, String currentAccountName, Sheet sheet, int entryRowNumber, String emptyField) {
		if (results != null && !results.isEmpty()) {
			for (final ProductData productData : results) {
				final JnjGTProductData data = (JnjGTProductData) productData;

				sheet.autoSizeColumn(entryRowNumber);
				final Row row = sheet.createRow(entryRowNumber++);

				row.createCell(0).setCellValue(currentAccount);
				row.createCell(1).setCellValue(data.getBaseMaterialNumber());
				row.createCell(2)
						.setCellValue((data.getDeliveryUnit() != null) ? data.getDeliveryUnit()
								: emptyField + "(" + ((data.getNumerator() != null) ? data.getNumerator() : emptyField)
										+ " " + ((data.getSalesUnit() != null) ? data.getSalesUnit() : emptyField)
										+ ")");
				row.createCell(3).setCellValue(currentAccountName);
			}
		}
	}

	/**
	 * @return the jnjCommonFacadeUtil
	 */
	public JnjCommonFacadeUtil getJnjCommonFacadeUtil() {
		return jnjCommonFacadeUtil;
	}

	/**
	 * @param jnjCommonFacadeUtil
	 *            the jnjCommonFacadeUtil to set
	 */
	public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil) {
		this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
	}

	public void setHeaderImage(final Workbook hssfWorkbook, final Sheet sheet, final InputStream inputStream) {
		int index = 0;
		try {
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes, HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		} catch (final IOException ioException) {

			JnjGTCoreUtil.logErrorMessage("IOException", "setHeaderImage",
					"Exception occured during input output operation in the method setHeaderImage() ", ioException,
					JnjLatamProducSearchResultExcelView.class);
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

}
