/**
 * 
 */
package com.jnj.facades.process.email.context;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjReturnOrderCSRProcessModel;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.data.JnjGTInvoiceOrderData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.facades.data.JnjGTOrderEntryData;
import com.jnj.facades.data.JnjGTProductData;
import com.jnj.facades.data.JnjGTReturnOrderData;
import com.jnj.facades.invoice.JnjGTInvoiceFacade;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.services.CMSSiteService;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

/**
 * @author nsinha7
 *
 */
public class JnjGTReturnOrderCSREmailContext extends CustomerEmailContext {

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjGTReturnOrderCSREmailContext.class);

	public static final String EMAIL_ATTACHMENT_LIST = "attachmentList";
	
	private static final String USER_FULL_NAME = "userFullName";
	public static final String MAIL_FOR = "emailFor";

	@Autowired
	private Converter<OrderModel, OrderData> orderConverter;

	@Autowired
	MediaService mediaService;

	@Autowired
	CMSSiteService cMSSiteService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private I18NService i18nService;
	
	@Resource(name = "jnjGTInvoiceFacade")
	protected JnjGTInvoiceFacade jnjGTInvoiceFacade;
	
	@Resource(name = "GTOrderFacade")
	protected JnjGTOrderFacade jnjGtOrderFacade;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected B2BOrderService b2boOrderService;
	/**
	 * JnJ Order Number.
	 */
	private String jnjOrderNumber;

	/**
	 * SAP based/originated Order Number.
	 */
	private String sapOrderNumber;

	/**
	 * Base URL for the BR/MX Site.
	 */
	private String baseUrl;


	/**
	 * Media URL for the Site.
	 */
	private String mediaUrl;

	/**
	 * The Order Data
	 */
	private JnjGTOrderData orderData;
	
	/**
	 * The Order Data
	 */
	private JnjGTCustomerData customerData;
	
	/**
	 * 
	 */
	private List<JnjGTReturnOrderData> returnOrderDataList;
	
	/**
	 * @return the returnOrderDataList
	 */
	public List<JnjGTReturnOrderData> getReturnOrderDataList() {
		return returnOrderDataList;
	}

	/**
	 * @param returnOrderDataList the returnOrderDataList to set
	 */
	public void setReturnOrderDataList(List<JnjGTReturnOrderData> returnOrderDataList) {
		this.returnOrderDataList = returnOrderDataList;
	}

	/**
	 * @return the mediaUrl
	 */
	public String getMediaUrl()
	{
		return mediaUrl;
	}

	/**
	 * @param mediaUrl
	 *           the mediaUrl to set
	 */
	public void setMediaUrl(final String mediaUrl)
	{
		this.mediaUrl = mediaUrl;
	}
	

	/**
	 * @return the customerData
	 */
	public JnjGTCustomerData getCustomerData() {
		return customerData;
	}

	/**
	 * @param customerData the customerData to set
	 */
	public void setCustomerData(JnjGTCustomerData customerData) {
		this.customerData = customerData;
	}
	
	/**
	 * @return the jnjOrderNumber
	 */
	public String getJnjOrderNumber()
	{
		return jnjOrderNumber;
	}

	/**
	 * @param jnjOrderNumber
	 *           the jnjOrderNumber to set
	 */
	public void setJnjOrderNumber(final String jnjOrderNumber)
	{
		this.jnjOrderNumber = jnjOrderNumber;
	}

	/**
	 * @return the sapOrderNumber
	 */
	public String getSapOrderNumber()
	{
		return sapOrderNumber;
	}

	/**
	 * @param sapOrderNumber
	 *           the sapOrderNumber to set
	 */
	public void setSapOrderNumber(final String sapOrderNumber)
	{
		this.sapOrderNumber = sapOrderNumber;
	}
	
	/**
	 * @return the baseUrl
	 */
	@Override
	public String getBaseUrl()
	{
		return baseUrl;
	}

	/**
	 * @param baseUrl
	 *           the baseUrl to set
	 */
	public void setBaseUrl(final String baseUrl)
	{
		this.baseUrl = baseUrl;
	}

	/**
	 * @return the orderData
	 */
	public JnjGTOrderData getOrderData()
	{
		return orderData;
	}

	/**
	 * @param orderData
	 *           the orderData to set
	 */
	public void setOrderData(final JnjGTOrderData orderData)
	{
		this.orderData = orderData;
	}

	@Override
	public void init(final StoreFrontCustomerProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		final String METHOD_INIT = "JnjGTReturnOrderCSREmailContext - init()";
		JnJB2bCustomerModel currentUser = null;
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjReturnOrderCSRProcessModel)
		{
			final JnjReturnOrderCSRProcessModel returnOrderCSRProcessModel = (JnjReturnOrderCSRProcessModel) businessProcessModel;

			if (null != returnOrderCSRProcessModel.getOrder())
			{
				final OrderModel order = returnOrderCSRProcessModel.getOrder();

				put(USER_FULL_NAME, order.getUser().getName());

				final OrderData populatedOrderData = orderConverter.convert(order, new JnjGTOrderData());
				setOrderData((JnjGTOrderData)populatedOrderData);
				
				final CustomerData populatedCustomerData = getCustomerConverter().convert((JnJB2bCustomerModel)returnOrderCSRProcessModel.getOrder().getUser(), new JnjGTCustomerData());
				setCustomerData((JnjGTCustomerData)populatedCustomerData);
				
				populateReturnData(populatedOrderData);

			}
			else
			{
				put(USER_FULL_NAME, returnOrderCSRProcessModel.getOrder().getUser().getName());
				setSapOrderNumber(returnOrderCSRProcessModel.getSapOrderNumber());
				setJnjOrderNumber(returnOrderCSRProcessModel.getJnjOrderNumber());
				CommonUtil.logDebugMessage("Return Order user Email", "init()",
						"Order not found. Order Data Has been set from the process in the JnjGTReturnOrderCSREmailContext!", LOGGER);
			}
			setMediaUrl(returnOrderCSRProcessModel.getMediaUrl());
			put(FROM_EMAIL, Config.getParameter("user.registration.from.address"));
			CommonUtil.logDebugMessage("Return Order CSR Notification - Email", "init()",
					Config.getParameter("user.registration.from.address"), LOGGER);
			put(FROM_DISPLAY_NAME, "Johnson & Johnson");
			populateReturnAttachments(returnOrderCSRProcessModel.getOrder());
			
			
			/** Setting the To Email address AAOL 4911**/
			final String toEmailID = returnOrderCSRProcessModel.getToEmail();
			put(EMAIL, toEmailID);
			put(MAIL_FOR,"CSR");
			put(DISPLAY_NAME,"Customer Service");
			
			setBaseUrl(returnOrderCSRProcessModel.getBaseUrl());
			
		}else{
			CommonUtil.logDebugMessage("Return Order user Email", "init()",
					"Order Data Has not been set in the JnjGTReturnOrderCSREmailContext!", LOGGER);	
		}
	}
	
	/**
	 * Method is used to populate return Order data
	 * @param populatedOrderData
	 */
	private void populateReturnData(OrderData populatedOrderData) {
		
		List<JnjGTReturnOrderData> returnDataList = new ArrayList<JnjGTReturnOrderData>();
		
		JnjGTOrderData jnjGtorderData = (JnjGTOrderData) populatedOrderData;
		
		try{
			for (final OrderEntryData entryData : populatedOrderData.getEntries()){
				if(entryData instanceof JnjGTOrderEntryData){
					JnjGTReturnOrderData returnOrderData = new JnjGTReturnOrderData();
					String invoiceNum= ((JnjGTOrderEntryData) entryData).getReturnInvNumber();
					
					if(invoiceNum!=null){
						final JnjGTInvoiceOrderData invoice = jnjGTInvoiceFacade.getInvoiceDetailsByInvoiceNumber(invoiceNum);
						
						String sapOrdeNum= invoice.getOrderNumber();
						final JnjGTOrderData parentOrderData = getInvoiceAssociatedOrderData(sapOrdeNum);
						
						returnOrderData.setSapOrderNum(sapOrdeNum);
						returnOrderData.setReturnOrderNum(populatedOrderData.getCode());
						returnOrderData.setQtyReturned(entryData.getQuantity().toString());
						returnOrderData.setReasonCode(jnjGtorderData.getReasonCode());
						returnOrderData.setPoNumber(parentOrderData.getPurchaseOrderNumber());

						for(OrderEntryData orderEntryData : parentOrderData.getEntries()){
							final JnjGTOrderEntryData jnjOrderEntryData = (JnjGTOrderEntryData) orderEntryData;

							if(jnjOrderEntryData.getProduct().getCode().equals(entryData.getProduct().getCode())){
								returnOrderData.setProductId(jnjOrderEntryData.getProduct().getCode());
								returnOrderData.setQtyOrdered(jnjOrderEntryData.getQuantity().toString());
								returnOrderData.setLotNumber(((JnjGTOrderEntryData) entryData).getLotNumber());
								JnjGTProductData productData = (JnjGTProductData) jnjOrderEntryData.getProduct();
								returnOrderData.setUom(productData.getDeliveryUnit());
							}
						}
							
						returnDataList.add(returnOrderData);


					}

				}
			}
			setReturnOrderDataList(returnDataList);
		}
		catch(Exception exc){
			LOGGER.error("Error populating return data for CSR Email");
		}
		
	}
	
	/**
	 * 
	 * @param sapOrdeNum
	 * @return
	 */
	private JnjGTOrderData getInvoiceAssociatedOrderData(String sapOrdeNum) {
		
		try{
			final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2boOrderService.getOrderForCode(sapOrdeNum);
				}
			}, userService.getAdminUser());
			
			final OrderData populatedOrderData = orderConverter.convert(orderModel, new JnjGTOrderData());
			
			return (JnjGTOrderData) populatedOrderData;
			
		}
		catch(Exception exc){
			LOGGER.error("Error getting detail of the parent order data against which the return order is placed ");
			return null;
		}
		
	}
	
	/**
	 * Populate Attachments added by user while placing Order
	 * @param order
	 */
	private void populateReturnAttachments(OrderModel order) {
		
		try{
			List<MediaModel> attachmentList = order.getReturnImages();
			List<MediaModel> attachmentListCSR = new ArrayList<MediaModel>();
			if(attachmentList!=null && !(attachmentList.isEmpty())){
				for(MediaModel media :attachmentList){
					String fileName = media.getRealFileName();
					media.setRealFileName("CSR"+"_"+fileName );
					attachmentListCSR.add(media);
				}
				put(EMAIL_ATTACHMENT_LIST, attachmentListCSR);
			}
			
		}
		catch(Exception ex){
			LOGGER.error("Error getting attachment for the order placed");
		}
		
	}


}
