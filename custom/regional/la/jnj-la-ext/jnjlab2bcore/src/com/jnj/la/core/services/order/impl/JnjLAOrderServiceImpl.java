/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.order.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dataload.mapper.PurchaseOrder;
import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.dto.FileUploadDTO;
import com.jnj.core.event.JnjLaConfirmShipmentEmailEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.core.services.impl.DefaultJnjGTOrderService;
import com.jnj.core.services.impl.JnjLaMessageServiceImpl;
import com.jnj.core.util.JnjFileUploadToSharedFolderUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.order.JnjLaSAPOrdersDao;
import com.jnj.la.core.daos.JnjLaOrderDao;
import com.jnj.la.core.daos.impl.DefaultLaJnjOrderDao;
import com.jnj.la.core.model.JnjLaSAPFailedOrdersReportEmailProcessModel;
import com.jnj.la.core.model.JnjOrderChannelModel;
import com.jnj.la.core.model.JnjOrderTypeModel;
import com.jnj.la.core.model.JnjUploadOrderModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.services.order.JnjLatamEdiOrderService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaSAPFailedOrdersReportUtil;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.Config;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

public class JnjLAOrderServiceImpl extends DefaultJnjGTOrderService implements JnjLAOrderService
{
    private static final String UPDATE_ORDER_ENTRY_STATUS = "updateOrderEntryStatus";
    private static final String UPDATE_ORDER_HEADER_STATUS = "updateOrderHeaderStatus";
    private static final String IMMEDIATE = "IMMEDIATE";
    private static final String ORDER_LINE_REASON_CODE = "C";
    private static final String SALES_ORDER_CREDIT_STATUS = "B";

	private static final Class<JnjLAOrderServiceImpl> CURRENT_CLASS = JnjLAOrderServiceImpl.class;
	private static final Logger LOG = Logger.getLogger(JnjLAOrderServiceImpl.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private JnjLaSAPOrdersDao jnjSAPOrdersDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private B2BOrderService b2bOrderService;

	@Autowired
	private JnjInvoiceService jnjInvoiceService;

	@Autowired
	private DefaultLaJnjOrderDao jnjOrderDao;

	@Autowired
	private JnjFileUploadToSharedFolderUtil jnjFileUploadToSharedFolderUtil;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private JnjLatamEdiOrderService jnjLatamEdiOrderService;

	@Autowired
	private JnjLaOrderDao jnjLaOrderDao;

	@Autowired
	private JnjLaMessageServiceImpl jnjLaMessageService;

	private List<String> errorDetails = new ArrayList<>();
	
	@Autowired
	private BusinessProcessService businessProcessService;
	
	@Autowired
	private BaseSiteService baseSiteService;

	@Override
	public OrderModel getExistingOrderByHybrisOrderNumber(final String orderNumber)
	{
		return jnjSAPOrdersDao.getExistingOrderByHybrisOrderNumber(orderNumber);
	}

	@Override
	public JnjOrderTypeModel getOrderType(final String orderTypeCode) throws BusinessException
	{
		final String methodName = "getOrderType()";
		String errorMessage;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjOrderTypeModel jnjOrderTypeModel;
		if (StringUtils.isEmpty(orderTypeCode))
		{
			errorMessage = "Received Null Order Type Code. Throwing Exception.";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CURRENT_CLASS);
			throw new BusinessException("Received Null Order Type Code. Throwing Exception");
		}

		final JnjOrderTypeModel tempJnjOrderTypeModel = modelService.create(JnjOrderTypeModel.class);
		tempJnjOrderTypeModel.setCode(orderTypeCode);
		try
		{
			jnjOrderTypeModel = flexibleSearchService.getModelByExample(tempJnjOrderTypeModel);
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{
			errorMessage = "jnjOrderTypeModel with code" + orderTypeCode + " not found in Hybris. Throwing Business Exception.";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, exception, CURRENT_CLASS);

			throw new BusinessException("jnjOrderTypeModel with code" + orderTypeCode + " not found in Hybris.");
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return jnjOrderTypeModel;

	}

	@Override
	public OrderModel getOrderDetailsForCode(final String orderCode, final boolean ignoreRestriction)
	{
		final String methodName = "Latam getOrderDetailsForCode()";
		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		OrderModel order = null;
		try
		{
			order = sessionService.executeInLocalView(new SessionExecutionBody()
			{
				@Override
				public OrderModel execute()
				{
					return b2bOrderService.getOrderForCode(orderCode);
				}
			}, userService.getAdminUser());

		}
		catch (final Exception es)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.OrderHistory.ORDER_HISTORY, methodName, "Message:", es, CURRENT_CLASS);
		}

		JnjGTCoreUtil.logInfoMessage(Jnjb2bCoreConstants.OrderHistory.ORDER_HISTORY, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return order;
	}

