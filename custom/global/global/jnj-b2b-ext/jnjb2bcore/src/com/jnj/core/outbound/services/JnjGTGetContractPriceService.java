/**
 * 
 */
package com.jnj.core.outbound.services;

import com.jnj.exceptions.IntegrationException;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapper;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapperResponse;


/**
 * The JnjGTGetContractPriceService interface contains the declaration of all the methods of the
 * JnjGTGetContractPriceServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTGetContractPriceService
{
	/**
	 * @param getContractPriceWrapper
	 * @return the gets the contract price wrapper response
	 * @throws IntegrationException
	 */
	public GetContractPriceWrapperResponse getContractPriceWrapper(final GetContractPriceWrapper getContractPriceWrapper)
			throws IntegrationException;
}
