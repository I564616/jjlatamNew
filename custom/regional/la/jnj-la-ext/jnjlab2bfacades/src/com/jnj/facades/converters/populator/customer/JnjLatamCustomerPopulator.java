/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.facades.converters.populator.customer;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.facades.customer.JnjLatamCustomerFacade;
import com.jnj.facades.data.JnJLaUserAccountPreferenceData;
import com.jnj.facades.data.JnjLaCustomerData;
import com.jnj.la.core.model.JnJLaUserAccountPreferenceModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Objects;
import java.util.Collections;
import com.jnj.la.core.enums.JnJEmailFrequency;
import com.jnj.la.core.enums.JnJEmailPeriodicity;

public class JnjLatamCustomerPopulator extends JnjGTCustomerPopulator {

    @Autowired
    private JnjLatamCustomerFacade jnjLaCustomerFacade;

    @Override
    public void populate(final CustomerModel source, final CustomerData target) {
        super.populate(source, target);    

        if (source instanceof JnJB2bCustomerModel jnJB2bCustomerModel && target instanceof JnjLaCustomerData jnjLaCustomerData) {
        	jnjLaCustomerData.setCommercialUserFlag(jnJB2bCustomerModel.getCommercialUserFlag());
      		jnjLaCustomerData.setCommercialUserSector(jnJB2bCustomerModel.getCommercialUserSector());
      		
            JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel = findCurrentAccountPreference((JnJB2bCustomerModel) source);
            if (jnjLaUserAccountPreferenceModel != null) {
                JnJLaUserAccountPreferenceData jnjLaUserAccountPreferenceData = new JnJLaUserAccountPreferenceData();
                jnjLaUserAccountPreferenceData.setPeriodicity(jnjLaUserAccountPreferenceModel.getPeriodicity());
                if (JnJEmailPeriodicity.CONSOLIDATED.equals(jnjLaUserAccountPreferenceModel.getPeriodicity())) {
                	JnjLatamCustomerPopulator.populatePeriodicity(jnjLaUserAccountPreferenceData, jnjLaUserAccountPreferenceModel);
                }
                jnjLaCustomerData.setCurrentAccountPreference(jnjLaUserAccountPreferenceData);
            }
        }

    }
    
    private static void populatePeriodicity(JnJLaUserAccountPreferenceData jnjLaUserAccountPreferenceData, JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel){
    	jnjLaUserAccountPreferenceData.setOrderTypes(jnjLaUserAccountPreferenceModel.getOrderTypes());
        jnjLaUserAccountPreferenceData.setConsolidatedEmailFrequency(jnjLaUserAccountPreferenceModel.getConsolidatedEmailFrequency());
        if (JnJEmailFrequency.WEEKLY.equals(jnjLaUserAccountPreferenceModel.getConsolidatedEmailFrequency())) {
            jnjLaUserAccountPreferenceData.setDayOfTheWeek(jnjLaUserAccountPreferenceModel.getDayOfTheWeek());
        }
        if (JnJEmailFrequency.MONTHLY.equals(jnjLaUserAccountPreferenceModel.getConsolidatedEmailFrequency())) {
            jnjLaUserAccountPreferenceData.setDaysOfTheMonth(CollectionUtils.isNotEmpty(jnjLaUserAccountPreferenceModel.getDaysOfTheMonth()) ? jnjLaUserAccountPreferenceModel.getDaysOfTheMonth() : Collections.emptyList());
        }

    }

    private JnJLaUserAccountPreferenceModel findCurrentAccountPreference(final JnJB2bCustomerModel model) {
        if (CollectionUtils.isEmpty(model.getAccountPreferences())){
            return null;
        }

        for (JnJLaUserAccountPreferenceModel jnjLaUserAccountPreferenceModel : model.getAccountPreferences()) {
            if (Objects.equals(jnjLaUserAccountPreferenceModel.getAccount(), jnjLaCustomerFacade.getCurrentB2bUnit())) {
                return jnjLaUserAccountPreferenceModel;
            }
        }

        return null;
    }

}
