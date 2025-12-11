package com.jnj.facades.invoice.coverters.populator;

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.i18n.impl.DefaultCommerceCommonI18NService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.IterableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.jnj.facade.util.impl.JnjPriceDataFactory;
import com.jnj.facades.data.JnjGTInvoiceEntryData;
import com.jnj.facades.data.JnjGTInvoiceOrderData;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.services.JnjGTOrderService;


public class JnjGTInvoiceOrderDataPopulator implements Populator<JnjGTInvoiceModel, JnjGTInvoiceOrderData>
{
	/** The jnj invoice entry data converter. */
	private Converter<JnjGTInvoiceEntryModel, JnjGTInvoiceEntryData> jnjGTInvoiceEntryDataConverter;

	/** The default commerce common i18 n service. */
	@Autowired
	private DefaultCommerceCommonI18NService defaultCommerceCommonI18NService;

	@Resource(name = "priceDataFactory")
	private JnjPriceDataFactory jnjPriceDataFactory;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private JnjGTOrderService jnjGTOrderService;

	/**
	 * Populates data attributes of JnjGTInvoiceOrderData from the source instance of JnjGTInvoiceModel.
	 */
	@Override
	public void populate(final JnjGTInvoiceModel source, final JnjGTInvoiceOrderData target) throws ConversionException
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		/*** Invoice Currency must be picked from its source itself rather from the associated order. ***/
		final CurrencyModel currentcurrency = (source.getCurrency() != null) ? source.getCurrency() : commonI18NService
				.getCurrentCurrency();
		final double totalTaxCharges = source.getTotalHsaPromotion().doubleValue()
				- (source.getSubTotalPrice().doubleValue() + source.getTotalFees().doubleValue() + source.getTotalTax().doubleValue());
		target.setInvoiceNumber(source.getInvoiceNum());
		target.setBillingdate(source.getBillingDate());
		target.setOrderNumber(source.getOrder().getCode());
		/** Code changes for JJEPIC-745 **/
		target.setAdjustedRateAllowance(createPrice(currentcurrency,
				Double.valueOf(-1 * source.getAdjustedRateAllowance().doubleValue())));
		target.setTotalFees(createPrice(currentcurrency, source.getTotalFees()));
		target.setOtherCharges(createPrice(currentcurrency, source.getOtherCharges()));
		target.setDropShipFee(createPrice(currentcurrency, source.getTotalDropShipFee()));
		target.setMinimumOrderFee(createPrice(currentcurrency, source.getTotalminimumOrderFee()));
		target.setTotalFreightFees(createPrice(currentcurrency, source.getTotalFreightFees()));
		target.setHsaPromotion(createPrice(currentcurrency, source.getTotalHsaPromotion()));
		target.setTaxTotal(createPrice(currentcurrency, source.getTotalTax()));
		final Double taxAmt = StringUtils.isNotEmpty(source.getTaxAmt()) ? Double.valueOf(source.getTaxAmt()) : Double.valueOf(0.0);
		target.setTaxAmt(createPrice(currentcurrency, taxAmt));
		final Double netValue = StringUtils.isNotEmpty(source.getNetValue()) ? Double.valueOf(source.getNetValue()) : Double
				.valueOf(0.0);
		target.setTempFeeAndPromotion(createPrice(currentcurrency,
				Double.valueOf(netValue.doubleValue() - (source.getSubTotalPrice().doubleValue() + taxAmt.doubleValue()))));
		target.setInvoiceTotal(createPrice(
				currentcurrency,
				StringUtils.isNotEmpty(source.getInvoiceTotalAmount().toString()) ? Double.valueOf(source.getInvoiceTotalAmount()
						.doubleValue() - source.getTotalTax().doubleValue()) : Double.valueOf(0.0)));
		target.setSubTotal(createPrice(currentcurrency, source.getSubTotalPrice()));
		target.setManualFee(createPrice(currentcurrency, source.getTotalManualFee()));
		target.setExpeditedFee(createPrice(currentcurrency, source.getTotalExpediteFee()));
		target.setNetValue(createPrice(currentcurrency,
				StringUtils.isNotEmpty(source.getNetValue()) ? Double.valueOf(source.getNetValue()) : Double.valueOf(0.0)));
		target.setOtherCharges(createPrice(currentcurrency, Double.valueOf(netValue.doubleValue() + totalTaxCharges)));
		//target.setTotalPromotionAllowance(createPrice(currentcurrency,Double.valueOf(source.getTotalPromotionalAllowance())));
		//target.setTotalUnsaleableAllowance(createPrice(currentcurrency,Double.valueOf(source.getTotalUnsaleableAllowance())));
		String carrierName = null;
		String deliveryNum = null;
		long totalWeight = 0;
		long totalVolume = 0;
		final Set<String> packingList = new HashSet();
		final List<JnjGTInvoiceEntryModel> invoiceEntries = (List<JnjGTInvoiceEntryModel>) source.getEntries();
		final List<JnjGTInvoiceEntryData> invoiceEntriesData = new ArrayList<JnjGTInvoiceEntryData>();

