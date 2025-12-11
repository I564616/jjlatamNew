/**
 * 
 */
package com.jnj.facades.serialization;

import com.jnj.core.dto.JnjGTSerialResponseData;
import com.jnj.core.dto.JnjGTSerializationForm;

/**
 * @author nsinha7
 *
 */
public interface JnjGTSerialFacade {
	
	
	/**
	 * 
	 * @param jnjGTB2BSerializationForm
	 */
	public JnjGTSerialResponseData verifySerial(JnjGTSerializationForm jnjGTB2BSerializationForm);

}
