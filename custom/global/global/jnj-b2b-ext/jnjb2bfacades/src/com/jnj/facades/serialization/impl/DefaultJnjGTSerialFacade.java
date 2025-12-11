/**
 * 
 */
package com.jnj.facades.serialization.impl;


import jakarta.annotation.Resource;

import com.jnj.core.dto.JnjGTSerialResponseData;
import com.jnj.core.dto.JnjGTSerializationForm;
import com.jnj.facades.serialization.JnjGTSerialFacade;
import com.jnj.gt.outbound.mapper.JnjGTSerializationMapper;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.SerialNoResponse;



/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTSerialFacade implements  JnjGTSerialFacade{
	
	
	@Resource(name="GTSerializationMapper")
	protected JnjGTSerializationMapper jnjGTSerializationMapper;
	
	@Override
	public JnjGTSerialResponseData verifySerial(JnjGTSerializationForm jnjGTSerializationForm) {
		
		JnjGTSerialResponseData responseData = new JnjGTSerialResponseData();
		SerialNoResponse serialResponse = new SerialNoResponse();
		
		serialResponse = jnjGTSerializationMapper.getSerialResponse(jnjGTSerializationForm);
		if(serialResponse!=null){
			responseData.setInputSerialNumber(jnjGTSerializationForm.getSerialNumber().replaceAll(" ",""));
			responseData.setGtin(jnjGTSerializationForm.getGtin().replaceAll(" ",""));
			responseData.setBatchNumber(jnjGTSerializationForm.getBatchNumber().replaceAll(" ",""));
			responseData.setExpiryYear(jnjGTSerializationForm.getExpiryYear());
			responseData.setExpiryMonth(jnjGTSerializationForm.getExpiryMonth());
			responseData.setExpiryDay(jnjGTSerializationForm.getExpiryDay());
			responseData.setLPN(serialResponse.getLPN());
			responseData.setSerialNumber(serialResponse.getSerialNum());
			responseData.setReasonCode(serialResponse.getReasonCode());
			responseData.setReason(serialResponse.getReason());
			responseData.setStatus(serialResponse.getStatus());
		}	
		
		return responseData;
		
	}
	
	

}