		for (final JnjGTInvoiceEntryModel invoiceEntry : invoiceEntries)
		{
			if (null != invoiceEntry.getQty() && invoiceEntry.getQty().longValue() > 0)
			{
				invoiceEntriesData.add(getJnjGTInvoiceEntryDataConverter().convert(invoiceEntry, new JnjGTInvoiceEntryData()));

				carrierName = (carrierName == null) ? ((invoiceEntry.getCarrierCode() != null && !invoiceEntry.getCarrierCode()
						.isEmpty()) ? (jnjGTOrderService.getShippingMethod(invoiceEntry.getCarrierCode(), null) != null ? jnjGTOrderService
						.getShippingMethod(invoiceEntry.getCarrierCode(), null).getDispName() : invoiceEntry.getCarrierCode())
						: null)
						: carrierName;

			deliveryNum = (deliveryNum == null) ? ((invoiceEntry.getDeliveryNum() != null && !invoiceEntry.getDeliveryNum().isEmpty()) ? invoiceEntry.getDeliveryNum() : null) : deliveryNum;
			if(invoiceEntry.getDeliveryNum()!=null){
			packingList.add(invoiceEntry.getDeliveryNum());
			}
			if (invoiceEntry.getReferencedVariant() != null)
			{
				totalWeight = totalWeight
						+ ((invoiceEntry.getReferencedVariant().getWeightQty() != null) ? (invoiceEntry.getReferencedVariant()
								.getWeightQty().longValue() * invoiceEntry.getQty().longValue()) : 0);

				totalVolume = totalVolume
						+ ((invoiceEntry.getReferencedVariant().getVolumeQty() != null) ? (invoiceEntry.getReferencedVariant()
								.getVolumeQty().longValue() * invoiceEntry.getQty().longValue()) : 0);
			}

			}
		}

		target.setBillOfLading(getBillOfLading(deliveryNum, source.getOrder()));
		target.setInvoiceEntries(invoiceEntriesData);
		target.setCarrier(carrierName);
		target.setPackingList(new ArrayList<>(packingList));
		target.setOrderWeight(Long.valueOf(totalWeight).toString());
		target.setCubicVolume(Long.valueOf(totalVolume).toString());
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
	 * Retrieves the Bill of Lading from a Shipment and Tracking associated with the Order and having same delivery
	 * number.
	 * 
	 * @param deliveryNum
	 * @param order
	 * @return String
	 */
	private String getBillOfLading(final String deliveryNum, final OrderModel order)
	{
		String billOfLading = null;
		final Set<JnjGTShippingDetailsModel> shippingDetails = order.getShippingDetails();
		if (!StringUtils.isEmpty(deliveryNum) && !IterableUtil.isNullOrEmpty(shippingDetails))
		{
			for (final JnjGTShippingDetailsModel shippingDetail : shippingDetails)
			{
				if (deliveryNum.equals(shippingDetail.getDeliveryNum()))
				{
					billOfLading = shippingDetail.getBolNum();
					break;
				}
			}
		}
		return billOfLading;
	}

	public Converter<JnjGTInvoiceEntryModel, JnjGTInvoiceEntryData> getJnjGTInvoiceEntryDataConverter()
	{
		return jnjGTInvoiceEntryDataConverter;
	}

	public void setJnjGTInvoiceEntryDataConverter(
			final Converter<JnjGTInvoiceEntryModel, JnjGTInvoiceEntryData> jnjGTInvoiceEntryDataConverter)
	{
		this.jnjGTInvoiceEntryDataConverter = jnjGTInvoiceEntryDataConverter;
	}

}
