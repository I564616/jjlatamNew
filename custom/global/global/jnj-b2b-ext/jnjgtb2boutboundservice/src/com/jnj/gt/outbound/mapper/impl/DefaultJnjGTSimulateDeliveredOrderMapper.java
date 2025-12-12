/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.outbound.mapper.JnjGTOutOrderLineMapper;
import com.jnj.hcswmd01.mu007_epic_simulatedelorder_v1.simulatedeliveredorderswebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_simulatedelorder_v1.simulatedeliveredorderswebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_simulatedelorder_v1.simulatedeliveredorderswebservice.TestPricingFromGatewayInput;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ScheduledLines3;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTSimulateDelOrderResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTSimulateDeliveredOrderMapper;
import com.jnj.gt.outbound.services.JnjGTSimulateDeliveredOrderService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * The JnjGTSimulateDeliveredOrderMapperImpl class contains the definition of all the method of the
 * JnjGTSimulateDeliveredOrderMapper interface.
 *
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTSimulateDeliveredOrderMapper implements JnjGTSimulateDeliveredOrderMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSimulateDeliveredOrderMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected JnjCartService jnjCartService;

	@Autowired
	protected JnjGTSimulateDeliveredOrderService jnjGTSimulateDeliveredOrderService;

	@Resource(name = "productService")
	protected
	JnJGTProductService jnJGTProductService;

	@Autowired
	protected JnjGTOrderService jnjGTOrderService;

	@Autowired
	protected ModelService modelService;
	
	@Autowired 
	protected JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper;

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnjGTSimulateDeliveredOrderService getJnjGTSimulateDeliveredOrderService() {
		return jnjGTSimulateDeliveredOrderService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public JnjGTOrderService getJnjGTOrderService() {
		return jnjGTOrderService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * {!{@inheritDoc}
	 *
	 * @throws IntegrationException
	 * @throws SystemException
	 * @throws BusinessException
	 */
	@Override
	public JnjGTSimulateDelOrderResponseData mapDelSimulateOrderRequestResponse(final CartModel cartModel,
			final JnjGTSapWsData wsData) throws IntegrationException, SystemException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapDelSimulateOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isRegional = Boolean.valueOf(Config.getParameter(Jnjb2bCoreConstants.Dropshipment.IS_NON_GLOBAL));
		StringBuilder productCodes = null;
		final JnjGTSimulateDelOrderResponseData jnjGTSimulateDelOrderResponseData = new JnjGTSimulateDelOrderResponseData();
		final TestPricingFromGatewayInput testPricingFromGatewayInput = new TestPricingFromGatewayInput();
		if (null != cartModel.getOrderType())
		{
			testPricingFromGatewayInput.setInOrderType(cartModel.getOrderType().getCode());
		}
		else
		{
			testPricingFromGatewayInput.setInOrderType(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		if (null != cartModel.getUnit())
		{
			testPricingFromGatewayInput.setInSoldToCustomerAddress(cartModel.getUnit().getUid());
		}
		if (null != cartModel.getDeliveryAddress())
		{
			testPricingFromGatewayInput.setInShipToCustomerAddress(cartModel.getDeliveryAddress().getJnJAddressId());
		}
		else
		{
			testPricingFromGatewayInput.setInShipToCustomerAddress(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		}
		// check for the not empty or not null
		if (BooleanUtils.isTrue(cartModel.getThirdpartyBilling()) || StringUtils.isNotEmpty(cartModel.getDropShipAccount()))
		{
			final JAXBElement<String> dropShipIndicator = objectFactory
					.createTestPricingFromGatewayInputInDropShipIndicator(Jnjgtb2boutboundserviceConstants.Y_STRING);
			testPricingFromGatewayInput.setInDropShipIndicator(dropShipIndicator);
		}//else set empty in it.
		else
		{
			final JAXBElement<String> dropShipIndicator = objectFactory
					.createTestPricingFromGatewayInputInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			testPricingFromGatewayInput.setInDropShipIndicator(dropShipIndicator);
		}

		// check for the empty value or null.
		if (StringUtils.isNotEmpty(cartModel.getDealerPONum()))
		{
			final JAXBElement<String> dealerPO = objectFactory.createTestPricingFromGatewayInputInDealerPONumber(cartModel
					.getDealerPONum());
			testPricingFromGatewayInput.setInDealerPONumber(dealerPO);
		}
		//Send the PO as Dealer PO as per Yesha's Input for replenish drop ship order
		if (StringUtils.isNotEmpty(cartModel.getDropShipAccount()) && JnjOrderTypesEnum.ZKB.equals(cartModel.getOrderType()))
		{
			final JAXBElement<String> dealerPO = objectFactory.createTestPricingFromGatewayInputInDealerPONumber(cartModel
					.getPurchaseOrderNumber());
			testPricingFromGatewayInput.setInDealerPONumber(dealerPO);
		}

		// Fetch value from the config table.
		testPricingFromGatewayInput.setInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));
		// check for the entries are not empty or are not null.
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			testPricingFromGatewayInput.getInOrderLines().addAll(mapOrderLinesFromAbstOrderEntries(cartModel.getEntries()));
		}
		//final TestPricingFromGatewayOutput testPricingFromGatewayOutput = jnjGTSimulateDeliveredOrderService
				//.simulateDeliveredOrders(testPricingFromGatewayInput, wsData);
		//final TestPricingFromGatewayOutput testPricingFromGatewayOutput = null;
		
		List<OutOrderLines3> outOrderLineList = new ArrayList<OutOrderLines3>();
		if(!isRegional) {
			outOrderLineList = jnjGTOutOrderLineMapper.createValidationOutOrderList(cartModel);
		} else {
			//outOrderLineList =  Regional SAP call
		}
		
		mapCartModelFromOutOrderLine(cartModel, outOrderLineList, productCodes);
		jnjCartService.calculateValidatedCart(cartModel);
		
		/*if (null != testPricingFromGatewayOutput
				&& null != testPricingFromGatewayOutput.getOrderLinesOutput()
				&& CollectionUtils.isNotEmpty(testPricingFromGatewayOutput.getOrderLinesOutput())
				&& CollectionUtils.isNotEmpty(cartModel.getEntries())
				&& (null == testPricingFromGatewayOutput.getErrorMessage() || StringUtils.isEmpty(testPricingFromGatewayOutput
						.getErrorMessage().getValue())))
		{
			mapCartModelFromOutOrderLine(cartModel, testPricingFromGatewayOutput, productCodes);
			//Calculate cart for total values and save in data base
			jnjGTSimulateDelOrderResponseData.setSapResponseStatus(true);
			jnjGTSimulateDelOrderResponseData.setSavedSuccessfully(jnjCartService.calculateValidatedCart(cartModel));
		}
		else if (null != testPricingFromGatewayOutput && null != testPricingFromGatewayOutput.getErrorMessage())
		{
			productCodes = new StringBuilder();
			// added below call for removing the excluded products and including the excluded customer scenario.
			mapCartModelFromOutOrderLine(cartModel, testPricingFromGatewayOutput, productCodes);
			jnjCartService.calculateValidatedCart(cartModel);
			if (StringUtils.isNotEmpty(productCodes.toString()))
			{
				throw new BusinessException(productCodes.toString(), MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
			}
			jnjGTSimulateDelOrderResponseData.setSapResponseStatus(false);
			jnjGTSimulateDelOrderResponseData.setErrorMessage(testPricingFromGatewayOutput.getErrorMessage().getValue());
		}*/

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapDelSimulateOrderRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTSimulateDelOrderResponseData;
	}

	/**
	 * Map order lines from abst order entries.
	 *
	 * @param abstOrdEntModelList
	 *           the abst ord ent model list
	 * @return the collection
	 */
	protected Collection<InOrderLines> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//final ArrayOfinOrderLines arrayOfinOrderLines = new ArrayOfinOrderLines();
		final Collection<InOrderLines> inOrderLineList = new ArrayList<InOrderLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InOrderLines inOrderLines = new InOrderLines();
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getProduct().getCode());
			inOrderLines.setQuantity(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inOrderLines.setSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}

			final JAXBElement<String> prodMod = objectFactory.createInOrderLinesModCode(Jnjgtb2boutboundserviceConstants.Y_STRING);
			inOrderLines.setModCode(prodMod);

			if (StringUtils.isNotEmpty(abstOrderEntryModel.getBatchNum()))
			{
				inOrderLines.setLot(abstOrderEntryModel.getBatchNum());
			}
			else
			{
				inOrderLines.setLot(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverride()))
			{
				inOrderLines.setPriceYB00(abstOrderEntryModel.getPriceOverride());
			}
			else
			{
				inOrderLines.setPriceYB00(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverrideReason()))
			{
				inOrderLines.setOverrideCode(abstOrderEntryModel.getPriceOverrideReason());
			}
			else
			{
				inOrderLines.setOverrideCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPriceOverrideApprover()))
			{
				inOrderLines.setApproverCode(abstOrderEntryModel.getPriceOverrideApprover());
			}
			else
			{
				inOrderLines.setApproverCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			}
			inOrderLines.setStockPartner(abstOrderEntryModel.getSpecialStockPartner());

			// add the object in the list
			inOrderLineList.add(inOrderLines);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return inOrderLineList;
	}

	/**
	 * Map cart model from out order line.
	 *
	 * @param cartModel
	 *           the cart model
	 * @param testPricingFromGatewayOutput
	 *           the test pricing from gateway output
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */
	protected void mapCartModelFromOutOrderLine(final CartModel cartModel,
			List<OutOrderLines3> outOrderLines3List, final StringBuilder productCodes)  {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalNetValue = 0.0;
		double dropShipFee = 0.0;
		double totalFreightFees = 0.0;
		double minimumOrderFee = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		final CurrencyModel currencyModel = cartModel.getCurrency();
		final DecimalFormat decimalFormat = new DecimalFormat("#.##");
		CatalogVersionModel catalogVersionModel = null;
		String productCode = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : cartModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				final	String materialId   = abstOrdEntryModel.getProduct().getCode();
				productCode = new String(materialId);
				mapMaterialNoWithOrdLinesOutput.put(productCode, abstOrdEntryModel);
			}
		}
		final List<String> itemCategories = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.ITEM_CATEGORY_FOR_MDD,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> lineNumberExcluded = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.LINE_NUMBER_EXCLUDED,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// To fetch active Catalog Version Model.
		if (null != cartModel.getSite() && CollectionUtils.isNotEmpty(cartModel.getSite().getStores()))
		{
			if (CollectionUtils.isNotEmpty(cartModel.getSite().getStores().get(0).getCatalogs()))
			{
				catalogVersionModel = cartModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}
	if( CollectionUtils.isNotEmpty( outOrderLines3List)){
		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (final OutOrderLines3 orderLinesOutput : outOrderLines3List)
		{
			// In case of excluded customer, enter inside if block.
			if (null != orderLinesOutput && null != orderLinesOutput.getLineNumber() && 
					!lineNumberExcluded.contains(orderLinesOutput.getLineNumber())) {
				AbstractOrderEntryModel abstOrdEntryModel = null;
				// In case of excluded products, enter inside if block.
				/*if (StringUtils.isNotEmpty(orderLinesOutput.getMessage())  && StringUtils.isNotEmpty(orderLinesOutput.getMaterialNumber()) && null != productCodes)
				{
					if (StringUtils.isNotEmpty(productCodes.toString()))
					{
						productCodes.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING);
						productCodes.append(orderLinesOutput.getMaterialNumber());
					}
					else
					{
						productCodes.append(orderLinesOutput.getMaterialNumber());
					}
					if (mapMaterialNoWithOrdLinesOutput.containsKey(orderLinesOutput.getMaterialNumber()))
					{
						abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(orderLinesOutput.getMaterialNumber());
						modelService.remove(abstOrdEntryModel);
						modelService.refresh(cartModel);
					}
				}
				else
				{*/
					try
					{
						/*if (null != orderLinesOutput.getItemCategory()
								&& !(itemCategories.contains(orderLinesOutput.getItemCategory()))
								&& null != orderLinesOutput.getMaterialNumber() && null != orderLinesOutput.getMaterialEntered())
						{*/

							JnJProductModel product = null;
							JnJProductModel productEntered = null;
							ProductModel baseProduct = null;
							ProductModel baseProductEntered = null;
							//orderLinesOutput.setMaterialNumber(productCode);
							
							/*for(String prodCode:mapMaterialNoWithOrdLinesOutput.keySet()){
								
							
							
							if (StringUtils.equals(prodCode, orderLinesOutput.getMaterialEntered()))
							{
								product = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMaterialNumber(), catalogVersionModel);
								if (null != product)
								{
									baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
									baseProductEntered = baseProduct;
								}
							}
							else
							{*/
								product = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMaterialEntered(), catalogVersionModel);
								productEntered = jnJGTProductService.getProductModelByCode(orderLinesOutput.getMaterialEntered(), catalogVersionModel);
								if (null != product)
								{
									baseProduct = product.getMaterialBaseProduct() == null ? product : product.getMaterialBaseProduct();
								}
								if (null != productEntered)
								{
									baseProductEntered = productEntered.getMaterialBaseProduct() == null ? productEntered : productEntered
											.getMaterialBaseProduct();
								}
//							}
							// Check if the base product entered is not null.
							if (null != baseProductEntered && null != baseProduct)
							{
								if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProductEntered.getCode()))
								{
									continue;
								}
								abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProductEntered.getCode());
								//abstOrdEntryModel.setProduct(baseProduct);

								// check for the null value and set it in Sap Order Line Number.
								if (null != orderLinesOutput.getLineNumber())
								{
									abstOrdEntryModel.setSapOrderlineNumber(orderLinesOutput.getLineNumber());
								}
								/*if (null != orderLinesOutput.getBaseUOM())
								{
									final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getBaseUOM());
									abstOrdEntryModel.setBaseUOM(unitModel);
								}*/

								/*if (null != orderLinesOutput.getMessage())
								{
									abstOrdEntryModel.setMessage(orderLinesOutput.getMessage());
								}*/
								/*if (null != orderLinesOutput.getSubstitutionReason())
								{
									abstOrdEntryModel.setSubstitutionReason(orderLinesOutput.getSubstitutionReason());

								}*/
								if (null != orderLinesOutput.getSalesUOM())
								{
									final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getSalesUOM());
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
															.equalsIgnoreCase(orderLinesOutput.getSalesUOM()))
											{
												// set the variant product model in reference variant model.
												abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
												break;
											}
										}
									}

								}
								if (null != orderLinesOutput.getDiscounts()
										&& StringUtils.isNotEmpty(orderLinesOutput.getDiscounts()))
								{
									abstOrdEntryModel.setDiscountValues(createDiscountValues(
											(Double.valueOf(orderLinesOutput.getDiscounts())), currencyModel));
									totalDiscounts = totalDiscounts
											+ Double.valueOf(orderLinesOutput.getDiscounts()).doubleValue();
								}
								if (null != orderLinesOutput.getHigherLevelItemNumber())
								{
									abstOrdEntryModel.setHigherLevelItemNo(orderLinesOutput.getHigherLevelItemNumber());
								}

								abstOrdEntryModel.setItemCategory(orderLinesOutput.getItemCategory());

								/*if (null != orderLinesOutput.getRoute())
								{
									abstOrdEntryModel.setRoute(orderLinesOutput.getRoute().getValue());
								}
								if (null != orderLinesOutput.getShippingPoint())
								{
									abstOrdEntryModel.setShippingPoint(orderLinesOutput.getShippingPoint().getValue());
								}
								if (null != orderLinesOutput.getPriceType())
								{
									abstOrdEntryModel.setPriceType(orderLinesOutput.getPriceType().getValue());
								}
								if (null != orderLinesOutput.getHSAPromotion()
										&& StringUtils.isNotEmpty(orderLinesOutput.getHSAPromotion().getValue()))
								{
									abstOrdEntryModel.setHsaPromotion(Double.valueOf(orderLinesOutput.getHSAPromotion().getValue()));
									hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
								}*/
								if (null != orderLinesOutput.getFreightFees() && !Jnjgtb2boutboundserviceConstants.EMPTY_STRING.equals(orderLinesOutput.getFreightFees() ))
								{
									abstOrdEntryModel.setFreightFees(Double.valueOf(orderLinesOutput.getFreightFees()));
									totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
								}
								if (null != orderLinesOutput.getDropshipFee()
										&& StringUtils.isNotEmpty(orderLinesOutput.getDropshipFee()))
								{
									abstOrdEntryModel.setDropshipFee(Double.valueOf(orderLinesOutput.getDropshipFee()));
									dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
								}
								if (null != orderLinesOutput.getMinimumOrderFee()
										&& StringUtils.isNotEmpty(orderLinesOutput.getMinimumOrderFee()))
								{
									abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(orderLinesOutput.getMinimumOrderFee()));
									minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
								}
								if (null != orderLinesOutput.getNetValue()
										&& StringUtils.isNotEmpty(orderLinesOutput.getNetValue()))
								{
									abstOrdEntryModel.setNetPrice(Double.valueOf(orderLinesOutput.getNetValue()));
									totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
								}
								// To Set the Schedule Lines information in CartModel
								if ((null != orderLinesOutput.getScheduledLines()) && !(orderLinesOutput.getScheduledLines().isEmpty()))
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
									for (final ScheduledLines3 scheduledLines : orderLinesOutput.getScheduledLines())
									{
										// Check for the not null object
										if (null != scheduledLines)
										{
											final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
											jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
											if (null != scheduledLines.getLineNumber())
											{
												jnjDelSchModel.setLineNumber(scheduledLines.getLineNumber());
											}
											/*if (null != scheduledLines.getScheduledLineNumber())
											{
												jnjDelSchModel.setScheduledLineNumber(scheduledLines.getScheduledLineNumber());
											}*/
											/*if (null != scheduledLines.getLineStatus())
											{
												jnjDelSchModel.setLineStatus(scheduledLines.getLineStatus().getValue());
											}*/
											/*if (null != scheduledLines.getQuantity()
													&& !Jnjgtb2boutboundserviceConstants.EMPTY_STRING.equals(scheduledLines.getQuantity()
															.getValue()))
											{
												jnjDelSchModel.setQty(JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity()
														.getValue()));
												quantity = quantity
														+ (JnjGTCoreUtil.convertStringToLong(scheduledLines.getQuantity().getValue())
																.longValue());
											}*/
											if (null != scheduledLines.getDeliveryDate()
													&& !exceptedDateFormatList.contains(scheduledLines.getDeliveryDate()))
											{
												jnjDelSchModel.setDeliveryDate(formatResponseDate(scheduledLines.getDeliveryDate()));
											}// if block loop
											/*if (null != scheduledLines.getMaterialAvailabilityDate()
													&& !exceptedDateFormatList.contains(scheduledLines.getMaterialAvailabilityDate()
															.getValue()))
											{
												jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(scheduledLines
														.getMaterialAvailabilityDate().getValue()));

											}*/// if block loop
											/*if (null != scheduledLines.getShipDate()
													&& !exceptedDateFormatList.contains(scheduledLines.getShipDate().getValue()))
											{
												jnjDelSchModel.setShipDate(formatResponseDate(scheduledLines.getShipDate().getValue()));
											}*/
											// if block loop
											jnjDelSchModelList.add(jnjDelSchModel);
										}
									}
									abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
									abstOrdEntryModel.setQuantity(Long.valueOf(quantity));
								}
								// Changes for Price Override CR.
								if (StringUtils.isNotEmpty(abstOrdEntryModel.getPriceOverride()))
								{
									abstOrdEntryModel.setBasePrice(Double.valueOf(abstOrdEntryModel.getPriceOverride()));
									abstOrdEntryModel.setTotalPrice(Double.valueOf(decimalFormat.format(abstOrdEntryModel.getBasePrice()
											.doubleValue() * (abstOrdEntryModel.getQuantity().doubleValue()))));
									grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
									abstOrdEntryModel.setNetPrice(abstOrdEntryModel.getTotalPrice());
									totalNetValue = totalNetValue + abstOrdEntryModel.getNetPrice().doubleValue();
								}
								else if ( StringUtils.isNotEmpty(orderLinesOutput.getGrossPrice())
										&&  StringUtils.isNotEmpty(orderLinesOutput.getMaterialQuantity()))
								{
									abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double
											.parseDouble(orderLinesOutput.getGrossPrice()) + Double.parseDouble(orderLinesOutput
											.getDiscounts()))
											/ (abstOrdEntryModel.getQuantity().doubleValue()))));
									abstOrdEntryModel.setTotalPrice(Double.valueOf(Double.parseDouble(orderLinesOutput.getGrossPrice() )
											+ Double.parseDouble(orderLinesOutput.getDiscounts())));
									grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
								}else{
									grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
								}
								jnjGTOrderService.populateMddOrderEntryStatus(abstOrdEntryModel);
