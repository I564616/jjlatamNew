/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnJInvoiceDTO;
import com.jnj.core.dto.JnjPageableData;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.impl.DefaultJnjInvoiceService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortColumn;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Invoice.SortOption;
import com.jnj.la.core.daos.JnjBrInvoiceDao;
import com.jnj.la.core.daos.JnjLaInvoiceDao;
import com.jnj.la.core.services.JnjLaInvoiceService;

import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.OrderModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jnj.core.util.JnjGTCoreUtil;

import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;

import com.jnj.core.event.JnjLaInvoiceNotificationEmailEvent;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;

import com.jnj.core.model.JnJB2bCustomerModel;

import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.b2b.model.B2BUnitModel;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;


/**
 * This class extends DefaultJnjInvoiceService and provides new functionality.
 *
 */
public class DefaultJnjLaInvoiceService extends DefaultJnjInvoiceService implements JnjLaInvoiceService {
	private static final Logger LOG = Logger.getLogger(DefaultJnjLaInvoiceService.class);

	private static final String ASCENDING = "asc";	
	
	private static final String MESSAGE = "CANNOT SEND INVOICE NOTIFICATION MAIL FOR THE ORDER WITH NUMBER: "; 

	@Autowired
	private JnjLaInvoiceDao jnjlaInvoiceDao;

	@Autowired
	private JnjBrInvoiceDao jnjBrInvoiceDao;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil b2bUnitUtil;
	
	@Autowired
	private BaseSiteService baseSiteService;
	
	@Autowired
	private BusinessProcessService businessProcessService;
	
	@Autowired
	private CommonI18NService commonI18NService;
	
	@Autowired
	private EventService eventService;


	@Override
	public JnJInvoiceDTO getInvoiceRecordsFormRSA(final String invoiceQuery)
	{
		return jnjlaInvoiceDao.getInvoiceRecordsFormRSA(invoiceQuery);
	}

	@Override
	public List<JnJInvoiceOrderModel> getInvoiceOrderData(final String invDocNo)
	{
		return jnjlaInvoiceDao.getInvoiceOrderData(invDocNo);
	}

	@Override
	public SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(final String searchBy, final String searchText, final String fromDate, final String toDate,
																   final Integer pageSize, final Integer currentPage, final String sortColumn, final String sortDirection) {
		final JnjPageableData pageableData = new JnjPageableData();
		pageableData.setSearchBy(searchBy);
		pageableData.setSearchText(searchText);
		pageableData.setFromDate(fromDate);
		pageableData.setToDate(toDate);
		pageableData.setPageSize(pageSize);
		pageableData.setCurrentPage(currentPage);
		pageableData.setSort(prepareSortOption(sortColumn, sortDirection));
		return getLocalizedInvoiceDao().getInvoiceOrderData(pageableData);
	}

	@Override
	public List<JnJInvoiceOrderModel> getInvoicesByOrders(final List<OrderModel> order) {
		return jnjlaInvoiceDao.getInvoicesByOrders(order);
	}

	@Override
	public SearchPageData<JnJInvoiceOrderModel> getInvoiceOrderData(final String searchBy, final String searchText,
																	final String fromDate, final String toDate) {
		final JnjPageableData pageableData = new JnjPageableData();
		pageableData.setSearchBy(searchBy);
		pageableData.setSearchText(searchText);
		pageableData.setFromDate(fromDate);
		pageableData.setToDate(toDate);
		pageableData.setPageSize(Jnjlab2bcoreConstants.PDF_EXCEL_MAX_PAGE_SIZE);
		return getLocalizedInvoiceDao().getInvoiceOrderData(pageableData);
	}

