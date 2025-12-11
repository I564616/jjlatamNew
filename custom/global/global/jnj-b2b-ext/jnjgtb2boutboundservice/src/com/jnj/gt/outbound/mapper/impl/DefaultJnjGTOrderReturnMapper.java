/**
 *
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import de.hybris.platform.variants.model.VariantProductModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.cart.JnjCartService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.InOrderLines;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPInput;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OrderReturnInSAPOutput;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.OutOrderLines;
import com.jnj.hcswmd01.mu007_epic_orderreturn_v1.orderreturnwebservice.ScheduledLines;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTOrderReturnResponseData;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.gt.outbound.mapper.JnjGTOrderReturnMapper;
import com.jnj.gt.outbound.services.JnjGTOrderReturnService;
import com.jnj.core.util.JnjGTCoreUtil;


/**
 * @author somya.sinha
 * 
 */
public class DefaultJnjGTOrderReturnMapper implements JnjGTOrderReturnMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTOrderReturnMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjCartService jnjCartService;

	@Autowired
	private JnjGTOrderReturnService jnjGTOrderReturnService;

	@Resource(name = "productService")
	private JnJGTProductService jnJGTProductService;

	@Autowired
	private ModelService modelService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;
	
	public JnjConfigService getJnjConfigService() {
		return jnjConfigService;
	}

	public JnjCartService getJnjCartService() {
		return jnjCartService;
	}

	public JnjGTOrderReturnService getJnjGTOrderReturnService() {
		return jnjGTOrderReturnService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * {!{@inheritDoc}.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @return the jnj na simulate order response data
	 * @throws IntegrationException
	 *            the integration exception
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */
	@Override
	public JnjGTOrderReturnResponseData mapOrderReturnRequestResponse(final CartModel cartModel) throws IntegrationException,
			SystemException, BusinessException
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapOrderReturnRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		StringBuilder productCodes = null;
		final JnjGTOrderReturnResponseData jnjGTOrderReturnData = new JnjGTOrderReturnResponseData();
		final OrderReturnInSAPInput orderReturnInSAPInput = new OrderReturnInSAPInput();
		if (null != cartModel.getOrderType())
		{
			orderReturnInSAPInput.setInOrderType(cartModel.getOrderType().getCode());
		}
		if (null != cartModel.getUnit())
		{
			orderReturnInSAPInput.setInSoldToNumber(cartModel.getUnit().getUid());
		}
		if (null != cartModel.getDeliveryAddress())
		{
			final JAXBElement<String> shipToAddress = objectFactory.createOrderReturnInSAPInputInShipToNumber(cartModel
					.getDeliveryAddress().getJnJAddressId());
			orderReturnInSAPInput.setInShipToNumber(shipToAddress);
		}
		// check for the not empty or not null
		if (null != cartModel.getPurchaseOrderNumber())
		{
			orderReturnInSAPInput.setInPONumber(cartModel.getPurchaseOrderNumber());
		}

		orderReturnInSAPInput.setInOrderSource(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_SOURCE));

		// Fetch value from the config table.
		orderReturnInSAPInput.setInOrderChannel(jnjConfigService
				.getConfigValueById(Jnjgtb2boutboundserviceConstants.Order.ORDER_CHANNEL));
		if (null != cartModel.getReasonCode())
		{
			orderReturnInSAPInput.setInOrderReason(cartModel.getReasonCode());
		}
		if (null != cartModel.getUser())
		{
			final JAXBElement<String> ContactName = objectFactory.createOrderReturnInSAPInputInContactName(cartModel.getUser()
					.getName());
			orderReturnInSAPInput.setInContactName(ContactName);

			for (final AddressModel addressModel : cartModel.getUser().getAddresses())
			{
				if (null != addressModel && null != addressModel.getContactAddress()
						&& addressModel.getContactAddress().booleanValue())
				{
					final JAXBElement<String> Contactphonenumber = objectFactory
							.createOrderReturnInSAPInputInContactphonenumber(addressModel.getPhone1());
					orderReturnInSAPInput.setInContactphonenumber(Contactphonenumber);
				}
			}
		}
		orderReturnInSAPInput.setInFax(objectFactory
				.createOrderReturnInSAPInputInFax(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		orderReturnInSAPInput.setInDebitMemo(objectFactory
				.createOrderReturnInSAPInputInDebitMemo(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		if (StringUtils.isNotEmpty(cartModel.getAttention()))
		{
			orderReturnInSAPInput.setInCustomerNotes(objectFactory.createOrderReturnInSAPInputInCustomerNotes(cartModel
					.getAttention()));
		}
		else
		{
			orderReturnInSAPInput.setInCustomerNotes(objectFactory
					.createOrderReturnInSAPInputInCustomerNotes(Jnjgtb2boutboundserviceConstants.EMPTY_STRING));
		}

		// check for the entries are not empty or are not null.
		if (CollectionUtils.isNotEmpty(cartModel.getEntries()))
		{
			orderReturnInSAPInput.getInOrderLines().addAll(mapOrderLinesFromAbstOrderEntries(cartModel.getEntries()));
		}

		final OrderReturnInSAPOutput orderReturnInSapOutput = jnjGTOrderReturnService.orderReturnInSAP(orderReturnInSAPInput);
		if (null != orderReturnInSapOutput
				|| CollectionUtils.isNotEmpty(orderReturnInSapOutput.getOutOrderLines())
				&& CollectionUtils.isNotEmpty(cartModel.getEntries())
				&& (null == orderReturnInSapOutput.getErrorMessage() || orderReturnInSapOutput.getErrorMessage().getValue()
						.equals(Jnjgtb2boutboundserviceConstants.EMPTY_STRING)))
		{
			// SAP order number value is saved in order number during the conversion of CartModel to order model.
			cartModel.setSapOrderNumber(orderReturnInSapOutput.getOutSalesOrderNumber());
			mapCartModelFromOutOrderLine(cartModel, orderReturnInSapOutput, productCodes);
			//Calculate cart for total values and save in data base
			jnjGTOrderReturnData.setSavedSuccessfully(jnjCartService.calculateValidatedCart(cartModel));
			jnjGTOrderReturnData.setSapResponseStatus(true);
			jnjGTOrderReturnData.setSapOrderNumber(orderReturnInSapOutput.getOutSalesOrderNumber());
		}
		else if (null != orderReturnInSapOutput && null != orderReturnInSapOutput.getErrorMessage())
		{
			productCodes = new StringBuilder();
			if (StringUtils.isNotEmpty(orderReturnInSapOutput.getOutSalesOrderNumber()))
			{
				cartModel.setSapOrderNumber(orderReturnInSapOutput.getOutSalesOrderNumber());
				productCodes.append(orderReturnInSapOutput.getOutSalesOrderNumber()).append(Jnjb2bCoreConstants.SYMBOl_DASH);
			}
			// added below call for removing the excluded products and including the excluded customer scenario.
			mapCartModelFromOutOrderLine(cartModel, orderReturnInSapOutput, productCodes);
			jnjCartService.calculateValidatedCart(cartModel);
			if (StringUtils.isNotEmpty(productCodes.toString()))
			{
				throw new BusinessException(productCodes.toString(), MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
			}
			jnjGTOrderReturnData.setSapResponseStatus(false);
			jnjGTOrderReturnData.setErrorMessage(orderReturnInSapOutput.getErrorMessage().getValue());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapOrderReturnRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTOrderReturnData;

	}

	/**
	 * Map in order lines fields from abstract order entries model.
	 * 
	 * @param abstOrdEntModelList
	 *           the abstract order entry model list
	 * @return the array of in order lines
	 */
	protected List<InOrderLines> mapOrderLinesFromAbstOrderEntries(final List<AbstractOrderEntryModel> abstOrdEntModelList)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<InOrderLines> arrayOfinOrderLinesItem = new ArrayList<InOrderLines>();
		// Iterating the Abstract Order Entries models one by one.
		for (final AbstractOrderEntryModel abstOrderEntryModel : abstOrdEntModelList)
		{
			final InOrderLines inOrderLines = new InOrderLines();
			inOrderLines.setMaterialNumber(abstOrderEntryModel.getProduct().getCode());
			inOrderLines.setQuantityReturned(String.valueOf(abstOrderEntryModel.getQuantity()));
			if (null != abstOrderEntryModel.getUnit())
			{
				final JAXBElement<String> salesUOM = objectFactory
						.createInOrderLinesSalesUOM(abstOrderEntryModel.getUnit().getCode());
				inOrderLines.setSalesUOM(salesUOM);
			}
			else
			{
				final JAXBElement<String> salesUOM = objectFactory
						.createInOrderLinesSalesUOM(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setSalesUOM(salesUOM);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getBatchNum()))
			{
				final JAXBElement<String> lot = objectFactory.createInOrderLinesLot(abstOrderEntryModel.getBatchNum());
				inOrderLines.setLot(lot);
			}
			else
			{
				final JAXBElement<String> lot = objectFactory.createInOrderLinesLot(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setLot(lot);
			}
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getReturnInvNumber()))
			{
				final JAXBElement<String> invoiceNumber = objectFactory.createInOrderLinesInvoiceNumber(abstOrderEntryModel
						.getReturnInvNumber());
				inOrderLines.setInvoiceNumber(invoiceNumber);
			}
			else
			{
				final JAXBElement<String> invoiceNumber = objectFactory
						.createInOrderLinesInvoiceNumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setInvoiceNumber(invoiceNumber);
			}
			final JAXBElement<String> invoiceLine = objectFactory
					.createInOrderLinesInvoiceLine(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
			inOrderLines.setInvoiceLine(invoiceLine);
			if (StringUtils.isNotEmpty(abstOrderEntryModel.getPONumber()))
			{
				final JAXBElement<String> poNumber = objectFactory.createInOrderLinesPONumber(abstOrderEntryModel.getPONumber());
				inOrderLines.setPONumber(poNumber);
			}
			else
			{
				final JAXBElement<String> poNumber = objectFactory
						.createInOrderLinesPONumber(Jnjgtb2boutboundserviceConstants.EMPTY_STRING);
				inOrderLines.setPONumber(poNumber);
			}
			// add the object in the list
			arrayOfinOrderLinesItem.add(inOrderLines);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapOrderLinesFromAbstOrderEntries()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return arrayOfinOrderLinesItem;
	}

	/**
	 * Map cart model from out order line.
	 * 
	 * @param cartModel
	 *           the cart model
	 * @param orderReturnInSAPOutput
	 *           the test pricing from gateway output
	 * @throws SystemException
	 *            the system exception
	 * @throws BusinessException
	 */
	protected void mapCartModelFromOutOrderLine(final CartModel cartModel, final OrderReturnInSAPOutput orderReturnInSAPOutput,
			final StringBuilder productCodes) throws SystemException, BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		CatalogVersionModel catalogVersionModel = null;
		final Map<String, AbstractOrderEntryModel> mapMaterialNoWithOrdLinesOutput = new HashMap<String, AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel abstOrdEntryModel : cartModel.getEntries())
		{
			// Null check for the OrderLinesOutput object and material number as the material number is of JAXB element type.
			if (null != abstOrdEntryModel && null != abstOrdEntryModel.getProduct())
			{
				mapMaterialNoWithOrdLinesOutput.put(abstOrdEntryModel.getProduct().getCode(), abstOrdEntryModel);
			}
		}
		final List<String> itemCategories = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.ITEM_CATEGORY_FOR_MDD,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> lineNumberExcluded = JnJCommonUtil.getValues(Jnjgtb2boutboundserviceConstants.LINE_NUMBER_EXCLUDED,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		// To fetch active Catalog Version Model.
		if (null != cartModel.getSite() && CollectionUtils.isNotEmpty(cartModel.getSite().getStores()))
		{
			if (CollectionUtils.isNotEmpty(cartModel.getSite().getStores().get(0).getCatalogs()))
			{
				catalogVersionModel = cartModel.getSite().getStores().get(0).getCatalogs().get(0).getActiveCatalogVersion();
			}
		}
		// Iterates the Cart Entries one by one and populates its fields value by getting them from the response object.
		for (final OutOrderLines orderLinesOutput : orderReturnInSAPOutput.getOutOrderLines())
		{
			// In case of excluded customer, enter inside if block.
			if (null != orderLinesOutput && null != orderLinesOutput.getLineNumber()
					&& !lineNumberExcluded.contains(orderLinesOutput.getLineNumber().getValue()))
			{
				AbstractOrderEntryModel abstOrdEntryModel = null;
				// In case of excluded products, enter inside if block.
				if (StringUtils.isNotEmpty(orderLinesOutput.getMessage()) && null != orderLinesOutput.getMaterialNumber()
						&& StringUtils.isNotEmpty(orderLinesOutput.getMaterialNumber().getValue()) && null != productCodes)
				{
					if (StringUtils.isNotEmpty(productCodes.toString())
							&& productCodes.toString().contains(Jnjb2bCoreConstants.SYMBOl_DASH))
					{
						productCodes.append(orderLinesOutput.getMaterialNumber().getValue());
					}
					else if (StringUtils.isNotEmpty(productCodes.toString()))
					{
						productCodes.append(Jnjgtb2boutboundserviceConstants.COMMA_STRING);
						productCodes.append(orderLinesOutput.getMaterialNumber().getValue());
					}
					else
					{
						productCodes.append(orderLinesOutput.getMaterialNumber().getValue());
					}
					if (mapMaterialNoWithOrdLinesOutput.containsKey(orderLinesOutput.getMaterialNumber().getValue()))
					{
						abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(orderLinesOutput.getMaterialNumber().getValue());
						modelService.remove(abstOrdEntryModel);
						modelService.refresh(cartModel);
					}
				}
				else
				{
					try
					{
						if (null != orderLinesOutput.getItemCategory()
								&& !(itemCategories.contains(orderLinesOutput.getItemCategory().getValue()))
								&& null != orderLinesOutput.getMaterialNumber())
						{

							final JnJProductModel product = jnJGTProductService.getProductModelByCode(orderLinesOutput
									.getMaterialNumber().getValue(), catalogVersionModel);
							final ProductModel baseProduct = product.getMaterialBaseProduct() == null ? product : product
									.getMaterialBaseProduct();

							if (!mapMaterialNoWithOrdLinesOutput.containsKey(baseProduct.getCode()))
							{
								continue;
							}
							abstOrdEntryModel = mapMaterialNoWithOrdLinesOutput.get(baseProduct.getCode());
							if (null != orderLinesOutput.getLineNumber())
							{
								abstOrdEntryModel.setSapOrderlineNumber(orderLinesOutput.getLineNumber().getValue());
							}

							if (null != orderLinesOutput.getBaseUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getBaseUOM().getValue());
								abstOrdEntryModel.setBaseUOM(unitModel);
							}
							if (null != orderLinesOutput.getSalesUOM())
							{
								final UnitModel unitModel = jnJGTProductService.getUnitByCode(orderLinesOutput.getSalesUOM().getValue());
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
														.equalsIgnoreCase(orderLinesOutput.getSalesUOM().getValue()))
										{
											// set the variant product model in reference variant model.
											abstOrdEntryModel.setReferencedVariant((JnjGTVariantProductModel) variantProductModel);
										}
									}
								}
							}
							if (null != orderLinesOutput.getMessage())
							{
								abstOrdEntryModel.setMessage(orderLinesOutput.getMessage());
							}
							if (null != orderLinesOutput.getHigherLevelItemNumber())
							{
								abstOrdEntryModel.setHigherLevelItemNo(orderLinesOutput.getHigherLevelItemNumber().getValue());
							}
							if (null != orderLinesOutput.getInvoiceNumber())
							{
								abstOrdEntryModel.setInvoiceNumber(orderLinesOutput.getInvoiceNumber().getValue());
							}
							if (null != orderLinesOutput.getBillingDeliveryBlock())
							{
								abstOrdEntryModel.setBillingDeliveryBlock(orderLinesOutput.getBillingDeliveryBlock().getValue());
							}
							if (null != orderLinesOutput.getPlant())
							{
								abstOrdEntryModel.setPlant(orderLinesOutput.getPlant().getValue());
							}
							if (null != orderLinesOutput.getRejectReason())
							{
								abstOrdEntryModel.setReasonForRejection(orderLinesOutput.getRejectReason().getValue());
							}
							abstOrdEntryModel.setItemCategory(orderLinesOutput.getItemCategory().getValue());

							if (null != orderLinesOutput.getScheduledLines())
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
								final List<String> exceptedDateFormatList = JnJCommonUtil.getValues(
										Jnjgtb2boutboundserviceConstants.EXCEPTED_DATE_FORMAT, Jnjb2bCoreConstants.SYMBOl_COMMA);
								final List<JnjDeliveryScheduleModel> jnjDelSchModelList = new ArrayList<JnjDeliveryScheduleModel>();
								for (final ScheduledLines scheduledLines : orderLinesOutput.getScheduledLines())
								{
									// Check for the not null object
									if (null != scheduledLines)
									{
										final JnjDeliveryScheduleModel jnjDelSchModel = new JnjDeliveryScheduleModel();
										jnjDelSchModel.setOwnerEntry(abstOrdEntryModel);
										if (null != scheduledLines.getLineNumber())
										{
											jnjDelSchModel.setLineNumber(scheduledLines.getLineNumber().getValue());
										}
										if (null != scheduledLines.getScheduledLineNumber())
										{
											jnjDelSchModel.setScheduledLineNumber(scheduledLines.getScheduledLineNumber().getValue());
										}

										if (null != scheduledLines.getConfirmedQuantity()
												&& StringUtils.isNotEmpty(scheduledLines.getConfirmedQuantity().getValue()))
										{
											jnjDelSchModel.setQty(JnjGTCoreUtil.convertStringToLong(scheduledLines.getConfirmedQuantity()
													.getValue()));
										}

										if (null != scheduledLines.getMaterialAvailabilityDate()
												&& !exceptedDateFormatList.contains(scheduledLines.getMaterialAvailabilityDate().getValue()))
										{
											jnjDelSchModel.setMaterialAvailabilityDate(formatResponseDate(scheduledLines
													.getMaterialAvailabilityDate().getValue()));
										}
										jnjDelSchModelList.add(jnjDelSchModel);
									}
								}
								abstOrdEntryModel.setDeliverySchedules(jnjDelSchModelList);
							}
						}
					}
					catch (final ModelNotFoundException exception)
					{
						LOGGER.error(Logging.ORDER_RETURN + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
								+ "Model Not Found Exception Occurred for sales UOM or base UOM" + exception.getMessage(), exception);
						throw new SystemException("System Exception throw from the JnjGTOrderReturnMapperImpl class",
								MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
					}
				}
			}
			else
			{
				throw new BusinessException();
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "mapCartModelFromOutOrderLine()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
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
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		Date formattedDate = null;
		try
		{
			if (StringUtils.isNotEmpty(date))
			{
				formattedDate = new SimpleDateFormat(Config.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT))
						.parse(date);
			}
		}
		catch (final ParseException exception)
		{
			LOGGER.error(Logging.ORDER_RETURN + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN
					+ "Parsing Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the JnjGTOrderReturnMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.ORDER_RETURN + Logging.HYPHEN + "formatResponseDate()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return formattedDate;
	}
}
