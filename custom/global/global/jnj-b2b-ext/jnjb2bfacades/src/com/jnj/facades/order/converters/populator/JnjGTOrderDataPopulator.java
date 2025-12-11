/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.facades.order.converters.populator;

import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2b.enums.CheckoutPaymentType;
import de.hybris.platform.commercefacades.order.converters.populator.OrderPopulator;
import de.hybris.platform.commercefacades.order.data.AbstractOrderData;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.order.OrderEntry;
import de.hybris.platform.jnjb2boutboundservice.constants.Jnjb2boutboundserviceConstants;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facade.util.impl.JnjPriceDataFactory;

import com.jnj.facades.data.JnjGTShippingTrckInfoData;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order.Invoice;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order.PackingList;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;

import com.jnj.core.enums.JnjGTPageType;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;

import de.hybris.platform.core.model.user.AddressModel;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTShipmentTrackingURLsModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTShippingLineDetailsModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.services.JnjGTOrderService;


/**
 * Extending OOTB OrderPopulator
 * 
 */

public class JnjGTOrderDataPopulator extends OrderPopulator
{
	@Autowired
	protected SessionService sessionService;

	@Resource(name = "priceDataFactory")
	protected JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired

	protected JnjGTOrderService jnjGTOrderService;


	protected JnjGTSurgeryPopulator jnjGTSurgeryPopulator;

	protected Converter<CheckoutPaymentType, B2BPaymentTypeData> b2bPaymentTypeConverter;

	@Autowired
	Populator<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoPopulator;

	@Autowired
	protected JnjConfigServiceImpl jnjConfigServiceImpl;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	EnumerationService enumerationService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	/**
	 * Flag to indicate if the session corresponds to MDD or Consumer site.
	 */
	protected boolean isMddSite;

	/**
	 * Constant used to build URL.
	 */
	protected static final String AMPERSAND = "&";

	/**
	 * Constant used to build URL.
	 */
	protected static final String PARAM_QUESTION = "?";

	/**
	 * Default URL value.
	 */
	protected static final String HASH_LINK = "#";

	/**
	 * Constant for equal.
	 */
	protected static final String EQUAL = "=";

	/**
	 * Constant plant value 'MLC'.
	 */
	protected static final String MLC_PLANT = "MLC";
	static final String PREFIX_ORDER_STATUS = "order.status.";
	protected static final List<JnjOrderTypesEnum> ORDER_TYPES_FOR_DISPUTE_ORDER_LINE = Arrays.asList(JnjOrderTypesEnum.ZDEL,

			JnjOrderTypesEnum.ZOR, JnjOrderTypesEnum.ZNC );

	@Override
	public void populate(final OrderModel source, final OrderData target)
	{
		/*** Super call not required ***/
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		/*** Page type to be used to determine upto what extent population needs to be performed. ***/


		final JnjGTPageType pageType = (sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_PAGE_TYPE) == null) ? JnjGTPageType.ORDER_DETAIL
				: (JnjGTPageType) sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_PAGE_TYPE);

		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		setMddSite(Jnjb2bCoreConstants.MDD.equals(currentSite) ? true : false);

		/***
		 * Populate all details if it's ORDER DETAIL page otherwise (for INVOICE DETAIL) populate only Order Header
		 * specifications.
		 ***/
		addOrderDetails(source, (JnjGTOrderData) target);
		/*to be removed from here on SAP Integration*/
		 
		/*to be removed till here on SAP Integration*/
		
		populateShipmentTrackingInfo((JnjGTOrderData) target, source.getShippingDetails(), source.getEntries());
