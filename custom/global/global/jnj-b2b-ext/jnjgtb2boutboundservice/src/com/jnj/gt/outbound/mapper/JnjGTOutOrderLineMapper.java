/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import java.util.List;

import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;

/**
 * @author nsinha7
 *
 */
public interface JnjGTOutOrderLineMapper {
	
	/**
	 * This method returns list of Out Order lines during validation.
	 * @param cartModel
	 * @return
	 */
	public List<OutOrderLines3> createValidationOutOrderList(CartModel cartModel);
	
	/**
	 * This method returns list of Out Order lines during order confirmation.
	 * @param cartModel
	 * @return
	 */
	public List<OutOrderLines3> createConfirmationOutOrderList(OrderModel orderModel);

}
