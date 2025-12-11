/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.cart;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jnj.core.data.JnjGTUpdatePriceData;
import com.jnj.core.dto.JnjGTSurgeryInfoData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjGTCreditCardModel;
import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.data.JnjGTProductData;
//import com.jnj.pcm.core.dto.SelectedAttributesForm;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;



/**
 * This represents the interface for JnjGTCartService, is used to perform various operations upon cart.
 * 
 * @author Accenture
 * @version 1.0
 */
public interface JnjGTCartService extends JnjCartService
{

	/**
	 * Update purchase order number.
	 * 
	 * @param purchaseOrderNumber
	 *           the string
	 * @return true, if successful
	 */
	public boolean updatePurchaseOrderNumber(final String purchaseOrderNumber);

	/**
	 * Update distributed purchase order number.
	 * 
	 * @param distributorPONumber
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateDistributorPONumber(final String distributorPONumber);

	/**
	 * Update attention.
	 * 
	 * @param attention
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateAttention(final String attention);

	/**
	 * Update specialText.
	 * 
	 * @param attention
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateSpecialText(final String specialText);
	
	
	/**
	 * Set default delivery date.
	 * 
	 * @param calculated
	 *           delivery date
	 * 
	 */
	public void setDefaultDeliveryDate(final Date defaultDeliveryDate);

	/**
	 * Update drop Ship Account.
	 * 
	 * @param dropShipAccount
	 *           the string
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	public AddressModel updateDropShipAccount(final String dropShipAccount) throws BusinessException;

	/**
	 * saves the Credit Card info.
	 * 
	 * @param ccPaymentInfoModel
	 *           the cc payment info model
	 */
	public void saveCreditCardInfo(JnjGTCreditCardModel ccPaymentInfoModel);

	/**
	 * Creates the cc payment info model.
	 * 
	 * @return the jnj na credit card model
	 */
	public JnjGTCreditCardModel createCCPaymentInfoModel();

	/**
	 * Update Lot/Comment at line item level.
	 * 
	 * @param entryNumber
	 *           the cart entry number
	 * @param newLotComment
	 *           the lot comment at cart entry number
	 * @return true, if successful
	 */
	public boolean updateLotNumberForEntry(final int entryNumber, final String newLotComment);

	/**
	 * Gets the shipping address by id.
	 * 
	 * @param shippingAddrId
	 *           the shipping addr id
	 * @return the shipping address by id
	 */
	public AddressModel getShippingAddressById(String shippingAddrId);

	/**
	 * Update payment info.
	 * 
	 * @param paymentInfoId
	 *           the payment info id
	 */
	public void updatePaymentInfo(String paymentInfoId);

	/**
	 * Change shipping address.
	 * 
	 * @param shippingAddrId
	 *           the shipping addr id
	 * @return the address model
	 */
	public AddressModel changeShippingAddress(String shippingAddrId);

	public AddressModel changeBillingAddress(String billingAddrId);
	/**
	 * This method is used to restore session cart for MDD and CONSUMER users accordingly.
	 * 
	 * @param srcSystemId
	 *           String source system Id of selected B2BUnitModel
	 */
	void restoreCartForCurrentUser(final String srcSystemId);

	/**
	 * Gets the all shipping methods.
	 * 
	 * @return the all shipping methods
	 */
	public List<JnjGTShippingMethodModel> getAllShippingMethods();


	/**
	 * Update shipping method.
	 * 
	 * @param route
	 *           the route
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	public boolean updateShippingMethod(String route, int entryNumber);

	/**
	 * Change order type.
	 * 
	 * @param orderType
	 *           JnjOrderTypesEnum code the order type
	 */
	public void changeOrderType(final String orderType);

	/**
	 * Gets the hidden fields.
	 * 
	 * @return Map key as the fields to be hide and css class used to hide
	 */
	public Map<String, String> getHiddenFields();

	/**
	 * Saves Surgeryinfo Model.
	 * 
	 * @param infoForm
	 *           the surgery info form
	 * @return true or false on basis of model saved or not.
	 */
	public boolean saveSurgeryInfo(JnjGTSurgeryInfoData infoForm);

	/**
	 * Gets the order type of session cart.
	 * 
	 * @return String orderType for session cart.
	 */
	public String getOrderType();


	/**
	 * Update reason code.
	 * 
	 * @param reasonCode
	 *           the reason code
	 * @return true, if successful
	 */
	public boolean updateReasonCode(final String reasonCode);

