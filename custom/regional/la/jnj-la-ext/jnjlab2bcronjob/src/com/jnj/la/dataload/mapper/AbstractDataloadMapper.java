/**
 * 
 */
package com.jnj.la.dataload.mapper;

import com.jnj.core.dto.JnjCustomerEligiblityDTO;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.exceptions.BusinessException;


/**
 * Abstract class to be extended by every Data Load Mapper
 * 
 * @author Manoj.K.Panda
 * 
 */
public abstract class AbstractDataloadMapper<T>
{
	/**
	 * Populates and saves intermediate <code>JnjImtCustomerEligibilityModel</code> from the DTO Object and persists it
	 * to the Hybris system.
	 * 
	 * @param customerEligibilityQuery
	 * @param jobModel
	 * @return String
	 */
	public abstract String populateCustomerEligibilityRecords(String customerEligibilityQuery, JnjIntegrationRSACronJobModel jobModel);

	/**
	 * Process all records from the Intermediary table and persist them in the Hybris system.
	 * 
	 * @throws BusinessException
	 */
	public abstract boolean processCustomerEligibilityRecords(JnjCustomerEligiblityDTO records) ;

}
