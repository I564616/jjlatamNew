/**
 * 
 */
package com.jnj.gt.outbound.mapper;

import com.jnj.core.dto.JnjGTSerializationForm;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.SerialNoResponse;

/**
 * @author nsinha7
 *
 */
public interface JnjGTSerializationMapper {

	public SerialNoResponse getSerialResponse(JnjGTSerializationForm jnjGTSerializationForm);

}
