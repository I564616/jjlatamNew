/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InRequest;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.OutResponse;
import com.jnj.core.data.ws.JnjGTSapWsData;


/**
 * The JnjNACreateConsOrdService interface contains the declaration of all the methods of the
 * JnjNACreateConsOrdServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTCreateConsOrdService
{

	/**
	 * Creates the order.
	 * 
	 * @param inRequest
	 *           the in request
	 * @param wsData YTODO
	 * @return the out response
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public OutResponse createOrder(final InRequest inRequest, JnjGTSapWsData wsData) throws IntegrationException;
}
