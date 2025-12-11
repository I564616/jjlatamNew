/**
 *
 */
package com.jnj.b2b.jnjlaordertemplate.download;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTOrderTemplateData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * @author plahiri1
 *
 */
public class JnJLatamOrderTemplatePdfView extends AbstractPdfView
{

    private JnjCommonFacadeUtil jnjCommonFacadeUtil;

    protected static final String ORDER_TEMPLATE_DATA_LIST = "orderTemplate";
    private static final int MARGIN = 32;

    @Override
    protected Document newDocument()
    {
        return new Document(PageSize.A4.rotate());
    }

    /**
     * This method generates the PDF doc
     */
    @Override
    protected void buildPdfDocument(final Map<String, Object> map, final Document document, final PdfWriter pdfWriter,
            final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
    {
        try
        {
            arg4.setHeader("Content-Disposition",
                    "attachment; filename=" + Jnjb2bCoreConstants.TemplateSearch.PDF_FILE_NAME + ".pdf");
            final List<JnjGTOrderTemplateData> jnjGTOrderTemplateDataList = (List<JnjGTOrderTemplateData>) map
                    .get(ORDER_TEMPLATE_DATA_LIST);

            final PdfPTable orderTemplateTable = new PdfPTable(5);
            orderTemplateTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            orderTemplateTable.setTotalWidth(822F);
            orderTemplateTable.setLockedWidth(true);
            orderTemplateTable.setSpacingAfter(736f);
            orderTemplateTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateName"));
            orderTemplateTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateAuthor"));
            orderTemplateTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateCreated"));
            orderTemplateTable
                    .addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateShareStatus"));
            orderTemplateTable.addCell(getJnjCommonFacadeUtil().getMessageFromImpex("text.template.templateLines"));

            // page number add start here
            final MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
            pdfWriter.setPageEvent(events);
            events.onOpenDocument(pdfWriter, document);

            // page number add end here

            // image adding start
            final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
            final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
            Image jnjConnectLogo = null;
            PdfPCell cell1 = null;
            final PdfPTable imageTable = new PdfPTable(1); // 3 columns.
            imageTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            imageTable.setTotalWidth(822F);
            imageTable.setLockedWidth(true);
            imageTable.setSpacingAfter(30f);

            if (jnjConnectLogoByteArray != null)
            {
                jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
                cell1 = new PdfPCell(jnjConnectLogo, false);
                cell1.setBorder(Rectangle.NO_BORDER);
            }

            imageTable.addCell(cell1);
            // image adding end

            final PdfPCell pdfCell = new PdfPCell();
            pdfCell.setFixedHeight(50f);

            final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

            if (null != jnjGTOrderTemplateDataList)
            {
                for (final JnjGTOrderTemplateData jnjGTOrderTemplateData : jnjGTOrderTemplateDataList)
                {
                    orderTemplateTable.addCell(jnjGTOrderTemplateData.getTemplateName() == null ? pdfCell
                            : new PdfPCell(new Phrase(jnjGTOrderTemplateData.getTemplateName())));
                    orderTemplateTable.addCell(jnjGTOrderTemplateData.getAuthor() == null ? pdfCell
                            : new PdfPCell(new Phrase(jnjGTOrderTemplateData.getAuthor())));
                    orderTemplateTable.addCell(jnjGTOrderTemplateData.getCreatedOn() == null ? pdfCell
                            : new PdfPCell(new Phrase(dateFormatter.format(jnjGTOrderTemplateData.getCreatedOn()))));
                    orderTemplateTable.addCell(jnjGTOrderTemplateData.getShareStatus() == null ? pdfCell
                            : new PdfPCell(new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex(
                                    "text.template.templateShareStatus." + jnjGTOrderTemplateData.getShareStatus()))));
                    orderTemplateTable.addCell(jnjGTOrderTemplateData.getLines() == null ? pdfCell
                            : new PdfPCell(new Phrase(jnjGTOrderTemplateData.getLines().toString())));
                }

            }
            document.add(imageTable);

            document.add(orderTemplateTable);
        }
        catch (final Exception exception)
        {
            JnjGTCoreUtil.logErrorMessage("Build PDF Document", "buildPdfDocument", "Error while creating PDF - ",
                    exception, JnJLatamOrderTemplatePdfView.class);
        }
    }

    private static class MyPageEvents extends PdfPageEventHelper
    {

        private final MessageSourceAccessor messageSourceAccessor;

        private PdfContentByte cb;

        private PdfTemplate template;

        private BaseFont bf = null;

        public MyPageEvents(final MessageSourceAccessor messageSourceAccessor)
        {
            this.messageSourceAccessor = messageSourceAccessor;
        }

        public void onOpenDocument(final PdfWriter writer, final Document document)
        {
            try
            {
                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                cb = writer.getDirectContent();
                template = cb.createTemplate(50, 50);
            }
            catch (final DocumentException de)
            {
                JnjGTCoreUtil.logErrorMessage("DocumentException", "onOpenDocument()", "Error while creating PDF - ",
                        de, JnJLatamOrderTemplatePdfView.class);
            }
            catch (final IOException ioe)
            {
                JnjGTCoreUtil.logErrorMessage("IOException", "onOpenDocument()", "Error while opening PDF - ", ioe,
                        JnJLatamOrderTemplatePdfView.class);
            }
        }

        public void onEndPage(final PdfWriter writer, final Document document)
        {
            final int pageN = writer.getPageNumber();
            final String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " "
                    + messageSourceAccessor.getMessage("on", "on") + " ";
            final float len = bf.getWidthPoint(text, 8);
            cb.beginText();
            cb.setFontAndSize(bf, 8);

            cb.setTextMatrix(MARGIN, 16);
            cb.showText(text);
            cb.endText();

            cb.addTemplate(template, MARGIN + len, 16);
            cb.beginText();
            cb.setFontAndSize(bf, 8);

            cb.endText();
        }

        public void onCloseDocument(final PdfWriter writer, final Document document)
        {
            template.beginText();
            template.setFontAndSize(bf, 8);
            template.showText(String.valueOf(writer.getPageNumber() - 1));
            template.endText();
        }
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
