/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.mapper.order;


import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Product;
import com.jnj.core.constants.Jnjgtb2bCONSConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjGTInvoiceEntryModel;
import com.jnj.core.model.JnjGTInvoiceModel;
import com.jnj.core.model.JnjGTOrdHdrNoteModel;
import com.jnj.core.model.JnjGTShippingDetailsModel;
import com.jnj.core.model.JnjGTShippingLineDetailsModel;
import com.jnj.core.model.JnjGTSurgeryInfoModel;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.synchronizeOrders.JnjSAPOrdersService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Order;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntOrdHdrNoteModel;
import com.jnj.gt.model.JnjGTIntOrdLineHoldLocalModel;
import com.jnj.gt.model.JnjGTIntOrderHeaderModel;
import com.jnj.gt.model.JnjGTIntOrderLineModel;
import com.jnj.gt.model.JnjGTIntOrderLinePartModel;
import com.jnj.gt.model.JnjGTIntOrderLineTxtModel;
import com.jnj.gt.model.JnjGTIntOrderSchLineModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.order.JnjGTOrderFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * This class maps data from intermediate models to hybris model for Sync Order.
 * 
 * @author Accenture
 * @version 1.0
 */
public class JnjGTOrderSyncDataLoadMapper extends JnjAbstractMapper
{

	private static final Logger LOGGER = Logger.getLogger(JnjGTOrderSyncDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjSAPOrdersService jnjSAPOrdersService;

	@Autowired
	ModelService modelService;

	@Autowired
	private KeyGenerator orderCodeGenerator;

	@Autowired
	JnjGTOrderFeedService jnjGTOrderFeedService;

	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	private CatalogVersionService catalogVersionService;

	@Autowired
	protected JnjGTAddressService jnjGTAddressService;

	@Autowired
	protected UserService userService;

	@Autowired
	private JnjConfigService jnjConfigService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	/**
	 * <code>CatalogVersionModel</code> instance for MDD catalog.
	 */
	private CatalogVersionModel mddCatalogVersion;

	/**
	 * <code>CatalogVersionModel</code> instance for Consumer catalog.
	 */

	private CatalogVersionModel caProdCatalog;

	private CatalogVersionModel consumerCatalogVersion;

	private static final Double DEFAULT_EMPTY_VALUE = Double.valueOf("0.0");

	private final static String CONFIRMED_LINE_STATUS_CODES = Config
			.getParameter(Jnjgtb2binboundserviceConstants.Order.CONFIRMED_SCHEDULE_LINE_STATUS);

	private static final String US_CURRENCY_CODE = "USD";

	private static final String EXPIRY_DATE_PROD_LOT = "PRODUCT LOT - EXPIRATION DATE";

	//Method to invoke the main logic.
	@Override
	public void processIntermediateRecords()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		// Set the catalog versions which will be used to fetch the products
		mddCatalogVersion = catalogVersionService.getCatalogVersion(Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);

		consumerCatalogVersion = catalogVersionService.getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);

		mapOrderData();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method maps Data for Order.
	 */
	public boolean mapOrderData()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		String errorMessage = null;

