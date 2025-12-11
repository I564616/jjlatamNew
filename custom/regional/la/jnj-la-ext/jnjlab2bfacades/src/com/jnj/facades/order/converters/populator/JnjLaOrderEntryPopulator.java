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

import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.constants.Jnjb2bFacadesConstants.Logging;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjLaProductData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.product.converters.populator.JnjLaOrderEntryProductPopulator;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.model.JnJLaProductModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.jnj.facades.data.JnjGTOrderEntryData;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.gt.pac.aera.model.JnjPacHiveEntryModel;

public class JnjLaOrderEntryPopulator extends JnjGTOrderEntryPopulator
{
	public static final String PAC_HIVE_ENABLED = "pac.aera.enabled";
	public static final Long LONG_DEFAULT_VALUE = 0L;

	@Autowired
	private UserService userService;

	@Autowired
	protected JnjLaOrderEntryProductPopulator jnjGTOrderEntryProductPopulator;

	@Autowired
	protected EnumerationService enumerationService;

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	@Autowired
	protected ConfigurationService configurationService;
	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		final String methodName = "populate()";
		JnjGTCoreUtil.logDebugMessage("Latam OrderEntry Populator", methodName, Logging.BEGIN_OF_METHOD,
				JnjLaOrderEntryPopulator.class);
		super.populate(source, target);
		final JnjGTOrderEntryData jnjOrderEntryData = (JnjGTOrderEntryData) target;
		jnjOrderEntryData.setScheduleLines(scheduleLineData(source));
		if (target instanceof JnjLaOrderEntryData){
			final JnjLaOrderEntryData laDataEntry = (JnjLaOrderEntryData) target;
			final Long orderedQuantity = updateQuantity(target, source.getQuantity());
			source.setQuantity(orderedQuantity);
			addCommon(source, target);
			if (null != source.getStatus()){
				laDataEntry.setStatus(source.getStatus());
			}

			final JnjLaProductData productData = new JnjLaProductData();
			productData.setCode((source.getMaterialNumber() != null) ? source.getMaterialNumber() : source.getMaterialEntered());
			productData.setName(source.getProduct().getName());

			jnjGTOrderEntryProductPopulator.populate(source.getProduct(), productData);

			final ProductModel product = source.getProduct();

			if (product instanceof JnJLaProductModel){
				final JnJLaProductModel laProduct = (JnJLaProductModel) product;
				laDataEntry.setCatalogId(laProduct.getCatalogId());
			}else{
				laDataEntry.setCatalogId(product.getCode());
			}
			laDataEntry.setIndirectCustomer(source.getIndirectCustomer());
			target.setProduct(productData);

			laDataEntry.setIndirectCustomer(source.getIndirectCustomer());
			laDataEntry.setIndirectCustomerName(source.getIndirectCustomerName());
			laDataEntry.setIndirectPayer(source.getIndirectPayer());
			laDataEntry.setIndirectPayerName(source.getIndirectPayerName());

			laDataEntry.setFreeGoodScheduleLines(getFreeGoodsScheduledLines(source.getFreeGoodScheduleLines()));
			laDataEntry.setFreeItemsQuanity(source.getFreeItemsQuanity());
			laDataEntry.setFreeItemsAvailabilityStatus(source.getFreeItemsAvailabilityStatus());
			laDataEntry.setFreeItem(source.getFreeItem());
			laDataEntry.setFreeItemUnit(source.getFreeItemUnit());
		}
		JnjGTCoreUtil.logDebugMessage("Latam OrderEntry Populator", methodName, Logging.END_OF_METHOD,
				JnjLaOrderEntryPopulator.class);
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	private List<JnjDeliveryScheduleData> scheduleLineData(final AbstractOrderEntryModel source) {
		List<JnjDeliveryScheduleData> schedLineDataList = new ArrayList<>();
		boolean pacHiveEntries = CollectionUtils.isNotEmpty(source.getJnjPacHiveEntries());
		boolean pacHiveEnabled = configurationService.getConfiguration().getBoolean(PAC_HIVE_ENABLED, false) && pacHiveEntries;
	
		if (pacHiveEnabled && (CollectionUtils.isEmpty(source.getDeliverySchedules()) || podIsNullOrEmpty(source.getDeliverySchedules()))) {
			 return getPacHiveScheduleLines(source);
		} else {
			filterDeliveryScheduleLines(source, schedLineDataList);		    	
		}

		return schedLineDataList;
	}

	/**
	 *
	 * @param source
	 * @param scheduleLineDataList
	 * @param pacHiveEnabled
	 * @param podFlag
	 * @return
	 */
	private List<JnjDeliveryScheduleData> getPacHiveScheduleLines(AbstractOrderEntryModel source) {
		List<JnjDeliveryScheduleData> scheduleLineDataList = new ArrayList();
		if (!JnjLaCommonUtil.isDisableCountries(source) && JnjLaCommonUtil.isEnabledForSectors(source)) {
			for (final JnjPacHiveEntryModel pacHiveEntryModel : source.getJnjPacHiveEntries()) {
				final JnjDeliveryScheduleData jnjDeliveryScheduleData = new JnjDeliveryScheduleData();
				if (ObjectUtils.isNotEmpty(pacHiveEntryModel.getConvertedRecommendedDeliveryDate()) && ObjectUtils.isNotEmpty(pacHiveEntryModel.getConfirmedQuantity())) {
					jnjDeliveryScheduleData.setDeliveryDate(pacHiveEntryModel.getConvertedRecommendedDeliveryDate());
					jnjDeliveryScheduleData.setQuantity(ObjectUtils.defaultIfNull(pacHiveEntryModel.getConfirmedQuantity(), LONG_DEFAULT_VALUE).longValue());
					scheduleLineDataList.add(jnjDeliveryScheduleData);
				}
			}
		}
		return scheduleLineDataList;
	}

