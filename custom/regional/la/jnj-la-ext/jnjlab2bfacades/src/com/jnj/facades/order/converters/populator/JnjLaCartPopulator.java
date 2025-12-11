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
package com.jnj.facades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.JnjModelService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaProductModel;


/**
 *
 */
public class JnjLaCartPopulator extends JnjGTCartPopulator
{
	@Autowired
	private CartService cartService;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	protected JnjModelService jnjModelService;

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}



	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected static final Logger LOG = Logger.getLogger(JnjLaCartPopulator.class);

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		final String methodName = "populate()";
		if (null != source)
		{
			checkDeliveryAddress(source);
			super.populate(source, target);
			if (target instanceof JnjLaCartData)
			{
				final JnjLaCartData jnjLaCartData = (JnjLaCartData) target;
				jnjLaCartData.setContractId(source.getContractNumber());
				final List<AbstractOrderEntryModel> modelEntries = source.getEntries();
				final List<OrderEntryData> dataEntries = jnjLaCartData.getEntries();
				final List<JnjContractEntryData> contractEntryList = sessionService.getAttribute("contractEntryList");
				if(StringUtils.isNotEmpty(source.getComplementaryInfo())){
					jnjLaCartData.setComplementaryInfo(source.getComplementaryInfo());
				}

				if (source.getEntries() != null && source.getSubtotal() == 0 && source.getEntries().size() == 0)
				{
					source.setContractNumber(null);
					source.setIsContractCart(false);
				}

				if (source.getIsContractCart() != null)
				{
					((JnjLaCartData) target).setIsContractCart(source.getIsContractCart());
				}
				JnjGTCoreUtil.logDebugMessage("Cart Population", methodName,
						"JnjLaCartData.isIsContractCart() :: " + ((JnjLaCartData) target).isIsContractCart(), JnjLaCartPopulator.class);

				JnjGTCoreUtil.logDebugMessage("Cart Population", methodName, "source.getSubtotal() :: " + source.getSubtotal(),
						JnjLaCartPopulator.class);
				((JnjLaCartData) target).setSubTotal(createPrice(source, source.getSubtotal()));
				JnjGTCoreUtil.logDebugMessage("Cart Population", methodName,
						"JnjLaCartData.getSubTotal() :: " + ((JnjLaCartData) target).getSubTotal(), JnjLaCartPopulator.class);
				
				((JnjLaCartData) target).setTotalTax(createPrice(source, source.getTotalTax()));
				
				if (null != source.getTotalGrossPrice())
				{
					((JnjLaCartData) target).setTotalGrossPrice(createPrice(source, source.getTotalGrossPrice()));
				}
				else
				{
					((JnjLaCartData) target).setTotalGrossPrice(createPrice(source, source.getTotalPrice()));
				}
				if (null != source.getUnit())
				{
					((JnjLaCartData) target).setPartialDelivFlag(((JnJLaB2BUnitModel) source.getUnit()).getPartialDelivFlag() == null
							? Boolean.TRUE : ((JnJLaB2BUnitModel) source.getUnit()).getPartialDelivFlag());
				}
				else
				{
					((JnjLaCartData) target).setPartialDelivFlag(Boolean.TRUE);
				}
				((JnjLaCartData) target)
						.setHoldCreditCardFlag(source.getHoldCreditCardFlag() == null ? Boolean.FALSE : source.getHoldCreditCardFlag());

				for (int i = 0; i < dataEntries.size(); i++)
				{
					if (dataEntries.get(i) instanceof JnjLaOrderEntryData)
					{
						final JnjLaOrderEntryData laDataEntry = (JnjLaOrderEntryData) dataEntries.get(i);
						laDataEntry.setIndirectCustomer(modelEntries.get(i).getIndirectCustomer());
						laDataEntry.setIndirectCustomerName(modelEntries.get(i).getIndirectCustomerName());
						laDataEntry.setIndirectPayer(modelEntries.get(i).getIndirectPayer());
						laDataEntry.setIndirectPayerName(modelEntries.get(i).getIndirectPayerName());

						final ProductModel product = modelEntries.get(i).getProduct();

						if (product instanceof JnJLaProductModel)
						{
							final JnJLaProductModel laProduct = (JnJLaProductModel) product;
							laDataEntry.setCatalogId(laProduct.getCatalogId());
						}
						else
						{
							laDataEntry.setCatalogId(product.getCode());
						}

						if (null != contractEntryList)
						{
							for (final JnjContractEntryData entry : contractEntryList)
							{
								if (null != entry.getProduct() && entry.getProduct() instanceof JnjLaProductData)
								{
									final JnjLaProductData laContractProductData = (JnjLaProductData) entry.getProduct();
									if (laContractProductData.getCatalogId().equals(laDataEntry.getCatalogId())
											&& null != entry.getContractProductPrice())
									{
										final PriceData price = new PriceData();
										price.setFormattedValue(entry.getContractProductPrice());
										laDataEntry.setBasePrice(price);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void checkDeliveryAddress(final CartModel source)
	{
		if (source.getDeliveryAddress() == null)
		{
			final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
			source.setDeliveryAddress(jnjGTB2BUnitService.getShippingAddress(currentB2BUnit));
			jnjModelService.save(source);
		}
	}
}
