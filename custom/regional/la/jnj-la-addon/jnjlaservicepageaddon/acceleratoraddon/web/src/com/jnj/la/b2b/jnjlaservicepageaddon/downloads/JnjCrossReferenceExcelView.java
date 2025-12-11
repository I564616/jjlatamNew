/**
 *
 */
package com.jnj.la.b2b.jnjlaservicepageaddon.downloads;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.jnj.core.dto.JnjCrossReferenceTableDTO;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * Generates Excel view format for the searched product results.
 *
 * @author skant3
 *
 */
public class JnjCrossReferenceExcelView extends AbstractXlsView
{
    private JnjCommonFacadeUtil jnjCommonFacadeUtil;
    
    @Override
    protected void buildExcelDocument(final Map<String, Object> arg0, final Workbook arg1,
            final HttpServletRequest arg2, final HttpServletResponse arg3) throws Exception
    {

        final String methodName = "buildExcelDocument()";
        try
        {
            arg3.setHeader("Content-Disposition", "attachment; filename=CrossReference.xls");
            final JnjCrossReferenceTableDTO jnjCrossReferenceTableDTO = (JnjCrossReferenceTableDTO) arg0
                    .get("crossRefTable");
            final SearchPageData<JnjCrossReferenceTableDTO> list = jnjCrossReferenceTableDTO.getResultList();

            final Sheet sheet = arg1.createSheet("Cross Reference Table");

            final Row header = sheet.createRow(8);
            setHeaderImage(arg1, sheet, (String) arg0.get("siteLogoPath"));

            final Row title = sheet.createRow(6);
            title.createCell(0).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.crossref.header"));

            header.createCell(0).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.your.id"));
            header.getCell(0).setCellStyle(createLabelCell(arg1));
            header.createCell(1).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.jnj.id"));
            header.getCell(1).setCellStyle(createLabelCell(arg1));
            header.createCell(2).setCellValue(getJnjCommonFacadeUtil().getMessageFromImpex("text.product.name"));
            header.getCell(2).setCellStyle(createLabelCell(arg1));

            int rowNum = 9;

            for (final JnjCrossReferenceTableDTO crossReferenceTableDTO : list.getResults())
            {
                final Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(crossReferenceTableDTO.getClientProductID());
                row.createCell(1).setCellValue(crossReferenceTableDTO.getJnjProductID());
                row.createCell(2).setCellValue(crossReferenceTableDTO.getProductName());
            }
            
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                    "Rows set in HSSFSheet", JnjCrossReferenceExcelView.class);
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                    " Error while setting table -- " + exception.getMessage(), exception,
                    JnjCrossReferenceExcelView.class);
        }
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
            JnjGTCoreUtil.logErrorMessage("IOException", "setHeaderImage",
                    "Exception occured during input output operation in the method setHeaderImage() ", ioException,
                    JnjCrossReferenceExcelView.class);

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

    protected static CellStyle createValueCell(final HSSFWorkbook workbook)
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
