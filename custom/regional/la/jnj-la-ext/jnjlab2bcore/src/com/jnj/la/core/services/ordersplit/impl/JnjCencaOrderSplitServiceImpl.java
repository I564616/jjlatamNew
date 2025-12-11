package com.jnj.la.core.services.ordersplit.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.predicates.ordersplit.JnjDropShipmentPredicates;
import com.jnj.la.core.services.ordersplit.JnjLatamAbstractOrderSplitService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.la.core.enums.JnjCountryEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class JnjCencaOrderSplitServiceImpl extends JnjLatamAbstractOrderSplitService {

	private static final Class currentClass = JnjCencaOrderSplitServiceImpl.class;

	private static final String CHECKOUT_TRUE_FLAG = "X";

	protected JnjGTB2BUnitService jnjGTB2BUnitService;
	
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;


	@Override
	public Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(final AbstractOrderModel abstOrderModel){
		final String methodName = "splitOrder() for " + Jnjlab2bcoreConstants.CENCA_SITE_NAME;
		final JnJB2BUnitModel currentB2BUnit = jnjGTB2BUnitService.getCurrentB2BUnit();
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "currentB2BUnit is :" + currentB2BUnit, currentClass);
		JnJLaB2BUnitModel b2bUnit = null;
		if(currentB2BUnit instanceof JnJLaB2BUnitModel)
		{
			b2bUnit = (JnJLaB2BUnitModel)currentB2BUnit;
		}
		final String b2bUnitSalesOrg = getSalesOrg(b2bUnit);
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "b2bUnit is :" + b2bUnit.getUid()+" SalesOrg: "+b2bUnitSalesOrg, currentClass);
		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		mapOrderEntries(abstOrderModel);

		if (StringUtils.isEmpty(b2bUnitSalesOrg)){
			return null;
		}

		final List<JnjDropShipmentDetailsModel> allDropShipmentDetails = jnjlaDropshipmentService
			.getDropShipmentDetails(getProductCodesList());
		List<JnjDropShipmentDetailsModel> dropShipmentDetailsByIsoCode =
			filterDropShipmentModelList(allDropShipmentDetails, JnjDropShipmentPredicates.sameIsoCode(b2bUnitSalesOrg));
		
		if(dropShipmentDetailsByIsoCode!=null)
		{
			JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "dropShipmentDatails size "+ dropShipmentDetailsByIsoCode.size(), currentClass);
			for(JnjDropShipmentDetailsModel dropmodel:dropShipmentDetailsByIsoCode )
			{
				
				JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,	"::::Entires from Dropshimpemtntable "+ dropmodel.getMaterialId()+" Pk: "+dropmodel.getPk(), currentClass);
			}
		}
		
		final Map<String, List<JnjDropShipmentDetailsModel>> dropMapByProduct = mapDropshipmentByProduct(dropShipmentDetailsByIsoCode);

		final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<>();

		JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName,
			":::::::::::: cartEntriesMap :::::::::::::::::::::: " + getCartEntriesMap(), currentClass);

		JnJProductModel product;
		JnjDropShipmentDetailsModel defaultDrop;

		for (final Map.Entry<String, AbstractOrderEntryModel> entry : getCartEntriesMap().entrySet()){

			final JnjLatamSplitOrderInfo splitOrderInfo = new JnjLatamSplitOrderInfo();
			final String shipTo = getShipTo(abstOrderModel.getDeliveryAddress());
			final String soldTo = getSoldTo(abstOrderModel.getUser());
			final String destinationCountry = abstOrderModel.getDeliveryAddress().getCountry().getIsocode();
			product = (JnJProductModel) entry.getValue().getProduct();
			
			JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, methodName, "Destination countries from cart "
					+ destinationCountry + ":: product Sector::" + product.getSector(), currentClass);
			
			if (product.getSector().equals(Jnjb2bCoreConstants.PHR_SECTOR)){
				
				//Fix for TASK000022348839 forbidden issue
				final String contractEccNum = abstOrderModel.getContractNumber();
				if(Objects.isNull(contractEccNum))
				{
					defaultDrop = filterDropShipmentWithoutContract(
							dropMapByProduct.get(entry.getKey()), destinationCountry, entry.getKey());
				}
				else
				{
					defaultDrop = filterDropShipmentByDestinationCountry(
							dropMapByProduct.get(entry.getKey()), destinationCountry, entry.getKey());
				}
								
				if (Objects.nonNull(defaultDrop)){
					if (destinationCountry.equals(defaultDrop.getDestinationCountry())){
						splitOrderInfo.setForbiddenFlag(CHECKOUT_TRUE_FLAG);
					}
					splitOrderInfo.setDocOrderType(defaultDrop.getDocumentType());
					splitOrderInfo.setSector(product.getSector());
					entry.getValue().setSapOrderType(defaultDrop.getDocumentType());
					entry.getValue().setSalesOrg(defaultDrop.getSalesOrganization());
					splitOrderInfo.setPrincipal(defaultDrop.getPrincipal());
					splitOrderInfo.setShipper(defaultDrop.getShipper());
					splitOrderInfo.setDestCountry(destinationCountry);
				}else{
					return null;
				}
			}else if (product.getSector().equals(Jnjb2bCoreConstants.MDD_SECTOR)){
				final JnjDropShipmentDetailsModel dropShipmentForProduct = filterDropshipmentBySoldtoShipto(
					dropMapByProduct.get(entry.getKey()), soldTo, shipTo);
				JnjGTCoreUtil.logInfoMessage(
						SPLIT_SERVICE, methodName, "dropShipmentForProduct Code: "
								+ dropShipmentForProduct.getMaterialId() + " DropPK: " + dropShipmentForProduct.getPk(),
						currentClass);
				
				if (dropShipmentForProduct != null){

					splitOrderInfo.setDocOrderType(dropShipmentForProduct.getDocumentType());
					splitOrderInfo.setSector(product.getSector());
					splitOrderInfo.setShipper(dropShipmentForProduct.getShipperMD());
					entry.getValue().setSapOrderType(dropShipmentForProduct.getDocumentType());
					entry.getValue().setSalesOrg(dropShipmentForProduct.getSalesOrganization());

					// Set forbidden flag for order of type ZEX or ZOR
					if (Jnjlab2bcoreConstants.Order.OrderType.ORDET_TYPE_ZEX.equals(dropShipmentForProduct.getDocumentType())
						|| Jnjlab2bcoreConstants.Order.OrderType.ORDET_TYPE_ZOR.equals(dropShipmentForProduct.getDocumentType())){
						if (dropShipmentForProduct.getDestinationCountry() != null && dropShipmentForProduct.getDestinationCountry()
							.equalsIgnoreCase(abstOrderModel.getDeliveryAddress().getCountry().getIsocode())){
							splitOrderInfo.setForbiddenFlag(CHECKOUT_TRUE_FLAG);
						}
					}
				}else{
					continue;
				}
			}
			splitOrderMap.putAll(dropCartFromSector(splitOrderInfo, entry, splitOrderMap));
		}
		return splitOrderMap;
	}

	private String getSalesOrg(final JnJLaB2BUnitModel b2bUnit) {
		String b2bUnitSalesOrg = null;
		if (b2bUnit != null){
			final CountryModel b2bUnitCountry = b2bUnit.getCountry();
			if (b2bUnitCountry != null) {
				
				String salesOrgPrefix = null;
				final CountryModel currentBaseStoreCountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
				final String baseStoreCountryIsoCode = JnjLaCommonUtil.getIdByCountry(currentBaseStoreCountry.getIsocode());
				if(StringUtils.equalsIgnoreCase(baseStoreCountryIsoCode,JnjCountryEnum.PERU.getCountryIso()))
				{
					salesOrgPrefix = JnjCountryEnum.PERU.getCountryIso();
					JnjGTCoreUtil.logInfoMessage(SPLIT_SERVICE, "getSalesOrg", "salesOrgPrefix vlaue:::::::::::::: "+salesOrgPrefix, currentClass);
				}
				else
				{
					salesOrgPrefix = JnjLaCommonUtil.getDefaultCountry(JnjLaCommonUtil.getIdByCountry(b2bUnitCountry.getIsocode()));
				}
				
				b2bUnitSalesOrg = StringUtils.upperCase(salesOrgPrefix);
			}
		}
		return b2bUnitSalesOrg;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}
	public void setJnjGTB2BUnitService(final JnjGTB2BUnitService jnjGTB2BUnitService) {
		this.jnjGTB2BUnitService = jnjGTB2BUnitService;}
	
	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}

	public void setJnjGetCurrentDefaultB2BUnitUtil(JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil) {
		this.jnjGetCurrentDefaultB2BUnitUtil = jnjGetCurrentDefaultB2BUnitUtil;
	}
}