	/**
	 *
	 * @param source
	 * @param scheduleLineDataList
	 * @return
	 */
	private static void filterDeliveryScheduleLines(AbstractOrderEntryModel source, List<JnjDeliveryScheduleData> scheduleLineDataList) {
		if (CollectionUtils.isNotEmpty(source.getDeliverySchedules())) {
			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : source.getDeliverySchedules()) {
				final JnjDeliveryScheduleData jnjDeliveryScheduleData = new JnjDeliveryScheduleData();
				if (ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getProofOfDeliveryDate()) || ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getDeliveryDate())
						&& ObjectUtils.isNotEmpty(jnjDeliveryScheduleModel.getQty())) {
					jnjDeliveryScheduleData.setDeliveryDate(jnjDeliveryScheduleModel.getProofOfDeliveryDate() != null ? jnjDeliveryScheduleModel.getProofOfDeliveryDate() : jnjDeliveryScheduleModel.getDeliveryDate());
					jnjDeliveryScheduleData.setQuantity(jnjDeliveryScheduleModel.getQty());
					scheduleLineDataList.add(jnjDeliveryScheduleData);
				}
			}
		}
	}

	/**
	 *
	 * @param deliverySchedules
	 * @return
	 */
	private static boolean podIsNullOrEmpty(List<JnjDeliveryScheduleModel> deliverySchedules) {
		return deliverySchedules.stream().findAny().map(JnjDeliveryScheduleModel::getProofOfDeliveryDate).isEmpty();
	}

	/**
	 *
	 * @param entry
	 * @param entryQuantity
	 * @return
	 */
	public Long updateQuantity(final OrderEntryData entry, final Long entryQuantity)
	{
		Long quantity = null;

		if (sessionService != null)
		{
			final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			if (freeGoodsMap != null && entry != null && freeGoodsMap.containsKey(entry.getProduct().getCode()))
			{
				final double freeItemsQuantity = Double
						.parseDouble(freeGoodsMap.get(entry.getProduct().getCode()).getMaterialQuantity());
				final JnjOutOrderLine outOrderLine = freeGoodsMap.get(entry.getProduct().getCode());
				long orderedQuantity = 0;
				if (outOrderLine.getOrderedQuantity() != null)
				{
					final double outOrderLineQty = Double.parseDouble(outOrderLine.getOrderedQuantity());
					orderedQuantity = (long) outOrderLineQty;
				}
				final long chargedQuantity = entry.getQuantity();
				final long totalQuantity = ((long) freeItemsQuantity) + chargedQuantity;
				if (chargedQuantity == entryQuantity){
					if (totalQuantity == orderedQuantity){
						quantity = orderedQuantity;
					}else if (totalQuantity != orderedQuantity){
						quantity = chargedQuantity;
					}else{
						quantity = totalQuantity;
					}
				}else{
					quantity = entryQuantity;
				}
			}else{
				quantity = entryQuantity;
			}
		}else{
			quantity = entryQuantity;
		}
		return quantity;
	}

	private List<JnjDeliveryScheduleData> getFreeGoodsScheduledLines(final List<JnjDeliveryScheduleModel> scheduledLineModelList)
	{
		List<JnjDeliveryScheduleData> scheduledLineDataList = null;
		SimpleDateFormat commonDateFormat = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		if (scheduledLineModelList != null && scheduledLineModelList.size() > 0)
		{
			scheduledLineDataList = new ArrayList<>();
			for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : scheduledLineModelList)
			{
				final JnjDeliveryScheduleData jnjDeliveryScheduleData = new JnjDeliveryScheduleData();
				jnjDeliveryScheduleData.setLineNumber(jnjDeliveryScheduleModel.getLineNumber());
				jnjDeliveryScheduleData.setDeliveryDate(jnjDeliveryScheduleModel.getDeliveryDate());
				jnjDeliveryScheduleData.setQuantity(jnjDeliveryScheduleModel.getQty());
				jnjDeliveryScheduleData.setRoundedQuantity(jnjDeliveryScheduleModel.getRoundedQuantity());

				if (jnjDeliveryScheduleModel.getProofOfDeliveryDate() != null) {
					jnjDeliveryScheduleData.setProofOfDeliveryDate(commonDateFormat.format(
							jnjDeliveryScheduleModel.getProofOfDeliveryDate()));
				}

				if (jnjDeliveryScheduleModel.getCarrierExpectedDeliveryDate() != null) {
					jnjDeliveryScheduleData.setCarrierExpectedDeliveryDate(commonDateFormat.format(
							jnjDeliveryScheduleModel.getCarrierExpectedDeliveryDate()));
				}

				scheduledLineDataList.add(jnjDeliveryScheduleData);
			}
		}
		return scheduledLineDataList;
	}

	@Override
	protected PriceData createPrice(final AbstractOrderEntryModel orderEntry, final Double val)
	{
		final UserModel user = userService.getCurrentUser();
		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(val), user.getSessionCurrency());
	}
}
