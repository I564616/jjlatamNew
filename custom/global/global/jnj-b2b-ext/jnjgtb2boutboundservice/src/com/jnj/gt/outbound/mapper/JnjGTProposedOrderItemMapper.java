/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import java.util.HashMap;
import java.util.List;

import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.facades.data.JnjGTCommonFormIOData;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.ManualSubLinesOutput;
import com.jnj.gt.aspac_epic_simulate_v1.simulatewebservice.TestBackOrderItemFromGatewayOutput;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;


 
/**
 * @author  
 *
 */
public interface JnjGTProposedOrderItemMapper
{

 
	/**
	 * @param cartModel
	 * @param wsData
	 * @return
	 */
	public JnjGTProposedOrderResponseData mapConsignmentSimulateOrderRequestResponse(final CartModel cartModel, final JnjGTSapWsData wsData);
	
	/**
	 * @param cartModel
	 * @param wsData
	 * @return
	 */
	public JnjGTProposedOrderResponseData checkReplacemenItemForProduct(String productId, final JnjGTSapWsData wsData);
	
	
	/**
	 * This method returns list of Out Order lines during validation.
	 * @param cartModel
	 * @return
	 */
	/*public List<ProposedOutOrderItem> createProposedOutOrderList(CartModel cartModel, CatalogVersionModel catalogVersionModel, 
			HashMap<String, List <AbstractOrderEntryModel>> cartEntryMap, TestBackOrderItemFromGatewayOutput response);
	
	
	public List<ManualSubLinesOutput> buildManualSubstitutionList(CartModel cartModel, String entryNo, 
			HashMap<String, List <AbstractOrderEntryModel>> cartEntryMap);*/
}
