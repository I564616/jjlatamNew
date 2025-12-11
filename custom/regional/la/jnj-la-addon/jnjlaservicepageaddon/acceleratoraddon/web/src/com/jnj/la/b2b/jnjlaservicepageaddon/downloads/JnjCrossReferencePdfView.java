package com.jnj.la.b2b.jnjlaservicepageaddon.downloads;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.io.InputStream;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import com.jnj.core.util.JnjGTCoreUtil;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjLatamUploadOrderData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.daos.impl.DefaultLaJnjOrderDao;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.jnj.core.dto.JnjCrossReferenceTableDTO;

/**
 * @author skant3
 *
 */

public class JnjCrossReferencePdfView extends AbstractPdfView
{

    private JnjCommonFacadeUtil JnjCommonFacadeUtil;

    /**
     * @return the jnjCommonFacadeUtil
     */
    public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
    {
        return JnjCommonFacadeUtil;
    }

    /**
     * @param jnjCommonFacadeUtil
     *            the jnjCommonFacadeUtil to set
     */
    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
    {
        this.JnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    
    @Override
    protected Document newDocument()
    {
        return new Document(PageSize.A3.rotate());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.view.document.AbstractPdfView#
     * buildPdfDocument(java.util.Map, com.lowagie.text.Document,
     * com.lowagie.text.pdf.PdfWriter, javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void buildPdfDocument(final Map<String, Object> arg0, final Document arg1, final PdfWriter arg2,
            final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
    {

        final String methodName = "buildPdfDocument()";
        setJnjCommonFacadeUtil(getJnjCommonFacadeUtil());
        
        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Forms.CROSS_REFERENCE_TABLE, methodName,
                "Starting the setting of Table in PDF Document", JnjCrossReferencePdfView.class);
        
        arg4.setHeader("Content-Disposition", "attachment; filename=CrossReference.pdf");
        
        final InputStream jnjConnectLogoIS = (InputStream) arg0.get("jnjConnectLogoURL");
        final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
        
        Image jnjConnectLogo = null;
        PdfPCell cell1 = null;
        final PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        table.setTotalWidth(822F);
        table.setLockedWidth(true);
        table.setSpacingAfter(30f);

        if (jnjConnectLogoByteArray != null) {
            jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
            cell1 = new PdfPCell(jnjConnectLogo, false);
            cell1.setBorder(Rectangle.NO_BORDER);
        }

        table.addCell(cell1);
        
        arg1.add(table);
        final PdfPTable orderLineTable = new PdfPTable(3);
        orderLineTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
        orderLineTable.setTotalWidth(1100F);
        orderLineTable.setLockedWidth(true);
        final String emptyField = new String();
        final Font boldFont = FontFactory.getFont("Arial", 12, Font.BOLD);
        final Paragraph header = new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("text.crossref.header"),
                boldFont);
        arg1.add(new Paragraph(header));
        arg1.add(new Paragraph("\n"));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.your.id"), boldFont)));
        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.jnj.id"), boldFont)));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.product.name"), boldFont)));

        final JnjCrossReferenceTableDTO jnjCrossReferenceTableDTO = (JnjCrossReferenceTableDTO) arg0
                .get("crossRefTable");
        final SearchPageData<JnjCrossReferenceTableDTO> list = jnjCrossReferenceTableDTO.getResultList();
        
        if (list != null) {
            for (final JnjCrossReferenceTableDTO crossReferenceTableDTO : list.getResults()) {
                orderLineTable.addCell(new PdfPCell(new Phrase((crossReferenceTableDTO.getClientProductID()))));

                orderLineTable.addCell(new PdfPCell(new Phrase((crossReferenceTableDTO.getJnjProductID()))));

                orderLineTable.addCell(new PdfPCell(new Phrase((crossReferenceTableDTO.getProductName()))));

            }
        }
        
        arg1.add(orderLineTable);
        arg1.close();
    }

}
