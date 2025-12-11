/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.b2b.browseandsearch.download.views;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.data.JnjGTProductData;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.util.Config;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.core.model.ProductDocumentsModel;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;


/**
 * Class responsible to create PDF view for Search Result page.
 */
public class JnjGTProductDetailsPdfView extends AbstractPdfView
{
    private static final Class<JnjGTProductDetailsPdfView> currentClass = JnjGTProductDetailsPdfView.class;

    private static final Logger LOG = Logger.getLogger(JnjGTProductDetailsPdfView.class);

    protected static final int MARGIN = 32;

    protected I18NService i18nService;

    protected MessageSource messageSource;

    protected JnJCommonUtil jnjCommonUtil;

    protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

    @Override
    protected Document newDocument()
    {
        return new Document(PageSize.A4);
    }

    @Override
    protected void buildPdfDocument(final Map<String, Object> arg0, final Document document, final PdfWriter pdfWriter,
                                    final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
    {
        final String METHOD_NAME = "buildPdfDocument()";
        final JnjGTProductData productData =  (JnjGTProductData) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT);
        LOG.error("Product Data Object"+productData);
        String siteLogoPath = (String) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY);
        String siteLogoPath1 = (String) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH1_PROPERTY);
        String productImage = (String) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_IMAGE_URL_PROPERTY);
        String franchiseLogo = (String) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.FRANCHISE_LOGO_PROPERTY);
        List<ProductDocumentsModel> productdocumentsList=(List<ProductDocumentsModel>) arg0.get(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY);
        final String siteName = (String) arg0.get(Jnjb2bCoreConstants.SITE_NAME);
        final boolean isMddSite = (siteName != null && Jnjb2bCoreConstants.MDD.equals(siteName)) ? true : false;
        final String htmlStringForPDF = generateVMForProductDetails(siteLogoPath,siteLogoPath1,productImage,productData,isMddSite,franchiseLogo,productdocumentsList);

        String finalFileName =productData.getCode() + Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_SUFFIX + Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_TYPE;

        arg4.setHeader("Content-Disposition", "attachment; filename="+finalFileName);
        /*page number add start here*/
        MyPageEvents events = new MyPageEvents(getMessageSourceAccessor());
        pdfWriter.setPageEvent(events);
        events.onOpenDocument(pdfWriter, document);

        try
        {
            final File file = File.createTempFile(Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_TEMPORARY_SUFFIX, Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_TYPE);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            final HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlStringForPDF));

            document.close();
        }
        catch (final Exception exception)
        {
            LOG.error(METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: " , exception);
        }



    }

    private String generateVMForProductDetails(String siteLogoPath,String siteLogoPath1,String productImage,final JnjGTProductData productData,final boolean isMddSite,String franchiseLogo,final List<ProductDocumentsModel> productDocumentsList)
    {
        final String METHOD_NAME = "generateProductDatails";

        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CLASS, Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_LIBRARY);

        LOG.info("Getting the vm file..");

        final String path = getClass().getResource(Config.getParameter(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAIL_PDF_PATH)).getPath();
        LOG.info("Found Product Detail PDF Path as:  " + path);

        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_PATH, path.substring(0, path.lastIndexOf('/')));
        LOG.info(Logging.HYPHEN + METHOD_NAME + Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_PATH + " :  " + path.substring(0, path.lastIndexOf('/')));
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CACHE, Boolean.TRUE);
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_MODIFICATION_CHECK_INTERVAL, "2");

        LOG.info(Logging.HYPHEN + METHOD_NAME + "Initializing Velocity Engine");
        velocityEngine.init();

        LOG.info(Logging.HYPHEN + METHOD_NAME + "Retrieving template from Velocity Engine");
        final Template template = velocityEngine.getTemplate(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAIL_PDF_VM);

        /*AEKL-3 Changes*/

        /*StringBuilder caseGtin = new StringBuilder();
        StringBuilder eachGtin = new StringBuilder();
        StringBuilder boxGtin = new StringBuilder();

        final List<String> casegtinList = productData.getMddSpecification().getCaseGtin();
        final List<String> eachgtinList = productData.getMddSpecification().getEachGtin();
        for(int i=0;i<casegtinList.size();i++) {
            caseGtin.append(casegtinList.get(i));

            if(i!= (casegtinList.size()-1)) {
                caseGtin.append(",");
            }

        }
        //AEKL-641
        if(CollectionUtils.isNotEmpty(casegtinList)) {
            boxGtin.append(casegtinList.get(0));
        }

        for(int i=0;i<eachgtinList.size();i++) {
            eachGtin.append(eachgtinList.get(i));

            if(i!= (eachgtinList.size()-1)) {
                eachGtin.append(",");
            }

        }*/

        /*AEKL-3 Changes*/
        final VelocityContext context = new VelocityContext();

        context.put(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY, siteLogoPath);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY, siteLogoPath1);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_IMAGE_PROPERTY, productImage);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.FRANCHISE_LOGO_PROPERTY, franchiseLogo);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DATA_PROPERTY,productData);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.IS_MDD_PROPERTY, isMddSite);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SELL_QUANTITY_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.SELL_QTY, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SHIP_QUANTITY_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.SHIP_QTY, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_CODE_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_CODE, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.GTIN_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.GTIN, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.UPC_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.UPC, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SHORT_OVER_VIEW_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.SHORT_OVER_VIEW, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.ABOUT_BRAND_TAB_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.ABOUT_BRAND_TAB, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAILS_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAILS_TAB, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.ADDITIONAL_INFORMATION_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.ADDITIONAL_INFORMATION_TAB, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_SPECIFICATION_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_SPECIFICATION, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.STATUS_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.STATUS, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.UNIT_MEASURE_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.UNIT_MEASURE, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DELIVERY_UNIT_MEASURE_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.DELIVERY_UNIT_MEASURE, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DEPTH_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.DEPTH, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.HEIGHT_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.HEIGHT, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.WIDTH_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.WIDTH, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.VOLUME_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.VOLUME, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.WEIGHT_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.WEIGHT, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DIVISION_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.DIVISION, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRICE_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRICE, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY, productDocumentsList);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.INFINITY_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.INFINITY, null, i18nService.getCurrentLocale()));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.NAN_PROPERTY, messageSource.getMessage(Jnjb2bbrowseandsearchConstants.PDP.NAN, null, i18nService.getCurrentLocale()));

        final StringWriter writer = new StringWriter();
        template.merge(context, writer);

        return String.valueOf(writer);
    }


    private static class MyPageEvents extends PdfPageEventHelper {

        protected MessageSourceAccessor messageSourceAccessor;


        protected PdfContentByte cb;


        protected PdfTemplate template;


        protected BaseFont bf = null;

        public MyPageEvents(MessageSourceAccessor messageSourceAccessor) {
            this.messageSourceAccessor = messageSourceAccessor;
        }

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            final String methodName = "onOpenDocument()";
            try {
                bf = BaseFont.createFont( BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED );
                cb = writer.getDirectContent();
                template = cb.createTemplate(50, 50);
            } catch (DocumentException de) {
                JnjGTCoreUtil.logErrorMessage("Opening pdf document", methodName, "Document exception :: " + de, currentClass);
            } catch (IOException ioe) {
                JnjGTCoreUtil.logErrorMessage("Opening pdf document", methodName, "IO Exception :: " + ioe, currentClass);
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            int pageN = writer.getPageNumber()-1;
            String text = messageSourceAccessor.getMessage("Page", "Page") + " " + pageN + " " +
                    messageSourceAccessor.getMessage("of", "of") + " ";
            float  len = bf.getWidthPoint( text, 8 );
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

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            template.beginText();
            template.setFontAndSize(bf, 8);
            template.showText(String.valueOf( writer.getPageNumber() - 2 ));
            template.endText();
        }
    }

    public static PdfPCell createImageCell(byte[] path, int align) {
        final String methodName = "createImageCell()";
        PdfPCell cell;

        Image img = null;
        try {
            img = Image.getInstance(path);
        } catch (BadElementException badElementException) {
            JnjGTCoreUtil.logErrorMessage("Bad element exception", methodName, "Exception :: " + badElementException, currentClass);
        } catch (IOException ioException) {
            JnjGTCoreUtil.logErrorMessage("IO exception", methodName, "Exception :: " + ioException, currentClass);
        }
        cell = new PdfPCell(img, false);
        labelImageCellStyle(cell,align);
        return cell;
    }

    public static void labelImageCellStyle(PdfPCell cell, int align){
        if(align ==2){
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }else{
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        }
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
    }

    public JnJCommonUtil getJnjCommonUtil() {
        return jnjCommonUtil;
    }

    public MessageSource getMessageSource()
    {
        return messageSource;
    }

    public I18NService getI18nService()
    {
        return i18nService;
    }

    public JnjCommonFacadeUtil getJnjCommonFacadeUtil()
    {
        return jnjCommonFacadeUtil;
    }
    public void setJnjCommonFacadeUtil(final JnjCommonFacadeUtil jnjCommonFacadeUtil)
    {
        this.jnjCommonFacadeUtil = jnjCommonFacadeUtil;
    }

    public void setMessageSource(final MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
        this.jnjCommonUtil = jnjCommonUtil;
    }

    public void setI18nService(final I18NService i18nService)
    {
        this.i18nService = i18nService;
    }
}