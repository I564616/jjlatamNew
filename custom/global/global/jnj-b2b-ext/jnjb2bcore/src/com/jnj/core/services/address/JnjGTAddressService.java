/**
 * 
 */
package com.jnj.core.services.address;

import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;

import com.jnj.core.services.JnJAddressService;


/**
 * The JnjGTAddressService class contains declaration all those method which are dealing with na address model and their
 * definition in JnjGTAddressServiceImpl.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTAddressService extends JnJAddressService
{


	/**
	 * Gets the address by id and source sys id.
	 * 
	 * @param jnjId
	 *           the jnj id
	 * @param sourceSysId
	 *           the source sys id
	 * @return the address by idand source sys id
	 */
	public List<AddressModel> getAddressByIdandSourceSysId(final String jnjId, final String sourceSysId);
}
