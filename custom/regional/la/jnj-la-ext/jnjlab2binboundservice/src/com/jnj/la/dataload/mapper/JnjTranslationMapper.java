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

import com.jnj.core.jalo.JnjIntegrationRSACronJob;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjTranslationDTO;
import com.jnj.la.core.model.LoadTranslationModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.JnjLoadTranslationService;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;


public class JnjTranslationMapper
{

	@Autowired
	private JnJLaProductService jnjLaProductService;

	@Resource(name = "jnjB2BUnitService")
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private JnjLoadTranslationService jnjLoadTranslationService;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	private static final Logger LOG = Logger.getLogger(JnjTranslationMapper.class);

	@SuppressWarnings("boxing")
	public boolean processTranslations(final List<JnjTranslationDTO> jnjTranslationDTO, final JnjIntegrationRSACronJobModel cronjobmodel)
			throws NumberFormatException, BusinessException
	{

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, "processTranslations()",
				Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), JnjTranslationMapper.class);

		final String methodName = "mapLoadTranslationData()";

		boolean modelSaved = false;
		LoadTranslationModel loadTranslationModel = null;

		for (int i = 0; i < jnjTranslationDTO.size(); i++)
		{
			final JnjTranslationDTO element = jnjTranslationDTO.get(i);
			try
			{
				final JnJProductModel jnjProductModel = jnjLaProductService.getProduct(getB2bUnit(element), element.getMaterialNum());

				if (null != element.getCustomerNum())
				{
					final B2BUnitModel userGroupModel = getB2bUnit(element);
					if (null != userGroupModel)
					{
						//Checking For Existing Load Translation Model
						loadTranslationModel = getLoadTranslationModel(userGroupModel, jnjProductModel);
						loadTranslationModel.setB2bUnit(userGroupModel);
						if (null != jnjProductModel)
						{
							loadTranslationModel.setCatalogId(jnjProductModel.getCatalogId().toUpperCase());
						}
						JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName, "B2B Unit is set in model",
								JnjTranslationMapper.class);
					}
					else
					{
						JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, methodName, "Customer NULL is not in the platform.",
								JnjTranslationMapper.class);
						throw new BusinessException("Customer NULL is not in the platform.", MessageCode.BUSINESS_EXCEPTION,
								Severity.BUSINESS_EXCEPTION);
					}
				}

				if (null != element.getMaterialNum())
				{
					if (null != jnjProductModel)
					{
						loadTranslationModel.setProductId(jnjProductModel);
						loadTranslationModel.setCatalogId(jnjProductModel.getCatalogId().toUpperCase());

						JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName, "Product is set in model",
								JnjTranslationMapper.class);
					}
					else
					{
						JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, methodName, "Product can not be null, skipping record",
								JnjTranslationMapper.class);
						throw new BusinessException("Product can not be null, skipping record", MessageCode.BUSINESS_EXCEPTION,
								Severity.BUSINESS_EXCEPTION);
					}
				}
				if (null != element.getCustMaterialNum())
				{
					loadTranslationModel.setCustMaterialNum(element.getCustMaterialNum());

					if (LOG.isDebugEnabled())
					{
						JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName, "Customer Material Number is set in model",
								JnjTranslationMapper.class);
					}
				}

				if (StringUtils.isNotEmpty(element.getBaseUom()))
				{
					final UnitModel baseUnitmodel = getUnit(element.getBaseUom());
					if (null != baseUnitmodel)
					{
						baseUnitmodel.setUnitType(Jnjb2bCoreConstants.LoadTranslation.LOAD_TRANSLATION_JNJ_UNIT_TYPE);
						loadTranslationModel.setBaseUOM(baseUnitmodel);

						if (LOG.isDebugEnabled())
						{
							JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName, "Base UOM is set in model",
									JnjTranslationMapper.class);
						}
					}
					else
					{
						JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, methodName,
								"Base UOM not found in Hybris, skipping record", JnjTranslationMapper.class);
						throw new BusinessException("Base UOM not found in Hybris, skipping record", MessageCode.BUSINESS_EXCEPTION,
								Severity.BUSINESS_EXCEPTION);
					}
				}

				if (StringUtils.isNotEmpty(element.getCustUom()))
				{
					loadTranslationModel.setCustomerUOM(element.getCustUom());
				}

				if (StringUtils.isNotEmpty(element.getRoundingProfile()))
				{
					final UnitModel customerdevUom = getUnit(element.getRoundingProfile());
					if (null != customerdevUom)
					{
						loadTranslationModel.setCustDevUom(customerdevUom);
						if (LOG.isDebugEnabled())
						{
							JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName, "Customer DEV UOM is set in model",
									JnjTranslationMapper.class);
						}
					}
					else
					{
						JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName, "Customer DEV UOM is not set set in model",
								JnjTranslationMapper.class);
					}
				}

				if (null != element.getDenConversion())
				{
					loadTranslationModel.setDenominator((Double.valueOf(element.getDenConversion())).intValue());

					if (LOG.isDebugEnabled())
					{
						JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName, "Conversion Denominator is set in model",
								JnjTranslationMapper.class);
					}
				}

				if (null != element.getNumConversion())
				{
					loadTranslationModel.setNumerator((Double.valueOf(element.getNumConversion())).intValue());

					if (LOG.isDebugEnabled())
					{
						JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName, "Conversion Numerator is set in model",
								JnjTranslationMapper.class);
					}
				}

				// Calling the service
				modelSaved = jnjLoadTranslationService.saveloadTranslationData(loadTranslationModel);

				if (modelSaved)
				{
					setLastsuccessfulDate(element.getLastUpdatedDate(), cronjobmodel,
							"customer###" + element.getCustomerNum() + "& product### " + element.getMaterialNum());
				}
			}
			catch (final NumberFormatException numberFormatException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, methodName,
						"NumberFormatException occurred. Skipping Record:", numberFormatException, JnjTranslationMapper.class);
			}
			catch (final Exception exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION, methodName, "Exception occurred. Skipping Record: ",
						exception, JnjTranslationMapper.class);
			}
			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD,
					JnjTranslationMapper.class);
		}
		return modelSaved;
	}

	public void setLastsuccessfulDate(final String lastsuccessfulDate, final JnjIntegrationRSACronJobModel cronjobmodel, final String data)
	{
		final String methodName = "setLastsuccessfulDate()";

		if (LOG.isDebugEnabled())
		{
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName,
					"Translation Data processed successfully for --------" + data, JnjTranslationMapper.class);
		}
		getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(lastsuccessfulDate, cronjobmodel);

	}

	private LoadTranslationModel getLoadTranslationModel(final B2BUnitModel b2BUnitModel, final JnJProductModel jnJProductModel)

	{
		final String methodName = "getLoadTranslationModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);

		final LoadTranslationModel tempLoadTranslationModel = modelService.create(LoadTranslationModel.class);
		tempLoadTranslationModel.setB2bUnit(b2BUnitModel);
		tempLoadTranslationModel.setProductId(jnJProductModel);
		LoadTranslationModel loadTranslationModel = null;
		try
		{
			loadTranslationModel = flexibleSearchService.getModelByExample(tempLoadTranslationModel);

			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
					"LoadTranslationModel found in Hybris. Returning Existing Model", JnjTranslationMapper.class);
		}
		catch (ModelNotFoundException | IllegalArgumentException modelNotFoundException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "Exception Occurred" + modelNotFoundException, JnjTranslationMapper.class);
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "LoadTranslationModel not found in Hybris. Creating New Model" + modelNotFoundException,
					JnjTranslationMapper.class);
			loadTranslationModel = modelService.create(LoadTranslationModel.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);
		return loadTranslationModel;

	}

	private B2BUnitModel getB2bUnit(final JnjTranslationDTO jnjTranslationDTO)
	{
		B2BUnitModel b2BUnitModel = null;
		final String methodName = "getB2bUnit()";

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);

		if (jnjTranslationDTO.getCustomerNum() != null && !(jnjTranslationDTO.getCustomerNum().isEmpty()))
		{
			b2BUnitModel = jnjGTB2BUnitService.getUnitForUid(jnjTranslationDTO.getCustomerNum());

			if (LOG.isDebugEnabled())
			{
				JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName,
						"B2BUnit is present in Hybris DB for the RSA customer number" + jnjTranslationDTO.getCustomerNum(),
						JnjTranslationMapper.class);
			}
		}

		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_TRANSLATION, methodName,
					"B2BUnit is not present in Hybris DB for the RSA customer number" + jnjTranslationDTO.getCustomerNum(),
					JnjTranslationMapper.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);
		return b2BUnitModel;
	}

	private UnitModel getUnit(final String code)
	{
		final String methodName = "getUnit()";
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);

		final UnitModel tempUnitModel = new UnitModel();
		tempUnitModel.setCode(code);
		UnitModel unitModel = null;

		try
		{
			unitModel = flexibleSearchService.getModelByExample(tempUnitModel);
			if (null != unitModel)
			{
				unitModel.setCode(code);
			}

		}
		catch (final ModelNotFoundException | IllegalArgumentException modelNotFoundException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "Exception Occurred" + modelNotFoundException, JnjTranslationMapper.class);
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_TRANSLATION + Logging.HYPHEN, methodName,
					Logging.HYPHEN + "Unit model not found. Returning Null" + modelNotFoundException, JnjTranslationMapper.class);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_TRANSLATION, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis(), JnjTranslationMapper.class);
		return unitModel;
	}

}
