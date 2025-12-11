package com.jnj.la.core.services.ordersplit.impl;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.model.JnJLaB2BUnitModel;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnjGTCoreUtil;

import de.hybris.platform.cms2.model.site.CMSSiteModel;

import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;


@Service("defaultLatamOrderSplitService")
public class DefaultLatamOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService
{
	private static final String COMMERCIAL_USER_ENABLED = "commercial.user.enabled.";

	@Autowired
	protected UserService userService;
	
	@Autowired
	private ConfigurationService configurationService;
	
	@Autowired
	private CMSSiteService cmsSiteService;

	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel)
	{
		final String methodName = "Default Latam splitOrder() ";
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, DefaultLatamOrderSplitServiceImpl.class);

		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();

		for (final AbstractOrderEntryModel orderEntryModel : abstOrderModel.getEntries())
		{
			final JnjLatamSplitOrderInfo splitOrderInfo = new JnjLatamSplitOrderInfo();
			final String salesOrg = orderEntryModel.getSalesOrg() == null ? Strings.EMPTY : orderEntryModel.getSalesOrg();
			splitOrderInfo.setSalesOrganization(salesOrg);
			
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel)abstOrderModel.getUser();
			final JnJProductModel product = (JnJProductModel) orderEntryModel.getProduct();			
			
			boolean isCommercialUser = BooleanUtils.isTrue(isCommercialUserEnabledForCurrentSite())
					&& BooleanUtils.isTrue(currentUser.getCommercialUserFlag());
			
		    boolean productSalesOrgFlag = getProductSalesOrgFlag(salesOrg);

			setDocumentType(abstOrderModel, splitOrderMap, orderEntryModel, splitOrderInfo, product, isCommercialUser,
					productSalesOrgFlag);
		}

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
				":::: splitOrderMap ::::::: " + splitOrderMap, DefaultLatamOrderSplitServiceImpl.class);

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.END_OF_METHOD, DefaultLatamOrderSplitServiceImpl.class);

		return splitOrderMap;
	}

	private void setDocumentType(AbstractOrderModel abstOrderModel, Map<JnjLatamSplitOrderInfo,
			List<AbstractOrderEntryModel>> splitOrderMap, AbstractOrderEntryModel orderEntryModel,
									JnjLatamSplitOrderInfo splitOrderInfo, JnJProductModel product,
									boolean isCommercialUser, boolean productSalesOrgFlag) {
		boolean isContractCart = abstOrderModel.getIsContractCart();
	    boolean orderTypeFlag = abstOrderModel.getOrderType() != null;
	    final JnJLaB2BUnitModel unit = (JnJLaB2BUnitModel) abstOrderModel.getUnit();
		boolean unitGovIndicatorFlag = unit.getIndicator() != null
				&& Jnjlab2bcoreConstants.GOVERNMENT.equalsIgnoreCase(unit.getIndicator());
	    boolean isBrazilStore = Jnjlab2bcoreConstants.BR_BASE_STORE.equals(abstOrderModel.getStore().getUid());
	    
		if(isBrazilStore && isCommercialUser && unitGovIndicatorFlag && isContractCart && orderTypeFlag) {
			splitOrderInfo.setDocOrderType(abstOrderModel.getOrderType().getCode());
		} else if (isCommercialUser && unitGovIndicatorFlag && isContractCart && productSalesOrgFlag) {
			splitOrderInfo.setDocOrderType(Jnjlab2bcoreConstants.SAP_ORDER_TYPE_ZOR);
		} else {
			splitOrderInfo.setDocOrderType(Jnjlab2bcoreConstants.SAP_ORDER_TYPE_ZOR);
		}

		if (StringUtils.isEmpty(product.getSector()) || Jnjb2bCoreConstants.MDD_SECTOR.equals(product.getSector()))
		{
			splitOrderInfo.setSector(Jnjb2bCoreConstants.MDD_SECTOR);
		}
		else if (Jnjb2bCoreConstants.PHR_SECTOR.equals(product.getSector()))
		{
			splitOrderInfo.setSector(Jnjb2bCoreConstants.PHR_SECTOR);
		}

		if (splitOrderMap.containsKey(splitOrderInfo) && !splitOrderMap.get(splitOrderInfo).contains(orderEntryModel))
        {
		  splitOrderMap.get(splitOrderInfo).add(orderEntryModel);
		}
		else
		{
			final List<AbstractOrderEntryModel> list = new ArrayList<>();
			list.add(orderEntryModel);
			splitOrderMap.put(splitOrderInfo, list);
		}
	}

	private boolean getProductSalesOrgFlag(final String salesOrg) {
		String productSalesOrg = configurationService.getConfiguration().getString(
						Jnjlab2bcoreConstants.CONTRACT_PRODUCT_SALES_ORG, StringUtils.EMPTY);
		List<String> productSalesOrgList = StringUtils.isNotEmpty(productSalesOrg) ? Arrays
				.asList(productSalesOrg.split(Jnjlab2bcoreConstants.CONST_COMMA)) : null;
		if (StringUtils.isNotBlank(salesOrg) && CollectionUtils.isNotEmpty(productSalesOrgList)) {
			for (String prodSalesOrg : productSalesOrgList) {
				if (salesOrg.equalsIgnoreCase(prodSalesOrg)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCommercialUserEnabledForCurrentSite() {
		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		return configurationService.getConfiguration().getBoolean(COMMERCIAL_USER_ENABLED + cmsSiteModel.getUid(),
				false);
	}
}