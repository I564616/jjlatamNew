package com.jnj.facades.process.email.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjGTOrderCancelEmailProcessModel;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.util.Config;

public class JnjGTOrderCancelNotificationContext extends CustomerEmailContext{

	private static final Logger LOG = Logger.getLogger(JnjGTOrderCancelNotificationContext.class);
	private static final String USER_EMAIL_ADDRESS = "userEmailAddress";
	
	private Map<String,String> createdUserEmailDetail = new HashMap<String,String>();
	private List<JnjGTOrderEntryData> orderEntryData = new ArrayList<JnjGTOrderEntryData>();
	
	@Override
	public void init(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel, EmailPageModel emailPageModel) {
		
		final String METHOD_NAME = "init()";
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ORDER_CANCEL_EMAIL, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);
		System.out.println("In context");
		super.init(storeFrontCustomerProcessModel, emailPageModel);

		if (storeFrontCustomerProcessModel instanceof JnjGTOrderCancelEmailProcessModel)
		{
			//"Setting the Create User Email data map in the context..."
			setCreatedUserEmailDetail(((JnjGTOrderCancelEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTCreateUserEmailDetails());
			setOrderEntryData(populateOrderEntries(storeFrontCustomerProcessModel));
			put(EMAIL,((JnjGTOrderCancelEmailProcessModel) storeFrontCustomerProcessModel).getJnjGTCreateUserEmailDetails().get(USER_EMAIL_ADDRESS));
			put(FROM_EMAIL, Config.getParameter(Jnjb2bCoreConstants.Register.REGISTRATION_EMAIL_CSR_EMAIL_ADDRESS));
			put(FROM_DISPLAY_NAME, "Johnson & Johnson"); 
		}
		System.out.println("end context");
		CommonUtil.logMethodStartOrEnd(Jnjb2bCoreConstants.Logging.ORDER_CANCEL_EMAIL, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		
	}
	
	protected List<JnjGTOrderEntryData> populateOrderEntries(StoreFrontCustomerProcessModel storeFrontCustomerProcessModel)
	{
		List<JnjGTOrderEntryData> orderEntries= new ArrayList<JnjGTOrderEntryData>();
		JnjGTOrderEntryData jnjGTOrderEntryData = null;
		for(OrderEntryModel orderEntryModel : ((JnjGTOrderCancelEmailProcessModel) storeFrontCustomerProcessModel).getJnjOrderEntries())
		{
			jnjGTOrderEntryData =new JnjGTOrderEntryData();
			jnjGTOrderEntryData.setPoNumber(orderEntryModel.getOrder().getPurchaseOrderNumber());
			jnjGTOrderEntryData.setStatus(orderEntryModel.getStatus());
			jnjGTOrderEntryData.setMaterialNumber(orderEntryModel.getProduct().getCode());
			jnjGTOrderEntryData.setSapOrderlineNumber(orderEntryModel.getSapOrderlineNumber());
			jnjGTOrderEntryData.setOrderCode(orderEntryModel.getOrder().getCode());
			JnjGTProductData productData = new JnjGTProductData();
			productData.setCode(orderEntryModel.getProduct().getCode());
			productData.setDescription(orderEntryModel.getProduct().getDescription());
			jnjGTOrderEntryData.setProduct(productData);
			orderEntries.add(jnjGTOrderEntryData);
		}
		return orderEntries;
	}
	public Map<String, String> getCreatedUserEmailDetail() {
		return createdUserEmailDetail;
	}
	public void setCreatedUserEmailDetail(Map<String, String> createdUserEmailDetail) {
		this.createdUserEmailDetail = createdUserEmailDetail;
	}
	public List<JnjGTOrderEntryData> getOrderEntryData() {
		return orderEntryData;
	}
	public void setOrderEntryData(List<JnjGTOrderEntryData> orderEntryData) {
		this.orderEntryData = orderEntryData;
	}
}
