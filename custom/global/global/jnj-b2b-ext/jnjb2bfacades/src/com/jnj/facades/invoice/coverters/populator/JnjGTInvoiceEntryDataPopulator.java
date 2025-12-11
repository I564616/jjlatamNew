/**
 * 
 */
package com.jnj.facades.invoice.coverters.populator;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.session.SessionService;
import com.jnj.core.services.JnjGTOrderService;
import java.math.BigDecimal;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import org.apache.log4j.Logger;
import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjGTInvoiceEntryData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


public class JnjGTInvoiceEntryDataPopulator implements Populator<JnjGTInvoiceEntryModel, JnjGTInvoiceEntryData>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTInvoiceEntryDataPopulator.class);

	private Converter<JnJProductModel, JnjGTProductData> jnjGTProductDataConverter;

	@Resource(name = "priceDataFactory")
	private JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;


	@Autowired
	protected OrderService orderService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	protected JnjGTOrderService jnjGTOrderService;

	/**
	 * Flag to indicate if the session corresponds to MDD or Consumer site.
	 */
	private boolean isMddSite;

	@Override
	public void populate(final JnjGTInvoiceEntryModel source, final JnjGTInvoiceEntryData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
		setMddSite(Jnjb2bCoreConstants.MDD.equals(currentSite) ? true : false);

		/*** Invoice Currency must be picked from its source itself rather from the associated order. ***/
		final CurrencyModel currentCurrency = (source.getInvoice().getCurrency() != null) ? source.getInvoice().getCurrency()
				: commonI18NService.getCurrentCurrency();

		//TODO [AR]: Dependent on shipment & Tracking.
		//target.setTrackingId(trackingId)

		target.setShipDate(source.getShipDate());
		target.setTaxValue(source.getTaxValue());

		target.setStatus(jnjConfigServiceImpl.getConfigValueById(Jnjb2bCoreConstants.Order.Invoice.INVOICE_STATUS_KEY));
		try
		{
			addProduct(source, target);
			if (isMddSite)
			{
				target.setBasePrice(createPrice(currentCurrency,
						((source.getNetValuePerUnit() != null) ? Double.valueOf(source.getNetValuePerUnit()) : null)));
				target.setTotalPrice(createPrice(currentCurrency,
						((source.getNetTotalValue() != null) ? Double.valueOf(source.getNetTotalValue()) : null)));
			}
			else
			{
				double basePrice = 0.0;
				if (null != source.getQty() && StringUtils.isNotEmpty(source.getSubTotal3()) && source.getQty().longValue() > 0)
				{
					basePrice = Double.valueOf(source.getSubTotal3()).doubleValue() / source.getQty().doubleValue();
					LOGGER.info("Base Price Of Consumer Entry " + basePrice);
				}
				target.setBasePrice(createPrice(currentCurrency, ((basePrice > 0.0) ? Double.valueOf(basePrice) : null)));
				target.setTotalPrice(createPrice(currentCurrency,
						((source.getSubTotal3() != null) ? Double.valueOf(source.getSubTotal3()) : null)));
			}

			target.setExtendedPrice(createPrice(currentCurrency, source.getExtendedPrice()));
			target.setQty((source.getQty() != null) ? source.getQty().toString() : Long.valueOf(0).toString());
			target.setLineNumber(source.getLineNum());
			final List<OrderEntryModel> entries = orderService.getEntriesForProduct(source.getInvoice().getOrder(),
					source.getProduct());
			if (CollectionUtils.isNotEmpty(entries))
			{
				target.setContractNum(entries.get(0).getContractNum());

				target.setOrderedQty(entries.get(0).getQuantity());
			}

		}
		catch (final IllegalArgumentException exception)
		{
			LOGGER.error("Exception while converting either of the Base, total, extended price for the invoice entry.");
		}
	}

	/**
	 * Converts and add product data target from source product in the invoice entry.
	 * 
	 * @param source
	 * @param target
	 */
	protected void addProduct(final JnjGTInvoiceEntryModel source, final JnjGTInvoiceEntryData target)
	{
		final JnjGTProductData productData = new JnjGTProductData();
		target.setProduct(getJnjGTProductDataConverter().convert(source.getProduct(), productData));

		if (source.getReferencedVariant() != null)
		{
			final JnjGTVariantProductModel variant = source.getReferencedVariant();
			productData.setGtin(variant.getEan());
			productData.setUpc(variant.getUpc());
			productData.setNumerator((variant.getNumerator() != null) ? variant.getNumerator().toString() : null);
			productData.setDeliveryUnit((variant.getUnit() != null) ? variant.getUnit().getName() : null);
			productData.setSalesUnit((variant.getLwrPackagingLvlUom() != null) ? variant.getLwrPackagingLvlUom().getName() : null);
		}
	}

	/**
	 * The createPrice method converts the double value in the proper price format.
	 * 
	 * @param currency
	 * @param val
	 * @return PriceData
	 */
	protected PriceData createPrice(final CurrencyModel currency, final Double val)
	{
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		final double priceValue = val != null ? val.doubleValue() : 0d;
		return jnjPriceDataFactory.create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	/**
	 * @return the jnjGTProductDataConverter
	 */
	public Converter<JnJProductModel, JnjGTProductData> getJnjGTProductDataConverter()
	{
		return jnjGTProductDataConverter;
	}


	/**
	 * @param jnjGTProductDataConverter
	 *           the jnjGTProductDataConverter to set
	 */
	public void setJnjGTProductDataConverter(final Converter<JnJProductModel, JnjGTProductData> jnjGTProductDataConverter)
	{
		this.jnjGTProductDataConverter = jnjGTProductDataConverter;
	}

	/**
	 * @param isMddSite
	 *           the isMddSite to set
	 */
	public void setMddSite(final boolean isMddSite)
	{
		this.isMddSite = isMddSite;
	}

}
