/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnjSalesOrgCustomerDao;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjSalesOrgAndSplProdMapModel;
import com.jnj.core.services.JnjSalesOrgCustService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;


/**
 * This class is used to save the data into Hybris data base
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjSalesOrgCustService implements JnjSalesOrgCustService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjSalesOrgCustService.class);

	@Autowired
	private ModelService modelService;
	
	@Autowired
	private ProductService productService;

	@Autowired
	private JnjSalesOrgCustomerDao jnJSalesOrgCustomerDao;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	

	public ModelService getModelService() {
		return modelService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public JnjSalesOrgCustomerDao getJnJSalesOrgCustomerDao() {
		return jnJSalesOrgCustomerDao;
	}

	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/*
	 * This method is used for save the sales Org customer Model details in to hybris database
	 * 
	 * @param jnJSalesOrgCustomerModel JnJSalesOrgCustomerModel;
	 * 
	 * @return success boolean
	 */
	@Override
	public boolean saveItemModel(final ItemModel itemModel)
	{
		final String METHOD_NAME = "saveItemModel()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + " - " + METHOD_NAME + Logging.BEGIN_OF_METHOD
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
			LOGGER.warn(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + "-" + METHOD_NAME + "model not saved into Hybris data base" + "-"
					+ modelSavingException.getLocalizedMessage() + JnJCommonUtil.getCurrentDateTime());
			success = false;
			throw modelSavingException;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ORG_CUSTOMER_DATA_LOAD + " - " + METHOD_NAME + Logging.END_OF_METHOD
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return success;

	}

	/**
	 * this method is used for Junit
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * This method is used to get the salesOrgModel for given CustomerId and Sector
	 */
	@Override
	public JnJSalesOrgCustomerModel getSalesOrgModel(final String customerId, final String sector)
	{

		return jnJSalesOrgCustomerDao.getJnJSalesOrgCustomerModeById(customerId, sector);
	}

	@Override
	public List<JnJSalesOrgCustomerModel> getSalesOrgsForCurrentUser()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgsForCurrentUser()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnJB2BUnitModel unit = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
		final JnJSalesOrgCustomerModel jnjSalesOrgCustomerModel = new JnJSalesOrgCustomerModel();
		jnjSalesOrgCustomerModel.setCustomerId(unit);
		final List<JnJSalesOrgCustomerModel> salesOrgList = flexibleSearchService.getModelsByExample(jnjSalesOrgCustomerModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgsForCurrentUser()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrgList;
	}

	@Override
	public boolean checkColdChainStorage(final String salesOrgId)
	{
		boolean result = false;
		JnjSalesOrgAndSplProdMapModel salesOrgMap = new JnjSalesOrgAndSplProdMapModel();
		salesOrgMap.setSalesOrg(salesOrgId);
		try
		{
			salesOrgMap = flexibleSearchService.getModelByExample(salesOrgMap);
			result = salesOrgMap.getHandleColdChain().booleanValue();
		}
		catch (final ModelNotFoundException modelNotFoundExp)
		{
			LOGGER.info("Sales org vs Cold chain mapping does not exists");
		}
		return result;
	}

	@Override
	public HashMap<String, String> getSectorAndSalesOrgMapping()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSectorAndSalesOrgMapping()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnJSalesOrgCustomerModel> salesOrgs = getSalesOrgsForCurrentUser();

		final HashMap<String, String> salesOrgMap = new HashMap<String, String>();
		for (final JnJSalesOrgCustomerModel salesOrg : salesOrgs)
		{
			if (null != salesOrg)
			{
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("getSectorAndSalesOrgMapping()" + Logging.HYPHEN + "Value returns sector is " + salesOrg.getSector()
							+ "-Salesorg is-" + salesOrg.getSalesOrg());
				}
				salesOrgMap.put(salesOrg.getSector().toUpperCase(), salesOrg.getSalesOrg());
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSectorAndSalesOrgMapping()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrgMap;
	}

	@Override
	public String getSalesOrgAndSapOrderType(final JnJProductModel jnjProductModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgAndSapOrderType()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final HashMap<String, String> sectorSalesOrgMapping = getSectorAndSalesOrgMapping();
		String orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZOR;

		String dispatcherSalesOrg = StringUtils.EMPTY;
		String productCode = jnjProductModel.getCode();
		final JnJProductModel productModel = (JnJProductModel) productService.getProductForCode(productCode);

		final String productSector = jnjProductModel.getSector() == null ? StringUtils.EMPTY : jnjProductModel.getSector()
				.toUpperCase();
		if (null != sectorSalesOrgMapping && sectorSalesOrgMapping.containsKey(productSector))
		{
			dispatcherSalesOrg = sectorSalesOrgMapping.get(productSector);
			//Check if product is cold Chain product.
			if (jnjProductModel.getColdChainProduct().booleanValue())
			{
				//Sap order type will be ZORD if corresponding salesOrg does not have cold chain storage.  
				if (!checkColdChainStorage(dispatcherSalesOrg))
				{
					orderType = Jnjb2bCoreConstants.SAP_ORDER_TYPE_ZORD;
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgAndSapOrderType()" + Logging.HYPHEN + "Value returns by method is "
					+ dispatcherSalesOrg.concat(Jnjb2bCoreConstants.Order.PIPE_STRING).concat(orderType));
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesOrgAndSapOrderType()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return dispatcherSalesOrg.concat(Jnjb2bCoreConstants.Order.PIPE_STRING).concat(orderType);
	}

	//This setter is used in JUnits
	public void setJnjGetCurrentDefaultB2BUnitUtil(final JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil)
	{
		this.jnjGetCurrentDefaultB2BUnitUtil = jnjGetCurrentDefaultB2BUnitUtil;
	}

	//This setter is used in JUnits
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
