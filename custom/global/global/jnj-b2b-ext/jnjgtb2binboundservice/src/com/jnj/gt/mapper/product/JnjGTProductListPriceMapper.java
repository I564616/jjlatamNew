/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJGTPriceRowModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;
import com.jnj.gt.model.JnjGTIntListPriceModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTListPriceFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * The Mapper class responsible to process all <code>JnjGTIntListPriceRowModel</code> records, and its associated
 * intermediate records to create or Update MDD or Consumer specific <code>JnjGTPriceRowtModel</code>. Logic includes
 * processing, mapping and establishing relationships of <code>JnjGTIntPriceRowModel</code> with associations.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */

public class JnjGTProductListPriceMapper extends JnjAbstractMapper
{
	private static final Logger lOGGER = LoggerFactory.getLogger(JnjGTProductListPriceMapper.class);

	/**
	 * Private instance of <code>jnjGTFeedService</code>
	 */
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * Private instance of <code>jnjGTListPriceFeedService</code>
	 */
	private JnjGTListPriceFeedService jnjGTListPriceFeedService;

	/**
	 * Private instance of <code>ProductService</code>
	 */
	@Autowired
	private ProductService productService;

	/**
	 * Private instance of <code>ModelService</code>
	 */
	@Autowired
	private ModelService modelService;

	/**
	 * Private instance of <code>FlexibleSearchService</code>
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * Processes all <code>JnjGTIntListPriceModel</code> records and persist them after processing as new or updated
	 * <code>JnJGTPriceRowModel</code>.
	 */
	@Override
	public void processIntermediateRecords()
	{
		// getting all intermediate list price .
		final Collection<JnjGTIntListPriceModel> interListPrices = (Collection<JnjGTIntListPriceModel>) getjnjGTFeedService()
				.getRecordsByStatus(JnjGTIntListPriceModel._TYPECODE, RecordStatus.PENDING);

		if (CollectionUtils.isEmpty(interListPrices))
		{
			if (lOGGER.isErrorEnabled())
			{
				lOGGER.error("JnjGTProductListPriceMapper : processIntermediateRecords() :: No IntermediateListPriceRow Model found to be proceed");
			}
			return;
		}
		// iterate list of intermediate list price
		for (final JnjGTIntListPriceModel intListPriceModel : interListPrices)
		{
			try
			{
				processIntermediatePriceRow(intListPriceModel);

				// updating intermediate table record status to "SUCCESS" 
				getjnjGTFeedService().updateIntermediateRecord(intListPriceModel, RecordStatus.SUCCESS, false, null,
						Logging.PRODUCT_LIST_PRICE, intListPriceModel.getListPriceID());
			}
			catch (final BusinessException e)
			{
				if (lOGGER.isErrorEnabled())
				{
					lOGGER.error("Exception occured while processing LISTPRICE Intermediate Records for the Base Material Number: "
							+ intListPriceModel.getProductSkuCode() + "\n Updating WRITE Dashboard.");
					getjnjGTFeedService().updateIntermediateRecord(intListPriceModel, null, true, e.getMessage(),
							Logging.PRODUCT_LIST_PRICE, intListPriceModel.getListPriceID());
				}
			}
			catch (final Exception e)
			{
				if (lOGGER.isErrorEnabled())
				{
					lOGGER.error("Exception occured while processing LISTPRICE Intermediate Records for the Product SKU Code: "
							+ intListPriceModel.getProductSkuCode() + "\n Updating WRITE Dashboard.");
					getjnjGTFeedService().updateIntermediateRecord(intListPriceModel, null, true, e.getMessage(),
							Logging.PRODUCT_LIST_PRICE, intListPriceModel.getListPriceID());
				}
			}

		}

	}

