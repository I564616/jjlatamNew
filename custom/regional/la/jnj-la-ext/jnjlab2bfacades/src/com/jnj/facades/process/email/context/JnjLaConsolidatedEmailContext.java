/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.process.email.context;

import com.jnj.facades.services.JnJLaConsolidatedEmailService;
import com.jnj.facades.services.JnjLAEmailOrderStatusService;
import com.jnj.facades.services.JnjLaConsolidatedReportService;
import com.jnj.facades.services.impl.JnjLatamEmailGenerationService;
import com.jnj.la.core.data.order.JnjLAConsolidatedEmailData;
import com.jnj.la.core.data.order.JnjLAConsolidatedReportRowData;
import com.jnj.la.core.model.JnjLaConsolidatedEmailProcessModel;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jnj.la.core.enums.JnJEmailPeriodicity.CONSOLIDATED;

public class JnjLaConsolidatedEmailContext extends AbstractJnjLaEmailContext<JnjLaConsolidatedEmailProcessModel> {

    private static final Logger LOG = Logger.getLogger(JnjLaConsolidatedEmailContext.class);
    private static final String DEFAULT_TO = "jnj.la.consolidatedEmail.default.to";
    private static final String ORDER_HISTORY_PAGE_REL_PATH = "/store/%s/order-history";
    private static final String DEFAULT_TO_EMAIL_ID = "noreply-jjccla@jjcustomerconnect.com";

    @Autowired
    private JnjLAEmailOrderStatusService jnjLAEmailOrderStatusService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private JnJLaConsolidatedEmailService consolidatedEmailService;
    private Converter<AbstractOrderEntryModel, JnjLAConsolidatedReportRowData> jnjLaConsolidatedReportRowDataConverter;
    private JnjLaConsolidatedReportService jnjLaConsolidatedReportService;
    private JnjLAConsolidatedEmailData data;
    private EmailAttachmentModel attachment;
    private String orderHistoryUrl;

    @Override
    public void init(final JnjLaConsolidatedEmailProcessModel processModel, final EmailPageModel emailPageModel) {
        super.init(processModel, emailPageModel);

        data = new JnjLAConsolidatedEmailData();
        final List<AbstractOrderEntryModel> orderEntries = extractOrderEntries(processModel.getReportOrders());
        data.setReportRows(jnjLaConsolidatedReportRowDataConverter.convertAll(filterRows(orderEntries)));
        data.setCustomer(processModel.getUnit().getUid());

        defineEmailAddresses(processModel);

        Locale locale = JnjLaCommonUtil.getLocaleForCountryIsoCode(processModel.getUnit().getCountry().getIsocode());

        orderHistoryUrl = getOrderHistoryUrl(processModel, locale);
        attachment = jnjLaConsolidatedReportService.createConsolidatedReport(
                data.getReportRows(), processModel.getUnit(), locale);

        try {
            jnjLAEmailOrderStatusService.removePendingFromOrderEntries(CONSOLIDATED, orderEntries);
        } catch (Exception ex) {
            LOG.error("Could not remove pendingConsolidatedEmail flag for emails of unit: "
                    + processModel.getUnit(), ex);
        }
    }

    private List<AbstractOrderEntryModel> filterRows(List<AbstractOrderEntryModel> allRows) {
        final List<String> exclusionOrderStatuses = consolidatedEmailService.getExclusionOrderStatuses();
        return allRows.stream().filter(row ->
                !exclusionOrderStatuses.contains(row.getStatus())).collect(Collectors.toList());
    }

    private List<AbstractOrderEntryModel> extractOrderEntries(Collection<OrderModel> orders) {
        return orders.stream().flatMap(o -> o.getEntries().stream()).collect(Collectors.toList());
    }

    private void defineEmailAddresses(JnjLaConsolidatedEmailProcessModel processModel) {
        final List<String> defaultRecipients = processModel.getDefaultRecipients();
        final List<String> userRecipients = processModel.getUsers().stream()
                .map(B2BCustomerModel::getEmail).collect(Collectors.toList());

        final Set<String> allRecipients = new HashSet<>();
        allRecipients.addAll(defaultRecipients);
        allRecipients.addAll(userRecipients);
        
        List<String> orderNumbers= new ArrayList<>();
        for(OrderModel orderNumber:processModel.getReportOrders())
        {
        	orderNumbers.add(orderNumber.getSapOrderNumber());
        }

        setBcc(StringUtils.join(allRecipients, JnjLatamEmailGenerationService.EMAIL_ADDRESS_SEPARATOR));
        final String toEmail = StringUtils.isNotBlank(configurationService.getConfiguration().getString(DEFAULT_TO))
                ? configurationService.getConfiguration().getString(DEFAULT_TO):DEFAULT_TO_EMAIL_ID;
        setTo(toEmail);
        
        LOG.info("B2B Unit: " + processModel.getUnit().getUid() + " Recipients(bcc): " + allRecipients
                + " Reported orders: " + orderNumbers + " To email id: " + toEmail+" Process model: "
                + processModel.getPk().toString());
    }

    @Override
    protected BaseSiteModel getSite(final JnjLaConsolidatedEmailProcessModel model) {
        return model.getSite();
    }

    @Override
    protected CustomerModel getCustomer(final JnjLaConsolidatedEmailProcessModel model) {
        return model.getCustomer();
    }

    @Override
    protected LanguageModel getEmailLanguage(final JnjLaConsolidatedEmailProcessModel model) {
        return model.getLanguage();
    }

    @Override
    public List<EmailAttachmentModel> getAttachments() {
        if (attachment == null) {
            return super.getAttachments();
        }

        return Collections.singletonList(attachment);
    }

    public JnjLAConsolidatedEmailData getData() {
        return data;
    }

    public void setJnjLaConsolidatedReportRowDataConverter(final Converter<AbstractOrderEntryModel, JnjLAConsolidatedReportRowData> jnjLaConsolidatedReportRowDataConverter) {
        this.jnjLaConsolidatedReportRowDataConverter = jnjLaConsolidatedReportRowDataConverter;
    }

    public void setJnjLaConsolidatedReportService(final JnjLaConsolidatedReportService jnjLaConsolidatedReportService) {
        this.jnjLaConsolidatedReportService = jnjLaConsolidatedReportService;
    }

    private String getOrderHistoryUrl(final JnjLaConsolidatedEmailProcessModel processModel, final Locale locale) {
        final String language = locale.getLanguage().toLowerCase();
        return getSiteBaseUrlHttps(processModel) + String.format(ORDER_HISTORY_PAGE_REL_PATH, language);
    }

    public String getOrderHistoryUrl() {
        return orderHistoryUrl;
    }
}
