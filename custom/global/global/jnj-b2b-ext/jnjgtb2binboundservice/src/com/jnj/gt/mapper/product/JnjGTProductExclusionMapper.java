package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.enums.JnjProductExclusionClassType;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.common.impl.DefaultJnjGTStgSerivce;
import com.jnj.gt.service.product.JnjGTProductExclusionFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;


/**
 * The Mapper class responsible to deduce logic for calculating <code>Collection</code> of <code>JnjGTProduct</code>
 * excluded for each <code>JnjB2bUnit</code> Customer account.
 * 
 * Version 0.1 [Author: akash.rawat]
 * 
 */

public class JnjGTProductExclusionMapper extends JnjAbstractMapper
{
	public JnjGTProductExclusionMapper()
	{

	}

	/**
	 * Constant LOGGER.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTProductExclusionMapper.class);

	/**
	 * Private Instance of <code>jnjGTFeedService</code>.
	 */
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	/**
	 * Private instance of <code>JnjGTProductExclusionFeedService</code>.
	 */
	@Autowired
	private JnjGTProductExclusionFeedService jnjGTProductExclusionFeedService;

	@Autowired
	private DefaultJnjGTStgSerivce jnjGTStgSerivce;

	private static final String MATERIAL_IND = JnJCommonUtil.getValue(Jnjgtb2binboundserviceConstants.Product.MATERIAL_INDICATOR);

	private CatalogVersionModel catalogVersionModel;

	// Fetching all the status code value from the config files.
	private static final List<String> activeCodeOne = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE_ONE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> activeCodeTwo = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE_TWO, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	private static final List<String> activeCodeList = JnJCommonUtil.getValues(
			Jnjgtb2binboundserviceConstants.Product.ACTIVE_STATUS_CODE, Jnjgtb2binboundserviceConstants.COMMA_STRING);

