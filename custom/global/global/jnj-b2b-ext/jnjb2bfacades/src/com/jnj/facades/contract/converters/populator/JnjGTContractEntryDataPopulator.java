package com.jnj.facades.contract.converters.populator;

import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.services.contract.JnjContractService;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjProductData;


public class JnjGTContractEntryDataPopulator implements Populator<JnjContractEntryModel, JnjContractEntryData>
{
	@Resource(name = "priceDataFactory")
	private JnjPriceDataFactory jnjPriceDataFactory;
	@Autowired
	private CommercePriceService commercePriceService;
	@Autowired
	private JnjContractService jnjContractService;
	@Resource(name = "b2bProductFacade")
	private ProductFacade productFacade;

	@Override
	public void populate(final JnjContractEntryModel source, final JnjContractEntryData target) throws ConversionException
	{
		ProductData productData = null;
		if(source.getProduct()!= null)
		{
			productData = productFacade.getProductForOptions(source.getProduct(),
				Arrays.asList(ProductOption.BASIC,
						ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION, ProductOption.GALLERY,
						ProductOption.CATEGORIES, ProductOption.PROMOTIONS, ProductOption.VARIANT_FULL, ProductOption.STOCK,
						ProductOption.VOLUME_PRICES, ProductOption.PRICE_RANGE, ProductOption.VARIANT_MATRIX));
		}
		if(null != source.getProduct())
		{
			target.setProduct((JnjGTProductData) productData);
		}
		if (null != source.getProduct() && null != source.getProduct().getCode())
		{
			target.setProductCode(source.getProduct().getCode());
		}
		if (null != source.getProduct().getDescription())
		{
			target.setProductDescription(source.getProduct().getDescription());
		}
		if (null != source.getProduct().getName())
		{
			target.setProductName(source.getProduct().getName());
		}
		if (null != source.getProduct())
		{
			final PriceInformation priceInformation = commercePriceService.getFromPriceForProduct(source.getProduct());
			if (null != priceInformation)
			{
				target.setProductPrice(jnjPriceDataFactory.formatPrice(
						BigDecimal.valueOf(priceInformation.getPriceValue().getValue()), source.getContractPrice().getCurrency()));
			}
		}
		if (null != source.getContractPrice())
		{
			if (null != source.getContractPrice().getPrice())
			{
				target.setContractProductPrice(jnjPriceDataFactory.formatPrice(
						BigDecimal.valueOf(source.getContractPrice().getPrice().doubleValue()), source.getContractPrice().getCurrency()));
			}
		}
		target.setContractBalanceQty(source.getContractBalanceQty());
		target.setContractQty(source.getContractQty());
		target.setLineNum(source.getLineNum());
		if (null != source.getContractPrice())
		{
			if (null != source.getProduct().getUnit())
			{
				target.setProductUnit(source.getProduct().getUnit().getName());
			}
		}
		if (null != source.getContractQty() && null != source.getContractBalanceQty())
		{
			target.setConsumedQty(Integer.valueOf((source.getContractQty().intValue() - source.getContractBalanceQty().intValue())));
		}
		target.setTenderLineNum(source.getTenderLineNum());
		/* CP008-CH003 Changes Start */
		final JnJProductModel sourceProduct = source.getProduct();
		if (null != source.getContractPrice() && null != source.getContractPrice().getUnit())
		{
			final JnjUomDTO jnjUomDTO = jnjContractService.getContractDelUomMapping(sourceProduct, source.getContractPrice()
					.getUnit());
			target.setProdSalesUnit((source.getContractPrice().getUnit().getName() != null) ? source.getContractPrice().getUnit()
					.getName() : null);
			if (null != jnjUomDTO)
			{
				target.setUnitOfMeasureQuantity(String.valueOf(jnjUomDTO.getSalesUnitsCount()));
			}
		}
		target.setProdDeliveryUnit((sourceProduct.getUnit() != null) ? sourceProduct.getUnit().getName() : null);
		/* CP008-CH003 Changes Ends */

		//Start of CP024 Changes
		final boolean entryActive = jnjContractService.isContractEntryActive(source);
		target.setActive(Boolean.valueOf(entryActive));
		//End  of CP024 Changes
	}
}