/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.cart;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import jakarta.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLStreamException;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.data.JnjGTGetPriceQuoteResponseData;
import com.jnj.core.data.JnjGTOrderReturnResponseData;
import com.jnj.core.data.JnjGTUpdatePriceData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.dto.JnjGTSurgeryInfoData;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjGTConsInventoryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTShippingMethodData;
import com.jnj.facades.data.JnjGTProposedOrderResponseData;
//import com.jnj.pcm.core.dto.SelectedAttributesForm;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * The Interface JnjGTCartFacade.
 * 
 * @author Accenture
 * @version 1.0
 */

public interface JnjGTCartFacade extends JnjCartFacade
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
	 * @param specialText
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateSpecialText(final String attention);

	/**
	 * Update drop Ship Account. This method found the Address based on the Account id. If Drop Ship Address is not
	 * available for the given Account Id it throws Exception Otherwise fetch the address update it in the cart model and
	 * return it after converting in AddressData.
	 * 
	 * @param dropShipAccount
	 *           the string
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 */
	public AddressData updateDropShipAccount(final String dropShipAccount) throws BusinessException;


	/**
	 * saves the Credit card info.
	 * 
	 * @param ccPaymentInfoData
	 *           the cc payment info data
	 * @param remember
	 *           the remember
	 */
	public void saveCreditCardInfo(CCPaymentInfoData ccPaymentInfoData, boolean remember);

	/**
	 * set default delivery date.
	 * 
	 * @param calculatedDeliveryDate
	 * 
	 */
	public void setDefaultDeliveryDate(Date defaulDeliveryDate);

	/**
	 * fetch the list of Credit Card Info of current user.
	 * 
	 * @return the all credit cards info
	 */
	public Collection<CCPaymentInfoData> getAllCreditCardsInfo();

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
	 * fetch the list of hidden fields for the current order type and account type.
	 * 
	 * @return map containing key as field name and value as hide
	 */
	Map<String, String> getRestrictedFieldsForUser();

	/**
	 * Gets the shipping address by id.
	 * 
	 * @param shippingAddrId
	 *           the shipping addr id
	 * @return the shipping address by id
	 */
	AddressData getShippingAddressById(String shippingAddrId);

	/**
	 * Update payment info.
	 * 
	 * @param paymentInfoNum
	 *           the payment info num
	 * @return true, if successful
	 */
	boolean updatePaymentInfo(String paymentInfoNum);

	/**
	 * Chnage shipping address.
	 * 
	 * @param shippingAddrId
	 *           the shipping addr id
	 * @return the address data
	 */
	AddressData chnageShippingAddress(String shippingAddrId,final HttpServletRequest request);
	
	/**
	 * Chnage billing address.
	 * 
	 * @param billingAddrId
	 *           the billing addr id
	 * @return the address data
	 */
	AddressData changeBillingAddress(String billingAddrId,final HttpServletRequest request);

	/**
	 * Validates the Product number for: division compatibility, .
	 * 
	 * @param productModel
	 *           the ProductModel
	 * @return String
	 *         <ul>
	 *         <li>null : Product code is valid i.e. eligible to add to the cart</li>
	 *         <li>error message to be displayed :Product code is not valid i.e. not eligible to add to the cart</li>
	 *         </ul>
	 */
	String validateProductCode(final ProductModel productModel);

	/**
	 * This method is used to restore session cart for MDD and CONSUMER users accordingly.
	 * 
	 * @param srcSystemId
	 *           String source system Id of selected B2BUnitModel
	 */
	void restoreCartForCurrentUser(final String srcSystemId);

	/**
	 * Gets the shipping route and name map, where route will be the key and value will the name to be displayed.
	 * 
	 * @return the shipping route and name map
	 */
	Map<String, String> getShippingRouteAndNameMap();

	/**
	 * This method is used to update route for given entry Number.
	 * 
	 * @param route
	 *           String new route value
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	boolean updateShippingMethod(String route, int entryNumber);

	/**
	 * Change order type.
	 * 
	 * @param orderType
	 *           JnjOrderTypesEnum code the order type
	 */
	void changeOrderType(final String orderType);

	/**
	 * save Surgery Information.
	 * 
	 * @param infoForm
	 *           JnjGTSurgeryInfoForm Form information
	 * @return true, if successful
	 */
	boolean saveSurgeryInfo(JnjGTSurgeryInfoData infoForm);

	/**
	 * get Surgery Values.
	 * 
	 * @param id
	 *           String id
	 * @param key
	 *           String key
	 * @return the surgery values
	 */
	List<String> getSurgeryValues(String id, String key);

	/**
	 * Gets the order type of session cart.
	 * 
	 * @return String orderType for session cart.
	 */
	String getOrderType();




	/**
	 * Sets the shipping address.
	 * 
	 * @param address
	 *           the new shipping address
	 */
	void setShippingAddress(final AddressData address);

	/**
	 * Sets the billing address.
	 * 
	 * @param address
	 *           the new billing address
	 */
	void setBillingAddress(final AddressData address);


	/**
	 * Set the customShippingAddress value to true.
	 * 
	 * @param boolean
	 * 
	 */
	void setCustomShippingAddress(final boolean customShippingAddress);

	/**
	 * Remove one time ship to address.
	 * 
	 */
	void removeOneTimeShippingAddress();

	/**
	 * Gets the path for view.
	 * 
	 * @param page
	 *           the page
	 * @param orderType
	 *           the order type
	 * @return the path for view
	 */
	String getPathForView(final String page, String orderType);

	/**
	 * Gets the reason code.
	 * 
	 * @param reasonCode
	 *           the reason code
	 * @return the reason code
	 */
	Map<String, String> getReasonCode(String reasonCode);

	/**
	 * Update reason code.
	 * 
	 * @param reasonCode
	 *           the reason code
	 * @return true, if successful
	 */
	boolean updateReasonCode(String reasonCode);

	/**
	 * Update po number for order entry.
	 * 
	 * @param poNumber
	 *           the po number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	public boolean updatePONumberForOrderEntry(final String poNumber, final int entryNumber);

	/**
	 * Update lot number for order entry.
	 * 
	 * @param lotNumber
	 *           the lot number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	//FIX GTR-1768
	public boolean updateLotNumberForOrderEntry(final String lotNumber, final String pcode, final int entryNumber);

	/**
	 * Update invoice number for order entry.
	 * 
	 * @param invoiceNumber
	 *           the invoice number
	 * @param entryNumber
	 *           the entry number
	 * @return true, if successful
	 */
	boolean updateInvoiceNumberForOrderEntry(final String invoiceNumber, final int entryNumber);

	/**
	 * Gets the salesRepPrimanyUCNs.
	 * 
	 * @return Map key as the sales rep primary UCN and value as the B2B unit id
	 */
	Set<String> getSalesRepPrimaryUCN();

	Set<String> getSpineOrderHeaderUCN();

	/**
	 * Update SalesRepUCN at order level.
	 * 
	 * @param salesRepUCN
	 *           the sales rep UCN
	 * @param specialStockPartner
	 *           the special stock partner that needs to be populated at entry level
	 * @return true, if successful
	 */
	boolean updateSalesRepUCN(String salesRepUCN, String specialStockPartner);

	/**
	 * Update special stock partner at line item level.
	 * 
	 * @param specialStockPartner
	 *           the special stock partner at cart entry number
	 * @param entryNumber
	 *           the cart entry number
	 * @return true, if successful
	 */
	boolean updateSpecialStockPartner(final String specialStockPartner, final int entryNumber);

	/**
	 * Update third party flag at order level.
	 * 
	 * @param thirdPartyFlag
	 *           the third party flag
	 * @return true, if successful
	 */
	boolean updateThirdPartyFlag(Boolean thirdPartyFlag);

	/**
	 * Fetches the division data POJO which contains flags to indicate whether current user has mitek, codman and spine
	 * as division or not.
	 * 
	 * @return JnjGTDivisonData
	 */
	JnjGTDivisonData getPopulatedDivisionData();

	/**
	 * Update customer po number.
	 * 
	 * @param customerPO
	 *           the customer po
	 * @return true, if successful
	 */
	boolean updateCustomerPONumber(final String customerPO);

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
	 * Gets the returun reason codes.
	 * 
	 * @param reasonCodeReturns
	 *           the reason code returns
	 * @return the returun reason codes
	 */
	public Object getReturunReasonCodes(String reasonCodeReturns);


	/**
	 * Update freight cost.
	 * 
	 * @param freightCost
	 *           the freight cost
	 * @return true, if successful
	 */
	public boolean updateFreightCost(final double freightCost);

	/**
	 * Save pay metrics response.
	 * 
	 * @param request
	 *           the request
	 * @param isRememberSet
	 *           the is remember set
	 * @return the string
	 * @throws CMSItemNotFoundException
	 *            the cMS item not found exception
	 * @throws IOException
	 *            Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *            the no such algorithm exception
	 * @throws InvalidKeyException
	 *            the invalid key exception
	 * @throws XMLStreamException
	 *            the xML stream exception
	 */
	public String savePayMetricsResponse(final HttpServletRequest request, boolean isRememberSet) throws CMSItemNotFoundException,
			IOException, NoSuchAlgorithmException, InvalidKeyException, XMLStreamException;

	/**
	 * Creates the media model for cart.
	 * 
	 * @param pdfFile
	 *           the pdf file
	 * @param name
	 *           the name
	 * @throws BusinessException
	 *            the business exception
	 */
	public void createMediaModelForCart(final File pdfFile, final String name) throws BusinessException;
	
	/*Added for AAOL-4937*/	
	/**
	 * Creates the media model for cart for Return Order.
	 * 
	 * @param pdfFile
	 *           the pdf file
	 * @param name
	 *           the name
	 * @throws BusinessException
	 *            the business exception
	 */
	public boolean saveReturnMedia(List<MultipartFile> returnUploadFiles);
	
	
	/**
	 * Pre validate cart for return order.
	 * 
	 * @return true, if successful
	 */
	public boolean preValidateCartForReturnOrder();

	/**
	 * Checks id the division is same for all the products in Cart.
	 * 
	 * @return true, if successful
	 */
	public boolean validateProductDivision();

	/**
	 * Cart order confirmation email.
	 * 
	 * @return true, if successful
	 */
	boolean cartOrderConfirmationEmail();

	/**
	 * Return order sap validation.
	 * 
	 * @return the jnj na order return response data
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 */
	public JnjGTOrderReturnResponseData returnOrderSAPValidation() throws SystemException, IntegrationException, BusinessException;

	/**
	 * Quote sap validation.
	 * 
	 * @return the jnj na get price quote response data
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public JnjGTGetPriceQuoteResponseData quoteSAPValidation() throws SystemException, IntegrationException, ParseException,
			BusinessException;


	/**
	 * Initiate replenish for delivered.
	 * 
	 * @param orderNum
	 *           the order num
	 * @throws BusinessException
	 */
	public void initiateReplenishForDelivered(String orderNum) throws BusinessException;

	/**
	 * Clears the drop ship and distributor purchase order number.
	 */
	public void clearDropShipPurchaseOrderNum();

	/**
	 * Update cart model with attributes selected by user on PCM Export Configuration Page.
	 * 
	 * @param selectedAttributesForm
	 *           the selected attributes form
	 * @return true, if successful
	 */
	//public boolean updateCartModelWithSelectedAttributes(SelectedAttributesForm selectedAttributesForm);

	/**
	 * Update named delivery date.
	 * 
	 * @param expDeliveryDate
	 *           the exp delivery date
	 * @return true, if successful
	 */
	public boolean updateNamedDeliveryDate(final String expDeliveryDate);

	/**
	 * Gets the saved surgery info.
	 * 
	 * @return the saved surgery info
	 */
	SurgeryInfoData getSavedSurgeryInfo();

	/**
	 * Generates download cart link.
	 * 
	 * @param orderCode
	 *           the order-code
	 * 
	 * @return amazon download url
	 */
	//public String downloadCartInRealTime(String orderCode);

	/**
	 * @return
	 */
	public Set<JnjGTShippingMethodData> getAllShippingMethods();

	/**
	 * @param cartData
	 * @return
	 */
	public JnjGTCartData setShippingMethodOnOrderType(JnjGTCartData cartData);

	/**
	 * This method is used to set the default shipping address as per Current B2bUnit
	 * 
	 * @return AddressData AddressData data object of the assigned address to session cart
	 */
	public AddressData resetShippingAddress();

	/**
	 * Update price for entry.
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
	 * fetch the list of mandatory fields for the current order type and account type.
	 * 
	 * @return map containing key as field name and value as required
	 */
	Map<String, String> getMandatoryFieldsForUser();

	/**
	 * Creates cart from the Order based on its Id, returns product data of the last product/line available in the order.
	 */
	ProductData createCartWithOrder(final String orderId);

	/**
	 * Returns Product-Quantity Map after processing uploaded file
	 */
	public Map<String, String> fileConverter(final MultipartFile homepagefile);

	/**
	 * Add multiple products to cart For PCM
	 * 
	 * @param productCodes
	 *           set of product codes to be added
	 * @param siteName
	 *           YTODO
	 * @return cart modification data
	 */
	JnjCartModificationData addToCartPCM(final Map<String, String> productCodes, boolean ignoreUPC, String siteName,
			boolean isHomePageFileUpload);
	
	
	/**
	 * Add multiple products to cart For NA
	 * @param productCodes
	 * @param ignoreUPC
	 * @param isHomePageFileUpload
	 * @return
	 */
	public JnjCartModificationData addToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC,
			boolean isHomePageFileUpload);
	/**
	 * Add multiple products to cart For Consignment Charge
	 * 
	 * @param productCodes
	 *           set of product codes to be added
	 * @param currentEntryNumber 
	 * @return cart modification data
	 */
	public JnjCartModificationData addToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC,
			boolean isHomePageFileUpload, boolean forceNewEntry, int currentEntryNumber);

	/**
	 * Calls OOTB method to Remove Cart from Session
	 */
	public void removeSessionCart();
	
	public void cleanSavedCart();

	/**
	 * This method is used to check if user can do the sap validation with session cart. This validation is performed
	 * according to value define in config key "order.validation.lines.count"
	 * 
	 * @return false in case cart size is greater then configured limit.
	 */
	public boolean canValidateCart();

	/**
	 * Multi add to cart na.
	 * 
	 * @param productCodes
	 *           the product codes
	 * @param ignoreUPC
	 *           the ignore upc
	 * @return the cart modification data
	 */
	public JnjCartModificationData multiAddToCartGT(final Map<String, String> productCodes, final boolean ignoreUPC);

	/**
	 * Validate order.
	 * 
	 * @param wsData
	 *           the ws data
	 * @return the jnj validate order data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 *            the business exception
	 */
	public JnjValidateOrderData validateOrder(JnjGTSapWsData wsData) throws IntegrationException, SystemException,
			BusinessException,TimeoutException;

	/**
	 * Adds the quote to cart.
	 * 
	 * @return true, if successful
	 */
	public boolean addQuoteToCart();

	public void createCartFromOrder(final String orderId, final JnjGTProductData productData);

	/**
	 * Returns number of entries in cart-model.
	 */
	public int getNumberOfCartEntriesInSessionCart();
	
	/*Added for JJEPIC 720 Start*/
		
	public boolean deleteMediaModelFromCart();
	/*Added for JJEPIC 720 Ends */
	
	/**
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code and country
	 *
	 * model.
	 *
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param countryModel
	 *           the country model
	 * @return the jnj indirect customer model
	 */
	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer, CountryModel countryModel);		
	
	/**
	 * This method is used to fetch the indirect customer name on the basis of given indirectCustomer code and country
	 *
	 * model.
	 *
	 * @param indirectCustomer
	 *           the indirect customer
	 * @return the jnj indirect customer model
	 */
	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer);
	/**
	 * Update named delivery date.
	 * 
	 * @param expShippingDate
	 *           the exp Shipping date
	 * @return true, if successful
	 */
	public boolean updateNamedShippingDate(final String expShippingDate, Integer entryNumber);
	
	
	/**
	 * This method is used to Calucate Cart total for Split cart
	 * @param splitCartData
	 */
	public void calculateAllTotals(JnjGTCartData splitCartData);
	
	/**
	 * This method returns Out Order Line for Split cart.
	 * @param productCode
	 * @param outOrderLinesList
	 * @return
	 */
	public OutOrderLines3 getOutOrderLinesResult(String productCode, List<OutOrderLines3> outOrderLinesList);
	
	/**
	 * This method check product is available in contracts of b2b unit
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


	public boolean updateShipToAddIdMap(String shippingAddressId,boolean checBoxStatus,final HttpServletRequest request);
	public boolean updateBillToAddIdMap(String billingAddressId,boolean checBoxStatus,final HttpServletRequest request);
	public AddressData getDefaultDisplayShippingAdd(List<AddressData> listOfAvailableAddress,final Model model,final HttpServletRequest request);
	public AddressData getDefaultDisplayBillingAdd(List<AddressData> listOfAvailableAddress,final Model model,final HttpServletRequest request);
	/**
	 * This method is used to display correct Quantity of Product in case of Update Or Validated Order.
	 * @param entry
	 * @param quantity
	 * @return
	 */
	public Long updateQuantity(OrderEntryData entry, Long quantity);

	/**
	 * @param selectedProductCatalogIds 
	 * @return JnjContractFormData
	 */
	public JnjContractFormData validateIsNonContract(String[] selectedProductCatalogIds,String contractNum);
	
	
	/**
	 * @param catalogVersionModel 
	 * @return JnjGTProposedOrderResponseData
	 */
	public JnjGTProposedOrderResponseData simulateOrderFirstSAPCall(final JnjGTSapWsData wsData);
	/**
	 * This method is used to display proposed item in the popup.
	 * @param entry
	 * @param quantity
	 * @return
	 */
	public JnjGTProposedOrderResponseData checkReplacemenItemForProduct(String productId, final JnjGTSapWsData wsData);

	public boolean updateCartModelReturn(JnjGTCartData cart);
	
	/**
	 * This method is used to update proposed flag and original product item in the current product level to display in the line level
	 * @param jnjGTProposedOrderResponseData
	 * @return boolean
	 */
	public boolean updateProposedAndOriginalItem(JnjGTProposedOrderResponseData jnjGTProposedOrderResponseData);

	boolean updateBatchDetailsForEntry(int entryNumber, String batchNumber, String serialNumber);

	public List<String> getBatchNumbersForProduct();

	public List<String> getSerialNumbersForProduct();
	
	public List<JnjGTConsInventoryData> fetchBatchDetails() throws IntegrationException, SystemException;
	
	//AAOL-5672  
	
	public void modifyQuantityForCopyCharge(String prodQty, String currentLineEntryNumber);

}