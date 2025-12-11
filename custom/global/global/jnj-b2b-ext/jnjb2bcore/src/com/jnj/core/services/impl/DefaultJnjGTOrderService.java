/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.core.services.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.GeneratedJnjb2bCoreConstants.Enumerations.OrderEntryStatus;
import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.EmailPreferences;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dao.customer.JnjGTCustomerDAO;
import com.jnj.core.dao.order.JnjGTOrderDao;
import com.jnj.core.dto.JnjGTBackorderEmailDto;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.enums.SelectedAccountType;
import com.jnj.core.event.JnjGTBackorderEmailNotificationEvent;
import com.jnj.core.event.JnjGTOrderCancelNotificationEvent;
import com.jnj.core.event.JnjGTOrderShipmentEmailEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTShippingMethodModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.model.JnjOrderLineItemCancelEmailTiggerCronJobModel;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.JnjModelService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.utils.CommonUtil;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import java.util.Locale;
import com.jnj.core.model.JnjOrderCreditLimitMsgModel;


/**
 * @author Accenture
 * @version 1.0
 */

public class DefaultJnjGTOrderService extends DefaultJnjOrderService implements JnjGTOrderService
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderService.class);

	@Autowired
	FlexibleSearchService flexibleSearchService;

	@Resource(name = "jnjB2BUnitService")
	JnjGTB2BUnitService jnjGTB2BUnitService;

	@Resource(name = "jnjGTOrderDao")
	JnjGTOrderDao jnjGTOrderDao;
	
	@Resource(name = "GTCustomerDao")
	JnjGTCustomerDAO jnjGTCustomerDao;

	@Autowired
	protected JnjModelService jnjModelService;

	@Autowired
	protected UserService userService;

	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;

	@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	protected CommonI18NService commonI18NService;

	@Autowired
	protected EventService eventService;

	@Autowired
	protected BaseStoreService baseStoreService;

	@Autowired
	protected BaseSiteService baseSiteService;
	
	@Autowired
	protected SessionService sessionService;
	
	@Autowired
	protected B2BOrderService b2bOrderService;
	

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	
	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjGTB2BUnitService getJnjGTB2BUnitService() {
		return jnjGTB2BUnitService;
	}

	public JnjGTOrderDao getJnjGTOrderDao() {
		return jnjGTOrderDao;
	}

	public JnjModelService getJnjModelService() {
		return jnjModelService;
	}

	public UserService getUserService() {
		return userService;
	}

	public JnjGTB2BCommerceUserService getJnjGTB2BCommerceUserService() {
		return jnjGTB2BCommerceUserService;
	}

	public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}

	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	public EventService getEventService() {
		return eventService;
	}

	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	public BaseSiteService getBaseSiteService() {
		return baseSiteService;
	}

	protected final static String CONFIRMED_LINE_STATUS_CODES = Config
			.getParameter(Jnjb2bCoreConstants.Order.CONFIRMED_SCHEDULE_LINE_STATUS);

	protected final static String BACKORDER_AND_OPEN_LINE_EMAIL_FUNCTIONALITY = "Backorder & Open Line Email Functionality";
	
	protected final static int BACKORDER_MAX_HISTORICAL_DAYS = 180;
	
	protected final static String ORDER_ENTRY_CANCEL_PREFERENCE ="jnj.orderEntry.cancel.preference";
	/**
	 * Constant List holds compatible division codes.
	 */
	protected final static List<String> COMPATIBLE_DIVISION_CODES = Arrays.asList(
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_CODMAN),
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_MITEK),
			Config.getParameter(Jnjb2bCoreConstants.Cart.DIVISION_SPINE));

	@Override
	public boolean isPONumberUsed(final String poNumber)
	{
		boolean poNumExists = false;
		final OrderModel order = new OrderModel();
		order.setPurchaseOrderNumber(poNumber);
		order.setUnit(jnjGTB2BUnitService.getCurrentB2BUnit());

		final List<OrderModel> orders = flexibleSearchService.getModelsByExample(order);
		if (null != orders && !orders.isEmpty())
		{
			poNumExists = true;
		}
		return poNumExists;
	}

	@Override
	public SearchPageData<OrderModel> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final JnjGTOrderHistoryForm form)
	{
		String[] selectedAccounts = form.getAccounts();
		if (selectedAccounts.length == 0)
		{
			final List<String> currentUnit = Collections.singletonList(jnjGTB2BUnitService.getCurrentB2BUnit().getUid());
			selectedAccounts = currentUnit.toArray(new String[currentUnit.size()]);
		}
		form.setAccounts(selectedAccounts);
		form.setSelectedAccountType(getAcccountType(selectedAccounts));
		return jnjGTOrderDao.getPagedOrderHistoryForStatuses(pageableData, form);
	}

	@Override
	public boolean saveOrder(final OrderModel orderModel)
	{
		return jnjModelService.save(orderModel);
	}

	@Override
	public boolean isSurgeonUpdateEligible(final OrderModel order)
	{
		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final List<String> userDivisions = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
		//TODO [AR:] Yet to include check for "Order has no shipment record," since clarification on it is still pending.
		return (checkingCompatibleDivisons(COMPATIBLE_DIVISION_CODES, userDivisions) && JnjOrderTypesEnum.ZDEL.equals(order
				.getOrderType()) && !(OrderStatus.COMPLETED.equals(order.getStatus())));
	}
	
	@Override
	public boolean isPurchaseOrderUpdateEligible(final OrderModel order)
	{

		final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
		final List<String> userDivisions = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
		return (StringUtils.isEmpty(order.getPurchaseOrderNumber())
				&& (checkingCompatibleDivisons(COMPATIBLE_DIVISION_CODES, userDivisions)) && JnjOrderTypesEnum.ZDEL.equals(order
				.getOrderType()));
	}

	/**
	 * Return true if any element in 'userDivisions' is contained in 'compatibleDivisionCodes'; otherwise returns false.
	 * 
	 * @param compatibleDivisionCodes
	 * @param userDivisions
	 * @return boolean
	 */
	protected boolean checkingCompatibleDivisons(final List<String> compatibleDivisionCodes, final List<String> userDivisions)
	{
		return CollectionUtils.containsAny(compatibleDivisionCodes, userDivisions);
	}

	@Override
	public OrderModel getOrderBySapOrderNumber(final String sapOrderNumber, final BaseSiteModel baseSiteModel)
	{
		OrderModel order = null;
		final OrderModel exampleOrder = new OrderModel();
		exampleOrder.setSapOrderNumber(sapOrderNumber);

		if (baseSiteModel != null)
		{
			exampleOrder.setSite(baseSiteModel);
		}
		order = flexibleSearchService.getModelByExample(exampleOrder);
		return order;
	}

	@Override
	public List<String> getDeliveryBlocksForEntries(final String orderPK)
	{
		return jnjGTOrderDao.getDeliveryBlocksForEntries(orderPK);
	}

	@Override
	public boolean updateBatchContentInd(final OrderModel orderModel)
	{
		final boolean batchedContent = true;
		orderModel.setBatchContentInd(batchedContent);
		return jnjModelService.save(orderModel);
	}

	@Override
	public void populateMddOrderEntryStatus(final AbstractOrderEntryModel orderEntryModel)
	{
		String orderEntryStatus = null;
		final String productDivision = (orderEntryModel.getProduct() != null) ? ((JnJProductModel) orderEntryModel.getProduct())
				.getSalesOrgCode() : null;

		int countConfirmed = 0;
		int countAccepted = 0;
		int countBackorder = 0;
		int countCancelled = 0;

		for (final JnjDeliveryScheduleModel scheduleLine : orderEntryModel.getDeliverySchedules())
		{
			final String currentSchLineStatus = scheduleLine.getLineStatus();
			if (CONFIRMED_LINE_STATUS_CODES.contains(currentSchLineStatus))
			{
				countConfirmed++;
				break;
			}
			else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_UC.equals(currentSchLineStatus))
			{
				if (JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_OCD).equals(productDivision)
						&& DateUtils.isSameDay(orderEntryModel.getOrder().getCreationtime(), new Date()))//Creation date check has been added as per FRS..
				{
					countAccepted++;
				}
				else
				{
					countBackorder++;
				}
			}
			else if (Jnjb2bCoreConstants.Order.SCHEDULE_LINE_STATUS_CANCELLED.equals(currentSchLineStatus))
			{
				countCancelled++;
			}
		}
		if (countConfirmed > 0)
		{
			orderEntryStatus = OrderEntryStatus.CONFIRMED;
		}
		else if (countAccepted > 0)
		{
			orderEntryStatus = OrderEntryStatus.ITEM_ACCEPTED;
		}
		else if (countBackorder > 0)
		{
			orderEntryStatus = OrderEntryStatus.BACKORDERED;
		}
		else if (countCancelled > 0)
		{
			orderEntryStatus = OrderEntryStatus.CANCELLED;
		}

		if (orderEntryStatus != null)
		{
			orderEntryModel.setStatus(orderEntryStatus.toString());
		}
	}

	@Override
	public String getSalesTerritoryByShipToNum(final String shipToNumber)
	{
		String salesTerritory = null;
		final JnjGTTerritoryDivisonModel JnjGTTerritoryDivisonModel = jnjGTOrderDao.getSalesTerritoryByShipToNum(shipToNumber);
		if (null != JnjGTTerritoryDivisonModel)
		{
			salesTerritory = JnjGTTerritoryDivisonModel.getTerritoryCode();
		}
		return salesTerritory;
	}

	/**
	 * Return <code>SelectedAccountType</code> based on the accounts selected on the Order History page.
	 * 
	 * @param selectedAccounts
	 * @return SelectedAccountType
	 */
	protected SelectedAccountType getAcccountType(final String selectedAccounts[])
	{
		B2BUnitModel b2bUnit = null;
		int mddAccounts = 0;
		int consAccounts = 0;
		for (final String account : selectedAccounts)
		{
			b2bUnit = companyB2BCommerceService.getUnitForUid(account);
			if (b2bUnit != null && b2bUnit instanceof JnJB2BUnitModel)
			{
				if (Jnjb2bCoreConstants.MDD.equals(((JnJB2BUnitModel) b2bUnit).getSourceSysId()))
				{
					mddAccounts++;
				}
				else if (Jnjb2bCoreConstants.CONSUMER.equals(((JnJB2BUnitModel) b2bUnit).getSourceSysId()))
				{
					consAccounts++;
				}
			}
		}

		if (mddAccounts > 0 && consAccounts == 0)
		{
			return SelectedAccountType.MDD;
		}
		else if (mddAccounts == 0 && consAccounts > 0)
		{
			return SelectedAccountType.CONSUMER;
		}
		else if (mddAccounts > 0 && consAccounts > 0)
		{
			return SelectedAccountType.BOTH;
		}
		/*** If no above condition satisfies, return default BOTH ***/
		return SelectedAccountType.BOTH;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<OrderModel> getOrdersEligibleForShipmentNotification()
	{
		return jnjGTOrderDao.getOrdersEligibleForShipmentNotification();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendOrderShipEmailNotification()
	{
		LOGGER.info("Instantiating and publishing the even for Order Partially Shipped/Shipped/Confirmed status email Notification");
		final Collection<OrderModel> orders = jnjGTOrderDao.getOrdersEligibleForShipmentNotification();
		if (CollectionUtils.isNotEmpty(orders))
		{
			for (final OrderModel order : orders)
			{
				final UserModel user = order.getUser();
				if (user instanceof JnJB2bCustomerModel)
				{
					final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) user;
					final List<String> emailPreferences = customer.getEmailPreferences();
					if (CollectionUtils.isNotEmpty(emailPreferences) && emailPreferences.contains(Jnjb2bCoreConstants.EmailPreferences.PLACED_ORDER_SHIPS))
					{
						final BaseStoreModel baseStore = order.getStore();
						if (baseStore == null)
						{
							LOGGER.error("CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
									+ " | ORDER DOES NOT HAVE ANY BASE STORE");
							continue;
						}
						BaseSiteModel site = order.getSite();
						if (site == null)
						{
							if (CollectionUtils.isNotEmpty(baseStore.getCmsSites()))
							{
								final List<BaseSiteModel> cmsSites = new ArrayList<>(baseStore.getCmsSites());
								site = cmsSites.get(0);
							}
							else
							{
								LOGGER.error("CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
										+ " | COULD NOT FIND CMS SITE FROM THE RESPECTIVE BASE STORE: " + baseStore.getUid());
								continue;
							}
						}
						/*** Instantiate and populate <code>JnjGTOrderShipmentEmailEvent</code> with all required data. ***/
						final JnjGTOrderShipmentEmailEvent event = new JnjGTOrderShipmentEmailEvent();
						event.setCustomer(customer);
						event.setBaseStore(baseStore);
						event.setSite(site);
						event.setLanguage(commonI18NService.getCurrentLanguage());
						event.setCurrency(commonI18NService.getCurrentCurrency());
						event.setSapOrderNumber(order.getSapOrderNumber());
						event.setOrderCode(order.getCode());
						eventService.publishEvent(event);

						/*** Turn off the flag post sending the shipment email notification. ***/
						order.setSendOrderShipmentEmail(false);
					}
					else
					{
						LOGGER.info("CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
								+ " | COULD NOT FIND EMAIL PREFERENCE OPTED BY THE USER: " + customer.getUid()
								+ " FOR SENDING SHIPMENT NOTIFICATION.");
						order.setShipmentEmailPreference(false);
					}
					saveOrder(order);
				}
			}
		}
		else
		{
			LOGGER.info("NO ORDERS HAVING UPDATED STATUS AS 'PARTIALLY_SHIPPED', 'SHIPPED', or 'COMPLETED' FOUND. NO MAILS GENERATED");
		}
	}

	@Override
	public void sendBackOrderEmailNotification() throws DuplicateUidException
	{
		LOGGER.info("Instantiating and publishing the events for Backorders email Notification");

		//final Collection<OrderEntryModel> orderEntriesOpenlineBackorder = JnjGTOrderDao
		//.getOpenlineBackordersEligibleForEmailNotification();
		String METHOD_NAME = "sendBackOrderEmailNotification()";
		//JJEPIC-825 gets OpenlineBackorders records eligible for email
		final Collection<JnJB2bCustomerModel> mddUsers = jnjGTCustomerDao.getAllMDDUsers();
		//Declare final variables
		final DateFormat formatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		if (CollectionUtils.isEmpty(mddUsers))
		{
			return;
		}

		for (JnJB2bCustomerModel user : mddUsers)
		{
			//Fetch the email preferences
			final List<String> emailPreferences = user.getEmailPreferences();

			//Check if the user has the email preference set.
			if (CollectionUtils.isNotEmpty(emailPreferences)
					&& (emailPreferences.contains(EmailPreferences.OPENLINE_AND_BACKORDER_AVAILABILITY)))
			{
				CommonUtil.logDebugMessage(BACKORDER_AND_OPEN_LINE_EMAIL_FUNCTIONALITY, METHOD_NAME, "Processing User " + user + " ",
						LOGGER);
				boolean sendMailFlag = false;
				if (user.getLastMailSent() == null)
				{
					sendMailFlag = true;
				}
				else
				{
					final String dateVal = user.getBackorderEmailType();
					final Date d1 = user.getLastMailSent();
					final Date d2 = new Date();

					//in milliseconds
					final long diff = d2.getTime() - d1.getTime();
					final long diffSeconds = diff / 1000 % 60;
					final long diffMinutes = diff / (60 * 1000) % 60;
					final long diffHours = diff / (60 * 60 * 1000) % 24;
					final long diffDays = diff / (24 * 60 * 60 * 1000);
					final long totaldiff = diffDays * 24 + diffHours;
					if (dateVal != null && !dateVal.isEmpty())
					{
						if ((dateVal.equalsIgnoreCase("Daily") && (totaldiff >= 24))
								|| ((dateVal.equalsIgnoreCase("Weekly") && (totaldiff >= 168))))
						{

							sendMailFlag = true;
						}
					}
				}

				if (sendMailFlag)
				{
					CommonUtil.logDebugMessage(BACKORDER_AND_OPEN_LINE_EMAIL_FUNCTIONALITY, METHOD_NAME,
							"Mail Flag Is True For " + user + " ", LOGGER);
					List<String> b2bUnitListPerUser = new ArrayList<String>();
					//Fetch all the accounts corresponding to the user
					//if user is a sales rep then fetch the associated accounts with territory data
					if (user.getAccessBy().equals(AccessBy.TERRITORIES) || user.getAccessBy().equals(AccessBy.WWID))
					{
						b2bUnitListPerUser = jnjGTCustomerDao.getUniqueB2BUnitCodesForRepUID(user.getUid());
					}
					else
					{
						//If not a rep then fetch accounts using groups
						final Set<PrincipalGroupModel> b2bGroupsListPerUser = user.getGroups();
						if (CollectionUtils.isNotEmpty(b2bGroupsListPerUser))
						{
							for (PrincipalGroupModel b2bGroup : b2bGroupsListPerUser)
							{
								if (b2bGroup instanceof JnJB2BUnitModel)
								{
									b2bUnitListPerUser.add(b2bGroup.getUid());
								}
							}
						}
					}

					//Check if the b2b unit list is not empty
					if (CollectionUtils.isNotEmpty(b2bUnitListPerUser))
					{
						final Collection<OrderEntryModel> orderEntries = jnjGTOrderDao
								.getBackordersAndOpenLinesEligibleForEmailNotification(b2bUnitListPerUser, getBackorderReportDays());
						//Proceed if there is data returned for this user
						if (CollectionUtils.isNotEmpty(orderEntries))
						{
							List<JnjGTBackorderEmailDto> backOrdersData = new ArrayList<>();
							//Iterate over entries collection ordered by users.
							for (final Iterator<OrderEntryModel> iterator = orderEntries.iterator(); iterator.hasNext();)
							{
								final OrderEntryModel entry = iterator.next();
								for (final JnjDeliveryScheduleModel jnjDeliveryScheduleModel : entry.getDeliverySchedules())
								{
									if ("UC".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus())
											|| "CC".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus())
											|| "CQ".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus())
											|| "CS".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus())
											|| "RJ".equalsIgnoreCase(jnjDeliveryScheduleModel.getLineStatus()))
									{

										// Populate dto object for current line.
										final JnJProductModel product = (JnJProductModel) entry.getProduct();
										final JnjGTBackorderEmailDto dto = new JnjGTBackorderEmailDto();
										dto.setOrderNumber(entry.getOrder().getSapOrderNumber());
										dto.setProductName(product.getName());
										dto.setProductCode(product.getCode());
										if (product.getBackOrderedDate() != null)
										{
											dto.setAvailabilityDate(formatter.format(product.getBackOrderedDate()));
										}
										else
										{
											dto.setAvailabilityDate("NA");
										}
										dto.setAccountNumber(entry.getOrder().getUnit().getUid());
										dto.setOperatingCompany(retrieveSuperCategory(entry));
										dto.setCustomerPO(entry.getOrder().getPurchaseOrderNumber() != null
												? entry.getOrder().getPurchaseOrderNumber() : " ");
										dto.setQuantity(jnjDeliveryScheduleModel.getQty().toString());
										dto.setUnit(entry.getUnit().getName());
										dto.setItemPrice(entry.getBasePrice().toString());
										dto.setExtendedPrice(calculateExtendedPrice(entry, jnjDeliveryScheduleModel));
										if (jnjDeliveryScheduleModel.getShipDate() != null)
										{
											dto.setShipDate(formatter.format(jnjDeliveryScheduleModel.getShipDate()));
										}
										else
										{
											dto.setShipDate("NA");
										}
										if (jnjDeliveryScheduleModel.getDeliveryDate() != null)
										{
											dto.setDeliveryDate(formatter.format(jnjDeliveryScheduleModel.getDeliveryDate()));
										}
										else
										{
											dto.setDeliveryDate("NA");
										}
										//dto.setStatus(entry.getStatus().toString()); 
										
										switch(jnjDeliveryScheduleModel.getLineStatus()){						
										case "UC":
											dto.setStatus("BACKORDERED");
											break;
										case "RJ":
											dto.setStatus("CANCELLED");
											break;
										default:
											dto.setStatus("CONFIRMED");
											break;
										
										}	
										dto.setAdditionComments(
												(product.getAdditionalComments() == null || product.getAdditionalComments().isEmpty()) ? " "
														: product.getAdditionalComments());
										try
										{
											dto.setOrderDate(formatter.format(entry.getOrder().getDate()));
										}
										catch (Exception e)
										{
											dto.setOrderDate("NA");
										}

										backOrdersData.add(dto);
									}
								}
							}
							//Send email if there is data
							if (CollectionUtils.isNotEmpty(backOrdersData))
							{
								Collections.sort(backOrdersData, new Comparator<JnjGTBackorderEmailDto>()
								{
									@Override
									public int compare(JnjGTBackorderEmailDto object1, JnjGTBackorderEmailDto object2)
									{

										try
										{
											return (object1.getStatus().compareTo(object2.getStatus()));
										}
										catch (final Exception ce)
										{
											return -1;
										}

									}
								});

								CommonUtil.logDebugMessage(BACKORDER_AND_OPEN_LINE_EMAIL_FUNCTIONALITY, METHOD_NAME,
										"Sending email For " + user + " ", LOGGER);
								sendBackorderEmailNotification(backOrdersData, user, "true");
							}
						}
					}
				}
			}
		}
		LOGGER.info("Instantiating and publishing the events for Backorders email Notification");
		final Collection<OrderEntryModel> orderEntries = jnjGTOrderDao.getBackordersEligibleForEmailNotification();
		if (CollectionUtils.isNotEmpty(orderEntries))
		{
			JnJB2bCustomerModel previousCustomer = null;
			final DateFormat formated = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
			List<JnjGTBackorderEmailDto> backOrdersData = new ArrayList<>();

			/*** Iterate over entries collection ordered by users. ***/
			for (final Iterator<OrderEntryModel> iterator = orderEntries.iterator(); iterator.hasNext();)
			{
				final OrderEntryModel entry = iterator.next();
				final OrderModel order = entry.getOrder();
				final UserModel user = order.getUser();
				if (user instanceof JnJB2bCustomerModel)
				{
					final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) user;
					final List<String> emailPreferences = customer.getEmailPreferences();

					/*** Only customer opted for Back-order Email notification preference would be eligible for email. ***/
					if (CollectionUtils.isNotEmpty(emailPreferences)
							&& emailPreferences.contains(Jnjb2bCoreConstants.EmailPreferences.BACKORDER_AVAILABILITY_DATE_AVAILABLE))
					{
						/*** Initialize previous customer when first eligible customer arrives. ***/
						previousCustomer = (previousCustomer == null) ? customer : previousCustomer;

						/***
						 * If current iterating Order customer is different from previous one, send notification for previous
						 * customer with data populated so far, re-initialize it to customer and clear the data collection.
						 ***/
						if (!customer.equals(previousCustomer))
						{
							sendBackorderEmailNotification(backOrdersData, previousCustomer ,"false");
							previousCustomer = customer;
							backOrdersData = new ArrayList<>();
						}

						/*** Populate dto object for current backordered line. ***/
						final JnJProductModel product = (JnJProductModel) entry.getProduct();
						final JnjGTBackorderEmailDto dto = new JnjGTBackorderEmailDto();
						dto.setOrderNumber(order.getSapOrderNumber());
						dto.setProductName(product.getName());
						dto.setProductCode(product.getCode());
						dto.setAvailabilityDate(formated.format(product.getBackOrderedDate()));
						backOrdersData.add(dto);

						/*** Send notification for current customer if no further entry exists in the iteration. ***/
						if (!iterator.hasNext() && CollectionUtils.isNotEmpty(backOrdersData))
						{
							sendBackorderEmailNotification(backOrdersData, customer,"false");
						}
					}
					else
					{
						LOGGER.info("CANNOT SEND ORDER SHIPMENT MAIL FOR THE ORDER WITH NUMBER: " + order.getSapOrderNumber()
								+ " | COULD NOT FIND EMAIL PREFERENCE OPTED BY THE USER: " + customer.getUid()
								+ " FOR SENDING BACKORDER EMAIL NOTIFICATION.");
					}
				}
			}
		}
		else
		{
			LOGGER.info("NO ORDERS/LINES FOUND WITH ATLEAST A LINE STATUS AS 'BACKORDERED' WITH AVAILABLE DATE POPULATED. NO MAILS GENERATED");
		}
	}

	/**
	 * Instantiates <code>JnjGTBackorderEmailNotificationEvent</code> for each customer from the map and populate it with
	 * required data (Back order data for each line) before publishing it.
	 * 
	 * @param backOrdersData
	 */
	protected void sendBackorderEmailNotification(final List<JnjGTBackorderEmailDto> backOrdersData,
			final JnJB2bCustomerModel customer, String openLineBackorderCheck) throws DuplicateUidException
	{
		final JnjGTBackorderEmailNotificationEvent event = new JnjGTBackorderEmailNotificationEvent();
		saveCustomer(customer);
		
		event.setEmailPreferenceOpenlinebackorder(openLineBackorderCheck);
		event.setCustomer(customer);
		event.setBaseStore(baseStoreService.getBaseStoreForUid(JnJCommonUtil.getValue(Jnjb2bCoreConstants.MDD_STORE_ID_KEY)));
		event.setSite(baseSiteService.getBaseSiteForUID(JnJCommonUtil.getValue(Jnjb2bCoreConstants.MDD_SITE_ID_KEY)));
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setEmailData(backOrdersData);
		eventService.publishEvent(event);
	}

	public void saveCustomer(final JnJB2bCustomerModel jnjB2BCustomer) throws DuplicateUidException
	{
		jnjB2BCustomer.setLastMailSent(new Date());
		companyB2BCommerceService.saveModel(jnjB2BCustomer);
	}
	
	/**
	 * Removing quote orders created the day before yesterday.
	 */
	@Override
	public void removeQuoteOrders()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final Date date = calendar.getTime();
		final List<OrderModel> orderMOdelList = jnjGTOrderDao
				.getOrdersByTypeAndCreationTime(JnjOrderTypesEnum.ZQT.toString(), date);
		jnjModelService.removeAll(orderMOdelList);
	}
	
	@Override
	public JnjGTShippingMethodModel getShippingMethodFromOrderEntry(final AbstractOrderEntryModel source)
	{
		JnjGTShippingMethodModel shippingMethodModel = null;
		if (StringUtils.isNotEmpty(source.getSelectedRoute()))
		{
			shippingMethodModel = getShippingMethod(null, source.getSelectedRoute());

		}
		else if (StringUtils.isNotEmpty(source.getRoute()))
		{
			shippingMethodModel = getShippingMethod(source.getRoute(), null);
		}
		else
		{
			shippingMethodModel = getShippingMethod(null, Config.getParameter(Jnjb2bCoreConstants.Cart.SHIPPING_METHODS_STANDARD));
		}
		return shippingMethodModel;
	}
	
	@Override
	public JnjGTShippingMethodModel getShippingMethod(final String route, final String selectedRoute)
	{
		final JnjGTShippingMethodModel shippingMethodModel;
		final JnjGTShippingMethodModel exampleShippingModel = new JnjGTShippingMethodModel();
		if (StringUtils.isNotEmpty(selectedRoute))
		{
			exampleShippingModel.setExpidateRoute(selectedRoute);
		}

		if (StringUtils.isNotEmpty(route))
		{
			exampleShippingModel.setRoute(route);
		}
		try
		{
			shippingMethodModel = flexibleSearchService.getModelByExample(exampleShippingModel);

		}
		catch (final ModelNotFoundException exception)
		{
			return null;
		}
		return shippingMethodModel;
	}

	public int getBackorderReportDays()
	{
		try
		{
			if (Integer.parseInt(Config.getParameter(Order.BACKORDER_REPORT_CONFIGURED_DAYS)) > BACKORDER_MAX_HISTORICAL_DAYS)
			{
				return BACKORDER_MAX_HISTORICAL_DAYS;
			}
			else
			{
				return Integer.parseInt(Config.getParameter(Order.BACKORDER_REPORT_CONFIGURED_DAYS));
			}
		}
		catch (Exception e)
		{
			return BACKORDER_MAX_HISTORICAL_DAYS;
		}
	}
	
	/**
	 * This method is used to retrieve the super category for setting the operating company
	 *
	 * @param orderEntryModel
	 * @return operating company
	 */

	protected String retrieveSuperCategory(final OrderEntryModel orderEntryModel)
	{
		final String METHOD_NAME = "retrieveSuperCategory()";
		//	logMethodStartOrEnd(JnjGTb2bcoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.BEGIN_OF_METHOD);

		String categoryName = null;
		/** iterating over the second level of super categories **/


		for (final CategoryModel secondLevelSuperCategory : orderEntryModel.getProduct().getSupercategories())
		{
			/** iterating over the first level of super categories **/

			for (final CategoryModel firstLevelSuperCategory : secondLevelSuperCategory.getSupercategories())
			{
				/** operating company name fetched **/

				categoryName = firstLevelSuperCategory.getName();
				//	logDebugMessage(JnjGTb2bcoreConstants.Logging.REPORTS_NAME, METHOD_NAME, "Category name fetched :: " + categoryName);
				/** operating company name fetched hence breaking the loop **/

				break;
			}
			if (null != categoryName)
			{
				/** operating company name fetched hence breaking the loop **/

				break;
			}
		}
		//logMethodStartOrEnd(JnjGTb2bcoreConstants.Logging.REPORTS_NAME, METHOD_NAME, Logging.END_OF_METHOD);
		return categoryName;
	}
	
	/**
	 * This method calculates the extended price for the items in the schedule line
	 *
	 * @param orderEntryModel
	 * @param jnjDeliveryScheduleModel
	 * @return extendedPrice
	 */

	protected String calculateExtendedPrice(final OrderEntryModel orderEntryModel,
			final JnjDeliveryScheduleModel jnjDeliveryScheduleModel)
	{
		final String METHOD_NAME = "calculateExtendedPrice()";

		String extendedPrice = null;

		/** check if base price and quantity not null **/

		if (null != orderEntryModel.getBasePrice() && null != jnjDeliveryScheduleModel.getQty())
		{
			/** multiplying the base price with quantity of the item in the schedule line to get the extended price **/

			extendedPrice = String
					.valueOf(orderEntryModel.getBasePrice().doubleValue() * jnjDeliveryScheduleModel.getQty().longValue());

		}
		else
		{
			/** base price or quantity found null **/

		}
		return extendedPrice;
	}
	
	@Override
	public OrderModel getOrderDetailsForCode(final String orderCode, final boolean ignoreRestriction)
	{
		OrderModel order = null;
		try
		{
			final JnJB2bCustomerModel currentUser = (JnJB2bCustomerModel) userService.getCurrentUser();
			 Boolean salesRep = sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS);
			if (ignoreRestriction
					|| JnjGTUserTypes.PORTAL_ADMIN.equals(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)))
			{
//				order = b2bOrderService.getOrderForCode(orderCode);
				order = sessionService.executeInLocalView(new SessionExecutionBody()
				{
					@Override
					public OrderModel execute()
					{
						return b2bOrderService.getOrderForCode(orderCode);
					}
				}, userService.getAdminUser());
			}
			else
			{
				final List<String> divisionList = jnjGTB2BCommerceUserService.getUserDivisions(currentUser);
				if(sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALESREP_ORDERING_RIGHTS) == null){
					salesRep = false;
				}
			
				order = jnjGTOrderDao.getOrder(orderCode, divisionList, salesRep);
				
			}
		     
			return order;
		}catch(Exception es){
			
			return order;
		}
		
	}

	// AAOL-4049
	@SuppressWarnings("null")
	@Override
	public Map<JnJB2bCustomerModel, List<OrderEntryModel>> getCancelOrderLineItems(
			JnjOrderLineItemCancelEmailTiggerCronJobModel jobModel) {
		// TODO Auto-generated method stub
		 Map<JnJB2bCustomerModel, List<OrderEntryModel>> map = jnjGTOrderDao.getCancelOrderLineItems();
		 Map<JnJB2bCustomerModel,List<OrderEntryModel>> latestMap = new HashMap<JnJB2bCustomerModel, List<OrderEntryModel>>();
		 for (Map.Entry<JnJB2bCustomerModel, List<OrderEntryModel>> entry : map.entrySet()) {
				if(CollectionUtils.isNotEmpty(entry.getKey().getEmailPreferences()))
				{
					boolean emailPreference = entry.getKey().getEmailPreferences().contains(Config.getParameter(ORDER_ENTRY_CANCEL_PREFERENCE));
					if(emailPreference)
					{
						latestMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
		 
		return latestMap;
	}
	
	// AAOL-4336
	@Override
	public void sendCancelOrderLineStatusEmail(JnJB2bCustomerModel jnJB2bCustomerModel ,List<OrderEntryModel> orderEntries)
	{
		JnjGTOrderCancelNotificationEvent event = new JnjGTOrderCancelNotificationEvent();
		event.setOrderEntries(orderEntries);
		eventService.publishEvent(initializeEmailEvent(event,jnJB2bCustomerModel));
	}
	
	/**
	 * This method populates the JnjGTSuccessfulRegistrationEmailEvent object.
	 *
	 * @return populated event object
	 */
	protected JnjGTOrderCancelNotificationEvent initializeEmailEvent(JnjGTOrderCancelNotificationEvent event,JnJB2bCustomerModel customerModel)
	{
		/** populating the event with the basic required values **/
		event.setCustomer(customerModel);
		event.setBaseStore(baseStoreService.getBaseStoreForUid(JnJCommonUtil.getValue(Jnjb2bCoreConstants.MDD_STORE_ID_KEY)));
		event.setSite(baseSiteService.getBaseSiteForUID(JnJCommonUtil.getValue(Jnjb2bCoreConstants.MDD_SITE_ID_KEY)));
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
	
	// end 4336
	
   //AAOL-2420 changes
	@Override
	public String getJnjOrderCreditLimitMsg(String code , Locale loc)
	{
		
		 JnjOrderCreditLimitMsgModel jnjOrderCreditLimitMsg = new JnjOrderCreditLimitMsgModel();
		jnjOrderCreditLimitMsg.setCode(code);
		jnjOrderCreditLimitMsg = flexibleSearchService.getModelByExample(jnjOrderCreditLimitMsg);
		return jnjOrderCreditLimitMsg.getMessage(loc);
		
	}
	//end of AAOL-2420 changes
	
}