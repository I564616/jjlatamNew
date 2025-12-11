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
package com.jnj.la.core.services.ordersplit;

import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.ordersplit.JnjOrderSplitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnjLaSoldToShipToSpecialCaseModel;
import com.jnj.la.core.predicates.ordersplit.JnjDropShipmentPredicates;
import com.jnj.la.core.services.dropshipment.JnjLaCustomerAddressDropshipmentService;
import com.jnj.la.core.services.dropshipment.impl.JnjDropshipmentServiceImpl;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class JnjLatamAbstractOrderSplitService implements JnjOrderSplitService{

	private static final Class currentClass = JnjLatamAbstractOrderSplitService.class;

	@Autowired
	protected JnjDropshipmentServiceImpl jnjlaDropshipmentService;

	@Autowired
	protected JnjLaCustomerAddressDropshipmentService jnjLaCustomerAddressDropshipmentService;

	private List<String> productCodesList = null;

	private Map<String, AbstractOrderEntryModel> cartEntriesMap = null;

	public void mapOrderEntries(final AbstractOrderModel abstOrderModel)
	{
		productCodesList = new ArrayList<>();
		cartEntriesMap = new HashMap<>();
		for (final AbstractOrderEntryModel abstOrderEntModel : abstOrderModel.getEntries())
		{
			if (abstOrderEntModel != null)
			{
				final String materialId = abstOrderEntModel.getProduct().getCode();
				productCodesList.add(materialId);
				cartEntriesMap.put(materialId, abstOrderEntModel);
			}
		}
	}

	/**
	 * Selects the best fit dropshipment record for a single product
	 *
	 * @param dropshipmentDetails
	 *           The list of dropshipment for the specific product
	 * @param soldTo
	 * @param shipTo
	 */
	public JnjDropShipmentDetailsModel filterDropshipmentBySoldtoShipto(final List<JnjDropShipmentDetailsModel> dropshipmentDetails,
																		final String soldTo, final String shipTo) {

		if (CollectionUtils.isEmpty(dropshipmentDetails)) {
			return null;
		}

		Optional<JnjDropShipmentDetailsModel> filteredDropShipment;

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
			JnjDropShipmentPredicates.sameSoldToAndShipTo(soldTo, shipTo));

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
			JnjDropShipmentPredicates.sameSoldToOnlyAndEmptyShipTo(soldTo));

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
			JnjDropShipmentPredicates.sameShipToOnly(shipTo));

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
			JnjDropShipmentPredicates.emptySoldTo());

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		return null;
	}

	public JnjDropShipmentDetailsModel filterDropShipmentByDestinationCountry
			(final List<JnjDropShipmentDetailsModel> dropshipmentDetails,
			 final String destinationCountry, final String materialId){

		if (CollectionUtils.isEmpty(dropshipmentDetails)) {
			return null;
		}

		Optional<JnjDropShipmentDetailsModel> filteredDropShipment;

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
				JnjDropShipmentPredicates.sameDestinationCountry(destinationCountry));

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
				JnjDropShipmentPredicates.sameMaterialIdAndEmptyDestinationCountry(destinationCountry, materialId));

		if(filteredDropShipment.isPresent()){
			return filteredDropShipment.get();
		}

		return null;
	}
	
	
	public JnjDropShipmentDetailsModel filterDropShipmentWithoutContract(
			final List<JnjDropShipmentDetailsModel> dropshipmentDetails, final String destinationCountry,
			final String materialId) {
			JnjDropShipmentDetailsModel jnjDropShipmentDetailsModel = null;
			
			if (CollectionUtils.isNotEmpty(dropshipmentDetails)) {
				
				Optional<JnjDropShipmentDetailsModel> filteredDropShipment;
		
				filteredDropShipment = findDropShipmentModelByRule(dropshipmentDetails,
						JnjDropShipmentPredicates.sameDestinationCountry(destinationCountry));
		
				if (filteredDropShipment.isPresent()) {
					jnjDropShipmentDetailsModel = filteredDropShipment.get();
				}
			}
			
		   return jnjDropShipmentDetailsModel;
		}

	private Optional<JnjDropShipmentDetailsModel> findDropShipmentModelByRule(List<JnjDropShipmentDetailsModel> dropShipmentDetails,
																			  Predicate<JnjDropShipmentDetailsModel> predicate) {
		return dropShipmentDetails
			.stream()
			.filter(predicate)
			.findFirst();
	}

	protected List<JnjDropShipmentDetailsModel> filterDropShipmentModelList(List<JnjDropShipmentDetailsModel> dropShipmentDetails,
																			  Predicate<JnjDropShipmentDetailsModel> predicate) {
		return dropShipmentDetails
				.stream()
				.filter(predicate)
				.collect(Collectors.toList());
	}

	/**
	 * Create a list of JnjDropShipmentDetailsModel for each material id in a HashMap
	 */
	public Map<String, List<JnjDropShipmentDetailsModel>> mapDropshipmentByProduct(
			final List<JnjDropShipmentDetailsModel> dropShipmentDetails)
	{
		final Map<String, List<JnjDropShipmentDetailsModel>> dropMapByProduct = new HashMap<>();
		if (dropShipmentDetails != null && !dropShipmentDetails.isEmpty())
		{
			for (final JnjDropShipmentDetailsModel drop : dropShipmentDetails)
			{
				final String materialId = drop.getMaterialId();
				if (dropMapByProduct.containsKey(materialId))
				{
					dropMapByProduct.get(materialId).add(drop);
				}
				else
				{
					dropMapByProduct.put(materialId, new ArrayList<JnjDropShipmentDetailsModel>());
					dropMapByProduct.get(materialId).add(drop);
				}
			}
		}
		return dropMapByProduct;
	}

	/**
	 * @return the productCodesList
	 */
	public List<String> getProductCodesList()
	{
		return new ArrayList<>(productCodesList);
	}

	/**
	 * @return the cartEntriesMap
	 */
	public Map<String, AbstractOrderEntryModel> getCartEntriesMap()
	{
		return cartEntriesMap;
	}


	/**
	 * This method sets products in the proper cart in drop shipment process
	 *
	 * @param splitOrderInfo
	 *           order criteria for drop shipment split {@link JnjLatamSplitOrderInfo}
	 * @param entry
	 *           cart entries {@link AbstractOrderEntryModel}
	 * @param splitOrderMap
	 *           a cart split between PHR and MD materials
	 * @return splitOrderMap cart split updated
	 */
	public static Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> dropCartFromSector(
			final JnjLatamSplitOrderInfo splitOrderInfo, final Map.Entry<String, AbstractOrderEntryModel> entry,
			final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap)
	{
		String methodName ="dropCartFromSector()- ";
		
		if (splitOrderMap.containsKey(splitOrderInfo))
		{
			if (!splitOrderMap.get(splitOrderInfo).contains(entry.getValue()))
			{
				splitOrderMap.get(splitOrderInfo).add(entry.getValue());
			}
		}
		else
		{
			final List<AbstractOrderEntryModel> list = new ArrayList<>();
			list.add(entry.getValue());
			splitOrderMap.put(splitOrderInfo, list);
		}

		for (Map.Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> dropEntry : splitOrderMap.entrySet()) {
			JnjGTCoreUtil.logInfoMessage("Mapping order keys", methodName,
					"JnjLatamSplitOrderInfo pk: " + dropEntry.getKey() + ": Shipto: "
							+ dropEntry.getKey().getShipTo() + ": getSoldTo: " + dropEntry.getKey().getSoldTo()
							+ " :Principal: " + dropEntry.getKey().getPrincipal() + " :DocOrderType: "
							+ dropEntry.getKey().getDocOrderType() + " :SalesOrg: "
							+ dropEntry.getKey().getSalesOrganization() + " :Sector:: " + dropEntry.getKey().getSector()
							+ " :NoProductError:: " + dropEntry.getKey().getNoProductError() + " :Country: "
							+ dropEntry.getKey().getCountry() + " :destCountry: " + dropEntry.getKey().getDestCountry()
							+ " :shipper: " + dropEntry.getKey().getShipper(),
					currentClass);

			for (AbstractOrderEntryModel detail : dropEntry.getValue()) {

				JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
						"splitOrderMapValues: " + detail.getProduct().getCode(), currentClass);
			}

		}
		
		return splitOrderMap;
	}

	/**
	 * Gets the b2b billing address ID
	 * @param userModel
	 * @return soldTo address ID
	 */
	protected String getSoldTo(final UserModel userModel){
		final String methodName = "getSoldTo()";
		String soldTo = StringUtils.EMPTY;

		final B2BUnitModel b2bUnit = ((B2BCustomerModel) userModel).getDefaultB2BUnit();
		if (b2bUnit != null){
			soldTo = b2bUnit.getUid();
		}else{
			JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				"DefaultB2BUnit not defined for user " + userModel.getUid() + ". SoldTo set to empty string.",
				currentClass);
		}
		return soldTo;
	}

	/**
	 * Gets the b2b shipping address ID
	 * @param deliveryAddress
	 * @return shipTo address ID
	 */
	protected String getShipTo(final AddressModel deliveryAddress){
		final String methodName = "getShipTo()";
		String shipTo = StringUtils.EMPTY;
		if (deliveryAddress != null){
			shipTo = deliveryAddress.getJnJAddressId();
		}else{
			JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				"Delivery address not defined! ShipTo set to empty string.", currentClass);
		}
		return shipTo;
	}

	/**
	 * Check order types based on customer's soldto and shipto addresses
	 * @param abstOrderModel
	 * @return
	 */
	protected JnjLaSoldToShipToSpecialCaseModel checkJnjLaSoldToShipToSpecialCase(AbstractOrderModel abstOrderModel) {
		final String shipTo = getShipTo(abstOrderModel.getDeliveryAddress());
		final String soldTo = getSoldTo(abstOrderModel.getUser());
		JnjLaSoldToShipToSpecialCaseModel jnjSoldToShipToOrderType = null;

		if(StringUtils.isNotEmpty(soldTo) && StringUtils.isNotEmpty(shipTo)) {
			jnjSoldToShipToOrderType = jnjLaCustomerAddressDropshipmentService.getDropshipmentDetailsSpecialCase(soldTo, shipTo);
		}
		return jnjSoldToShipToOrderType;
	}
}