	/**
	 * Update lot num for entry.
	 * 
	 * @param lotNumber
	 *           the lot number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	//FIX GTR-1768
	public boolean updateLotNumForEntry(final String lotNumber, final String pcode, final int entryNumber);



	/**
	 * Update po num for entry.
	 * 
	 * @param poNumber
	 *           the po number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	public boolean updatePONumForEntry(final String poNumber, final int entryNumber);



	/**
	 * Update invoice num for entry.
	 * 
	 * @param invoiceNumber
	 *           the invoice number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	public boolean updateInvoiceNumForEntry(final String invoiceNumber, final int entryNumber);

	/**
	 * Gets the salesRepPrimanyUCNs.
	 * 
	 * @param francMajorGrpCode
	 *           String Major group code of product
	 * @param primarySalesRepOnly
	 *           ture in case only primary sales rep required
	 * @return Map key as the sales rep primary UCN and value as the B2B unit id
	 */
	public Set<String> getSalesRepUCN(final String francMajorGrpCode, boolean primarySalesRepOnly);

	/**
	 * OS-23 New Method to generate Spine specific UCN list for header. This method will return all the UCN associated
	 * with territories of current B2Bunit
	 * 
	 * @return the spine order header UCN
	 */
	public Set<String> getSpineOrderHeaderUCN();

	/**
	 * Update SalesRepUCN at order level.
	 * 
	 * @param salesRepUCN
	 *           the sales rep UCN
	 * @param specialStockPartner
	 *           the special stock partner that needs to be populated at entry level
	 * @return true, if successful
	 */
	public boolean updateSalesRepUCN(final String salesRepUCN, final String specialStockPartner);

	/**
	 * Update special stock partner at line item level.
	 * 
	 * @param specialStockPartner
	 *           the special stock partner at cart entry number
	 * @param entryNumber
	 *           the cart entry number
	 * @return true, if successful
	 */
	public boolean updateSpecialStockPartner(final String specialStockPartner, final int entryNumber);

	/**
	 * Update third party flag at order level.
	 * 
	 * @param thirdPartyFlag
	 *           the third party flag
	 * @return true, if successful
	 */
	public boolean updateThirdPartyFlag(final Boolean thirdPartyFlag);

	/**
	 * Update customer po number.
	 * 
	 * @param customerPO
	 *           the customer po
	 * @return true, if successful
	 */
	boolean updateCustomerPONumber(String customerPO);


	/**
	 * Update overriden price.
	 * 
	 * @param reasonCode
	 *           the reason code
	 * @param approver
	 *           the approver
	 * @param overridePrice
	 *           the override price
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	boolean updateOverridenPrice(String reasonCode, final String approver, final double overridePrice, final int entryNumber);

	/**
	 * Update cordis house account.
	 * 
	 * @param cordisHouseAccount
	 *           the cordis house account
	 * @return true, if successful
	 */
	public boolean updateCordisHouseAccount(String cordisHouseAccount);

	/**
	 * Update surgeon id.
	 * 
	 * @param surgeonId
	 *           the string
	 * @return true, if successful
	 */
	String updateSurgeon(final String surgeonId, final String selectSurgeonName, final String hospitalId);


	/**
	 * This method checks whether the user is eligible for Order Return or not.
	 * 
	 * @return boolean
	 */
	public boolean isEligibleForOrderReturn();

	/**
	 * Method to Determine Default Order Type For Use.
	 * 
	 * @return JnjOrderTypesEnum
	 */

	public JnjOrderTypesEnum getDefaultOrderType();


	/**
	 * Update freight cost.
	 * 
	 * @param freightCost
	 *           the freight cost
	 * @return true, if successful
	 */
	public boolean updateFreightCost(final double freightCost);

	/**
	 * Creates the media model for cart.
	 * 
	 * @param pdfFile
	 *           the pdf file
	 * @param name
	 * @throws BusinessException
	 *            the business exception
	 */
	public void createMediaModelForCart(final File pdfFile, final String name) throws BusinessException;
	
	/**
	 * AAOL-4937
	 * @param returnUploadFiles
	 * @return
	 */
	public boolean saveReturnMedia(List<MultipartFile> returnUploadFiles);
	
	/**
	 * Pre sap return cart validation.
	 * 
	 * @return true, if successful
	 */
	public boolean preSAPReturnCartValidation();

	/**
	 * Initiate replenish for delivered.
	 * 
	 * @param orderNum
	 * @throws BusinessException
	 *            the business exception
	 */
	public void initiateReplenishForDelivered(String orderNum) throws BusinessException;

	/**
	 * Clears the drop ship and distributor purchase order number.
	 */
	public void clearDropShipPurchaseOrderNum();

	/**
	 * Updates attributes selected by the user into cart model.
	 * 
	 * @param selectedAttributesForm
	 *           the selected attributes form
	 * @return true, if successful
	 */
	//public boolean updateCartModelWithSelectedAttributes(SelectedAttributesForm selectedAttributesForm);


