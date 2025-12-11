/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services.impl;

import com.jnj.core.event.JnjLaConsolidatedEmailEvent;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.facades.services.JnJLaConsolidatedEmailService;
import com.jnj.la.core.enums.JnJEmailFrequency;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.jnj.core.enums.JnjOrderTypesEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Locale;
import java.util.Calendar;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class JnJLaConsolidatedEmailServiceImpl extends JnjLAEmailOrderStatusServiceImpl implements JnJLaConsolidatedEmailService {

    private static final Logger LOG = Logger.getLogger(JnJLaConsolidatedEmailServiceImpl.class);

    private static final String EXCLUSION_STATUS_LIST = "jnj.la.consolidatedEmail.exclusionStatusList";
    private static final String INCLUSION_ORDER_TYPES = "jnj.la.consolidatedEmail.inclusionOrderTypeList";
    private static final String COMMA_SEPARATOR = ",";

    private ConfigurationService configurationService;

    @Override
    public void processPendingEmails(final JnjIntegrationRSACronJobModel model, final String site) {
        final List<String> orderStatuses = getExclusionOrderStatuses();
        final List<String> orderTypes = getInclusionOrderTypes();
        final List<String> countries = JnjLaCommonUtil.getCountriesBySite(site);
        final Set<OrderModel> orders = new HashSet<>(getJnjOrderDao().getOrdersForConsolidatedEmails(countries, orderStatuses, orderTypes));

        LOG.info("Found " + orders.size() + " opened orders to send in CONSOLIDATED emails. Countries: " + countries + ".");

        if (CollectionUtils.isNotEmpty(orders)) {
            sendEmails(orders);
        } else {
            LOG.warn("No orderEntries pending for CONSOLIDATED in open orders");
        }
    }

    public List<String> getExclusionOrderStatuses() {
        final List<Object> statuses = Arrays.asList(configurationService.getConfiguration().getString(EXCLUSION_STATUS_LIST, StringUtils.EMPTY).split(COMMA_SEPARATOR));
        return statuses.stream().map(s -> OrderStatus.valueOf(s.toString()).getCode()).collect(Collectors.toList());
    }

    private List<String> getInclusionOrderTypes() {
        List<String> orderTypes = new ArrayList<>();
        if (StringUtils.isNotEmpty(configurationService.getConfiguration().getString(INCLUSION_ORDER_TYPES))) {
            orderTypes = Arrays.asList(
                    configurationService.getConfiguration().getString(INCLUSION_ORDER_TYPES).split(COMMA_SEPARATOR));
            return orderTypes.stream().map(t -> JnjOrderTypesEnum.valueOf(t).getCode()).collect(Collectors.toList());
        }
        return orderTypes;
    }

    private void sendEmails(final Set<OrderModel> orders) {
        Map<B2BUnitModel, List<JnJB2bCustomerModel>> allB2bCustomersMap = getJnjOrderDao().getUsersForConsolidatedEmail();
        Map<B2BUnitModel, List<String>> allDefaultRecipientsMap = getJnjOrderDao().getDefaultRecipientsForConsolidatedEmail();
        Set<B2BUnitModel> b2bUnits = orders.stream().map(OrderModel::getUnit).collect(Collectors.toSet());
        LOG.info("Grouping orders in " + b2bUnits.size() + " units.");

        int preparedEmails = 0;
        for (final B2BUnitModel b2bUnit : b2bUnits) {
            try {
                if (sendEmail(b2bUnit, orders, allB2bCustomersMap, allDefaultRecipientsMap)) {
                    preparedEmails++;
                };
            } catch (Exception e) {
                LOG.error("Error preparing email to unit: " + b2bUnit.getUid(), e);
            }
        }
        LOG.info("Consolidated emails: " + preparedEmails + " emails prepared.");
    }

    private Boolean sendEmail(final B2BUnitModel b2bUnit, final Set<OrderModel> orders, final Map<B2BUnitModel,
            List<JnJB2bCustomerModel>> b2bCustomersMap, final Map<B2BUnitModel, List<String>> allDefaultRecipientsMap) {

        List<OrderModel> b2bUnitOrders = getOrdersForB2bUnit(orders, b2bUnit);

        if (CollectionUtils.isNotEmpty(b2bUnitOrders)) {
            final List<JnJB2bCustomerModel> allB2bCustomers = Optional.ofNullable(b2bCustomersMap.get(b2bUnit)).orElse(new ArrayList<>());
            final List<JnJB2bCustomerModel> eligibleB2bCustomers = filterEligibleB2bCustomers(allB2bCustomers, b2bUnit);
            final List<String> b2bUnitDefaultRecipients = Optional.ofNullable(allDefaultRecipientsMap.get(b2bUnit)).orElse(new ArrayList<>());
            if (eligibleB2bCustomers.size() + b2bUnitDefaultRecipients.size() > 0) {
                LOG.info("Publishing JnjLaConsolidatedEmailEvent for b2bUnit: " + b2bUnit.getUid() + " for " + b2bUnitOrders.size() + " orders.");
                getEventService().publishEvent(new JnjLaConsolidatedEmailEvent(b2bUnit, eligibleB2bCustomers, b2bUnitDefaultRecipients, b2bUnitOrders));
                return true;
            }
        }
        return false;
    }

    private static List<OrderModel> getOrdersForB2bUnit(final Set<OrderModel> orders, final B2BUnitModel unit) {
        return orders.stream().filter(sameUnit(unit)).collect(Collectors.toList());
    }

    private static Predicate<OrderModel> sameUnit(final B2BUnitModel unit) {
        return o -> Objects.equals(unit, o.getUnit());
    }

    private List<JnJB2bCustomerModel> filterEligibleB2bCustomers(final List<JnJB2bCustomerModel> customers, final B2BUnitModel unit) {
        return customers.stream().filter(customer -> isEligibleForEmail(customer, unit)).collect(Collectors.toList());
    }

    public boolean isEligibleForEmail(final JnJB2bCustomerModel model, final B2BUnitModel unit) {
        List<JnJLaUserAccountPreferenceModel> accountPreferences = new ArrayList<>(model.getAccountPreferences());
        Optional<JnJLaUserAccountPreferenceModel> accountPreference = accountPreferences.stream().filter(
                item -> Objects.equals(item.getAccount(), unit)).findAny();
		if (accountPreference.isPresent()) {			
			if (JnJEmailFrequency.DAILY.equals(accountPreference.get().getConsolidatedEmailFrequency())) {
				return true;
			} 
			if (JnJEmailFrequency.WEEKLY.equals(accountPreference.get().getConsolidatedEmailFrequency())
                    && JnJLaConsolidatedEmailServiceImpl.filterWeekDay(accountPreference.get())) {
				return true;
			}
			if (JnJEmailFrequency.MONTHLY.equals(accountPreference.get().getConsolidatedEmailFrequency())
                    && JnJLaConsolidatedEmailServiceImpl.filterMonthDate(accountPreference.get())) {
				return true;
			}
		}
        return false;
    }

    
    private static boolean filterWeekDay(final JnJLaUserAccountPreferenceModel jnJLaUserAccountPreferenceModel)
    {
        LocalDate date = LocalDate.now();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return StringUtils.equalsIgnoreCase(jnJLaUserAccountPreferenceModel.getDayOfTheWeek().getCode(), dayName);
    }

    private static boolean filterMonthDate(final JnJLaUserAccountPreferenceModel jnJLaUserAccountPreferenceModel)
    {
        final Calendar calendar = Calendar.getInstance();
        final List<String> listOfDates = new ArrayList<>();
        boolean checkTrueOrFalse = false;
        listOfDates.add("29");
        listOfDates.add("30");
        listOfDates.add("31");

        if(CollectionUtils.isNotEmpty(jnJLaUserAccountPreferenceModel.getDaysOfTheMonth()))
        {
            checkTrueOrFalse = jnJLaUserAccountPreferenceModel.getDaysOfTheMonth().stream().anyMatch(selectedDate ->
                (Integer.valueOf(selectedDate) == calendar.get(Calendar.DAY_OF_MONTH))
                        || (calendar.get(Calendar.DAY_OF_MONTH) >= calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                        && (calendar.getActualMaximum(Calendar.DAY_OF_MONTH) <= Integer.valueOf(selectedDate)
                        && listOfDates.iterator().next().contains(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)))))
            );
        }
        return checkTrueOrFalse;
    }

    @Override
    public void setConfigurationService(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}