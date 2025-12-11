package com.jnj.core.event;

import java.io.Serial;
import java.util.List;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;

public class JnjGTOrderCancelNotificationEvent extends AbstractCommerceUserEvent{

	@Serial
	private static final long serialVersionUID = 1L;
	
	private List<OrderEntryModel> orderEntries;
	
	public List<OrderEntryModel> getOrderEntries() {
		return orderEntries;
	}
	public void setOrderEntries(List<OrderEntryModel> orderEntries) {
		this.orderEntries = orderEntries;
	}
	
}
