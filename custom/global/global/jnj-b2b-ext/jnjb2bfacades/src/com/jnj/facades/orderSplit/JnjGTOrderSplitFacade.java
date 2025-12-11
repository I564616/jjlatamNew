/**
 * 
 */
package com.jnj.facades.orderSplit;

import java.util.List;
import java.util.Map;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author nsinha7
 *
 */
public interface JnjGTOrderSplitFacade<S, L> {
	
	public Map<S, List<L>> splitOrder(AbstractOrderModel abstOrderModel, String contryIsocode);

}
