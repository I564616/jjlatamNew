/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.impl;

import de.hybris.platform.b2b.services.B2BOrderService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;
import java.util.Locale;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTSurgeonData;
import com.jnj.facades.data.JnjOrderData;
import com.jnj.facades.order.impl.DefaultJnjOrderFacade;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dto.JnjGTDisputeInquiryDto;
import com.jnj.core.dto.JnjGTDisputeItemInquiryDto;
import com.jnj.core.dto.JnjGTDisputeOrderInquiryDto;
import com.jnj.core.dto.JnjGTOrderHistoryForm;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTOrderChangeResponseData;
import com.jnj.core.event.JnjGTDisputeInquiryEvent;
import com.jnj.facades.order.JnjGTOrderFacade;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.gt.outbound.mapper.JnjGTCmodRgaMapper;
import com.jnj.gt.outbound.mapper.JnjGTProofOfDeliveryMapper;
import com.jnj.gt.outbound.mapper.JnjGTReqOrderChangeMapper;
import com.jnj.services.CMSSiteService;
import com.jnj.core.services.JnjGTOrderService;
import com.jnj.core.services.company.JnjGTB2BCommerceUserService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.surgeon.JnjGTSurgeonService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTOrderFacade extends DefaultJnjOrderFacade implements JnjGTOrderFacade
{
	@Resource(name = "jnjGTOrderService")
	protected JnjGTOrderService jnjGTOrderService;

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected EnumerationService enumerationService;

	@Autowired
	protected JnjGTReqOrderChangeMapper jnjGTReqOrderChangeMapper;

	@Autowired
	protected B2BOrderService b2bOrderService;

	@Autowired
	protected JnjGTSurgeonService jnjGTSurgeonService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected EventService eventService;

	@Autowired
	@Qualifier(value = "addressReversePopulator")
	protected AddressReversePopulator addressReversePopulator;

	@Autowired
	protected BaseStoreService baseStoreService;

	@Autowired
	protected BaseSiteService baseSiteService;

	@Autowired
	protected CommonI18NService commonI18NService;

	@Autowired
	protected Converter<JnjGTSurgeonModel, JnjGTSurgeonData> jnjgtSurgeonDataConverter;

	@Autowired
	protected JnjGTProofOfDeliveryMapper jnjGTProofOfDeliveryMapper;

	@Autowired
	protected JnjGTCmodRgaMapper jnjGTCmodRgaMapper;
	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected CMSSiteService cmsSiteService;
	
	@Autowired
	protected MediaService mediaService;

	@Resource(name="b2bCommerceUserService")
	protected JnjGTB2BCommerceUserService jnjGTB2BCommerceUserService;
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderFacade.class);

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Override
	public boolean isPONumUsed(final String poNumber)
	{
		return jnjGTOrderService.isPONumberUsed(poNumber);
	}


	@Override
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final JnjGTOrderHistoryForm form)
	{
		if (isSalesRepUserInSession())
		{
			checkSalesRepDivisionCompatibleOrders(form);

		}
		final SearchPageData<OrderModel> orderResults = jnjGTOrderService.getPagedOrderHistoryForStatuses(pageableData, form);
		return super.convertPageData(orderResults, getOrderHistoryConverter());
	}


	@Override
	public boolean submitPOOrderChangeRequest(final String orderCode, final String updatedPoNumber) throws SystemException,
			IntegrationException
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);
		order.setPurchaseOrderNumber(updatedPoNumber);

		final JnjGTOrderChangeResponseData response = jnjGTReqOrderChangeMapper.mapChangeOrderRequestResponse(order,
				order.getSapOrderNumber(), false);

		if (response.isSapResponseStatus())
		{
			modelService.save(order);
			return true;
		}
		return false;
	}


	@Override
	public String submitSurgeonOrderChangeRequest(final String orderCode, final String selectedSurgeonId) throws SystemException,
			IntegrationException
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);
		final JnjGTSurgeonModel surgeon = new JnjGTSurgeonModel();
		surgeon.setSurgeonId(selectedSurgeonId);
		final JnjGTSurgeonModel existingSurgeon = jnjGTSurgeonService.getJnjGTSurgeonModelByExample(surgeon);
		order.setSurgeon(existingSurgeon);

		final JnjGTOrderChangeResponseData response = jnjGTReqOrderChangeMapper.mapChangeOrderRequestResponse(order,
				order.getSapOrderNumber(), false);

		if (response.isSapResponseStatus())
		{
			modelService.save(order);

			final StringBuilder surgeonName = new StringBuilder(JnjGTCoreUtil.formatValuesByCommaSeparated(
					existingSurgeon.getFirstName(), existingSurgeon.getMiddleName(), existingSurgeon.getLastName()));
			surgeonName.append(" (" + existingSurgeon.getSurgeonId() + ")");
			return surgeonName.toString();
		}
		return StringUtils.EMPTY;
	}

	@Override
	public List<JnjGTSurgeonData> getSurgeonData()
	{
		final List<JnjGTSurgeonData> data = new ArrayList<>();
		final Collection<JnjGTSurgeonModel> surgeonModels = jnjGTSurgeonService.getAllSurgeonRecords();
		for (final JnjGTSurgeonModel surgeon : surgeonModels)
		{
			final JnjGTSurgeonData surgeonData = new JnjGTSurgeonData();
			getJnjGTSurgeonDataConverter().convert(surgeon, surgeonData);
			data.add(surgeonData);
		}
		return data;
	}


	@Override
	public SearchPageData<JnjGTSurgeonData> getSurgeonData(final String searchPattern, final int loadMoreCounter)
	{
		final int pageSize = Config.getInt("surgeon.page.size", 5);
		final int counter = loadMoreCounter == 0 ? 1 : loadMoreCounter;
		final PageableData pageableData = new PageableData();
		pageableData.setPageSize(pageSize * counter);
		final SearchPageData<JnjGTSurgeonModel> surgeonRecords = jnjGTSurgeonService.getSurgeonRecords(pageableData, searchPattern);
		return convertPageData(surgeonRecords, getJnjGTSurgeonDataConverter());
	}



	@Override
	public Map<String, String> getAccountsMap()
	{
		return jnjGTCustomerService.getAccountsMap(true);
	}

	@Override
	public String getCurrentB2bUnitId()
	{
		return jnjGTCustomerService.getCurrentUser().getCurrentB2BUnit().getUid();
	}

	protected void checkSalesRepDivisionCompatibleOrders(final JnjGTOrderHistoryForm form)
	{

		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentUser = ((JnJB2bCustomerModel) userService.getCurrentUser());
			form.setUserDivisions(jnjGTB2BCommerceUserService.getUserDivisions(currentUser));
		}
	}




	/**
	 * Check sales rep division compatible order.
	 * 
	 * @param order
	 *           the order
	 * @return true, if successful
	 */
	protected boolean checkSalesRepDivisionCompatibleOrder(final OrderModel order)
	{
		boolean isOrderCompatible = false;
		for (final AbstractOrderEntryModel orderEntry : order.getEntries())
		{
			if (jnJGTProductService.isProductDivisionSameAsUserDivision((JnJProductModel) orderEntry.getProduct()))
			{
				isOrderCompatible = true;
				break;
			}
		}
		return isOrderCompatible;
	}

	@Override
	public boolean isSalesRepUserInSession()
	{
		if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER) != null 
				&& sessionService.getAttribute(Jnjb2bCoreConstants.Login.SALES_REP_USER).equals(Boolean.TRUE))
		{
			return true;
		}
		return false;
	}


	@Override
	public boolean sendDisputeorderInquiry(final JnjGTDisputeInquiryDto data, final String orderCode)
	{
		final JnjGTDisputeInquiryEvent event = new JnjGTDisputeInquiryEvent();
		initializeDisputeinquiryEvent(event);

		populateInquiryData(data, orderCode);
		if (data instanceof JnjGTDisputeOrderInquiryDto)
		{
			final JnjGTDisputeOrderInquiryDto disputeOrderData = (JnjGTDisputeOrderInquiryDto) data;
			event.setDisputeOrder(true);
			event.setDisputeOrderInquiryData(disputeOrderData);

			if (disputeOrderData.getShipToAddress() != null)
			{
				final AddressModel shipAddress = new AddressModel();
				addressReversePopulator.populate(disputeOrderData.getShipToAddress(), shipAddress);
				shipAddress.setOwner(userService.getCurrentUser());
				modelService.save(shipAddress);
				event.setShipToAddress(shipAddress);
			}

			if (disputeOrderData.isInCorrectAddressDispute() && disputeOrderData.getCorrectAddress() != null)
			{
				final AddressModel correctAddress = new AddressModel();
				addressReversePopulator.populate(disputeOrderData.getCorrectAddress(), correctAddress);
				correctAddress.setOwner(userService.getCurrentUser());
				modelService.save(correctAddress);
				event.setCorrectAddress(correctAddress);
			}
		}
		else
		{
			event.setDisputeItemInquiryData((JnjGTDisputeItemInquiryDto) data);
			event.setDisputeOrder(false);
		}

		eventService.publishEvent(event);
		return true;
	}

	/**
	 * Populate inquiry data.
	 * 
	 * @param data
	 *           the data
	 * @param orderCode
	 *           the order code
	 */
	protected void populateInquiryData(final JnjGTDisputeInquiryDto data, final String orderCode)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);

		data.setPhoneNumber(order.getDeliveryAddress() != null ? (order.getDeliveryAddress().getPhone1() != null) ? order
				.getDeliveryAddress().getPhone1() : order.getDeliveryAddress().getPhone2() : null);

		final List<String> invoices = new ArrayList<>();
		if (order.getInvoices() != null)
		{
			for (final JnjGTInvoiceModel invoice : order.getInvoices())
			{
				invoices.add(invoice.getInvoiceNum());
			}
		}
		data.setInvoiceNumber(invoices);
	}

	/**
	 * Initialize disputeinquiry event.
	 * 
	 * @param event
	 *           the event
	 */
	protected void initializeDisputeinquiryEvent(final JnjGTDisputeInquiryEvent event)
	{
		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setCustomer((CustomerModel) userService.getCurrentUser());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
	}


	@Override
	public JnjGTSurgeonData getSurgeonInformation(final String orderCode)
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);
		final JnjGTSurgeonData data = new JnjGTSurgeonData();
		getJnjGTSurgeonDataConverter().convert(order.getSurgeon(), data);
		return data;
	}

	public Converter<JnjGTSurgeonModel, JnjGTSurgeonData> getJnjGTSurgeonDataConverter()
	{
		return jnjgtSurgeonDataConverter;
	}


	@Override
	public File getDeliveryProof(final String trackingId, final String shipDate)
	{
		if (userService.getCurrentUser() instanceof JnJB2bCustomerModel)
		{
			final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
			final JnJB2BUnitModel currentUnit = currentCustomer.getCurrentB2BUnit();

			final String personName = currentCustomer.getName();
			final String companyName = currentUnit.getName();

			try
			{
				final File proofOfDeliveryDocument = jnjGTProofOfDeliveryMapper.mapProofOfDeliveryRequestResponse(trackingId,
						personName, companyName, shipDate);
				return proofOfDeliveryDocument;
			}
			catch (final SystemException | IntegrationException exception)
			{
				LOGGER.error("Fetching proof of Delivery document has led to an exception: " + exception.getMessage());
				exception.printStackTrace();
			}
		}
		return null;
	}


	@Override
	public boolean updateBatchContentInd(final String orderCode)
	{
		final OrderModel orderModel = b2bOrderService.getOrderForCode(orderCode);
		return jnjGTOrderService.updateBatchContentInd(orderModel);
	}

	@Override
	public Map<String, byte[]> isCmodRgaCall(final String orderNumber, final boolean isCmod) throws IntegrationException,
			BusinessException
	{
		return jnjGTCmodRgaMapper.mapCmodRgaRequestResponse(orderNumber, isCmod);
	}
	
	@Override
	public OrderData getOrderDetailsForCode(
			String orderCode, boolean ignoreRestriction) {
	//	 boolean ignoreRestriction = false;
			final OrderModel orderModel = jnjGTOrderService.getOrderDetailsForCode(orderCode, ignoreRestriction);
			if (orderModel == null)
			{
				throw new UnknownIdentifierException("Order with code " + orderCode + " not found for current user in current BaseStore");
			}
			OrderData orderData = new OrderData();
			orderData =  getOrderConverter().convert(orderModel);
			return orderData;	
	}

	/**
	 * This method is responsible for returning the Site Logo Media (Image) file system path
	 * 
	 * @return mediaPath
	 */
	@Override
	public String getImagePathForOrderandInvoiceDownloads(final String mediaUid)
	{
		
		
			final CatalogVersionModel currentCatalog = cmsSiteService.getCurrentSite().getContentCatalogs().get(0)
					.getActiveCatalogVersion();
			final String mediaDirBase = Config.getParameter(Jnjb2bCoreConstants.MediaFolder.MEDIA_DIR_KEY);

			return mediaDirBase + File.separator + "sys_master" + File.separator
					+ mediaService.getMedia(currentCatalog, mediaUid).getLocation();
		}  
	@Override
	public String submitSurgeonOrderChangeRequest(final String orderCode, final String selectedSurgeonId, final String name,
			final String hospitalID) throws SystemException, IntegrationException
	{
		final OrderModel order = b2bOrderService.getOrderForCode(orderCode);
		JnjGTSurgeonModel surgeon = new JnjGTSurgeonModel();
		List<JnjGTSurgeonModel> surgeonModelList = null;
		String surgName = null;
		if (!selectedSurgeonId.equalsIgnoreCase("null"))
		{
			final JnjGTSurgeonModel expsurgeon = new JnjGTSurgeonModel();
			expsurgeon.setSurgeonId(selectedSurgeonId);
			if (!hospitalID.equals(""))
			{
				expsurgeon.setHospitalId(hospitalID);
			}
			surgeonModelList = flexibleSearchService.getModelsByExample(expsurgeon);
			if (surgeonModelList != null && !(surgeonModelList.isEmpty()))
			{
				surgeon = surgeonModelList.get(0);
				surgName = JnjGTCoreUtil.formatValuesByCommaSeparated(surgeon.getFirstName(), surgeon.getMiddleName(),
						surgeon.getLastName())
						+ "(" + selectedSurgeonId + ")";
			}
		}
		if (name.equalsIgnoreCase(surgName))
		{
			order.setSurgeon(surgeon);
		}
		else
		{
			order.setSurgeonName(name.toUpperCase());
			order.setSurgeon(null);
			surgName = name.toUpperCase();
		}
		final JnjGTOrderChangeResponseData response = jnjGTReqOrderChangeMapper.mapChangeOrderRequestResponse(order,
				order.getSapOrderNumber(), false);

		if (response.isSapResponseStatus())
		{
			modelService.save(order);
			return surgName;
		}
		return StringUtils.EMPTY;
	}
	
	public OrderData getPriceInquiryOrderDetailsForCode(final String orderCode, final String jnjOrderNo)
	{
		final OrderModel orderModel = sessionService.executeInLocalView(new SessionExecutionBody()
		{
			@Override
			public OrderModel execute()
			{
				return b2bOrderService.getOrderForCode(orderCode);
			}
		}, userService.getAdminUser());
		
		if (orderModel == null)
		{
			throw new UnknownIdentifierException("Order with orderGUID " + orderCode + " not found for current user in current BaseStore");
		} else {
			orderModel.setSapOrderNumber(jnjOrderNo);
		}
		modelService.save(orderModel);
		return getOrderConverter().convert(orderModel);
	}
	
	//AAOL-2420 changes
	@Override
	public String getJnjOrderCreditLimitMsg(String code, Locale loc)
	{
		
		
		return jnjGTOrderService.getJnjOrderCreditLimitMsg(code , loc);
		
	}
	//end of AAOL-2420 changes

}
