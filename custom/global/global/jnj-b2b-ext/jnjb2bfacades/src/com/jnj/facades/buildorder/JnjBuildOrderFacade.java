package com.jnj.facades.buildorder;

import com.jnj.facades.data.JnjBuildOrderData;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

public interface JnjBuildOrderFacade {
	
	public static final String BUILD_ORDER_SESSION_ATTRIBUTE = "buildorder";
	
	
	
	
	public JnjBuildOrderData getBuildOrderFromSession(JnjBuildOrderData buildOrder);
	
	public boolean productExistsInList(final String productCode, final JnjBuildOrderData buildOrder);
	
	public void addItemToBuildOrder(JnjBuildOrderData buildOrder, String productCode, long qty);
	

	public OrderEntryData getOrderEntryDataFromProductCode(String productCode, String b2bunit);
	
	public OrderEntryData getProductData(final String productCode, long qty);

	public void deleteItemFromBuildOrder(JnjBuildOrderData buildOrder, String productCode);

}
