package com.jnj.la.facades.contract.converters.populator;

import de.hybris.platform.commerceservices.price.CommercePriceService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjContractEntryData;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.services.contract.JnjContractService;


public class JnjContractEntryDataPopulator implements Populator<JnjContractEntryModel, JnjContractEntryData>
{
	@Autowired
	private JnjPriceDataFactory jnjPriceDataFactory;
	@Autowired
	private CommercePriceService commercePriceService;
	@Autowired
	private JnjContractService jnjContractService;

	@Override
	public void populate(final JnjContractEntryModel source, final JnjContractEntryData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (null != source.getProduct() && source.getProduct() instanceof JnJLaProductModel
				&& null != ((JnJLaProductModel) source.getProduct()).getCatalogId())
		{
			target.setProductCode(((JnJLaProductModel) source.getProduct()).getCatalogId());
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
				target.setContractProductPrice(
						jnjPriceDataFactory.formatPrice(BigDecimal.valueOf(source.getContractPrice().getPrice().doubleValue()),
								source.getContractPrice().getCurrency()));
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
			final JnjUomDTO jnjUomDTO = jnjContractService.getContractDelUomMapping(sourceProduct,
					source.getContractPrice().getUnit());
			target.setProdSalesUnit(
					(source.getContractPrice().getUnit().getName() != null) ? source.getContractPrice().getUnit().getName() : null);
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