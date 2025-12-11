/**
 * 
 */
package com.jnj.core.services.address.impl;

import de.hybris.platform.core.model.user.AddressModel;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.impl.DefaultJnJAddressService;
import com.jnj.core.dao.address.JnjGTAddressDao;
import com.jnj.core.services.address.JnjGTAddressService;


/**
 * The JnjGTAddressServiceImpl class contains definition all those method which are dealing with na address model and
 * defined in JnjGTAddressService.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTAddressService extends DefaultJnJAddressService implements JnjGTAddressService
{
	@Autowired
	private JnjGTAddressDao jnjGTAddressDao;

	public JnjGTAddressDao getJnjGTAddressDao() {
		return jnjGTAddressDao;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<AddressModel> getAddressByIdandSourceSysId(final String jnjId, final String sourceSysId)
	{
		return jnjGTAddressDao.getAddressByIdandSourceSysId(jnjId, sourceSysId);
	}

}
