/**
 * 
 */
package com.jnj.b2b.browseandsearch.download.views;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.b2b.browseandsearch.controllers.Jnjb2bbrowseandsearchControllerConstants;
import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTProductData;
import org.springframework.web.servlet.view.document.AbstractXlsView;


/**
 * @author t.e.sharma
 * 
 */
public class JnjProducSearchResultExcelView extends AbstractXlsView
{
	protected static final String RESULT_LIMIT_EXCEEDED_MESSAGE = "More than 1,000 products matched your search results and the first 1,000 have been included in this file. Please refer to the site FAQ for instruction on how to download a full product catalog.";
	/**
	 * Private instance of <code>JnjCommonFacadeUtil</code>
	 */
	protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

	protected static final String sheetName = "Product-Catalog(MDD)";
	
	private static final String DISCLAIMER = "Product.search.disclaimer";
	private static final String RESULTS = "Product.search.category.results";
	private static final String DOWNLOAD = "Product.search.download";
	private static final String ACC_NUMBER = "Product.search.account.number";
	private static final String PRODUCT_CODE = "Product.search.product.code";
	private static final String GTIN = "Product.search.product.gtin";
	private static final String PRODUCT_DESC = "Product.search.product.description";
	private static final String PRICE = "Product.search.price";
	private static final String UPC = "Product.search.upc";
	private static final String MEASURE = "Product.search.unit.measure";
	private static final String SHIP_NAME = "Product.search.ship.name";
	
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

