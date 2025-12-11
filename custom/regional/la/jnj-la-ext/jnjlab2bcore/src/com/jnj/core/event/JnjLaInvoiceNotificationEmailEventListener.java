/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.core.event;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.enumeration.EnumerationService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.services.order.JnjLAOrderService;

import org.apache.commons.lang3.StringUtils;
import com.jnj.la.core.model.JnjOrderTypeModel;
import org.apache.log4j.Logger;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.la.core.model.JnjLaInvoiceNotificationEmailProcessModel;

import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;

import com.jnj.core.util.JnjGTCoreUtil;

/**
 * 
 * Event Listener class responsible publish the <code>JnjLaInvoiceNotificationEmailEvent</code> along with setting
 * required data from event to the business process model.
 *
 */
public class JnjLaInvoiceNotificationEmailEventListener extends AbstractSiteEventListener<JnjLaInvoiceNotificationEmailEvent>
{
	private static final Logger LOGGER = Logger.getLogger(JnjLaInvoiceNotificationEmailEventListener.class);
	private static final String BLANK = " ";
	private static final String PAC_HIVE_ENABLED = "pac.aera.enabled";
	private static final Long LONG_DEFAULT_VALUE = 0L;
	private static final String DASH = " - ";
	private DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.DATE_FORMAT);

	protected ModelService modelService;
	protected BusinessProcessService businessProcessService;
	protected EnumerationService enumerationService;
	protected JnjLAOrderService jnjLAOrderService;	
	protected ConfigurationService configurationService;

	@Override
	protected void onSiteEvent(final JnjLaInvoiceNotificationEmailEvent event) {
		final JnjLaInvoiceNotificationEmailProcessModel emailBusinessProcessModel =  businessProcessService
				.createProcess("JnjLaInvoiceNotificationEmailProcess" + "-" + System.currentTimeMillis(),
						"JnjLaInvoiceNotificationEmailProcess");
		
		emailBusinessProcessModel.setCustomer(event.getCustomer());
		emailBusinessProcessModel.setStore(event.getBaseStore());
		emailBusinessProcessModel.setSite(event.getSite());
		emailBusinessProcessModel.setCurrency(event.getCurrency());
		emailBusinessProcessModel.setLanguage(event.getLanguage());		
		emailBusinessProcessModel.setB2bCustomerEmails(event.getB2bCustomerEmails());

		JnJInvoiceOrderModel invoice = event.getInvoice();
		OrderModel order = event.getOrder();
		StringBuilder invoiceHeader = new StringBuilder();

		invoiceHeader.append(order.getUnit().getUid())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(order.getSapOrderNumber())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(order.getPurchaseOrderNumber())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(invoice.getNfNumber())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(invoice.getInvDocNo())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(getOrderType(order))
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(order.getUser().getName())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(convertAddressToString(order.getPaymentAddress()))
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(convertAddressToString(order.getDeliveryAddress()))
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(order.getOrderChannel() != null ? order.getOrderChannel().getName(): StringUtils.EMPTY)
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
		        .append(order.getCode())
		        .append(Jnjb2bCoreConstants.SYMBOl_PIPE)
		        .append(order.getUnit().getCountry().getIsocode());		
		
		LOGGER.info("invoice header :"+ invoiceHeader);
		emailBusinessProcessModel.setInvoiceNotificationEmailData(invoiceHeader.toString());

		List<String> strList = new ArrayList<>();
		List<JnJInvoiceEntryModel> entryList = invoice.getEntries();
		
		for (final JnJInvoiceEntryModel entry : entryList) {
			StringBuilder invoiceEntry = new StringBuilder();
            if (null != entry.getMaterial()){
            	invoiceEntry.append(entry.getMaterial().getName())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(entry.getMaterial().getCode())
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE);
            } else {            	
            	invoiceEntry.append(StringUtils.EMPTY)
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
				.append(StringUtils.EMPTY)
				.append(Jnjb2bCoreConstants.SYMBOl_PIPE);
            }

            String uom = StringUtils.EMPTY;
	        if (null != entry.getMaterial()){
	        	uom = entry.getMaterial().getDeliveryUnitOfMeasure().getName();
	        }
			
			List<String> eddData = getEstimatedDeliveryDateData(order, entry.getSalesOrderItemNo(), uom);
					
            invoiceEntry.append(entry.getQty())
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(uom)
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(CollectionUtils.isNotEmpty(eddData) ? eddData : StringUtils.EMPTY)
					.append(Jnjb2bCoreConstants.SYMBOl_PIPE)
					.append(enumerationService.getEnumerationName(order.getStatus()));					
			
			LOGGER.info("invoice entry :"+ invoiceEntry);
			strList.add(invoiceEntry.toString());
		}
		emailBusinessProcessModel.setInvoiceNotificationEmailEntries(strList);

		try {
			modelService.save(emailBusinessProcessModel);
			businessProcessService.startProcess(emailBusinessProcessModel);
		} catch (final Exception e) {
			JnjGTCoreUtil.logErrorMessage("Send Invoice Email", "onSiteEvent()",
					"Saving 'jnjLaInvoiceNotificationEmailProcessModel' has caused an exception: "
							+ e.getMessage(), e, JnjLaInvoiceNotificationEmailEventListener.class);
		}
	}
	
	private String getOrderType(final OrderModel order) {

		if (order.getJnjOrderType() != null) {
			return order.getJnjOrderType().getName();
		} else if (order.getOrderType() != null) {
			try {
				JnjOrderTypeModel orderTypeModel = jnjLAOrderService.getOrderType(order.getOrderType().getCode());
				if (orderTypeModel != null) {
					return orderTypeModel.getName();
				}
			} catch(Exception exe) {
				JnjGTCoreUtil.logErrorMessage("Send Invoice Email", "onSiteEvent()",
						"Fetch order type has caused an exception: "
								+ exe.getMessage(), exe, JnjLaInvoiceNotificationEmailEventListener.class);
			}
		}
		return StringUtils.EMPTY;
	}

    private List<String> getEstimatedDeliveryDateData(final OrderModel order, final String salesOrderItemNo, String uom) {

		List<AbstractOrderEntryModel> entries = order.getEntries();
		for (AbstractOrderEntryModel entry : entries) {
			if (StringUtils.isNotEmpty(entry.getSapOrderlineNumber())
					&& StringUtils.isNotEmpty(salesOrderItemNo)
					&& entry.getSapOrderlineNumber().equals(salesOrderItemNo)) {
					return getScheduleLineData(entry, uom);
			}
		}
		return new ArrayList<>();
	}

	private List<String> getScheduleLineData(final AbstractOrderEntryModel source, String uom) {
		List<String> schedLineDataList = new ArrayList<>();
		boolean pacHiveEntries = CollectionUtils.isNotEmpty(source
				.getJnjPacHiveEntries());
		boolean pacHiveEnabled = configurationService.getConfiguration()
				.getBoolean(PAC_HIVE_ENABLED, false) && pacHiveEntries;
		boolean podFlag = CollectionUtils
				.isEmpty(source.getDeliverySchedules())
				|| podIsNullOrEmpty(source.getDeliverySchedules());

		populateDeliveryScheduleLines(source, schedLineDataList, uom);
		populatePacHiveScheduleLines(source, schedLineDataList, pacHiveEnabled,
				podFlag, uom);

		return schedLineDataList;
	}

	

	private void populatePacHiveScheduleLines(final AbstractOrderEntryModel source,
			final List<String> scheduleLineDataList, final boolean pacHiveEnabled, boolean podFlag, String uom) {

		if (podFlag && pacHiveEnabled && !JnjLaCommonUtil.isDisableCountries(source)
				&& JnjLaCommonUtil.isEnabledForSectors(source)) {
			for (final JnjPacHiveEntryModel pacHiveEntryModel : source.getJnjPacHiveEntries()) {
				if (ObjectUtils.isNotEmpty(pacHiveEntryModel.getConvertedRecommendedDeliveryDate())
						&& ObjectUtils.isNotEmpty(pacHiveEntryModel.getConfirmedQuantity())) {
					StringBuilder deliveryDate = new StringBuilder();
					deliveryDate.append(dateFormat.format(pacHiveEntryModel.getConvertedRecommendedDeliveryDate()));

					deliveryDate.append(DASH);
					deliveryDate.append(ObjectUtils.defaultIfNull(pacHiveEntryModel.getConfirmedQuantity(),
							LONG_DEFAULT_VALUE).longValue() + StringUtils.EMPTY);
					deliveryDate.append(BLANK).append(uom);
					scheduleLineDataList.add(deliveryDate.toString());
				}
			}
		}
	}


	private void populateDeliveryScheduleLines(final AbstractOrderEntryModel source,
			final List<String> scheduleLineDataList, String uom) {		

		if (CollectionUtils.isNotEmpty(source.getDeliverySchedules())) {
			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : source.getDeliverySchedules()) {
				if (ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getProofOfDeliveryDate())
						|| ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getDeliveryDate())
						&& ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getQty())) {
					StringBuilder deliveryDate = new StringBuilder();
					Date date = jnjDeliveryScheduleModel.getProofOfDeliveryDate() != null
							? jnjDeliveryScheduleModel.getProofOfDeliveryDate()
							: jnjDeliveryScheduleModel.getDeliveryDate();
					deliveryDate.append(dateFormat.format(date)).append(DASH).append(jnjDeliveryScheduleModel.getQty()).append(BLANK).append(uom);
					scheduleLineDataList.add(deliveryDate.toString());
				}
			}
		}
	}

	/**
	 * 
	 * @param deliverySchedules
	 * @return will return true if POD is null.
	 */
	private static boolean podIsNullOrEmpty(List<JnjDeliveryScheduleModel> deliverySchedules) {
		return deliverySchedules.stream().findAny().map(JnjDeliveryScheduleModel::getProofOfDeliveryDate).isEmpty();
	}
	
	private static String convertAddressToString(final AddressModel address) {
		StringBuilder str = new StringBuilder();
		if (address != null)
		{				
			str.append(StringUtils.isNotEmpty(address.getFirstname())?address.getFirstname()+ BLANK: StringUtils.EMPTY);			
			str.append(StringUtils.isNotEmpty(address.getLine1()) ? address.getLine1() + BLANK: StringUtils.EMPTY);			
			str.append(StringUtils.isNotEmpty(address.getTown()) ? address.getTown() + BLANK: StringUtils.EMPTY);
			str.append(null!=address.getRegion() ? Jnjb2bCoreConstants.CONST_COMMA
					+ Jnjb2bCoreConstants.SPACE + address.getRegion().getName() + BLANK: StringUtils.EMPTY);			
			str.append(StringUtils.isNotEmpty(address.getPostalcode()) ? address.getPostalcode():StringUtils.EMPTY);
			return str.toString();
		}
		return StringUtils.EMPTY;
	}	
	
		
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
	
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	/**
	 * Overridden to handle the event publish request.
	 */
	@Override
	protected boolean shouldHandleEvent(final JnjLaInvoiceNotificationEmailEvent event)
	{		
		return true;
	}
	
	public JnjLAOrderService getJnjLAOrderService() {
		return jnjLAOrderService;
	}

	public void setJnjLAOrderService(JnjLAOrderService jnjLAOrderService) {
		this.jnjLAOrderService = jnjLAOrderService;
	}
	
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}
}
