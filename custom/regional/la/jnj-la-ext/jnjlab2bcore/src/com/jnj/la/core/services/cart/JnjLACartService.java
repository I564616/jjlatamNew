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
package com.jnj.la.core.services.cart;

import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnjContractModel;
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.cart.JnjGTCartService;
import com.jnj.facades.data.JnjLaOrderEntryData;


/**
 *
 */
public interface JnjLACartService extends JnjGTCartService
{
	public CommerceCartModification addToCart(CartModel cartmodel, ProductModel productmodel, long quantityToAdd,
			UnitModel unitmodel, boolean flag, boolean calculateFlag, String catalogId, JnjLaOrderEntryData entryData)
			throws CommerceCartModificationException;

	public boolean addIndirectPayer(String indirectPayer, String indirectPayerName, int cartEntryNumber);

	public boolean addIndirectCustomer(String indirectCustomer, String indirectCustName, int cartEntryNumber);

	public boolean addIndirectCustomer(String indirectCustomer, String indirectCustomerName, String productCode);

	public boolean addIndirectPayer(String indirectPayer, String indirectPayerName, String productCode);

	boolean updateContractIdInCart(String empty);

	public ProductModel getActiveProduct(final JnJProductModel productModel, final String contractNumber)
			throws CommerceCartModificationException;

	public boolean isProductAllignedToRestrictedCategory(final ProductModel product);

	public boolean removeNonContractProduct(String contractNum);

	/**
	 * This method returns order type for commercial user
	 *
	 * @param jnjContractModel
	 * @param cartModel
	 * @return JnjOrderTypesEnum
	 */
	public JnjOrderTypesEnum calculateOrderTypeForCommercialUser(final JnjContractModel jnjContractModel, final CartModel cartModel);

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
