/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dao.JnjOrderDao;
import com.jnj.core.dataload.mapper.Header;
import com.jnj.core.dataload.mapper.Items;
import com.jnj.core.dataload.mapper.LineItem;
import com.jnj.core.dataload.mapper.PurchaseOrder;
import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.dto.JnjCheckoutDTO;
import com.jnj.core.dto.OrderHistoryDTO;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;
import com.jnj.core.services.JnJAddressService;
import com.jnj.core.services.JnJProductService;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.exceptions.BusinessException;


/**
 * have method of ModelService to save the Order Model in hybris data.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjOrderService implements JnjOrderService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjOrderService.class);
	/**  */
	protected static final String CHECKOUT_TRUE_FLAG = "X";
	SimpleDateFormat sdfForAlberFile = new SimpleDateFormat(Jnjb2bCoreConstants.LoadInvoices.DATE_FORMAT);
	SimpleDateFormat sdfForSAPRequest = new SimpleDateFormat(Jnjb2bCoreConstants.Order.PO_DATE_FORMAT);
	SimpleDateFormat sdfForAliancaFile = new SimpleDateFormat(Jnjb2bCoreConstants.Order.EXP_DELIVERY_DATE_FORMAT);
	protected String elementValue;

	@Autowired
	ModelService modelService;

	@Autowired
	JnjOrderDao jnjOrderDao;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	JnJProductService jnjProductService;



	@Autowired
	DefaultJnjSalesOrgCustService defaultjnjSalesOrgCustService;

	@Autowired
	UserService userService;


	@Autowired
	JnJCommonUtil jnjCommonUtil;

	@Autowired
	@Qualifier(value = "jnjGTAddressService")
	JnJAddressService jnJAddressService;



	public ModelService getModelService() {
		return modelService;
	}



	public JnjOrderDao getJnjOrderDao() {
		return jnjOrderDao;
	}



	public B2BOrderService getB2bOrderService() {
		return b2bOrderService;
	}



	public JnjGetCurrentDefaultB2BUnitUtil getJnjGetCurrentDefaultB2BUnitUtil() {
		return jnjGetCurrentDefaultB2BUnitUtil;
	}



	public JnJProductService getJnjProductService() {
		return jnjProductService;
	}



	public DefaultJnjSalesOrgCustService getJnjSalesOrgCustServiceImpl() {
		return defaultjnjSalesOrgCustService;
	}



	public UserService getUserService() {
		return userService;
	}



	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}



	public JnJAddressService getJnJAddressService() {
		return jnJAddressService;
	}



	@Override
	public List<OrderModel> getSubmitOrderDataList()
	{
		final List<OrderModel> orderModelList = jnjOrderDao.getOrderModel();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("List Of Order Model for submitting to sap " + orderModelList);
		}
		return orderModelList;
	}



	@Override
	public List<OrderModel> getAllOrderDetails(final String purchaseOrderNumber, final String orderNumber)
	{
		return jnjOrderDao.geAlltOrderDetails(purchaseOrderNumber, orderNumber);
	}

	@Override
	public boolean updateSAPFileds(final OrderModel orderModel, final JnjCheckoutDTO jnjCheckoutDTO)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Updating custom sap filelds to order model");
		}
		//# JnJ CR 30787
		//If PO number is not given by user, Hybirs OrderNum will be used as PO Number
		if (StringUtils.isNotEmpty(jnjCheckoutDTO.getPoOrderNumber()))
		{
			orderModel.setPurchaseOrderNumber(jnjCheckoutDTO.getPoOrderNumber());
		}
		else
		{
			orderModel.setPurchaseOrderNumber(orderModel.getCode());
		}

		if (jnjCheckoutDTO.isCompletOrder())
		{
			orderModel.setCompleteDelivery(CHECKOUT_TRUE_FLAG);
		}
		b2bOrderService.saveOrder(orderModel);
		return true;
	}



	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public SearchPageData<OrderModel> getOrderList(final OrderStatus[] statuses, final PageableData pageableData,
			final OrderHistoryDTO orderHistoryDTO)
	{

		return jnjOrderDao.getOrderDetails(statuses, pageableData, orderHistoryDTO);
	}




	protected boolean generateFile(final PurchaseOrder purchaseOrder, final List<File> fileListForSAP, final String specialOrderType)
			throws JAXBException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		JAXBContext jaxbContext;
		Marshaller marshaller;
		boolean uploadStatus = false;

		// Not null check for the purchase order object
		if (null != purchaseOrder && null != purchaseOrder.getHeader() && null != purchaseOrder.getHeader().getVendorID())
		{
			// To retrieve the shared folder location from config.
			final String sharedFolder = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER);

			final String salesOrg = purchaseOrder.getHeader().getVendorID();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN + "Sales Org-" + salesOrg
						+ " and Special Order Type-" + specialOrderType);
			}
			// Current time of the system.
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Order.DATE_FORMAT_FOR_FILE_NAME);
			final Date date = new Date();

			// Form the file name for the different xml file which has been generated at different time duration.
			final String fileName = Order.FILE_NAME_INITAL.concat(Order.UNDER_SCORE).concat(salesOrg).concat(Order.UNDER_SCORE)
					.concat(specialOrderType).concat(Order.UNDER_SCORE).concat(simpleDateFormat.format(date))
					.concat(Order.XML_FILE_NAME_EXTENSION);

			final String localFilePath = sharedFolder + fileName;
			final File file = new File(localFilePath);
			jaxbContext = JAXBContext.newInstance(PurchaseOrder.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(purchaseOrder, file);
			fileListForSAP.add(file);
			uploadStatus = true;
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN + fileName
						+ " File is created in Shared Folder Location " + sharedFolder);
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN
						+ "Purchase Order object is null or the Purchase Order Object doesn't contains the Sales Org");
			}
			throw new BusinessException("Purchase Order object is null or the Purchase Order Object doesn't contains the Sales Org",
					MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return uploadStatus;
	}




	/**
	 * This method is used to populate the hybris related data in Purchase order object and also set the Sales Org for an
	 * Order on the basis of split logic.
	 * 
	 * @param purchaseOrder
	 *           the purchase order
	 * @throws JAXBException
	 * @throws BusinessException
	 */

	protected boolean spiltLogicForOrdersWithFileGeneration(final PurchaseOrder purchaseOrder,
			final Map<String, String> productNumberWithSalesOrg, final List<File> fileListForSAP) throws JAXBException,
			BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "spiltLogicForOrdersWithFileGeneration()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Items itemsListExisted = null;
		boolean fileCreated = false;

		final Map<String, PurchaseOrder> purchaseOrderMapWithSalesOrg = new HashMap<String, PurchaseOrder>();

		if (null != purchaseOrder && null != purchaseOrder.getItems() && !purchaseOrder.getItems().getLineItem().isEmpty()
				&& null != productNumberWithSalesOrg)
		{
			for (final LineItem lineItem : purchaseOrder.getItems().getLineItem())
			{
				if (productNumberWithSalesOrg.containsKey(lineItem.getProductNumber()))
				{
					final String salesOrgWithOrderType = productNumberWithSalesOrg.get(lineItem.getProductNumber());

					if (!purchaseOrderMapWithSalesOrg.isEmpty() && purchaseOrderMapWithSalesOrg.containsKey(salesOrgWithOrderType))
					{
						itemsListExisted = purchaseOrderMapWithSalesOrg.get(salesOrgWithOrderType).getItems();
						lineItem.setProductNumber(Jnjb2bCoreConstants.Order.EMPTY_STRING);
						itemsListExisted.getLineItem().add(lineItem);
						purchaseOrderMapWithSalesOrg.get(salesOrgWithOrderType).setItems(itemsListExisted);
					}
					else
					{
						final PurchaseOrder currentPurchaseOrder = new PurchaseOrder();
						Header header = new Header(jnjCommonUtil.getDateFormat());
						final List<LineItem> lineItemList = new ArrayList<LineItem>();
						final Items items = new Items();
						// Not null check on the Header object as it would be null in case of Sao Luiz file. 
						if (null != purchaseOrder.getHeader())
						{
							final Header headerRetrieved = purchaseOrder.getHeader();
							// To avoid the same reference.
							header = headerRetrieved.clone();
						}
						final String[] salesOrgWithOrderTypeArray = salesOrgWithOrderType
								.split(Jnjb2bCoreConstants.Order.PIPE_STRING_SEPARATOR);
						header.setVendorID(salesOrgWithOrderTypeArray[0]);
						currentPurchaseOrder.setHeader(header);
						lineItem.setProductNumber(Jnjb2bCoreConstants.Order.EMPTY_STRING);
						lineItemList.add(lineItem);
						items.setLineItem(lineItemList);
						currentPurchaseOrder.setItems(items);
						purchaseOrderMapWithSalesOrg.put(salesOrgWithOrderType, currentPurchaseOrder);
					}
				}
			}
		}

		// check if the map which contains SalesOrg as key and Purchase Order object as its value is not empty.
		if (!purchaseOrderMapWithSalesOrg.isEmpty())
		{
			for (final Map.Entry<String, PurchaseOrder> mapEntries : purchaseOrderMapWithSalesOrg.entrySet())
			{
				final PurchaseOrder finalPurchaseOrder = mapEntries.getValue();
				final String[] specialProductType = mapEntries.getKey().split(Jnjb2bCoreConstants.Order.PIPE_STRING_SEPARATOR);
				// call the generate file method where the file is created in shared folder and then move to sftp location.
				fileCreated = generateFile(finalPurchaseOrder, fileListForSAP, specialProductType[1]);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "spiltLogicForOrdersWithFileGeneration()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return fileCreated;
	}


	/**
	 * /** {@inheritDoc}
	 */
	@Override
	public OrderStatus getOrderStatus(final String overAllStatus, final String rejectionStatus, final String creditStatus,
			final String deliveryStatus, final String invoiceStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OrderStatus orderHeaderStatus = null;
		final List<JnjOrdStsMappingModel> jnjOrderStatusModelList = jnjOrderDao.getOrderStatus(overAllStatus, rejectionStatus,
				creditStatus, deliveryStatus, invoiceStatus);
		if (null != jnjOrderStatusModelList && !jnjOrderStatusModelList.isEmpty())
		{
			orderHeaderStatus = jnjOrderStatusModelList.get(Order.ZERO).getOrderStatus();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Header Status " + orderHeaderStatus);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderHeaderStatus;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderEntryStatus getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OrderEntryStatus orderEntryStatus = null;
		final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = jnjOrderDao.getOrderEntryStatus(overAllStatus,
				rejectionStatus, deliveryStatus, invoiceStatus);
		if (null != jnjOrderEntryStatusModelList && !jnjOrderEntryStatusModelList.isEmpty())
		{
			orderEntryStatus = jnjOrderEntryStatusModelList.get(Order.ZERO).getOrderEntryStatus();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Entry Status Model "
						+ orderEntryStatus);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderEntryStatus;
	}

	@Override
	public OrderModel getLatestOrder()
	{
		final JnJB2bCustomerModel user = (JnJB2bCustomerModel) userService.getCurrentUser();
		try
		{
			return jnjOrderDao.geLatestOrderDetails(user.getDefaultB2BUnit().getPk().toString());
		}
		catch (final ModelNotFoundException modelException)
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getLatestOrder()" + Jnjb2bCoreConstants.Logging.HYPHEN + " No Model found " + modelException);
			}
			return null;
		}
		catch (final BusinessException businessException)
		{

			LOGGER.error("getLatestOrder()" + Jnjb2bCoreConstants.Logging.HYPHEN + " No Unit found " + businessException);

			return null;
		}

	}

	/**
	 * The getSalesOrgByProductModel method retrieves the sales org on the basis of JnJ Product Model.
	 * 
	 * @param jnjProductModel
	 *           the jnj product model
	 * @return the sales org by product model
	 */
	protected String getSalesOrgByProductModel(final JnJProductModel jnjProductModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "getSalesOrgByProductModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Retrieve the sales Org by passing the Jnj Product Model 
		final String salesOrgWithOrderType = defaultjnjSalesOrgCustService.getSalesOrgAndSapOrderType(jnjProductModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "getSalesOrgByProductModel()" + Logging.HYPHEN
					+ salesOrgWithOrderType);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "getSalesOrgByProductModel()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return salesOrgWithOrderType;
	}


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean sftpCallAndMoveFileToZipFolder(final List<File> fileListForSAP)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "sftpCallAndMoveFileToZipFolder()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean uploadStatus = false;
		//To retrieve the remote File Path from config.
		final String remoteFilePath = Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_DESTINATION_SUBMIT_ORDER_EDI_FOLDER);
		final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN + fileListForSAP
					+ " Files are moving from Shared Folder Location to SFTP folder location " + remoteFilePath);
		}
		//Calling JnjSftpFileTransferUtil class method to save file to SFTP folder
		uploadStatus = jnjSftpFileTransferUtil.uploadMultipleFileToSftp(fileListForSAP, remoteFilePath,
				Jnjb2bCoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "generateFile()" + Logging.HYPHEN
					+ "After getting response from the SFTP File Upload Util class");
		}
		// if the uploadStatus is true then delete the file from the shared folder.
		if (uploadStatus)
		{
			for (final File file : fileListForSAP)
			{
				uploadStatus = JnJXMLFilePicker.zipAndMoveFile(file, Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
						Jnjb2bCoreConstants.FEEDS_ARCHIVE_FOLDER_NAME, false);
			}
		}
		else
		{
			for (final File file : fileListForSAP)
			{
				uploadStatus = JnJXMLFilePicker.zipAndMoveFile(file, Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
						Jnjb2bCoreConstants.FEEDS_ERROR_FOLDER_NAME, false);

			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "sftpCallAndMoveFileToZipFolder()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return uploadStatus;
	}


	/**
	 * The stringBuilderMethod method is used to append the string builder for all the invalid customer Product
	 * Number/ean number/customer uom which is passed in the input file.
	 * 
	 * @param inputString
	 *           the customer product number
	 */
	protected void stringBuilderMethod(final StringBuilder stringBuilder, final String inputString)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "stringBuilderMethod()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		// if the string builder contains the one element in it then add the incoming input string with comma
		if (stringBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			stringBuilder.append(Jnjb2bCoreConstants.CONST_COMMA).append(inputString);
		}
		// else add the incoming input string with out comma
		else
		{
			stringBuilder.append(inputString);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "stringBuilderMethod()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	/**
	 * The populateErrorMessageInList method adds the upcoming string builder value in the error list.
	 * 
	 * @param errorDetailsList
	 *           the error details list
	 * @param custProdNumStrBuilder
	 *           the cust prod num str builder
	 * @param eanProdNumberStrBuilder
	 *           the ean prod number
	 * @param customerUomStrBuilder
	 *           the customer uom str builder
	 */
	protected void populateErrorMessageInList(final List<String> errorDetailsList, final StringBuilder custProdNumStrBuilder,
			final StringBuilder eanProdNumberStrBuilder, final StringBuilder customerUomStrBuilder,
			final StringBuilder shipToStrBuilder, final StringBuilder uomMultiplicityCheckBuilder)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorMessageInList()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// check for the custProdNumStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != custProdNumStrBuilder && custProdNumStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			final String custProdNumbErrorMessage = jnjCommonUtil.getMessageFromImpex("order.home.errorMessageForMaterialNumber");
			final String errorMessage = custProdNumbErrorMessage.replace(Jnjb2bCoreConstants.Order.MATERIAL_STRING,
					custProdNumStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the eanProdNumberStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != eanProdNumberStrBuilder && eanProdNumberStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			final String eanProdNumbErrorMessage = jnjCommonUtil.getMessageFromImpex("order.home.errorMessageForEanNumber");
			final String errorMessage = eanProdNumbErrorMessage.replace(Jnjb2bCoreConstants.Order.MATERIAL_STRING,
					eanProdNumberStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the customerUomStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list	
		if (null != customerUomStrBuilder && customerUomStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			final String custUomErrorMessage = jnjCommonUtil.getMessageFromImpex("order.home.errorMessageForUom");
			final String errorMessage = custUomErrorMessage
					.replace(Jnjb2bCoreConstants.Order.MATERIAL_STRING, customerUomStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the shipToStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list	
		if (null != shipToStrBuilder && shipToStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			final String custUomErrorMessage = jnjCommonUtil.getMessageFromImpex("order.home.errorMessageForShipTo");
			final String errorMessage = custUomErrorMessage.replace(Jnjb2bCoreConstants.Order.MATERIAL_STRING, shipToStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the shipToStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list	
		if (null != uomMultiplicityCheckBuilder && uomMultiplicityCheckBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{

			final String uomMultiplicityCheckErrorMessage = jnjCommonUtil.getMessageFromImpex("order.home.errorMessageForQuantity");
			final String errorMessage = uomMultiplicityCheckErrorMessage.replace(Jnjb2bCoreConstants.Order.MATERIAL_STRING,
					uomMultiplicityCheckBuilder);
			errorDetailsList.add(errorMessage);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorMessageInList()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Populate error details list with general error message in case of any exception.
	 * 
	 * @param errorDetails
	 *           the error details
	 */
	protected void populateErrorDetailsList(List<String> errorDetails)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorDetailsList()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// if the error details is null then create the list else add the general error message in the list.
		if (null == errorDetails)
		{
			errorDetails = new ArrayList<String>();
		}
		errorDetails.add(jnjCommonUtil.getMessageFromImpex("order.home.generalErrorMessage"));
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER_EDI + Logging.HYPHEN + "populateErrorDetailsList()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * YTODO <<Replace this line with text describing the purpose of overriding>>
	 */
	@Override
	public List<String> getProductCodes(final String orderId)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderId);
		final List<String> productList = new ArrayList<String>();
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			productList.add(entry.getProduct().getCode());

		}
		return productList;
	}




}
