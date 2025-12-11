package com.jnj.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjCanonicalDTO;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjIntegrationCronJobModel;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * @author  
 *
 */
public class JnjDeliveryScheduleUtil {

	@Autowired
	private FlexibleSearchService flexibleSearchService;
	
	@Autowired
	ModelService modelService;
	
	/*@Autowired
	JnjMasterFeedDao jnjMasterFeedDao;*/
	
	protected static final String orderEntryQuery = "SELECT  {PK} FROM {OrderEntry AS OE JOIN Order AS O ON {OE.ORDER}={O.PK}} WHERE {O:SAPORDERNUMBER}=?orderNumber ";
	protected static final String appendSapOrderLineNumber= " AND {OE:SAPORDERLINENUMBER}=?saporderlinenum ";
	
	/**
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @return ItemModel
	 */
	public ItemModel fetchOrderEntryReference(JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap){
		String query="";
		ItemModel fetchItemModel=null;
		query=orderEntryQuery;
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("orderNumber", resultSetMap.get("ORDER_NUM"));
		if(resultSetMap.get("ORD_LINE_NUM")!=null && resultSetMap.get("ORD_LINE_NUM").length()>0){
			query=query+appendSapOrderLineNumber;
		}
		queryParams.put("saporderlinenum", resultSetMap.get("ORD_LINE_NUM"));
		
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query, queryParams);
		
		final List<Object> listResult = flexibleSearchService.search(fQuery).getResult();
		if(listResult!=null && listResult.size()>0){
			
			fetchItemModel= (ItemModel)listResult.get(0);
		}
		
		return fetchItemModel;
		
		
	}
	
	
	/**
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 */
	public void updateOrderPrice(JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap){
		Map<String, String> queryParams = new HashMap<String,String>();
		queryParams.put("orderNumber", resultSetMap.get(jnjCanonicalDTO.getSourceColumn()));
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(orderEntryQuery, queryParams);
		final List<OrderEntryModel> listResult = flexibleSearchService.<OrderEntryModel>search(fQuery).getResult();
		Double price = 0.0;
		if(listResult!=null && listResult.size()>0){
			for(OrderEntryModel orderEntry:listResult){
			OrderModel  order = 	orderEntry.getOrder();
			price = order.getTotalPrice();//0.0
			order.setTotalPrice(price+orderEntry.getTotalPrice());
			updateDeliverySchedulesList(orderEntry);
			
			modelService.saveAll(order);
			
				
			}
		}
		
	}
	
	 /** this method used to update the total in the order table based on order number
	 * @param orderNumList 
	 * @param jnjCanonicalDTO 
	 * @param resultSetMap 
	 */
	public void updateOrderPrice(JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap, Set<String> orderNumList){
		for (String orderNumber : orderNumList) {
   		try{
   			Map<String, String> queryParams = new HashMap<String,String>();
      		queryParams.put("orderNumber", orderNumber);
      		//System.out.println("orderEntryQuery ****** :"+orderEntryQuery + " AND queryParams : "+queryParams);
      		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(orderEntryQuery, queryParams);
      		final List<OrderEntryModel> listResult = flexibleSearchService.<OrderEntryModel>search(fQuery).getResult();
      		Double price = 0.0;
      		if(listResult!=null && listResult.size()>0){
      			for(OrderEntryModel orderEntry:listResult){
      			OrderModel  order = 	orderEntry.getOrder();
      			price = order.getTotalPrice();//0.0
      			order.setTotalPrice(price+orderEntry.getTotalPrice());
      			updateDeliverySchedulesList(orderEntry);
      			modelService.saveAll(order);
      			}
      		}
   		}catch (Exception e) {
   			e.printStackTrace();
   			continue;
   		}
		}
	} 
	
	
	/**
	 * @param orderEntry
	 */
	public void updateDeliverySchedulesList(OrderEntryModel orderEntry){
		List<JnjDeliveryScheduleModel> scheduleLineList = new ArrayList<JnjDeliveryScheduleModel>();
		List<JnjDeliveryScheduleModel> updscheduleLineList = new ArrayList<JnjDeliveryScheduleModel>();
			JnjDeliveryScheduleModel deliverySchedule= new JnjDeliveryScheduleModel();
			deliverySchedule.setOwnerEntry(orderEntry);
			JnjDeliveryScheduleModel scheduleLine=flexibleSearchService.getModelByExample(deliverySchedule);
			if(scheduleLine!=null){
				if(orderEntry.getDeliverySchedules()!=null && orderEntry.getDeliverySchedules().size()>0 ){
					if(!orderEntry.getDeliverySchedules().contains(scheduleLine)){
						updscheduleLineList.addAll(orderEntry.getDeliverySchedules());
						updscheduleLineList.add(scheduleLine);
						orderEntry.setDeliverySchedules(updscheduleLineList);
					}
				}else{
					scheduleLineList.add(scheduleLine);
					orderEntry.setDeliverySchedules(scheduleLineList);
				}
			}
			
			modelService.saveAll(orderEntry);
		
		
	}
	
	/** This method is used to update the total value in the order table and as well as schedule delivery
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @param orderNumList 
	 */
	public void postProcessOrderPrice(JnjCanonicalDTO jnjCanonicalDTO, Map<String, String> resultSetMap, Set<String> orderNumList){
	      updateOrderPrice(jnjCanonicalDTO,resultSetMap,orderNumList);
	}
	
	
	/**  
	 * @param jnjCanonicalDTO
	 * @param resultSetMap
	 * @return
	 * This method is used to get the status for the consolidate of multiple status
	 */
	public ItemModel getOrderStatus(JnjCanonicalDTO jnjCanonicalDTO,Map<String, String> resultSetMap){
		ItemModel fetchItemModel=null;
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put(Jnjb2bCoreConstants.Order.OVER_ALL_STATUS,
				resultSetMap.get("OVERALL_STATUS") != null ? resultSetMap.get("OVERALL_STATUS") : Jnjb2bCoreConstants.Order.EMPTY_STRING);
		queryParams.put(Jnjb2bCoreConstants.Order.REJECTION_STATUS,
				resultSetMap.get("REJECTION_STATUS") != null ? resultSetMap.get("REJECTION_STATUS") : Jnjb2bCoreConstants.Order.EMPTY_STRING);
		queryParams.put(Jnjb2bCoreConstants.Order.CREDIT_STATUS,
				resultSetMap.get("CREDIT_STATUS") != null ? resultSetMap.get("CREDIT_STATUS") : Jnjb2bCoreConstants.Order.EMPTY_STRING);
		queryParams.put(Jnjb2bCoreConstants.Order.DELIVERY_STATUS,
				resultSetMap.get("DELIVERY_STATUS") != null ? resultSetMap.get("DELIVERY_STATUS") : Jnjb2bCoreConstants.Order.EMPTY_STRING);
		queryParams.put(Jnjb2bCoreConstants.Order.INVOICE_STATUS,
				resultSetMap.get("INVOICE_STATUS") != null ? resultSetMap.get("INVOICE_STATUS") : Jnjb2bCoreConstants.Order.EMPTY_STRING);
		 
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(Jnjb2bCoreConstants.Order.ORDER_STATUS_QUERY, queryParams);
		
		final List<Object> listResult = flexibleSearchService.search(fQuery).getResult();
		if( CollectionUtils.isNotEmpty(listResult)){
			fetchItemModel= (ItemModel)listResult.get(0);
		}
		return fetchItemModel;
	}

	/**
	 * @return
	 */
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 */
	public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
		this.flexibleSearchService = flexibleSearchService;
	}
}
