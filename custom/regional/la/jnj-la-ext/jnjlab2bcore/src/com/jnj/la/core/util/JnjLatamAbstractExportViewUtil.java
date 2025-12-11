/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.core.util;

import com.jnj.la.core.export.JnjLatamAbstractBaseExport;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.io.IOUtils;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class JnjLatamAbstractExportViewUtil extends JnjLatamAbstractBaseExport {

    private static final Class<JnjLatamAbstractExportViewUtil> currentClass = JnjLatamAbstractExportViewUtil.class;

    public static final String RESULTS_EXCEEDED_MESSAGE = "More than 1,000 orders matched your search results and the first 1,000 have been included in this file.";

    public JnjLatamAbstractExportViewUtil(MessageSourceAccessor messageSourceAccessor) {
        super(messageSourceAccessor);
    }

    public static void addPdfPageNumber(final Document document, final PdfWriter pdfWriter, JnjLatamAbstractBaseExport events) {
        pdfWriter.setPageEvent(events);
        events.onOpenDocument(pdfWriter, document);
    }

    public static PdfPTable setPdfImageHeader(final Map<String, Object> map, final float width,
                                              final Boolean isWidthLocked, final float spacingAfter,
                                              final int horizontalAlignment) throws IOException, BadElementException {
        final InputStream jnjConnectLogoIS = (InputStream) map.get("jnjConnectLogoURL");
        final byte[] jnjConnectLogoByteArray = IOUtils.toByteArray(jnjConnectLogoIS);
        Image jnjConnectLogo;
        PdfPCell cell1 = null;
        PdfPTable table = new PdfPTable(1);
        setPdfTableProperties(width, isWidthLocked, spacingAfter, horizontalAlignment, table);

        if (jnjConnectLogoByteArray != null) {
            jnjConnectLogo = Image.getInstance(jnjConnectLogoByteArray);
            cell1 = new PdfPCell(jnjConnectLogo, Boolean.FALSE);
            cell1.setBorder(Rectangle.NO_BORDER);
        }
        table.addCell(cell1);

        return table;
    }

    public static void setPdfTableProperties(final float width, final Boolean isWidthLocked, final float spacingAfter,
                                              final int horizontalAlignment, final PdfPTable table) {
        table.setHorizontalAlignment(horizontalAlignment);
        table.setTotalWidth(width);
        table.setLockedWidth(isWidthLocked);
        table.setSpacingAfter(spacingAfter);
    }
}
