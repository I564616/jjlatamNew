/**
 * 
 */
package com.jnj.gt.outbound.services;

import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequest;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequestResponse;

/**
 * @author nsinha7
 *
 */
public interface JnjGTSerializationService {
	
	public ProcessSNVerificationRequestResponse getSerialResponse(ProcessSNVerificationRequest serialRequest);

}
