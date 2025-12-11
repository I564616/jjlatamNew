/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import com.jnj.la.core.model.JnjLAOrderStatusChangeProcessModel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;

public class JnjLAOrderStatusNotificationEventListener extends AbstractSiteEventListener<JnjLAOrderStatusNotificationEvent> {

    private static final String ORDER_STATUS_CHANGE_PROCESS = "JnjLAOrderStatusChangeProcess";

    private ModelService modelService;

    private BusinessProcessService businessProcessService;
    
    private CommerceCommonI18NService commerceCommonI18NService;
    
    
    @Override
    protected void onSiteEvent(final JnjLAOrderStatusNotificationEvent event) {
        final String processId = ORDER_STATUS_CHANGE_PROCESS + "-" + event.getOrder().getPk().toString() + "-" + event.getPeriodicity().getCode() + "-" + System.currentTimeMillis();
        final JnjLAOrderStatusChangeProcessModel processModel = businessProcessService.createProcess(processId, ORDER_STATUS_CHANGE_PROCESS);
        processModel.setOrder(event.getOrder());
        processModel.setCustomer((CustomerModel) event.getOrder().getUser());
        
        final BaseStoreModel store = getStore(event.getOrder());
        
        processModel.setStore(null!=event.getOrder().getStore()?event.getOrder().getStore():store);
        processModel.setSite(null!=event.getOrder().getSite()?event.getOrder().getSite():store.getCmsSites().iterator().next());
        processModel.setCurrency(null!=event.getOrder().getCurrency()?event.getOrder().getCurrency():commerceCommonI18NService.getDefaultCurrency());
        //fallback to default language of store 
        final LanguageModel b2bLanguage = null!=store.getDefaultLanguage()?store.getDefaultLanguage():getDefaultValue();
        processModel.setLanguage(null!=event.getOrder().getLanguage()?event.getOrder().getLanguage():b2bLanguage);
        processModel.setPeriodicity(event.getPeriodicity());
        modelService.save(processModel);
        businessProcessService.startProcess(processModel);
    }

    
    private static BaseStoreModel getStore(final OrderModel orderModel) {
        return orderModel.getUnit().getCountry().getBaseStores().iterator().next();
    }
    
    
	private LanguageModel getDefaultValue()
	{
		return commerceCommonI18NService.getDefaultLanguage();
	}
    @Override
    protected boolean shouldHandleEvent(JnjLAOrderStatusNotificationEvent event) {
        return true;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public void setBusinessProcessService(BusinessProcessService businessProcessService) {
        this.businessProcessService = businessProcessService;
    }


	public CommerceCommonI18NService getCommerceCommonI18NService() {
		return commerceCommonI18NService;
	}

    public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService) {
		this.commerceCommonI18NService = commerceCommonI18NService;
	}
}