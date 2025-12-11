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

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjLaOrderData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class JnjLaOrderDataPopulator extends JnjGTOrderDataPopulator
{
	private static final Class<JnjLaOrderDataPopulator> currentClass = JnjLaOrderDataPopulator.class;

	@Override
	public void populate(final OrderModel source, final OrderData target){
		final String methodName = "populate()";
		super.populate(source, target);
		if (target instanceof JnjLaOrderData jnjLaOrderData)
		{
			setCustomStatus(source, (JnjLaOrderData)target);
			JnjGTCoreUtil.logInfoMessage("Latam convert from orderModel to orderData populator:" +
							"credit hold flag" + source.getHoldCreditCardFlag(),
					methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, currentClass);
			if (null != source.getUnit()){
				jnjLaOrderData
						.setPartialDelivFlag(((JnJLaB2BUnitModel) source.getUnit()).getPartialDelivFlag() == null ? Boolean.TRUE
								: ((JnJLaB2BUnitModel) source.getUnit()).getPartialDelivFlag());
			}else{
				jnjLaOrderData.setPartialDelivFlag(Boolean.TRUE);
			}

			if (source.getSapOrderNumber() != null){
				jnjLaOrderData.setSapOrderNumber(source.getSapOrderNumber());
			}
			if (source.getContractNumber() != null)	{
				jnjLaOrderData.setContractNumber(source.getContractNumber());
			}
			if(StringUtils.isNotEmpty(source.getComplementaryInfo())){
				jnjLaOrderData.setComplementaryInfo(source.getComplementaryInfo());
			}
			
			if(source.getSapErrorMessages() != null && !source.getSapErrorMessages().isEmpty() && source.getSapOrderNumber() == null)
			{
				jnjLaOrderData.setHasError(Boolean.TRUE);
			}
			jnjLaOrderData.setTotalTax(createPrice(source, source.getTotalTax()));
			jnjLaOrderData.setSubTotal(createPrice(source, source.getSubtotal()));
			jnjLaOrderData.setTotalPrice(createPrice(source, source.getTotalGrossPrice()));
			jnjLaOrderData.setChannel(source.getPoType());
			jnjLaOrderData.setTotalDiscounts(createPrice(source,
					Double.valueOf(Math.abs(source.getTotalDiscounts().doubleValue()))));
			handleEntries(source.getEntries(),target);

			if (null != source.getTotalNetValue() && source.getTotalNetValue().intValue() <= 0)
			{
				jnjLaOrderData.setTotalNetValue(createPrice(source, source.getTotalNetValue()));
			}

			String shipToNumber = null;
			String shipToNumberForConsumer = null;
			if (source.getDeliveryAddress() != null)
			{
				final AddressModel shippingAddress = source.getDeliveryAddress();
				shipToNumber = shippingAddress.getJnJAddressId();
				jnjLaOrderData.setShipToName(shippingAddress.getFirstname());
				final String regionString = (null != shippingAddress.getRegion()) ? Jnjb2bCoreConstants.CONST_COMMA
						+ Jnjb2bCoreConstants.SPACE + shippingAddress.getRegion().getName() : "";
				shipToNumberForConsumer = shippingAddress.getTown() + regionString;
			}
			if (null != sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME))
			{
				if (Jnjb2bCoreConstants.MDD.equals(sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME)))
				{
					jnjLaOrderData.setShipToNumber((shipToNumber != null) ? shipToNumber : String.valueOf("0"));
					jnjLaOrderData.setSoldToAccount(source.getUnit().getUid());
					jnjLaOrderData.setB2bUnitName(source.getUnit().getDisplayName());
				}
				else
				{
					jnjLaOrderData.setShipToNumber((shipToNumberForConsumer != null) ? shipToNumberForConsumer : String
							.valueOf("0"));
				}
			}
			jnjLaOrderData.setChannel(source.getPoType());
			if (source.getHoldCreditCardFlag() != null)
			{
				jnjLaOrderData.setHoldCreditCardFlag(source.getHoldCreditCardFlag());
			}

			JnjGTCoreUtil.logInfoMessage("Latam convert from orderModel to orderData populator:" +
							"partial delivery flag" + jnjLaOrderData.getPartialDelivFlag(),
					methodName, Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD, currentClass);


			final UserModel associatedUser = source.getUser();
			if (associatedUser != null)
			{
				jnjLaOrderData.setOrderedBy(associatedUser.getName());
			}
		}
	}

	public void handleEntries(final List<AbstractOrderEntryModel> entries, final OrderData target) {
		if (CollectionUtils.isNotEmpty(entries) && CollectionUtils.isNotEmpty(target.getEntries()) &&
				entries.size() == target.getEntries().size()){
			for(int i=0; i<target.getEntries().size(); i++){
				((JnjLaOrderEntryData) target.getEntries().get(i)).setGtsHold(entries.get(i).getGTSHold());
			}
		}
	}
	
	private void setCustomStatus(final OrderModel source, final JnjGTOrderData target) {
		if(source.getStatus() != null) {
			if(OrderStatus.ERP_EXPORT_ERROR.getCode().equalsIgnoreCase(source.getStatus().getCode())) {
				target.setStatusDisplay(enumerationService.getEnumerationName(OrderStatus.CREATED));
			}else {
				target.setStatusDisplay(enumerationService.getEnumerationName(source.getStatus()));
			}
		} else {
			target.setStatusDisplay(StringUtils.EMPTY);
		}
	}
}
