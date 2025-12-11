/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.jnj.la.core.util;


import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.order.util.JnjOrderUtil;
import com.jnj.core.services.impl.JnjLaMessageServiceImpl;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjLatamSplitOrderInfo;
import com.jnj.la.core.model.JnjUploadOrderSHAModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.DatatypeConverter;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author mpanda3
 *
 */
public class JnjlatamOrderUtil extends JnjOrderUtil
{
	@Autowired
	protected SessionService sessionService;

	@Autowired
	private JnjLaMessageServiceImpl jnjLaMessageService;

	private static JnjLaMessageServiceImpl staticJnjLaMessageService;

	private static final String LATAM_ORDER_UTIL = "Jnj latam Order Util";

	private static final Class currentClass = JnjlatamOrderUtil.class;

	@PostConstruct
	public void registerInstance()
	{
		staticJnjLaMessageService = jnjLaMessageService;
	}

	/**
	 * This method validates the availability of product in the dropshipment table.
	 *
	 * @param splitOrderMap
	 * @param cartModel
	 * @return Map
	 */

	public Map<String, String> validateSplitOrderMapLatam(
			final Map<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> splitOrderMap, final CartModel cartModel)
	{
		final String methodName = "validateSplitOrderMapLatam()";
		String lineMessage = null;
		try
		{
			lineMessage = messageFacade.getMessageTextForCode(Jnjb2bCoreConstants.Dropshipment.NOT_FOUND_ERROR);
		}
		catch (final Exception e)
		{

			JnjGTCoreUtil.logErrorMessage(LATAM_ORDER_UTIL, methodName, "Exception:", e, currentClass);

			lineMessage = "Order Type not defined for this material. Please contact support.";
		}

		final Map<String, String> codesNotFound = new HashMap<>();

		for (final AbstractOrderEntryModel cartEntry : cartModel.getEntries())
		{

			boolean found = false;
			if (splitOrderMap != null)
			{
				for (final Entry<JnjLatamSplitOrderInfo, List<AbstractOrderEntryModel>> mapEntry : splitOrderMap.entrySet())
				{

					if (!found)
					{
						for (final AbstractOrderEntryModel mapProduct : mapEntry.getValue())
						{

							if (mapProduct.getProduct().getCode().equalsIgnoreCase(cartEntry.getProduct().getCode()))
							{
								found = true;
							}
						}
					}
				}
			}
			if (!found)
			{
				codesNotFound.put(cartEntry.getProduct().getCode(), lineMessage);
			}
		}
		return codesNotFound;
	}

	/**
	 * This method takes in a MultipartFile object and returns the SHA512 hash for it in String format
	 *
	 * @param submitOrderFile
	 *           The MultipartFile object
	 * @return The 512 bit hash in String format
	 */


	public static String getFileSha512(final InputStream inputStream, final String fileName)
	{
		final String methodName = "getFileSha512()";
		String fileHash = null;
		try
		{
			final MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			final byte[] buffer = new byte[1024];
			for (int read = 0; (read = inputStream.read(buffer)) != -1;)
			{
				messageDigest.update(buffer, 0, read);
			}
			fileHash = DatatypeConverter.printHexBinary(messageDigest.digest());
		}
		catch (final Exception e)
		{
			JnjGTCoreUtil.logErrorMessage(LATAM_ORDER_UTIL, methodName, "Exception: " + e, e, currentClass);
		}
		finally
		{
			try
			{
				if (inputStream != null)
				{
					inputStream.close();
				}
			}
			catch (final Exception e)
			{
				JnjGTCoreUtil.logErrorMessage(LATAM_ORDER_UTIL, methodName, "Exception: " + e, e, currentClass);
			}
		}
		return fileHash + fileName;
	}

	/**
	 * @param fileName
	 * @param currentUser
	 *
	 */
	public static boolean isIgnoreFileRequired(final Map<String, String> fileStatusMap,
			final List<JnjUploadOrderSHAModel> shaModels, final String fileName, final JnJB2bCustomerModel currentUser)
	{
		for (final JnjUploadOrderSHAModel shaModel : shaModels)
		{
			if (!shaModel.getUserFileName().equalsIgnoreCase(fileName) || !shaModel.getUser().equals(currentUser))
			{
				return false;
			}
		}
		fileStatusMap.put(fileName, Jnjlab2bcoreConstants.UploadOrder.ERROR_STATUS);

		return true;
	}

