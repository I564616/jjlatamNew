/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterReply;
import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterRequest;
import com.jnj.exceptions.IntegrationException;



/**
 * The JnjNAProofOfDeliveryService interface contains the declaration of all the methods of the
 * JnjNAProofOfDeliveryServiceImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTProofOfDeliveryService
{

	/**
	 * The retrieveSignatureProofOfDeliveryLetter method accepts the SignatureProofOfDeliveryLetterRequest object as its
	 * input parameters and passes the same to FEDEX service to validate it and receive the TestPricingFromGatewayOutput
	 * object.
	 * 
	 * @param signatureProofOfDeliveryLetterRequest
	 *           the signature proof of delivery letter request
	 * @return the test pricing from gateway output
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public SignatureProofOfDeliveryLetterReply retrieveSignatureProofOfDeliveryLetter(
			final SignatureProofOfDeliveryLetterRequest signatureProofOfDeliveryLetterRequest) throws IntegrationException;
}
