/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services.impl;

import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.event.JnjLAOrderStatusNotificationEvent;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.facades.services.JnjLAEmailOrderStatusService;
import com.jnj.facades.services.JnjLAOrderEmailMatrixService;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.enums.JnJEmailPeriodicity;
import com.jnj.la.core.util.JnjLaCommonUtil;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JnjLAEmailOrderStatusServiceImpl implements JnjLAEmailOrderStatusService {

    private static final Logger LOG = Logger.getLogger(JnjLAEmailOrderStatusServiceImpl.class);

    private JnjLaOrderDao jnjOrderDao;

    private EventService eventService;

    private JnjLAOrderEmailMatrixService matrixService;

    private ModelService modelService;
    
    private static final String DAILY_IMMEDIATE_ORDER_TYPES_LIST = "jnj.la.order.types.for.daily.immediate.email";
    private static final String COMMA_SEPARATOR = ",";

    private ConfigurationService configurationService;

    @Override
    public void processPendingEmails(final JnjIntegrationRSACronJobModel model, final JnJEmailPeriodicity periodicity) {
        processOrders(periodicity, jnjOrderDao.getOrdersByPeriodicity(periodicity));
        removePendingFromOrderEntries(periodicity, jnjOrderDao.getPendingFromOrderEntries(periodicity));
    }
    
    @Override
    public void processPendingEmailsForImmediate(final JnjIntegrationRSACronJobModel model, final JnJEmailPeriodicity periodicity) {    	
        final List<String> orderTypes = getExclusionOrderStatusesForDailyorImmediate();
        if(CollectionUtils.isNotEmpty(orderTypes))
        {
            processOrders(periodicity, jnjOrderDao.getOrdersForImmediateEmails(periodicity, orderTypes));
            removePendingFromOrderEntries(periodicity, jnjOrderDao.getPendingFromOrderEntries(periodicity));
        }       
        
    }
    
    public void processPendingEmailsForDaily(final JnJEmailPeriodicity periodicity, final String site) {
    	final List<String> orderTypes = getExclusionOrderStatusesForDailyorImmediate();
    	if(CollectionUtils.isNotEmpty(orderTypes))
    	{
    	    final List<String> countries = JnjLaCommonUtil.getCountriesBySite(site);
    	    final List<OrderModel> orders = (getJnjOrderDao().getOrdersForDailyEmails(periodicity,countries, orderTypes));
    	    processOrders(periodicity,orders);
    	    removePendingFromOrderEntries(periodicity, jnjOrderDao.getPendingFromOrderEntriesByStore(periodicity, site,orders));
    	}
    }
    
    public List<String> getExclusionOrderStatusesForDailyorImmediate() {
        List<String> orderTypes = new ArrayList<>();
        if(StringUtils.isNotEmpty(configurationService.getConfiguration().getString(DAILY_IMMEDIATE_ORDER_TYPES_LIST)))
        {
            orderTypes = Arrays.asList(configurationService.getConfiguration().getString(DAILY_IMMEDIATE_ORDER_TYPES_LIST).split(COMMA_SEPARATOR));
            return orderTypes.stream().map(s -> JnjOrderTypesEnum.valueOf(s).getCode()).collect(Collectors.toList());
        }
        return orderTypes;
    }

    @Override
    public void processPendingEmails(final JnjIntegrationRSACronJobModel model, final JnJEmailPeriodicity periodicity, final String site) {
    		processOrders(periodicity, jnjOrderDao.getOrdersByPeriodicityAndStore(periodicity, site));        
    		removePendingFromOrderEntries(periodicity, jnjOrderDao.getPendingFromOrderEntriesByStore(periodicity, site,null));
    }

    public void removePendingFromOrderEntries(final JnJEmailPeriodicity periodicity, final List<? extends AbstractOrderEntryModel> entries) {
        if (CollectionUtils.isNotEmpty(entries)) {
            LOG.info("OrderEntries pending for " +  periodicity + "will be set to FALSE");
            
            AbstractOrderEntryModel orderEntry = null;
			for (int count = 0; count < entries.size(); count++) {
				try 
				{
					orderEntry = entries.get(count);
					saveAndUpdateEntryForPendingEmail(periodicity, orderEntry);
				} catch (Throwable e) {
					LOG.error("Order entry which is causing error due to object no longer " + orderEntry);
				}
			}			

        } else {
            LOG.info("No orderEntries pending for " +  periodicity + " found");
        }
    }
    
	private void saveAndUpdateEntryForPendingEmail(final JnJEmailPeriodicity periodicity, final AbstractOrderEntryModel orderEntry) 
	{
		try 
		{
			modelService.refresh(orderEntry);
			if (orderEntry != null && !modelService.isRemoved(orderEntry)) 
			{
				if (JnJEmailPeriodicity.IMMEDIATE.equals(periodicity)) {
					orderEntry.setPendingImmediateEmail(Boolean.FALSE);
				} else if (JnJEmailPeriodicity.DAILY.equals(periodicity)) {
					orderEntry.setPendingDailyEmail(Boolean.FALSE);
				} else if (JnJEmailPeriodicity.CONSOLIDATED.equals(periodicity)) {
					orderEntry.setPendingConsolidatedEmail(Boolean.FALSE);
				}
				modelService.save(orderEntry);
			}
		} catch (Throwable e) {
			LOG.error("Order entry which is causing error: " + orderEntry);
		}
	}
    
    private void processOrders(final JnJEmailPeriodicity periodicity, final List<OrderModel> orders) {
        if (CollectionUtils.isNotEmpty(orders)) {
            orders.stream().filter(o -> mustSendEmail(o, periodicity)).forEach(o -> sendEmail(o, periodicity));
        } else {
            LOG.warn("No orderEntries pending for " +  periodicity + " email for users with " +  periodicity + " option selected");
        }
    }

    private boolean mustSendEmail(final OrderModel order, final JnJEmailPeriodicity periodicity) {
        return matrixService.mustSendEmail(order, periodicity);
    }

    private void sendEmail(final OrderModel order, final JnJEmailPeriodicity periodicity) {
        try {
            LOG.debug("Publishing JnjLAOrderStatusNotificationEvent for order: " + order.getOrderNumber());
            eventService.publishEvent(new JnjLAOrderStatusNotificationEvent(order, periodicity));
        } catch (Exception e) {
            LOG.error("Error publishing email for order: " + order.getOrderNumber());
        }
    }

    public void setJnjOrderDao(final JnjLaOrderDao jnjOrderDao) {
        this.jnjOrderDao = jnjOrderDao;
    }

    public void setEventService(final EventService eventService) {
        this.eventService = eventService;
    }

    public void setMatrixService(final JnjLAOrderEmailMatrixService matrixService) {
        this.matrixService = matrixService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    public JnjLaOrderDao getJnjOrderDao() {
        return jnjOrderDao;
    }

    public EventService getEventService() {
        return eventService;
    }

    public ModelService getModelService() {
        return modelService;
    }
    
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
    
}