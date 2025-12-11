/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.services.order.impl;

import com.jnj.core.util.JnJCommonUtil;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Order;
import com.jnj.core.dataload.mapper.Header;
import com.jnj.core.dataload.mapper.Items;
import com.jnj.core.dataload.mapper.LatamLineItem;
import com.jnj.core.dataload.mapper.LineItem;
import com.jnj.core.dataload.mapper.PurchaseOrder;
import com.jnj.core.dataload.utility.JnJXMLFilePicker;
import com.jnj.core.dto.JnjUomDTO;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjIndirectCustomerModel;
import com.jnj.core.model.JnjUomConversionModel;
import com.jnj.core.services.JnJCustomerDataService;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.services.JnjLaSalesOrgCustomerService;
import com.jnj.core.services.company.JnjCompanyService;
import com.jnj.core.services.impl.JnjLaMessageServiceImpl;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.core.util.JnjGetCurrentDefaultB2BUnitUtil;
import com.jnj.core.util.JnjSftpFileTransferUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.daos.impl.DefaultLaJnjOrderDao;
import com.jnj.la.core.dto.JnjLaUomDTO;
import com.jnj.la.core.model.JnJCompanyModel;
import com.jnj.la.core.model.JnJLaB2BUnitModel;
import com.jnj.la.core.model.JnjCmirConversionModel;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import com.jnj.la.core.model.LoadTranslationModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.JnjLoadTranslationService;
import com.jnj.la.core.services.order.JnjLatamEdiOrderService;
import com.jnj.la.core.util.JnjlatamOrderUtil;
import com.jnj.la.core.util.JnjLaSftpFileTransferUtil;


/**
 *
 */
public class JnjLatamEdiOrderServiceImpl implements JnjLatamEdiOrderService
{

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private JnjLoadTranslationService jnjLoadTranslationService;

	@Autowired
	private JnJLaProductService jnjLaProductService;

	@Autowired
	private JnJCustomerDataService jnJCustomerDataService;

	@Autowired
	private JnjCompanyService jnjCompanyService;

	@Autowired
	private JnjLaSalesOrgCustomerService jnjLaSalesOrgCustomerService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private DefaultLaJnjOrderDao jnjOrderDao;

	@Autowired
	private JnjGetCurrentDefaultB2BUnitUtil jnjGetCurrentDefaultB2BUnitUtil;

	@Autowired
	private JnjLaMessageServiceImpl jnjLaMessageService;

    @Autowired
	private JnJCommonUtil jnJCommonUtil;

	private String elementValue;

	private final SimpleDateFormat sdfForAlberFile = new SimpleDateFormat(Jnjb2bCoreConstants.LoadInvoices.DATE_FORMAT);

