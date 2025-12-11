/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.product.converters.populator;

import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.data.JnjGTConsumerSpecificationData;
import com.jnj.facades.data.JnjGTMddSpecificationData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProductKitData;
import com.jnj.facades.data.JnjGTShippingUomData;
import com.jnj.facades.data.JnjGTUomData;
import com.jnj.core.model.JnJGTProductKitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.services.MessageService;


/**
 * @author Accenture
 * @version 1.0
 * 
 *          Class responsible to convert and set data from source instance of <code>JnJProductModel</code> to the
 *          target instance of <code>JnjGTProductData</code>.
 * 
 */
public class JnjGTProductBasicPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		ProductBasicPopulator<SOURCE, TARGET>
{

	@Autowired
	protected SessionService sessionService;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	protected MessageService messageService;

	protected Populator<ProductModel, ProductData> productPricePopulator;
	
	/* KIT ADDED */
	private Populator<JnJGTProductKitModel, ProductData> jnjGTProductKitPopulator;

	public Populator<JnJGTProductKitModel, ProductData> getJnjGTProductKitPopulator() {
		return jnjGTProductKitPopulator;
	}

	public void setJnjGTProductKitPopulator(
			Populator<JnJGTProductKitModel, ProductData> jnjGTProductKitPopulator) {
		this.jnjGTProductKitPopulator = jnjGTProductKitPopulator;
	}
	 /*KIT ADDED */ 
	public enum SITE_NAME
	{
		MDD, CONS, PCM;
	}

	/**
	 * Constant value of status for Consumer Product.
	 */
	protected static final String CONS_PROD_STATUS = "DEA Regulated";

	final String emptyField = new String();
	protected static final Logger LOG = Logger.getLogger(JnjGTProductBasicPopulator.class);
	protected static final String CASE = "CS";
	protected static final String EACH = "EA";
	protected static final String INNER_PACK = "IP";
	protected static final String PALLET = "PAL";
	protected static final String TIER = "TR";

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		super.populate(productModel, productData);

