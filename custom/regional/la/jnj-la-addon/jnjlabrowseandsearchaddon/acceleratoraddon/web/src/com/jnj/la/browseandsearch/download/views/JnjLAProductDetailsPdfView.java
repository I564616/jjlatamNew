/**
 * Copyright: Copyright Â© 2020
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.la.browseandsearch.download.views;

import com.jnj.b2b.browseandsearch.constants.Jnjb2bbrowseandsearchConstants;
import com.jnj.b2b.browseandsearch.download.views.JnjGTProductDetailsPdfView;
import com.jnj.core.model.ProductDocumentsModel;
import com.jnj.facades.data.JnjGTMddSpecificationData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.la.browseandsearch.constants.JnjlabrowseandsearchaddonConstants;
import de.hybris.platform.site.BaseSiteService;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import de.hybris.platform.acceleratorservices.urlresolver.SiteBaseUrlResolutionService;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

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
 * This class is responsible for building the pdf for PDP.
 *
 */
public class JnjLAProductDetailsPdfView extends JnjGTProductDetailsPdfView {
    private static final Logger LOG = Logger.getLogger(JnjLAProductDetailsPdfView.class);

    private JnjGTProductData productData;
    private String siteLogoPath;
    private String siteLogoPath1;
    private String productImage;
    private String franchiseLogo;
    private List<ProductDocumentsModel> productDocumentsList;
    private boolean pdpTabsHide;
    private SiteBaseUrlResolutionService siteBaseUrlResolutionService;
    private BaseSiteService baseSiteService;

    @Override
    protected Document newDocument() {
        return new Document(PageSize.A4);
    }

    @Override
    protected void buildPdfDocument(final Map<String, Object> properties, final Document document, final PdfWriter pdfWriter,
                                    final HttpServletRequest request, final HttpServletResponse response) {

        productData = (JnjGTProductData) properties.get(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_PROPERTY);
        siteLogoPath = (String) properties.get(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY);
        siteLogoPath1 = (String) properties.get(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH1_PROPERTY);
        productImage = (String) properties.get(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_MAIN_IMAGE_FILE_PROPERTY);
        franchiseLogo = (String) properties.get(JnjlabrowseandsearchaddonConstants.PDP.FRANCHISE_LOGO_PDP_FILE_PROPERTY);
        productDocumentsList = (List<ProductDocumentsModel>) properties.get(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY);
        pdpTabsHide = (boolean) properties.get(JnjlabrowseandsearchaddonConstants.PDP.PDP_TABS_HIDE_FLAG_PROPERTY);

        final String htmlStringForPDF = generateVMForProductDetails();
        final String filename = productData.getCode() + Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_SUFFIX;
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_TYPE);

        final JnjLAPdfPageEvent pageEvent = new JnjLAPdfPageEvent(getMessageSourceAccessor());
        pdfWriter.setPageEvent(pageEvent);
        pageEvent.onOpenDocument(pdfWriter, document);

