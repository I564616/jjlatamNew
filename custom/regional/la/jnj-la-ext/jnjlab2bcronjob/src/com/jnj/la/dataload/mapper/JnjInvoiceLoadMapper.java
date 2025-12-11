/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload.mapper;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.dto.JnJInvoiceHeaderDataDTO;
import com.jnj.core.dto.JnJInvoiceLineItemDataDTO;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJInvoiceEntryModel;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.services.synchronizeOrders.impl.DefaultJnjSAPOrdersService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.constants.JnjLautilConstants.Logging;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnJLaInvoiceHeaderDataDTO;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.JnjLaInvoiceService;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;


/**
 * This class stores the DTO object in to the Invoice order and entry models.
 *
 * @author Manoj.K.Panda
 *
 */
public class JnjInvoiceLoadMapper
{
	private static final Logger LOG = Logger.getLogger(JnjInvoiceLoadMapper.class);

	private ModelService modelService;


	private B2BUnitService<CompanyModel, UserModel> b2bUnitService;

	@Autowired
	private JnJLaProductService jnjLaProductService;

	@Autowired
	private JnjLaInvoiceService jnjInvoiceService;

	@Autowired
	private JnjLaInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	@Autowired
	private JnjLAOrderService jnjOrderService;

	@Autowired
	private DefaultJnjSAPOrdersService jnjSAPOrdersService;

	private static final Class currentClass = JnjInvoiceLoadMapper.class;

	public boolean populateInvoiceRecords(final String invoiceQuery, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "populateInvoiceRecords()";
		boolean hasInvalidRecords;

		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN
				+ "Creating a new JnjIntInvoiceOrderModel for invoice doc number");

		hasInvalidRecords = processInvoiceRecords(jnjInvoiceService.getInvoiceRecordsFormRSA(invoiceQuery), jobModel);

