package com.jnj.core.services.ordersplit;

import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.List;
import java.util.Map;


public interface JnjOrderSplitService<S, L>
{

	final String SPLIT_SERVICE = "Order Split Service";

	public Map<S, List<L>> splitOrder(AbstractOrderModel abstOrderModel);
}
