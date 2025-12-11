/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.dataload.mapper;

import static com.jnj.la.core.util.JnjLaCommonUtil.getIdByCountry;

import de.hybris.platform.b2b.model.B2BBudgetModel;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.services.B2BCostCenterService;
import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceBudgetService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.StandardDateRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnJCustomerDTO;
import com.jnj.core.dto.JnJSalesOrgDTO;
import com.jnj.core.dto.PartnerFunction;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjCompanyService;
import com.jnj.core.services.customerEligibility.JnjCustomerEligibilityService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.SystemException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.core.dto.JnJLaSalesOrgDTO;
import com.jnj.la.core.dto.LaPartnerFunction;
import com.jnj.la.core.enums.JnjCountryEnum;
import com.jnj.la.core.model.JnJCompanyModel;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnjIndirectPayerModel;
import com.jnj.la.core.services.JnJLaCustomerDataService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.wrapper.JnjCompanyModelWrapper;
import org.apache.log4j.Logger;




/**
 *
 */
public class JnjLatamRSACustomerDataLoadMapper extends JnjRSACustomerDataLoadMapper
{
	public static final String CUSTOMER = "Customer ";
	private static final String NOT_SAVED_IN_THE_PLATFORM = " wasn't saved in the platform";
	private static final String MODEL_NOT_FOUND_FOR_GIVEN_KEY = "model not found for given key -";
	private static final String MODEL_NOT_SAVED_INTO_DATA_BASE = "model not saved into data base -";
	public static final String EXCEPTION_OCCURRED_WHILE_TRY_TO_SAVE_THE_DATA_INTO_DATA_BASE = "exception occurred while try to save the data into data base";
	public static final String UNKNOWN_IDENTIFIER_EXCEPTION_OCCURED = "unknown identifier exception occured -";
	public static final String DATA_IS_NOT_AVAILABLE_FOR_GIVEN_CODE = "data is not available for given code -";
	private static final Logger LOG = Logger.getLogger(JnjLatamRSACustomerDataLoadMapper.class);
	
	/** The B2B unit service. */
	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	/** The jnj customer data service. */
	@Autowired
	protected JnJLaCustomerDataService jnJCustomerDataService;

	/** The b2b cost center service. */
	@Autowired
	private B2BCostCenterService b2BCostCenterService;

	/** The customer eligibility service. */
	@Autowired
	protected JnjCustomerEligibilityService customerEligibilityService;

	/** The model service. */
	@Autowired
	protected ModelService modelService;

	/** The user service. */
	@Autowired
	protected UserService userService;

	@Autowired
	protected JnjCompanyService jnjCompanyService;

	/** The common i18 n service. */
	@Autowired
	protected CommonI18NService commonI18NService;

	/** The B2B commerce budget service. */
	@Autowired
	protected B2BCommerceBudgetService b2BCommerceBudgetService;

	/** The jnj address service. */
	@Autowired
	protected JnjGTAddressService jnjGTAddressService;



	@Override
	public Map<Boolean, String> mapCustomerDataForSoldTo(final JnJLaCustomerDTO customer)
	{
		final String methodName = "mapCustomerDataForSoldTo()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		String errorMessage = null;
		JnJLaB2BUnitModel jnjLaB2BUnitModel = null;
		Boolean recordStatus = Boolean.FALSE;

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
				"SoldTo Account type :: " + customer.getAccountType(), JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			final String customerType = Config
					.getParameter(Jnjb2bCoreConstants.UpsertCustomer.INDUSTRYCODEPREFIX + customer.getIndustryCode1());

			JnJB2BUnitModel jnJB2BUnitModel = (JnJB2BUnitModel) jnjGTB2BUnitService.getUnitForUid(customer.getCustomerNumer());
			if (jnJB2BUnitModel == null)
			{
				jnJB2BUnitModel = modelService.create(JnJLaB2BUnitModel.class);
			}
			jnJB2BUnitModel = createB2BUnitWithGlobalAttributes(customer, jnJB2BUnitModel);

			final AddressModel addressModel = extractAdressFromSapAccount(customer, jnJB2BUnitModel);
			// Contact Address Needed for Help Page
			jnJB2BUnitModel = setContactAddressToB2BUnit(addressModel, jnJB2BUnitModel);

			if (customerType != null
					&& customerType.equals(Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_KEY_ACCOUNT)))
			{

				if (customer.getCustomerType().equals(Jnjb2bCoreConstants.UpsertCustomer.STRING_HOSPITAL))
				{
					setIndicatorToB2BUnit(Jnjb2bCoreConstants.UpsertCustomer.STRING_HOSPITAL, jnJB2BUnitModel);
				}
				else if (customer.getCustomerType().equals(Jnjb2bCoreConstants.UpsertCustomer.STRING_DISTRIBUTOR))
				{
					setIndicatorToB2BUnit(Jnjb2bCoreConstants.UpsertCustomer.STRING_DISTRIBUTOR, jnJB2BUnitModel);
				}
			}
			else
			{
				setIndicatorToB2BUnit(customer.getCustomerType(), jnJB2BUnitModel);
			}

			if (jnJB2BUnitModel instanceof JnJLaB2BUnitModel)
			{
				jnjLaB2BUnitModel = (JnJLaB2BUnitModel) jnJB2BUnitModel;
				if(StringUtils.isNotBlank(customer.getIndustryCode1()))
				{
					jnjLaB2BUnitModel.setIndustryCode1(customer.getIndustryCode1());
				}
				if (Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_B.equalsIgnoreCase(customer.getBothIndicator()))
				{
					jnjLaB2BUnitModel.setBothIndicator(Jnjlab2bcoreConstants.UpsertCustomer.INDIRECT_CUSTOMER_Y);
					mapCustomerDataforIndirectCustomer(customer);
				}
				else
				{
					jnjLaB2BUnitModel.setBothIndicator(Jnjlab2bcoreConstants.UpsertCustomer.INDIRECT_CUSTOMER_N);
				}

				//Mapping Address Model From Sap Like Table
				final Set<AddressModel> newAddressCollection = new HashSet<>();

				newAddressCollection.add(addressModel);
				final List<JnJLaSalesOrgDTO> jnjSalesOrgDTOList = customer.getJnjLaSalesOrgDTOList();
				// This method is used to create an address model on the basis of ship to for Sold To
				final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList = createAddressModelForSoldTo(customer,
						jnjLaB2BUnitModel, newAddressCollection, addressModel, jnjSalesOrgDTOList);

				updateMasterCompanies(jnjLaB2BUnitModel, customer, newJnJSalesOrgCustomerModelList);

				jnJB2BUnitModel.setAddresses(newAddressCollection);
				final List<B2BBudgetModel> b2bBudgetModelList = createB2BBudgetForB2BUnit(customer, jnjLaB2BUnitModel);
				jnjLaB2BUnitModel.setBudgets(b2bBudgetModelList);

				final List<B2BCostCenterModel> b2bCostCenterModelList = createB2BCostCenterForB2BUnit(customer, jnjLaB2BUnitModel);
				jnjLaB2BUnitModel.setCostCenters(b2bCostCenterModelList);

				jnjLaB2BUnitModel.setType(customer.getAccountType());
				jnjLaB2BUnitModel.setLocName(getB2bUnitName(customer.getName1(), customer.getName2()));
				jnjLaB2BUnitModel.setCnpj(customer.getCnpj());
				setFreightType(customer, jnjLaB2BUnitModel);
				final CountryModel country = commonI18NService.getCountry(customer.getCountry());
				final List<String> priceGroupUpsertCountriesList = JnjLaCoreUtil
						.getCountriesList(Jnjlab2bcoreConstants.KEY_PRICE_GROUP_VALID_COUNTRIES);

				if (JnjLaCoreUtil.isCountryValidForACountriesList(country.getIsocode(), priceGroupUpsertCountriesList))
				{
					final String userPriceGroup = country.getName() + customer.getRegion();
					final UserPriceGroup userPriceGrp = UserPriceGroup.valueOf(userPriceGroup);
					jnjLaB2BUnitModel.setUserPriceGroup(userPriceGrp);
				}
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
						"jnjLaB2BUnitModel name :: " + jnjLaB2BUnitModel.getName(), JnjLatamRSACustomerDataLoadMapper.class);

