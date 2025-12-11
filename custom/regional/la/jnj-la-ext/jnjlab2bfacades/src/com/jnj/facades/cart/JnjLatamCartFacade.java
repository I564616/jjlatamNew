/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.facades.cart;

import com.jnj.la.core.data.ReplacementProductData;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjCartModificationData;
import com.jnj.facades.data.JnjContractFormData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.la.core.model.JnjIndirectPayerModel;

import jakarta.servlet.http.HttpServletRequest;


/**
 *
 */
public interface JnjLatamCartFacade extends CartFacade
{


	/**
	 * This method is used for updating the Indirect Customer.
	 *
	 * @param indirectCustomer
	 *           the indirect customer
	 * @param indirectCustName
	 * @param cartEntryId
	 *           the cart entry id
	 * @return true, if successful
	 */
	public boolean updateIndirectCustomer(String indirectCustomer, String indirectCustName, int cartEntryId);


	/**
	 * The placeOrderInSap method passes the orderModel object to JnjOrderFacadeImpl class so that SAP order details is
	 * updated in hybris data base.
	 *
	 * @param orderCode
	 *           the order code
	 * @return JnjOrderData the jnjorderdata
	 * @throws SystemException
	 *            the system exception
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public JnjOrderData placeOrderInSap(final String orderCode) throws SystemException, IntegrationException, ParseException;


	/**
	 * The validate order method is used to invoke the JnjOrderFacadeImpl class so that SAP order service validate the
	 * current cart.
	 *
	 * @return true, if successful
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws TimeoutException
	 *            the timeout exception
	 */

	public JnjValidateOrderData validateOrder(JnjGTSapWsData wsData)
			throws IntegrationException, SystemException, TimeoutException, BusinessException;

	/**
	 * Method to get the Cart Model. Added as part of Panama Enhancement
	 *
	 */
	public CartModel getCartModel();

	/**
	 * Method to save the Cart Model. Added as part of Panama Enhancement
	 *
	 */
	public void saveCart(final CartModel cartModel);


	/**
	 * @param entry
	 * @param quantity
	 * @return
	 */
	public Long updateQuantity(OrderEntryData entry, Long quantity);

	JnjCartModificationData addToCartQuick(String productCode, String productQuantity,
			List<CartModificationData> cartModificationList) throws CommerceCartModificationException;

	public JnjCartModificationData addToCartFromContract(final String productCode, final String productQuantity)
			throws CommerceCartModificationException;

	List<String> getIndirectCustomer(final String country);

	List<String> getIndirectCustomer(final String siteDefaultCountry, String term);

	List<String> getIndirectPayer(String country);

	List<String> getIndirectPayer(String siteDefaultCountry, String term);

	List<JnjIndirectPayerModel> getIndirectPayers(String country);

	List<JnjIndirectCustomerModel> getIndirectCustomers(String country);

	String getIndirectCustomerName(String country, String indirectCustomer);

	String getIndirectPayerName(String country, String indirectPayer);

	public boolean updateIndirectPayer(final String indirectPayer, final String indirectPayerName, final int cartEntryNumber);

	ProductData createCartWithOrder(final String orderId);

	public void createCartFromOrder(final String orderId, final JnjGTProductData productData);

	public void saveCartWithContractNo(String contractNo);

	public void saveCartTypeBasedOnContractNo(final String contractNo);
	public JnjCartModificationData addToCartLatam(final Map<String, String> productCodes, final boolean ignoreUPC,
			final boolean isHomePageFileUpload, boolean forceNewEntry, int currentEntryNumber) throws CommerceCartModificationException;


	public void saveCartRemoveContractNo();

	public boolean isContractProduct(String catalogId);

	public boolean isContractProduct(String catalogId, String currentContractNumber);

	public Map<String, String> fileConverter(MultipartFile homepagefile, boolean indirectCustomer, boolean indirectPayer);

	public boolean updateIndirectPayer(String indirectPayer, String productCode);

	public boolean updateIndirectCustomer(String indirectCustomer, String productCode);

	public boolean updateNamedExpectedDeliveryDate(String expShippingDate, Integer entryNumber, String language);

	public String getLanguageSpecificDatePattern(LanguageData sessionLanguage);

	public String getCurrentUserLanguage();

	public JnjCartModificationData addMultipleProds(final Map<String, String> productCodes, final Model model,
			final boolean isHomePageFileUpload, final boolean forceNewEntry, int currentEntryNumber);

	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer);

	public JnjIndirectCustomerModel fetchIndirectCustomerName(String indirectCustomer, CountryModel countryModel);

	public List<JnjIndirectCustomerModel> fetchIndirectCustomers(String indirectCustomer, CountryModel countryModel);

	public JnjContractFormData validateIsNonContract(final String[] selectedProductCatalogIds, final String contractNum);

	String getPathForView(final String page, String orderType);

	public Map<String, String> getShippingDetails();

	/**
	 * This method is used to remove the non contract product from the cart which is ordered previously
	 *
	 * @param contract
	 * @return boolean
	 */
	public boolean removeNonContractProduct(String contract);

	void updateShippingAddress(JnJB2BUnitModel currentB2BUnit);
	
	/**
	 * This method is used to get the substitute product eligibility.
	 * @return will return the susbtitute flag.
	 */
	public boolean displaySubstitutes();

    /**
     * This method is used to update the substitute products in cart entries.
     * @param cartModel will pass as parameter.
     * @param replacementProductsData data os the replacement product.
     */
	public void replaceProducts(final CartModel cartModel, final List<ReplacementProductData> replacementProductsData);

	/**
	 * Chnage shipping address.
	 *
	 * @param shippingAddrId
	 *           the shipping addr id
	 * @return the address data
	 */
	AddressData chnageShippingAddress(final String shippingAddrId,final HttpServletRequest request);

	/**
	 * Update complementary info.
	 *
	 * @param complementaryInfo
	 *           the string
	 * @return true, if successful
	 */
	public boolean updateComplementaryInfo(final String complementaryInfo);
	
	/**
	 * Set customer freight type.
	 * @param customerFreightType
	 * @return
	 */
	public void setCustomerFreightType(final String customerFreightType);
}