if (JnjGTPageType.ORDER_DETAIL.equals(pageType)||JnjGTPageType.INVOICE_DETAIL.equals(pageType))
		{
			addTotals(source, target);
			addEntries(source, target);
		}
 
	}

	/**
	 * Populates and adds all Order Details to the DTO object.
	 * 
	 * @param source
	 * @param target
	 */

	public void addOrderDetails(final OrderModel source, final JnjGTOrderData target)
	{
		target.setCode(source.getCode());
		target.setGln((source.getUnit() != null) ? ((JnJB2BUnitModel) source.getUnit()).getGlobalLocNo() : null);
		target.setDistributorPONumber(source.getDealerPONum());
		target.setAttention(source.getAttention());
		target.setDropShipAccount(source.getDropShipAccount());
		target.setAccountNumber((source.getUnit() != null) ? source.getUnit().getUid() : null);
		target.setOrderType((source.getOrderType() != null) ? source.getOrderType().toString() : null);
		target.setShipToAccount(source.getShipToAccount());
		target.setSoldToAccount(source.getUnit().getUid());
		target.setExpectedDeliveryDate(source.getNamedDeliveryDate());
		target.setSpineSalesRepUCN(source.getSpineSalesRepUCN());
		target.setOrderOnPAHold(source.isOrderOnPAHold());
		/*soumitra changes for consignment related fields AAOL-3773 and AAOL-2886*/
		target.setPoDate(source.getPoDate());
		target.setCreated(source.getCreationtime());
		target.setStockUser(source.getStockUser());
		target.setEndUser(source.getEndUser());
		target.setInvoiceStatus(source.getInvoiceStatus());
		/* changes for AAOL-4339*/
		target.setCreatedOn(source.getReturnCreatedDate());
		
		target.setShippingInstructions(source.getShippingInstruction());
//changes for 4341
		target.setPaymentmethod(source.getPaymentmethod());
		target.setPackingList(source.getPackingList());
		target.setCarrierName(source.getCarrierName());
		target.setProofOfDelivery(source.getProofOfDelivery());
		target.setTracking(source.getTracking());
		target.setBillOfLanding(source.getBillOfLanding());
		//populate the credit hold flag
		target.setShowCreditHold(false);
		if (source.getOrderType().equals(JnjOrderTypesEnum.ZOR))
		{
			String riskCategoryCodesArray[] = null;
			try
			{
				riskCategoryCodesArray = Config.getParameter(Jnjgtb2boutboundserviceConstants.RISK_CATEGORY_CODES).split(
						Jnjgtb2boutboundserviceConstants.COMMA_STRING);
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
			if (riskCategoryCodesArray != null && source.getRiskCategory() != null)
			{
				for (final String riskCategoryCode : riskCategoryCodesArray)
				{
					if (riskCategoryCode.trim().equals(source.getRiskCategory()))
					{
						target.setShowCreditHold(true);
						break;
					}
				}
			}
		}

		final JnjGTSurgeonModel surgeon = source.getSurgeon();
		// If surgeon model is present
		if (surgeon != null)
		{
			final StringBuilder surgeonName = new StringBuilder();

			// If first name is not blank
			if (StringUtils.isNotBlank(surgeon.getFirstName()))
			{
				surgeonName.append(surgeon.getFirstName()).append(" ");
			}
			// If last name is not blank
			if (StringUtils.isNotBlank(surgeon.getLastName()))
			{
				surgeonName.append(surgeon.getLastName());
			}
			target.setSurgeonName(surgeonName.toString());
		}
		else if(source.getSurgeonName()!=null){

			target.setSurgeonName(source.getSurgeonName());
		}
	
		target.setOrderNumber(source.getOrderNumber());
		target.setCreated(source.getDate());
		target.setStatus(source.getStatus());
		target.setCordisHouseAccount(source.getCordisHouseAccount());
		target.setStatusDisplay((source.getStatus() != null) ? enumerationService.getEnumerationName(source.getStatus())
				: StringUtils.EMPTY);
		target.setReasonCode(source.getReasonCode());
		target.setReasonCodeNoCharge(source.getReasonCode());
		target.setReasonCodeReturn(source.getReasonCode());
		target.setCustomerPoNumber(source.getCustomerReferencePO());
		target.setPurchaseOrderNumber(source.getPurchaseOrderNumber());
		target.setRequestedDeliveryDate(source.getRequestedDeliveryDate());

		/** Setting Site uid **/
		if (source.getSite() != null)
		{
			target.setSite(source.getSite().getUid());
		}
		/** Setting Store uid **/
		if (source.getStore() != null)
		{
			target.setStore(source.getStore().getUid());
		}
		/** Setting SAP Order number **/
		if (source.getSapOrderNumber() != null)
		{
			target.setOrderNumber(source.getSapOrderNumber());
		}

		if (null != source.getUnit())
		{
			/** Setting b2b unit id **/
			target.setB2bUnitId(source.getUnit().getUid());

			/** Setting b2b unit name **/
			target.setB2bUnitName(source.getUnit().getName());

			/** Checking if the unit is an instance of JnJB2BUnitModel **/
			if (source.getUnit() instanceof JnJB2BUnitModel)
			{
				/** Setting the b2b unit GLN **/
				target.setB2bUnitGln(((JnJB2BUnitModel) source.getUnit()).getGlobalLocNo());
			}
		}

		if (source.getAttachedDoc() != null && source.getAttachedDoc().getURL() != null)
		{
			target.setAttachDocURL(source.getAttachedDoc().getURL());
		}

		/** Populate and set Surgery info, if any. **/
		if (null != source.getSurgeryInfo())
		{
			final SurgeryInfoData data = new SurgeryInfoData();

			jnjGTSurgeryPopulator.populate(source.getSurgeryInfo(), data);
			target.setSurgeryInfo(data);
		}

		/** Setting Payment type **/
		if (source.getPaymentType() != null)
		{
			target.setPaymentType(getB2bPaymentTypeConverter().convert(source.getPaymentType()));
		}

		/*** Adding Shipping Address ***/
		if (null != source.getDeliveryAddress())
		{
			addDeliveryAddress(source, target);
		}

		/*** Adding Billing Address ***/
		if (null != source.getPaymentAddress())
		{

			final AddressData billingAddress = new JnjGTAddressData();
			getAddressConverter().convert(source.getPaymentAddress(), billingAddress);
			target.setBillingAddress(billingAddress);
		}

		/** Convert and Populate payment information, if available in source. **/
		final PaymentInfoModel paymentInfoModel = source.getPaymentInfo();
		if (null != paymentInfoModel && paymentInfoModel instanceof CreditCardPaymentInfoModel)
		{
			final CreditCardPaymentInfoModel ccInfoModel = (CreditCardPaymentInfoModel) paymentInfoModel;
			final CCPaymentInfoData ccPaymentInfoData = new CCPaymentInfoData();
			creditCardPaymentInfoPopulator.populate(ccInfoModel, ccPaymentInfoData);
			if (StringUtils.isNotEmpty(ccPaymentInfoData.getCardType()))
			{
				final String crediCardType = StringUtils.substring(ccPaymentInfoData.getCardType(), 2);
				ccPaymentInfoData.setCardType(crediCardType);
			}
			ccPaymentInfoData.setCardNumber(maskPaymentCardNumber(ccPaymentInfoData.getCardNumber()));
			target.setPaymentInfo(ccPaymentInfoData);
		}

		/***
		 * As per clarification received, payment type is fixed and hence to be fetched from the Config table.
		 ***/
		final B2BPaymentTypeData paymentType = new B2BPaymentTypeData();
		paymentType.setDisplayName(jnjConfigServiceImpl.getConfigValueById(Jnjb2bCoreConstants.Order.PAYMENT_METHOD_KEY));
		target.setPaymentType(paymentType);

		/*** Set user only if order has been placed through Portal ***/
		final UserModel associatedUser = source.getUser();
		if (associatedUser != null && !Order.DUMMY_SAP_USER_ID.equals(associatedUser.getUid()))
		{
			target.setOrderedBy(associatedUser.getName());
		}

		/*** Set the required fields ONLY when MDD order type is Delivered and third party billing flasg is set. ***/
		/*** Remove third party billing as per JJEPIC-673 or defect no 78 **/
		if (JnjOrderTypesEnum.ZDEL.equals(source.getOrderType()) || JnjOrderTypesEnum.ZOR.equals(source.getOrderType()))
		{
			target.setDealerPoNumber(source.getDealerPONum());
			target.setDropShipAccount(source.getDropShipAccount() != null ? source.getDropShipAccount().replaceAll("^0+",
					StringUtils.EMPTY) : null);
		}

		/*** Set falg to display Surgeon Information Link on Order detail page only when surgeon exists with order. ***/
		target.setDisplaySurgeonInfo((source.getSurgeon() != null) ? Boolean.TRUE : Boolean.FALSE);

		//TODO [AR]: As per mapping clarification received, shipment location to be set with 'JnjDeliveryScheduleModel.deliveryDate',
		//but which Schedule Line to be selected to set this value.

		//jnjGTOrderData.setExpectedDeliveryDate(expectedDeliveryDate)

		/* Check if RGA/COD links required on Order Details Page <For Replenish Order Only> */
		if (source.getOrderType() != null && source.getOrderType().equals(JnjOrderTypesEnum.ZRE))
		{

			final List<String> deliveryBlocks = jnjGTOrderService.getDeliveryBlocksForEntries(source.getPk().toString());
			if (CollectionUtils.isNotEmpty(deliveryBlocks))
			{
				if (deliveryBlocks.contains(null) || deliveryBlocks.contains(StringUtils.EMPTY))
				{
					target.setRgaLinkURL(Boolean.TRUE);
				}
				for (final String value : deliveryBlocks)
				{
					if (StringUtils.isNotEmpty(value))
					{
						target.setCodLinkURL(Boolean.TRUE);
						break;
					}
				}
			}
		}

		/*** Change introduced as part of a CR where dispute options gets restricted to few Order Types only. ***/
		final boolean displayDisputeOptions ;
		if(isMddSite){
		displayDisputeOptions = isMddSite && ORDER_TYPES_FOR_DISPUTE_ORDER_LINE.contains(source.getOrderType());
		}
		else {
		displayDisputeOptions = ORDER_TYPES_FOR_DISPUTE_ORDER_LINE.contains(source.getOrderType());
		}
		target.setDisplayDisputeOption(Boolean.valueOf(displayDisputeOptions));
		/***

		 * Sales Territory - Set using territory code from the JnjGTTerritoryDivison record whose addresses contains the
		 * shipToNumber of the order.
		 ***/
		if (source.getDeliveryAddress() instanceof AddressModel)
		{
			final AddressModel shippingAddress = (AddressModel) source.getDeliveryAddress();
			final String shipToNumber = shippingAddress.getJnJAddressId();
			
			target.setSalesTerritory((shipToNumber != null) ? jnjGTOrderService.getSalesTerritoryByShipToNum(shipToNumber)
					: StringUtils.EMPTY);
		}

		/*** Read GHX-E link url from configuration and set it in the target. ***/
		target.setGhxUrl(jnjConfigServiceImpl.getConfigValueById(Invoice.GHX_E_INVOICING_URL_KEY));
	}

	/**
	 * Overridden to add/convert other prices and components.
	 */
	@Override
	protected void addTotals(final AbstractOrderModel source, final AbstractOrderData target)
	{
		//target.setTotalPromotionAllowance(createPrice(source, source.getTotalPromotionalAllowance()));
		//target.setTotalUnsaleableAllowance(createPrice(source, source.getTotalUnsaleableAllowance()));
		
		double subtotal = 0.0;
		double totalTax = 0;
		double grossPrice = 0;
		double totalFee = 0;
		target.setTotalTax(createPrice(source, source.getTotalTax()));
		target.setSubTotal(createPrice(source, source.getSubtotal()));
		target.setDeliveryCost(source.getDeliveryCost() != null ? createPrice(source, source.getDeliveryCost()) : createPrice(
				source, Double.valueOf(0.00)));
		target.setTotalPriceWithTax((createPrice(source, calcTotalWithTax(source))));


		if (target instanceof JnjGTOrderData)
		{


			final JnjGTOrderData jnjGTOrderData = (JnjGTOrderData) target;
			jnjGTOrderData.setTempFee(createPrice(
					source,
					Double.valueOf(source.getTotalPrice().doubleValue()
							- (source.getSubtotal().doubleValue() + source.getTotalHsaPromotion().doubleValue() + source.getTotalTax()
									.doubleValue()))));
			if (StringUtils.equalsIgnoreCase(Jnjb2bFacadesConstants.Y_STRING,
					jnjConfigServiceImpl.getConfigValueById(Jnjb2bFacadesConstants.MDD_TEMP_ORDER_PRICE_FLAG)))
			{

				jnjGTOrderData.setTempFee(createPrice(
						source,
						Double.valueOf(source.getTotalPrice().doubleValue()
								- (source.getSubtotal().doubleValue() + source.getTotalHsaPromotion().doubleValue() + source
										.getTotalTax().doubleValue()))));

				jnjGTOrderData.setMddTempOrderPriceFlag(Boolean.TRUE);
			}
			else
			{

				jnjGTOrderData.setTempFee(createPrice(
						source,
						Double.valueOf(source.getTotalPrice().doubleValue()
								- (source.getTotalTax().doubleValue() + source.getSubtotal().doubleValue()))));

				jnjGTOrderData.setMddTempOrderPriceFlag(Boolean.FALSE);
			}





			jnjGTOrderData.setTotalNetValue(createPrice(source, source.getSubtotal()));
			/** Total fee **/
			jnjGTOrderData.setTotalDropShipFee(createPrice(source, source.getTotalDropShipFee()));
			jnjGTOrderData.setTotalFreightFees(createPrice(source, source.getTotalFreightFees()));
			jnjGTOrderData.setTotalminimumOrderFee(createPrice(source, source.getTotalminimumOrderFee()));

			/*** Set Total Fee and other sub-components of it ***/





			jnjGTOrderData.setTotalFees(createPrice(source, source.getTotalFees()));
			/** End of fee **/
			
			totalTax = source.getTotalTax();
			subtotal =source.getSubtotal(); 
			totalFee = source.getTotalFees();
			
			grossPrice =totalTax +subtotal +totalFee;
			target.setTotalPrice(createPrice(source,grossPrice));
			jnjGTOrderData.setHsaPromotion(createPrice(source, source.getTotalHsaPromotion()));
			/*** Set Total Fee and other sub-components of it ***/
			jnjGTOrderData.setOtherCharges(createPrice(source, source.getTotalOtherCharge()));
			jnjGTOrderData.setManualFee(createPrice(source, source.getTotalmanualFee()));
			jnjGTOrderData.setAdjustedRateAllowance(createPrice(source, source.getTotalAdjRateAll()));
			jnjGTOrderData.setDiscountTotal(createPrice(source, Double.valueOf(Math.abs(source.getTotalDiscounts().doubleValue()))));

			if (null != source.getTotalGrossPrice())
			{

				jnjGTOrderData.setTotalGrossPrice(createPrice(source, source.getTotalGrossPrice()));
			}
			else
			{

				jnjGTOrderData.setTotalGrossPrice(createPrice(source, source.getTotalPrice()));
			}
		}
	}

	/**
	 * This method is used to get the order Entries (Product details for particular order)
	 */
	@Override
	protected void addEntries(final AbstractOrderModel source, final AbstractOrderData prototype)
	{
		double orderWeight = 0;
		double orderVolume = 0;

		if (prototype instanceof JnjGTOrderData)
		{
			String plant = null;
			String shipmentLocation = null;

			final JnjGTOrderData orderData = (JnjGTOrderData) prototype;
			final Integer orderDetailPageSize = sessionService.getAttribute("orderDetailPageSize");

			/***
			 * Select final page size according to the selected page size from detail page and number of entries available.
			 ***/
			final int selectedPageSize = (orderDetailPageSize != null) ? ((source.getEntries().size() < orderDetailPageSize
					.intValue()) ? source.getEntries().size() : orderDetailPageSize.intValue()) : source.getEntries().size();

			Date expectedDeliveryDate = null;
			AbstractOrderEntryModel entry = null;
			final List<OrderEntryData> orderEntryList = new ArrayList<OrderEntryData>();
			String weightUOM = StringUtils.EMPTY;
			String volumeUOM = StringUtils.EMPTY;

			// changes for jjepic-533 - sort order line items based on sapline number
			
			final List<AbstractOrderEntryModel> orderEntryModelList = new ArrayList<AbstractOrderEntryModel>();
			if (null != source.getEntries() && CollectionUtils.isNotEmpty(source.getEntries()))
			{
				orderEntryModelList.addAll(source.getEntries());

				Collections.sort(orderEntryModelList, new Comparator<AbstractOrderEntryModel>()
				{
					@Override
					public int compare(final AbstractOrderEntryModel item1, final AbstractOrderEntryModel item2)
					{
						try
						{
							final Integer i1 = Integer.parseInt(item1.getSapOrderlineNumber());
							final Integer i2 = Integer.parseInt(item2.getSapOrderlineNumber());
							if (i1 > i2)
							{
								return 1;
							}
							else
							{
								return -1;
							}
						}
						catch (final NumberFormatException numberFormatException)
						{
							return 1;
						}
					}
				});

			}

			for (int i = 0; i < selectedPageSize; i++)
			{

				OrderEntryData orderEntryData = new JnjGTOrderEntryData();
				if (null != orderEntryModelList && CollectionUtils.isNotEmpty(orderEntryModelList))
				{
					entry = orderEntryModelList.get(i);
					orderEntryData = getOrderEntryConverter().convert(entry);

					if (expectedDeliveryDate == null
							|| (entry.getExpectedDeliveryDate() != null && expectedDeliveryDate.before(entry.getExpectedDeliveryDate())))
					{
						expectedDeliveryDate = entry.getExpectedDeliveryDate();
					}
					shipmentLocation = (shipmentLocation == null) ? ((entry.getShipmentLoc() != null && !entry.getShipmentLoc()
							.isEmpty()) ? entry.getShipmentLoc() : null) : shipmentLocation;

					plant = (plant == null) ? ((entry.getPlant() != null && !entry.getPlant().isEmpty()) ? entry.getPlant() : null)
							: plant;
				}
				orderEntryList.add(orderEntryData);




				orderWeight += ((JnjGTOrderEntryData) orderEntryData).getOrderWeight();
				orderVolume += ((JnjGTOrderEntryData) orderEntryData).getCubicVolume();
				weightUOM = ((JnjGTOrderEntryData) orderEntryData).getWeightUOM();
				volumeUOM = ((JnjGTOrderEntryData) orderEntryData).getVolumeUOM();
			}

			orderData.setEntries(orderEntryList);

			((JnjGTOrderData) prototype).setTotalNumberOfEntries(String.valueOf(source.getEntries() == null ? 0 : source
					.getEntries().size()));
			orderData.setExpectedDeliveryDate(expectedDeliveryDate);
			orderData.setShipmentLocation(shipmentLocation);
			orderData.setOrderWeightUOM(weightUOM);
			orderData.setOrderVolumeUOM(volumeUOM);
			//Modification for OCD changes
			orderData.setContainsOCDProduct(source.isContainsOCDProduct());
			//End Modification
		}
		else
		{
			super.addEntries(source, prototype);
		}

		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		prototype.setOrderWeight((Double.valueOf(decimalFormat.format(orderWeight))).doubleValue());
		prototype.setOrderVolume((Double.valueOf(decimalFormat.format(orderVolume))).doubleValue());
	}

	/**
	 * The createPrice method converts the double value in the proper price format.
	 * 
	 * @param source
	 * @param val
	 * 
	 * @return PriceData
	 * 
	 */
	@Override
	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return jnjPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	/**
	 * 
	 * @param target
	 * @param jnjGTShippingDetails
	 */


	protected void populateShipmentTrackingInfo(final JnjGTOrderData target,
			final Set<JnjGTShippingDetailsModel> jnjGTShippingDetails, final List<AbstractOrderEntryModel> entries)
	{
		if (jnjGTShippingDetails != null)
		{

			final Map<String, Set<JnjGTShippingTrckInfoData>> shippingTrckInfoDataMap = new HashMap<>();
			final Map<String, String> packingListDetails = new HashMap<>();
			final Map<String, String> trackingIdDetails = new HashMap<>();
			final Map<String, Date> proofOfDelDetails = new HashMap<>();
			final Map<String, Date> entryWithShipDate = new HashMap<>();
			final List<String> carrierList = new ArrayList<>();
			final List<String> billOfLadings = new ArrayList<>();
			String trackingNumber = null;
			String trackingUrl = null;
			final List<String> mddFedexCarrierCodes = JnJCommonUtil.getValues(
					Jnjb2bFacadesConstants.MDD_SHIPMENT_FREIGHT_CARRIER_CODE, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<String> consumerFedexCarrierCodes = JnJCommonUtil.getValues(
					Jnjb2bFacadesConstants.CONSUMER_SHIPMENT_FREIGHT_CARRIER_CODE, Jnjb2bCoreConstants.SYMBOl_COMMA);

			for (final JnjGTShippingDetailsModel shippingDetail : jnjGTShippingDetails)
			{
				
				String plant = null;
				carrierList.add(shippingDetail.getCarrierScacName());
				billOfLadings.add(shippingDetail.getBolNum());

				if (CollectionUtils.isNotEmpty(shippingDetail.getShippingLineDetails()))
				{

					final JnjGTShippingLineDetailsModel firstShippingLineDetail = shippingDetail.getShippingLineDetails().iterator()
							.next();
					//can we pass a map of saplinenumber & plant
					plant = getPlantFromOrderLine(firstShippingLineDetail.getSapOrderLineNum(), entries, entryWithShipDate);
					


					for (final JnjGTShippingLineDetailsModel lineDetail : shippingDetail.getShippingLineDetails())
					{
						final String zipCode = (target.getDeliveryAddress() != null) ? target.getDeliveryAddress().getPostalCode()
								: null;
						if (isMddSite)
						{
							trackingNumber = lineDetail.getTrackingNum();
							trackingUrl = getTrackingUrl(shippingDetail, plant, zipCode, lineDetail);
							/*** Put values in map here since Tracking Number is available at line level for MDD order. ***/
							trackingIdDetails.put(trackingNumber, trackingUrl);
							if (!proofOfDelDetails.containsKey(trackingNumber)
									&& entryWithShipDate.containsKey(firstShippingLineDetail.getSapOrderLineNum())
									&& StringUtils.isNotEmpty(shippingDetail.getCarrierScacCde())
									&& mddFedexCarrierCodes.contains(shippingDetail.getCarrierScacCde()))
							{
								proofOfDelDetails
										.put(trackingNumber, entryWithShipDate.get(firstShippingLineDetail.getSapOrderLineNum()));
							}
						}
						else
						{
							trackingNumber = shippingDetail.getTrackingNum();
							trackingUrl = getTrackingUrl(shippingDetail, null, zipCode, null);
							if (!trackingIdDetails.keySet().contains(trackingNumber))
							{
								trackingIdDetails.put(trackingNumber, trackingUrl);
							}

							if (!proofOfDelDetails.containsKey(trackingNumber)
									&& StringUtils.isNotEmpty(shippingDetail.getCarrierScacCde())
									&& CollectionUtils.isNotEmpty(consumerFedexCarrierCodes)
									&& consumerFedexCarrierCodes.contains(shippingDetail.getCarrierScacCde()))
							{
								proofOfDelDetails.put(trackingNumber, shippingDetail.getActualShipDate());
							}
						}
						populateShippingTrckInfoDataMap(shippingTrckInfoDataMap, lineDetail.getSapOrderLineNum(), trackingNumber,
								trackingUrl, shippingDetail.getActualShipDate());
					}
				}
				else
				{
					if (StringUtils.isNotEmpty(shippingDetail.getTrackingNum()))
					{
						final String zipCode = (target.getDeliveryAddress() != null) ? target.getDeliveryAddress().getPostalCode()
								: null;
						trackingNumber = shippingDetail.getTrackingNum();
						trackingUrl = getTrackingUrl(shippingDetail, plant, zipCode, null);
						if (!trackingIdDetails.keySet().contains(trackingNumber))
						{
							trackingIdDetails.put(trackingNumber, trackingUrl);
						}
						final Set<JnjGTShippingTrckInfoData> shippingTrackDataSet = new HashSet<JnjGTShippingTrckInfoData>();
						final JnjGTShippingTrckInfoData jnjGTShippingTrckInfoData = new JnjGTShippingTrckInfoData();
						jnjGTShippingTrckInfoData.setTrackingId(trackingNumber);
						jnjGTShippingTrckInfoData.setTrackingUrl(trackingUrl);
						shippingTrackDataSet.add(jnjGTShippingTrckInfoData);
						shippingTrckInfoDataMap.put(Jnjb2bCoreConstants.EMPTY_STRING, shippingTrackDataSet);
					}
				}
				target.setActualDeliveryDate(shippingDetail.getActualDeliveryDate());
				target.setActualShipDate(shippingDetail.getActualShipDate());
				packingListDetails.put(shippingDetail.getDeliveryNum(), getPackingListUrl(plant));
			}
			target.setTrackingIdList(trackingIdDetails);
			target.setPackingListDetails(packingListDetails);
			target.setBillOfLading(billOfLadings);
			target.setCarrier(carrierList);
			target.setShippingTrackingInfo(shippingTrckInfoDataMap);
			target.setProofOfDelDetails(proofOfDelDetails);
		}
	}

	/***
	 * Populates map corresponding to each shipment line number as key, and <code>jnjGTShippingTrckInfoData</code>
	 * containing tracking number, url and shipping date as the value.
	 * 
	 * @param shippingTrckInfoDataMap
	 * @param shipmentLineNumber
	 * @param trackingNumber
	 * @param trackingUrl
	 * @param shippingDate
	 */

	protected void populateShippingTrckInfoDataMap(final Map<String, Set<JnjGTShippingTrckInfoData>> shippingTrckInfoDataMap,
			final String shipmentLineNumber, final String trackingNumber, final String trackingUrl, final Date shippingDate)
	{
		if (null != shipmentLineNumber && null != trackingNumber)
		{

			Set<JnjGTShippingTrckInfoData> shippingTrackDataSet = null;
			boolean shipmentDataExists = false;

			final JnjGTShippingTrckInfoData jnjGTShippingTrckInfoData = new JnjGTShippingTrckInfoData();
			jnjGTShippingTrckInfoData.setTrackingId(trackingNumber);
			jnjGTShippingTrckInfoData.setShipDate(shippingDate);
			jnjGTShippingTrckInfoData.setTrackingUrl(trackingUrl);
			if (shippingTrckInfoDataMap.containsKey(shipmentLineNumber) && shippingTrckInfoDataMap.get(shipmentLineNumber) != null)
			{
				
				shippingTrackDataSet = shippingTrckInfoDataMap.get(shipmentLineNumber);

				for (final JnjGTShippingTrckInfoData data : shippingTrackDataSet)
				{
					if (trackingNumber.equals(data.getTrackingId()))
					{
						shipmentDataExists = true;
						break;
					}
				}
				if (!shipmentDataExists)
				{
					shippingTrackDataSet.add(jnjGTShippingTrckInfoData);
				}
			}
			else
			{
				shippingTrackDataSet = new HashSet<>();
				shippingTrackDataSet.add(jnjGTShippingTrckInfoData);
				shippingTrckInfoDataMap.put(shipmentLineNumber, shippingTrackDataSet);
				
			}
		}
	}

	/**
	 * Finds the matching <code>AbstractOrderEntryModel</code> based on orderLineNumber and returns plant from it.
	 * 
	 * @param orderLineNumber
	 * @param entries
	 * @return String
	 */
	protected String getPlantFromOrderLine(final String orderLineNumber, final List<AbstractOrderEntryModel> entries,
			final Map<String, Date> entryWithShipDate)
	{
		String plant = null;
		boolean isPlantSet = true;
		for (final AbstractOrderEntryModel orderEntryModel : entries)
		{
			if (isPlantSet && orderEntryModel.getSapOrderlineNumber() != null
					&& orderEntryModel.getSapOrderlineNumber().equals(orderLineNumber))
			{
				plant = orderEntryModel.getPlant();
				isPlantSet = false;
			}
			if (!entryWithShipDate.containsKey(orderEntryModel.getSapOrderlineNumber()))
			{
				for (final JnjDeliveryScheduleModel jnjDelSchModel : orderEntryModel.getDeliverySchedules())
				{
					if (null != jnjDelSchModel.getShipDate())
					{
						entryWithShipDate.put(orderEntryModel.getSapOrderlineNumber(), jnjDelSchModel.getShipDate());
					}
				}
			}
		}
		return plant;
	}

	/**
	 * Populates and returns URL for Packing List, through logical rule (whether SDC or MLC based URL) based on the plant
	 * value.
	 * 
	 * @return String
	 */
	public String getPackingListUrl(final String plant)
	{

		/* To be uncommented on SAP Integration
		 * 
		 * final List<String> packingListSdcDcValues = JnJCommonUtil.getValues(PackingList.SDC_SERVER_DC_VALUES_KEY,
				Jnjb2bCoreConstants.SYMBOl_COMMA);

		final List<String> packingListMlcDcValues = JnJCommonUtil.getValues(PackingList.MLC_SERVER_DC_VALUES_KEY,
				Jnjb2bCoreConstants.SYMBOl_COMMA);

		String param1 = null;
		String repUser = null;
		String url = null;
		String param6 = null;
		if (packingListSdcDcValues.contains(plant))
		{
			param1 = Config.getParameter(PackingList.PARAM_ENV_SDC_KEY);
			repUser = Config.getParameter(PackingList.SDC_REPUSER_KEY);
			url = Config.getParameter(PackingList.SDC_URL_KEY);
			param6 = Config.getParameter(PackingList.SDC_PARAM_SERVER_KEY);
		}
		else if (packingListMlcDcValues.contains(plant))
		{
			param1 = Config.getParameter(PackingList.PARAM_ENV_MLC_KEY);
			repUser = Config.getParameter(PackingList.MLC_REPUSER_KEY);
			url = Config.getParameter(PackingList.MLC_URL_KEY);
			param6 = Config.getParameter(PackingList.MLC_PARAM_SERVER_KEY);
		}
		else
		{
			return HASH_LINK;
		}
		final String param2 = Config.getParameter(PackingList.PARAM_DESFORMAT_KEY);
		final String param3 = Config.getParameter(PackingList.PARAM_DESTYPE_KEY);
		final String param4 = Config.getParameter(PackingList.PARAM_DISTRIBUTION_KEY);
		final String param5 = Config.getParameter(PackingList.PARAM_REPORT_KEY);
		final String param7 = Config.getParameter(PackingList.PARAM_STATUS_FORMAT_KEY);
		final String orderNumberParam = Config.getParameter(PackingList.PARAM_ORD_NUM_KEY);

		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url).append(PARAM_QUESTION).append(repUser).append(AMPERSAND).append(param1).append(AMPERSAND)
				.append(param2).append(AMPERSAND).append(param3).append(AMPERSAND).append(param4).append(AMPERSAND).append(param5)
				.append(AMPERSAND).append(param6).append(AMPERSAND).append(param7).append(AMPERSAND).append(orderNumberParam);


		return stringBuilder.toString();*/
		//remove the return stmt on SAP Integration
		return HASH_LINK;
	}

	/**
	 * Builds tracking url based on the tracking number, tracking type code and associated multiple parameters.
	 * 
	 * @param jnjGTShippingDetailsModel
	 * @param plantCode
	 * @param zipCode
	 * @return String
	 */


	public String getTrackingUrl(final JnjGTShippingDetailsModel jnjGTShippingDetailsModel, final String plantCode,
			final String zipCode, final JnjGTShippingLineDetailsModel lineDetail)
	{
		/*To be uncommented on SAP Integration*/
		  JnjGTShipmentTrackingURLsModel shipmentTrackingUrlModel = null;
		if (!StringUtils.isEmpty(jnjGTShippingDetailsModel.getCarrierScacCde()))
		
		{
			final JnjGTShipmentTrackingURLsModel exampleShippingModel = new JnjGTShipmentTrackingURLsModel();
			exampleShippingModel.setFreightCarrCode(jnjGTShippingDetailsModel.getCarrierScacCde());

			try
			{
				shipmentTrackingUrlModel = (JnjGTShipmentTrackingURLsModel) flexibleSearchService.getModelByExample(exampleShippingModel);
			}

			catch (final ModelNotFoundException exception)
			{
				return HASH_LINK;
			}
		}

		if (shipmentTrackingUrlModel != null)
		{
			final String billOfLading = jnjGTShippingDetailsModel.getBolNum();
			final String currentSite = (String) sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			final boolean isMddSite = Jnjb2bCoreConstants.MDD.equals(currentSite) ? true : false;


			//*** If MDD site, pick the first tracking number else pick the tracking number from header level. ***//*
			String trackingNum = null;
			if (isMddSite)
			{
				if (lineDetail.getTrackingNum() != null)
				{
					trackingNum = lineDetail.getTrackingNum();
				}
			}
			else
			{
				trackingNum = jnjGTShippingDetailsModel.getTrackingNum();
			}

			final StringBuilder url = new StringBuilder();
			url.append(shipmentTrackingUrlModel.getFreightCarrURL()).append(AMPERSAND)
					.append(shipmentTrackingUrlModel.getTrackingName());

			/** Based on tracking type code value, append and build the Tracking URL ***/
			if (shipmentTrackingUrlModel.getTrackingTypeCode() != null)
			{
				switch (Integer.valueOf(shipmentTrackingUrlModel.getTrackingTypeCode()).intValue())
				{
					case 1:
						url.append(EQUAL + billOfLading);
						break;

					case 3:
						url.append(EQUAL + billOfLading.substring(2));
						break;

					case 4:
						if (plantCode != null && MLC_PLANT.equals(plantCode))
						{
							final int length = billOfLading.length();
							url.append(EQUAL + billOfLading.substring(3, length - 1) + '0');
						}
						else
						{
							url.append(EQUAL + billOfLading);
						}
						break;

					default:
						url.append(EQUAL + trackingNum);
						break;
				}
			}

			if (shipmentTrackingUrlModel.getParam1TypeCode() != null
					&& Integer.valueOf(shipmentTrackingUrlModel.getParam1TypeCode()).intValue() > 0)
			{
				final int param1TypeCode = Integer.valueOf(shipmentTrackingUrlModel.getParam1TypeCode()).intValue();
				final String param1Name = shipmentTrackingUrlModel.getParam1Name();

				url.append(AMPERSAND).append(param1Name);


				
				if (param1TypeCode == 1)
				{
					url.append(EQUAL + zipCode);
				}
				else if (param1TypeCode == 3)
				{
					final Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.DAY_OF_MONTH, -14);
					final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
					final String time = sdf.format(calendar.getTime());

					url.append(EQUAL + time);
				}

			}

			if (shipmentTrackingUrlModel.getParam2TypeCode() != null
					&& Integer.valueOf(shipmentTrackingUrlModel.getParam2TypeCode()).intValue() > 0
					&& Integer.valueOf(shipmentTrackingUrlModel.getParam2TypeCode()).intValue() == 4)
			{
				final String param2Name = shipmentTrackingUrlModel.getParam2Name();
				url.append(AMPERSAND).append(param2Name);

				final Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, 5);
				final SimpleDateFormat sdf = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
				final String time = sdf.format(calendar.getTime());

				url.append(EQUAL + time);
			}

			return url.toString();
		}

		return HASH_LINK;
	}

	protected String maskPaymentCardNumber(final String cardNumber)
	{
		final String maskedCardNumber = Jnjb2bCoreConstants.CREDIT_CARD_MASKING + cardNumber;
		return maskedCardNumber;

	}

	/**
	 * @return the b2bPaymentTypeConverter
	 */
	public Converter<CheckoutPaymentType, B2BPaymentTypeData> getB2bPaymentTypeConverter()
	{
		return b2bPaymentTypeConverter;
	}

	/**
	 * @param b2bPaymentTypeConverter
	 *           the b2bPaymentTypeConverter to set
	 */
	public void setB2bPaymentTypeConverter(final Converter<CheckoutPaymentType, B2BPaymentTypeData> b2bPaymentTypeConverter)
	{
		this.b2bPaymentTypeConverter = b2bPaymentTypeConverter;
	}

	/**


	 * @param jnjGTSurgeryPopulator
	 *           the jnjGTSurgeryPopulator to set
	 */

	public void setJnjGTSurgeryPopulator(final JnjGTSurgeryPopulator jnjGTSurgeryPopulator)
	{

		this.jnjGTSurgeryPopulator = jnjGTSurgeryPopulator;
	}

	/**
	 * @param isMddSite
	 *           the isMddSite to set
	 */
	public void setMddSite(final boolean isMddSite)
	{
		this.isMddSite = isMddSite;
	}

}
