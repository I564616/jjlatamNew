/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.order.impl;

import com.jnj.core.dto.order.JnjOrderDTO;
import com.jnj.core.dto.order.JnjOrderLineDTO;
import com.jnj.core.dto.order.JnjOrderSchLineDTO;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dao.order.JnjRSAOrderDataService;
import com.jnj.la.core.dataload.utility.JnjSAPOrderConverter;
import com.jnj.la.core.enums.JnJOrderColumnStatus;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.order.JnjLAOrderDataService;
import com.jnj.la.core.services.order.JnjLAOrderService;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.jnj.la.core.util.JnjLaCommonUtil.getBaseStorePrefix;
import static com.jnj.la.core.util.JnjLaCommonUtil.getIntConfigValue;


public class JnjLAOrderDataServiceImpl implements JnjLAOrderDataService {

	public static final Class<JnjLAOrderDataServiceImpl> CLASS = JnjLAOrderDataServiceImpl.class;

	@Autowired
	protected JnjRSAOrderDataService jnjRSAOrderDataService;

	@Autowired
	protected JnjLAOrderService jnjLAOrderService;

	@Autowired
	protected JnjSAPOrderConverter orderConverter;

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	@Autowired
	protected DefaultJnjLASAPOrdersService jnjSAPOrdersService;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;

	@Autowired
	protected JnjLaCoreUtil jnjLaCoreUtil;

	@Autowired
	protected JnJLaProductService jnjLaProductService;

	@Autowired
    private OrderEntryCloneHelper orderEntryCloneHelper;

	@Override
	public List<JnjOrderDTO> pullOrdersFromRSA(final JnjIntegrationRSACronJobModel cronJob) {
		final String methodName = "pullOrdersFromRSA()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final List<JnjOrderDTO> jnjOrderDtoList = jnjRSAOrderDataService.pullOrdersFromRSA(cronJob);

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return jnjOrderDtoList;
	}

