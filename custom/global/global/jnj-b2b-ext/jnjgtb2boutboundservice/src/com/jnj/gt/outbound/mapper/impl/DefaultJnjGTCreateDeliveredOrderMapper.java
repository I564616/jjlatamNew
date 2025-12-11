/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersInput;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.CreateDeliveredOrdersOutput;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.OutOrderLines;
import com.jnj.hcswmd01.mu007_epic_createdelorder_v1.createdeliveredorderswebservice.ScheduledLines;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTCreateDelOrderResponseData;
import com.jnj.core.data.JnjGTOrderChangeResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.model.JnjGTSurgeryInfoModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTCreateDeliveredOrderMapper;
import com.jnj.gt.outbound.mapper.JnjGTReqOrderChangeMapper;
import com.jnj.gt.outbound.services.JnjGTCreateDeliveredOrderService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * The JnjGTCreateDeliveredOrderMapperImpl class contains the definition of all the method of the
 * JnjGTCreateDeliveredOrderMapper interface.
 *
 * @author Accenture
 * @version 1.0
 *
 */
public class DefaultJnjGTCreateDeliveredOrderMapper implements JnjGTCreateDeliveredOrderMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateDeliveredOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	private JnjGTCreateDeliveredOrderService jnjGTCreateDeliveredOrderService;

	@Autowired
	private JnjCartService jnjCartService;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	private JnjGTReqOrderChangeMapper jnjGTReqOrderChangeMapper;

	@Autowired
	protected JnjGTOrderService jnjGTOrderService;

	@Autowired
	protected ModelService modelService;

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjGTCreateDeliveredOrderService getJnjGTCreateDeliveredOrderService() {
		return jnjGTCreateDeliveredOrderService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public JnjGTReqOrderChangeMapper getJnjGTReqOrderChangeMapper() {
		return jnjGTReqOrderChangeMapper;
	}

	public JnjGTOrderService getJnjGTOrderService() {
		return jnjGTOrderService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	protected Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = null;
	
	/**
	 * {!{@inheritDoc}
	 *
	 * @throws IntegrationException
	 * @throws BusinessException
	 */
	@Override
	public JnjGTCreateDelOrderResponseData mapCreateDelOrderRequestResponse(final OrderModel orderModel,
			final JnjGTSapWsData sapWsData) throws IntegrationException, SystemException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCreateDelOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTCreateDelOrderResponseData jnjGTCreateDelOrderResponseData = new JnjGTCreateDelOrderResponseData();

		final CreateDeliveredOrdersInput orderCreationInSAPInput = new CreateDeliveredOrdersInput();
		if (null != orderModel.getOrderType())
		{
			orderCreationInSAPInput.setInOrderType(orderModel.getOrderType().getCode());
			if (StringUtils.equals(JnjOrderTypesEnum.ZKB.getCode(), orderModel.getOrderType().getCode()))
			{
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.ORDER_REASON, Jnjgtb2boutboundserviceConstants.ORDER_REASON_FOR_REPLENISHMENT);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					orderCreationInSAPInput.setInOrderReason(objectFactory
							.createCreateDeliveredOrdersInputInOrderReason(jnjConfigModels.get(0).getValue()));
				}
				orderCreationInSAPInput.setInReferenceOrder(objectFactory.createCreateDeliveredOrdersInputInReferenceOrder(orderModel
						.getAssociatedSAPOrderNum()));
				final JAXBElement<String> holdCode = objectFactory.createCreateDeliveredOrdersInputInHoldCode(jnjConfigService
						.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.HOLD_CODE));
				orderCreationInSAPInput.setInHoldCode(holdCode);
				// check for the empty value.
				if (StringUtils.isNotEmpty(orderModel.getDropShipAccount()))
				{
					//Send the PO as Dealer PO as per Yesha's Input for replenish drop ship order
					final JAXBElement<String> dealerPO = objectFactory.createCreateDeliveredOrdersInputInDealerPONumber(orderModel
							.getPurchaseOrderNumber());
					orderCreationInSAPInput.setInDealerPONumber(dealerPO);
				}
			}
			else
			{
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.ORDER_REASON, Jnjgtb2boutboundserviceConstants.ORDER_REASON_FOR_DELIVERED);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					orderCreationInSAPInput.setInOrderReason(objectFactory
							.createCreateDeliveredOrdersInputInOrderReason(jnjConfigModels.get(0).getValue()));
				}
				orderCreationInSAPInput.setInHoldCode(objectFactory.createCreateDeliveredOrdersInputInHoldCode(jnjConfigService
						.getConfigValueById(Jnjgtb2boutboundserviceConstants.EMPTY_STRING)));
				// check for the empty value.
				if (StringUtils.isNotEmpty(orderModel.getDealerPONum()))
				{
					final JAXBElement<String> dealerPO = objectFactory.createCreateDeliveredOrdersInputInDealerPONumber(orderModel
							.getDealerPONum());
					orderCreationInSAPInput.setInDealerPONumber(dealerPO);
				}
			}
		}
		if (null != orderModel.getUnit())
		{
			orderCreationInSAPInput.setInSoldToNumber(orderModel.getUnit().getUid());
		}
		if (null != orderModel.getDeliveryAddress())
		{
			orderCreationInSAPInput.setInShipToNumber(orderModel.getDeliveryAddress().getJnJAddressId());
		}
		if (null != orderModel.getPaymentAddress())
		{
			final JAXBElement<String> billToNumber = objectFactory.createCreateDeliveredOrdersInputInBillToNumber(orderModel
					.getPaymentAddress().getJnJAddressId());
			orderCreationInSAPInput.setInBillToNumber(billToNumber);
		}
		// check for the not empty or not null
		if (BooleanUtils.isTrue(orderModel.getThirdpartyBilling()) || StringUtils.isNotEmpty(orderModel.getDropShipAccount()))
		{
			orderCreationInSAPInput.setInDropShipIndicator(Jnjgtb2boutboundserviceConstants.Y_STRING);
		}
		else
		{
			orderCreationInSAPInput.setInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		if (StringUtils.isNotEmpty(orderModel.getSpineSalesRepUCN()))
		{
			final JAXBElement<String> salesRepUcn = objectFactory.createCreateDeliveredOrdersInputInSalesRepUCN(orderModel
					.getSpineSalesRepUCN());
			orderCreationInSAPInput.setInSalesRepUCN(salesRepUcn);
		}
		orderCreationInSAPInput.setInStockPartnerHdr(objectFactory
				.createCreateDeliveredOrdersInputInStockPartnerHdr(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		orderCreationInSAPInput.setInGatewayRelatedOrdernumber(objectFactory
				.createCreateDeliveredOrdersInputInGatewayRelatedOrdernumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		orderCreationInSAPInput.setInGatewayOrdernumber(orderModel.getCode());
		orderCreationInSAPInput.setInPONumber(orderModel.getPurchaseOrderNumber());
		// Fetch value from the config table.
		orderCreationInSAPInput.setInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));
		orderCreationInSAPInput.setInOrderSource(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_SOURCE));

		if (StringUtils.isNotEmpty(orderModel.getAttention()))
		{
			orderCreationInSAPInput.setInCustomerNotes(objectFactory.createCreateDeliveredOrdersInputInCustomerNotes(orderModel
					.getAttention()));
		}
		else
		{
			orderCreationInSAPInput.setInCustomerNotes(objectFactory
					.createCreateDeliveredOrdersInputInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}

		orderCreationInSAPInput.setInRequestedDeliveryDate(objectFactory
				.createCreateDeliveredOrdersInputInRequestedDeliveryDate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		if (null != orderModel.getUser())
		{
			// check for the non empty value if its empty or null then set empty value.
			orderCreationInSAPInput.setInContactName(StringUtils.isNotEmpty(orderModel.getUser().getName()) ? orderModel.getUser()
					.getName() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);

			// Iterates the Address Model to find the contact address and get phone1 value from it.
			for (final AddressModel addressModel : orderModel.getUser().getAddresses())
			{
				if (null != addressModel && null != addressModel.getContactAddress()
						&& addressModel.getContactAddress().booleanValue())
				{
					orderCreationInSAPInput.setInContactPhoneNumber(addressModel.getPhone1());
					break;
				}
			}
		}
		// Set info related to Credit Card
		if (null != orderModel.getPaymentInfo())
		{
			final JnjGTCreditCardModel jnjGTCreditCardModel = (JnjGTCreditCardModel) orderModel.getPaymentInfo();
			final JAXBElement<String> cardSequenceNumber = objectFactory
					.createCreateDeliveredOrdersInputInCreditCardSequenceNumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			orderCreationInSAPInput.setInCreditCardSequenceNumber(cardSequenceNumber);
			if (null != jnjGTCreditCardModel.getType())
			{
				final String crediCardType = StringUtils.substring(jnjGTCreditCardModel.getType().getCode(), 2);
				final JAXBElement<String> cardType = objectFactory.createCreateDeliveredOrdersInputInCreditCardType(crediCardType);
				orderCreationInSAPInput.setInCreditCardType(cardType);
			}
			if (null != jnjGTCreditCardModel.getCreditCardtoken())
			{
				final JAXBElement<String> cardAccountNo = objectFactory
						.createCreateDeliveredOrdersInputInCreditCardAccountNumber(jnjGTCreditCardModel.getCreditCardtoken());
				orderCreationInSAPInput.setInCreditCardAccountNumber(cardAccountNo);
			}
			if (StringUtils.isNotEmpty(jnjGTCreditCardModel.getValidToMonth())
					&& StringUtils.isNotEmpty(jnjGTCreditCardModel.getValidToYear()))
			{
				final String expirationYear = Config.getParameter(Jnjgtb2boutboundserviceConstants.CREDIT_CARD_YEAR_INITIAL).concat(
						jnjGTCreditCardModel.getValidToYear());
				final Calendar calendar = new GregorianCalendar(Integer.parseInt(expirationYear),
						Integer.parseInt(jnjGTCreditCardModel.getValidToMonth()), 0);
				final Date formattedDate = calendar.getTime();

				// Format the Date in requested date format and set it in the expire field.
				final JAXBElement<String> expireDate = objectFactory
						.createCreateDeliveredOrdersInputInCreditCardExpirationCard(new SimpleDateFormat(Config
								.getParameter(Jnjgtb2boutboundserviceConstants.REQUEST_DATE_FORMAT)).format(formattedDate));
				orderCreationInSAPInput.setInCreditCardExpirationCard(expireDate);
			}
		}
		// Map the surgeon related data in input request object.
		if (null != orderModel.getSurgeryInfo())
		{
			final JnjGTSurgeryInfoModel jnjGTSurgeryInfoModel = orderModel.getSurgeryInfo();
			mapSurgeonDataWithInputRequest(orderCreationInSAPInput, jnjGTSurgeryInfoModel, orderModel.getSurgeon());
		}

		if (CollectionUtils.isNotEmpty(orderModel.getEntries()))
		{
			orderCreationInSAPInput.getInOrderLines().addAll(mapOrderLinesFromAbstOrderEntries(orderModel.getEntries()));
		}
//		final CreateDeliveredOrdersOutput orderCreationInSAPOutput = jnjNACreateDeliveredOrderService.createDeliveredOrders(
//		orderCreationInSAPInput, sapWsData);
		final CreateDeliveredOrdersOutput orderCreationInSAPOutput = new CreateDeliveredOrdersOutput();
		// if We hit the SAP webservice for Replenishment order then enter inside if block.
		if (null != orderCreationInSAPOutput
				&& CollectionUtils.isNotEmpty(orderCreationInSAPOutput.getOutOrderLines())
				&& CollectionUtils.isNotEmpty(orderModel.getEntries())
				&& (null == orderCreationInSAPOutput.getErrorMessage() || StringUtils.isEmpty(orderCreationInSAPOutput
						.getErrorMessage().getValue()))
				&& StringUtils.equals(JnjOrderTypesEnum.ZKB.getCode(), orderModel.getOrderType().getCode()))
		{
			// Calls the request order change service.
			final JnjGTOrderChangeResponseData jnjGTOrderChangeResponseData = jnjGTReqOrderChangeMapper
					.mapChangeOrderRequestResponse(orderModel, orderCreationInSAPOutput.getOutSalesOrderNumber(), true); //sap call
			if (jnjGTOrderChangeResponseData.isSapResponseStatus())
			{
				orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
				mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
				//Calculate cart for total values and save in data base
				jnjGTCreateDelOrderResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModel, true));
				jnjGTCreateDelOrderResponseData.setSapResponseStatus(true);
			}
			else
			{
				jnjGTCreateDelOrderResponseData.setSapResponseStatus(false);
				jnjGTCreateDelOrderResponseData.setErrorMessage(jnjGTOrderChangeResponseData.getErrorMessage());
			}
		}
		// if We hit the SAP webservice for Delivered order then enter inside if block.
		else if (null != orderCreationInSAPOutput
				&& CollectionUtils.isNotEmpty(orderCreationInSAPOutput.getOutOrderLines())
				&& CollectionUtils.isNotEmpty(orderModel.getEntries())
				&& (null == orderCreationInSAPOutput.getErrorMessage() || StringUtils.isEmpty(orderCreationInSAPOutput
						.getErrorMessage().getValue())))
		{
			orderModel.setSapOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			orderModel.setOrderNumber(orderCreationInSAPOutput.getOutSalesOrderNumber());
			mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
			//Calculate cart for total values and save in data base
			jnjGTCreateDelOrderResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModel, true));
			jnjGTCreateDelOrderResponseData.setSapResponseStatus(true);
		}
		else if (null != orderCreationInSAPOutput && null != orderCreationInSAPOutput.getErrorMessage())
		{
			// check for the excluded customer check.
			mapOrderModelFromOutOrderLine(orderModel, orderCreationInSAPOutput);
			jnjGTCreateDelOrderResponseData.setSapResponseStatus(false);
			jnjGTCreateDelOrderResponseData.setErrorMessage(orderCreationInSAPOutput.getErrorMessage().getValue());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCreateDelOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTCreateDelOrderResponseData;
	}

	/**
	 * Map in order lines fields from abstract order entries model.
	 *
	 * @param abstOrdEntModelList
	 *           the abstract order entry model list
	 * @return the array of in order lines
	 */
	protected Collection<InOrderLines> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Collection<InOrderLines> inOrderLineList = new ArrayList<InOrderLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InOrderLines inOrderLines = new InOrderLines();
			final ProductModel productModel = abstOrderEntryModel.getProduct();
			inOrderLines.setMaterialNumber(productModel.getCode());
			inOrderLines.setQuantityRequested(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inOrderLines.setSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getCustLineNumber()))
			{
				inOrderLines.setCustomerLineNumber(objectFactory.createInOrderLinesCustomerLineNumber(abstOrderEntryModel
						.getCustLineNumber()));
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getLot()))
			{
				final JAXBElement<String> lotJaxbElement = objectFactory.createInOrderLinesLot(abstOrderEntryModel.getLot());
				inOrderLines.setLot(lotJaxbElement);
			}
			else
			{
				final JAXBElement<String> lotJaxbElement = objectFactory
						.createInOrderLinesLot(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setLot(lotJaxbElement);
			}
			// if the selected route is null or empty and it's not equal to route which comes from sap then set the route, shipping point and plant else set it empty.
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getSelectedRoute()))
			{
				final JAXBElement<String> route = objectFactory.createInOrderLinesRoute(abstOrderEntryModel.getSelectedRoute());
				inOrderLines.setRoute(route);
				if (StringUtils.isNotEmpty(abstOrderEntryModel.getShippingPoint()))
				{
					final JAXBElement<String> shippingPoint = objectFactory.createInOrderLinesShippingPoint(abstOrderEntryModel
							.getShippingPoint());
					inOrderLines.setShippingPoint(shippingPoint);
				}
				if (StringUtils.isNotEmpty(abstOrderEntryModel.getPlant()))
				{
					final JAXBElement<String> plant = objectFactory.createInOrderLinesPlant(abstOrderEntryModel.getPlant());
					inOrderLines.setPlant(plant);
				}
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.UNLOADING_POINT_ID, Jnjgtb2boutboundserviceConstants.UNLOADING_POINT_KEY);
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					final JAXBElement<String> unloadingPoint = objectFactory.createInOrderLinesUnloadingPoint(jnjConfigModels.get(0)
							.getValue());
					inOrderLines.setUnloadingPoint(unloadingPoint);
				}
			}
			else
			{
				final JAXBElement<String> route = objectFactory
						.createInOrderLinesRoute(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setRoute(route);
				final JAXBElement<String> shippingPoint = objectFactory
						.createInOrderLinesShippingPoint(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setShippingPoint(shippingPoint);
				final JAXBElement<String> plant = objectFactory
						.createInOrderLinesPlant(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPlant(plant);
				final JAXBElement<String> unloadingPoint = objectFactory
						.createInOrderLinesUnloadingPoint(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setUnloadingPoint(unloadingPoint);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getBatchNum()))
			{
				final JAXBElement<String> batch = objectFactory.createInOrderLinesBatch(abstOrderEntryModel.getBatchNum());
				inOrderLines.setBatch(batch);
			}
			else
			{
				final JAXBElement<String> batch = objectFactory
						.createInOrderLinesBatch(getBatchForCodmanNSpine((JnJProductModel) productModel));
				//final JAXBElement<String> batch = objectFactory
				//	.createInOrderLinesBatch(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setBatch(batch);
			}

			final JAXBElement<String> stockPartner = objectFactory.createInOrderLinesStockPartner(abstOrderEntryModel
					.getSpecialStockPartner());
			inOrderLines.setStockPartner(stockPartner);

			if (null != abstOrderEntryModel.getFreightFees())
			{
				final JAXBElement<String> freightFees = objectFactory.createInOrderLinesFreight(String.valueOf(abstOrderEntryModel
						.getFreightFees().doubleValue()));
				inOrderLines.setUnloadingPoint(freightFees);
			}
			else
			{
				final JAXBElement<String> freightFees = objectFactory.createInOrderLinesFreight(String
						.valueOf(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
				inOrderLines.setUnloadingPoint(freightFees);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverride()))
			{
				final JAXBElement<String> priceOverrideAmount = objectFactory
						.createInOrderLinesPriceOverrideAmount(abstOrderEntryModel.getPriceOverride());
				inOrderLines.setPriceOverrideAmount(priceOverrideAmount);
			}
			else
			{
				final JAXBElement<String> priceOverrideAmount = objectFactory
						.createInOrderLinesPriceOverrideAmount(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPriceOverrideAmount(priceOverrideAmount);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverrideApprover()))
			{
				final JAXBElement<String> priceOverrideApprover = objectFactory
						.createInOrderLinesPriceOverrideApprover(abstOrderEntryModel.getPriceOverrideApprover());
				inOrderLines.setPriceOverrideApprover(priceOverrideApprover);
			}
			else
			{
				final JAXBElement<String> priceOverrideApprover = objectFactory
						.createInOrderLinesPriceOverrideApprover(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPriceOverrideApprover(priceOverrideApprover);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverrideReason()))
			{
				final JAXBElement<String> priceOverrideReason = objectFactory
						.createInOrderLinesPriceOverrideReason(abstOrderEntryModel.getPriceOverrideReason());
				inOrderLines.setPriceOverrideReason(priceOverrideReason);
			}
			else
			{
				final JAXBElement<String> priceOverrideReason = objectFactory
						.createInOrderLinesPriceOverrideReason(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPriceOverrideReason(priceOverrideReason);
			}

			// add the object in the list
			inOrderLineList.add(inOrderLines);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return inOrderLineList;
	}


	/**
	 * Map order model from out order line.
	 *
	 * @param orderModel
	 *           the order model
	 * @param orderCreationInSAPOutput
	 *           the order creation in sap output
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */
	protected void mapOrderModelFromOutOrderLine(final OrderModel orderModel,
			final CreateDeliveredOrdersOutput orderCreationInSAPOutput) throws SystemException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalNetValue = 0.0;
		double totalTax = 0.0;
		double dropShipFee = 0.0;
		double totalFreightFees = 0.0;
		double minimumOrderFee = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		final CurrencyModel currencyModel = orderModel.getCurrency();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		CatalogVersionModel catalogVersionModel = null;
		mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : orderModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}

		final List<String> itemCategories = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.ITEM_CATEGORY_FOR_MDD,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> lineNumberExcluded = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.LINE_NUMBER_EXCLUDED,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// To fetch active Catalog Version Model.
		if (null != orderModel.getSite() && CollectionUtils.isNotEmpty(orderModel.getSite().getStores()))
		{
			if (CollectionUtils.isNotEmpty(orderModel.getSite().getStores().get(0).getCatalogs()))
			{
				catalogVersionModel = orderModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}
		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (final OutOrderLines outOrderLine : orderCreationInSAPOutput.getOutOrderLines())
		{
			// In case of excluded customer, enter inside if block.
			if (null != outOrderLine && null != outOrderLine.getLineNumber()
					&& !lineNumberExcluded.contains(outOrderLine.getLineNumber().getValue()))
			{
				try
				{
					if (null != outOrderLine.getItemCategory()
							&& !(itemCategories.contains(outOrderLine.getItemCategory().getValue()))
							&& null != outOrderLine.getMaterialNumber() && null != outOrderLine.getMaterialEntered())
					{
						AbstractOrderEntryModel abstOrdEntryModel = null;
						JnJProductModel product = null;
						JnJProductModel productEntered = null;
						ProductModel baseProduct = null;
						ProductModel baseProductEntered = null;
						if (StringUtils.equals(outOrderLine.getMaterialNumber().getValue(), outOrderLine.getMaterialEntered()
								.getValue()))
						{
							product = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							if (null != product)
							{
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
								baseProductEntered = baseProduct;
							}
						}
						else
						{
							product = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialNumber().getValue(),
									catalogVersionModel);
							productEntered = jnJGTProductService.getProductModelByCode(outOrderLine.getMaterialEntered().getValue(),
									catalogVersionModel);
							if (null != product)
							{
								baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
							}
							if (null != productEntered)
							{
								baseProductEntered = productEntered.getMaterialBaseProduct() == null ? productEntered : productEntered
										.getMaterialBaseProduct();
							}
						}
						// Check if the base product entered is not null.
						if (null != baseProductEntered && null != baseProduct)
						{
							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProductEntered.getCode()))
							{
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProductEntered.getCode());
							abstOrdEntryModel.setProduct(baseProduct);
							// check for the null value and set it in Sap Order Line Number.
							if (null != outOrderLine.getLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(outOrderLine.getLineNumber().getValue());
							}
							if (null != outOrderLine.getBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outOrderLine.getBaseUOM().getValue());
								abstOrdEntryModel.setBaseUOM(unitModel);
							}
							if (null != outOrderLine.getMessage())
							{
								abstOrdEntryModel.setMessage(outOrderLine.getMessage().getValue());
							}
							if (null != outOrderLine.getSubstitutionReason())
							{
								abstOrdEntryModel.setSubstitutionReason(outOrderLine.getSubstitutionReason().getValue());
							}
							if (null != outOrderLine.getSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outOrderLine.getSalesUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
								if (CollectionUtils.isNotEmpty(baseProduct.getVariants()))
								{
									// Get the Variant Product Models of the base product.
									final List<VariantProductModel> variantProductModels = (List) baseProduct.getVariants();
									// Iterate them one by one.
									for (final VariantProductModel variantProductModel : variantProductModels)
									{
										// Check it for not null value for the models and equate the unit model code of the Variant model with the incoming response sales uom.
										if (null != variantProductModel
												&& null != variantProductModel.getUnit()
												&& variantProductModel.getUnit().getCode()
														.equalsIgnoreCase(outOrderLine.getSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}

							if (null != outOrderLine.getDiscounts() && StringUtils.isNotEmpty(outOrderLine.getDiscounts().getValue()))
							{
								abstOrdEntryModel.setDiscountValues(createDiscountValues(
										(Double.valueOf(outOrderLine.getDiscounts().getValue())), currencyModel));
								totalDiscounts = totalDiscounts + Double.valueOf(outOrderLine.getDiscounts().getValue()).doubleValue();
							}
							if (null != outOrderLine.getTax() && StringUtils.isNotEmpty(outOrderLine.getTax().getValue()))
							{
								abstOrdEntryModel.setTaxValues(createTaxValues((Double.valueOf(outOrderLine.getTax().getValue())),
										currencyModel));

								totalTax = totalTax + Double.valueOf(outOrderLine.getTax().getValue()).doubleValue();
							}
							if (null != outOrderLine.getHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(outOrderLine.getHigherLevelItemNumber().getValue());
							}

							abstOrdEntryModel.setItemCategory(outOrderLine.getItemCategory().getValue());
							if (null != outOrderLine.getRoute())
							{
								abstOrdEntryModel.setRoute(outOrderLine.getRoute().getValue());
							}
							if (null != outOrderLine.getShippingPoint())
							{
								abstOrdEntryModel.setShippingPoint(outOrderLine.getShippingPoint().getValue());
							}
							if (null != outOrderLine.getPriceType())
							{
								abstOrdEntryModel.setPriceType(outOrderLine.getPriceType().getValue());
							}
							if (null != outOrderLine.getHSAPromotion()
									&& StringUtils.isNotEmpty(outOrderLine.getHSAPromotion().getValue()))
							{
								abstOrdEntryModel.setHsaPromotion(Double.valueOf(outOrderLine.getHSAPromotion().getValue()));
								hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
							}
							if (null != outOrderLine.getFreightAndHandling()
									&& StringUtils.isNotEmpty(outOrderLine.getFreightAndHandling().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outOrderLine.getFreightAndHandling().getValue()));
								totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
							}
							if (null != outOrderLine.getDropShipFee()
									&& StringUtils.isNotEmpty(outOrderLine.getDropShipFee().getValue()))
							{
								abstOrdEntryModel.setDropshipFee(Double.valueOf(outOrderLine.getDropShipFee().getValue()));
								dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
							}
							if (null != outOrderLine.getFeeMin() && StringUtils.isNotEmpty(outOrderLine.getFeeMin().getValue()))
							{
								abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(outOrderLine.getFeeMin().getValue()));
								minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
							}
							if (null != outOrderLine.getNetValue() && StringUtils.isNotEmpty(outOrderLine.getNetValue().getValue()))
							{
								abstOrdEntryModel.setNetPrice(Double.valueOf(outOrderLine.getNetValue().getValue()));
								totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
							}
							if (null != outOrderLine.getStockPartner()
									&& StringUtils.isNotEmpty(outOrderLine.getStockPartner().getValue()))
							{
								abstOrdEntryModel.setSpecialStockPartner(outOrderLine.getStockPartner().getValue());
							}
							// To set the schedule lines information in order model.
							if (null != outOrderLine.getScheduledLines())
							{
								long quantity = 0;
								if (CollectionUtils.isNotEmpty(abstOrdEntryModel.getDeliverySchedules()))
								{
									try
									{
										modelService.removeAll(abstOrdEntryModel.getDeliverySchedules());
									}
									catch (final ModelRemovalException exception)
									{
										LOGGER.error(exception.getMessage());
										abstOrdEntryModel.setDeliverySchedules(null);
									}
								}
								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues(
										Jnjgtb2boutboundserviceConstants.EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								for (final ScheduledLines scheduledLines : outOrderLine.getScheduledLines())
								{
									// Check for the not null object
									if (null != scheduledLines)
									{
										final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
										jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
										if (null != scheduledLines.getLineNumber())
										{
											jnjDelSchModel.setLineNumber(scheduledLines.getLineNumber().getValue());
										}
										if (null != scheduledLines.getScheduledLineNumber())
										{
											jnjDelSchModel.setScheduledLineNumber(scheduledLines.getScheduledLineNumber().getValue());
										}
										if (null != scheduledLines.getLineStatus())
										{
											jnjDelSchModel.setLineStatus(scheduledLines.getLineStatus().getValue());
										}
										if (null != scheduledLines.getQuantity()
												&& StringUtils.isNotEmpty(scheduledLines.getQuantity().getValue()))
										{
											jnjDelSchModel
													.setQty(JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity().getValue()));
											quantity = quantity
													+ (JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity().getValue()).longValue());
										}
										if (null != scheduledLines.getDeliveryDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getDeliveryDate().getValue()))
										{
											jnjDelSchModel.setDeliveryDate(formatResponseDate(scheduledLines.getDeliveryDate().getValue()));
										}// if block loop
										if (null != scheduledLines.getMaterialAvailabilityDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getMaterialAvailabilityDate().getValue()))
										{
											jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(scheduledLines
													.getMaterialAvailabilityDate().getValue()));

										}// if block loop
										if (null != scheduledLines.getShipDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getShipDate().getValue()))
										{
											jnjDelSchModel.setShipDate(formatResponseDate(scheduledLines.getShipDate().getValue()));
										}// if block loop
										jnjDelSchModelList.add(jnjDelSchModel);
									}
								}
								abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
								abstOrdEntryModel.setQuantity(Long.valueOf(quantity));
							}
							if (null != outOrderLine.getBillingDeliveryBlock()
									&& StringUtils.isNotEmpty(outOrderLine.getBillingDeliveryBlock().getValue()))
							{
								abstOrdEntryModel.setBillingDeliveryBlock(outOrderLine.getBillingDeliveryBlock().getValue());
							}
							if (null != outOrderLine.getGrossPrice() && StringUtils.isNotEmpty(outOrderLine.getGrossPrice().getValue())
									&& null != outOrderLine.getAvailableQuantity()
									&& StringUtils.isNotEmpty(outOrderLine.getAvailableQuantity().getValue()))
							{
								abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double.parseDouble(outOrderLine
										.getGrossPrice().getValue()) + Double.parseDouble(outOrderLine.getDiscounts().getValue()))
										/ (abstOrdEntryModel.getQuantity().doubleValue()))));
								abstOrdEntryModel.setTotalPrice(Double.valueOf(Double
										.parseDouble(outOrderLine.getGrossPrice().getValue())
										+ Double.parseDouble(outOrderLine.getDiscounts().getValue())));
								grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
							}
							jnjGTOrderService.populateMddOrderEntryStatus(abstOrdEntryModel);
						}
					}
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
							+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
					throw new SystemException("System Exception throw from the JnjGTCreateDeliveredOrderMapperImpl class",
							MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
				}
			}
			else
			{
				throw new BusinessException();
			}
		}
		// Populates the order level value in order model.
		orderModel.setTotalPrice(Double.valueOf(totalNetValue + totalTax));
		orderModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
		orderModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		orderModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		orderModel.setDeliveryCost(Double.valueOf(totalFreightFees - minimumOrderFee));
		orderModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		orderModel.setSubtotal(Double.valueOf(grossPrice));
		orderModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		orderModel.setTotalOtherCharge(Double.valueOf(totalNetValue - grossPrice - minimumOrderFee - dropShipFee + hsaPromotion));
		orderModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee));
		orderModel.setTotalTax(Double.valueOf(totalTax));
		orderModel.setStatus(OrderStatus.CREATED);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	/**
	 * Sets Discount Values for Order Entry.
	 *
	 * @param discountValue
	 *           the discount value
	 * @param currencyModel
	 *           the currency model
	 * @return the list
	 */
	protected List<DiscountValue> createDiscountValues(final Double discountValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return discountValues;
	}

	/**
	 * Format response date and return the formatted date.
	 *
	 * @param date
	 *           the date
	 * @return the date
	 * @throws SystemException
	 *            the system exception
	 */
	protected Date formatResponseDate(final String date) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Date formattedDate = null;
		try
		{
			if (StringUtils.isNotEmpty(date))
			{
				formattedDate = new SimpleDateFormat(Config.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT))
						.parse(date);
			}
		}
		catch (final ParseException exception)
		{
			LOGGER.error(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ "Parsing Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the jnjGTCreateOrderMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return formattedDate;
	}

	/**
	 * Map surgeon data with input request.
	 *
	 * @param orderCreationInSAPInput
	 *           the order creation in sap input
	 * @param jnjGTSurgeryInfoModel
	 *           the jnj na surgeon model
	 */
	protected void mapSurgeonDataWithInputRequest(final CreateDeliveredOrdersInput orderCreationInSAPInput,
			final JnjGTSurgeryInfoModel jnjGTSurgeryInfoModel, final JnjGTSurgeonModel jnjGTSurgeonModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapSurgeonDataWithInputRequest()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (null != jnjGTSurgeryInfoModel.getCaseDate())
		{
			final JAXBElement<String> caseDate = objectFactory.createCreateDeliveredOrdersInputInCaseDate(new SimpleDateFormat(
					Config.getParameter(Jnjgtb2boutboundserviceConstants.REQUEST_DATE_FORMAT)).format(jnjGTSurgeryInfoModel
					.getCaseDate()));
			orderCreationInSAPInput.setInCaseDate(caseDate);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getInterbody()))
		{
			final JAXBElement<String> interBody = objectFactory.createCreateDeliveredOrdersInputInInterbody(jnjGTSurgeryInfoModel
					.getInterbody());
			orderCreationInSAPInput.setInInterbody(interBody);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getLevelsInstrumented()))
		{
			final JAXBElement<String> levelsInstrumented = objectFactory
					.createCreateDeliveredOrdersInputInLevelsInstrumented(jnjGTSurgeryInfoModel.getLevelsInstrumented());
			orderCreationInSAPInput.setInLevelsInstrumented(levelsInstrumented);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getZone()))
		{
			final JAXBElement<String> zone = objectFactory.createCreateDeliveredOrdersInputInZone(jnjGTSurgeryInfoModel.getZone());
			orderCreationInSAPInput.setInZone(zone);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getOrthobiologics()))
		{
			final JAXBElement<String> orthoBio = objectFactory
					.createCreateDeliveredOrdersInputInOrthobiologics(jnjGTSurgeryInfoModel.getOrthobiologics());
			orderCreationInSAPInput.setInOrthobiologics(orthoBio);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getPathology()))
		{
			final JAXBElement<String> pathology = objectFactory.createCreateDeliveredOrdersInputInPathology(jnjGTSurgeryInfoModel
					.getPathology());
			orderCreationInSAPInput.setInPathology(pathology);
		}
		if (null != jnjGTSurgeonModel && StringUtils.isNotEmpty(jnjGTSurgeonModel.getSurgeonId()))
		{
			final JAXBElement<String> surgeonId = objectFactory.createCreateDeliveredOrdersInputInSurgeonID(jnjGTSurgeonModel
					.getSurgeonId());
			orderCreationInSAPInput.setInSurgeonID(surgeonId);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getSurgerySpecialty()))
		{
			final JAXBElement<String> surgeonSpeciality = objectFactory
					.createCreateDeliveredOrdersInputInSurgeonSpecialty(jnjGTSurgeryInfoModel.getSurgerySpecialty());
			orderCreationInSAPInput.setInSurgeonSpecialty(surgeonSpeciality);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getSurgicalApproach()))
		{
			final JAXBElement<String> surgeonApproach = objectFactory
					.createCreateDeliveredOrdersInputInSurgicalApproach(jnjGTSurgeryInfoModel.getSurgicalApproach());
			orderCreationInSAPInput.setInSurgicalApproach(surgeonApproach);
		}
		if (null != jnjGTSurgeonModel && StringUtils.isNotEmpty(jnjGTSurgeonModel.getFirstName()))
		{
			final JAXBElement<String> surgeonName = objectFactory.createCreateDeliveredOrdersInputInSurgeonName(getSurgeonName(
					jnjGTSurgeonModel.getFirstName(), jnjGTSurgeonModel.getMiddleName(), jnjGTSurgeonModel.getLastName()));
			orderCreationInSAPInput.setInSurgeonName(surgeonName);
		}
		final JAXBElement<String> emailAddress = objectFactory
				.createCreateDeliveredOrdersInputInEmailAddress(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		orderCreationInSAPInput.setInEmailAddress(emailAddress);

		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getCaseNumber()))
		{
			final JAXBElement<String> caseNumber = objectFactory.createCreateDeliveredOrdersInputInCaseNumber(jnjGTSurgeryInfoModel
					.getCaseNumber());
			orderCreationInSAPInput.setInCaseNumber(caseNumber);
		}
		if (StringUtils.isNotEmpty(jnjGTSurgeryInfoModel.getInterbodyFusion()))
		{
			final JAXBElement<String> interbodyFusion = objectFactory
					.createCreateDeliveredOrdersInputInLevelsIBFusion(jnjGTSurgeryInfoModel.getInterbodyFusion());
			orderCreationInSAPInput.setInLevelsIBFusion(interbodyFusion);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "mapSurgeonDataWithInputRequest()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Gets the surgeon name.
	 *
	 * @param firstName
	 *           the first name
	 * @param middleName
	 *           the middle name
	 * @param lastName
	 *           the last name
	 * @return the surgeon name
	 */
	protected String getSurgeonName(final String firstName, final String middleName, final String lastName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "getSurgeonName()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String finalName = StringUtils.EMPTY;

		if (StringUtils.isNotEmpty(firstName))
		{
			finalName = firstName;
		}
		if (StringUtils.isNotEmpty(middleName))
		{
			finalName = finalName.concat(" ").concat(middleName);
		}
		if (StringUtils.isNotEmpty(lastName))
		{
			finalName = finalName.concat(" ").concat(lastName);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "getSurgeonName()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return finalName.trim();
	}

	/**
	 * Sets Tax values for Order Entry.
	 *
	 * @param taxValue
	 *           the tax value
	 * @param currencyModel
	 *           the currency model
	 * @return the collection
	 */
	protected Collection<TaxValue> createTaxValues(final Double taxValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final TaxValue tax = new TaxValue(Jnjgtb2boutboundserviceConstants.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(currencyModel == null) ? null

				: currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<TaxValue>();
		taxValues.add(tax);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_DELIVERED_ORDER + Logging.HYPHEN + "createTaxValues()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return taxValues;
	}

	protected String getBatchForCodmanNSpine(final JnJProductModel baseProduct)
	{
		/*
		 * For spine User if in-field in false and product is not a service fee then we have to set the lot value to DSLOT
		 */
		String batchNum = StringUtils.EMPTY;
		if (BooleanUtils.isFalse(baseProduct.getInFieldInd()))
		{
			final String[] serviceFeeProducts = Config.getString(Jnjb2bCoreConstants.Cart.SERVICE_FEE_PRODUCT_CODES,
					StringUtils.EMPTY).split(Jnjb2bCoreConstants.SYMBOl_COMMA);

			if (Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_SPINE).equals(baseProduct.getSalesOrgCode())
					&& !Arrays.asList(serviceFeeProducts).contains(baseProduct.getCode()))
			{
				batchNum = Config.getParameter(Jnjb2bCoreConstants.Cart.SPINE_DEFAULT_LOT);
			}
			else if (Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_CODMAN).equals(baseProduct.getSalesOrgCode()))
			{
				batchNum = Config.getParameter(Jnjb2bCoreConstants.Cart.CODMAN_DEFAULT_LOT);
			}
		}
		return batchNum;
	}

}