	private String prepareSortOption(final String sortColumn, final String sortDirection) {
		if (StringUtils.isEmpty(sortColumn) || StringUtils.isEmpty(sortDirection)) {
			return SortOption.DEFAULT_SORT_CODE;
		}

		if (ASCENDING.equalsIgnoreCase(sortDirection)) {
			switch (sortColumn) {
				case SortColumn.INVOICE_NUMBER:
					return SortOption.INVOICE_NUMBER_INCREASING;
				case SortColumn.INVOICE_TOTAL:
					return SortOption.INVOICE_TOTAL_INCREASING;
				case SortColumn.ORDER_NUMBER:
					return SortOption.ORDER_NUMBER_INCREASING;
				case SortColumn.CREATION_DATE:
					return SortOption.INVOICE_DATE_OLDEST_TO_NEWEST;
				default:
					return SortOption.DEFAULT_SORT_CODE;
			}
		} else {
			switch (sortColumn) {
				case SortColumn.INVOICE_NUMBER:
					return SortOption.INVOICE_NUMBER_DECREASING;
				case SortColumn.INVOICE_TOTAL:
					return SortOption.INVOICE_TOTAL_DECREASING;
				case SortColumn.ORDER_NUMBER:
					return SortOption.ORDER_NUMBER_DECREASING;
				case SortColumn.CREATION_DATE:
					return SortOption.INVOICE_DATE_NEWEST_TO_OLDEST;
				default:
					return SortOption.DEFAULT_SORT_CODE;
			}
		}
	}

	private JnjLaInvoiceDao getLocalizedInvoiceDao() {
		final CountryModel country = b2bUnitUtil.getCurrentCountryForSite();
		if (country != null && Jnjb2bCoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(country.getIsocode())) {
			return jnjBrInvoiceDao;
		}
		return jnjlaInvoiceDao;

	}
	
