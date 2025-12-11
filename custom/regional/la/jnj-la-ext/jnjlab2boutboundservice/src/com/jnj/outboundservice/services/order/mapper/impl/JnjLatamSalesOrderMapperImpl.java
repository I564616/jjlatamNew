package com.jnj.outboundservice.services.order.mapper.impl;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.data.JnjGTOutboundStatusData;
import com.jnj.core.data.JnjGTSimulateOrderResponseData;
import com.jnj.core.dto.SplitOrderData;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJSalesOrgCustomerModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjOrderService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjObjectComparator;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.JnjLatamSAPErrorMessageFacade;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnjDeliveryScheduleData;
import com.jnj.facades.data.JnjGTCartData;
import com.jnj.facades.data.JnjLaCartData;
import com.jnj.facades.data.JnjLaOrderEntryData;
import com.jnj.facades.data.JnjOutOrderLine;
import com.jnj.facades.data.JnjProductData;
import com.jnj.facades.data.JnjSAPErrorMessageData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.facades.order.impl.DefaultJnjGTSalesOrderMapper;
import com.jnj.facades.product.JnjProductFacade;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.BTBControlArea;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.InOrderLines;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.InOrderLines2;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.InOrderLines3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.MessageReturn;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.MessageReturn3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.OutOrderLines3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderCreationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderIn;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderIn2;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderIn3;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderPricingResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationRequest;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.SalesOrderSimulationResponse;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ScheduledLines;
import com.jnj.itsusmpl00082.sg910_btb_in0498_salesorder_global_source_v1_webservice.salesorderws.ScheduledLines3;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.contract.JnjContractService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaCoreUtil;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import com.jnj.outboundservice.facades.orderSplit.impl.DefaultJnjLatamOrderSplitFacade;
import com.jnj.outboundservice.services.order.JnjLatamSalesOrder;
import com.jnj.outboundservice.services.order.mapper.JnjLatamSalesOrderMapper;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.converters.populator.ProductBasicPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import jakarta.xml.bind.JAXBElement;
import java.math.BigDecimal;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.Objects;


public class JnjLatamSalesOrderMapperImpl extends DefaultJnjGTSalesOrderMapper implements JnjLatamSalesOrderMapper
{
	private static final Class<JnjLatamSalesOrderMapperImpl> currentClass = JnjLatamSalesOrderMapperImpl.class;
	
	private static final Logger LOGGER = Logger.getLogger(JnjLatamSalesOrderMapperImpl.class);
	

	/** The Constant LOGGER for SAP Error Logs. */
	private static final Logger SAPLOGGER = Logger.getLogger("sapLogger");

	private final String available = "available";


	private final String notAvailable = "notAvailable";
	private final String partialyAvailable = "partialyAvailable";

	private final String multipleDateAvailable = "multipleDateAvailable";
	
	private static final String CUSTOMER_FREIGHT_TYPE = "FOB";
	private static final String VTEX_ORDER_TYPE = "VTEX";
	/** The Constant objectFactory. */
	public static final ObjectFactory objectFactory = new ObjectFactory();

	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjLatamSalesOrder jnjLatamSalesOrder;

	public JnjLatamSalesOrder getJnjLatamSalesOrder()
	{
		return jnjLatamSalesOrder;
	}

	public void setJnjLatamSalesOrder(final JnjLatamSalesOrder jnjLatamSalesOrder)
	{
		this.jnjLatamSalesOrder = jnjLatamSalesOrder;
	}

	@Autowired
	protected CartService cartService;

	@Autowired
	private DefaultJnjLatamOrderSplitFacade<JnjLatamSplitOrderInfo, AbstractOrderEntryModel> jnjLatamOrderSplitFacade;

	private static double salesOrderTotalNetValue = 0;

	private static final String CHECKOUT_TRUE_FLAG = "X";	

	private static final String CHECK_PHARMA_PRODUCT = "isAtLeastOnePharamaProduct";

	protected static Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine = new HashMap<>();

	@Autowired
	private UserService userService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private JnjContractService jnjContractService;

	@Autowired
	private JnjLatamSAPErrorMessageFacade jnjSAPErrorMessageFacade;

	@Autowired
	protected SessionService sessionService;

	@Autowired
	protected JnjOrderService jnjOrderService;

	@Autowired
	protected JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private JnjProductFacade jnjProductFacade;

	@Autowired
	protected JnjlatamOrderUtil orderUtil;

	@Autowired
	private ProductBasicPopulator<ProductModel, JnjProductData> productBasicPopulator;

	@Autowired
	private JnjCartService jnjCartService;

	@Autowired
	protected CatalogService catalogService;

	@Autowired
	protected JnJLaProductService jnjLaProductService;

	@Autowired
	protected CategoryService categoryService;

	@Autowired
	private CMSSiteService cmsSiteService;
	
	private static ConfigurationService configurationService;
	
	public static ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public static void setConfigurationService(ConfigurationService configurationService) {
		JnjLatamSalesOrderMapperImpl.configurationService = configurationService;
	}

	public void setJnjConfigService(final JnjConfigService jnjConfigService)
	{
		this.jnjConfigService = jnjConfigService;
	}


