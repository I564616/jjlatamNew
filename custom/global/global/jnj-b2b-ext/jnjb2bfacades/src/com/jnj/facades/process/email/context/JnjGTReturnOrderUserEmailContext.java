/**
 * 
 */
package com.jnj.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hmc.model.UserProfileModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.jalo.JnJB2bCustomer;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjReturnOrderUserProcessModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.facades.data.JnjGTCustomerData;
import com.jnj.facades.data.JnjGTOrderData;
import com.jnj.services.CMSSiteService;
import com.jnj.services.MessageService;
import com.jnj.utils.CommonUtil;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
/**
 * @author nsinha7
 *
 */
public class JnjGTReturnOrderUserEmailContext extends CustomerEmailContext{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjGTReturnOrderUserEmailContext.class);

	public static final String EMAIL_ATTACHMENT_PATH = "attachmentPath";
	public static final String EMAIL_ATTACHMENT_FILE_NAME = "attachmentFileName";
	public static final String EMAIL_ATTACHMENT_LIST = "attachmentList";
	public static final String MAIL_FOR = "emailFor";

	private static final String USER_FULL_NAME = "userFullName";

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
		final String METHOD_INIT = "JnjGTReturnOrderUserEmailContext - init()";
		JnJB2bCustomerModel currentUser = null;
		
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + METHOD_INIT + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof JnjReturnOrderUserProcessModel)
		{
			final JnjReturnOrderUserProcessModel returnOrderUserProcessModel = (JnjReturnOrderUserProcessModel) businessProcessModel;

			if (null != returnOrderUserProcessModel.getOrder())
			{
				final OrderModel order = returnOrderUserProcessModel.getOrder();

				put(USER_FULL_NAME, order.getUser().getName());

				final OrderData populatedOrderData = orderConverter.convert(order, new JnjGTOrderData());
				setOrderData((JnjGTOrderData)populatedOrderData);
				
				final CustomerData populatedCustomerData = getCustomerConverter().convert((JnJB2bCustomerModel)returnOrderUserProcessModel.getOrder().getUser(), new JnjGTCustomerData());
				setCustomerData((JnjGTCustomerData)populatedCustomerData);
			}
			else
			{
				put(USER_FULL_NAME, returnOrderUserProcessModel.getOrder().getUser().getName());
				setSapOrderNumber(returnOrderUserProcessModel.getSapOrderNumber());
				setJnjOrderNumber(returnOrderUserProcessModel.getJnjOrderNumber());
				CommonUtil.logDebugMessage("Return Order user Email", "init()",
						"Order not found. Order Data Has been set from the process in the JnjGTReturnOrderUserEmailContext!", LOGGER);
			}
			setMediaUrl(returnOrderUserProcessModel.getMediaUrl());
			put(FROM_EMAIL, Config.getParameter("user.registration.from.address"));
			CommonUtil.logDebugMessage("Return Order User Notification - Email", "init()",
					Config.getParameter("user.registration.from.address"), LOGGER);
			put(FROM_DISPLAY_NAME, "Johnson & Johnson");
			populateReturnAttachments(returnOrderUserProcessModel.getOrder());
			
			/** Setting the To Email address AAOL 4911**/
			final String toEmailID = returnOrderUserProcessModel.getToEmail();
			put(EMAIL, toEmailID);
			put(MAIL_FOR,"USR");
			
			setBaseUrl(returnOrderUserProcessModel.getBaseUrl());
			
		}else{
			CommonUtil.logDebugMessage("Return Order user Email", "init()",
					"Order Data Has not been set in the JnjGTReturnOrderUserEmailContext!", LOGGER);	
		}
	}
	
	/**
	 * Populate Attachments added by user while placing Order
	 * @param order
	 */
	private void populateReturnAttachments(OrderModel order) {
		
		try{
			List<MediaModel> attachmentList = order.getReturnImages();
			List<MediaModel> attachmentListUSR = new ArrayList<MediaModel>();
			if(attachmentList!=null && !(attachmentList.isEmpty())){
				for(MediaModel media :attachmentList){
					String fileName = media.getRealFileName();
					media.setRealFileName("USR"+"_"+fileName );
					attachmentListUSR.add(media);
				}
				put(EMAIL_ATTACHMENT_LIST, attachmentListUSR);
			}
			
		}
		catch(Exception ex){
			
		}
		
	}

	


}
