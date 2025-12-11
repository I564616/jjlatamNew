/*
 * Copyright: Copyright © 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.la.browseandsearch.download.views;

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

public class JnjLAPdfPageEvent extends PdfPageEventHelper {

    private static final Class<JnjLAPdfPageEvent> currentClass = JnjLAPdfPageEvent.class;
    private static final int MARGIN = 32;
    private static final int FONT_SIZE = 8;
    private static final int PAGE_NUMBER_SIZE = 2;
    private final MessageSourceAccessor messageSourceAccessor;
    private PdfContentByte contentByte;
    private PdfTemplate template;
    private BaseFont baseFont;

    public JnjLAPdfPageEvent(final MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @Override
    public void onOpenDocument(final PdfWriter writer, final Document document) {
        final String methodName = "onOpenDocument()";
        try {
            this.baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            this.contentByte = writer.getDirectContent();
            this.template = this.contentByte.createTemplate(50, 50);
        } catch (final DocumentException de) {
            JnjGTCoreUtil.logErrorMessage("Opening pdf document", methodName, "Document exception :: " + de, currentClass);
        } catch (final IOException ioe) {
            JnjGTCoreUtil.logErrorMessage("Opening pdf document", methodName, "IO Exception :: " + ioe, currentClass);
        }
    }

    @Override
    public void onEndPage(final PdfWriter writer, final Document document) {
        final int pageN = writer.getPageNumber() - 1;
        final String text = this.messageSourceAccessor.getMessage("page", "page") + " " + pageN + " " +
                this.messageSourceAccessor.getMessage("of", "of") + " ";
        final float len = this.baseFont.getWidthPoint(text, FONT_SIZE);

        this.contentByte.beginText();
        this.contentByte.setFontAndSize(this.baseFont, FONT_SIZE);
        this.contentByte.setTextMatrix(MARGIN, 16);
        this.contentByte.showText(text);
        this.contentByte.endText();
        this.contentByte.addTemplate(this.template, MARGIN + len, 16);
        this.contentByte.beginText();
        this.contentByte.setFontAndSize(this.baseFont, FONT_SIZE);
        this.contentByte.endText();
    }

    @Override
    public void onCloseDocument(final PdfWriter writer, final Document document) {
        final int pageCount = writer.getPageNumber() - PAGE_NUMBER_SIZE;
        this.template.beginText();
        this.template.setFontAndSize(this.baseFont, FONT_SIZE);
        this.template.showText(String.valueOf(pageCount));
        this.template.endText();
    }

}
