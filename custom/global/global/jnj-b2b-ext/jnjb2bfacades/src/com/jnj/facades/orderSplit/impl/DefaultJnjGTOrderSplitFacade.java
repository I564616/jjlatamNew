/**
 * 
 */
package com.jnj.facades.orderSplit.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.ordersplit.JnjGTOrderSplitService;
import com.jnj.facades.orderSplit.JnjGTOrderSplitFacade;

import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTOrderSplitFacade<S,L> implements JnjGTOrderSplitFacade<S, L> {
	
	@Autowired
	Map<String, JnjGTOrderSplitService<S, L>> jnjGTorderSplitService;
	
	final String SPLIT_SERVICE_SUFIX = "jnjGTorderSplitService";
	
	@Override
	public Map<S, List<L>> splitOrder(AbstractOrderModel abstOrderModel, String contryIsocode) {
		String splitServiceName = (contryIsocode != null) ? contryIsocode.toLowerCase() + SPLIT_SERVICE_SUFIX : SPLIT_SERVICE_SUFIX;
		if(jnjGTorderSplitService.containsKey(splitServiceName)){
			// Returns specific order split implementation
			return jnjGTorderSplitService.get(splitServiceName).splitOrder(abstOrderModel);
		}
		// Returns default order split implementation
		return jnjGTorderSplitService.get(SPLIT_SERVICE_SUFIX).splitOrder(abstOrderModel);
	}

}
