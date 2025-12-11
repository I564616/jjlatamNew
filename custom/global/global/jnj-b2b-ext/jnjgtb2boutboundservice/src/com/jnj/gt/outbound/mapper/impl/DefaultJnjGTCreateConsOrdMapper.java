/**
 *
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InDataHeader;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InDataLineItem;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InRequest;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InRequestData;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.InWMControlArea;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.ObjectFactory;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.OutDataLineItem;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.OutResponse;
import com.jnj.itsusmpl00082.sg910_btbepic_in2903_ordercreate_hybris_source_v1_webservices.createorder_ws.OutScheduleLineItem;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTCreateConsOrdResponseData;
import com.jnj.core.data.ws.JnjGTSapWsData;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTCreateConsOrdMapper;
import com.jnj.gt.outbound.services.JnjGTCreateConsOrdService;
import com.jnj.core.util.JnjGTCoreUtil;



/**
 * The JnjGTCreateConsOrdMapperImpl class contains the definition of all the method of the JnjGTCreateConsOrdMapper
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTCreateConsOrdMapper implements JnjGTCreateConsOrdMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCreateConsOrdMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjGTCreateConsOrdService JnjGTCreateConsOrdService;

	@Autowired
	private JnjCartService jnjCartService;

	@Autowired
	private SessionService sessionService;

	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	@Autowired
	private ModelService modelService;

	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjGTCreateConsOrdService getJnjGTCreateConsOrdService() {
		return JnjGTCreateConsOrdService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 */
	@Override
	public JnjGTCreateConsOrdResponseData mapCreateConsOrdRequestResponse(final OrderModel orderModel,
			final JnjGTSapWsData sapWsData) throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapCreateConsOrdRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTCreateConsOrdResponseData jnjGTCreateConsOrdResponseData = new JnjGTCreateConsOrdResponseData();
		final InRequest inRequest = new InRequest();
		final InRequestData inRequestData = new InRequestData();
		final InDataHeader inDataHeader = new InDataHeader();
		final InWMControlArea inWmControlArea = new InWMControlArea();
		final Map<String, StringBuilder> removedProductCodes = new HashMap<String, StringBuilder>();
		// Call to set the control area header data in request.
		mapControlAreaDataInRequest(inWmControlArea, orderModel.getCode());
		inRequestData.setInWMControlArea(inWmControlArea);
		if (null != orderModel.getUnit())
		{
			final JAXBElement<String> soldToNumber = objectFactory.createInDataHeaderInSoldToNumber(orderModel.getUnit().getUid());
			inDataHeader.setInSoldToNumber(soldToNumber);

			final JnJB2BUnitModel JnJB2BUnitModel = (JnJB2BUnitModel) orderModel.getUnit();
			if (null != JnJB2BUnitModel.getSalesOrgCustomers() && JnJB2BUnitModel.getSalesOrgCustomers().iterator().hasNext())
			{
				inDataHeader.setInDivision(objectFactory.createInDataHeaderInDivision(JnJB2BUnitModel.getSalesOrgCustomers()
						.iterator().next().getDivision()));
				inDataHeader.setInDistributionChannel(objectFactory.createInDataHeaderInDistributionChannel(JnJB2BUnitModel
						.getSalesOrgCustomers().iterator().next().getDistributionChannel()));
			}
		}
		inDataHeader.setInCustomerPortalOrdernumber(objectFactory.createInDataHeaderInCustomerPortalOrdernumber(orderModel
				.getCode()));
		if (null != orderModel.getDeliveryAddress())
		{
			if (StringUtils.isNotEmpty(orderModel.getDeliveryAddress().getJnJAddressId()))
			{
				final JAXBElement<String> shipToNumber = objectFactory.createInDataHeaderInShipToNumber(orderModel
						.getDeliveryAddress().getJnJAddressId());
				inDataHeader.setInShipToNumber(shipToNumber);
			}
			else if (orderModel.isCustomShippingAddress())
			{
				final List<JnjConfigModel> oneTimeShippingValue = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.ONE_TIME_SHIPPING_VALUES);
				if (CollectionUtils.isNotEmpty(oneTimeShippingValue))
				{
					inDataHeader.setInShipToNumber(objectFactory.createInDataHeaderInShipToNumber(oneTimeShippingValue.get(0)
							.getValue()));
				}
			}

			final AddressModel AddressModel = (AddressModel) orderModel.getDeliveryAddress();
			if (null != AddressModel.getTown())
			{
				final JAXBElement<String> shipToCity = objectFactory.createInDataHeaderInShipToCity(AddressModel.getTown());
				inDataHeader.setInShipToCity(shipToCity);
			}
			else
			{
				inDataHeader.setInShipToCity(objectFactory
						.createInDataHeaderInShipToCity(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}
			if (null != AddressModel.getRegion() && StringUtils.isNotEmpty(AddressModel.getRegion().getIsocode()))
			{
				final JAXBElement<String> shipToState = objectFactory.createInDataHeaderInShipToState(AddressModel.getRegion()
						.getIsocode());
				inDataHeader.setInShipToState(shipToState);
			}
			else
			{
				inDataHeader.setInShipToState(objectFactory
						.createInDataHeaderInShipToState(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}
			if (null != AddressModel.getLine1())
			{
				final JAXBElement<String> shipToAddress1 = objectFactory.createInDataHeaderInShipToAddress1(AddressModel
						.getLine1());
				inDataHeader.setInShipToAddress1(shipToAddress1);
			}
			else
			{
				final JAXBElement<String> shipToAddress1 = objectFactory
						.createInDataHeaderInShipToAddress1(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToAddress1(shipToAddress1);
			}
			if (null != AddressModel.getLine2())
			{
				final JAXBElement<String> shipToAddress2 = objectFactory.createInDataHeaderInShipToAddress2(AddressModel
						.getLine2());
				inDataHeader.setInShipToAddress2(shipToAddress2);
			}
			else
			{
				final JAXBElement<String> shipToAddress2 = objectFactory
						.createInDataHeaderInShipToAddress2(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToAddress2(shipToAddress2);
			}
			if (null != AddressModel.getFirstname())
			{
				inDataHeader.setInShipToName(objectFactory.createInDataHeaderInShipToName(AddressModel.getFirstname()));
			}
			else
			{
				inDataHeader.setInShipToName(objectFactory
						.createInDataHeaderInShipToName(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}

			if (null != AddressModel.getPostalcode())
			{
				final JAXBElement<String> shipToPostalCd = objectFactory.createInDataHeaderInShipToPostalCd(AddressModel
						.getPostalcode());
				inDataHeader.setInShipToPostalCd(shipToPostalCd);
			}
			else
			{
				final JAXBElement<String> shipToPostalCd = objectFactory
						.createInDataHeaderInShipToPostalCd(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToPostalCd(shipToPostalCd);
			}
		}
		if (null != orderModel.getOrderType())
		{
			final JAXBElement<String> orderType = objectFactory.createInDataHeaderInOrderType(orderModel.getOrderType().getCode());
			inDataHeader.setInOrderType(orderType);
		}
		final List<JnjConfigModel> salesOrg = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.SALES_ORG);
		if (CollectionUtils.isNotEmpty(salesOrg))
		{
			inDataHeader.setInSalesOrganization(objectFactory.createInDataHeaderInSalesOrganization(salesOrg.get(0).getValue()));
		}

		if (null != orderModel.getNamedDeliveryDate())
		{
			final JAXBElement<String> requestedDeliveryDate = objectFactory
					.createInDataHeaderInRequestedDeliveryDate(new SimpleDateFormat(Config
							.getParameter(Jnjgtb2boutboundserviceConstants.CONSUMER_REQUEST_DATE_FORMAT)).format(orderModel
							.getNamedDeliveryDate()));
			inDataHeader.setInRequestedDeliveryDate(requestedDeliveryDate);
		}
		else
		{
			inDataHeader.setInRequestedDeliveryDate(objectFactory
					.createInDataHeaderInRequestedDeliveryDate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}
		if (null != orderModel.getPaymentAddress() && null != orderModel.getPaymentAddress().getJnJAddressId())
		{
			final JAXBElement<String> billToNumber = objectFactory.createInDataHeaderInBillToNumber(orderModel.getPaymentAddress()
					.getJnJAddressId());
			inDataHeader.setInBillToNumber(billToNumber);
		}

		final JAXBElement<String> dropShipIndicator = objectFactory
				.createInDataHeaderInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		inDataHeader.setInDropShipIndicator(dropShipIndicator);

		if (null != orderModel.getPurchaseOrderNumber())
		{
			final JAXBElement<String> poNumber = objectFactory.createInDataHeaderInPONumber(orderModel.getPurchaseOrderNumber());
			inDataHeader.setInPONumber(poNumber);
		}

		final JAXBElement<String> requestType = objectFactory
				.createInDataHeaderInRequestType(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		inDataHeader.setInRequestType(requestType);

		// Fetch value from the config table.
		final JAXBElement<String> orderChannel = objectFactory.createInDataHeaderInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.CreateConsOrd.ORDER_CHANNEL_FOR_CONS));
		inDataHeader.setInOrderChannel(orderChannel);
		if (null != orderModel.getReasonCode())
		{
			inDataHeader.setInOrderReason(objectFactory.createInDataHeaderInOrderReason(orderModel.getReasonCode()));
		}
		else
		{
			inDataHeader.setInOrderReason(objectFactory
					.createInDataHeaderInOrderReason(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}
		inDataHeader.setInOrderSource(objectFactory.createInDataHeaderInOrderSource(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInHoldCode(objectFactory.createInDataHeaderInHoldCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInPODate(objectFactory.createInDataHeaderInPODate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));

		final JAXBElement<String> customerNotes = objectFactory
				.createInDataHeaderInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		inDataHeader.setInCustomerNotes(customerNotes);
		inDataHeader.setInHouseAccount(objectFactory
				.createInDataHeaderInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInCompleteDelivery(objectFactory
				.createInDataHeaderInCompleteDelivery(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInForbiddenSales(objectFactory
				.createInDataHeaderInForbiddenSales(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInContractReferenceNumber(objectFactory
				.createInDataHeaderInContractReferenceNumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));

		if (null != orderModel.getUser())
		{
			// check for the non empty value if its empty or null then set empty value.
			final JAXBElement<String> contactName = objectFactory.createInDataHeaderInContactName(StringUtils.isNotEmpty(orderModel
					.getUser().getName()) ? orderModel.getUser().getName() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataHeader.setInContactName(contactName);
			// Changes For JJEPIC-350
			// E-Mail Address of the user placing the order on the website. User Profile.
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) orderModel.getUser();
			inDataHeader.setInContactEmail(objectFactory.createInDataHeaderInContactEmail(StringUtils
					.isNotEmpty(JnJB2bCustomerModel.getEmail()) ? JnJB2bCustomerModel.getEmail()
					: Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			// First telephone number of the user creating the order on the website.  User profile.
			if (CollectionUtils.isNotEmpty(JnJB2bCustomerModel.getAddresses()))
			{
				for (final AddressModel addressModel : JnJB2bCustomerModel.getAddresses())
				{
					inDataHeader.setInContactPhoneNumber(objectFactory.createInDataHeaderInContactPhoneNumber(StringUtils
							.isNotEmpty(addressModel.getPhone1()) ? addressModel.getPhone1()
							: Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
					break;
				}
			}
			inDataHeader.getInDataLineItem().addAll(mapOrderLinesFromAbstOrderEntries(orderModel.getEntries()));
			inRequestData.setInDataHeader(inDataHeader);
			inRequest.getInRequestData().add(inRequestData);
			final OutResponse outResponse = JnjGTCreateConsOrdService.createOrder(inRequest, sapWsData);


			if (null != outResponse
					&& null != outResponse.getOutResponseData()
					&& null != outResponse.getOutResponseData().get(0)
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
					&& CollectionUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
							.getOutDataLineItem())
					&& CollectionUtils.isNotEmpty(orderModel.getEntries())
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage()
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
							.getReturnMessageType()
					&& jnjConfigService.getConfigValueById(Jnjgtb2boutboundserviceConstants.SUCCESS).equals(
							outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
									.getReturnMessageType().getValue()))
			{
				final String sapOrderNum = outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
						.getOutSalesOrderNumber().getValue();
				if (StringUtils.isNotEmpty(sapOrderNum))
				{
					orderModel.setSapOrderNumber(sapOrderNum);
					orderModel.setOrderNumber(sapOrderNum);
				}

				mapOrderModelFromOutDataLineItem(orderModel, outResponse, removedProductCodes);
				//Calculate cart for total values and save in data base
				jnjGTCreateConsOrdResponseData.setSavedSuccessfully(jnjCartService.saveCartModel(orderModel, true));
				jnjGTCreateConsOrdResponseData.setSapResponseStatus(true);
				jnjGTCreateConsOrdResponseData.setRemovedProductCodes(removedProductCodes);
			}
			else if (null != outResponse
					&& null != outResponse.getOutResponseData()
					&& null != outResponse.getOutResponseData().get(0)
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
					&& CollectionUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
							.getOutDataLineItem())
					&& CollectionUtils.isNotEmpty(orderModel.getEntries())
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage()
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
							.getReturnMessageType()
					&& jnjConfigService.getConfigValuesById(Jnjgtb2boutboundserviceConstants.CONSUMER_ERROR,
							Jnjgtb2boutboundserviceConstants.COMMA_STRING).contains(
							outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
									.getReturnMessageType().getValue()))
			{
				jnjGTCreateConsOrdResponseData.setSapResponseStatus(false);
				jnjGTCreateConsOrdResponseData.setHardStopErrorOcurred(true);
				jnjGTCreateConsOrdResponseData.setErrorMessage(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
						.getOutSAPResponseMessage().get(0).getReturnMessage().getValue());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapCreateConsOrdRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTCreateConsOrdResponseData;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 */
	@Override
	public JnjGTCreateConsOrdResponseData mapSimulateConsOrdRequestResponse(final CartModel cartModel, final JnjGTSapWsData wsData)
			throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_CONS_ORD + Logging.HYPHEN + "mapSimulateConsOrdRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTCreateConsOrdResponseData jnjGTCreateConsOrdResponseData = new JnjGTCreateConsOrdResponseData();
		final InRequest inRequest = new InRequest();
		final InRequestData inRequestData = new InRequestData();
		final InDataHeader inDataHeader = new InDataHeader();
		final InWMControlArea inWmControlArea = new InWMControlArea();
		final Map<String, StringBuilder> removedProductCodes = new HashMap<String, StringBuilder>();
		// Call to set the control area header data in request.
		mapControlAreaDataInRequest(inWmControlArea, cartModel.getCode());
		inRequestData.setInWMControlArea(inWmControlArea);
		if (null != cartModel.getUnit())
		{
			final JAXBElement<String> soldToNumber = objectFactory.createInDataHeaderInSoldToNumber(cartModel.getUnit().getUid());
			inDataHeader.setInSoldToNumber(soldToNumber);
			final JnJB2BUnitModel JnJB2BUnitModel = (JnJB2BUnitModel) cartModel.getUnit();
			if (null != JnJB2BUnitModel.getSalesOrgCustomers() && JnJB2BUnitModel.getSalesOrgCustomers().iterator().hasNext())
			{
				inDataHeader.setInDivision(objectFactory.createInDataHeaderInDivision(JnJB2BUnitModel.getSalesOrgCustomers()
						.iterator().next().getDivision()));
				inDataHeader.setInDistributionChannel(objectFactory.createInDataHeaderInDistributionChannel(JnJB2BUnitModel
						.getSalesOrgCustomers().iterator().next().getDistributionChannel()));
			}
		}
		inDataHeader
				.setInCustomerPortalOrdernumber(objectFactory.createInDataHeaderInCustomerPortalOrdernumber(cartModel.getCode()));
		if (null != cartModel.getDeliveryAddress())
		{
			if (StringUtils.isNotEmpty(cartModel.getDeliveryAddress().getJnJAddressId()))
			{
				final JAXBElement<String> shipToNumber = objectFactory.createInDataHeaderInShipToNumber(cartModel
						.getDeliveryAddress().getJnJAddressId());
				inDataHeader.setInShipToNumber(shipToNumber);
			}
			else if (cartModel.isCustomShippingAddress())
			{
				final List<JnjConfigModel> oneTimeShippingValue = jnjConfigService.getConfigModelsByIdAndKey(
						Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.ONE_TIME_SHIPPING_VALUES);
				if (CollectionUtils.isNotEmpty(oneTimeShippingValue))
				{
					inDataHeader.setInShipToNumber(objectFactory.createInDataHeaderInShipToNumber(oneTimeShippingValue.get(0)
							.getValue()));
				}
			}

			final AddressModel AddressModel = (AddressModel) cartModel.getDeliveryAddress();
			if (null != AddressModel.getTown())
			{
				final JAXBElement<String> shipToCity = objectFactory.createInDataHeaderInShipToCity(AddressModel.getTown());
				inDataHeader.setInShipToCity(shipToCity);
			}
			else
			{
				inDataHeader.setInShipToCity(objectFactory
						.createInDataHeaderInShipToCity(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}
			if (null != AddressModel.getRegion() && StringUtils.isNotEmpty(AddressModel.getRegion().getIsocode()))
			{
				final JAXBElement<String> shipToState = objectFactory.createInDataHeaderInShipToState(AddressModel.getRegion()
						.getIsocode());
				inDataHeader.setInShipToState(shipToState);
			}
			else
			{
				inDataHeader.setInShipToState(objectFactory
						.createInDataHeaderInShipToState(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}
			if (null != AddressModel.getLine1())
			{
				final JAXBElement<String> shipToAddress1 = objectFactory.createInDataHeaderInShipToAddress1(AddressModel
						.getLine1());
				inDataHeader.setInShipToAddress1(shipToAddress1);
			}
			else
			{
				final JAXBElement<String> shipToAddress1 = objectFactory
						.createInDataHeaderInShipToAddress1(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToAddress1(shipToAddress1);
			}
			if (null != AddressModel.getLine2())
			{
				final JAXBElement<String> shipToAddress2 = objectFactory.createInDataHeaderInShipToAddress2(AddressModel
						.getLine2());
				inDataHeader.setInShipToAddress2(shipToAddress2);
			}
			else
			{
				final JAXBElement<String> shipToAddress2 = objectFactory
						.createInDataHeaderInShipToAddress2(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToAddress2(shipToAddress2);
			}
			if (null != AddressModel.getFirstname())
			{
				inDataHeader.setInShipToName(objectFactory.createInDataHeaderInShipToName(AddressModel.getFirstname()));
			}
			else
			{
				inDataHeader.setInShipToName(objectFactory
						.createInDataHeaderInShipToName(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			}
			if (null != AddressModel.getPostalcode())
			{
				final JAXBElement<String> shipToPostalCd = objectFactory.createInDataHeaderInShipToPostalCd(AddressModel
						.getPostalcode());
				inDataHeader.setInShipToPostalCd(shipToPostalCd);
			}
			else
			{
				final JAXBElement<String> shipToPostalCd = objectFactory
						.createInDataHeaderInShipToPostalCd(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inDataHeader.setInShipToPostalCd(shipToPostalCd);
			}
		}
		if (null != cartModel.getOrderType())
		{
			final JAXBElement<String> orderType = objectFactory.createInDataHeaderInOrderType(cartModel.getOrderType().getCode());
			inDataHeader.setInOrderType(orderType);
		}
		final List<JnjConfigModel> salesOrg = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.SALES_ORG);
		if (CollectionUtils.isNotEmpty(salesOrg))
		{
			inDataHeader.setInSalesOrganization(objectFactory.createInDataHeaderInSalesOrganization(salesOrg.get(0).getValue()));
		}

		if (null != cartModel.getPaymentAddress() && null != cartModel.getPaymentAddress().getJnJAddressId())
		{
			final JAXBElement<String> billToNumber = objectFactory.createInDataHeaderInBillToNumber(cartModel.getPaymentAddress()
					.getJnJAddressId());
			inDataHeader.setInBillToNumber(billToNumber);
		}
		final JAXBElement<String> dropShipIndicator = objectFactory
				.createInDataHeaderInDropShipIndicator(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);

		inDataHeader.setInDropShipIndicator(dropShipIndicator);

		if (null != cartModel.getPurchaseOrderNumber())
		{
			final JAXBElement<String> poNumber = objectFactory.createInDataHeaderInPONumber(cartModel.getPurchaseOrderNumber());
			inDataHeader.setInPONumber(poNumber);
		}

		final JAXBElement<String> requestType = objectFactory.createInDataHeaderInRequestType(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.CreateConsOrd.REQ_TYPE_FOR_SIM_CONS));
		inDataHeader.setInRequestType(requestType);

		// Fetch value from the config table.
		final JAXBElement<String> orderChannel = objectFactory.createInDataHeaderInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.CreateConsOrd.ORDER_CHANNEL_FOR_CONS));
		inDataHeader.setInOrderChannel(orderChannel);
		if (null != cartModel.getReasonCode())
		{
			inDataHeader.setInOrderReason(objectFactory.createInDataHeaderInOrderReason(cartModel.getReasonCode()));
		}
		else
		{
			inDataHeader.setInOrderReason(objectFactory
					.createInDataHeaderInOrderReason(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}

		inDataHeader.setInOrderSource(objectFactory.createInDataHeaderInOrderSource(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInHoldCode(objectFactory.createInDataHeaderInHoldCode(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInPODate(objectFactory.createInDataHeaderInPODate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		final JAXBElement<String> customerNotes = objectFactory
				.createInDataHeaderInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
		inDataHeader.setInCustomerNotes(customerNotes);
		if (null != cartModel.getNamedDeliveryDate())
		{
			final JAXBElement<String> requestedDeliveryDate = objectFactory
					.createInDataHeaderInRequestedDeliveryDate(new SimpleDateFormat(Config
							.getParameter(Jnjgtb2boutboundserviceConstants.CONSUMER_REQUEST_DATE_FORMAT)).format(cartModel
							.getNamedDeliveryDate()));
			inDataHeader.setInRequestedDeliveryDate(requestedDeliveryDate);
		}
		else
		{
			inDataHeader.setInRequestedDeliveryDate(objectFactory
					.createInDataHeaderInRequestedDeliveryDate(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}

		inDataHeader.setInHouseAccount(objectFactory
				.createInDataHeaderInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInCompleteDelivery(objectFactory
				.createInDataHeaderInCompleteDelivery(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInForbiddenSales(objectFactory
				.createInDataHeaderInForbiddenSales(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		inDataHeader.setInContractReferenceNumber(objectFactory
				.createInDataHeaderInContractReferenceNumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));

		if (null != cartModel.getUser())
		{
			// check for the non empty value if its empty or null then set empty value.
			final JAXBElement<String> contactName = objectFactory.createInDataHeaderInContactName(StringUtils.isNotEmpty(cartModel
					.getUser().getName()) ? cartModel.getUser().getName() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataHeader.setInContactName(contactName);
			// Changes For JJEPIC-350
			// E-Mail Address of the user placing the order on the website. User Profile.
			final JnJB2bCustomerModel JnJB2bCustomerModel = (JnJB2bCustomerModel) cartModel.getUser();
			inDataHeader.setInContactEmail(objectFactory.createInDataHeaderInContactEmail(StringUtils
					.isNotEmpty(JnJB2bCustomerModel.getEmail()) ? JnJB2bCustomerModel.getEmail()
					: Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
			// First telephone number of the user creating the order on the website.  User profile.
			if (CollectionUtils.isNotEmpty(JnJB2bCustomerModel.getAddresses()))
			{
				for (final AddressModel addressModel : JnJB2bCustomerModel.getAddresses())
				{
					inDataHeader.setInContactPhoneNumber(objectFactory.createInDataHeaderInContactPhoneNumber(StringUtils
							.isNotEmpty(addressModel.getPhone1()) ? addressModel.getPhone1()
							: Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
					break;
				}
			}
			inDataHeader.getInDataLineItem().addAll(mapOrderLinesFromAbstOrderEntries(cartModel.getEntries()));
			inRequestData.setInDataHeader(inDataHeader);
			inRequest.getInRequestData().add(inRequestData);
			final OutResponse outResponse = JnjGTCreateConsOrdService.createOrder(inRequest, wsData);

			if (null != outResponse
					&& null != outResponse.getOutResponseData()
					&& null != outResponse.getOutResponseData().get(0)
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
					&& CollectionUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
							.getOutDataLineItem())
					&& CollectionUtils.isNotEmpty(cartModel.getEntries())
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage()
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
							.getReturnMessageType()
					&& jnjConfigService.getConfigValueById(Jnjgtb2boutboundserviceConstants.SUCCESS).equals(
							outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
									.getReturnMessageType().getValue()))
			{
				mapCartModelFromOutDataLineItem(cartModel, outResponse, removedProductCodes);
				//Calculate cart for total values and save in data base
				jnjGTCreateConsOrdResponseData.setSavedSuccessfully(jnjCartService.calculateValidatedCart(cartModel));
				jnjGTCreateConsOrdResponseData.setSapResponseStatus(true);
				jnjGTCreateConsOrdResponseData.setRemovedProductCodes(removedProductCodes);
			}
			else if (null != outResponse
					&& null != outResponse.getOutResponseData()
					&& null != outResponse.getOutResponseData().get(0)
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
					&& CollectionUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
							.getOutDataLineItem())
					&& CollectionUtils.isNotEmpty(cartModel.getEntries())
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage()
					&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
							.getReturnMessageType()
					&& jnjConfigService.getConfigValuesById(Jnjgtb2boutboundserviceConstants.CONSUMER_ERROR,
							Jnjgtb2boutboundserviceConstants.COMMA_STRING).contains(
							outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSAPResponseMessage().get(0)
									.getReturnMessageType().getValue()))
			{
				jnjGTCreateConsOrdResponseData.setSapResponseStatus(false);
				jnjGTCreateConsOrdResponseData.setHardStopErrorOcurred(true);
				jnjGTCreateConsOrdResponseData.setErrorMessage(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
						.getOutSAPResponseMessage().get(0).getReturnMessage().getValue());
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_CONS_ORD + Logging.HYPHEN + "mapSimulateConsOrdRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTCreateConsOrdResponseData;
	}

	/**
	 * Map order lines from abst order entries.
	 * 
	 * @param abstOrdEntModelList
	 *           the abst ord ent model list
	 * @return the list
	 */
	public List<InDataLineItem> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<InDataLineItem> arrayOfinDataLineItem = new ArrayList<InDataLineItem>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InDataLineItem inDataLineItem = new InDataLineItem();
			final JAXBElement<String> materialNumber = objectFactory.createInDataLineItemInMaterialNumber(abstOrderEntryModel
					.getProduct().getCode());
			inDataLineItem.setInMaterialNumber(materialNumber);
			inDataLineItem.setInLineNumber(objectFactory.createInDataLineItemInLineNumber(String.valueOf((abstOrderEntryModel
					.getEntryNumber().intValue() + 1) * 10)));
			final JAXBElement<String> quantityRequested = objectFactory.createInDataLineItemInQuantityRequested(String
					.valueOf(abstOrderEntryModel.getQuantity().doubleValue()));
			inDataLineItem.setInQuantityRequested(quantityRequested);
			final JAXBElement<String> salesUOM = objectFactory
					.createInDataLineItemInSalesUOM(null != abstOrderEntryModel.getUnit() ? abstOrderEntryModel.getUnit().getCode()
							: Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInSalesUOM(salesUOM);
			final JAXBElement<String> priceOverrideRate = objectFactory
					.createInDataLineItemInPriceOverrideRate(null != abstOrderEntryModel.getPriceOverride() ? abstOrderEntryModel
							.getPriceOverride() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInPriceOverrideRate(priceOverrideRate);
			final JAXBElement<String> priceOverrideReason = objectFactory
					.createInDataLineItemInPriceOverrideReason(null != abstOrderEntryModel.getPriceOverrideReason() ? abstOrderEntryModel
							.getPriceOverrideReason() : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInPriceOverrideReason(priceOverrideReason);
			final JAXBElement<String> expectedDeliveryDate = objectFactory
					.createInDataLineItemInExpectedDeliveryDate(null != abstOrderEntryModel.getExpectedDeliveryDate() ? new SimpleDateFormat(
							Config.getParameter(Jnjgtb2boutboundserviceConstants.REQUEST_DATE_FORMAT)).format(abstOrderEntryModel
							.getExpectedDeliveryDate()) : Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInExpectedDeliveryDate(expectedDeliveryDate);

			final JAXBElement<String> lot = objectFactory.createInDataLineItemInLot(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInLot(lot);
			final JAXBElement<String> route = objectFactory
					.createInDataLineItemInRoute(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInRoute(route);
			final JAXBElement<String> shippingPoint = objectFactory
					.createInDataLineItemInShippingPoint(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInShippingPoint(shippingPoint);
			final JAXBElement<String> plant = objectFactory
					.createInDataLineItemInPlant(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inDataLineItem.setInPlant(plant);
			// add the object in the list
			arrayOfinDataLineItem.add(inDataLineItem);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return arrayOfinDataLineItem;
	}

	/**
	 * Map order model from out order line.
	 * 
	 * @param cartModel
	 *           the order model
	 * @throws SystemException
	 *            the system exception
	 */
	public void mapCartModelFromOutDataLineItem(final CartModel cartModel, final OutResponse outResponse,
			final Map<String, StringBuilder> removedProductCodes) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_CONS_ORD + Logging.HYPHEN + "mapCartModelFromOutDataLineItem()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalTax = 0.0;
		double dropShipFee = 0.0;
		double totalFreightFees = 0.0;
		double minimumOrderFee = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		final CurrencyModel currencyModel = cartModel.getCurrency();
		CatalogVersionModel catalogVersionModel = null;
		Date expectedDelDate = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : cartModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}

		if (null != outResponse && CollectionUtils.isNotEmpty(outResponse.getOutResponseData())
				&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
				&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue())
		{
			final DecimalFormat decimalFormat = new DecimalFormat("#.##");
			final List<String> itemCategories = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<String> itemCategoriesForTan = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<String> highLevelItemValues = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_ORDER_LINE_HIGH_LEVEL_VALUE, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final String productLevelErrorCode = JnJCommonUtil
					.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_LEVEL_ERROR_CODE);
			final Map<String, String> mapContainsProductErrorKeyAndMessage = new HashMap<String, String>();
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_EFFECTIVITY_ERROR_CODE),
					"cart.consumer.product.effectivity.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_EXCLUSION_BLOCK_ERROR_CODE),
					"cart.consumer.product.exclusion.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_FIRST_DEA_BLOCK_ERROR_CODE),
					"cart.consumer.product.dea.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_SECOND_DEA_BLOCK_ERROR_CODE),
					"cart.consumer.product.dea.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_MULTIPLE_BLOCK_ERROR_CODE),
					"cart.consumer.product.multiple.block.error.code");
			final List<String> itemCategoryForTapa = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_ORDER_TAPA_LINE_ITEM_CATEGORY, Jnjb2bCoreConstants.SYMBOl_COMMA);

			// To fetch active Catalog Version Model.
			if (null != cartModel.getSite() && CollectionUtils.isNotEmpty(cartModel.getSite().getStores()))
			{
				if (CollectionUtils.isNotEmpty(cartModel.getSite().getStores().get(0).getCatalogs()))
				{
					catalogVersionModel = cartModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
				}
			}
			// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
			for (final OutDataLineItem outDataLineItem : outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
					.getOutDataLineItem())
			{
				try
				{
					if (null != outDataLineItem
							&& null != outDataLineItem.getOutItemCategory()
							&& (itemCategories.contains(outDataLineItem.getOutItemCategory().getValue()) || (itemCategoriesForTan
									.contains(outDataLineItem.getOutItemCategory().getValue()) && (null == outDataLineItem
									.getOutHigherLevelItemNumber() || highLevelItemValues.contains(outDataLineItem
									.getOutHigherLevelItemNumber().getValue())))) && null != outDataLineItem.getOutMaterialNumber())
					{
						double totalQuantity = 0.0;
						double totalNetValue = 0.0;
						boolean isOrderEntryDeleted = false;
						AbstractOrderEntryModel abstOrdEntryModel = null;
						final JnJProductModel product = jnJGTProductService.getProductModelByCode(outDataLineItem
								.getOutMaterialNumber().getValue(), catalogVersionModel);
						if (null != product)
						{
							final ProductModel baseProduct = product.getMaterialBaseProduct() == null ? product : product
									.getMaterialBaseProduct();

							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProduct.getCode()))
							{
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProduct.getCode());

							// check for the null value and set it in Sap Order Line Number.
							if (null != outDataLineItem.getOutLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(outDataLineItem.getOutLineNumber().getValue());
							}

							if (null != outDataLineItem.getOutMaterialQuantity()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMaterialQuantity().getValue()))
							{
								abstOrdEntryModel.setQuantity(JnjGTCoreUtil.convertStringToLong(outDataLineItem.getOutMaterialQuantity()
										.getValue()));
							}

							if (null != outDataLineItem.getOutReasonForRejection())
							{
								abstOrdEntryModel.setReasonForRejection(outDataLineItem.getOutReasonForRejection().getValue());
							}
							if (null != outDataLineItem.getOutExpeditedFees()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutExpeditedFees().getValue()))
							{
								abstOrdEntryModel.setExpeditedFee(Double.valueOf(outDataLineItem.getOutExpeditedFees().getValue()));
							}
							if (null != outDataLineItem.getOutInsurance()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutInsurance().getValue()))
							{
								abstOrdEntryModel.setInsurance(Double.valueOf(outDataLineItem.getOutInsurance().getValue()));
							}
							if (null != outDataLineItem.getOutInternaitonalFreight()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutInternaitonalFreight().getValue()))
							{
								abstOrdEntryModel.setInternationalFreight(Double.valueOf(outDataLineItem.getOutInternaitonalFreight()
										.getValue()));
							}
							if (null != outDataLineItem.getOutHandlingFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutHandlingFee().getValue()))
							{
								abstOrdEntryModel.setHandlingFee(Double.valueOf(outDataLineItem.getOutHandlingFee().getValue()));
							}
							if (null != outDataLineItem.getOutMinimumOrderFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMinimumOrderFee().getValue()))
							{
								abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(outDataLineItem.getOutMinimumOrderFee().getValue()));
								minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
							}
							if (null != outDataLineItem.getOutBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outDataLineItem.getOutBaseUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
							}
							if (null != outDataLineItem.getOutSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService
										.getUnitByCode(outDataLineItem.getOutSalesUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
								if (CollectionUtils.isNotEmpty(product.getVariants()))
								{
									// Get the Variant Product Models of the base product.
									final List<VariantProductModel> variantProductModels = (List) product.getVariants();
									// Iterate them one by one.
									for (final VariantProductModel variantProductModel : variantProductModels)
									{
										// Check it for not null value for the models and equate the unit model code of the Variant model with the incoming response sales uom.
										if (null != variantProductModel
												&& null != variantProductModel.getUnit()
												&& variantProductModel.getUnit().getCode()
														.equalsIgnoreCase(outDataLineItem.getOutSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}

							abstOrdEntryModel.setItemCategory(outDataLineItem.getOutItemCategory().getValue());
							if (null != outDataLineItem.getOutHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(outDataLineItem.getOutHigherLevelItemNumber().getValue());
							}
							if (null != outDataLineItem.getOutGrossPriceBR()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutGrossPriceBR().getValue()))
							{
								abstOrdEntryModel.setGrossPrice(Double.valueOf(outDataLineItem.getOutGrossPriceBR().getValue()));
							}
							if (null != outDataLineItem.getOutNetValue()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutNetValue().getValue())
									&& null != outDataLineItem.getOutMaterialQuantity()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMaterialQuantity().getValue()))
							{
								abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double.parseDouble(outDataLineItem
										.getOutNetValue().getValue()))
										/ (Double.parseDouble(outDataLineItem.getOutMaterialQuantity().getValue())))));
								abstOrdEntryModel.setTotalPrice(Double.valueOf(decimalFormat.format(Double.parseDouble(outDataLineItem
										.getOutNetValue().getValue()))));
							}
							if (null != outDataLineItem.getOutDiscounts()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutDiscounts().getValue()))
							{
								abstOrdEntryModel.setDiscountValues(createDiscountValues(
										(Double.valueOf(outDataLineItem.getOutDiscounts().getValue())), currencyModel));
								totalDiscounts = totalDiscounts
										+ Double.valueOf(outDataLineItem.getOutDiscounts().getValue()).doubleValue();
							}
							if (null != outDataLineItem.getOutTaxes()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutTaxes().getValue()))
							{
								abstOrdEntryModel.setTaxValues(createTaxValues(
										(Double.valueOf(outDataLineItem.getOutTaxes().getValue())), currencyModel));

								totalTax = totalTax + Double.valueOf(outDataLineItem.getOutTaxes().getValue()).doubleValue();
							}
							if (null != outDataLineItem.getOutRoute())
							{
								abstOrdEntryModel.setRoute(outDataLineItem.getOutRoute().getValue());
							}
							if (null != outDataLineItem.getOutShippingPoint())
							{
								abstOrdEntryModel.setShippingPoint(outDataLineItem.getOutShippingPoint().getValue());
							}
							if (null != outDataLineItem.getOutPriceType())
							{
								abstOrdEntryModel.setPriceType(outDataLineItem.getOutPriceType().getValue());
							}
							if (null != outDataLineItem.getOutHSAPromotion()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutHSAPromotion().getValue()))
							{
								abstOrdEntryModel.setHsaPromotion(Double.valueOf(outDataLineItem.getOutHSAPromotion().getValue()));
								hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
							}
							if (null != outDataLineItem.getOutFreightAndHandling()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutFreightAndHandling().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outDataLineItem.getOutFreightAndHandling().getValue()));
								totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
							}
							if (null != outDataLineItem.getOutDropshipFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutDropshipFee().getValue()))
							{
								abstOrdEntryModel.setDropshipFee(Double.valueOf(outDataLineItem.getOutDropshipFee().getValue()));
								dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
							}
							if (null != outDataLineItem.getOutNetValue()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutNetValue().getValue()))
							{
								abstOrdEntryModel.setNetPrice(Double.valueOf(outDataLineItem.getOutNetValue().getValue()));
							}
							if (null != outDataLineItem.getOutSubstitutionReason())
							{
								abstOrdEntryModel.setReasonForRejection(outDataLineItem.getOutSubstitutionReason().getValue());
							}
							if (null != outDataLineItem.getOutFreightFees()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutFreightFees().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outDataLineItem.getOutFreightFees().getValue()));
							}
							// changes Start for JJEPIC-111
							// Check if the the item category is tapa then enter inside if block
							if (itemCategoryForTapa.contains(outDataLineItem.getOutItemCategory().getValue()))
							{
								for (final OutDataLineItem outDataLineItemForPrice : outResponse.getOutResponseData().get(0)
										.getOutDataHeader().getValue().getOutDataLineItem())
								{
									if (null != outDataLineItemForPrice.getOutHigherLevelItemNumber()
											&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutHigherLevelItemNumber().getValue())
											&& StringUtils.equals(abstOrdEntryModel.getSapOrderlineNumber(), outDataLineItemForPrice
													.getOutHigherLevelItemNumber().getValue()))
									{
										if (null != outDataLineItemForPrice.getOutMaterialQuantity()
												&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutMaterialQuantity().getValue()))
										{
											totalQuantity = totalQuantity
													+ Double.parseDouble(outDataLineItemForPrice.getOutMaterialQuantity().getValue());
										}
										if (null != outDataLineItemForPrice.getOutNetValue()
												&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutNetValue().getValue()))
										{
											totalNetValue = totalNetValue
													+ Double.parseDouble(outDataLineItemForPrice.getOutNetValue().getValue());
										}
										if (null != outDataLineItem.getOutScheduleLineItem())
										{
											for (final OutScheduleLineItem outScheduleLineItem : outDataLineItemForPrice
													.getOutScheduleLineItem())
											{
												// Check for the not null object
												if (null != outScheduleLineItem)
												{
													// if the delivery line block is one of the line block 16,17, 18,29,14 then enter inside if block and remove that entry from the cart model
													if (null != outScheduleLineItem.getOutLineDeliveryBlock()
															&& StringUtils.isNotEmpty(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
															&& productLevelErrorCode.contains(outScheduleLineItem.getOutLineDeliveryBlock()
																	.getValue()))
													{
														if (removedProductCodes.containsKey(mapContainsProductErrorKeyAndMessage
																.get(outScheduleLineItem.getOutLineDeliveryBlock().getValue())))
														{
															removedProductCodes
																	.get(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
																			.getOutLineDeliveryBlock().getValue()))
																	.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING)
																	.append(Jnjgtb2boutboundserviceConstants.EMPTY_SPACE)
																	.append(abstOrdEntryModel.getProduct().getCode());
														}
														else
														{
															final StringBuilder stringBuilder = new StringBuilder(abstOrdEntryModel.getProduct()
																	.getCode());
															removedProductCodes.put(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
																	.getOutLineDeliveryBlock().getValue()), stringBuilder);
														}
														modelService.remove(abstOrdEntryModel);
														modelService.refresh(cartModel);
														isOrderEntryDeleted = true;
														break;
													}
												}
											}
										}
									}
								}
								if (totalQuantity > 0.0 && !isOrderEntryDeleted)
								{
									abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format(totalNetValue / totalQuantity)));
									abstOrdEntryModel.setTotalPrice(Double.valueOf(totalNetValue));
									grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
								}
							}
							// To set the schedule lines information in order model.
							if (null != outDataLineItem.getOutScheduleLineItem() && !isOrderEntryDeleted)
							{
								if (CollectionUtils.isNotEmpty(abstOrdEntryModel.getDeliverySchedules()))
								{
									try
									{
										modelService.refresh(cartModel);
										modelService.removeAll(abstOrdEntryModel.getDeliverySchedules());
									}
									catch (final ModelRemovalException exception)
									{
										LOGGER.error(exception.getMessage());
										abstOrdEntryModel.setDeliverySchedules(null);
									}
								}
								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues(
										Jnjgtb2boutboundserviceConstants.CONS_EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								final List<String> errorList = new ArrayList<String>();
								for (final OutScheduleLineItem outScheduleLineItem : outDataLineItem.getOutScheduleLineItem())
								{
									// Check for the not null object
									if (null != outScheduleLineItem)
									{
										// if the delivery line block is one of the line block 16,17, 18,29,14 then enter inside if block and remove that entry from the cart model
										if (null != outScheduleLineItem.getOutLineDeliveryBlock()
												&& StringUtils.isNotEmpty(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
												&& productLevelErrorCode.contains(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
												&& !itemCategoryForTapa.contains(outDataLineItem.getOutItemCategory().getValue()))
										{
											if (removedProductCodes.containsKey(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
													.getOutLineDeliveryBlock().getValue())))
											{
												removedProductCodes
														.get(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
																.getOutLineDeliveryBlock().getValue()))
														.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING)
														.append(Jnjgtb2boutboundserviceConstants.EMPTY_SPACE)
														.append(abstOrdEntryModel.getProduct().getCode());
											}
											else
											{
												final StringBuilder stringBuilder = new StringBuilder(abstOrdEntryModel.getProduct()
														.getCode());
												removedProductCodes.put(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
														.getOutLineDeliveryBlock().getValue()), stringBuilder);
											}

											modelService.remove(abstOrdEntryModel);
											modelService.refresh(cartModel);
											isOrderEntryDeleted = true;
											break;
										}
										else
										{
											final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
											jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
											if (null != outScheduleLineItem.getOutLineNumber())
											{
												jnjDelSchModel.setLineNumber(outScheduleLineItem.getOutLineNumber().getValue());
											}
											if (null != outScheduleLineItem.getOutDeliveryDate()
													&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutDeliveryDate().getValue()))
											{
												jnjDelSchModel.setDeliveryDate(formatResponseDate(outScheduleLineItem.getOutDeliveryDate()
														.getValue()));
												if (errorList.size() < 1
														&& null != cartModel.getNamedDeliveryDate()
														&& null != outScheduleLineItem.getOutLineDeliveryBlock()
														&& StringUtils.isNotEmpty(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
														&& jnjDelSchModel.getDeliveryDate().after(cartModel.getNamedDeliveryDate())
														&& outScheduleLineItem
																.getOutLineDeliveryBlock()
																.getValue()
																.equals(
																		jnjConfigService
																				.getConfigModelsByIdAndKey(
																						Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA,
																						Jnjgtb2boutboundserviceConstants.DELIVERY_BLOCK).get(0)
																				.getValue()))
												{
													errorList.add(Jnjgtb2boutboundserviceConstants.CART_CONS_INVALID_DELIVERY_DATE);
												}
												// enter inside if block if epectedDelDate is null or the deliver date is not null and expectedDelDate is of past date then delivery date.
												if (null == expectedDelDate
														|| (null != jnjDelSchModel.getDeliveryDate() && jnjDelSchModel.getDeliveryDate().after(
																expectedDelDate)))
												{
													expectedDelDate = jnjDelSchModel.getDeliveryDate();
												}
											}
											if (null != outScheduleLineItem.getOutMaterialAvailabilityDate()
													&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutMaterialAvailabilityDate()
															.getValue()))
											{
												jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(outScheduleLineItem
														.getOutMaterialAvailabilityDate().getValue()));
											}
											if (null != outScheduleLineItem.getOutShipDate()
													&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutShipDate().getValue()))
											{
												jnjDelSchModel
														.setShipDate(formatResponseDate(outScheduleLineItem.getOutShipDate().getValue()));
											}// if block loop
											if (null != outScheduleLineItem.getOutLineStatus())
											{
												jnjDelSchModel.setLineStatus(outScheduleLineItem.getOutLineStatus().getValue());
											}// if block loop
											if (null != outScheduleLineItem.getOutConfirmedQuantity()
													&& StringUtils.isNotEmpty(outScheduleLineItem.getOutConfirmedQuantity().getValue()))
											{
												jnjDelSchModel.setQty(JnjGTCoreUtil.convertStringToLong(outScheduleLineItem
														.getOutConfirmedQuantity().getValue()));
											}// if block loop
											jnjDelSchModelList.add(jnjDelSchModel);
										}
									}
								}
								// if the entry is removed then no need to set any thing in it.
								if (!isOrderEntryDeleted)
								{
									abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
									cartModel.setValidationErrorKeys(errorList);
									cartModel.setNamedDeliveryDate(expectedDelDate);
								}
							}
						}
					}
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SIMULATE_CONS_ORD + Logging.HYPHEN + "mapCartModelFromOutDataLineItem()" + Logging.HYPHEN
							+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
					throw exception;
				}
			}
		}

		// Populates the order level value in order model.
		cartModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
		cartModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		cartModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		cartModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
		cartModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		cartModel.setTotalGrossPrice(Double.valueOf(grossPrice));
		cartModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		cartModel.setTotalTax(Double.valueOf(totalTax));
		if (null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSalesOrdertTotalNetValue()
				&& StringUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
						.getOutSalesOrdertTotalNetValue().getValue()))
		{
			final String totalNetValue = outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
					.getOutSalesOrdertTotalNetValue().getValue();
			cartModel.setSubtotal(Double.valueOf(totalNetValue));
			cartModel.setTotalPrice(Double.valueOf((Double.parseDouble(totalNetValue) + totalTax)));
		}
		cartModel.setSapValidated(Boolean.TRUE);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SIMULATE_CONS_ORD + Logging.HYPHEN + "mapCartModelFromOutDataLineItem()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Map order model from out order line.
	 * 
	 * @param orderModel
	 *           the order model
	 * @throws SystemException
	 *            the system exception
	 */
	public void mapOrderModelFromOutDataLineItem(final OrderModel orderModel, final OutResponse outResponse,
			final Map<String, StringBuilder> removedProductCodes) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapOrderModelFromOutDataLineItem()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		double totalDiscounts = 0.0;
		double totalTax = 0.0;
		double dropShipFee = 0.0;
		double totalFreightFees = 0.0;
		double minimumOrderFee = 0.0;
		double grossPrice = 0.0;
		double hsaPromotion = 0.0;
		final CurrencyModel currencyModel = orderModel.getCurrency();
		CatalogVersionModel catalogVersionModel = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : orderModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}

		if (null != outResponse && CollectionUtils.isNotEmpty(outResponse.getOutResponseData())
				&& null != outResponse.getOutResponseData().get(0).getOutDataHeader()
				&& null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue())
		{
			final List<String> itemCategories = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<String> itemCategoriesForTan = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_VALID_ITEM_CATEGORY_KEY_FOR_TAN, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final List<String> highLevelItemValues = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_ORDER_LINE_HIGH_LEVEL_VALUE, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final String productLevelErrorCode = JnJCommonUtil
					.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_LEVEL_ERROR_CODE);
			final Map<String, String> mapContainsProductErrorKeyAndMessage = new HashMap<String, String>();
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_EFFECTIVITY_ERROR_CODE),
					"cart.consumer.product.effectivity.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_EXCLUSION_BLOCK_ERROR_CODE),
					"cart.consumer.product.exclusion.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_FIRST_DEA_BLOCK_ERROR_CODE),
					"cart.consumer.product.dea.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_SECOND_DEA_BLOCK_ERROR_CODE),
					"cart.consumer.product.dea.block.error.code");
			mapContainsProductErrorKeyAndMessage.put(
					JnJCommonUtil.getValue(Jnjb2bCoreConstants.Order.CONSUMER_PRODUCT_MULTIPLE_BLOCK_ERROR_CODE),
					"cart.consumer.product.multiple.block.error.code");
			final List<String> itemCategoryForTapa = JnJCommonUtil.getValues(
					Jnjb2bCoreConstants.Order.CONSUMER_ORDER_TAPA_LINE_ITEM_CATEGORY, Jnjb2bCoreConstants.SYMBOl_COMMA);
			final DecimalFormat decimalFormat = new DecimalFormat("#.##");
			// To fetch active Catalog Version Model.
			if (null != orderModel.getSite() && CollectionUtils.isNotEmpty(orderModel.getSite().getStores()))
			{
				if (CollectionUtils.isNotEmpty(orderModel.getSite().getStores().get(0).getCatalogs()))
				{
					catalogVersionModel = orderModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
				}
			}
			// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
			for (final OutDataLineItem outDataLineItem : outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
					.getOutDataLineItem())
			{
				try
				{
					if (null != outDataLineItem
							&& null != outDataLineItem.getOutItemCategory()
							&& (itemCategories.contains(outDataLineItem.getOutItemCategory().getValue()) || (itemCategoriesForTan
									.contains(outDataLineItem.getOutItemCategory().getValue()) && (null == outDataLineItem
									.getOutHigherLevelItemNumber() || highLevelItemValues.contains(outDataLineItem
									.getOutHigherLevelItemNumber().getValue())))) && null != outDataLineItem.getOutMaterialNumber())
					{
						double totalQuantity = 0.0;
						double totalNetValue = 0.0;
						AbstractOrderEntryModel abstOrdEntryModel = null;
						boolean isOrderEntryDeleted = false;
						final JnJProductModel product = jnJGTProductService.getProductModelByCode(outDataLineItem
								.getOutMaterialNumber().getValue(), catalogVersionModel);
						if (null != product)
						{
							final ProductModel baseProduct = product.getMaterialBaseProduct() == null ? product : product
									.getMaterialBaseProduct();

							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProduct.getCode()))
							{
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProduct.getCode());

							// check for the null value and set it in Sap Order Line Number.
							if (null != outDataLineItem.getOutLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(outDataLineItem.getOutLineNumber().getValue());
							}
							if (null != outDataLineItem.getOutMaterialQuantity()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMaterialQuantity().getValue()))
							{
								abstOrdEntryModel.setQuantity(JnjGTCoreUtil.convertStringToLong(outDataLineItem.getOutMaterialQuantity()
										.getValue()));
							}

							if (null != outDataLineItem.getOutReasonForRejection())
							{
								abstOrdEntryModel.setReasonForRejection(outDataLineItem.getOutReasonForRejection().getValue());
							}
							if (null != outDataLineItem.getOutExpeditedFees()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutExpeditedFees().getValue()))
							{
								abstOrdEntryModel.setExpeditedFee(Double.valueOf(outDataLineItem.getOutExpeditedFees().getValue()));
							}
							if (null != outDataLineItem.getOutInsurance()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutInsurance().getValue()))
							{
								abstOrdEntryModel.setInsurance(Double.valueOf(outDataLineItem.getOutInsurance().getValue()));
							}
							if (null != outDataLineItem.getOutInternaitonalFreight()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutInternaitonalFreight().getValue()))
							{
								abstOrdEntryModel.setInternationalFreight(Double.valueOf(outDataLineItem.getOutInternaitonalFreight()
										.getValue()));
							}
							if (null != outDataLineItem.getOutHandlingFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutHandlingFee().getValue()))
							{
								abstOrdEntryModel.setHandlingFee(Double.valueOf(outDataLineItem.getOutHandlingFee().getValue()));
							}
							if (null != outDataLineItem.getOutMinimumOrderFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMinimumOrderFee().getValue()))
							{
								abstOrdEntryModel.setMinimumOrderFee(Double.valueOf(outDataLineItem.getOutMinimumOrderFee().getValue()));
								minimumOrderFee = minimumOrderFee + abstOrdEntryModel.getMinimumOrderFee().doubleValue();
							}
							if (null != outDataLineItem.getOutBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(outDataLineItem.getOutBaseUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
							}
							if (null != outDataLineItem.getOutSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService
										.getUnitByCode(outDataLineItem.getOutSalesUOM().getValue());
								abstOrdEntryModel.setUnit(unitModel);
								if (CollectionUtils.isNotEmpty(product.getVariants()))
								{
									// Get the Variant Product Models of the base product.
									final List<VariantProductModel> variantProductModels = (List) product.getVariants();
									// Iterate them one by one.
									for (final VariantProductModel variantProductModel : variantProductModels)
									{
										// Check it for not null value for the models and equate the unit model code of the Variant model with the incoming response sales uom.
										if (null != variantProductModel
												&& null != variantProductModel.getUnit()
												&& variantProductModel.getUnit().getCode()
														.equalsIgnoreCase(outDataLineItem.getOutSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
											break;
										}
									}
								}
							}

							abstOrdEntryModel.setItemCategory(outDataLineItem.getOutItemCategory().getValue());
							if (null != outDataLineItem.getOutHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(outDataLineItem.getOutHigherLevelItemNumber().getValue());
							}
							if (null != outDataLineItem.getOutGrossPriceBR()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutGrossPriceBR().getValue()))
							{
								abstOrdEntryModel.setGrossPrice(Double.valueOf(outDataLineItem.getOutGrossPriceBR().getValue()));
							}
							if (null != outDataLineItem.getOutNetValue()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutNetValue().getValue())
									&& null != outDataLineItem.getOutMaterialQuantity()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutMaterialQuantity().getValue()))
							{
								abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format((Double.parseDouble(outDataLineItem
										.getOutNetValue().getValue()))
										/ (Double.parseDouble(outDataLineItem.getOutMaterialQuantity().getValue())))));
								abstOrdEntryModel.setTotalPrice(Double.valueOf(decimalFormat.format(Double.parseDouble(outDataLineItem
										.getOutNetValue().getValue()))));
							}
							if (null != outDataLineItem.getOutDiscounts()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutDiscounts().getValue()))
							{
								abstOrdEntryModel.setDiscountValues(createDiscountValues(
										(Double.valueOf(outDataLineItem.getOutDiscounts().getValue())), currencyModel));
								totalDiscounts = totalDiscounts
										+ Double.valueOf(outDataLineItem.getOutDiscounts().getValue()).doubleValue();
							}
							if (null != outDataLineItem.getOutTaxes()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutTaxes().getValue()))
							{
								abstOrdEntryModel.setTaxValues(createTaxValues(
										(Double.valueOf(outDataLineItem.getOutTaxes().getValue())), currencyModel));

								totalTax = totalTax + Double.valueOf(outDataLineItem.getOutTaxes().getValue()).doubleValue();
							}
							if (null != outDataLineItem.getOutRoute())
							{
								abstOrdEntryModel.setRoute(outDataLineItem.getOutRoute().getValue());
							}
							if (null != outDataLineItem.getOutShippingPoint())
							{
								abstOrdEntryModel.setShippingPoint(outDataLineItem.getOutShippingPoint().getValue());
							}
							if (null != outDataLineItem.getOutPriceType())
							{
								abstOrdEntryModel.setPriceType(outDataLineItem.getOutPriceType().getValue());
							}
							if (null != outDataLineItem.getOutHSAPromotion()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutHSAPromotion().getValue()))
							{
								abstOrdEntryModel.setHsaPromotion(Double.valueOf(outDataLineItem.getOutHSAPromotion().getValue()));
								hsaPromotion = hsaPromotion + abstOrdEntryModel.getHsaPromotion().doubleValue();
							}
							if (null != outDataLineItem.getOutFreightAndHandling()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutFreightAndHandling().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outDataLineItem.getOutFreightAndHandling().getValue()));
								totalFreightFees = totalFreightFees + abstOrdEntryModel.getFreightFees().doubleValue();
							}
							if (null != outDataLineItem.getOutDropshipFee()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutDropshipFee().getValue()))
							{
								abstOrdEntryModel.setDropshipFee(Double.valueOf(outDataLineItem.getOutDropshipFee().getValue()));
								dropShipFee = dropShipFee + abstOrdEntryModel.getDropshipFee().doubleValue();
							}
							if (null != outDataLineItem.getOutNetValue()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutNetValue().getValue()))
							{
								abstOrdEntryModel.setNetPrice(Double.valueOf(outDataLineItem.getOutNetValue().getValue()));
							}
							if (null != outDataLineItem.getOutSubstitutionReason())
							{
								abstOrdEntryModel.setReasonForRejection(outDataLineItem.getOutSubstitutionReason().getValue());
							}
							if (null != outDataLineItem.getOutFreightFees()
									&& StringUtils.isNotEmpty(outDataLineItem.getOutFreightFees().getValue()))
							{
								abstOrdEntryModel.setFreightFees(Double.valueOf(outDataLineItem.getOutFreightFees().getValue()));
							}
							if (itemCategoryForTapa.contains(outDataLineItem.getOutItemCategory().getValue()))
							{
								for (final OutDataLineItem outDataLineItemForPrice : outResponse.getOutResponseData().get(0)
										.getOutDataHeader().getValue().getOutDataLineItem())
								{
									if (null != outDataLineItemForPrice.getOutHigherLevelItemNumber()
											&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutHigherLevelItemNumber().getValue())
											&& StringUtils.equals(abstOrdEntryModel.getSapOrderlineNumber(), outDataLineItemForPrice
													.getOutHigherLevelItemNumber().getValue()))
									{
										if (null != outDataLineItemForPrice.getOutMaterialQuantity()
												&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutMaterialQuantity().getValue()))
										{
											totalQuantity = totalQuantity
													+ Double.parseDouble(outDataLineItemForPrice.getOutMaterialQuantity().getValue());
										}
										if (null != outDataLineItemForPrice.getOutNetValue()
												&& StringUtils.isNotEmpty(outDataLineItemForPrice.getOutNetValue().getValue()))
										{
											totalNetValue = totalNetValue
													+ Double.parseDouble(outDataLineItemForPrice.getOutNetValue().getValue());
										}
										if (null != outDataLineItem.getOutScheduleLineItem())
										{
											for (final OutScheduleLineItem outScheduleLineItem : outDataLineItemForPrice
													.getOutScheduleLineItem())
											{
												// Check for the not null object
												if (null != outScheduleLineItem)
												{
													// if the delivery line block is one of the line block 16,17, 18,29,14 then enter inside if block and remove that entry from the cart model
													if (null != outScheduleLineItem.getOutLineDeliveryBlock()
															&& StringUtils.isNotEmpty(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
															&& productLevelErrorCode.contains(outScheduleLineItem.getOutLineDeliveryBlock()
																	.getValue()))
													{
														if (removedProductCodes.containsKey(mapContainsProductErrorKeyAndMessage
																.get(outScheduleLineItem.getOutLineDeliveryBlock().getValue())))
														{
															removedProductCodes
																	.get(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
																			.getOutLineDeliveryBlock().getValue()))
																	.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING)
																	.append(Jnjgtb2boutboundserviceConstants.EMPTY_SPACE)
																	.append(abstOrdEntryModel.getProduct().getCode());
														}
														else
														{
															final StringBuilder stringBuilder = new StringBuilder(abstOrdEntryModel.getProduct()
																	.getCode());
															removedProductCodes.put(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
																	.getOutLineDeliveryBlock().getValue()), stringBuilder);
														}
														modelService.remove(abstOrdEntryModel);
														modelService.refresh(orderModel);
														isOrderEntryDeleted = true;
														break;
													}
												}
											}
										}
									}
								}
								if (totalQuantity > 0.0 && !isOrderEntryDeleted)
								{
									abstOrdEntryModel.setBasePrice(Double.valueOf(decimalFormat.format(totalNetValue / totalQuantity)));
									abstOrdEntryModel.setTotalPrice(Double.valueOf(totalNetValue));
									grossPrice = grossPrice + abstOrdEntryModel.getTotalPrice().doubleValue();
								}
							}
							// To set the schedule lines information in order model.
							if (null != outDataLineItem.getOutScheduleLineItem() && !isOrderEntryDeleted)
							{
								if (CollectionUtils.isNotEmpty(abstOrdEntryModel.getDeliverySchedules()))
								{
									try
									{
										modelService.removeAll(abstOrdEntryModel.getDeliverySchedules());
									}
									catch (final ModelRemovalException exception)
									{
										LOGGER.error(exception.getMessage());
										abstOrdEntryModel.setDeliverySchedules(null);
									}
								}
								final List<OutScheduleLineItem> arrayLine = outDataLineItem.getOutScheduleLineItem();
								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues(
										Jnjgtb2boutboundserviceConstants.CONS_EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								for (final OutScheduleLineItem outScheduleLineItem : arrayLine)
								{
									// if the delivery line block is one of the line block 16,17, 18,29,14 then enter inside if block and remove that entry from the cart model
									if (null != outScheduleLineItem.getOutLineDeliveryBlock()
											&& StringUtils.isNotEmpty(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
											&& productLevelErrorCode.contains(outScheduleLineItem.getOutLineDeliveryBlock().getValue())
											&& !itemCategoryForTapa.contains(outDataLineItem.getOutItemCategory().getValue()))
									{
										if (removedProductCodes.containsKey(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
												.getOutLineDeliveryBlock().getValue())))
										{
											removedProductCodes
													.get(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
															.getOutLineDeliveryBlock().getValue()))
													.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING)
													.append(Jnjgtb2boutboundserviceConstants.EMPTY_SPACE)
													.append(abstOrdEntryModel.getProduct().getCode());
										}
										else
										{
											final StringBuilder stringBuilder = new StringBuilder(abstOrdEntryModel.getProduct().getCode());
											removedProductCodes.put(mapContainsProductErrorKeyAndMessage.get(outScheduleLineItem
													.getOutLineDeliveryBlock().getValue()), stringBuilder);
										}

										modelService.remove(abstOrdEntryModel);
										modelService.refresh(orderModel);
										break;
									}
									else
									{
										final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
										jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
										if (null != outScheduleLineItem.getOutLineNumber())
										{
											jnjDelSchModel.setLineNumber(outScheduleLineItem.getOutLineNumber().getValue());
										}
										if (null != outScheduleLineItem.getOutDeliveryDate()
												&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutDeliveryDate()))
										{
											jnjDelSchModel.setDeliveryDate(formatResponseDate(outScheduleLineItem.getOutDeliveryDate()
													.getValue()));
										}
										if (null != outScheduleLineItem.getOutMaterialAvailabilityDate()
												&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutMaterialAvailabilityDate()
														.getValue()))
										{
											jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(outScheduleLineItem
													.getOutMaterialAvailabilityDate().getValue()));
										}
										if (null != outScheduleLineItem.getOutShipDate()
												&& !exceptedDateFormatList.contains(outScheduleLineItem.getOutShipDate().getValue()))
										{
											jnjDelSchModel.setShipDate(formatResponseDate(outScheduleLineItem.getOutShipDate().getValue()));
										}// if block loop
										if (null != outScheduleLineItem.getOutLineStatus())
										{
											jnjDelSchModel.setLineStatus(outScheduleLineItem.getOutLineStatus().getValue());
										}// if block loop
										if (null != outScheduleLineItem.getOutConfirmedQuantity()
												&& StringUtils.isNotEmpty(outScheduleLineItem.getOutConfirmedQuantity().getValue()))
										{
											jnjDelSchModel.setQty(JnjGTCoreUtil.convertStringToLong(outScheduleLineItem
													.getOutConfirmedQuantity().getValue()));
										}// if block loop
										jnjDelSchModelList.add(jnjDelSchModel);
									}
								}
								// if the entry is removed then no need to set any thing in it.
								if (!isOrderEntryDeleted)
								{
									abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
								}
							}

						}
					}
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapCartModelFromOutDataLineItem()" + Logging.HYPHEN
							+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
					throw exception;
				}
			}
		}

		// Populates the order level value in order model.
		orderModel.setTotalTaxValues(createTaxValues(Double.valueOf(totalTax), currencyModel));
		orderModel.setTotalminimumOrderFee(Double.valueOf(minimumOrderFee));
		orderModel.setTotalDropShipFee(Double.valueOf(dropShipFee));
		orderModel.setTotalFreightFees(Double.valueOf(totalFreightFees));
		orderModel.setTotalDiscounts(Double.valueOf(totalDiscounts));
		orderModel.setTotalGrossPrice(Double.valueOf(grossPrice));
		orderModel.setTotalHsaPromotion(Double.valueOf(hsaPromotion));
		orderModel.setTotalTax(Double.valueOf(totalTax));
		if (null != outResponse.getOutResponseData().get(0).getOutDataHeader().getValue().getOutSalesOrdertTotalNetValue()
				&& StringUtils.isNotEmpty(outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
						.getOutSalesOrdertTotalNetValue().getValue()))
		{
			final String totalNetValue = outResponse.getOutResponseData().get(0).getOutDataHeader().getValue()
					.getOutSalesOrdertTotalNetValue().getValue();
			orderModel.setSubtotal(Double.valueOf(totalNetValue));
			orderModel.setTotalPrice(Double.valueOf((Double.parseDouble(totalNetValue) + totalTax)));
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CREATE_CONS_ORD + Logging.HYPHEN + "mapOrderModelFromOutDataLineItem()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Sets Discount Values for Order Entry.
	 * 
	 * @param discountValue
	 *           the discount value
	 * @param currencyModel
	 *           the currency model
	 * @return the list
	 */
	protected List<DiscountValue> createDiscountValues(final Double discountValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("createDiscountValues()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final DiscountValue discount = new DiscountValue(Jnjgtb2boutboundserviceConstants.DISCOUNT_VALUE, 0.0D, false,
				discountValue.doubleValue(), (currencyModel == null) ? null : currencyModel.getIsocode());
		final List<DiscountValue> discountValues = new ArrayList<DiscountValue>();
		discountValues.add(discount);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("createDiscountValues()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return discountValues;
	}

	/**
	 * Format response date and return the formatted date.
	 * 
	 * @param date
	 *           the date
	 * @return the date
	 * @throws SystemException
	 *            the system exception
	 */
	protected Date formatResponseDate(final String date) throws SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("formatResponseDate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		Date formattedDate = null;
		try
		{
			if (StringUtils.isNotEmpty(date))
			{
				formattedDate = new SimpleDateFormat(
						Config.getParameter(Jnjgtb2boutboundserviceConstants.CONSUMER_REQUEST_DATE_FORMAT)).parse(date);
			}
		}
		catch (final ParseException exception)
		{
			LOGGER.error("formatResponseDate()" + Logging.HYPHEN + "Parsing Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the JnjGTCreateConsOrdMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("formatResponseDate()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return formattedDate;
	}

	/**
	 * Sets Tax values for Order Entry.
	 * 
	 * @param taxValue
	 *           the tax value
	 * @param currencyModel
	 *           the currency model
	 * @return the collection
	 */
	protected Collection<TaxValue> createTaxValues(final Double taxValue, final CurrencyModel currencyModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("createTaxValues()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final TaxValue tax = new TaxValue(Jnjgtb2boutboundserviceConstants.TAX_VALUE, 0.0D, false, taxValue.doubleValue(),
				(currencyModel == null) ? null

				: currencyModel.getIsocode());
		final Collection<TaxValue> taxValues = new ArrayList<TaxValue>();
		taxValues.add(tax);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("createTaxValues()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return taxValues;
	}

	protected void mapControlAreaDataInRequest(final InWMControlArea inWmControlArea, final String orderCode)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapControlAreaDataInRequest()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjConfigModel> interFaceNumber = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.INTERFACE_NUMBER);
		if (CollectionUtils.isNotEmpty(interFaceNumber))
		{
			inWmControlArea.setInInterfaceNumber(objectFactory.createInWMControlAreaInInterfaceNumber(interFaceNumber.get(0)
					.getValue()));
		}
		final List<JnjConfigModel> businessObjectName = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.BUSINESS_OBJECT_NAME);
		if (CollectionUtils.isNotEmpty(businessObjectName))
		{
			inWmControlArea.setInBusinessObjectName(objectFactory.createInWMControlAreaInBusinessObjectName(businessObjectName
					.get(0).getValue()));
		}
		final List<JnjConfigModel> sourceSysId = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.SOURCE_SYS_ID);
		if (CollectionUtils.isNotEmpty(sourceSysId))
		{
			inWmControlArea.setInSourceSystemID(sourceSysId.get(0).getValue());
		}
		final List<JnjConfigModel> businessSector = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.BUSINESS_SECTOR);
		if (CollectionUtils.isNotEmpty(businessSector))
		{
			inWmControlArea.setInBusinessSector(businessSector.get(0).getValue());
		}
		final List<JnjConfigModel> zone = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.ZCODE);
		if (CollectionUtils.isNotEmpty(zone))
		{
			inWmControlArea.setInZCODE(objectFactory.createInWMControlAreaInZCODE(zone.get(0).getValue()));
		}
		final List<JnjConfigModel> region = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.REGION);
		if (CollectionUtils.isNotEmpty(region))
		{
			inWmControlArea.setInRegion(objectFactory.createInWMControlAreaInRegion(region.get(0).getValue()));
		}
		final Date date = new Date();
		final Format secondformatter = new SimpleDateFormat(Jnjb2bCoreConstants.SECOND_FORMATTER);
		final String formatedSecondTime = secondformatter.format(date);
		inWmControlArea.setInUniqueTransactionID(objectFactory.createInWMControlAreaInUniqueTransactionID(orderCode
				.concat(formatedSecondTime)));
		final List<JnjConfigModel> transactionDateTime = jnjConfigService.getConfigModelsByIdAndKey(
				Jnjgtb2boutboundserviceConstants.WM_CONTROL_AREA, Jnjgtb2boutboundserviceConstants.CONSUMER_DATE_FORMATER);
		if (CollectionUtils.isNotEmpty(transactionDateTime))
		{
			final Format miliSecondformatter = new SimpleDateFormat(transactionDateTime.get(0).getValue());
			final String formatedMiliSecondTime = miliSecondformatter.format(date);
			inWmControlArea.setInTransactionDateTime(objectFactory
					.createInWMControlAreaInTransactionDateTime(formatedMiliSecondTime));
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapControlAreaDataInRequest()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}
}
