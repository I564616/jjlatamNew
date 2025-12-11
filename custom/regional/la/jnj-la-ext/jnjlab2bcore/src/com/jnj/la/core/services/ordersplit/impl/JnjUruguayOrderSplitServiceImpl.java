package com.jnj.la.core.services.ordersplit.impl;

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;
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


@Service("uyOrderSplitService")
public class JnjUruguayOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService {
	private static final Class currentClass = JnjUruguayOrderSplitServiceImpl.class;

	private static final String UY_SALES_ORG_MDD = "UY02";

	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel)
	{
		final String methodName = "splitOrder() for " + Jnjlab2bcoreConstants.UY_SITE_NAME;
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		mapOrderEntries(abstOrderModel);

		final List<JnjDropShipmentDetailsModel> dropShipmentDetails = jnjlaDropshipmentService
				.getDropShipmentDetails(getProductCodesList(), UY_SALES_ORG_MDD);

		final Map<String, List<JnjDropShipmentDetailsModel>> dropMapByProduct = mapDropshipmentByProduct(dropShipmentDetails);

		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::::::::::: cartEntriesMap :::::::::::::::::::::: " + getCartEntriesMap(), currentClass);

		JnJProductModel product;

		final String destinationCountry = getDestinationCountry(abstOrderModel.getDeliveryAddress());
		final String shipTo = getShipTo(abstOrderModel.getDeliveryAddress());
		final String soldTo = getSoldTo(abstOrderModel.getUser());

		for (final Map.Entry<String, AbstractOrderEntryModel> entry : getCartEntriesMap().entrySet())
		{
			final JnjLatamSplitOrderInfo splitOrderInfo = new JnjLatamSplitOrderInfo();
			splitOrderInfo.setCountry(Jnjlab2bcoreConstants.UY_SITE_NAME);

			splitOrderInfo.setShipTo(shipTo);
			product = (JnJProductModel) entry.getValue().getProduct();

			if (Jnjlab2bcoreConstants.UY_SITE_NAME.equals(destinationCountry))
			{
				splitOrderInfo.setDocOrderType(JnjOrderTypesEnum.ZOR.getCode());

				splitOrderInfo.setSector(product.getSector());
				entry.getValue().setSapOrderType(JnjOrderTypesEnum.ZOR.getCode());
				entry.getValue().setSalesOrg(product.getSalesOrgCode());
			}
			else
			{
				if (product.getSector().equals(Jnjb2bCoreConstants.MDD_SECTOR) && !dropMapByProduct.containsKey(entry.getKey()))
				{
					continue;
				}

				if (product.getSector() == null || product.getSector().equals(Strings.EMPTY)
						|| product.getSector().equals(Jnjb2bCoreConstants.PHR_SECTOR))
				{

					splitOrderInfo.setDocOrderType(JnjOrderTypesEnum.ZOR.getCode());
					splitOrderInfo.setSector(product.getSector());
					entry.getValue().setSapOrderType(JnjOrderTypesEnum.ZOR.getCode());
					entry.getValue().setSalesOrg(product.getSalesOrgCode());
				}
				else if (product.getSector().equals(Jnjb2bCoreConstants.MDD_SECTOR))
				{
					final JnjDropShipmentDetailsModel dropShipmentForProduct = filterDropshipmentBySoldtoShipto(
							dropMapByProduct.get(entry.getKey()), soldTo, shipTo);
					if (dropShipmentForProduct != null)
					{
						splitOrderInfo.setDocOrderType(dropShipmentForProduct.getDocumentType());
						splitOrderInfo.setSector(product.getSector());
						splitOrderInfo.setShipper(dropShipmentForProduct.getShipperMD());
						entry.getValue().setSapOrderType(dropShipmentForProduct.getDocumentType());
						entry.getValue().setSalesOrg(dropShipmentForProduct.getSalesOrganization());
					}
				}
			}

			if (splitOrderInfo.getDocOrderType() == null)
			{
				continue;
			}

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
		}

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::::::::::: splitOrderMap :::::::::::::::::::::: " + splitOrderMap, currentClass);

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.END_OF_METHOD, currentClass);
		return splitOrderMap;
	}

	private String getDestinationCountry(final AddressModel deliveryAddress)
	{
		final String methodName = "getDestinationCountry()";
		String destinationCountry = Jnjb2bCoreConstants.EMPTY_STRING;
		if (deliveryAddress != null)
		{
			final CountryModel country = deliveryAddress.getCountry();
			if (country != null)
			{
				destinationCountry = country.getIsocode();
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
						"No country set Delivery address: " + deliveryAddress.getUrl() + ". Destination country Set to empty string",
						currentClass);
			}
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
					"Delivery address not defined! Destination country set to empty string.", currentClass);
		}
		return destinationCountry;
	}
}
