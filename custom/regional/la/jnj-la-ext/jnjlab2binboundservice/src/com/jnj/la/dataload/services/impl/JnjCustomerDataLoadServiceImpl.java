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
package com.jnj.la.dataload.services.impl;

import com.jnj.core.jalo.JnjIntegrationRSACronJob;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.dataload.dao.JnjCustomerDataLoadDao;
import com.jnj.la.dataload.services.JnjCustomerDataLoadService;


/**
 *
 */
public class JnjCustomerDataLoadServiceImpl implements JnjCustomerDataLoadService
{

	@Autowired
	private JnjCustomerDataLoadDao jnjCustomerDataLoadDao;

	private static Map<Integer, List<JnJLaCustomerDTO>> listOfAllCustomers = new HashMap<>();

	@Override
	public Map<Integer, List<JnJLaCustomerDTO>> fetchCustomerRecords(final JnjIntegrationRSACronJobModel jobModel)
	{
		final List<JnJLaCustomerDTO> listOfSoldToCustomers = new ArrayList<>();
		final List<JnJLaCustomerDTO> listOfShipToCustomers = new ArrayList<>();
		final List<JnJLaCustomerDTO> listOfKeyAccountCustomers = new ArrayList<>();
		final List<JnJLaCustomerDTO> listOfIndirectCustomers = new ArrayList<>();

		List<String> b2bUnitCodesForSoldToList = new ArrayList<>();
		List<String> b2bUnitCodesForShipToList = new ArrayList<>();

		final List<JnJLaCustomerDTO> listOfAllCustomersFromDao = jnjCustomerDataLoadDao.fetchCustomerRecords(jobModel);

		final String[] b2bUnitCodesForSoldTo = Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_SOLDTO).split(
				",");

		if (b2bUnitCodesForSoldTo.length > 0)
		{
			b2bUnitCodesForSoldToList = Arrays.asList(b2bUnitCodesForSoldTo);
		}
		final String[] b2bUnitCodesForShipTo = Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_SHIPTO).split(
				",");

		if (b2bUnitCodesForShipTo.length > 0)
		{
			b2bUnitCodesForShipToList = Arrays.asList(b2bUnitCodesForShipTo);
		}
		final String b2bUnitCodesForIndirectCustomer = Config
				.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_INDIRECT_CUSTOMER);

		final String b2bUnitCodeForKeyAccounts = Config.getParameter(Jnjb2bCoreConstants.UpsertCustomer.ACCOUNT_TYPE_KEY_ACCOUNT);

		for (final JnJLaCustomerDTO customerDTO : listOfAllCustomersFromDao)
		{
			if (customerDTO.getAccountType() != null)
			{
				if (b2bUnitCodesForSoldToList.contains(customerDTO.getAccountType()))
				{
					listOfSoldToCustomers.add(customerDTO);
				}
				else if (b2bUnitCodesForShipToList.contains(customerDTO.getAccountType()))
				{
					listOfShipToCustomers.add(customerDTO);
				}
				else if (b2bUnitCodesForIndirectCustomer != null
						&& b2bUnitCodesForIndirectCustomer.equalsIgnoreCase(customerDTO.getAccountType()))
				{
					listOfIndirectCustomers.add(customerDTO);
				}
				else if (b2bUnitCodeForKeyAccounts != null
						&& b2bUnitCodeForKeyAccounts.equalsIgnoreCase(customerDTO.getAccountType()))
				{
					listOfKeyAccountCustomers.add(customerDTO);
				}
			}
		}

		listOfAllCustomers.put(0, listOfSoldToCustomers);
		listOfAllCustomers.put(1, listOfKeyAccountCustomers);
		listOfAllCustomers.put(2, listOfShipToCustomers);
		listOfAllCustomers.put(3, listOfIndirectCustomers);

		return listOfAllCustomers;
	}

	/**
	 * @return the listOfAllCustomers
	 */
	public static Map<Integer, List<JnJLaCustomerDTO>> getListOfAllCustomers()
	{
		return listOfAllCustomers;
	}

	/**
	 * @param listOfAllCustomers
	 *           the listOfAllCustomers to set
	 */
	public static void setListOfAllCustomers(final Map<Integer, List<JnJLaCustomerDTO>> listOfAllCustomers)
	{
		JnjCustomerDataLoadServiceImpl.listOfAllCustomers = listOfAllCustomers;
	}

}
