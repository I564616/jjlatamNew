package com.jnj.facades.order.converters.populator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.jnj.core.model.JnJGTProductKitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.services.JnJGTProductService;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;

import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTProductKitData;
import com.jnj.facades.product.JnjGTProductFacade;








import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.variants.model.VariantProductModel;

public class JnjGTProductKitPopulator implements Populator<JnJGTProductKitModel, ProductData> {
	
	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;
	
	@Override
	public void populate(JnJGTProductKitModel source, ProductData target)
			throws ConversionException 
	{
		if(target instanceof JnjGTProductKitData){
			JnjGTProductKitData prodKitData = (JnjGTProductKitData) target;
			String componentCode;		
			componentCode = source.getComponentCode();
			final ProductModel deliveryVariant = jnJGTProductService.getMDDDeliveryVariantByProdCode(componentCode);
				if (null != deliveryVariant)
				{
					final ProductModel product = ((VariantProductModel) deliveryVariant).getBaseProduct();
					
					prodKitData.setProductName(product.getName());
					prodKitData.setProductDescription(product.getDescription());
					if(product != null && product instanceof JnJProductModel){
						JnJProductModel jnjProd = (JnJProductModel) product;
						if(StringUtils.isNotBlank(jnjProd.getProductDesc2()))
						{
							prodKitData.setProductDesc2(jnjProd.getProductDesc2());
						}
					}
					String prodUrl = jnJGTProductService.getProductUrl(product);
					prodKitData.setProductImageUrl(prodUrl);
				}
				prodKitData.setProductCode(source.getComponentCode());
				prodKitData.setProductQty(source.getComponentQty().toString());
	}
	}
	
 
}
