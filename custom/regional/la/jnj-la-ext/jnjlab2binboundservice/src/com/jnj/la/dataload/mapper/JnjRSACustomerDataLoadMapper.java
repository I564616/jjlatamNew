package com.jnj.la.dataload.mapper;

import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceBudgetService;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.core.util.JnjLaCoreUtil;


/**
 * @author Suparno Banerjee
 *
 */
public class JnjRSACustomerDataLoadMapper
{

	/** The b2 b unit service. */
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	/** The jnj customer data service. */
	@Autowired
	protected JnJCustomerDataService jnJCustomerDataService;

	/** The model service. */
	@Autowired
	protected ModelService modelService;

	/** The user service. */
	@Autowired
	protected UserService userService;

	/** The common i18 n service. */
	@Autowired
	protected CommonI18NService commonI18NService;

	/** The b2b commerce budget service. */
	@Autowired
	protected B2BCommerceBudgetService b2BCommerceBudgetService;

	/** The jnj address service. */
	@Autowired
	protected JnjGTAddressService jnjGTAddressService;


	public Map<Boolean, String> mapCustomerDataForSoldTo(final JnJLaCustomerDTO customer)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForSoldTo", Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		errorFlagAndMessage.put(Boolean.FALSE, null);
		return errorFlagAndMessage;
	}

	public Map<Boolean, String> mapCustomerDataForKeyAccount(final JnJLaCustomerDTO customerDTO)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForKeyAccount", Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		errorFlagAndMessage.put(Boolean.FALSE, null);
		return errorFlagAndMessage;
	}

	public Map<Boolean, String> mapCustomerDataForShipTo(final JnJLaCustomerDTO customerDTO)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataForShipTo", Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		errorFlagAndMessage.put(Boolean.FALSE, null);
		return errorFlagAndMessage;
	}

	public Map<Boolean, String> mapCustomerDataforIndirectCustomer(final JnJLaCustomerDTO customer)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, "mapCustomerDataforIndirectCustomer", Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		errorFlagAndMessage.put(Boolean.FALSE, null);
		return errorFlagAndMessage;
	}


	protected final JnJB2BUnitModel createB2BUnitWithGlobalAttributes(final JnJLaCustomerDTO customer,
			final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "createB2BUnitByJnJSapAccount";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);

		//Setting the Id
		jnJB2BUnitModel.setUid(customer.getCustomerNumer());
		//Setting Whether It is a distributor or Hospital
		//CR 31439 Change
		// To do We will put the name in all the three Locale
		jnJB2BUnitModel.setName(getB2bUnitName(customer.getName1(), customer.getName2()));
		jnJB2BUnitModel.setLocName(getB2bUnitName(customer.getName1(), customer.getName2()));

		final CountryModel country = commonI18NService.getCountry(customer.getCountry());
		jnJB2BUnitModel.setCountry(country);

		/* Create the PriceGroup Information for BRAZIL PHR customers during UpsertCustomers */
		/* We need to Set Price Group Only For BR */
		final List<String> priceGroupUpsertCountriesList = JnjLaCoreUtil
				.getCountriesList(Jnjlab2bcoreConstants.KEY_PRICE_GROUP_VALID_COUNTRIES);

		if (JnjLaCoreUtil.isCountryValidForACountriesList(country.getIsocode(), priceGroupUpsertCountriesList))
		{
			final String userPriceGroup = country.getName() + customer.getRegion();
			final UserPriceGroup userPriceGrp = UserPriceGroup.valueOf(userPriceGroup);
			jnJB2BUnitModel.setUserPriceGroup(userPriceGrp);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);

		return jnJB2BUnitModel;
	}


	/**
	 * Gets the b2b unit name.
	 *
	 * @param firstName
	 *           the first name
	 * @param secondName
	 *           the second name
	 * @return the b2b unit name
	 */
	protected String getB2bUnitName(final String firstName, final String secondName)
	{
		final String methodName = "getB2bUnitName";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		String finalName = StringUtils.EMPTY;

		if (null != firstName)
		{
			finalName = firstName;
		}
		if (null != secondName)
		{
			finalName = finalName.concat(" ").concat(secondName);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		return finalName.trim();
	}


	protected AddressModel extractAdressFromSapAccount(final JnJLaCustomerDTO jnJSapAccountModel,
			final JnJB2BUnitModel jnJB2bUnitModel)
	{
		final String methodName = "extractAdressFromSapAccount";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		AddressModel addressModel = null;
		try
		{
			if (null != jnJB2bUnitModel.getPk())
			{
				addressModel = jnjGTAddressService.getAddressForJnjAddressId(jnJB2bUnitModel, jnJSapAccountModel.getCustomerNumer());
			}
			if (addressModel == null)
			{
				addressModel = modelService.create(AddressModel.class);
				addressModel.setOwner(jnJB2bUnitModel);
			}
			addressModel.setJnJAddressId(jnJSapAccountModel.getCustomerNumer());
			addressModel.setTown(jnJSapAccountModel.getCity());
			addressModel.setPostalcode(jnJSapAccountModel.getPostalCode());
			addressModel.setDistrict(jnJSapAccountModel.getDistrict());
			addressModel.setStreetname(jnJSapAccountModel.getStreet());
			addressModel.setPhone1(jnJSapAccountModel.getTelephone());
			final CountryModel countryModel = commonI18NService.getCountry(jnJSapAccountModel.getCountry());
			addressModel.setCountry(countryModel);
			addressModel.setRegion(commonI18NService.getRegion(countryModel, jnJSapAccountModel.getRegion()));
			addressModel.setContactAddress(Boolean.TRUE);

		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
					"model not found for given key -" + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					exception, JnjRSACustomerDataLoadMapper.class);
		}
		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME, methodName, "ISO Code not matched found for given key -"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					exception, JnjRSACustomerDataLoadMapper.class);
		}
		try
		{
			JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
					"jnJB2bUnitModel.getName() :: " + jnJB2bUnitModel.getName(), JnjRSACustomerDataLoadMapper.class);
			jnJCustomerDataService.saveItemModel(addressModel);
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
					"model not saved into data base -" + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					exception, JnjRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjRSACustomerDataLoadMapper.class);
		return addressModel;
	}

	protected final JnJB2BUnitModel setContactAddressToB2BUnit(final AddressModel address, final JnJB2BUnitModel jnJB2BUnitModel)
	{
		jnJB2BUnitModel.setContactAddress(address);
		return jnJB2BUnitModel;
	}

	protected final JnJB2BUnitModel setIndicatorToB2BUnit(final String indicator, final JnJB2BUnitModel jnJB2BUnitModel)
	{
		jnJB2BUnitModel.setIndicator(indicator);
		return jnJB2BUnitModel;
	}

	public boolean mapCustomerDataforIndirectPayer(final JnJLaCustomerDTO jnjCustomerDTO)
	{
		return false;
	}

}