	@Override
	public CurrencyModel getCurrentCurrency(final String salesOrgOrder)
	{
		final String methodName = "getCurrentCurrency()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		CurrencyModel currencyModel = null;
		try
		{
			if (null != salesOrgOrder)
			{
				final String countryStoreIsoCode = (String) salesOrgOrder.subSequence(0, 2);
				final String baseStoreID = JnjLaCommonUtil.getBaseStorePrefix(countryStoreIsoCode)
						+ Jnjlab2bcoreConstants.UserRoles.BASE_STORE;
				currencyModel = baseStoreService.getBaseStoreForUid(baseStoreID).getDefaultCurrency();
			}
			else
			{
				JnjGTCoreUtil.logWarnMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"Cannot determine currency for order since its sales org is null.", CURRENT_CLASS);
			}
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
					"Error while getting default Currency]. Exception: " + e, CURRENT_CLASS);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return currencyModel;
	}

	@Override
	public JnjOrderChannelModel getOrderChannel(String orderChannelCode) throws BusinessException
	{
		final String methodName = "getOrderChannel()";

		JnjGTCoreUtil.logDebugMessage(Logging.ORDER_FLOW, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjOrderChannelModel jnjOrderChannelModel;

		if (StringUtils.isEmpty(orderChannelCode))
		{
			orderChannelCode = Jnjlab2bcoreConstants.Order.ORDER_CHANNEL_CODE_NULL;
			JnjGTCoreUtil.logDebugMessage(Logging.ORDER_FLOW, methodName, "Received Null Order Channel Code.", CURRENT_CLASS);
		}

		final JnjOrderChannelModel tempJnjOrderChannelModel = modelService.create(JnjOrderChannelModel.class);
		tempJnjOrderChannelModel.setCode(orderChannelCode);
		try
		{
			jnjOrderChannelModel = flexibleSearchService.getModelByExample(tempJnjOrderChannelModel);
		}
		catch (ModelNotFoundException | IllegalArgumentException exception)
		{

			JnjGTCoreUtil.logErrorMessage(Logging.ORDER_FLOW, methodName,
					"JnjOrderChannelModel with code" + orderChannelCode + " not found in Hybris. Throwing Business Exception.",
					exception, CURRENT_CLASS);
			throw new BusinessException("JnjOrderChannelModel with code" + orderChannelCode + " not found in Hybris.");
		}
		JnjGTCoreUtil.logDebugMessage(Logging.ORDER_FLOW, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);

		return jnjOrderChannelModel;
	}



	@Override
	public void sendOrderShipEmailNotification(final String invoiceNumber)
	{
		final String methodName = "sendOrderShipEmailNotification()";
		JnjGTCoreUtil.logInfoMessage("Logging.ORDER_FLOW", methodName,
				"Instantiating and publishing the even for Order Partially Shipped/Shipped/Confirmed status email Notification", CURRENT_CLASS);

		if (null != jnjInvoiceService.getInvoicebyCode(invoiceNumber))
		{
			final String orderNumber = jnjInvoiceService.getInvoicebyCode(invoiceNumber).getSalesOrder();
			OrderModel order = b2bOrderService.getOrderForCode(orderNumber);

			if (null == order && StringUtils.isNotEmpty(orderNumber))
			{
				final OrderModel exampleOrder = new OrderModel();

				exampleOrder.setSapOrderNumber(orderNumber);

				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Customer product number.
				try
				{
					order = flexibleSearchService.getModelByExample(exampleOrder);
				}
				catch (final ModelNotFoundException exception)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.ORDER_FLOW, methodName,
							"Order with code" + orderNumber + " not found in Hybris.", exception, CURRENT_CLASS);
				}
			}

			UserModel user = null;
			if (null != order)
			{
				user = order.getUser();
			}

			if (null != user && user instanceof JnJB2bCustomerModel)
			{
				final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) user;
				final List<String> emailPreferences = customer.getEmailPreferences();
				if (CollectionUtils.isNotEmpty(emailPreferences)
						&& emailPreferences.contains(Jnjb2bCoreConstants.EmailPreferences.PLACED_ORDER_SHIPS))
				{
					final BaseStoreModel baseStore = order.getStore();
					if (baseStore == null)
					{
						JnjGTCoreUtil.logInfoMessage("Email Notification", methodName,
								"CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
										+ " | ORDER DOES NOT HAVE ANY BASE STORE", CURRENT_CLASS);
					}
					BaseSiteModel site = order.getSite();
					if (site == null && null != baseStore)
					{
						if (CollectionUtils.isNotEmpty(baseStore.getCmsSites()))
						{
							final List<BaseSiteModel> cmsSites = new ArrayList<>(baseStore.getCmsSites());
							site = cmsSites.get(0);
						}
						else
						{
							JnjGTCoreUtil.logInfoMessage(
									"Email Notification", methodName, "CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: "
											+ order.getSapOrderNumber() + " | ORDER DOES NOT HAVE ANY BASE STORE" + baseStore.getUid(), CURRENT_CLASS);
						}
					}
					/*** Instantiate and populate <code>JnjLaConfirmShipmentEmailEvent</code> with all required data. ***/
					final JnjLaConfirmShipmentEmailEvent event = new JnjLaConfirmShipmentEmailEvent();
					event.setCustomer(customer);
					event.setBaseStore(baseStore);
					event.setSite(site);
					event.setLanguage(commonI18NService.getCurrentLanguage());
					event.setCurrency(commonI18NService.getCurrentCurrency());
					event.setSapOrderNumber(order.getSapOrderNumber());
					event.setOrderCode(order.getCode());
					event.setOrder(order);
					eventService.publishEvent(event);

					/*** Turn off the flag post sending the shipment email notification. ***/
					order.setSendOrderShipmentEmail(false);
				}
				else
				{
					JnjGTCoreUtil.logInfoMessage("Email Notification", methodName,
							"CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
									+ " | COULD NOT FIND EMAIL PREFERENCE OPTED BY THE USER: " + customer.getUid()
									+ " FOR SENDING SHIPMENT NOTIFICATION.", CURRENT_CLASS);
					order.setShipmentEmailPreference(false);
				}
				saveOrder(order);
			}
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage("Logging.ORDER_FLOW", methodName,
					"NO ORDERS HAVING UPDATED STATUS AS 'PARTIALLY_SHIPPED', 'SHIPPED', or 'COMPLETED' FOUND. NO MAILS GENERATED", CURRENT_CLASS);
		}
	}



	@Override
	public boolean saveUploadEdiOrderFileToFTP(final CommonsMultipartFile submitOrderFile)
	{
		final String methodName = "saveUploadEdiOrderFileToFTP()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		boolean isUploadSuccess;
		final FileUploadDTO fileUploadDTO = new FileUploadDTO();
		fileUploadDTO.setFile(submitOrderFile);
		fileUploadDTO.setRenameFileTo(submitOrderFile.getFileItem().getName());

		final String destainationDir = Config.getParameter(Jnjlab2bcoreConstants.FEED_FILEPATH_ROOT)
				+ Config.getParameter(Jnjlab2bcoreConstants.FEED_FILEPATH_OUTGOING)
				+ Config.getParameter(Jnjlab2bcoreConstants.UploadFile.SHARED_FOLDER_LOCATION_EDI_ORDER);
		fileUploadDTO.setFileDirInShareFolder(destainationDir);
		isUploadSuccess = jnjFileUploadToSharedFolderUtil.uploadFileToSharedFolder(fileUploadDTO, destainationDir);

		JnjGTCoreUtil.logInfoMessage("Save Uploaded EDI File to FTO", methodName,
				"File Upload to local folder status :" + isUploadSuccess, CURRENT_CLASS);
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return isUploadSuccess;
	}

	@Override
	public void createUploadOrderStatus(final Map<String, String> fileStatusMap)
	{
		final String methodName = "createUploadOrderStatus()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		for (final Entry<String, String> fileStatusEntry : fileStatusMap.entrySet())
		{
			try
			{
				final JnjUploadOrderModel jnjUploadOrderModel = modelService.create(JnjUploadOrderModel.class);
				final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();

				final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				final String currentDate = dateFormat.format(new Date());
				final long timeMiliSecond = Long.parseLong(currentDate);
				final String fileName = fileStatusEntry.getKey();
				jnjUploadOrderModel.setFileName(fileName);
				jnjUploadOrderModel.setFileNameId(fileName + "-" + timeMiliSecond);
				jnjUploadOrderModel.setDate(new Date());
				jnjUploadOrderModel.setUploadFileStatus(fileStatusEntry.getValue());
				final JnJB2BUnitModel jnjB2bUnitModel = jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit();
				setB2bUnitUploadedFile(jnjB2bUnitModel, jnjUploadOrderModel);
				jnjUploadOrderModel.setUploadByUser(currentCustomer.getName());
				modelService.save(jnjUploadOrderModel);
			}
			catch (final ModelSavingException modelSavingException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"Model not saved into Hybris data base because mandatory field is missing: ", modelSavingException, CURRENT_CLASS);
			}
			catch (final Exception exception)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"An exception occured updating jnjUploadOrderModel", exception, CURRENT_CLASS);
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
	}

	@Override
	public boolean updateUploadOrderStatus(final Map<String, List<String>> fileNameDetailsMap,
			final Map<String, String> fileStatusMap, final Map<String, List<PurchaseOrder>> purchaseOrderMap)
	{
		final String methodName = "updateUploadOrderStatus ()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		boolean savedStatus = false;
		try
		{
			final Set<String> fileNameSet = fileNameDetailsMap.keySet();

			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "PurchaseOrders size:" + fileNameSet.size(), CURRENT_CLASS);

			for (final String fileName : fileNameSet)
			{
				final int uniqueIdentifier = 0;
				final List<PurchaseOrder> purchaseOrderList = purchaseOrderMap.get(fileName);

				final JnjUploadOrderModel jnjUploadOrderModel = getJnjUploadOrderModelTemplate(fileName);

				if(jnjUploadOrderModel!=null)
				{
					if (CollectionUtils.isEmpty(purchaseOrderList))
					{
						savedStatus = saveUploadOrderWithoutTrackingIds(jnjUploadOrderModel, fileStatusMap, fileNameDetailsMap, fileName);
					}
					else
					{
						savedStatus = getAndSaveTrackingIds(jnjUploadOrderModel, purchaseOrderList, fileNameDetailsMap, fileStatusMap,
								fileName, uniqueIdentifier);
					}
				}
			}
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"An exception occurred while trying to upload JnjUploadOrder table.", exception, CURRENT_CLASS);
			savedStatus = false;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return savedStatus;
	}


	/**
	 *
	 */
	private boolean saveUploadOrderWithoutTrackingIds(final JnjUploadOrderModel jnjUploadOrderModel,
			final Map<String, String> fileStatusMap, final Map<String, List<String>> fileNameDetailsMap, final String fileName)
	{
		final String methodName = "saveUploadOrderWithoutTrackingIds()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		if (jnjUploadOrderModel != null)
		{
			final List<String> errorsDetails = fileNameDetailsMap.get(fileName);
			final String fileStatus = fileStatusMap.get(fileName);
			jnjUploadOrderModel.setUploadFileStatus(fileStatus);
			jnjUploadOrderModel.setErrorMessageList(errorsDetails);
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Saving file: " + fileName + " with processing status: " + fileStatus + " and error messageList: " + errorsDetails, CURRENT_CLASS);

			return saveUploadOrderModel(jnjUploadOrderModel);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);

		return false;
	}

	private boolean getAndSaveTrackingIds(final JnjUploadOrderModel jnjUploadOrderModel,
			final List<PurchaseOrder> purchaseOrderList, final Map<String, List<String>> fileNameDetailsMap,
			final Map<String, String> fileStatusMap, final String fileName, final int uniqueIdentifier)
	{
		final String methodName = "getAndSaveTrackingIds()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		boolean result = false;
		if (jnjUploadOrderModel == null)
		{
			return result;
		}

		int fileNumber = uniqueIdentifier;
		for (final PurchaseOrder purchaseOrder : purchaseOrderList)
		{
			fileNumber++;
			updateB2bUnitUploadedFile(purchaseOrder, jnjUploadOrderModel);
			final String trackingId = getTrackingId(purchaseOrder);
			final List<String> errorsDetails = fileNameDetailsMap.get(fileName);
			final String fileStatus = fileStatusMap.get(fileName);
			final String fileNameId = getFileNameId(fileName, fileNumber);

			jnjUploadOrderModel.setFileNameId(fileNameId);
			jnjUploadOrderModel.setUploadFileStatus(fileStatus);
			jnjUploadOrderModel.setErrorMessageList(errorsDetails);
			jnjUploadOrderModel.setTrackingId(trackingId);

			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Saving file: " + fileName + " with processing status: " + fileStatus + " trackingId: " + trackingId
							+ " and error messageList: " + errorsDetails, CURRENT_CLASS);

			if (fileNumber > 1)
			{
				result = createAndSaveNewUploadOrderModel(jnjUploadOrderModel);
			}
			else
			{
				result = saveUploadOrderModel(jnjUploadOrderModel);
			}

		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);

		return result;
	}

	private boolean createAndSaveNewUploadOrderModel(final JnjUploadOrderModel jnjUploadOrderModel)
	{
		final String methodName = "createAndSaveNewUploadOrderModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
				"Creating new entry in jnjUploadOrder for the file: " + jnjUploadOrderModel.getFileName(), CURRENT_CLASS);
		final JnjUploadOrderModel newJnjUploadOrderModel = modelService.create(JnjUploadOrderModel.class);
		newJnjUploadOrderModel.setB2bUnitId(jnjUploadOrderModel.getB2bUnitId());
		newJnjUploadOrderModel.setDate(jnjUploadOrderModel.getDate());
		newJnjUploadOrderModel.setErrorMessageList(jnjUploadOrderModel.getErrorMessageList());
		newJnjUploadOrderModel.setFileName(jnjUploadOrderModel.getFileName());
		newJnjUploadOrderModel.setFileNameId(jnjUploadOrderModel.getFileNameId());
		newJnjUploadOrderModel.setTrackingId(jnjUploadOrderModel.getTrackingId());
		newJnjUploadOrderModel.setUploadByCustomer(jnjUploadOrderModel.getUploadByCustomer());
		newJnjUploadOrderModel.setUploadByUser(jnjUploadOrderModel.getUploadByUser());
		newJnjUploadOrderModel.setUploadFileStatus(jnjUploadOrderModel.getUploadFileStatus());

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return saveUploadOrderModel(newJnjUploadOrderModel);
	}

	private JnjUploadOrderModel getJnjUploadOrderModelTemplate(final String fileName)
	{
		final String methodName = "getJnjUploadOrderModelTemplate()";

		JnjUploadOrderModel jnjUploadOrderModel = null;
		try
		{
			final JnjUploadOrderModel jnjUploadOrderModelTemplate = modelService.create(JnjUploadOrderModel.class);
			jnjUploadOrderModelTemplate.setFileName(fileName);
			jnjUploadOrderModelTemplate.setUploadFileStatus(Jnjlab2bcoreConstants.UploadOrder.RECEIVED_STATUS);
			jnjUploadOrderModel = flexibleSearchService.getModelByExample(jnjUploadOrderModelTemplate);

		}
		catch (final ModelNotFoundException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "An exception occurred.", exception, CURRENT_CLASS);
		}
		return jnjUploadOrderModel;
	}

	@Override
	public List<File> getUploadOrderFiles()
	{
		final String methodName = "getUploadOrderFiles";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		final String destainationDir = Config.getParameter(Jnjlab2bcoreConstants.FEED_FILEPATH_ROOT)
				+ Config.getParameter(Jnjlab2bcoreConstants.FEED_FILEPATH_OUTGOING)
				+ Config.getParameter(Jnjlab2bcoreConstants.UploadFile.SHARED_FOLDER_LOCATION_EDI_ORDER);

		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Source location fo the file: "+destainationDir, CURRENT_CLASS);
		List<File> filesInFolder = null;
		try (final Stream<Path> walk = Files.walk(Path.of(destainationDir))) {

            filesInFolder = walk.filter(Files::isRegularFile).map(Path::toFile)
					.collect(Collectors.toList());
		}
		catch (final IOException exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_FILES_INTERFACE, methodName,
					"An error occured while loading the EDI files from: " + destainationDir, exception, CURRENT_CLASS);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return filesInFolder;
	}

	@Override
	public void processEdiFiles(final List<File> uploadeFilesList)
	{
		final String methodName = "processEdiFiles()";
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		List<PurchaseOrder> purchaseOrderList = null;
		final PurchaseOrder purchaseOrder = null;
		String fileName = null;
		String fileStatus;
		final List<File> fileListForSAP = new ArrayList<>();
		final Map<String, List<String>> fileNameDetailsMap = new HashMap<>();
		final Map<String, List<PurchaseOrder>> purchaseOrderMap = new HashMap<>();
		final Map<String, String> fileStatusMap = new HashMap<>();

		try
		{
			boolean created;
			for (final File submitOrderFile : uploadeFilesList)
			{
				purchaseOrderList = new ArrayList<>();
				fileName = submitOrderFile.getName();
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "File name:" + fileName, CURRENT_CLASS);
				final String[] fileNameExtensionSplit = fileName.split(Jnjb2bFacadesConstants.Order.FILE_EXTENSION_SPILTTER);

				// check the string array as null and its length should be greater than zero.
				if (null == fileNameExtensionSplit || fileNameExtensionSplit.length <= Jnjb2bFacadesConstants.Order.ZERO_AS_INT
						&& StringUtils.isNotEmpty(fileName))
				{
					continue;
				}
				final InputStream inputStream = new FileInputStream(submitOrderFile);
				final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				// Get the first element of the file so that we check in this class if the the file is empty or not.
				final BufferedReader bufferReader = new BufferedReader(inputStreamReader);
				final String dataRow = bufferReader.readLine();
				bufferReader.close();
				inputStreamReader.close();
				inputStream.close();
				final String fileExtension = fileNameExtensionSplit[fileNameExtensionSplit.length
						- Jnjb2bFacadesConstants.Order.ONE_AS_INT];
				String uploadStatus;
				if (StringUtils.isNotEmpty(fileExtension) && fileExtension.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.TXT_STRING)
						&& StringUtils.isNotEmpty(dataRow))
				{

					uploadStatus = processTxtFormatFile(submitOrderFile, dataRow, fileNameDetailsMap, fileStatusMap, purchaseOrderList,
							purchaseOrderMap, fileListForSAP);
				}
				else if (StringUtils.isNotEmpty(fileExtension)
						&& fileExtension.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.XML_STRING))
				{
					uploadStatus = processXmlFormatFile(submitOrderFile, dataRow, fileNameDetailsMap, fileStatusMap, purchaseOrderList,
							purchaseOrderMap, fileListForSAP);

				}
				// if the file extension is ean then enter in the else if block
				else
				{
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Given File is from EAN format", CURRENT_CLASS);
					uploadStatus = processEanFormatFile(submitOrderFile, fileNameDetailsMap, fileStatusMap, purchaseOrderList,
							purchaseOrderMap, fileListForSAP);
				}
				
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"File Name: " + fileName + " Final Upload file status is: "+uploadStatus, CURRENT_CLASS);
				
				if (uploadStatus != null && uploadStatus.equals(Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS)
						&& JnJXMLFilePicker.zipAndMoveFile(submitOrderFile, Jnjlab2bcoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
								Jnjlab2bcoreConstants.FEEDS_ERROR_FOLDER_NAME, false))
				{
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"Uploaded file: " + fileName + " zipped and moved to ERROR. Please review this file!", CURRENT_CLASS);
				}
				else if (JnJXMLFilePicker.zipAndMoveFile(submitOrderFile, Jnjlab2bcoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
						Jnjlab2bcoreConstants.FEEDS_ARCHIVE_FOLDER_NAME, false))
				{
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"Uploaded file: " + fileName + " zipped and moved to ARCHIVE successfully!", CURRENT_CLASS);
				}
				
			}
			// if the fileListForSAP list is not empty then move the corresponding file in archive folder or error folder.
			if (!fileListForSAP.isEmpty())
			{
				created = jnjLatamEdiOrderService.sftpCallAndMoveFileToZipFolder(fileListForSAP, errorDetails);
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"Created Flag value after getting response from the sftpCallAndMoveFileToZipFolder method is " + created, CURRENT_CLASS);
				if (!created)
				{
					populateFileStatusError(fileNameDetailsMap, fileStatusMap, purchaseOrderList, purchaseOrderMap, errorDetails,
							purchaseOrder);

				}
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"File list for SAP is empty " + fileListForSAP, CURRENT_CLASS);
			}
		}
		catch (final IOException inputOutputException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Input Output exception occured.",
					inputOutputException, CURRENT_CLASS);
			populateErrorDetailsList(errorDetails);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			fileStatusMap.put(fileName, fileStatus);
			purchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, purchaseOrderList);
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Illegal Argument exception occured.",
					illegalArgumentException, CURRENT_CLASS);
			populateErrorDetailsList(errorDetails);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			fileStatusMap.put(fileName, fileStatus);
			purchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, purchaseOrderList);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured.", exception, CURRENT_CLASS);
			populateErrorDetailsList(errorDetails);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			fileStatusMap.put(fileName, fileStatus);
			purchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, purchaseOrderList);
		}

		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Updating Upload Order Status.", CURRENT_CLASS);
		updateUploadOrderStatus(fileNameDetailsMap, fileStatusMap, purchaseOrderMap);

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	private static void setB2bUnitUploadedFile(final JnJB2BUnitModel jnjB2bUnitModel, final JnjUploadOrderModel jnjUploadOrderModel)
	{
		final String methodName = "setB2bUnitUploadedFile()";
		if (jnjB2bUnitModel != null)
		{
			jnjUploadOrderModel.setB2bUnitId(jnjB2bUnitModel);
			jnjUploadOrderModel.setUploadByCustomer(jnjB2bUnitModel.getName());
		}
		else
		{
			JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName, "File b2b unit is null", CURRENT_CLASS);
		}
	}

	private String getFileNameId(final String fileName, final int uniqueIdentifier)
	{
		final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		final String currentDate = dateFormat.format(new Date());
		long timeMiliSecond = Long.parseLong(currentDate);
		timeMiliSecond = timeMiliSecond + uniqueIdentifier;

		return fileName + "-" + timeMiliSecond;
	}

	private String getTrackingId(final PurchaseOrder purchaseOrder)
	{
		if (null != purchaseOrder && null != purchaseOrder.getHeader() && null != purchaseOrder.getHeader().getpONumber())
		{
			return purchaseOrder.getHeader().getpONumber();
		}
		return null;
	}

	private void updateB2bUnitUploadedFile(final PurchaseOrder purchaseOrder, final JnjUploadOrderModel jnjUploadOrderModel)
	{
		final String methodName = "updateB2bUnitUploadedFile()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		String soldToNumber;
		if (null != purchaseOrder && null != purchaseOrder.getHeader() && null != purchaseOrder.getHeader().getSoldToNumber())
		{
			soldToNumber = purchaseOrder.getHeader().getSoldToNumber();
			final JnJB2BUnitModel jnjB2bUnitModel = jnjOrderDao.fetchAllSoldToNumberForFile(soldToNumber);
			setB2bUnitUploadedFile(jnjB2bUnitModel, jnjUploadOrderModel);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	/**
	 * @throws FileNotFoundException
	 *
	 */
	private String processEanFormatFile(final File submitOrderFile, final Map<String, List<String>> fileNameDetailsMap,
			final Map<String, String> fileStatusMap, final List<PurchaseOrder> puchaseOrderList,
			final Map<String, List<PurchaseOrder>> purchaseOrderMap, final List<File> fileListForSAP) throws FileNotFoundException
	{
		final String methodName = "processEanFormatFile()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);

		final PurchaseOrder purchaseOrder = new PurchaseOrder();
		final InputStream inputStream = new FileInputStream(submitOrderFile);
		final String fileName = submitOrderFile.getName();
		final String fileHash = JnjlatamOrderUtil.getFileSha512(new FileInputStream(submitOrderFile), fileName);
		final List<JnjUploadOrderSHAModel> shaModels = jnjOrderDao.getUploadOrderSHADetails(fileHash);
		errorDetails = new ArrayList<>();
		String fileStatus;
		fileStatus = jnjLatamEdiOrderService.createOrderFromEanLayOut(inputStream, fileListForSAP, errorDetails, purchaseOrder,
				puchaseOrderList, shaModels);
		fileNameDetailsMap.put(fileName, errorDetails);
		fileStatusMap.put(fileName, fileStatus);
		purchaseOrderMap.put(fileName, puchaseOrderList);

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return fileStatus;
	}

	/**
	 * @throws XMLStreamException
	 * @throws IOException
	 *
	 */
	private String processXmlFormatFile(final File submitOrderFile, final String dataRow,
			final Map<String, List<String>> fileNameDetailsMap, final Map<String, String> fileStatusMap,
			final List<PurchaseOrder> puchaseOrderList, final Map<String, List<PurchaseOrder>> purchaseOrderMap,
			final List<File> fileListForSAP) throws XMLStreamException, IOException
	{
		final String methodName = "processTxtFormatFile()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		final PurchaseOrder purchaseOrder = new PurchaseOrder();
		final InputStream inputStreamFile = new FileInputStream(submitOrderFile);
		final InputStream inputStream = new FileInputStream(submitOrderFile);
		final String fileName = submitOrderFile.getName();
		final String fileHash = JnjlatamOrderUtil.getFileSha512(new FileInputStream(submitOrderFile), fileName);
		final List<JnjUploadOrderSHAModel> shaModels = jnjOrderDao.getUploadOrderSHADetails(fileHash);
		errorDetails = new ArrayList<>();
		String fileStatus = null;

		// Create the instance of XML Input Factory.
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		final XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStreamFile);

		final String xmlFileType = getXmlFileType(xmlStreamReader);
		xmlStreamReader.close();
		inputStreamFile.close();
		// Enter in the if block for the Albert Einstein File
		if (StringUtils.isNotEmpty(dataRow) && xmlFileType != null
				&& xmlFileType.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.PEDIDO))
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Given File is from Albert Einstien format", CURRENT_CLASS);
			fileStatus = jnjLatamEdiOrderService.createOrderFromAlbertFile(inputStream, fileListForSAP, errorDetails, purchaseOrder,
					shaModels);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatusMap.put(fileName, fileStatus);
			puchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, puchaseOrderList);
		}
		//Enter in the else block for the Alianca File
		else if (StringUtils.isNotEmpty(dataRow) && xmlFileType != null
				&& xmlFileType.equalsIgnoreCase(Jnjb2bFacadesConstants.Order.PROCESS_PURCHASE_ORDER))
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Given File is from Alianca format", CURRENT_CLASS);
			fileStatus = jnjLatamEdiOrderService.createOrderFromAliancaFile(inputStream, fileListForSAP, errorDetails, purchaseOrder,
					shaModels);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatusMap.put(fileName, fileStatus);
			puchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, puchaseOrderList);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, "File is Empty", CURRENT_CLASS);
			errorDetails.add("order.home.fileFormatInvalid");
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatusMap.put(fileName, Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS);
		}
		inputStream.close();
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return fileStatus;
	}

	/**
	 * @throws FileNotFoundException
	 *
	 */
	private String processTxtFormatFile(final File submitOrderFile, final String dataRow,
			final Map<String, List<String>> fileNameDetailsMap, final Map<String, String> fileStatusMap,
			final List<PurchaseOrder> puchaseOrderList, final Map<String, List<PurchaseOrder>> purchaseOrderMap,
			final List<File> fileListForSAP) throws FileNotFoundException
	{
		final String methodName = "processTxtFormatFile()";
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "[ Starting processing TXT file ]", CURRENT_CLASS);

		final PurchaseOrder purchaseOrder = new PurchaseOrder();
		final InputStream inputStream = new FileInputStream(submitOrderFile);
		final String fileName = submitOrderFile.getName();
		final String fileHash = JnjlatamOrderUtil.getFileSha512(new FileInputStream(submitOrderFile), fileName);
		final List<JnjUploadOrderSHAModel> shaModels = jnjOrderDao.getUploadOrderSHADetails(fileHash);
		errorDetails = new ArrayList<>();

		String fileStatus;

		if (dataRow.contains(Jnjlab2bcoreConstants.SubmitEdiOrderFiles.SAO_LUIZ_EXTENSION))
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Given File is from SAO LUIZ format", CURRENT_CLASS);
			fileStatus = jnjLatamEdiOrderService.createOrderFromSaoLuiz(inputStream, fileListForSAP, errorDetails, purchaseOrder,
					shaModels);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatusMap.put(fileName, fileStatus);
			puchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(fileName, puchaseOrderList);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Given File is from TXT format but not SAO Luiz so it will consider as EAN format", CURRENT_CLASS);
			fileStatus = jnjLatamEdiOrderService.createOrderFromEanLayOut(inputStream, fileListForSAP, errorDetails, purchaseOrder,
					puchaseOrderList, shaModels);
			fileNameDetailsMap.put(fileName, errorDetails);
			fileStatusMap.put(fileName, fileStatus);
			purchaseOrderMap.put(fileName, puchaseOrderList);
		}
		return fileStatus;
	}

	private static void populateFileStatusError(final Map<String, List<String>> fileNameDetailsMap,
			final Map<String, String> fileStatusMap, final List<PurchaseOrder> puchaseOrderList,
			final Map<String, List<PurchaseOrder>> purchaseOrderMap, final List<String> errorDetails,
			final PurchaseOrder purchaseOrder)
	{
		final String methodName = "populateFileStatusError()";
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		for (final Entry<String, List<String>> entry : fileNameDetailsMap.entrySet())
		{
			final String finalFileName = entry.getKey();
			final List<String> finalErrorList = fileNameDetailsMap.get(finalFileName);
			if (finalErrorList.isEmpty())
			{
				fileNameDetailsMap.put(finalFileName, errorDetails);
			}
			else
			{
				fileNameDetailsMap.put(finalFileName, finalErrorList);
			}
			final String fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			fileStatusMap.put(finalFileName, fileStatus);
			puchaseOrderList.add(purchaseOrder);
			purchaseOrderMap.put(finalFileName, puchaseOrderList);
		}
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	private static String getXmlFileType(final XMLStreamReader xmlStreamReader) throws XMLStreamException
	{
		final String methodName = "getXmlFileType()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		String xmlFileType = null;
		int eventType = xmlStreamReader.getEventType();
		while (xmlStreamReader.hasNext())
		{
			if (eventType == XMLStreamConstants.START_ELEMENT)
			{
				// check the Pedido tag in XML
				if (Jnjlab2bcoreConstants.Order.PEDIDO.equals(xmlStreamReader.getLocalName()))
				{
					xmlFileType = Jnjb2bFacadesConstants.Order.PEDIDO;
				}
				else if (Jnjlab2bcoreConstants.Order.PROCESS_PURCHASE_ORDER.equals(xmlStreamReader.getLocalName()))
				{
					xmlFileType = Jnjb2bFacadesConstants.Order.PROCESS_PURCHASE_ORDER;
				}
			}
			// To get the next element in xml stream reader.
			eventType = xmlStreamReader.next();
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		return xmlFileType;
	}

	/**
	 * @param jnjUploadOrderModel
	 * @return savedStatus
	 */
	private boolean saveUploadOrderModel(final JnjUploadOrderModel jnjUploadOrderModel)
	{
		final String methodName = "saveUploadOrderModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);

		boolean savedStatus = false;
		try
		{
			modelService.saveAll(jnjUploadOrderModel);
			savedStatus = true;
			JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, "AFTER SUCCESSFULLY SAVE THE JNJ Upload Order Model", CURRENT_CLASS);
		}
		catch (final ModelSavingException modelSavingException)
		{
			JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Upload Order Model not saved into Hybris data base", CURRENT_CLASS);
			throw modelSavingException;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
		return savedStatus;
	}

	@Override
	protected void populateErrorDetailsList(final List<String> errorDetails)
	{
		final String methodName = "populateErrorDetailsList()";

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, CURRENT_CLASS);
		if (null != errorDetails)
		{
			errorDetails.add(jnjLaMessageService.getMessageFromImpex("order.home.generalErrorMessage", null));
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, CURRENT_CLASS);
	}

	@Override
	public SearchPageData<JnjUploadOrderModel> getUploadOrderData(final String sortflag, final String filterFlag,
			final PageableData pageableData)
	{
		return jnjOrderDao.getUploadOrderData(sortflag, filterFlag, pageableData);

	}

	@Override
	public JnjUploadOrderModel getUploadOrderDataDetails(final String uploadedOrderId)
	{
		return jnjOrderDao.getUploadOrderDataDetails(uploadedOrderId);
	}

	@Override
	public List<JnjDropShipmentDetailsModel> getShippingDetails(final List<String> materialIds)
	{
		return jnjLaOrderDao.getShippingDetails(materialIds);
	}

	@Override
	public Date processOrderEntryStatus(final Date lastUpdatedDate) {
	    final List<OrderEntryModel> updatedEntries = jnjLaOrderDao.getOrderEntryModelByModifiedTime(lastUpdatedDate);
        if (CollectionUtils.isEmpty(updatedEntries)) {
        	String logMessage = String.format("No order entries updated since last execution (%s)", lastUpdatedDate);
            JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_STATUS, UPDATE_ORDER_ENTRY_STATUS, logMessage, CURRENT_CLASS);
            return null;
        } else {
            String logMessage = String.format("Found %s pending orders entries since last execution (%s)", updatedEntries.size(), lastUpdatedDate);
            JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_STATUS, UPDATE_ORDER_ENTRY_STATUS, logMessage, CURRENT_CLASS);
            return updateOrderEntriesStatus(updatedEntries);
        }
    }

	@Override
	public Date processOrdersHeaderStatus(final Date lastUpdatedDate) {
		final List<OrderModel> updatedOrders = jnjLaOrderDao.getOrderModelByModifiedTimeInEntries(lastUpdatedDate);
		if (CollectionUtils.isEmpty(updatedOrders)) {
            String logMessage = String.format("No orders updated since last execution (%s)", lastUpdatedDate);
            JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_HEADER_STATUS, UPDATE_ORDER_HEADER_STATUS, logMessage, CURRENT_CLASS);
			return null;
		} else {
            String logMessage = String.format("Found %s pending orders since last execution (%s)", updatedOrders.size(), lastUpdatedDate);
            JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_HEADER_STATUS, UPDATE_ORDER_HEADER_STATUS, logMessage, CURRENT_CLASS);
			return updateOrdersHeaderStatus(updatedOrders);
		}
	}

	private Date updateOrdersHeaderStatus(final List<OrderModel> pendingOrders) {
		for (final OrderModel order : pendingOrders) {
			updateOrderHeaderStatus(order);
		}

		return getLatestModifiedTime(pendingOrders);
	}

	public void updateOrderHeaderStatus(OrderModel order) {
		JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_HEADER_STATUS, UPDATE_ORDER_HEADER_STATUS, getOrderHeaderStatusCombinationMessage(order), CURRENT_CLASS);
		OrderStatus status = calculateOrderHeaderStatus(order);
		status = getOrderStatusForPOD(order, status);
		if (status == null) {
			JnjGTCoreUtil.logErrorMessage(Logging.UPDATE_ORDER_HEADER_STATUS, UPDATE_ORDER_HEADER_STATUS, "No header status found for the given combination, Order " + order.getCode() + " cannot be saved.", CURRENT_CLASS);
		} else {
			order.setStatus(status);
			modelService.save(order);
			JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_HEADER_STATUS, UPDATE_ORDER_HEADER_STATUS, "Order " + order.getCode() + " successfully saved with status " + status.getCode() , CURRENT_CLASS);
		}
	}

	private static Date getLatestModifiedTime(final List<OrderModel> orders){
        Date higherDate = null;

        for (final OrderModel order : orders) {
            Date date = getLatestModifiedTimeInEntries(new ArrayList<>(order.getEntries()));
			if (date != null && (higherDate == null || date.compareTo(higherDate) > 0)) {
				higherDate = date;
			}
        }

        return higherDate;
    }

    private static Date getLatestModifiedTimeInEntries(final List<ItemModel> items){
        return items.stream().map(ItemModel::getModifiedtime).max(Date::compareTo).orElse(null);
    }

	private Date updateOrderEntriesStatus(final List<OrderEntryModel> updatedEntries) {
        for (final OrderEntryModel entry : updatedEntries) {
            updateOrderEntryStatus(entry);
        }

        return getLatestModifiedTimeInEntries(new ArrayList<>(updatedEntries));
    }

    public void updateOrderEntryStatus(AbstractOrderEntryModel entry) {
        final String orderCode = entry.getOrder().getCode();
        final String productCode = entry.getProduct().getCode();
        final String entryLineMessage = getEntryLineCombinationMessage(entry, orderCode, productCode);

        JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_STATUS, UPDATE_ORDER_ENTRY_STATUS, entryLineMessage, CURRENT_CLASS);
        
        final OrderModel order = (OrderModel)entry.getOrder();
        OrderEntryStatus newStatus = null;
        
        if (entry.getReasonForRejection() != null && ORDER_LINE_REASON_CODE.equals(entry.getReasonForRejection()))
        {
        	newStatus=OrderEntryStatus.CANCELLED;
        }
        else if(order.getSalesOrderCreditStatus() != null && SALES_ORDER_CREDIT_STATUS.equals(order.getSalesOrderCreditStatus()))
        {            
            newStatus=OrderEntryStatus.UNDER_ANALYSIS;
        }
        else 
        {
	        newStatus = jnjLaOrderDao.getOrderEntryStatusUsingPermutationMatrix(entry);
        }
       
        newStatus = getEntryStatusForPOD(entry, newStatus);
        if (newStatus == null) 
        {
            String message = "No PermMatrixOrderLine result found for the given combination, Entry (" + productCode + ") for Order (" + orderCode + ") cannot be saved.";
      	    JnjGTCoreUtil.logErrorMessage(Logging.UPDATE_ORDER_STATUS, UPDATE_ORDER_ENTRY_STATUS, message, CURRENT_CLASS);
        }
        else if(!Objects.equals(entry.getQuantityStatus(), newStatus)){
        	entry.setPreviousStatus(entry.getQuantityStatus());
            entry.setQuantityStatus(newStatus);
            entry.setStatus(newStatus.getCode());
			entry.setPendingDailyEmail(Boolean.TRUE);
			entry.setPendingImmediateEmail(Boolean.TRUE);
			entry.setPendingConsolidatedEmail(Boolean.TRUE);
			modelService.save(entry);
	        JnjGTCoreUtil.logInfoMessage(Logging.UPDATE_ORDER_STATUS, UPDATE_ORDER_ENTRY_STATUS, "Order entry saved successfully with status: " + entry.getQuantityStatus().getCode() , CURRENT_CLASS);
		}
    }
    
    private static OrderEntryStatus getEntryStatusForPOD(final AbstractOrderEntryModel entry, OrderEntryStatus status) {
    	List<JnjDeliveryScheduleModel> deliverySchedules= entry.getDeliverySchedules();
    	if(CollectionUtils.isNotEmpty(deliverySchedules)) {
    		int totalDeliveredItems = deliverySchedules.stream()
    		.filter(e -> e.getProofOfDeliveryDate()!=null)
    		.mapToInt(x -> x.getQty().intValue())
    		.sum();
    		if(totalDeliveredItems == entry.getQuantity()){
    			status = OrderEntryStatus.DELIVERED;
    		}   			
    		else if (totalDeliveredItems < entry.getQuantity() && totalDeliveredItems > 0) {
    			status = OrderEntryStatus.PARTIALLY_DELIVERED;
    		}
    			
    	}
    	return status;
    }
    
    private static OrderStatus getOrderStatusForPOD(OrderModel order, OrderStatus status) {
    	if(CollectionUtils.isNotEmpty(order.getEntries())){
    		int totalEntriesSize = order.getEntries().size();
    		int deliveredEntryStatuses = order.getEntries().stream()
    				.filter(x -> OrderEntryStatus.DELIVERED.getCode().equals(x.getStatus()))
    				.collect(Collectors.toList()).size();
    		int partiallyDeliveredEntryStatuses = order.getEntries().stream()
    				.filter(x -> OrderEntryStatus.PARTIALLY_DELIVERED.getCode().equals(x.getStatus()))
    				.collect(Collectors.toList()).size();
    		if(deliveredEntryStatuses == totalEntriesSize) {
    			status = OrderStatus.DELIVERED;
    		}else if ((partiallyDeliveredEntryStatuses <= totalEntriesSize && partiallyDeliveredEntryStatuses > 0) 
					|| (deliveredEntryStatuses > 0 && deliveredEntryStatuses < totalEntriesSize)) {
    			status = OrderStatus.PARTIALLY_DELIVERED;
    		}
    	}
    	return status;
    }

    private static String getEntryLineCombinationMessage(AbstractOrderEntryModel entry, String orderCode, String productCode) {
        return String.format("Processing order(%s),product(%s): ;%s;%s;%s;%s;%s;%s;%s;%s",
            orderCode, productCode, entry.getLineOverallStatus(), entry.getReasonForRejection(), entry.getDeliveryStatus(),
            entry.getLineInvoiceStatus(), entry.getGTSHold(), entry.getBackorderStatus(),
            entry.getCarrierEstDeliveryDateStatus(), entry.getCarrierActualDeliveryDateStatus());
    }

    private static String getOrderHeaderStatusCombinationMessage(OrderModel order) {
        return String.format("Processing order (%s)", order.getCode());
    }

    public OrderStatus calculateOrderHeaderStatus(final OrderModel order) {
        return jnjLaOrderDao.calculateOrderHeaderStatus(order);
    }

	@Override
	public List<String> getAllRecipientsForImmediateOrDaily(final String periodicity, final B2BUnitModel b2bUnit) {
		if (IMMEDIATE.equalsIgnoreCase(periodicity)) {
			final Map<B2BUnitModel, List<String>> recipients = jnjLaOrderDao
					.getDefaultRecipientsForImmediateEmail(b2bUnit);
			return getFinalRecipientsList(recipients, periodicity, b2bUnit);

		} else {
			final Map<B2BUnitModel, List<String>> recipients = jnjLaOrderDao.getDefaultRecipientsForDailyEmail(b2bUnit);
			return getFinalRecipientsList(recipients, periodicity, b2bUnit);
		}
	}
    
    /**
	 * To get recipient and Unit map 
	 * 
	 * @param recipients the recipients is the list of users
	 * @param periodicity the periodicity to be used
	 * @param b2bUnit the b2bUnit to be used
	 * @return ArrayList<>(hashSet) 
	 */
	private List<String> getFinalRecipientsList(final Map<B2BUnitModel, List<String>> recipients,final String periodicity,final B2BUnitModel b2bUnit) {
		Iterator<List<String>> iterator = recipients.values().iterator();
        Set<String> finalRecipients = new HashSet<>();
        
        Set<JnJB2bCustomerModel> jnJB2bCustomerModelSet = new HashSet<>();
        jnJB2bCustomerModelSet.addAll(jnjLaOrderDao.getRecipientsForDailyAndImmediateEmail(b2bUnit,periodicity));

        while(iterator.hasNext()) {
            finalRecipients.addAll(iterator.next());
        }
        for(final JnJB2bCustomerModel jnJB2bCustomerModel: jnJB2bCustomerModelSet) {
        	finalRecipients.add(jnJB2bCustomerModel.getEmail());
        }

        String finalUserList = String.join(",", finalRecipients);
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(Arrays.asList(finalUserList.split(",")));
		LOG.info("FinalRecipientsList#################################: "+hashSet.toString());
        return new ArrayList<>(hashSet);
	}

    @Override
    public boolean getUserPreference(String uid, B2BUnitModel b2bUnit) {
        return jnjLaOrderDao.getUserPreference(uid, b2bUnit);
    }
    
	/**
	 * This method is used to send email report for sap failed orders.
	 * 
	 * @param sapFailedOrders - holds sap failed orders even after max retry count
	 * 
	 */
	@Override
	public boolean sendSAPFailedOrdersReportEmail() {

		final Integer maxRetryCount = Integer
				.parseInt(Config.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.MAX_RETRY_COUNT_KEY));
		final String[] sapFailedOrderStatuses = Config
				.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.SAP_FAILED_ORDER_STATUS)
				.split(Jnjlab2bcoreConstants.SapFailedOrderReport.SAP_FAILED_ORDER_STATUS_DELIMITER);

		List<OrderModel> sapFailedOrders = jnjLaOrderDao.findSAPFailedOrders(maxRetryCount, sapFailedOrderStatuses);

		if (CollectionUtils.isNotEmpty(sapFailedOrders)) {

			final DateFormat dateFormat = new SimpleDateFormat(Jnjlab2bcoreConstants.SapFailedOrderReport.DATE_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone(Jnjlab2bcoreConstants.SapFailedOrderReport.TIME_ZONE));
			final String date = dateFormat.format(new Date());
			final String filepath = Config.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.FILE_PATH_KEY);
			final String fileName = Config.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.FILE_NAME_KEY) + date
					+ Jnjlab2bcoreConstants.SapFailedOrderReport.XLS;
			final String sheetName = Config
					.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.REPORT_SHEET_NAME_KEY);

			boolean isReportCreated = JnjLaSAPFailedOrdersReportUtil.createSAPFailedOrderReport(sapFailedOrders,
					filepath, fileName, sheetName);

			if (isReportCreated) {
				final Map<String, String> sapFailedOrdersReportData = new HashMap<>();

				final String fromEmail = Config.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_KEY);
				final String toEmail = Config.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.TO_EMAIL_KEY);
				final String fromEmailName = Config
						.getParameter(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_NAME_KEY);

				sapFailedOrdersReportData.put(Jnjlab2bcoreConstants.SapFailedOrderReport.TO_EMAIL, toEmail);
				sapFailedOrdersReportData.put(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_ADDRESS, fromEmail);
				sapFailedOrdersReportData.put(Jnjlab2bcoreConstants.SapFailedOrderReport.ATTACHMENT_FILE_NAME,
						fileName);
				sapFailedOrdersReportData.put(Jnjlab2bcoreConstants.SapFailedOrderReport.ATTACHMENT_FILE_PATH,
						filepath);
				sapFailedOrdersReportData.put(Jnjlab2bcoreConstants.SapFailedOrderReport.FROM_EMAIL_NAME,
						fromEmailName);

				String sapFailedOrderEmailProcessName = Jnjlab2bcoreConstants.SapFailedOrderReport.SAP_FAILED_ORDER_EMAIL_PROCESS_NAME;
				final JnjLaSAPFailedOrdersReportEmailProcessModel jnjLaSAPFailedOrdersReportEmailProcessModel = businessProcessService
						.createProcess(sapFailedOrderEmailProcessName + "-" + System.currentTimeMillis(),
								sapFailedOrderEmailProcessName);
				jnjLaSAPFailedOrdersReportEmailProcessModel.setSapFailedOrdersReportData(sapFailedOrdersReportData);
				jnjLaSAPFailedOrdersReportEmailProcessModel.setSapFailedOrders(sapFailedOrders);

				jnjLaSAPFailedOrdersReportEmailProcessModel.setSite(baseSiteService
						.getBaseSiteForUID(JnjLaCommonUtil.getValue(Jnjlab2bcoreConstants.CURRENT_BASE_SITE)));
				jnjLaSAPFailedOrdersReportEmailProcessModel
						.setLanguage(commonI18NService.getLanguage(Jnjlab2bcoreConstants.LANGUAGE_ISOCODE_EN));

				getModelService().save(jnjLaSAPFailedOrdersReportEmailProcessModel);
				businessProcessService.startProcess(jnjLaSAPFailedOrdersReportEmailProcessModel);
			} else {
				return false;
			}
		}
		return true;

	}


}
