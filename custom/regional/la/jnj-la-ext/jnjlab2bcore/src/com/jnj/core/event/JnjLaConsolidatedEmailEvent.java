/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.core.event;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.List;

public class JnjLaConsolidatedEmailEvent extends AbstractCommerceUserEvent {

    private List<OrderModel> orders;

    private JnJB2BUnitModel unit;

    private List<JnJB2bCustomerModel> users;

    private List<String> defaultRecipients;

    public JnjLaConsolidatedEmailEvent(B2BUnitModel unit, List<JnJB2bCustomerModel> users, List<String> defaultRecipients, List<OrderModel> orders) {
        this.unit = (JnJB2BUnitModel) unit;
        this.users = users;
        this.defaultRecipients = defaultRecipients;
        this.orders = orders;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public JnJB2BUnitModel getUnit() {
        return unit;
    }

    public void setUnit(final JnJB2BUnitModel unit) {
        this.unit = unit;
    }

    public List<JnJB2bCustomerModel> getUsers() {
        return users;
    }

    public void setUsers(final List<JnJB2bCustomerModel> users) {
        this.users = users;
    }

    public List<String> getDefaultRecipients() {
        return defaultRecipients;
    }

    public void setDefaultRecipients(List<String> defaultRecipients) {
        this.defaultRecipients = defaultRecipients;
    }

}