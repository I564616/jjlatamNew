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

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjSalesOrgCustDTO;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.la.dataload.services.impl.JnjLASalesOrgCustomerServiceImpl;



/**
 *
 */

@SuppressWarnings("deprecation")
public class JnjLASalesOrgCustomerMapper
{
	@Autowired
	private ModelService modelService;

	@Autowired
	JnjSalesOrgCustService jnJSalesOrgCustService;

	@SuppressWarnings("deprecation")
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	@SuppressWarnings("deprecation")
	public CompanyB2BCommerceService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;


	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	private static final Logger LOG = Logger.getLogger(JnjLASalesOrgCustomerServiceImpl.class);

	protected boolean recordStatus = false;

	public void saveDataToHybris(final List<JnjSalesOrgCustDTO> SalesOrgData, final JnjIntegrationRSACronJobModel cronJobModel)
	{
		final String methodName = "saveDataToHybris()";
		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, methodName, Logging.BEGIN_OF_METHOD,
				JnjLASalesOrgCustomerServiceImpl.class);
		String errorMessage = null;

		for (final JnjSalesOrgCustDTO salesOrg : SalesOrgData)
		{
			try
			{
				final JnJB2BUnitModel b2bUnit = (JnJB2BUnitModel) getCompanyB2BCommerceService()
						.getUnitForUid(salesOrg.getCustomerNum());

				if (b2bUnit != null && b2bUnit instanceof JnJLaB2BUnitModel)
				{
					JnJSalesOrgCustomerModel jnJSalesOrgCustomerModel = jnJSalesOrgCustService
							.getSalesOrgModel(b2bUnit.getPk().toString(), salesOrg.getSector());

					if (jnJSalesOrgCustomerModel == null)
					{
						JnjGTCoreUtil.logWarnMessage(Logging.SALES_ORG_CUST, methodName,
								"jnJSalesOrgCustomerModel not found for given JnjB2BUnitModel pk,"
										+ "Creating a new jnJSalesOrgCustomerModel",
								JnjLASalesOrgCustomerServiceImpl.class);
						jnJSalesOrgCustomerModel = modelService.create(JnJSalesOrgCustomerModel.class);
					}
					jnJSalesOrgCustomerModel.setCustomerId(b2bUnit);
					jnJSalesOrgCustomerModel.setSalesOrg(salesOrg.getSalesOrg());
					jnJSalesOrgCustomerModel.setSector(salesOrg.getSector());
					getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(salesOrg.getLastUpdatedDate(), cronJobModel);
					modelService.save(jnJSalesOrgCustomerModel);
				}
				else
				{
					LOG.error("B2B Unit not present in Hybris DB : " + salesOrg.getCustomerNum());
				}

			}
			catch (final ModelNotFoundException modelNotFoundException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SALES_ORG_CUST, methodName,
						"Error Occured While Saving Data to Hybris" + Logging.HYPHEN + modelNotFoundException.getMessage(),
						modelNotFoundException, JnjLASalesOrgCustomerServiceImpl.class);
				recordStatus = false;
				errorMessage = modelNotFoundException.getMessage();
				if (StringUtils.isEmpty(errorMessage))
				{
					errorMessage = "Data wasn't saved in the platform";
				}
			}
			catch (final ModelSavingException modelSavingException)
			{
				JnjGTCoreUtil.logErrorMessage(
						Logging.SALES_ORG_CUST, methodName, "modelSaving Exception  Occured While Saving data to Models"
								+ Logging.HYPHEN + modelSavingException.getMessage(),
						modelSavingException, JnjLASalesOrgCustomerServiceImpl.class);
				recordStatus = false;
				errorMessage = "Data wasn't saved in the platform";
			}
			catch (final IllegalArgumentException illegalArgumentException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SALES_ORG_CUST, methodName,
						"illegalArgument exception occured while loading the data from RSA" + Logging.HYPHEN
								+ illegalArgumentException.getMessage(),
						illegalArgumentException, JnjLASalesOrgCustomerServiceImpl.class);
				recordStatus = false;
				errorMessage = illegalArgumentException.getMessage();
				if (StringUtils.isEmpty(errorMessage))
				{
					errorMessage = "Data wasn't saved in the platform";
				}
			}
			catch (final Exception exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SALES_ORG_CUST, methodName,
						"Error Occured While Saving the data to actual Model" + Logging.HYPHEN + exception.getMessage(), exception,
						JnjLASalesOrgCustomerServiceImpl.class);
				recordStatus = false;
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SALES_ORG_CUST, methodName, Logging.END_OF_METHOD,
				JnjLASalesOrgCustomerServiceImpl.class);
	}
}
