/**
 * 
 */
package com.jnj.core.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dao.JnJGtDropshipmentDAO;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.JnjGTDropshipmentService;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTDropshipmentService implements JnjGTDropshipmentService {
	
	@Autowired
	JnJGtDropshipmentDAO jnjGTDropshipmentDAO;

	@Override
	public List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList) {
		
		
		return jnjGTDropshipmentDAO.getDropShipmentDetails(productCodesList);
		
	}

	@Override
	public List<JnjDropShipmentDetailsModel> getDropShipmentDetails(
			List<String> productCodesList, String salesOrg) {
		return jnjGTDropshipmentDAO.getDropShipmentDetails(productCodesList, salesOrg);
	}

}
