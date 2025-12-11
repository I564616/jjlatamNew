package com.jnj.la.core.services.ordersplit.impl;

import com.jnj.la.core.model.JnjLaSoldToShipToSpecialCaseModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;


@Service("arOrderSplitService")
public class JnjArgentinaOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService {

	private static final Class currentClass = JnjArgentinaOrderSplitServiceImpl.class;

	private static final String AR_SALES_ORG_PHR = "AR01";

	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel){
		final String methodName = "splitOrder() for " + Jnjlab2bcoreConstants.AR_SITE_NAME;

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		mapOrderEntries(abstOrderModel);

		JnjLaSoldToShipToSpecialCaseModel jnjSoldToShipToOrderType = checkJnjLaSoldToShipToSpecialCase(abstOrderModel);

		final List<JnjDropShipmentDetailsModel> dropShipmentDetails = jnjlaDropshipmentService
				.getDropShipmentDetails(getProductCodesList(), AR_SALES_ORG_PHR);

		final Map<String, List<JnjDropShipmentDetailsModel>> dropMapByProduct = mapDropshipmentByProduct(dropShipmentDetails);

		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::::::::::: cartEntriesMap :::::::::::::::::::::: " + getCartEntriesMap(), currentClass);

		JnJProductModel product;

		for (final Map.Entry<String, AbstractOrderEntryModel> entry : getCartEntriesMap().entrySet()){
			final JnjLatamSplitOrderInfo splitOrderInfo = new JnjLatamSplitOrderInfo();
			splitOrderInfo.setCountry(Jnjlab2bcoreConstants.AR_SITE_NAME);

			product = (JnJProductModel) entry.getValue().getProduct();

			if (product.getSector().equals(Jnjb2bCoreConstants.PHR_SECTOR)){
				if (!dropMapByProduct.containsKey(entry.getKey())){
					continue;
				}

				final JnjDropShipmentDetailsModel dropShipmentForProduct = filterDropshipmentBySoldtoShipto(
						dropMapByProduct.get(entry.getKey()), Jnjb2bCoreConstants.EMPTY_STRING, Jnjb2bCoreConstants.EMPTY_STRING);

				if (dropShipmentForProduct != null){
					splitOrderInfo.setSector(Jnjb2bCoreConstants.PHR_SECTOR);
					splitOrderInfo.setDocOrderType(dropShipmentForProduct.getDocumentType());
					splitOrderInfo.setSalesOrganization(dropShipmentForProduct.getSalesOrganization());
					splitOrderInfo.setShipper(dropShipmentForProduct.getShipper());
					entry.getValue().setSapOrderType(dropShipmentForProduct.getDocumentType());
					entry.getValue().setSalesOrg(dropShipmentForProduct.getSalesOrganization());
				}
			}else{
				splitOrderInfo.setSector(Jnjb2bCoreConstants.MDD_SECTOR);
				if (jnjSoldToShipToOrderType != null){
					splitOrderInfo.setDocOrderType(jnjSoldToShipToOrderType.getJnjOrderType().getCode());
					entry.getValue().setSapOrderType(jnjSoldToShipToOrderType.getJnjOrderType().getCode());
				}else{
					splitOrderInfo.setDocOrderType(JnjOrderTypesEnum.ZOR.getCode());
					entry.getValue().setSapOrderType(JnjOrderTypesEnum.ZOR.getCode());
				}
				splitOrderInfo.setSalesOrganization(product.getSalesOrgCode());
				entry.getValue().setSalesOrg(product.getSalesOrgCode());
			}

			if (splitOrderInfo.getDocOrderType() == null){
				continue;
			}

			if (splitOrderMap.containsKey(splitOrderInfo)){
				if (!splitOrderMap.get(splitOrderInfo).contains(entry.getValue())){
					splitOrderMap.get(splitOrderInfo).add(entry.getValue());
				}
			}else{
				final List<AbstractOrderEntryModel> list = new ArrayList<>();
				list.add(entry.getValue());
				splitOrderMap.put(splitOrderInfo, list);
			}
		}

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::::::::::: splitOrderMap :::::::::::::::::::::: " + splitOrderMap, currentClass);

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);
		return splitOrderMap;
	}
}
