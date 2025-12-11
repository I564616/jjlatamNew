/*
 * Copyright: Copyright © 2022 This file contains trade secrets of Johnson & Johnson. No part may be
 * reproduced or transmitted in any form by any means or for any purpose without the express written
 * permission of Johnson & Johnson.
 */
package com.jnj.facades.process.email.context;

import com.jnj.constants.JnjutilConstants;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailService;

import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnjInvoiceNotificationEmailDTO;
import com.jnj.la.core.dto.JnjInvoiceNotificationEntryDTO;
import com.jnj.la.core.model.JnjLaInvoiceNotificationEmailProcessModel;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.core.util.JnJCommonUtil;

import de.hybris.platform.util.Config;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;

/**
 * This class represents the email context for the Invoice-Notification Email process.
 */
public class JnjLaInvoiceNotificationEmailProcessContext extends CustomerEmailContext {

	private static final Logger LOG = LoggerFactory
			.getLogger(JnjLaInvoiceNotificationEmailProcessContext.class);
    private static final String INOVICEDATA = "invoicedata";
    private static final String ENTRIES = "entries";    
    private static final String COUNTRY_CODE = "countryCode";
    private static final String URL_PATH ="store";     
    private static final String CMS = "CMS";
    private static final String INVOICE_BASE_ULR = "invoiceBaseUrl";
    private static final String JNJ_ROOT_SERVER_URL_HTTPS = "jnj.root.server.url.https";
    public static final String INVOICE_NOTIFICATION_FROM_EMAIL_KEY = "invoice.notification.from.email";
    public static final String INVOICE_NOTIFICATION_FROM_EMAIL_NAME_KEY = "invoice.notification.from.email.displayname";  
    
    private List<JnjInvoiceNotificationEntryDTO> invoiceNotificationEmailEntries = new ArrayList<>();
    
    private CMSSiteService cmsSiteService;

    private JnjConfigService jnjConfigService;

    private DefaultEmailService emailService;    

