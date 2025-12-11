/**
 * 
 */
package com.jnj.facades.converters.populator.customer;


import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCompanyInfoData;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.core.model.CompanyInfoModel;
import de.hybris.platform.core.model.user.AddressModel;


/**
 * @author ujjwal.negi
 * 
 */
public class JnjGTCompanyInfoReversePopulator implements Populator<JnjCompanyInfoData, CompanyInfoModel>
{

	@Autowired
	@Qualifier(value = "addressReversePopulator")
	private AddressReversePopulator addressReversePopulator;


	@Autowired
	private ModelService modelService;

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the addressReversePopulator
	 */
	public AddressReversePopulator getAddressReversePopulator()
	{
		return addressReversePopulator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final JnjCompanyInfoData source, final CompanyInfoModel target) throws ConversionException
	{
		// YTODO Auto-generated method stub
		final AddressModel shipToAddressModel = getModelService().create(AddressModel.class);
		final AddressModel billToAddressModel = getModelService().create(AddressModel.class);
		final JnjGTAddressData shipToAddressData = new JnjGTAddressData();
		final JnjGTAddressData billToAddressData = new JnjGTAddressData();

		//Setup of ship to Address Data
		final CountryData shipToCountry = new CountryData();
		shipToCountry.setIsocode(source.getShipToCountry());

		if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.US, source.getShipToCountry()))
		{
			final RegionData shipToregion = new RegionData();
			shipToregion.setIsocode(source.getShipToState());
			shipToregion.setCountryIso(source.getShipToCountry());
			shipToAddressData.setRegion(shipToregion);
		}
		else
		{
			shipToAddressData.setState(source.getShipToState());
		}
		shipToAddressData.setShippingAddress(true);
		shipToAddressData.setCountry(shipToCountry);
		shipToAddressData.setLine1(source.getShipToLine1());
		shipToAddressData.setLine2(source.getShipToLine2());
		shipToAddressData.setTown(source.getShipToCity());
		shipToAddressData.setPostalCode(source.getShipToZipCode());

		getAddressReversePopulator().populate(shipToAddressData, shipToAddressModel);
		shipToAddressModel.setOwner(target);

		//Setup of bill to Address Data
		final CountryData billToCountry = new CountryData();
		billToCountry.setIsocode(source.getBillToCountry());




		if (StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.US, source.getBillToCountry()))
		{
			final RegionData billToregion = new RegionData();
			billToregion.setIsocode(source.getBillToState());
			billToregion.setCountryIso(source.getBillToCountry());
			billToAddressData.setRegion(billToregion);
		}
		else
		{
			billToAddressData.setState(source.getBillToState());
		}


		billToAddressData.setBillingAddress(true);
		billToAddressData.setCountry(billToCountry);
		billToAddressData.setLine1(source.getBillToLine1());
		billToAddressData.setLine2(source.getBillToLine2());
		billToAddressData.setTown(source.getBillToCity());
		billToAddressData.setPostalCode(source.getBillToZipCode());

		getAddressReversePopulator().populate(billToAddressData, billToAddressModel);
		billToAddressModel.setOwner(target);

		//Setup of CompanyInfo model
		target.setBillToAddress(billToAddressModel);
		target.setShipToAddress(shipToAddressModel);
		target.setAccountName(source.getAccountName());
		target.setGln(source.getgLN());
		target.setInitialOpeningOrderAmount(source.getInitialOpeningOrderAmount());
		target.setSubsidaryOf(source.getSubsidiaryOf());
		target.setTypeOfBusiness(source.getTypeOfBusiness());
		if(source.getSalesAndUseTaxFlag()!=null)
		{
			target.setSalesAndUseTaxFlag(source.getSalesAndUseTaxFlag().booleanValue());
		}
		target.setInitialOpeningOrderAmount(source.getInitialOpeningOrderAmount());
		target.setEstimatedAmountPerYear(source.getEstimatedAmountPerYear());
		target.setMedicalProductsPurchase(source.getMedicalProductsPurchase());
	}
}
