/**
 * 
 */
package com.jnj.core.dao;

import java.util.List;

import com.jnj.core.model.JnjDropShipmentDetailsModel;

/**
 * @author nsinha7
 *
 */
public interface JnJGtDropshipmentDAO {
	List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList);
	
	List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList, String salesOrg);

}