	/**
	 * Processes all <code>JnjGTIntProductExclusionModel</code> records and persist them in form of exclusions in the
	 * Hybris database.
	 */
	@Override
	public void processIntermediateRecords()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		catalogVersionModel = catalogVersionService.getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID,

				Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);


		final List<JnJProductModel> JnJProductModels = jnjGTProductFeedService.getProductModelForExclusionFeed(
				catalogVersionModel, false);
		for (final JnJProductModel JnJProductModel : JnJProductModels)
		{
			// Refresh the model.
			modelService.refresh(JnJProductModel);
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
						+ "JnJProductModel code is " + JnJProductModel.getCode());
			}
			// if the mode status is not applicable then enter inside if block.
			if (null != JnJProductModel.getMaterialStatusEffectDate()
					&& !JnjGTModStatus.NOTAPPLICABLE.getCode().equals(JnJProductModel.getModStatus().getCode()))
			{
				// if the material status effective date lie after 365 days then enter inside if block.
				if (materialStatusEffectDateLieAfterThreeSixtyFiveDays(JnJProductModel.getMaterialStatusEffectDate()))
				{
					JnJProductModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
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
							if (JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getModStatus()))
							{
								jnjGTProdModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
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
								if (i > 0 && JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getModStatus()))
								{
									jnjGTProdModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
									modelService.save(jnjGTProdModel);
								}
								else if (JnjGTModStatus.DISCONTINUED.equals(jnjGTProdModel.getModStatus()))
								{
									i++;
								}
							}
							else
							{
								jnjGTProdModel.setModStatus(JnjGTModStatus.NOTAPPLICABLE);
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

		// Fetch all those records which have status as pending and class type as "Customer Group"
		final Collection<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModels = getJnjGTProductExclusionFeedService()
				.getProdExclusionRecords(JnjProductExclusionClassType.PRODUCT_CUST_GROUP, null, null);

		// Process all the intermediates records.
		processIntProdExclusionRecords(jnjGTIntProductExclusionModels);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Process intermediate products exclusion records by using the jnjGTIntProductExclusionModels which have only
	 * customer group related data.
	 * 
	 * @param jnjGTIntProductExclusionModels
	 *           the jnj na int product exclusion models
	 * 
	 */
	private void processIntProdExclusionRecords(final Collection<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModels)

	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntProdExclusionRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		List<JnjGTIntProductExclusionModel> prodExcModelInProcessing = null;
		// Iterates the records one by one.
		for (final JnjGTIntProductExclusionModel jnjGTIntProductExclusionModel : jnjGTIntProductExclusionModels)
		{
			// Refresh the model to get the latest value.
			modelService.refresh(jnjGTIntProductExclusionModel);
			// Check the status if it's pending then enter inside if block.
			if (StringUtils.isNotEmpty(jnjGTIntProductExclusionModel.getCharacteristic())
					&& RecordStatus.PENDING.equals(jnjGTIntProductExclusionModel.getRecordStatus()))
			{
				// Get the b2b unit by using the characteristic value.
				final Collection<JnJB2BUnitModel> b2bUnitModelList = getJnjGTProductExclusionFeedService().getCustomersForGroup(
						jnjGTIntProductExclusionModel.getCharacteristic());
				// Fetch Product Exclusions Intermediate records which are the associated with Customer Group and having characteristic value.
				final Collection<JnjGTIntProductExclusionModel> jnjGTIntProdExclModelsForCustGrp = getJnjGTProductExclusionFeedService()
						.getProdExclusionRecords(JnjProductExclusionClassType.PRODUCT_CUST_GROUP,
								jnjGTIntProductExclusionModel.getCharacteristic(), null);
				// Check for the non empty or null if it's not empty or not null then enter inside if block else write all those in dash board
				if (CollectionUtils.isNotEmpty(b2bUnitModelList))
				{
					prodExcModelInProcessing = new ArrayList<JnjGTIntProductExclusionModel>();
					final Set<JnJProductModel> JnJProductModels = new HashSet<JnJProductModel>();
					for (final JnjGTIntProductExclusionModel jnjGTIntPrdExModel : jnjGTIntProdExclModelsForCustGrp)
					{
						// Check for the material indicator if its a material indicator then enter inside if block.
						if (StringUtils.isEmpty(jnjGTIntPrdExModel.getMaterialCustInd())
								|| MATERIAL_IND.equals(jnjGTIntPrdExModel.getMaterialCustInd()))
						{
							// Fetch the jnjGTProduct Model by using material customer number.
							final JnJProductModel jnjGTProduct = jnjGTProductFeedService.getProductByCode(
									jnjGTIntPrdExModel.getMaterialCustNumber(), catalogVersionModel);
							// If the JnjGTProduct Model is not null and it's active status and also it lies inside within 30 days from the current date then enter inside 
							// if block else write it in write dash board.
							if (null == jnjGTProduct)
							{
								//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
								jnjGTFeedService.updateIntermediateRecord(jnjGTIntPrdExModel, RecordStatus.ERROR, true,
										"JnJProductModel is not existed for this customer group", Logging.PRODUCT_EXCLUSION_FEED,
										jnjGTIntPrdExModel.getMaterialCustNumber());
							}
							else if (JnjGTModStatus.ACTIVE.getCode().equals(jnjGTProduct.getModStatus().getCode())
									&& null != jnjGTProduct.getFirstShipEffectDate()
									&& shipDateLieWithInThirtyDays(jnjGTProduct.getFirstShipEffectDate()))
							{
								JnJProductModels.add(jnjGTProduct);
								jnjGTIntPrdExModel.setRecordStatus(RecordStatus.PROCESSING);
								modelService.save(jnjGTIntPrdExModel);
								prodExcModelInProcessing.add(jnjGTIntPrdExModel);
							}// set the status of the records as success to avoid executing the same customer group record for the jnjGTIntProductExclusionModel model.
							else
							{
								jnjGTIntPrdExModel.setRecordStatus(RecordStatus.SUCCESS);
								modelService.save(jnjGTIntPrdExModel);
							}
						}
						else
						{
							//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
							jnjGTFeedService.updateIntermediateRecord(jnjGTIntPrdExModel, RecordStatus.ERROR, true,
									"Material Customer Indicator is null or false for this material number.",
									Logging.PRODUCT_EXCLUSION_FEED, jnjGTIntPrdExModel.getMaterialCustNumber());
						}
					}
					try
					{
						if (CollectionUtils.isNotEmpty(b2bUnitModelList) && JnJProductModels.size() > 0)
						{
							if (LOGGER.isDebugEnabled())
							{
								LOGGER.debug("B2BUnit Model List size is " + b2bUnitModelList.size() + " and Product model list "
										+ JnJProductModels.size());
							}
							// Call the below mentioned method to work on Product Group and product attribute.
							processCustomerGrpDataUsingB2BUnitsAndProdModels(b2bUnitModelList, JnJProductModels);
							// Setting the records status as success for those records on which we have set the status as processing and applied the multi mode logic.
							for (final JnjGTIntProductExclusionModel prodExclusionSuccessModel : prodExcModelInProcessing)
							{
								prodExclusionSuccessModel.setRecordStatus(RecordStatus.SUCCESS);
								modelService.save(prodExclusionSuccessModel);
							}
						}
					}
					catch (final BusinessException exception)
					{
						LOGGER.error(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
								+ "Business Exception occurred -" + exception.getMessage(), exception);
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						jnjGTFeedService.updateIntermediateRecord(jnjGTIntProductExclusionModel, RecordStatus.ERROR, true,
								exception.getMessage(), Logging.PRODUCT_EXCLUSION_FEED,
								jnjGTIntProductExclusionModel.getMaterialCustNumber());
					}
					catch (final Throwable exception)
					{
						LOGGER.error(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
								+ "Throwable Exception occurred -" + exception.getMessage(), exception);
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						jnjGTFeedService.updateIntermediateRecord(jnjGTIntProductExclusionModel, RecordStatus.ERROR, true,
								exception.getMessage(), Logging.PRODUCT_EXCLUSION_FEED,
								jnjGTIntProductExclusionModel.getMaterialCustNumber());
					}
				}
				else
				{
					for (final JnjGTIntProductExclusionModel jnjGTIntPrdExModel : jnjGTIntProdExclModelsForCustGrp)
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						jnjGTFeedService.updateIntermediateRecord(jnjGTIntPrdExModel, RecordStatus.ERROR, true,
								"B2B Unit Models are not existed for this customer group", Logging.PRODUCT_EXCLUSION_FEED,
								jnjGTIntPrdExModel.getCharacteristic());
					}
				}
			}
			else if (RecordStatus.PENDING.equals(jnjGTIntProductExclusionModel.getRecordStatus()))
			{
				//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
				jnjGTFeedService.updateIntermediateRecord(jnjGTIntProductExclusionModel, RecordStatus.ERROR, true,
						"Characteristic is null for the material number material number.", Logging.PRODUCT_EXCLUSION_FEED,
						jnjGTIntProductExclusionModel.getMaterialCustNumber());
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processIntProdExclusionRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Process customer group data using b2b units and product models.
	 * 
	 * @param b2bUnitModelList
	 *           the b2b unit model list
	 * @param JnJProductModels
	 *           the jnj na product models
	 * @throws BusinessException
	 *            the business exception
	 */
	private void processCustomerGrpDataUsingB2BUnitsAndProdModels(final Collection<JnJB2BUnitModel> b2bUnitModelList,
			final Collection<JnJProductModel> JnJProductModels) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processCustomerGrpDataUsingB2BUnitsAndProdModels()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		// Iterate the b2b unit models one by one.
		for (final JnJB2BUnitModel JnJB2BUnitModel : b2bUnitModelList)
		{
			final Map<String, Set<JnJProductModel>> mapJnJProductModel = new HashMap<String, Set<JnJProductModel>>();
			Set<JnJProductModel> jnjGTProdModelSet = null;
			for (final JnJProductModel JnJProductModel : JnJProductModels)
			{
				final List<String> b2bUnitCodes = getJnjGTProductExclusionFeedService().getB2BUnitCodesUsingMaterialNumber(
						JnJProductModel.getCode());
				// Get the list of b2b unit codes in b2bUnitCodes using current product code and check if this list does not contains the 
				// iterated b2bunit code in it. if yes then move to next product in for loop and if not then move to next line. Also if there is no entry for the product
				// in allowed customer and it's in Product cust group then that product can be associated all the B2B Unit which are associated with customer group and if there is
				// an entry in allowed customer then that product is only associated with that particular b2b unit.
				if (CollectionUtils.isNotEmpty(b2bUnitCodes) && !b2bUnitCodes.contains(JnJB2BUnitModel.getUid()))
				{
					continue;
				}
				// Fetches all those intermediates records which have product group value existed with a particular uid of JnJB2BUnitModel and product material number.
				final Collection<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModelsWithProdGrpData = getJnjGTProductExclusionFeedService()
						.getProdExclusionRecords(JnjProductExclusionClassType.PRODUCT_GROUP, JnJB2BUnitModel.getUid(),
								JnJProductModel.getCode());
				// Process the Product Group records to find if there is any product in intermediate records which has 
				// Product Number whose characteristic value same as Customer number for the Customer group. 
				final boolean matchingPrdGrpExist = processProductGroupOrAttributesData(jnjGTIntProductExclusionModelsWithProdGrpData);
				// If there is no match product group then enter inside if block else skip the product group.
				if (!matchingPrdGrpExist)
				{
					final Collection<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModelsWithProdAttrData = getJnjGTProductExclusionFeedService()
							.getProdExclusionRecords(JnjProductExclusionClassType.PRODUCT_ATTRIBUTE, JnJB2BUnitModel.getUid(),
									JnJProductModel.getCode());
					// Process the Product  Attribute records to find if there is any product in intermediate records which has 
					// Product Number whose characteristic value same as Customer number for the Customer group.
					final boolean matchingPrdAttrExist = processProductGroupOrAttributesData(jnjGTIntProductExclusionModelsWithProdAttrData);
					// If there is no match product group then enter inside if block else skip the product group.
					if (!matchingPrdAttrExist)
					{
						String baseMaterialCode = null;
						if (null != JnJProductModel.getMaterialBaseProduct())
						{
							baseMaterialCode = JnJProductModel.getMaterialBaseProduct().getCode();
						}
						else
						{
							baseMaterialCode = JnJProductModel.getCode();
						}
						if (mapJnJProductModel.containsKey(baseMaterialCode))
						{
							jnjGTProdModelSet = mapJnJProductModel.get(baseMaterialCode);
							jnjGTProdModelSet.add(JnJProductModel);
						}
						else
						{
							jnjGTProdModelSet = new HashSet<JnJProductModel>();
							jnjGTProdModelSet.add(JnJProductModel);
							mapJnJProductModel.put(baseMaterialCode, jnjGTProdModelSet);
						}
					}
				}
			}
			//Implements the Multi & Single mode logic by using the map which contains base material code as key and Set of JnJProductModels which have same base material code.
			if (mapJnJProductModel.size() > 0)
			{
				setMultiAndSingleModLogic(mapJnJProductModel, JnJB2BUnitModel);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processCustomerGrpDataUsingB2BUnitsAndProdModels()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Process product group or attributes data.
	 * 
	 * @param jnjGTIntProductExclusionModels
	 *           the jnj na int product exclusion models
	 * @return true, if successful
	 */
	private boolean processProductGroupOrAttributesData(
			final Collection<JnjGTIntProductExclusionModel> jnjGTIntProductExclusionModels)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processProductGroupOrAttributesData()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean matchingProductGrpOrAttrExist = false;

		final List<String> productGroups = new ArrayList<String>();
		for (final JnjGTIntProductExclusionModel jnjGTIntProductExclusionModel : jnjGTIntProductExclusionModels)
		{
			if (productGroups.contains(jnjGTIntProductExclusionModel.getCharacteristic()))
			{
				matchingProductGrpOrAttrExist = true;
				break;
			}
			productGroups.add(jnjGTIntProductExclusionModel.getCharacteristic());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processProductGroupOrAttributesData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return matchingProductGrpOrAttrExist;
	}

	/**
	 * The setMultiAndSingleModLogic method is used to set the multi and single mod logic.
	 * 
	 * @param mapJnJProductModel
	 *           the map jnj na product model
	 * @return the sets the
	 * @throws BusinessException
	 */
	private Set<JnJProductModel> setMultiAndSingleModLogic(final Map<String, Set<JnJProductModel>> mapJnJProductModel,
			final JnJB2BUnitModel JnJB2BUnitModel) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "setMultiAndSingleModLogic()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Set<JnJProductModel> jnjGTProdModelSet = null;
		final Set<JnJProductModel> finalJnjGTProdModelSet = new HashSet<JnJProductModel>();

		// Iterates the map entries one by one.
		for (final Entry<String, Set<JnJProductModel>> entry : mapJnJProductModel.entrySet())
		{
			jnjGTProdModelSet = entry.getValue();
			// Check the size of the set if it's one then set the jnjGTProdModelSet in final Jnj NA Product Model set.
			if (jnjGTProdModelSet.size() == 1)
			{
				finalJnjGTProdModelSet.addAll(jnjGTProdModelSet);
			}
			else
			{
				// Maintain three set to avoid multiples iteration.
				final Set<JnJProductModel> jnjGTPrdModWithPubInd = new HashSet<JnJProductModel>();
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
					}
					else if (activeCodeOne.contains(JnJProductModel.getProductStatusCode()))
					{
						jnjGTPrdModWithPubIndWithActiveCodeOne.add(JnJProductModel);
						if (JnJProductModel.getPublishInd().booleanValue())
						{
							jnjGTPrdModWithPubInd.add(JnJProductModel);
						}
					}
					else if (JnJProductModel.getPublishInd().booleanValue())
					{
						jnjGTPrdModWithPubInd.add(JnJProductModel);
					}
				}
				// Get the JnjGTProduct Model after processing the aforementioned sets.
				processDiffListAsPerModLogic(jnjGTPrdModWithPubInd, jnjGTPrdModWithPubIndWithActiveCodeOne,
						jnjGTPrdModWithPubIndWithActiveCodeTwo, finalJnjGTProdModelSet);
			}

		}
		if (CollectionUtils.isNotEmpty(finalJnjGTProdModelSet))
		{
			JnJB2BUnitModel.setIncludedProducts(finalJnjGTProdModelSet);
			// Save the JnjGTB2BUnit model data which is having included products data.
			getJnjGTProductExclusionFeedService().saveItem(JnJB2BUnitModel);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "setMultiAndSingleModLogic()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return finalJnjGTProdModelSet;
	}

	/**
	 * The processDiffListAsPerModLogic method is used to process different list to implement the multi & single mod
	 * logic.
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
			final List<JnJProductModel> jnjGTPrdModWithPubIndWithActiveCodeTwo, final Set<JnJProductModel> finalJnjGTProdModelSet)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processDiffListAsPerModLogic()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		JnjObjectComparator jnjObjectComparator = null;
		// sorting the JnJProductModel object on the basis of the firstShipEffectDate in descending order.
		jnjObjectComparator = new JnjObjectComparator(JnJProductModel.class, "getFirstShipEffectDate", false, true);
		if (jnjGTPrdModWithPubInd.size() == 1)
		{
			finalJnjGTProdModelSet.addAll(jnjGTPrdModWithPubInd);
		}
		else if (jnjGTPrdModWithPubInd.size() != 1 && jnjGTPrdModWithPubIndWithActiveCodeOne.size() == 1)
		{
			finalJnjGTProdModelSet.addAll(jnjGTPrdModWithPubIndWithActiveCodeOne);
		}
		else if (jnjGTPrdModWithPubInd.size() != 1 && jnjGTPrdModWithPubIndWithActiveCodeOne.size() > 1)
		{
			// Sort the jnjGTPrdModWithPubIndWithActiveCodeOne set on online date.
			Collections.sort(jnjGTPrdModWithPubIndWithActiveCodeOne, jnjObjectComparator);
			finalJnjGTProdModelSet.add(jnjGTPrdModWithPubIndWithActiveCodeOne.get(0));
		}
		else if (jnjGTPrdModWithPubInd.size() != 1 && jnjGTPrdModWithPubIndWithActiveCodeTwo.size() > 1)
		{
			// Sort the jnjGTPrdModWithPubIndWithActiveCodeOne set on online date.
			Collections.sort(jnjGTPrdModWithPubIndWithActiveCodeTwo, jnjObjectComparator);
			finalJnjGTProdModelSet.add(jnjGTPrdModWithPubIndWithActiveCodeTwo.get(0));
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "processDiffListAsPerModLogic()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "shipDateLieWithInThirtyDays()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "shipDateLieWithInThirtyDays()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isShipDateLie;
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "shipDateLieWithInCurrentDate()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "shipDateLieWithInCurrentDate()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "materialStatusEffectDateLieAfterThreeSixtyFiveDays()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
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
			LOGGER.debug(Logging.PRODUCT_EXCLUSION_FEED + Logging.HYPHEN + "materialStatusEffectDateLieAfterThreeSixtyFiveDays()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return ismaterialStatusEffectDateLie;
	}

	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	public JnjGTProductExclusionFeedService getJnjGTProductExclusionFeedService()
	{
		return jnjGTProductExclusionFeedService;
	}

	@Override
	public void processIntermediateRecords(String facadeBeanId) {
		// TODO Auto-generated method stub
		
	}

}