				recordStatus = getJnJCustomerDataService().saveItemModel(jnjLaB2BUnitModel);
				
			}
			else
			{
				errorMessage = "The given customer " + jnJB2BUnitModel.getUid()
						+ " is not a LATAM Customer. Skipping the creation of B2B unit.";
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, errorMessage,
						JnjLatamRSACustomerDataLoadMapper.class);
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + "-", methodName, "Unknown Identifier Exception occurred" + exception + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + "-", methodName, "model not found for given key " + exception + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + "-", methodName, "model is not saved into hybris database" + exception + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = CUSTOMER + customer.getCustomerNumer() + NOT_SAVED_IN_THE_PLATFORM;
			recordStatus = Boolean.FALSE;
		}
		catch (final Exception exception)
		{

			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + "-",
					methodName, EXCEPTION_OCCURRED_WHILE_TRY_TO_SAVE_THE_DATA_INTO_DATA_BASE + exception + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}

		errorFlagAndMessage.put(recordStatus, errorMessage);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return errorFlagAndMessage;
	}

	private static void setFreightType(JnJLaCustomerDTO customer, JnJLaB2BUnitModel jnjLaB2BUnitModel) {
		if (StringUtils.isNotBlank(customer.getCustomerFreightType())) {
			jnjLaB2BUnitModel.setCustomerFreightType(customer.getCustomerFreightType());
		}
	}

	/**
	 * This method is used to create an address model on the basis of Ship To for sold To.
	 *
	 * @param jnJB2BUnitModel
	 *           the JnJB2BUnitModel
	 * @param newAddressCollection
	 *           the Collection<AddressModel>
	 * @param addressModel
	 *           the AddressModel
	 * @return the list
	 */
	private List<JnJSalesOrgCustomerModel> createAddressModelForSoldTo(final JnJLaCustomerDTO customerDTO,
			final JnJB2BUnitModel jnJB2BUnitModel, final Collection<AddressModel> newAddressCollection,
			final AddressModel addressModel, final List<JnJLaSalesOrgDTO> jnjSalesOrgModelList)
	{
		final String methodName = "createAddressModelForSoldTo";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Set<String> setMDD = new HashSet<>();
		final Set<String> setPHR = new HashSet<>();
		JnJLaB2BUnitModel jnjLaB2BUnitModel;
		/*
		 * jnjSalesOrgCustomerModelExistingList is the List containing all the JnJSalesOrgCustomerModels stored in HYBIRS
		 * for the provided B2bUnit
		 */
		List<JnJSalesOrgCustomerModel> jnjSalesOrgCustomerModelExistingList = new ArrayList<>();

		/*
		 * newJnJSalesOrgCustomerModelList is the List that will contain final JnJSalesOrgCustomerModels to be associated
		 * on the B2bUnit
		 */
		final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList = new ArrayList<>();


		/* Fetch existing JnJSalesOrgCustomerModel from HYBRIS based on B2bUnit */
		if (null != jnJB2BUnitModel.getPk())
		{
			jnjSalesOrgCustomerModelExistingList = jnJCustomerDataService
					.getJnJSalesOrgCustomerModeBylId(jnJB2BUnitModel.getPk().toString());
		}

		/* Create MDD and PHR sets based on incoming JnJSalesOrgCustomerModels */
		createJnJSalesOrgForMDDAndPHR(jnjSalesOrgModelList, setMDD, setPHR);

		processAndUpdateSalesOrgCustomer(jnJB2BUnitModel, setMDD, setPHR, newJnJSalesOrgCustomerModelList,
				jnjSalesOrgCustomerModelExistingList);

		// Call the customer eligibility method to add & remove the restricted category model.
		if (jnJB2BUnitModel instanceof JnJLaB2BUnitModel)
		{
			jnjLaB2BUnitModel = (JnJLaB2BUnitModel) jnJB2BUnitModel;
			jnjLaB2BUnitModel.setSalesOrg(newJnJSalesOrgCustomerModelList);



			changeCustomerEligibility(setMDD, setPHR, jnjLaB2BUnitModel);

			/* This Set is used to Check whether an address exists while creating new address */
			final Set<String> existingAddressIdSet = new HashSet<>();
			/* Adding all the Existing JnJ Address Ids to the Set */
			for (final AddressModel listAddressModel : newAddressCollection)
			{
				existingAddressIdSet.add(listAddressModel.getJnJAddressId());
			}

			final List<LaPartnerFunction> partnerFunctionModelList = customerDTO.getLaPartnerFunction();
			if (partnerFunctionModelList != null && !partnerFunctionModelList.isEmpty())
			{
				for (final PartnerFunction partner : partnerFunctionModelList)
				{
					if (partner.getPartnerFunctionType() != null)
					{
						if (partner.getPartnerFunctionType().equals(Jnjb2bCoreConstants.UpsertCustomer.PARTNERFUNCTION_SHIPTO))
						{
							final AddressModel existAddressModel = jnjGTAddressService.getAddressForJnjAddressId(jnjLaB2BUnitModel,
									partner.getPartnerFunctionID());
							// Checking whether the new address is in the existing address of the B2B Unit and exist in the database
							if (existAddressModel != null)
							{
								// If true the add the new address final in the B2B final Unit with final the shipment flag setting true
								existAddressModel.setShippingAddress(Boolean.TRUE);
								modelService.save(existAddressModel);
								newAddressCollection.add(existAddressModel);
							}
							else if (addressModel.getJnJAddressId().equals(partner.getPartnerFunctionID()))
							{
								addressModel.setShippingAddress(Boolean.TRUE);
								modelService.save(addressModel);
							}
							/*
							 * Only Create A new Address if there is no existing Address in the newAdressCollection with the
							 * same JnjAddressID
							 */
							else if (!existingAddressIdSet.contains(partner.getPartnerFunctionID()))
							{
								final AddressModel newAddressModel = modelService.create(AddressModel.class);
								newAddressModel.setJnJAddressId(partner.getPartnerFunctionID());
								newAddressModel.setOwner(jnjLaB2BUnitModel);
								newAddressModel.setShippingAddress(Boolean.TRUE);
								modelService.save(newAddressModel);
								newAddressCollection.add(newAddressModel);
								existingAddressIdSet.add(newAddressModel.getJnJAddressId());
							}
						}
					}
				}
			}
		}

		for (final JnJLaSalesOrgDTO jnjSalesOrgModel : jnjSalesOrgModelList)
		{
			if (null != jnjSalesOrgModel.getPartialDelivery() && StringUtils.isNotEmpty(jnjSalesOrgModel.getPartialDelivery())
					&& (StringUtils.equalsIgnoreCase(jnjSalesOrgModel.getPartialDelivery(),
							Config.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.UPSERT_CUSTOMER_KZTLF_B))
							|| StringUtils.equalsIgnoreCase(jnjSalesOrgModel.getPartialDelivery(),
									Config.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.UPSERT_CUSTOMER_KZTLF_C)))
					&& jnJB2BUnitModel instanceof JnJLaB2BUnitModel)
			{
				final JnJLaB2BUnitModel jnJLaB2BUnitModel = (JnJLaB2BUnitModel) jnJB2BUnitModel;
				jnJLaB2BUnitModel.setPartialDelivFlag(Boolean.FALSE);
			}
			else
			{
				final JnJLaB2BUnitModel jnJLaB2BUnitModel = (JnJLaB2BUnitModel) jnJB2BUnitModel;
				jnJLaB2BUnitModel.setPartialDelivFlag(Boolean.TRUE);
			}
		}

		/* CR--35029 end */
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return newJnJSalesOrgCustomerModelList;
	}


	/**
	 * This method is used to create sales org model for Product Attribute 1 , and for customer number.
	 *
	 * @param setMDD
	 *           the Set<String>
	 * @param setPHR
	 *           the Set<String>
	 */
	private void createJnJSalesOrgForMDDAndPHR(final List<JnJLaSalesOrgDTO> jnjSalesOrgModelList, final Set<String> setMDD,
			final Set<String> setPHR)
	{
		final String methodName = "createJnJSalesOrgForMDDAndPHR";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			if (null != jnjSalesOrgModelList)
			{
				for (final JnJSalesOrgDTO jnJSapSalesOrgModel : jnjSalesOrgModelList)
				{
					final String mddFlag = jnJSapSalesOrgModel.getProductAttribute1();
					final String pharmaFlag = jnJSapSalesOrgModel.getProductAttribute2();
					boolean sectorMdd = false;
					boolean sectorPhr = false;
					LOG.info("mddFlag:::::::::::::: "+mddFlag+" pharmaFlag::::::::::::: "+sectorPhr);
					if (StringUtils.isEmpty(mddFlag)
							|| !StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.UpsertCustomer.ATTRIBUTE_MDD_X, mddFlag))
					{
						LOG.info("sectorMdd ::::::: ");
						setMDD.add(jnJSapSalesOrgModel.getSalesOrganization());
						sectorMdd = true;
					}
					if (StringUtils.isEmpty(pharmaFlag)
							|| !StringUtils.equalsIgnoreCase(Jnjb2bCoreConstants.UpsertCustomer.ATTRIBUTE_MDD_X, pharmaFlag))

					{
						LOG.info("sectorPhr::::::: ");
						setPHR.add(jnJSapSalesOrgModel.getSalesOrganization());
						sectorPhr = true;
					}
					LOG.info("sectorMdd values:::::::::::::: "+sectorMdd+" sectorPhr values::::::::::::: "+sectorPhr);
					LOG.info("SalesOrg values: "+jnJSapSalesOrgModel.getSalesOrganization().toString());
				}
			}
		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_FOUND_FOR_GIVEN_KEY + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, EXCEPTION_OCCURRED_WHILE_TRY_TO_SAVE_THE_DATA_INTO_DATA_BASE + exception + "-"
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
	}

	/**
	 * Process incoming JnJSapSalesOrgModels and map them to existing JnJSalesOrgCustomerModels.
	 *
	 * @param jnJB2BUnitModel
	 *           the jn j b2 b unit model
	 * @param setMDD
	 *           the set mdd
	 * @param setPHR
	 *           the set phr
	 * @param newJnJSalesOrgCustomerModelList
	 *           the new jn j sales org customer model list
	 * @param jnjSalesOrgCustomerModelExistingList
	 *           the jnj sales org customer model existing list
	 */
	private void processAndUpdateSalesOrgCustomer(final JnJB2BUnitModel jnJB2BUnitModel, final Set<String> setMDD,
			final Set<String> setPHR, final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList,
			final List<JnJSalesOrgCustomerModel> jnjSalesOrgCustomerModelExistingList)
	{
		final String methodName = "processAndUpdateSalesOrgCustomer()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		/* Adding all Models from exiting list to modifiedexistingList */
		newJnJSalesOrgCustomerModelList.addAll(jnjSalesOrgCustomerModelExistingList);

		/*
		 * SET ZERO OPERATION:: Remove the existing JnJSalesOrgCustomerModel if the size of MDD and PHR set is zero.
		 */
		for (final JnJSalesOrgCustomerModel existingJnjSalesOrgCustModel : jnjSalesOrgCustomerModelExistingList)
		{
			/* This block will check that sales org customer is inserted from sales org customer model */
			if (!existingJnjSalesOrgCustModel.getIsSalesOrgCustomer())
			{
				if ((setMDD.isEmpty()
						&& StringUtils.equalsIgnoreCase(existingJnjSalesOrgCustModel.getSector(), Jnjb2bCoreConstants.MDD_SECTOR))
						|| (setPHR.isEmpty() && StringUtils.equalsIgnoreCase(existingJnjSalesOrgCustModel.getSector(),
								Jnjb2bCoreConstants.PHR_SECTOR)))
				{
					newJnJSalesOrgCustomerModelList.remove(existingJnjSalesOrgCustModel);
					modelService.remove(existingJnjSalesOrgCustModel);
				}
			}
		} //End of existingJnjSalesOrgCustomerModelList Loop


		if (!newJnJSalesOrgCustomerModelList.isEmpty())
		{
			/*
			 * SET ONE OPERATION:: MDD SET sizes are equal to 1
			 */
			if (setMDD.size() == 1)
			{
				updateSalesOrgCustomerForSector(jnJB2BUnitModel, setMDD, newJnJSalesOrgCustomerModelList, false,
						Jnjb2bCoreConstants.MDD_SECTOR);
			}
			/*
			 * SET MORE THAN ONE OPERATION:: MDD SET size is greater then 1.
			 */
			else if (setMDD.size() > 1)
			{
				updateSalesOrgCustomerForSector(jnJB2BUnitModel, setMDD, newJnJSalesOrgCustomerModelList, true,
						Jnjb2bCoreConstants.MDD_SECTOR);
			}
			/*
			 * SET ONE OPERATION:: PHR SET sizes are equal to 1
			 */
			if (setPHR.size() == 1)
			{
				updateSalesOrgCustomerForSector(jnJB2BUnitModel, setPHR, newJnJSalesOrgCustomerModelList, false,
						Jnjb2bCoreConstants.PHR_SECTOR);
			}
			/*
			 * SET MORE THAN ONE OPERATION:: PHR SET size is greater then 1.
			 */
			else if (setPHR.size() > 1)
			{
				updateSalesOrgCustomerForSector(jnJB2BUnitModel, setPHR, newJnJSalesOrgCustomerModelList, true,
						Jnjb2bCoreConstants.PHR_SECTOR);
			}
			/*
			 * Break the existingLoop if the JnJSalesOrgCustomerModel is found and processed. Iterate to the next
			 * JnJSapSalesOrgModel
			 */
		}
		/*
		 * newJnJSalesOrgCustomerModelList is empty. We need to create all new JnJSalesOrgCustomers
		 */
		else
		{
			createJnjSalesOrgCustomer(jnJB2BUnitModel, setMDD, setPHR, newJnJSalesOrgCustomerModelList);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
	}

	/**
	 * This method updates the SalesOrgCustomer with respect to the Sector.
	 *
	 * @param jnJB2BUnitModel
	 *           the jn j b2 b unit model
	 * @param sectorSet
	 *           the sector set
	 * @param newJnJSalesOrgCustomerModelList
	 *           the new jn j sales org customer model list
	 * @param moreThanOneSalesOrg
	 *           the more than one sales org
	 * @param sectorToUpdate
	 *           the sector to update
	 */
	private void updateSalesOrgCustomerForSector(final JnJB2BUnitModel jnJB2BUnitModel, final Set<String> sectorSet,
			final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList, final boolean moreThanOneSalesOrg,
			final String sectorToUpdate)
	{

		boolean existingFound = false;

		/* Looping thought the existing JnJSalesOrgCustomerModels returned from HYBRIS */
		for (final JnJSalesOrgCustomerModel existingJnjSalesOrgCustModel : newJnJSalesOrgCustomerModelList)
		{
			if (StringUtils.equalsIgnoreCase(existingJnjSalesOrgCustModel.getSector(), sectorToUpdate))
			{
				/* Customer Sales Org Matched in exiting list with the incoming sector */
				existingFound = true;
				if (!moreThanOneSalesOrg)
				{
					final String salesOrg = existingJnjSalesOrgCustModel.getIsSalesOrgCustomer().equals(Boolean.FALSE)
							? sectorSet.iterator().next() : existingJnjSalesOrgCustModel.getSalesOrg();
					if (salesOrg != null && Strings.isNotEmpty(salesOrg))
					{
						existingJnjSalesOrgCustModel.setSalesOrg(salesOrg);
					}
					existingJnjSalesOrgCustModel.setSalesOrgList(Collections.<String> emptyList());
				}
				else
				{
					// When multiple sales orgs defined don't update the salesOrg field, SalesOrgCron job will handle this situation
					final List<String> salesOrgList = new ArrayList<>();
					salesOrgList.addAll(sectorSet);
					existingJnjSalesOrgCustModel.setSalesOrgList(salesOrgList);
				}
				modelService.save(existingJnjSalesOrgCustModel);
				break;
			}
		} // End of existingLoop

		/* If Customer Sales Organization not in exiting list with the incoming sector then create New */
		if (!existingFound)
		{
			final JnJSalesOrgCustomerModel newJnjSalesOrgCustModel = modelService.create(JnJSalesOrgCustomerModel.class);
			newJnjSalesOrgCustModel.setCustomerId(jnJB2BUnitModel);
			newJnjSalesOrgCustModel.setSector(sectorToUpdate);
			newJnjSalesOrgCustModel.setIsSalesOrgCustomer(Boolean.FALSE);
			if (!moreThanOneSalesOrg)
			{
				newJnjSalesOrgCustModel.setSalesOrg(sectorSet.iterator().next());
				newJnjSalesOrgCustModel.setSalesOrgList(Collections.<String> emptyList());
			}
			else
			{
				final List<String> salesOrgList = new ArrayList<>();
				salesOrgList.addAll(sectorSet);
				newJnjSalesOrgCustModel.setSalesOrgList(salesOrgList);
			}
			modelService.save(newJnjSalesOrgCustModel);
			newJnJSalesOrgCustomerModelList.add(newJnjSalesOrgCustModel);
		}
	}


	/**
	 * this method is used to create the JnjSalesORg Customer model.
	 *
	 * @param jnJB2BUnitModelKeyAccountExist
	 *           the jn j b2 b unit model key account exist
	 * @param setMDD
	 *           the set mdd
	 * @param setPHR
	 *           the set phr
	 * @param newJnJSalesOrgCustomerModelList
	 *           the new jn j sales org customer model list
	 */
	private void createJnjSalesOrgCustomer(final JnJB2BUnitModel jnJB2BUnitModelKeyAccountExist, final Set<String> setMDD,
			final Set<String> setPHR, final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList)
	{
		JnJSalesOrgCustomerModel newJnjSalesOrgCustModel;
		if (setMDD.size() == 1)
		{
			newJnjSalesOrgCustModel = modelService.create(JnJSalesOrgCustomerModel.class);
			newJnjSalesOrgCustModel.setCustomerId(jnJB2BUnitModelKeyAccountExist);
			newJnjSalesOrgCustModel.setSector(Jnjb2bCoreConstants.MDD_SECTOR);
			newJnjSalesOrgCustModel.setSalesOrg(setMDD.iterator().next());
			newJnjSalesOrgCustModel.setIsSalesOrgCustomer(Boolean.FALSE);
			modelService.save(newJnjSalesOrgCustModel);
			newJnJSalesOrgCustomerModelList.add(newJnjSalesOrgCustModel);

		}
		// if the MDD set size is greater then one and it's not updated the create the new Jnj Sales Org Cust Model object.
		else if (setMDD.size() > 1)
		{
			final List<String> salesOrgList = new ArrayList<>();
			salesOrgList.addAll(setMDD);
			newJnjSalesOrgCustModel = modelService.create(JnJSalesOrgCustomerModel.class);
			newJnjSalesOrgCustModel.setCustomerId(jnJB2BUnitModelKeyAccountExist);
			newJnjSalesOrgCustModel.setSalesOrg(StringUtils.EMPTY);
			newJnjSalesOrgCustModel.setSalesOrgList(salesOrgList);
			newJnjSalesOrgCustModel.setSector(Jnjb2bCoreConstants.MDD_SECTOR);
			newJnjSalesOrgCustModel.setIsSalesOrgCustomer(Boolean.FALSE);
			modelService.save(newJnjSalesOrgCustModel);
			newJnJSalesOrgCustomerModelList.add(newJnjSalesOrgCustModel);
		}
		// if the PHR set size is equal to one and it's not updated the create the new Jnj Sales Org Cust Model object.
		if (setPHR.size() == 1)
		{
			newJnjSalesOrgCustModel = modelService.create(JnJSalesOrgCustomerModel.class);
			newJnjSalesOrgCustModel.setCustomerId(jnJB2BUnitModelKeyAccountExist);
			newJnjSalesOrgCustModel.setSector(Jnjb2bCoreConstants.PHR_SECTOR);
			newJnjSalesOrgCustModel.setSalesOrg(setPHR.iterator().next());
			newJnjSalesOrgCustModel.setIsSalesOrgCustomer(Boolean.FALSE);
			modelService.save(newJnjSalesOrgCustModel);
			newJnJSalesOrgCustomerModelList.add(newJnjSalesOrgCustModel);
		}
		// if the MDD set size is greater then one and it's not updated the create the new Jnj Sales Org Cust Model object.
		else if (setPHR.size() > 1)
		{
			final List<String> salesOrgList = new ArrayList<>();
			salesOrgList.addAll(setPHR);
			newJnjSalesOrgCustModel = modelService.create(JnJSalesOrgCustomerModel.class);
			newJnjSalesOrgCustModel.setCustomerId(jnJB2BUnitModelKeyAccountExist);
			newJnjSalesOrgCustModel.setSalesOrg(StringUtils.EMPTY);
			newJnjSalesOrgCustModel.setSalesOrgList(salesOrgList);
			newJnjSalesOrgCustModel.setSector(Jnjb2bCoreConstants.PHR_SECTOR);
			newJnjSalesOrgCustModel.setIsSalesOrgCustomer(Boolean.FALSE);
			modelService.save(newJnjSalesOrgCustModel);
			newJnJSalesOrgCustomerModelList.add(newJnjSalesOrgCustModel);
		}
	}

	/**
	 * This method Adds/Updates the Parent Company bases upon the SalesOrgCustomers.
	 *
	 * @param jnJB2BUnitModel
	 *           the jnj B2B unit model
	 * @param customerDTO
	 *           customer data
	 * @param newJnJSalesOrgCustomerModelList
	 *           the new jnj sales org customer model list
	 */
	private void updateMasterCompanies(final JnJB2BUnitModel jnJB2BUnitModel, final JnJLaCustomerDTO customerDTO,
			final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList)
	{
		final String methodName = "updateMasterCompany";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			/* Fetching the Groups Associated with the B2B Unit */
			final Set<PrincipalGroupModel> value = jnJB2BUnitModel.getGroups();
			final Set<PrincipalGroupModel> updatedGroups = new HashSet<>();
			updatedGroups.addAll(value);

			final String customerCountryIso = customerDTO.getCountry();
			updateMasterCompanyCountry(customerCountryIso, updatedGroups);

			final Map<String, JnjCompanyModelWrapper> pharmaCountryMap = new HashMap<>();
			final Map<String, JnjCompanyModelWrapper> medicalCountryMap = new HashMap<>();

			/* Fetching Master MD/PHR Companies from Hybris */
			for (final JnjCountryEnum jnjCountryEnum : JnjCountryEnum.values())
			{
				populateCountryMap(pharmaCountryMap, Jnjb2bCoreConstants.UpsertCustomer.SECTOR_PHR, jnjCountryEnum);
				populateCountryMap(medicalCountryMap, Jnjb2bCoreConstants.UpsertCustomer.SECTOR_MDD, jnjCountryEnum);
			}

			/* Iterating JnJSalesOrgCustomerModels to identify Master PHR/MD Companies */
			for (final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel : newJnJSalesOrgCustomerModelList)
			{
				handleSectorEnabled(pharmaCountryMap, medicalCountryMap, jnjSalesOrgCustomerModel, customerCountryIso);
			}

			/* Updating the User Groups with the Master MD/PHR Companies */
			
			LOG.info("pharmaCountryMap values::::::::::::::::: "+pharmaCountryMap.toString()+" medicalCountryMap value::::::::::::::::: "+medicalCountryMap);
			updateMasterSectorCompaniesForAllCountries(pharmaCountryMap, updatedGroups);
			updateMasterSectorCompaniesForAllCountries(medicalCountryMap, updatedGroups);

			//Added code to verify updated user groups.
			for(PrincipalGroupModel userGroupVal:updatedGroups){
				LOG.info("Final user group value: "+userGroupVal.getUid());
			}
			
			/* Adding the Updated Groups to B2B Unit */
			jnJB2BUnitModel.setGroups(updatedGroups);
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "Exception Occurred while Adding/Updating Master Company" + businessException,
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
	}

	/**
	 * This method updated the Master Sector Company associated with the B2B Unit.
	 *
	 * @param sectorCountryFlag
	 *           the sector country flag
	 * @param updatedGroups
	 *           the updated groups
	 * @param sectorCountryCompany
	 *           the sector country company
	 */
	private void updateMasterSectorCompanies(final boolean sectorCountryFlag, final Set<PrincipalGroupModel> updatedGroups,
			final JnJCompanyModel sectorCountryCompany)
	{
		LOG.info("sectorCountryFlag value:::::::::::::::: " + sectorCountryFlag+" sectorCountryCompany value:::::::::::::::: "+sectorCountryCompany.getUid());
		if (sectorCountryFlag)
		{
			if (!updatedGroups.contains(sectorCountryCompany))
			{
				updatedGroups.add(sectorCountryCompany);
				LOG.info("sectorCountryCompany value: "+sectorCountryCompany.getUid());
			}
		}
		else
		{
			if (updatedGroups.contains(sectorCountryCompany))
			{
				updatedGroups.remove(sectorCountryCompany);
				LOG.info("sectorCountryCompany #####: "+sectorCountryCompany.getUid());
			}
		}
	}


	/**
	 * this method is used to create B2B Budget for given B2B Unit.
	 *
	 * @param jnJSapAccountModel
	 *           the JnJSapAccountModel
	 * @param jnJB2BUnitModelKeyAccount
	 *           the JnJB2BUnitModel
	 * @return List<B2BBudgetModel>
	 */
	private List<B2BBudgetModel> createB2BBudgetForB2BUnit(final JnJLaCustomerDTO jnJSapAccountModel,
			final JnJB2BUnitModel jnJB2BUnitModelKeyAccount)
	{
		final String methodName = "createB2BBudgetForB2BUnit";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final List<B2BBudgetModel> b2bBudgetModelList = new ArrayList();
		try
		{
			B2BBudgetModel b2bBudgetModel = b2BCommerceBudgetService.getBudgetModelForCode(jnJSapAccountModel.getCustomerNumer());
			if (b2bBudgetModel == null)
			{
				b2bBudgetModel = modelService.create(B2BBudgetModel.class);
			}
			final Calendar calendar = Calendar.getInstance();
			final Date startdate = calendar.getTime();
			calendar.add(Calendar.YEAR, 3);
			final Date endDate = calendar.getTime();
			final StandardDateRange datePeriodValid = new StandardDateRange(startdate, endDate);
			b2bBudgetModel.setDateRange(datePeriodValid);
			b2bBudgetModel.setCode(jnJSapAccountModel.getCustomerNumer());
			if (JnJCommonUtil.checkValidCountry(jnJSapAccountModel.getCountry()))
			{
				final String currencyIsoCode = Config
						.getParameter(jnJSapAccountModel.getCountry().toLowerCase() + Jnjb2bCoreConstants.KEY_Valid_CURRENCY);
				b2bBudgetModel.setCurrency(commonI18NService.getCurrency(currencyIsoCode));
			}
			b2bBudgetModel.setUnit(jnJB2BUnitModelKeyAccount);
			b2bBudgetModelList.add(b2bBudgetModel);

		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + Logging.HYPHEN, methodName, "model not found for given code -" + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);

		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + Logging.HYPHEN,
					methodName, EXCEPTION_OCCURRED_WHILE_TRY_TO_SAVE_THE_DATA_INTO_DATA_BASE + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		return b2bBudgetModelList;
	}


	/**
	 * this method is used to create b2b cost center for B2B Unit.
	 *
	 * @param jnJSapAccountModel
	 *           the JnJSapAccountModel
	 * @param jnJB2BUnitModelKeyAccount
	 *           the JnJB2BUnitModel
	 * @return List<B2BCostCenterModel>
	 */
	private List<B2BCostCenterModel> createB2BCostCenterForB2BUnit(final JnJLaCustomerDTO jnJSapAccountModel,
			final JnJB2BUnitModel jnJB2BUnitModelKeyAccount)
	{
		final String methodName = "createB2BCostCenterForB2BUnit";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final List<B2BCostCenterModel> b2bCostCenterModelList = new ArrayList();
		try
		{
			B2BCostCenterModel b2bCostCenterModel = (B2BCostCenterModel) b2BCostCenterService
					.getCostCenterForCode(jnJSapAccountModel.getCustomerNumer());
			if (b2bCostCenterModel == null)
			{
				b2bCostCenterModel = modelService.create(B2BCostCenterModel.class);
			}
			b2bCostCenterModel.setCode(jnJSapAccountModel.getCustomerNumer());
			if (JnJCommonUtil.checkValidCountry(jnJSapAccountModel.getCountry()))
			{
				final String currencyIsoCode = Config
						.getParameter(jnJSapAccountModel.getCountry().toLowerCase() + Jnjb2bCoreConstants.KEY_Valid_CURRENCY);
				b2bCostCenterModel.setCurrency(commonI18NService.getCurrency(currencyIsoCode));
			}
			b2bCostCenterModel.setUnit(jnJB2BUnitModelKeyAccount);
			b2bCostCenterModelList.add(b2bCostCenterModel);

		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);

		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, "model not found for given code -" + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);

		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, EXCEPTION_OCCURRED_WHILE_TRY_TO_SAVE_THE_DATA_INTO_DATA_BASE + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		return b2bCostCenterModelList;
	}


	/**
	 * This Method is used for map the data for key account and it will do further processing for key account.
	 */

	@Override
	public Map<Boolean, String> mapCustomerDataForKeyAccount(final JnJLaCustomerDTO customerDTO)
	{
		final String methodName = "mapCustomerDataForKeyAccount()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		String errorMessage = null;
		JnJLaB2BUnitModel jnjLaB2BUnitModelKeyAccount = null;
		Boolean recordStatus = Boolean.FALSE;

		//Increasing the Record Retry Count before processing.
		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
				"Key Account type :: " + customerDTO.getAccountType(), JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			if (null != customerDTO.getCustomerType())
			{
				JnJB2BUnitModel jnJB2BUnitModelKeyAccount = (JnJB2BUnitModel) jnjGTB2BUnitService
						.getUnitForUid(customerDTO.getCustomerNumer());

				final Collection<AddressModel> newAdressCollection = new ArrayList<>();
				if (jnJB2BUnitModelKeyAccount == null)
				{
					jnJB2BUnitModelKeyAccount = modelService.create(JnJLaB2BUnitModel.class);
				}

				jnJB2BUnitModelKeyAccount = createB2BUnitWithGlobalAttributes(customerDTO, jnJB2BUnitModelKeyAccount);

				final AddressModel addressModel = extractAdressFromSapAccount(customerDTO, jnJB2BUnitModelKeyAccount);
				newAdressCollection.add(addressModel);
				jnJB2BUnitModelKeyAccount.setAddresses(newAdressCollection);
				// Setting the Country in JnjB2b Unit Model for Key Account
				final CountryModel countryModel = commonI18NService.getCountry(customerDTO.getCountry());
				jnJB2BUnitModelKeyAccount.setCountry(countryModel);


				//Creating a B2BBudget For the B2B Unit
				final List<B2BBudgetModel> b2bBudgetModelList = createB2BBudgetForB2BUnit(customerDTO, jnJB2BUnitModelKeyAccount);
				jnJB2BUnitModelKeyAccount.setBudgets(b2bBudgetModelList);

				//Creating a B2BCostCenterModel For the B2B Unit
				final List<B2BCostCenterModel> b2bCostCenterModelList = createB2BCostCenterForB2BUnit(customerDTO,
						jnJB2BUnitModelKeyAccount);
				jnJB2BUnitModelKeyAccount.setCostCenters(b2bCostCenterModelList);

				jnJB2BUnitModelKeyAccount = setIndicatorToB2BUnit(customerDTO.getCustomerType(), jnJB2BUnitModelKeyAccount);
				//Saving the Key Account
				if (jnJB2BUnitModelKeyAccount instanceof JnJLaB2BUnitModel)
				{
					jnjLaB2BUnitModelKeyAccount = (JnJLaB2BUnitModel) jnJB2BUnitModelKeyAccount;
					jnjLaB2BUnitModelKeyAccount.setType(customerDTO.getAccountType());
				}

				final Set<PrincipalGroupModel> soldToParentSet = new HashSet<>();
				final Set<PrincipalGroupModel> newMemberSet = new HashSet<>();

				//Get the SalesOrg List for Key Account
				final List<JnJLaSalesOrgDTO> jnjSapSalesOrgModelList = customerDTO.getJnjLaSalesOrgDTOList();
				//Get the Saved KeyAccount
				final JnJB2BUnitModel jnJB2BUnitModelKeyAccountExist = (JnJB2BUnitModel) jnjGTB2BUnitService
						.getUnitForUid(customerDTO.getCustomerNumer());

				//Get the  existing child of the Key Account if it was saved before
				final Set<PrincipalModel> principalGroupModelSet = jnJB2BUnitModelKeyAccountExist.getMembers();

				final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList = saveB2BUnitForKeyAcc(soldToParentSet,
						newMemberSet, jnjSapSalesOrgModelList, jnJB2BUnitModelKeyAccountExist, principalGroupModelSet, customerDTO);

				/* 35481: Company Migration Changes */
				updateMasterCompanies(jnJB2BUnitModelKeyAccountExist, customerDTO, newJnJSalesOrgCustomerModelList);

				recordStatus = getJnJCustomerDataService().saveItemModel(jnJB2BUnitModelKeyAccountExist);
				realignOldSoldTo(customerDTO, newMemberSet, jnJB2BUnitModelKeyAccountExist);
			}
			else
			{
				errorMessage = CUSTOMER + customerDTO.getCustomerNumer() + " has an industry code not defined";
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, errorMessage,
						JnjLatamRSACustomerDataLoadMapper.class);
			}
		}
		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, UNKNOWN_IDENTIFIER_EXCEPTION_OCCURED + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_FOUND_FOR_GIVEN_KEY + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final NullPointerException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, DATA_IS_NOT_AVAILABLE_FOR_GIVEN_CODE + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, "exception occurred while try to save the data into data base" + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = throwable.getMessage();
			recordStatus = Boolean.FALSE;
		}

		errorFlagAndMessage.put(recordStatus, errorMessage);
		// End of For Loop // end of if block
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return errorFlagAndMessage;
	}

	/**
	 * This method is used for mapping all SOLD To Data to a Key Account.
	 *
	 * @param soldToParentSet
	 *           the Set<PrincipalGroupModel>
	 * @param newMemberSet
	 *           the Set<PrincipalGroupModel>
	 * @param jnJB2BUnitModelKeyAccountExist
	 *           the JnJB2BUnitModel
	 * @param principalGroupModelSet
	 *           the Set<PrincipalModel>
	 * @return the list
	 */
	private List<JnJSalesOrgCustomerModel> saveB2BUnitForKeyAcc(final Set<PrincipalGroupModel> soldToParentSet,
			final Set<PrincipalGroupModel> newMemberSet, final List<JnJLaSalesOrgDTO> jnjSalesOrgModelList,
			final JnJB2BUnitModel jnJB2BUnitModelKeyAccountExist, final Set<PrincipalModel> principalGroupModelSet,
			final JnJLaCustomerDTO jnJSapAccountModel)
	{
		final String methodName = "saveB2BUnitForKeyAcc()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Set<String> setMDD = new HashSet<>();
		final Set<String> setPHR = new HashSet<>();
		JnJLaB2BUnitModel jnjLaB2BUnitModelKeyAccountExist = null;
		/*
		 * existingJnjSalesOrgCustomerModelList is the List containing all the JnJSalesOrgCustomerModels stored in HYBIRS
		 * for the provided B2bUnit
		 */
		List<JnJSalesOrgCustomerModel> existingJnjSalesOrgCustomerModelList = new ArrayList<>();

		/*
		 * newJnJSalesOrgCustomerModelList is the List that will contain final JnJSalesOrgCustomerModels to be associated
		 * on the B2bUnit
		 */
		final List<JnJSalesOrgCustomerModel> newJnJSalesOrgCustomerModelList = new ArrayList<>();

		/* Fetch existing JnJSalesOrgCustomerModel from HYBRIS based on B2bUnit */
		if (null != jnJB2BUnitModelKeyAccountExist.getPk())
		{
			existingJnjSalesOrgCustomerModelList = jnJCustomerDataService
					.getJnJSalesOrgCustomerModeBylId(jnJB2BUnitModelKeyAccountExist.getPk().toString());
		}

		/* Create MDD and PHR sets based on incoming JnJSalesOrgCustomerModels */
		createJnJSalesOrgForMDDAndPHR(jnjSalesOrgModelList, setMDD, setPHR);

		processAndUpdateSalesOrgCustomer(jnJB2BUnitModelKeyAccountExist, setMDD, setPHR, newJnJSalesOrgCustomerModelList,
				existingJnjSalesOrgCustomerModelList);

		// Call the customer eligibility method to add & remove the restricted category model.
		if (jnJB2BUnitModelKeyAccountExist instanceof JnJLaB2BUnitModel)
		{
			jnjLaB2BUnitModelKeyAccountExist = (JnJLaB2BUnitModel) jnJB2BUnitModelKeyAccountExist;
			jnjLaB2BUnitModelKeyAccountExist.setSalesOrg(newJnJSalesOrgCustomerModelList);

			changeCustomerEligibility(setMDD, setPHR, jnjLaB2BUnitModelKeyAccountExist);

			//Getting the partner Function Id List from the SalesOrg
			final List<LaPartnerFunction> partnerFunctionModelList = jnJSapAccountModel.getLaPartnerFunction();
			//Iterating Through the  partner Function Type For Sold To
			for (final PartnerFunction partner : partnerFunctionModelList)
			{
				//If the type IS "Sold To"
				if (partner.getPartnerFunctionType() != null)
				{
					if (partner.getPartnerFunctionType().equals(Jnjb2bCoreConstants.UpsertCustomer.PARTNERFUNCTION_SOLDTO))
					{
						final JnJB2BUnitModel jnJB2BUnitSoldTo = (JnJB2BUnitModel) jnjGTB2BUnitService
								.getUnitForUid(partner.getPartnerFunctionID());

						//Get the incoming B2B Unit  for that Key Account
						//Checking  whether the  B2B Unit  for that Key Account is there or not
						if (jnJB2BUnitSoldTo instanceof JnJLaB2BUnitModel)
						{
							final JnJLaB2BUnitModel jnJLaB2BUnitSoldTo = (JnJLaB2BUnitModel) jnJB2BUnitSoldTo;
							//Checking the exiting child set contains the new Sold To
							if (!principalGroupModelSet.contains(jnJLaB2BUnitSoldTo))
							{
								//Make the  new Incoming Sold  as a child of Key Account
								soldToParentSet.add(jnjLaB2BUnitModelKeyAccountExist);
								jnJB2BUnitSoldTo.setGroups(soldToParentSet);
								newMemberSet.add(jnJLaB2BUnitSoldTo);
								getJnJCustomerDataService().saveItemModel(jnJLaB2BUnitSoldTo);
							}
						}
					}
				}
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return newJnJSalesOrgCustomerModelList;
	}

	/**
	 * Realign old sold to.
	 *
	 * @param newMemberSet
	 *           the Set<PrincipalGroupModel>
	 * @param jnJB2BUnitModelKeyAccountExist
	 *           the JnJB2BUnitModel
	 */
	private void realignOldSoldTo(final JnJCustomerDTO customerDTO, final Set<PrincipalGroupModel> newMemberSet,
			final JnJB2BUnitModel jnJB2BUnitModelKeyAccountExist)
	{
		//Get  now the  all the members of the Key Account
		final String methodName = "realignOldSoldTo";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			final Set<PrincipalModel> allMembers = jnJB2BUnitModelKeyAccountExist.getMembers();
			for (final PrincipalModel set : allMembers)
			{
				if (!newMemberSet.contains(set))
				{
					final Set<PrincipalGroupModel> newParentset = new HashSet<>();
					JnJCompanyModel jnJCompanyModel = null;
					if (JnJCommonUtil.checkValidCountry(customerDTO.getCountry()))
					{
						jnJCompanyModel = jnjCompanyService.getMasterCompanyForUid(
								Jnjb2bCoreConstants.UpsertCustomer.MASTER_B2BUNIT_INITIAL + customerDTO.getCountry());
					}
					newParentset.add(jnJCompanyModel);
					set.setGroups(newParentset);
					getJnJCustomerDataService().saveItemModel(set);
				}
			}
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);

		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, "exception occured while try to save the data into data base" + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
	}

	/**
	 * Change customer eligibility method to add the restricted Category model in the B2b unit model.
	 *
	 * @param setMDD
	 *           the Set<String>
	 * @param setPHR
	 *           the Set<String>
	 * @param jnJB2BUnitModel
	 *           the JnJB2BUnitModel
	 */
	private void changeCustomerEligibility(final Set<String> setMDD, final Set<String> setPHR,
			final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "changeCustomerEligibility";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		// To retrieve the restricted categories set.
		final String pharmaCategory = Config.getParameter(Jnjb2bCoreConstants.PHARMA_CATEGORY_CODE);
		final String mddCategory = Config.getParameter(Jnjb2bCoreConstants.MDD_CATEGORY_CODE);
		final Set<CategoryModel> updatedRestrictedCategories = new HashSet<>();
		final Set<CategoryModel> restrictedCategories = jnJB2BUnitModel.getRestrictedCategories();
		if (null != restrictedCategories && !restrictedCategories.isEmpty())
		{
			updatedRestrictedCategories.addAll(restrictedCategories);
		}

		final CategoryModel mddCategoryModel = customerEligibilityService.getCategoryByCode(mddCategory,
				(jnJB2BUnitModel.getCountry() == null) ? null : jnJB2BUnitModel.getCountry().getIsocode());
		final CategoryModel phrCategoryModel = customerEligibilityService.getCategoryByCode(pharmaCategory,
				(jnJB2BUnitModel.getCountry() == null) ? null : jnJB2BUnitModel.getCountry().getIsocode());
		// check the restrictedCategories value in jnj b2b unit model if it exist
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
				" : Size of MDD :" + setMDD.size() + "and PHR:" + setPHR.size(), JnjLatamRSACustomerDataLoadMapper.class);
		if (setMDD.isEmpty())
		{
			// if the restricted categories in the Jnj B2B unit model doesn't contain the MDD category model then add it.
			if ((null != restrictedCategories && !restrictedCategories.contains(mddCategoryModel)) || null == restrictedCategories)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, " : Adding the restriction for MDD :",
						JnjLatamRSACustomerDataLoadMapper.class);
				updatedRestrictedCategories.add(mddCategoryModel);
			}
		}
		else
		{
			// if the restricted categories in the Jnj B2B unit model contains the MDD category model then remove it.
			if (null != restrictedCategories && restrictedCategories.contains(mddCategoryModel))
			{
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, " : Removing the restriction for MDD :",
						JnjLatamRSACustomerDataLoadMapper.class);
				updatedRestrictedCategories.remove(mddCategoryModel);
			}
		}
		// check for the PHR set size equal to zero.
		if (setPHR.isEmpty())
		{
			// if the restricted categories in the Jnj B2B unit model doesn't contain the PHR category model then add it.
			if ((null != restrictedCategories && !restrictedCategories.contains(phrCategoryModel)) || null == restrictedCategories)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, " : Adding the restriction for PHR :",
						JnjLatamRSACustomerDataLoadMapper.class);
				updatedRestrictedCategories.add(phrCategoryModel);
			}
		}
		else
		{
			// if the restricted categories in the Jnj B2B unit model contains the PHR category model then remove it.
			if (null != restrictedCategories && restrictedCategories.contains(phrCategoryModel))
			{
				JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, " : Removing the restriction for PHR :",
						JnjLatamRSACustomerDataLoadMapper.class);
				updatedRestrictedCategories.remove(phrCategoryModel);
			}
		}
		jnJB2BUnitModel.setRestrictedCategories(updatedRestrictedCategories);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
	}

	/**
	 * This Method is used for map the data for Ship To and it will do further processing for ship To. It will update
	 * address model if exist other wise it will throw an exception
	 *
	 *
	 */
	@Override
	public Map<Boolean, String> mapCustomerDataForShipTo(final JnJLaCustomerDTO customerDTO)
	{
		final String methodName = "mapCustomerDataForShipTo()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		String errorMessage = null;
		Boolean recordStatus = Boolean.FALSE;

		JnjGTCoreUtil.logInfoMessage(Logging.UPSERT_CUSTOMER_NAME, methodName,
				"ShipTo Account type :: " + customerDTO.getAccountType(), JnjLatamRSACustomerDataLoadMapper.class);

		try
		{
			final List<AddressModel> addressModelList = jnjGTAddressService
					.getAddressByIdandOnwerType(customerDTO.getCustomerNumer());

			if (!addressModelList.isEmpty())
			{
				for (final AddressModel newAddressModel : addressModelList)
				{
					newAddressModel.setJnJAddressId(customerDTO.getCustomerNumer());
					newAddressModel.setTown(customerDTO.getCity());
					newAddressModel.setPostalcode(customerDTO.getPostalCode());
					newAddressModel.setDistrict(customerDTO.getDistrict());
					newAddressModel.setStreetname(customerDTO.getStreet());
					newAddressModel.setPhone1(customerDTO.getTelephone());
					final CountryModel countryModel = commonI18NService.getCountry(customerDTO.getCountry());
					newAddressModel.setCountry(countryModel);
					newAddressModel.setRegion(commonI18NService.getRegion(countryModel, customerDTO.getRegion()));
					recordStatus = jnJCustomerDataService.saveItemModel(newAddressModel);
				}
			}
			else
			{
				errorMessage = "The Ship-to " + customerDTO.getCustomerNumer() + " is an orphan ship-to.";
				throw new SystemException(errorMessage);
			}
			if (Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_B.equalsIgnoreCase(customerDTO.getBothIndicator()))
			{
				mapCustomerDataforIndirectCustomer(customerDTO);
			}
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = Boolean.FALSE;
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, "exception occurred while try to save the data into data base" + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = throwable.getMessage();
			recordStatus = Boolean.FALSE;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		errorFlagAndMessage.put(recordStatus, errorMessage);

		return errorFlagAndMessage;
	}


	/**
	 * This Method is used for map the data for Ship To and it will do further processing for ship To. It will update
	 * address model if exist other wise it will throw an exception
	 */
	@Override
	public Map<Boolean, String> mapCustomerDataforIndirectCustomer(final JnJLaCustomerDTO jnjCustomerDTO)
	{

		final String methodName = "mapCustomerDataforIndirectCustomer()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		final Map<Boolean, String> errorFlagAndMessage = new HashMap<>();
		String errorMessage = null;
		Boolean recordStatus = Boolean.FALSE;

		JnjGTCoreUtil.logInfoMessage(
				Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, "IndirectCustomer Account type :: "
						+ jnjCustomerDTO.getAccountType() + " :: Customer No :: " + jnjCustomerDTO.getCustomerNumer(),
				JnjLatamRSACustomerDataLoadMapper.class);
		try
		{
			boolean check = false;
			boolean check1 = false;
			if (null != jnjCustomerDTO.getBothIndicator())
			{
				if ((jnjCustomerDTO.getBothIndicator()).equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_D))
				{
					check = true;
				}
				if ((jnjCustomerDTO.getBothIndicator()).equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_A)
						|| (jnjCustomerDTO.getBothIndicator()).equalsIgnoreCase(Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_B))
				{
					check1 = true;
				}
			}

			if (null != jnjCustomerDTO.getBothIndicator())
			{
				if (((jnjCustomerDTO.getAccountType()
						.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_SOLDTO).split(",")[0]))
						&& Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_A.equalsIgnoreCase(jnjCustomerDTO.getBothIndicator()))
						|| ((jnjCustomerDTO.getAccountType()
								.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_INDIRECT_CUSTOMER)))
								&& (Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_C
										.equalsIgnoreCase(jnjCustomerDTO.getBothIndicator())
										|| Jnjlab2bcoreConstants.UpsertCustomer.BOTH_INDICATOR_D
												.equalsIgnoreCase(jnjCustomerDTO.getBothIndicator()))))
				{
					mapCustomerDataforIndirectPayer(jnjCustomerDTO);
				}
			}

			if (((jnjCustomerDTO.getAccountType()
					.equalsIgnoreCase(Config.getParameter(Jnjlab2bcoreConstants.UpsertCustomer.ACCOUNT_TYPE_INDIRECT_CUSTOMER)))
					&& (((null == jnjCustomerDTO.getBothIndicator())) || check))
					|| (((jnjCustomerDTO.getAccountType()
							.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_SOLDTO).split(",")[0]))
							&& (((null == jnjCustomerDTO.getBothIndicator())) || check1))))
			{

				final CountryModel countryModel = commonI18NService.getCountry(jnjCustomerDTO.getCountry());
				JnjIndirectCustomerModel jnJIndirectCustomerModel = getJnJCustomerDataService()
						.getJnjInidrectCustomerByIDCountry(jnjCustomerDTO.getCustomerNumer(), countryModel.getPk().toString());


				if (null == jnJIndirectCustomerModel)
				{
					jnJIndirectCustomerModel = modelService.create(JnjIndirectCustomerModel.class);
					jnJIndirectCustomerModel.setIndirectCustomer(jnjCustomerDTO.getCustomerNumer());
					jnJIndirectCustomerModel.setCountry(countryModel);
					jnJIndirectCustomerModel
							.setIndirectCustomerName(getB2bUnitName(jnjCustomerDTO.getName1(), jnjCustomerDTO.getName2()));


					recordStatus = jnJCustomerDataService.saveItemModel(jnJIndirectCustomerModel);
				}
				else
				{
					// Case when indirect customer is already exist for given combination of indirect customer number and country model so updating the status to true
					errorMessage = "indirect customer is already exist for given combination of indirect customer and country model";
					JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, errorMessage,
							JnjLatamRSACustomerDataLoadMapper.class);

					jnJIndirectCustomerModel
							.setIndirectCustomerName(getB2bUnitName(jnjCustomerDTO.getName1(), jnjCustomerDTO.getName2()));
					recordStatus = jnJCustomerDataService.saveItemModel(jnJIndirectCustomerModel);
				}
			}
		}

		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, UNKNOWN_IDENTIFIER_EXCEPTION_OCCURED + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getLocalizedMessage();
			recordStatus = false;
		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_FOUND_FOR_GIVEN_KEY + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = false;
		}
		catch (final NullPointerException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, DATA_IS_NOT_AVAILABLE_FOR_GIVEN_CODE + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = false;
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = exception.getMessage();
			recordStatus = false;
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, "exception occured while try to save the data into data base" + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			errorMessage = throwable.getMessage();
			recordStatus = false;
		}


		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		errorFlagAndMessage.put(recordStatus, errorMessage);

		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return errorFlagAndMessage;
	}


	@Override
	public boolean mapCustomerDataforIndirectPayer(final JnJLaCustomerDTO jnjCustomerDTO)
	{
		final String methodName = "mapCustomerDataforIndirectPayer()";
		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);
		String errorMessage;
		boolean recordStatus;

		JnjGTCoreUtil
				.logInfoMessage(
						Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, "IndirectPayer Account type :: "
								+ jnjCustomerDTO.getAccountType() + " :: Payer No :: " + jnjCustomerDTO.getCustomerNumer(),
						JnjLatamRSACustomerDataLoadMapper.class);
		try
		{

			final CountryModel countryModel = commonI18NService.getCountry(jnjCustomerDTO.getCountry());
			JnjIndirectPayerModel jnJIndirectPayerModel = getJnJCustomerDataService()
					.getJnjIndirectPayerByIDCountry(jnjCustomerDTO.getCustomerNumer(), countryModel.getPk().toString());
			if (null == jnJIndirectPayerModel)
			{
				jnJIndirectPayerModel = modelService.create(JnjIndirectPayerModel.class);
				jnJIndirectPayerModel.setIndirectPayer(jnjCustomerDTO.getCustomerNumer());
				jnJIndirectPayerModel.setCountry(countryModel);
				jnJIndirectPayerModel.setIndirectPayerName(getB2bUnitName(jnjCustomerDTO.getName1(), jnjCustomerDTO.getName2()));
				recordStatus = jnJCustomerDataService.saveItemModel(jnJIndirectPayerModel);
			}
			else
			{
				// Case when indirect customer is already exist for given combination of indirect customer number and country model so updating the status to true
				errorMessage = "indirect payer is already exist for given combination of indirect payer and country model";
				JnjGTCoreUtil.logWarnMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, errorMessage,
						JnjLatamRSACustomerDataLoadMapper.class);

				jnJIndirectPayerModel.setIndirectPayerName(getB2bUnitName(jnjCustomerDTO.getName1(), jnjCustomerDTO.getName2()));
				recordStatus = jnJCustomerDataService.saveItemModel(jnJIndirectPayerModel);
			}
		}

		catch (final UnknownIdentifierException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, UNKNOWN_IDENTIFIER_EXCEPTION_OCCURED + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			recordStatus = false;
		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_FOUND_FOR_GIVEN_KEY + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			recordStatus = false;
		}
		catch (final NullPointerException exception)
		{
			JnjGTCoreUtil.logErrorMessage(
					Logging.UPSERT_CUSTOMER_NAME + " - ", methodName, DATA_IS_NOT_AVAILABLE_FOR_GIVEN_CODE + exception
							+ exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			recordStatus = false;
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ", methodName,
					MODEL_NOT_SAVED_INTO_DATA_BASE + exception + exception.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			recordStatus = false;
		}
		catch (final Exception throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + " - ",
					methodName, "exception occured while try to save the data into data base" + throwable + "-"
							+ throwable.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime(),
					JnjLatamRSACustomerDataLoadMapper.class);
			recordStatus = false;
		}


		JnjGTCoreUtil.logDebugMessage(Logging.UPSERT_CUSTOMER_NAME, methodName, Logging.END_OF_METHOD,
				JnjLatamRSACustomerDataLoadMapper.class);

		return recordStatus;

	}


	private JnJLaCustomerDataService getJnJCustomerDataService()
	{
		return jnJCustomerDataService;
	}


	public void setJnJCustomerDataService(final JnJLaCustomerDataService jnJCustomerDataService)
	{
		this.jnJCustomerDataService = jnJCustomerDataService;
	}


	public ModelService getModelService()
	{
		return modelService;
	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	public UserService getUserService()
	{
		return userService;
	}


	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}


	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}


	public B2BCommerceBudgetService getB2BCommerceBudgetService()
	{
		return b2BCommerceBudgetService;
	}


	public void setB2BCommerceBudgetService(final B2BCommerceBudgetService b2bCommerceBudgetService)
	{
		b2BCommerceBudgetService = b2bCommerceBudgetService;
	}


	/**
	 * This method updates the Master Company Country
	 *
	 * @param customerCountryIso
	 *           customer country iso code
	 * @param updatedGroups
	 *           set of principal group model which extends a principal model
	 * @return updatedGroups updated according customer country
	 */
	public void updateMasterCompanyCountry(final String customerCountryIso, final Set<PrincipalGroupModel> updatedGroups)
	{
		final JnJCompanyModel jnjMasterCompanyModel;
		final String methodName = "updateMasterCompanyCountry";

		try
		{
			if (JnJCommonUtil.checkValidCountry(customerCountryIso))
			{
				jnjMasterCompanyModel = jnjCompanyService.getMasterCompanyForUid(
						Jnjb2bCoreConstants.UpsertCustomer.MASTER_B2BUNIT_INITIAL + getIdByCountry(customerCountryIso).toUpperCase());
				LOG.info("jnjMasterCompanyModel: "+jnjMasterCompanyModel.getUid());
				if (jnjMasterCompanyModel != null && !updatedGroups.contains(jnjMasterCompanyModel))
				{
					updatedGroups.add(jnjMasterCompanyModel);
				}

				for (final JnjCountryEnum jnjCountryEnum : JnjCountryEnum.values())
				{
					handleMasterCompanyModelByCountryIso(customerCountryIso, jnjCountryEnum, updatedGroups);
				}
			}
		}
		catch (final BusinessException businessException)
		{

			JnjGTCoreUtil.logErrorMessage(Logging.UPSERT_CUSTOMER_NAME + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "Exception Occurred while Adding/Updating Master Company" + businessException,
					JnjLatamRSACustomerDataLoadMapper.class);
		}
	}

	/**
	 * This method handles master company model by each country iso code defined in JnjCountryEnum
	 *
	 * @param customerCountryIso
	 *           customer country iso code
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @param updatedGroups
	 *           set of principal group model
	 * @throws BusinessException
	 */
	private void handleMasterCompanyModelByCountryIso(final String customerCountryIso, final JnjCountryEnum jnjCountryEnum,
			final Set<PrincipalGroupModel> updatedGroups) throws BusinessException
	{
		if (countryIsoCodeContainsCustomerCountryIso(customerCountryIso, jnjCountryEnum))
		{
			final JnJCompanyModel jnjCompanyModel = jnjCompanyService.getMasterCompanyForUid(
					Jnjb2bCoreConstants.UpsertCustomer.MASTER_B2BUNIT_INITIAL + jnjCountryEnum.getCountryIso());
			LOG.info("jnjCompanyModel value#############: "+jnjCompanyModel.getUid());
			updatedGroups.add(jnjCompanyModel);
		}
	}

	/**
	 * This method checks if the country has a list of valid countries in jnj.valid.{isoCode}.countries.isoCode and if
	 * the customer country iso code is valid for this list
	 *
	 * @param customerCountryIso
	 *           country iso codes enum
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @return Boolean regarding if the customer country has a list of valid countries which it belongs
	 */
	private Boolean countryIsoCodeContainsCustomerCountryIso(final String customerCountryIso, final JnjCountryEnum jnjCountryEnum)
	{
		LOG.info("customerCountryIso value: "+customerCountryIso+" ISO code parameter: "+jnjCountryEnum.getIsoCodeParameter());
		Boolean countryContainsCustomerCountryIso = Boolean.FALSE;
		if (StringUtils.isNotBlank(jnjCountryEnum.getIsoCodeParameter()))
		{
			final String isoCodeParameter = Config.getParameter(jnjCountryEnum.getIsoCodeParameter());
			LOG.info("isoCodeParameter values form config: "+isoCodeParameter);
			final List<String> validCountryIsoCodes = Arrays.asList(isoCodeParameter.split(Jnjb2bCoreConstants.CONST_COMMA));
			countryContainsCustomerCountryIso = validCountryIsoCodes.contains(customerCountryIso);
			LOG.info("countryContainsCustomerCountryIso: "+countryContainsCustomerCountryIso);
			
		}
		return countryContainsCustomerCountryIso;
	}

	/**
	 * Check if it is a PHR sector
	 *
	 * @param sector
	 * @return Boolean
	 */
	private Boolean isPharmaSector(final String sector)
	{
		return StringUtils.equalsIgnoreCase(sector.trim(), Jnjb2bCoreConstants.PHR_SECTOR);
	}

	/**
	 * Check if it is a MD sector
	 *
	 * @param sector
	 * @return Boolean
	 */
	private Boolean isMedicalSector(final String sector)
	{
		return StringUtils.equalsIgnoreCase(sector.trim(), Jnjb2bCoreConstants.MDD_SECTOR);
	}

	/**
	 * Check if a sector by country is disabled
	 *
	 * @param countryMap
	 *           map of countries by sector
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @return Boolean
	 */
	private Boolean isSectorNotEnabled(final Map<String, JnjCompanyModelWrapper> countryMap, final JnjCountryEnum jnjCountryEnum)
	{
		return countryMap.containsKey(jnjCountryEnum.getCountryIso())
				&& !countryMap.get(jnjCountryEnum.getCountryIso()).getSectorEnabled();
	}

	/**
	 * This method populates a map of countries by sector
	 *
	 * @param sectorCountryMap
	 *           map of countries by sector
	 * @param sector
	 *           upsert customer sector
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @throws BusinessException
	 */
	private void populateCountryMap(final Map<String, JnjCompanyModelWrapper> sectorCountryMap, final String sector,
			final JnjCountryEnum jnjCountryEnum) throws BusinessException
	{
		final JnJCompanyModel jnjCompanyModel = jnjCompanyService
				.getMasterCompanyForUid(sector + jnjCountryEnum.getCountryIso() + Jnjlab2bcoreConstants.UpsertCustomer.GROUP);
		LOG.info("Group id##############: "+sector + jnjCountryEnum.getCountryIso() + Jnjlab2bcoreConstants.UpsertCustomer.GROUP);
		LOG.info("jnjCompanyModel values:"+jnjCompanyModel.getUid());
		sectorCountryMap.put(jnjCountryEnum.getCountryIso(), new JnjCompanyModelWrapper(jnjCompanyModel));
	}

	/**
	 * This method checks if the country doesn't have a list of valid countries in jnj.valid.{isoCode}.countries.isoCode
	 * and if the customer country iso code is valid for this list
	 *
	 * @param customerCountryIso
	 *           customer country iso code
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @return Boolean
	 */
	private Boolean isCountryNotValidForOtherCountry(final String customerCountryIso, final JnjCountryEnum jnjCountryEnum)
	{
		return StringUtils.isBlank(jnjCountryEnum.getIsoCodeParameter())
				&& StringUtils.equalsIgnoreCase(customerCountryIso, jnjCountryEnum.getCountryIso());
	}

	/**
	 * Check if a country has a list of valid countries in jnj.valid.{isoCode}.countries.isoCode or doesn't
	 *
	 * @param customerCountryIso
	 *           customer country iso code
	 * @param jnjCountryEnum
	 *           country iso codes enum
	 * @return Boolean
	 */
	private Boolean isCountryValid(final String customerCountryIso, final JnjCountryEnum jnjCountryEnum)
	{
		return isCountryNotValidForOtherCountry(customerCountryIso, jnjCountryEnum)
				|| countryIsoCodeContainsCustomerCountryIso(customerCountryIso, jnjCountryEnum);
	}

	/**
	 * Update the user groups with the master sector companies
	 *
	 * @param sectorCountryMap
	 *           map of countries by sector
	 * @param updatedGroups
	 *           the updated groups
	 */
	private void updateMasterSectorCompaniesForAllCountries(final Map<String, JnjCompanyModelWrapper> sectorCountryMap,
			final Set<PrincipalGroupModel> updatedGroups)
	{
		for (final Map.Entry<String, JnjCompanyModelWrapper> mapLine : sectorCountryMap.entrySet())
		{
			final Boolean sectorEnabled = mapLine.getValue().getSectorEnabled();
			final JnJCompanyModel jnjCompanyModel = mapLine.getValue().getJnjCompanyModel();
			updateMasterSectorCompanies(sectorEnabled, updatedGroups, jnjCompanyModel);
		}
	}

	/**
	 * This method populates both pharma and medical maps according to each sector by country
	 *
	 * @param pharmaCountryMap
	 *           map of PHR countries
	 * @param medicalCountryMap
	 *           map of MD countries
	 * @param jnjSalesOrgCustomerModel
	 * @param customerCountryIso
	 *           customer country iso code
	 */
	private void handleSectorEnabled(final Map<String, JnjCompanyModelWrapper> pharmaCountryMap,
			final Map<String, JnjCompanyModelWrapper> medicalCountryMap, final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel,
			final String customerCountryIso)
	{
		for (final JnjCountryEnum jnjCountryEnum : JnjCountryEnum.values())
		{
			if (isPharmaSector(jnjSalesOrgCustomerModel.getSector()) && isSectorNotEnabled(pharmaCountryMap, jnjCountryEnum)
					&& isCountryValid(customerCountryIso, jnjCountryEnum))
			{
				pharmaCountryMap.get(jnjCountryEnum.getCountryIso()).setSectorEnabled(Boolean.TRUE);
				LOG.info("pharmaCountryMap#############"+pharmaCountryMap.get(jnjCountryEnum.getCountryIso()));
			}

			if (isMedicalSector(jnjSalesOrgCustomerModel.getSector()) && isSectorNotEnabled(medicalCountryMap, jnjCountryEnum)
					&& isCountryValid(customerCountryIso, jnjCountryEnum))
			{
				medicalCountryMap.get(jnjCountryEnum.getCountryIso()).setSectorEnabled(Boolean.TRUE);
				LOG.info("medicalCountryMap#############"+pharmaCountryMap.get(jnjCountryEnum.getCountryIso()));
			}
		}
	}
}
