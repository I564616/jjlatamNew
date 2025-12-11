/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjRegistrationData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTSuccessfulRegistrationEmailProcessModel;
import com.jnj.core.model.JnjGTSupervisorApprovalEmailProcessModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnjLatamRegistrationData;


/**
 *
 */
public class JnjLatamSuccessfulRegistrationEmailEventListener
		extends AbstractSiteEventListener<JnjLatamSuccessfulRegistrationEmailEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTSuccessfulRegistrationEmailEventListener.class);

	/** Model service **/
	@Autowired
	protected ModelService modelService;


	@Autowired
	protected JnjConfigService jnjConfigService;

	/** Business process services required to create process **/
	@Autowired
	protected BusinessProcessService businessProcessService;


	public ModelService getModelService()
	{
		return modelService;
	}

	public BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	/** Start : Fields for registrationDataMap **/
	protected static final String SECTOR_TYPE = "sectorType";
	protected static final String ACCOUNTS_LIST = "accountsList";
	protected static final String USER_EMAIL_ADDRESS = "email";
	protected static final String USER_FIRST_NAME = "userFirstName";
	protected static final String USER_LAST_NAME = "userLastName";
	protected static final String ACCOUNT_NAME = "accountName";
	protected static final String TYPE_OF_BUSINESS = "typeOfBusiness";
	protected static final String GLN = "gln";
	protected static final String SUBSIDIARY_OF = "subsidiaryOf";
	protected static final String SHIP_TO_ADDRESS = "shipToAddress";
	protected static final String BILL_TO_ADDRESS = "billToAddress";
	protected static final String SALES_AND_USE_TAX_FALG = "salesAndUseTaxFlag";
	protected static final String INITIAL_OPENING_ORDER_AMOUNT = "initialOpeningOrderAmount";
	protected static final String ESTIMATED_AMOUNT_PER_YEAR = "estimatedAmountPerYear";
	protected static final String PRODUCTS_PURCHASING = "productsPurchasing";
	protected static final String SITE_LOGO_PATH = "siteLogoPath";
	protected static final String PHONE_NUMBER = "phoneNumber";
	protected static final String ROLE = "role";
	public static final String SOLD_TO = "SoldTo";
	public static final String CUSTOMER_CODE = "customerCode";
	public static final String COUNTRY = "countryName";
	public static final String CUSTOMERNAME = "customerName";
	public static final String ACCOUNT_MANAGER = "accountManager";
	public static final String TO_EMAIL = "toEmail";
	public static final String DISPLAY_TO_EMAIL = "displaytoEmail";

	/** End : Fields for registrationDataMap **/

	/**
	 * This method is triggered when an object of the event is caught.
	 *
	 * @param JnjGTSuccessfulRegistrationEmailEvent
	 */
	@Override
	protected void onSiteEvent(final JnjLatamSuccessfulRegistrationEmailEvent JnjLatamSuccessfulRegistrationEmailEvent)
	{


		/** Creating the process **/
		final JnjGTSuccessfulRegistrationEmailProcessModel JnjGTSuccessfulRegistrationProcessModel = (JnjGTSuccessfulRegistrationEmailProcessModel) businessProcessService
				.createProcess("jnjGTSuccessfulRegistrationProcess" + "-" + System.currentTimeMillis(),
						"jnjGTSuccessfulRegistrationProcess");

		Map<String, String> jnjCustomerFormMap = null;
		final JnjLatamRegistrationData jnjRegistrationData = JnjLatamSuccessfulRegistrationEmailEvent.getJnjRegistrationData();

		/** Populating the Registration Data Map **/
		if (null != jnjRegistrationData)
		{
			jnjCustomerFormMap = populateRegistrationDataMap(jnjRegistrationData);

			/** Setting the site logo URL **/
			final JnJB2bCustomerModel jnJB2bCustomerModel = (JnJB2bCustomerModel) JnjLatamSuccessfulRegistrationEmailEvent
					.getCustomer();
			jnjCustomerFormMap.put(SITE_LOGO_PATH, JnjLatamSuccessfulRegistrationEmailEvent.getSiteLogoURL());
			jnjCustomerFormMap.put(CUSTOMERNAME, jnJB2bCustomerModel.getCustomerName());
			jnjCustomerFormMap.put(CUSTOMER_CODE, jnJB2bCustomerModel.getCustomerCode());
			jnjCustomerFormMap.put(ACCOUNT_MANAGER, jnjRegistrationData.getCompanyInfoData().getJnjAccountManager());
			if (jnjRegistrationData.getCompanyInfoData().getShipToCountry() != null)
			{

				jnjCustomerFormMap.put(TO_EMAIL, jnjConfigService.getConfigValueById("register.email.to.AR"));
			}

			JnjGTSuccessfulRegistrationProcessModel.setJnjGTRegistrationDetails(jnjCustomerFormMap);

			/** Populating the process model and then starting the process for the successful registration email **/
			populateProcessModel(JnjLatamSuccessfulRegistrationEmailEvent, JnjGTSuccessfulRegistrationProcessModel);

			}

	}

	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTSuccessfulRegistrationProcessModel
	 */
	protected void populateProcessModel(final JnjLatamSuccessfulRegistrationEmailEvent event,
			final JnjGTSuccessfulRegistrationEmailProcessModel JnjGTSuccessfulRegistrationProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";


		JnjGTSuccessfulRegistrationProcessModel.setSite(event.getSite());
		JnjGTSuccessfulRegistrationProcessModel.setCustomer(event.getCustomer());

		JnjGTSuccessfulRegistrationProcessModel.setLanguage(event.getLanguage());
		JnjGTSuccessfulRegistrationProcessModel.setCurrency(event.getCurrency());
		JnjGTSuccessfulRegistrationProcessModel.setStore(event.getBaseStore());
		modelService.save(JnjGTSuccessfulRegistrationProcessModel);
		JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"JnjGTSuccessfulRegistrationProcessModel saved! Now starting the process - jnjGTSuccessfulRegistrationProcess",
				JnjLatamSuccessfulRegistrationEmailEventListener.class);
		businessProcessService.startProcess(JnjGTSuccessfulRegistrationProcessModel);

	}

	/**
	 *
	 * This method populates the process model with essential data from the event
	 *
	 * @param event
	 * @param JnjGTSupervisorApprovalProcessModel
	 */
	protected void populateProcessModel(final JnjLatamSuccessfulRegistrationEmailEvent event,
			final JnjGTSupervisorApprovalEmailProcessModel JnjGTSupervisorApprovalProcessModel)
	{
		JnjGTSupervisorApprovalProcessModel.setSite(event.getSite());
		JnjGTSupervisorApprovalProcessModel.setCustomer(event.getCustomer());

		JnjGTSupervisorApprovalProcessModel.setLanguage(event.getLanguage());
		JnjGTSupervisorApprovalProcessModel.setCurrency(event.getCurrency());
		JnjGTSupervisorApprovalProcessModel.setStore(event.getBaseStore());
		modelService.save(JnjGTSupervisorApprovalProcessModel);
		businessProcessService.startProcess(JnjGTSupervisorApprovalProcessModel);
	}

	/**
	 * This method populates the registration data map
	 *
	 * @param jnjRegistrationData
	 * @return registrationDataMap
	 */
	protected Map<String, String> populateRegistrationDataMap(final JnjLatamRegistrationData jnjRegistrationData)
	{



		final Map<String, String> registrationDataMap = new HashMap();

		/**
		 * Setting the following in the map for the context -
		 *
		 * Sector (MD&D, Consumer) - Business Email Address - First Name - Last Name - Account Name - Global Location
		 * Number - Type of Business - Subsidiary text box - Ship-To Address - Bill-To Address - Account Subject to Sales
		 * and Use Tax in the Ship-To State? - Estimated $ Amount for Initial Opening Order - Estimated $ Amount Per Year
		 * - Products Purchasing
		 *
		 */
		/** Setting Sector info **/
		if (null != jnjRegistrationData.getJnjSectorTypeData())
		{

			if ("jjEmployee".equals(jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile()))
			{
				registrationDataMap.put(ACCOUNTS_LIST, jnjRegistrationData.getJnjSectorTypeData().getGlnOrAccountNumber());
			}
			else
			{
				registrationDataMap.put(ACCOUNTS_LIST, jnjRegistrationData.getJnjSectorTypeData().getAccountNumbers());
			}
			registrationDataMap.put(SECTOR_TYPE, jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile());
		}
		/** Setting user info data **/
		if (null != jnjRegistrationData.getJnjUserInfoData())
		{
			setUserInformation(registrationDataMap, jnjRegistrationData);
		}
		/** Setting company info data **/
		if (null != jnjRegistrationData.getCompanyInfoData())
		{

			setCompanyInformation(registrationDataMap, jnjRegistrationData);
		}

		return registrationDataMap;
	}

	/**
	 * This method sets the user information in the registration Data Map
	 *
	 * @param registrationDataMap
	 * @param jnjRegistrationData
	 */
	protected void setUserInformation(final Map<String, String> registrationDataMap,
			final JnjLatamRegistrationData jnjRegistrationData)
	{

		/** Setting User Information **/
		registrationDataMap.put(USER_EMAIL_ADDRESS, jnjRegistrationData.getJnjUserInfoData().getEmailAddress());
		registrationDataMap.put(USER_FIRST_NAME, jnjRegistrationData.getJnjUserInfoData().getFirstName());
		registrationDataMap.put(USER_LAST_NAME, jnjRegistrationData.getJnjUserInfoData().getLastName());
		registrationDataMap.put(PHONE_NUMBER, jnjRegistrationData.getJnjUserInfoData().getPhone());
		registrationDataMap.put(ROLE, jnjRegistrationData.getJnjUserInfoData().getPermissionLevel());
		registrationDataMap.put(COUNTRY, jnjRegistrationData.getCompanyInfoData().getShipToCountry());

	}

	/**
	 * This method sets the company information in the registration Data Map
	 *
	 * @param registrationDataMap
	 * @param jnjRegistrationData
	 */
	protected void setCompanyInformation(final Map<String, String> registrationDataMap,
			final JnjRegistrationData jnjRegistrationData)
	{


		/** Setting Company Information **/
		registrationDataMap.put(ACCOUNT_NAME, jnjRegistrationData.getCompanyInfoData().getAccountName());

		registrationDataMap.put(GLN, jnjRegistrationData.getCompanyInfoData().getgLN());

		registrationDataMap.put(TYPE_OF_BUSINESS, jnjRegistrationData.getCompanyInfoData().getTypeOfBusiness());

		registrationDataMap.put(SUBSIDIARY_OF, jnjRegistrationData.getCompanyInfoData().getSubsidiaryOf());

		registrationDataMap.put(SHIP_TO_ADDRESS,
				jnjRegistrationData.getCompanyInfoData().getShipToLine1() + jnjRegistrationData.getCompanyInfoData().getShipToLine2()
						+ jnjRegistrationData.getCompanyInfoData().getShipToCity()
						+ jnjRegistrationData.getCompanyInfoData().getShipToState()
						+ jnjRegistrationData.getCompanyInfoData().getShipToCountry()
						+ jnjRegistrationData.getCompanyInfoData().getShipToZipCode());

		registrationDataMap.put(BILL_TO_ADDRESS,
				jnjRegistrationData.getCompanyInfoData().getShipToLine1() + jnjRegistrationData.getCompanyInfoData().getBillToLine2()
						+ jnjRegistrationData.getCompanyInfoData().getBillToCity()
						+ jnjRegistrationData.getCompanyInfoData().getBillToState()
						+ jnjRegistrationData.getCompanyInfoData().getBillToCountry()
						+ jnjRegistrationData.getCompanyInfoData().getBillToZipCode());

		registrationDataMap.put(SALES_AND_USE_TAX_FALG,
				String.valueOf(jnjRegistrationData.getCompanyInfoData().getSalesAndUseTaxFlag()));

		registrationDataMap.put(INITIAL_OPENING_ORDER_AMOUNT,
				jnjRegistrationData.getCompanyInfoData().getInitialOpeningOrderAmount());

		registrationDataMap.put(ESTIMATED_AMOUNT_PER_YEAR, jnjRegistrationData.getCompanyInfoData().getEstimatedAmountPerYear());

		/** Going to create Products purchase string **/
		registrationDataMap.put(PRODUCTS_PURCHASING, buildProductsPurchaseString(jnjRegistrationData));


	}

	/**
	 * This method builds the string for the products purchase.
	 *
	 * @param jnjRegistrationData
	 * @return productsPurchaseString
	 */
	protected String buildProductsPurchaseString(final JnjRegistrationData jnjRegistrationData)
	{
		final String METHOD_NAME = "buildProductsPurchaseString()";

		StringBuilder productsPurchase = null;
		if (null != jnjRegistrationData.getCompanyInfoData()
				&& null != jnjRegistrationData.getCompanyInfoData().getMedicalProductsPurchase())
		{
			productsPurchase = new StringBuilder();

			/** Iterating over the MedicalProductsPurchase List and extracting each product **/
			for (final String product : jnjRegistrationData.getCompanyInfoData().getMedicalProductsPurchase())
			{
				/** appending and creating a comma separated string of products **/
				productsPurchase.append(product);
				productsPurchase.append(Jnjb2bCoreConstants.CONST_COMMA);
			}
			/** removing last comma delimiter **/
			productsPurchase.substring(0, productsPurchase.lastIndexOf(Jnjb2bCoreConstants.CONST_COMMA));

		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
					"Products purchase string not created!", JnjLatamSuccessfulRegistrationEmailEventListener.class);
		}
		return String.valueOf(productsPurchase);
	}

	/**
	 * This method simply enables this listener to handle the event when spotted. Hence default return is true.
	 *
	 * @param JnjGTSuccessfulRegistrationEmailEvent
	 * @return true
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjLatamSuccessfulRegistrationEmailEvent JnjLatamSuccessfulRegistrationEmailEvent)
	{
		return true;
	}


}
