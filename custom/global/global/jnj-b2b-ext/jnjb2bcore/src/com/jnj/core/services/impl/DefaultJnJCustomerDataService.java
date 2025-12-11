/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.commerceservices.customer.PasswordMismatchException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.JnJ.common.logging.MessageLoggerHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnJCustomerDataDao;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.model.OldPasswordModel;
import com.jnj.core.model.SecretQuestionsAndAnswersModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;


/**
 * TODO:<Komal-class level comments are missing>.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnJCustomerDataService implements JnJCustomerDataService
{
	protected static final String CLASS_NAME = DefaultJnJCustomerDataService.class.getName();

	private static final Logger LOGGER = Logger.getLogger(DefaultJnJCustomerDataService.class);



	@Autowired
	ModelService modelService;

	@Autowired
	PasswordEncoderService passwordEncoderService;
	@Autowired
	UserService userService;

	@Autowired
	FlexibleSearchService flexibleSearchService;

	
	
	public ModelService getModelService() {
		return modelService;
	}

	public PasswordEncoderService getPasswordEncoderService() {
		return passwordEncoderService;
	}

	public UserService getUserService() {
		return userService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	private JnJCustomerDataDao jnJCustomerDataDao;

	protected JnJCustomerDataDao getJnJCustomerDataDao()
	{
		return jnJCustomerDataDao;
	}

	public void setJnJCustomerDataDao(final JnJCustomerDataDao jnJCustomerDataDao)
	{
		this.jnJCustomerDataDao = jnJCustomerDataDao;
	}

	/*
	 * This method is used to save Item model
	 */

	@Override
	public boolean saveItemModel(final ItemModel itemModel)
	{
		final String METHOD_NAME = "saveItemModel";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean success = false;
		try
		{
			modelService.saveAll(itemModel);
			modelService.refresh(itemModel);
			success = true;

		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.warn(Logging.UPSERT_CUSTOMER_NAME + "-" + METHOD_NAME + "model is not saved into hybris database" + "-"
					+ modelSavingException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
			success = false;
			throw modelSavingException;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return success;

	}

	/*
	 * This method is used to save Dummy Customer Model
	 */

	@Override
	public boolean saveModel(final JnJB2bCustomerModel jnjB2bCustomerModel)
	{
		final String METHOD_NAME = "saveModel";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean success = false;
		try
		{
			modelService.save(jnjB2bCustomerModel);
			modelService.refresh(jnjB2bCustomerModel);
			success = true;

		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.warn(MessageLoggerHelper
					.buildErrorMessage("userId", "transactionId", "code",
							"customer data is not getting saved" + modelSavingException.getLocalizedMessage() + "---", METHOD_NAME,
							CLASS_NAME));
			success = false;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.UPSERT_CUSTOMER_NAME + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return success;

	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/*
	 * This method is used to create JnJB2bCustomerModel
	 */
	@Override
	public JnJB2bCustomerModel createModel()
	{
		final JnJB2bCustomerModel jnjB2bCustomerModel = modelService.create(JnJB2bCustomerModel.class);
		return jnjB2bCustomerModel;
	}

	/*
	 * This method is used to create AddressModel
	 */
	@Override
	public AddressModel createAddressModel()
	{
		final AddressModel addressModel = modelService.create(AddressModel.class);
		return addressModel;
	}

	/**
	 * 
	 * this method is used to get sales org model for given Jnjb2bUnit model and Sector
	 */

	@Override
	public JnJSalesOrgCustomerModel getJnJSalesOrgCustomerModeBylId(final String jnjb2bUnitModel, final String sector)
	{
		return getJnJCustomerDataDao().getJnJSalesOrgCustomerModeBylId(jnjb2bUnitModel, sector);
	}

	@Override
	public List<SecretQuestionsAndAnswersModel> getSecretQuestionsForUser(final String userId)
	{


		return getJnJCustomerDataDao().getSecretQuestionsForUser(userId);
	}

	@Override
	public List<CountryModel> getCountries()
	{
		return jnJCustomerDataDao.getCountries();
	}

	@Override
	public List<CountryModel> getRegions(final String country)
	{
		return jnJCustomerDataDao.getRegions(country);
	}

	@Override
	public Boolean isPasswordChangeRequired(final String loginId)
	{
		final Calendar comparisionDate = Calendar.getInstance();
		comparisionDate.add(Calendar.DAY_OF_YEAR, -90);
		if (jnJCustomerDataDao.getLastPasswordChangeDate(loginId) != null
				&& jnJCustomerDataDao.getLastPasswordChangeDate(loginId).before(comparisionDate.getTime()))
		{
			return Boolean.TRUE;
		}
		else
		{
			return Boolean.FALSE;
		}
	}

	@Override
	public void changePassword(final JnJB2bCustomerModel customerModel, final String oldPassword, final String newPassword)
			throws PasswordMismatchException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("customerModel", customerModel);
		final OldPasswordModel oldPasswordModel = new OldPasswordModel();
		List<OldPasswordModel> oldPasswordsList = new ArrayList();
		if (userService.isAnonymousUser(customerModel))
		{
			return;
		}
		final String encodedCurrentPassword = passwordEncoderService.encode(customerModel, oldPassword,
				customerModel.getPasswordEncoding());
		if (encodedCurrentPassword.equals(customerModel.getEncodedPassword()))
		{
			final String encodedNewPassword = passwordEncoderService.encode(customerModel, newPassword,
					customerModel.getPasswordEncoding());
			oldPasswordModel.setOldPassword(encodedNewPassword);
			oldPasswordModel.setModifiedDate(Calendar.getInstance().getTime());
			oldPasswordModel.setJnjCustomer(customerModel);
			userService.setPassword(customerModel, newPassword, customerModel.getPasswordEncoding());
			if (customerModel.getOldPasswords() != null)
			{
				if (!customerModel.getOldPasswords().isEmpty())
				{
					oldPasswordsList = getOldPasswordList((List<OldPasswordModel>) customerModel.getOldPasswords());
				}

				oldPasswordsList.add(oldPasswordModel);
				customerModel.setOldPasswords(oldPasswordsList);
				customerModel.setPasswordChangeDate(oldPasswordModel.getModifiedDate());
			}
			modelService.saveAll(customerModel);
			modelService.refresh(customerModel);
		}
		else
		{
			throw new PasswordMismatchException(customerModel.getUid());
		}
	}

	@Override
	public String changePassword(final UserModel userModel, final String newPassword) throws BusinessException
	{
		String returnValue = Boolean.FALSE.toString();
		ServicesUtil.validateParameterNotNullStandardMessage("userModel", userModel);
		final OldPasswordModel oldPasswordModel = new OldPasswordModel();
		List<OldPasswordModel> oldPasswordsList = new ArrayList();
		if (!userService.isAnonymousUser(userModel) && userModel != null && userModel.getUid() != null
				&& !StringUtils.isEmpty(userModel.getUid()))
		{
			if (userModel instanceof JnJB2bCustomerModel)
			{
				final JnJB2bCustomerModel customerModel = (JnJB2bCustomerModel) userService.getUserForUID(userModel.getUid());
				final String encodedNewPassword = passwordEncoderService.encode(customerModel, newPassword,
						customerModel.getPasswordEncoding());
				oldPasswordModel.setOldPassword(encodedNewPassword);
				oldPasswordModel.setModifiedDate(Calendar.getInstance().getTime());
				oldPasswordModel.setJnjCustomer(customerModel);
				userService.setPassword(customerModel, newPassword, customerModel.getPasswordEncoding());
				if (customerModel.getOldPasswords() != null)
				{
					if (!customerModel.getOldPasswords().isEmpty())
					{
						oldPasswordsList = getOldPasswordList((List<OldPasswordModel>) customerModel.getOldPasswords());
					}

					oldPasswordsList.add(oldPasswordModel);
					customerModel.setOldPasswords(oldPasswordsList);
					customerModel.setPasswordChangeDate(oldPasswordModel.getModifiedDate());
				}
				modelService.saveAll(customerModel);
				modelService.refresh(customerModel);
				returnValue = Boolean.TRUE.toString();
			}
		}
		return returnValue;
	}


	List<OldPasswordModel> getOldPasswordList(final List<OldPasswordModel> oldPassList)
	{
		if (oldPassList == null)
		{
			return null;
		}
		else
		{
			final List<OldPasswordModel> returnListOfOldPassword = new ArrayList<OldPasswordModel>();
			returnListOfOldPassword.addAll(oldPassList);
			return returnListOfOldPassword;
		}
	}

	/*
	 * This method is used to get the Jnj sales Org customer model on the basis of customer number
	 */
	@Override
	public List<JnJSalesOrgCustomerModel> getJnJSalesOrgCustomerModeBylId(final String customerNumber)
	{

		return getJnJCustomerDataDao().getJnJSalesOrgCustomerModeBylId(customerNumber);
	}

	/*
	 * this method is used to get the indirect customer on the basis of Country
	 */
	@Override
	public List<JnjIndirectCustomerModel> getJnjInidrectCustomer(final String country)
	{

		return getJnJCustomerDataDao().getIndirectCustomer(country);
	}

	/*
	 * this method is used to get the indirect customer on the basis of Country
	 */
	@Override
	public JnjIndirectCustomerModel getJnjInidrectCustomerByIDCountry(final String customerNumber, final String country)
	{

		return getJnJCustomerDataDao().getIndirectCustomerByIdCountry(customerNumber, country);
	}

}