	@Override
	public void saveOrdersToHybris(final List<JnjOrderDTO> sapOrders, final JnjIntegrationRSACronJobModel cronJob) {
		final String methodName = "saveOrdersToHybris()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		int threadsToBeUsed = getIntConfigValue(Jnjlab2bcoreConstants.SynchronizeOrder.JNJ_SYNC_ORDER_THREADS_COUNT);
		if (threadsToBeUsed != 0 && threadsToBeUsed > 1)
		{
			// Use Multithreading- multiple threads to process lists of SAP Orders
			final int numberOfOrders = sapOrders.size();

			if (threadsToBeUsed > numberOfOrders)
			{
				threadsToBeUsed = numberOfOrders;
			}

			final int ordersForeachThread = numberOfOrders / threadsToBeUsed;

			//Prepare list of orders each thread should handle
			final List<List<JnjOrderDTO>> listOfLists = new ArrayList<>();
			int countStep = 0;
			int countTotal = 0;
			boolean firstTime = true;
			List<JnjOrderDTO> ordersList = null;

			for (final JnjOrderDTO sapOrder : sapOrders)
			{
				if (countStep == 0 && firstTime)
				{
					ordersList = new ArrayList<>();
					firstTime = false;
				}

				countStep++;
				countTotal++;

				ordersList.add(sapOrder);

				if (countStep >= ordersForeachThread)
				{
					listOfLists.add(ordersList);
					ordersList = new ArrayList<>();
					countStep = 0;
				}
				else if (countTotal == numberOfOrders)
				{
					listOfLists.add(ordersList);
					ordersList = new ArrayList<>();
				}
			}
			processOrdersMultiThread(listOfLists, cronJob);
		}
		else
		{
			// Normal flow
			processSAPOrdersList(sapOrders, cronJob);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	@Override
	public void processSAPOrdersList(final List<JnjOrderDTO> sapOrders, final JnjIntegrationRSACronJobModel cronJob) {
		final String methodName = "processSAPOrdersList()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		boolean mustSendEmail = false;
		OrderModel jnjOrder = null;
		String sapOrderNumber = null;
		OrderStatus currentOrderStatus = null;

		for (final JnjOrderDTO sapOrder : sapOrders) {
			/*
			 * 'Transactional' Approach is used to ensure that the order is saved only when every order entry and every
			 * schedule line is successfully processed.
			 */

			sapOrderNumber = sapOrder.getSAPOrderNumber();
			boolean transactionSuccessful = true;
			final Transaction transaction = Transaction.current();
			transaction.begin();
			/* Delayed Store is enabled to ensure all the operations are performed in a single operational statement */
			transaction.enableDelayedStore(true);

			try {
				jnjOrder = getExistingOrder(sapOrder);
				if (jnjOrder != null) {
					currentOrderStatus = jnjOrder.getStatus();
					updateExistingOrder(jnjOrder, sapOrder);
				} else {
					jnjOrder = createNewOrder(sapOrder);
				}

				if (jnjOrder.getStatus() != null) {
                    setDefaultPrices(jnjOrder);
                    processSAPOrderEntries(sapOrder, sapOrderNumber, jnjOrder);
					saveOrder(jnjOrder, sapOrder);
				} else {
                    String errorMessage = "Could not define order status order: " + sapOrderNumber + ". Order status is defined based on: JnjLAOrderPermutationMatrix";
                    JnjGTCoreUtil.logWarnMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
					throw new BusinessException(errorMessage);
				}

				mustSendEmail = mustSendEmail(jnjOrder, currentOrderStatus);
			} catch (final BusinessException businessException) {
				JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Order COULD NOT BE SYNCHRONIZED for the SAP Order Number [" + sapOrderNumber + "]. Exception: " + businessException.getMessage(), businessException, CLASS);
				transactionSuccessful = false;
				mustSendEmail = false;
			} catch (final Exception exception) {
				JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "method. Order COULD NOT BE SYNCHRONIZED for the SAP Order Number: [" + sapOrderNumber + "]. Exception: ", exception, CLASS);
				transactionSuccessful = false;
				mustSendEmail = false;
			} finally {
				/* Finalizing the transaction after processing */
				final boolean transactionActive = transaction.isRunning();
				if (transactionActive) {
					if (transactionSuccessful) {
						JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
								"Transaction for Order with Sap Order No. [" + sapOrderNumber + "] successful. Commiting the changes.", CLASS);
						transaction.flushDelayedStore();
						transaction.commit();

						interfaceOperationArchUtility.setLastSuccesfulStartTimeForJob(sapOrder.getLastUpdatedDate(), cronJob);

					} else {
						/* Exception Occurred. Rolling back the changes */
						JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
								"Transaction for Order Sap Order No. [" + sapOrderNumber + "] failed. Rolling back the changes.", CLASS);
						transaction.flushDelayedStore();
						transaction.rollback();
					}
				} else {
					/* Exception Occurred. Rolling back the changes */
					JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
							"Transaction for Order Sap Order No. [" + sapOrderNumber
									+ "] is not found Running. Skipping the order and increading the Write Attempt of current record.", CLASS);
				}
			}

			if (transactionSuccessful && mustSendEmail) {
				try {
					jnjSAPOrdersService.sendStatusChangeNotification((JnJB2bCustomerModel) jnjOrder.getUser(),
							jnjOrder.getSapOrderNumber(), jnjOrder.getPurchaseOrderNumber(), jnjOrder.getCode(), jnjOrder.getStatus(),
							currentOrderStatus, null, Boolean.TRUE, null, StringUtils.EMPTY);
				} catch (final Exception e) {
					JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
							"An error ocurred while trying to send email. ", e, CLASS);
				}
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

    private boolean mustSendEmail(OrderModel jnjOrder, OrderStatus currentOrderStatus) {
        return isOrderStatusChange(currentOrderStatus, jnjOrder.getStatus())
                        && ((jnjOrder.getUser() instanceof JnJB2bCustomerModel)
                        && ((JnJB2bCustomerModel) jnjOrder.getUser()).getEmailNotification());
    }

    private static void setDefaultPrices(OrderModel jnjOrder) {
        jnjOrder.setTotalExpeditedFees(0d);
        jnjOrder.setTotalFee(0d);
        jnjOrder.setTotalPrice(0d);
        jnjOrder.setTotalTax(0d);
        jnjOrder.setTotalDiscounts(0d);
        jnjOrder.setTotalInsurance(0d);
        jnjOrder.setTotalHandlingFee(0d);
        jnjOrder.setTotalDropShipFee(0d);
        jnjOrder.setTotalminimumOrderFee(0d);
        jnjOrder.setTotalFreightFees(0d);
        jnjOrder.setTotalGrossPrice(0d);
    }

