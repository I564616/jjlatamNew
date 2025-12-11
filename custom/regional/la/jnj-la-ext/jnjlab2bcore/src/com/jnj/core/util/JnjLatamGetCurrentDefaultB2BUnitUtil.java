package com.jnj.core.util;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dataload.mapper.UserRolesData;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author dsilv107
 */
public class JnjLatamGetCurrentDefaultB2BUnitUtil extends JnjGetCurrentDefaultB2BUnitUtil {

    @Autowired
    private SessionService sessionService;

    public UserRolesData getUserRolesFromSession()
    {
        final UserRolesData userRoles;
        if (sessionService.getAttribute("jnj.session.userRoles") != null)
        {
            userRoles = sessionService.getAttribute("jnj.session.userRoles");
        }
        else
        {

            userRoles = getUserRoles();
            sessionService.setAttribute("jnj.session.userRoles", userRoles);

        }

        return userRoles;
    }

    /**
     * The getUserRoles method sets the user roles in the User Roles Data object.
     *
     * @return the user roles data
     */
    public UserRolesData getUserRoles()
    {
        final UserRolesData userRoles = new UserRolesData();
        // Get the User Roles by passing the indirect customer, change price & expected price.
        final Set<String> indirectCustomerUserRoles = new HashSet<String>();
        indirectCustomerUserRoles.addAll(JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserRoles.INDIRECT_CUSTOMER_GROUP_KEY,
            Jnjb2bCoreConstants.CONST_COMMA));
        final Set<String> distributorRoles = new HashSet<String>();
        distributorRoles.addAll(JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserRoles.DISTRIBUTOR_GROUP_KEY,
            Jnjb2bCoreConstants.CONST_COMMA));
        final Set<String> expectedPriceUserRoles = new HashSet<String>();
        expectedPriceUserRoles.addAll(JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserRoles.EXPECTED_PRICE_GROUP_KEY,
            Jnjb2bCoreConstants.CONST_COMMA));
        final Set<String> changePriceUserRoles = new HashSet<String>();
        changePriceUserRoles.addAll(JnJCommonUtil.getValues(Jnjb2bCoreConstants.UserRoles.PRICE_CHANGE_GROUP_KEY,
            Jnjb2bCoreConstants.CONST_COMMA));
        // Call the getAllRoles method to retrieve the all user roles.
        final Set<String> allUserRoles = getAllRoles();
        indirectCustomerUserRoles.retainAll(allUserRoles);
        if (CollectionUtils.isNotEmpty(indirectCustomerUserRoles))
        {
            userRoles.setIndirectCustomer(true);
        }

        distributorRoles.retainAll(allUserRoles);
        if (CollectionUtils.isNotEmpty(distributorRoles))
        {
            userRoles.setDistributor(true);
        }

        expectedPriceUserRoles.retainAll(allUserRoles);
        if (CollectionUtils.isNotEmpty(expectedPriceUserRoles))
        {
            userRoles.setExpectedPrice(true);
        }

        changePriceUserRoles.retainAll(allUserRoles);
        if (CollectionUtils.isNotEmpty(changePriceUserRoles))
        {
            userRoles.setPriceChange(true);
        }
        return userRoles;
    }

    /**
     * This method returns true for the user who has the role for submitting Order EDI From Home Page
     *
     */

    public boolean validEdiSubmitOrderUser()
    {
        List<String> validSubmitOrderRoles = null;
        boolean hasProperRole = true;
        final String requiredRolesForCheckout = Config.getParameter(Jnjb2bCoreConstants.VALID_EDI_SUBMIT_ORDER_ROLES_KEY);
        if (StringUtils.isNotEmpty(requiredRolesForCheckout))
        {
            validSubmitOrderRoles = Arrays.asList(requiredRolesForCheckout.split(Jnjb2bCoreConstants.CONST_COMMA));
            final Set<String> userRolesSet = getAllRoles();
            hasProperRole = !Collections.disjoint(userRolesSet, validSubmitOrderRoles);
        }
        return hasProperRole;
    }

    /**
     * Gets the defalut time taken for delivery for user country.
     *
     * @return the days to be taken for delivery
     */
    public Integer getOrderDeliveryTime()
    {
        Integer deliveryDays = Integer.valueOf(0);
        if (null != getDefaultB2BUnit().getCountry() && null != getDefaultB2BUnit().getCountry().getOrderDeliveryTime())
        {
            deliveryDays = getDefaultB2BUnit().getCountry().getOrderDeliveryTime();
        }
        return deliveryDays;
    }
}
