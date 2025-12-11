/**
 * 
 */
package com.jnj.gt.outbound.mapper.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTSerializationForm;
import com.jnj.gt.outbound.mapper.JnjGTSerializationMapper;
import com.jnj.gt.outbound.services.JnjGTSerializationService;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequest;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.ProcessSNVerificationRequestResponse;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.SerialNoRequest;
import com.jnj.itsusral00725.pu023_atlas_sn_verification_v1_webservices.snverificationrequestresponse.SerialNoResponse;

import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.util.Config;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTSerializationMapper implements JnjGTSerializationMapper{
	
	@Autowired
	protected JnjGTSerializationService jnjGTSerializationService;
	
	@Autowired
	protected PersistentKeyGenerator serializationMsgIdGenerator;
	
	@Override
	public SerialNoResponse getSerialResponse(JnjGTSerializationForm jnjGTSerializationForm) {
		
		SerialNoRequest serialRequest = new SerialNoRequest();
		SerialNoResponse serialResponse = new SerialNoResponse();
		
		final String gtin = jnjGTSerializationForm.getGtin().replaceAll(" ","");
		final String serialNumber = jnjGTSerializationForm.getSerialNumber().replaceAll(" ","");
		final String batchNumber = jnjGTSerializationForm.getBatchNumber().replaceAll(" ","");
		final String expiryYear = jnjGTSerializationForm.getExpiryYear();
		String expiryMonth = jnjGTSerializationForm.getExpiryMonth();
		String expiryDay = jnjGTSerializationForm.getExpiryDay();
		
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		
		if (expiryDay==null || (expiryDay=="")){
			expiryDay = Jnjb2bCoreConstants.Serialization.DEFAULT_DAY;
		}
		
		final String date = expiryYear.substring(expiryYear.length() - 2) + expiryMonth + expiryDay;
		
		if(gtin!=null && serialNumber!=null && batchNumber!=null){
			serialRequest.setLPN(Jnjb2bCoreConstants.Serialization.GTIN_PREFIX + gtin + Jnjb2bCoreConstants.Serialization.SERIAL_PREFIX + serialNumber 
					+Jnjb2bCoreConstants.Serialization.BATCH_PREFIX + batchNumber + Jnjb2bCoreConstants.Serialization.DATE_PREFIX + date);
		}
		serialRequest.setAppID(Config.getParameter(Jnjb2bCoreConstants.Serialization.APP_ID));
		serialRequest.setScanTimeStamp(timeStamp.replace(" ", "T")+"Z");
		serialRequest.setMessageID(serializationMsgIdGenerator.generate().toString());
		
		ProcessSNVerificationRequest processSNRequest = new ProcessSNVerificationRequest();
		processSNRequest.setSerialNoRequest(serialRequest);
		
		ProcessSNVerificationRequestResponse verifySerialResponse = jnjGTSerializationService.getSerialResponse(processSNRequest);
		if(verifySerialResponse!=null){
			serialResponse = verifySerialResponse.getSerialNoResponse();
		}
		
		return serialResponse;
	}

}
