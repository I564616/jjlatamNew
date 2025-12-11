package com.jnj.facades.order.converters.populator;


import de.hybris.platform.commercefacades.order.converters.populator.CartPopulator;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.impl.JnjConfigServiceImpl;
import com.jnj.facades.data.SurgeryInfoData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.facades.data.JnjGTAddressData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import org.joda.time.LocalTime;



public class JnjGTCartPopulator extends CartPopulator<CartData>
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTCartPopulator.class);

	private static final String DELIVERY_CUTT_OFF_START_TIME = "jnj.delivery.cutt.of.start.time";
	private static final String DELIVERY_CUTT_OFF_END_TIME = "jnj.delivery.cutt.of.end.time";
	private static final String DELIVERY_CUTT_OFF_MSG_DURATION = "jnj.delivery.cutt.of.duration";
	@Autowired
	Populator<CreditCardPaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoPopulator;

	/**
	 * The Instance of <code>SessionService</code>.
	 */
	@Autowired
	private SessionService sessionService;

	@Autowired
	private JnjConfigServiceImpl jnjConfigServiceImpl;

	private JnjGTSurgeryPopulator jnjGTSurgeryPopulator;

	boolean expidate = false;

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);

		if (target instanceof JnjGTCartData)
		{
			final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);
				populateEpicData(source, target);	 
		}
	}

	public void setJnjGTSurgeryPopulator(final JnjGTSurgeryPopulator jnjGTSurgeryPopulator)
	{
		this.jnjGTSurgeryPopulator = jnjGTSurgeryPopulator;
	}

	private void populateCutOfMessageStatus(final CartModel source, final JnjGTCartData jnjGTCartData)
	{
		/*//Set cut of hour to 5pm <Default Server time>
		int startHour = 17;
		int startMintue = 00;
		int endHour = 24;
		int endMinute = 00;

		//Try to get configured value of cut of hour
		try
		{
			startHour = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_START_TIME).split(":")[0]);
			startMintue = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_START_TIME).split(":")[1]);
			endHour = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_END_TIME).split(":")[0]);
			endMinute = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_END_TIME).split(":")[1]);
		}
		catch (final Exception exception)
		{
			LOGGER.error("Delivery cutt of Start/End time is not defined in the Configuration.");
		}

		final Calendar rightNow = Calendar.getInstance();
		final int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
		final int currentMinutes = rightNow.get(Calendar.MINUTE);
		LOGGER.info("Current Server Time : " + rightNow);
		LOGGER.info("Current Hour : " + currentHour);
		LOGGER.info("Cut of Hour : " + startHour);
		if (JnJGTCartPopulatorHelper.isExpediateOrder(source) && currentHour >= startHour && currentMinutes >= startMintue
				&& currentHour < endHour && currentMinutes < endMinute)
		{
			jnjGTCartData.setShowCutOffTimeMessage(true);
		}*/
		
		LocalTime startTime = null;
		final Calendar msgEndTime = Calendar.getInstance();

		//Try to get configured value of cut of hour
		try
		{
			startTime = new LocalTime(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_START_TIME));
			final int durationHour = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_MSG_DURATION).split(
					":")[0]);
			final int durationMinute = Integer.parseInt(jnjConfigServiceImpl.getConfigValueById(DELIVERY_CUTT_OFF_MSG_DURATION)
					.split(":")[1]);

			/* Get the end time for message while using Start time and Duration of Message display */
			msgEndTime.set(Calendar.HOUR_OF_DAY, startTime.getHourOfDay());
			msgEndTime.set(Calendar.MINUTE, startTime.getMinuteOfHour());

			msgEndTime.add(Calendar.HOUR, durationHour);
			msgEndTime.add(Calendar.MINUTE, durationMinute);
			msgEndTime.add(Calendar.SECOND, 60);// Add second to show message till mintue ends


		}
		catch (final Exception exception)
		{
			LOGGER.error("Delivery cutt of Start/End time is not defined in the Configuration.");
			startTime = new LocalTime("17:00");


			msgEndTime.set(Calendar.HOUR_OF_DAY, startTime.getHourOfDay());
			msgEndTime.set(Calendar.MINUTE, startTime.getMinuteOfHour());
			msgEndTime.add(Calendar.HOUR, 8);
			msgEndTime.add(Calendar.MINUTE, 00);
			msgEndTime.add(Calendar.SECOND, 60);// Add second to show message till mintue ends
		}

		LOGGER.info("Cut of Hour Start : " + startTime);

		final LocalTime now = LocalTime.now();
		final Calendar currentDT = Calendar.getInstance();

		if (JnJGTCartPopulatorHelper.isExpediateOrder(source) && now.isAfter(startTime) && currentDT.before(msgEndTime))
		{
			jnjGTCartData.setShowCutOffTimeMessage(true);
		}
	}

	private void populateEpicData(final CartModel source, final CartData target)
	{
		
		final JnjGTCartData jnjGTCartData = (JnjGTCartData) target;
		jnjGTCartData.setDistributorPONumber(source.getDealerPONum());
		jnjGTCartData.setCustomerPoNumber(source.getCustomerReferencePO());
		jnjGTCartData.setAttention(source.getAttention());
		jnjGTCartData.setSpecialText(source.getSpecialText());
		jnjGTCartData.setDropShipAccount(source.getDropShipAccount());
		jnjGTCartData.setOrderType(source.getOrderType().getCode());
		jnjGTCartData.setReasonCode(source.getReasonCode());
		jnjGTCartData.setReasonCodeNoCharge(source.getReasonCode());
		jnjGTCartData.setReasonCodeReturn(source.getReasonCode());
		jnjGTCartData.setSpineSalesRepUCN(source.getSpineSalesRepUCN());
		jnjGTCartData.setCordisHouseAccount(source.getCordisHouseAccount());
		jnjGTCartData.setTotalDropShipFee(createPrice(source, source.getTotalDropShipFee()));
		jnjGTCartData.setTotalminimumOrderFee(createPrice(source, source.getTotalminimumOrderFee()));
		jnjGTCartData.setManualFee(createPrice(source, source.getTotalmanualFee()));
		jnjGTCartData.setExpeditedFee(createPrice(source, source.getTotalExpeditedFees()));
		jnjGTCartData.setSapOrderNumber(source.getSapOrderNumber());
		jnjGTCartData.setLinkedWithDelivered(source.isLinkedWithDelivered());
		jnjGTCartData.setCustomShippingAddress(source.isCustomShippingAddress());
		jnjGTCartData.setEndUser(source.getEndUser());
		jnjGTCartData.setStockUser(source.getStockUser());
		jnjGTCartData.setPoDate(source.getPoDate());
		jnjGTCartData.setReturnCreatedDate(source.getReturnCreatedDate());
		jnjGTCartData.setShippingInstructions(source.getShippingInstruction());

		//adding code for OCD
		//jnjGTCartData.setContainsOCDProduct(Boolean.valueOf(source.isContainsOCDProduct()));
		jnjGTCartData.setContainsOCDProduct(source.isContainsOCDProduct());
		// End Modification
		if (null != source.getUnit())
		{
			/** Setting b2b unit id **/
			jnjGTCartData.setB2bUnitId(source.getUnit().getUid());

			/** Setting b2b unit name **/
			jnjGTCartData.setB2bUnitName(source.getUnit().getName());

			/** Checking if the unit is an instance of JnJB2BUnitModel **/
			if (source.getUnit() instanceof JnJB2BUnitModel)
			{
				/** Setting the b2b unit GLN **/
				jnjGTCartData.setB2bUnitGln(((JnJB2BUnitModel) source.getUnit()).getGlobalLocNo());
			}
		}

		// If third party checkbox is not changed it will be null, so setting to true

		if (source.getAttachedDoc() != null && source.getAttachedDoc().getURL() != null)
		{
			jnjGTCartData.setAttachDocURL(source.getAttachedDoc().getURL());
		}
  //Added
		if (source.getAttachedDoc() != null && source.getAttachedDoc().getRealFileName() != null)
		{
			jnjGTCartData.setAttachDocName(source.getAttachedDoc().getRealFileName());
		}
		if (source.getThirdpartyBilling() == null)
		{
			jnjGTCartData.setThirdPartyBilling(Boolean.FALSE);
		}
		else
		{
			jnjGTCartData.setThirdPartyBilling(source.getThirdpartyBilling());
		}
		if (null != source.getSurgeryInfo())
		{
			final SurgeryInfoData data = new SurgeryInfoData();
			jnjGTSurgeryPopulator.populate(source.getSurgeryInfo(), data);
			jnjGTCartData.setSurgeryInfo(data);
		}
		/*** Adding Shipping Address ***/
		if (null != source.getDeliveryAddress())
		{
			addDeliveryAddress(source, jnjGTCartData);
		}

		/*** Adding Billing Address ***/
		if (null != source.getPaymentAddress())
		{
			final AddressData billingAddress = new JnjGTAddressData();
			getAddressConverter().convert(source.getPaymentAddress(), billingAddress);
			jnjGTCartData.setBillingAddress(billingAddress);
		}

		final PaymentInfoModel paymentInfoModel = source.getPaymentInfo();
		if (null != paymentInfoModel)
		{

			if (paymentInfoModel instanceof CreditCardPaymentInfoModel)
			{
				final CreditCardPaymentInfoModel ccInfoModel = (CreditCardPaymentInfoModel) paymentInfoModel;

				final CCPaymentInfoData ccPaymentInfoData = new CCPaymentInfoData();
				creditCardPaymentInfoPopulator.populate(ccInfoModel, ccPaymentInfoData);
				if (StringUtils.isNotEmpty(ccPaymentInfoData.getCardType()))
				{
					final String crediCardType = StringUtils.substring(ccPaymentInfoData.getCardType(), 2);
					ccPaymentInfoData.setCardType(crediCardType);
				}
				target.setPaymentInfo(ccPaymentInfoData);
			}
		}
		jnjGTCartData.setTotalDropShipFee(createPrice(source, source.getTotalDropShipFee()));
		jnjGTCartData.setHsaPromotion(createPrice(source, source.getTotalHsaPromotion()));
		jnjGTCartData.setTotalFreightFees(createPrice(source, source.getTotalFreightFees()));
		jnjGTCartData.setTotalFees(createPrice(source, source.getTotalFees()));
		jnjGTCartData.setTotalminimumOrderFee(createPrice(source, source.getTotalminimumOrderFee()));
		jnjGTCartData.setDiscountTotal(createPrice(source, Double.valueOf(Math.abs(source.getTotalDiscounts().doubleValue()))));
		jnjGTCartData.setExpectedShipDate(source.getRequestedDeliveryDate());
		//jnjGTCartData.setTotalPromotionAllowance(createPrice(source, source.getTotalPromotionalAllowance()));
		//jnjGTCartData.setTotalUnsaleableAllowance(createPrice(source, source.getTotalUnsaleableAllowance()));
		jnjGTCartData.setTotalPrice(createPrice(source, source.getTotalPrice()));
		/*target.setTotalPrice(createPrice(
				source,Double.valueOf(source.getSubtotal().doubleValue() -source.getTotalPromotionalAllowance().doubleValue() 
						- source.getTotalUnsaleableAllowance().doubleValue() +source.getTotalTax())));*/
		/* Valid from and valid up to dates for Quote order */
		jnjGTCartData.setQuoteEndDate(source.getQuoteExpirationDate());
		jnjGTCartData.setQuoteStartDate(source.getDate());

		final JnjGTSurgeonModel surgeon = source.getSurgeon();
		// If surgeon model is present
		if (surgeon != null)
		{
			final StringBuilder surgeonName = new StringBuilder();

			// If first name is not blank
			if (StringUtils.isNotBlank(surgeon.getFirstName()))
			{
				surgeonName.append(surgeon.getFirstName()).append(" ");
			}
			// If last name is not blank
			if (StringUtils.isNotBlank(surgeon.getLastName()))
			{
				surgeonName.append(surgeon.getLastName());
			}
			jnjGTCartData.setSurgeonName(surgeonName.toString());
		}
		else if(source.getSurgeonName()!=null){

			jnjGTCartData.setSurgeonName(source.getSurgeonName());
		}
				
		
		JnJGTCartPopulatorHelper.populateCartTotalWeightNVolume(source, jnjGTCartData);
		populateCutOfMessageStatus(source, jnjGTCartData);
	}

	/*
	 * private void setCartWeightAndVolume(final JnjGTCartData jnjGTCartData) { double cartWeight = 0; double cartVolume
	 * = 0; String weightUOM = null; String volumeUOM = null;
	 *
	 * for (final OrderEntryData entry : jnjGTCartData.getEntries()) { final JnjGTOrderEntryData jnjNAOrderEntry =
	 * (JnjGTOrderEntryData) entry; if (StringUtils.isNotEmpty(jnjNAOrderEntry.getSelectedRoute())) { expidate = true; }
	 * cartWeight += jnjNAOrderEntry.getOrderWeight(); cartVolume += jnjNAOrderEntry.getCubicVolume(); weightUOM =
	 * jnjNAOrderEntry.getWeightUOM(); volumeUOM = jnjNAOrderEntry.getVolumeUOM(); }
	 * jnjGTCartData.setOrderWeight(cartWeight); jnjGTCartData.setOrderWeightUOM(weightUOM);
	 * jnjGTCartData.setOrderVolume(cartVolume); jnjGTCartData.setOrderVolumeUOM(volumeUOM); }
	 */
}