//							}
							
							}
							
							
							
							
							
						//}
					}
					catch (final ModelNotFoundException exception)
					{
						LOGGER.error(
								Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
										+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(),
								exception);
						/*throw new SystemException("System Exception throw from the JnjGTSimulateDeliveredOrderMapperImpl class",
								MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);*/
					}
					
				//}
			}
			/*else
			{
				throw new BusinessException();
			}*/
		}
	}
		cartModel.setTotalPrice(Double.valueOf(totalNetValue));
		cartModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		cartModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		cartModel.setDeliveryCost(Double.valueOf(totalFreightFees - minimumOrderFee));
		cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		cartModel.setSubtotal(Double.valueOf(grossPrice));
		cartModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		cartModel.setTotalOtherCharge(Double.valueOf(totalNetValue - grossPrice - minimumOrderFee - dropShipFee + hsaPromotion));
		cartModel.setTotalFees(Double.valueOf(minimumOrderFee + dropShipFee));
		cartModel.setSapValidated(Boolean.TRUE);
		cartModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
		cartModel.setTotalGrossPrice(Double.valueOf(grossPrice));
		cartModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));//CP022 Changes
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
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
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "createDiscountValues()" + Logging.HYPHEN
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
	protected Date formatResponseDate(final String date)  {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
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
			LOGGER.error(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ "Parsing Exception Occured " + exception.getMessage(), exception);
			/*throw new SystemException("System Exception throw from the JnjGTSimulateDeliveredOrderMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);*/
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_DELIVERED_ORDER + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return formattedDate;
	}

    public void setJnjGTOutOrderLineMapper(JnjGTOutOrderLineMapper jnjGTOutOrderLineMapper) {
        this.jnjGTOutOrderLineMapper = jnjGTOutOrderLineMapper;
    }


}
