/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import java.io.File;

import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;


/**
 * The JnjNAProofOfDeliveryMapper interface contains the declaration of all the method of the
 * JnjNAProofOfDeliveryMapperImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTProofOfDeliveryMapper
{

	/**
	 * Map proof of delivery request and response by using incoming argument and fetching some value from the properties
	 * file.
	 * 
	 * @param trackingNumber
	 *           the tracking number
	 * @param personName
	 *           the person name
	 * @param companyName
	 *           the company name
	 * @param shipDate
	 *           the ship date
	 * @return File
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 */
	public File mapProofOfDeliveryRequestResponse(final String trackingNumber, final String personName, final String companyName,
			final String shipDate) throws IntegrationException, SystemException;
}
