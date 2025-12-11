/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */

package com.jnj.core.services.cart.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;

import jakarta.annotation.Resource;

import org.apache.commons.beanutils2.BeanComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.comparators.ComparableComparator;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjgtb2bMDDConstants;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.cart.JnjGTCartDao;
import com.jnj.core.dao.customer.JnjGTCustomerDAO;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.data.JnjGTGetContractPriceResponseData;
import com.jnj.core.data.JnjGTUpdatePriceData;
import com.jnj.core.dto.JnjGTSurgeryInfoData;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.model.JnjGTSurgeryInfoModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivProdMappingModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.outbound.mapper.JnjGTGetContractPriceMapper;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjModelService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.core.services.creditcard.JnjGTCreditCardService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.operations.JnjGTOperationsService;
import com.jnj.core.services.surgeon.JnjGTSurgeonService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.core.services.unit.JnjUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
//import com.jnj.core.services.cart.SelectedAttributesForm;
import com.jnj.core.util.JnjGTOrderTypeComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
import com.jnj.facades.data.JnjGTCommonFormIOData;
//import com.jnj.pcm.constants.JnjPCMCoreConstants;
//import com.jnj.pcm.core.dto.SelectedAttributesForm;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCartService;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaIOException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.Config;


/**
 * This is implementation class of JnjGTCartService, is used to perform various operations related to cart specific to
 * JNJ NA requirement.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTCartService extends DefaultJnjCartService implements JnjGTCartService
{
	/**
	 *
	 */
	protected static final String OCD_PRODUCT_SALES_ORG = "CD";
	protected static final String SIGNATURE = "signature";
	protected static final String BASKET_PAGE_MESSAGE_QTY_UPDATE = "basket.page.message.update";
	protected static final String BASKET_PAGE_MESSAGE_QTY_ADJUSTED = "basket.page.message.qtyAdjusted";
	protected static final String BASKET_PAGE_MESSAGE_MIN_QTY_ADDED = "basket.page.message.minQtyAdded";
	protected static final String HYPHEN = "-";
	protected static final String EACH = "EA";
	private static final String SHIPPING_METHOD_STANDARD = "Standard";

	@Override
	public void recalculateCart(final CartModel cartModel) throws CalculationException
	{
		calculateCart(cartModel);
	}

	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCartService.class);
	private static final int JnjContractEntryModel = 0;

	@Autowired
	private JnjGTSurgeonService jnjGTSurgeonService;
	@Autowired
	private FlexibleSearchService flexibleSearchService;
	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;
	@Autowired
	protected ModelService modelService;
	@Autowired
	private JnjModelService jnjModelService;
	@Autowired
	private JnjGTCreditCardService jnjGTCreditCardService;
	
	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;
	@Autowired
	private Populator<ProductModel, ProductData> productPricePopulator;
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;
	@Resource(name = "jnjB2BUnitService")
	private JnjGTB2BUnitService jnjGTB2BUnitService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private MediaService mediaService;
	@Autowired
	CatalogVersionService catalogVersionService;
	@Autowired
	JnjGTGetContractPriceMapper jnjGTGetContractPriceMapper;
	@Autowired
	private JnjConfigService jnjConfigService;
	@Autowired
	B2BOrderService b2bOrderService;
	@Autowired
	B2BCartService b2bCartService;
	@Autowired
	JnjUnitService jnjUnitService;
	@Autowired
	MessageService messageService;
	@Autowired
	private JnjGTB2BUnitDao jnjGTB2BUnitDao;
	@Autowired
	private JnjGTTerritoryService jnjGtTerritoryService;
	@Autowired
	private JnjGTOperationsService jnjGTOperationsService;
	
	@Autowired
	protected CatalogService catalogService;
	
	@Resource(name = "GTCartDao")
	JnjGTCartDao jnjGTCartDao;
	//thimma  replaced with getCartService()
	@Autowired
	protected CartService cartService;

	//thimma  replaced with userService
	@Autowired
	UserService userService;
	
	@Autowired
	protected BaseSiteService baseSiteService;
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}
	
	/** The jnj customer dao. */
	
	@Resource(name="GTCustomerDao")
	JnjGTCustomerDAO jnjGTCustomerDAO;


	public JnjGTSurgeonService getJnjGTSurgeonService() {
		return jnjGTSurgeonService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	public JnjModelService getJnjModelService() {
		return jnjModelService;
	}

	public JnjGTCreditCardService getJnjGTCreditCardService() {
		return jnjGTCreditCardService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	public Populator<ProductModel, ProductData> getProductPricePopulator() {
		return productPricePopulator;
	}

	public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public MediaService getMediaService() {
		return mediaService;
	}

	public CatalogVersionService getCatalogVersionService() {
		return catalogVersionService;
	}

	public JnjGTGetContractPriceMapper getJnjGTGetContractPriceMapper() {
		return jnjGTGetContractPriceMapper;
	}

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}

	public B2BCartService getB2bCartService() {
		return b2bCartService;
	}

	public JnjUnitService getJnjUnitService() {
		return jnjUnitService;
	}

	public MessageService getMessageService() {
		return messageService;
	}

	public JnjGTB2BUnitDao getJnjGTB2BUnitDao() {
		return jnjGTB2BUnitDao;
	}

	public JnjGTTerritoryService getJnjGTTerritoryService() {
		return jnjGtTerritoryService;
	}

	public JnjGTOperationsService getJnjGTOperationsService() {
		return jnjGTOperationsService;
	}

	public JnjGTCartDao getJnjGTCartDao() {
		return jnjGTCartDao;
	}

	public CartService getCartService() {
		return cartService;
	}

	public UserService getUserService() {
		return userService;
	}

	protected final Double DEFAULT_VALUE = Double.valueOf(0.0);

	@Override
	public boolean updatePurchaseOrderNumber(final String purchaseOrderNumber)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setPurchaseOrderNumber(purchaseOrderNumber);
		return saveCartModel(cartModel, false);
	}

	@Override
	public boolean updateReasonCode(final String reasonCode)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setReasonCode(reasonCode);
		return saveCartModel(cartModel, false);
	}

	@Override
	public boolean updateDistributorPONumber(final String distributorPONumber)
	{
		final CartModel cartModel = cartService.getSessionCart();
		//cartModel.setDistributorPO(distributorPONumber);
		cartModel.setDealerPONum(distributorPONumber);
		return saveCartModel(cartModel, false);
	}

	@Override
	public boolean updateAttention(final String attention)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setAttention(attention);
		return saveCartModel(cartModel, false);
	}
	
	@Override
	public boolean updateSpecialText(final String specialText) {
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setSpecialText(specialText);
		return saveCartModel(cartModel, false);
	}


	@Override
	public void setDefaultDeliveryDate(final Date defaultDeliveryDate)
	{
		final CartModel cartModel = cartService.getSessionCart();
		if (cartModel.getNamedDeliveryDate() == null || cartModel.getNamedDeliveryDate().before(defaultDeliveryDate))
		{
			cartModel.setRequestedDeliveryDate(defaultDeliveryDate);
			saveCartModel(cartModel, false);
		}

	}

	/**
	 * Returns the computed quantity that needs to be added in case if level code of the entered variant is not more than
	 * the delivery variants level code AND entered variant is NOT the delivery variant of the Parent Product
	 * 
	 * @param delVariant
	 *           the delivery variant
	 * @param variantProduct
	 *           the variant entered by user, that needs to be added
	 * @param quantityToAdd
	 *           the quantity of the entered variant, that needs to be added
	 * @return true, if successful
	 */
	protected long getRoundedQuantity(final JnjGTVariantProductModel delVariant, final JnjGTVariantProductModel variantProduct,
			final long quantityToAdd)
	{
		LOGGER.info("Start : Rounding of QTY");
		final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
		final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator().longValue() : 1;

		final long roundedQty = (((quantityToAdd * enteredVariantLevelQty) / deliveryVariantLevelQty) + ((quantityToAdd
				% deliveryVariantLevelQty > 0) ? 1 : 0))
				* (deliveryVariantLevelQty / enteredVariantLevelQty);
		LOGGER.info("Start : Rounding of QTY");
		return roundedQty;
	}

	protected long getQuantityToBeAdded(final JnjGTVariantProductModel delVariant, final JnjGTVariantProductModel variantProduct,
			final long quantityToAdd)
	{
		long roundedQty = quantityToAdd;
		if (isQtyAdjustmentRequired(variantProduct, delVariant))
		{
			final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
			final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator().longValue()
					: 1;

			roundedQty = ((quantityToAdd / deliveryVariantLevelQty) + ((quantityToAdd % deliveryVariantLevelQty > 0) ? 1 : 0))
					* (deliveryVariantLevelQty / enteredVariantLevelQty);
		}
		return roundedQty;
	}

	/**
	 * Returns the delivery variant for the current variant's base product
	 * 
	 * @param variantProduct
	 *           the variant whose base product is used to find delivery variant
	 * @return delivery variant
	 */
	protected JnjGTVariantProductModel getOrderableUnit(final JnjGTVariantProductModel variantProduct)
	{
		if (variantProduct.getBaseProduct() instanceof JnJProductModel)
		{
			return jnJGTProductService.getDeliveryGTIN(variantProduct.getBaseProduct());
		}
		return null;
	}

	@Override
	public CommerceCartModification addToCart(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final UnitModel unit, final boolean forceNewEntry) throws CommerceCartModificationException {
		final Date startTime = new Date();
		String addToCartMsgKey = "success";
		final CommerceCartModification cartModification = new CommerceCartModification();
		if (productModel instanceof JnjGTVariantProductModel){
			final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
			final JnJProductModel modProduct = (JnJProductModel) variantProduct.getBaseProduct();
			JnJProductModel baseProduct = modProduct.getMaterialBaseProduct();
			// if base product is null
			if (baseProduct == null){
				baseProduct = modProduct;
			}
			final UnitModel productUnit = variantProduct.getUnit();
			/**
			 * 
			 * There is a different logic for add products in he cart depending on the Cart Type For Standard, No Charge,
			 * International, Consumer units will be as proportional to delivery GTIN, but for Delivered and Replenish
			 * order these will be same what user enters.
			 * 
			 **/
			if (cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) || cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
			{
				addToCartMsgKey = addToCartDeliveredOrder(cartModel, quantityToAdd, addToCartMsgKey, modProduct, baseProduct,
						productUnit, cartModification);
			}
			else
			{
				addToCartMsgKey = addToCartGenric(cartModel, quantityToAdd, addToCartMsgKey, variantProduct, modProduct, baseProduct,
						productUnit, cartModification,false);
			}

			//Check if added product is OCD
			if (StringUtils.equals(baseProduct.getSalesOrgCode(), OCD_PRODUCT_SALES_ORG))
			{
				cartModel.setContainsOCDProduct(true);
			}
			else
			{
				cartModel.setContainsOCDProduct(false);
			}
			LOGGER.info("Start : Calculate Cart");
			calculateCart(cartModel);
			LOGGER.info("End : Calculate Cart");
			cartModification.setStatusCode(addToCartMsgKey);
		}

		final Date endTime = new Date();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("------>Total Time taken to execute JnjGTCartService.addToCart() : "
					+ (endTime.getTime() - startTime.getTime()) + "seconds");

		}

		return cartModification;
	}
	
	/**
	 * Method used for order types other than Consignment charge
	 * @param cartModel
	 * @param quantityToAdd
	 * @param addToCartMsgKey
	 * @param variantProduct
	 * @param modProduct
	 * @param baseProduct
	 * @param productUnit
	 * @param cartModification
	 * @return
	 */
	protected String addToCartGenric(final CartModel cartModel, final long quantityToAdd, String addToCartMsgKey,
			final JnjGTVariantProductModel variantProduct, final JnJProductModel modProduct, final JnJProductModel baseProduct,
			final UnitModel productUnit, final CommerceCartModification cartModification)
	{		
		addToCartMsgKey = addProductToCartGeneric(cartModel, quantityToAdd, addToCartMsgKey, variantProduct, modProduct, baseProduct,
				variantProduct.getUnit(), cartModification,false);
		return addToCartMsgKey;		
	}
	
	/**
	 * Method used for order types Consignment charge
	 * @param cartModel
	 * @param quantityToAdd
	 * @param addToCartMsgKey
	 * @param variantProduct
	 * @param modProduct
	 * @param baseProduct
	 * @param productUnit
	 * @param cartModification
	 * @param forceNewEntry
	 * @return
	 */
	protected String addToCartGenric(final CartModel cartModel, final long quantityToAdd, String addToCartMsgKey,
			final JnjGTVariantProductModel variantProduct, final JnJProductModel modProduct, final JnJProductModel baseProduct,
			final UnitModel productUnit, final CommerceCartModification cartModification, final boolean forceNewEntry)
	{		
		addToCartMsgKey = addProductToCartGeneric(cartModel, quantityToAdd, addToCartMsgKey, variantProduct, modProduct, baseProduct,
				variantProduct.getUnit(), cartModification,forceNewEntry);
		return addToCartMsgKey;	
	
	}
	
	/**
	 * MEthod added to prevent duplicate code because of overloading addToCartGenric method.
	 * @param cartModel
	 * @param quantityToAdd
	 * @param addToCartMsgKey
	 * @param variantProduct
	 * @param modProduct
	 * @param baseProduct
	 * @param unit
	 * @param cartModification
	 * @param forceNewEntry
	 * @return
	 */
	private String addProductToCartGeneric(CartModel cartModel, long quantityToAdd, String addToCartMsgKey,
			JnjGTVariantProductModel variantProduct, JnJProductModel modProduct, JnJProductModel baseProduct,
			UnitModel productUnit, CommerceCartModification cartModification, final boolean forceNewEntry) {
		

		final List<CartEntryModel> entriesForProd = cartService.getEntriesForProduct(cartModel, baseProduct);
		final Date startTime = new Date();
		CartEntryModel cartEntryModel = null;
		//	If Base Product(parents parent product) of the added variant is not in the cart
		if (CollectionUtils.isEmpty(entriesForProd) || forceNewEntry) //Soumitra added the OR(||) condition for copy line feature @ ConsignmentCharge CartPage.AAOL-4737
		{
			final JnjGTVariantProductModel delVariant = jnJGTProductService.getDeliveryGTIN(modProduct);

			long derivedQuantity = quantityToAdd;

			// If level code of the entered  variant is not more than the delivery variants level code AND entered variant is NOT the delivery variant of the Parent Product
			// Quantity to be added needs to be derived

			if (isQtyAdjustmentRequired(variantProduct, delVariant))
			{
				LOGGER.debug("Coming in the qtyadjustmentreqd-------------------------------");
				variantProduct.getMinOrderQuantity();
				if (quantityToAdd == 0)
				{
					derivedQuantity = variantProduct.getMinOrderQuantity();
					addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
					derivedQuantity = getRoundedQuantity(delVariant, variantProduct, derivedQuantity);
				}
				else
				{
					LOGGER.debug("inside non zero-------------------------------------"+variantProduct.getMinOrderQuantity());
					derivedQuantity = getRoundedQuantity(delVariant, variantProduct, quantityToAdd);
					if (quantityToAdd != derivedQuantity)
					{
						addToCartMsgKey = BASKET_PAGE_MESSAGE_QTY_ADJUSTED;
					}
				}
			}
			else
			{
				
				LOGGER.debug("Coming in the other block of addToCartGenric-------------------------------");
				if (quantityToAdd == 0 && null != variantProduct.getMinOrderQuantity())
				{
					derivedQuantity = variantProduct.getMinOrderQuantity();
					addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
					LOGGER.debug("inside non null min qty-------------------------------------"+variantProduct.getMinOrderQuantity());
				}
			  if(quantityToAdd == 0 &&  variantProduct.getMinOrderQuantity()==null)
				{
					derivedQuantity = 1;
					addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
				
					LOGGER.debug("inside null min qty-------------------------------------"+variantProduct.getMinOrderQuantity());
				}
			}
			// Add the entered variant
			cartEntryModel = cartService.addNewEntry(cartModel, baseProduct, derivedQuantity, productUnit, -1, false);
			cartEntryModel.setReferencedVariant(variantProduct);

			//Save updated Entry
			saveAbstOrderEntry(cartEntryModel);
			cartModification.setQuantityAdded(derivedQuantity);
		}
		/** If the Base Product(parents parent product) is exists in the cart **/
		else
		{
			LOGGER.debug("entering non-empty part-------------------------------------");
			cartEntryModel = entriesForProd.get(0);
			long finalQtyToAdd;
			if(quantityToAdd==0 && null != variantProduct.getMinOrderQuantity()){
				finalQtyToAdd = variantProduct.getMinOrderQuantity();
			}
			else if(quantityToAdd == 0 &&  variantProduct.getMinOrderQuantity()==null)
			{
				finalQtyToAdd=1;
			}
			else{
				finalQtyToAdd = quantityToAdd;
			}
			
			final JnjGTVariantProductModel delVariant = getOrderableUnit(variantProduct);
			//If the entered variant is the Cart variant, Variant remains unchanged, just quantity needs to be added
			if (cartEntryModel.getReferencedVariant().getCode().equals(variantProduct.getCode()))
			{
				addToCartMsgKey = BASKET_PAGE_MESSAGE_QTY_UPDATE;
				long derivedQuantity = finalQtyToAdd;
				/**
				 * If level code of the entered variant is not more than the delivery variants level code AND entered
				 * variant is NOT the delivery variant of the Parent Product. In this case qty need to sync with delivery
				 * unit
				 **/
				// Quantity to be added needs to be derived
				if (isQtyAdjustmentRequired(variantProduct, delVariant))
				{
					derivedQuantity = getRoundedQuantity(delVariant, variantProduct, finalQtyToAdd);
					addToCartMsgKey = BASKET_PAGE_MESSAGE_QTY_ADJUSTED;
				}
				finalQtyToAdd = cartEntryModel.getQuantity().longValue() + derivedQuantity;
				cartModification.setQuantityAdded(derivedQuantity);
			}

			//	If the enter variant is not the existing variant
			// Delivery Variant will be added to cart
			else
			{
				long deliveryEnteredQty = finalQtyToAdd;
				long existingQty = cartEntryModel.getQuantity().longValue();
				final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
				if (Jnjb2bCoreConstants.MDD.equals(currentSiteName) && null != variantProduct.getPackagingLvlCode()
						&& null != delVariant.getPackagingLvlCode())
				{
					final long cartVariantLevelQty = cartEntryModel.getReferencedVariant().getNumerator() != null ? cartEntryModel
							.getReferencedVariant().getNumerator().longValue() : 1;
					final long deliveryVariantLevelQty = delVariant.getNumerator() != null ? delVariant.getNumerator().longValue() : 1;
					existingQty = (cartEntryModel.getQuantity().longValue() * cartVariantLevelQty) / deliveryVariantLevelQty;
					if (variantProduct.getPackagingLvlCode().intValue() > delVariant.getPackagingLvlCode().intValue()
							|| variantProduct.getCode().equals(delVariant.getCode()))
					{
						final long enteredVariantLevelQty = variantProduct.getNumerator() != null ? variantProduct.getNumerator()
								.longValue() : 1;
						deliveryEnteredQty = (finalQtyToAdd * enteredVariantLevelQty) / deliveryVariantLevelQty;
					}
					else
					{
						deliveryEnteredQty = (finalQtyToAdd / deliveryVariantLevelQty)
								+ ((finalQtyToAdd % deliveryVariantLevelQty > 0) ? 1 : 0);
					}
				}
				finalQtyToAdd = existingQty + deliveryEnteredQty;
				cartEntryModel.setUnit(delVariant.getUnit());
				cartEntryModel.setReferencedVariant(delVariant);
				
				cartModification.setQuantityAdded(deliveryEnteredQty);
			}
			cartEntryModel.setQuantity(Long.valueOf(finalQtyToAdd));
		}
		cartEntryModel.setSalesOrg(jnjConfigService.getConfigValueById(Jnjb2bCoreConstants.Order.SALES_ORGANISATION));
		cartEntryModel.setSapOrderType(cartModel.getOrderType().getCode());
		cartEntryModel.setCalculated(Boolean.FALSE);
		cartModification.setProduct(baseProduct);
		cartModification.setEntry(cartEntryModel);
		//Save updated Entry
		saveAbstOrderEntry(cartEntryModel);

		final Date endTime = new Date();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("---------->Total Time taken to execute JnjGTCartService.addToCart().addToCartGeneric() : "
					+ (endTime.getTime() - startTime.getTime()) / (1000) + "seconds");

		}

		return addToCartMsgKey;
	
		
	}
	

	/**
	 * Checks if is qty adjustment required.
	 * 
	 * @param variantProduct
	 *           the variant product
	 * @param delVariant
	 *           the del variant
	 * @return true, if is qty adjustment required
	 */
	protected boolean isQtyAdjustmentRequired(final JnjGTVariantProductModel variantProduct,
			final JnjGTVariantProductModel delVariant)
	{
		final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		return Jnjb2bCoreConstants.MDD.equals(currentSiteName) &&
				variantProduct != null && null != variantProduct.getPackagingLvlCode()
				&& delVariant != null && null != delVariant.getPackagingLvlCode()
				&& (!(variantProduct.getCode().equals(delVariant.getCode()) || variantProduct.getPackagingLvlCode().intValue() > delVariant
						.getPackagingLvlCode().intValue()));
	}

	@Override
	public AddressModel updateDropShipAccount(final String dropShipAccountId) throws BusinessException
	{
		AddressModel dropShipAddress = new AddressModel();
		if (StringUtils.isNotEmpty(dropShipAccountId))
		{
			// fetching the drop ship address corresponding to drop ship acc id.
			final List<AddressModel> dropShipAddressList = jnjAddressService.getAddressByIdandOnwerType(dropShipAccountId, true,
					true);

			if (CollectionUtils.isEmpty(dropShipAddressList))
			{
				throw new BusinessException("Invalid Drop ship Account : " + dropShipAccountId);
			}
			dropShipAddress = dropShipAddressList.get(0);
			// current B2B unit and associated drop ship addresses.
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			final JnJB2BUnitModel JnjGTb2bUnit = currentUser.getCurrentB2BUnit();

			// drop ship address is not available in the B2B unit then adding it.

			boolean found = false;
			if (null != JnjGTb2bUnit.getDropShipAddresses())
			{
				for (final AddressModel dropShipaddressModel : JnjGTb2bUnit.getDropShipAddresses())
				{
					if (dropShipaddressModel.getJnJAddressId().equals(dropShipAddress.getJnJAddressId()))
					{
						found = true;
						break;
					}
				}
			}
			if (!found)
			{
				final AddressModel addressClone = modelService.clone(dropShipAddress);
				addressClone.setOwner(JnjGTb2bUnit);

				final Set<AddressModel> dropShipAddresses = new HashSet<>();
				dropShipAddresses.addAll(JnjGTb2bUnit.getDropShipAddresses());
				dropShipAddresses.add(addressClone);
				JnjGTb2bUnit.setDropShipAddresses(dropShipAddresses);
				getModelService().save(JnjGTb2bUnit);
			}

			// adding drop ship account and address to cart model
			final CartModel cartModel = cartService.getSessionCart();
			cartModel.setDropShipAccount(dropShipAddress.getJnJAddressId());
			cartModel.setDeliveryAddress(dropShipAddress);
			final AddressModel addressModel = jnjGTB2BUnitService.getShippingAddress(null);
			if (null != addressModel && !StringUtils.equals(dropShipAccountId, addressModel.getJnJAddressId()))
			{
				cartModel.setThirdpartyBilling(Boolean.TRUE);
			}
			saveCartModel(cartModel, false);
		}
		else
		{
			final CartModel cartModel = cartService.getSessionCart();
			final AddressModel defaultShippingAddress = jnjGTB2BUnitService
					.getShippingAddress((JnJB2BUnitModel) cartModel.getUnit());
			cartModel.setDeliveryAddress(defaultShippingAddress);
			cartModel.setDropShipAccount(dropShipAccountId);
			cartModel.setThirdpartyBilling(Boolean.FALSE);
			saveCartModel(cartModel, false);
			dropShipAddress = defaultShippingAddress;
		}
		return dropShipAddress;
	}

	@Override
	public void saveCreditCardInfo(final JnjGTCreditCardModel ccPaymentInfoModel)
	{

		if (ccPaymentInfoModel.isSaved())
		{
			jnjGTCreditCardService.saveCreditCard(ccPaymentInfoModel);
		}
		else
		{
			final CartModel cartModel = cartService.getSessionCart();
			cartModel.setPaymentInfo(ccPaymentInfoModel);
			cartService.saveOrder(cartModel);

		}

	}

	@Override
	public JnjGTCreditCardModel createCCPaymentInfoModel()
	{
		final Object ccPaymentInfoModel = getModelService().create(JnjGTCreditCardModel.class);
		return (JnjGTCreditCardModel) ccPaymentInfoModel;
	}


	@Override
	public boolean updateLotNumberForEntry(final int entryNumber, final String newLotComment)
	{
		final CartModel cartModel = cartService.getSessionCart();
		//thimma
		final AbstractOrderEntryModel entryToUpdate = getEntryModelForNumber(cartModel, entryNumber);
		entryToUpdate.setLot(newLotComment);
		return saveCartModel(cartModel, true);
	}
	
	@Override
	public boolean updateBatchDetailsForEntry(final int entryNumber, final String batchNumber, final String serialNumber)
	{
		final CartModel cartModel = cartService.getSessionCart();
		final AbstractOrderEntryModel entryToUpdate = getEntryModelForNumber(cartModel, entryNumber);
		entryToUpdate.setBatchNumber(batchNumber);
		entryToUpdate.setSerialNumber(serialNumber);
		return saveCartModel(cartModel, true);
	}

	@Override
	public AddressModel getShippingAddressById(final String shippingAddrId)
	{
		final AddressModel addressModel = jnjAddressService.getAddressByPK(shippingAddrId);
		return addressModel;
	}

	@Override
	public void updatePaymentInfo(final String paymentInfoId)
	{

		final CartModel cartModel = cartService.getSessionCart();
		if (paymentInfoId.equalsIgnoreCase(Jnjb2bCoreConstants.Cart.PURCHASE_ORDER))
		{
			cartModel.setPaymentInfo(null);
		}
		else
		{
			final PK pk = PK.fromLong(Long.parseLong(paymentInfoId));
			final CreditCardPaymentInfoModel paymentInfoModel = getModelService().get(pk);
			cartModel.setPaymentInfo(paymentInfoModel);
		}
		saveCartModel(cartModel, false);
	}

	@Override
	public AddressModel changeShippingAddress(final String shippingAddrId)
	{
		final AddressModel addressModel = jnjAddressService.getAddressByPK(shippingAddrId);
		updateDeliveryAddress(addressModel);
		return addressModel;
	}
	
	@Override
	public AddressModel changeBillingAddress(final String billingAddrId)
	{
		final AddressModel addressModel = jnjAddressService.getAddressByPK(billingAddrId);
		updateBillAddress(addressModel);
		return addressModel;
	}

	@Override
	public void restoreCartForCurrentUser(final String srcSystemId)
	{
		 JnJB2bCustomerModel currentUser = null;
		 JnJB2BUnitModel b2BUnit = null;
		 boolean isAddressSet = false;
		try{
			 currentUser = jnjGTCustomerService.getCurrentUser();
			 b2BUnit = currentUser.getCurrentB2BUnit();
			 CartModel cart = jnjGTCartDao.getCartForUserAndUnit(getBaseSiteService().getCurrentBaseSite(), currentUser, b2BUnit);
	
			/* Creates New Cart in case of cart is not exists for current site/User */
			if (null == cart)
			{
				LOGGER.warn("JnjGTCartServiceImpl - cart is null IN restoreCartForCurrentUser :  creating new cart ::"+ currentUser + ":: for ::" + b2BUnit);
				getSessionService().removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
				cart = cartService.getSessionCart();
				isAddressSet=true;
			}
			if (!isAddressSet) {
				cart.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress(b2BUnit));
				cart.setPaymentAddress(jnjGTB2BUnitService.getBillingAddress(b2BUnit));
			}
			cartService.saveOrder(cart);
			getSessionService().setAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME, cart);
		}catch(Exception ex){
			LOGGER.error("JnjGTCartServiceImpl - Exception IN restoreCartForCurrentUser" + currentUser + ":: for ::" + b2BUnit);
			ex.printStackTrace();
		}
	}

	@Override
	public boolean calculateCart(final CartModel cartModel)
	{
		boolean calculated = false;
		try
		{
			calculateEntries(cartModel);
			calculateTotals(cartModel);
			calculated = saveCartModel(cartModel, true);
		}
		catch (final CalculationException calculateExp)
		{
			LOGGER.error("cart calculaton not done");
		}
		return calculated;
	}


	protected void resetEntriesToDefaultPrice(final AbstractOrderModel order) throws CalculationException
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			updatePriceForEntry(entry, null);
			entry.setTotalPrice(calculateEntryTotal(entry) * entry.getQuantity());
		}
	}

	protected Double calculateEntryTotal(final AbstractOrderEntryModel entry)
	{
		// If not overridden price is present, use base price for calculation
		if (StringUtils.isBlank(entry.getPriceOverride()))
		{
			return entry.getBasePrice();
		}
		// If overridden price is present, use overridden price
		else
		{
			return Double.valueOf(entry.getPriceOverride());
		}
	}

	protected void calculateEntries(final AbstractOrderModel order) throws CalculationException
	{
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			/** Get Contract Price For particular Product**/
			updatePriceForEntry(entry);
		}
	}


	/**
	 * Update price for entry.
	 * 
	 * @param entry
	 *           the entry
	 */
	protected void updatePriceForEntry(final AbstractOrderEntryModel entry)
	{
		if (BooleanUtils.isFalse(entry.getCalculated()))
		{
			//final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			/* In case of MDD price need to be updated from SAP if exists */
			/*if (Jnjb2bCoreConstants.MDD.equals(currentSiteName))
			{
				JnjGTGetContractPriceResponseData priceResponseData = null;
				try
				{
					LOGGER.error("Start: SAP Call For Get Price");
					priceResponseData = jnjGTGetContractPriceMapper.mapGetContractPriceMapperRequestResponse(
							(CartModel) entry.getOrder(), (CartEntryModel) entry);
					LOGGER.error("End>>: SAP Call For Get Price");
				}
				catch (final SystemException | IntegrationException exception)
				{
					LOGGER.error("Sap price can not be retirved");
				}
				updatePriceForEntry(entry, priceResponseData);
			}
			else
			{*/
				setHybrisPricesForEntry(entry);
			//}
		}

		entry.setTotalPrice(calculateEntryTotal(entry) * entry.getQuantity());
		entry.setNetPrice(calculateEntryTotal(entry) * entry.getQuantity());
	}

	@Override
	public JnjGTUpdatePriceData updatePriceForEntry(final int entryNumber)
	{
		final CartModel cart = cartService.getSessionCart();
		final CartEntryModel entry = cartService.getEntryForNumber(cart, entryNumber);

		JnjGTGetContractPriceResponseData priceResponseData = null;
		try
		{
			priceResponseData = jnjGTGetContractPriceMapper.mapGetContractPriceMapperRequestResponse(cart, entry);
		}
		catch (final SystemException | IntegrationException exception)
		{
			LOGGER.error("Sap price can not be retirved");
		}
		updatePriceForEntry(entry, priceResponseData);
		final double totalPrice = entry.getBasePrice().doubleValue() * entry.getQuantity().longValue();
		entry.setTotalPrice(Double.valueOf(totalPrice));
		entry.setNetPrice(Double.valueOf(totalPrice));
		saveAbstOrderEntry(entry);

		final JnjGTUpdatePriceData priceData = new JnjGTUpdatePriceData();
		priceData.setEntryBasePrice(String.valueOf(entry.getBasePrice())); //Retrieve from Entry model as here it persist after truncating
		priceData.setEntryTotalPrice(String.valueOf(entry.getTotalPrice()));
		priceData.setCurrencySymbol(entry.getOrder().getCurrency().getSymbol());
		return priceData;
	}

	protected void calculateTotals(final AbstractOrderModel order)
	{
		double toalGrossValue = 0.0;
		double totalNetValue = 0.0;
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			toalGrossValue += entry.getTotalPrice().doubleValue();
			totalNetValue += entry.getNetPrice().doubleValue();
		}
		LOGGER.info("DefaultJnjGTCartService calculateTotals totalNetValue : "+totalNetValue + " and toalGrossValue : "+ toalGrossValue);
		order.setTotalPrice(Double.valueOf(totalNetValue));
		order.setSubtotal(Double.valueOf(toalGrossValue));
	}

	/**
	 * First check price in SAP if not available set price from List Price
	 * 
	 * @param cartEntryModel
	 * @param sapPriceResponseData
	 */
	protected void updatePriceForEntry(final AbstractOrderEntryModel cartEntryModel,
			final JnjGTGetContractPriceResponseData sapPriceResponseData)
	{
		//Checking pricing at sap site
		/*if (null != sapPriceResponseData && sapPriceResponseData.isSapResponseStatus())
		{
			if (StringUtils.isNotEmpty(sapPriceResponseData.getPer()) && Integer.parseInt(sapPriceResponseData.getPer()) != 0)
			{

				try
				{
					cartEntryModel.setBasePrice(Double.valueOf(sapPriceResponseData.getContractPrice()
							/ Integer.parseInt(sapPriceResponseData.getPer())));
					cartEntryModel.setContractNum(sapPriceResponseData.getContractNumber());
				}
				catch (final NumberFormatException nfex)
				{
					cartEntryModel.setBasePrice(DEFAULT_VALUE);
					LOGGER.error("Not able to parse SAP Price ", nfex);
				}

			}
		}*/
		//else{
			
			LOGGER.info("Start:Not able load SAP price trying to set prices from Hybris DB");
			setHybrisPricesForEntry(cartEntryModel);
			LOGGER.info("End>>:Not able load SAP price trying to set prices from Hybris DB");
		//}
		cartEntryModel.setCalculated(Boolean.TRUE);
	}

	/**
	 * YTODO <<Replace this line with the method description and describe each parameter below>>
	 * 
	 * @param cartEntryModel
	 */
	protected void setHybrisPricesForEntry(final AbstractOrderEntryModel cartEntryModel)
	{
		final ProductData productData = new ProductData();
		if(cartEntryModel.getReferencedVariant() != null) {
			productPricePopulator.populate(cartEntryModel.getReferencedVariant(), productData);
		}
/*		PriceData pd = new PriceData();
		pd.setValue(new BigDecimal("100.00"));
		productData.setPrice(pd);*/
		if (null != productData.getPrice() && null != productData.getPrice().getValue())
		{
			LOGGER.info("Prices Exists in Hybris DB");
			cartEntryModel.setBasePrice(Double.valueOf(productData.getPrice().getValue().doubleValue()));
		}
		else
		{
			LOGGER.info("Prices does not Exists in Hybris DB, set to 0");
			cartEntryModel.setBasePrice(DEFAULT_VALUE);
		}
	}

	@Override
	public List<JnjGTShippingMethodModel> getAllShippingMethods()
	{
		return jnjGTCartDao.getShippingMethods();
	}

	@Override
	public boolean updateShippingMethod(final String route, final int entryNumber)
	{
		final String[] routeValues = route.split("~~", 2);
		final String standardRoute = routeValues[0].trim();
		final String expidiateRoute = routeValues[1].trim();
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		if (StringUtils.equals(standardRoute, entry.getRoute())|| StringUtils.equalsIgnoreCase(SHIPPING_METHOD_STANDARD, standardRoute))
		{
			entry.setSelectedRoute(StringUtils.EMPTY);
			if (StringUtils.isNotEmpty(entry.getShippingPoint()))
			{
				// Get the value from the config table
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjb2bCoreConstants.SHIPPING_METHOD_REVERSAL, entry.getShippingPoint());
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					entry.setShippingPoint(jnjConfigModels.get(0).getValue());
				}
			}
		}
		else
		{
			entry.setSelectedRoute(expidiateRoute);
			if (StringUtils.isNotEmpty(entry.getShippingPoint()))
			{
				// Get the value from the config table
				final List<JnjConfigModel> jnjConfigModels = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjb2bCoreConstants.SHIPPING_METHOD, entry.getShippingPoint());
				if (CollectionUtils.isNotEmpty(jnjConfigModels))
				{
					entry.setShippingPoint(jnjConfigModels.get(0).getValue());
				}
			}
		}
		return saveAbstOrderEntry(entry);
	}

	@Override
	public void changeOrderType(final String selectedOrderType)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setLinkedWithDelivered(false);
		final JnjOrderTypesEnum existingOrderType = cartModel.getOrderType();

		cartModel.setOrderType(JnjOrderTypesEnum.valueOf(selectedOrderType));
		if (JnjOrderTypesEnum.ZDEL.getCode().equalsIgnoreCase(selectedOrderType)
				|| JnjOrderTypesEnum.ZKB.getCode().equalsIgnoreCase(selectedOrderType))
		{
			changeRefVariantsToSalesUOM();
			if (jnjGTCustomerService.getPopulatedDivisionData(null).isIsMitek())
			{
				cartModel.setDeliveryCost(Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.MITEK_FREIGHT_CHARGES)));
				cartModel.setTotalFees(Double.valueOf(Config.getParameter(Jnjb2bCoreConstants.MITEK_FREIGHT_CHARGES)));
			}
		}
		else if (existingOrderType.equals(JnjOrderTypesEnum.ZDEL))
		{
			roundOffQtyForCartEntries();
			cartModel.setDeliveryCost(DEFAULT_VALUE);
			cartModel.setTotalFees(DEFAULT_VALUE);
		}
		cartModel.setPaymentInfo(null);
		saveCartModel(cartModel, true);
	}

	@Override
	public boolean updateLotNumForEntry(final String lotNumber, final String pcode, final int entryNumber)
	{
		//FIX GTR-1768
		if(jnjGTCartDao.isValidLotNumForProduct(lotNumber, pcode)){			
		
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		entry.setBatchNum(lotNumber);
		return saveAbstOrderEntry(entry);
		
		}else{
			return false;
		}
	}

	@Override
	public boolean updatePONumForEntry(final String poNumber, final int entryNumber)
	{
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		entry.setPONumber(poNumber);
		return saveAbstOrderEntry(entry);
	}

	@Override
	public boolean updateInvoiceNumForEntry(final String invoiceNumber, final int entryNumber)
	{
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		entry.setReturnInvNumber(invoiceNumber);
		return saveAbstOrderEntry(entry);
	}

	@Override
	public Map<String, String> getHiddenFields()
	{
		final String accountType = jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator();
		final String orderType = cartService.getSessionCart().getOrderType().getCode();

		Map<String, String> hideFields = null;
		final String fieldString = Config.getParameter(orderType + "." + accountType);
		if (StringUtils.isNotEmpty(fieldString))
		{
			hideFields = new HashMap<String, String>();
			final String[] fieldsToHide = fieldString.split(",");
			for (final String string : fieldsToHide)
			{
				hideFields.put(string, "strictHide");
			}
		}
		return hideFields;
	}

	@Override
	public Map<String, String> getMandatoryFields()
	{
		final String accountType = jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator();
		final String orderType = cartService.getSessionCart().getOrderType().getCode();

		Map<String, String> requiredFields = null;
		final String fieldString = Config.getParameter(orderType + "." + accountType + "." + "mandatoryFields");
		if (StringUtils.isNotEmpty(fieldString))
		{
			requiredFields = new HashMap<String, String>();
			final String[] fieldsReqired = fieldString.split(",");
			for (final String string : fieldsReqired)
			{
				requiredFields.put(string, "required");
			}
		}
		return requiredFields;
	}

	@Override
	public boolean saveSurgeryInfo(final JnjGTSurgeryInfoData infoForm)
	{
		final JnjGTSurgeryInfoModel JnjGTSurgeryInfoModel = modelService.create(JnjGTSurgeryInfoModel.class);
		JnjGTSurgeryInfoModel.setSurgerySpecialty(infoForm.getSurgerySpecialty());
		JnjGTSurgeryInfoModel.setOrthobiologics(infoForm.getOrthobiologics());
		JnjGTSurgeryInfoModel.setSurgicalApproach(infoForm.getSurgicalApproach());
		JnjGTSurgeryInfoModel.setProcedureType(infoForm.getProcedureType());
		JnjGTSurgeryInfoModel.setInterbody(infoForm.getInterbody());
		JnjGTSurgeryInfoModel.setInterbodyFusion(infoForm.getInterbodyFusion());
		JnjGTSurgeryInfoModel.setCas(infoForm.getCas());

		final Date caseDate = JnjGTCoreUtil.convertStringToDate(infoForm.getCaseDate(), Jnjb2bCoreConstants.GENERIC_DATE_FORMAT);
		if (null != caseDate)
		{
			JnjGTSurgeryInfoModel.setCaseDate(caseDate);
		}
		JnjGTSurgeryInfoModel.setCaseNumber(infoForm.getCaseNumber());
		JnjGTSurgeryInfoModel.setLevelsInstrumented(infoForm.getLevelsInstrumented());
		JnjGTSurgeryInfoModel.setPathology(infoForm.getPathology());
		JnjGTSurgeryInfoModel.setZone(infoForm.getZone());
		final CartModel sessionCart = cartService.getSessionCart();
		JnjGTSurgeryInfoModel.setOwner(sessionCart);
		sessionCart.setSurgeryInfo(JnjGTSurgeryInfoModel);
		saveCartModel(sessionCart, false);
		return jnjModelService.save(JnjGTSurgeryInfoModel);
	}

	@Override
	public String getOrderType()
	{
		return cartService.getSessionCart().getOrderType().getCode();
	}

	@Override
	public boolean updateSpecialStockPartner(final String specialStockPartner, final int entryNumber)
	{
		/*
		 * OS-23 this method should remain as is right now. Will need to get some clarification going forward, but the
		 * logic seems to make sense for line level
		 */
		final String METHOD_NAME = "updateSpecialStockPartner()";
		final String functionalityName = "Validate Special Stock Partner";
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		boolean validated = false;

		CommonUtil.logDebugMessage(functionalityName, METHOD_NAME, "Product is a JNJ NA Product. Proceeding with validation.",
				LOGGER);
		final String francMajorGrpCode = ((JnJProductModel) entry.getProduct()).getFrancMjrPrdGrpCd();
		final JnjGTDivisonData divisionData = jnjGTCustomerService.getPopulatedDivisionData(null);
		final boolean primarySalesRepOnly = divisionData.isIsSpine();
		if (null != francMajorGrpCode)
		{
			final Set<String> specialStockPartners = getSalesRepUCN(francMajorGrpCode, primarySalesRepOnly);
			if (CollectionUtils.isNotEmpty(specialStockPartners))
			{
				for (final String uniqueCustomer : specialStockPartners)
				{
					if (uniqueCustomer.matches("(0*)" + specialStockPartner))
					{
						CommonUtil.logDebugMessage(functionalityName, METHOD_NAME,
								"Special Stock Partner Validation SUCCESSFUL! Going to save...", LOGGER);
						entry.setSpecialStockPartner(uniqueCustomer);
						validated = true;
						break;
					}
				}
			}
			if (!validated)
			{
				entry.setSpecialStockPartner(StringUtils.EMPTY);
			}
			saveAbstOrderEntry(entry);
		}
		return validated;
	}

	@Override
	public Set<String> getSpineOrderHeaderUCN()
	{
		/* OS-23 Lookup Spine territories for account and then apply similar logic as getSalesRepUCN function */
		final Set<String> salesRepUCNs = new HashSet();
		final JnJB2BUnitModel currentAccount = jnjGTB2BUnitService.getCurrentB2BUnit();
		//Now we want to get all Spine Territories for this account
		final Set<String> territoryPKSet = new HashSet<String>();
		final Set<PrincipalGroupModel> assignedGroupsToUnit = currentAccount.getGroups();
		for (final PrincipalGroupModel principalGroupModel : assignedGroupsToUnit)
		{
			if (principalGroupModel instanceof JnjGTTerritoryDivisonModel)
			{
				final JnjGTTerritoryDivisonModel territoryDiv = (JnjGTTerritoryDivisonModel) principalGroupModel;
				if (StringUtils.equalsIgnoreCase(JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_SPINE),
						territoryDiv.getDivCode()))
				{
					territoryPKSet.add(territoryDiv.getPk().toString());
				}
			}
		}
		if (CollectionUtils.isNotEmpty(territoryPKSet))
		{
			final List<JnjGTTerritoryDivCustRelModel> unitRelWithTerritories = jnjGtTerritoryService
					.getTerritoryNCustomerRels(territoryPKSet);

			// Filter the CustRelModels to create a final list of strings having principals of type User, not B2BUnitModel
			for (final JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModel : unitRelWithTerritories)
			{
				salesRepUCNs.add(JnjGTTerritoryDivCustRelModel.getUniqueCustomer().getUid());
			}
		}
		return salesRepUCNs;
	}

	@Override
	public Set<String> getSalesRepUCN(final String francMajorGrpCode, final boolean primarySalesRepOnly)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getSalesRepPrimaryUCN()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		// Empty list of string to be returned
		final Set<String> onlyUserNATerrDivCustRel = new HashSet();

		// To fetch all the Territory Division models based on the first prod code
		final JnjGTTerritoryDivProdMappingModel JnjGTTerritoryDivProdMapping = new JnjGTTerritoryDivProdMappingModel();
		//to be changed
		JnjGTTerritoryDivProdMapping.setMajorGroupCode(francMajorGrpCode);

		List<JnjGTTerritoryDivProdMappingModel> JnjGTTerritoryDivProdMappings = null;
		try
		{
			JnjGTTerritoryDivProdMappings = flexibleSearchService.getModelsByExample(JnjGTTerritoryDivProdMapping);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}

		// If some territory division models are found
		if (CollectionUtils.isNotEmpty(JnjGTTerritoryDivProdMappings))
		{
			// use the first one
			//use all models not the first one while checking today's date is between start & end date range

			for (final JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel : JnjGTTerritoryDivProdMappings)
			{
				final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGTTerritoryDivProdMappingModel.getTerritoryDivison();

				if (null != jnjGTTerritoryDivisonModel)
				{
					// Get DivCusRelModels
					final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel = new JnjGTTerritoryDivCustRelModel();
					jnjGTTerritoryDivCustRel.setSource(jnjGTTerritoryDivisonModel);
					if (primarySalesRepOnly)
					{
						jnjGTTerritoryDivCustRel.setPrimarySalesRep(Boolean.TRUE);
					}

					List<JnjGTTerritoryDivCustRelModel> JnjGTTerritoryDivCustRelModels = null;
					try
					{
						JnjGTTerritoryDivCustRelModels = flexibleSearchService.getModelsByExample(jnjGTTerritoryDivCustRel);
					}
					catch (final ModelNotFoundException exp)
					{
						LOGGER.error("Model was not found with the loaded attributes");
					}

					// Filter the CustRelModels to create a final list of strings having principals of type User, not B2BUnitModel
					for (final JnjGTTerritoryDivCustRelModel JnjGTTerritoryDivCustRelModel : JnjGTTerritoryDivCustRelModels)
					{
						if (null != JnjGTTerritoryDivCustRelModel.getUniqueCustomer())
						{
							onlyUserNATerrDivCustRel.add(JnjGTTerritoryDivCustRelModel.getUniqueCustomer().getUid());
						}
					}
				}
			}
		}
		return onlyUserNATerrDivCustRel;
	}

	@Override
	public boolean updateSalesRepUCN(final String salesRepUCN, final String specialStockPartner)
	{
		final CartModel cart = cartService.getSessionCart();

		// Update the salesRepUCN
		cart.setSpineSalesRepUCN(salesRepUCN);

		// For each cart entry, if special stock partner is null, update it
		final List<AbstractOrderEntryModel> cartEntries = cart.getEntries();
		for (final AbstractOrderEntryModel abstractOrderEntryModel : cartEntries)
		{
		        String sspVal = abstractOrderEntryModel.getSpecialStockPartner();
		       if(sspVal== null){
			abstractOrderEntryModel.setSpecialStockPartner(specialStockPartner);
		       }
		       }
		return saveCartModel(cart, true);
	}

	@Override
	public boolean updateThirdPartyFlag(final Boolean thirdPartyFlag)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setThirdpartyBilling(thirdPartyFlag);
		return saveCartModel(cartModel, false);
	}


	@Override
	public boolean updateCustomerPONumber(final String customerPO)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setCustomerReferencePO(customerPO);
		return saveCartModel(cartModel, false);
	}

	@Override
	public boolean updateOverridenPrice(final String reasonCode, final String approver, final double overridePrice,
			final int entryNumber)
	{
		final CartEntryModel entry = (CartEntryModel) getEntryModelForNumber(cartService.getSessionCart(), entryNumber);
		entry.setPriceOverrideApprover(approver);
		entry.setPriceOverrideReason(reasonCode);
		entry.setPriceOverride(Double.toString(overridePrice));
		//ZDEL Enhancement
		if (overridePrice < (0.9 * entry.getBasePrice()))
		{
			entry.setHoldCode(Jnjb2bCoreConstants.Order.PRICE_OVERRIDE_HOLD_CODE);
			entry.setPriceOverrideApprover("");
			entry.setPriceOverrideReason("");
		}
		calculateSessionCart();
		return saveAbstOrderEntry(entry);
	}

	@Override
	public boolean updateCordisHouseAccount(final String cordisHouseAccount)
	{
		// Fetch the b2bUnitModel for the entered account number
		final B2BUnitModel b2bUnitModel = jnjGTB2BUnitDao.getB2BUnitByUid(cordisHouseAccount);

		// If b2bUnit is found and its of type JnJB2BUnitModel
		if (b2bUnitModel != null && b2bUnitModel instanceof JnJB2BUnitModel)
		{
			final CartModel cartModel = cartService.getSessionCart();
			cartModel.setCordisHouseAccount(cordisHouseAccount);
			cartModel.setCordisHouseAccount(b2bUnitModel.getUid());
			return saveCartModel(cartModel, false);
		}
		else
		{
			return false;
		}
	}


	
	@Override
	public String updateSurgeon(final String surgeonId, final String name, final String hospitalId)
	{
		/*final JnjGTSurgeonModel surgeon = new JnjGTSurgeonModel();
		surgeon.setSurgeonId(surgeonId);
		final JnjGTSurgeonModel existingSurgeon = jnjGTSurgeonService.getJnjGTSurgeonModelByExample(surgeon);

		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setSurgeon(existingSurgeon);
		if (saveCartModel(cartModel, false) && existingSurgeon != null)
		{
			final StringBuilder surgeonName = new StringBuilder();

			// If first name is not blank
			if (StringUtils.isNotBlank(existingSurgeon.getFirstName()))
			{
				surgeonName.append(existingSurgeon.getFirstName()).append(Jnjb2bCoreConstants.UserSearch.SPACE);
			}
			// If last name is not blank
			if (StringUtils.isNotBlank(existingSurgeon.getLastName()))
			{
				surgeonName.append(existingSurgeon.getLastName());
			}
			return surgeonName.toString();
		}
		return Jnjb2bCoreConstants.ADDTO_CART_ERROR;*/

		final CartModel cartModel = getCartService().getSessionCart();
		JnjGTSurgeonModel surgeonModel = null;
		List<JnjGTSurgeonModel> surgeonModelList = null;
		String surgName = null;
		if (!surgeonId.equalsIgnoreCase("null"))
		{
			final JnjGTSurgeonModel surgeon = new JnjGTSurgeonModel();
			surgeon.setSurgeonId(surgeonId);
			if (!hospitalId.equals(""))
			{
				surgeon.setHospitalId(hospitalId);
			}

			surgeonModelList = flexibleSearchService.getModelsByExample(surgeon);
			if (surgeonModelList != null && !(surgeonModelList.isEmpty()))
			{
				surgeonModel = surgeonModelList.get(0);
				surgName = JnjGTCoreUtil.formatValuesByCommaSeparated(surgeonModel.getFirstName(), surgeonModel.getMiddleName(),
						surgeonModel.getLastName()) + "(" + surgeonId + ")";
			}
		}
		if (name.equalsIgnoreCase(surgName))
		{
			cartModel.setSurgeon(surgeonModel);
		}
		else
		{
			cartModel.setSurgeonName(name.toUpperCase());
			cartModel.setSurgeon(null);
			surgName = name.toUpperCase();
		}
		if (saveCartModel(cartModel, false))
		{
			return surgName;
		}
		return Jnjb2bCoreConstants.ADDTO_CART_ERROR;

	}

	@Override
	public boolean isEligibleForOrderReturn()
	{
		boolean isEligibleForOrderReturn = false;
		final JnJB2bCustomerModel currentUser = jnjGTCustomerService.getCurrentUser();
		final List<String> deisiredClassOfTrades = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.HomePage.ORDER_RETURN_DEDIRED_CLASS_OF_TRADE, Jnjb2bCoreConstants.Register.COMMA_SEPARATOR);
		if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS) != null)
		{
			final boolean isOrderPlaced = sessionService.getAttribute(Jnjb2bCoreConstants.Login.ORDERING_RIGHTS);
			if (null != currentUser.getCurrentB2BUnit() && deisiredClassOfTrades.contains(currentUser.getCurrentB2BUnit().getIndicator()) && isOrderPlaced)
			{
				isEligibleForOrderReturn = true;
			}
		}
		return isEligibleForOrderReturn;
	}


	/* Method to Determine Default Order Type For User */
	@Override
	public JnjOrderTypesEnum getDefaultOrderType()
	{

		JnjOrderTypesEnum orderTypeEnum = JnjOrderTypesEnum.ZOR;
		//If the user has View Only Rights Then Add To Cart will be Quote Type
		if (null != sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)
				&& (sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE).equals(JnjGTUserTypes.VIEW_ONLY) || sessionService
						.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE).equals(JnjGTUserTypes.VIEW_ONLY_SALES_REP)))
		{
			orderTypeEnum = JnjOrderTypesEnum.ZQT;
		}
		else
		{
			final String currentSiteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
			if (Jnjb2bCoreConstants.CONS.equals(currentSiteName))
			{
				orderTypeEnum = JnjOrderTypesEnum.ZHOR;
				final String affiliateAccName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_CONS_AFFILIATE);
				final String tradeAccName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_TRADE);
				final String houseAccName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_HOUSE);

				if (jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator() != null)
				{
					if (affiliateAccName.equalsIgnoreCase(jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator()))
					{
						orderTypeEnum = JnjOrderTypesEnum.ZIO2;
					}
					if (tradeAccName.equalsIgnoreCase(jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator()))
					{
						orderTypeEnum = JnjOrderTypesEnum.ZTOR;
					}
					if (houseAccName.equalsIgnoreCase(jnjGTB2BUnitService.getCurrentB2BUnit().getIndicator()))
					{
						orderTypeEnum = JnjOrderTypesEnum.ZHOR;
					}
				}
			}
			//Setting STANDARD for PCM users.
			/*else if (JnjPCMCoreConstants.PCM.equals(currentSiteName))
			{
				orderTypeEnum = JnjOrderTypesEnum.ZOR;
			}*/

			else
			{
				// Get all the applicable order types for the user
				final Set<String> orderTypes = jnjGTB2BUnitService.getOrderTypesForAccount();

				if (CollectionUtils.isNotEmpty(orderTypes))
				{
					// Create a temporary list out of set so that sorting can be done
					final List<String> orderTypesInList = new ArrayList<String>(orderTypes);

					// Create instance of comparator
					final JnjGTOrderTypeComparator JnjGTOrderTypeComparator = new JnjGTOrderTypeComparator();

					// Sort the list
					Collections.sort(orderTypesInList, JnjGTOrderTypeComparator);

					// Return the first option
					orderTypeEnum = JnjOrderTypesEnum.valueOf(orderTypesInList.get(0));
				}
				//Needs to be Modified as per the user type in MDD
				//orderTypeEnum = JnjOrderTypesEnum.ZOR;
			}
		}
		return orderTypeEnum;
	}


	@Override
	public boolean updateFreightCost(final double freightCost)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setDeliveryCost(freightCost);
		return saveCartModel(cartModel, false);
	}

	@Override
	public void createMediaModelForCart(final File file, final String name) throws BusinessException
	{

		if (file.exists())
		{

			final CatalogVersionModel catalogVersionModel = catalogVersionService.getCatalogVersion("mstrContCatalog", "Staged");
			InputStream inputStream = null;
			MediaModel mediaModel = null;
			try
			{
				/* Fetching existing media from HYBRIS */
				mediaModel = mediaService.getMedia(catalogVersionModel, file.getName());
			}
			catch (final UnknownIdentifierException e)
			{
				LOGGER.info("No Media Found with code:" + file.getName(), e);
			}

			if (null == mediaModel)
			{
				mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
				mediaModel.setFolder(mediaService.getFolder("cartAttachments")); //Setting the Media folder
			}
			
			mediaModel.setCatalogVersion(catalogVersionModel);
			mediaModel.setCode(file.getName());
			mediaModel.setRealFileName(file.getName());
			/** Checking if the file is for the signature flow **/
			if (file.getName().contains(SIGNATURE))
			{
				/** Setting the name of the user in the description field of the media model **/
				mediaModel.setDescription(name);
			}
			modelService.save(mediaModel);
			try
			{
				inputStream = new FileInputStream(file); // Creating InputStream from PDF file,
			}
			catch (final FileNotFoundException fileNotFoundException)
			{
				throw new BusinessException("FileNotFoundException occured.");
			}
			try
			{
				mediaService.setStreamForMedia(mediaModel, inputStream, file.getName(),
						Files.probeContentType(Path.of(file.getAbsolutePath()))); //Setting the File into the Media Model
				modelService.save(mediaModel);
			}
			catch (final MediaIOException | IllegalArgumentException | IOException mediaIOException)
			{

				throw new BusinessException("MediaIOException occured.");
			}
			final CartModel cartModel = cartService.getSessionCart();
			/** Checking if the file is for the signature flow **/
			if (file.getName().contains(SIGNATURE))
			{
				/** THEN : Save the signature field **/
				cartModel.setSignature(mediaModel);
			}
			else
			{
				/** ELSE : Save the attached doc field **/
				cartModel.setAttachedDoc(mediaModel);
			}
			saveCartModel(cartModel, false);
		}
	}

	/*Added for AAOL-4937*/
	public MediaModel createMediaModelForCartReturn(final File file)
	{
		MediaModel mediaModel = null;

		if (file.exists())
		{
			final CatalogVersionModel catalogVersionModel = getCatalogVersion();
			
			InputStream inputStream = null;
			try
			{
				/* Fetching existing media from HYBRIS */
				mediaModel = mediaService.getMedia(catalogVersionModel, file.getName());
			}
			catch (final UnknownIdentifierException e)
			{
				LOGGER.info("No Media Found with code:" + file.getName(), e);
			}
				
			if (null == mediaModel)
			{
				mediaModel = modelService.create(MediaModel.class); //Creating a new Media Model
				mediaModel.setFolder(mediaService.getFolder("returnAttachments")); //Setting the Media folder
				mediaModel.setCatalogVersion(catalogVersionModel);
				mediaModel.setCode(file.getName());
				mediaModel.setRealFileName(file.getName());
				modelService.save(mediaModel);
				try {
					inputStream = new FileInputStream(file);
					try {
						mediaService.setStreamForMedia(mediaModel, inputStream, file.getName(),
								Files.probeContentType(Path.of(file.getAbsolutePath())));
						modelService.save(mediaModel);
					} catch (MediaIOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} //Setting the File into the Media Model
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 }
			
		}
		return mediaModel;
	}
	public boolean saveReturnMedia(List<MultipartFile> returnUploadFiles)
	{
		boolean isReturnMediaSave = Boolean.FALSE;
		List<MediaModel> returnImagesModel = new ArrayList<MediaModel>();
        
		final CartModel cartModel = cartService.getSessionCart();
		
		if (null != returnUploadFiles && returnUploadFiles.size() > 0) {
          for (MultipartFile multipartFile : returnUploadFiles) {

              String fileName = multipartFile.getOriginalFilename();
              if (!"".equalsIgnoreCase(fileName)) {
                  // Handle file content - multipartFile.getInputStream()
                  try{
                		 String nameMimeArray[] = fileName.split("\\.");
                		 File parent = new File(System.getProperty("java.io.tmpdir"));   

                		 File temp=new File(parent,cartModel.getCode()+"_"+fileName);
                		 if (!temp.exists()) {
                		     temp.createNewFile();
                		 }

	                    multipartFile.transferTo(temp);
	                    MediaModel returnImageModel= createMediaModelForCartReturn(temp);
	                    returnImagesModel.add(returnImageModel);
                  }
                  catch (IllegalStateException | IOException exception){
                  		exception.printStackTrace();
						LOGGER.error("CART_PAGE_CONTROLLER" + "uploadFileforReturn()" + Logging.HYPHEN + "Error in Uploading File"
								+ exception.getMessage(), exception);
					}
              }
          }
			cartModel.setReturnImages(returnImagesModel);
			isReturnMediaSave = jnjGTCartService.saveCartModel(cartModel, false);
			
      }
	  return isReturnMediaSave;
	}
	/*End of AAOL-4937*/
	
	@Override
	public boolean preSAPReturnCartValidation()
	{
		boolean validationError = false;
		final CartModel cartModel = cartService.getSessionCart();
		List<AbstractOrderEntryModel> cartEntries = null;
		cartEntries = cartModel.getEntries();

		boolean invoiceValidationReq = true;
		for (final AbstractOrderEntryModel cartEntry : cartEntries)
		{
			final String batchNum = cartEntry.getBatchNum();
			final String returnInvNumber = cartEntry.getReturnInvNumber();
			final JnJProductModel product = ((JnJProductModel) cartEntry.getProduct());
			final List<String> entryErrors = new ArrayList<String>();

			//Invoice number is not required in case of Distributor or CSC User
			if (jnjGTB2BUnitService.isCSCUser() || jnjGTB2BUnitService.isCustomerDistributor())
			{
				if (StringUtils.isEmpty(returnInvNumber))
				{
					invoiceValidationReq = false;
					if (StringUtils.isNotEmpty(batchNum) && !jnjGTCartDao.validateLotNumber(product, batchNum)) //lot# is not required and valid for current line
					{
						entryErrors.add("cart.return.lotNumberNotFound");
					}
				}
			}

			//Validate invoice#, Batch# validation mapping for non CSC/Distributor
			if (invoiceValidationReq)
			{
				final JnjGTInvoiceEntryModel invoiceEntry = jnjGTCartDao.findInvoiceEntryforProduct(product, returnInvNumber);
				//if (!jnjGTCartDao.validateInvoiceNumber(product, returnInvNumber)) // Check if invoice number is valid.
				if (null == invoiceEntry) //Invoice number is invalid for given product
				{
					entryErrors.add("cart.return.invoiceNumber.enter");
				}
				else
				{
					if (null != invoiceEntry.getQty() && (cartEntry.getQuantity().longValue() > invoiceEntry.getQty().longValue()))
					{
						entryErrors.add("cart.return.invalid.qty");
						cartEntry.setQuantity(invoiceEntry.getQty());
					}
					if (StringUtils.isNotEmpty(batchNum) && !jnjGTCartDao.validateLotNumber(product, batchNum)) //lot# is not required and valid for current line
					{
						entryErrors.add("cart.return.lotNumberNotFound");
					}
					/*
					 * else if (StringUtils.isNotEmpty(batchNum) && !jnjGTCartDao.isValidLotNumber(returnInvNumber,
					 * batchNum))//If lot number is required it must be valid againt invoice number {
					 * entryErrors.add("cart.return.lotNumberAndinvoiceNumber.notFound"); }
					 */
				}
			}
			if (CollectionUtils.isNotEmpty(entryErrors))
			{
				validationError = true;
				((CartEntryModel) cartEntry).setValidationErrorKeys(entryErrors);
				saveAbstOrderEntry(cartEntry);
			}else{
				((CartEntryModel) cartEntry).setValidationErrorKeys(entryErrors);
				saveAbstOrderEntry(cartEntry);
			}
			
			
			
			
			
		}
		return validationError;
	}

	protected String addToCartDeliveredOrder(final CartModel cartModel, final long entredQuantity, String addToCartMsgKey,
			final JnJProductModel modProduct, final JnJProductModel baseProduct, final UnitModel productUnit,
			final CommerceCartModification cartModification)
	{

		final List<CartEntryModel> entriesForProd = cartService.getEntriesForProduct(cartModel, baseProduct);
		long quantityToAdd = entredQuantity;
		if (entredQuantity <= 0)
		{
			quantityToAdd = 1;
			addToCartMsgKey = BASKET_PAGE_MESSAGE_MIN_QTY_ADDED;
		}
		CartEntryModel cartEntryModel = null;
		if (CollectionUtils.isEmpty(entriesForProd))
		{
			final JnjGTVariantProductModel salesVariant = jnJGTProductService.getSalesGTIN(modProduct);

			// Add the entered variant
			cartEntryModel = cartService.addNewEntry(cartModel, baseProduct, quantityToAdd, productUnit, -1, false);
			cartEntryModel.setReferencedVariant(salesVariant);
			/* Start: Set Special stock partner for Entry */
			if (cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) && StringUtils.isNotEmpty(baseProduct.getFrancMjrPrdGrpCd()))
			{
				LOGGER.info("Start : Align Sales Rep UCN to Entry");
				final Set<String> UCNMap = getSalesRepUCN(baseProduct.getFrancMjrPrdGrpCd(), false);
				if (null != UCNMap && !UCNMap.isEmpty())
				{
					cartEntryModel.setSpecialStockPartner(UCNMap.iterator().next());
				}
				LOGGER.info("End : Align Sales Rep UCN to Entry");
			}
			/* End: Set Special stock partner for Entry */
		}
		/** If the Base Product(parents parent product) is exists in the cart **/
		else
		{
			cartEntryModel = entriesForProd.get(0);
			final long finalQtyToAdd = cartEntryModel.getQuantity().longValue() + quantityToAdd;
			cartEntryModel.setQuantity(Long.valueOf(finalQtyToAdd));
		}
		cartEntryModel.setCalculated(Boolean.FALSE);
		cartModification.setEntry(cartEntryModel);
		cartModification.setProduct(baseProduct);
		cartModification.setQuantityAdded(quantityToAdd);
		return addToCartMsgKey;
	}

	protected void changeRefVariantsToSalesUOM()
	{
		final CartModel cartModel = cartService.getSessionCart();
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			final JnjGTVariantProductModel salesUOM = jnJGTProductService.getSalesGTIN((JnJProductModel) entry.getProduct());
			if (null != salesUOM && !salesUOM.equals(entry.getReferencedVariant()))
			{
				entry.setReferencedVariant(salesUOM);
			}
		}
	}

	protected void roundOffQtyForCartEntries()
	{
		final CartModel cartModel = cartService.getSessionCart();

		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			final JnjGTVariantProductModel delVariant = jnJGTProductService.getDeliveryGTIN(entry.getProduct());
			final long derivedQuantity = getRoundedQuantity(delVariant, entry.getReferencedVariant(), entry.getQuantity()
					.longValue());
			entry.setQuantity(Long.valueOf(derivedQuantity));
		}
	}

	@Override
	public void initiateReplenishForDelivered(final String orderNum) throws BusinessException
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderNum);
		modelService.detach(order);
		final CartModel tempCartModel = b2bCartService.createCartFromAbstractOrder(order);

		final CartModel cartModel = getCartService().getSessionCart();
		removeAllEntries(cartModel);
		getSessionService().setAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME, cartModel);

		final List<AbstractOrderEntryModel> cartentries = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel entry : tempCartModel.getEntries())
		{
			entry.setOrder(cartModel);
			entry.setCustLineNumber(entry.getSapOrderlineNumber());
			entry.setFreightFees(null);
			entry.setPriceOverride(null);
			entry.setPriceOverrideApprover(null);
			entry.setPriceOverrideReason(null);
			entry.setBatchNum(null);
			cartentries.add(entry);
		}

		cartModel.setEntries(cartentries);
		cartModel.setOrderType(JnjOrderTypesEnum.ZKB);
		cartModel.setAssociatedSAPOrderNum(order.getSapOrderNumber());
		cartModel.setLinkedWithDelivered(true);
		cartModel.setDropShipAccount(StringUtils.EMPTY);
		try
		{

			/* Unit will be SSP of first Product in cart */
			final String accForReplenish = order.getEntries().get(0).getSpecialStockPartner();

			final B2BUnitModel unit = jnjGTB2BUnitService.getUnitForUid(accForReplenish);
			cartModel.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress((JnJB2BUnitModel) unit));
			cartModel.setPaymentAddress(jnjGTB2BUnitService.getBillingAddress((JnJB2BUnitModel) unit));
			cartModel.setUnit(unit);
		}
		catch (final UnknownIdentifierException unkownUnitId)
		{
			LOGGER.error("B2bUnit does not exists for spl stock partner while initiating Replenish for Del order", unkownUnitId);
			throw new BusinessException("B2bUnit does not exists for spl stock partner while initiating Replenish for Del order");
		}

		final JnjGTDivisonData divisonData = jnjGTCustomerService.getPopulatedDivisionData(null);
		if (divisonData.isIsSpine())
		{
			//For JJEPIC-752
			if (StringUtils.isNotEmpty(order.getPurchaseOrderNumber()))
			{
				cartModel.setPurchaseOrderNumber("R" + order.getPurchaseOrderNumber());
			}
			else
			{
				cartModel.setPurchaseOrderNumber("R");
			}
		}
		else if (divisonData.isIsMitek())
		{
			final String userName = order.getUser().getName();

			final SimpleDateFormat dateFormatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			dateFormatter.setTimeZone(TimeZone.getTimeZone("EST5EDT")); //Use the EST time zone
			final SimpleDateFormat poFormatter = new SimpleDateFormat("MMddyy");
			final String poSufix = poFormatter.format(new Date());
			LOGGER.info("Date As Per EST is :" + poSufix);

			if (StringUtils.isNotEmpty(userName) && userName.contains(Jnjb2bCoreConstants.UserSearch.SPACE))
			{
				cartModel.setPurchaseOrderNumber((userName.split(Jnjb2bCoreConstants.UserSearch.SPACE)[1] + poSufix).toUpperCase());
			}
			else
			{
				cartModel.setPurchaseOrderNumber(poSufix);
			}
		}
		calculateCart(cartModel);
		//saveCartModel(cartModel, true);
		getCartService().setSessionCart(cartModel);
	}

	@Override
	public void clearDropShipPurchaseOrderNum()
	{
		// clear drop ship and purchase order number
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setDropShipAccount(null);
		cartModel.setDealerPONum(null);
		saveCartModel(cartModel, false);
	}

	@Override
	//public CommerceCartModification updateQuantityForCartEntry(final CartModel cartModel, final long entryNumber, long newQuantity)
	
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		final long entryNumber = parameter.getEntryNumber();
		long newQuantity = parameter.getQuantity();
		final CartModel cartModel = parameter.getCart();
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
	
		final AbstractOrderEntryModel entryToUpdate = getEntryModelForNumber(cartModel, (int) entryNumber);
		
		// Start GTR-1720
				final JnjGTDivisonData divisonData = jnjGTCustomerService.getPopulatedDivisionData(null);
				
		if ((cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
						&& ((newQuantity > entryToUpdate.getQuantity()) && !(divisonData.isIsMitek())))
		{
					LOGGER.error("Cannot Increase the Quantity for Replinishment Orders");
					throw new CommerceCartModificationException("Cannot Increase the Product Quantity");

				}	
				
				
		if (newQuantity == 0L)
		{
			

			// Start GTR-1720
			if ((cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB)) && (cartModel.getEntries().size() == 1)
					&& !(divisonData.isIsMitek()))
			{
				throw new CommerceCartModificationException("Cannot remove the Product.");
			}
			// End GTR-1720
			else
			{
			modelService.remove(entryToUpdate);
			modelService.refresh(cartModel);
			if (cartModel.isContainsOCDProduct())
			{
				validateForOCDProdcuts(cartModel);
			}
			modelService.refresh(cartModel);
			}
		}
		else
		{
			//Rounding of Entries except for delivered or replenish order
			if (!cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) && !cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
			{
				newQuantity = getQuantityToBeAdded(jnJGTProductService.getDeliveryGTIN(entryToUpdate.getProduct()),
						entryToUpdate.getReferencedVariant(), newQuantity);
			}
			entryToUpdate.setQuantity(Long.valueOf(newQuantity));
			//Set the price to default price from Hybris DB.
			updatePriceForEntry(entryToUpdate);
		}
		//For PCM calculate cart is not required..
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		/*if (!siteName.equals(JnjPCMCoreConstants.PCM))
		{*/
			calculateCart(cartModel);
		/*}*/
		//
		modelService.refresh(cartModel);
		final CommerceCartModification modification = new CommerceCartModification();
		modification.setQuantityAdded(newQuantity);
		modification.setEntry(entryToUpdate);
		modification.setQuantity(newQuantity);
		return modification;
	}

	
	
		/*@Override
	//public CommerceCartModification updateQuantityForCartEntry(final CartModel cartModel, final long entryNumber, long newQuantity)
	
	public CommerceCartModification updateQuantityForCartEntry(final CommerceCartParameter parameter)
			throws CommerceCartModificationException
	{
		final long entryNumber = parameter.getEntryNumber();
		long newQuantity = parameter.getQuantity();
		final CartModel cartModel = parameter.getCart();
		ServicesUtil.validateParameterNotNull(cartModel, "Cart model cannot be null");
	
		final AbstractOrderEntryModel entryToUpdate = getEntryModelForNumber(cartModel, (int) entryNumber);
		
		
		if (newQuantity == 0L)
		{
			modelService.remove(entryToUpdate);
			modelService.refresh(cartModel);
			if (cartModel.isContainsOCDProduct())
			{
				validateForOCDProdcuts(cartModel);
			}
			modelService.refresh(cartModel);
		}
		else
		{
			//Rounding of Entries except for delivered or replenish order
			if (!cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) && !cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
			{
				newQuantity = getQuantityToBeAdded(jnJGTProductService.getDeliveryGTIN(entryToUpdate.getProduct()),
						entryToUpdate.getReferencedVariant(), newQuantity);
			}
			entryToUpdate.setQuantity(Long.valueOf(newQuantity));
			//Set the price to default price from Hybris DB.
			updatePriceForEntry(entryToUpdate);
		}
		//For PCM calculate cart is not required..
		final String siteName = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		if (!siteName.equals(JnjPCMCoreConstants.PCM))
		{
			calculateCart(cartModel);
		}
		//
		modelService.refresh(cartModel);
		final CommerceCartModification modification = new CommerceCartModification();
		modification.setQuantityAdded(newQuantity);
		modification.setEntry(entryToUpdate);
		modification.setQuantity(newQuantity);
		return modification;
	}*/

	protected void validateForOCDProdcuts(final CartModel cartModel)
	{
		for (final AbstractOrderEntryModel orderEntry : cartModel.getEntries())
		{
			final String salesOrgCode = ((JnJProductModel) orderEntry.getProduct()).getSalesOrgCode();
			if (StringUtils.equalsIgnoreCase(salesOrgCode, OCD_PRODUCT_SALES_ORG))
			{
				return;
			}
		}
		cartModel.setContainsOCDProduct(false);
		modelService.save(cartModel);
	}

	/*@Override
	public boolean updateCartModelWithSelectedAttributes(final SelectedAttributesForm selectedAttributesForm)
	{
		final List<String> selectedAttributes = new ArrayList<String>();
		if (null != selectedAttributesForm.getSelectedProductAttributes())
		{
			for (final String formAttribute : selectedAttributesForm.getSelectedProductAttributes())
			{
				if (formAttribute.contains(HYPHEN))
				{
					final String[] attriButesArray = formAttribute.split(",");
					selectedAttributes.addAll(Arrays.asList(attriButesArray));
				}
				else
				{
					selectedAttributes.add(formAttribute);
				}
			}
		}

		final CartModel cartModel = cartService.getSessionCart();

		cartModel.setSelectedFileType(selectedAttributesForm.getFileFormat());
		cartModel.setSelectedAttributes(selectedAttributes);
		cartModel.setSelectedImageFormats(selectedAttributesForm.getImageFormat());
		cartModel.setSelectedTextFormat(selectedAttributesForm.getTextFormat());

		return saveCartModel(cartModel, false);
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.cart.JnjGTCartService#getFasteShippingMethods(java.lang.String)
	 */
	@Override
	public List<JnjGTShippingMethodModel> getFasterShippingMethods(final int rank, final List<String> routeList)
	{
		return jnjGTCartDao.getFasterShippingMethods(rank, routeList);
	}

	@Override
	public JnjGTShippingMethodModel getShippingMethodByRoute(final String defaultRoute)
	{

		JnjGTShippingMethodModel shippingModel = new JnjGTShippingMethodModel();
		shippingModel.setRoute(defaultRoute);
		try
		{
			shippingModel = flexibleSearchService.getModelByExample(shippingModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return shippingModel;
	}



	@Override
	public List<JnjGTEarlyZipCodesModel> getJnjGTEarlyZipCodesModelByZipCode(final JnjGTEarlyZipCodesModel JnjGTEarlyZipCodesModel)
	{
		List<JnjGTEarlyZipCodesModel> JnjGTEarlyZipCodesList = null;
		try
		{
			JnjGTEarlyZipCodesList = flexibleSearchService.getModelsByExample(JnjGTEarlyZipCodesModel);
		}
		catch (final ModelNotFoundException exp)
		{
			LOGGER.error("Model was not found with the loaded attributes");
		}
		return JnjGTEarlyZipCodesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.cart.JnjGTCartService#getFasteShippingMethods(java.lang.String)
	 */
	@Override
	public List<JnjGTShippingMethodModel> getRequiredShippingMethod(final List<String> requiredIds)
	{
		return jnjGTCartDao.getRequiredShippingMethod(requiredIds);
	}

	/**
	 * @param addressModel
	 */
	protected boolean updateDeliveryAddress(final AddressModel addressModel)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setDeliveryAddress(addressModel);
		return saveCartModel(cartModel, false);
	}
	
	protected boolean updateBillAddress(final AddressModel addressModel)
	{
		final CartModel cartModel = cartService.getSessionCart();
		cartModel.setPaymentAddress(addressModel);
		return saveCartModel(cartModel, false);
	}

	@Override
	public JnjGTUpdatePriceData getCartSubTotal()
	{
		final CartModel cartModel = cartService.getSessionCart();
		final JnjGTUpdatePriceData updatePriceData = new JnjGTUpdatePriceData();
		double toalGrossValue = 0.0;
		for (final AbstractOrderEntryModel entry : cartModel.getEntries())
		{
			toalGrossValue += entry.getTotalPrice().doubleValue();
		}
		cartModel.setSubtotal(Double.valueOf(toalGrossValue));
		updatePriceData.setCurrencySymbol(cartModel.getCurrency().getSymbol());
		updatePriceData.setCartSubTotal(String.valueOf(cartModel.getSubtotal()));
		return updatePriceData;
	}


	@Override
	public void createCartFromOrder(final String orderId)
	{
		createCartFromOrder(orderId, null);
	}

	@Override
	public void createCartFromOrder(final String orderId, final JnjGTProductData productData)
	{
		final CartModel sessionCart = b2bCartService.getSessionCart();
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		final List<String> invalidProductCodes = new ArrayList<>();
		String lastAddedProductCode = null;
		String lastAddedProductQuantity = null;
		for (final AbstractOrderEntryModel entry : entries)
		{
			/*** Proceed ONLY when the below fields are empty i.e. NO dummy product is associated. ***/
			if (StringUtils.isEmpty(entry.getMaterialNumber()) && StringUtils.isEmpty(entry.getMaterialEntered()))
			{
				try
				{
					ProductModel product = entry.getProduct();
					final String productCode = product.getCode();
					/**
					 * Perform add to cart for the product if: 1. Entry product is still available for the account, & 2.
					 * Product is saleable and passes the validation.
					 */
					final ProductModel modProduct = jnJGTProductService.getProductByValue(product.getCode(), true);

					if (modProduct != null && validateProductCode(modProduct) == null)
					{
						if (modProduct instanceof JnJProductModel)
						{
							product = jnJGTProductService.getDeliveryGTIN(modProduct);
							lastAddedProductCode = modProduct.getCode();
						}
						else if (modProduct instanceof JnjGTVariantProductModel)
						{
							product = modProduct;
							lastAddedProductCode = ((JnjGTVariantProductModel) modProduct).getBaseProduct().getCode();
						}
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug("---------->Add to cart call for variant product code : " + product.getCode()
									+ " and product code :" + modProduct.getCode());

						}
						final CommerceCartModification commerceCartModification = this.addToCart(sessionCart, product, entry
								.getQuantity().longValue(), modProduct.getUnit(), false);
						if (null != commerceCartModification)
						{
							lastAddedProductQuantity = commerceCartModification.getQuantityAdded() + "";
						}
					}
					else
					{
						invalidProductCodes.add(productCode);
					}

				}
				catch (final CommerceCartModificationException modificaitonExp)
				{
					LOGGER.error("Creating cart from Order---  Product Code: " + entry.getProduct().getCode() + " from Order:"
							+ orderId + " can not be added to cart");
				}
			}

		}
		if (productData != null)
		{
			productData.setLastAddedProduct(lastAddedProductCode);
			productData.setLastAddedProductQuantity(lastAddedProductQuantity);
			if (CollectionUtils.isNotEmpty(invalidProductCodes))
			{
				productData.setInvalidProductCodes(StringUtils.join(invalidProductCodes, Jnjb2bCoreConstants.SYMBOl_COMMA));
			}
		}
		saveCartModel(sessionCart, true);
	}

	@Override
	public CommerceCartModification addToCartForPCM(final CartModel cartModel, final ProductModel productModel, final long qty,
			final UnitModel unit, final boolean forceNewEntry) throws CommerceCartModificationException
	{
		final Date startTime = new Date();
		final String addToCartMsgKey = "success";
		final CommerceCartModification cartModification = new CommerceCartModification();
		long quantityToAdd = qty;
		if (quantityToAdd == 0)
		{
			quantityToAdd = 1;
		}
		if (productModel instanceof JnjGTVariantProductModel)
		{
			final JnjGTVariantProductModel variantProduct = (JnjGTVariantProductModel) productModel;
			final JnJProductModel modProduct = (JnJProductModel) variantProduct.getBaseProduct();
			JnJProductModel baseProduct = modProduct.getMaterialBaseProduct();

			// if base product is null
			if (baseProduct == null)
			{
				baseProduct = modProduct;
			}

			final UnitModel productUnit = variantProduct.getUnit();

			final List<CartEntryModel> entriesForProd = cartService.getEntriesForProduct(cartModel, baseProduct);

			CartEntryModel cartEntryModel = null;
			//     If Base Product(parents parent product) of the added variant is not in the cart
			if (CollectionUtils.isEmpty(entriesForProd))
			{
				//long derivedQuantity = quantityToAdd;

				// Add the entered variant
				cartEntryModel = cartService.addNewEntry(cartModel, baseProduct, quantityToAdd, productUnit, -1, false);
				cartEntryModel.setReferencedVariant(variantProduct);
				cartModification.setQuantityAdded(quantityToAdd);
				cartModification.setProduct(baseProduct);
				cartModification.setEntry(cartEntryModel);
			}
			else
			{
				cartEntryModel = entriesForProd.get(0);
				cartEntryModel.setReferencedVariant(variantProduct);
				cartModification.setQuantityAdded(quantityToAdd);
				cartModification.setProduct(baseProduct);
				cartModification.setEntry(cartEntryModel);
			}
			/** If the Base Product(parents parent product) is exists in the cart **/

			final Date endTime = new Date();

			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("---------->Total Time taken to execute JnjGTCartService.addToCart().addToCartGeneric() : "
						+ (endTime.getTime() - startTime.getTime()) / (1000) + "seconds");

			}
			cartModification.setStatusCode(addToCartMsgKey);
		}

		final Date endTime = new Date();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("------>Total Time taken to execute JnjGTCartService.addToCart() : "
					+ (endTime.getTime() - startTime.getTime()) + "seconds");

		}
		saveCartModel(cartModel, true);
		return cartModification;
	}

	@Override
	public List<CommerceCartModification> addMultiProdsToCartPCM(final CartModel cartModel, final Set<ProductModel> products)
	{
		final String addToCartMsgKey = "success";
		CommerceCartModification cartModification = null;
		final List<CommerceCartModification> cartModificationList = new ArrayList<CommerceCartModification>();

		CartEntryModel cartEntryModel = null;
		JnJProductModel product = null;
		List<CartEntryModel> entriesForProd = null;

		/** Quantity is not considered in PCM Cart, Hence, we are always setting quantity to be added equal to 1 **/
		final long quantityToAdd = 1;

		final UnitModel productUnit = jnjUnitService.getUnit(EACH);
		for (final ProductModel productModel : products)
		{
			cartModification = new CommerceCartModification();
			if (productModel instanceof JnJProductModel)
			{
				product = (JnJProductModel) productModel;
				entriesForProd = cartService.getEntriesForProduct(cartModel, product);

				if (CollectionUtils.isEmpty(entriesForProd))
				{
					cartEntryModel = cartService.addNewEntry(cartModel, product, Jnjb2bCoreConstants.DEFAULT_ADD_TO_CART_QTY,
							productUnit, -1, false);
				}
			}
			cartModification.setQuantityAdded(quantityToAdd);
			cartModification.setProduct(product);
			cartModification.setStatusCode(addToCartMsgKey);
			if (cartEntryModel != null)
			{
				cartModification.setEntry(cartEntryModel);
			}
			/** If the Base Product(parents parent product) is exists in the cart **/
			else if (CollectionUtils.isNotEmpty(entriesForProd))
			{
				cartModification.setEntry(entriesForProd.get(0));
			}
			cartModificationList.add(cartModification);
		}

		saveCartModel(cartModel, true);
		return cartModificationList;
	}
	
	
	@Override
	public CommerceCartModification addToCartGT(CartModel cartModel, Map<ProductModel, Long> productQtyMap) {
		
		CommerceCartModification cartModification = addProductToCart(cartModel,productQtyMap,false,0);
		return cartModification;
	}
	
	@Override
	public CommerceCartModification addToCartGT(final CartModel cartModel, final Map<ProductModel, Long> productQtyMap, final boolean forceNewEntry, int currentEntryNumber)
	{
		CommerceCartModification cartModification = addProductToCart(cartModel,productQtyMap,forceNewEntry,currentEntryNumber);
		return cartModification;
	}

	protected CommerceCartModification addProductToCart(CartModel cartModel, Map<ProductModel, Long> productQtyMap,
			boolean forceNewEntry, int currentEntryNumber) {

		String addToCartMsgKey = "success";
		final Set<Entry<ProductModel, Long>> entrySet = productQtyMap.entrySet();
		final CommerceCartModification cartModification = new CommerceCartModification();

 		JnjGTVariantProductModel variantProduct = null;
		long enteredQty = 0;
		JnJProductModel modProduct = null;
		JnJProductModel baseProduct = null;

		if (cartModel.getOrderType().equals(JnjOrderTypesEnum.ZDEL) || cartModel.getOrderType().equals(JnjOrderTypesEnum.ZKB))
		{
			LOGGER.debug("Entering into the if block of addtoCartGT---------------");
			
			for (final Entry entry : entrySet)
			{
				variantProduct = (JnjGTVariantProductModel) entry.getKey();
				enteredQty = ((Long) entry.getValue()).longValue();
				modProduct = (JnJProductModel) variantProduct.getBaseProduct();
				baseProduct = modProduct.getMaterialBaseProduct();
				if (baseProduct == null)
				{
					baseProduct = modProduct;
				}
				addToCartDeliveredOrder(cartModel, enteredQty, addToCartMsgKey, modProduct, baseProduct, variantProduct.getUnit(),
						cartModification);
			}
		}
		else
		{
			LOGGER.debug("Entering into the else block of addtoCartGT---------------");
			for (final Entry entry : entrySet)
			{
				variantProduct = (JnjGTVariantProductModel) entry.getKey();
				enteredQty = ((Long) entry.getValue()).longValue();
				modProduct = (JnJProductModel) variantProduct.getBaseProduct();
				baseProduct = modProduct.getMaterialBaseProduct();
				if (baseProduct == null)
				{
					baseProduct = modProduct;
				}
				
				addToCartMsgKey = addToCartGenric(cartModel, enteredQty, addToCartMsgKey, variantProduct, modProduct, baseProduct,
						variantProduct.getUnit(), cartModification,forceNewEntry);
				// Copy line item changes Reshuffle by Tharun K
				if(forceNewEntry){
					final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>(cartModel.getEntries());
					Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
					for(int i=(entries.size()-1) ; i>=0 ;i--){
						AbstractOrderEntryModel currentEntry = entries.get(i);
						if(currentEntryNumber < currentEntry.getEntryNumber())						
						{
							currentEntry.setEntryNumber(i+1);
							getModelService().save(currentEntry);
						}
						else 
						{
							currentEntry.setEntryNumber(i+1);
							getModelService().save(currentEntry);
							AbstractOrderEntryModel newEntry = entries.get(entries.size()-1);
							newEntry.setEntryNumber(i);
							getModelService().save(newEntry);
							break;
						}
					}
					Collections.sort(entries, new BeanComparator(AbstractOrderEntryModel.ENTRYNUMBER, new ComparableComparator()));
				}

				
			}
		}
		cartModification.setStatusCode(addToCartMsgKey);
		LOGGER.info("Start : Calculate Cart");
		calculateCart(cartModel);
		LOGGER.info("End : Calculate Cart");
		return cartModification;
	
	}

	

	public String validateProductCode(final ProductModel productModel)
	{
		ProductModel productModelToValidate = productModel;
		final Date startTime = new Date();
		if (productModelToValidate != null)
		{
			// From session get the current site i.e. MDD/CONS
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

			// If site is MDD, variant needs to be searched upon GTIN and Base Material Number
			if (productModelToValidate instanceof JnjGTVariantProductModel)
			{
				productModelToValidate = ((JnjGTVariantProductModel) productModelToValidate).getBaseProduct();
			}
			final Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);

			// If product is not saleable return the corresponding error message
			final String status = jnJGTProductService.isProductSaleable((JnJProductModel) productModelToValidate, currentSite);
			if (null != status)
			{
				jnjGTOperationsService.logAuditData("User", status , "", false, false, new Date(), userService.getCurrentUser().getName());
				return status;
			}
			// If user is a sales rep with place order privilege, and If product is not of compatible division return the corresponding error message
			else if (Jnjb2bCoreConstants.MDD.equals(currentSite) && BooleanUtils.isTrue(salesRep)
					&& !jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) productModelToValidate))
			{
				return messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null,
						productModelToValidate.getCode());
			}
			else if (JnjOrderTypesEnum.ZHOR.equals(getCartService().getSessionCart().getOrderType())
					&& !jnJGTProductService.validateProductMfgForHouseOrder((JnJProductModel) productModelToValidate))
			{
				return messageService.getMessageForCode(Jnjgtb2bMDDConstants.Cart.DIVISION_ERROR, null,
						productModelToValidate.getCode());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("----->JnjGTCartServiceImpl - validateProduct(" + productModelToValidate.getCode()
					+ ") Time taken for Validating ProductModel: " + (new Date().getTime() - startTime.getTime()));
		}
		return null;
	}

	@Override
	public List<JnjIndirectCustomerModel> fetchIndirectCustomers(final String indirectCustomer, final CountryModel countryModel)
	{

		return jnjGTCustomerDAO.fetchIndirectCustomers(indirectCustomer, countryModel);
	}
	
	/**
	 * Fetch indirect customer name.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return the jnj indirect customer model
	 */
	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer, final CountryModel countryModel)
	{

		return jnjGTCustomerDAO.fetchIndirectCustomerName(indirectCustomer, countryModel);
	}
	
	
	/**
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @return the jnj indirect customer model
	 */
	@Override
	public JnjIndirectCustomerModel fetchIndirectCustomerName(final String indirectCustomer)
	{

		return jnjGTCustomerDAO.fetchIndirectCustomerName(indirectCustomer);
	}
	/*@Override
	public boolean updateCartModelWithSelectedAttributes(
			SelectedAttributesForm selectedAttributesForm) {
		// TODO Auto-generated method stub
		return false;
	}*/

	/**
	 * Comment Added BY SURABHI CHOUKSEY
	 *  Check Product in  contract list of Particular B2b unit  And  Checking Weather added product is contract product or normal product
	 * If Basket is having contract product than product base price become contract price 
	 * Or if it is Normal product which is not the part of contract than we will take product base price .
	 * **/
	@Override
	public boolean checkProductInContract(String productCode, boolean basePriceFlag,Map<String, List<JnjContractEntryModel>> contractProductMap,
			Map<String, Set<String>> cartOrSelMap) {

		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		LOGGER.info(currentB2BUnit + "B2BUnit" + baseSiteService.getCurrentBaseSite());
		String key1 = "CONTRACT";
		String key2 = "NONCONTRACT";
		String contractNumKey = null;
		int isContractCount = 0;
		int isNonContractCount = 0;
		List<String> contractProductList = new ArrayList<String>();
		if (currentB2BUnit != null) {
			List<JnjContractModel> contractList = (List<JnjContractModel>) currentB2BUnit.getContracts();
			if (CollectionUtils.isNotEmpty(contractList)) {
				for (JnjContractModel jnjContractModel : contractList) {
					contractNumKey = jnjContractModel.getECCContractNum();
					List<JnjContractEntryModel> contractEntryList = (List<JnjContractEntryModel>) jnjContractModel.getJnjContractEntries();
					if (CollectionUtils.isNotEmpty(contractEntryList)) {
						List<JnjContractEntryModel> jnjContractEntryModelList = contractProductMap.get(contractNumKey);
						if (CollectionUtils.isEmpty(jnjContractEntryModelList)) {
							jnjContractEntryModelList = new ArrayList<JnjContractEntryModel>();
						}
						for (JnjContractEntryModel jnjContractEntryModel : contractEntryList) {
							String productCd = jnjContractEntryModel.getProduct().getCode();
							contractProductList.add(productCd);
							jnjContractEntryModelList.add(jnjContractEntryModel);
						}
						contractProductMap.put(contractNumKey, jnjContractEntryModelList);
					}// end if contractEntryList
				} // for loop for all contract entry list
				
				//maintain all the contract and non contract map to identify mixed of product is ordering in the cart
				Set<String> contractSet = null;
				Set<String> nonContractSet = null;
				if(cartOrSelMap!= null)
				{
					contractSet = cartOrSelMap.get(key1);
	                if (CollectionUtils.isEmpty(contractSet)) {
	                    contractSet = new HashSet<String>();
	                }
	                nonContractSet = cartOrSelMap.get(key2);
	                if (CollectionUtils.isEmpty(nonContractSet)) {
	                    nonContractSet = new HashSet<String>();
	                }
				}
                
                if (CollectionUtils.isNotEmpty(contractProductList)) {
                   for (String contractProdCode : contractProductList) {
                       if(contractProdCode.equalsIgnoreCase(productCode)){
                           isContractCount = isContractCount+1; // if contract product it wil match
                           contractSet.add(productCode);
                           cartOrSelMap.put(key1, contractSet);
                           boolean isremoved =  nonContractSet.remove(productCode); // removing is already exists this product in the non contract list
                           LOGGER.info("isremoved from nonContractSet : "+isremoved);
                           break;
                       } else {
                           isNonContractCount = isNonContractCount+1; // if non contract product it wil match
                           nonContractSet.add(productCode);
                           cartOrSelMap.put(key2, nonContractSet);
                       }
                   }
                } // end contractProductList
			}

		}
		basePriceFlag = isContractCount>0?false:isNonContractCount>0?true:true;
		LOGGER.info("basePriceFlag : "+basePriceFlag + " isContractCount : " + isContractCount +" and isNonContractCount : "+ isNonContractCount); 
		sessionService.setAttribute("contractProductMap", contractProductMap);
		return basePriceFlag;
	}

	@Override
	public boolean updateContractNumInCartEntry(String productCode,String ContractNum) {
		
		boolean updateContractNumber = false;
		CartModel cartModel = getCartService().getSessionCart();
		if (CollectionUtils.isNotEmpty(cartModel.getEntries())) {
			List<AbstractOrderEntryModel> cartEntryModel = cartModel.getEntries();
			for (AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModel) {
				String productCodeInEntry = abstractOrderEntryModel.getProduct().getCode();
				if(productCode.equals(productCodeInEntry)) {
					abstractOrderEntryModel.setContractNum(ContractNum);
					 updateContractNumber = saveAbstOrderEntry(abstractOrderEntryModel);
				}
			}
		}
		return updateContractNumber;
	}
	
	/* (non-Javadoc)
	 * @see com.jnj.core.services.cart.JnjGTCartService#removeNonContractProduct()
	 */
	@Override
	public boolean removeNonContractProduct()	{
		boolean removeNonContractFlag = false;
		List<String> contractProductList = new ArrayList<String>();
		CartModel cartModel = getCartService().getSessionCart();
		if (CollectionUtils.isNotEmpty(cartModel.getEntries())) {
			List<AbstractOrderEntryModel> cartEntryModel = cartModel.getEntries();
			for (AbstractOrderEntryModel abstractOrderEntryModel : cartEntryModel) {
				String productCodeInEntry = abstractOrderEntryModel.getProduct().getCode();
				removeNonContractFlag = checkProductInContract(productCodeInEntry,contractProductList);
				if(removeNonContractFlag){
					modelService.remove(abstractOrderEntryModel);
				}
			}
		}
		return removeNonContractFlag;
	}

	//Need to check this condition later to review the code for this loop
	private boolean checkProductInContract(String productCode, List<String> contractProductList) {
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		LOGGER.info(currentB2BUnit + "B2BUnit" + baseSiteService.getCurrentBaseSite());
		boolean removeNonContractFlag = false;

		if (currentB2BUnit != null) {
			if (CollectionUtils.isEmpty(contractProductList)) {
				// checking this list is empty or not retain this list to check
				// further products instead of iterate everytime
				List<JnjContractModel> contractList = (List<JnjContractModel>) currentB2BUnit.getContracts();
				if (CollectionUtils.isNotEmpty(contractList)) {
					for (JnjContractModel jnjContractModel : contractList) {
						List<JnjContractEntryModel> contractEntryList = (List<JnjContractEntryModel>) jnjContractModel
								.getJnjContractEntries();
						if (CollectionUtils.isNotEmpty(contractEntryList)) {
							for (JnjContractEntryModel jnjContractEntryModel : contractEntryList) {
								String productCd = jnjContractEntryModel.getProduct().getCode();
								contractProductList.add(productCd);
							}
						}
					}
				}
			}

			if (CollectionUtils.isNotEmpty(contractProductList)) {
				if (contractProductList.contains(productCode)) {
					removeNonContractFlag = false;
					// no need to remove because this is contract product
				} else {
					removeNonContractFlag = true;
					LOGGER.info(
							"productCode : " + productCode + " and removeNonContractFlag : " + removeNonContractFlag);
					// need to remove the non contract product
				}
			}
		}
		return removeNonContractFlag;
	}
	
	
	@Override
	public boolean updateProposedAndOriginalItem(JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData) {
		
		final CartModel cartModel = cartService.getSessionCart();
		List<AbstractOrderEntryModel> abstractOrderEntryModels = cartModel.getEntries();
		for (Iterator iterator = abstractOrderEntryModels.iterator(); iterator.hasNext();) {
			AbstractOrderEntryModel abstractOrderEntryModel = (AbstractOrderEntryModel) iterator.next();
		 for (JnjGTCommonFormIOData proposedOutOrderItem : jnjGTProposedOrderResponseData.getJnjGTCommonFormIODataList()) {
			 //getting first element and list converted into String-AAOL-6378
			 if(abstractOrderEntryModel.getProduct().getCode().equalsIgnoreCase((String)proposedOutOrderItem.getProposedItemCd().get(0)))
			 {
				 LOGGER.info("getHybrisLineItemNo() : "+proposedOutOrderItem.getHybrisLineItemNo() + " and getOrderItemCd() : "+ proposedOutOrderItem.getOrderItemCd());
					final AbstractOrderEntryModel entryToUpdate = abstractOrderEntryModel;
					entryToUpdate.setOriginalOrderItem(proposedOutOrderItem.getOrderItemCd());
					entryToUpdate.setIsProposed(true);
					saveCartModel(cartModel, true);
			 }
		 	}
		}
		 return true;
	}
	
	@Override
	public List<String> getBatchNumbersForProduct() {
		
		List<String> batchNumbers=Jnjb2bCoreConstants.ConsignmentCharge.BATCH_NO;
		return batchNumbers;
	}
	
	@Override
	public List<String> getSerialNumbersForProduct() {
		
		List<String> serialNumbers=Jnjb2bCoreConstants.ConsignmentCharge.SERIAL_NO;
		return serialNumbers;
	}
	
	/**
	 * This method is used to get the catalog version.
	 * Added for return order attachments
	 * @return
	 */
	protected CatalogVersionModel getCatalogVersion()
	{
		CatalogVersionModel catalogVersion = catalogService.getDefaultCatalog() == null ? null : catalogService.getDefaultCatalog()
				.getActiveCatalogVersion();
		if (catalogVersion == null)
		{
			final Collection<CatalogVersionModel> catalogs = catalogVersionService.getSessionCatalogVersions();
			for (final CatalogVersionModel cvm : catalogs)
			{
				if (cvm.getCatalog() instanceof ContentCatalogModel)
				{
					catalogVersion = cvm;
					break;
				}
			}
		}

		return catalogVersion;
	}

		
	
}
