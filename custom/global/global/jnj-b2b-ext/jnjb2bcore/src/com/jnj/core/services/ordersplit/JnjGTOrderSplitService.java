/**
 * 
 */
package com.jnj.core.services.ordersplit;

import java.util.List;
import java.util.Map;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author nsinha7
 *
 */
public interface JnjGTOrderSplitService<S, L> {
	
final String SPLIT_SERVICE = "Order Split Service";
	
public Map<S, List<L>> splitOrder(AbstractOrderModel abstOrderModel);

}