    @Override
    public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel) {
    	try {

        final String METHOD_NAME = "init()";
        JnjGTCoreUtil.logDebugMessage(METHOD_NAME, "initialized context",
                "Start of Method-init()" + JnjutilConstants.Logging.HYPHEN + JnjutilConstants.Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(),
                JnjLaInvoiceNotificationEmailProcessContext.class);
        super.init(businessProcessModel, emailPageModel);
        if (businessProcessModel instanceof JnjLaInvoiceNotificationEmailProcessModel) {
            final JnjLaInvoiceNotificationEmailProcessModel invoiceNotificationEmailBusinessProcessModel = (JnjLaInvoiceNotificationEmailProcessModel) businessProcessModel;
            String b2bCustomerEmails = StringUtils.EMPTY;
            if(CollectionUtils.isNotEmpty(invoiceNotificationEmailBusinessProcessModel.getB2bCustomerEmails())) {
                final List<String> invoiceNotificationUsersEmailList = new ArrayList<>(
                        invoiceNotificationEmailBusinessProcessModel.getB2bCustomerEmails());
                b2bCustomerEmails = StringUtils.join(invoiceNotificationUsersEmailList, Jnjlab2bcoreConstants.LoadInvoices.SEMICOLON);
            }
            String data1 = invoiceNotificationEmailBusinessProcessModel.getInvoiceNotificationEmailData();
            JnjInvoiceNotificationEmailDTO invoiceNotificationEmailData = new JnjInvoiceNotificationEmailDTO();               
            
            final String[] dataArray = data1.split(Jnjlab2bcoreConstants.LoadInvoices.PIPE_SPLIT_EXPRESSION);
            invoiceNotificationEmailData.setAccountNumber(dataArray[0]);
            invoiceNotificationEmailData.setSapOrderNumber(dataArray[1]);
            invoiceNotificationEmailData.setCustomerPO(dataArray[2]);
            invoiceNotificationEmailData.setNotaFiscal(dataArray[3]);
            invoiceNotificationEmailData.setInvoiceNumber(dataArray[4]);
            invoiceNotificationEmailData.setOrderType(dataArray[5]);
            invoiceNotificationEmailData.setBillingName(dataArray[6]);
            invoiceNotificationEmailData.setBillingAddress(dataArray[7]);
            invoiceNotificationEmailData.setShippingAddress(dataArray[8]);
            invoiceNotificationEmailData.setOrderChannel(dataArray[9]);
            invoiceNotificationEmailData.setHybrisOrder(dataArray[10]);
            Locale locale = JnjLaCommonUtil.getLocaleForCountryIsoCode(dataArray[11]);

            for (final String data : invoiceNotificationEmailBusinessProcessModel.getInvoiceNotificationEmailEntries()) {
                final JnjInvoiceNotificationEntryDTO dto1 = new JnjInvoiceNotificationEntryDTO();

                final String[] dataArray1 = data.split(Jnjlab2bcoreConstants.LoadInvoices.PIPE_SPLIT_EXPRESSION);
                dto1.setProductName(dataArray1[0]);
                dto1.setProductcode(dataArray1[1]);
                dto1.setQuantity(dataArray1[2]);
                dto1.setShipUOM(dataArray1[3]);
                dto1.setEstimatedDeliveryDate(dataArray1[4]);
                dto1.setStatus(dataArray1[5]);
                invoiceNotificationEmailEntries.add(dto1);
                
            }
            String msg = "Prepared Data For Invoice Notification Email :" + invoiceNotificationEmailData.getInvoiceNumber();
            LOG.info(msg);
            put(ENTRIES,invoiceNotificationEmailEntries);
            put(INOVICEDATA,invoiceNotificationEmailData);
            put(EMAIL, b2bCustomerEmails);            
            put(COUNTRY_CODE, getCmsSiteService().getCurrentSite().getDefaultCountry().getIsocode());
            String urlpath = JnjLaInvoiceNotificationEmailProcessContext.getSiteBaseUrlHttps(invoiceNotificationEmailBusinessProcessModel, JNJ_ROOT_SERVER_URL_HTTPS) + Jnjlab2bcoreConstants.Forms.SLASH_SYMBOL +URL_PATH+ Jnjlab2bcoreConstants.Forms.SLASH_SYMBOL + locale.getLanguage().toLowerCase(Locale.getDefault());
            put(INVOICE_BASE_ULR, urlpath);
        }
        String fromEmail = jnjConfigService.getConfigValueById(INVOICE_NOTIFICATION_FROM_EMAIL_KEY);
        String fromEmailDisplayName = jnjConfigService.getConfigValueById(INVOICE_NOTIFICATION_FROM_EMAIL_NAME_KEY);
        put(FROM_EMAIL, fromEmail);
        put(FROM_DISPLAY_NAME, fromEmailDisplayName);
    	} catch (Exception exe){
    		LOG.info(exe.getMessage());
    		LOG.error(("Exception occurred while while sending the invoice notification email:" + exe
					.getMessage()) + "-" + JnJCommonUtil.getCurrentDateTime());
    	}

    }

    protected CMSSiteService getCmsSiteService() {
        return cmsSiteService;
    }

    public void setCmsSiteService(final CMSSiteService cmsSiteService) {
        this.cmsSiteService = cmsSiteService;
    }

    protected JnjConfigService getJnjConfigService() {
        return jnjConfigService;
    }

    public void setJnjConfigService(final JnjConfigService jnjConfigService) {
        this.jnjConfigService = jnjConfigService;
    }

    protected DefaultEmailService getEmailService() {
        return emailService;
    }

    public void setEmailService(final DefaultEmailService emailService) {
        this.emailService = emailService;
    }    
    
    private static String getSiteBaseUrlHttps(final JnjLaInvoiceNotificationEmailProcessModel processModel, String property) {
        final String url = Config.getString(property, StringUtils.EMPTY);
        if (StringUtils.isBlank(url)) {
            return StringUtils.EMPTY;
        }

        final String store = processModel.getSite().getUid().split(CMS)[0];
        String baseSiteUrl = String.format(url, store);
        String msg = "Base site URL::::::::::::::::::::::::::::: "+baseSiteUrl;
        LOG.info(msg);
        return baseSiteUrl;
    }



}