    @Override
    public void processOrdersHeaderStatus(JnjIntegrationRSACronJobModel cronJob) {
		final Date lastSuccessfulTime = jnjLAOrderService.processOrdersHeaderStatus(cronJob.getLastSuccessFulRecordProcessTime());
		updateLastSuccessfulTime(cronJob, lastSuccessfulTime);
	}

	private void updateLastSuccessfulTime(JnjIntegrationRSACronJobModel cronJob, Date lastSuccessfulTime) {
		if (lastSuccessfulTime != null) {
			cronJob.setLastSuccessFulRecordProcessTime(lastSuccessfulTime);
			jnjSAPOrdersService.saveItem(cronJob);
		}
	}

	private boolean isOrderStatusChange(final OrderStatus currentOrderStatus, final OrderStatus newOrderStatus)
	{
		return (currentOrderStatus == null && newOrderStatus != null)
				|| (newOrderStatus != null && !newOrderStatus.equals(currentOrderStatus));
	}

	/**
	 * Persists the updated/created <code>Order</code> in the Hybris database system.
	 *
	 * @param order
	 * @param sapOrder
	 */
	private void saveOrder(final OrderModel order, final JnjOrderDTO sapOrder) throws BusinessException
	{
		final String methodName = "saveOrder()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final String sapOrderNumber = sapOrder.getSAPOrderNumber();
		final String modelSavingError = jnjSAPOrdersService.saveItem(order);

		if (modelSavingError == null)
		{
			errorMessage = "Order Synchronised SUCCESSFULLY for the SAP Order Number:" + sapOrderNumber;
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
		}
		else
		{
			errorMessage = "Error in saveOrder() method. Saving the New/Existing Order [Order Number: " + sapOrderNumber
					+ "] has caused an error";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
			throw new BusinessException(modelSavingError);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	public void processSAPOrderEntries(final JnjOrderDTO sapOrder, final String sapOrderNumber, final OrderModel order) throws BusinessException {
		final String methodName = "processSAPOrderEntries()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final List<AbstractOrderEntryModel> existingOrderEntries = order.getEntries();
		OrderEntryModel jnjOrderEntry = null;
		String entryNumber = null;

		final List<JnjOrderLineDTO> orderEntries = sapOrder.getJnjOrderLines();
		if (orderEntries == null || orderEntries.isEmpty())
		{
			errorMessage = "No order entries were received for the order " + sapOrderNumber;
			throw new BusinessException(errorMessage);
		}

		try {
            Map<String, OrderEntryModel> clonedOrderEntries = orderEntryCloneHelper.cloneEntries(existingOrderEntries);

			if (existingOrderEntries != null && !existingOrderEntries.isEmpty()) {
                removeExistingOrderEntries(existingOrderEntries, sapOrderNumber);
				order.setEntries(Collections.EMPTY_LIST);
			}

			for (final JnjOrderLineDTO sapOrderEntry : orderEntries) {
				entryNumber = sapOrderEntry.getOrderLineNumber();
				errorMessage = "'SAP Order Entry Number' is null.";
				ServicesUtil.validateParameterNotNull(entryNumber, errorMessage);

				jnjOrderEntry = createNewOrderEntry(order, sapOrderEntry, clonedOrderEntries);

				if (order.getOrderType() != null
                    && !StringUtils.equalsIgnoreCase(order.getOrderType().getCode(), Jnjlab2bcoreConstants.SAP_ORDER_TYPE_ZOCR)
                    && !StringUtils.equalsIgnoreCase(order.getOrderType().getCode(), Jnjlab2bcoreConstants.SAP_ORDER_TYPE_ZODR)) {
                    processSAPScheduleLines(sapOrderNumber, sapOrderEntry, jnjOrderEntry);
                }

                saveOrderEntry(jnjOrderEntry, sapOrderEntry);
			}

			final B2BUnitModel b2bUnitModel = order.getUnit();
			if (null == b2bUnitModel) {
				throw new BusinessException("The order " + order.getCode() + " does not have one sold-to associated");
			}
			final CountryModel countryModel = b2bUnitModel.getCountry();

			if (null != countryModel) {
				//Setting Tax and Discount Value on Order
				order.setTotalTaxValues(createTaxValues(order.getTotalTax(), countryModel));
				order.setGlobalDiscountValues(createDiscountValues(order.getTotalDiscounts(), countryModel));
			}

		}
		catch (final BusinessException businessException)
		{
			errorMessage = "Processing of SAP Order Entry FAILED for Entry Number: " + entryNumber;
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, businessException, CLASS);
			throw businessException;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

    private static void cascadeOrderHeaderValues(OrderEntryModel orderEntry, OrderModel order) {
        if (order.getSalesOrderCreditStatus() != null && JnJOrderColumnStatus.B.equals(JnJOrderColumnStatus.valueOf(order.getSalesOrderCreditStatus()))) {
            orderEntry.setReasonForRejection(JnJOrderColumnStatus.B.getCode());
        }
    }

	private void saveOrderEntry(final OrderEntryModel orderEntry, final JnjOrderLineDTO sapOrderEntry) throws BusinessException {
		final String methodName = "saveOrderEntry()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final String modelSavingError = jnjSAPOrdersService.saveItem(orderEntry);
		if (modelSavingError == null && sapOrderEntry != null)
		{
			errorMessage = "Order Entry Synchronised SUCCESSFULLY for the SAP Order Entry Number:" + orderEntry.getEntryNumber();
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
		}
		else if (sapOrderEntry != null)
		{
			errorMessage = "Error in saveOrderEntry() method. Saving the New/Existing Order Entry [Entry Number: "
					+ sapOrderEntry.getEntryNumber() + "] has" + " caused an error";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
			throw new BusinessException(
					"[Entry Number: " + sapOrderEntry.getEntryNumber() + "]" + Logging.HYPHEN + modelSavingError);
		}
		else
		{
			errorMessage = "Error in saveOrderEntry() method. Saving the New/Existing Order Entry [null: ] has" + " caused an error";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
			throw new BusinessException("[Entry Number: null]" + Logging.HYPHEN + modelSavingError);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	/**
	 * Processes all <code>JnjSAPScheduleLine</code> records.
	 *
	 * @throws BusinessException
	 */
	public void processSAPScheduleLines(final String sapOrderNumber, final JnjOrderLineDTO sapOrderEntry, final OrderEntryModel orderEntry) throws BusinessException {
		final String methodName = "processSAPScheduleLines()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);

		final List<JnjDeliveryScheduleModel> updatedDeliverySchedules = new ArrayList<>();
		final List<JnjDeliveryScheduleModel> existingDeliverySchedules = (orderEntry == null) ? null : orderEntry.getDeliverySchedules();
		JnjDeliveryScheduleModel jnjDeliveryScheduleLine = null;
		String lineNumber = null;
		final List<JnjOrderSchLineDTO> orderScheduleLines = sapOrderEntry.getJnjOrderSchLines();

		if (orderScheduleLines == null || orderScheduleLines.isEmpty())
		{
			errorMessage = "No SAP final Schedule Lines present final to be converted final to Hybris Order final Entries and persist final in the database.";
			throw new BusinessException(errorMessage);
		}

		try
		{
			if (existingDeliverySchedules != null && !existingDeliverySchedules.isEmpty())
			{
				removeExistingScheduleLines(existingDeliverySchedules, sapOrderEntry.getEntryNumber(), sapOrderNumber);
			}

			for (final JnjOrderSchLineDTO sapScheduleLine : orderScheduleLines)
			{
				lineNumber = sapScheduleLine.getLineNumber();
				ServicesUtil.validateParameterNotNull(lineNumber, "'SAP Schedule Line Number' is null.");


				// Schedule Line will be processed only when Confirmed Quantity is NOT zero (0).
				if (!Integer.valueOf(0).equals(Double.valueOf(sapScheduleLine.getConfirmedQuantity()).intValue()))
				{
					jnjDeliveryScheduleLine = createNewScheduleLine(orderEntry, sapScheduleLine);
					saveScheduleLine(jnjDeliveryScheduleLine, sapScheduleLine);
					updatedDeliverySchedules.add(jnjDeliveryScheduleLine);
				}
				else
				{
					errorMessage = "Processing of SAP Schedule Line has been SKIPPED for Line Number: " + lineNumber
							+ ". Reason: Confirmed Quantity is Zero (0).";
					JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
				}
			}
			if (orderEntry != null) {
				orderEntry.setDeliverySchedules(updatedDeliverySchedules);
				orderEntry.setBackorderStatus(getBackorderStatus(orderEntry));

				if (jnjDeliveryScheduleLine != null && updatedDeliverySchedules.size() == 1 && !Integer.valueOf(0).equals((int) jnjDeliveryScheduleLine.getQty().doubleValue())) {
					orderEntry.setExpectedDeliveryDate(jnjDeliveryScheduleLine.getDeliveryDate());
				}
			}
		}
		catch (final BusinessException businessException)
		{
			errorMessage = "Processing of SAP Schedule Line FAILED for Line Number: " + lineNumber + businessException.getMessage();
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, businessException, CLASS);
			throw businessException;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

    private static JnJOrderColumnStatus getBackorderStatus(final OrderEntryModel orderEntry) {
        final Long orderedQuantity = orderEntry.getQuantity();
        final Long confirmedQuantity = orderEntry.getDeliverySchedules().stream().mapToLong(JnjDeliveryScheduleModel::getQty).sum();

        if (confirmedQuantity >= orderedQuantity) {
        	return JnJOrderColumnStatus.A;
		} else if (confirmedQuantity > 0 && confirmedQuantity < orderedQuantity) {
        	return JnJOrderColumnStatus.B;
		} else if (confirmedQuantity == 0) {
        	return JnJOrderColumnStatus.C;
		}

        return JnJOrderColumnStatus.D;
    }

	private void saveScheduleLine(final JnjDeliveryScheduleModel scheduleLine, final JnjOrderSchLineDTO sapScheduleLine)
			throws BusinessException
	{
		final String methodName = "saveScheduleLine()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final String modelSavingError = jnjSAPOrdersService.saveItem(scheduleLine);
		if (modelSavingError == null)
		{
			errorMessage = "Order Schedule Line Synchronised SUCCESSFULLY for the SAP Order Schedule Line Number:"
					+ scheduleLine.getLineNumber();
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
		}
		else
		{
			errorMessage = "Error in saveScheduleLine() method. Saving the New/Existing Schedule Line [Schedule Line Number: "
					+ sapScheduleLine.getLineNumber() + "] has" + " caused an error";
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
			throw new BusinessException(
					"[Schedule Line Number: " + sapScheduleLine.getLineNumber() + "]" + Logging.HYPHEN + modelSavingError);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	private JnjDeliveryScheduleModel createNewScheduleLine(final OrderEntryModel orderEntry,
			final JnjOrderSchLineDTO sapSchLine) throws BusinessException
	{
		final String methodName = "JnjDeliveryScheduleModel()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final JnjDeliveryScheduleModel newDeliveryScheduleLine = jnjSAPOrdersService.createJnjDeliverySchedule();
		orderConverter.convertFromSAPScheduleLine(orderEntry, newDeliveryScheduleLine, sapSchLine);

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return newDeliveryScheduleLine;
	}

	/**
	 * Removes Obsolete/NonCurrent <code>JnjDeliveryScheduleModel</code> from an <code>OrderEntry</code>.
	 *
	 * @param jnjSchLines
	 * @param sapOrderEntryNumber
	 * @param sapOrderNumber
	 */
	private void removeExistingScheduleLines(final Collection<JnjDeliveryScheduleModel> jnjSchLines,
			final String sapOrderEntryNumber, final String sapOrderNumber)
	{
		final String methodName = "removeExistingScheduleLines()";
		String errorMessage = null;
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);
		final String modelRemovalError = jnjSAPOrdersService.removeItems(jnjSchLines);
		if (modelRemovalError == null)
		{
			errorMessage = "Obsolete/Noncorrent Delivery Schedule Lines for the SAP Order Entry Number:" + sapOrderEntryNumber
					+ " have been dumped/removed SUCCESSFULLY.";
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
		}
		else
		{
			errorMessage = "Error in removeExistingScheduleLines() method. Removal of Obsolete Delivery Schedule Lines for the SAP Order Number:"
					+ sapOrderNumber + " | SAP Order Entry Number:" + sapOrderEntryNumber + " has caused an error: "
					+ modelRemovalError;
			JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, errorMessage, CLASS);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	/**
	 * Sets Discount Values for Order Entry.
	 *
	 * @param discountValue
	 *           the discount value
	 * @param countryModel
	 *           the country model
	 * @return the list
	 */
	private List<DiscountValue> createDiscountValues(final Double discountValue, final CountryModel countryModel)
	{
		final String methodName = "createDiscountValues()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);
		final DiscountValue discount = new DiscountValue(Jnjlab2bcoreConstants.Order.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (countryModel == null) ? null : countryModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<>();
		discountValues.add(discount);

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return discountValues;
	}

	/**
	 * Sets Tax values for Order Entry.
	 *
	 * @param taxValue
	 *           the tax value
	 * @param countryModel
	 *           the country model
	 * @return the collection
	 */
	private Collection<TaxValue> createTaxValues(final Double taxValue, final CountryModel countryModel)
	{
		final String methodName = "createTaxValues()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final TaxValue tax = new TaxValue(Jnjlab2bcoreConstants.Order.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(countryModel == null) ? null : countryModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<>();
		taxValues.add(tax);

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return taxValues;
	}

	private OrderEntryModel createNewOrderEntry(final OrderModel existingOrder, final JnjOrderLineDTO sapOrderEntry, final Map<String, OrderEntryModel> clonedOrderEntries) throws BusinessException {
		final String methodName = "createNewOrderEntry()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final OrderEntryModel newOrderEntry = jnjSAPOrdersService.createOrderEntry();
		newOrderEntry.setOrder(existingOrder);
		try {
			newOrderEntry.setEntryNumber(Integer.valueOf(sapOrderEntry.getOrderLineNumber()));

			final String salesOrgOrder = existingOrder.getSalesOrganizationCode();
			String isoCode = null;
			if (salesOrgOrder != null) {
				final String countryStoreIsoCode = (String) salesOrgOrder.subSequence(0, 2);
				isoCode = getBaseStorePrefix(countryStoreIsoCode);
			} else {
				JnjGTCoreUtil.logWarnMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, "Cannot determine product catalog  for order since its sales org is null.", CLASS);
			}

			newOrderEntry.setProduct(jnjLaProductService.getProductForCode(sapOrderEntry.getCode(), isoCode));
			orderConverter.createNewOrderEntry(existingOrder, newOrderEntry, sapOrderEntry);
		} catch (UnknownIdentifierException | NumberFormatException | BusinessException e) {
			if (e instanceof BusinessException) {
				throw e;
			}
			throw new BusinessException(e.getMessage());
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);

		orderEntryCloneHelper.copyClonedEntryValues(clonedOrderEntries, newOrderEntry);
		return newOrderEntry;
	}

	private void removeExistingOrderEntries(final Collection<AbstractOrderEntryModel> orderEntries, final String sapOrderNumber) throws ModelRemovalException {
		final String methodName = "removeExistingOrderEntries()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);
		final String modelRemovalError = jnjSAPOrdersService.removeItems(orderEntries);

		if (modelRemovalError == null)
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
					"Obsolete/Noncorrent Order Entries for the SAP Order Number:" + sapOrderNumber
							+ " have been dumped/removed SUCCESSFULLY.", CLASS);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
					"Error in removeExistingOrderEntries() method. Removal of Obsolete Order Entries for the SAP Order Number:"
							+ sapOrderNumber + " has caused an error: " + modelRemovalError, CLASS);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}



	/**
	 * Creates a new Hybris order based on the SAP Order, associated entries and schedule lines.
	 *
	 * @param sapOrder
	 * @throws BusinessException
	 */
	private OrderModel createNewOrder(final JnjOrderDTO sapOrder) throws BusinessException
	{
		final String methodName = "createNewOrder()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final OrderModel newOrder = jnjSAPOrdersService.createNewOrder();
		orderConverter.createNewOrder(newOrder, sapOrder);

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return newOrder;
	}

	/**
	 * Updates an existing Hybris based order.
	 *
	 * @param existingOrder
	 * @param sapOrder
	 * @throws BusinessException
	 */
	private void updateExistingOrder(final OrderModel existingOrder, final JnjOrderDTO sapOrder) throws BusinessException
	{
		orderConverter.updateExistingOrder(existingOrder, sapOrder);
	}

	private OrderModel getExistingOrder(final JnjOrderDTO sapOrder)
	{
		final String methodName = "getExistingOrder()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		OrderModel jnjOrder = null;
		if (sapOrder != null)
		{
			if (sapOrder.getOrderNumber() != null && sapOrder.getOrderNumber().length() > 0)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"[ Syncronize Orders - Looking for existent Hybris Order " + sapOrder.getOrderNumber() + " ]", CLASS);

				jnjOrder = jnjLAOrderService.getExistingOrderByHybrisOrderNumber(sapOrder.getOrderNumber());
			}
			if (jnjOrder == null)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"[ Syncronize Orders - Hybris Order not informed or not found ]", CLASS);

				if (sapOrder.getSAPOrderNumber() != null && sapOrder.getSAPOrderNumber().length() > 0)
				{
					JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
							"[ Syncronize Orders - Looking for existent Hybris Order by SAP Order Nr " + sapOrder.getSAPOrderNumber()
									+ " ]", CLASS);

					jnjOrder = jnjSAPOrdersService.getExistingOrderBySAPOrderNumber(sapOrder.getSAPOrderNumber());
				}
			}

			if (jnjOrder != null)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"[ Syncronize Orders - Hybris Order " + jnjOrder.getCode() + " found ]", CLASS);
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName,
						"[ Syncronize Orders - Hybris Order not found ]", CLASS);
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		return jnjOrder;
	}