	/**
	 * This method will be used to publish the invoice notification email.
	 * @param invoiceNumber invoice number to send notification for.
	 */
	public void publishInvoiceNotificationEmail(final String invoiceNumber) {		
		final String methodName = "publishInvoiceNotificationEmail()";
		JnjGTCoreUtil
				.logInfoMessage(
						"Logging.ORDER_FLOW",
						methodName,
						"Instantiating and publishing the event for Invoice Notification Email",
						DefaultJnjLaInvoiceService.class);
		
		try {
		JnJInvoiceOrderModel invoiceModel = getInvoicebyCode(invoiceNumber);

		if (null != invoiceModel) {
			publishInvoiceEmail(invoiceNumber, invoiceModel);
		}
		else {
			JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LoadInvoices.EMAIL_NOTIFICATION, methodName,
					"INVOICE NOT LOADED ", DefaultJnjLaInvoiceService.class);
		}
		} catch(Exception exe){
			LOG.info(exe);
			LOG.error("Exception occurred while publishing the invoice email for the invoice :"+invoiceNumber + exe
					.getStackTrace() + "-" + JnJCommonUtil.getCurrentDateTime());
		}
	}

	private void publishInvoiceEmail(final String invoiceNumber, final JnJInvoiceOrderModel invoiceModel) {
		final String methodName = "publishInvoiceEmail()";
		OrderModel order = invoiceModel.getOrder();
		B2BUnitModel unit = null;
		Set<PrincipalModel> members = null;
		BaseStoreModel baseStore = null;
		BaseSiteModel site = null;
		String sapOrderNumber = null;
		UserModel user = null;

		if (null != order) {
			unit = order.getUnit();
			members = unit.getMembers();
			baseStore = order.getStore();
			site = order.getSite();
			sapOrderNumber = order.getSapOrderNumber();
			user = order.getUser();
		}

		site = DefaultJnjLaInvoiceService.getBasesiteFromBaseStore(site, baseStore, sapOrderNumber);
		List<String> b2bCustomerEmails = null;

		if(CollectionUtils.isNotEmpty(members)) {
			b2bCustomerEmails = DefaultJnjLaInvoiceService.getB2bCustomerEmails(members, sapOrderNumber);
			if (CollectionUtils.isNotEmpty(b2bCustomerEmails)) {
			if (null != baseStore && null != site) {
				publishInvoiceEmail(invoiceModel, order, baseStore, site, user, b2bCustomerEmails);
			}
			else {
				JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LoadInvoices.EMAIL_NOTIFICATION, "setSite",
						MESSAGE + sapOrderNumber
								+ " | ORDER DOES NOT HAVE ANY BASE STORE and BASE SITE"  ,
						DefaultJnjLaInvoiceService.class);
			}
			} else {
				LOG.error(methodName +" Unit members not opted to receive the invoice notification email - Unit id :" +unit.getUid()+" Invoice Number "+ invoiceNumber);
			}

		} else {
			LOG.error(methodName +" No member exists to receive the invoice notification email Invoice Number :" + invoiceNumber);
		}
	}

	private void publishInvoiceEmail(final JnJInvoiceOrderModel invoiceModel, final OrderModel order,final BaseStoreModel baseStore,final BaseSiteModel site, final UserModel user,final List<String> b2bCustomerEmails) {
		final JnjLaInvoiceNotificationEmailEvent event = new JnjLaInvoiceNotificationEmailEvent();
		if (user instanceof JnJB2bCustomerModel) {
			event.setCustomer((JnJB2bCustomerModel) user);
		}
		event.setB2bCustomerEmails(b2bCustomerEmails);
		event.setBaseStore(baseStore);
		event.setSite(site);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setOrder(order);
		event.setInvoice(invoiceModel);
		eventService.publishEvent(event);
	}

	/**
	 * This method will be used to setup user list to send the emails.
	 * @param members
	 * @param sapOrderNumber
	 * @return
	 */
	private static List<String> getB2bCustomerEmails(final Set<PrincipalModel> members, final String sapOrderNumber) {
		List<String> b2bCustomerEmails = new ArrayList<>();
		for (PrincipalModel member : members) {
			if (member instanceof JnJB2bCustomerModel) {
				final JnJB2bCustomerModel customer = (JnJB2bCustomerModel) member;
				final List<String> emailPreferences = customer
						.getEmailPreferences();
				if (CollectionUtils.isNotEmpty(emailPreferences)
						&& emailPreferences
								.contains(Jnjlab2bcoreConstants.UserCreation.INVOICE_NOTIFICATION)) {
					b2bCustomerEmails.add(customer.getContactEmail());
				} else {
					JnjGTCoreUtil
							.logInfoMessage(
									Jnjlab2bcoreConstants.LoadInvoices.EMAIL_NOTIFICATION,
									"setB2bCustomerEmails",
									MESSAGE
											+ sapOrderNumber
											+ " | COULD NOT FIND EMAIL PREFERENCE OPTED BY THE USER: "
											+ customer.getUid()
											+ " FOR SENDING INVOICE NOTIFICATION.",
									DefaultJnjLaInvoiceService.class);

				}
			}
		}
		return b2bCustomerEmails;
	}
	
	/**
	 * This method will be used to get the base site from base store.
	 * @param site
	 * @param baseStore
	 * @param sapOrderNumber
	 * @return
	 */
	private static BaseSiteModel getBasesiteFromBaseStore(final BaseSiteModel site, final BaseStoreModel baseStore, final String sapOrderNumber) {
		if (site == null && null != baseStore) {
			if (CollectionUtils.isNotEmpty(baseStore.getCmsSites())) {
				final List<BaseSiteModel> cmsSites = new ArrayList<>(
						baseStore.getCmsSites());
				return cmsSites.get(0);
			} else {
				JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.LoadInvoices.EMAIL_NOTIFICATION, "setSite",
						MESSAGE + sapOrderNumber
								+ " | ORDER DOES NOT HAVE ANY BASE STORE"
								+ baseStore.getUid(),
						DefaultJnjLaInvoiceService.class);
			}			
		}
		return site;
	}
	
}