	/**
	 * process a single intermediate Price Row Model
	 * 
	 * @param interPriceRowModel
	 * @throws BusinessException
	 */
	private void processIntermediatePriceRow(final JnjGTIntListPriceModel interPriceRowModel) throws BusinessException
	{
		final String METHOD_NAME = "processIntermediatePriceRow()";
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<JnjGTIntListPriceAmtModel> listPriceAmountRecords = jnjGTListPriceFeedService
				.getListPriceAmountRecordsByListPriceId(interPriceRowModel.getListPriceID());

		for (final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel : listPriceAmountRecords)
		{
			// fetching list of  JnjGTVariant products on the basis of uom and basecode
			final String srcSystemId = JnjGTInboundUtil.fetchValidSourceSysId(interPriceRowModel.getSrcSysId());
			final String priceUomCode = jnjGTIntListPriceAmountModel.getPriceUomCode();
			//final String productSkuCode = interPriceRowModel.getProductSkuCode().concat(Jnjb2bCoreConstants.SYMBOl_PIPE).concat(priceUomCode);
			final String productSkuCode = interPriceRowModel.getProductSkuCode();
			final Collection<JnjGTVariantProductModel> variantProducts = getjnjGTListPriceFeedService().getProductByUom(
					productSkuCode, priceUomCode, srcSystemId);
			try
			{
				// if list is empty
				if (CollectionUtils.isEmpty(variantProducts))
				{
					lOGGER.error("No Variant products available for the product with code: " + productSkuCode
							+ " | And price UOM code: " + priceUomCode);
					throw new BusinessException(
							"JnjGTProductListPriceMapper : processIntermediatePriceRow : No Variant Products found for productSkuCode-"
									+ productSkuCode + " and Uom code - " + priceUomCode);
				}

				// iterate variant products
				for (final JnjGTVariantProductModel jnjGTVariantProductModel : variantProducts)
				{
					findAllVariants(jnjGTVariantProductModel, interPriceRowModel, jnjGTIntListPriceAmountModel);
				}
			}
			catch (final Exception e)
			{
				lOGGER.error("Not able to find Product for code:" + productSkuCode + " UOM Code:" + priceUomCode, e);
				throw new BusinessException(e.getMessage());
			}
		}
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method is used to find variant products for that product.
	 * 
	 * @param inputVariantProduct
	 * @param interPriceRowModel
	 * @param jnjGTIntListPriceAmountModel
	 */
	private void findAllVariants(final JnjGTVariantProductModel inputVariantProduct,
			final JnjGTIntListPriceModel interPriceRowModel, final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel)
			throws BusinessException
	{
		final String METHOD_NAME = "findAllVariants";
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		//Set price for the variant direct from Int Table 
		updateVariantProductForJnjGTPriceRow(inputVariantProduct, interPriceRowModel, jnjGTIntListPriceAmountModel);

		/** Set the price for other sibling variant according to the variant from Int Price Model : START **/
		final Collection<JnjGTVariantProductModel> jnjGTVariantProductModels = (Collection<JnjGTVariantProductModel>) (Collection<?>) inputVariantProduct
				.getBaseProduct().getVariants();
		for (final JnjGTVariantProductModel jnjGTVariantProduct : jnjGTVariantProductModels)
		{
			if (jnjGTVariantProduct == inputVariantProduct)
			{
				continue;
			}
			final JnjGTIntListPriceAmtModel newJnjGTIntListPriceAmtModel = new JnjGTIntListPriceAmtModel();
			populateIntermediateModel(jnjGTIntListPriceAmountModel, newJnjGTIntListPriceAmtModel, inputVariantProduct,
					jnjGTVariantProduct);
			updateVariantProductForJnjGTPriceRow(jnjGTVariantProduct, interPriceRowModel, newJnjGTIntListPriceAmtModel);
		}
		/** Set the price for other sibling variant according to the variant from Int Price Model : END **/
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method is used to populate intermediate na list price amount model for given intermediate na list price
	 * model. this method is used to update price and and numerator for given list price amount model
	 * 
	 * @param jnjGTIntListPriceAmountModel
	 * @param newJnjGTIntListPriceAmtModel
	 * @param inputVariantProduct
	 * @param jnjGTVariantProduct
	 * @throws BusinessException
	 */
	private void populateIntermediateModel(final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel,
			final JnjGTIntListPriceAmtModel newJnjGTIntListPriceAmtModel, final JnjGTVariantProductModel inputVariantProduct,
			final JnjGTVariantProductModel jnjGTVariantProduct) throws BusinessException
	{
		final Integer inputNumerator = inputVariantProduct.getNumerator();
		final Double listPrice = jnjGTIntListPriceAmountModel.getListPriceAmt();
		final Integer newNumerator = jnjGTVariantProduct.getNumerator();
		newJnjGTIntListPriceAmtModel.setCurrencyCode(jnjGTIntListPriceAmountModel.getCurrencyCode());
		Double updatedListPrice = null;
		try
		{
			updatedListPrice = Double.valueOf((listPrice.doubleValue() / inputNumerator.doubleValue()) * newNumerator.doubleValue());
		}
		catch (final Throwable throwable)
		{
			throw new BusinessException(
					"JnjGTProductListPriceMapper : JnjGTIntListPriceAmtModel : list price value can not be null so throwing business exception :"
							+ jnjGTIntListPriceAmountModel.getListPriceAmt() + " and Uom code - "
							+ jnjGTIntListPriceAmountModel.getPriceUomCode());

		}

		if (null != jnjGTVariantProduct.getUnit())
		{
			newJnjGTIntListPriceAmtModel.setPriceUomCode(jnjGTVariantProduct.getUnit().getCode());
		}
		else
		{
			newJnjGTIntListPriceAmtModel.setPriceUomCode(jnjGTIntListPriceAmountModel.getPriceUomCode());
		}

		newJnjGTIntListPriceAmtModel.setListPriceAmt(updatedListPrice);

	}

	/**
	 * process the JnjGTVariantProductModel for JnjIntListPriceModel fetch all price rows associated with
	 * JnjGTVariantProductModel if the passed JnjIntListPriceModel already exists in collection update it otherwise add
	 * it.
	 * 
	 * @param jnjGTVariantProductModel
	 */
	private void updateVariantProductForJnjGTPriceRow(final JnjGTVariantProductModel jnjGTVariantProductModel,
			final JnjGTIntListPriceModel interPriceRowModel, final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel)
			throws BusinessException
	{
		final String METHOD_NAME = "updateVariantProductForJnjGTPriceRow";
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		// fetching the existing price row models
		final Collection<PriceRowModel> existingPriceRows = jnjGTVariantProductModel.getEurope1Prices();

		lOGGER.info("Number of Existing Price rows:" + (existingPriceRows != null ? existingPriceRows.size() : 0));
		// if there are no existing price rows

		/*** Flag to indicate if the base product is Mitek based or not. ***/
		final JnJProductModel baseProduct = (JnJProductModel) jnjGTVariantProductModel.getBaseProduct();
		final boolean isMitekproduct = (baseProduct.getSalesOrgCode() != null && baseProduct.getSalesOrgCode().equals(
				JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK))) ? true : false;

		if (CollectionUtils.isEmpty(existingPriceRows))
		{
			lOGGER.info("Creating New Price row for Product:" + jnjGTVariantProductModel.getCode());
			// creating new collection of JnjGTPriceRowModel and setting in JnJGTVariantProductModel
			final JnJGTPriceRowModel newPriceRowModel = createAndPopulateNewJnjGTPriceRowModel(interPriceRowModel,
					jnjGTIntListPriceAmountModel, jnjGTVariantProductModel, isMitekproduct);

			final Set<PriceRowModel> priceRowSet = new HashSet<PriceRowModel>(1);
			priceRowSet.add(newPriceRowModel);

			jnjGTVariantProductModel.setEurope1Prices(priceRowSet);
			// update Variant product model
			getjnjGTListPriceFeedService().saveItem(newPriceRowModel);
		}
		else
		{
			lOGGER.info("Adding New Price row for Product:" + jnjGTVariantProductModel.getCode());
			//since existingPriceRows collection was read only, creating new collection for updating changes.
			final Collection<PriceRowModel> latestPriceRowCollection = new HashSet<>();
			latestPriceRowCollection.addAll(existingPriceRows);

			boolean found = false;
			boolean isDelete = false;
			// iterating existing price rows and update matching Price Rows.
			for (final PriceRowModel priceRowModel : latestPriceRowCollection)
			{
				//typecast into JnJGTPriceRowModel
				if (priceRowModel instanceof JnJGTPriceRowModel)
				{
					final JnJGTPriceRowModel existingPriceRow = (JnJGTPriceRowModel) priceRowModel;
					// checking if there is a existing JnjGTPriceRowModel for intListPriceModel.
					if (existingPriceRow.getPriceUomCode().equals(jnjGTIntListPriceAmountModel.getPriceUomCode())
							&& existingPriceRow.getListPriceId().equals(interPriceRowModel.getListPriceID())
							&& jnjGTIntListPriceAmountModel.getCurrencyCode().equals(existingPriceRow.getCurrency().getIsocode()))
					{
						// Set PriceRow for MDD
						if (isMDD(interPriceRowModel.getSrcSysId()))
						{ //checking UPG (combination of price book and price type code)
							lOGGER.info("Updating New Price row for source system id MDD:" + jnjGTVariantProductModel.getCode());
							if (existingPriceRow.getPriceTypeCode().equals(interPriceRowModel.getPriceTypeCode())
									&& (StringUtils.isEmpty(existingPriceRow.getPriceBook()) || existingPriceRow.getPriceBook().equals(
											interPriceRowModel.getPriceBook())))
							{
								//if exists updating price row model
								found = true;
								updateExistingJnjGTPriceRowModel(existingPriceRow, interPriceRowModel, jnjGTIntListPriceAmountModel,
										isMitekproduct);
								break;
							}
						}
						//Set PriceRow for Consumer
						else
						{
							lOGGER.info("Updating New Price row for source system id CONSUMER:" + jnjGTVariantProductModel.getCode());
							// checks if the sales org, division and valid-to are same 
							if ((existingPriceRow.getDateRange().getEnd() != null && DateUtils.isSameDay(existingPriceRow.getDateRange()
									.getEnd(), interPriceRowModel.getValidTo()))
									&& ((JnJProductModel) ((JnjGTVariantProductModel) existingPriceRow.getProduct()).getBaseProduct())
											.getSalesOrg().contains(interPriceRowModel.getSalesOrg()))
							{
								lOGGER.info("Date range and Other conditon true for Consumer:" + jnjGTVariantProductModel.getCode());
								//if exists updating price row model
								found = true;
								isDelete = jnjGTIntListPriceAmountModel.getDeleteIndicator().booleanValue();
								// if the data needs to be deleted then no need to update
								if (!isDelete)
								{
									updateExistingJnjGTPriceRowModel(existingPriceRow, interPriceRowModel, jnjGTIntListPriceAmountModel,
											isMitekproduct);
								}
								break;
							}
						}
					}

				}
				// remove the price row if delete indicator is set true
				if (isDelete)
				{
					lOGGER.info("Deleting Price Row Model with Price: " + priceRowModel.getPrice() + "for variant product with code: "
							+ jnjGTVariantProductModel.getCode());
					latestPriceRowCollection.remove(priceRowModel);
				}
			}
			// if newPriceRow is not found in existing JnjGTPriceRows will be adding to existing list
			if (!found)
			{
				lOGGER.info("Adding new Price row| " + interPriceRowModel.getListPriceID() + " | in existing list price collection.");
				final JnJGTPriceRowModel newJnjGTPriceRowModel = createAndPopulateNewJnjGTPriceRowModel(interPriceRowModel,
						jnjGTIntListPriceAmountModel, jnjGTVariantProductModel, isMitekproduct);
				latestPriceRowCollection.add(newJnjGTPriceRowModel);
			}
			// updating JnJGTVariantProdcutModel with existing + new PriceRow Models.
			jnjGTVariantProductModel.setEurope1Prices(latestPriceRowCollection);
			getjnjGTListPriceFeedService().saveItem(jnjGTVariantProductModel);
		}
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * crate a new JnJGTpriceRowModel copy all the fields from intermediate price row model to JnjGTPriceRowModel
	 * 
	 * @param intListPriceModel
	 * @return JnJGTPriceRowModel
	 * @throws BusinessException
	 */

	private JnJGTPriceRowModel createAndPopulateNewJnjGTPriceRowModel(final JnjGTIntListPriceModel intListPriceModel,
			final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel, final JnjGTVariantProductModel jnjGTVariantProductModel,
			final boolean isMitekproduct) throws BusinessException
	{

		final String METHOD_NAME = "createAndPopulateNewJnjGTPriceRowModel";
		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnJGTPriceRowModel newJnjGTPriceRowModel = (JnJGTPriceRowModel) getjnjGTListPriceFeedService().createNewItem(
				JnJGTPriceRowModel.class);
		final CatalogVersionModel stgCatalogVersion = jnjGTListPriceFeedService.getStgCatalogVersionByProduct(intListPriceModel
				.getSrcSysId());

		newJnjGTPriceRowModel.setPriceUomCode(jnjGTIntListPriceAmountModel.getPriceUomCode());
		newJnjGTPriceRowModel.setDivision(intListPriceModel.getDivision());
		newJnjGTPriceRowModel.setPriceBook(intListPriceModel.getPriceBook());
		newJnjGTPriceRowModel.setMdmCustomerGroup(intListPriceModel.getCustomerGroup());
		newJnjGTPriceRowModel.setPriceTypeCode(intListPriceModel.getPriceTypeCode());
		final UnitModel salesUOM = new UnitModel();
		salesUOM.setCode(jnjGTIntListPriceAmountModel.getPriceUomCode());
		newJnjGTPriceRowModel.setUnit(flexibleSearchService.getModelByExample(salesUOM));
		newJnjGTPriceRowModel.setListPriceId(intListPriceModel.getListPriceID());
		newJnjGTPriceRowModel.setCatalogVersion(stgCatalogVersion);
		newJnjGTPriceRowModel.setProduct(jnjGTVariantProductModel);
		newJnjGTPriceRowModel.setDistChannel(intListPriceModel.getDistChannel());
		final CurrencyModel currencyModel = getjnjGTListPriceFeedService().createNewItem(CurrencyModel.class);
		currencyModel.setIsocode(jnjGTIntListPriceAmountModel.getCurrencyCode());
		newJnjGTPriceRowModel.setCurrency(flexibleSearchService.getModelByExample(currencyModel));
		//rest of the details will be populated same as it was for existing JnjGTPriceRow model. 
		newJnjGTPriceRowModel = updateExistingJnjGTPriceRowModel(newJnjGTPriceRowModel, intListPriceModel,
				jnjGTIntListPriceAmountModel, isMitekproduct);
		try
		{
			modelService.save(newJnjGTPriceRowModel);
		}
		catch (final ModelSavingException exception)
		{
			throw new BusinessException("Exeption while saving New Price Row Model with values: | List Price ID: "
					+ intListPriceModel.getListPriceID() + "Price Type Code: " + intListPriceModel.getPriceTypeCode()
					+ " | Exception: " + exception.getMessage());
		}

		if (lOGGER.isDebugEnabled())
		{
			lOGGER.debug(METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return newJnjGTPriceRowModel;
	}

	/**
	 * update JnjGTPriceRowModel form intermediate price row model update non composite attributes only.
	 * 
	 * @param priceRow
	 *           the price row
	 * @param newJnaIntPriceModel
	 *           the new jna int price model
	 * @param jnjGTIntListPriceAmountModel
	 *           the jnj na int list price amount model
	 * @return the jn jna price row model
	 * @throws BusinessException
	 *            the business exception
	 */

	private JnJGTPriceRowModel updateExistingJnjGTPriceRowModel(final JnJGTPriceRowModel priceRow,
			final JnjGTIntListPriceModel newJnaIntPriceModel, final JnjGTIntListPriceAmtModel jnjGTIntListPriceAmountModel,
			final boolean isMitekproduct) throws BusinessException
	{ // if the price lists is stg catalog version 
		if (priceRow
				.getCatalogVersion()
				.getPk()
				.toString()
				.equals(jnjGTListPriceFeedService.getStgCatalogVersionByProduct(newJnaIntPriceModel.getSrcSysId()).getPk().toString()))
		{
			priceRow.setStartTime(newJnaIntPriceModel.getValidFrom());
			priceRow.setEndTime(newJnaIntPriceModel.getValidTo());
			priceRow.setPrice(jnjGTIntListPriceAmountModel.getListPriceAmt());
			priceRow.setCondRecNum(newJnaIntPriceModel.getListPriceID()); //was getCondRecNum()
			setUgForJnjGTVariantProduct(priceRow, newJnaIntPriceModel, isMitekproduct);
			priceRow.setDivision(newJnaIntPriceModel.getDivision());
			priceRow.setPriceBook(newJnaIntPriceModel.getPriceBook());
			priceRow.setMdmCustomerGroup(newJnaIntPriceModel.getCustomerGroup());
			priceRow.setDistChannel(newJnaIntPriceModel.getDistChannel());
		}
		return priceRow;

	}

	/**
	 * set UG on the basis of defined logic
	 * 
	 * @param priceRow
	 * @param newJnaIntPriceModel
	 */

	@Autowired
	private JnjConfigService jnjConfigService;

	private void setUgForJnjGTVariantProduct(final JnJGTPriceRowModel priceRow, final JnjGTIntListPriceModel newJnaIntPriceModel,
			final boolean isMitekproduct) throws BusinessException
	{
		String userPriceGroup = null;
		// if MDD , UG will be fetched on list price id
		if (isMDD(newJnaIntPriceModel.getSrcSysId()))
		{
			/** Get Mapping for price group code in config table and throw exception in case mapping not exists : START **/

			final List<JnjConfigModel> configList = jnjConfigService.getConfigModelsByIdAndKey(priceRow.getPriceTypeCode(),
					priceRow.getPriceTypeCode());

			if (CollectionUtils.isNotEmpty(configList))
			{
				userPriceGroup = configList.get(0).getValue();
			}
			else
			{
				throw new BusinessException("Mapping for user Group does not exist in the config table" + priceRow.getPriceTypeCode());
			}

			/*** Concatenate Price Book to the user price group obtained above ONLY if it's a Mitek product. ***/
			if (isMitekproduct && StringUtils.isNotEmpty(priceRow.getPriceBook()))
			{
				userPriceGroup = userPriceGroup.concat(Jnjgtb2binboundserviceConstants.PIPE_STRING).concat(priceRow.getPriceBook());
			}
		}
		// if Consumer, Ug will decided on (price book and cg)
		else
		{ // for COUNSUMER
			userPriceGroup = newJnaIntPriceModel.getPriceBook();
			final String customerGrp = newJnaIntPriceModel.getCustomerGroup();

			if (StringUtils.isEmpty(userPriceGroup)
					|| JnJCommonUtil.getValue(Jnjgtb2binboundserviceConstants.PRICE_LIST_VALUE).equalsIgnoreCase(userPriceGroup))
			{
				userPriceGroup = JnJCommonUtil.getValue(Jnjgtb2binboundserviceConstants.PRICE_LIST_VALUE);
				if (StringUtils.isNotEmpty(customerGrp))
				{
					userPriceGroup = userPriceGroup.concat(Jnjgtb2binboundserviceConstants.PIPE_STRING).concat(customerGrp);
				}
			}

			if (StringUtils.isNotEmpty(userPriceGroup))
			{
				priceRow.setUg(UserPriceGroup.valueOf(userPriceGroup));
			}
		}
	}

	/**
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}


	/**
	 * @param jnjGTFeedService
	 *           the jnjGTFeedService to set
	 */
	public void setjnjGTFeedService(final JnjGTFeedService jnjGTFeedService)
	{
		this.jnjGTFeedService = jnjGTFeedService;
	}


	/**
	 * @return the jnjGTListPriceFeedService
	 */
	public JnjGTListPriceFeedService getjnjGTListPriceFeedService()
	{
		return jnjGTListPriceFeedService;
	}


	/**
	 * @param jnjGTListPriceFeedService
	 *           the jnjGTListPriceFeedService to set
	 */
	public void setjnjGTListPriceFeedService(final JnjGTListPriceFeedService jnjGTListPriceFeedService)
	{
		this.jnjGTListPriceFeedService = jnjGTListPriceFeedService;
	}

	public boolean isMDD(final String srcSystemID)
	{
		return JnjGTSourceSysId.MDD.toString().equals(JnjGTInboundUtil.fetchValidSourceSysId(srcSystemID));
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
