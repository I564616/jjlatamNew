package com.jnj.la.b2b.jnjlaselloutaddon.downloads;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.io.InputStream;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjLatamUploadOrderData;
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

/**
 * @author nbanerj1
 *
 */

public class JnjUploadOrderPdfView extends AbstractPdfView
{
    private static final Logger LOG = Logger.getLogger(JnjUploadOrderPdfView.class);


    private JnjCommonFacadeUtil JnjCommonFacadeUtil;

    /**
     * @return the jnjCommonFacadeUtil
     */

    public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
    {
        return JnjCommonFacadeUtil;
    }

    @Override
    protected Document newDocument()
    {
        return new Document(PageSize.A3.rotate());
    }


    /**
     * @param jnjCommonFacadeUtil
     *            the jnjCommonFacadeUtil to set
     */

    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
    {

        this.JnjCommonFacadeUtil = jnjCommonFacadeUtil;
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

        JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.SUBMIT_ORDER_EDI, "buildPdfDocument", "Starting the setting of Table in PDF Document", JnjUploadOrderPdfView.class);
        arg4.setHeader("Content-Disposition", "attachment; filename=UploadOrder.pdf");
        final InputStream jnjConnectLogoIS = (InputStream) arg0.get("jnjConnectLogoURL");
        final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
   
        Image jnjConnectLogo = null;
        Image jnjConnectLogo2 = null;
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

        final PdfPTable orderLineTable = new PdfPTable(6);
        orderLineTable.setHorizontalAlignment(Element.ALIGN_JUSTIFIED_ALL);
        orderLineTable.setTotalWidth(1100F);
        orderLineTable.setLockedWidth(true);
        final String emptyField = new String();
        final Font boldFont = FontFactory.getFont("Arial", 12, Font.BOLD);
        final Paragraph header = new Paragraph(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.header"),
                boldFont);
        arg1.add(new Paragraph(header));
        arg1.add(new Paragraph("\n"));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.document"), boldFont)));
        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.user"), boldFont)));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.customer"), boldFont)));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.date"), boldFont)));

        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.trackingID"), boldFont)));
        orderLineTable.addCell(new PdfPCell(
                new Phrase(getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.status"), boldFont)));

        final SearchPageData<JnjLatamUploadOrderData> searchPageData = (SearchPageData<JnjLatamUploadOrderData>) arg0
                .get(Jnjb2bCoreConstants.SellOutReports.DATA_LIST);

        // adding data table
        if (searchPageData != null) {
            for (final JnjLatamUploadOrderData jnjUploadOrderData : searchPageData.getResults()) {

                orderLineTable.addCell(new PdfPCell(new Phrase((jnjUploadOrderData.getDocName()))));

                orderLineTable.addCell(new PdfPCell(new Phrase((jnjUploadOrderData.getUser()))));

                orderLineTable.addCell(new PdfPCell(new Phrase((jnjUploadOrderData.getCustomer()))));

                orderLineTable.addCell(new PdfPCell(new Phrase((jnjUploadOrderData.getDate()))));

                if (null != jnjUploadOrderData.getTrackingID()) {
                    orderLineTable.addCell(new PdfPCell(new Phrase((jnjUploadOrderData.getTrackingID()))));
                }
                else
                {
                    orderLineTable.addCell(new PdfPCell(new Phrase((""))));
                }

                if (null != jnjUploadOrderData.getStatus()) {
                    
                        if(jnjUploadOrderData.getStatus().equalsIgnoreCase("Sent With Restrictions"))
                        {
                            orderLineTable.addCell(new PdfPCell(new Phrase((getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.partialSuccess")))));
                        }
                        if(jnjUploadOrderData.getStatus().equalsIgnoreCase("Error"))
                        {
                            orderLineTable.addCell(new PdfPCell(new Phrase((getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.error")))));
                        }
                       
                        if(jnjUploadOrderData.getStatus().equalsIgnoreCase("Sent Successfully"))
                        {
                            
                            orderLineTable.addCell(new PdfPCell(new Phrase((getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.success")))));
                        }
                        
                        if(jnjUploadOrderData.getStatus().equalsIgnoreCase("Received"))
                        {
                            
                            orderLineTable.addCell(new PdfPCell(new Phrase((getJnjCommonFacadeUtil().getMessageFromImpex("text.uploadOrder.received")))));
                        }
                }
                else
                {
                    orderLineTable.addCell(new PdfPCell(new Phrase((""))));
                }

            }
        }

        arg1.add(orderLineTable);
        arg1.close();

    }

}
