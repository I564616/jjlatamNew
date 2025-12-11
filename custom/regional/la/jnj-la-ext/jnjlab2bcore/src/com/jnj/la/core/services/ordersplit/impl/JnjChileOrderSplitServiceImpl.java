package com.jnj.la.core.services.ordersplit.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

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
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;



@Service("clOrderSplitService")
public class JnjChileOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService
{

	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel)
	{
		final String methodName = "splitOrder() for " + Jnjlab2bcoreConstants.CL_SITE_NAME;
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, JnjChileOrderSplitServiceImpl.class);

		mapOrderEntries(abstOrderModel);

		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::::::::::: cartEntriesMap :::::::::::::::::::::: " + getCartEntriesMap(), JnjChileOrderSplitServiceImpl.class);
		JnJProductModel product;
		for (final Map.Entry<String, AbstractOrderEntryModel> entry : getCartEntriesMap().entrySet())
		{
			final JnjLatamSplitOrderInfo splitOrderInfo = new JnjLatamSplitOrderInfo();
			splitOrderInfo.setCountry(Jnjlab2bcoreConstants.CL_SITE_NAME);

			product = (JnJProductModel) entry.getValue().getProduct();

			if (!(product.getSector().equals(Jnjb2bCoreConstants.MDD_SECTOR)
					|| product.getSector().equals(Jnjb2bCoreConstants.PHR_SECTOR)))
			{
				continue;
			}

			if (product.getSector() == null || product.getSector().equals(Strings.EMPTY)
					|| product.getSector().equals(Jnjb2bCoreConstants.MDD_SECTOR))
			{
				splitOrderInfo.setSector(Jnjb2bCoreConstants.MDD_SECTOR);
			}
			else if (product.getSector().equals(Jnjb2bCoreConstants.PHR_SECTOR))
			{
				splitOrderInfo.setSector(Jnjb2bCoreConstants.PHR_SECTOR);
			}

			if (splitOrderInfo.getSector() == null)
			{
				continue;
			}

			splitOrderInfo.setDocOrderType(JnjOrderTypesEnum.ZOR.getCode());
			splitOrderInfo.setSalesOrganization(product.getSalesOrgCode());
			entry.getValue().setSapOrderType(JnjOrderTypesEnum.ZOR.getCode());
			entry.getValue().setSalesOrg(product.getSalesOrgCode());

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
				":::::::::::: splitOrderMap :::::::::::::::::::::: " + splitOrderMap, JnjChileOrderSplitServiceImpl.class);

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.END_OF_METHOD, JnjChileOrderSplitServiceImpl.class);
		return splitOrderMap;
	}
}
