/**
 * 
 */
package com.jnj.core.services;

import java.util.List;

import com.jnj.core.model.JnjDropShipmentDetailsModel;

/**
 * @author nsinha7
 *
 */
public interface JnjGTDropshipmentService {


	List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList);
	
	List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList, String salesOrg);

}
