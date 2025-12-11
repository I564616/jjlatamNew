/**
 * 
 */
package com.jnj.core.services.ordersplit.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dto.JnjGTSplitOrderInfo;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.JnjGTDropshipmentService;
import com.jnj.core.services.ordersplit.JnjGTOrderSplitService;
import com.jnj.core.util.JnJCommonUtil;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTOrderSplitService implements JnjGTOrderSplitService {
	
	private List<String> productCodesList = null;
	private Map<String, AbstractOrderEntryModel> cartEntriesMap = null;
	private static final String FORBIDDEN_TRUE_FLAG = "X";
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderSplitService.class);
	
	@Autowired
	protected JnjGTDropshipmentService jnjGTDropShipmentService;

	public void mapOrderEntries(final AbstractOrderModel abstOrderModel)
	{
		cartEntriesMap = new HashMap<String, AbstractOrderEntryModel>();
		productCodesList = new ArrayList<String>();
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

	@Override
	public Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrder(AbstractOrderModel abstOrderModel) {
		
		final Map<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap = new HashMap<JnjGTSplitOrderInfo, List<AbstractOrderEntryModel>>();
		
		List<JnjDropShipmentDetailsModel> dropShipmentDetails = new ArrayList<JnjDropShipmentDetailsModel>();
		if(abstOrderModel!=null){
			mapOrderEntries(abstOrderModel);
		}
		if(productCodesList!=null && !(productCodesList.isEmpty())){
			
			dropShipmentDetails = jnjGTDropShipmentService.getDropShipmentDetails(productCodesList);
		}
		
		
		final String customerCountry = abstOrderModel.getDeliveryAddress().getCountry().getIsocode();
		
		if(dropShipmentDetails!=null && !dropShipmentDetails.isEmpty()){
			for (final JnjDropShipmentDetailsModel tableentry : dropShipmentDetails)
			{
				final JnjGTSplitOrderInfo splitOrderInfo = new JnjGTSplitOrderInfo();
				
				splitOrderInfo.setDocorderType(tableentry.getDocumentType());
				splitOrderInfo.setSalesOrganizantion(tableentry.getSalesOrganization());
				splitOrderInfo.setShipTo(tableentry.getShipTo());
				splitOrderInfo.setSoldTo(tableentry.getSoldTo());
				if (tableentry.getDestinationCountry() != null
						&& tableentry.getDestinationCountry().equalsIgnoreCase(customerCountry))
				{
					splitOrderInfo.setForbiddenFlag(FORBIDDEN_TRUE_FLAG);
				}
				else
				{
					splitOrderInfo.setForbiddenFlag(StringUtils.EMPTY);
				}
				
				final AbstractOrderEntryModel abstractOrderEntryModel = getCartEntriesMap().get(tableentry.getMaterialId());

				// Setting OrderType and SalesOrg for SAP XML request
				abstractOrderEntryModel.setSapOrderType(tableentry.getDocumentType());
				abstractOrderEntryModel.setSalesOrg(tableentry.getSalesOrganization());

				if (splitOrderMap.containsKey(splitOrderInfo))
				{
					if(!splitOrderMap.get(splitOrderInfo).contains(abstractOrderEntryModel))
					{
						splitOrderMap.get(splitOrderInfo).add(abstractOrderEntryModel);
					}
				}
				else
				{
					final List<AbstractOrderEntryModel> list = new ArrayList<AbstractOrderEntryModel>();
					list.add(abstractOrderEntryModel);
					splitOrderMap.put(splitOrderInfo, list);
				}
				
			}
			
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getSplitOrderMap()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
						+ JnJCommonUtil.getCurrentDateTime());
			}
		}
		
		return splitOrderMap;
	}
	

	/**
	 * @return the productCodesList
	 */
	public List<String> getProductCodesList()
	{
		return productCodesList;
	}

	/**
	 * @return the cartEntriesMap
	 */
	public Map<String, AbstractOrderEntryModel> getCartEntriesMap()
	{
		return cartEntriesMap;
	}

}
