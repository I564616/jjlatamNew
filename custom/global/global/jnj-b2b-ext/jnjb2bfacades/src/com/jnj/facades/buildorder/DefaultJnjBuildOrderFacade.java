package com.jnj.facades.buildorder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;

import jakarta.annotation.Resource;
import org.apache.log4j.Logger;

import com.jnj.facades.data.JnjBuildOrderData;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

public class DefaultJnjBuildOrderFacade implements JnjBuildOrderFacade{
	
	
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private PriceService priceService;
	
	@Resource(name = "productService")
	private ProductService productService;
	
	@Resource(name = "productFacade")
	private ProductFacade productFacade;


	
	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService() {
		return b2bUnitService;
	}


	public void setB2bUnitService(B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService) {
		this.b2bUnitService = b2bUnitService;
	}


	public PriceService getPriceService() {
		return priceService;
	}


	public void setPriceService(PriceService priceService) {
		this.priceService = priceService;
	}
	
	/**
	 * @return the productService
	 */
	public ProductService getProductService() {
		return productService;
	}


	/**
	 * @param productService the productService to set
	 */
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}


	public JnjBuildOrderData getBuildOrderFromSession(JnjBuildOrderData buildOrder)
	{
		if (buildOrder == null)
		{
			buildOrder = new JnjBuildOrderData();
			buildOrder.setLineItems(new LinkedHashMap<String,OrderEntryData>());
		}
		return buildOrder;
	}
	

	public boolean productExistsInList(final String productCode, final JnjBuildOrderData buildOrder)
	{
		boolean flag = false;
		/*for (final OrderEntryData orderEntry : buildOrder.getLineItems())
		{
			if (orderEntry.getProduct() != null && orderEntry.getProduct().getCode().equals(productCode))
			{
				flag = true;
				break;
			}

		}*/

		return flag;
	}
	
	
	public void addItemToBuildOrder(final JnjBuildOrderData buildOrder, final String productCode, final long qty)
	{
		/*if (productExistsInList(productCode, buildOrder))
		{
			final OrderEntryData entry = getExistingProductFromList(productCode, buildOrder);
			//final Long qty = entry.getQuantity();
			entry.setQuantity(qty);
		}
		else
		{
			buildOrder.getLineItems().add(getProductData(productCode, qty));
		}*/

	}


	public OrderEntryData getExistingProductFromList(final String productCode, final JnjBuildOrderData buildOrder)
	{
		OrderEntryData entry = null;
		/*for (final OrderEntryData orderEntry : buildOrder.getLineItems())
		{
			if (orderEntry.getProduct() != null && (orderEntry.getProduct().getCode().equals(productCode)))
			{
				entry = orderEntry;
				break;
			}

		}*/

		return entry;


	}
	
	
	public void deleteItemFromBuildOrder(final JnjBuildOrderData buildOrder, final String productCode)
	{
		for (final OrderEntryData entry : buildOrder.getLineItems().values())
		{
			if (entry.getProduct().getCode().equals(productCode))
			{
				buildOrder.getLineItems().values().remove(entry);
				break;
			}
		}
	
	}
	
	
	public OrderEntryData getProductData(final String productCode, final long qty)
	{
		OrderEntryData orderEntry = null;
		ProductModel productModel = null;
		List<PriceInformation> prices = null;
		final BigDecimal bigValue = null;

		final UserModel user = userService.getCurrentUser();
		if (user instanceof B2BCustomerModel)
		{
			final B2BUnitModel unit = b2bUnitService.getParent((B2BCustomerModel) user);
			final String b2bunit = unit.getUid();
			
			
		}
		if (!productCode.isEmpty())
		{
			try
			{
				productModel = productService.getProductForCode(productCode);
			}
			catch (final UnknownIdentifierException ex)
			{
				productModel = null;
			}
		}
		if (productModel != null)
		{
			prices = priceService.getPriceInformationsForProduct(productModel);
			orderEntry = new OrderEntryData();
			final ProductData product = new ProductData();
			product.setCode(productModel.getCode());
			product.setName(productModel.getName());
			product.setDescription(productModel.getDescription());
			
			if (!prices.isEmpty())
			{
				final PriceInformation price = prices.iterator().next();
				final PriceData priceData = new PriceData();
				final double val = price.getPriceValue().getValue();
				
				priceData.setCurrencyIso(price.getPriceValue().getCurrencyIso());
				priceData.setValue(bigValue.valueOf(val));
				product.setPrice(priceData);
			}
			orderEntry.setQuantity(qty);
			orderEntry.setProduct(product);

		}

		return orderEntry;
	}
	
	
	public OrderEntryData getOrderEntryDataFromProductCode(final String productCode, final String b2bunit)
	{
		OrderEntryData orderEntry = null;

		if (productCode != null && b2bunit != null)
		{
			orderEntry = new OrderEntryData();

			final ProductModel productModel = productService.getProductForCode(productCode);
			final ProductData productData = productFacade.getProductForOptions(productModel, Arrays.asList(ProductOption.BASIC));
			orderEntry.setProduct(productData);
		}

		return orderEntry;
	}
}
