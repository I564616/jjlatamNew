/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.core.export;

import com.jnj.core.util.JnjGTCoreUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;

public abstract class JnjLatamAbstractBaseExport extends PdfPageEventHelper {

    private static final Class<JnjLatamAbstractBaseExport> currentClass = JnjLatamAbstractBaseExport.class;

    protected MessageSourceAccessor messageSourceAccessor;
    private static final int MARGIN = 32;

    // This is the PdfContentByte object of the writer
    protected PdfContentByte cb;

    // We will put the final number of pages in a template
    protected PdfTemplate template;

    // This is the BaseFont we are going to use for the header / footer
    protected BaseFont bf = null;

    public JnjLatamAbstractBaseExport(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    // we override the onOpenDocument method
    public void onOpenDocument(PdfWriter writer, Document document) {
        final String methodName = "onOpenDocument()";
        try {
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            cb = writer.getDirectContent();
            template = cb.createTemplate(50, 50);
        }
        catch (DocumentException documentException) {
            JnjGTCoreUtil.logErrorMessage("Open PDF file", methodName, "Error while creating template - ",
                documentException, currentClass);
        }
        catch (IOException ioException) {
            JnjGTCoreUtil.logErrorMessage("Open PDF document", methodName, "Error while opening PDF template - ",
                ioException, currentClass);
        }
    }

    // we override the onEndPage method
    public void onEndPage(PdfWriter writer, Document document){
        int pageN = writer.getPageNumber();
        String text = messageSourceAccessor.getMessage("page", "page") + " " + pageN + " "
            + messageSourceAccessor.getMessage("on", "on") + " ";
        float len = bf.getWidthPoint(text, 8);
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

    // we override the onCloseDocument method
    public void onCloseDocument(PdfWriter writer, Document document) {
        template.beginText();
        template.setFontAndSize(bf, 8);
        template.showText(String.valueOf(writer.getPageNumber() - 1));
        template.endText();
    }
}
