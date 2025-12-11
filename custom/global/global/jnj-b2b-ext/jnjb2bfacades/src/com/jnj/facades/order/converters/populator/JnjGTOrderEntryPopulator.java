/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.converters.populator;


import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import com.jnj.core.enums.JnjOrderTypesEnum;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.enums.JnjGTPageType;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.product.converters.populator.JnjGTOrderEntryProductPopulator;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commercefacades.order.converters.populator.OrderEntryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;


/**
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderEntryPopulator extends OrderEntryPopulator
{

	protected static final String ONE_SPACE = " ";

	/**
	 * Enum constant to store Site Names.
	 */
	public enum SITE_NAME
	{
		MDD, CONS, PCM;
	}

	@Resource(name = "priceDataFactory")
	protected JnjPriceDataFactory jnjPriceDataFactory;
	
	@Resource(name = "scheduleLineConverter")
	protected Converter<AbstractOrderEntryModel, JnjGTOrderEntryData> jnjScheduleLineConverter;

	@Autowired
	protected UserService userService;

	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjGTOrderEntryProductPopulator jnjGTOrderEntryProductPopulator;

	@Autowired
	protected JnjConfigServiceImpl jnjConfigServiceImpl;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		/***
		 * If order entry has Non-Remaining (Non-Hybris based) product then don't populate and display values for the
		 * product line item.
		 ***/
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		addCommon(source, target);
		if (StringUtils.isNotEmpty(source.getMaterialNumber()) || StringUtils.isNotEmpty(source.getMaterialEntered()))
		{
			((JnjGTOrderEntryData) target).setMaterialNumber(source.getMaterialNumber());
			((JnjGTOrderEntryData) target).setMaterialEntered(source.getMaterialEntered());

			/*** Setting product data to keep EXCEL/PDF views intact and display material number/entered. ***/
			final ProductData productData = new JnjGTProductData();
			productData.setCode((source.getMaterialNumber() != null) ? source.getMaterialNumber() : source.getMaterialEntered());
			target.setProduct(productData);
		}
		else
		{
			addProduct(source, target);
		}
		addTotals(source, target);
		addDeliveryMode(source, target);

		if (target instanceof JnjGTOrderEntryData)
		{
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (!StringUtils.equals(SITE_NAME.PCM.toString(), currentSite))
			{
				populateEpicData(source, target);
			}
		}
	}

	/**
	 * @param source
	 * @param target
	 */
	protected void populateEpicData(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		final JnjGTOrderEntryData jnjOrderEntryData = (JnjGTOrderEntryData) target;
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		/** Setting the expandable flag for displaying the delivery schedules **/
		if (null != source.getDeliverySchedules() && source.getDeliverySchedules().size() > 1
				&& Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			jnjOrderEntryData.setExpandableSchedules(true);
		}
		else
		{
			jnjOrderEntryData.setExpandableSchedules(false);
			jnjOrderEntryData.setStatus(source.getStatus());
		}

		//** F_ORD_1.2.20
		if (source.getOrder() instanceof OrderModel && OrderEntryStatus.ITEM_ACCEPTED.toString().equals(source.getStatus())
						&& !DateUtils.isSameDay(source.getOrder().getCreationtime(), new Date()))
		{
				jnjOrderEntryData.setStatus(OrderEntryStatus.BACKORDERED.toString());
		}
		jnjOrderEntryData.setExpectedDeliveryDate(source.getExpectedDeliveryDate());
		jnjOrderEntryData.setSapOrderlineNumber(source.getSapOrderlineNumber());
		jnjOrderEntryData.setShippingDate(source.getShippingDate());
		//Currently calling schedule line converter for Order Entries only
		if (null != source.getOrder()
				&& (StringUtils.isNotEmpty(source.getOrder().getSapOrderNumber()) || BooleanUtils.isTrue(source.getOrder()
						.getSapValidated())))
		{
			jnjScheduleLineConverter.convert(source,jnjOrderEntryData);
			//jnjScheduleLinePopulator.populate(source, jnjOrderEntryData);
		}
		
		
		/***  ScheduleLineNumber Sorting ***/
		List<JnjDeliveryScheduleData> schList = jnjOrderEntryData.getScheduleLines();
		if(schList!=null && !schList.isEmpty())
		{
        Collections.sort(schList, new Comparator<JnjDeliveryScheduleData>(){

           public int compare(JnjDeliveryScheduleData o1, JnjDeliveryScheduleData o2) {
            	if(o1.getDeliveryDate() != null && o2.getDeliveryDate() !=null )
            	{
                return (o1.getDeliveryDate()).compareTo(o2.getDeliveryDate());
            	}
				return 0;
            }
        });
       }
        jnjOrderEntryData.setScheduleLines(schList);
        /****************/


		if (OrderEntryStatus.BACKORDERED.getCode().equals(source.getStatus()))
		{
			jnjOrderEntryData.setExpectedShipDate(((JnJProductModel) source.getProduct()).getBackOrderedDate());
		}
		
		/** Fixes For JJEPIC-703 **/
		// Put this change only for MDD as at line number 157 we are marking the status as backorder.
		if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD)
				&& OrderEntryStatus.BACKORDERED.getCode().equals(jnjOrderEntryData.getStatus()))
		{
			jnjOrderEntryData.setExpectedDeliveryDate(null);
		}
		jnjOrderEntryData.setLotComment(source.getLot());
		jnjOrderEntryData.setStatus(source.getStatus());
		jnjOrderEntryData.setSelectedRoute(source.getSelectedRoute());
		jnjOrderEntryData.setPoNumber(source.getPONumber());
		jnjOrderEntryData.setInvoiceNumber(source.getInvoiceNumber());
		jnjOrderEntryData.setReturnInvNumber(source.getReturnInvNumber()); // Used in Order return... Instead using "invoiceNumber"
		if (source instanceof CartEntryModel)
		{
			jnjOrderEntryData.setValidationErrorKeys(((CartEntryModel) source).getValidationErrorKeys());
			jnjOrderEntryData.setQuoteSAPErrorMessage(((CartEntryModel) source).getMessage());
		}
		jnjOrderEntryData.setDefaultRoute(source.getRoute());

		/***
		 * If MDD, rejection resaon would remain same as the source has. For CONS, need to get verbiage as specified in
		 * defect -JIRA-410
		 */
		String rejectionReason = null;
		if (Jnjb2bCoreConstants.MDD.equals(currentSite))
		{
			rejectionReason = source.getReasonForRejection();
		}
		else
		{
			rejectionReason = jnjConfigServiceImpl.getConfigValueById(Order.ORDER_ENTRY_REASON_FOR_REJECTION_KEY
					+ source.getReasonForRejection());
			if (StringUtils.isEmpty(rejectionReason))
			{
				rejectionReason = source.getReasonForRejection();
			}
		}
		jnjOrderEntryData.setRejectionReason(rejectionReason);
		jnjOrderEntryData.setLotNumber(source.getBatchNum());
		jnjOrderEntryData.setLotExpiration((source.getExpiryDate() != null) ? source.getExpiryDate().toString() : null);
		jnjOrderEntryData.setHazmatInd(((JnJProductModel) source.getProduct()).getHazmatCode());
		jnjOrderEntryData.setSpecialStockPartner(source.getSpecialStockPartner());
		jnjOrderEntryData.setPriceOverrideReason(source.getPriceOverrideReason());
		jnjOrderEntryData.setApprover(source.getApprover());

		setShippingMethod(source, jnjOrderEntryData);

		if (StringUtils.isNotBlank(source.getPriceOverride()))
		{
			jnjOrderEntryData.setOverridenPrice(createPrice(source, Double.valueOf(source.getPriceOverride())));
		}
		jnjOrderEntryData.setPriceOverrideApprover(source.getPriceOverrideApprover());
		if (source.getBasePrice() != null)
		{
			jnjOrderEntryData.setExtendedPrice(createPrice(source, source.getBasePrice()));
		}

		/** For add to cart from order history as sales rep can add division product only **/
		boolean isSalesRepDivisionCompatible = true;
		final Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);
		if (StringUtils.equals(currentSite, Jnjb2bCoreConstants.MDD) && BooleanUtils.isTrue(salesRep)
				&& !jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) source.getProduct()))
		{
			isSalesRepDivisionCompatible = false;
		}
		jnjOrderEntryData.setSalesRepDivisionCompatible(isSalesRepDivisionCompatible);
		jnjOrderEntryData.setShipmentLocation(source.getShipmentLoc());
		jnjOrderEntryData.setContractNumber(source.getContractNum());
		jnjOrderEntryData.setHoldCode(source.getHoldCode());
		//Jira#AAOL-4186 start
		boolean isProposed = source.getIsProposed() != null ? source.getIsProposed() : false;
		jnjOrderEntryData.setIsProposed(isProposed);
		
		String originalOrderItem = source.getOriginalOrderItem() != null ? source.getOriginalOrderItem() : "";
		jnjOrderEntryData.setOriginalOrderItem(originalOrderItem);
		//Jira#AAOL-4186 end
		
		//Soumitra AAOL-3785
		String batchNumber = source.getBatchNumber() != null ? source.getBatchNumber() : "";
		jnjOrderEntryData.setBatchNumber(batchNumber);
		String serialNumber = source.getSerialNumber() != null ? source.getSerialNumber() : "";
		jnjOrderEntryData.setSerialNumber(serialNumber);
		//Soumitra AAOL-3785
	}

	/**
	 * This mehtod is used to set the shipping method base on the selected shipping method on the validate page.
	 * 
	 * @param source
	 * @param jnjOrderEntryData
	 */
	protected void setShippingMethod(final AbstractOrderEntryModel source, final JnjGTOrderEntryData jnjOrderEntryData)
	{
		if (StringUtils.isNotEmpty(source.getSelectedRoute()))
		{
			final JnjGTShippingMethodModel shippingMethodModel = getShippingMethod(null, source.getSelectedRoute());
			if (shippingMethodModel != null)
			{
				jnjOrderEntryData.setShippingMethod(shippingMethodModel.getDispName());
				jnjOrderEntryData.setSelectableShippingMethod(shippingMethodModel.isSelectable());
			}
		}
		else if (StringUtils.isNotEmpty(source.getRoute()))
		{
			final JnjGTShippingMethodModel shippingModel = getShippingMethod(source.getRoute(), null);
			if (shippingModel != null)
			{
				jnjOrderEntryData.setShippingMethod(shippingModel.getDispName());
				jnjOrderEntryData.setSelectableShippingMethod(shippingModel.isSelectable());
			}
		}
		else if (StringUtils.isEmpty(jnjOrderEntryData.getShippingMethod()))
		{
			final JnjGTShippingMethodModel shippingMethodModel = getShippingMethod(null,
					Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
			if (shippingMethodModel != null)
			{
				jnjOrderEntryData.setShippingMethod(shippingMethodModel.getDispName());
				jnjOrderEntryData.setSelectableShippingMethod(shippingMethodModel.isSelectable());
			}
		}
	}

	@Override
	protected void addProduct(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		final JnjGTPageType pageType = (sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_PAGE_TYPE) == null) ? JnjGTPageType.OTHER
				: (JnjGTPageType) sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_PAGE_TYPE);


		if (JnjGTPageType.ORDER_DETAIL.equals(pageType))
		{
			final JnjGTProductData product = new JnjGTProductData();
			jnjGTOrderEntryProductPopulator.populate(orderEntry.getProduct(), product);
			entry.setProduct(product);
		}
		else
		{
			super.addProduct(orderEntry, entry);
		}

		double totalWeight = 0;
		double totalVolume = 0;
		String weightUOM = null;
		String volumeUOM = null;
		final ProductData productData = entry.getProduct();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		if (orderEntry.getProduct() instanceof JnJProductModel && productData instanceof JnjGTProductData)
		{
			// FOR EPIC ONLY
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (!StringUtils.equals(SITE_NAME.PCM.toString(), currentSite))
			{
				if (orderEntry.getReferencedVariant() != null)
				{
					final JnjGTVariantProductModel referenceVariant = orderEntry.getReferencedVariant();
					((JnjGTProductData) productData).setGtin(referenceVariant.getEan());
					((JnjGTProductData) productData).setUpc(referenceVariant.getUpc());
					/*((JnjGTProductData) productData).setNumerator(referenceVariant.getNumerator().toString());
					((JnjGTProductData) productData).setDeliveryUnit(referenceVariant.getUnit().getName());
					((JnjGTProductData) productData).setSalesUnit(referenceVariant.getLwrPackagingLvlUom().getName());*/
					
					/* Display UOM changes as per JJEPIC-597 <There will always be eaches> */
					final JnJProductModel productModel = (JnJProductModel) orderEntry.getProduct();
					if ((JnjOrderTypesEnum.ZDEL.equals(orderEntry.getOrder().getOrderType()) || JnjOrderTypesEnum.ZKB
							.equals(orderEntry.getOrder().getOrderType()))
							&& StringUtils.equals(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_MITEK),
									productModel.getSalesOrgCode()))
					{
						((JnjGTProductData) productData).setNumerator("1");
						((JnjGTProductData) productData).setDeliveryUnit("Box");
						((JnjGTProductData) productData).setSalesUnit("each");
					}
					else
					{
						//JJEPIC-275
						if (referenceVariant.getNumerator() != null)
						{
							((JnjGTProductData) productData).setNumerator(referenceVariant.getNumerator().toString());
						}
						((JnjGTProductData) productData).setDeliveryUnit(referenceVariant.getUnit().getName());
						((JnjGTProductData) productData).setSalesUnit(referenceVariant.getLwrPackagingLvlUom().getName());
					}

					if (referenceVariant.getWeightQty() != null && referenceVariant.getWeightUom() != null)
					{
						/*
						 * final String productWeight = referenceVariant.getWeightQty().toString() + ONE_SPACE +
						 * referenceVariant.getWeightUom().getCode().toString();
						 */
						final String productWeight = decimalFormat.format(referenceVariant.getWeightQty());
						totalWeight = totalWeight + referenceVariant.getWeightQty().doubleValue()
								* orderEntry.getQuantity().longValue();

						((JnjGTProductData) productData).setProductWeight(productWeight);
						weightUOM = referenceVariant.getWeightUom().getCode().toString();
					}
					if (referenceVariant.getVolumeQty() != null && referenceVariant.getVolumeUom() != null)
					{

						/*
						 * final String productVolume = referenceVariant.getVolumeQty().toString() + ONE_SPACE +
						 * referenceVariant.getVolumeUom().getCode().toString();
						 */
						final String productVolume = decimalFormat.format(referenceVariant.getVolumeQty());
						totalVolume = totalVolume + referenceVariant.getVolumeQty().doubleValue()
								* orderEntry.getQuantity().longValue();

						((JnjGTProductData) productData).setProductVolume(productVolume);
						volumeUOM = referenceVariant.getVolumeUom().getCode().toString();
					}
				}


				((JnjGTOrderEntryData) entry).setOrderWeight(Double.valueOf((decimalFormat.format(totalWeight))).doubleValue());
				((JnjGTOrderEntryData) entry).setCubicVolume(Double.valueOf((decimalFormat.format(totalVolume))).doubleValue());
				((JnjGTOrderEntryData) entry).setWeightUOM(weightUOM);
				((JnjGTOrderEntryData) entry).setVolumeUOM(volumeUOM);
			}
			// Contract needs to be added
			else
			{
				// FOR PCM ONLY
				final JnJProductModel modProduct = (JnJProductModel) orderEntry.getProduct();
				final JnJProductModel baseProduct = modProduct.getMaterialBaseProduct();

				((JnjGTProductData) productData).setUpc(modProduct.getUpcCode());

				// Setting Summary & Image Available Indicator for PCM Cart Page
				if (baseProduct != null)
				{
					final String modProductUrl = jnJGTProductService.getProductUrl(baseProduct);
					productData.setUrl(modProductUrl);

					productData.setSummary(baseProduct.getSummary());
					((JnjGTProductData) productData).setImageAvailableInd(baseProduct.getImageAvailableInd());
					((JnjGTProductData) productData).setName(baseProduct.getName());
				}
				else
				{
					productData.setSummary(modProduct.getSummary());
					((JnjGTProductData) productData).setImageAvailableInd(modProduct.getImageAvailableInd());
					((JnjGTProductData) productData).setName(modProduct.getName());
				}

				// Setting First Ship Effective Date for PCM Cart Page
				((JnjGTProductData) productData).setFirstShipEffective(modProduct.getFirstShipEffectDate());

				// Setting Status for PCM Cart Page
				((JnjGTProductData) productData).setStatus(jnJGTProductService.getLaunchStatus(modProduct, Boolean.FALSE));
			}
		}
	}

	/**
	 * Retrieves <code>JnjGTShippingMethodModel</code> instance based on route or selected route.
	 * 
	 * @param route
	 * @param selectedRoute
	 * @return JnjGTShippingMethodModel
	 */
	protected final JnjGTShippingMethodModel getShippingMethod(final String route, final String selectedRoute)
	{
		final JnjGTShippingMethodModel shippingMethodModel;
		final JnjGTShippingMethodModel exampleShippingModel = new JnjGTShippingMethodModel();
		if (StringUtils.isNotEmpty(selectedRoute))
		{
			exampleShippingModel.setExpidateRoute(selectedRoute);
		}

		if (StringUtils.isNotEmpty(route))
		{
			exampleShippingModel.setRoute(route);
		}
		try
		{
			shippingMethodModel = flexibleSearchService.getModelByExample(exampleShippingModel);

		}
		catch (final ModelNotFoundException exception)
		{
			return null;
		}
		catch (final AmbiguousIdentifierException aie) {
			
			return null;
		}
		return shippingMethodModel;
	}
}