        try {
            final File file = File.createTempFile(filename + JnjlabrowseandsearchaddonConstants.PDP.PDF_FILE_TEMPORARY_SUFFIX, Jnjb2bbrowseandsearchConstants.PDP.PDF_FILE_TYPE);
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            document.add(new Chunk(""));
            final HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlStringForPDF));
        } catch (IOException | DocumentException exception) {
            LOG.error("Exception was throw while creating pdf: ", exception);
        } finally {
            document.close();
        }
    }

    /**
     * This method is to generate VM for PDP
     *
     * @return String the String to be returned
     */
    private String generateVMForProductDetails() {
        final Template template = getTemplate();
        final VelocityContext context = new VelocityContext();
        final StringWriter writer = new StringWriter();

        setValues(context);
        setLabels(context);

        setMddOnlyValues(context);

        template.merge(context, writer);
        return String.valueOf(writer);
    }

    /**
     * This method is to set the values for attributes for MDD site
     *
     * @param context
     *           the context to be used
     */
    private void setMddOnlyValues(final VelocityContext context)
    {
        final JnjGTMddSpecificationData mddspecificationData = productData.getMddSpecification();
        context.put(JnjlabrowseandsearchaddonConstants.PDP.DELIVERY_UOM_PROPERTY, mddspecificationData.getDeliveryUom());
        context.put(JnjlabrowseandsearchaddonConstants.PDP.DIVISION_VALUE, mddspecificationData.getDivision());
        context.put(JnjlabrowseandsearchaddonConstants.PDP.UOM_VALUE, mddspecificationData.getSalesUom());
    }

    /**
     * This method is to set the template
     *
     * @return Template the Template to be returned
     */
    private Template getTemplate() {
        final String path = getClass().getResource(Config.getParameter(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_DETAIL_PDF_PATH)).getPath();
        final VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CLASS, Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_LIBRARY);
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_PATH, path.substring(0, path.lastIndexOf('/')));
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_CACHE, Boolean.TRUE);
        velocityEngine.setProperty(Jnjb2bbrowseandsearchConstants.PDP.VELOCITY_ENGINE_FILE_RESOURCE_LOADER_MODIFICATION_CHECK_INTERVAL, "2");
        velocityEngine.init();

        return velocityEngine.getTemplate(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAIL_PDF_VM);
    }

    /**
     * This method is to set the values in context
     *
     * @param context
     *           the context to be used
     */
    private void setValues(final VelocityContext context) {
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH1_PROPERTY, siteLogoPath1);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SITE_LOGO_PATH_PROPERTY, siteLogoPath);
        context.put(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_IMAGE_PROPERTY, productImage);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.FRANCHISE_LOGO_PROPERTY, franchiseLogo);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DATA_PROPERTY, productData);
        context.put(Jnjb2bbrowseandsearchConstants.PDP.JNJ_PRODUCT_DOCUMENT_LIST_PROPERTY, productDocumentsList);
        context.put(JnjlabrowseandsearchaddonConstants.PDP.MEDIA_BASE_URL_PROPERTY, siteBaseUrlResolutionService.getMediaUrlForSite(baseSiteService.getCurrentBaseSite(), true));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.PDP_TABS_HIDE_FLAG_PROPERTY,Boolean.valueOf(pdpTabsHide));
    }

    /**
     * This method is to set the labels for attributes
     *
     * @param context
     *           the context to be used
     */
    private void setLabels(final VelocityContext context) {
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SELL_QUANTITY_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.SELL_QTY));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.SHIP_QUANTITY_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.SHIP_QTY));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.GTIN_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.GTIN));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.UPC_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.UPC));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.SHORT_OVER_VIEW_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.SHORT_OVER_VIEW));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.ABOUT_BRAND_TAB_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.ABOUT_BRAND_TAB));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_DETAILS_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_DETAILS_TAB));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.ADDITIONAL_INFORMATION_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.ADDITIONAL_INFORMATION_TAB));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.PRODUCT_SPECIFICATION_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_SPECIFICATION));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.STATUS_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.STATUS));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.UNIT_MEASURE_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.UNIT_MEASURE));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DELIVERY_UNIT_MEASURE_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.DELIVERY_UNIT_MEASURE));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.DELIVERED_IN_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.DELIVERED_IN));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.SOLD_IN_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.SOLD_IN));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.CONTAINS_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.CONTAINS));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.EACH_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.EACH));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.COUNTRY_OF_ORIGIN_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.COUNTRY_OF_ORIGIN));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DEPTH_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.DEPTH));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.HEIGHT_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.HEIGHT));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.WIDTH_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.WIDTH));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.VOLUME_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.VOLUME));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.WEIGHT_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.WEIGHT));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.DIVISION_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.DIVISION));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRICE_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRICE));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.INFINITY_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.INFINITY));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.NAN_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.NAN));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_NAMES_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_NAME));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_DESC_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_DESCRIPTION));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_UNITS_PROPERTY, getMessage(JnjlabrowseandsearchaddonConstants.PDP.KIT_COMPONENT_UNIT));
        context.put(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_CODE_PROPERTY, getMessage(Jnjb2bbrowseandsearchConstants.PDP.PRODUCT_CODE));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.ATTRIBUTES_PROPERTY , getMessage(JnjlabrowseandsearchaddonConstants.PDP.ATTRIBUTES));
        context.put(JnjlabrowseandsearchaddonConstants.PDP.OTHER_ATTRIBUTES_PROPERTY , getMessage(JnjlabrowseandsearchaddonConstants.PDP.OTHER_ATTRIBUTES));
    }

    private String getMessage(final String property) {
        return messageSource.getMessage(property, null, i18nService.getCurrentLocale());
    }

    protected SiteBaseUrlResolutionService getSiteBaseUrlResolutionService()
    {

        return siteBaseUrlResolutionService;
    }

    public void setSiteBaseUrlResolutionService(final SiteBaseUrlResolutionService siteBaseUrlResolutionService)
    {
        this.siteBaseUrlResolutionService = siteBaseUrlResolutionService;
    }

    protected  BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

}