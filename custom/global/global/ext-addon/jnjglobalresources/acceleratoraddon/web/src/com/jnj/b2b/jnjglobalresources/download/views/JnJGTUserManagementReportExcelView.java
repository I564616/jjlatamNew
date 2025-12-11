package com.jnj.b2b.jnjglobalresources.download.views;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.jnj.b2b.jnjglobalresources.form.JnjGTUserSearchForm;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.data.JnjGTCustomerData;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.i18n.I18NService;
import org.springframework.web.servlet.view.document.AbstractXlsView;

public class JnJGTUserManagementReportExcelView extends AbstractXlsView {

	@Autowired
	protected I18NService i18nService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
	private MessageSource messageSource;

	protected static final Logger LOG = Logger
			.getLogger(JnJGTUserManagementReportExcelView.class);
	protected static final String USER_MANAGEMENT_RESPONSE_DATA_LIST = "searchPageData";
	protected static final String USERMANAGEMENT_FORM_NAME = "JnjGTUserSearchForm";
	private static final String sheetName = "USERMANAGEMENT REPORT RESULTS";

	/**
	 * This method generates the Excel doc
	 */
	@Override
	protected void buildExcelDocument(final Map<String, Object> map,
			final Workbook workbook,
			final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse) throws Exception {
		try {
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=USERManagement_Report.xls");
			@SuppressWarnings("unchecked")
			final SearchPageData<JnjGTCustomerData> jnjUserManagementReportResponseDataList = (SearchPageData<JnjGTCustomerData>) map
					.get(USER_MANAGEMENT_RESPONSE_DATA_LIST);
			final JnjGTUserSearchForm searchCriteria = (JnjGTUserSearchForm) map
					.get(USERMANAGEMENT_FORM_NAME);

			final Sheet searchSheet = workbook.createSheet(sheetName);
			setHeaderImage(workbook, searchSheet,
					(String) map.get("siteLogoPath"));

			// START Font size needs to display
			final Font font = workbook.createFont();
			font.setBold(true);

			final CellStyle style = workbook.createCellStyle();
			style.setFont(font);
			// END Font size needs to display

			final Row searchRow6 = searchSheet.createRow(6);
			searchRow6.createCell(1).setCellValue("User Profile Search");
			searchRow6.getCell(1).setCellStyle(style);

			final Row searchRow8 = searchSheet.createRow(8);
			final Row searchRow9 = searchSheet.createRow(9);

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			String date = simpleDateFormat.format(new Date());
			SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm");
			String time = simpleTimeFormat.format(new Date());

			searchRow8.createCell(1).setCellValue("Download Date");
			searchRow8.getCell(1).setCellStyle(style);
			searchRow9.createCell(1).setCellValue("Download Time");
			searchRow9.getCell(1).setCellStyle(style);

			searchRow8.createCell(2).setCellValue(date);
			searchRow9.createCell(2).setCellValue(time + "  " + "IST");

			final Row searchRow11 = searchSheet.createRow(11);
			searchRow11.createCell(1).setCellValue("Search Criteria :");
			searchRow11.getCell(1).setCellStyle(style);

			final Row searchRow13 = searchSheet.createRow(13);
			final Row searchRow14 = searchSheet.createRow(14);
			final Row searchRow15 = searchSheet.createRow(15);
			final Row searchRow16 = searchSheet.createRow(16);
			final Row searchRow17 = searchSheet.createRow(17);
			final Row searchRow18 = searchSheet.createRow(18);
			final Row searchRow19 = searchSheet.createRow(19);
			final Row searchRow20 = searchSheet.createRow(20);
			final Row searchRow21 = searchSheet.createRow(21);

			searchRow13.createCell(1).setCellValue("Last Name");
			searchRow13.getCell(1).setCellStyle(style);
			searchRow14.createCell(1).setCellValue("First Name");
			searchRow14.getCell(1).setCellStyle(style);
			searchRow15.createCell(1).setCellValue("Account Number");
			searchRow15.getCell(1).setCellStyle(style);
			searchRow16.createCell(1).setCellValue("Account Name");
			searchRow16.getCell(1).setCellStyle(style);
			searchRow17.createCell(1).setCellValue("Status");
			searchRow17.getCell(1).setCellStyle(style);
			searchRow18.createCell(1).setCellValue("Phone");
			searchRow18.getCell(1).setCellStyle(style);
			searchRow19.createCell(1).setCellValue("Email");
			searchRow19.getCell(1).setCellStyle(style);
			searchRow20.createCell(1).setCellValue("Role");
			searchRow20.getCell(1).setCellStyle(style);
			searchRow21.createCell(1).setCellValue("Sector");
			searchRow21.getCell(1).setCellStyle(style);

			searchRow13.createCell(2)
					.setCellValue(searchCriteria.getLastName());
			searchRow14.createCell(2).setCellValue(
					searchCriteria.getFirstName());
			searchRow15.createCell(2).setCellValue(
					searchCriteria.getAccountNumber());
			searchRow16.createCell(2).setCellValue(
					searchCriteria.getAccountName());
			searchRow17.createCell(2).setCellValue(searchCriteria.getStatus());
			searchRow18.createCell(2).setCellValue(searchCriteria.getPhone());
			searchRow19.createCell(2).setCellValue(searchCriteria.getEmail());
			StringBuilder roleData1 = new StringBuilder();
			if (searchCriteria.getRole() != null) {
				if (!(searchCriteria.getRole().equalsIgnoreCase("ALL"))) {
					if (!searchCriteria.getRole().equals("b2bcustomergroup")) {
						String name = "b2busergroup."
								+ searchCriteria.getRole() + ".name";
						String role = messageSource.getMessage(name, null,
								i18nService.getCurrentLocale());
						roleData1.append(role + "     ");
						searchRow20.createCell(2).setCellValue(
								roleData1.toString());
					} else {
						searchRow20.createCell(2).setCellValue("");
					}
				} else {
					searchRow20.createCell(2).setCellValue("All");
				}
			}

			searchRow21.createCell(2).setCellValue(searchCriteria.getSector());

			final Row searchRow24 = searchSheet.createRow(24);
			searchRow24.createCell(1).setCellValue("Search  Results :");
			searchRow24.getCell(1).setCellStyle(style);

			final CellStyle styleCell = workbook.createCellStyle();
			styleCell.setWrapText(true);

			final Row header = searchSheet.createRow(27);
			font.setBold(true);
			CellStyle headerStyleCell = createLabelCell(workbook);
			headerStyleCell.setFont(font);
			header.createCell(1).setCellValue("Profile Name");
			header.getCell(1).setCellStyle(headerStyleCell);
			header.createCell(2).setCellValue("Role");
			header.getCell(2).setCellStyle(headerStyleCell);
			header.createCell(3).setCellValue("Email");
			header.getCell(3).setCellStyle(headerStyleCell);
			header.createCell(4).setCellValue("Status");
			header.getCell(4).setCellStyle(headerStyleCell);

			int rowNum = 28;
			List<JnjGTCustomerData> customerDataList = jnjUserManagementReportResponseDataList
					.getResults();
			if (null != customerDataList) {
				for (final JnjGTCustomerData jnjGTCustomerData : customerDataList) {
					final Row row = searchSheet.createRow(rowNum++);
					row.createCell(1).setCellValue(
							jnjGTCustomerData.getFirstName() + " "
									+ jnjGTCustomerData.getLastName());
					row.getCell(1).setCellStyle(createLabelCell(workbook));
					List<String> roleLists = (List<String>) jnjGTCustomerData
							.getRoles();
					StringBuilder roleData = new StringBuilder();
					for (String roleList : roleLists) {
						if (!roleList.equals("b2bcustomergroup")) {
							String name = "b2busergroup." + roleList + ".name";
							String role = messageSource.getMessage(name, null,
									i18nService.getCurrentLocale());
							roleData.append(role + "     ");
						}
					}
					row.createCell(2).setCellValue(roleData.toString());
					row.getCell(2).setCellStyle(createLabelCell(workbook));
					row.createCell(3)
							.setCellValue(jnjGTCustomerData.getEmail());
					row.getCell(3).setCellStyle(createLabelCell(workbook));
					row.createCell(4).setCellValue(
							jnjGTCustomerData.getStatus());
					row.getCell(4).setCellStyle(createLabelCell(workbook));
				}
			}
		} catch (final Exception exception) {
			LOG.error("Error while creating excel - " + exception.getMessage());
		}
	}

	public void setHeaderImage(final Workbook hssfWorkbook,
			final Sheet sheet, final String logoPath) {
		final CellStyle styleImgCell = hssfWorkbook.createCellStyle();
		styleImgCell.setAlignment(HorizontalAlignment.CENTER);
		// final Row header0 = sheet.createRow(0);
		InputStream inputStream = null;
		int index = 0;
		try {
			inputStream = new FileInputStream(logoPath);
			final byte[] bytes = IOUtils.toByteArray(inputStream);
			index = hssfWorkbook.addPicture(bytes,
					HSSFWorkbook.PICTURE_TYPE_JPEG);
			inputStream.close();
		} catch (final IOException ioException) {
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
		sheet.addMergedRegion(new CellRangeAddress(0, 4, 0, 9));
		// header0.getCell(0).setCellStyle(styleImgCell);
	}

	protected static CellStyle createLabelCell(Workbook workbook) {

		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());

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

	public I18NService getI18nService() {
		return i18nService;
	}

	public void setI18nService(I18NService i18nService) {
		this.i18nService = i18nService;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