	/**
	 * This method is used to append the string builder for multiplicity check which is passed in the input file.
	 *
	 * @param materialNumber
	 *           the customer product number
	 * @param quantity
	 *           quantity
	 */
	public static void stringBuilderMethodForMultiplicityCheck(final StringBuilder stringBuilder, final String materialNumber,
			final String quantity)
	{
		final String methodName = "stringBuilderMethodForMultiplicityCheck()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		// if the string builder contains the one element in it then add the incoming input string with comma
		if (stringBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO && !stringBuilder.toString().contains(materialNumber))
		{
			stringBuilder.append(Jnjlab2bcoreConstants.CONST_COMMA).append(materialNumber)
					.append(Jnjlab2bcoreConstants.UNDERSCORE_SYMBOL).append(quantity);
		}
		// else add the incoming input string with out comma
		else
		{
			if (!stringBuilder.toString().contains(materialNumber))
			{
				stringBuilder.append(materialNumber).append(Jnjlab2bcoreConstants.UNDERSCORE_SYMBOL).append(quantity);
			}
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
	}

	/**
	 * The populateErrorMessageInList method adds the upcoming string builder value in the error list.
	 *
	 * @param errorDetailsList
	 *           the error details list
	 * @param custProdNumStrBuilder
	 *           the cust prod num str builder
	 * @param eanProdNumberStrBuilder
	 *           the ean prod number
	 * @param customerUomStrBuilder
	 *           the customer uom str builder
	 * @param jnJB2bCustomerModel
	 */
	public static void populateErrorMessageInList(final List<String> errorDetailsList, final StringBuilder custProdNumStrBuilder,
			final StringBuilder eanProdNumberStrBuilder, final StringBuilder customerUomStrBuilder,
			final StringBuilder shipToStrBuilder, final StringBuilder uomMultiplicityCheckBuilder,
			final StringBuilder soldToStrBuilder, final StringBuilder expectedDeliveryDateStrBuilder,
			final StringBuilder indirectCutomerBuilder, final StringBuilder requestDeliveryDateBuilder,
			final StringBuilder requestDeliveryDateFormatBuilder, final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String methodName = "populateErrorMessageInList()";

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);
		// check for the custProdNumStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != custProdNumStrBuilder && custProdNumStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String custProdNumbErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageForMaterialNumber", jnJB2bCustomerModel);
			final String errorMessage = custProdNumbErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					custProdNumStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the eanProdNumberStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != eanProdNumberStrBuilder && eanProdNumberStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String eanProdNumbErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageForEanNumber", jnJB2bCustomerModel);
			final String errorMessage = eanProdNumbErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					eanProdNumberStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the customerUomStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != customerUomStrBuilder && customerUomStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String custUomErrorMessage = staticJnjLaMessageService.getMessageFromImpex("order.home.errorMessageForUom",
					jnJB2bCustomerModel);
			final String[] materialQty = customerUomStrBuilder.toString().split(",");
			for (final String matQty : materialQty)
			{
				final String[] uomMatQty = matQty.split("_");
				final List<String> uomMatQtyList = new ArrayList<>();
				for (final String string : uomMatQty)
				{
					uomMatQtyList.add(string);
				}
				final String errorMessage = custUomErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
						uomMatQtyList.get(0));
				final String finalErrorMessage = errorMessage.replace(Jnjlab2bcoreConstants.Order.QUANTITY_STRING,
						uomMatQtyList.get(1));
				errorDetailsList.add(finalErrorMessage);
			}
		}
		// check for the shipToStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != shipToStrBuilder && shipToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String custUomErrorMessage = staticJnjLaMessageService.getMessageFromImpex("order.home.errorMessageForShipTo",
					jnJB2bCustomerModel);
			final String errorMessage = custUomErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING, shipToStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the soldToStrBuilder parameter value and if its not valid then create the error message corresponding to it and add it in the error list
		if (null != soldToStrBuilder && soldToStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String custsoldToErrorMessage = staticJnjLaMessageService.getMessageFromImpex("order.home.errorMessageForSoldTo",
					jnJB2bCustomerModel);
			final String errorMessage = custsoldToErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					soldToStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the shipToStrBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != uomMultiplicityCheckBuilder && uomMultiplicityCheckBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String uomMultiplicityCheckErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageForQuantity", jnJB2bCustomerModel);
			final String[] materialQty = uomMultiplicityCheckBuilder.toString().split(",");
			for (final String matQty : materialQty)
			{
				final String[] uomMatQty = matQty.split("_");
				final List<String> uomMatQtyList = new ArrayList<>();
				for (final String string : uomMatQty)
				{
					uomMatQtyList.add(string);
				}
				final String errorMessage = uomMultiplicityCheckErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
						uomMatQtyList.get(0));
				final String finalErrorMessage = errorMessage.replace(Jnjlab2bcoreConstants.Order.QUANTITY_STRING,
						uomMatQtyList.get(1));
				errorDetailsList.add(finalErrorMessage);
			}
		}
		// check for the expected delivery date value and if its not valid then create the error message corresponding to it and add it in the error list
		if (null != expectedDeliveryDateStrBuilder && expectedDeliveryDateStrBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String expectedDeliveryDateErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageExpectedDeliveryDate", jnJB2bCustomerModel);
			final String errorMessage = expectedDeliveryDateErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					expectedDeliveryDateStrBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the indirect customer value and if its not valid then create the error message corresponding to it and add it in the error list
		if (null != indirectCutomerBuilder && indirectCutomerBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String indirectCutomerErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageIndirectCustomer", jnJB2bCustomerModel);
			final String errorMessage = indirectCutomerErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					indirectCutomerBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the requestDeliveryDateBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != requestDeliveryDateBuilder && requestDeliveryDateBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String requestDeliveryDateErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageRequestDeliverDate", jnJB2bCustomerModel);
			final String errorMessage = requestDeliveryDateErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					requestDeliveryDateBuilder);
			errorDetailsList.add(errorMessage);
		}
		// check for the requestDeliveryDateFormatBuilder parameter value and if its not null then create the error message corresponding to it and add it in the error list
		if (null != requestDeliveryDateFormatBuilder
				&& requestDeliveryDateFormatBuilder.length() > Jnjlab2bcoreConstants.Order.ZERO)
		{
			final String requestDeliveryDateFormatErrorMessage = staticJnjLaMessageService
					.getMessageFromImpex("order.home.errorMessageRequestDeliveryDateFormat", jnJB2bCustomerModel);
			final String errorMessage = requestDeliveryDateFormatErrorMessage.replace(Jnjlab2bcoreConstants.Order.MATERIAL_STRING,
					requestDeliveryDateFormatBuilder);
			errorDetailsList.add(errorMessage);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
	}

	public static Locale getUserLanguageLocale(final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		if (jnJB2bCustomerModel != null && jnJB2bCustomerModel.getSessionLanguage() != null)
		{
			return Locale.of(jnJB2bCustomerModel.getSessionLanguage().getIsocode());
		}
		return Locale.of(Jnjlab2bcoreConstants.LANGUAGE_ISOCODE_PT.toLowerCase());
	}

	public static void stringBuilderMethod(final StringBuilder stringBuilder, final String inputString)
	{
		final String methodName = "stringBuilderMethod()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		// if the string builder contains the one element in it then add the incoming input string with comma
		if (stringBuilder.length() > Jnjb2bCoreConstants.Order.ZERO)
		{
			stringBuilder.append(Jnjb2bCoreConstants.CONST_COMMA).append(inputString);
		}
		// else add the incoming input string with out comma
		else
		{
			stringBuilder.append(inputString);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
	}


	public static void populateErrorDetailsList(List<String> errorDetails, final JnJB2bCustomerModel jnJB2bCustomerModel)
	{
		final String methodName = "populateErrorDetailsList()";
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.BEGIN_OF_METHOD, currentClass);

		// if the error details is null then create the list else add the general error message in the list.
		if (null == errorDetails)
		{
			errorDetails = new ArrayList<>();
		}

		errorDetails.add(staticJnjLaMessageService.getMessageFromImpex("order.home.generalErrorMessage", jnJB2bCustomerModel));
		JnjGTCoreUtil.logDebugMessage(Logging.SUBMIT_ORDER_EDI, methodName, Logging.END_OF_METHOD, currentClass);
	}
}
