/*
 * Copyright: Copyright © 2018 This file contains trade secrets of Johnson & Johnson. No part may be
 * reproduced or transmitted in any form by any means or for any purpose without the express written
 * permission of Johnson & Johnson.
 */
package com.jnj.facades.process.email.context;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.data.order.JnjLAEmailOrderData;
import com.jnj.la.core.enums.JnjLAOrderStatusChangeEnum;
import com.jnj.la.core.model.JnjLAOrderStatusChangeProcessModel;
import com.jnj.la.core.services.order.impl.JnjLAOrderServiceImpl;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.store.BaseStoreModel;

import java.util.List;
import java.util.Locale;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Optional;
import org.apache.log4j.Logger;

public class JnjLAOrderStatusNotificationEmailContext
        extends AbstractJnjLaEmailContext<JnjLAOrderStatusChangeProcessModel> {
    private static final String EMAIL_STATUS_IMAGE = "emailStatusImage-%s-MAX-%s-PART-%s-%s";
    private static final String ORDER_DETAIL_PAGE_REL_PATH = "/store/%s/order-history/order/%s";
	private static final String DEFAULT_TO = "jnj.la.consolidatedEmail.default.to";
    private static final String FULLY_STATUS = "C";
    private static final String DUMMY_SAP_USER_ID = "dummy@sapuser.com";
	private static final List<String> partiallyStatus = new ArrayList<>();
	static {
		partiallyStatus.add("A");
		partiallyStatus.add("B");
		partiallyStatus.add(StringUtils.EMPTY);
	}
    private static final Integer PREVIOUSLY_STATUS_NOT_PARTIALLY = 0;
    private static final Integer ONLY_CURRENT_STATUS_IS_PARTIALLY = 1;
    private static final Integer PREVIOUSLY_STATUS_PARTIALLY = 2;
    private static final Integer BEFORE_PREVIOUSLY_STATUS_PARTIALLY = 3;
    private static final Integer UNTIL_INVOICE_STATUS = 6;
    private static final Integer PARTIALLY_SHIPPED_TO_SHIPPED = 8;
    private static final Integer PARTIALLY_DELIVERED_TO_DELIVERED = 10;

    private static final Logger LOG = Logger.getLogger(JnjLAOrderStatusNotificationEmailContext.class);
    
    private String orderDetailUrl;

    private String orderStatusImageURL;

    private JnjLAEmailOrderData orderData;

    private JnjLAOrderServiceImpl jnjLAOrderService;

    private Converter<OrderModel, JnjLAEmailOrderData> jnjLAEmailOrderConverter;

    @Override
    public void init(final JnjLAOrderStatusChangeProcessModel processModel,
            final EmailPageModel emailPageModel) {
       try
       {
    	   String emailPeriodicity = StringUtils.EMPTY;
           String b2bUnit = StringUtils.EMPTY;
           String sapOrderNumber = StringUtils.EMPTY;
           
    	   if(null != processModel.getOrder() && null != processModel.getOrder().getUnit()) {
    		    emailPeriodicity = processModel.getPeriodicity().getCode();
    		    b2bUnit = processModel.getOrder().getUnit().getUid();
    		    sapOrderNumber = processModel.getOrder().getSapOrderNumber();
           }
    	   
    	   LOG.info("Email periodicity########: " + emailPeriodicity + " B2B unit########: " + b2bUnit + " SAP order number########: "+ sapOrderNumber);
    	   addRequiredMissingFieldsOnOrder(processModel);
           orderData = jnjLAEmailOrderConverter.convert(processModel.getOrder());
           final CMSSiteModel site = (CMSSiteModel) getSite(processModel);
           orderStatusImageURL = getOrderStatusImageURL(processModel, site);
           orderDetailUrl = getOrderDetailUrl(processModel);
           LOG.info("Process model##### " +processModel.getPk().toString() + "User id########" + processModel.getCustomer().getUid() + "Locale####### "+processModel.getLanguage().getIsocode());
           super.init(processModel, emailPageModel);
           LOG.info("Base store######################## " +processModel.getStore().getUid());
           processModel.getPeriodicity();
           defineEmailAddress(processModel);
       }
       catch (Exception exception) 
       {
    	   LOG.error("Exception occured while generating the email##############: ", exception);
    	   throw new RuntimeException("Exception occured while generating the email: "+exception.getMessage());
       }
    }

    private void addRequiredMissingFieldsOnOrder(JnjLAOrderStatusChangeProcessModel processModel) {
        OrderModel order = processModel.getOrder();
        if (order.getSite() == null) {
            order.setSite(processModel.getSite());
        }
        if (order.getStore() == null) {
            order.setStore(processModel.getStore());
        }
    }

    private void defineEmailAddress(final JnjLAOrderStatusChangeProcessModel processModel) {
        if (processModel.getOrder().getUser() instanceof JnJB2bCustomerModel) {

            final String uid = processModel.getOrder().getUser().getUid();
            String bccEmailAddresses = StringUtils.EMPTY;
            boolean status =
                    jnjLAOrderService.getUserPreference(uid, processModel.getOrder().getUnit());
            if (status) {
                // Please use setBcc instead of EMAIL when adding more than one email, due to
                // privacy
                // reasons
                setTo(uid);
            } else {
                setTo(getConfigurationService().getConfiguration().getString(DEFAULT_TO));
            }
            final List<String> finalRecipients = jnjLAOrderService
                    .getAllRecipientsForImmediateOrDaily(processModel.getPeriodicity().getCode(),
                            processModel.getOrder().getUnit());
            if (CollectionUtils.isNotEmpty(finalRecipients)) {
                finalRecipients.remove(uid);
                bccEmailAddresses = StringUtils.join(finalRecipients, ";");
                if (StringUtils.isNotEmpty(bccEmailAddresses)) {
                    setBcc(bccEmailAddresses);
                }
            }
            String emailPeriodicity = StringUtils.EMPTY;
            String unit = StringUtils.EMPTY;
            String orderNumber = StringUtils.EMPTY;
            if(null !=processModel.getPeriodicity() ) {
            	emailPeriodicity = processModel.getPeriodicity().getCode();
            }
            if(null != processModel.getOrder() && null != processModel.getOrder().getUnit()) {
            	unit = processModel.getOrder().getUnit().getUid();
            	orderNumber = processModel.getOrder().getSapOrderNumber();
            }
            LOG.info("["+emailPeriodicity +" - "+processModel.getCode()+" EMAIL] B2B Unit: "+unit+" Recipients(bcc): "+bccEmailAddresses+" Reported order: "+orderNumber+" To email id: "+uid+" Process model: "+processModel.getPk().toString());

        }
    }

    private String getOrderStatusImageURL(final JnjLAOrderStatusChangeProcessModel processModel,
            final CMSSiteModel site) {
        final String orderHeaderStatusCode =
                processModel.getOrder().getStatus().getCode().toUpperCase(Locale.ENGLISH);
         final String language = setLanguageForDummyUser(processModel);
                
        final JnjLAOrderStatusChangeEnum statusChangeEnum =
                JnjLAOrderStatusChangeEnum.getValue(orderHeaderStatusCode);
        String emailImageStatus = StringUtils.EMPTY;

        if (statusChangeEnum != null) {
            final int statusWeight = statusChangeEnum.getStatusWeight();
            LOG.info("statusWeight:::::::::::::::::::::::: "+statusWeight);
            if (isOrderUntilInvoiceStatus(statusWeight)) {
                return buildEmailImageUrl(site, processModel, language, statusWeight,
                        PREVIOUSLY_STATUS_NOT_PARTIALLY, orderHeaderStatusCode);
            }

            if (isOrderUntilShippedStatus(statusWeight)) {
                final Optional<Integer> previouslyShippedStatusWeight =
                        getPreviouslyShippedStatusWeight(statusWeight, processModel);
                if (previouslyShippedStatusWeight.isPresent()) {
                    return buildEmailImageUrl(site, processModel, language, statusWeight,
                            previouslyShippedStatusWeight.get(), orderHeaderStatusCode);
                }
            }

            if (isOrderUntilDeliveredStatus(statusWeight)) {
                final Optional<Integer> previouslyDeliveredStatusWeight =
                        getPreviouslyDeliveryStatusPositionWeight(statusWeight, processModel);
                if (previouslyDeliveredStatusWeight.isPresent()) {
                    return buildEmailImageUrl(site, processModel, language, statusWeight,
                            previouslyDeliveredStatusWeight.get(), orderHeaderStatusCode);
                }
            }
        }
        return getSiteBaseUrlHttp(processModel) + getMediaService()
                .getMedia(getContentCatalogVersion(site), emailImageStatus).getURL();
    }

	private static String setLanguageForDummyUser(final JnjLAOrderStatusChangeProcessModel processModel) {
		final OrderModel order = processModel.getOrder();
        final String language;
        if(order.getUser().getUid().equalsIgnoreCase(DUMMY_SAP_USER_ID)) {
        	language= getStore(order).getDefaultLanguage().getIsocode().toUpperCase(Locale.ENGLISH);
        }else {
        	language =processModel.getOrder().getUser().getSessionLanguage().getIsocode().toUpperCase(Locale.ENGLISH);
        }
        LOG.info("Language details###################: "+language);
		return language;
	}
    private static BaseStoreModel getStore(final OrderModel orderModel) {
        return orderModel.getUnit().getCountry().getBaseStores().iterator().next();
    }
    private static boolean isOrderUntilDeliveredStatus(final int statusWeight) {
        return statusWeight <= PARTIALLY_DELIVERED_TO_DELIVERED;
    }

    private static boolean isOrderUntilShippedStatus(final int statusWeight) {
        return statusWeight <= PARTIALLY_SHIPPED_TO_SHIPPED;
    }

    private static boolean isOrderUntilInvoiceStatus(final int statusWeight) {
        return statusWeight <= UNTIL_INVOICE_STATUS;
    }

    private static Optional<Integer> getPreviouslyDeliveryStatusPositionWeight(final int statusWeight,
            final JnjLAOrderStatusChangeProcessModel processModel) {
        if (isACompleteStatus(statusWeight)) {
            return Optional.of(PREVIOUSLY_STATUS_NOT_PARTIALLY);
        } else if (isOrderPartiallyInvoiced(processModel)) {
            return Optional.of(BEFORE_PREVIOUSLY_STATUS_PARTIALLY);
        } else if (isOrderFullyInvoiced(processModel)) {
            final List<AbstractOrderEntryModel> entries = processModel.getOrder().getEntries();
            if (CollectionUtils.isNotEmpty(entries)) {
                if (isAnyEntryPartiallyShipped(entries)) {
                    return Optional.of(PREVIOUSLY_STATUS_PARTIALLY);
                }
                if (isOrderFullyShipped(entries)) {
                    return Optional.of(ONLY_CURRENT_STATUS_IS_PARTIALLY);
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<Integer> getPreviouslyShippedStatusWeight(final int statusWeight,
            final JnjLAOrderStatusChangeProcessModel processModel) {
        if (isACompleteStatus(statusWeight)) {
            return Optional.of(PREVIOUSLY_STATUS_NOT_PARTIALLY);
        } else if (isOrderPartiallyInvoiced(processModel)) {
            return Optional.of(PREVIOUSLY_STATUS_PARTIALLY);
        } else if (isOrderFullyInvoiced(processModel)) {
            return Optional.of(ONLY_CURRENT_STATUS_IS_PARTIALLY);
        }
        return Optional.empty();
    }

    private static boolean isOrderFullyShipped(final List<AbstractOrderEntryModel> entries) {
        final Optional<AbstractOrderEntryModel> anyNotFullyShippedEntry =
                getAnyNotFullyShippedEntry(entries);
        return !anyNotFullyShippedEntry.isPresent();
    }

    private static boolean isAnyEntryPartiallyShipped(final List<AbstractOrderEntryModel> entries) {
        final Optional<AbstractOrderEntryModel> anyPartiallyShippedEntry =
                getAnyPartiallyShippedEntry(entries);
        return anyPartiallyShippedEntry.isPresent();
    }

    private static Optional<AbstractOrderEntryModel> getAnyNotFullyShippedEntry(
            final List<AbstractOrderEntryModel> entries) {
        return entries.stream()
                .filter(abstractOrderEntryModel -> !(StringUtils.equals(
                        abstractOrderEntryModel.getCarrierEstDeliveryDateStatus().getCode(),
                        FULLY_STATUS)))
                .findFirst();
    }

    private static Optional<AbstractOrderEntryModel> getAnyPartiallyShippedEntry(
            final List<AbstractOrderEntryModel> entries) {
        return entries.stream()
                .filter(abstractOrderEntryModel -> partiallyStatus.contains(
                        abstractOrderEntryModel.getCarrierEstDeliveryDateStatus().getCode()))
                .findFirst();
    }

    private static boolean isOrderFullyInvoiced(final JnjLAOrderStatusChangeProcessModel processModel) {
        return StringUtils.equals(processModel.getOrder().getInvoiceStatus(), FULLY_STATUS);
    }

    private static boolean isACompleteStatus(final int statusWeight) {
        return statusWeight % 2 == 0;
    }

    private static boolean isOrderPartiallyInvoiced(
            final JnjLAOrderStatusChangeProcessModel processModel) {
        return partiallyStatus.contains(processModel.getOrder().getInvoiceStatus());
    }

    private String buildEmailImageUrl(final CMSSiteModel site,
            final JnjLAOrderStatusChangeProcessModel processModel, final String language,
            final int statusWeight, final Integer partiallyWeight,
            final String orderHeaderStatusCode) {
        final String emailImageStatus = String.format(EMAIL_STATUS_IMAGE, language, statusWeight,
                partiallyWeight, orderHeaderStatusCode);

        return getSiteBaseUrlHttp(processModel) + getMediaService()
                .getMedia(getContentCatalogVersion(site), emailImageStatus).getURL();
    }

    private String getOrderDetailUrl(final JnjLAOrderStatusChangeProcessModel processModel) {
    	final String language = setLanguageForDummyUser(processModel).toLowerCase();
    	 LOG.info("Detail URL path####################: " + getSiteBaseUrlHttps(processModel)
                 + String.format(ORDER_DETAIL_PAGE_REL_PATH, language, orderData.getCode()));
        return getSiteBaseUrlHttps(processModel)
                + String.format(ORDER_DETAIL_PAGE_REL_PATH, language, orderData.getCode());
    }

    @Override
    protected BaseSiteModel getSite(final JnjLAOrderStatusChangeProcessModel model) {
        return model.getSite();
    }

    @Override
    protected CustomerModel getCustomer(final JnjLAOrderStatusChangeProcessModel model) {
        return model.getCustomer();
    }

    @Override
    protected LanguageModel getEmailLanguage(final JnjLAOrderStatusChangeProcessModel model) {
        return model.getLanguage();
    }

    public void setJnjLAEmailOrderConverter(
            final Converter<OrderModel, JnjLAEmailOrderData> jnjLAEmailOrderConverter) {
        this.jnjLAEmailOrderConverter = jnjLAEmailOrderConverter;
    }
    
	public void setJnjLAOrderService(final JnjLAOrderServiceImpl jnjLAOrderService) {
		this.jnjLAOrderService = jnjLAOrderService;
	}
    
    public JnjLAEmailOrderData getOrderData() {
        return orderData;
    }

    public String getOrderStatusImageURL() {
        return orderStatusImageURL;
    }

    public String getOrderDetailUrl() {
        return orderDetailUrl;
    }
}