	@Override
	public SalesOrderCreationResponse mapSalesOrderCreationWrapper(final OrderModel orderModel)
			throws IntegrationException, SystemException, ParseException
	{
		final String methodName = "mapSalesOrderCreationWrapper()";
		JnjGTCoreUtil.logDebugMessage(methodName, Logging.SUBMIT_ORDER, Logging.BEGIN_OF_METHOD, currentClass);

		//Getting the B2bUnit with Cart
		final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) orderModel.getUnit();
		if (null == jnJB2bUnitModel)
		{
			throw new SystemException("No B2bUnit associated with Order having order No [" + orderModel.getCode() + "]");
		}
		//Fetch the Country Associated with Cart in Order to get Individual Taxes.
		final CountryModel countryModel = jnJB2bUnitModel.getCountry();
		if (null == countryModel)
		{
			throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
					+ "] for Order having order No [" + orderModel.getCode() + "]");
		} 

		//JnjSAPErrorMessageData errorMessageData = null;
		boolean outOrderLineFlag = false;
		final SalesOrderCreationRequest salesOrderCreationRequest = new SalesOrderCreationRequest();
		final SalesOrderIn salesOrderIn = new SalesOrderIn();

		final BTBControlArea bTBControlArea = new BTBControlArea();
		final Date date = new Date();
		final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
		final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);

		final String formatedSecondTime = secondformatter.format(date);
		final String formatedMiliSecondTime = miliSecondformatter.format(date);


		bTBControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.SUBMIT_ORDER + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
				+ orderModel.getCode() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
		bTBControlArea.setTransactionDateTime(formatedMiliSecondTime);
		salesOrderCreationRequest.setHeader(bTBControlArea);

		if (null != orderModel.getEntries() && !orderModel.getEntries().isEmpty())
		{
			salesOrderIn.setInOrderType(orderModel.getOrderType().getCode());
			salesOrderIn.setInSalesOrganization(orderModel.getSalesOrganizationCode());
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Entry for the OrderModel is not existed.", currentClass);

			// As these are the mandatory fields
			salesOrderIn.setInSalesOrganization(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInOrderType(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		//Call the config service impl to retrieve the value from the config model.
		final String jnjConfigModelRequestType = jnjConfigService
				.getConfigValueById(Jnjb2bFacadesConstants.Order.REQUEST_TYPE_CREATION);

		JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
				Logging.SUBMIT_ORDER + Logging.HYPHEN + "Value from the Config Table " + jnjConfigModelRequestType, currentClass);

		salesOrderIn.setInRequestType(jnjConfigModelRequestType);
		
		JnJLaB2BUnitModel b2bUnit = (JnJLaB2BUnitModel) jnJB2bUnitModel;
	    String industryCode = getConfigurationService().getConfiguration().getString(Jnjlab2bcoreConstants.DISTRIBUTOR_INDUSTRY_CODE);

		setFreightTypeInSalesOrderIn(orderModel, countryModel, salesOrderIn, b2bUnit, industryCode);

		final String jnjConfigModelDistChannel = jnjConfigService
				.getConfigValueById(Jnjb2bFacadesConstants.Order.DISTRIBUTION_CHANNEL);
		salesOrderIn.setInDistributionChannel(jnjConfigModelDistChannel);

		final String jnjConfigModelDivision = jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.DIVISION);
		salesOrderIn.setInDivision(jnjConfigModelDivision);

		final JAXBElement<String> jnjConfigModelOrderChannel = objectFactory
				.createSalesOrderInInOrderChannel(null != orderModel.getOrderChannel() ? orderModel.getOrderChannel().getCode()
						: jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_CHANNEL));
		salesOrderIn.setInOrderChannel(jnjConfigModelOrderChannel);

		if (StringUtils.isNotEmpty(orderModel.getCode())){
			final JAXBElement<String> customerPortalOrdernumber = objectFactory
					.createSalesOrderInInCustomerPortalOrdernumber(orderModel.getCode());

			salesOrderIn.setInCustomerPortalOrdernumber(customerPortalOrdernumber);
		}else{
			final JAXBElement<String> customerPortalOrdernumber = objectFactory
					.createSalesOrderInInCustomerPortalOrdernumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInCustomerPortalOrdernumber(customerPortalOrdernumber);
		}


		if (null != orderModel.getDeliveryAddress() && StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getJnJAddressId())){
			salesOrderIn.setInShipToNumber(orderModel.getDeliveryAddress().getJnJAddressId());
		}else{
			salesOrderIn.setInShipToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}
		if (null != orderModel.getUnit() && StringUtils.isNotEmpty(orderModel.getUnit().getUid())){
			salesOrderIn.setInSoldToNumber(orderModel.getUnit().getUid());
		}else{
			salesOrderIn.setInSoldToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		if (null != orderModel.getUser()){
			final JAXBElement<String> contactName = objectFactory
					.createSalesOrderInInContactName(StringUtils.isNotEmpty(orderModel.getUser().getName())
							? orderModel.getUser().getName() : Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			// Check the value of User Name if it's null then set empty else set the same value which is coming in order model
			salesOrderIn.setInContactName(contactName);

			// Changes as per new wsdl
			final JAXBElement<String> contactEmail = objectFactory
					.createSalesOrderInInContactEmail(StringUtils.isNotEmpty(((B2BCustomerModel) orderModel.getUser()).getEmail())
							? (((B2BCustomerModel) orderModel.getUser()).getEmail()) : Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInContactEmail(contactEmail);
		}
		// as this is the mandatory field
		else{
			final JAXBElement<String> contactName = objectFactory
					.createSalesOrderInInContactName(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInContactName(contactName);

			// Changes as per new wsdl
			final JAXBElement<String> contactEmail = objectFactory
					.createSalesOrderInInContactName(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInContactEmail(contactEmail);
		}
		
		JnjContractModel jnjContractModel = null;
		if (StringUtils.isNotEmpty(orderModel.getContractNumber())) {
			jnjContractModel = jnjContractService.getContractDetailsById(orderModel.getContractNumber());
		}
		
		// Check for the Not Empty and Not Null
		if (null != orderModel.getContractNumber() && !orderModel.getContractNumber().isEmpty())
		{
			// Call the Jnj Contract Service to get the Jnj Contract Model object so that we can compare the Contract Order Reason value with BID orders value.
			
			if (null != jnjContractModel && StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
					&& Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_1
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())){
				// Creating the object from the object factory		 final to form the final JAXB element.
				final JAXBElement<String> orderReason = objectFactory.createSalesOrderInInOrderReason(
						jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_REASON));
				salesOrderIn.setInOrderReason(orderReason);
			}else{
				final JAXBElement<String> orderReason = objectFactory
						.createSalesOrderInInOrderReason(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				salesOrderIn.setInOrderReason(orderReason);
			}
			
			if (null != jnjContractModel && StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
					&& !Jnjlab2bcoreConstants.Contract.LOAD_CONTRACT_ORDER_REASON_Z48
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())){
				setForbiddenFlag(salesOrderIn, countryModel.getIsocode());
			}
			
		} else{ // Changes as per new wsdl
			final JAXBElement<String> orderReason = objectFactory
					.createSalesOrderInInOrderReason(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInOrderReason(orderReason);
		}

		// Call the User Service to get User Model of Current User.
		final UserModel userModel = userService.getCurrentUser();
		if (null != userModel && null != userModel.getDefaultPaymentAddress())
		{
			// check if the cell phone is existed
			if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getCellphone()))
			{
				// Changes as per new wsdl
				final JAXBElement<String> contactPhoneNumber = objectFactory
						.createSalesOrderInInContactPhoneNumber(userModel.getDefaultPaymentAddress().getCellphone());
				salesOrderIn.setInContactPhoneNumber(contactPhoneNumber);
			}
			//check if the phone1 is existed
			else if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getPhone1()))
			{
				// Changes as per new wsdl
				final JAXBElement<String> contactPhoneNumber = objectFactory
						.createSalesOrderInInContactPhoneNumber(userModel.getDefaultPaymentAddress().getPhone1());
				salesOrderIn.setInContactPhoneNumber(contactPhoneNumber);
			}
			else{
				// Changes as per new wsdl
				final JAXBElement<String> contactPhoneNumber = objectFactory
						.createSalesOrderInInContactPhoneNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				salesOrderIn.setInContactPhoneNumber(contactPhoneNumber);
			}
		}
		else{
			// Changes as per new wsdl
			final JAXBElement<String> contactPhoneNumber = objectFactory
					.createSalesOrderInInContactPhoneNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInContactPhoneNumber(contactPhoneNumber);
		}
		
		final String cencaSalesOrg=configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.CENCA_SALES_ORG_LIST);
		if (Objects.nonNull(orderModel.getForbiddenSales())
				&& (Objects.nonNull(orderModel.getSalesOrganizationCode()) && cencaSalesOrg.contains(orderModel.getSalesOrganizationCode()))
				&& Objects.isNull(jnjContractModel))
		{
			final JAXBElement<String> inForBiddenSalesElement = objectFactory
					.createSalesOrderInInForbiddenSales(orderModel.getForbiddenSales());
			salesOrderIn.setInForbiddenSales(inForBiddenSalesElement);
		}
		else if (Objects.nonNull(orderModel.getForbiddenSales()) && StringUtils.isNotEmpty(orderModel.getForbiddenSales())
				&& ((Objects.nonNull(jnjContractModel) && StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
						&& !Jnjlab2bcoreConstants.Contract.LOAD_CONTRACT_ORDER_REASON_Z48
								.equalsIgnoreCase(jnjContractModel.getContractOrderReason()))))
		{
			final JAXBElement<String> inForBiddenSalesElement = objectFactory
					.createSalesOrderInInForbiddenSales(orderModel.getForbiddenSales());
			salesOrderIn.setInForbiddenSales(inForBiddenSalesElement);
		}

		if (null != orderModel.getContractNumber()){
			final JAXBElement<String> contractReferenceNumber = objectFactory
					.createSalesOrderInInContractReferenceNumber(orderModel.getContractNumber());
			salesOrderIn.setInContractReferenceNumber(contractReferenceNumber);
		}

		if (null != orderModel.getCompleteDelivery())
		{
			final JAXBElement<String> inCompleteDelivery = objectFactory
					.createSalesOrderInInCompleteDelivery(orderModel.getCompleteDelivery());
			salesOrderIn.setInCompleteDelivery(inCompleteDelivery);
		}
		if (null != orderModel.getPurchaseOrderNumber()){
			final JAXBElement<String> inPONumber = objectFactory.createSalesOrderInInPONumber(orderModel.getPurchaseOrderNumber());
			salesOrderIn.setInPONumber(inPONumber);
		}

		if (null == orderModel.getPurchaseOrderNumber()){
			final JAXBElement<String> inPONumber = objectFactory.createSalesOrderInInPONumber(orderModel.getOrderNumber());
			salesOrderIn.setInPONumber(inPONumber);
		}

		if (null != orderModel.getComplementaryInfo()){
			final JAXBElement<String> inComplementaryInfo = objectFactory.createSalesOrderInInComplementaryInfo(orderModel.getComplementaryInfo());
			salesOrderIn.setInComplementaryInfo(inComplementaryInfo);
		} else {
			final JAXBElement<String> inComplementaryInfo = objectFactory.createSalesOrderInInComplementaryInfo(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInComplementaryInfo(inComplementaryInfo);
		}
		
		final String sourceInfo=configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.ORDER_SOURCE_INFO_FOR_B2ALL);
		if(StringUtils.isNotBlank(orderModel.getExternalOrderRefNumber()) && StringUtils.isNotBlank(sourceInfo)) {
			final JAXBElement<String> inSourceInfo = objectFactory
					.createSalesOrderInInSourceInfo(sourceInfo);
			salesOrderIn.setInSourceInfo(inSourceInfo);
		}else {
			final JAXBElement<String> inSourceInfo = objectFactory
					.createSalesOrderInInSourceInfo(Jnjlab2bcoreConstants.ORDER_SOURCE_INFO);
			salesOrderIn.setInSourceInfo(inSourceInfo);
		}
		
		final JAXBElement<String> inRequestDeliveryDate = objectFactory
				.createSalesOrderInInRequestedDeliveryDate(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		salesOrderIn.setInRequestedDeliveryDate(inRequestDeliveryDate);

		for (final AbstractOrderEntryModel abstOrderEntryModel : orderModel.getEntries())
		{
			// To invoke the setOrderLineForSimulation method to get the data from the AbstractOrderEntryModel object and set into InOrderLines3 object.
			final InOrderLines inOrderLines = setOrderLineForCreation(abstOrderEntryModel);

			salesOrderIn.getInOrderLines().add(inOrderLines);
		}
		salesOrderCreationRequest.setSalesOrderIn(salesOrderIn);

		final Map<String, OutOrderLines> mapMaterialNumberWithOutOrderLine = new HashMap<>();

		final List<String> sapErrorMessagesList = new ArrayList<>();

		final SalesOrderCreationResponse salesOrderCreationResponse = jnjLatamSalesOrder
				.salesOrderCreationWrapper(salesOrderCreationRequest);

		final Map<String, JnjOutOrderLine> materialLineToMaterialEntered = new HashMap<>();
		// Iterate the List of OutOrderLines3 object from the response object and put in the
		// mapMaterialNumberWithOutOrderLine map object.
		final Map<String, JnjOutOrderLine> freeGoodsMapFromSession = sessionService
				.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
		Map<String, JnjOutOrderLine> freeGoodsMap = null;

		if (freeGoodsMapFromSession == null)
		{
			freeGoodsMap = new HashMap<>();
		}
		else
		{
			freeGoodsMap = new HashMap<>(freeGoodsMapFromSession);
		}

		if (null != salesOrderCreationResponse && null != salesOrderCreationResponse.getSalesOrderOut())
		{
			JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response getOutSalesOrderOverallStatus "
							+ salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderOverallStatus() + " getOutSalesOrderNumber "
							+ salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderNumber(),
					currentClass);

			if (null != salesOrderCreationResponse.getSalesOrderOut().getOutOrderLines()
					&& !salesOrderCreationResponse.getSalesOrderOut().getOutOrderLines().isEmpty())
			{
				// Iterate the List of OutOrderLines3 object from the response object and put in the mapMaterialNumberWithOutOrderLine map object.
				for (final OutOrderLines outOrderLine : salesOrderCreationResponse.getSalesOrderOut().getOutOrderLines()){
					if (null != outOrderLine){
						populateFreeItemMaterialTrigger(materialLineToMaterialEntered, outOrderLine);
						if(!outOrderLine.getHigherLevelItemNumber().equalsIgnoreCase("000000")){
							if ((outOrderLine.getItemCategory().equals("ZFNT") || outOrderLine.getItemCategory().equals("ZFNN")
									|| outOrderLine.getItemCategory().equals("ZFRY"))){
								final JnjOutOrderLine jnjOutOrderLine = new JnjOutOrderLine();

								mapValuesToJnjOutOrderLine(jnjOutOrderLine, outOrderLine);
								freeGoodsMap.put(materialLineToMaterialEntered.get(outOrderLine.getHigherLevelItemNumber()).getMaterialEntered(),jnjOutOrderLine);
							}
						}else{
							mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
						}
						mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
						outOrderLineFlag = true;
					}
				}
			}
			// Set the map to session
			sessionService.setAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP, freeGoodsMap);
		}

		// Enter inside if block if the out order line is null in response.
		JnjSAPErrorMessageData errorMessageData;
		if (outOrderLineFlag)
		{
			JnjGTCoreUtil.logWarnMessage("Mapping sales order", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response  for Hard Stop Error", currentClass);
			if (null != salesOrderCreationResponse.getSalesOrderOut().getMessageReturn())
			{
				final List<MessageReturn> message3List = salesOrderCreationResponse.getSalesOrderOut().getMessageReturn();

				for (final MessageReturn messageResponse : message3List)
				{
					JnjGTCoreUtil.logWarnMessage("Mapping sales order", methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response  for Hard Stop Error TYPE :  "
									+ messageResponse.getTYPE() + ", Error ID : " + messageResponse.getID(),
							currentClass);

					if (messageResponse.getTYPE() != null && messageResponse.getID() != null){
						errorMessageData = new JnjSAPErrorMessageData();
						errorMessageData.setId(messageResponse.getID());
						errorMessageData.setErrorMessage(messageResponse.getMESSAGE());
						errorMessageData.setErrorMessageType(messageResponse.getTYPE());
						errorMessageData.setNumber(messageResponse.getNUMBER());

						if ((messageResponse.getTYPE().equals(Jnjb2bCoreConstants.Order.SAP_ORDER_HARD_ERROR_TYPE))
								&& messageResponse.getID().equals(Jnjb2bCoreConstants.Order.SAP_ORDER_HARD_ERROR_ID) && salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderNumber() != null)
						{
							orderModel.setStatus(OrderStatus.PROCESSING_ERROR);
							sapErrorMessagesList.add(jnjSAPErrorMessageFacade.getErrorDetails(errorMessageData, orderModel.getEntries()));
							sapErrorMessagesList.add(messageResponse.getMESSAGE());
						}
					}
				}
			}
			final Integer wirteAttempt = Integer.valueOf(0);
			/*
			 * if (null != orderModel.getOrderOpsArch()) { wirteAttempt = orderModel.getOrderOpsArch().getWriteAttempts();
			 * }
			 *
			 * if (wirteAttempt.intValue() > Config.getInt(Jnjb2bCoreConstants.ORDER_RETRY_COUNT, 3)) {
			 * orderModel.setStatus(OrderStatus.PROCESSING_ERROR); } else { orderModel.setStatus(OrderStatus.CREATED); }
			 */
			orderModel.setSapErrorMessages(sapErrorMessagesList);
			modelService.save(orderModel);
		}


		if (null != salesOrderCreationResponse && CollectionUtils.isEmpty(sapErrorMessagesList))

		{
			/* CR 35029 START */
			String outSalesOrderCreditStatus = null;
			String outSalesOrderRejectionStatus = null;
			if (null != salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderCreditStatus())
			{
				outSalesOrderCreditStatus = salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderCreditStatus();
			}
			if (null != salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderRejectionStatus())
			{
				outSalesOrderRejectionStatus = salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderRejectionStatus();

			}

			final OrderModel orderModelWithSubmitOrderData = mapSalesOrderCreationResponse(salesOrderCreationResponse, orderModel,
					mapMaterialNumberWithOutOrderLine, outSalesOrderCreditStatus, outSalesOrderRejectionStatus);
			// To invoke the JnjOrderServiceImpl class to persist the submit order data in hybris database.
			//Calculate cart for total values and save in data base
			jnjCartService.calculateValidatedCart(orderModelWithSubmitOrderData);
		}

		JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		return salesOrderCreationResponse;
	}

	private static void setFreightTypeInSalesOrderIn(OrderModel orderModel, CountryModel countryModel, SalesOrderIn salesOrderIn, JnJLaB2BUnitModel b2bUnit, String industryCode) {
		if (CUSTOMER_FREIGHT_TYPE.equals(orderModel.getCustomerFreightType()) && industryCode.equals(b2bUnit.getIndustryCode1()) && (null!= countryModel && countryModel.getIsocode().equals(Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL)))
		{
			final JAXBElement<String> inIncoterms = objectFactory
					.createSalesOrderInInIncoterms(orderModel.getCustomerFreightType());
			salesOrderIn.setInIncoterms(inIncoterms);
		} else {
			final JAXBElement<String> inIncoterms = objectFactory
					.createSalesOrderInInIncoterms(StringUtils.EMPTY);
			salesOrderIn.setInIncoterms(inIncoterms);
		}
	}

	//Actual Mapper
	@Override
	public JnjValidateOrderData mapSalesOrderSimulationWrapper(final CartModel cartModel)
			throws IntegrationException, SystemException, TimeoutException
	{
		final String methodName = "mapSalesOrderSimulationWrapper()";
		JnjSAPErrorMessageData errorMessageData;
		final JnjValidateOrderData validateOrderData = new JnjValidateOrderData();
		validateOrderData.setSapErrorResponse(Boolean.FALSE);

		// Orders data object list to show split order on checkout
		JnjGTCoreUtil.logInfoMessage("Mapping sales order", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		boolean cartModelFlag;
		boolean outOrderLineObjFlag;
		boolean isErrorInResponse = false;
		boolean isIncorretShipTo = false;

		//Getting the B2bUnit with Cart
		final JnJLaB2BUnitModel jnJB2bUnitModel = (JnJLaB2BUnitModel) cartModel.getUnit();
		if(StringUtils.isNotBlank(cartModel.getDeliveryAddress().getJnJAddressId()))
		{
			isIncorretShipTo = checkIncorrectShipTo(jnJB2bUnitModel, cartModel);
			LOGGER.info("isIncorretShipTo value: "+isIncorretShipTo);
		}
		if (null == jnJB2bUnitModel)
		{
			throw new SystemException("No B2bUnit associated with Order having order No [" + cartModel.getCode() + "]");
		}
		String b2bUnitCountry = null;
		if (null != jnJB2bUnitModel.getCountry())
		{
			b2bUnitCountry = jnJB2bUnitModel.getCountry().getIsocode();
		}
		//Fetch the Country Associated with Cart in Order to get Individual Taxes.
		final CountryModel countryModel = jnJB2bUnitModel.getCountry();
		if (null == countryModel)
		{
			throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
					+ "] for Order having order No [" + cartModel.getCode() + "]");
		}
		/* final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode(); */

		final List<SalesOrderSimulationRequest> salesOrderSimulationRequestList = new ArrayList<>();
		// Invoking the getSalesOrgFromAbstOrdEntModel method of JnjOrderUtil class so separate out the spilt order on the basis
		// of Sales Org and Special Product Type.

		Map<SplitOrderData, List<AbstractOrderEntryModel>> abstOrdEntModelMap = new HashMap<>();
		Map<SplitOrderData, List<AbstractOrderEntryModel>> retAbstOrdEntModelMap;

		try
		{
			retAbstOrdEntModelMap = JnjOrderUtil.getSalesOrgFromAbstOrdEntModel(cartModel);
			String sector = null;
			JnJLaProductModel jnjLaProductModel;
			for (final Map.Entry<SplitOrderData, List<AbstractOrderEntryModel>> entry : retAbstOrdEntModelMap.entrySet())
			{
				final SplitOrderData splitOrderData = entry.getKey();
				final List<AbstractOrderEntryModel> orderEntriesList = retAbstOrdEntModelMap.get(splitOrderData);
				if (orderEntriesList != null && !orderEntriesList.isEmpty())
				{
					final ProductModel product = orderEntriesList.get(0).getProduct();
					if (product instanceof JnJLaProductModel)
					{
						jnjLaProductModel = (JnJLaProductModel) product;
						sector = jnjLaProductModel.getSector();
						checkIfPharamProductInOrder(sector);
					}
				}

				final List<JnJSalesOrgCustomerModel> salesOrgList = jnJB2bUnitModel.getSalesOrg();

				for (final JnJSalesOrgCustomerModel salesOrg : salesOrgList)
				{
					if (salesOrg.getSector() != null && salesOrg.getSector().equals(sector) == true)
					{
						splitOrderData.setSalesOrg(salesOrg.getSalesOrg());
					}
				}
				abstOrdEntModelMap.put(splitOrderData, orderEntriesList);
			}
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage("Mapping order values", methodName,
					Logging.SUBMIT_ORDER + Logging.HYPHEN + "Error exception Occurred" + e.getMessage(), e, currentClass);
			abstOrdEntModelMap = new HashMap<>();
		}

		// Iterate the entries of the abstOrdEntModelMap map and populate the List of SalesOrderSimulation Request.
		for (final Entry<SplitOrderData, List<AbstractOrderEntryModel>> abstOrdEntModelMapSet : abstOrdEntModelMap.entrySet())
		{
			if (null != abstOrdEntModelMapSet && !abstOrdEntModelMap.isEmpty())
			{
				JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
						"abstOrdEntModelMapSet :: " + abstOrdEntModelMapSet.getKey().getOrderType(), currentClass);

				for (final AbstractOrderEntryModel am : abstOrdEntModelMapSet.getValue())
				{
					JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName, "am MaterialNumber :: " + am.getMaterialNumber(),
							currentClass);
				}
				final SalesOrderSimulationRequest salesOrderSimulationRequest = new SalesOrderSimulationRequest();
				final SalesOrderIn3 salesOrderIn = new SalesOrderIn3();
				final BTBControlArea bTBControlArea = new BTBControlArea();
				final Date date = new Date();
				final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
				final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);

				final String formatedSecondTime = secondformatter.format(date);
				final String formatedMiliSecondTime = miliSecondformatter.format(date);


				bTBControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.SIMULATE_ORDER + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
						+ cartModel.getCode() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
				bTBControlArea.setTransactionDateTime(formatedMiliSecondTime);
				salesOrderSimulationRequest.setHeader(bTBControlArea);
				// Retrieve the Sales Org and Order Type value from the first entry of the abstOrdEntModelMap map.
				// Mapping of the Mandatory Fields Start
				if (null != abstOrdEntModelMapSet.getKey().getSalesOrg())
				{
					salesOrderIn.setInSalesOrganization(abstOrdEntModelMapSet.getKey().getSalesOrg());
				}
				else
				{
					salesOrderIn.setInSalesOrganization(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				}

				if (null != abstOrdEntModelMapSet.getValue().get(0).getSapOrderType())
				{
					salesOrderIn.setInOrderType(abstOrdEntModelMapSet.getValue().get(0).getSapOrderType());
				}
				else
				{
					salesOrderIn.setInOrderType(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				}

				//Call the config service impl to retrieve the value from the config model.
				final String jnjConfigModelRequestType = jnjConfigService
						.getConfigValueById(Jnjb2bFacadesConstants.Order.REQUEST_TYPE_SIMULATION);

				JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
						Logging.SUBMIT_ORDER + Logging.HYPHEN + "Value from the Config Table " + jnjConfigModelRequestType,
						currentClass);

				salesOrderIn.setInRequestType(jnjConfigModelRequestType);
				final String jnjConfigModelDivision = jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.DIVISION);
				salesOrderIn.setInDivision(jnjConfigModelDivision);

				final String jnjConfigModelDistChannel = jnjConfigService
						.getConfigValueById(Jnjb2bFacadesConstants.Order.DISTRIBUTION_CHANNEL);
				salesOrderIn.setInDistributionChannel(jnjConfigModelDistChannel);
				if (null != cartModel.getDeliveryAddress()
						&& StringUtils.isNotEmpty(cartModel.getDeliveryAddress().getJnJAddressId()))
				{
					salesOrderIn.setInShipToNumber(cartModel.getDeliveryAddress().getJnJAddressId());
				}
				else
				{
					salesOrderIn.setInShipToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				}
				if (null != cartModel.getUnit() && StringUtils.isNotEmpty(cartModel.getUnit().getUid()))
				{
					salesOrderIn.setInSoldToNumber(cartModel.getUnit().getUid());
				}
				else
				{
					salesOrderIn.setInSoldToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				}

				// Mapping of the Optional Fields Start
				setContractToSalesOrderIn(cartModel, salesOrderIn, b2bUnitCountry);

				if (null != cartModel.getCode())
				{
					final JAXBElement<String> customerPortalOrderNumber = objectFactory
							.createSalesOrderIn3InCustomerPortalOrdernumber(cartModel.getCode());
					salesOrderIn.setInCustomerPortalOrdernumber(customerPortalOrderNumber);
				}

				final JAXBElement<String> inOrderChannel = objectFactory
						.createSalesOrderIn3InOrderChannel(null != cartModel.getOrderChannel() ? cartModel.getOrderChannel().getCode()
								: jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_CHANNEL));

				final JAXBElement<String> vtexOrderChannel = objectFactory.createSalesOrderIn3InOrderChannel(sessionService.getAttribute(Jnjlab2bcoreConstants.VTEX_ORDER_CHANNEL));
				LOGGER.info("mapSalesOrderSimulationWrapper - Order Channel Code: " + vtexOrderChannel);
				if (null != vtexOrderChannel.getValue() && VTEX_ORDER_TYPE.equalsIgnoreCase(vtexOrderChannel.getValue())) {
					salesOrderIn.setInOrderChannel(vtexOrderChannel);
				} else {
					salesOrderIn.setInOrderChannel(inOrderChannel);
				}
				if (null != cartModel.getPurchaseOrderNumber())
				{
					final JAXBElement<String> inPONumber = objectFactory
							.createSalesOrderIn3InPONumber(cartModel.getPurchaseOrderNumber());
					salesOrderIn.setInPONumber(inPONumber);
				}
								
				final String industryCode = getConfigurationService().getConfiguration().getString(Jnjlab2bcoreConstants.DISTRIBUTOR_INDUSTRY_CODE);

				setIncotermInSalesOrderIn(jnJB2bUnitModel, b2bUnitCountry, salesOrderIn, industryCode);


				final JAXBElement<String> inRequestDeliveryDate = objectFactory
						.createSalesOrderIn3InRequestedDeliveryDate(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				salesOrderIn.setInRequestedDeliveryDate(inRequestDeliveryDate);

				if (null != cartModel.getUser())
				{
					if (null != cartModel.getUser().getName())
					{
						final JAXBElement<String> contactName = objectFactory
								.createSalesOrderIn3InContactName(cartModel.getUser().getName());
						salesOrderIn.setInContactName(contactName);
					}
					if (null != ((B2BCustomerModel) cartModel.getUser()).getEmail())
					{
						final JAXBElement<String> contactEmail = objectFactory
								.createSalesOrderIn3InContactEmail(((B2BCustomerModel) cartModel.getUser()).getEmail());
						salesOrderIn.setInContactEmail(contactEmail);
					}
				}
				if (null != cartModel.getCompleteDelivery())
				{
					final JAXBElement<String> inCompleteDelivery = objectFactory
							.createSalesOrderIn3InCompleteDelivery(cartModel.getCompleteDelivery());
					salesOrderIn.setInCompleteDelivery(inCompleteDelivery);
				}
				JnjContractModel jnjContractModel = null;
				if (StringUtils.isNotEmpty(cartModel.getContractNumber())) {
				   jnjContractModel = jnjContractService.getContractDetailsById(cartModel.getContractNumber());
				}
				if (null != cartModel.getForbiddenSales() && StringUtils.isNotEmpty(cartModel.getForbiddenSales())
						&& (null != jnjContractModel && (null != jnjContractModel.getContractOrderReason()
								&& StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
								&& !Jnjlab2bcoreConstants.Contract.LOAD_CONTRACT_ORDER_REASON_Z48
										.equalsIgnoreCase(jnjContractModel.getContractOrderReason()))))
				{
					final JAXBElement<String> inForBiddenSalesElement = objectFactory
							.createSalesOrderIn3InForbiddenSales(cartModel.getForbiddenSales());
					salesOrderIn.setInForbiddenSales(inForBiddenSalesElement);
				}

				setContractToSalesOrderIn(cartModel, salesOrderIn, b2bUnitCountry);

				// Call the User Service to get User Model of Current User.
				final UserModel userModel = userService.getCurrentUser();
				if (null != userModel && null != userModel.getDefaultPaymentAddress())
				{
					// check if the cell phone is existed
					if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getCellphone()))
					{
						final JAXBElement<String> inContactPhoneNumber = objectFactory
								.createSalesOrderIn3InContactPhoneNumber(userModel.getDefaultPaymentAddress().getCellphone());
						salesOrderIn.setInContactPhoneNumber(inContactPhoneNumber);
					}
					//check if the phone1 is existed
					else if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getPhone1()))
					{
						final JAXBElement<String> inContactPhoneNumber = objectFactory
								.createSalesOrderIn3InContactPhoneNumber(userModel.getDefaultPaymentAddress().getPhone1());
						salesOrderIn.setInContactPhoneNumber(inContactPhoneNumber);
					}
				}

				// Display order split for Ecuador
				/*
				 * if (StringUtils.equalsIgnoreCase(loggedInSite, Jnjb2bCoreConstants.COUNTRY_ISO_EQUADOR)) {
				 * addEntriesToCartPlitEC(abstOrdEntModelMapSet, jnjCartDataList, cartModel.getCode()); if
				 * (!jnjCartDataList.isEmpty()) { sessionService.setAttribute("jnjCartDataList", jnjCartDataList); } }
				 */
				// Mapping of the Optional Fields End
				// Get the value from the map which contains the List of AbstractOrderEntryModel Object and iterate them one by one to populate the orde entries in the request object.
				for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelMapSet.getValue())
				{
					// To invoke the setOrderLineForSimulation method to get the data from the AbstractOrderEntryModel object and set into InOrderLines3 object.
					final InOrderLines3 inOrderLines = setOrderLineForSimulation(abstOrderEntryModel);

					salesOrderIn.getInOrderLines().add(inOrderLines);
				}

				salesOrderSimulationRequest.setSalesOrderIn(salesOrderIn);
				salesOrderSimulationRequestList.add(salesOrderSimulationRequest);
			}
		}
		//}

		final List<String> sapErrorMessagelist = new ArrayList<>();
		Map<String, JnjOutOrderLine> freeGoodsMap = new HashMap<>();
		String outSalesOrderCreditStatus = null;
		String outSalesOrderRejectionStatus = null;

		validateOrderData.setSapErrorResponse(Boolean.FALSE);
		if(isIncorretShipTo)
		{
			LOGGER.info("Shipto Status:::::::::::::::::::: " + isIncorretShipTo);
			validateOrderData.setSapErrorResponse(Boolean.TRUE);
			validateOrderData.setValidateOrderResponse(Boolean.FALSE);
			sapErrorMessagelist.add("incorrectShipToSelected");
			validateOrderData.setSapErrorMessages(sapErrorMessagelist);
			isErrorInResponse=true;
			LOGGER.info("Error response is updated********************: " + isIncorretShipTo);

		}
		// Iterate the list of SalesOrderSimulateRequest and form the list of SalesOrderSimulationResponse Object.
		if(!isIncorretShipTo){
			
			for (final SalesOrderSimulationRequest salesOrderSimulateRequest : salesOrderSimulationRequestList)
			{
				outOrderLineObjFlag = false;
	
				final SalesOrderSimulationResponse salesOrderSimulationResponse = jnjLatamSalesOrder
						.salesOrderSimulationWrapper(salesOrderSimulateRequest);
	
				final Map<String, String> quantityMap = new HashMap<>();
				final List<InOrderLines3> inorderLineList = salesOrderSimulateRequest.getSalesOrderIn().getInOrderLines();
				for (final InOrderLines3 inorderLine : inorderLineList)
				{
					quantityMap.put(inorderLine.getMaterialNumber(), inorderLine.getQuantityRequested());
				}
	
				if (null != salesOrderSimulationResponse && null != salesOrderSimulationResponse.getSalesOrderOut()
						&& null != salesOrderSimulationResponse.getSalesOrderOut().getOutOrderLines())
				{
					JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "mapSalesOrderSimulationWrapper()" + Logging.HYPHEN
									+ "after getting response  outSalesOrderOverallStatus "
									+ salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderOverallStatus(),
							currentClass);
	
					JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response  outSalesOrderOverallStatus "
									+ salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderOverallStatus(),
							currentClass);
	
					// Iterate the List of OutOrderLines3 object from the response object and put in the mapMaterialNumberWithOutOrderLine map object.
					final Integer isProductFromSameCart = sessionService.getAttribute("isProductFromSameCart");
					int i = 0;
					if (isProductFromSameCart != null)
					{
						i = isProductFromSameCart.intValue();
					}
					if (i == 0)
					{
						salesOrderTotalNetValue = 0;
						mapMaterialNumberWithOutOrderLine.clear();
					}
					String orderedQuantity = null;
					String materialEntered = null;
					String lineNumber = StringUtils.EMPTY;
	
					final Map<String, JnjOutOrderLine> materialLineToMaterialEntered = new HashMap<>();
					final Map<String, JnjOutOrderLine> freeGoodsMapFromSession = sessionService
							.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
					if (freeGoodsMapFromSession == null)
					{
						freeGoodsMap = new HashMap<>();
					}
					else
					{
						freeGoodsMap = new HashMap<>(freeGoodsMapFromSession);
					}
					
					//INC000022269502: Adding condition to check to handle "credit customer block" error in response.
					isErrorInResponse= checkResponseForErrors(salesOrderSimulationResponse,validateOrderData,sapErrorMessagelist,cartModel);
	
					validateOrderData.setSapErrorMessages(sapErrorMessagelist);
					
					if(!isErrorInResponse)
					{
						
						for (final OutOrderLines3 outOrderLine : salesOrderSimulationResponse.getSalesOrderOut().getOutOrderLines())
						{
							if (null != outOrderLine)
							{
								if (quantityMap.containsKey(outOrderLine.getMaterialEntered())
										&& (Jnjb2bCoreConstants.BonusItem.CHRGD_ITEM_CATEGORY.equals(outOrderLine.getItemCategory()) || Jnjlab2bcoreConstants.ZTAS_ITEM_CATEGORY.equals(outOrderLine.getItemCategory())))
								{
									orderedQuantity = quantityMap.get(outOrderLine.getMaterialEntered());
									materialEntered = outOrderLine.getMaterialEntered();
									lineNumber = outOrderLine.getLineNumber();
	
									if (Jnjlab2bcoreConstants.ZTAS_ITEM_CATEGORY.equals(outOrderLine.getItemCategory())) { // Hiding ATP for ZTAS
										setATPProperties(cartModel, outOrderLine);
									}
								}
								populateFreeItemMaterialTrigger(materialLineToMaterialEntered, outOrderLine);
	
								if(!outOrderLine.getHigherLevelItemNumber().equalsIgnoreCase("000000")){
									if ((Jnjb2bCoreConstants.BonusItem.FREE_ITEM_CATEGORY.equals(outOrderLine.getItemCategory()) || Jnjlab2bcoreConstants.ZFNN_ITEM_CATEGORY.equals(outOrderLine.getItemCategory())
											|| Jnjlab2bcoreConstants.ZFRY_ITEM_CATEGORY.equals(outOrderLine.getItemCategory()))){
										final JnjOutOrderLine jnjOutOrderLine = new JnjOutOrderLine();
	
										setFreeItemQuantity(orderedQuantity, materialEntered, lineNumber, outOrderLine, jnjOutOrderLine);
										mapValuesToJnjOutOrderLine(jnjOutOrderLine, outOrderLine);
										freeGoodsMap.put(materialLineToMaterialEntered.get(outOrderLine.getHigherLevelItemNumber()).getMaterialEntered(),jnjOutOrderLine);
									}
								}else{
									mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
									outOrderLineObjFlag = true;
								}
							}
	
							// Enter in the if block if out order line object is null in response.
							LOGGER.info("outOrderLineObjFlag@@@@@@@@@@@@@@@@@@@@@@@@@@:"+outOrderLineObjFlag);
							if (!outOrderLineObjFlag)
							{
								JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
										Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response  for Hard Stop Error", currentClass);
	
								validateOrderData.setSapErrorResponse(Boolean.TRUE);
								//temporary making true till this method fixed. Please revert the code and make it false once fixed.
								validateOrderData.setValidateOrderResponse(Boolean.FALSE);
								//validateOrderData.setValidateOrderResponse(Boolean.TRUE);
								isErrorInResponse = true;
								for (final MessageReturn3 message3Response : salesOrderSimulationResponse.getSalesOrderOut().getMessageReturn())
								{
									JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
											Logging.SUBMIT_ORDER + Logging.HYPHEN + "after getting response  for Hard Stop Error TYPE",
											currentClass);
	
									if (message3Response.getTYPE() != null && message3Response.getID() != null
											&& message3Response.getTYPE().equalsIgnoreCase(Jnjb2bCoreConstants.CONST_CODE_TYPE))
									{
										//Improvement for errors coming at header level
										errorMessageData = new JnjSAPErrorMessageData();
	
										errorMessageData.setId(message3Response.getID());
										errorMessageData.setErrorMessage(message3Response.getMESSAGE());
										errorMessageData.setErrorMessageType(message3Response.getTYPE());
										errorMessageData.setNumber(message3Response.getNUMBER());
	
										/* Adding Entry to SAP Log File */
										SAPLOGGER.error(errorMessageData.getErrorMessageType() + Jnjb2bCoreConstants.Order.SEMI_COLON
												+ errorMessageData.getId() + Jnjb2bCoreConstants.Order.SEMI_COLON + errorMessageData.getNumber()
												+ Jnjb2bCoreConstants.Order.SEMI_COLON + errorMessageData.getErrorMessage());
	
										sapErrorMessagelist
												.add(jnjSAPErrorMessageFacade.getErrorDetails(errorMessageData, cartModel.getEntries()));
									}
								}
								validateOrderData.setSapErrorMessages(sapErrorMessagelist);
							}
	
							if (null != salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderCreditStatus())
							{
								outSalesOrderCreditStatus = salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderCreditStatus()
										.getValue();
							}
							if (null != salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderRejectionStatus())
							{
								outSalesOrderRejectionStatus = salesOrderSimulationResponse.getSalesOrderOut().getOutSalesOrderRejectionStatus()
										.getValue();
							}
						}
					}
					
					final String outSalesOrdertTotalNetValue = salesOrderSimulationResponse.getSalesOrderOut()
							.getOutSalesOrdertTotalNetValue();
	
					if (StringUtils.isNotEmpty(outSalesOrdertTotalNetValue))
					{
						salesOrderTotalNetValue = salesOrderTotalNetValue + Double.parseDouble(outSalesOrdertTotalNetValue);
					}
				}
	
				sessionService.setAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP, freeGoodsMap);
	
				if (!mapMaterialNumberWithOutOrderLine.isEmpty())
				{
					CartModel existingCartModel = getCart();
	
					existingCartModel = mapSalesOrderSimulationResponse(mapMaterialNumberWithOutOrderLine, existingCartModel,
							salesOrderTotalNetValue, outSalesOrderCreditStatus, outSalesOrderRejectionStatus);
					// To invoke the JnjOrderServiceImpl class to persist the simulate order data in hybris database.
					jnjCartService.saveCartModel(existingCartModel, true);
					cartModelFlag = jnjCartService.calculateValidatedCart(existingCartModel);
	
					if (!isErrorInResponse)
					{
						validateOrderData.setValidateOrderResponse(Boolean.valueOf(cartModelFlag));
						JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName,
								"Inside the  if block when isErrorInResponse flag false  or there is no error", currentClass);
					}
	
				}
				JnjGTCoreUtil.logDebugMessage("Mapping sales order", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
						+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
	
				LOGGER.info("validateOrderData error response value: " + validateOrderData.getSapErrorResponse() + " validate order response value: " + validateOrderData.getValidateOrderResponse());
	
				return validateOrderData;
			}
		}
		
		LOGGER.info("validateOrderData error response value: " + validateOrderData.getSapErrorResponse() + " validate order response value: " + validateOrderData.getValidateOrderResponse());
		return validateOrderData;

	}

	private static void setIncotermInSalesOrderIn(JnJLaB2BUnitModel jnJB2bUnitModel, String b2bUnitCountry, SalesOrderIn3 salesOrderIn, String industryCode) {
		if (CUSTOMER_FREIGHT_TYPE.equals(jnJB2bUnitModel.getCustomerFreightType()) && industryCode.equals(jnJB2bUnitModel.getIndustryCode1()) && b2bUnitCountry.equals(Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL)) {
			final JAXBElement<String> inIncoterms = objectFactory
					.createSalesOrderIn3InIncoterms(jnJB2bUnitModel.getCustomerFreightType());
					salesOrderIn.setInIncoterms(inIncoterms);

		} else {
			final JAXBElement<String> inIncoterms = objectFactory
					.createSalesOrderIn3InIncoterms(StringUtils.EMPTY);
			salesOrderIn.setInIncoterms(inIncoterms);
		}
	}

	private boolean checkResponseForErrors(SalesOrderSimulationResponse salesOrderSimulationResponse, JnjValidateOrderData validateOrderData,List<String> sapErrorMessagelist, CartModel cartModel)
	{
		boolean isErrorInResponse=false;
		
		for (final MessageReturn3 message3ResponseData : salesOrderSimulationResponse.getSalesOrderOut().getMessageReturn())
		{
			if (message3ResponseData.getTYPE() != null && message3ResponseData.getID() != null
					&& Jnjb2bCoreConstants.CONST_CODE_TYPE.equalsIgnoreCase(message3ResponseData.getTYPE()))
			{
				LOGGER.info("checkResponseForErrors###############################");
				validateOrderData.setSapErrorResponse(Boolean.TRUE);
				validateOrderData.setValidateOrderResponse(Boolean.FALSE);
				isErrorInResponse = true;

				final JnjSAPErrorMessageData errorMessageData = new JnjSAPErrorMessageData();
				errorMessageData.setId(message3ResponseData.getID());
				errorMessageData.setErrorMessage(message3ResponseData.getMESSAGE());
				errorMessageData.setErrorMessageType(message3ResponseData.getTYPE());
				errorMessageData.setNumber(message3ResponseData.getNUMBER());

				sapErrorMessagelist
						.add(jnjSAPErrorMessageFacade.getErrorDetails(errorMessageData, cartModel.getEntries()));
			}
		}
		return isErrorInResponse;
	}
    private void setFreeItemQuantity(String orderedQuantity, String materialEntered, String lineNumber, OutOrderLines3 outOrderLine, JnjOutOrderLine jnjOutOrderLine) {
		jnjOutOrderLine.setOrderedQuantity(outOrderLine.getMaterialQuantity().toString());
	}

    private void populateFreeItemMaterialTrigger(Map<String, JnjOutOrderLine> materialLineToMaterialEntered, OutOrderLines3 outOrderLine) {
		if(outOrderLine.getHigherLevelItemNumber().equalsIgnoreCase("000000")){
			final JnjOutOrderLine jnjOutOrderLine = new JnjOutOrderLine();
			jnjOutOrderLine.setMaterialEntered(outOrderLine.getMaterialEntered());
			materialLineToMaterialEntered.put(outOrderLine.getLineNumber(), jnjOutOrderLine);
		}
	}

	private void populateFreeItemMaterialTrigger(Map<String, JnjOutOrderLine> materialLineToMaterialEntered, OutOrderLines outOrderLine) {
		if(outOrderLine.getHigherLevelItemNumber().equalsIgnoreCase("000000")){
			final JnjOutOrderLine jnjOutOrderLine = new JnjOutOrderLine();
			jnjOutOrderLine.setMaterialEntered(outOrderLine.getMaterialEntered());
			materialLineToMaterialEntered.put(outOrderLine.getLineNumber(), jnjOutOrderLine);
		}
	}

	private void setContractToSalesOrderIn(final CartModel cartModel, final SalesOrderIn3 salesOrderIn,
			final String b2bUnitCountry)
	{
		if (null != cartModel.getContractNumber() && !cartModel.getContractNumber().isEmpty())
		{
			final JAXBElement<String> contractReferenceNumber = objectFactory
					.createSalesOrderIn2InContractReferenceNumber(cartModel.getContractNumber());
			salesOrderIn.setInContractReferenceNumber(contractReferenceNumber);

			// Call the Jnj Contract Service to get the Jnj Contract Model object so that we can compare the Contract Order Reason value with BID orders value.
			JnjContractModel jnjContractModel = null;
			if (StringUtils.isNotEmpty(cartModel.getContractNumber())) {
			    jnjContractModel = jnjContractService.getContractDetailsById(cartModel.getContractNumber());
			}
			if (null != jnjContractModel && (null != jnjContractModel.getContractOrderReason()
					&& StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
					&& Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_1
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())
					|| Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_2
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())))
			{
				// Creating the object from the object factory to form the JAXB element.
				final String jnjConfigModelOrderReason = jnjConfigService
						.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_REASON);
				final JAXBElement<String> inOrderReason = objectFactory.createSalesOrderIn2InOrderReason(jnjConfigModelOrderReason);
				salesOrderIn.setInOrderReason(inOrderReason);
			}
			else
			{
				final JAXBElement<String> inOrderReason = objectFactory
						.createSalesOrderIn2InOrderReason(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				salesOrderIn.setInOrderReason(inOrderReason);
			}
			if (null != jnjContractModel && (null != jnjContractModel.getContractOrderReason()
					&& StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason())
					&& !Jnjlab2bcoreConstants.Contract.LOAD_CONTRACT_ORDER_REASON_Z48
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())))
			{
				setForbiddenFlag(salesOrderIn, b2bUnitCountry);
			}
			
		}
	}


	private void setContractToSalesOrderIn(final CartModel cartModel, final SalesOrderIn2 salesOrderIn)
	{
		if (null != cartModel.getContractNumber())
		{
			final JAXBElement<String> contractReferenceNumber = objectFactory
					.createSalesOrderIn2InContractReferenceNumber(cartModel.getContractNumber());
			salesOrderIn.setInContractReferenceNumber(contractReferenceNumber);

			// Call the Jnj Contract Service to get the Jnj Contract Model object so that we can compare the Contract Order Reason value with BID orders value.
			JnjContractModel jnjContractModel = null;
			if (StringUtils.isNotEmpty(cartModel.getContractNumber())) {
			  jnjContractModel = jnjContractService.getContractDetailsById(cartModel.getContractNumber());
			}
			if ((null != jnjContractModel
					&& StringUtils.isNotEmpty(jnjContractModel.getContractOrderReason()))
					&& ( Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_1
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())
					|| Jnjb2bCoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASON_2
							.equalsIgnoreCase(jnjContractModel.getContractOrderReason())))
			{
				// Creating the object from the object factory to form the JAXB element.
				final String jnjConfigModelOrderReason = jnjConfigService
						.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_REASON);
				final JAXBElement<String> inOrderReason = objectFactory.createSalesOrderIn2InOrderReason(jnjConfigModelOrderReason);
				salesOrderIn.setInOrderReason(inOrderReason);
			}
			else
			{
				final JAXBElement<String> inOrderReason = objectFactory
						.createSalesOrderIn2InOrderReason(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				salesOrderIn.setInOrderReason(inOrderReason);
			}
		}
	}

	@Override
	public SalesOrderPricingResponse mapSalesOrderPricingWrapper(final CartModel cartModel, final String orderEntryNumber)
			throws IntegrationException, TimeoutException
	{
		final String methodName = "mapSalesOrderPricingWrapper()";
		JnjGTCoreUtil.logDebugMessage("Mapping sales order price", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		final SalesOrderPricingRequest salesOrderPricingRequest = new SalesOrderPricingRequest();

		final SalesOrderIn2 salesOrderIn = new SalesOrderIn2();

		final BTBControlArea bTBControlArea = new BTBControlArea();
		final Date date = new Date();
		final Format miliSecondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.MILI_SECOND_FORMATTER);
		final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);

		final String formatedSecondTime = secondformatter.format(date);
		final String formatedMiliSecondTime = miliSecondformatter.format(date);


		bTBControlArea.setUniqueTransactionID(Jnjb2bCoreConstants.GET_PRICE + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL
				+ cartModel.getCode() + Jnjb2bCoreConstants.UNDERSCORE_SYMBOL + formatedSecondTime);
		bTBControlArea.setTransactionDateTime(formatedMiliSecondTime);
		salesOrderPricingRequest.setHeader(bTBControlArea);

		// Mapping the Mandatory Fields Start
		//Call the config service impl to retrieve the value from the config model.
		final String jnjConfigModelRequestType = jnjConfigService
				.getConfigValueById(Jnjb2bFacadesConstants.Order.REQUEST_TYPE_PRICING);

		JnjGTCoreUtil.logDebugMessage("Mapping sales order price", methodName,
				Logging.SUBMIT_ORDER + Logging.HYPHEN + "Value from the Config Table " + jnjConfigModelRequestType, currentClass);

		salesOrderIn.setInRequestType(jnjConfigModelRequestType);

		final String jnjConfigModelDistChannel = jnjConfigService
				.getConfigValueById(Jnjb2bFacadesConstants.Order.DISTRIBUTION_CHANNEL);
		salesOrderIn.setInDistributionChannel(jnjConfigModelDistChannel);
		final String jnjConfigModelDivision = jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.DIVISION);
		salesOrderIn.setInDivision(jnjConfigModelDivision);
		if (null != cartModel.getDeliveryAddress() && StringUtils.isNotEmpty(cartModel.getDeliveryAddress().getJnJAddressId()))
		{
			salesOrderIn.setInShipToNumber(cartModel.getDeliveryAddress().getJnJAddressId());
		}
		else
		{
			salesOrderIn.setInShipToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}
		if (null != cartModel.getUnit() && StringUtils.isNotEmpty(cartModel.getUnit().getUid()))
		{
			salesOrderIn.setInSoldToNumber(cartModel.getUnit().getUid());
		}
		else
		{
			salesOrderIn.setInSoldToNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}
		// Mapping of the Mandatory Fields End

		if (null != cartModel.getCode())
		{
			final JAXBElement<String> customerPortalOrderNumber = objectFactory
					.createSalesOrderIn2InCustomerPortalOrdernumber(cartModel.getCode());
			salesOrderIn.setInCustomerPortalOrdernumber(customerPortalOrderNumber);
		}
		if (null != cartModel.getPurchaseOrderNumber())
		{
			final JAXBElement<String> inPONumber = objectFactory.createSalesOrderIn2InPONumber(cartModel.getPurchaseOrderNumber());
			salesOrderIn.setInPONumber(inPONumber);
		}
		final String jnjConfigModelOrderChannel = jnjConfigService.getConfigValueById(Jnjb2bFacadesConstants.Order.ORDER_CHANNEL);
		if (null != jnjConfigModelOrderChannel && !jnjConfigModelOrderChannel.isEmpty())
		{
			final JAXBElement<String> inOrderChannel = objectFactory.createSalesOrderIn2InOrderChannel(jnjConfigModelOrderChannel);
			salesOrderIn.setInOrderChannel(inOrderChannel);
		}
		final JAXBElement<String> inRequestDeliveryDate = objectFactory
				.createSalesOrderIn2InRequestedDeliveryDate(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		salesOrderIn.setInRequestedDeliveryDate(inRequestDeliveryDate);
		if (null != cartModel.getUser())
		{
			if (null != cartModel.getUser().getName())
			{
				final JAXBElement<String> contactName = objectFactory.createSalesOrderIn2InContactName(cartModel.getUser().getName());
				salesOrderIn.setInContactName(contactName);
			}
			if (null != ((B2BCustomerModel) cartModel.getUser()).getEmail())
			{
				final JAXBElement<String> contactEmail = objectFactory
						.createSalesOrderIn2InContactEmail(((B2BCustomerModel) cartModel.getUser()).getEmail());
				salesOrderIn.setInContactEmail(contactEmail);
			}
		}

		if (null != cartModel.getCompleteDelivery())
		{
			final JAXBElement<String> inCompleteDelivery = objectFactory
					.createSalesOrderIn2InCompleteDelivery(cartModel.getCompleteDelivery());
			salesOrderIn.setInCompleteDelivery(inCompleteDelivery);
		}
		if (null != cartModel.getForbiddenSales())
		{
			final JAXBElement<String> inForBiddenSalesElement = objectFactory
					.createSalesOrderIn2InForbiddenSales(cartModel.getForbiddenSales());
			salesOrderIn.setInForbiddenSales(inForBiddenSalesElement);
		}

		// Check for the Not Empty and Not Null
		setContractToSalesOrderIn(cartModel, salesOrderIn);

		// Call the User Service to get User Model of Current User.
		final UserModel userModel = userService.getCurrentUser();
		if (null != userModel && null != userModel.getDefaultPaymentAddress())
		{
			// check if the cell phone is existed
			if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getCellphone()))
			{
				final JAXBElement<String> inContactPhoneNumber = objectFactory
						.createSalesOrderIn2InContactPhoneNumber(userModel.getDefaultPaymentAddress().getCellphone());
				salesOrderIn.setInContactPhoneNumber(inContactPhoneNumber);
			}
			//check if the phone1 is existed
			else if (StringUtils.isNotEmpty(userModel.getDefaultPaymentAddress().getPhone1()))
			{
				final JAXBElement<String> inContactPhoneNumber = objectFactory
						.createSalesOrderIn2InContactPhoneNumber(userModel.getDefaultPaymentAddress().getPhone1());
				salesOrderIn.setInContactPhoneNumber(inContactPhoneNumber);
			}
		}
		// Mapping of the Optional Fields End

		// Calling the getEntryModelForNumber method of JnJ Cart Service to retrieve the abstOrderEntryModel.
		final AbstractOrderEntryModel abstOrderEntryModel = jnjCartService.getEntryModelForNumber(cartModel,
				Integer.parseInt(orderEntryNumber));

		// to set only the order entry for which we want the get price information.
		if (null != abstOrderEntryModel)
		{
			final InOrderLines2 inOrderLines = setOrderLineForPricing(abstOrderEntryModel);

			salesOrderIn.getInOrderLines().add(inOrderLines);
			salesOrderIn.setInSalesOrganization(abstOrderEntryModel.getSalesOrg());
			salesOrderIn.setInOrderType(abstOrderEntryModel.getSapOrderType());
		}
		// As these are the Mandatory fields
		else
		{
			salesOrderIn.setInSalesOrganization(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			salesOrderIn.setInOrderType(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		salesOrderPricingRequest.setSalesOrderIn(salesOrderIn);

		final SalesOrderPricingResponse salesOrderPricingResponse = jnjLatamSalesOrder
				.salesOrderPricingWrapper(salesOrderPricingRequest);

		JnjGTCoreUtil.logDebugMessage("Mapping sales order price", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return salesOrderPricingResponse;
	}

	protected CartModel mapSalesOrderSimulationResponse(final Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine,
			final CartModel cartModel, final double salesOrderTotalNetValue, final String outSalesOrderCreditStatus,
			final String outSalesOrderRejectionStatus) throws SystemException
	{
		final String methodName = "mapSalesOrderSimulationResponse()";

		double totalTax = 0;
		double totalFreightFees = 0;
		double totalExpeditedFee = 0;
		double handlingFee = 0;
		double dropShipFee = 0;
		double minimumOrderFee = 0;
		double totalInsurance = 0;
		double totalDiscounts = 0;
		double grossPrice = 0;
		double taxValueForNetPrice = 0;
		final double subTotal = 0;
		JnjObjectComparator jnjObjectComparator;
		Set<String> scheduleLineDeliveryDate;

		JnjGTCoreUtil.logDebugMessage("Mapping sales order response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		// Check the mapMaterialNumberWithOutOrderLine map as null and not empty.
		if (null != mapMaterialNumberWithOutOrderLine && !mapMaterialNumberWithOutOrderLine.isEmpty())
		{
			// Check the cartModel & cartModel Entries as null and not empty.
			if (null != cartModel && null != cartModel.getEntries() && !cartModel.getEntries().isEmpty())
			{
				//Get the Currency Associated with Cart
				final CurrencyModel currencyModel = cartModel.getCurrency();

				//Getting the B2bUnit with Cart
				final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) cartModel.getUnit();
				if (null == jnJB2bUnitModel)
				{
					throw new SystemException("No B2bUnit associated with Order having order No [" + cartModel.getCode() + "]");
				}

				//Fetch the Country Associated with Cart in Order to get Individual Taxes.
				final CountryModel countryModel = jnJB2bUnitModel.getCountry();

				if (null == countryModel)
				{
					throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
							+ "] for Order having order No [" + cartModel.getCode() + "]");
				}
				final String loggedInSite = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite().getIsocode();

				// List to store values for total calculation on cart split
				final List<OutOrderLines3> outOrderLinesList = new ArrayList<>();

				// Iterating the Order line object and setting the value in Abstract order entry model.
				final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
				// Iterate the cartModel entries one by one and populates those value in cartModel.
				for (final AbstractOrderEntryModel abstOrdEntMode : cartModel.getEntries())
				{
					populateFreeItemDataInOrderEntry(freeGoodsMap, abstOrdEntMode);
					double materialQuantity = 0.0;
					double scheduleLineConfirmedQuantity = 0.0;
					if (mapMaterialNumberWithOutOrderLine.containsKey(abstOrdEntMode.getMaterialEntered()))
					{
						final OutOrderLines3 outOrderLine = mapMaterialNumberWithOutOrderLine.get(abstOrdEntMode.getMaterialEntered());
						outOrderLinesList.add(outOrderLine);
						final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<>();
						if (StringUtils.isNotEmpty(outOrderLine.getMaterialQuantity()))
						{
							abstOrdEntMode.setQuantity(JnjLaCoreUtil.getLongValueFromDecimalString(outOrderLine.getMaterialQuantity()));
							materialQuantity = Double.parseDouble(outOrderLine.getMaterialQuantity());
						}
						// Changes base vale to unit value and total price for entry to net value.
						if (StringUtils.isNotEmpty(outOrderLine.getUnitValue()))
						{
							final double basePrice = Double.parseDouble(outOrderLine.getUnitValue());
							// Check the default price and base price is not equal then only base price value is updated in default price
							if (Double.compare(abstOrdEntMode.getBasePrice().doubleValue(), basePrice) != 0)
							{
								abstOrdEntMode.setDefaultPrice(abstOrdEntMode.getBasePrice());
							}
							abstOrdEntMode.setBasePrice(Double.valueOf(basePrice));
						}
						else
						{
							JnjGTCoreUtil.logDebugMessage("Mapping sales order price", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
									+ "Inside else block when we get unit value as null or empty in SAP response", currentClass);
						}

						if (StringUtils.isNotEmpty(outOrderLine.getNetValue()))
						{
							abstOrdEntMode.setTotalPrice(Double.valueOf(outOrderLine.getNetValue()));
						}

						final List<String> taxesLocalCountriesList = JnjLaCoreUtil
								.getCountriesList(Jnjlab2bcoreConstants.KEY_TAXES_LOCAL_COUNTRIES);
						final List<String> taxesCountriesList = JnjLaCoreUtil
								.getCountriesList(Jnjlab2bcoreConstants.KEY_TAXES_COUNTRIES);

						if (taxesLocalCountriesList.contains(loggedInSite))
						{
							if (StringUtils.isNotEmpty(outOrderLine.getTaxesLocal()) && StringUtils.isNotEmpty(outOrderLine.getTaxes()))
							{
								abstOrdEntMode
										.setTaxValues(createTaxValues((Double.valueOf(outOrderLine.getTaxesLocal())), currencyModel));
								totalTax = totalTax + Double.parseDouble(outOrderLine.getTaxesLocal());
								taxValueForNetPrice = taxValueForNetPrice + Double.parseDouble(outOrderLine.getTaxes());
							}
						}
						else if (taxesCountriesList.contains(loggedInSite))
						{
							if (StringUtils.isNotEmpty(outOrderLine.getTaxes()))
							{
								abstOrdEntMode.setTaxValues(createTaxValues((Double.valueOf(outOrderLine.getTaxes())), currencyModel));
								totalTax = totalTax + Double.parseDouble(outOrderLine.getTaxes());
								taxValueForNetPrice = totalTax;
							}
						}

						if (StringUtils.isNotEmpty(outOrderLine.getFreightFees()))
						{
							abstOrdEntMode.setFreightFees(Double.valueOf(outOrderLine.getFreightFees()));
							totalFreightFees = totalFreightFees + abstOrdEntMode.getFreightFees().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getExpeditedFees()))
						{
							abstOrdEntMode.setExpeditedFee(Double.valueOf(outOrderLine.getExpeditedFees()));
							totalExpeditedFee = totalExpeditedFee + abstOrdEntMode.getExpeditedFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getDiscounts()))
						{
							//abstOrdEntMode.setDiscountsOnPrice(Double.valueOf(outOrderLine.getDiscounts()));
							abstOrdEntMode
									.setDiscountValues(createDiscountValues(Double.valueOf(outOrderLine.getDiscounts()), currencyModel));
							totalDiscounts = totalDiscounts + Double.parseDouble(outOrderLine.getDiscounts());
						}
						if (StringUtils.isNotEmpty(outOrderLine.getGrossPrice()))
						{
							abstOrdEntMode.setGrossPrice(Double.valueOf(outOrderLine.getGrossPrice()));
							//grossPrice = grossPrice + abstOrdEntMode.getGrossPrice().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getInsurance()))
						{
							abstOrdEntMode.setInsurance(Double.valueOf(outOrderLine.getInsurance()));
							totalInsurance = totalInsurance + abstOrdEntMode.getInsurance().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getHandlingFee()))
						{
							abstOrdEntMode.setHandlingFee(Double.valueOf(outOrderLine.getHandlingFee()));
							handlingFee = handlingFee + abstOrdEntMode.getHandlingFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getDropshipFee()))
						{
							abstOrdEntMode.setDropshipFee(Double.valueOf(outOrderLine.getDropshipFee()));
							dropShipFee = dropShipFee + abstOrdEntMode.getDropshipFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLine.getMinimumOrderFee()))
						{
							abstOrdEntMode.setMinimumOrderFee(Double.valueOf(outOrderLine.getMinimumOrderFee()));
							minimumOrderFee = minimumOrderFee + abstOrdEntMode.getMinimumOrderFee().doubleValue();
						}

						if (StringUtils.isNotEmpty(outOrderLine.getMaterialQuantity()))
						{
							abstOrdEntMode.setQuantity(JnjLaCoreUtil.getLongValueFromDecimalString(outOrderLine.getMaterialQuantity()));
							//abstOrdEntMode.setReasonForRejection(StringoutOrderLine.getReasonForRejection());
							materialQuantity = Double.parseDouble(outOrderLine.getMaterialQuantity());
						}
						// Changes base vale to unit value and total price for entry to net value.
						if (StringUtils.isNotEmpty(outOrderLine.getUnitValue()))
						{
							abstOrdEntMode.setBasePrice(Double.valueOf(Double.parseDouble(outOrderLine.getUnitValue())));
						}
						if (StringUtils.isNotEmpty(outOrderLine.getNetValue()))
						{
							abstOrdEntMode.setTotalPrice(Double.valueOf(outOrderLine.getNetValue()));
						}
						//subTotal += abstOrdEntMode.getTotalPrice();
						if (CollectionUtils.isNotEmpty(outOrderLine.getScheduledLines()))
						{
							// sorting the schedule line object on the basis of the line number in descending order.
							jnjObjectComparator = new JnjObjectComparator(ScheduledLines3.class, "getLineNumber", false, true);
							Collections.sort(outOrderLine.getScheduledLines(), jnjObjectComparator);

							scheduleLineDeliveryDate = new HashSet<>();
							// Iterating the schedule line object and setting its value in the JnJ Delivery Schedule Model.
							Date deliveryDate;
							for (final ScheduledLines3 scheduleLine : outOrderLine.getScheduledLines())
							{
								if (null != scheduleLine && StringUtils.isNotEmpty(scheduleLine.getConfirmedQuantity())
										&& Double.compare(Jnjb2bFacadesConstants.ZERO_IN_DOUBLE,
												Double.parseDouble(scheduleLine.getConfirmedQuantity())) != 0
										&& !scheduleLineDeliveryDate.contains(scheduleLine.getDeliveryDate())
										&& (int) materialQuantity != (int) scheduleLineConfirmedQuantity)
								{
									final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
									jnjDelSchModel.setLineNumber(scheduleLine.getLineNumber());
									jnjDelSchModel.setQty(Long.valueOf(Double.valueOf(scheduleLine.getConfirmedQuantity()).longValue()));
									// set the value in the double variable so that we can compare the requested quantity with schedule confirmed quantity
									scheduleLineConfirmedQuantity = scheduleLineConfirmedQuantity
											+ Double.parseDouble(scheduleLine.getConfirmedQuantity());
									if (StringUtils.isNotEmpty(scheduleLine.getDeliveryDate()))
									{
										try
										{
											deliveryDate = new SimpleDateFormat(

													Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT))
															.parse(scheduleLine.getDeliveryDate());
											jnjDelSchModel.setDeliveryDate(deliveryDate);
											scheduleLineDeliveryDate.add(scheduleLine.getDeliveryDate());
										}
										catch (final ParseException exception)
										{
											JnjGTCoreUtil
													.logErrorMessage(
															"Mapping sales order response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
																	+ "Parsing Exception Occurred" + exception.getMessage(),
															exception, currentClass);
											throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
													MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
										}
									}
									jnjDelSchModelList.add(jnjDelSchModel);
								}
							}
							abstOrdEntMode.setDeliverySchedules(jnjDelSchModelList);

						}
					}
				}
				sessionService.setAttribute("outOrderLinesList", outOrderLinesList);

				JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
						"salesOrderTotalNetValue :: " + salesOrderTotalNetValue, currentClass);
				JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName, "taxValueForNetPrice :: " + taxValueForNetPrice,
						currentClass);

				final Double totalFees = Double.valueOf(minimumOrderFee + dropShipFee + handlingFee + totalExpeditedFee);

				JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
						"totalFees(minimumOrderFee + dropShipFee + handlingFee + totalExpeditedFee) :: " + totalFees, currentClass);

				grossPrice = BigDecimal.valueOf(salesOrderTotalNetValue)
						.add(BigDecimal.valueOf(taxValueForNetPrice).add(BigDecimal.valueOf(totalFees))).doubleValue();
				cartModel.setTotalPrice(subTotal);
				cartModel.setSubtotal(salesOrderTotalNetValue);
				cartModel.setTotalFees(totalFees);
				cartModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
				cartModel.setTotalExpeditedFees(Double.valueOf(totalExpeditedFee));
				cartModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
				cartModel.setTotalHandlingFee(Double.valueOf(handlingFee));
				cartModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
				cartModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
				cartModel.setTotalTax(Double.valueOf(taxValueForNetPrice));

				JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
						"cartModel.getTotalTax() :: " + cartModel.getTotalTax(), currentClass);

				cartModel.setTotalInsurance(Double.valueOf(totalInsurance));
				cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
				cartModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));
				cartModel.setTotalGrossPrice(Double.valueOf(grossPrice));
				cartModel.setSapValidated(Boolean.TRUE);

				if ((StringUtils.equalsIgnoreCase(outSalesOrderRejectionStatus,
						Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_B))
						|| StringUtils.equalsIgnoreCase(outSalesOrderRejectionStatus,
								Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_C)))
						|| (StringUtils.equalsIgnoreCase(outSalesOrderCreditStatus,
								Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_B))
								|| StringUtils.equalsIgnoreCase(outSalesOrderCreditStatus,
										Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_C))))
				{
					cartModel.setHoldCreditCardFlag(Boolean.TRUE);
				}
				else
				{
					cartModel.setHoldCreditCardFlag(Boolean.FALSE);
				}
			}
		}
		JnjGTCoreUtil.logDebugMessage("Mapping simulation response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		return cartModel;
	}


	@Override
	protected List<DiscountValue> createDiscountValues(final Double discountValue, final CurrencyModel currencyModel)
	{
		final DiscountValue discount = new DiscountValue(Order.DISCOUNT_VALUE, 0.0D, false, discountValue.doubleValue(),
				(currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<>();
		discountValues.add(discount);
		return discountValues;
	}

	private void setATPProperties(final CartModel cart, final OutOrderLines3 outOrderLine)
	{
		for (final AbstractOrderEntryModel abstOrderEntModel : cart.getEntries())
		{
			if (abstOrderEntModel.getMaterialEntered().equals(outOrderLine.getMaterialEntered()))
			{
				abstOrderEntModel.setItemCategory(outOrderLine.getItemCategory());
				abstOrderEntModel.setShowDescLbl(Boolean.FALSE);
				abstOrderEntModel.setShowInfoLbl(Boolean.FALSE);
			}
		}
	}

	@Override
	protected Collection<TaxValue> createTaxValues(final Double taxValue, final CurrencyModel currencyModel)
	{
		final TaxValue tax = new TaxValue(Order.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(currencyModel == null) ? null : currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<>();
		taxValues.add(tax);
		return taxValues;
	}

	private InOrderLines setOrderLineForCreation(final AbstractOrderEntryModel abstOrderEntryModel)
	{
		final String methodName = "setOrderLineForCreation()";
		JnjGTCoreUtil.logDebugMessage("Order line creation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				Config.getParameter(Jnjb2bFacadesConstants.Order.REQUEST_DATE_FORMAT));
		final InOrderLines inOrderLines = new InOrderLines();
		// Mapping of the Mandatory Fields Start
		if (null != abstOrderEntryModel)
		{
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getMaterialEntered().toUpperCase());
			inOrderLines.setQuantityRequested(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
			}
			else
			{
				inOrderLines.setSalesUOM(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			}
			if (null != abstOrderEntryModel.getEntryNumber())
			{
				inOrderLines.setLineNumber(String.valueOf(Order.TEN * (abstOrderEntryModel.getEntryNumber().intValue() + Order.ONE)));
				//inOrderLines.setLineNumber(String.valueOf(10));
			}
			else
			{
				inOrderLines.setLineNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
				//inOrderLines.setLineNumber(String.valueOf(10));
			}
			if (null != abstOrderEntryModel.getPriceOverrideReason())
			{
				inOrderLines.setPriceOverrideReason(abstOrderEntryModel.getPriceOverrideReason());
			}
			else
			{
				inOrderLines.setPriceOverrideReason(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
			}

			// Mapping of the Optional Fields Start
			if (null != abstOrderEntryModel.getPriceOverride())
			{
				final JAXBElement<String> priceOverrideRate = objectFactory
						.createInOrderLinesPriceOverrideRate(abstOrderEntryModel.getPriceOverride());
				inOrderLines.setPriceOverrideRate(priceOverrideRate);
			}
			/*
			 * if (null != abstOrderEntryModel.getExpectedPrice()) { final JAXBElement<String> expectedPriceRate =
			 * objectFactory .createInOrderLinesExpectedPriceRate(abstOrderEntryModel.getExpectedPrice());
			 * inOrderLines.setExpectedPriceRate(expectedPriceRate); }
			 */

			if (null != abstOrderEntryModel.getExpectedDeliveryDate())
			{
				final JAXBElement<String> expectedDeliveryDate = objectFactory
						.createInOrderLinesExpectedDeliveryDate(simpleDateFormat.format(abstOrderEntryModel.getExpectedDeliveryDate()));
				inOrderLines.setExpectedDeliveryDate(expectedDeliveryDate);
			}

			if (abstOrderEntryModel.getIndirectCustomer() != null)
			{
				inOrderLines.setIndirectCustomerAcct(
						objectFactory.createInOrderLines3IndirectCustomerAcct(abstOrderEntryModel.getIndirectCustomer()));
			}

			if (abstOrderEntryModel.getIndirectPayer() != null)
			{
				inOrderLines.setIndirectPayerAcct(
						objectFactory.createInOrderLinesIndirectPayerAcct(abstOrderEntryModel.getIndirectPayer()));
			}

			final ProductModel product = abstOrderEntryModel.getProduct();
			if (product instanceof JnJLaProductModel)
			{
				final String sector = ((JnJLaProductModel) product).getSector();
				checkIfPharamProductInOrder(sector);
			}
		}
		else
		{
			inOrderLines.setMaterialNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		JnjGTCoreUtil.logDebugMessage("Order line creation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return inOrderLines;
	}


	private OrderModel mapSalesOrderCreationResponse(final SalesOrderCreationResponse salesOrderCreationResponse,
			final OrderModel orderModel, final Map<String, OutOrderLines> mapMaterialNumberWithOutOrderLine,
			final String outSalesOrderCreditStatus, final String outSalesOrderRejectionStatus) throws SystemException, ParseException
	{
		final String methodName = "mapSalesOrderCreationResponse()";
		double totalTax = 0;
		double totalFreightFees = 0;
		double totalExpeditedFee = 0;
		double handlingFee = 0;
		double dropShipFee = 0;
		double minimumOrderFee = 0;
		double totalDiscounts = 0;
		double grossPrice = 0;
		double salesOrderTotalNetValue = 0;
		double taxValueForNetPrice = 0;
		JnjObjectComparator jnjObjectComparator;
		Set<String> scheduleLineDeliveryDate;

		JnjGTCoreUtil.logDebugMessage("Order line creation response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		if (null != salesOrderCreationResponse && null != salesOrderCreationResponse.getSalesOrderOut() && null != orderModel
				&& null != orderModel.getEntries() && !orderModel.getEntries().isEmpty())
		{
			//Get the Currency Associated with Cart
			final CurrencyModel currencyModel = orderModel.getCurrency();
			//Getting the B2bUnit with Cart
			final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) orderModel.getUnit();
			if (null == jnJB2bUnitModel)
			{
				throw new SystemException("No B2bUnit associated with Order having order No [" + orderModel.getCode() + "]");
			}
			//Fetch the Country Associated with Cart in Order to get Individual Taxes.
			final CountryModel countryModel = jnJB2bUnitModel.getCountry();
			if (null == countryModel)
			{
				throw new SystemException("No Country associated with B2bUnit with ID [" + jnJB2bUnitModel.getUid()
						+ "] for Order having order No [" + orderModel.getCode() + "]");
			}
			CountryModel country = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
			String loggedInSite = null;
			if(country != null) {
				loggedInSite = country.getIsocode();
			}else {
				loggedInSite = jnJB2bUnitModel.getCountry().getIsocode();
			}

			// set the SAP order number.
			orderModel.setSapOrderNumber(salesOrderCreationResponse.getSalesOrderOut().getOutSalesOrderNumber());

			// Iterating the Order line object and setting the value in Abstract order entry model.
			final Map<String, JnjOutOrderLine> freeGoodsMap = sessionService.getAttribute(Jnjlab2bcoreConstants.FREE_GOODS_MAP);
			for (final AbstractOrderEntryModel abstOrderEntryModel : orderModel.getEntries())
			{
				populateFreeItemDataInOrderEntry(freeGoodsMap, abstOrderEntryModel);

				double materialQuantity = 0.0;
				double scheduleLineConfirmedQuantity = 0.0;
				if (null != mapMaterialNumberWithOutOrderLine && !mapMaterialNumberWithOutOrderLine.isEmpty()
						&& mapMaterialNumberWithOutOrderLine.containsKey(abstOrderEntryModel.getMaterialEntered()))
				{
					// Retrieve the Out Order Line object from the map
					final OutOrderLines outOrderLines = mapMaterialNumberWithOutOrderLine
							.get(abstOrderEntryModel.getMaterialEntered());
					final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<>();

					if (!StringUtils.equals("ZFNN", outOrderLines.getItemCategory())
							&& !StringUtils.equals("ZFNT", outOrderLines.getItemCategory())
							&& !StringUtils.equals("ZFRY", outOrderLines.getItemCategory()))
					{

						final List<String> taxesLocalCountriesList = JnjLaCoreUtil
								.getCountriesList(Jnjlab2bcoreConstants.KEY_TAXES_LOCAL_COUNTRIES);
						final List<String> taxesCountriesList = JnjLaCoreUtil
								.getCountriesList(Jnjlab2bcoreConstants.KEY_TAXES_COUNTRIES);

						if (taxesCountriesList.contains(loggedInSite))
						{
							if (StringUtils.isNotEmpty(outOrderLines.getTaxes()))
							{
								abstOrderEntryModel
										.setTaxValues(createTaxValues((Double.valueOf(outOrderLines.getTaxes())), currencyModel));
								totalTax = totalTax + Double.parseDouble(outOrderLines.getTaxes());
								taxValueForNetPrice = totalTax;
							}
						}
						else if (taxesLocalCountriesList.contains(loggedInSite))
						{
							if (StringUtils.isNotEmpty(outOrderLines.getTaxesLocal())
									&& StringUtils.isNotEmpty(outOrderLines.getTaxes()))
							{
								{
									abstOrderEntryModel
											.setTaxValues(createTaxValues((Double.valueOf(outOrderLines.getTaxesLocal())), currencyModel));
									totalTax = totalTax + Double.parseDouble(outOrderLines.getTaxesLocal());
									taxValueForNetPrice = taxValueForNetPrice + Double.parseDouble(outOrderLines.getTaxes());
								}
							}
						}

						if (StringUtils.isNotEmpty(outOrderLines.getFreightFees()))
						{
							abstOrderEntryModel.setFreightFees(Double.valueOf(outOrderLines.getFreightFees()));
							totalFreightFees = totalFreightFees + abstOrderEntryModel.getFreightFees().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getExpeditedFees()))
						{
							abstOrderEntryModel.setExpeditedFee(Double.valueOf(outOrderLines.getExpeditedFees()));
							totalExpeditedFee = totalExpeditedFee + abstOrderEntryModel.getExpeditedFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getDiscounts()))
						{
							abstOrderEntryModel
									.setDiscountValues(createDiscountValues(Double.valueOf(outOrderLines.getDiscounts()), currencyModel));
							totalDiscounts = totalDiscounts + Double.parseDouble(outOrderLines.getDiscounts());
						}
						if (StringUtils.isNotEmpty(outOrderLines.getGrossPrice()))
						{
							abstOrderEntryModel.setGrossPrice(Double.valueOf(outOrderLines.getGrossPrice()));
							grossPrice = grossPrice + abstOrderEntryModel.getGrossPrice().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getInsurance()))
						{
							abstOrderEntryModel.setInsurance(Double.valueOf(outOrderLines.getInsurance()));
						}
						if (StringUtils.isNotEmpty(outOrderLines.getHandlingFee()))
						{
							abstOrderEntryModel.setHandlingFee(Double.valueOf(outOrderLines.getHandlingFee()));
							handlingFee = handlingFee + abstOrderEntryModel.getHandlingFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getDropshipFee()))
						{
							abstOrderEntryModel.setDropshipFee(Double.valueOf(outOrderLines.getDropshipFee()));
							dropShipFee = dropShipFee + abstOrderEntryModel.getDropshipFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getMinimumOrderFee()))
						{
							abstOrderEntryModel.setMinimumOrderFee(Double.valueOf(outOrderLines.getMinimumOrderFee()));
							minimumOrderFee = minimumOrderFee + abstOrderEntryModel.getMinimumOrderFee().doubleValue();
						}
						if (StringUtils.isNotEmpty(outOrderLines.getMaterialQuantity()))
						{
							abstOrderEntryModel
									.setQuantity(JnjLaCoreUtil.getLongValueFromDecimalString(outOrderLines.getMaterialQuantity()));
							abstOrderEntryModel.setReasonForRejection(outOrderLines.getReasonForRejection());
							materialQuantity = Double.parseDouble(outOrderLines.getMaterialQuantity());
						}
						if (StringUtils.isNotEmpty(outOrderLines.getUnitValue()))
						{
							abstOrderEntryModel.setBasePrice(Double.valueOf(Double.parseDouble(outOrderLines.getUnitValue())));
						}
						if (StringUtils.isNotEmpty(outOrderLines.getNetValue()))
						{
							abstOrderEntryModel.setTotalPrice(Double.valueOf(outOrderLines.getNetValue()));
						}

						if (CollectionUtils.isNotEmpty(outOrderLines.getScheduledLines()))
						{
							// sorting the schedule line object on the basis of the line number in descending order.
							jnjObjectComparator = new JnjObjectComparator(ScheduledLines.class, "getLineNumber", false, true);
							Collections.sort(outOrderLines.getScheduledLines(), jnjObjectComparator);
							scheduleLineDeliveryDate = new HashSet<>();
							Date deliveryDate;
							// Iterating the schedule line object and setting its value in the JnJ Delivery Schedule Model.
							JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
									"outOrderLines.getScheduledLines() :: " + outOrderLines.getScheduledLines(), currentClass);
							for (final ScheduledLines scheduleLine : outOrderLines.getScheduledLines())
							{
								if (null != scheduleLine && StringUtils.isNotEmpty(scheduleLine.getConfirmedQuantity())
										&& Float.compare(Jnjlab2bcoreConstants.ZERO_IN_FLOAT,
												Float.parseFloat(scheduleLine.getConfirmedQuantity())) != 0
										&& !scheduleLineDeliveryDate.contains(scheduleLine.getDeliveryDate())
										&& Double.compare(materialQuantity, scheduleLineConfirmedQuantity) != 0)
								{
									final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
									jnjDelSchModel.setLineNumber(scheduleLine.getLineNumber());
									jnjDelSchModel.setOwnerEntry(abstOrderEntryModel);
									jnjDelSchModel.setQty(Long.valueOf(Double.valueOf(scheduleLine.getConfirmedQuantity()).longValue()));
									// set the value in the double variable so that we can compare the requested quantity with schedule confirmed quantity
									scheduleLineConfirmedQuantity = scheduleLineConfirmedQuantity
											+ Double.parseDouble(scheduleLine.getConfirmedQuantity());
									if (StringUtils.isNotEmpty(scheduleLine.getDeliveryDate()))
									{
										try
										{
											deliveryDate = new SimpleDateFormat(
													Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT))
															.parse(scheduleLine.getDeliveryDate());
											jnjDelSchModel.setDeliveryDate(deliveryDate);
											scheduleLineDeliveryDate.add(scheduleLine.getDeliveryDate());
										}
										catch (final ParseException exception)
										{
											JnjGTCoreUtil
													.logErrorMessage(
															"Order line creation response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
																	+ "Parsing Exception Occurred " + exception.getMessage(),
															exception, currentClass);

											throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
													MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
										}
									}
									jnjDelSchModelList.add(jnjDelSchModel);
								}
							}
							abstOrderEntryModel.setDeliverySchedules(jnjDelSchModelList);
						}
					}
				}
			}

			final String outSalesOrdertTotalNetValue = salesOrderCreationResponse.getSalesOrderOut()
					.getOutSalesOrdertTotalNetValue();
			if (StringUtils.isNotEmpty(outSalesOrdertTotalNetValue))
			{
				salesOrderTotalNetValue = Double.parseDouble(outSalesOrdertTotalNetValue);

			}
			grossPrice = salesOrderTotalNetValue + taxValueForNetPrice;
			orderModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
			orderModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
			orderModel.setTotalHandlingFee(Double.valueOf(handlingFee));
			orderModel.setTotalExpeditedFees(Double.valueOf(totalExpeditedFee));
			orderModel.setTotalFreightFees(Double.valueOf(totalFreightFees));

			JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
					"mapSalesOrderCreationResponse() totalTax :: " + taxValueForNetPrice, currentClass);

			orderModel.setTotalTax(Double.valueOf(taxValueForNetPrice));

			JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
					"mapSalesOrderCreationResponse() totalTaxValues :: " + createTaxValues(Double.valueOf(totalTax), currencyModel),
					currentClass);
			JnjGTCoreUtil.logInfoMessage("Mapping order values", methodName,
					"mapSalesOrderCreationResponse() taxValueForNetPrice :: " + taxValueForNetPrice, currentClass);
			orderModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
			orderModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
			orderModel.setGlobalDiscountValues(createDiscountValues(Double.valueOf(totalDiscounts), currencyModel));
			orderModel.setTotalGrossPrice(Double.valueOf(grossPrice));

			if (((outSalesOrderRejectionStatus
					.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_B)))
					|| outSalesOrderRejectionStatus
							.equalsIgnoreCase(Config.getParameter(Jnjb2bCoreConstants.OUTSALESORDERREJECTIONSTATUS_C)))
					|| ((outSalesOrderCreditStatus
							.equalsIgnoreCase(Config.getParameter(Jnjlab2bcoreConstants.OUTSALESORDERCREDITSTATUS_C)))
							|| (outSalesOrderCreditStatus
									.equalsIgnoreCase(Config.getParameter(Jnjlab2bcoreConstants.OUTSALESORDERCREDITSTATUS_B)))))
			{
				orderModel.setHoldCreditCardFlag(Boolean.TRUE);
			}
			else
			{
				orderModel.setHoldCreditCardFlag(Boolean.FALSE);
			}
		}

		JnjGTCoreUtil.logDebugMessage("Order line creation response", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return orderModel;
	}

	private void populateFreeItemDataInOrderEntry(final Map<String, JnjOutOrderLine> freeGoodsMap,
			final AbstractOrderEntryModel abstOrderEntryModel)
	{
		if (freeGoodsMap != null && freeGoodsMap.containsKey(abstOrderEntryModel.getMaterialEntered()))
		{
			final String freeItemTrigger = abstOrderEntryModel.getMaterialEntered();
			abstOrderEntryModel.setFreeItem(freeGoodsMap.get(freeItemTrigger).getMaterialEntered());
			abstOrderEntryModel.setFreeItemsQuanity(freeGoodsMap.get(freeItemTrigger).getMaterialQuantity());
			abstOrderEntryModel.setFreeItemsAvailabilityStatus(freeGoodsMap.get(freeItemTrigger).getAvailabilityStatus());
			abstOrderEntryModel.setFreeItemUnit(freeGoodsMap.get(freeItemTrigger).getSalesUOM());
			abstOrderEntryModel.setFreeItemsLogicOriginalQuantity(abstOrderEntryModel.getQuantity().toString());

			final List<JnjDeliveryScheduleModel> deliveryScheduleModelList = new ArrayList<>();
			final JnjDeliveryScheduleModel deliveryScheduleModel = new JnjDeliveryScheduleModel();

			for (final JnjDeliveryScheduleData freeItemScheduleData : freeGoodsMap.get(freeItemTrigger).getScheduleLines())
			{
				deliveryScheduleModel.setDeliveryDate(freeItemScheduleData.getDeliveryDate());
				deliveryScheduleModel.setLineNumber(freeItemScheduleData.getLineNumber());
				deliveryScheduleModel.setQty(freeItemScheduleData.getQuantity());
				deliveryScheduleModel.setRoundedQuantity(freeItemScheduleData.getRoundedQuantity());
				deliveryScheduleModelList.add(deliveryScheduleModel);
			}
			abstOrderEntryModel.setFreeGoodScheduleLines(deliveryScheduleModelList);
		}
	}


	private InOrderLines2 setOrderLineForPricing(final AbstractOrderEntryModel abstOrderEntryModel)
	{
		final String methodName = "setOrderLineForPricing()";
		JnjGTCoreUtil.logDebugMessage("Order line price", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				Config.getParameter(Jnjb2bFacadesConstants.Order.REQUEST_DATE_FORMAT));
		final InOrderLines2 inOrderLines = new InOrderLines2();

		// Mapping the Mandatory Fields Start
		if (null != abstOrderEntryModel.getProduct())
		{
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getMaterialEntered().toUpperCase());
		}
		else
		{
			inOrderLines.setMaterialNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		inOrderLines.setQuantityRequested(String.valueOf(abstOrderEntryModel.getQuantity()));

		if (null != abstOrderEntryModel.getUnit())
		{
			inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
		}
		else
		{
			inOrderLines.setSalesUOM(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}
		if (null != abstOrderEntryModel.getEntryNumber())
		{
			inOrderLines.setLineNumber(String.valueOf(Order.TEN * (abstOrderEntryModel.getEntryNumber().intValue() + Order.ONE)));
		}
		else
		{
			inOrderLines.setLineNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		// Mapping the Optional Fields Start
		if (null != abstOrderEntryModel.getPriceOverride())
		{
			final JAXBElement<String> priceOverrideRate = objectFactory
					.createInOrderLines2PriceOverrideRate(abstOrderEntryModel.getPriceOverride());
			inOrderLines.setPriceOverrideRate(priceOverrideRate);
		}
		if (null != abstOrderEntryModel.getPriceOverrideReason())
		{
			final JAXBElement<String> priceOverrideReason = objectFactory
					.createInOrderLines2PriceOverrideReason(abstOrderEntryModel.getPriceOverrideReason());
			inOrderLines.setPriceOverrideReason(priceOverrideReason);
		}

		if (null != abstOrderEntryModel.getExpectedDeliveryDate())
		{
			final JAXBElement<String> expectedDeliveryDate = objectFactory
					.createInOrderLines2ExpectedDeliveryDate(simpleDateFormat.format(abstOrderEntryModel.getExpectedDeliveryDate()));
			inOrderLines.setExpectedDeliveryDate(expectedDeliveryDate);
		}
		if (abstOrderEntryModel.getIndirectCustomer() != null)
		{
			inOrderLines.setIndirectCustomerAcct(
					objectFactory.createInOrderLines3IndirectCustomerAcct(abstOrderEntryModel.getIndirectCustomer()));
		}

		JnjGTCoreUtil.logDebugMessage("Order line price", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.END_OF_METHOD
				+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
		return inOrderLines;
	}


	private InOrderLines3 setOrderLineForSimulation(final AbstractOrderEntryModel abstOrderEntryModel)
	{
		final String methodName = "setOrderLineForSimulation()";
		JnjGTCoreUtil.logDebugMessage("Order line simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				Config.getParameter(Jnjb2bFacadesConstants.Order.REQUEST_DATE_FORMAT));
		final InOrderLines3 inOrderLines = new InOrderLines3();

		if (abstOrderEntryModel == null)
		{
			return inOrderLines;
		}

		// Mapping of the Mandatory Fields Start
		if (abstOrderEntryModel.getMaterialEntered() != null)
		{
			JnjGTCoreUtil.logInfoMessage("Setting order line", methodName,
					"setOrderLineForSimulation() MaterialNumber :: " + abstOrderEntryModel.getMaterialEntered().toUpperCase(),
					currentClass);
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getMaterialEntered().toUpperCase());
		}
		else
		{
			inOrderLines.setMaterialNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		inOrderLines.setQuantityRequested(String.valueOf(abstOrderEntryModel.getQuantity()));

		if (null != abstOrderEntryModel.getUnit())
		{
			inOrderLines.setSalesUOM(abstOrderEntryModel.getUnit().getCode());
		}
		else
		{
			inOrderLines.setSalesUOM(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		if (null != abstOrderEntryModel.getEntryNumber())
		{
			inOrderLines.setLineNumber(String.valueOf(Order.TEN * (abstOrderEntryModel.getEntryNumber().intValue() + Order.ONE)));
		}
		else
		{
			inOrderLines.setLineNumber(Jnjb2bFacadesConstants.Order.EMPTY_STRING);
		}

		// Mapping of the Optional Fields Start
		if (null != abstOrderEntryModel.getPriceOverride())
		{
			final JAXBElement<String> priceOverrideRate = objectFactory
					.createInOrderLines3PriceOverrideRate(abstOrderEntryModel.getPriceOverride());
			inOrderLines.setPriceOverrideRate(priceOverrideRate);
		}

		if (null != abstOrderEntryModel.getPriceOverrideReason())
		{
			final JAXBElement<String> priceOverrideReason = objectFactory
					.createInOrderLines3PriceOverrideReason(abstOrderEntryModel.getPriceOverrideReason());
			inOrderLines.setPriceOverrideReason(priceOverrideReason);
		}

		if (null != abstOrderEntryModel.getExpectedDeliveryDate())
		{
			final JAXBElement<String> expectedDeliveryDate = objectFactory
					.createInOrderLines3ExpectedDeliveryDate(simpleDateFormat.format(abstOrderEntryModel.getExpectedDeliveryDate()));
			inOrderLines.setExpectedDeliveryDate(expectedDeliveryDate);
		}

		if (abstOrderEntryModel.getIndirectCustomer() != null)
		{
			inOrderLines.setIndirectCustomerAcct(
					objectFactory.createInOrderLines3IndirectCustomerAcct(abstOrderEntryModel.getIndirectCustomer()));
		}

		if (abstOrderEntryModel.getIndirectPayer() != null)
		{
			inOrderLines
					.setIndirectPayerAcct(objectFactory.createInOrderLinesIndirectPayerAcct(abstOrderEntryModel.getIndirectPayer()));
		}

		JnjGTCoreUtil.logDebugMessage("Order line simulation", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN
				+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		return inOrderLines;
	}


	@Override
	public JnjGTOutboundStatusData mapSalesOrderSimulationWrapper(final CartModel cartModel, final boolean isCallFromGetPrice)
			throws IntegrationException, SystemException, TimeoutException, BusinessException
	{
		final String methodName = "mapSalesOrderSimulationWrapper()";
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SIMULATE_ORDER, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamSalesOrderMapperImpl.class);

		final JnjGTSimulateOrderResponseData jnjNASimulateOrderResponseData = new JnjGTSimulateOrderResponseData();

		/* Code changes for Order Split Start */
		final List<JnjGTCartData> jnjCartDataList = new ArrayList<>();
		Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap;

		final CountryModel currentBaseStoreCountry = jnjGetCurrentDefaultB2BUnitUtil.getCurrentCountryForSite();
		final String baseStoreCountryIsoCode = JnjLaCommonUtil.getIdByCountry(currentBaseStoreCountry.getIsocode());
		
		JnjGTCoreUtil.logInfoMessage("Order simulation", methodName,
				"Current logged in Site::::::::::::::::" + baseStoreCountryIsoCode, currentClass);
		splitOrderMap = jnjLatamOrderSplitFacade.splitOrder(cartModel, baseStoreCountryIsoCode);
		sessionService.setAttribute("splitMap", splitOrderMap);
		
		JnjGTCoreUtil.logInfoMessage("Order simulation", methodName,"splitMap Zize#####: " + splitOrderMap.size(), currentClass);
		final Map<String, String> codesNotFound = orderUtil.validateSplitOrderMapLatam(splitOrderMap, cartModel);
		sessionService.setAttribute("codesNotFound", codesNotFound);
		if (codesNotFound != null && !codesNotFound.isEmpty())
		{
			LOGGER.info("Validation error Occurred. Please contact administrator::::::::::::::::");
			jnjNASimulateOrderResponseData.setErrorMessage("Validation error Occurred. Please contact administrator.");
			return jnjNASimulateOrderResponseData;
		}

		for (final Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry : splitOrderMap.entrySet())
		{
			addEntriesToCartSplitLatam(mapEntry, jnjCartDataList, cartModel.getCode());
		}
		if (!jnjCartDataList.isEmpty())
		{
			sessionService.setAttribute("jnjCartDataList", jnjCartDataList);
		}

		if (isCallFromGetPrice)
		{
			//Create Outorder Line List
			List<OutOrderLines3> outOrderLineList;
			outOrderLineList = jnjGTOutOrderLineMapper.createValidationOutOrderList(cartModel);

			final Map<String, OutOrderLines3> mapMaterialNumberWithOutOrderLine = new HashMap<>();

			for (final AbstractOrderEntryModel entry : cartModel.getEntries())
			{

				final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
				Date deliveryDate;
				// set the value in the double variable so that we can compare the requested quantity with schedule confirmed quantity
				final List<JnjDeliveryScheduleModel> scheduleLineDeliveryDate = new ArrayList<>();
				try
				{

					deliveryDate = new SimpleDateFormat(Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT))
							.parse("2016-12-12");
					jnjDelSchModel.setDeliveryDate(deliveryDate);

					scheduleLineDeliveryDate.add(jnjDelSchModel);
				}
				catch (final ParseException exception)
				{
					JnjGTCoreUtil.logErrorMessage("Mapping order", methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "Parsing Exception Occurred" + exception.getMessage(), exception,
							currentClass);

					throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
							MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
				}
				entry.setDeliverySchedules(scheduleLineDeliveryDate);
			}

			double salesOrderTotalNetValue = 0;

			final CartData cartData = cartFacade.getSessionCart();
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final double productValue = entry.getQuantity() * Double.valueOf(entry.getBasePrice().getValue().doubleValue());
				salesOrderTotalNetValue = salesOrderTotalNetValue + productValue;
			}

			for (final OutOrderLines3 outOrderLine : outOrderLineList)
			{
				mapMaterialNumberWithOutOrderLine.put(outOrderLine.getMaterialEntered(), outOrderLine);
			}

			if (!mapMaterialNumberWithOutOrderLine.isEmpty())
			{
				final CartModel cartModelResponse = mapSalesOrderSimulationResponse(mapMaterialNumberWithOutOrderLine, cartModel,
						salesOrderTotalNetValue);
				// To invoke the JnjOrderServiceImpl class to persist the simulate order data in hybris database.
				jnjCartService.calculateValidatedCart(cartModelResponse);
			}
		}

		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SIMULATE_ORDER, methodName, Logging.END_OF_METHOD,
				JnjLatamSalesOrderMapperImpl.class);

		jnjNASimulateOrderResponseData.setSavedSuccessfully(true);
		return jnjNASimulateOrderResponseData;

	}

	/**
	 *
	 * @param mapEntry
	 * @param jnjCartDataList
	 * @param cartCode
	 */
	protected void addEntriesToCartSplitLatam(final Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry,
			final List<JnjGTCartData> jnjCartDataList, final String cartCode)
	{
		final Collection<ProductOption> options = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);


		final CMSSiteModel cmsSiteModel = cmsSiteService.getCurrentSite();
		final List<CatalogModel> productCatalogs = cmsSiteModel.getProductCatalogs();
		JnjGTCoreUtil.logInfoMessage(Jnjlab2bcoreConstants.SIMULATE_ORDER, "addEntriesToCartSplitLatam()",
				"Fetching products from :: " + productCatalogs.get(0).getId() + " " + Jnjb2bCoreConstants.ONLINE,
				JnjLatamSalesOrderMapperImpl.class);

		final CatalogVersionModel catalogVersionModel = catalogService.getCatalogVersion(productCatalogs.get(0).getId(),
				Jnjb2bCoreConstants.ONLINE);

		//Entries for order
		final List<OrderEntryData> orderEntryDataList = new ArrayList<>();

		// Mapping of the Optional Fields End
		// Get the value from the map which contains the List of AbstractOrderEntryModel Object and iterate them one by one to populate the order entries in the request object.
		for (final AbstractOrderEntryModel abstOrderEntryModel : mapEntry.getValue())
		{
			final JnjLaOrderEntryData jnjOrderEntryData = new JnjLaOrderEntryData();
			JnJLaProductModel jnjLaProduct = (JnJLaProductModel) abstOrderEntryModel.getProduct();

			try
			{
				jnjLaProduct = (JnJLaProductModel) jnjLaProductService.getProductForCodeAndCatalogId(catalogVersionModel,
						jnjLaProduct.getCode(), jnjLaProduct.getCatalogId());

				final ProductData productData = jnjGTProductFacade.getProductForOptions(jnjLaProduct, options);
				jnjOrderEntryData.setProduct(productData);
				orderEntryDataList.add(jnjOrderEntryData);
			}
			catch (final BusinessException ex)
			{

				JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.SIMULATE_ORDER, "addEntriesToCartSplitLatam()", ex.getMessage(),
						ex, JnjLatamSalesOrderMapperImpl.class);
			}

		}
		//Initialize cart data with product entry for order split display
		final JnjLaCartData jnjCartData = new JnjLaCartData();
		jnjCartData.setCode(cartCode);
		final CartModel cartModel = getCart();
		if (null != cartModel.getUnit())
		{
			jnjCartData.setPartialDelivFlag(((JnJLaB2BUnitModel) cartModel.getUnit()).getPartialDelivFlag() == null ? Boolean.TRUE
					: ((JnJLaB2BUnitModel) cartModel.getUnit()).getPartialDelivFlag());
		}
		else
		{
			jnjCartData.setPartialDelivFlag(Boolean.TRUE);
		}

		jnjCartData
				.setHoldCreditCardFlag(cartModel.getHoldCreditCardFlag() == null ? Boolean.FALSE : cartModel.getHoldCreditCardFlag());
		jnjCartData.setEntries(orderEntryDataList);
		jnjCartData.setOrderType(mapEntry.getKey().getDocOrderType());
		jnjCartDataList.add(jnjCartData);

	}


	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}


	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected CartModel getCart()
	{
		return getCartService().getSessionCart();
	}

	private void checkIfPharamProductInOrder(final String sector)
	{
		boolean isAtLeastOnePharamaProduct;
		if (sector != null && Jnjlab2bcoreConstants.PHR_SECTOR.equalsIgnoreCase(sector))
		{
			isAtLeastOnePharamaProduct = true;
			sessionService.setAttribute(CHECK_PHARMA_PRODUCT, Boolean.valueOf(isAtLeastOnePharamaProduct));
		}
	}

	private void setForbiddenFlag(final SalesOrderIn3 salesOrderIn, final String b2bUnitCountry)
	{
		if (null != b2bUnitCountry && (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(b2bUnitCountry)
				|| Jnjlab2bcoreConstants.COUNTRY_ISO_MEXICO.equalsIgnoreCase(b2bUnitCountry)))
		{
			final Boolean isAtLeastOnePharamaProduct = (Boolean) sessionService.getAttribute(CHECK_PHARMA_PRODUCT);
			if (null != isAtLeastOnePharamaProduct && (Boolean.TRUE).equals(isAtLeastOnePharamaProduct))
			{
				salesOrderIn.setInForbiddenSales(objectFactory.createSalesOrderInInForbiddenSales(CHECKOUT_TRUE_FLAG));
			}
		}
	}

	private void setForbiddenFlag(final SalesOrderIn salesOrderIn, final String b2bUnitCountry)
	{
		if (null != b2bUnitCountry && (Jnjlab2bcoreConstants.COUNTRY_ISO_BRAZIL.equalsIgnoreCase(b2bUnitCountry)
				|| Jnjlab2bcoreConstants.COUNTRY_ISO_MEXICO.equalsIgnoreCase(b2bUnitCountry)))
		{
			final Boolean isAtLeastOnePharamaProduct = (Boolean) sessionService.getAttribute(CHECK_PHARMA_PRODUCT);
			if (null != isAtLeastOnePharamaProduct && (Boolean.TRUE).equals(isAtLeastOnePharamaProduct))
			{
				salesOrderIn.setInForbiddenSales(objectFactory.createSalesOrderInInForbiddenSales(CHECKOUT_TRUE_FLAG));
			}
		}
	}

	private void mapValuesToJnjOutOrderLine(final JnjOutOrderLine jnjOutOrderLine, final OutOrderLines3 outOrderLine)
			throws SystemException
	{
		final String methodName = "mapValuesToJnjOutOrderLine3()";
		JnjGTCoreUtil.logDebugMessage("Mapping values", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
				+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);

		jnjOutOrderLine.setLineNumber(outOrderLine.getLineNumber());
		jnjOutOrderLine.setMaterialNumber(outOrderLine.getMaterialNumber());
		final String materialQuantity = outOrderLine.getMaterialQuantity();
		jnjOutOrderLine.setMaterialQuantity(materialQuantity.substring(0, materialQuantity.indexOf(".")));
		jnjOutOrderLine.setSalesUOM(outOrderLine.getSalesUOM());
		jnjOutOrderLine.setItemCategory(outOrderLine.getItemCategory());
		jnjOutOrderLine.setMaterialEntered(outOrderLine.getMaterialEntered());
		jnjOutOrderLine.setHigherLevelItemNumber(outOrderLine.getHigherLevelItemNumber());
		jnjOutOrderLine.setFreightFees(outOrderLine.getFreightFees());
		jnjOutOrderLine.setTaxes(outOrderLine.getTaxes());

		jnjOutOrderLine.setUnitValue(outOrderLine.getUnitValue());
		jnjOutOrderLine.setGrossPrice(outOrderLine.getGrossPrice());
		jnjOutOrderLine.setNetValue(outOrderLine.getNetValue());
		jnjOutOrderLine.setDiscounts(outOrderLine.getDiscounts());
		jnjOutOrderLine.setExpeditedFees(outOrderLine.getExpeditedFees());

		jnjOutOrderLine.setInsurance(outOrderLine.getInsurance());
		jnjOutOrderLine.setInternaitonalFreight(outOrderLine.getInternaitonalFreight());
		jnjOutOrderLine.setHandlingFee(outOrderLine.getHandlingFee());
		jnjOutOrderLine.setDropshipFee(outOrderLine.getDropshipFee());
		jnjOutOrderLine.setMinimumOrderFee(outOrderLine.getMinimumOrderFee());
		jnjOutOrderLine.setTaxesLocal(outOrderLine.getTaxesLocal());
		jnjOutOrderLine.setQuantity(JnjLaCoreUtil.getLongValueFromDecimalString((outOrderLine.getMaterialQuantity())));

		long confirmedQuant = 0;
		final List<JnjDeliveryScheduleData> scheduledLinesList = new ArrayList<>();

		if (outOrderLine.getScheduledLines() != null && outOrderLine.getScheduledLines().size() > 0)
		{
			for (final ScheduledLines3 scheduledLine : outOrderLine.getScheduledLines())
			{
				final JnjDeliveryScheduleData jnjScheduledLine = new JnjDeliveryScheduleData();
				jnjScheduledLine.setLineNumber(scheduledLine.getLineNumber());
				final long qtty = Double.valueOf(scheduledLine.getConfirmedQuantity()).longValue();
				jnjScheduledLine.setQuantity(qtty);

				confirmedQuant = confirmedQuant + qtty;

				Date deliveryDate;
				try
				{
					deliveryDate = new SimpleDateFormat(Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT))
							.parse(scheduledLine.getDeliveryDate());
				}
				catch (final ParseException e)
				{
					JnjGTCoreUtil.logErrorMessage("Mapping order values", methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "Parsing Exception Occurred" + e.getMessage(), e, currentClass);
					throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
							MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
				}
				jnjScheduledLine.setDeliveryDate(deliveryDate);
				scheduledLinesList.add(jnjScheduledLine);
			}

			if (jnjOutOrderLine.getQuantity().longValue() == confirmedQuant && scheduledLinesList.size() == 1)
			{
				jnjOutOrderLine.setAvailabilityStatus(available);
			}
			else if (jnjOutOrderLine.getQuantity().longValue() == confirmedQuant)
			{
				jnjOutOrderLine.setAvailabilityStatus(multipleDateAvailable);
			}
			else if (confirmedQuant == 0)
			{
				jnjOutOrderLine.setAvailabilityStatus(notAvailable);
			}
			else
			{
				jnjOutOrderLine.setAvailabilityStatus(partialyAvailable);
			}
		}
		else
		{
			jnjOutOrderLine.setAvailabilityStatus(notAvailable);

		}
		jnjOutOrderLine.setScheduleLines(scheduledLinesList);
		jnjOutOrderLine.setConfirmedQty(confirmedQuant);

		JnjGTCoreUtil.logDebugMessage("Mapping values", methodName, Logging.SUBMIT_ORDER + Logging.HYPHEN + Logging.END_OF_METHOD
				+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(), currentClass);
	}


	private void mapValuesToJnjOutOrderLine(final JnjOutOrderLine jnjOutOrderLine, final OutOrderLines outOrderLine)
			throws SystemException
	{
		final String methodName = "mapValuesToJnjOutOrderLine()";

		JnjGTCoreUtil
				.logDebugMessage(
						Logging.SUBMIT_ORDER + Logging.HYPHEN, methodName, Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
								+ Logging.START_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(),
						currentClass);

		jnjOutOrderLine.setLineNumber(outOrderLine.getLineNumber());
		jnjOutOrderLine.setMaterialNumber(outOrderLine.getMaterialNumber());
		jnjOutOrderLine.setMaterialQuantity(outOrderLine.getMaterialQuantity());
		jnjOutOrderLine.setSalesUOM(outOrderLine.getSalesUOM());
		jnjOutOrderLine.setItemCategory(outOrderLine.getItemCategory());
		jnjOutOrderLine.setMaterialEntered(outOrderLine.getMaterialEntered());
		jnjOutOrderLine.setHigherLevelItemNumber(outOrderLine.getHigherLevelItemNumber());

		jnjOutOrderLine.setFreightFees(outOrderLine.getFreightFees());
		jnjOutOrderLine.setTaxes(outOrderLine.getTaxes());

		jnjOutOrderLine.setUnitValue(outOrderLine.getUnitValue());
		jnjOutOrderLine.setGrossPrice(outOrderLine.getGrossPrice());
		jnjOutOrderLine.setNetValue(outOrderLine.getNetValue());
		jnjOutOrderLine.setDiscounts(outOrderLine.getDiscounts());
		jnjOutOrderLine.setExpeditedFees(outOrderLine.getExpeditedFees());

		jnjOutOrderLine.setInsurance(outOrderLine.getInsurance());
		jnjOutOrderLine.setInternaitonalFreight(outOrderLine.getInternaitonalFreight());
		jnjOutOrderLine.setHandlingFee(outOrderLine.getHandlingFee());
		jnjOutOrderLine.setDropshipFee(outOrderLine.getDropshipFee());
		jnjOutOrderLine.setMinimumOrderFee(outOrderLine.getMinimumOrderFee());
		jnjOutOrderLine.setTaxesLocal(outOrderLine.getTaxesLocal());
		jnjOutOrderLine.setQuantity(JnjLaCoreUtil.getLongValueFromDecimalString((outOrderLine.getMaterialQuantity())));

		long confirmedQuantity = 0;
		final List<JnjDeliveryScheduleData> scheduledLinesList = new ArrayList<>();

		if (outOrderLine.getScheduledLines() != null && outOrderLine.getScheduledLines().size() > 0)
		{
			for (final ScheduledLines scheduledLine : outOrderLine.getScheduledLines())
			{
				final JnjDeliveryScheduleData jnjScheduledLine = new JnjDeliveryScheduleData();
				jnjScheduledLine.setLineNumber(scheduledLine.getLineNumber());
				final long qtty = Long.valueOf(Double.valueOf(scheduledLine.getConfirmedQuantity()).longValue());
				jnjScheduledLine.setQuantity(qtty);

				confirmedQuantity = confirmedQuantity + qtty;

				Date deliveryDate = null;
				try
				{
					deliveryDate = new SimpleDateFormat(Config.getParameter(Jnjb2bFacadesConstants.Order.RESPONSE_DATE_FORMAT))
							.parse(scheduledLine.getDeliveryDate());
				}
				catch (final ParseException e)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER + Logging.HYPHEN, methodName,
							Logging.SUBMIT_ORDER + Logging.HYPHEN + "Parsing Exception Occurred" + e.getMessage(), e, currentClass);

					throw new SystemException("System Exception throw from the JnjSalesOrderMapperImpl class",
							MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);

				}
				jnjScheduledLine.setDeliveryDate(deliveryDate);
				jnjScheduledLine.setRoundedQuantity(scheduledLine.getRoundedQuantity());

				scheduledLinesList.add(jnjScheduledLine);
			}

			if (jnjOutOrderLine.getQuantity().longValue() == confirmedQuantity && scheduledLinesList.size() == 1)
			{
				jnjOutOrderLine.setAvailabilityStatus(available);
			}
			else if (jnjOutOrderLine.getQuantity().longValue() == confirmedQuantity)
			{
				jnjOutOrderLine.setAvailabilityStatus(multipleDateAvailable);
			}
			else if (confirmedQuantity == 0)
			{
				jnjOutOrderLine.setAvailabilityStatus(notAvailable);
			}
			else
			{
				jnjOutOrderLine.setAvailabilityStatus(partialyAvailable);
			}
		}
		else
		{
			jnjOutOrderLine.setAvailabilityStatus(notAvailable);
		}
		jnjOutOrderLine.setScheduleLines(scheduledLinesList);
		jnjOutOrderLine.setConfirmedQty(confirmedQuantity);

		JnjGTCoreUtil
				.logDebugMessage(
						Logging.SUBMIT_ORDER + Logging.HYPHEN, methodName, Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
								+ Logging.START_TIME + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime(),
						currentClass);
	}

	private static boolean checkIncorrectShipTo(final JnJB2BUnitModel unit,final CartModel cartModel) {
		final List<String> jnjAddressIdList = new ArrayList<>();
		for (AddressModel address : unit.getShippingAddresses()) {
			jnjAddressIdList.add(address.getJnJAddressId());
		}
		
		String jnjBillingAddressId = null;
		String jnjShipToAddressId = null;
		
		if(cartModel.getPaymentAddress() != null){
			jnjBillingAddressId = cartModel.getPaymentAddress().getJnJAddressId();
		}
		
		if(cartModel.getDeliveryAddress() != null)
		{
			jnjShipToAddressId = cartModel.getDeliveryAddress().getJnJAddressId();
		}
		
		LOGGER.info("Selected b2b unit############: " + unit.getUid() + " Selected shipto id##################: " + jnjShipToAddressId + " Billing address###########: " + jnjBillingAddressId);
		return !jnjAddressIdList.contains(jnjShipToAddressId);
	}

}