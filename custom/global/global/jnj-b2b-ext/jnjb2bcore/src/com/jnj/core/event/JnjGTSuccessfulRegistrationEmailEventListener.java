/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
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
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjRegistrationData;
import com.jnj.core.model.JnjGTSuccessfulRegistrationEmailProcessModel;
import com.jnj.core.model.JnjGTSupervisorApprovalEmailProcessModel;


/**
 * This class represents the event listener for the successful email registration flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTSuccessfulRegistrationEmailEventListener extends
		AbstractSiteEventListener<JnjGTSuccessfulRegistrationEmailEvent>
{
	protected static final Logger LOG = Logger.getLogger(JnjGTSuccessfulRegistrationEmailEventListener.class);

	/** Model service **/
	@Autowired
	protected ModelService modelService;

	/** Business process services required to create process **/
	@Autowired
	protected BusinessProcessService businessProcessService;
	

	public ModelService getModelService() {
		return modelService;
	}

	public BusinessProcessService getBusinessProcessService() {
		return businessProcessService;
	}

	/** Start : Fields for registrationDataMap **/
	protected static final String SECTOR_TYPE = "sectorType";
	protected static final String ACCOUNTS_LIST = "accountsList";
	protected static final String USER_EMAIL_ADDRESS = "userEmailAddress";
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

	/** End : Fields for registrationDataMap **/

	/**
	 * This method is triggered when an object of the event is caught.
	 * 
	 * @param JnjGTSuccessfulRegistrationEmailEvent
	 */
	@Override
	protected void onSiteEvent(final JnjGTSuccessfulRegistrationEmailEvent JnjGTSuccessfulRegistrationEmailEvent)
	{
		final String METHOD_NAME = "onSiteEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Creating the process **/
		final JnjGTSuccessfulRegistrationEmailProcessModel JnjGTSuccessfulRegistrationProcessModel = (JnjGTSuccessfulRegistrationEmailProcessModel) businessProcessService
				.createProcess("jnjGTSuccessfulRegistrationProcess" + "-" + System.currentTimeMillis(),
						"jnjGTSuccessfulRegistrationProcess");

		Map<String, String> registrationDataMap = null;
		final JnjRegistrationData jnjRegistrationData = JnjGTSuccessfulRegistrationEmailEvent.getJnjRegistrationData();

		/** Populating the Registration Data Map **/
		if (null != jnjRegistrationData)
		{
			registrationDataMap = populateRegistrationDataMap(jnjRegistrationData);

			/** Setting the site logo URL **/
			registrationDataMap.put(SITE_LOGO_PATH, JnjGTSuccessfulRegistrationEmailEvent.getSiteLogoURL());

			JnjGTSuccessfulRegistrationProcessModel.setJnjGTRegistrationDetails(registrationDataMap);

			/** Populating the process model and then starting the process for the successful registration email **/
			populateProcessModel(JnjGTSuccessfulRegistrationEmailEvent, JnjGTSuccessfulRegistrationProcessModel);

			/**
			 * Mail for the approval of the supervisor when a JnJ employee tries to register or a user wants permission to
			 * place orders
			 **/
			if ("jjEmployee".equals(jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile())
					|| "placeOrder".equals(jnjRegistrationData.getJnjUserInfoData().getPermissionLevel()))
			{
				registrationDataMap.put("division", jnjRegistrationData.getJnjSectorTypeData().getDivision());
				registrationDataMap.put("supervisorEmail", jnjRegistrationData.getJnjUserInfoData().getSupervisorEmail());
				final JnjGTSupervisorApprovalEmailProcessModel JnjGTSupervisorApprovalProcessModel = (JnjGTSupervisorApprovalEmailProcessModel) businessProcessService
						.createProcess("jnjGTSupervisorApprovalProcess" + "-" + System.currentTimeMillis(),
								"jnjGTSupervisorApprovalProcess");
				JnjGTSupervisorApprovalProcessModel.setJnjGTRegistrationDetails(registrationDataMap);
				populateProcessModel(JnjGTSuccessfulRegistrationEmailEvent, JnjGTSupervisorApprovalProcessModel);
			}
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * 
	 * This method populates the process model with essential data from the event
	 * 
	 * @param event
	 * @param JnjGTSuccessfulRegistrationProcessModel
	 */
	protected void populateProcessModel(final JnjGTSuccessfulRegistrationEmailEvent event,
			final JnjGTSuccessfulRegistrationEmailProcessModel JnjGTSuccessfulRegistrationProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"Populating the JnjGTSuccessfulRegistrationProcessModel");
		JnjGTSuccessfulRegistrationProcessModel.setSite(event.getSite());
		JnjGTSuccessfulRegistrationProcessModel.setCustomer(event.getCustomer());

		JnjGTSuccessfulRegistrationProcessModel.setLanguage(event.getLanguage());
		JnjGTSuccessfulRegistrationProcessModel.setCurrency(event.getCurrency());
		JnjGTSuccessfulRegistrationProcessModel.setStore(event.getBaseStore());
		modelService.save(JnjGTSuccessfulRegistrationProcessModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"JnjGTSuccessfulRegistrationProcessModel saved! Now starting the process - jnjGTSuccessfulRegistrationProcess");
		businessProcessService.startProcess(JnjGTSuccessfulRegistrationProcessModel);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * 
	 * This method populates the process model with essential data from the event
	 * 
	 * @param event
	 * @param JnjGTSupervisorApprovalProcessModel
	 */
	protected void populateProcessModel(final JnjGTSuccessfulRegistrationEmailEvent event,
			final JnjGTSupervisorApprovalEmailProcessModel JnjGTSupervisorApprovalProcessModel)
	{
		final String METHOD_NAME = "populateProcessModel()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"Populating the JnjGTSupervisorApprovalEmailProcessModel");
		JnjGTSupervisorApprovalProcessModel.setSite(event.getSite());
		JnjGTSupervisorApprovalProcessModel.setCustomer(event.getCustomer());

		JnjGTSupervisorApprovalProcessModel.setLanguage(event.getLanguage());
		JnjGTSupervisorApprovalProcessModel.setCurrency(event.getCurrency());
		JnjGTSupervisorApprovalProcessModel.setStore(event.getBaseStore());
		modelService.save(JnjGTSupervisorApprovalProcessModel);
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME,
				"JnjGTSupervisorApprovalEmailProcessModel saved! Now starting the process - JnjGTSupervisorApprovalProcessModel");
		businessProcessService.startProcess(JnjGTSupervisorApprovalProcessModel);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method populates the registration data map
	 * 
	 * @param jnjRegistrationData
	 * @return registrationDataMap
	 */
	protected Map<String, String> populateRegistrationDataMap(final JnjRegistrationData jnjRegistrationData)
	{
		final String METHOD_NAME = "populateRegistrationDataMap()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);

		final Map<String, String> registrationDataMap = new HashMap<String, String>();

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
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting sector information");
			
			if ("jjEmployee".equals(jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile())) {
				registrationDataMap.put(ACCOUNTS_LIST, jnjRegistrationData.getJnjSectorTypeData().getGlnOrAccountNumber());
			} else {
				registrationDataMap.put(ACCOUNTS_LIST, jnjRegistrationData.getJnjSectorTypeData().getAccountNumbers());
			}
			registrationDataMap.put(SECTOR_TYPE, jnjRegistrationData.getJnjSectorTypeData().getTypeOfProfile());
		}
		/** Setting user info data **/
		if (null != jnjRegistrationData.getJnjUserInfoData())
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting the User Info");
			setUserInformation(registrationDataMap, jnjRegistrationData);
		}
		/** Setting company info data **/
		if (null != jnjRegistrationData.getCompanyInfoData())
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Setting the company info");
			setCompanyInformation(registrationDataMap, jnjRegistrationData);
		}

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return registrationDataMap;
	}

	/**
	 * This method sets the user information in the registration Data Map
	 * 
	 * @param registrationDataMap
	 * @param jnjRegistrationData
	 */
	protected void setUserInformation(final Map<String, String> registrationDataMap, final JnjRegistrationData jnjRegistrationData)
	{
		final String METHOD_NAME = "setUserInformation()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting User Information **/
		registrationDataMap.put(USER_EMAIL_ADDRESS, jnjRegistrationData.getJnjUserInfoData().getEmailAddress());
		registrationDataMap.put(USER_FIRST_NAME, jnjRegistrationData.getJnjUserInfoData().getFirstName());
		registrationDataMap.put(USER_LAST_NAME, jnjRegistrationData.getJnjUserInfoData().getLastName());
		registrationDataMap.put(PHONE_NUMBER, jnjRegistrationData.getJnjUserInfoData().getPhone());
		registrationDataMap.put(ROLE, jnjRegistrationData.getJnjUserInfoData().getPermissionLevel());
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "User Info Set!");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
	}

	/**
	 * This method sets the company information in the registration Data Map
	 * 
	 * @param registrationDataMap
	 * @param jnjRegistrationData
	 */
	protected void setCompanyInformation(final Map<String, String> registrationDataMap, final JnjRegistrationData jnjRegistrationData)
	{
		final String METHOD_NAME = "setCompanyInformation()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		/** Setting Company Information **/
		registrationDataMap.put(ACCOUNT_NAME, jnjRegistrationData.getCompanyInfoData().getAccountName());

		registrationDataMap.put(GLN, jnjRegistrationData.getCompanyInfoData().getgLN());

		registrationDataMap.put(TYPE_OF_BUSINESS, jnjRegistrationData.getCompanyInfoData().getTypeOfBusiness());

		registrationDataMap.put(SUBSIDIARY_OF, jnjRegistrationData.getCompanyInfoData().getSubsidiaryOf());

		registrationDataMap.put(SHIP_TO_ADDRESS, jnjRegistrationData.getCompanyInfoData().getShipToLine1()
				+ jnjRegistrationData.getCompanyInfoData().getShipToLine2()
				+ jnjRegistrationData.getCompanyInfoData().getShipToCity()
				+ jnjRegistrationData.getCompanyInfoData().getShipToState()
				+ jnjRegistrationData.getCompanyInfoData().getShipToCountry()
				+ jnjRegistrationData.getCompanyInfoData().getShipToZipCode());

		registrationDataMap.put(BILL_TO_ADDRESS, jnjRegistrationData.getCompanyInfoData().getShipToLine1()
				+ jnjRegistrationData.getCompanyInfoData().getBillToLine2()
				+ jnjRegistrationData.getCompanyInfoData().getBillToCity()
				+ jnjRegistrationData.getCompanyInfoData().getBillToState()
				+ jnjRegistrationData.getCompanyInfoData().getBillToCountry()
				+ jnjRegistrationData.getCompanyInfoData().getBillToZipCode());

		registrationDataMap.put(SALES_AND_USE_TAX_FALG,
				String.valueOf(jnjRegistrationData.getCompanyInfoData().getSalesAndUseTaxFlag()));

		registrationDataMap.put(INITIAL_OPENING_ORDER_AMOUNT, jnjRegistrationData.getCompanyInfoData()
				.getInitialOpeningOrderAmount());

		registrationDataMap.put(ESTIMATED_AMOUNT_PER_YEAR, jnjRegistrationData.getCompanyInfoData().getEstimatedAmountPerYear());

		/** Going to create Products purchase string **/
		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Going to create Products purchase string!");
		registrationDataMap.put(PRODUCTS_PURCHASING, buildProductsPurchaseString(jnjRegistrationData));

		logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Company Info Set!");

		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
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
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);

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

			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Products purchase string created :: "
					+ productsPurchase);
		}
		else
		{
			logDebugMessage(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, "Products purchase string not created!");
		}
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return String.valueOf(productsPurchase);
	}

	/**
	 * This method simply enables this listener to handle the event when spotted. Hence default return is true.
	 * 
	 * @param JnjGTSuccessfulRegistrationEmailEvent
	 * @return true
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjGTSuccessfulRegistrationEmailEvent JnjGTSuccessfulRegistrationEmailEvent)
	{
		final String METHOD_NAME = "shouldHandleEvent()";
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD);
		//logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.REGISTRATION_EMAIL, METHOD_NAME, Logging.END_OF_METHOD);
		return true;
	}

	/**
	 * Utility method used for logging entry into / exit from any method
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param entryOrExit
	 */
	protected void logMethodStartOrEnd(final String functionalityName, final String methodName, final String entryOrExit)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + entryOrExit + Logging.HYPHEN
					+ System.currentTimeMillis());
		}
	}

	/**
	 * Utility method used for logging custom messages
	 * 
	 * @param functionalityName
	 * @param methodName
	 * @param message
	 */
	protected void logDebugMessage(final String functionalityName, final String methodName, final String message)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(functionalityName + Logging.HYPHEN + methodName + Logging.HYPHEN + message);
		}
	}
}
