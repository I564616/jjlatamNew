/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
 

package com.jnj.facades.product.converters.populator;


import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.facades.company.JnjGTB2BUnitFacade;
import com.jnj.facades.data.JnjProductCarouselData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.util.Assert;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nsinha7
 *
 */
public class JnjGTProductCarouselPopulator implements Populator<JnJProductModel, JnjProductCarouselData> {

	
	protected JnJGTProductService jnJGTProductService;
	protected JnjGTB2BUnitFacade jnjGTB2BUnitFacade;
	protected Populator<ProductModel, ProductData> productPricePopulator;
	
	@Override
	public void populate(JnJProductModel source, JnjProductCarouselData target) throws ConversionException {

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setProductCode(source.getCode());
		target.setProductName(source.getName());
		target.setKitType(source.getKitType()!=null?source.getKitType().getCode():StringUtils.EMPTY );

		
		String productUrl = jnJGTProductService.getProductUrl(source);
		target.setProductURL(productUrl);
		if(jnjGTB2BUnitFacade.isCustomerInternationalAff()){
			String price = getProductPrice(source);
			target.setPrice(price);
		}else{
			target.setPrice(StringUtils.EMPTY);
		}
		target.setImageURL(source.getPicture()!=null?source.getPicture().getURL():StringUtils.EMPTY);

	}

	protected String getProductPrice(JnJProductModel source) {
		
		String price =StringUtils.EMPTY;
		final ProductData productData = new ProductData();
		final JnjGTVariantProductModel variantModel = jnJGTProductService.getDeliveryGTIN(source);
		productPricePopulator.populate(variantModel, productData);
		if(null != productData.getPrice() && null != productData.getPrice().getValue())
		{
			price = productData.getPrice().getFormattedValue();
		}
		
		return price;
	}



	public JnJGTProductService getJnJGTProductService() {
		return jnJGTProductService;
	}

	public void setJnJGTProductService(JnJGTProductService jnJGTProductService) {
		this.jnJGTProductService = jnJGTProductService;
	}


	public JnjGTB2BUnitFacade getJnjGTB2BUnitFacade() {
		return jnjGTB2BUnitFacade;
	}

	public void setJnjGTB2BUnitFacade(JnjGTB2BUnitFacade jnjGTB2BUnitFacade) {
		this.jnjGTB2BUnitFacade = jnjGTB2BUnitFacade;
	}


	public Populator<ProductModel, ProductData> getProductPricePopulator() {
		return productPricePopulator;
	}

	public void setProductPricePopulator(Populator<ProductModel, ProductData> productPricePopulator) {
		this.productPricePopulator = productPricePopulator;
	}
	
	
}
