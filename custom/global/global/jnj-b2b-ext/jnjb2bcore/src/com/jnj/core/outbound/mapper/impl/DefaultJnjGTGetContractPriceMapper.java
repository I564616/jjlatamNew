/**
 * 
 */
package com.jnj.core.outbound.mapper.impl;

import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.ContractPriceReply;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.ContractPriceRequest;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapper;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.GetContractPriceWrapperResponse;
import com.jnj.hcswmd01.mu007_epic_contractprice_v1.getcontractpricewrapperwebservice.ObjectFactory;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.data.JnjGTGetContractPriceResponseData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.core.outbound.mapper.JnjGTGetContractPriceMapper;
import com.jnj.core.outbound.services.JnjGTGetContractPriceService;
import com.jnj.core.services.b2bunit.impl.DefaultJnjGTB2BUnitService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * The JnjGTGetContractPriceMapperImpl class contains the definition of all the method of the
 * JnjGTGetContractPriceMapper interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTGetContractPriceMapper implements JnjGTGetContractPriceMapper
{
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTGetContractPriceMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	protected JnjConfigService jnjConfigService;

	@Autowired
	protected JnjCartService jnjCartService;

	@Autowired
	protected JnjGTGetContractPriceService jnjGTGetContractPriceService;
	

	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnJCommonUtil getJnjCommonUtil() {
		return jnjCommonUtil;
	}

	public void setJnjCommonUtil(JnJCommonUtil jnjCommonUtil) {
		this.jnjCommonUtil = jnjCommonUtil;
	}

	@Resource(name = "jnjB2BUnitService")
	protected DefaultJnjGTB2BUnitService defaultjnjGTB2BUnitService;

	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnjGTGetContractPriceService getJnjGTGetContractPriceService() {
		return jnjGTGetContractPriceService;
	}

	public DefaultJnjGTB2BUnitService getJnjGTB2BUnitServiceImpl() {
		return defaultjnjGTB2BUnitService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	@Override
	public JnjGTGetContractPriceResponseData mapGetContractPriceMapperRequestResponse(final CartModel cartModel,
			final CartEntryModel cartEntryModel) throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "mapGetContractPriceRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final JnjGTGetContractPriceResponseData JnjGTGetContractPriceResponseData = mapGetContractPrice(
				cartEntryModel.getReferencedVariant(), cartEntryModel.getQuantity(), cartModel.getOrderType().toString());

		return JnjGTGetContractPriceResponseData;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	@Override
	public JnjGTGetContractPriceResponseData mapGetContractPrice(final JnjGTVariantProductModel JnjGTVariantProductModel,
			final Long quantity, final String orderType) throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "mapGetContractPrice()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final ContractPriceRequest contractPriceRequest = new ContractPriceRequest();

		// Fetch system value from the config table.
		final JAXBElement<String> system = objectFactory.createContractPriceRequestSystem(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.SYSTEM).get(0).getValue());
		contractPriceRequest.setSystem(system);
		// Fetch userName value from the config table.
		final JAXBElement<String> userName = objectFactory.createContractPriceRequestUsername(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.USERNAME).get(0).getValue());
		contractPriceRequest.setUsername(userName);
		// Fetch interfaceID value from the config table.
		final JAXBElement<String> interfaceID = objectFactory.createContractPriceRequestInterfaceID(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.INTERFACE_ID).get(0).getValue());
		contractPriceRequest.setInterfaceID(interfaceID);
		// Fetch orderChannel value from the config table.
		final JAXBElement<String> orderChannel = objectFactory.createContractPriceRequestOrderChannel(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.ORDER_CHANNEL).get(0).getValue());
		contractPriceRequest.setOrderChannel(orderChannel);
		// Fetch value from the config table.
		final JAXBElement<String> orderSource = objectFactory.createContractPriceRequestOrderSource(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.ORDER_SOURCE).get(0).getValue());
		contractPriceRequest.setOrderSource(orderSource);
		// Fetch orderSource value from the config table.
		final JAXBElement<String> sync = objectFactory.createContractPriceRequestSYNC(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.SYNC).get(0).getValue());
		contractPriceRequest.setSYNC(sync);
		// Fetch eChannelIndicator value from the config table.
		final JAXBElement<String> eChannelIndicator = objectFactory.createContractPriceRequestEChannelIndicator(jnjConfigService
				.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
						Jnjb2bCoreConstants.GetContractPrice.E_CHANNEL_INDICATOR).get(0).getValue());
		contractPriceRequest.setEChannelIndicator(eChannelIndicator);
		// Fetch pricingDate value from the config table.
		final String formattedDate = new SimpleDateFormat(jnjCommonUtil.getDateFormat())
				.format(new Date());
		final JAXBElement<String> pricingDate = objectFactory.createContractPriceRequestPricingDate(formattedDate);
		contractPriceRequest.setPricingDate(pricingDate);
		if (null != (JnjGTVariantProductModel.getBaseProduct())
				&& null != ((JnJProductModel) JnjGTVariantProductModel.getBaseProduct()).getSalesOrgCode())
		{
			contractPriceRequest.setDivision(((JnJProductModel) JnjGTVariantProductModel.getBaseProduct()).getSalesOrgCode());
		}
		if (orderType == null)
		{
			final JAXBElement<String> getOrderType = objectFactory.createContractPriceRequestOrderType(jnjConfigService
					.getConfigModelsByIdAndKey(Jnjb2bCoreConstants.GetContractPrice.GET_CONTRACT_PRICE,
							Jnjb2bCoreConstants.GetContractPrice.ORDER_TYPE).get(0).getValue());
			contractPriceRequest.setOrderType(getOrderType);
		}
		else
		{
			final JAXBElement<String> getOrderType = objectFactory.createContractPriceRequestOrderType(orderType);
			contractPriceRequest.setOrderType(getOrderType);
		}
		contractPriceRequest.setSoldToCustomer(defaultjnjGTB2BUnitService.getCurrentB2BUnit().getUid());
		final JAXBElement<String> shipToCustomer = objectFactory.createContractPriceRequestShipToCustomer(defaultjnjGTB2BUnitService
				.getCurrentB2BUnit().getUid());
		contractPriceRequest.setShipToCustomer(shipToCustomer);
		if (((JnJProductModel) JnjGTVariantProductModel.getBaseProduct()).getMaterialBaseProduct() == null)
		{
			contractPriceRequest.setMaterial(JnjGTVariantProductModel.getBaseProduct().getCode());
		}
		else
		{
			contractPriceRequest.setMaterial(((JnJProductModel) JnjGTVariantProductModel.getBaseProduct())
					.getMaterialBaseProduct().getCode());
		}
		if (null != JnjGTVariantProductModel.getUnit())
		{
			final JAXBElement<String> uom = objectFactory
					.createContractPriceRequestUOM(JnjGTVariantProductModel.getUnit().getCode());
			contractPriceRequest.setUOM(uom);
		}
		if (null != quantity)
		{
			final JAXBElement<String> orderQuantity = objectFactory.createContractPriceRequestOrderQuantity(quantity.toString());
			contractPriceRequest.setOrderQuantity(orderQuantity);
		}
		final GetContractPriceWrapper getContractPriceWrapper = new GetContractPriceWrapper();
		getContractPriceWrapper.setContractPriceRequest(contractPriceRequest);
		final GetContractPriceWrapperResponse getContractPriceWrapperResponse = jnjGTGetContractPriceService
				.getContractPriceWrapper(getContractPriceWrapper);
		//put all values here
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "mapGetContractPrice()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return mapResponseWithContPriceData(getContractPriceWrapperResponse);
	}

	/**
	 * Map cart model from ContractPriceReply.
	 * 
	 * @param getContractPriceWrapperResponse
	 *           the getContractPriceWrapperResponse model
	 * @return the jnj na get contract price response data
	 * @throws SystemException
	 *            the system exception
	 */
	protected JnjGTGetContractPriceResponseData mapResponseWithContPriceData(
			final GetContractPriceWrapperResponse getContractPriceWrapperResponse)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "mapResponseWithContPriceData()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final JnjGTGetContractPriceResponseData JnjGTGetContractPriceResponseData = new JnjGTGetContractPriceResponseData();
		if (null != getContractPriceWrapperResponse
				&& null != getContractPriceWrapperResponse.getContractPriceReply()
				&& (null == getContractPriceWrapperResponse.getErrorMessage() || StringUtils.isEmpty(getContractPriceWrapperResponse
						.getErrorMessage().getValue())))
		{
			final ContractPriceReply contractPriceReply = getContractPriceWrapperResponse.getContractPriceReply();
			if (null != contractPriceReply.getOrderLineQuantity()
					&& StringUtils.isNotEmpty(contractPriceReply.getOrderLineQuantity().getValue()))
			{
				JnjGTGetContractPriceResponseData.setOrderLineQuantity(JnjGTCoreUtil.convertStringToLong(contractPriceReply
						.getOrderLineQuantity().getValue()));
			}
			if (null != contractPriceReply.getContractPrice()
					&& StringUtils.isNotEmpty(contractPriceReply.getContractPrice().getValue()))
			{
				JnjGTGetContractPriceResponseData.setContractPrice(Double.valueOf(contractPriceReply.getContractPrice().getValue())
						.doubleValue());
			}
			if (null != contractPriceReply.getUOM())
			{
				JnjGTGetContractPriceResponseData.setUom(contractPriceReply.getUOM().getValue());
			}
			if (null != contractPriceReply.getPer())
			{
				JnjGTGetContractPriceResponseData.setPer(contractPriceReply.getPer().getValue());
			}
			if (null != contractPriceReply.getCurrency())
			{
				JnjGTGetContractPriceResponseData.setCurrency(contractPriceReply.getCurrency().getValue());
			}
			if (null != contractPriceReply.getContractNumber())
			{
				JnjGTGetContractPriceResponseData.setContractNumber(contractPriceReply.getContractNumber().getValue());
			}
			JnjGTGetContractPriceResponseData.setSapResponseStatus(true);
		}
		else if (null != getContractPriceWrapperResponse && null != getContractPriceWrapperResponse.getErrorMessage())
		{
			JnjGTGetContractPriceResponseData.setSapResponseStatus(false);
			JnjGTGetContractPriceResponseData.setErrorMessage(getContractPriceWrapperResponse.getErrorMessage().getValue());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.GET_CONTRACT_PRICE + Logging.HYPHEN + "mapResponseWithContPriceData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnjGTGetContractPriceResponseData;
	}
}