		// Fetch all those records whose record status is pending.
		final List<JnjGTIntOrderHeaderModel> jnjGTIntOrderHeaderList = (List<JnjGTIntOrderHeaderModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntOrderHeaderModel._TYPECODE, RecordStatus.PENDING);
		if (!jnjGTIntOrderHeaderList.isEmpty())
		{
			for (final JnjGTIntOrderHeaderModel jnjGTIntHeaderModel : jnjGTIntOrderHeaderList)
			{
				recordStatus = false;
				if (StringUtils.isNotBlank(jnjGTIntHeaderModel.getSourceSystemId())
						&& StringUtils.isNotBlank(jnjGTIntHeaderModel.getSapOrderNumber()))
				{
					final String sapOrderNumber = jnjGTIntHeaderModel.getSapOrderNumber();

					OrderModel jnjGTOrder = null;
					// Call To fetch the source system id
					final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntHeaderModel.getSourceSystemId());
					final BaseSiteModel baseSiteModel = jnjGTOrderFeedService.getBaseSiteModelUsingSourceSysId(sourceSysId);
					jnjGTOrder = jnjGTOrderFeedService.getOrderModelUsingSapOrdNoAndBaseSite(sapOrderNumber, baseSiteModel);

					try
					{
						if (jnjGTOrder != null)
						{
							updateExistingOrder(jnjGTOrder, jnjGTIntHeaderModel);
						}
						else
						{
							jnjGTOrder = modelService.create(OrderModel.class);
							createNewOrder(jnjGTIntHeaderModel, jnjGTOrder, baseSiteModel);
						}
						//processSAPOrderEntries(sapOrderNumber, jnjGTOrder, jnjGTIntHeaderModel.getSourceSystemId());


						if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId))
						{
							//processOrderHeaderNote(sapOrderNumber, jnjGTOrder, jnjGTIntHeaderModel.getSourceSystemId());
							populateMddOrderStatus(jnjGTOrder);
							// Populate the other charge
							final double otherCharge = Double.parseDouble(jnjGTIntHeaderModel.getTotalNetValue())
									+ (jnjGTOrder.getTotalHsaPromotion() != null ? jnjGTOrder.getTotalHsaPromotion().doubleValue() : 0.0)
									- (jnjGTOrder.getSubtotal() != null ? jnjGTOrder.getSubtotal().doubleValue() : 0.0)
									- (jnjGTOrder.getTotalFees() != null ? jnjGTOrder.getTotalFees().doubleValue() : 0.0);

							jnjGTOrder.setTotalOtherCharge(Double.valueOf(otherCharge));
							jnjGTOrder.setTotalNetValue((jnjGTIntHeaderModel.getTotalNetValue() != null) ? Double
									.valueOf(jnjGTIntHeaderModel.getTotalNetValue()) : DEFAULT_EMPTY_VALUE);
						}

						// Populate total price
						final double totalPrice = Double.parseDouble(jnjGTIntHeaderModel.getTotalNetValue())
								+ jnjGTOrder.getTotalTax().doubleValue();
						jnjGTOrder.setTotalPrice(Double.valueOf(totalPrice));

						modelService.saveAll(jnjGTOrder);
						recordStatus = true;
					}
					catch (final UnknownIdentifierException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final BusinessException exception)
					{
						LOGGER.error("Processing of Order with order number: " + sapOrderNumber + " has caused an exception: "
								+ exception);
						errorMessage = exception.getMessage();
					}
					catch (final ModelSavingException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final ParseException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Parsing Exception occured -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final Throwable throwable)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
						errorMessage = throwable.getMessage();
					}
				}
				else
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Skipping the record as Source Sytem id or Order Number is null/empty.");
					}
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, null, true, errorMessage, Logging.ORDER_SYNC_FEED,
							jnjGTIntHeaderModel.getSapOrderNumber());
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Order Number " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " was not processed successfully.");
					}

				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	private void processOrderHeaderNote(final String orderNumber, final OrderModel order, final String sourceSystemId)
	{

		final List<JnjGTOrdHdrNoteModel> orderHdrNoteList = new ArrayList<>();
		final List<JnjGTIntOrdHdrNoteModel> jnjGTIntOrdHdrs = jnjGTOrderFeedService.getJnjGTIntOrdHdrNoteModel(orderNumber,
				sourceSystemId);

		JnjGTOrdHdrNoteModel ordHdrNote;
		for (final JnjGTIntOrdHdrNoteModel jnjGTIntOrdHdrNoteModel : jnjGTIntOrdHdrs)
		{

			ordHdrNote = modelService.create(JnjGTOrdHdrNoteModel.class);
			ordHdrNote.setHdrNoteCode(jnjGTIntOrdHdrNoteModel.getHdrNoteCode());
			ordHdrNote.setHdrNotes(jnjGTIntOrdHdrNoteModel.getHdrNotes());
			orderHdrNoteList.add(ordHdrNote);
		}

		modelService.saveAll(orderHdrNoteList);
		//order.setHeaderNotes(orderHdrNoteList);
	}

	/**
	 * Converts an SAP order into a Hybris based order.
	 * 
	 * @param order
	 *           the order
	 * @param sapOrder
	 *           the sap order
	 * @throws ParseException
	 *            the parse exception
	 * @throws ParseException
	 * @throws BusinessException
	 */
	private void updateExistingOrder(final OrderModel order, final JnjGTIntOrderHeaderModel sapOrder) throws ParseException,
			BusinessException
	{
		final String sapOrderNumber = sapOrder.getSapOrderNumber();
		String errorMessage = null;

		order.setSapOrderNumber(sapOrderNumber);
		order.setOrderNumber(sapOrderNumber);
		order.setPurchaseOrderNumber(sapOrder.getPurchaseOrder());
		order.setDealerPONum(sapOrder.getShipToPoNum());
		/*** This overrides the auto generated code from Hybris, should not be set ***/
		//order.setCode(sapOrder.getWebOrderNumber());
		//order.setOrderType(JnjOrderTypesEnum.valueOf(sapOrder.getOrderType()));
		order.setAttention(sapOrder.getShipAttention());
		order.setShipToAccount(sapOrder.getShipToGLN());
		order.setReasonCode(sapOrder.getOrderReasonCode());
		order.setPoType(sapOrder.getPoType());
		order.setSurgeonName(sapOrder.getSurgeonName());
		order.setSpineSalesRepUCN(sapOrder.getRefCustNum());
		order.setSalesOrganizationCode(sapOrder.getSalesOrg());
		order.setDistributionChannel(sapOrder.getDistributionChannel());
		order.setDivision(sapOrder.getDivision());

		// check if address contains delivery address
		if (order.getDeliveryAddress() != null)
		{

			final List<AddressModel> addressModels = jnjGTAddressService.getAddressByIdandOnwerType(sapOrder
					.getShipToCustomerNumber());
			// check if existing address is not as same address in feed
			if (!order.getDeliveryAddress().getJnJAddressId().equals(sapOrder.getShipToCustomerNumber()))
			{
				if (addressModels.size() < 1)
				{
					final AddressModel newAddress = createNewAddress(sapOrder, order);
					order.setDeliveryAddress(newAddress);
				}
				else
				{
					order.setDeliveryAddress(modelService.clone(addressModels.get(0)));
				}
			}
		}
		else
		{
			final AddressModel newAddress = createNewAddress(sapOrder, order);
			order.setDeliveryAddress(newAddress);
		}


		final List<AddressModel> billingAddressModels = jnjGTAddressService.getAddressByIdandOnwerType(sapOrder.getBillToAccount());
		if (billingAddressModels.size() > 0)
		{
			order.setPaymentAddress(billingAddressModels.get(0));
		}

		if (!StringUtils.isEmpty(sapOrder.getReqDeliveryDate()))
		{
			order.setNamedDeliveryDate(parseDate(sapOrder.getReqDeliveryDate(), JnjGTIntOrderHeaderModel.REQDELIVERYDATE));
		}

		if (StringUtils.isEmpty(sapOrder.getCreationDate()))
		{
			errorMessage = "No Creation date available for Order Number: " + sapOrderNumber + ", couldn't process the order.";
			throw new BusinessException(errorMessage);
		}
		else
		{
			order.setDate(parseDate(sapOrder.getCreationDate(), JnjGTIntOrderHeaderModel.CREATIONDATE));
		}

		final String fetchedSourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(sapOrder.getSourceSystemId());

		/***
		 * Based on recent clarification received, for MDD <code>JnjGTIntOrdLinePriceLocal</code> is looked up for the
		 * currency iso code.
		 ***/
		String currencyIsoCode = null;

		if (JnjGTSourceSysId.MDD.toString().equals(fetchedSourceSysId))
		{
			currencyIsoCode = jnjGTOrderFeedService.getCurrencyFromOrdLinePriceLocal(sapOrderNumber, sapOrder.getSourceSystemId());
		}
		else
		{
			currencyIsoCode = sapOrder.getCurrency();
		}
		if (currencyIsoCode != null)
		{
			order.setCurrency(getCurrencyFromIsoCode(currencyIsoCode));
		}
		else
		{
			errorMessage = "CURRENCY ISO CODE IS NULL FOR THE SAP ORDER NUMBER: " + sapOrder.getSapOrderNumber()
					+ ", CANNOT PROCESS THE RECORD.";
			throw new BusinessException(errorMessage);
		}

		// Based on the source system id, calculate taxes

		if (JnjGTSourceSysId.CONSUMER.getCode().equals(fetchedSourceSysId))
		{
			final OrderStatus orderStatus = order.getStatus();
			final OrderStatus newOrderStatus = jnjSAPOrdersService.getOrderStatus(sapOrder.getOverAllStatus(),
					sapOrder.getRejectionStatus(), sapOrder.getCreditStatus(), sapOrder.getDeliveryStatus(),
					sapOrder.getInvoiceStatus());

			order.setStatus(newOrderStatus);

			/***
			 * Set Email Notification flag (so as to be picked in Shipment Notification cronjob), if new order status is
			 * different from previous and corresponds to COMPLETED
			 ***/
			if ((orderStatus == null || !orderStatus.equals(newOrderStatus))
					&& (newOrderStatus != null && OrderStatus.COMPLETED.equals(newOrderStatus)))
			{
				order.setSendOrderShipmentEmail(true);
				order.setShipmentEmailPreference(true);
			}
		}

		order.setDeliveryStatus((sapOrder.getDeliveryStatus() != null) ? DeliveryStatus.valueOf(sapOrder.getDeliveryStatus())
				: null);
		order.setInvoiceStatus(sapOrder.getInvoiceStatus());

		order.setThirdpartyBilling((sapOrder.getDropShipInd() != null) ? Boolean.valueOf((sapOrder.getDropShipInd()))
				: Boolean.FALSE);

		if (sapOrder.getSurgeonType() != null)
		{

			JnjGTSurgeryInfoModel surgeryInfo = order.getSurgeryInfo();
			if (surgeryInfo == null)
			{

				surgeryInfo = modelService.create(JnjGTSurgeryInfoModel.class);
			}

			try
			{
				surgeryInfo.setProcedureType(sapOrder.getSurgeonType());
				surgeryInfo.setCaseDate(parseDate(sapOrder.getCaseDate(), JnjGTIntOrderHeaderModel.CASEDATE));
				modelService.save(surgeryInfo);
			}
			catch (final ModelSavingException exception)
			{
				LOGGER.error("EXCEPTION  WHILE SAVING SURGERY INFO FOR SAP ORDER NUMBER: " + sapOrderNumber + ". Exception: "
						+ exception.getMessage());
			}
		}


		final JnJB2BUnitModel b2bUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid("00080888", fetchedSourceSysId);
		/*
		 * if (b2bUnitModel == null) { errorMessage = "NO B2B Unit found with Sold To Account Number: " +
		 * sapOrder.getSoldToAccount() + ", and Source Sys ID: " + sapOrder.getSourceSystemId(); throw new
		 * BusinessException(errorMessage); }
		 */
		if (b2bUnitModel != null)
		{
			order.setUnit(b2bUnitModel);
		}
	}

	/**
	 * Creates new address based on <code>AddressModel</code> instance based on the inputs.
	 * 
	 * @param sapOrder
	 * @param order
	 * @return AddressModel
	 */
	private AddressModel createNewAddress(final JnjGTIntOrderHeaderModel sapOrder, final OrderModel order)
	{
		final AddressModel addressModel = modelService.create(AddressModel.class);
		addressModel.setJnJAddressId(sapOrder.getShipToCustomerNumber());
		addressModel.setFirstname(sapOrder.getShipToName());
		addressModel.setLine1(sapOrder.getShipStreet1());
		addressModel.setLine2(sapOrder.getShipStreet2());
		addressModel.setTown(sapOrder.getShipCity());

		if (StringUtils.isNotEmpty(sapOrder.getShipCountry()))
		{
			final CountryModel country = commonI18NService.getCountry(sapOrder.getShipCountry());
			addressModel.setCountry(country);

			/*
			 * if (StringUtils.isNotEmpty(sapOrder.getShipState())) {
			 * addressModel.setRegion(commonI18NService.getRegion(country, sapOrder.getShipState())); }
			 */
		}
		addressModel.setPostalcode(sapOrder.getShipPostalCode());
		addressModel.setGlobalLocNo(sapOrder.getShipToGLN());
		addressModel.setOwner(order);
		return addressModel;
	}

	/**
	 * Creates a new Hybris order based on the SAP Order, associated entries and schedule lines.
	 * 
	 * @param sapOrder
	 * @throws ParseException
	 * @throws BusinessException
	 */
	private void createNewOrder(final JnjGTIntOrderHeaderModel sapOrder, final OrderModel newOrder,
			final BaseSiteModel baseSiteModel) throws ParseException, BusinessException
	{
		newOrder.setCode(generateOrderCode());
		newOrder.setSite(baseSiteModel);
		newOrder.setStore(baseSiteModel.getStores().get(0));
		newOrder.setUser(userService.getUserForUID(Order.SAP_ONLY_USER));

		updateExistingOrder(newOrder, sapOrder);
	}

	/**
	 * Generate a code for created order. Default implementation use {@link KeyGenerator}.
	 * 
	 * @return String
	 */
	protected String generateOrderCode()
	{
		final Object generatedValue = orderCodeGenerator.generate();
		if (generatedValue instanceof String)
		{
			return (String) generatedValue;
		}
		else
		{
			return String.valueOf(generatedValue);
		}
	}


	/*
	 * private void processSAPOrderEntries(final String sapOrderNumber, final OrderModel order, final String sourceSysId)
	 * throws BusinessException, ParseException
	 */

	private void processSAPOrderEntries(final String sapOrderNumber, final OrderModel order, final String sourceSysId,
			final Double cancelledLineItemPrice, final Date recordTimeStamp) throws BusinessException, ParseException
	{
		final List<AbstractOrderEntryModel> existingOrderEntries = order.getEntries();

		List<JnjGTIntOrderLineModel> jnjGTIntOrderLines = null;

		final String fetchedSourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(sourceSysId);

		// If consumer fetch order lines based on item category

		/*
		 * if (JnjGTSourceSysId.MDD.toString().equals(fetchedSourceSysId)) { jnjGTIntOrderLines =
		 * jnjGTOrderFeedService.getJnjGTIntOrderLineModel(sapOrderNumber, sourceSysId, null, null); }
		 */

		if (JnjGTSourceSysId.CONSUMER.toString().equals(fetchedSourceSysId))
		{
			final ArrayList<String> validItemCategories = new ArrayList<>();

			validItemCategories.addAll(JnJCommonUtil.getValues(Jnjgtb2bCONSConstants.OrderFeed.ITEM_CATEGORY,
					Jnjb2bCoreConstants.SYMBOl_COMMA));
			validItemCategories.addAll(JnJCommonUtil.getValues(Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN,
					Jnjb2bCoreConstants.SYMBOl_COMMA));

			jnjGTIntOrderLines = jnjGTOrderFeedService.getJnjGTIntOrderLineModel(sapOrderNumber, sourceSysId, validItemCategories,
					null);
		}

		if (CollectionUtils.isNotEmpty(jnjGTIntOrderLines))
		{
			final List<AbstractOrderEntryModel> updatedOrderEntries = new ArrayList();
			final Map<String, AbstractOrderEntryModel> existingOrderLinesMap = new HashMap();
			double dropShipFee = 0;
			double hsaPromotion = 0;
			double subtotal = 0;
			double manualFee = 0;
			double minimumFee = 0;
			double expeditedFee = 0;
			double totalTax = 0;
			double netPrice = 0;
			double freight = 0;
			if (CollectionUtils.isNotEmpty(existingOrderEntries))
			{
				for (final AbstractOrderEntryModel existingEntry : existingOrderEntries)
				{
					existingOrderLinesMap.put(existingEntry.getSapOrderlineNumber(), existingEntry);
				}
			}

			for (final JnjGTIntOrderLineModel intOrderLine : jnjGTIntOrderLines)
			{
				final AbstractOrderEntryModel orderEntry;

				final List<String> validConsItemCategories = JnJCommonUtil.getValues(
						Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY, Jnjb2bCoreConstants.SYMBOl_COMMA);

				final List<String> itemCategoriesForTan = JnJCommonUtil.getValues(
						Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN, Jnjb2bCoreConstants.SYMBOl_COMMA);

				final List<String> highLevelItemValues = JnJCommonUtil.getValues(
						Jnjb2bCoreConstants.Order.CONSUMER_ORDER_LINE_HIGH_LEVEL_VALUE, Jnjb2bCoreConstants.SYMBOl_COMMA);

				final String itemCategory = intOrderLine.getItemCategory();


				if ((JnjGTSourceSysId.CONSUMER.toString().equals(fetchedSourceSysId) && (validConsItemCategories
						.contains(itemCategory) || (itemCategoriesForTan.contains(itemCategory) && (null == intOrderLine
						.getHighLevelItemNumber() || highLevelItemValues.contains(intOrderLine.getHighLevelItemNumber())))))

						|| (JnjGTSourceSysId.MDD.toString().equals(fetchedSourceSysId) && !(JnJCommonUtil.getValues(
								Jnjgtb2boutboundserviceConstants.ITEM_CATEGORY_FOR_MDD, Jnjb2bCoreConstants.SYMBOl_COMMA))
								.contains(itemCategory)))
				{

					if (JnjGTSourceSysId.CONSUMER.toString().equals(fetchedSourceSysId))
					{
						// Populate the order line prices
						populatePrice(intOrderLine);
					}

					// Check if orderEntry is already present for order line number, if yes fetch and update it
					if (existingOrderLinesMap.containsKey(intOrderLine.getSapOrderLineNumber()))
					{
						orderEntry = existingOrderLinesMap.get(intOrderLine.getSapOrderLineNumber());
					}
					else
					{
						orderEntry = modelService.create(OrderEntryModel.class);
						orderEntry.setOrder(order);
						orderEntry.setSapOrderlineNumber(intOrderLine.getSapOrderLineNumber());
					}

					// Update the order entry
					updateOrderEntry(intOrderLine, orderEntry);
					updatedOrderEntries.add(orderEntry);

					// Get all the schedule lines that need to be updated for the orderEntry
					final List<JnjGTIntOrderSchLineModel> intOrderSchLines = jnjGTOrderFeedService.getJnjGTIntOrderSchLineModel(
							sapOrderNumber, intOrderLine.getSourceSystemId(), intOrderLine.getSapOrderLineNumber());

					// Update schedule lines
					processOrderSchLines(orderEntry, intOrderSchLines, sapOrderNumber);

					/*** Process other associated child table only when order source is MDD based ***/

					if (JnjGTSourceSysId.MDD.toString().equals(fetchedSourceSysId))
					{
						// Get all the schedule lines that need to be updated for the orderEntry
						final List<JnjGTIntOrdLineHoldLocalModel> intOrderLinesHoldLocal = jnjGTOrderFeedService
								.getJnjGTIntOrdLineHoldLocalModel(sapOrderNumber, intOrderLine.getSourceSystemId(),
										intOrderLine.getSapOrderLineNumber(), null);

						// Update order lines hold
						if (intOrderLinesHoldLocal.size() > 0)
						{
							orderEntry.setHoldCode(intOrderLinesHoldLocal.get(0).getHoldCode());
						}


						// Get all the order line text that need to be updated for the orderEntry
						final List<JnjGTIntOrderLineTxtModel> intOrderLinesTxt = jnjGTOrderFeedService.getJnjGTIntOrderLineTxtModel(
								sapOrderNumber, intOrderLine.getSourceSystemId());

						// Update order lines text
						if (intOrderLinesTxt.size() > 0)
						{
							orderEntry.setLot(intOrderLinesTxt.get(0).getTxtDesc());
						}

						// Get all the order line part that need to be updated for the orderEntry
						final List<JnjGTIntOrderLinePartModel> intOrderLinesPart = jnjGTOrderFeedService.getJnjGTIntOrderLinePartModel(
								sapOrderNumber, intOrderLine.getSourceSystemId());

						// Update order lines text
						if (intOrderLinesPart.size() > 0)
						{
							orderEntry.setSpecialStockPartner(intOrderLinesPart.get(0).getCustomerNum());
						}

						populateMddOrderEntryStatus(order, orderEntry);
					}

					dropShipFee = (orderEntry.getDropshipFee() != null) ? dropShipFee + orderEntry.getDropshipFee().doubleValue()
							: dropShipFee;
					hsaPromotion = (orderEntry.getHsaPromotion() != Double.valueOf(0.0)) ? hsaPromotion
							+ orderEntry.getHsaPromotion().doubleValue() : hsaPromotion;
					subtotal = (orderEntry.getTotalPrice() != null) ? subtotal + orderEntry.getTotalPrice().doubleValue() : subtotal;
					netPrice = (orderEntry.getDefaultPrice() != null) ? netPrice + orderEntry.getDefaultPrice().doubleValue()
							: netPrice;
					manualFee = (orderEntry.getManualFee() != Double.valueOf(0.0)) ? manualFee
							+ orderEntry.getManualFee().doubleValue() : manualFee;
					expeditedFee = (orderEntry.getExpeditedFee() != null) ? expeditedFee + orderEntry.getExpeditedFee().doubleValue()
							: expeditedFee;
					minimumFee = (orderEntry.getMinimumOrderFee() != null) ? minimumFee
							+ orderEntry.getMinimumOrderFee().doubleValue() : minimumFee;
					totalTax = (orderEntry.getTaxes() != null) ? totalTax + orderEntry.getTaxes().doubleValue() : totalTax;
					freight = (orderEntry.getFreightFees() != null) ? freight + orderEntry.getFreightFees().doubleValue() : freight;

					modelService.save(orderEntry);
				}

				// Update the order entries in order
				order.setEntries(updatedOrderEntries);
				// Set the sum of all the totals at order level
				order.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
				order.setTotalDropShipFee(Double.valueOf(dropShipFee));
				order.setTotalAdjRateAll(Double.valueOf(subtotal - netPrice));
				order.setSubtotal(Double.valueOf(subtotal));
				order.setTotalExpeditedFees(Double.valueOf(expeditedFee));
				order.setTotalminimumOrderFee(Double.valueOf(minimumFee));
				order.setTotalmanualFee(Double.valueOf(manualFee));
				order.setTotalTax(Double.valueOf(totalTax));
				order.setDeliveryCost(Double.valueOf(freight));
				order.setTotalFees(Double.valueOf(dropShipFee + minimumFee));

				if (JnjGTSourceSysId.CONSUMER.toString().equals(fetchedSourceSysId))
				{
					order.setTotalNetValue(Double.valueOf(netPrice));
				}
			}
		}
		else
		{
			throw new BusinessException("NO ORDER LINES PRESENT FOR ORDER NUMBER: " + sapOrderNumber);
		}
	}

	private void populatePrice(final JnjGTIntOrderLineModel intOrderLine)
	{
		List<JnjGTIntOrderLineModel> jnjGTIntOrderLines = null;
		OrderEntryStatus firstTanLineStatus = null;
		OrderEntryStatus secondTanLineStatus = null;
		int tanLineCount = 0;
		// If consumer fetch order lines based on item category
		final List<String> itemCategoriesForTapa = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN, Jnjb2bCoreConstants.SYMBOl_COMMA);
		jnjGTIntOrderLines = jnjGTOrderFeedService.getJnjGTIntOrderLineModel(intOrderLine.getSapOrderNumber(),
				intOrderLine.getSourceSystemId(), null, intOrderLine.getSapOrderLineNumber());


		// If order lines is not empty
		if (CollectionUtils.isNotEmpty(jnjGTIntOrderLines))
		{
			double subtotal3 = 0, tax = 0, netValue = 0;
			for (final JnjGTIntOrderLineModel orderLine : jnjGTIntOrderLines)
			{
				subtotal3 += Double.parseDouble(orderLine.getSubTotal3());
				tax += Double.parseDouble(orderLine.getTaxAmt());
				netValue += Double.parseDouble(orderLine.getNetValue());
				// check if the item category is TAN or ZHOR then enter inside if block.
				if (itemCategoriesForTapa.contains(orderLine.getItemCategory()))
				{
					// find out the first tan line status using the status fields.
					if (null == firstTanLineStatus && tanLineCount < 1)
					{
						firstTanLineStatus = jnjSAPOrdersService.getOrderEntryStatus(orderLine.getOverAllStatus(), StringUtils
								.isNotEmpty(orderLine.getReasonForRejection()) ? Jnjb2bCoreConstants.Y_STRING
								: Jnjb2bCoreConstants.N_STRING, orderLine.getDeliveryStatus(), orderLine.getInvoiceStatus(),
								Jnjb2bCoreConstants.N_STRING);
					}// find out the second tan line status using the status fields.
					else if (null == secondTanLineStatus && tanLineCount < 2)
					{
						secondTanLineStatus = jnjSAPOrdersService.getOrderEntryStatus(orderLine.getOverAllStatus(), StringUtils
								.isNotEmpty(orderLine.getReasonForRejection()) ? Jnjb2bCoreConstants.Y_STRING
								: Jnjb2bCoreConstants.N_STRING, orderLine.getDeliveryStatus(), orderLine.getInvoiceStatus(),
								Jnjb2bCoreConstants.N_STRING);
					}
					tanLineCount++;
				}
			}
			// if the first tan line status and second tan line status is equal then set the same in the consumer line status.
			if (null != firstTanLineStatus && null == secondTanLineStatus)
			{
				intOrderLine.setConsumerLineStatus(firstTanLineStatus);
			}// if the first tan line status and second tan line status is not equal then get the status using the tan order entry status model and set it in consumer line status.
			else if (null != firstTanLineStatus && null != secondTanLineStatus)
			{
				final OrderEntryStatus orderEntryStatus = jnjGTOrderFeedService.getTanOrderLineStatus(firstTanLineStatus.getCode(),
						secondTanLineStatus.getCode());
				if (null != orderEntryStatus)
				{
					intOrderLine.setConsumerLineStatus(orderEntryStatus);
				}
			}
			else
			{
				intOrderLine.setConsumerLineStatus(secondTanLineStatus);
			}
			intOrderLine.setSubTotal3(String.valueOf(subtotal3));
			intOrderLine.setTaxAmt(String.valueOf(tax));
			intOrderLine.setNetValue(String.valueOf(netValue));
		}
	}

	private void updateOrderEntry(final JnjGTIntOrderLineModel jnjGTIntOrderLine, final AbstractOrderEntryModel orderEntry)
			throws BusinessException
	{
		final String sapOrderNumber = jnjGTIntOrderLine.getSapOrderNumber();

		final boolean isMddRecord = JnjGTSourceSysId.MDD.toString().equals(
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntOrderLine.getSourceSystemId())) ? true : false;
		// Fetch the catalog version in which product needs to be searched or fetched
		final CatalogVersionModel catalogVersionModel = consumerCatalogVersion;
		JnJProductModel product = null;
		try
		{
			product = getProductForOrderEntry(orderEntry, catalogVersionModel, jnjGTIntOrderLine, sapOrderNumber, isMddRecord);
		}
		catch (final UnknownIdentifierException exception)
		{
			throw new BusinessException("No product found for order entry line number: " + jnjGTIntOrderLine.getSapOrderLineNumber()
					+ " | Sap Order Number: " + sapOrderNumber + "Exception: " + exception.getMessage());
		}

		/*** If the found product itself is a base, then set it otherwise set base product from it. ***/
		if (product.getMaterialBaseProduct() == null)
		{
			orderEntry.setProduct(product);
		}
		else
		{
			orderEntry.setProduct(product.getMaterialBaseProduct());
		}

		/*** Populate the reference Variant ***/
		orderEntry.setReferencedVariant(jnJGTProductService.getDeliveryGTIN(product));

		// Get unit model if available
		final UnitModel unit = getUnitOfMeasurement(jnjGTIntOrderLine.getSalesUom(), null);
		if (unit == null)
		{
			throw new BusinessException("Could not find unit corresponding to the unit code: " + jnjGTIntOrderLine.getSalesUom()
					+ ", Line Number: " + jnjGTIntOrderLine.getSapOrderLineNumber());
		}
		orderEntry.setUnit(unit);
		try
		{
			final Double qty = (jnjGTIntOrderLine.getQty() != null) ? Double.valueOf(jnjGTIntOrderLine.getQty()) : Double
					.valueOf(0.0);
			orderEntry.setQuantity(Long.valueOf(qty.longValue()));
		}
		catch (final NumberFormatException exception)
		{
			LOGGER.error("Exception while parsing line quantity of line Number: " + jnjGTIntOrderLine.getSapOrderLineNumber());
			throw new BusinessException(exception.getMessage());
		}
		try
		{
			final double subTotal = Double.parseDouble(jnjGTIntOrderLine.getSubTotal3());
			final double quantity = (jnjGTIntOrderLine.getQty() != null) ? Double.parseDouble(jnjGTIntOrderLine.getQty()) : 0.0;
			final long qty = (quantity != 0.0) ? Double.valueOf(quantity).longValue() : 1;

			orderEntry.setBasePrice(Double.valueOf(subTotal / qty));
			orderEntry.setTotalPrice((jnjGTIntOrderLine.getSubTotal3() != null) ? Double.valueOf(jnjGTIntOrderLine.getSubTotal3())
					: Double.valueOf(0.0));

			if (isMddRecord)
			{
				orderEntry.setDropshipFee((jnjGTIntOrderLine.getSubTotal2() != null) ? Double.valueOf(jnjGTIntOrderLine
						.getSubTotal2()) : Double.valueOf(0));
				orderEntry.setHsaPromotion((jnjGTIntOrderLine.getSubTotal5() != null) ? Double.valueOf(jnjGTIntOrderLine
						.getSubTotal5()) : Double.valueOf(0));
			}
			else
			{
				orderEntry
						.setDefaultPrice((jnjGTIntOrderLine.getNetValue() != null) ? Double.valueOf(jnjGTIntOrderLine.getNetValue())
								: Double.valueOf(0.0));
			}
		}
		catch (final Exception exception)
		{
			LOGGER.error("Exception while setting either of Base/Total/Default/Drop-ship/HSA Promotion price for Order Number: "
					+ sapOrderNumber + " | Order Line Number: " + jnjGTIntOrderLine.getSapOrderLineNumber() + ". Exception: "
					+ exception.getMessage());
		}

		// Placeholder for currency and overallStatus
		orderEntry.setReasonForRejection(jnjGTIntOrderLine.getReasonForRejection());

		/** Set rejected flag **/
		if (StringUtils.isNotEmpty(jnjGTIntOrderLine.getReasonForRejection()))
		{
			orderEntry.setRejected(Boolean.TRUE);
		}
		else
		{
			orderEntry.setRejected(Boolean.FALSE);
		}

		// Placeholder for plant and plant name
		orderEntry.setItemCategory(jnjGTIntOrderLine.getItemCategory());
		orderEntry.setHigherLevelItemNo(jnjGTIntOrderLine.getHighLevelItemNumber());
		orderEntry.setBatchNum(jnjGTIntOrderLine.getBatchNumber());
		orderEntry.setPlant(jnjGTIntOrderLine.getPlant());
		if (isMddRecord)
		{
			// Populate freight and handling charges
			orderEntry.setFreightFees(jnjGTOrderFeedService.getPriceJnjGTIntOrdLineHoldLocal(sapOrderNumber,
					jnjGTIntOrderLine.getSapOrderLineNumber(), jnjGTIntOrderLine.getSourceSystemId(),
					JnJCommonUtil.getValue(Order.PRC_COND_TYPE_FREIGHT_FEE).split(Jnjgtb2binboundserviceConstants.COMMA_STRING)));

			// Populate manual fee
			orderEntry.setManualFee(jnjGTOrderFeedService.getPriceJnjGTIntOrdLineHoldLocal(sapOrderNumber,
					jnjGTIntOrderLine.getSapOrderLineNumber(), jnjGTIntOrderLine.getSourceSystemId(),
					JnJCommonUtil.getValue(Order.PRC_COND_TYPE_MANUAL_FEE).split(Jnjgtb2binboundserviceConstants.COMMA_STRING)));
			// Populate minimum fee
			orderEntry.setMinimumOrderFee(jnjGTOrderFeedService.getPriceJnjGTIntOrdLineHoldLocal(sapOrderNumber,
					jnjGTIntOrderLine.getSapOrderLineNumber(), jnjGTIntOrderLine.getSourceSystemId(),
					JnJCommonUtil.getValue(Order.PRC_COND_TYPE_MIN_FEE).split(Jnjgtb2binboundserviceConstants.COMMA_STRING)));
			// Populate expidited fee
			orderEntry.setExpeditedFee(jnjGTOrderFeedService.getPriceJnjGTIntOrdLineHoldLocal(sapOrderNumber,
					jnjGTIntOrderLine.getSapOrderLineNumber(), jnjGTIntOrderLine.getSourceSystemId(),
					JnJCommonUtil.getValue(Order.PRC_COND_TYPE_EXPEDIT_FEE).split(Jnjgtb2binboundserviceConstants.COMMA_STRING)));

			final Date expirationDate = jnJGTProductService.getProductLotExpirationDate(product, jnjGTIntOrderLine.getBatchNumber());
			orderEntry.setExpiryDate(expirationDate);
		}

		orderEntry.setRoute(jnjGTIntOrderLine.getRoute());
		orderEntry.setReturnInvNumber(jnjGTIntOrderLine.getReturnInvNumber());
		orderEntry.setContractNum(jnjGTIntOrderLine.getContractNum());
		orderEntry.setPriceOverrideReason(jnjGTIntOrderLine.getPriceOverrideReasonCd());
		orderEntry.setApprover(jnjGTIntOrderLine.getApproverNameCd());
		orderEntry.setShipmentLoc(jnjGTIntOrderLine.getPlantName());



		// Based on the source system id, calculate taxes
		final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntOrderLine.getSourceSystemId());
		// Call mdd and cons specific order entry update methods

		if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId))
		{
			mddSpecificOrderEntryUpdates(jnjGTIntOrderLine, orderEntry);
		}

		else if (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSysId))
		{
			consSpecificOrderEntryUpdates(jnjGTIntOrderLine, orderEntry);
		}
	}

	private void processOrderSchLines(final AbstractOrderEntryModel orderEntry,
			final List<JnjGTIntOrderSchLineModel> intOrderSchLines, final String sapOrderNumber) throws BusinessException
	{
		final List<JnjDeliveryScheduleModel> orderEntrySchLines = new ArrayList<>();
		try
		{
			for (final JnjGTIntOrderSchLineModel jnjGTIntOrderSchLineModel : intOrderSchLines)
			{
				final JnjDeliveryScheduleModel jnjGTDeliverySchedule = modelService.create(JnjDeliveryScheduleModel.class);
				jnjGTDeliverySchedule.setOwnerEntry(orderEntry);
				jnjGTDeliverySchedule.setScheduledLineNumber(jnjGTIntOrderSchLineModel.getSchLineNumber());
				jnjGTDeliverySchedule.setLineStatus(jnjGTIntOrderSchLineModel.getLineStatus());

				if (jnjGTIntOrderSchLineModel.getDeliveryDate() != null)
				{
					jnjGTDeliverySchedule.setDeliveryDate(parseDate(jnjGTIntOrderSchLineModel.getDeliveryDate(),
							JnjGTIntOrderSchLineModel.DELIVERYDATE));
				}

				final Double qty = (jnjGTIntOrderSchLineModel.getConfirmedQty() != null) ? Double.valueOf(jnjGTIntOrderSchLineModel
						.getConfirmedQty()) : Double.valueOf(0);
				jnjGTDeliverySchedule.setQty(Long.valueOf(qty.longValue()));

				if (jnjGTIntOrderSchLineModel.getSchedShipDate() != null)
				{
					jnjGTDeliverySchedule.setShipDate(parseDate(jnjGTIntOrderSchLineModel.getSchedShipDate(),
							JnjGTIntOrderSchLineModel.SCHEDSHIPDATE));
				}
				jnjGTDeliverySchedule.setDeliveryLineBlock(jnjGTIntOrderSchLineModel.getDeliveryLineBlock());
				orderEntrySchLines.add(jnjGTDeliverySchedule);
			}
		}
		catch (final NumberFormatException exception)
		{
			throw new BusinessException("Exception while parsing values for Scheudle Line for Order Line Number: "
					+ orderEntry.getSapOrderlineNumber() + ", and Order Number: " + sapOrderNumber);
		}

		modelService.saveAll(orderEntrySchLines);
		orderEntry.setDeliverySchedules(orderEntrySchLines);
	}

	private void mddSpecificOrderEntryUpdates(final JnjGTIntOrderLineModel jnjGTIntOrderLine,
			final AbstractOrderEntryModel orderEntry)
	{
		orderEntry.setTaxes(jnjGTOrderFeedService.getPriceJnjGTIntOrdLineHoldLocal(jnjGTIntOrderLine.getSapOrderNumber(),
				jnjGTIntOrderLine.getSapOrderLineNumber(), jnjGTIntOrderLine.getSourceSystemId(),
				JnJCommonUtil.getValue(Order.PRC_COND_TYPE_MDD_TAX).split(Jnjgtb2binboundserviceConstants.COMMA_STRING)));
	}

	private void consSpecificOrderEntryUpdates(final JnjGTIntOrderLineModel jnjGTIntOrderLine,
			final AbstractOrderEntryModel orderEntry)
	{
		if (StringUtils.isNotEmpty(jnjGTIntOrderLine.getTaxAmt()))
		{
			orderEntry.setTaxes(Double.valueOf(jnjGTIntOrderLine.getTaxAmt()));
		}
		if (null != jnjGTIntOrderLine.getConsumerLineStatus())
		{
			orderEntry.setQuantityStatus(jnjGTIntOrderLine.getConsumerLineStatus());
		}
		else
		{
			orderEntry.setQuantityStatus(jnjSAPOrdersService.getOrderEntryStatus(jnjGTIntOrderLine.getOverAllStatus(), StringUtils
					.isNotEmpty(jnjGTIntOrderLine.getReasonForRejection()) ? Jnjb2bCoreConstants.Y_STRING
					: Jnjb2bCoreConstants.N_STRING, jnjGTIntOrderLine.getDeliveryStatus(), jnjGTIntOrderLine.getInvoiceStatus(),
					Jnjb2bCoreConstants.N_STRING));
		}
		if (null != orderEntry.getQuantityStatus())
		{
			orderEntry.setStatus(orderEntry.getQuantityStatus().getCode());
		}
	}

	/**
	 * 
	 * @param orderModel
	 */
	public void populateMddOrderStatus(final OrderModel orderModel)
	{
		OrderStatus orderStatus = null;

		int shippedStatusCount = 0;
		int cancelledStatusCount = 0;
		int confirmedStatusCount = 0;
		int acceptedStatusCount = 0;
		int backorderedStatusCount = 0;
		int pendingStatusCount = 0;
		final int totalEntries = orderModel.getEntries().size();

		for (final AbstractOrderEntryModel orderEntry : orderModel.getEntries())
		{
			final OrderEntryStatus orderLineStatus = (orderEntry.getStatus() != null) ? OrderEntryStatus.valueOf(orderEntry
					.getStatus()) : null;

			if (OrderEntryStatus.INVOICED.equals(orderLineStatus))
			{
				orderStatus = OrderStatus.INVOICED;
				break;
			}
			else if (OrderEntryStatus.SHIPPED.equals(orderLineStatus))
			{
				shippedStatusCount++;
			}
			else if (OrderEntryStatus.CANCELLED.equals(orderLineStatus))
			{
				cancelledStatusCount++;
			}
			else if (OrderEntryStatus.CONFIRMED.equals(orderLineStatus))
			{
				confirmedStatusCount++;
			}
			else if (OrderEntryStatus.ITEM_ACCEPTED.equals(orderLineStatus))
			{
				acceptedStatusCount++;
			}
			else if (OrderEntryStatus.BACKORDERED.equals(orderLineStatus))
			{
				backorderedStatusCount++;
			}

			else if (OrderEntryStatus.PENDING.equals(orderLineStatus))
			{
				pendingStatusCount++;
			}
		}

		if (orderStatus == null)
		{
			if (shippedStatusCount != 0 && shippedStatusCount == (totalEntries - cancelledStatusCount))
			{
				orderStatus = OrderStatus.SHIPPED;

			}
			else if (shippedStatusCount > 0)
			{
				orderStatus = OrderStatus.PARTIALLY_SHIPPED;
			}
			else if (confirmedStatusCount > 0)
			{
				orderStatus = OrderStatus.RELEASED;
			}
			else if (acceptedStatusCount > 0)
			{
				orderStatus = OrderStatus.ACCEPTED;
			}
			else if (backorderedStatusCount != 0 && backorderedStatusCount == (totalEntries - cancelledStatusCount))
			{
				orderStatus = OrderStatus.BACKORDERED;
			}
			else if (cancelledStatusCount == totalEntries)
			{
				orderStatus = OrderStatus.CANCELLED;
			}
			else if (pendingStatusCount == totalEntries)
			{
				orderStatus = OrderStatus.PENDING;
			}
			else
			{
				orderStatus = OrderStatus.INCOMPLETE;
			}
		}
		orderModel.setStatus(orderStatus);
	}

	/**
	 * 
	 * @param orderModel
	 * @param orderEntryModel
	 */
	public void populateMddOrderEntryStatus(final OrderModel orderModel, final AbstractOrderEntryModel orderEntryModel)
	{
		OrderEntryStatus orderEntryStatus = OrderEntryStatus.PENDING;// changed from null to PENDING as default value for orderEntryStatus should be PENDING if there is no match in Hybris for the order.
		boolean lineStatusFound = false;
		final String orderLineNumber = orderEntryModel.getSapOrderlineNumber();
		final String productDivision = (orderEntryModel.getProduct() != null) ? ((JnJProductModel) orderEntryModel.getProduct())
				.getSalesOrgCode() : null;
		final int schLinesCount = (orderEntryModel.getDeliverySchedules() != null) ? orderEntryModel.getDeliverySchedules().size()
				: 0;

		if (!CollectionUtils.isEmpty(orderModel.getInvoices()))
		{
			final boolean isDeliveredOrderType = JnjOrderTypesEnum.ZDEL.equals(orderModel.getOrderType());
			final boolean isTrackingExists = (orderModel.getShippingDetails() != null) ? isTrackingExistsForOrderLine(
					orderModel.getShippingDetails(), orderLineNumber) : false;

			if (isDeliveredOrderType && schLinesCount == 1)
			{
				//The logic for line number check of invoice entry and order line
				final JnjDeliveryScheduleModel singleDeliveryScheduleLine = orderEntryModel.getDeliverySchedules().get(0);
				singleDeliveryScheduleLine.setLineStatus(OrderEntryStatus.INVOICED.toString());
				modelService.save(singleDeliveryScheduleLine);
				orderEntryStatus = OrderEntryStatus.INVOICED;
				lineStatusFound = true;
			}
			else
			{

				for (final JnjGTInvoiceModel invoice : orderModel.getInvoices())
				{

					for (final JnjGTInvoiceEntryModel invoiceEntry : invoice.getEntries())
					{
						if (invoiceEntry.getLineNum().equals(orderLineNumber) && isTrackingExists && schLinesCount > 0)
						{
							for (final JnjDeliveryScheduleModel deliveryScheduleLine : orderEntryModel.getDeliverySchedules())
							{
								final Long invoicedQty = (invoiceEntry.getQty() != null) ? invoiceEntry.getQty() : Long.valueOf(0);
								if (schLinesCount == 1
										|| (deliveryScheduleLine.getQty() != null && deliveryScheduleLine.getQty().equals(invoicedQty)))
								{
									deliveryScheduleLine.setLineStatus(OrderEntryStatus.INVOICED.toString());
									modelService.save(deliveryScheduleLine);
									lineStatusFound = true;
								}
							}
						}
					}
					if (lineStatusFound)
					{
						orderEntryStatus = OrderEntryStatus.INVOICED;
						break;
					}
				}
			}
		}

		if (!lineStatusFound && !CollectionUtils.isEmpty(orderModel.getShippingDetails()))
		{

			for (final JnjGTShippingDetailsModel shippinDetailModel : orderModel.getShippingDetails())
			{

				for (final JnjGTShippingLineDetailsModel shippingLineDetailsModel : shippinDetailModel.getShippingLineDetails())
				{
					if (StringUtils.isNotEmpty(shippingLineDetailsModel.getSapOrderLineNum())
							&& shippingLineDetailsModel.getSapOrderLineNum().equals(orderLineNumber))
					{
						if (schLinesCount == 1)
						{
							final JnjDeliveryScheduleModel singleDeliveryScheduleLine = orderEntryModel.getDeliverySchedules().get(0);
							singleDeliveryScheduleLine.setLineStatus(OrderEntryStatus.SHIPPED.toString());
							modelService.save(singleDeliveryScheduleLine);

							orderEntryStatus = OrderEntryStatus.SHIPPED;
							lineStatusFound = true;
							break;
						}
						else if (schLinesCount >= 2)
						{
							for (final JnjDeliveryScheduleModel deliveryScheduleLine : orderEntryModel.getDeliverySchedules())
							{
								try
								{
									final Long deliveredQty = (shippingLineDetailsModel.getDeliveryQty() != null) ? Long
											.valueOf(shippingLineDetailsModel.getDeliveryQty()) : Long.valueOf(0);

									if (deliveryScheduleLine.getQty() != null && deliveryScheduleLine.getQty().equals(deliveredQty))
									{
										deliveryScheduleLine.setLineStatus(OrderEntryStatus.SHIPPED.toString());
										modelService.save(deliveryScheduleLine);
										lineStatusFound = true;
									}
								}
								catch (final NumberFormatException exception)
								{
									LOGGER.error("ORDER ENTRY STATUS CALCULATION: Exception while converting Schedule Line quantity value for Line Number: "
											+ orderLineNumber
											+ ", and Sch Line Number: "
											+ deliveryScheduleLine.getScheduledLineNumber()
											+ " Exception: " + exception.getMessage());
									continue;
								}
							}
							if (lineStatusFound)
							{
								orderEntryStatus = OrderEntryStatus.SHIPPED;
							}
						}
					}
				}
				if (lineStatusFound)
				{
					break;
				}
			}
		}

		if (!lineStatusFound)
		{
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
				else if (Jnjgtb2binboundserviceConstants.Order.SCHEDULE_LINE_STATUS_UC.equals(currentSchLineStatus))
				{
					if (JnJCommonUtil.getValue(Jnjb2bCoreConstants.Cart.DIVISION_OCD).equals(productDivision)
							&& DateUtils.isSameDay(orderModel.getCreationtime(), new Date()))//Creation date check has been added as per FRS..
					{
						countAccepted++;
					}
					else
					{
						countBackorder++;
					}
				}
				else if (Jnjgtb2binboundserviceConstants.Order.SCHEDULE_LINE_STATUS_CANCELLED.equals(currentSchLineStatus))
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
		}

		if (orderEntryStatus != null)
		{
			orderEntryModel.setStatus(orderEntryStatus.toString());
		}
	}

	/**
	 * 
	 * @param shipppingDetails
	 * @param orderLineNumber
	 * @return boolean
	 */

	private boolean isTrackingExistsForOrderLine(final Set<JnjGTShippingDetailsModel> shipppingDetails,
			final String orderLineNumber)
	{

		for (final JnjGTShippingDetailsModel shippingDetailModel : shipppingDetails)
		{

			for (final JnjGTShippingLineDetailsModel shippingLineModel : shippingDetailModel.getShippingLineDetails())
			{
				if (StringUtils.isNotEmpty(orderLineNumber) && orderLineNumber.equals(shippingLineModel.getSapOrderLineNum()))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Finds existing UoM corresponding to the code, and if not found creates new <code>UnitModel</code> with the code.
	 * 
	 * @param code
	 * @param name
	 * @return UnitModel
	 */
	private UnitModel getUnitOfMeasurement(final String code, final String name)
	{
		if (code == null)
		{
			return null;
		}
		else
		{
			UnitModel unitModel = null;
			try
			{
				unitModel = jnJGTProductService.getUnitByCode(code);
			}
			catch (final ModelNotFoundException exception)
			{
				unitModel = modelService.create(UnitModel.class);
				unitModel.setCode(code);
				unitModel.setName(name);
				unitModel.setUnitType(Jnjgtb2binboundserviceConstants.Product.JNJ_UNIT_TYPE);
				modelService.save(unitModel);
			}
			return unitModel;
		}
	}

	/**
	 * Parses a date value from Intermediate record based on a Configurable Date Format.
	 * 
	 * @param dateValue
	 * @param attributeName
	 * @return Date
	 */
	private Date parseDate(final String dateValue, final String attributeName)
	{
		
		final DateFormat formatter = new SimpleDateFormat(jnjCommonUtil.getDateFormat());
		Date parsedDate = null;
		try
		{
			parsedDate = formatter.parse(dateValue);
		}
		catch (final ParseException exception)
		{
			LOGGER.error("EXCEPTION WHILE PARSING " + attributeName + " FROM INTERMEDIATE | " + exception.getMessage(), exception);
		}

		return parsedDate;
	}

	/**
	 * Retrieve currency based on the iso code received.
	 * 
	 * @param isoCode
	 * @return CurrencyModel
	 * @throws BusinessException
	 */
	private CurrencyModel getCurrencyFromIsoCode(final String isoCode) throws BusinessException
	{
		CurrencyModel currency = null;
		try
		{
			currency = commonI18NService.getCurrency(isoCode);
		}
		catch (final UnknownIdentifierException exception)
		{
			throw new BusinessException(exception.getMessage());
		}
		return currency;
	}

	/**
	 * Returns <code>JnJProductModel</code> based on condition:
	 * <ul>
	 * <li>If <code>materialNumber</code> is null or, no product exists with code as <code>materialNumber</code>,
	 * retrieves dummy product.</li>
	 * <li>Else, retrieves and return product found based on <code>materialNumber</code></li>
	 * 
	 * @param catalogVersion
	 * @param jnjGTIntOrderLine
	 * @return JnJProductModel
	 * @throws UnknownIdentifierException
	 */
	private JnJProductModel getProductForOrderEntry(final AbstractOrderEntryModel orderEntry,
			final CatalogVersionModel catalogVersion, final JnjGTIntOrderLineModel jnjGTIntOrderLine, final String sapOrderNumber,
			final boolean isMddRecord) throws UnknownIdentifierException
	{
		JnJProductModel product = null;
		final String materialNumber = jnjGTIntOrderLine.getMaterialNumber();
		final String materialEntered = jnjGTIntOrderLine.getMaterialEntered();
		//final String matNum = findmaterialNumber(materialNumber);

		if (StringUtils.isNotEmpty(materialNumber))
		{
			/*
			 * if (isMddRecord) { LOGGER.info(
			 * "MATERIAL NUMBER IS NULL, DUMMY PRODUCT WOULD BE SET INSTEAD FOR THE ORDER ENTRY WITH SAP ORDER NUMBER: " +
			 * sapOrderNumber); product = (JnJProductModel) jnJGTProductService.getProductForCode(catalogVersion,
			 * Order.DUMMY_PRODUCT_CODE); orderEntry .setMaterialEntered(StringUtils.isEmpty(materialEntered) ?
			 * Product.NON_REMAINING_PRO_CODE : materialEntered); } else { new
			 * BusinessException("MATERIAL NUMBER IS NULL FOR THE ORDER ENTRY WITH SAP ORDER NUMBER: " + sapOrderNumber); }
			 * } else {
			 */
			try
			{
				LOGGER.info("MATERIAL NUMBER IS NULL, DUMMY PRODUCT WOULD BE SET INSTEAD FOR THE ORDER ENTRY WITH SAP ORDER NUMBER: "
						+ sapOrderNumber);
				product = (JnJProductModel) jnJGTProductService.getProductForCode(catalogVersion, Order.DUMMY_PRODUCT_CODE);
				orderEntry
						.setMaterialEntered(StringUtils.isEmpty(materialEntered) ? Product.NON_REMAINING_PRO_CODE : materialEntered);
			}
			catch (final UnknownIdentifierException exception)
			{
				if (!isMddRecord)
				{
					LOGGER.info("COULD NOT FIND PRODUCT WITH MATERIAL NUMBER: " + materialNumber
							+ ", DUMMY PRODUCT WOULD BE SET INSTEAD FOR THE ORDER ENTRY WITH SAP ORDER NUMBER: " + sapOrderNumber);
					product = (JnJProductModel) jnJGTProductService.getProductForCode(catalogVersion, Order.DUMMY_PRODUCT_CODE);
					orderEntry.setMaterialNumber(materialNumber);
				}
				else
				{
					throw exception;
				}
			}
		}
		return product;
	}


	private String findmaterialNumber(final String materialNumber)
	{

		int count = 0;
		for (int i = 0; i < materialNumber.length(); i++)
		{
			if (materialNumber.charAt(i) == '0')
			{
				count++;
			}
			else
			{
				break;
			}

		}

		return materialNumber.substring(count, materialNumber.length());



	}

	// Newly added
	public void processIntermediateRecordsMultiThread(final List<String> sourceSysId,
			final List<List<JnjGTIntOrderHeaderModel>> orderNumberSets)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "processIntermediateRecordsMultiThread()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		// Set the catalog versions which will be used to fetch the products
		SetProductCatalogs();


		if (orderNumberSets != null)
		{
			final List<Thread> impexThreads = new ArrayList<Thread>();

			for (final List<JnjGTIntOrderHeaderModel> orderNumbers : orderNumberSets)
			{
				final JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapperPrototype = (JnjGTOrderSyncDataLoadMapper) Registry
						.getApplicationContext().getBean("jnjGTOrderSyncDataLoadMapperPrototype");
				final Thread intToHybrisThread = new Thread(new IntToHybrisThread(Registry.getCurrentTenant().getTenantID(),
						orderNumbers, sourceSysId, jnjGTOrderSyncDataLoadMapperPrototype));
				intToHybrisThread.start();
				impexThreads.add(intToHybrisThread);
			}
			for (final Thread intToHybrisThread : impexThreads)
			{
				try
				{
					intToHybrisThread.join();
				}
				catch (final InterruptedException ie)
				{
					//Log message if required.
				}
			}
		}
		else
		{
			mapOrderData(sourceSysId);
		}


		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "processIntermediateRecordsMultiThread()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	public class IntToHybrisThread implements Runnable
	{

		private final String tenant;
		private final List<JnjGTIntOrderHeaderModel> orderNumbers;
		private final List<String> sourceSysIds;
		private final JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper;

		public IntToHybrisThread(final String tenant, final List<JnjGTIntOrderHeaderModel> orderNumbers,
				final List<String> sourceSysIds, final JnjGTOrderSyncDataLoadMapper jnjGTOrderSyncDataLoadMapper)
		{
			super();
			this.orderNumbers = orderNumbers;
			this.tenant = tenant;
			this.sourceSysIds = sourceSysIds;
			this.jnjGTOrderSyncDataLoadMapper = jnjGTOrderSyncDataLoadMapper;
		}

		@Override
		public void run()
		{
			Registry.setCurrentTenant(Registry.getTenantByID(tenant));
			try
			{
				JaloSession.getCurrentSession().setUser(UserManager.getInstance().getAdminEmployee());
				jnjGTOrderSyncDataLoadMapper.SetProductCatalogs();
				jnjGTOrderSyncDataLoadMapper.mapOrderDataByOrderNumber(orderNumbers);
				//product = (JnJProductModel) jnJGTProductService.getProductForCode(catalogVersion, materialNumber);
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}
		}
	}

	// Newly added methods Sumit

	private void SetProductCatalogs()
	{
		/*
		 * mddCatalogVersion =
		 * catalogVersionService.getCatalogVersion(Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID,
		 * Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);
		 */

		consumerCatalogVersion = catalogVersionService.getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.CONSUMER_CANADA_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.ONLINE_CATALOG_VERSION);
	}


	public boolean mapOrderData(final List<String> sourceSysIds)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData(sourceSysIds=" + sourceSysIds + ")"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		final String errorMessage = null;

		// Fetch all those records whose record status is pending.
		final List<JnjGTIntOrderHeaderModel> jnjGTIntOrderHeaderList = (List<JnjGTIntOrderHeaderModel>) jnjGTFeedService
				.getRecordsByStatusAndSourceSys(JnjGTIntOrderHeaderModel._TYPECODE, RecordStatus.PENDING, sourceSysIds);
		if (!jnjGTIntOrderHeaderList.isEmpty())
		{
			for (final JnjGTIntOrderHeaderModel jnjGTIntHeaderModel : jnjGTIntOrderHeaderList)
			{
				recordStatus = false;
				if (StringUtils.isNotBlank(jnjGTIntHeaderModel.getSourceSystemId())
						&& StringUtils.isNotBlank(jnjGTIntHeaderModel.getSapOrderNumber()))
				{
					/*
					 * LOGGER.info("COULD NOT FIND PRODUCT WITH MATERIAL NUMBER: " + materialNumber +
					 * ", DUMMY PRODUCT WOULD BE SET INSTEAD FOR THE ORDER ENTRY WITH SAP ORDER NUMBER: " + sapOrderNumber);
					 * product = (JnJProductModel) jnJGTProductService.getProductForCode(catalogVersion,
					 * Order.DUMMY_PRODUCT_CODE); orderEntry.setMaterialNumber(materialNumber);
					 */
				}
				else
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Skipping the record as Source Sytem id or Order Number is null/empty.");
					}
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, null, true, errorMessage, Logging.ORDER_SYNC_FEED,
							jnjGTIntHeaderModel.getSapOrderNumber());
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Order Number " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " was not processed successfully.");
					}

				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

	public boolean mapOrderDataByOrderNumber(final List<JnjGTIntOrderHeaderModel> jnjGTIntOrderHeaderList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderDataByOrderNumber()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		String errorMessage = null;

		if (!jnjGTIntOrderHeaderList.isEmpty())
		{
			for (final JnjGTIntOrderHeaderModel jnjGTIntHeaderModel : jnjGTIntOrderHeaderList)
			{
				recordStatus = false;
				if (StringUtils.isNotBlank(jnjGTIntHeaderModel.getSourceSystemId())
						&& StringUtils.isNotBlank(jnjGTIntHeaderModel.getSapOrderNumber()))
				{
					final String sapOrderNumber = jnjGTIntHeaderModel.getSapOrderNumber();

					OrderModel jnjGTOrder = null;
					final Double cancelledLineItemPrice = Double.valueOf(0.0);
					// Call To fetch the source system id
					final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntHeaderModel.getSourceSystemId());
					final BaseSiteModel baseSiteModel = jnjGTOrderFeedService.getBaseSiteModelUsingSourceSysId(sourceSysId);
					jnjGTOrder = jnjGTOrderFeedService.getOrderModelUsingSapOrdNoAndBaseSite(sapOrderNumber, baseSiteModel);

					try
					{
						if (jnjGTOrder != null)
						{
							updateExistingOrder(jnjGTOrder, jnjGTIntHeaderModel);
						}
						else
						{
							jnjGTOrder = modelService.create(OrderModel.class);
							createNewOrder(jnjGTIntHeaderModel, jnjGTOrder, baseSiteModel);
						}
						processSAPOrderEntries(sapOrderNumber, jnjGTOrder, jnjGTIntHeaderModel.getSourceSystemId(),
								cancelledLineItemPrice, jnjGTIntHeaderModel.getRecordTimeStamp());

						/*
						 * if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId)) { //processOrderHeaderNote(sapOrderNumber,
						 * jnjGTOrder, jnjGTIntHeaderModel.getSourceSystemId());
						 * jnjGTOrderService.populateMddOrderStatus(jnjGTOrder);//For JJEPIC-790, changed to call the method
						 * from the order service // Populate the other charge final double otherCharge =
						 * Double.parseDouble(jnjGTIntHeaderModel.getTotalNetValue()) + (jnjGTOrder.getTotalHsaPromotion() !=
						 * null ? jnjGTOrder.getTotalHsaPromotion().doubleValue() : 0.0) - (jnjGTOrder.getSubtotal() != null ?
						 * jnjGTOrder.getSubtotal().doubleValue() : 0.0) - (jnjGTOrder.getTotalFees() != null ?
						 * jnjGTOrder.getTotalFees().doubleValue() : 0.0) - (jnjGTOrder.getTotalTax() != null ?
						 * jnjGTOrder.getTotalTax().doubleValue() : 0.0);
						 * 
						 * jnjGTOrder.setTotalOtherCharge(Double.valueOf(otherCharge));
						 * jnjGTOrder.setTotalNetValue((jnjGTIntHeaderModel.getTotalNetValue() != null) ? Double
						 * .valueOf(jnjGTIntHeaderModel.getTotalNetValue()) : DEFAULT_EMPTY_VALUE);
						 * 
						 * }
						 */

						// Populate total price
						final double totalPrice = Double.parseDouble(jnjGTIntHeaderModel.getTotalNetValue())
								+ jnjGTOrder.getTotalTax().doubleValue();
						jnjGTOrder.setTotalPrice(Double.valueOf(totalPrice));

						modelService.saveAll(jnjGTOrder);
						recordStatus = true;
					}
					catch (final UnknownIdentifierException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final BusinessException exception)
					{
						LOGGER.error("Processing of Order with order number: " + sapOrderNumber + " has caused an exception: "
								+ exception);
						errorMessage = exception.getMessage();
					}
					catch (final ModelSavingException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final ParseException exception)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Parsing Exception occured -" + exception.getMessage(), exception);
						errorMessage = exception.getMessage();
					}
					catch (final Throwable throwable)
					{
						LOGGER.error(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
						errorMessage = throwable.getMessage();
					}
				}
				else
				{
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "Skipping the record as Source Sytem id or Order Number is null/empty.");
					}
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntHeaderModel, null, true, errorMessage, Logging.ORDER_SYNC_FEED,
							jnjGTIntHeaderModel.getSapOrderNumber());
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
								+ "The Record with Order Number " + jnjGTIntHeaderModel.getSapOrderNumber()
								+ " was not processed successfully.");
					}

				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_SYNC_FEED + Logging.HYPHEN + "mapOrderData()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}
}
