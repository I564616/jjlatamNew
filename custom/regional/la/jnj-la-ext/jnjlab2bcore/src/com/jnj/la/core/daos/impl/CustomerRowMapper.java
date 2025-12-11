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
package com.jnj.la.core.daos.impl;

import de.hybris.platform.util.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnJLaCustomerDTO;
import com.jnj.la.core.dto.JnJLaSalesOrgDTO;
import com.jnj.la.core.dto.LaPartnerFunction;


public class CustomerRowMapper implements RowMapper<List<JnJLaCustomerDTO>>
{

	private static final Logger LOGGER = Logger.getLogger(CustomerRowMapper.class);


	@Override
	public List<JnJLaCustomerDTO> mapRow(final ResultSet customerRecords, final int rowNum) throws SQLException
	{
		final String customerNumber = null;
		JnJLaCustomerDTO customerDTO;

		final List<JnJLaCustomerDTO> listOfCustomers = new ArrayList<JnJLaCustomerDTO>();

		final Map<String, JnJLaCustomerDTO> mapCustomerNoAndCustomerDTO = new HashMap<String, JnJLaCustomerDTO>();

		final Map<String, List<JnJLaSalesOrgDTO>> mapCustomerNoAndSalesOrgDTO = new HashMap<String, List<JnJLaSalesOrgDTO>>();

		final Map<String, List<LaPartnerFunction>> mapCustomerNoAndPartnerFunction = new HashMap<String, List<LaPartnerFunction>>();

		try
		{

			if (customerRecords != null)
			{
				do
				{
					if (customerNumber == null || !customerNumber.equals(customerRecords.getString("CUSTOMER_NUM")))
					{
						final JnJLaSalesOrgDTO salesOrg = new JnJLaSalesOrgDTO();

						salesOrg.setSalesOrganization(customerRecords.getString("SALES_ORG"));
						salesOrg.setDivision(customerRecords.getString("DIVISION"));
						salesOrg.setDistributionChannel(customerRecords.getString("DIST_CHANNEL"));
						salesOrg.setCustomerNumber(customerRecords.getString("CUSTOMER_NUM"));
						salesOrg.setProductAttribute1(customerRecords.getString("MDD_PROD_ATTR1"));
						salesOrg.setProductAttribute2(customerRecords.getString("PHR_PROD_ATTR2"));
						salesOrg.setProductAttribute3(customerRecords.getString("CON_PROD_ATTR3"));
						salesOrg.setPartialDelivery(customerRecords.getString("PARTIAL_DELIVERY"));
						if (!mapCustomerNoAndSalesOrgDTO.containsKey(salesOrg.getCustomerNumber()))
						{
							final List<JnJLaSalesOrgDTO> salesOrgList = new ArrayList<JnJLaSalesOrgDTO>();
							salesOrgList.add(salesOrg);
							mapCustomerNoAndSalesOrgDTO.put(salesOrg.getCustomerNumber(), salesOrgList);
						}
						else
						{
							final List<JnJLaSalesOrgDTO> salesOrgList = mapCustomerNoAndSalesOrgDTO.get(salesOrg.getCustomerNumber());
							salesOrgList.add(salesOrg);
							mapCustomerNoAndSalesOrgDTO.put(salesOrg.getCustomerNumber(), salesOrgList);
						}

						final LaPartnerFunction partnerFunction = new LaPartnerFunction();

						partnerFunction.setCustomerNumber(customerRecords.getString("CUSTOMER_NUM"));
						partnerFunction.setPartnerFunctionID(customerRecords.getString("PARTNER_FUNC_CD"));
						partnerFunction.setPartnerFunctionType(customerRecords.getString("PARTNER_FUNC_TYPE"));

						if (!mapCustomerNoAndPartnerFunction.containsKey(partnerFunction.getCustomerNumber()))
						{
							final List<LaPartnerFunction> partnerFunctionList = new ArrayList<LaPartnerFunction>();
							partnerFunctionList.add(partnerFunction);
							mapCustomerNoAndPartnerFunction.put(salesOrg.getCustomerNumber(), partnerFunctionList);
						}
						else
						{
							final List<LaPartnerFunction> partnerFunctionList = mapCustomerNoAndPartnerFunction
									.get(partnerFunction.getCustomerNumber());
							partnerFunctionList.add(partnerFunction);
							mapCustomerNoAndPartnerFunction.put(salesOrg.getCustomerNumber(), partnerFunctionList);
						}

						final JnJLaCustomerDTO customer = new JnJLaCustomerDTO();

						customer.setCustomerNumer(customerRecords.getString("CUSTOMER_NUM"));
						customer.setName1(customerRecords.getString("NAME_1"));
						customer.setName2(customerRecords.getString("NAME_2"));

						customer.setStreet(customerRecords.getString("ADDRESS_LINE_1"));
						customer.setCity(customerRecords.getString("CITY"));
						customer.setDistrict(customerRecords.getString("COUNTY"));
						customer.setCountry(customerRecords.getString("COUNTRY_CD"));
						customer.setRegion(customerRecords.getString("REGION_STATE_CD"));
						customer.setPostalCode(customerRecords.getString("POSTAL_CODE"));

						customer.setAccountType(customerRecords.getString("ACCOUNT_TYPE"));
						customer.setTelephone(customerRecords.getString("TELEPHONE"));

						customer.setCnpj(customerRecords.getString("TAXID"));
						customer.setIndustryCode1(customerRecords.getString("INDUSTRY_CODE_1"));
						customer.setCustomerFreightType(customerRecords.getString("CUSTOMER_FREIGHT_TYPE"));
						customer.setIndustryCode2(customerRecords.getString("INDUSTRY_CODE_2"));
						customer.setBothIndicator(customerRecords.getString("BOTH_INDICATOR"));
						customer.setLastUpdateDate(customerRecords.getString("LAST_UPDATED_DATE"));

						customer.setCustomerType(Config.getParameter(
								Jnjlab2bcoreConstants.UpsertCustomer.INDUSTRYCODEPREFIX + customerRecords.getString("INDUSTRY_CODE_1")));

						if (!mapCustomerNoAndCustomerDTO.containsKey(customer.getCustomerNumer()))
						{
							mapCustomerNoAndCustomerDTO.put(customer.getCustomerNumer(), customer);
						}
					}
				}
				while (customerRecords.next());

				for (final Map.Entry<String, JnJLaCustomerDTO> entry : mapCustomerNoAndCustomerDTO.entrySet())
				{
					final String customerNo = entry.getKey();
					customerDTO = mapCustomerNoAndCustomerDTO.get(customerNo);

					final List<JnJLaSalesOrgDTO> salesOrgList = mapCustomerNoAndSalesOrgDTO.get(customerNo);
					final List<LaPartnerFunction> partnerFunctionList = mapCustomerNoAndPartnerFunction.get(customerNo);

					LOGGER.info("No of Sales Org found with customer number :: " + customerNo + " is :: " + salesOrgList.size());
					LOGGER.info("No of Partner Function found with customer number :: " + customerNo + " is :: "
							+ partnerFunctionList.size());

					customerDTO.setJnjLaSalesOrgDTOList(salesOrgList);
					customerDTO.setLaPartnerFunction(partnerFunctionList);
					listOfCustomers.add(customerDTO);
				}
				return listOfCustomers;
			}
			else
			{

				return listOfCustomers;
			}

		}
		catch (final RuntimeException e)
		{
			LOGGER.info("Customer Row Mapper Catch Error" + e);
			return listOfCustomers;
		}

	}
}
