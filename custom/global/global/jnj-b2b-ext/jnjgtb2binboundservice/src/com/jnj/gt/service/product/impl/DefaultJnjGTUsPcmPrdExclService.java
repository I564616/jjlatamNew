/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.service.product.impl;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTExProductAttributeModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.gt.dao.exclusions.JnjGTUsPcmPrdExclDao;
import com.jnj.gt.service.product.JnjGTProductFeedService;
import com.jnj.gt.service.product.JnjGTUsPcmPrdExclService;



/**
 * The JnjGTUsPcmPrdExclServiceImpl class contains the definition of all the method of the JnjGTUsPcmPrdExclService
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTUsPcmPrdExclService implements JnjGTUsPcmPrdExclService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTUsPcmPrdExclService.class);
	@Autowired
	private JnjGTUsPcmPrdExclDao jnjGTUsPcmPrdExclDao;

	@Resource(name = "productService")
	JnJGTProductService jnjGTProductService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	private CatalogVersionModel catalogVersionModel;

	// Fetching all the status code value from the config files.
	private static final List<String> activeCodeOne = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE_ONE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> activeCodeTwo = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE_TWO, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> activeCodeList = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE, Jnjgtb2binboundserviceConstants.COMMA_STRING);


	@Override
	public boolean getExProductAttributeModels() throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "getExProductAttributeModels()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean isMultiModLogicImplemented = false;
		Set<JnJProductModel> jnjGTProdModelSet = null;
		// get the catalog version model by using the catalog and version id value.
		catalogVersionModel = catalogVersionService.getCatalogVersion(Jnjb2bCoreConstants.CONSUMER_US_PRODUCT_CATALOG_ID,
				Jnjb2bCoreConstants.STAGED);
		// Fetch all those records which we have saved at the time of product exclusion cron job execution in hybris table for given specified
		// characteristic values.
		final List<JnjGTExProductAttributeModel> jnjGTExProductAttributeModels = jnjGTUsPcmPrdExclDao.getExProductAttributeModels();

		if (CollectionUtils.isNotEmpty(jnjGTExProductAttributeModels))
		{
			for (final JnjGTExProductAttributeModel jnjGTExProductAttributeModel : jnjGTExProductAttributeModels)
			{
				try
				{// Fetch the jnjGTProduct Model by using material customer number.
					final JnJProductModel jnjGTProduct = (JnJProductModel) jnjGTProductService.getProductForCode(catalogVersionModel,
							jnjGTExProductAttributeModel.getCode());
					// set the pcm indicator and pcm mod status field if the publish ind is false or the kit indicator is true.
					if (BooleanUtils.isTrue(jnjGTProduct.getPcmInd()))
					{
						jnjGTProduct.setPcmInd(Boolean.FALSE);
						jnjGTProduct.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
						modelService.save(jnjGTProduct);
					}
				}
				catch (final Throwable exception)
				{
					LOGGER.error(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "getExProductAttributeModels()"
							+ Logging.HYPHEN + "Throwable Exception occurred -" + exception.getMessage(), exception);
				}
			}
		}

		// JnjGTProduct Model having Publish ind as false to make them NOT APPLICABLE so that they won't be shown on front end.
		final List<JnJProductModel> JnJProductModelsHavingPubInd = jnjGTUsPcmPrdExclDao
				.getProductModelHavingPublishIndFalse(catalogVersionModel);
		if (CollectionUtils.isNotEmpty(JnJProductModelsHavingPubInd))
		{
			for (final JnJProductModel JnJProductModelWithPubInd : JnJProductModelsHavingPubInd)
			{
				// Refresh the model.
				modelService.refresh(JnJProductModelWithPubInd);
				JnJProductModelWithPubInd.setPcmInd(Boolean.FALSE);
				JnJProductModelWithPubInd.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				modelService.save(JnJProductModelWithPubInd);
			}
		}

		// Fetch all those inactive records which has first ship effect date after 1 years set those product status as not applicable.
		final List<JnJProductModel> JnJProductModels = jnjGTProductFeedService.getProductModelForExclusionFeed(catalogVersionModel,
				true);
		for (final JnJProductModel JnJProductModel : JnJProductModels)
		{
			// Refresh the model.
			modelService.refresh(JnJProductModel);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "processIntermediateRecords()"
						+ Logging.HYPHEN + "JnJProductModel code is " + JnJProductModel.getCode());
			}
			// if the mode status is not applicable then enter inside if block.
			if (null != JnJProductModel.getMaterialStatusEffectDate()
					&& !JnjGTModStatus.NOTAPPLICABLE.getCode().equals(JnJProductModel.getPcmModStatus().getCode()))
			{
				// if the material status effective date lie after 365 days then enter inside if block.
				if (materialStatusEffectDateLieAfterThreeSixtyFiveDays(JnJProductModel.getMaterialStatusEffectDate()))
				{
					JnJProductModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
					modelService.save(JnJProductModel);
				}
				else
				{
					List<JnJProductModel> JnJProductModelSiblings = null;
					boolean isActiveProductExist = false;
					int i = 0;

					String baseProductPk = null;
					JnJProductModel product = null;
					if (null != JnJProductModel.getMaterialBaseProduct())
					{
						baseProductPk = JnJProductModel.getMaterialBaseProduct().getPk().toString();
						product = JnJProductModel.getMaterialBaseProduct();
					}
					else
					{
						baseProductPk = JnJProductModel.getPk().toString();
						product = JnJProductModel;
					}

					JnJProductModelSiblings = new ArrayList<JnJProductModel>(
							jnjGTProductFeedService.getJnJProductModelsUsingMaterialBaseProductPK(baseProductPk, catalogVersionModel));

					JnJProductModelSiblings.add(product);

					for (final JnJProductModel jnjGTNaProductModelSibling : JnJProductModelSiblings)
					{
						// Check any siblings is lie inside active bucket.
						if (activeCodeList.contains(jnjGTNaProductModelSibling.getProductStatusCode()))
						{
							isActiveProductExist = true;
							break;
						}
					}

					// If there is any active product in the list then set all the siblings with not applicable status.
					if (isActiveProductExist)
					{
						for (final JnJProductModel jnjGTProdModel : JnJProductModelSiblings)
						{
							if (JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getPcmModStatus()))
							{
								jnjGTProdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
								modelService.save(jnjGTProdModel);
							}
						}
					}
					else if (JnJProductModelSiblings.size() > 1)
					{
						for (final JnJProductModel jnjGTProdModel : JnJProductModelSiblings)
						{
							// the first ship date lie with in 
							if (null != JnJProductModel.getFirstShipEffectDate()
									&& shipDateLieWithInCurrentDate(JnJProductModel.getFirstShipEffectDate()))
							{
								// Only set "not applicable" for those products which is not at the zeroth position and exist as inactive status.
								if (i > 0 && JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getPcmModStatus()))
								{
									jnjGTProdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
									modelService.save(jnjGTProdModel);
								}
								else if (JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getPcmModStatus()))
								{
									i++;
								}
							}
							else
							{
								jnjGTProdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
								modelService.save(jnjGTProdModel);
							}
						}
					}
				}
			}
			else
			{
				LOGGER.warn("Material Status Effective Date is "
						+ JnJProductModel.getMaterialStatusEffectDate()
						+ " and product code is: "
						+ JnJProductModel.getCode()
						+ " and we didn't perform any activity on this product as the material status effective date is null or the pcm mod status is NOTAPPLICABLE already.");
			}
		}

		// Fetch all those records which has active status code and then implement mod logic.
		final List<JnJProductModel> JnJProductModelActiveList = jnjGTUsPcmPrdExclDao
				.getProductModelForUSProducts(catalogVersionModel);
		final Map<String, Set<JnJProductModel>> mapJnJProductModel = new HashMap<String, Set<JnJProductModel>>();
		for (final JnJProductModel JnJProductModel : JnJProductModelActiveList)
		{
			// check if the first ship effective date lie with in 30 days.
			if (null != JnJProductModel.getFirstShipEffectDate()
					&& shipDateLieWithInThirtyDays(JnJProductModel.getFirstShipEffectDate()))
			{
				if (mapJnJProductModel.containsKey(JnJProductModel.getMaterialBaseNum()))
				{
					jnjGTProdModelSet = mapJnJProductModel.get(JnJProductModel.getMaterialBaseNum());
					jnjGTProdModelSet.add(JnJProductModel);
				}
				else
				{
					jnjGTProdModelSet = new HashSet<JnJProductModel>();
					jnjGTProdModelSet.add(JnJProductModel);
					mapJnJProductModel.put(JnJProductModel.getMaterialBaseNum(), jnjGTProdModelSet);
				}
			}
			else
			{
				JnJProductModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				modelService.save(JnJProductModel);
			}
		}

		//Implements the Multi  mode logic by using the map which contains base material code as key and Set of JnJProductModels which have same base material code.
		if (mapJnJProductModel.size() > 0)
		{
			isMultiModLogicImplemented = setMultiModLogic(mapJnJProductModel);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "getExProductAttributeModels()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isMultiModLogicImplemented;
	}

	/**
	 * Ship date lie with in current date.
	 * 
	 * @param shipDate
	 *           the ship date
	 * @return true, if successful
	 */
	private boolean shipDateLieWithInCurrentDate(final Date shipDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "shipDateLieWithInCurrentDate()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean isShipDateLie = false;
		final Calendar cal = Calendar.getInstance();
		final Calendar inComingCalendar = Calendar.getInstance();
		inComingCalendar.setTime(shipDate);
		if (inComingCalendar.compareTo(cal) < 0)
		{
			isShipDateLie = true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "shipDateLieWithInCurrentDate()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isShipDateLie;
	}

	/**
	 * Material Status Effect date lie after three sixty five days.
	 * 
	 * @param materialStatusEffectDate
	 *           the ship date
	 * @return true, if successful
	 */
	private boolean materialStatusEffectDateLieAfterThreeSixtyFiveDays(final Date materialStatusEffectDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN
					+ "materialStatusEffectDateLieAfterThreeSixtyFiveDays()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean ismaterialStatusEffectDateLie = false;
		final Calendar cal = Calendar.getInstance();
		final Calendar inComingCalendar = Calendar.getInstance();
		inComingCalendar.setTime(materialStatusEffectDate);
		cal.set(Calendar.YEAR, (cal.get(Calendar.YEAR) - 1));
		// If the material status effective date lies after 365 days then the set the ismaterialStatusEffectDateLie flag as true. 
		if (inComingCalendar.compareTo(cal) < 0)
		{
			ismaterialStatusEffectDateLie = true;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN
					+ "materialStatusEffectDateLieAfterThreeSixtyFiveDays()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return ismaterialStatusEffectDateLie;
	}

	/**
	 * Ship date lie with in thirty days.
	 * 
	 * @param shipDate
	 *           the ship date
	 * @return true, if successful
	 */
	private boolean shipDateLieWithInThirtyDays(final Date shipDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "shipDateLieWithInThirtyDays()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean isShipDateLie = false;
		final Calendar cal = Calendar.getInstance();
		final Calendar inComingCalendar = Calendar.getInstance();
		inComingCalendar.setTime(shipDate);
		cal.set(Calendar.DAY_OF_MONTH, (cal.get(Calendar.DAY_OF_MONTH) + 30));
		if (inComingCalendar.compareTo(cal) <= 0)
		{
			isShipDateLie = true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "shipDateLieWithInThirtyDays()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isShipDateLie;
	}

	/**
	 * The setMultiModLogic method is used to set the multi mod logic.
	 * 
	 * @param mapJnJProductModel
	 *           the map jnj na product model
	 * @return the sets the
	 * @throws BusinessException
	 */
	private boolean setMultiModLogic(final Map<String, Set<JnJProductModel>> mapJnJProductModel) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "setMultiModLogic()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Set<JnJProductModel> jnjGTProdModelSet = null;
		boolean isMultiModLogicImplemented = false;

		// Iterates the map entries one by one.
		for (final Entry<String, Set<JnJProductModel>> entry : mapJnJProductModel.entrySet())
		{
			jnjGTProdModelSet = entry.getValue();
			// Check the size of the set if it's one then set the jnjGTProdModelSet in final Jnj NA Product Model set.
			if (jnjGTProdModelSet.size() > 1)
			{
				// Maintain three set to avoid multiples iteration.
				final Set<JnJProductModel> jnjGTPrdModWithPubInd = new HashSet<JnJProductModel>();
				final Set<JnJProductModel> jnjGTPrdModWithoutPubInd = new HashSet<JnJProductModel>();
				final List<JnJProductModel> jnjGTPrdModWithPubIndWithActiveCodeOne = new ArrayList<JnJProductModel>();
				final List<JnJProductModel> jnjGTPrdModWithPubIndWithActiveCodeTwo = new ArrayList<JnJProductModel>();
				for (final JnJProductModel JnJProductModel : jnjGTProdModelSet)
				{
					if (activeCodeTwo.contains(JnJProductModel.getProductStatusCode()))
					{
						jnjGTPrdModWithPubIndWithActiveCodeTwo.add(JnJProductModel);
						if (JnJProductModel.getPublishInd().booleanValue())
						{
							jnjGTPrdModWithPubInd.add(JnJProductModel);
						}
						else
						{
							jnjGTPrdModWithoutPubInd.add(JnJProductModel);
						}
					}
					else if (activeCodeOne.contains(JnJProductModel.getProductStatusCode()))
					{
						jnjGTPrdModWithPubIndWithActiveCodeOne.add(JnJProductModel);
						if (JnJProductModel.getPublishInd().booleanValue())
						{
							jnjGTPrdModWithPubInd.add(JnJProductModel);
						}
						else
						{
							jnjGTPrdModWithoutPubInd.add(JnJProductModel);
						}
					}
				}
				// Get the JnjGTProduct Model after processing the aforementioned sets.
				processDiffListAsPerModLogic(jnjGTPrdModWithPubInd, jnjGTPrdModWithPubIndWithActiveCodeOne,
						jnjGTPrdModWithPubIndWithActiveCodeTwo, jnjGTPrdModWithoutPubInd);
				isMultiModLogicImplemented = true;
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "setMultiModLogic()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isMultiModLogicImplemented;
	}

	/**
	 * The processDiffListAsPerModLogic method is used to process different list to implement the multi mod logic.
	 * 
	 * @param jnjGTPrdModWithPubInd
	 *           the jnj na prd mod with pub ind
	 * @param jnjGTPrdModWithPubIndWithActiveCodeOne
	 *           the jnj na prd mod with pub ind with active code one
	 * @param jnjGTPrdModWithPubIndWithActiveCodeTwo
	 *           the jnj na prd mod with pub ind with active code two
	 */
	private void processDiffListAsPerModLogic(final Set<JnJProductModel> jnjGTPrdModWithPubInd,
			final List<JnJProductModel> jnjGTPrdModWithPubIndWithActiveCodeOne,
			final List<JnJProductModel> jnjGTPrdModWithPubIndWithActiveCodeTwo, final Set<JnJProductModel> jnjGTPrdModWithoutPubInd)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "processDiffListAsPerModLogic()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (jnjGTPrdModWithPubInd.size() == 1)
		{
			for (final JnJProductModel product : jnjGTPrdModWithoutPubInd)
			{
				product.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				modelService.save(product);
			}
		}
		else if (jnjGTPrdModWithPubInd.size() > 1)
		{
			for (final JnJProductModel product : jnjGTPrdModWithoutPubInd)
			{
				jnjGTPrdModWithPubIndWithActiveCodeOne.remove(product);
				jnjGTPrdModWithPubIndWithActiveCodeTwo.remove(product);
				product.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				modelService.save(product);
			}
			filterOnlyOneActiveModStatusProduct(jnjGTPrdModWithPubIndWithActiveCodeOne, jnjGTPrdModWithPubIndWithActiveCodeTwo);
		}
		else
		{
			filterOnlyOneActiveModStatusProduct(jnjGTPrdModWithPubIndWithActiveCodeOne, jnjGTPrdModWithPubIndWithActiveCodeTwo);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN + "processDiffListAsPerModLogic()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * The filterOnlyOneActiveModStatusProduct method is used to filter only one active product model in group of active
	 * product status code which has value 1 or 2.
	 * 
	 * @param jnjGTPrdModActiveCodeOne
	 *           the jnj na prd mod with pub ind with active code one
	 * @param jnjGTPrdModWithActiveCodeTwo
	 *           the jnj na prd mod with pub ind with active code two
	 */
	private void filterOnlyOneActiveModStatusProduct(final List<JnJProductModel> jnjGTPrdModActiveCodeOne,
			final List<JnJProductModel> jnjGTPrdModWithActiveCodeTwo)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN
					+ "filterOnlyOneActiveModStatusProduct()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjObjectComparator jnjObjectComparator = null;
		int i = 0;
		// sorting the JnJProductModel object on the basis of the firstShipEffectDate in descending order.
		jnjObjectComparator = new JnjObjectComparator(JnJProductModel.class, "getFirstShipEffectDate", false, true);
		if (jnjGTPrdModActiveCodeOne.size() >= 1)
		{
			for (final JnJProductModel jnjGTPrdModel : jnjGTPrdModWithActiveCodeTwo)
			{
				jnjGTPrdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
				modelService.save(jnjGTPrdModel);
			}
			if (jnjGTPrdModActiveCodeOne.size() > 1)
			{
				i = 0;
				// Sort the jnjGTPrdModWithPubIndWithActiveCodeOne set on online date.
				Collections.sort(jnjGTPrdModActiveCodeOne, jnjObjectComparator);
				for (final JnJProductModel jnjGTPrdModel : jnjGTPrdModActiveCodeOne)
				{
					if (i > 0)
					{
						jnjGTPrdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
						modelService.save(jnjGTPrdModel);
					}
					i++;
				}
			}
		}
		else
		{
			i = 0;
			// Sort the jnjGTPrdModWithPubIndWithActiveCodeOne set on online date.
			Collections.sort(jnjGTPrdModWithActiveCodeTwo, jnjObjectComparator);
			for (final JnJProductModel jnjGTPrdModel : jnjGTPrdModWithActiveCodeTwo)
			{
				if (i > 0)
				{
					jnjGTPrdModel.setPcmModStatus(JnjGTModStatus.NOTAPPLICABLE);
					modelService.save(jnjGTPrdModel);
				}
				i++;
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Jnjb2bCoreConstants.Logging.US_PCM_PROD_EXCLUSION + Logging.HYPHEN
					+ "filterOnlyOneActiveModStatusProduct()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}
}