	/**
	 * Method to process SAP Orders using Multiple threads
	 *
	 * @param listOfLists
	 * @param cronJob
	 */
	public void processOrdersMultiThread(final List<List<JnjOrderDTO>> listOfLists, final JnjIntegrationRSACronJobModel cronJob)
	{
		final String methodName = "processOrdersMultiThread()";
		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

		final List<Thread> threadsList = new ArrayList<>();

		for (final List<JnjOrderDTO> sapOrders : listOfLists)
		{
			final JnjLAOrderDataServiceImpl jnjLAOrderDataServicePrototype = (JnjLAOrderDataServiceImpl) Registry
					.getApplicationContext().getBean("jnjLAOrderDataServicePrototype");
			final Thread synOrderHybrisThread = new Thread(new SynOrderHybrisThread(Registry.getCurrentTenant().getTenantID(),
					sapOrders, jnjLAOrderDataServicePrototype, cronJob));

			synOrderHybrisThread.start();
			threadsList.add(synOrderHybrisThread);
		}

		for (final Thread synOrdHybrisThread : threadsList)
		{
			try
			{
				synOrdHybrisThread.join();
			}
			catch (final InterruptedException ie)
			{
				final String errMessage = "Interrupted Exception while joining threads.";
				JnjGTCoreUtil.logErrorMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.HYPHEN + errMessage, ie, CLASS);
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
	}

	public void processOrderEntriesStatus(final JnjIntegrationRSACronJobModel cronJob) {
        final Date lastSuccessfulTime = jnjLAOrderService.processOrderEntryStatus(cronJob.getLastSuccessFulRecordProcessTime());
		updateLastSuccessfulTime(cronJob, lastSuccessfulTime);
	}

	public class SynOrderHybrisThread implements Runnable
	{

		private final String tenant;
		private final List<JnjOrderDTO> sapOrders;
		private final JnjLAOrderDataServiceImpl jnjLAOrderDataServicePrototype;
		private final JnjIntegrationRSACronJobModel cronJob;

		public SynOrderHybrisThread(final String tenant, final List<JnjOrderDTO> sapOrders,
				final JnjLAOrderDataServiceImpl jnjLAOrderServiceProcessorPrototype, final JnjIntegrationRSACronJobModel cronJob)
		{
			super();
			this.sapOrders = new ArrayList<>(sapOrders);
			this.tenant = tenant;
			this.jnjLAOrderDataServicePrototype = jnjLAOrderServiceProcessorPrototype;
			this.cronJob = cronJob;
		}

		@Override
		public void run()
		{
			final String methodName = "THREAD run()";
			JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.BEGIN_OF_METHOD, CLASS);

			Registry.setCurrentTenant(Registry.getTenantByID(tenant));
			try
			{
				JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
				// Invoking method to process SAP Orders
				jnjLAOrderDataServicePrototype.processSAPOrdersList(sapOrders, cronJob);
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}

			JnjGTCoreUtil.logDebugMessage(Logging.SYNCHRONIZE_ORDER_INTERFACE, methodName, Logging.END_OF_METHOD, CLASS);
		}

	}

}