		if (productData instanceof JnjGTProductData && productModel instanceof JnJProductModel)
		{
			populateJnjProductSpecification(productModel, productData);
		}
	}

	/**
	 * Populates and convert data from source instance of <code>JnJProductModel</code> to the target instance of
	 * <code>JnjGTProductData</code>.
	 * 
	 * @param productModel
	 *           <code>SOURCE</code>
	 * @param productData
	 *           <code>TARGET</code>
	 */
	protected void populateJnjProductSpecification(final SOURCE productModel, final TARGET productData)
	{
		JnJProductModel source = null;
		final JnjGTProductData target = (JnjGTProductData) productData;

		/**
		 * Set basic info and other generic details in target from the Material Base product, or if absent from product
		 * itself.
		 **/
		if (productModel instanceof JnjGTVariantProductModel)
		{
			final JnjGTVariantProductModel variant = (JnjGTVariantProductModel) productModel;
			this.setBasicValuesForVariant(variant, target);
			getProductPricePopulator().populate(variant, target);
		}
		else
		{
			source = (JnJProductModel) productModel;
			JnJProductModel baseMaterialProduct = source.getMaterialBaseProduct();

			if (baseMaterialProduct == null)
			{
				baseMaterialProduct = source;
			}
			setBasicValuesForProduct(baseMaterialProduct, target);
			target.setCode(source.getCode());
			target.setSalesOrgCode(source.getSalesOrgCode());
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			target.setSiteName(currentSite);

			if (SITE_NAME.MDD.toString().equals(currentSite))
			{
				populatorEpicData(source, target, currentSite);
				populateMddSpecification(target, source);
			}
			else if (SITE_NAME.CONS.toString().equals(currentSite))
			{
				populatorEpicData(source, target, currentSite);
				populateConsumerEpicSpecification(target, source, true);

				/*** For Consumer products, override and set name with value of base products' name updated by PCM ***/
				if (StringUtils.isNotEmpty(baseMaterialProduct.getName()))
				{
					target.setName(baseMaterialProduct.getName());
				}
				else if (StringUtils.isNotEmpty(source.getMdmDescription()))
				{
					target.setName(source.getMdmDescription());
				}
			}
			else if (SITE_NAME.PCM.toString().equals(currentSite))
			{
				populateConsumerEpicSpecification(target, source, false);
				populateConsumerPcmSpecification(baseMaterialProduct, target, source);
			}
		}
	}

	/**
	 * @param source
	 * @param target
	 * @param currentSite
	 */
	protected void populatorEpicData(final JnJProductModel source, final JnjGTProductData target, final String currentSite)
	{
		/** Saleable check **/
		String saleableStatus = jnJGTProductService.isProductSaleable(source, currentSite);
		/** If Saleable Status message not null, set the Saleable Ind False. **/
		if (saleableStatus != null)
		{
			target.setSaleableInd(Boolean.FALSE);
		}
		else
		{
			target.setSaleableInd(Boolean.TRUE);
			/** Else, check for Sales Rep user and if it is then its division compatibility with thta of product. **/
			final Boolean isSalesRepUser = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);
			if (Jnjb2bCoreConstants.MDD.equals(currentSite) && isSalesRepUser != null && isSalesRepUser.booleanValue()
					&& !jnJGTProductService.isProductDivisionSameAsUserDivision(source))
			{
				saleableStatus = messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null, source.getCode());
				target.setSalesRepCompatibleInd(Boolean.FALSE);
			}
			else
			{
				target.setSalesRepCompatibleInd(Boolean.TRUE);
			}
		}
		target.setErrorMessage(saleableStatus);
		target.setIsProdViewable(jnJGTProductService.isProductBrowsable(source));
		target.setHazmatCode(source.getHazmatCode());
	}

	/**
	 * To set basic values for variant
	 * 
	 * @param variant
	 * @param productData
	 */
	protected void setBasicValuesForVariant(final JnjGTVariantProductModel variant, final JnjGTProductData productData)
	{
		final ProductModel product = variant.getBaseProduct();
		productData.setCode(variant.getCode());
		productData.setDescription(product.getDescription());
		productData.setSummary(product.getSummary());
		productData.setName(product.getName());
		productData.setBaseMaterialNumber(product.getCode());
		productData.setGtin(variant.getEan());
		productData.setUpc(variant.getUpc());
	}


	/**
	 * To set basic values for Product
	 * 
	 * @param product
	 * @param productData
	 */
	protected void setBasicValuesForProduct(final ProductModel product, final JnjGTProductData productData)
	{
		productData.setDescription(product.getDescription());
		productData.setSummary(product.getSummary());
		if (StringUtils.isNotBlank(product.getName()))
		{
			productData.setDisplayName(product.getName().replaceAll("'", "&rsquo;"));
		}
		productData.setBaseMaterialNumber(product.getCode());

	}

	/**
	 * Populates on converts MDD product specific data into the target instance.
	 * 
	 * @param target
	 * @param source
	 */
	protected void populateMddSpecification(final JnjGTProductData target, final JnJProductModel source)
	{
		final JnjGTUomData deliveryUomData = new JnjGTUomData();
		final JnjGTUomData salesUomData = new JnjGTUomData();
		final JnjGTMddSpecificationData mddSpecificationData = new JnjGTMddSpecificationData();
	/* KIT ADDED*/ 
		final List<JnjGTProductKitData> kitProductModelsList = new ArrayList<JnjGTProductKitData>();
		final JnjGTProductKitData kitData = new JnjGTProductKitData();
		/* KIT ADDED*/
		target.setStatus((source.getModStatus() != null) ? source.getModStatus().toString() : emptyField);
		setFranchiseAndDivision(source, mddSpecificationData);

		/** If the variant is Delivery GTIN and is valid as per the date, set all Delivery GTIN dependent info in target. **/
		for (final VariantProductModel variant : source.getVariants())
		{
			if (variant instanceof JnjGTVariantProductModel)
			{
				final JnjGTVariantProductModel jnjGTVariant = (JnjGTVariantProductModel) variant;

				if (jnjGTVariant.getDeliveryGtinInd() != null && jnjGTVariant.getDeliveryGtinInd().booleanValue())
				{
					//Since it's delivery GTIN, set product level and Case GTIN with the same value.
					final String gtin = jnjGTVariant.getEan();
					target.setGtin(gtin);
					mddSpecificationData.setCaseGtin(gtin);

					//Populates other MDD specification Uom data.
					populateUomData(deliveryUomData, jnjGTVariant);
					getProductPricePopulator().populate(variant, target);
				}
				else if (jnjGTVariant.getSalesGtinInd() != null && jnjGTVariant.getSalesGtinInd().booleanValue())
				{
					mddSpecificationData.setEachGtin(jnjGTVariant.getEan());
					populateUomData(salesUomData, jnjGTVariant);
				}

			}
		}
		/* KIT ADDED*/
		if(source.getKitType() != null){
		target.setKitType(source.getKitType().getCode());
		}
		/* KIT ADDED*/
		
		populateKitInformation(mddSpecificationData, source);
		mddSpecificationData.setDeliveryUom(deliveryUomData);
		mddSpecificationData.setSalesUom(salesUomData);
		mddSpecificationData.setBatchMgmt(source.getBatchMgmt());
		mddSpecificationData.setInFieldInd(source.getInFieldInd());
		target.setMddSpecification(mddSpecificationData);
		target.setIsKitProduct(source.getKitInd());
		
	}

	/**
	 * Sets the Franchise and Division values for a MDD product, based on the Level 2 and level 1 super categories
	 * associated with the MDD product.
	 * 
	 * @param source
	 * @param mddspecificationData
	 */
	protected void setFranchiseAndDivision(final JnJProductModel source, final JnjGTMddSpecificationData mddspecificationData)
	{
		for (final CategoryModel levelTwoCategory : source.getSupercategories())
		{
			mddspecificationData.setDivision(levelTwoCategory.getName());
			for (final CategoryModel levelOneCategory : levelTwoCategory.getSupercategories())
			{
				mddspecificationData.setFranchise(levelOneCategory.getName());
				break;
			}
			break;
		}
	}

	/**
	 * Populates <code>JnjGTUomData</code> for a MDD Specification: Sales and Delivery UOM.
	 * 
	 * @param uomData
	 * @param jnjGTVariantModel
	 */
	protected void populateUomData(final JnjGTUomData uomData, final JnjGTVariantProductModel jnjGTVariantModel)
	{
		final UnitModel unit = jnjGTVariantModel.getUnit();
		if (unit != null)
		{
			uomData.setCode(unit.getCode());
			uomData.setName(unit.getName());
		}

		uomData.setGtin((jnjGTVariantModel.getEan() != null) ? jnjGTVariantModel.getEan() : emptyField);
		uomData.setNumerator(jnjGTVariantModel.getNumerator());
		uomData.setLwrPackagingLvlUomCode((jnjGTVariantModel.getLwrPackagingLvlUom() != null) ? jnjGTVariantModel
				.getLwrPackagingLvlUom().getName() : emptyField);
		uomData.setPackagingLevelQty(jnjGTVariantModel.getPackagingLevelQty());

		uomData.setDepth((jnjGTVariantModel.getDepth() == null) ? emptyField : jnjGTVariantModel.getDepth().toString());
		uomData.setWeight((jnjGTVariantModel.getWeightQty() == null) ? emptyField : jnjGTVariantModel.getWeightQty().toString());
		uomData.setWidth((jnjGTVariantModel.getWidth() == null) ? emptyField : jnjGTVariantModel.getWidth().toString());
		uomData.setHeight((jnjGTVariantModel.getHeight() == null) ? emptyField : jnjGTVariantModel.getHeight().toString());
		uomData.setVolume((jnjGTVariantModel.getVolumeQty() == null) ? emptyField : jnjGTVariantModel.getVolumeQty().toString());

		/*** Select Linear UOM Name, if not present then its code. ***/
		final String linearUomName = (jnjGTVariantModel.getLinearUom() != null) ? ((jnjGTVariantModel.getLinearUom().getName() != null) ? jnjGTVariantModel
				.getLinearUom().getName() : jnjGTVariantModel.getLinearUom().getCode())
				: emptyField;
		uomData.setHeightLinearUom(linearUomName);
		uomData.setDepthLinearUom(linearUomName);
		uomData.setWidthLinearUom(linearUomName);
		uomData
				.setWeightLinearUom((jnjGTVariantModel.getWeightUom() != null) ? ((jnjGTVariantModel.getWeightUom().getName() != null) ? jnjGTVariantModel
						.getWeightUom().getName() : jnjGTVariantModel.getWeightUom().getCode())
						: emptyField);
		uomData
				.setVolumeLinearUom((jnjGTVariantModel.getVolumeUom() != null) ? ((jnjGTVariantModel.getVolumeUom().getName() != null) ? jnjGTVariantModel
						.getVolumeUom().getName() : jnjGTVariantModel.getVolumeUom().getCode())
						: emptyField);
	}

	/**
	 * Populates Kit information for a MDD product based on the component code from assoacited
	 * <code>JnJGTProductKitModel</code>.
	 * 
	 * @param mddSpecification
	 * @param source
	 */
	protected void populateKitInformation(final JnjGTMddSpecificationData mddSpecification, final JnJProductModel source)
	{
		
		List<ProductData> productKitDataList = new ArrayList<ProductData>();
		final Collection<JnJGTProductKitModel> productKitModels = source.getProductKits();
		if (!CollectionUtils.isEmpty(productKitModels))
		{
			final List<String> kitComponentDescriptions = new ArrayList<>();
			final List<String> kitComponentNames = new ArrayList<>();
			final List<String> kitComponentUnits = new ArrayList<>();

			String componentCode = null;
			for (final JnJGTProductKitModel productKitModel : productKitModels)
			{
				/* KIT ADDED*/
				ProductData productKitdata = new JnjGTProductKitData();
				jnjGTProductKitPopulator.populate(productKitModel,productKitdata);
				productKitDataList.add(productKitdata);
				/* KIT ADDED*/
				componentCode = productKitModel.getComponentCode();
				if (StringUtils.isNotEmpty(componentCode))
				{

					final ProductModel deliveryVariant = jnJGTProductService.getMDDDeliveryVariantByProdCode(componentCode);
					if (null != deliveryVariant)
					{
						final ProductModel product = ((VariantProductModel) deliveryVariant).getBaseProduct();

						kitComponentUnits.add((deliveryVariant.getUnit() == null) ? null : deliveryVariant.getUnit().getName());
						kitComponentDescriptions.add(product.getDescription());
						kitComponentNames.add(product.getName());
					}

				}
			}
			/* KIT ADDED*/
			mddSpecification.setKitData(productKitDataList);
			mddSpecification.setKitComponentDesc(kitComponentDescriptions);
			mddSpecification.setKitComponentNames(kitComponentNames);
			mddSpecification.setKitComponentUnits(kitComponentUnits);
		}
	}

	/**
	 * Populates on converts Consumer product specific data <code>JnjGTConsumerSpecificationData</code> into the target
	 * instance <code>JnjGTProductData</code>.
	 * 
	 * @param target
	 * @param source
	 */
	protected void populateConsumerEpicSpecification(final JnjGTProductData target, final JnJProductModel source,
			final boolean siteFlag)
	{
		String unitName = null, unitCode = null;
		int caseUomNumerator = Integer.MIN_VALUE;
		int innerPackUomNumerator = Integer.MIN_VALUE;
		int innerPacksPerCase = 0;

		final JnjGTConsumerSpecificationData data = new JnjGTConsumerSpecificationData();
		data.setFranchise(source.getFranchiseName());
		//data.setBrand(source.getPcmBrand());
		data.setOriginCountry((source.getOriginCountry() == null) ? emptyField : source.getOriginCountry().getName());
		data.setAssemblyCountry((source.getAssemblyCountry() == null) ? emptyField : source.getAssemblyCountry().getName());
		target.setStatus(BooleanUtils.isTrue(source.getDeaRegInd()) ? CONS_PROD_STATUS : emptyField);
		target.setUpc(source.getUpcCode());

		final JnjGTShippingUomData shippingInfo = new JnjGTShippingUomData();
		data.setShippingInfo(shippingInfo);
		shippingInfo.setShipEffectiveDate(source.getFirstShipEffectDate());
		PriceData deliveryGtinPrice = null;
		for (final VariantProductModel variant : source.getVariants())
		{
			if (variant instanceof JnjGTVariantProductModel)
			{
				final JnjGTVariantProductModel jnjGTVariant = (JnjGTVariantProductModel) variant;

				if (jnjGTVariant.getUnit() != null)
				{
					unitName = jnjGTVariant.getUnit().getName();
					unitCode = jnjGTVariant.getUnit().getCode();
				}
				if (jnjGTVariant.getDeliveryGtinInd() != null && jnjGTVariant.getDeliveryGtinInd().booleanValue())
				{
					shippingInfo.setShippingUom(unitName);
					shippingInfo.setNumerator(jnjGTVariant.getNumerator());
					shippingInfo.setLwrPackagingLvlUomCode((jnjGTVariant.getLwrPackagingLvlUom() != null) ? jnjGTVariant
							.getLwrPackagingLvlUom().getName() : emptyField);
					if (siteFlag)
					{
						getProductPricePopulator().populate(variant, target);
						deliveryGtinPrice = target.getPrice();
					}
				}

				switch (unitCode)
				{
					case CASE:
						caseUomNumerator = jnjGTVariant.getNumerator().intValue();
						shippingInfo.setEaPerCase(caseUomNumerator);
						data.setCaseInfo(getUomData(jnjGTVariant));
						data.setOuterCaseCode(jnjGTVariant.getOuterCaseCode());
						if (siteFlag)
						{
							getProductPricePopulator().populate(jnjGTVariant, target);
							data.setCasePrice(target.getPrice());
							target.setPrice(deliveryGtinPrice);
						}
						break;

					case EACH:
						data.setEachesInfo(getUomData(jnjGTVariant));
						if (siteFlag)
						{
							getProductPricePopulator().populate(jnjGTVariant, target);
							data.setEachPrice(target.getPrice());
							target.setPrice(deliveryGtinPrice);
						}
						break;

					case INNER_PACK:
						innerPackUomNumerator = jnjGTVariant.getNumerator();
						shippingInfo.setEaPerInnerPacks(innerPackUomNumerator);
						break;

					case PALLET:
						shippingInfo.setTrPerPallet(jnjGTVariant.getTiersPerPallet());
						break;

					case TIER:
						shippingInfo.setCsPerTier(jnjGTVariant.getCasesPerTier());
						break;

					default:
						break;
				}

				innerPacksPerCase = caseUomNumerator / innerPackUomNumerator;
				shippingInfo.setIpPerCAse(Integer.valueOf(innerPacksPerCase));
			}
		}
		target.setConsumerSpecification(data);
	}

	protected void populateConsumerPcmSpecification(final JnJProductModel baseMaterialProduct, final JnjGTProductData target,
			final JnJProductModel source)
	{
		final JnjGTConsumerSpecificationData data = target.getConsumerSpecification();

		final List<String> keywordsList = new ArrayList<>();
		final List<KeywordModel> keywords = baseMaterialProduct.getKeywords();
		for (final KeywordModel keywordModel : keywords)
		{
			keywordsList.add(keywordModel.getKeyword());
		}
		data.setKeywords(keywordsList);
		//data.setGlobalBusinessUnit(source.getGlobalBusinessUnit());
		data.setSourceSystemId(source.getSourceSystemId());
		data.setMasterDataLastUp(source.getRecordTimeStamp());
		/*if (source.getDeaRegInd() != null)
		{
			data.setDeaRegulated((source.getDeaRegInd().booleanValue() ? JnjPCMCoreConstants.PLP.TRUE
					: JnjPCMCoreConstants.PLP.FALSE));
		}
		data.setGlobalProductCode(source.getGlobalProductCode());
		data.setShortBullets(baseMaterialProduct.getShortBullets());
		data.setLongBullets(baseMaterialProduct.getLongBullet());
		data.setIngredients(baseMaterialProduct.getIngredient());
		data.setIndications(baseMaterialProduct.getIndication());
		data.setDirections(baseMaterialProduct.getDirection());
		data.setWarnings(baseMaterialProduct.getWarning());
		data.setArtworkApprovalDate(baseMaterialProduct.getArtworkApprovalDate());
		data.setCopyApprovalDate(baseMaterialProduct.getCopyApprovalDate());
		data.setCopyApprovalReference(baseMaterialProduct.getCopyApprovalReference());
		data.setRmcNo(baseMaterialProduct.getRmcNo());
		data.setArtworkApprovalCrr(baseMaterialProduct.getArtworkApprovalCrr());
		data.setImageLastUpdateDate(source.getImageLastUpdateDate());
		target.setStatus(source.getPcmModStatus().getCode());
		data.setItemSizeCount(source.getNetContent());

		data.setBrand(baseMaterialProduct.getPcmBrand());
		data.setFranchise(baseMaterialProduct.getPcmSubBrand());*/
		data.setLaunchStatus(jnJGTProductService.getLaunchStatus(source, Boolean.TRUE));
		final Date prodDate = (source.getCatalogVersion().getCatalog().getId()
				.equals(Jnjb2bCoreConstants.CONSUMER_CA_PRODUCT_CATALOG_ID)) ? source.getFirstShipEffectDate() : source
				.getNewProductStartDate();
		target.setFirstShipEffective(prodDate);
	}

	/**
	 * Populates and returns <code>JnjGTUomData</code> for Consumer Specification: Case Info and Eaches Info.
	 * 
	 * @param jnjGTVariant
	 * @return JnjGTUomData
	 */
	protected JnjGTUomData getUomData(final JnjGTVariantProductModel jnjGTVariant)
	{
		final JnjGTUomData data = new JnjGTUomData();

		final UnitModel unit = jnjGTVariant.getUnit();
		if (unit != null)
		{
			data.setCode(jnjGTVariant.getUnit().getCode());
			data.setName(unit.getName());
		}

		data.setGtin(jnjGTVariant.getEan());

		data.setDepth((jnjGTVariant.getDepth() == null) ? null : jnjGTVariant.getDepth().toString());
		data.setWeight((jnjGTVariant.getWeightQty() == null) ? null : jnjGTVariant.getWeightQty().toString());
		data.setWidth((jnjGTVariant.getWidth() == null) ? null : jnjGTVariant.getWidth().toString());
		data.setHeight((jnjGTVariant.getHeight() == null) ? null : jnjGTVariant.getHeight().toString());
		data.setVolume((jnjGTVariant.getVolumeQty() == null) ? null : jnjGTVariant.getVolumeQty().toString());

		/*** Select Linear UOM Name, if not present then its code. ***/
		final String linearUomName = (jnjGTVariant.getLinearUom() != null) ? ((jnjGTVariant.getLinearUom().getName() != null) ? jnjGTVariant
				.getLinearUom().getName() : jnjGTVariant.getLinearUom().getCode())
				: emptyField;
		data.setHeightLinearUom(linearUomName);
		data.setDepthLinearUom(linearUomName);
		data.setWidthLinearUom(linearUomName);
		data.setWeightLinearUom((jnjGTVariant.getWeightUom() != null) ? jnjGTVariant.getWeightUom().getCode() : emptyField);
		if (jnjGTVariant.getVolumeUom() != null)
		{
			if (StringUtils.length(jnjGTVariant.getVolumeUom().getCode()) > 1)
			{
				try
				{
					Integer.parseInt(jnjGTVariant.getVolumeUom().getCode()
							.substring(jnjGTVariant.getVolumeUom().getCode().length() - 1));
					data.setVolumeLUomSuperscript(jnjGTVariant.getVolumeUom().getCode()
							.substring(jnjGTVariant.getVolumeUom().getCode().length() - 1));
					data.setVolumeLinearUom(jnjGTVariant.getVolumeUom().getCode()
							.substring(0, jnjGTVariant.getVolumeUom().getCode().length() - 1));
				}
				catch (final NumberFormatException numberFormatException)
				{
					LOG.warn("Volume Linear UOM does not comtain any superscript");
					data.setVolumeLUomSuperscript(emptyField);
					data.setVolumeLinearUom(jnjGTVariant.getVolumeUom().getCode());
				}
			}
			else
			{
				data.setVolumeLUomSuperscript(emptyField);
				data.setVolumeLinearUom(jnjGTVariant.getVolumeUom().getCode());
			}
		}
		return data;
	}

	public Populator<ProductModel, ProductData> getProductPricePopulator()
	{
		return productPricePopulator;
	}

	public void setProductPricePopulator(final Populator<ProductModel, ProductData> productPricePopulator)
	{
		this.productPricePopulator = productPricePopulator;
	}
}
