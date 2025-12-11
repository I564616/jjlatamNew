/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.b2b.jnjlaordertemplate.controllers.pages;

import com.jnj.b2b.jnjlaordertemplate.constants.ContentType;
import com.jnj.b2b.storefront.breadcrumb.Breadcrumb;
import com.jnj.b2b.storefront.constants.WebConstants;
import com.jnj.b2b.storefront.controllers.pages.AbstractSearchPageController;
import com.jnj.b2b.storefront.controllers.util.GlobalMessages;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facade.util.JnjCommonFacadeUtil;
import com.jnj.facades.util.JnjLatamCommonFacadeUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnJLALanguageDateFormatUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractJnjLaBasePageController extends AbstractSearchPageController {

    private static final String SESSION_LANGUAGE_PATTERN = "sessionlanguagePattern";
    private static final Logger LOGGER = Logger.getLogger(AbstractJnjLaBasePageController.class);
    private static final String SEARCH_OPTIONS = "searchOptions";
    private static final String SYS_MASTER = "sys_master";
    private static final String EPIC_EMAIL_LOGO_IMAGE_ONE = "epicEmailLogoImageOne";
    private static final String JNJ_CONNECT_LOGO_URL = "jnjConnectLogoURL";
    private static final String JNJ_CONNECT_LOGO_URL_2 = "jnjConnectLogoURL2";
    private static final String SITE_LOGO_PATH = "siteLogoPath";
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String SESSION_LANGUAGE = "sessionLanguage";
    private static final String INVOICE_PDF_PROPERTY = "download.invoice.pdf.countries";
    private static final String INVOICE_XML_PROPERTY = "download.invoice.xml.countries";
    private static final String HIDDEN_MESSAGE_SHOW_ATTRIBUTE = "hiddenMessageShow";
    private static final String HEADER_FILENAME = "Content-Disposition";

    @Autowired
    protected JnjCommonFacadeUtil jnjCommonFacadeUtil;

    @Autowired
    protected UserService userService;

    @Resource(name = "sessionService")
    protected SessionService sessionService;

    @Autowired
    protected MediaService mediaService;

    @Autowired
    protected CatalogVersionService catalogVersionService;

    @Autowired
    private CommonI18NService commonI18NService;

    @Autowired
    private JnjGetCurrentDefaultB2BUnitUtil b2bUnitUtil;

    @Autowired
    private JnjLatamCommonFacadeUtil jnjLatamCommonFacadeUtil;

    protected void preparePage(final Model model, final String cmsPage, final String breadcrumbKey){
        storeCmsPageInModel(model, cmsPage);
        handleSessionLanguage(model);
        prepareChangeAccountLink(model);
        addSimpleBreadcrumb(model, jnjCommonFacadeUtil.getMessageFromImpex(breadcrumbKey));
    }

    void handleSessionLanguage(final Model model) {
        model.addAttribute(SESSION_LANGUAGE_PATTERN, getLanguageSpecificDatePattern(model));

        String language = DEFAULT_LANGUAGE;
        if (commonI18NService.getCurrentLanguage() != null) {
            language = commonI18NService.getCurrentLanguage().getIsocode().toLowerCase();
        }
        getSessionService().getCurrentSession().setAttribute(SESSION_LANGUAGE, language);
    }

    private String getLanguageSpecificDatePattern(final Model model) {
        if (model.asMap().get("currentLanguage") != null) {
            final LanguageData lang = (LanguageData) model.asMap().get("currentLanguage");
            return JnJLALanguageDateFormatUtil.getLanguageSecificDatePattern(lang.getIsocode().toLowerCase());
        }

        return JnJLALanguageDateFormatUtil.getLanguageSecificDatePattern(getCurrentUserLanguage());
    }

    private String getCurrentUserLanguage() {
        final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
        return currentUser.getSessionLanguage() == null ? "en" : currentUser.getSessionLanguage().getIsocode().toLowerCase();
    }

    protected JnJB2bCustomerModel getCurrentUser() {
        return (JnJB2bCustomerModel) userService.getCurrentUser();
    }

    protected void storeCmsPageInModel(final Model model, final String labelOrId){
        try {
            storeCmsPageInModel(model, getContentPageForLabelOrId(labelOrId));
        } catch (CMSItemNotFoundException exception) {
            LOGGER.error(labelOrId + " not found in current content", exception);
        }
    }

    private void addSimpleBreadcrumb(final Model model, final String name) {
        Breadcrumb breadcrumb = new Breadcrumb(null, name, null);
        model.addAttribute(WebConstants.BREADCRUMBS_KEY, Collections.singletonList(breadcrumb));
    }

    void addSearchOptions(final Model model, final String... keys) {
        model.addAttribute(SEARCH_OPTIONS, Arrays.asList(keys));
    }

    void populateSiteLogoForDownload(final Model model, final DownloadType downloadType) {

        final ContentCatalogModel catalog = getContentCatalogs().get(0);

        if (DownloadType.EXCEL.equals(downloadType)) {
            final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);
            final CatalogVersionModel currentCatalog = catalog.getActiveCatalogVersion();
            final Path siteLogoPath = Path.of(mediaDirBase, SYS_MASTER, mediaService.getMedia(currentCatalog, EPIC_EMAIL_LOGO_IMAGE_ONE).getLocation());
            model.addAttribute(SITE_LOGO_PATH, siteLogoPath.toString());
            model.addAttribute(Jnjlab2bcoreConstants.SITE_LOGO_PROPERTY, mediaService.getStreamFromMedia(mediaService.getMedia(currentCatalog, EPIC_EMAIL_LOGO_IMAGE_ONE)));
        } else if (DownloadType.PDF.equals(downloadType)) {
            final CatalogVersionModel onlineCatalog = catalogVersionService.getCatalogVersion(catalog.getId(), Jnjb2bCoreConstants.ONLINE);
            addLogoMedia(model, onlineCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE, JNJ_CONNECT_LOGO_URL);
            addLogoMedia(model, onlineCatalog, Jnjb2bCoreConstants.TemplateSearch.SITE_LOGO_IMAGE_2, JNJ_CONNECT_LOGO_URL_2);
        }

    }

    private void addLogoMedia(final Model model, final CatalogVersionModel onlineCatalog, final String logoName, final String modelAttribute) {
        final MediaModel mediaModel2 = mediaService.getMedia(onlineCatalog, logoName);
        if (mediaModel2 != null) {
            model.addAttribute(modelAttribute, mediaService.getStreamFromMedia(mediaModel2));
        }
    }

    private List<ContentCatalogModel> getContentCatalogs() {
        return getCmsSiteService().getCurrentSite().getContentCatalogs();
    }

    enum DownloadType {
        PDF, EXCEL, XML, NONE
    }

    void populatePdfXmlDownloadFlag(final Model model) {
        final CountryModel country = b2bUnitUtil.getCurrentCountryForSite();
        boolean hasDownloadAvailability = false;
        if (country != null) {
            final String countryIso = country.getIsocode();

            final List<String> pdfCountriesList = JnjLaCoreUtil.getCountriesList(INVOICE_PDF_PROPERTY);
            if (pdfCountriesList != null && pdfCountriesList.contains(countryIso)) {
                model.addAttribute("pfdInvoiceLinkFlag", Boolean.TRUE);
                hasDownloadAvailability = true;
            }

            final List<String> xmlCountriesList = JnjLaCoreUtil.getCountriesList(INVOICE_XML_PROPERTY);
            if (xmlCountriesList != null && xmlCountriesList.contains(countryIso)) {
                hasDownloadAvailability = true;
                if (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(countryIso)) {
                    model.addAttribute("brXmlInvoiceLinkFlag", Boolean.TRUE);
                } else {
                    model.addAttribute("xmlInvoiceLinkFlag", Boolean.TRUE);
                }
            }
        }
        model.addAttribute("hasDownloadAvailability", hasDownloadAvailability);
    }

    private void prepareChangeAccountLink(final Model model){
        jnjLatamCommonFacadeUtil.checkShowChangeAccountLink(model);
    }

    void addNotLoadedOrderMessage(final Model model) {
        model.addAttribute(HIDDEN_MESSAGE_SHOW_ATTRIBUTE, Boolean.TRUE);
    }

    protected void downloadFile(final File file, final ContentType contentType, final HttpServletResponse response) throws IOException {
        try (final FileInputStream fileInputStream = new FileInputStream(file);
             final ServletOutputStream servletOutputStream = response.getOutputStream()) {
            response.setContentType(contentType.getValue());
            response.setContentLength((int) file.length());
            response.setHeader(HEADER_FILENAME, "attachment; filename=" + file.getName());
            StreamUtils.copy(fileInputStream, servletOutputStream);
        } catch (final IOException e) {
            LOGGER.error("Error downloading file " + file, e);
            throw e;
        }
    }

    protected String redirect(String path) {
        return REDIRECT_PREFIX + path;
    }

    private void logErrorException(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
    }

    protected void handleExceptionWithMessage(Model model, String defaultExceptionCode, BusinessException exception) {
        logErrorException(exception);
        GlobalMessages.addErrorMessage(model, getExceptionCode(defaultExceptionCode, exception));
    }

    protected void handleExceptionWithFlashMessage(RedirectAttributes model, String defaultExceptionCode, BusinessException exception) {
        logErrorException(exception);
        addErrorFlashMessage(model, getExceptionCode(defaultExceptionCode, exception));
    }

    private String getExceptionCode(String defaultExceptionCode, BusinessException exception) {
        if (exception.getExceptionCode() != null) {
            return exception.getExceptionCode();
        }
        return defaultExceptionCode;
    }

    public static void addConfFlashMessage(final RedirectAttributes model, final String messageKey) {
        GlobalMessages.addFlashMessage(model, GlobalMessages.CONF_MESSAGES_HOLDER, messageKey, null);
    }

    public static void addErrorFlashMessage(final RedirectAttributes model, final String messageKey) {
        GlobalMessages.addFlashMessage(model, GlobalMessages.ERROR_MESSAGES_HOLDER, messageKey, null);
    }

}