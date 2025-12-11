/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.b2b.jnjglobalordertemplate.forms.JnjGTTemplateDetailsForm;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTOrderTemplateEntryData;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author plahiri1
 *
 */
public class JnJLatamOrderTemplateDetailExcelView extends AbstractXlsView
{

    private JnjCommonFacadeUtil jnjCommonFacadeUtil;

    private static final String DATA_LIST = "orderTemplateDetail";
    private static final String DATA_LIST_TEMP = "templateEditForm";

    /**
     * This method generates the Excel doc
     */
    @Override
    protected void buildExcelDocument(final Map<String, Object> map, final Workbook hssfWorkbook,
            final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception
    {
        try
        {
            final JnjGTTemplateDetailsForm templateEditForm = (JnjGTTemplateDetailsForm) map.get(DATA_LIST_TEMP);
            arg3.setHeader("Content-Disposition", "attachment; filename=Template_Detail.xls");
            final List<JnjGTOrderTemplateEntryData> orderTemplateDatas = (List<JnjGTOrderTemplateEntryData>) map
                    .get(DATA_LIST);

            final Sheet sheet = hssfWorkbook.createSheet("Order Template Details");
            final Row header = sheet.createRow(9);
            setHeaderImage(hssfWorkbook, sheet, (InputStream) map.get(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY));

            final Row title = sheet.createRow(6);
            title.createCell(0)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.pdfDetailsHeader"));

            final Row name = sheet.createRow(7);
            name.createCell(0).setCellValue(templateEditForm.getTemplateName());

            header.createCell(0)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productName"));
            header.getCell(0).setCellStyle(createLabelCell(hssfWorkbook));
            header.createCell(1)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productCode"));
            header.getCell(1).setCellStyle(createLabelCell(hssfWorkbook));
            header.createCell(2)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productDescription"));
            header.getCell(2).setCellStyle(createLabelCell(hssfWorkbook));
            header.createCell(3)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productVolume"));
            header.getCell(3).setCellStyle(createLabelCell(hssfWorkbook));
            header.createCell(4)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productWeight"));
            header.getCell(4).setCellStyle(createLabelCell(hssfWorkbook));
            header.createCell(5)
                    .setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.productQuantity"));
            header.getCell(5).setCellStyle(createLabelCell(hssfWorkbook));
            final CellStyle css = createValueCell(hssfWorkbook);

            int rowNum = 10;
            if (null != orderTemplateDatas)
            {
                for (final JnjGTOrderTemplateEntryData orderTemplateData : orderTemplateDatas)
                {
                    final String deliveryUnit = orderTemplateData.getRefVariant().getDeliveryUnit() == null
                            ? Jnjb2bCoreConstants.SPACE : orderTemplateData.getRefVariant().getDeliveryUnit();
                    final String numerator = orderTemplateData.getRefVariant().getNumerator()
                            + Jnjb2bCoreConstants.SPACE + orderTemplateData.getRefVariant().getSalesUnit() == null
                                    ? Jnjb2bCoreConstants.SPACE
                                    : orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
                                            + orderTemplateData.getRefVariant().getSalesUnit();

                    final Row row = sheet.createRow(rowNum++);

                    final Cell cell0 = row.createCell(0);
                    cell0.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getName()));
                    cell0.setCellStyle(css);

                    final Cell cell1 = row.createCell(1);
                    cell1.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getCode()));
                    cell1.setCellStyle(css);
                    final Cell cell2 = row.createCell(2);
                    cell2.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getDescription()));
                    cell2.setCellStyle(css);
                    final Cell cell3 = row.createCell(3);
                    cell3.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getProductVolume()));
                    cell3.setCellStyle(css);

                    final Cell cell4 = row.createCell(4);
                    cell4.setCellValue(new HSSFRichTextString(orderTemplateData.getRefVariant().getProductWeight()));
                    cell4.setCellStyle(css);

                    final Cell cell5 = row.createCell(5);
                    if (StringUtils.isEmpty(numerator))
                    {
                        cell5.setCellValue(new HSSFRichTextString(
                                orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE + deliveryUnit));
                    }
                    else
                    {
                        cell5.setCellValue(new HSSFRichTextString(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
                                + orderTemplateData.getRefVariant().getDeliveryUnit() + "("
                                + orderTemplateData.getRefVariant().getNumerator() + Jnjb2bCoreConstants.SPACE
                                + orderTemplateData.getRefVariant().getSalesUnit() + ")"));
                    }

                    cell5.setCellValue(new HSSFRichTextString(orderTemplateData.getQty() + Jnjb2bCoreConstants.SPACE
                            + orderTemplateData.getRefVariant().getDeliveryUnit() + "(" + numerator + ")"));
                    cell5.setCellStyle(css);
                }

                sheet.autoSizeColumn(0);
                sheet.autoSizeColumn(1);
                sheet.autoSizeColumn(2);
                sheet.autoSizeColumn(3);
                sheet.autoSizeColumn(4);
                sheet.autoSizeColumn(5);
            }
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage("Build Excel Document", "buildExcelDocument", "Error while creating Excel - ",
                    exception, JnJLatamOrderTemplateDetailExcelView.class);
        }
    }

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
            JnjGTCoreUtil.logErrorMessage("IOException", "setHeaderImage",
                    "Exception occured during input output operation in the method setHeaderImage() ", ioException,
                    JnJLatamOrderTemplateDetailExcelView.class);

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

    public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
    {
        return jnjCommonFacadeUtil;
    }

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
    {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }
}
