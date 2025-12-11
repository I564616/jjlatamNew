/**
 *
 */
package com.jnj.facades.converters.populator.address;

import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.user.AddressModel;

import org.apache.commons.lang3.StringUtils;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTAddressData;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTAddressPopulator extends AddressPopulator
{

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		super.populate(source, target);
		target.setDefaultAddress(Boolean.valueOf(source.getContactAddress()));
		if (target instanceof JnjGTAddressData)
		{
			final JnjGTAddressData addressData = (JnjGTAddressData) target;
			addressData.setFax(source.getFax());
			addressData.setMobile(source.getPhone2());
			if (StringUtils.isNotEmpty(source.getPhone1()) && source.getPhone1().contains(Jnjb2bCoreConstants.SYMBOl_PIPE))
			{
				final String[] phoneNumber = StringUtils.split(source.getPhone1(), Jnjb2bCoreConstants.SYMBOl_PIPE);
				if (phoneNumber.length > 1)
				{
					addressData.setPhoneCode(phoneNumber[0]);
					addressData.setPhone(phoneNumber[1]);
				}
			}
			if (StringUtils.isNotEmpty(source.getFax()) && source.getFax().contains(Jnjb2bCoreConstants.SYMBOl_PIPE))
			{
				/* Start JJEPIC-261 */
				final String[] faxNumber = source.getFax().split("\\" + Jnjb2bCoreConstants.SYMBOl_PIPE, 2);
				if (faxNumber.length > 1)
				{
					addressData.setFaxCode(faxNumber[0]);
					addressData.setFax(faxNumber[1]);
				}

			}
			if (StringUtils.isNotEmpty(source.getPhone2()) && source.getPhone2().contains(Jnjb2bCoreConstants.SYMBOl_PIPE))
			{
				final String[] mobileNumber = source.getPhone2().split("\\" + Jnjb2bCoreConstants.SYMBOl_PIPE, 2);
				if (mobileNumber.length > 1)
				{
					addressData.setMobileCode(mobileNumber[0]);
					addressData.setMobile(mobileNumber[1]);
				}
				/* end of JJEPIC-261 */
			}

			addressData.setJnjAddressId(source.getJnJAddressId());
			if (StringUtils.isNotEmpty(((AddressModel) source).getJnjGTState()))
			{
				addressData.setState(((AddressModel) source).getJnjGTState());
			}
			//Changes to populate tax id
			if (StringUtils.isNotEmpty(((AddressModel) source).getTaxId()))
			{
				addressData.setTaxid(((AddressModel) source).getTaxId());
			}
			//AAOM-6890
			if (StringUtils.isNotEmpty(((AddressModel) source).getAttnLine()))
			{
				addressData.setAttnLine(((AddressModel) source).getAttnLine());
			}

		}

	}
}