	@Override
	protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook, final HttpServletRequest arg2,
									  final HttpServletResponse arg3) throws Exception
	{
		arg3.setHeader("Content-Disposition", "attachment; filename=Product_Catalog.xls");


		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = (ProductSearchPageData<SearchStateData, ProductData>) map
				.get("searchPageData");

		final List<ProductData> results = searchPageData.getResults();
		final Boolean isMddSite = (map.get("isMddSite") != null) ? (Boolean) map.get("isMddSite") : Boolean.FALSE;
		final String currentAccount = (String) map.get("currentAccount");
		final String currentAccountName = (String) map.get("currentAccountName");
		final boolean displayPrice = (searchPageData.getPagination() != null && searchPageData.getPagination()
				.getTotalNumberOfResults() > 200) ? false : true;
		final String pricingDisclaimer = displayPrice ? getJnjCommonFacadeUtil().getMessageFromImpex(
				Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.PRICING_DISCLAIMER_MSG) : getJnjCommonFacadeUtil()
				.getMessageFromImpex(Jnjb2bbrowseandsearchControllerConstants.JnjGTExcelPdfViewLabels.ProductSearch.NO_PRICING_DISCLAIMER_MSG);

		final Boolean resultLimitExceeded = (Boolean) map.get("resultLimitExceeded");

		final Sheet sheet = hssfWorkbook.createSheet(sheetName);

		/*** ***/
		final Font font = hssfWorkbook.createFont();
		font.setBold(true);
		final CellStyle style = hssfWorkbook.createCellStyle();
		style.setFont(font);
		CellStyle css = createValueCell(hssfWorkbook);
		/*** ***/
		int rowNumber = 6;
		if (resultLimitExceeded.booleanValue())
		{
			final Row note = sheet.createRow(rowNumber++);

			note.createCell(0).setCellValue(RESULT_LIMIT_EXCEEDED_MESSAGE);
		}
		final Row firstHeader = sheet.createRow(rowNumber++);
		
		setHeaderImage(hssfWorkbook, sheet, (String) map.get("siteLogoPath"));
		
		firstHeader.createCell(0).setCellValue(messageSource.getMessage(DISCLAIMER, null, i18nService.getCurrentLocale()));
		firstHeader.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));;

		firstHeader.createCell(1).setCellValue(messageSource.getMessage(RESULTS, null, i18nService.getCurrentLocale()));
		firstHeader.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));

		firstHeader.createCell(2).setCellValue(messageSource.getMessage(DOWNLOAD, null, i18nService.getCurrentLocale()));
		firstHeader.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));

		/*** ***/
		final Date currentDate = new Date();
		final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());

		final Row secondHeader = sheet.createRow(rowNumber++);
		Cell cel0 = secondHeader.createCell(0);
		cel0.setCellValue(new HSSFRichTextString(pricingDisclaimer));
		cel0.setCellStyle(css);
		if (!StringUtils.isEmpty(searchPageData.getFreeTextSearch()))
		{
			Cell cel1 = secondHeader.createCell(1);
			cel1.setCellValue(new HSSFRichTextString(searchPageData.getFreeTextSearch()));
			cel1.setCellStyle(css);			
			
		}
		else
		{
			Cell cel1 = secondHeader.createCell(1);
			cel1.setCellValue(new HSSFRichTextString(searchPageData.getCategoryCode()));
			cel1.setCellStyle(css);
			
		}
		Cell cel2 = secondHeader.createCell(2);
		cel2.setCellValue(new HSSFRichTextString(sdf.format(currentDate)));
		cel2.setCellStyle(css);

		/*** ***/
		final Row header = sheet.createRow(rowNumber++);
		header.createCell(0).setCellValue(messageSource.getMessage(ACC_NUMBER, null, i18nService.getCurrentLocale()));
		header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));

		header.createCell(1).setCellValue(messageSource.getMessage(PRODUCT_CODE, null, i18nService.getCurrentLocale()));
		header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));

		header.createCell(2).setCellValue(messageSource.getMessage(GTIN, null, i18nService.getCurrentLocale()));
		header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));

		header.createCell(3).setCellValue(messageSource.getMessage(PRODUCT_DESC, null, i18nService.getCurrentLocale()));
		header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));

		if (displayPrice)
		{
			header.createCell(4).setCellValue(messageSource.getMessage(PRICE, null, i18nService.getCurrentLocale()));
			header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
		}

		header.createCell(5).setCellValue(messageSource.getMessage(UPC, null, i18nService.getCurrentLocale()));
		header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));

		header.createCell(6).setCellValue(messageSource.getMessage(MEASURE, null, i18nService.getCurrentLocale()));
		header.getCell(6).setCellStyle(createLabelCell(hssfWorkbook));

		//JJEPIC-551
		
			header.createCell(7).setCellValue(messageSource.getMessage(SHIP_NAME, null, i18nService.getCurrentLocale()));
			header.getCell(7).setCellStyle(createLabelCell(hssfWorkbook));
		
		for (int columnNumber = 0; columnNumber <= header.getLastCellNum(); columnNumber++)
		{
			sheet.autoSizeColumn(columnNumber);
		}

		int entryRowNumber = rowNumber++;
		final String emptyField = new String();
		if (results != null && !results.isEmpty())
		{
			for (final ProductData productData : results)
			{
				final JnjGTProductData data = (JnjGTProductData) productData;

				sheet.autoSizeColumn(entryRowNumber);
				final Row row = sheet.createRow(entryRowNumber++);

				Cell cell0 = row.createCell(0);
				cell0.setCellValue(new HSSFRichTextString(currentAccount));
				cell0.setCellStyle(css);
				Cell cell1 = row.createCell(1);
				cell1.setCellValue(new HSSFRichTextString(data.getBaseMaterialNumber()));
				cell1.setCellStyle(css);
				Cell cell2 = row.createCell(2);
				cell2.setCellValue(new HSSFRichTextString(data.getGtin()));
				cell2.setCellStyle(css);
				Cell cell3 = row.createCell(3);
				cell3.setCellValue(new HSSFRichTextString(data.getDescription()));
				cell3.setCellStyle(css);
				if (displayPrice)
				{
					Cell cell4 = row.createCell(4);
					cell4.setCellValue(new HSSFRichTextString(data.getListPrice()));
					cell4.setCellStyle(css);
					
				}
				Cell cell5 = row.createCell(5);
				cell5.setCellValue(new HSSFRichTextString(data.getUpc()));
				cell5.setCellStyle(css);
				Cell cell6 = row.createCell(6);
				cell6.setCellValue(new HSSFRichTextString((data.getDeliveryUnit() != null) ? data.getDeliveryUnit() : emptyField + "("
								+ ((data.getNumerator() != null) ? data.getNumerator() : emptyField) + " "
								+ ((data.getSalesUnit() != null) ? data.getSalesUnit() : emptyField) + ")"));
                cell6.setCellStyle(css);
				//JJEPIC-551
                Cell cell7 = row.createCell(7);
				cell7.setCellValue(new HSSFRichTextString(currentAccountName));
				cell7.setCellStyle(css);					
					//row.createCell(7).setCellValue((data.getStatus() != null) ? data.getStatus().toString() : emptyField);
				
			}
		}
	}

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
