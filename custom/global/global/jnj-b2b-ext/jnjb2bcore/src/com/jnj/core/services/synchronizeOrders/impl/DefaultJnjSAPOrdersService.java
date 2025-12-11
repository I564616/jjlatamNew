/**
 * 
 */
package com.jnj.core.services.synchronizeOrders.impl;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
//import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.basecommerce.enums.OrderEntryStatus;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.CompanyModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.util.Config;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dao.synchronizeOrders.JnjSAPOrdersDao;
import com.jnj.core.event.JnjGTReturnOrderCSREvent;
import com.jnj.core.event.JnjGTReturnOrderUserEvent;
import com.jnj.core.event.JnjOrderStatusNotificationEvent;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;
import com.jnj.core.services.synchronizeOrders.JnjSAPOrdersService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;


/**
 * @author akash.rawat
 * 
 */
public class DefaultJnjSAPOrdersService implements JnjSAPOrdersService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjSAPOrdersService.class);
	protected static final String BASE_STORE = "BaseStore";
	protected static final String SEND_STATUS_CHANGE_NOTIFICATION = "sendStatusChangeNotification()";
	protected static final String CURRENT_BASE_SIORE = "brBaseStore";
	
	//Added for AAOL-5163
	protected static final String SEND_RETURN_USER_NOTIFICATION = "sendReturnOrderUserEmail()";
	protected static final String SEND_RETURN_CSR_NOTIFICATION = "sendReturnOrderCSREmail()";

	protected static final String CURRENT_BASE_SITE = "brCMSite";

	private ModelService modelService;
	private AddressService addressService;
	private B2BUnitService<CompanyModel, UserModel> b2bUnitService;
	private B2BCommerceUnitService b2BCommerceUnitService;
	private ProductService productService;
	private CommonI18NService commonI18NService;
	private EventService eventService;
	private BaseSiteService baseSiteService;
	private BaseStoreService baseStoreService;
	private JnjSAPOrdersDao jnjSAPOrdersDao;
	private CatalogService catalogService;
	private CatalogVersionService catalogVersionService;


	@Autowired
	private FlexibleSearchService flexibleSearchService;



	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}


	@Override
	public JnjDeliveryScheduleModel getExistingScheduleLineByLineNumber(final String lineNumber)
	{
		return getJnjSAPOrdersDao().getExistingScheduleLineByLineNumber(lineNumber);
	}


	@Override
	public OrderModel getExistingOrderBySAPOrderNumber(final String orderNumber)
	{
		return getJnjSAPOrdersDao().getExistingOrderBySAPOrderNumber(orderNumber);
	}

	@Override
	public OrderModel createNewOrder()
	{
		return modelService.create(OrderModel.class);
	}

	@Override
	public String saveItem(final ItemModel item)
	{
		try
		{
			getModelService().save(item);
			return null;
		}
		catch (final ModelSavingException e)
		{
			return e.getMessage();
		}
	}


	@Override
	public UserModel getUser(final B2BUnitModel unit)
	{
		if (unit == null)
		{
			LOGGER.error("Unit is null, cannot find user.");
		}
		else
		{
			for (final PrincipalModel user : unit.getMembers())
			{
				if (user instanceof JnJB2bCustomerModel)
				{
					return (UserModel) user;
				}
			}
		}
		return null;
	}

	@Override
	public OrderEntryModel createOrderEntry()
	{
		return getModelService().create(OrderEntryModel.class);
	}

	@Override
	public ProductModel getProductForCode(final String productCode, final String countryCode)
	{
	final String METHOD_NAME= "getProductForCode";
		
		List<String> countryList = null;
		String list = Config.getParameter(Jnjb2bCoreConstants.KEY_Valid_COUNTRIES);
		if(list == null){
			JnjGTCoreUtil.logWarnMessage("Get Product", METHOD_NAME, "Properties: " + Jnjb2bCoreConstants.KEY_Valid_COUNTRIES + " is null.", DefaultJnjSAPOrdersService.class);
		}else{
			countryList = Arrays.asList( list.split(","));
		}
		
		CatalogVersionModel catalogVersion = null;
		if(countryList.contains(countryCode)){
			final String catalogId = countryCode.toLowerCase() + Jnjb2bCoreConstants.PRODUCT_CATALOG;
			final CatalogModel catalogModel = catalogService.getCatalogForId(catalogId);
			catalogVersion = catalogVersionService.getCatalogVersion(catalogModel.getId(),
					Jnjb2bCoreConstants.CATALOG_VERSION_ONLINE);
		}
		
		return getProductService().getProductForCode(catalogVersion, productCode);
	}

	@Override
	public JnjDeliveryScheduleModel createJnjDeliverySchedule()
	{
		return getModelService().create(JnjDeliveryScheduleModel.class);
	}

	@Override
	public B2BUnitModel getUnitForUid(final String shipToNumber)
	{
		return (B2BUnitModel) getB2bUnitService().getUnitForUid(shipToNumber);
	}

	@Override
	public AddressModel getAddressForCode(final B2BUnitModel unit, final String addressCode)
	{
		return getB2BCommerceUnitService().getAddressForCode(unit, addressCode);
	}

	@Override
	public DeliveryModeModel createDeliveryMode()
	{
		return getModelService().create(DeliveryModeModel.class);
	}


	@Override
	public CurrencyModel getCurrentCurrency(final JnJB2BUnitModel jnJB2BUnitModel)
	{
		LOGGER.debug("DefaultJnjSAPOrdersService - getCurrentCurrency() -" + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
				+ Jnjb2bCoreConstants.Logging.HYPHEN + System.currentTimeMillis());
		CurrencyModel currencyModel = null;
		if (null != jnJB2BUnitModel && null != jnJB2BUnitModel.getCountry()
				&& StringUtils.isNotEmpty(jnJB2BUnitModel.getCountry().getIsocode()))
		{
			final String cuntryISO = jnJB2BUnitModel.getCountry().getIsocode();
			final String baseStoreID = cuntryISO.toLowerCase() + BASE_STORE;
			currencyModel = baseStoreService.getBaseStoreForUid(baseStoreID).getDefaultCurrency();
		}
		else
		{
			LOGGER.error("DefaultJnjSAPOrdersService - getCurrentCurrency() - Error while getting the Country. Returning Null");
		}
		LOGGER.debug("DefaultJnjSAPOrdersService - getCurrentCurrency() -" + Jnjb2bCoreConstants.Logging.END_OF_METHOD
				+ Jnjb2bCoreConstants.Logging.HYPHEN + System.currentTimeMillis());
		return currencyModel;
	}


	@Override
	public OrderStatus getOrderStatus(final String overAllStatus, final String rejectionStatus, final String creditStatus,
			final String deliveryStatus, final String invoiceStatus) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OrderStatus orderHeaderStatus = null;
		final List<JnjOrdStsMappingModel> jnjOrderStatusModelList = getJnjSAPOrdersDao().getOrderStatus(overAllStatus,
				rejectionStatus, creditStatus, deliveryStatus, invoiceStatus);
		if (null != jnjOrderStatusModelList && !jnjOrderStatusModelList.isEmpty())
		{
			orderHeaderStatus = jnjOrderStatusModelList.get(Order.ZERO).getOrderStatus();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Header Status " + orderHeaderStatus);
			}
		}
		else
		{
			LOGGER.error("No Order status found for the given combination in the database, Order cannot be saved.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderHeaderStatus;
	}

	@Override
	public OrderEntryStatus getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus, final String gtsHold)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		OrderEntryStatus orderEntryStatus = null;
		final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = getJnjSAPOrdersDao().getOrderEntryStatus(overAllStatus,
				rejectionStatus, deliveryStatus, invoiceStatus, gtsHold);
		if (null != jnjOrderEntryStatusModelList && !jnjOrderEntryStatusModelList.isEmpty())
		{
			orderEntryStatus = jnjOrderEntryStatusModelList.get(Order.ZERO).getOrderEntryStatus();
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Entry Status Model "
						+ orderEntryStatus);
			}
		}
		else
		{
			LOGGER.error("No Order Entry status found for the given combination in the database, Order Entry cannot be saved.");
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return orderEntryStatus;
	}


	@Override
	public UnitModel getUnitOfMeasurement(final String unitCode)
	{
		UnitModel salesUOM = new UnitModel();
		salesUOM.setCode(unitCode);
		try
		{
			return getJnjSAPOrdersDao().getUnitOfMeasurement(salesUOM);
		}
		catch (final ModelNotFoundException | IllegalArgumentException modelNotFoundException)
		{
			salesUOM = getModelService().create(UnitModel.class);
			salesUOM.setCode(unitCode);
			salesUOM.setUnitType(Jnjb2bCoreConstants.UpsertProduct.UPSERT_PRODUCT_BASE_UNIT_TYPE);
			getModelService().save(salesUOM);
		}

		return salesUOM;
	}



	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendStatusChangeNotification(final CustomerModel customer, final String sapOrderNumber,
			final String clientOrderNumber, final String jnjOrderNumber, final OrderStatus currentStatus,
			final OrderStatus previousStatus, final String baseUrl, final Boolean isSyncOrder, final String mediaLogoURL,final String toEmail)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_STATUS_CHANGE_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final JnjOrderStatusNotificationEvent event = new JnjOrderStatusNotificationEvent();

		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setCustomer(customer);
		/*START AAOL 4911*/
		event.setToEmail(toEmail);
		/*END AAOL 4911*/
		event.setCurrentStatus(currentStatus);
		event.setPreviousStatus(previousStatus);
		event.setJnjOrderNumber(jnjOrderNumber);
		event.setSapOrderNumber(sapOrderNumber);
		event.setClientOrderNumber(clientOrderNumber);
		event.setSyncOrderNotification(isSyncOrder);
		event.setBaseUrl(baseUrl);
		event.setMediaUrl(baseUrl + mediaLogoURL);

		eventService.publishEvent(event);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_STATUS_CHANGE_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendStatusChangeNotification(final CustomerModel customer, final String orderCode, final String baseUrl,
			final String mediaLogoURL,String toEmail)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_STATUS_CHANGE_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final JnjOrderStatusNotificationEvent event = new JnjOrderStatusNotificationEvent();

		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setCustomer(customer);
		/*START AAOL 4911*/
		event.setToEmail(toEmail);
		/*END AAOL 4911*/
		event.setOrderCode(orderCode);

		event.setBaseUrl(baseUrl);
		LOGGER.debug("mediaLogoURL.." + mediaLogoURL);
		event.setMediaUrl(baseUrl + mediaLogoURL);

		try{
		LOGGER.debug("Start publishing order status notification.. " + event.getOrderCode());
		eventService.publishEvent(event);
		LOGGER.debug("End published order status notification.. " + event.getOrderCode());
		}catch(Exception e){
			LOGGER.debug("Exception occured while publishing order status notification.. " + event.getOrderCode());
			e.printStackTrace();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_STATUS_CHANGE_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}

	@Override
	public AddressModel cloneAddress(final AddressModel address)
	{
		return getModelService().clone(address);
	}

	@Override
	public String removeItems(final Collection<? extends ItemModel> items)
	{
		try
		{
			getModelService().removeAll(items);
			return null;
		}
		catch (final ModelRemovalException e)
		{
			return e.getMessage();
		}
	}

	public AddressService getAddressService()
	{
		return addressService;
	}

	public void setAddressService(final AddressService addressService)
	{
		this.addressService = addressService;
	}

	public B2BUnitService<CompanyModel, UserModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<CompanyModel, UserModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public B2BCommerceUnitService getB2BCommerceUnitService()
	{
		return b2BCommerceUnitService;
	}

	public void setB2BCommerceUnitService(final B2BCommerceUnitService b2bCommerceUnitService)
	{
		b2BCommerceUnitService = b2bCommerceUnitService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public ProductService getProductService()
	{
		return productService;
	}

	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public JnjSAPOrdersDao getJnjSAPOrdersDao()
	{
		return jnjSAPOrdersDao;
	}

	public void setJnjSAPOrdersDao(final JnjSAPOrdersDao jnjSAPOrdersDao)
	{
		this.jnjSAPOrdersDao = jnjSAPOrdersDao;
	}

	public EventService getEventService()
	{
		return eventService;
	}

	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * @return the catalogService
	 */
	public CatalogService getCatalogService()
	{
		return catalogService;
	}

	public void setCatalogService(final CatalogService catalogService)
	{
		this.catalogService = catalogService;
	}


	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}

	@Override
	public OrderEntryModel getExistingOrderEntry(final String orderEntryNumber, final String orderNumber)
	{
		return getJnjSAPOrdersDao().getExistingOrderEntryByEntryNumber(orderEntryNumber, orderNumber);
	}


	@Override
	public void sendReturnOrderUserEmail(CustomerModel customer, String orderCode, String baseUrl,
			String mediaLogoURL, String toEmail) {
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_RETURN_USER_NOTIFICATION + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}

		final JnjGTReturnOrderUserEvent event = new JnjGTReturnOrderUserEvent();

		event.setBaseStore(baseStoreService.getCurrentBaseStore());
		event.setSite(baseSiteService.getCurrentBaseSite());
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		event.setCustomer(customer);
		event.setToEmail(toEmail);
		event.setOrderCode(orderCode);

		event.setBaseUrl(baseUrl);
		LOGGER.debug("mediaLogoURL.." + mediaLogoURL);
		event.setMediaUrl(baseUrl + mediaLogoURL);

		try{
		LOGGER.debug("Start publishing return order user notification.. " + event.getOrderCode());
		eventService.publishEvent(event);
		LOGGER.debug("End published return order user notification.. " + event.getOrderCode());
		}catch(Exception e){
			LOGGER.debug("Exception occured while publishing return order user notification.. " + event.getOrderCode());
			e.printStackTrace();
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_RETURN_USER_NOTIFICATION + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
		}
	}
		
		@Override
		public void sendReturnOrderCSREmail(CustomerModel customer, String orderCode, String baseUrl,
				String mediaLogoURL, String toEmail) {
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_RETURN_CSR_NOTIFICATION + Logging.HYPHEN
						+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
			}

			final JnjGTReturnOrderCSREvent event = new JnjGTReturnOrderCSREvent();

			event.setBaseStore(baseStoreService.getCurrentBaseStore());
			event.setSite(baseSiteService.getCurrentBaseSite());
			event.setLanguage(commonI18NService.getCurrentLanguage());
			event.setCurrency(commonI18NService.getCurrentCurrency());
			event.setCustomer(customer);
			event.setToEmail(toEmail);
			event.setOrderCode(orderCode);

			event.setBaseUrl(baseUrl);
			LOGGER.debug("mediaLogoURL.." + mediaLogoURL);
			event.setMediaUrl(baseUrl + mediaLogoURL);

			try{
			LOGGER.debug("Start publishing return order CSR notification.. " + event.getOrderCode());
			eventService.publishEvent(event);
			LOGGER.debug("End published return order CSR notification.. " + event.getOrderCode());
			}catch(Exception e){
				LOGGER.debug("Exception occured while publishing return order CSR notification.. " + event.getOrderCode());
				e.printStackTrace();
			}
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.EMAIL_NOTIFICATION_PROCESS + Logging.HYPHEN + SEND_RETURN_CSR_NOTIFICATION + Logging.HYPHEN
						+ Logging.END_OF_METHOD + Logging.HYPHEN + System.currentTimeMillis());
			}
	}

}
