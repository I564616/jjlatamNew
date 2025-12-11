/**
 *
 */
package com.jnj.facades.converters.populator.customer;

import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import org.apache.commons.lang3.StringUtils;

import com.jnj.facades.data.JnjGTAddressData;


/**
 * @author ujjwal.negi
 * 
 */
public class JnjGTAddressReversePopulator extends AddressReversePopulator
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator#populate(de.hybris.platform
	 * .commercefacades.user.data.AddressData, de.hybris.platform.core.model.user.AddressModel)
	 */
	@Override
	public void populate(final AddressData addressData, final AddressModel addressModel) throws ConversionException
	{
		// YTODO Auto-generated method stub
		super.populate(addressData, addressModel);
		addressModel.setStreetname(addressData.getLine1());
		addressModel.setStreetnumber(addressData.getLine2());
		addressModel.setContactAddress(Boolean.valueOf(addressData.isDefaultAddress()));
		if (addressData instanceof JnjGTAddressData)
		{
			final JnjGTAddressData jnjAddressData = (JnjGTAddressData) addressData;
			addressModel.setFax(jnjAddressData.getFax());
			addressModel.setPhone2(jnjAddressData.getMobile());
			addressModel.setJnJAddressId(jnjAddressData.getJnjAddressId());
			//Changes to populate TaxID field
			if (StringUtils.isNotEmpty(jnjAddressData.getTaxid())){
				addressModel.setTaxId(jnjAddressData.getTaxid());
			}
			
			if (addressModel instanceof AddressModel)
			{
				if (StringUtils.isNotEmpty(jnjAddressData.getState()))
				{
					((AddressModel) addressModel).setJnjGTState(jnjAddressData.getState());
				}
				//AAOM-6890
				if (StringUtils.isNotEmpty(jnjAddressData.getAttnLine())) 
				{
					((AddressModel) addressModel).setAttnLine(jnjAddressData.getAttnLine());
				}

			}
		}


	}

}