	private final SimpleDateFormat sdfForSAPRequest = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.PO_DATE_FORMAT);

	private final SimpleDateFormat sdfForAliancaFile = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.EXP_DELIVERY_DATE_FORMAT);

	private final SimpleDateFormat sdfForSaoLuizFile = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.SAO_LUIZ_DATE_FORMAT);

	private static final Class currentClass = JnjLatamEdiOrderServiceImpl.class;

	private static final String salesOrgError = "order.home.salesOrg.error";
	private static final String SHAMODELS_LIST_IS_EMPTY = "Unable to obtain a B2B Unit from SHAModels. SHAModels list is empty";
	private static final String SHAMODELS_NO_USER_FOUND = "Unable to obtain a B2B Unit from SHAModels. No user found";

	@Override
	public String createOrderFromAlbertFile(final InputStream inputStream, final List<File> fileListForSAP,
			final List<String> errorDetailsList, final PurchaseOrder purchaseOrder,
			final List<JnjUploadOrderSHAModel> passedSHAModels)
	{
		final String methodName = "createOrderFromAlbertFile ()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		XMLStreamReader xmlStreamReader = null;
		String fileStatus = null;
		Header header = null;
		Items items = null;
		List<LineItem> lineItemList = null;
		LineItem lineItem = null;
		boolean lineItemProcess = false;
		Map<String, String> productNumberWithSalesOrg = null;
		JnJB2BUnitModel jnjB2bUnitModel = null;
		JnjUomDTO jnjUomDTO = null;
		boolean validProductNumber = true;
		boolean validShipTo = true;
		boolean errorStatus = false;
		final StringBuilder custProdNumStrBuilder = new StringBuilder();
		final StringBuilder expectedDeliveryDateStrBuilder = new StringBuilder();
		final StringBuilder shipToStrBuilder = new StringBuilder();
		final StringBuilder uomMultiplicityCheckBuilder = new StringBuilder();
		final StringBuilder uomStrBuilder = new StringBuilder();
		int lineValue = 10;
		int lineCounter = 0;
		int actualLineCounter = 0;
		// Create the instance of XML Input Factory.
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

		try
		{
			final JnJB2BUnitModel b2bUnit = getB2bUnitFromSHAModel(passedSHAModels);
			xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
			int eventType = xmlStreamReader.getEventType();
			while (xmlStreamReader.hasNext())
			{
				switch (eventType)
				{
					case XMLStreamConstants.START_ELEMENT:

						// check the Pedido tag in XML
						if (Jnjlab2bcoreConstants.Order.PEDIDO.equals(xmlStreamReader.getLocalName()))
						{
							productNumberWithSalesOrg = new HashMap<>();
						}
						// check the Cabecalho tag in XML
						else if (Jnjlab2bcoreConstants.Order.CABECALHO.equals(xmlStreamReader.getLocalName()))
						{
							header = new Header();
						}
						// check the Itens_Requisicao tag in XML
						else if (Jnjlab2bcoreConstants.Order.ITEMS_REQUISICAO.equals(xmlStreamReader.getLocalName()))
						{
							items = new Items();
							lineItemList = new ArrayList<>();
						}
						// check the Item_Requisicao tag in XML
						else if (Jnjlab2bcoreConstants.Order.ITEM_REQUISICAO.equals(xmlStreamReader.getLocalName()))
						{
							validProductNumber = true;
							lineItemProcess = false;
							lineItem = new LineItem();
							actualLineCounter++;
						}
						break;

					case XMLStreamConstants.CHARACTERS:
						elementValue = xmlStreamReader.getText();
						break;

					case XMLStreamConstants.END_ELEMENT:

						if (Jnjlab2bcoreConstants.Order.REQUISICAO.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							header.setpONumber(elementValue);
							jnjB2bUnitModel = passedSHAModel.getB2bUnitId();
							if (null != jnjB2bUnitModel)
							{
								header.setSoldToNumber(jnjB2bUnitModel.getUid());
							}
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"PO Number from XML file is : " + elementValue + "and sold to number  : " + jnjB2bUnitModel.getUid(),
									currentClass);
						}
						if (Jnjlab2bcoreConstants.Order.CENTRO.equalsIgnoreCase(xmlStreamReader.getLocalName()))
						{
							// if the shipToNumber is already set then won't enter in the below if block.
							if (StringUtils.isEmpty(header.getShipToNumber()) && validShipTo)
							{
								// if the value retrieves from the file is not empty then enter in the if block.
								if (StringUtils.isNotEmpty(elementValue.trim()))
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Ship To Number from XML file is : " + elementValue.trim(), currentClass);
									final JnJB2BUnitModel jnjB2BUnitModel = passedSHAModel.getB2bUnitId();
									final String finalShipToNumber = foundConversion(elementValue.trim(), jnjB2BUnitModel);
									if (null != finalShipToNumber)
									{
										header.setShipToNumber(finalShipToNumber);
									}
									else
									{

										{
											JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Ship To Number is not existed" + elementValue.trim(), currentClass);
										}
										JnjlatamOrderUtil.stringBuilderMethod(shipToStrBuilder, elementValue.trim());
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
										validShipTo = false;
									}
								}
								// set the element value in the Error Details.
								else
								{

									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Ship To Number is empty " + elementValue + " ,invalidShipTo :" + validShipTo, currentClass);
									}
									JnjlatamOrderUtil.stringBuilderMethod(shipToStrBuilder, "order.home.emptyKey");
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
									validShipTo = false;
								}
							}
							else
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Ship to Number is already set so skipping the record with value : " + elementValue, currentClass);
							}
						}
						else if (Jnjlab2bcoreConstants.Order.CODIGO_PRODUTO.equalsIgnoreCase(xmlStreamReader.getLocalName())
								&& validShipTo)
						{
							final String customerProductNumber = elementValue.trim();
							try
							{
								// Set the line number value to 10 and then increase the value by 10 for each line
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Product Number from XML file is : " + customerProductNumber, currentClass);
								// check it Customer Product Number is not Empty and null.
								if (StringUtils.isNotEmpty(customerProductNumber))
								{
									final LoadTranslationModel loadTranslationModel = jnjLoadTranslationService
											.getLoadTranslationModel(customerProductNumber, b2bUnit);
									// Check for the Load Translation Model object. Enters in the if block if it's not null.
									if (null != loadTranslationModel && null != loadTranslationModel.getProductId())
									{
										final JnJProductModel jnjProductModel = loadTranslationModel.getProductId();
										lineItem.setProductNumber(jnjProductModel.getCatalogId().toUpperCase());
										// call the getCustDelUomMappingByUnitModel method to get the value of the UOM mapping.
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Product Number from load translation model : "
														+ jnjProductModel.getCatalogId().toUpperCase(),
												currentClass);
										if (null != loadTranslationModel.getCustomerUOM())
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"value of unit of measure in CMIR model  : " + loadTranslationModel.getCustomerUOM(),
													currentClass);
											jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForAlbert(jnjProductModel,
													loadTranslationModel);
											if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
											{
												JnjGTCoreUtil.logInfoMessage(
														Logging.SUBMIT_ORDER_EDI, methodName, "unit of measure is : "
																+ ((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode() + " when CMIR is present",
														currentClass);
												lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase());
											}
											else
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"Load translation model with  given catalog id : "
																+ jnjProductModel.getCatalogId().toUpperCase()
																+ "  and with customer unit of measure : " + loadTranslationModel.getCustomerUOM()
																+ " , is does not have numerator define",
														currentClass);
												JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomStrBuilder,
														customerProductNumber, loadTranslationModel.getCustomerUOM().toString());
												fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
												validProductNumber = false;
											}
										}
										else
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Load translation model with  given catalog id : "
															+ jnjProductModel.getCatalogId().toUpperCase()
															+ " does not have customer unit of measure define please check",
													currentClass);
											JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomStrBuilder, customerProductNumber,
													"order.home.emptyKey");
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validProductNumber = false;
										}
										if (!setSalesOrgCustomer(jnjProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
										{
											JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
													+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validProductNumber = false;
										}

									}
									//Whenever we don't find the Load Translation Model for an element then throw business exception.
									else
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Load translation model was not present for given customer product number : "
														+ customerProductNumber.toUpperCase() + " , So querying Product model for this.",
												currentClass);
										final JnJProductModel jnJProductModel = jnjLaProductService.getProductByCatalogIdForEdi(
												customerProductNumber.toUpperCase(), passedSHAModel.getB2bUnitId());
										if (null != jnJProductModel && null != jnJProductModel.getCatalogId())
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Product model with catalog id : " + jnJProductModel.getCatalogId()
															+ " present for given customerProductNumer : " + customerProductNumber.toUpperCase(),
													currentClass);
											lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
											final LoadTranslationModel loadTranslationModelNew = jnjLoadTranslationService
													.getLoadTranslationModelByProductNumber(jnJProductModel, b2bUnit);
											if (null != loadTranslationModelNew
													&& StringUtils.isNotEmpty(loadTranslationModelNew.getCustomerUOM()))
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"value of unit of measure in CMIR model in case of catalog ID is : "
																+ loadTranslationModelNew.getCustomerUOM(),
														currentClass);
												jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForAlbert(jnJProductModel,
														loadTranslationModelNew);
												if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
												{
													JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
															"unit of measure is : " + ((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase(),
															currentClass);
													lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase());
												}
												else
												{
													JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
															"Load translation model with  given catalog id : "
																	+ jnJProductModel.getCatalogId().toUpperCase()
																	+ " either not present or  not have numerator define please check",
															currentClass);
													JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomStrBuilder,
															customerProductNumber, "order.home.emptyKey");
													fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
													validProductNumber = false;
												}
											}
											else
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"Load translation model with  given catalog id : "
																+ jnJProductModel.getCatalogId().toUpperCase()
																+ " does not have customer unit of measure define please check",
														currentClass);
												JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomStrBuilder,
														customerProductNumber, "order.home.emptyKey");
												fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
												validProductNumber = false;
											}

											if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
											{
												JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
														+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
												fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
												validProductNumber = false;
											}
										}
										else
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Product model not present for given catalog id : " + customerProductNumber.toUpperCase(),
													currentClass);
											JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validProductNumber = false;
										}
									}
								}
								else
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Customer Product Number is null or empty in the file " + elementValue, currentClass);
									JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, "order.home.emptyKey");
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
									validProductNumber = false;
								}
							}
							catch (final ModelNotFoundException modelNotFoundException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Not Found exception occured.",
										modelNotFoundException, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								validProductNumber = false;
							}
							catch (final ModelLoadingException modelLoadingException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Loading exception occured",
										modelLoadingException, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								validProductNumber = false;
							}
							catch (final Throwable throwable)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Exception occured", throwable,
										currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								validProductNumber = false;
							}
						}
						else if (Jnjlab2bcoreConstants.Order.QUANTIDADE.equalsIgnoreCase(xmlStreamReader.getLocalName())
								&& validProductNumber && validShipTo)
						{
							final String quantity = elementValue.trim();
							final float finalQty = Integer.parseInt(quantity) * ((JnjLaUomDTO) jnjUomDTO).getFinalUnitCount();
							final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
							dfs.setDecimalSeparator('.');
							final DecimalFormat decimalFormat = new DecimalFormat("#.00", dfs);
							final float finalQuantity = Float.parseFloat(decimalFormat.format(finalQty));
							lineItem.setQuantity(String.valueOf(finalQuantity));
						}
						else if (Jnjlab2bcoreConstants.Order.DTREMESSA.equalsIgnoreCase(xmlStreamReader.getLocalName())
								&& validProductNumber && validShipTo)
						{
							if (null != elementValue && StringUtils.isNotEmpty(elementValue.trim()))
							{
								try
								{
									lineItemProcess = true;
									sdfForAlberFile.setLenient(false);
									lineItem.setLineNumber(String.valueOf(lineValue));
									lineValue = lineValue + 10;
									lineCounter++;
									final Date givenDate = sdfForAlberFile.parse(elementValue);
									final Date currentDate = sdfForAlberFile.parse(sdfForAlberFile.format(new Date()));
									if (currentDate.after(givenDate))
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Requested Delivery Date is less then todays date so updating date to current date",
												currentClass);
										final String expDelDateInString = sdfForSAPRequest.format(currentDate);
										lineItem.setExpectedDeliveryDate(expDelDateInString);
									}
									else
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Requested Delivery Date is in correct format and it is greater then equal to todays date.",
												currentClass);
										final String expDelDateInString = sdfForSAPRequest.format(givenDate);
										lineItem.setExpectedDeliveryDate(expDelDateInString);
									}
								}
								catch (final ParseException parseException)
								{
									JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Requested Delivery Date is not in correct format so setting today date  in expected delivery date. ",
											parseException, currentClass);
									final String expDelDateInString = sdfForSAPRequest
											.format(sdfForAlberFile.parse(sdfForAlberFile.format(new Date())));
									lineItem.setExpectedDeliveryDate(expDelDateInString);
								}
							}
						}
						else if (Jnjlab2bcoreConstants.Order.ITEM_REQUISICAO.equals(xmlStreamReader.getLocalName()) && lineItemProcess)
						{
							lineItemList.add(lineItem);
						}
						else if (Jnjlab2bcoreConstants.Order.ITEMS_REQUISICAO.equals(xmlStreamReader.getLocalName()))
						{
							items.setLineItem(lineItemList);
							purchaseOrder.setItems(items);
						}
						else if (Jnjlab2bcoreConstants.Order.PEDIDO.equals(xmlStreamReader.getLocalName()))
						{
							purchaseOrder.setHeader(header);
						}
						break;
				}
				// To get the next element in xml stream reader.
				eventType = xmlStreamReader.next();
			} //end of while loop
			  // close the XML Strema Reader and Input Stream.
			xmlStreamReader.close();
			inputStream.close();
			// if the errorDetailsList is not empty or the custProdNumStrBuilder string builder is not empty then populate the error message in errorDetailsList list.
			if (shipToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, null, null, null, shipToStrBuilder, null, null, null,
						null, null, null, passedSHAModel.getUser());
				errorStatus = true;
			}
			else
			{
				// Done the split logic and file generation by calling the spiltLogicForOrdersWithFileGeneration method.
				splitLogicForOrdersWithFileGeneration(purchaseOrder, productNumberWithSalesOrg, fileListForSAP, passedSHAModels);
			}
			if (!errorStatus && (!errorDetailsList.isEmpty() || custProdNumStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| uomMultiplicityCheckBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| expectedDeliveryDateStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| uomStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO))
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, custProdNumStrBuilder, null, uomStrBuilder, null,
						uomMultiplicityCheckBuilder, null, expectedDeliveryDateStrBuilder, null, null, null, passedSHAModel.getUser());
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Business exception occured.", businessException,
					currentClass);
			errorDetailsList.add(businessException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IOException inputOutputException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Input Output exception occured",
					inputOutputException, currentClass);
			errorDetailsList.add(inputOutputException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final XMLStreamException xmlStreamException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "XML Stream exception occured.", currentClass);
			errorDetailsList.add(xmlStreamException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final JAXBException jaxbException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "JAXB exception occured.", currentClass);
			errorDetailsList.add(jaxbException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Illegal Argument exception occured.", currentClass);
			errorDetailsList.add(illegalArgumentException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final Throwable throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured", currentClass);
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			if (actualLineCounter == lineCounter)
			{
				lineCounter = 0;
			}
		}

		if (actualLineCounter == lineCounter && lineCounter != 0 && fileStatus != Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS;
		}
		else if (lineCounter == 0)
		{
			if (errorDetailsList.isEmpty())
			{
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
		}
		else
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return fileStatus;
	}

	private static JnJB2BUnitModel getB2bUnitFromSHAModel(final List<JnjUploadOrderSHAModel> passedSHAModels) throws BusinessException {
		if (CollectionUtils.isEmpty(passedSHAModels)) {
			throw new BusinessException(SHAMODELS_LIST_IS_EMPTY);
		}

		final JnJB2bCustomerModel user = passedSHAModels.get(0).getUser();
		if (user == null) {
			throw new BusinessException(SHAMODELS_NO_USER_FOUND);
		}

		return (JnJB2BUnitModel) user.getDefaultB2BUnit();
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String createOrderFromAliancaFile(final InputStream inputStream, final List<File> fileListForSAP,
			final List<String> errorDetailsList, final PurchaseOrder purchaseOrder,
			final List<JnjUploadOrderSHAModel> passedSHAModels)
	{

		final String methodName = "createOrderFromAliancaFile ()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		XMLStreamReader xmlStreamReader = null;
		Header header = null;
		Items items = null;
		List<LineItem> lineItemList = null;
		LineItem lineItem = null;
		boolean customerDocumentId = false;
		boolean sendorId = false;
		boolean customerItemId = false;
		boolean itemId = false;
		boolean shipToNumberIdFlag = false;
		boolean lineItemProcess = false;
		boolean needDeliveryDate = false;
		Map<String, String> productNumberWithSalesOrg = null;
		LoadTranslationModel loadTranslationModel = null;
		JnjUomDTO jnjUomDTO = null;
		boolean validProductNumber = true;
		boolean validUom = true;
		boolean validSoldTo = true;
		boolean validShipTo = true;
		final StringBuilder custProdNumStrBuilder = new StringBuilder();
		final StringBuilder shipToStrBuilder = new StringBuilder();
		final StringBuilder soldToStrBuilder = new StringBuilder();
		final StringBuilder expectedDeliveryDateStrBuilder = new StringBuilder();
		final StringBuilder customerUomStrBuilder = new StringBuilder();
		int lineValue = 10;
		String uomField = null;
		String itemIdValue = null;
		String soldToNumber = null;
		String customerProductNumber = null;
		JnJB2BUnitModel jnjB2bUnitModel = null;
		String fileStatus = null;
		int lineCounter = 0;
		int actualLineCounter = 0;
		// Creating the instance of XML Input Factory.
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		try
		{
			xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
			int eventType = xmlStreamReader.getEventType();
			while (xmlStreamReader.hasNext())
			{
				switch (eventType)
				{
					case XMLStreamConstants.START_ELEMENT:
						// check the ProcessPurchaseOrder tag in XML
						if (Jnjlab2bcoreConstants.Order.PROCESS_PURCHASE_ORDER.equals(xmlStreamReader.getLocalName()))
						{
							productNumberWithSalesOrg = new HashMap<>();
						}
						if (Jnjlab2bcoreConstants.Order.SENDER.equals(xmlStreamReader.getLocalName()))
						{
							sendorId = true;
						}
						// check the PurchaseOrder tag in XML
						else if (Jnjlab2bcoreConstants.Order.PURCHASE_ORDER.equals(xmlStreamReader.getLocalName()))
						{
							items = new Items();
							lineItemList = new ArrayList<>();
						}
						// check the Header tag in XML
						else if (Jnjlab2bcoreConstants.Order.HEADER.equals(xmlStreamReader.getLocalName()))
						{
							header = new Header();
						}
						// check the Line tag in XML
						else if (Jnjlab2bcoreConstants.Order.LINE.equals(xmlStreamReader.getLocalName()))
						{
							lineItem = new LineItem();
							lineItemProcess = false;
							validProductNumber = true;
							validUom = true;
							actualLineCounter++;
							needDeliveryDate = true;
						}
						// check the CustomerDocumentId tag in XML
						else if (Jnjlab2bcoreConstants.Order.CUSTOMER_DOCUMENT_ID.equals(xmlStreamReader.getLocalName()))
						{
							customerDocumentId = true;
						}
						// check the CustomerItemId tag in XML
						else if (Jnjlab2bcoreConstants.Order.CUSTOMER_ITEM_ID.equals(xmlStreamReader.getLocalName()))
						{
							customerItemId = true;
						}
						// check the CustomerItemId tag in XML
						else if (Jnjlab2bcoreConstants.Order.ITEM_ID.equals(xmlStreamReader.getLocalName()))
						{
							itemId = true;
						}
						else if (Jnjlab2bcoreConstants.Order.PARTY_ID.equals(xmlStreamReader.getLocalName()))
						{
							shipToNumberIdFlag = true;
						}
						else if (Jnjlab2bcoreConstants.Order.Order_QUANTITY.equals(xmlStreamReader.getLocalName()))
						{
							uomField = xmlStreamReader.getAttributeValue(Order.ZERO);
						}
						break;

					case XMLStreamConstants.CHARACTERS:
						elementValue = xmlStreamReader.getText();
						break;
					case XMLStreamConstants.END_ELEMENT:

						if (Jnjlab2bcoreConstants.Order.LOGICAL_ID.equalsIgnoreCase(xmlStreamReader.getLocalName()) && sendorId)
						{
							final String logicalId = elementValue.trim();
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"value of LOGICAL_ID from XML file : " + logicalId, currentClass);
							if (null != logicalId)
							{
								jnjB2bUnitModel = fetchSoldFromCnpjValue(logicalId);
								if (null != jnjB2bUnitModel)
								{
									soldToNumber = jnjB2bUnitModel.getUid();
								}
								// set the value in the Error Details List.
								else
								{
									JnjlatamOrderUtil.stringBuilderMethod(soldToStrBuilder, logicalId);
									validSoldTo = false;
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
								}
							}
							else
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Logical Id can not be null , This will be error scenario", currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(soldToStrBuilder, "order.home.emptyKey");
								validSoldTo = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
							}
							sendorId = false;
						}
						else if (Jnjlab2bcoreConstants.Order.ID.equalsIgnoreCase(xmlStreamReader.getLocalName()) && shipToNumberIdFlag
								&& validSoldTo)
						{
							// if the value retrieves from the file is not empty then enter in the if block.
							header.setSoldToNumber(soldToNumber);
							if (StringUtils.isNotEmpty(elementValue.trim()) && !elementValue.trim().equals("0"))
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"SHIP TO value from XML file : " + elementValue.trim(), currentClass);
								final String finalShipToNumber = foundConversion(elementValue.trim(), jnjB2bUnitModel);
								if (null != finalShipToNumber)
								{
									header.setShipToNumber(finalShipToNumber);
								}
								else
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"No Conversion found for given : " + elementValue + " Ship To number", currentClass);
									JnjlatamOrderUtil.stringBuilderMethod(shipToStrBuilder, elementValue);
									validShipTo = false;
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
								}
							}
							// set the value in the Error Details List.
							else if (elementValue.trim().equals("0"))
							{
								header.setShipToNumber(soldToNumber);
							}
							else
							{
								JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Ship To Number can not be empty so returning error with empty key " + "order.home.emptyKey",
										currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(shipToStrBuilder, "order.home.emptyKey");
								validShipTo = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
							}
							shipToNumberIdFlag = false;
						}
						else if (Jnjlab2bcoreConstants.Order.ID.equalsIgnoreCase(xmlStreamReader.getLocalName()) && customerDocumentId)
						{
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"PO Number from XML file :" + elementValue.trim(), currentClass);
							header.setpONumber(elementValue.trim());
							customerDocumentId = false;
						}
						else if (Jnjlab2bcoreConstants.Order.ID.equalsIgnoreCase(xmlStreamReader.getLocalName()) && itemId)
						{
							itemIdValue = elementValue.trim();
							itemId = false;
						}
						else if (Jnjlab2bcoreConstants.Order.ID.equalsIgnoreCase(xmlStreamReader.getLocalName()) && customerItemId
								&& validShipTo && validSoldTo)
						{

							if (null != elementValue && StringUtils.isNotEmpty(elementValue.trim()) && !elementValue.equals("0"))
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"customer Item ID :" + elementValue + ",  so using this as product code", currentClass);
								customerProductNumber = elementValue;
							}
							else
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "customer Item ID :" + elementValue
										+ " is not present so using item id value : " + itemIdValue + ",  so using this as product code",
										currentClass);
								customerProductNumber = itemIdValue;
							}
							// check it Customer Product Number is not Empty and null.
							if (StringUtils.isEmpty(customerProductNumber))
							{
								JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Customer Product Number is empty or null in the file " + elementValue, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, "order.home.emptyKey");
								validProductNumber = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
							customerItemId = false;
						}

						else if (Jnjlab2bcoreConstants.Order.Order_QUANTITY.equalsIgnoreCase(xmlStreamReader.getLocalName())
								&& validProductNumber && validShipTo && validSoldTo)
						{
							final String quantity = elementValue.trim();
							try
							{
								JnJProductModel jnJProductModel = null;
								jnjUomDTO = null;
								// check it Customer Product Number is not Empty and null.
								loadTranslationModel = jnjLoadTranslationService.getLoadTranslationModelFile(customerProductNumber,
										soldToNumber);
								// Check for the Load Translation Model object. Enters in the if block if it's not null.
								if (null != loadTranslationModel && null != loadTranslationModel.getProductId())
								{
									jnJProductModel = loadTranslationModel.getProductId();
									lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"customer material number : " + customerProductNumber
													+ "EXIST in CMIR model , final Product code which is being sent to SAP is : "
													+ jnJProductModel.getCatalogId().toUpperCase(),
											currentClass);
									jnjUomDTO = processUOMForAlianca(lineItem, loadTranslationModel, jnjUomDTO, uomField, jnJProductModel);
									if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
									{
										lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase());
										final float finalUOMCount = Integer.parseInt(quantity)
												* ((JnjLaUomDTO) jnjUomDTO).getFinalUnitCount();
										final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
										dfs.setDecimalSeparator('.');
										final DecimalFormat decimalFormat = new DecimalFormat("#.00", dfs);
										final float finalQuantity = Float.parseFloat(decimalFormat.format(finalUOMCount));
										lineItem.setQuantity(String.valueOf(finalQuantity));
									}
									else
									{
										if (StringUtils.isEmpty(uomField))
										{
											uomField = "order.home.emptyKey";
										}
										JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Entered UOM : " + uomField + " is not valid ", currentClass);
										JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(customerUomStrBuilder,
												customerProductNumber, uomField);
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										validUom = false;
									}

									if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
									{
										JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
												+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										validProductNumber = false;
									}
								}
								else
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"customer material number : " + customerProductNumber + " NOT exist in CMIR model",
											currentClass);
									jnjB2bUnitModel = jnjOrderDao.fetchAllSoldToNumberForFile(soldToNumber);
									jnJProductModel = jnjLaProductService.getProductByCatalogIdForEdi(customerProductNumber,
											jnjB2bUnitModel);
									if (null != jnJProductModel)
									{
										lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"customer material number : " + customerProductNumber
														+ " exist in product model with catalog id : "
														+ jnJProductModel.getCatalogId().toUpperCase(),
												currentClass);
										final LoadTranslationModel loadTranslationModelNew = jnjLoadTranslationService
												.getLoadTranslationModelByCatalogIdUnit(jnJProductModel, soldToNumber);
										jnjUomDTO = processUOMForAlianca(lineItem, loadTranslationModelNew, jnjUomDTO, uomField,
												jnJProductModel);
										if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
										{
											lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase());
											final float finalUOMCount = Integer.parseInt(quantity)
													* ((JnjLaUomDTO) jnjUomDTO).getFinalUnitCount();
											final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
											dfs.setDecimalSeparator('.');
											final DecimalFormat decimalFormat = new DecimalFormat("#.00", dfs);
											final float finalQuantity = Float.parseFloat(decimalFormat.format(finalUOMCount));
											lineItem.setQuantity(String.valueOf(finalQuantity));
										}
										else
										{
											if (StringUtils.isEmpty(uomField))
											{
												uomField = "order.home.emptyKey";
											}
											JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Entered UOM : " + uomField + " is not valid ", currentClass);
											JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(customerUomStrBuilder,
													customerProductNumber, uomField);
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validUom = false;
										}
										if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
										{
											JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
													+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validProductNumber = false;
										}
									}
									else
									{
										JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Product model not found for given :  " + customerProductNumber, currentClass);
										JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										validProductNumber = false;
									}
								}
							}
							catch (final Throwable throwable)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Exception occured.", throwable,
										currentClass);
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Product model not found for given :  " + customerProductNumber, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								validProductNumber = false;
							}
						}
						else if (Jnjlab2bcoreConstants.Order.NEED_DELIVERY_DATE.equalsIgnoreCase(xmlStreamReader.getLocalName())
								&& validUom && validProductNumber && validShipTo && validSoldTo && needDeliveryDate)
						{
							if (null != elementValue && StringUtils.isNotEmpty(elementValue.trim()))
							{
								lineItem.setLineNumber(String.valueOf(lineValue));
								lineValue = lineValue + 10;
								lineCounter++;
								try
								{
									lineItemProcess = true;
									sdfForAliancaFile.setLenient(false);
									final Date givenDate = sdfForAliancaFile.parse(elementValue);
									final Date currentDate = sdfForAliancaFile.parse(sdfForAliancaFile.format(new Date()));
									if (currentDate.after(givenDate))
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Requested Delivery Date is less then todays date so updating date to current date",
												currentClass);
										final String expDelDateInString = sdfForSAPRequest.format(currentDate);
										lineItem.setExpectedDeliveryDate(expDelDateInString);
									}
									else
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Requested Delivery Date is in correct format and it is greater then equal to todays date.",
												currentClass);
										final String expDelDateInString = sdfForSAPRequest.format(givenDate);
										lineItem.setExpectedDeliveryDate(expDelDateInString);
									}

								}
								catch (final ParseException | IllegalArgumentException parseException)
								{
									JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Parse Exception Occured for the expected delivery date field" + elementValue, parseException,
											currentClass);
									final String expDelDateInString = sdfForSAPRequest
											.format(sdfForAliancaFile.parse(sdfForAliancaFile.format(new Date())));
									lineItem.setExpectedDeliveryDate(expDelDateInString);
								}
							}
						}
						else if (Jnjlab2bcoreConstants.Order.LINE.equals(xmlStreamReader.getLocalName()) && lineItemProcess)
						{
							lineItemList.add(lineItem);
						}
						else if (Jnjlab2bcoreConstants.Order.PURCHASE_ORDER.equals(xmlStreamReader.getLocalName()))
						{
							items.setLineItem(lineItemList);
							purchaseOrder.setItems(items);
						}
						else if (Jnjlab2bcoreConstants.Order.HEADER.equals(xmlStreamReader.getLocalName()))
						{
							purchaseOrder.setHeader(header);
						}
						break;
				}
				eventType = xmlStreamReader.next();
			} // end of while loop
			  // Close the XML Stream Reader and Input Stream.
			xmlStreamReader.close();
			inputStream.close();
			// if the errorDetailsList is not empty or the custProdNumStrBuilder or uomMultiplicityCheckBuilder or custUomStrBuilder string builder is not empty then populate the error message in errorDetailsList list.
			if (soldToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| shipToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, null, null, null, shipToStrBuilder, null,
						soldToStrBuilder, null, null, null, null, passedSHAModel.getUser());
			}
			else
			{
				// Done the split logic and file generation by calling the spiltLogicForOrdersWithFileGeneration method.
				splitLogicForOrdersWithFileGeneration(purchaseOrder, productNumberWithSalesOrg, fileListForSAP, passedSHAModels);
			}
			if (!errorDetailsList.isEmpty() || custProdNumStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| customerUomStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| expectedDeliveryDateStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, custProdNumStrBuilder, null, customerUomStrBuilder,
						null, null, null, expectedDeliveryDateStrBuilder, null, null, null, passedSHAModel.getUser());
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Business exception occured.", businessException,
					currentClass);

			errorDetailsList.add(businessException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IOException inputOutputException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Input Output exception occured.",
					inputOutputException, currentClass);
			errorDetailsList.add(inputOutputException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final XMLStreamException xmlStreamException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "XML Stream exception occured ", xmlStreamException,
					currentClass);
			errorDetailsList.add(xmlStreamException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final JAXBException jaxbException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "JAXB exception occured", jaxbException,
					currentClass);
			errorDetailsList.add(jaxbException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Illegal Argument exception occured",
					illegalArgumentException, currentClass);
			errorDetailsList.add(illegalArgumentException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final Throwable throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured.", currentClass);
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			if (actualLineCounter == lineCounter)
			{
				lineCounter = 0;
			}
		}

		if (actualLineCounter == lineCounter && lineCounter != 0 && fileStatus != Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS;
		}
		else if (lineCounter == 0)
		{
			if (errorDetailsList.isEmpty())
			{
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
		}
		else
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return fileStatus;

	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String createOrderFromSaoLuiz(final InputStream inputStream, final List<File> fileListForSAP,
			final List<String> errorDetailsList, final PurchaseOrder purchaseOrder,
			final List<JnjUploadOrderSHAModel> passedSHAModels)

	{
		final String methodName = "createOrderFromSaoLuiz";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		List<LineItem> lineItems = null;
		Items itemObject = null;
		LatamLineItem lineItem = null;
		Header header = null;
		final StringBuilder custProdNumStrBuilder = new StringBuilder();
		final StringBuilder soldToNumStrBuilder = new StringBuilder();
		final StringBuilder uomMultiplicityCheckBuilder = new StringBuilder();
		final StringBuilder expectedDeliveryDateStrBuilder = new StringBuilder();
		final Map<String, String> productNumberWithSalesOrg = new HashMap<>();
		final StringBuilder uomStrBuilder = new StringBuilder();
		final StringBuilder shipToStrBuilder = new StringBuilder();
		final StringBuilder indirectCutomerBuilder = new StringBuilder();
		boolean validSoldTo = true;
		boolean validShipTo = true;
		String fileStatus = null;
		boolean lineStatus=true;
		int lineNumber = 10;
		int lineCounter = 0;
		int actualLineCounter = 0;
		String expectedDeliveryDate = null;
		String shipToNumber = null;
		JnJB2BUnitModel jnjB2bUnitModel = null;
		try
		{
			//Creates a buffering character-input stream that uses a default-sized input buffer.
			final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
			// BufferedReader reads the line.
			String dataRow = null;
			lineItems = new ArrayList<>();
			itemObject = new Items();
			header = new Header();

			// Check the first line for header.
			String firstLine = bufferReader.readLine();
			firstLine = bufferReader.readLine();
			final String[] firstLineAray = firstLine.split(Jnjlab2bcoreConstants.Order.SEMI_COLON);
			if (null != firstLineAray && firstLineAray.length >= 1)
			{
				final String validSoldToNumber = firstLineAray[Jnjlab2bcoreConstants.Order.ZERO];
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Entered Sold To number : " + validSoldToNumber,
						currentClass);
				jnjB2bUnitModel = jnjOrderDao.fetchAllSoldToNumberForFile(validSoldToNumber);
				if (null == jnjB2bUnitModel)
				{
					jnjB2bUnitModel = fetchSoldFromCnpjValue(validSoldToNumber);
				}
				if (null != jnjB2bUnitModel)
				{
					header.setSoldToNumber(jnjB2bUnitModel.getUid());
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"Final Sold To number after CNPJ check is : " + jnjB2bUnitModel.getUid(), currentClass);
				}
				else
				{
					JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"Sold to Number : " + validSoldToNumber + " , is not valid", currentClass);
					JnjlatamOrderUtil.stringBuilderMethod(soldToNumStrBuilder, validSoldToNumber);
					validSoldTo = false;
					fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
				}
				shipToNumber = firstLineAray[Jnjlab2bcoreConstants.Order.ONE];
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Entered Ship To number : " + shipToNumber,
						currentClass);
				if (validSoldTo)
				{
					final String finalShipToNumber = foundConversion(shipToNumber, jnjB2bUnitModel);
					if (null != finalShipToNumber)
					{
						header.setShipToNumber(finalShipToNumber);
						JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
								"final Ship To Number After conversion is : " + finalShipToNumber, currentClass);
					}
					else
					{

						JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
								"Ship to Number : " + shipToNumber + " , is not valid", currentClass);
						JnjlatamOrderUtil.stringBuilderMethod(shipToStrBuilder, shipToNumber);
						validShipTo = false;
						fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
					}
				}
				if (StringUtils.isNotEmpty(String.valueOf(Jnjlab2bcoreConstants.Order.TWO)))
				{
					try
					{
						header.setpONumber(firstLineAray[Jnjlab2bcoreConstants.Order.TWO]);
						JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
								"Entered PO Number : " + firstLineAray[Jnjlab2bcoreConstants.Order.TWO], currentClass);
					}
					catch (final Throwable throwable)
					{
						header.setpONumber("");
					}
				}

			}
			// check Not null and not empty on the data row.
			while ((dataRow = bufferReader.readLine()) != null)
			{
				if (dataRow.isEmpty())
				{
					continue;
				}
				JnjUomDTO jnjUomDTO = null;
				boolean validProductNumber = true;
				boolean validUom = true;
				boolean invalidQty = true;
				boolean cmirNotExist = true;
				boolean validIndirectCustomer = true;
				final String[] dataArray = dataRow.split(Jnjlab2bcoreConstants.Order.SEMI_COLON);
				lineItem = new LatamLineItem();
				JnJProductModel jnJProductModel = null;
				if (null != dataArray && dataArray.length == Jnjlab2bcoreConstants.Order.SIX)
				{
					final String customerProductNumber = dataArray[Jnjlab2bcoreConstants.Order.ZERO];
					String uom = dataArray[Jnjlab2bcoreConstants.Order.TWO].trim();
					final String quantity = dataArray[Jnjlab2bcoreConstants.Order.ONE].trim();
					actualLineCounter++;
					if (validShipTo && validSoldTo)
					{
						// check it Customer Product Number is not Empty and null.
						if (StringUtils.isNotEmpty(customerProductNumber))
						{
							try
							{
								final LoadTranslationModel loadTranslationModel = jnjLoadTranslationService
										.getLoadTranslationModelFile(customerProductNumber, header.getSoldToNumber());
								// Check for the Load Translation Model object. Enters in the if block if it's not null.
								if (null != loadTranslationModel && null != loadTranslationModel.getProductId())
								{
									JnjGTCoreUtil.logInfoMessage(
											Logging.SUBMIT_ORDER_EDI, methodName, "CMIR Record found for given Customer Product number : "
													+ customerProductNumber + " and sold to number : " + header.getSoldToNumber(),
											currentClass);
									jnJProductModel = loadTranslationModel.getProductId();
									lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Product Catalog id  is : " + jnJProductModel.getCatalogId(), currentClass);
									if (StringUtils.isEmpty(uom))
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"uom is empty in input file for line : " + customerProductNumber, currentClass);
										if (null != loadTranslationModel.getCustomerUOM())
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"UOM is exist in CMIR so updating the value to CMIR uom value : "
															+ loadTranslationModel.getCustomerUOM(),
													currentClass);
											uom = loadTranslationModel.getCustomerUOM();
										}
										else
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"UOM is not exist in CMIR so updating the UOM to product sales UOM value : "
															+ jnJProductModel.getUnit().getCode(),
													currentClass);
											uom = jnJProductModel.getUnit().getCode();
										}
									}
									jnjUomDTO = processUomForSaoLuiz(lineItem, jnjUomDTO, uomStrBuilder, uom, loadTranslationModel,
											jnJProductModel, customerProductNumber);
									if (null == jnjUomDTO)
									{
										validUom = false;
									}

									if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
									{
										JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
												+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										validProductNumber = false;
									}

									cmirNotExist = false;
								}
								else if (cmirNotExist)
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"CMIR Record not found so fetch product model for given EAN number : " + customerProductNumber,
											currentClass);
									final List<JnJProductModel> jnJProductModelList = jnjLaProductService
											.getProductModelByEanProductNumber(customerProductNumber);
									if (null != jnJProductModelList && !jnJProductModelList.isEmpty())
									{
										jnJProductModel = jnJProductModelList.get(0);
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Product model from given EAN number is : " + jnJProductModel.getCatalogId(), currentClass);
										final LoadTranslationModel loadTranslationModelNew = jnjLoadTranslationService
												.getLoadTranslationModelByCatalogIdUnit(jnJProductModel, header.getSoldToNumber());
										lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
										if (StringUtils.isEmpty(uom))
										{
											JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"uom is empty in input file for line : " + customerProductNumber, currentClass);
											if (null != loadTranslationModelNew
													&& StringUtils.isNotEmpty(loadTranslationModelNew.getCustomerUOM()))
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"UOM is exist in CMIR so updating the value to CMIR uom value : "
																+ loadTranslationModelNew.getCustomerUOM(),
														currentClass);
												uom = loadTranslationModelNew.getCustomerUOM();
											}
											else
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"UOM is not exist in CMIR so updating the UOM to product sales UOM value : "
																+ jnJProductModel.getUnit().getCode(),
														currentClass);
												uom = jnJProductModel.getUnit().getCode();
											}
										}
										jnjUomDTO = processUomForSaoLuiz(lineItem, jnjUomDTO, uomStrBuilder, uom, loadTranslationModelNew,
												jnJProductModel, customerProductNumber);
										if (null == jnjUomDTO)
										{
											validUom = false;
										}
										if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
										{
											JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
													+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
											validProductNumber = false;
										}
									}
									else
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Product model for given EAN number : " + customerProductNumber
														+ "  is not found so treat customer number as product code and fetch the value",
												currentClass);

										jnJProductModel = jnjLaProductService.getProductByCatalogIdForAnyEcommerceFlag(
												customerProductNumber.toUpperCase(), jnjB2bUnitModel);
										if (null != jnJProductModel)
										{
											final LoadTranslationModel loadTranslationModelNew = jnjLoadTranslationService
													.getLoadTranslationModelByCatalogIdUnit(jnJProductModel, header.getSoldToNumber());
											lineItem.setProductNumber(jnJProductModel.getCatalogId().toUpperCase());
											if (StringUtils.isEmpty(uom))
											{
												JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
														"uom is empty in input file for line : " + customerProductNumber, currentClass);
												if (null != loadTranslationModelNew
														&& StringUtils.isNotEmpty(loadTranslationModelNew.getCustomerUOM()))
												{
													JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
															"UOM is exist in CMIR so updating the value to CMIR uom value : "
																	+ loadTranslationModelNew.getCustomerUOM(),
															currentClass);
													uom = loadTranslationModelNew.getCustomerUOM();
												}
												else
												{
													JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
															"UOM is not exist in CMIR so updating the UOM to product sales UOM value : "
																	+ jnJProductModel.getUnit().getCode(),
															currentClass);
													uom = jnJProductModel.getUnit().getCode();
												}
											}
											jnjUomDTO = processUomForSaoLuiz(lineItem, jnjUomDTO, uomStrBuilder, uom,
													loadTranslationModelNew, jnJProductModel, customerProductNumber);
											if (null == jnjUomDTO)
											{
												validUom = false;
											}
											if (!setSalesOrgCustomer(jnJProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
											{
												JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber
														+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
												fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
												validProductNumber = false;
											}
										}
										else
										{
											JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
													"Load Translation Model is not existed for this element " + customerProductNumber,
													currentClass);
											JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
											validProductNumber = false;
											fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										}
									}
								}
							}
							catch (final ModelNotFoundException modelNotFoundException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Not Found exception occured.",
										modelNotFoundException, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								validProductNumber = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
							catch (final ModelLoadingException modelLoadingException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Loading exception occured.",
										modelLoadingException, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								validProductNumber = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
							catch (final Throwable throwable)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Exception occured.", throwable,
										currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, customerProductNumber);
								validProductNumber = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
						}
						else
						{
							JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Customer Product Number is empty in the input file " + lineItem.getCustomerProductNumber(),
									currentClass);
							JnjlatamOrderUtil.stringBuilderMethod(custProdNumStrBuilder, "order.home.emptyKey");
							validProductNumber = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
					}
					// Changes For CP016 Starts
					// Check For the multiplicity of the entered quantity at the line level with the uom.
					if (validProductNumber && validShipTo && validSoldTo && validUom)
					{
						if (StringUtils.isNotEmpty(quantity) && (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO))
						{
							try
							{
								final float value = Integer.parseInt(quantity) * jnjUomDTO.getSalesUnitsCount();
								final float finalQty = Integer.parseInt(quantity) * ((JnjLaUomDTO) jnjUomDTO).getFinalUnitCount();

								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"quantity : " + quantity + " , sales unit Count : " + jnjUomDTO.getSalesUnitsCount()
												+ "final unit count : " + ((JnjLaUomDTO) jnjUomDTO).getFinalUnitCount(),
										currentClass);
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "value after multiplication is : "
										+ value + " , and final quantity after multiplication is : " + finalQty, currentClass);
								final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
								dfs.setDecimalSeparator('.');
								final DecimalFormat decimalFormat = new DecimalFormat("#.0000", dfs);
								final float finalvalue = Float.parseFloat(decimalFormat.format(value));
								final float finalQuantity = Float.parseFloat(decimalFormat.format(finalQty));

								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "fianl value after formating is : "
										+ finalvalue + " , and final quantity after formating is : " + finalQuantity, currentClass);
								if (finalvalue == Math.round(finalvalue))
								{
									if (finalQuantity == Math.round(finalQuantity))
									{
										lineItem.setQuantity(String.valueOf(finalQuantity));
									}
									else
									{
										JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Entered quantity : " + quantity + " is not the multiple of uom ", currentClass);
										JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder,
												customerProductNumber, quantity);
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										invalidQty = false;
									}
								}
								else
								{
									JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Entered quantity : " + quantity + " is not the multiple of uom ", currentClass);
									JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder,
											customerProductNumber, quantity);
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
									invalidQty = false;
								}
							}
							catch (final Throwable parseException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Entered quantity : " + quantity + " is not in correct format so sending number format exception ",
										currentClass);
								JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder,
										customerProductNumber, quantity);
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								invalidQty = false;
							}
						}
						else
						{
							JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Entered quantity : " + quantity + " is not the multiple of uom ", currentClass);
							JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder, customerProductNumber,
									"order.home.emptyKey");
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							invalidQty = false;
						}
					}
					// Changes For CP016 End
					final String indirectCustomer = dataArray[Jnjlab2bcoreConstants.Order.THREE];
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "indirectCustomer value from file is : "
							+ indirectCustomer + " for the customer product number is : " + customerProductNumber, currentClass);
					if (null != indirectCustomer && StringUtils.isNotEmpty(indirectCustomer))
					{
						final JnJB2BUnitModel jnJB2bUnitModel = passedSHAModel.getB2bUnitId();
						final CountryModel countryModel = jnJB2bUnitModel.getCountry();
						final JnjIndirectCustomerModel jnJIndirectCustomerModel = jnJCustomerDataService
								.getJnjInidrectCustomerByIDCountry(indirectCustomer, countryModel.getPk().toString());
						if (invalidQty && validSoldTo && validShipTo && validProductNumber && validUom)
						{
							if (null != jnJIndirectCustomerModel)
							{
								lineItem.setIndirectCustomer(jnJIndirectCustomerModel.getIndirectCustomer());
								lineCounter++;
							}
							else
							{

								JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Entered indirectCustomer : " + indirectCustomer + " ,is not valid ", currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(indirectCutomerBuilder, indirectCustomer);
								validIndirectCustomer = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
						}
					}
					else if (invalidQty && validSoldTo && validShipTo && validProductNumber && validUom)
					{
						lineCounter++;
					}
					expectedDeliveryDate = (dataArray[Jnjlab2bcoreConstants.Order.FOUR]);
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "ExpectedDeliveryDate from the file is :"
							+ expectedDeliveryDate + " for givem prroduct number line :" + customerProductNumber, currentClass);
					if (null != expectedDeliveryDate && StringUtils.isNotEmpty(expectedDeliveryDate))
					{
						if (validIndirectCustomer && invalidQty && validSoldTo && validShipTo && validProductNumber && validUom)
						{
							try
							{
								sdfForSaoLuizFile.setLenient(false);
								final Date givenDate = sdfForSaoLuizFile.parse(expectedDeliveryDate);
								final Date currentDate = sdfForSaoLuizFile.parse(sdfForSaoLuizFile.format(new Date()));
								if (currentDate.after(givenDate))
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Requested Delivery Date is less then todays date so updating date to current date",
											currentClass);
									final String expDelDateInString = sdfForSAPRequest.format(currentDate);
									lineItem.setExpectedDeliveryDate(expDelDateInString);
								}
								else
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Requested Delivery Date is in correct format and it is greater then equal to todays date.",
											currentClass);
									final String expDelDateInString = sdfForSAPRequest.format(givenDate);
									lineItem.setExpectedDeliveryDate(expDelDateInString);
								}
							}
							catch (final ParseException | IllegalArgumentException parseException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Parse Exception Occured for the expected delivery date field" + expectedDeliveryDate,
										currentClass);
								final String expDelDateInString = sdfForSAPRequest
										.format(sdfForSaoLuizFile.parse(sdfForSaoLuizFile.format(new Date())));
								lineItem.setExpectedDeliveryDate(expDelDateInString);
							}
							lineItem.setLineNumber(String.valueOf(lineNumber));
							lineItems.add(lineItem);
							// increase the line number by 10
							lineNumber = lineNumber + 10;
						}
					}
				}
				else
				{
					lineStatus=false;
					JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"Array Index Out of Bound Exception Occured or File Format is not valid " + dataRow, currentClass);
					final String errorFromMessageImpex = jnjLaMessageService.getMessageFromImpex("order.home.fileFormatInvalid", passedSHAModel.getUser());
					final String errorFromMessageImpexForDatarow = jnjLaMessageService.getMessageFromImpex("order.home.for.datarow", passedSHAModel.getUser());
					final String errorMessage = errorFromMessageImpex.concat(" " + errorFromMessageImpexForDatarow + " ")
							.concat(dataRow);
					errorDetailsList.add(errorMessage);
				}
			}
			itemObject.setLineItem(lineItems);
			purchaseOrder.setItems(itemObject);
			// set the value Purchase order number as it has been provided in input stream.
			purchaseOrder.setHeader(header);
			// close the buffer reader.
			bufferReader.close();
			// if the errorDetailsList is not empty or the custProdNumStrBuilder string builder is not empty or or the uomMultiplicityCheckBuilder string builder is not empty then populate the error message in errorDetailsList list.
			if (!errorDetailsList.isEmpty() || soldToNumStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| shipToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, null, null, null, shipToStrBuilder, null,
						soldToNumStrBuilder, null, null, null, null, passedSHAModel.getUser());
			}
			else
			{
				// Done the split logic and file generation by calling the spiltLogicForOrdersWithFileGeneration method.
				splitLogicForOrdersWithFileGeneration(purchaseOrder, productNumberWithSalesOrg, fileListForSAP, passedSHAModels);
			}
			if (!errorDetailsList.isEmpty() || custProdNumStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| uomMultiplicityCheckBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| expectedDeliveryDateStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| uomStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO
					|| indirectCutomerBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
			{
				JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, custProdNumStrBuilder, null, uomStrBuilder, null,
						uomMultiplicityCheckBuilder, null, expectedDeliveryDateStrBuilder, indirectCutomerBuilder, null, null,
						passedSHAModel.getUser());
			}
		}
		catch (final BusinessException businessException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Business exception occured.", businessException,
					currentClass);
			errorDetailsList.add(businessException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IOException inputOutputException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Input Output exception occured ",
					inputOutputException, currentClass);
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final JAXBException jaxbException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "JAXB exception occured.", jaxbException,
					currentClass);
			errorDetailsList.add(jaxbException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Illegal Argument exception occured.",
					illegalArgumentException, currentClass);
			errorDetailsList.add(illegalArgumentException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final Throwable throwable)
		{
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured.", throwable,
					currentClass);
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			if (actualLineCounter == lineCounter)
			{
				lineCounter = 0;
			}
		}
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "value of actualLineCounter is : " + actualLineCounter
				+ " and line counter is : " + lineCounter + " and file status is : " + fileStatus, currentClass);
		if (actualLineCounter == lineCounter && lineCounter != 0 && fileStatus != Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS && lineStatus)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS;
		}
		else if (lineCounter == 0 || !lineStatus)
		{
			if (errorDetailsList.isEmpty())
			{
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
		}
		else
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return fileStatus;
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public String createOrderFromEanLayOut(final InputStream inputStream, final List<File> fileListForSAP,
			final List<String> errorDetailsList, PurchaseOrder purchaseOrder, final List<PurchaseOrder> puchaseOrderList,
			final List<JnjUploadOrderSHAModel> passedSHAModels)
	{
		final String methodName = "createOrderFromEanLayOut()";
		final String skippingProduct = " Skipping this product.";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		int counterSequenceNumber;
		List<LineItem> lineItems = null;
		Items itemObject = null;
		LineItem lineItem = null;
		Header header = null;
		String fileStatus = Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS;
		Map<String, String> productNumberWithSalesOrg = null;
		List<JnJProductModel> jnjProductModelList = null;
		JnJProductModel jnjProductModel = null;
		boolean validSoldTo = true;
		boolean validDate = true;
		int actualLineCounter = 0;
		int lineCounter = 0;
		StringBuilder eanNumStrBuilder = null;
		StringBuilder soldToStrBuilder = null;
		StringBuilder uomMultiplicityCheckBuilder = null;
		StringBuilder requestedDeliveryDateBuilder = null;
		String cnpjNumberN = Strings.EMPTY;
		StringBuilder requestedDeliveryDateFormatBuilder = null;
		String cnpjNumberY = Strings.EMPTY;
		final SimpleDateFormat formatter = new SimpleDateFormat(Jnjlab2bcoreConstants.Order.REQUESTED_DELIVERY_DATE_FORMATTER);
		final SimpleDateFormat formatterFinal = new SimpleDateFormat(
				Jnjlab2bcoreConstants.Order.REQUESTED_DELIVERY_DATE_FORMATTER_FINAL);
		int lineNumber = 10;
		boolean isPanarello = false;
		boolean newOrder = false;
		try
		{
			final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
			productNumberWithSalesOrg = new HashMap<>();
			// To get the first row from the buffer reader.
			String dataRow = bufferReader.readLine();
			while (StringUtils.isNotEmpty(dataRow))
			{
				JnjUomDTO jnjUomDTO = null;
				boolean validEanProductNumber = true;
				boolean validUomQty = true;
				final String orderSubString = dataRow.substring(Jnjlab2bcoreConstants.Order.ZERO, Jnjlab2bcoreConstants.Order.TWO)
						.trim();
				// Check the first two element of the data row if its value is 01 then the value contains in it would be set in header object.
				if (StringUtils.isNotEmpty(orderSubString)
						&& Jnjlab2bcoreConstants.Order.ZERO_ONE_STRING.equalsIgnoreCase(orderSubString))
				{
					/* Start 35100:: On WebEDI, for EAN file enable multiple orders per file. */
					if (newOrder)
					{
						purchaseOrder = processPreviousOrder(lineItems, itemObject, purchaseOrder, newOrder, errorDetailsList,
								eanNumStrBuilder, uomMultiplicityCheckBuilder, soldToStrBuilder, requestedDeliveryDateBuilder,
								requestedDeliveryDateFormatBuilder, productNumberWithSalesOrg, fileListForSAP, puchaseOrderList,
								passedSHAModels);
					}
					lineItems = new ArrayList<>();
					itemObject = new Items();
					lineNumber = 10;
					soldToStrBuilder = new StringBuilder();
					requestedDeliveryDateBuilder = new StringBuilder();
					uomMultiplicityCheckBuilder = new StringBuilder();
					eanNumStrBuilder = new StringBuilder();
					requestedDeliveryDateFormatBuilder = new StringBuilder();
					/* End 35100:: On WebEDI, for EAN file enable multiple orders per file. */
					final String sequenceNumber = jnjConfigService.getConfigValueById(Jnjlab2bcoreConstants.SEQUENCE_NUMBER);
					if (sequenceNumber == null)
					{
						final JnjConfigModel jnjConfigModel = modelService.create(JnjConfigModel.class);
						jnjConfigModel.setId("poNumberSequence");
						jnjConfigModel.setDescription("Sequence number which will append in po number in Web EDI");
						jnjConfigModel.setValue("000001");
						modelService.save(jnjConfigModel);
						counterSequenceNumber = Integer.parseInt("000001");
					}
					else
					{
						counterSequenceNumber = Integer.parseInt(sequenceNumber);
					}

					try
					{
						final String poNumber = dataRow.substring(Jnjlab2bcoreConstants.Order.TWO,
								Jnjlab2bcoreConstants.Order.PO_END_INDEX);
						final String finalPONumber = poNumber.trim() + Jnjlab2bcoreConstants.UNDERSCORE_SYMBOL
								+ Jnjlab2bcoreConstants.PO_NUMBER_STRING + String.format("%06d", Integer.valueOf(counterSequenceNumber));
						counterSequenceNumber++;
						jnjConfigService.saveConfigValues(Jnjlab2bcoreConstants.SEQUENCE_NUMBER, String.valueOf(counterSequenceNumber));
						header = new Header();
						header.setpONumber(finalPONumber);
						// To retrieve the default b2b unit.
						final JnJB2BUnitModel jnjB2bUnitModel = passedSHAModels.get(0).getB2bUnitId();
						final Set<PrincipalGroupModel> tmpDetail = jnjB2bUnitModel.getGroups();
						final JnJCompanyModel jnjCompanyModel = jnjCompanyService
								.getMasterCompanyForUid(Jnjlab2bcoreConstants.PANARELLO);
						for (final PrincipalGroupModel principalGroupModel : tmpDetail)
						{
							if (null != jnjCompanyModel
									&& StringUtils.equalsIgnoreCase(jnjCompanyModel.getUid(), principalGroupModel.getUid()))
							{
								isPanarello = true;
								break;
							}
						}
						if (isPanarello)
						{
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Account is aligned with panarello branch", currentClass);
							cnpjNumberY = dataRow.substring(Jnjlab2bcoreConstants.Order.SOLD_TO_NUMBER_START_INDEX,
									Jnjlab2bcoreConstants.Order.SOLD_TO_NUMBER_END_INDEX).trim();
							validSoldTo = processHeader(header, validSoldTo, soldToStrBuilder, cnpjNumberY);
						}
						else
						{
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Account is not aligned with panarello branch", currentClass);
							cnpjNumberN = dataRow.substring(Jnjlab2bcoreConstants.Order.SHIP_TO_NUMBER_START_INDEX,
									Jnjlab2bcoreConstants.Order.SHIP_TO_NUMBER_END_INDEX).trim();
							validSoldTo = processHeader(header, validSoldTo, soldToStrBuilder, cnpjNumberN);
						}
						if (validSoldTo)
						{
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Found valid SOLD TO number from given CNPJ", currentClass);
							String requestedDeliveryDate = null;
							try
							{
								requestedDeliveryDate = dataRow.substring(Jnjlab2bcoreConstants.Order.PO_END_INDEX,
										Jnjlab2bcoreConstants.Order.EAN_PRODUCT_NUMBER_END_INDEX).trim();

								final Date givenDate = formatter.parse(requestedDeliveryDate);
								final Date currentDate = formatter.parse(formatter.format(new Date()));
								if (currentDate.after(givenDate))
								{
									validDate = true;
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Requested Delivery Date is less then todays date so updating the date to current date",
											currentClass);
									final String requestedDeliveryDateFinal = formatterFinal.format(currentDate);
									header.setRequestedDeliveryDate(requestedDeliveryDateFinal);
								}
								else
								{
									validDate = true;
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Requested Delivery Date is in correct format and it is greater then equal to todays date.",
											currentClass);
									final String requestedDeliveryDateFinal = formatterFinal.format(givenDate);
									header.setRequestedDeliveryDate(requestedDeliveryDateFinal);
								}
							}
							catch (final ParseException | IllegalArgumentException parseException)
							{
								JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"Parse Exception Occured for the expected delivery date field" + elementValue, parseException,
										currentClass);
								final String requestedDeliveryDateFinal = formatterFinal
										.format(formatter.parse(formatter.format(new Date())));
								header.setRequestedDeliveryDate(requestedDeliveryDateFinal);
								validDate = true;
							}
						}
						else
						{
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
						}

					}
					catch (final Throwable throwable)
					{
						JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured.", throwable,
								currentClass);
						JnjlatamOrderUtil.stringBuilderMethod(soldToStrBuilder, "order.home.emptyKey");
						fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
						validSoldTo = false;
					}
					purchaseOrder.setHeader(header);
				}
				// Check the first two element of the data row if its value is 03 then the value contains in it would be set in Line Item object.
				else if (StringUtils.isNotEmpty(orderSubString)
						&& Jnjlab2bcoreConstants.Order.ZERO_THREE_STRING.equalsIgnoreCase(orderSubString))
				{
					if (validDate && validSoldTo)
					{
						actualLineCounter++;
						if (!newOrder)
						{
							newOrder = true;
						}
						JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
								"Sold To Number and Rquested delivery date is correct so start reading line ", currentClass);
						lineItem = new LineItem();
						lineItem.setLineNumber(String.valueOf(lineNumber));
						// trim the retrieve value to remove the space if its exist in the value.
						String eanProductNumber = null;
						try
						{
							eanProductNumber = dataRow.substring(Jnjlab2bcoreConstants.Order.EIGHT,
									Jnjlab2bcoreConstants.Order.EAN_PRODUCT_NUMBER_START_INDEX).trim();
							if (StringUtils.isNotEmpty(eanProductNumber))
							{
								// Call the Jnj Product Service to retrieve the List of Active Jnj Product Model on the basis of EAN Product Number.
								jnjProductModelList = jnjLaProductService.getProductModelByEanProductNumber(eanProductNumber,
										passedSHAModel.getB2bUnitId());
								if (null != jnjProductModelList && !jnjProductModelList.isEmpty())
								{
									// Fix for defect no. 30699
									jnjProductModel = jnjProductModelList.get(Jnjlab2bcoreConstants.Order.ZERO);
									lineItem.setProductNumber(jnjProductModel.getCatalogId().toUpperCase());
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Jnj Product Model found" + jnjProductModel.getCatalogId().toUpperCase()
													+ " for given EAN Product Number  :" + eanProductNumber,
											currentClass);
									jnjUomDTO = jnjLoadTranslationService.getCustDelUomEDIMappingForEAN(jnjProductModel,
											header.getSoldToNumber());

									// set the product number as key and Sales Org as value in a map.
									final JnJB2BUnitModel jnjB2bUnitModel = jnjOrderDao
											.fetchAllSoldToNumberForFile(header.getSoldToNumber());
									if (!setSalesOrgCustomer(jnjProductModel, jnjB2bUnitModel, productNumberWithSalesOrg, lineItem))
									{
										JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, eanProductNumber
												+ jnjLaMessageService.getMessageFromImpex(salesOrgError, passedSHAModel.getUser()));
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
										validEanProductNumber = false;
									}
								}
								else
								{
									JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Jnj Product Model is not existed for this element " + "having EAN number " + eanProductNumber
													+ skippingProduct,
											currentClass);

									JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, eanProductNumber);
									validEanProductNumber = false;
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								}
							}
							else
							{
								JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"EAN is empty in the file for the line number" + eanProductNumber + skippingProduct, currentClass);
								JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, "order.home.emptyKey");
								validEanProductNumber = false;
								fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
							}
						}
						catch (final ModelNotFoundException modelNotFoundException)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Model Not Found exception occured." + skippingProduct, modelNotFoundException, currentClass);
							JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, eanProductNumber);
							validEanProductNumber = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
						catch (final ModelLoadingException modelLoadingException)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Model Loading exception occured." + skippingProduct, modelLoadingException, currentClass);
							JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, eanProductNumber);
							validEanProductNumber = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
						catch (final Throwable throwable)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Throwable exception occured." + skippingProduct, throwable, currentClass);
							JnjlatamOrderUtil.stringBuilderMethod(eanNumStrBuilder, "order.home.emptyKey");
							validEanProductNumber = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
						String quantity = null;
						try
						{
							quantity = dataRow.substring(Jnjlab2bcoreConstants.Order.EAN_PRODUCT_NUMBER_END_INDEX,
									Jnjlab2bcoreConstants.Order.QUANTITY_START_INDEX).trim();
							// Check For the multiplicity of the entered quantity at the line level with the uom.
							if (validEanProductNumber)
							{
								if (StringUtils.isNotEmpty(quantity) && null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
								{
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Given queantity in file : " + quantity, currentClass);
									final int quantityInt = Integer.parseInt(quantity);
									final float value = quantityInt * jnjUomDTO.getSalesUnitsCount();
									final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
									dfs.setDecimalSeparator('.');
									final DecimalFormat decimalFormat = new DecimalFormat("#.0000", dfs);
									final float finalvalue = Float.parseFloat(decimalFormat.format(value));
									JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Quantity after parse : "
											+ quantityInt + " , final value after multiplicty check : " + finalvalue, currentClass);
									if (finalvalue == Math.round(finalvalue))
									{
										JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"final value " + finalvalue + "is interger after multiplicty check.", currentClass);
										lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase());
										lineItem.setQuantity(String.valueOf(quantityInt));
										lineCounter++;
										lineNumber = lineNumber + 10;
										lineItems.add(lineItem);
									}
									else
									{
										JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName,
												"Entered quantity is not the multiple of uom " + quantityInt + skippingProduct, currentClass);

										JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder,
												eanProductNumber, String.valueOf(quantityInt));
										validUomQty = false;
										fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
									}
								}
								else
								{
									JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName,
											"Invalid quantity: " + quantity + " or product: " + eanProductNumber
													+ " does not have a entry in uomDetails list matching its deliveryUnitOfMeasure"
													+ skippingProduct,
											currentClass);
									JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder,
											eanProductNumber, quantity);
									validUomQty = false;
									fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
								}
							}
							else
							{
								JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
										"EAN product number is not valid, so not comparing quantity against unit of measure", currentClass);
							}
						}
						catch (final IllegalArgumentException illegalArgumentException)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Illegal Argument exception occured." + skippingProduct, illegalArgumentException, currentClass);
							JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder, eanProductNumber,
									quantity);
							validUomQty = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
						catch (final Throwable throwable)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"Throwable exception occured." + skippingProduct, throwable, currentClass);
							JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomMultiplicityCheckBuilder, eanProductNumber,
									quantity);
							validUomQty = false;
							fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
						}
					}
					else
					{
						if (!newOrder)
						{
							newOrder = true;
						}
					}
				}
				// To read the next line.
				dataRow = bufferReader.readLine();
			} //End of While

			/* Start 35100:: On WebEDI, Processing the last order. */
			if (newOrder)
			{
				purchaseOrder = processPreviousOrder(lineItems, itemObject, purchaseOrder, newOrder, errorDetailsList,
						eanNumStrBuilder, uomMultiplicityCheckBuilder, soldToStrBuilder, requestedDeliveryDateBuilder,
						requestedDeliveryDateFormatBuilder, productNumberWithSalesOrg, fileListForSAP, puchaseOrderList,
						passedSHAModels);
			}
			/* End 35100:: On WebEDI, Processing the last order. */
			bufferReader.close();
		}
		catch (final IOException inputOutputException)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Input Output exception occured.",
					inputOutputException, currentClass);
			errorDetailsList.add(inputOutputException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final IllegalArgumentException illegalArgumentException)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Illegal Argument exception occured.",
					illegalArgumentException, currentClass);
			errorDetailsList.add(illegalArgumentException.getMessage());
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		catch (final Throwable throwable)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Throwable exception occured.", throwable,
					currentClass);
			JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
		}
		if (actualLineCounter == lineCounter && lineCounter != 0 && fileStatus != Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS)
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.SUCCESS_STATUS;
		}
		else if (lineCounter == 0)
		{
			if (errorDetailsList.isEmpty())
			{
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS;
		}
		else
		{
			fileStatus = Jnjlab2bcoreConstants.UploadOrder.PARTIAL_SUCCESS;
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return fileStatus;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	public boolean sftpCallAndMoveFileToZipFolder(final List<File> fileListForSAP, final List<String> errorDetails)
	{
		final String methodName = "sftpCallAndMoveFileToZipFolder()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		boolean uploadStatus = false;
		//To retrieve the remote File Path from config.
		final String remoteFilePath = Config
				.getParameter(Jnjlab2bcoreConstants.UploadFile.SFTP_DESTINATION_SUBMIT_ORDER_EDI_FOLDER);

		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
				fileListForSAP + " Files are moving from Shared Folder Location to SFTP folder location " + remoteFilePath,
				currentClass);
		
		//Calling JnjSftpFileTransferUtil class method to save file to SFTP folder
				
		final boolean useRetryLogic = Config.getBoolean(Jnjlab2bcoreConstants.Order.USE_RETRY_LOGIC, false);
		
		if(useRetryLogic)
		{
			final JnjSftpFileTransferUtil jnjLaSftpFileTransferUtil = new JnjLaSftpFileTransferUtil();
			uploadStatus = jnjLaSftpFileTransferUtil.uploadMultipleFileToSftp(fileListForSAP, remoteFilePath,
					Jnjlab2bcoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI);
		}
		else
		{
			final JnjSftpFileTransferUtil jnjSftpFileTransferUtil = new JnjSftpFileTransferUtil();
			uploadStatus = jnjSftpFileTransferUtil.uploadMultipleFileToSftp(fileListForSAP, remoteFilePath,
					Jnjlab2bcoreConstants.UploadFile.SFTP_CONNECTION__TYPE_SUBMITEDI);
		}
				
		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
				"After getting response from the SFTP File Upload Util class, Upload status: "+uploadStatus, currentClass);
		// if the uploadStatus is true then delete the file from the shared folder.
		if (uploadStatus)
		{
			for (final File file : fileListForSAP)
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"FILE  is successfully placed to SFTP" + file.getName(), currentClass);
				uploadStatus = JnJXMLFilePicker.zipAndMoveFile(file, Jnjlab2bcoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
						Jnjlab2bcoreConstants.FEEDS_ARCHIVE_FOLDER_NAME, false);
			}
		}
		else
		{

			for (final File file : fileListForSAP)
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "File Place to SFTP folder fail" + file,
						currentClass);
				JnJXMLFilePicker.zipAndMoveFile(file, Jnjlab2bcoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER,
						Jnjlab2bcoreConstants.FEEDS_ERROR_FOLDER_NAME, false);
			}

			errorDetails.add(Jnjlab2bcoreConstants.UploadOrder.SFTP_ERROR);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return uploadStatus;
	}


	/**
	 * This method is used to find the conversion for given ship to id
	 *
	 * @param shipToNumber
	 */
	private String foundConversion(final String shipToNumber, final JnJB2BUnitModel jnJB2BUnitModel)
	{
		final String methodName = "foundConversion ()";

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		String convertedShipToNumber = null;
		final AddressModel addressModel = jnjOrderDao.foundConversion(shipToNumber, jnJB2BUnitModel);
		if (null != addressModel)
		{
			JnjGTCoreUtil.logInfoMessage(
					Logging.SUBMIT_ORDER_EDI, methodName, "Found a conversion :" + addressModel.getJnJAddressId()
							+ " ,for given shipToNumber : " + shipToNumber + "and given JnjB2bUnit model : " + jnJB2BUnitModel.getUid(),
					currentClass);
			convertedShipToNumber = addressModel.getJnJAddressId();
		}
		if (null == convertedShipToNumber && null != jnJB2BUnitModel)
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI,
					methodName, "conversion for given shipToNumber : " + shipToNumber + "and given JnjB2bUnit model : "
							+ jnJB2BUnitModel.getUid() + " is not present so check for original ship to	is valid for Sold to ",
					currentClass);
			final List<AddressModel> addressModelList = jnjOrderDao.fetchShipToAddress(jnJB2BUnitModel);
			final List<String> shipToList = new ArrayList<>();
			String shipToValue = null;
			if (null != addressModelList && !addressModelList.isEmpty())
			{
				for (int addressId = 0; addressId < addressModelList.size(); addressId++)
				{
					shipToValue = addressModelList.get(addressId).getJnJAddressId();
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"ship to value from Address model is : " + shipToValue, currentClass);
					shipToList.add(shipToValue);
				}
				if (shipToList.contains(shipToNumber))
				{
					convertedShipToNumber = shipToNumber;
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"final Converted Ship To number : " + convertedShipToNumber, currentClass);
				}
				else
				{
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "final Converted Ship To number is not present",
							currentClass);
				}
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return convertedShipToNumber;
	}

	/**
	 * This method is used to populate the hybris related data in Purchase order object and also set the Sales Org for an
	 * Order on the basis of split logic.
	 *
	 * @param purchaseOrder
	 *           the purchase order
	 * @throws JAXBException
	 * @throws BusinessException
	 */

	private boolean splitLogicForOrdersWithFileGeneration(final PurchaseOrder purchaseOrder,
			final Map<String, String> productNumberWithSalesOrg, final List<File> fileListForSAP,
			final List<JnjUploadOrderSHAModel> passedSHAModels) throws JAXBException, BusinessException
	{
		final String methodName = "spiltLogicForOrdersWithFileGeneration()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		Items itemsListExisted = null;
		boolean fileCreated = false;

		final Map<String, PurchaseOrder> purchaseOrderMapWithSalesOrg = new HashMap<>();

		if (null != purchaseOrder && null != purchaseOrder.getItems() && !purchaseOrder.getItems().getLineItem().isEmpty()
				&& null != productNumberWithSalesOrg)
		{

			for (final LineItem lineItem : purchaseOrder.getItems().getLineItem())
			{
				if (productNumberWithSalesOrg.containsKey(lineItem.getProductNumber()))
				{
					final String salesOrgWithOrderType = productNumberWithSalesOrg.get(lineItem.getProductNumber());

					if (!purchaseOrderMapWithSalesOrg.isEmpty() && purchaseOrderMapWithSalesOrg.containsKey(salesOrgWithOrderType))
					{
						itemsListExisted = purchaseOrderMapWithSalesOrg.get(salesOrgWithOrderType).getItems();
						itemsListExisted.getLineItem().add(lineItem);
						purchaseOrderMapWithSalesOrg.get(salesOrgWithOrderType).setItems(itemsListExisted);
					}
					else
					{
						final PurchaseOrder currentPurchaseOrder = new PurchaseOrder();
						Header header = new Header();
						final List<LineItem> lineItemList = new ArrayList<>();
						final Items items = new Items();
						// Not null check on the Header object as it would be null in case of Sao Luiz file.
						if (null != purchaseOrder.getHeader())
						{
							final Header headerRetrieved = purchaseOrder.getHeader();
							// To avoid the same reference.
							header = headerRetrieved.clone();
						}
						final String[] salesOrgWithOrderTypeArray = salesOrgWithOrderType
								.split(Jnjlab2bcoreConstants.Order.PIPE_STRING_SEPARATOR);
						header.setVendorID(salesOrgWithOrderTypeArray[0]);
						currentPurchaseOrder.setHeader(header);
						lineItemList.add(lineItem);
						items.setLineItem(lineItemList);
						currentPurchaseOrder.setItems(items);
						purchaseOrderMapWithSalesOrg.put(salesOrgWithOrderType, currentPurchaseOrder);
					}
				}
			}
		}

		// check if the map which contains SalesOrg as key and Purchase Order object as its value is not empty.
		if (!purchaseOrderMapWithSalesOrg.isEmpty())
		{
			passedSHAModel.setXmlFileCount(Integer.valueOf(purchaseOrderMapWithSalesOrg.size()));
			for (final Map.Entry<String, PurchaseOrder> mapEntries : purchaseOrderMapWithSalesOrg.entrySet())
			{
				final PurchaseOrder finalPurchaseOrder = mapEntries.getValue();
				final String[] specialProductType = mapEntries.getKey().split(Jnjlab2bcoreConstants.Order.PIPE_STRING_SEPARATOR);
				// call the generate file method where the file is created in shared folder and then move to sftp location.
				fileCreated = generateXmlOrderFile(finalPurchaseOrder, fileListForSAP, specialProductType[1], passedSHAModels);
			}
		}
		else
		{
			updateUploadOrderSHADetails(passedSHAModel, null, RecordStatus.ERROR);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return fileCreated;
	}

	/**
	 * @param header
	 * @param invalidSoldTo
	 * @param soldToStrBuilder
	 * @param cnpjNumberY
	 * @return invalidSoldTo
	 */
	private boolean processHeader(final Header header, boolean invalidSoldTo, final StringBuilder soldToStrBuilder,
			final String cnpjNumberY)
	{
		final String methodName = "processHeader()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		if (StringUtils.isNotEmpty(cnpjNumberY))
		{
			final JnJB2BUnitModel jnJB2BUnitModelNew = fetchSoldFromCnpjValue(cnpjNumberY);
			if (null != jnJB2BUnitModelNew)
			{
				header.setSoldToNumber(jnJB2BUnitModelNew.getUid());
				header.setShipToNumber(jnJB2BUnitModelNew.getUid());
				invalidSoldTo = true;
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"Sold To Number : " + cnpjNumberY + " is not valid", currentClass);
				JnjlatamOrderUtil.stringBuilderMethod(soldToStrBuilder, cnpjNumberY);
				invalidSoldTo = false;
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Sold To Number is empty in the input file " + cnpjNumberY, currentClass);
			JnjlatamOrderUtil.stringBuilderMethod(soldToStrBuilder, cnpjNumberY);
			invalidSoldTo = false;
		}
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return invalidSoldTo;
	}

	/**
	 * The generateFile method generates the file at the particular location by parsing the Purchase Order object and
	 * then transfer the file to sftp location.
	 *
	 * @param purchaseOrder
	 *           the purchase order
	 * @return true, if successful
	 * @throws JAXBException
	 * @throws BusinessException
	 */
	private boolean generateXmlOrderFile(final PurchaseOrder purchaseOrder, final List<File> fileListForSAP,
			final String specialOrderType, final List<JnjUploadOrderSHAModel> passedSHAModels)
			throws JAXBException, BusinessException
	{
		final String methodName = "generateXmlOrderFile()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		JAXBContext jaxbContext;
		Marshaller marshaller;
		boolean uploadStatus;

		// Not null check for the purchase order object
		if (null != purchaseOrder && null != purchaseOrder.getHeader() && null != purchaseOrder.getHeader().getVendorID())
		{
			// To retrieve the shared folder location from config.
			final String sharedFolder = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER);

			final String salesOrg = purchaseOrder.getHeader().getVendorID();

			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Sales Org-" + salesOrg + " and Special Order Type-" + specialOrderType, currentClass);
			// Current time of the system.
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Order.DATE_FORMAT_FOR_FILE_NAME);
			final Date date = new Date();

			// Form the file name for the different xml file which has been generated at different time duration.
			final String fileName = Order.FILE_NAME_INITAL.concat(Order.UNDER_SCORE).concat(salesOrg).concat(Order.UNDER_SCORE)
					.concat(specialOrderType).concat(Order.UNDER_SCORE).concat(simpleDateFormat.format(date))
					.concat(Order.XML_FILE_NAME_EXTENSION);

			updateUploadOrderSHADetails(passedSHAModel, fileName, RecordStatus.SUCCESS);

			final String localFilePath = sharedFolder + fileName;
			final File file = new File(localFilePath);
			jaxbContext = JAXBContext.newInstance(PurchaseOrder.class);
			marshaller = jaxbContext.createMarshaller();
			marshaller.marshal(purchaseOrder, file);
			fileListForSAP.add(file);
			uploadStatus = true;

			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					fileName + " File is created in Shared Folder Location " + sharedFolder + " File Name: "+ fileName, currentClass);
		}
		else
		{
			updateUploadOrderSHADetails(passedSHAModel, null, RecordStatus.ERROR);
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"Purchase Order object is null or the Purchase Order Object doesn't contains the Sales Org", currentClass);

			throw new BusinessException("Purchase Order object is null or the Purchase Order Object doesn't contains the Sales Org",
					MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return uploadStatus;
	}


	/**
	 * This method is used to b2b unit model for given cnpj value
	 *
	 * @param logicalId
	 * @return List<JnJB2BUnitModel> jnJB2BUnitModelLisst
	 */
	private JnJB2BUnitModel fetchSoldFromCnpjValue(final String logicalId)
	{
		final String methodName = "fetchSoldFromCnpjValue()";
		final List<JnJLaB2BUnitModel> jnJB2BUnitModelList = jnjOrderDao.fetchB2BUnitForCnpj(logicalId);

		if (null != jnJB2BUnitModelList && !jnJB2BUnitModelList.isEmpty())
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"JnJ B2B Unit Model List found for given CNPJ : " + logicalId + " , so returning : " + jnJB2BUnitModelList.get(0),
					currentClass);
			return jnJB2BUnitModelList.get(0);
		}

		JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
				"JnJ B2B Unit Model List not  for given CNPJ : " + logicalId + " , so returning null ", currentClass);
		return null;

	}

	/**
	 * This method is used to fetch UOM for Alianca file
	 *
	 * @param lineItem
	 * @param loadTranslationModel
	 * @param jnjUomDTO
	 * @param uomField
	 * @param jnJProductModel
	 * @return JnjUomDTO
	 */
	private JnjUomDTO processUOMForAlianca(final LineItem lineItem, final LoadTranslationModel loadTranslationModel,
			JnjUomDTO jnjUomDTO, final String uomField, final JnJProductModel jnJProductModel)
	{
		final String methodName = "processUOMForAlianca ()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		if (StringUtils.isNotEmpty(uomField))
		{
			String convertedUomField = null;
			if (null != loadTranslationModel && null != loadTranslationModel.getB2bUnit())
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"value of uom field before conversion is : " + uomField + " ,Converting uom field from CMIR conversion model",
						currentClass);
				convertedUomField = fetchConvertedUomFromCmirConversion(loadTranslationModel.getB2bUnit(), uomField);
				if (StringUtils.isEmpty(convertedUomField) || null == convertedUomField)
				{
					JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
							"there is no conversion define for given uom filed :" + uomField + ", so retrurning  input uom field",
							currentClass);
					convertedUomField = uomField;
				}
			}
			if (null != loadTranslationModel && null != loadTranslationModel.getCustomerUOM() && null != convertedUomField
					&& convertedUomField.equalsIgnoreCase(loadTranslationModel.getCustomerUOM()))
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"UOM value : " + convertedUomField + " EXIST in CMIR model", currentClass);
				jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForAlianca(jnJProductModel, loadTranslationModel);
			}
			else
			{
				JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
						"UOM value : " + uomField + " NOT EXIST in CMIR model SO checking in material master", currentClass);
				if (null != jnJProductModel.getUomDetails() && StringUtils.isNotEmpty(convertedUomField))
				{
					for (final JnjUomConversionModel jnjUomConvModel : jnJProductModel.getUomDetails())
					{
						if (uomField.equalsIgnoreCase(jnjUomConvModel.getUOM().getCode()) && null != jnjUomConvModel.getNumerator())
						{
							JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
									"UOM value : " + uomField + " exist in material master", currentClass);
							jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForAliancaMM(jnJProductModel,
									jnjUomConvModel.getNumerator());
							break;
						}
					}
				}
			}

			if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
			{
				lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode());
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Entered UOM is not valid ", currentClass);
			}
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Entered UOM is not valid, it can not be null ",
					currentClass);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return jnjUomDTO;
	}

	/**
	 * This method is used to fetch uom for sao luiz file
	 *
	 * @param lineItem
	 * @param jnjUomDTO
	 * @param uomStrBuilder
	 * @param uom
	 * @param loadTranslationModel
	 * @param jnJProductModel
	 * @param cutomerProductNumber
	 * @return JnjUomDTO
	 */
	private JnjUomDTO processUomForSaoLuiz(final LineItem lineItem, JnjUomDTO jnjUomDTO, final StringBuilder uomStrBuilder,
			String uom, final LoadTranslationModel loadTranslationModel, final JnJProductModel jnJProductModel,
			final String cutomerProductNumber)
	{
		final String methodName = "processUomForSaoLuiz()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final String actualUom = uom;
		if (uom.length() > 3)
		{
			uom = fetchConvertedUomFromCmirConversion(loadTranslationModel.getB2bUnit(), uom);
		}
		Integer receiveNumerator = null;
		if (null != loadTranslationModel && null != loadTranslationModel.getCustomerUOM() && null != uom
				&& uom.equalsIgnoreCase(loadTranslationModel.getCustomerUOM()))
		{
			JnjGTCoreUtil.logInfoMessage(Logging.SUBMIT_ORDER_EDI, methodName,
					"LOAD TRANSLATION MODEL  is present for given product code : " + jnJProductModel.getCatalogId()
							+ " and given sold to number : " + loadTranslationModel.getB2bUnit().getUid(),
					currentClass);
			receiveNumerator = loadTranslationModel.getNumerator();
			jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForSaoLuiz(jnJProductModel, loadTranslationModel);
			// set the product number as key and Sales Org as value in a map.
			jnjLoadTranslationService.getCustDelUomMappingByRoundigForSaoLuiz(jnJProductModel, loadTranslationModel, jnjUomDTO,
					receiveNumerator);
		}
		else
		{
			JnjGTCoreUtil.logInfoMessage(
					Logging.SUBMIT_ORDER_EDI, methodName, "LOAD TRANSLATION MODEL  is NOT present for given product code : "
							+ jnJProductModel.getCatalogId() + " and given sold to number  , so checking the value in material master ",
					currentClass);
			if (null != jnJProductModel.getUomDetails())
			{
				for (final JnjUomConversionModel jnjUomConvModel : jnJProductModel.getUomDetails())
				{
					//
					if (null != uom && uom.equalsIgnoreCase(jnjUomConvModel.getUOM().getCode())
							&& null != jnjUomConvModel.getNumerator())
					{
						receiveNumerator = jnjUomConvModel.getNumerator();
						jnjUomDTO = jnjLoadTranslationService.getCustDelUomMappingForSaoLuizMM(jnJProductModel, receiveNumerator);
						// set the product number as key and Sales Org as value in a map.
						jnjLoadTranslationService.getCustDelUomMappingByRoundigForSaoLuiz(jnJProductModel, loadTranslationModel,
								jnjUomDTO, receiveNumerator);
					}
				}
			}
		}
		if (null != jnjUomDTO && jnjUomDTO instanceof JnjLaUomDTO)
		{
			lineItem.setUom(((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode() != null
					? ((JnjLaUomDTO) jnjUomDTO).getSalesUnitCode().toUpperCase() : "");
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Entered UOM is not valid.", currentClass);
			JnjlatamOrderUtil.stringBuilderMethodForMultiplicityCheck(uomStrBuilder, cutomerProductNumber, actualUom);
		}

		return jnjUomDTO;
	}

	/**
	 * @param b2bUnit
	 * @param uomField
	 * @return String : CustUOM value
	 */
	private String fetchConvertedUomFromCmirConversion(final B2BUnitModel b2bUnit, final String uomField)
	{
		final String methodName = "fetchConvertedUomFromCmirConversion()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		JnjCmirConversionModel jnjCmirConversionModel = null;
		final JnJB2BUnitModel jnJB2bUnitModel = (JnJB2BUnitModel) b2bUnit;
		JnjCmirConversionModel jnjCmirConversionModelNew = new JnjCmirConversionModel();
		try
		{
			jnjCmirConversionModel = new JnjCmirConversionModel();
			jnjCmirConversionModel.setB2bUnitId(jnJB2bUnitModel);
			jnjCmirConversionModel.setCustUom(uomField);
			// Invoking the Flexible Search Service to get the JnJ CMIR Conversion model on passing the Customer product number.
			jnjCmirConversionModelNew = flexibleSearchService.getModelByExample(jnjCmirConversionModel);

		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Not Found exception occured.",
					modelNotFoundException, currentClass);
			jnjCmirConversionModelNew = null;
		}
		catch (final ModelLoadingException modelLoadingException)
		{
			JnjGTCoreUtil.logWarnMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Model Loading exception occured.",
					modelLoadingException, currentClass);
			jnjCmirConversionModelNew = null;
		}


		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		if (null != jnjCmirConversionModelNew)
		{
			return jnjCmirConversionModelNew.getCmirUom();
		}

		return null;
	}

	private PurchaseOrder processPreviousOrder(List<LineItem> lineItems, Items itemObject, PurchaseOrder purchaseOrder,
			boolean newOrder, final List<String> errorDetailsList, final StringBuilder eanNumStrBuilder,
			final StringBuilder uomMultiplicityCheckBuilder, final StringBuilder soldToStrBuilder,
			final StringBuilder requestedDeliveryDateBuilder, final StringBuilder requestDeliveryDateFormatBuilder,
			final Map<String, String> productNumberWithSalesOrg, final List<File> fileListForSAP,
			final List<PurchaseOrder> puchaseOrderList, final List<JnjUploadOrderSHAModel> passedSHAModels)
	{
		final String methodName = "processPreviousOrder()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		final JnjUploadOrderSHAModel passedSHAModel = passedSHAModels.get(0);
		if (newOrder)
		{
			try
			{
				itemObject.setLineItem(lineItems);
				purchaseOrder.setItems(itemObject);
				// if the errorDetailsList is not empty or the eanNumStrBuilder or custUomStrBuilder string builder is not empty then populate the error message in errorDetailsList list.
				if (soldToStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO
						|| requestedDeliveryDateBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
				{
					JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, null, null, null, null, null, soldToStrBuilder,
							null, null, requestedDeliveryDateBuilder, requestDeliveryDateFormatBuilder, passedSHAModel.getUser());
				}
				else
				{
					// Done the split logic and file generation by calling the spiltLogicForOrdersWithFileGeneration method.
					splitLogicForOrdersWithFileGeneration(purchaseOrder, productNumberWithSalesOrg, fileListForSAP, passedSHAModels);
				}
				if (!errorDetailsList.isEmpty() || eanNumStrBuilder.length() > Jnjb2bCoreConstants.Order.ZERO
						|| uomMultiplicityCheckBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
				{
					JnjlatamOrderUtil.populateErrorMessageInList(errorDetailsList, null, eanNumStrBuilder, null, null,
							uomMultiplicityCheckBuilder, null, null, null, null, null, passedSHAModel.getUser());
				}
			}
			catch (final BusinessException businessException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "Business exception occured", businessException,
						currentClass);
				errorDetailsList.add(businessException.getMessage());
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
			catch (final JAXBException jaxbException)
			{
				JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "JAXBException exception occured", jaxbException,
						currentClass);
				errorDetailsList.add(jaxbException.getMessage());
				JnjlatamOrderUtil.populateErrorDetailsList(errorDetailsList, passedSHAModel.getUser());
			}
		}

		/* Resetting Object Values to Porcess Next Order */
		puchaseOrderList.add(purchaseOrder);
		purchaseOrder = new PurchaseOrder();
		lineItems = new ArrayList<>();
		newOrder = false;
		itemObject = new Items();
		itemObject.setLineItem(lineItems);
		purchaseOrder.setItems(itemObject);

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return purchaseOrder;
	}


	@Override
	public JnjUploadOrderSHAModel createUploadOrderSHADetails(final String fileHash, final String fileName,
			final JnJB2bCustomerModel currentUser)
	{
		final String methodName = "saveUploadOrderSHADetails()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		final JnjUploadOrderSHAModel jnjUploadOrderSHAModel = modelService.create(JnjUploadOrderSHAModel.class);
		jnjUploadOrderSHAModel.setUserFileHashId(fileHash);
		jnjUploadOrderSHAModel.setUserFileName(fileName);
		jnjUploadOrderSHAModel.setUserFileTS(new Date());
		jnjUploadOrderSHAModel.setUser(currentUser);
		jnjUploadOrderSHAModel.setNodeId(Integer.valueOf(Config.getParameter(Jnjlab2bcoreConstants.CLUSTER_ID)));
		jnjUploadOrderSHAModel.setXmlFileCount(Integer.valueOf(0));
		jnjUploadOrderSHAModel.setXmlFileStatus(RecordStatus.PENDING);
		jnjUploadOrderSHAModel.setB2bUnitId(jnjGetCurrentDefaultB2BUnitUtil.getDefaultB2BUnit());
		modelService.save(jnjUploadOrderSHAModel);

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
		return jnjUploadOrderSHAModel;
	}

	@Override
	public void updateUploadOrderSHADetails(final JnjUploadOrderSHAModel jnjUploadOrderSHAModel, final String fileName,
			final RecordStatus recordStatus)
	{
		jnjUploadOrderSHAModel.setXmlFileName(fileName);
		jnjUploadOrderSHAModel.setXmlFileStatus(recordStatus);
		modelService.save(jnjUploadOrderSHAModel);

	}

	private boolean setSalesOrgCustomer(final JnJProductModel jnjProductModel, final JnJB2BUnitModel jnjB2bUnitModel,
			final Map<String, String> productNumberWithSalesOrg, final LineItem lineItem)
	{
		final String methodName = "setSalesOrgCustomer()";
		boolean result = true;
		try
		{
			final String salesOrgCustomer = jnjLaSalesOrgCustomerService.getSalesOrgByProductAndB2bUnitModel(jnjProductModel,
					jnjB2bUnitModel);
			// set the product number as key and Sales Org as value in a map.
			productNumberWithSalesOrg.put(lineItem.getProductNumber(), salesOrgCustomer);
		}
		catch (final Exception exception)
		{
			result = false;
			JnjGTCoreUtil.logErrorMessage(Logging.SUBMIT_ORDER_EDI, methodName, "There is no sales org defined for the customer: "
					+ jnjB2bUnitModel + "Product : " + lineItem.getProductNumber() + " Skipping this product.", exception,
					currentClass);
		}
		return result;
	}
}