	/**
	 * Gets the shipping method by route.
	 * 
	 * @param defaultRoute
	 *           the default route
	 * @return the shipping method by route
	 */
	public JnjGTShippingMethodModel getShippingMethodByRoute(String defaultRoute);


	/**
	 * Gets the faster shipping methods.
	 * 
	 * @param rank
	 *           the rank
	 * @param routeList
	 *           the route list
	 * @return the faster shipping methods
	 */
	List<JnjGTShippingMethodModel> getFasterShippingMethods(int rank, List<String> routeList);

	/**
	 * Gets the required shipping method.
	 * 
	 * @param requiredIds
	 *           the required ids
	 * @return the required shipping method
	 */
	List<JnjGTShippingMethodModel> getRequiredShippingMethod(List<String> requiredIds);

	/**
	 * Gets the jnj na early zip codes model by zip code.
	 * 
	 * @param JnjGTEarlyZipCodesModel
	 *           the jnj na early zip codes model
	 * @return the jnj na early zip codes model by zip code
	 */
	public List<JnjGTEarlyZipCodesModel> getJnjGTEarlyZipCodesModelByZipCode(JnjGTEarlyZipCodesModel JnjGTEarlyZipCodesModel);

	/**
	 * Update SAP price in the cart entry having given product code. Price will be set to SAP price in case of it fails
	 * price will be set from Hybris DB.
	 * 
	 * @param entryNumber
	 *           the entry number
	 * @return the jnj na update price data
	 */
	public JnjGTUpdatePriceData updatePriceForEntry(final int entryNumber);

	/**
	 * Gets the cart sub total.
	 * 
	 * @return the cart sub total
	 */
	public JnjGTUpdatePriceData getCartSubTotal();

	/**
	 * Gets the mandatory fields.
	 * 
	 */
	public Map<String, String> getMandatoryFields();

	public CommerceCartModification addToCartForPCM(final CartModel cartModel, final ProductModel productModel,
			final long quantityToAdd, final UnitModel unit, final boolean forceNewEntry) throws CommerceCartModificationException;

	/**
	 * Add multiple products to cart.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param products
	 *           Set of product models
	 * @return commerce cart modification
	 */
	public List<CommerceCartModification> addMultiProdsToCartPCM(final CartModel cartModel, final Set<ProductModel> products);
	
	/**
	 * Adds the to cart na.
	 * @param cartModel
	 * @param productQtyMap
	 * @return
	 */
	public CommerceCartModification addToCartGT(final CartModel cartModel, final Map<ProductModel, Long> productQtyMap);
	/**
	 * Adds the to cart for Consignment charge.
	 * @param cartModel
	 * @param productQtyMap
	 * @param forceNewEntry
	 * @param currentEntryNumber
	 * @return
	 */
	 
	public CommerceCartModification addToCartGT(final CartModel cartModel, final Map<ProductModel, Long> productQtyMap, final boolean forceNewEntry, int currentEntryNumber);

	/**
	 * Creates the cart from order.
	 * 
	 * @param orderId
	 *           the order id
	 */
	public void createCartFromOrder(final String orderId, JnjGTProductData productData);
	
	/**
	 * Fetch indirect customers.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return the list
	 */
	List<JnjIndirectCustomerModel> fetchIndirectCustomers(String indirectCustomer, CountryModel countryModel);
	
	/**
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code and country
	 * model.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return the jnj indirect customer model
	 */
	public abstract JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer, CountryModel countryModel);
	
	/**
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code and country
	 * model.
	 * 
	 * @param indirectCustomer
	 *           the indirect customer
	 * @return the jnj indirect customer model
	 */
	public abstract JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer);
	
	/**
	 * @param productCode
	 * @param basePriceFlag
	 * @param contractProductMap
	 * @param cartOrSelMap 
	 * @return
	 */
	public boolean checkProductInContract(String productCode,boolean basePriceFlag,Map<String, List<JnjContractEntryModel>> contractProductMap,
			Map<String, Set<String>> cartOrSelMap);
	
	/**
	 * @param productCode
	 * @param contractNum 
	 * @return
	 */
	public boolean updateContractNumInCartEntry(String productCode,String contractNum); 
	/** This method is used to remove the non contract product from the cart which is ordered previously
	 * @return booelan
	 */
	public boolean removeNonContractProduct();
	
	/**
	 * This method is used to update proposed flag and original product item in the current product level to display in the line level
	 * @param jnjGTProposedOrderResponseData
	 * @return boolean
	 */
	public boolean updateProposedAndOriginalItem(JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData);

	boolean updateBatchDetailsForEntry(int entryNumber, String batchNumber, String serialNumber);

	public List<String> getBatchNumbersForProduct();

	public List<String> getSerialNumbersForProduct();
}