		if (hasInvalidRecords)
		{
			LOG.debug(
					Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + " could not call processInvoiceRecords Successfully");
		}
		else
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + " could call processInvoiceRecords Successfully");
		}

		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
				+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());

		return hasInvalidRecords;
	}

	private boolean processInvoiceRecords(final JnJInvoiceDTO records, final JnjIntegrationRSACronJobModel jobModel)
	{
		final String methodName = "processInvoiceRecords()";
		boolean containsInvalidRecords = false;
		String errorMessage = null;

		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN
				+ "Begin of Processing Invoices from RSA with status");
		
		if (records != null) {
			
			for (final JnJInvoiceHeaderDataDTO header : records.getJnJInvoiceHeaderDataDTO())
			{
				try
				{
					mapInvoiceToModel(header);
					jnjInvoiceService.publishInvoiceNotificationEmail(header.getInvoiceDocNumber());
					jnjInterfaceOperationArchUtility
							.setLastSuccesfulStartTimeForJob(((JnJLaInvoiceHeaderDataDTO) header).getLastUpdateDate(), jobModel);
				}
				catch (final ParseException parseException)
				{
					LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + methodName + Logging.HYPHEN
							+ "ParseException Occurred While Saving Data to Hybris. Dropping the record with Invoice Doc Number ID: "
							+ header.getInvoiceDocNumber() + Logging.HYPHEN + parseException);
					containsInvalidRecords = true;
					errorMessage = parseException.getMessage();
				}
				catch (final BusinessException businessException)
				{
					LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "processInvoices()" + Logging.HYPHEN
							+ "BusinessException Occurred While Saving Data to Hybris. Dropping the record with Invoice Doc Number ID: "
							+ header.getInvoiceDocNumber() + Logging.HYPHEN + businessException);
					containsInvalidRecords = true;
					errorMessage = businessException.getMessage();
				}
				catch (final Exception exception)
				{
					LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + "processInvoices()" + Logging.HYPHEN
							+ "Exception Occurred While Saving Data to Hybris. Dropping the record with Invoice Doc Number ID: "
							+ header.getInvoiceDocNumber() + Logging.HYPHEN + exception);
					containsInvalidRecords = true;
					errorMessage = exception.getMessage();
				}

				if (containsInvalidRecords)
				{
					LOG.error(errorMessage);
				}
				jnjOrderService.sendOrderShipEmailNotification(header.getInvoiceDocNumber());
			}
		}

		return containsInvalidRecords;
	}

	/**
	 * This method converts data objects to model and then invokes the service methods to store the model into the
	 * database.
	 *
	 * @param jNjHeaderDto
	 *           the jn j invoice header data dto
	 * @return true, if successful
	 * @throws BusinessException
	 *            the business exception
	 * @throws ParseException
	 *            the parse exception
	 */
	public boolean mapInvoiceToModel(final JnJInvoiceHeaderDataDTO jNjHeaderDto) throws BusinessException, ParseException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_MAP_INVOICE_MODEL + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}

		boolean invoiceModelStatus = false;

		if (null != jNjHeaderDto && StringUtils.isNotEmpty(jNjHeaderDto.getInvoiceDocNumber()))
		{
			//Creating model objects
			JnJInvoiceOrderModel tempJnJInvoiceOrderModel = modelService.create(JnJInvoiceOrderModel.class);
			final List<JnJInvoiceOrderModel> jnjInvoiceOrderModels = jnjInvoiceService
					.getInvoiceOrderData(jNjHeaderDto.getInvoiceDocNumber());
			if (jnjInvoiceOrderModels != null && !jnjInvoiceOrderModels.isEmpty())
			{
				tempJnJInvoiceOrderModel = jnjInvoiceOrderModels.get(0);
			}
			tempJnJInvoiceOrderModel.setInvDocNo(jNjHeaderDto.getInvoiceDocNumber());
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Models have been created");
			}

			//Setting data into model from header DTO classes
			invoiceModelStatus = addDataToInvoiceOrderModel(jNjHeaderDto, tempJnJInvoiceOrderModel);
			if (LOG.isDebugEnabled())
			{
				LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
						+ Logging.LOAD_INVOICE_MAP_INVOICE_MODEL + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}

		}
		else
		{
			LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.LOAD_INVOICE_MAP_INVOICE_MODEL + Logging.HYPHEN
					+ "Null JnJInvoiceHeaderDataDTO or InvoiceDocNo is Empty");
		}
		return invoiceModelStatus;
	}

	private boolean addDataToInvoiceOrderModel(final JnJInvoiceHeaderDataDTO jNjHeaderDto,
			final JnJInvoiceOrderModel jnjInvoiceOrderModel) throws BusinessException, ParseException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_ADD_TO_ORDER + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		boolean orderFlag = false;
		JnJB2BUnitModel jnJB2BUnitModel;
		if (jNjHeaderDto != null)
		{
			jnJB2BUnitModel = populateInvoiceModel(jNjHeaderDto, jnjInvoiceOrderModel);

			if (LOG.isDebugEnabled())
			{
				LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN
						+ "All values of jnjIntInvoiceOrderModel set in JnJInvoiceOrderModel. Calling Service to Persist to Hybris."
						+ Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
			}

			//Adding Line Items to JnjInvoiceOrderModel
			final List<JnJInvoiceLineItemDataDTO> jnjIntInvoiceEntryModelList = jNjHeaderDto.getLineItemList();
			final List<JnJInvoiceEntryModel> jnjInvoiceEntryModelList = new ArrayList<>();
			if (!(jnjIntInvoiceEntryModelList.isEmpty()))
			{
				for (final JnJInvoiceLineItemDataDTO jnJInvoiceLineItemDataDTO : jnjIntInvoiceEntryModelList)
				{
					final JnJInvoiceEntryModel jnjInvoiceEntryModel = modelService.create(JnJInvoiceEntryModel.class);
					addDataToInvoiceEntryModel(jnJInvoiceLineItemDataDTO, jnjInvoiceEntryModel, jnJB2BUnitModel);
					jnjInvoiceEntryModelList.add(jnjInvoiceEntryModel);
				}
				jnjInvoiceOrderModel.setEntries(jnjInvoiceEntryModelList);
			}
			setOrderToInvoiceModel(jnjInvoiceOrderModel);
			orderFlag = jnjInvoiceService.saveInvoiceOrder(jnjInvoiceOrderModel);
		}
		else
		{
			LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.LOAD_INVOICE_ADD_TO_ORDER + Logging.HYPHEN
					+ "Header Data Missing in the DTO. Returning False");
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_ADD_TO_ORDER + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return orderFlag;
	}

	/**
	 *
	 */
	private void setOrderToInvoiceModel(final JnJInvoiceOrderModel jnjInvoiceOrderModel)
	{
		final String methodName = "setOrderToInvoiceModel()";
		final String sapOrderNumber = jnjInvoiceOrderModel.getSalesOrder();
		OrderModel jnjOrder;
		if (sapOrderNumber != null && !sapOrderNumber.isEmpty())
		{
			jnjOrder = jnjSAPOrdersService.getExistingOrderBySAPOrderNumber(sapOrderNumber);
		}
		else
		{
			JnjGTCoreUtil
					.logInfoMessage(
							Logging.LOAD_INVOICES_JOB, methodName, "Sap order number is null or empty for Invoice number: "
									+ jnjInvoiceOrderModel.getInvDocNo() + ". No order was set to this invoice.",
							JnjInvoiceLoadMapper.class);
			return;
		}

		if (jnjOrder != null)
		{
			jnjInvoiceOrderModel.setOrder(jnjOrder);
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_INVOICES_JOB, methodName,
					"Invoice number: " + jnjInvoiceOrderModel.getInvDocNo()
							+ " was successfully set to order number / sap order number: " + jnjOrder.getOrderNumber() + " / "
							+ jnjOrder.getSapOrderNumber(),
					JnjInvoiceLoadMapper.class);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.LOAD_INVOICES_JOB, methodName, "Did not find any order with the sap order number: "
					+ sapOrderNumber + "No order was set to invoice: " + jnjInvoiceOrderModel.getInvDocNo(),
					JnjInvoiceLoadMapper.class);
		}

	}

	private JnJB2BUnitModel populateInvoiceModel(final JnJInvoiceHeaderDataDTO jNjHeaderDto,
			final JnJInvoiceOrderModel jnjInvoiceOrderModel) throws BusinessException, ParseException
	{
		JnJB2BUnitModel jnJB2BUnitModel = null;
		if (jNjHeaderDto.getSoldTo() != null)
		{
			jnJB2BUnitModel = getB2bUnitForId(jNjHeaderDto);
			if (null == jnJB2BUnitModel)
			{
				LOG.error(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.LOAD_INVOICE_ADD_TO_ORDER + Logging.HYPHEN
						+ "JnJ B2B Unit model is not present for  given sold to number from RSA :  " + jNjHeaderDto.getSoldTo());
				throw new BusinessException(
						"JnJ B2B Unit model is not present for  given sold to number from RSA :  " + jNjHeaderDto.getSoldTo());
			}
			jnjInvoiceOrderModel.setSoldTo(jnJB2BUnitModel);
		}
		jnjInvoiceOrderModel.setBillingDoc(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getBillingDoc()));
		jnjInvoiceOrderModel.setBillType(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getBillType()));
		jnjInvoiceOrderModel.setCancelledDocNo(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getCancelledDocNo()));
		jnjInvoiceOrderModel.setCdv(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getCdv()));
		jnjInvoiceOrderModel.setCreationDate(getFormattedDate(jNjHeaderDto));
		jnjInvoiceOrderModel.setDocNumber(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getDocNumber()));
		jnjInvoiceOrderModel.setModel(JnjLaCoreUtil.getStringTwoDigitsValue(jNjHeaderDto.getModel()));
		jnjInvoiceOrderModel.setNetValue(JnjLaCoreUtil.getDoubleValue(jNjHeaderDto.getNetValue()));
		jnjInvoiceOrderModel.setPayer(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getPayer()));
		jnjInvoiceOrderModel.setPoNumber(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getPoNumber()));
		jnjInvoiceOrderModel.setRegion(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getRegion()));
		jnjInvoiceOrderModel.setNfYear(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getNfYear()));
		jnjInvoiceOrderModel.setNfMonth(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getNfMonth()));
		jnjInvoiceOrderModel.setNfNumber(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getNfNumber()));
		jnjInvoiceOrderModel.setStcd(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getStcd()));
		jnjInvoiceOrderModel.setSeries(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getSeries()));
		jnjInvoiceOrderModel.setSalesOrder(JnjLaCoreUtil.getStringValue(jNjHeaderDto.getSalesOrder()));

		return jnJB2BUnitModel;
	}

	private Date getFormattedDate(final JnJInvoiceHeaderDataDTO jNjHeaderDto) throws ParseException
	{
		final SimpleDateFormat formatter = new SimpleDateFormat(Jnjlab2bcoreConstants.LoadInvoices.RSA_DATE_FORMAT);
		return (jNjHeaderDto.getCreationDate() != null) ? formatter.parse(jNjHeaderDto.getCreationDate()) : null;
	}

	/**
	 * This method saves all the Invoice Entries from dto classes to model class.
	 *
	 * @param jnJInvoiceLineItemDataDTO
	 *           the jn j invoice line item data dto
	 * @param jnjInvoiceEntryModel
	 *           the jnj invoice entry model
	 * @param jnJB2BUnitModel
	 *           the jn j b2 b unit model
	 * @throws BusinessException
	 *            the business exception
	 */
	private void addDataToInvoiceEntryModel(final JnJInvoiceLineItemDataDTO jnJInvoiceLineItemDataDTO,
			final JnJInvoiceEntryModel jnjInvoiceEntryModel, final JnJB2BUnitModel jnJB2BUnitModel) throws BusinessException
	{

		final String methodName = "addDataToInvoiceEntryModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_INVOICES_JOB, methodName,
				Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.LOAD_INVOICE_ADD_TO_ENTRY, currentClass);

		if (jnJInvoiceLineItemDataDTO != null)
		{
			if (jnJInvoiceLineItemDataDTO.getMaterial() != null)
			{
				final String productCode = jnJInvoiceLineItemDataDTO.getMaterial();
				final String productSector = jnjLaProductService.getProductSector(productCode);
				final String customerSalesOrgCountry = JnjLaCommonUtil.getSalesOrgCustomerForProductSector(productSector,
						jnJB2BUnitModel);

				JnJProductModel jnJProductModel = jnjLaProductService.getProduct(customerSalesOrgCountry, productCode);

				if (customerSalesOrgCountry != null)
				{
					jnJProductModel = jnjLaProductService.getProduct(customerSalesOrgCountry, productCode);
				}
				else
				{
					JnjGTCoreUtil.logWarnMessage(Logging.LOAD_INVOICES_JOB, methodName,
							"Coundn't find a valid sales org from sector:" + productSector + " in customer: "
									+ (jnJB2BUnitModel != null ? jnJB2BUnitModel.getUid() : " null")
									+ ". Falling back to determine the product's catalog version with the customer's country. This might cause problems for customers which buy from more than one store, such as AR. The current country for the customer is: "
									+ (jnJB2BUnitModel != null ? jnJB2BUnitModel.getCountry().getIsocode() : " null"),
							JnjContractMapper.class);
					jnJProductModel = jnjLaProductService.getProduct(jnJB2BUnitModel, productCode);
				}

				if (null != jnJProductModel)
				{
					jnjInvoiceEntryModel.setMaterial(jnJProductModel);
				}
				else
				{
					JnjGTCoreUtil.logErrorMessage(Logging.LOAD_INVOICES_JOB, methodName,
							"Product in Line Item not Found in Hybris. Throwing Exception and Skipping Record :::"
									+ jnJInvoiceLineItemDataDTO.getMaterial(),
							currentClass);
					throw new BusinessException("Product in Line Item not Found in Hybris. Throwing Exception and Skipping Record");
				}
			}
			if ((jnJInvoiceLineItemDataDTO.getQty() != null) && (jnJInvoiceLineItemDataDTO.getQty().trim().length() != 0))
			{
				String qtyStr = jnJInvoiceLineItemDataDTO.getQty();
				if (qtyStr.contains(Jnjb2bCoreConstants.LoadInvoices.DECIMAL_POINT))
				{
					final String[] qtyArr = qtyStr.split(Jnjb2bCoreConstants.LoadInvoices.DECIMAL_SPLIT_EXPRESSION);
					qtyStr = qtyArr[0];
				}
				jnjInvoiceEntryModel.setQty(Integer.valueOf(qtyStr));
			}

			jnjInvoiceEntryModel.setLotNo(JnjLaCoreUtil.getStringValue(jnJInvoiceLineItemDataDTO.getLotNo()));
			jnjInvoiceEntryModel.setItemNo(JnjLaCoreUtil.getStringValue(jnJInvoiceLineItemDataDTO.getItemNumber()));
			jnjInvoiceEntryModel.setOrderReason(JnjLaCoreUtil.getStringValue(jnJInvoiceLineItemDataDTO.getOrderReason()));
			jnjInvoiceEntryModel.setSalesOrderItemNo(JnjLaCoreUtil.getStringValue(jnJInvoiceLineItemDataDTO.getSalesOrderItemNo()));

			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_INVOICES_JOB, methodName,
					"All values of jnJInvoiceLineItemDataDTO set in JnJInvoiceEntryModel", currentClass);
		}
		else
		{
			JnjGTCoreUtil.logErrorMessage(Logging.LOAD_INVOICES_JOB, methodName,
					"Recieved Null InvoiceEntry Line Item Details. No InvoiceEntry associated with Inovice stored in Hybris.",
					currentClass);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_INVOICES_JOB, methodName,
				Logging.END_OF_METHOD + Logging.HYPHEN + Logging.LOAD_INVOICE_ADD_TO_ENTRY, currentClass);
	}

	/**
	 * This method gets the b2bunit associated with the passed b2bunit id.
	 */
	private JnJB2BUnitModel getB2bUnitForId(final JnJInvoiceHeaderDataDTO jNjHeaderDto)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_GET_B2BUNIT + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		final JnJB2BUnitModel jnJB2BUnitModel = (JnJB2BUnitModel) b2bUnitService.getUnitForUid(jNjHeaderDto.getSoldTo());

		if (LOG.isDebugEnabled())
		{
			LOG.debug(Logging.LOAD_INVOICES_JOB + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.LOAD_INVOICE_GET_B2BUNIT + Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime());
		}
		return jnJB2BUnitModel;
	}

	public B2BUnitService<CompanyModel, UserModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<CompanyModel, UserModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
