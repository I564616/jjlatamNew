package com.jnj.core.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.services.address.JnjGTAddressService;

import de.hybris.platform.core.model.user.AddressModel;

public class JnjInvoiceUtil {

	/**
	 * Private instance of <code>JnjGTAddressService</code>.
	 */
	@Autowired
	private JnjGTAddressService jnjGTAddressService;
	
	/**
	 * Get shipping address using shipping customer number
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @return
	 */
	public AddressModel getCloneAddress(JnjCanonicalDTO jnjCanonicalDTO,LinkedHashMap<String, String> resultSetMap)
	{
		String value= null ;
		value = resultSetMap.get(jnjCanonicalDTO.getSourceColumn());
		final List<AddressModel> addressModels = jnjGTAddressService.getAddressByIdandOnwerType(value);
		final AddressModel cloneAddress = (addressModels.size() > 0) ? jnjGTAddressService.cloneAddress(addressModels.get(0))
				: null;
		return cloneAddress;
	}
}
