/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.facades.order.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.facades.JnjLatamSAPErrorMessageFacade;
import com.jnj.facades.data.JnjSAPErrorMessageData;
import com.jnj.facades.data.JnjValidateOrderData;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.enums.ErrorMessageType;
import com.jnj.la.core.enums.Scope;
import com.jnj.la.core.model.JnjSAPErrorTranslationTableModel;
import com.jnj.la.core.services.order.JnjLatamSAPErrorService;



/**
 * The Class JnjSAPErrorMessageFacadeImpl.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjLatamSAPErrorMessageFacadeImpl implements JnjLatamSAPErrorMessageFacade
{

	/** The Constant SPACE_REGEX. */
	private static final String SPACE_REGEX = "\\s+";

	/** The Constant KEYWORD_IGNORE. */
	private static final String KEYWORD_IGNORE = "IGNORE";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjLatamSAPErrorMessageFacadeImpl.class);

	@Autowired
	protected SessionService sessionService;

	/** The jnj sap error service. */
	@Autowired
	private JnjLatamSAPErrorService jnjSAPErrorService;

	/** The cart service. */
	@Autowired
	private CartService cartService;

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The enumeration service. */
	@Autowired
	private EnumerationService enumerationService;

	@Autowired
	protected I18NService i18NService;

	/**
	 * Generate key.
	 *
	 * @param errorMessageData
	 *           the error message data
	 * @return the string
	 */
	private String generateKey(final JnjSAPErrorMessageData errorMessageData)
	{
		final String key = errorMessageData.getErrorMessageType() + Jnjb2bCoreConstants.CONST_DOT + errorMessageData.getId()
				+ Jnjb2bCoreConstants.CONST_DOT + errorMessageData.getNumber();
		return key;
	}

	/**
	 *
	 * @param errorMessageData
	 *           the error message data
	 * @return the error details
	 */
	@Override
	public String getErrorDetails(final JnjSAPErrorMessageData errorMessageData, final List<AbstractOrderEntryModel> cartEntries)
	{
		final String METHOD_NAME = "getErrorDetails()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		String finalErrorMessage = null;
		StringBuilder finalErrorMessageBuilder = new StringBuilder();
		JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel = null;
		/*
		 * Getting the session cart final CartModel cartModel = cartService.getSessionCart(); final
		 * List<AbstractOrderEntryModel> cartEntries = cartModel.getEntries();
		 */

		final String errorKey = generateKey(errorMessageData);

		/* Get Error Message From Hybris */
		jnjSAPErrorTranslationTableModel = jnjSAPErrorService.getAllErrorDetails(errorKey);


        /*Default message in case of Unknown Error*/
		if (null == jnjSAPErrorTranslationTableModel) {
			LOGGER.info(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "Setting incoming message as translated message for key[" + errorKey + "].");

			jnjSAPErrorTranslationTableModel = modelService.create(JnjSAPErrorTranslationTableModel.class);
			jnjSAPErrorTranslationTableModel.setTanslatedMessage(errorMessageData.getErrorMessage());
			jnjSAPErrorTranslationTableModel.setErrorMessageType(
					enumerationService.getEnumerationValue(ErrorMessageType.class, errorMessageData.getErrorMessageType()));
			jnjSAPErrorTranslationTableModel.setScope(Scope.DEFAULT);
		}

		/*Header and Default Errors*/
		if ((Scope.HEADER).equals(jnjSAPErrorTranslationTableModel.getScope())
				|| (Scope.DEFAULT).equals(jnjSAPErrorTranslationTableModel.getScope())) {
			/*
			 * final error message at the header will look like
			 * "Scope.HEADER##JnjSAPErrorTranslationTable.translatedMessage"
			 */

			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Scope.HEADER.toString());
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(jnjSAPErrorTranslationTableModel.getTanslatedMessage());
		}

		/*Line Errors*/
        else if (jnjSAPErrorTranslationTableModel.getScope().equals(Scope.LINE))
		{
			final Map<Boolean, String> messageMatchResult = searchInMessage(cartEntries, errorMessageData.getErrorMessage(),
					jnjSAPErrorTranslationTableModel);
			if (messageMatchResult.keySet().iterator().next().booleanValue())
			{
				/*
				 * Product Code Found in the Incoming Error Message
				 *
				 * final error message at the line will look like
				 * "Scope.LINE##JnjSAPErrorTranslationTable.tanslatedMessage~~productCode"
				 */
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Scope.LINE.toString());
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
				finalErrorMessageBuilder = finalErrorMessageBuilder
						.append(exractTranslatedMessage(errorMessageData.getErrorMessage(), jnjSAPErrorTranslationTableModel));
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjlab2bcoreConstants.SYMBOL_TILDE);
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjlab2bcoreConstants.SYMBOL_TILDE);
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(messageMatchResult.get(Boolean.TRUE));
			}
			else
			{
				/*
				 * Product Code Not Found in the Incoming Error Message
				 *
				 * * final error message at the Header will look like
				 * "Scope.HEADER##JnjSAPErrorTranslationTable.tanslatedMessag(for default)"
				 */
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Scope.DEFAULT.toString());
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
				finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
				finalErrorMessageBuilder = finalErrorMessageBuilder
						.append(jnjSAPErrorService.getDefaultError().getTanslatedMessage());
			}
		}

		else if (jnjSAPErrorTranslationTableModel.getScope().equals(Scope.IGNORE))
		{
			/*
			 * No Message will be displayed.
			 */
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Scope.IGNORE.toString());
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(Jnjb2bCoreConstants.HASH_SYMBOL);
			finalErrorMessageBuilder = finalErrorMessageBuilder.append(KEYWORD_IGNORE);
		}

		finalErrorMessage = finalErrorMessageBuilder.toString();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return finalErrorMessage;
	}

	/**
	 * This method extracts the strings from the incoming message and append the final translated message.
	 *
	 * @param incommingErrorMessage
	 *           the incomming error message
	 * @param jnjSAPErrorTranslationTableModel
	 *           the jnj sap error translation table model
	 * @return the string
	 */
	private String exractTranslatedMessage(final String incommingErrorMessage,
			final JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel)
	{
		final String METHOD_NAME = "exractTranslatedMessage()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		String extractedMessage = null;
		final StringBuilder extractedMessageBuilder = new StringBuilder();
		String secondExtraction = null;
		String thirdExtraction = null;

		/* Extracting Strings From the Incoming Error Message */
		switch (jnjSAPErrorTranslationTableModel.getNoOfExtractions().intValue())
		{
			case 2:
				secondExtraction = extractString(incommingErrorMessage, jnjSAPErrorTranslationTableModel.getSecondExtraction());
				break;
			case 3:
				secondExtraction = extractString(incommingErrorMessage, jnjSAPErrorTranslationTableModel.getSecondExtraction());
				thirdExtraction = extractString(incommingErrorMessage, jnjSAPErrorTranslationTableModel.getThirdExtraction());
				break;
			default:
				break;
		}

		/* Building the Extracted Message */
		extractedMessageBuilder.append(jnjSAPErrorTranslationTableModel.getTanslatedMessage());
		if (StringUtils.isNotEmpty(secondExtraction))
		{
			extractedMessageBuilder.append(Jnjb2bCoreConstants.SPACE);
			extractedMessageBuilder.append(secondExtraction);
		}
		if (StringUtils.isNotEmpty(thirdExtraction))
		{
			extractedMessageBuilder.append(Jnjb2bCoreConstants.SPACE);
			extractedMessageBuilder.append(thirdExtraction);
		}

		extractedMessage = extractedMessageBuilder.toString();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return extractedMessage;
	}

	/**
	 * Search in message.
	 *
	 * @param cartEntries
	 *           the product code
	 * @param errorMessage
	 *           the error message
	 * @param jnjSAPErrorTranslationTableModel
	 *           the jnj sap error translation table model
	 * @return true, if successful
	 */
	private Map<Boolean, String> searchInMessage(final List<AbstractOrderEntryModel> cartEntries, final String errorMessage,
			final JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel)
	{
		final String METHOD_NAME = "searchInMessage()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		final Map<Boolean, String> searchSuccesfulList = new HashMap<>();

		//Map to store GTS information
		final Map<String, JnjSAPErrorMessageData> gtsProductMap = new HashMap<>();

		/*
		 * We will search for Product Code in the Message only when the No. of Extractions in the corresponding
		 * JnjSAPErrorTranslationTableModel is greater than 1
		 */
		if (StringUtils.isNotEmpty(errorMessage) && null != jnjSAPErrorTranslationTableModel.getNoOfExtractions()
				&& jnjSAPErrorTranslationTableModel.getNoOfExtractions().intValue() > 0)
		{
			boolean matchFound = false;
			String productCode = null;

			/* Extracting the product code from the first extraction value. */
			final String extractedProductCode = extractString(errorMessage, jnjSAPErrorTranslationTableModel.getFirstExtraction());
			cartEntriesLoop: for (final AbstractOrderEntryModel entry : cartEntries)
			{
				productCode = entry.getProduct().getCode();
				if (StringUtils.equalsIgnoreCase(extractedProductCode, productCode))
				{
					matchFound = true;
					break cartEntriesLoop;
				}
			}
			if (matchFound)
			{
				searchSuccesfulList.put(Boolean.TRUE, productCode);
				//JCC-20: Changes for GTS message display
				if (errorMessage.toUpperCase().contains("GTS"))
				{
					final JnjSAPErrorMessageData sapMessage = new JnjSAPErrorMessageData();
					sapMessage.setErrorMessage(jnjSAPErrorTranslationTableModel.getTanslatedMessage(i18NService.getCurrentLocale()));
					gtsProductMap.put(productCode, sapMessage);
				}
			}
			else
			{
				searchSuccesfulList.put(Boolean.FALSE, StringUtils.EMPTY);
			}
		}
		else
		{
			searchSuccesfulList.put(Boolean.FALSE, StringUtils.EMPTY);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		sessionService.setAttribute("gtsProductMap", gtsProductMap);
		return searchSuccesfulList;
	}

	/**
	 * This method is used to extract the string from the mentioned position.
	 *
	 * @param errorMessage
	 *           the error message
	 * @param stringPosition
	 *           the string position
	 * @return the string
	 */
	private String extractString(final String errorMessage, final Integer stringPosition)
	{
		final String METHOD_NAME = "extractString()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}

		String extractedString = null;
		if (StringUtils.isNotEmpty(errorMessage) && stringPosition != null)
		{
			final String[] errorMessageArray = errorMessage.split(SPACE_REGEX);
			if (null != errorMessageArray && errorMessageArray.length > 0)
			{
				extractedString = errorMessageArray[stringPosition.intValue()];
			}
			else
			{
				LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
						+ "No String Extarcted from the Incomming Error Message [" + errorMessage + "]");
			}
		}
		else
		{
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN
					+ "Incomming Error Message or Search Position is [Null]. No String Extarcted.");
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return extractedString;
	}

	/**
	 *
	 * @param headerErrorMessageList
	 *           the header error message list
	 * @param lineErrorMessageMap
	 *           the line error message map
	 * @param validateOrderData
	 *           the validate order data
	 */
	@Override
	public void populateSapErrorDetails(final List<String> headerErrorMessageList,
			final Map<String, List<String>> lineErrorMessageMap, final JnjValidateOrderData validateOrderData)
	{
		final String METHOD_NAME = "populateSapErrorDetails()";
		final Integer TYPE = 0;
		final Integer MESSAGE = 1;
		Boolean headerMessageExist;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		if (null != validateOrderData)
		{
			final List<String> incomingSapErrorMessageList = validateOrderData.getSapErrorMessages();
			if (CollectionUtils.isNotEmpty(incomingSapErrorMessageList))
			{
				/* 'singleDefaultAtHeader' makes sure only one Default error Message is placed on Header Level */
				boolean singleDefaultAtHeader = false;

                for (final String incomingSapErrorMessage : incomingSapErrorMessageList) {

					headerMessageExist = Boolean.FALSE;

					LOGGER.info(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + "Processing SAP Error Message ["
							+ incomingSapErrorMessage + "]");

					if (null != incomingSapErrorMessage && StringUtils.isNotEmpty(incomingSapErrorMessage)) {

						final List<String> message = Arrays.asList(
								incomingSapErrorMessage.split(Jnjb2bCoreConstants.HASH_SYMBOL + Jnjb2bCoreConstants.HASH_SYMBOL));

                        /*Header Error*/
                        for(String headerMessage:headerErrorMessageList){
							if(StringUtils.equalsIgnoreCase(headerMessage, message.get(MESSAGE))){
								headerMessageExist = Boolean.TRUE;
							}
						}
                        if (StringUtils.equalsIgnoreCase(message.get(TYPE), Scope.HEADER.toString()) && !headerMessageExist){
							headerErrorMessageList.add(message.get(MESSAGE));

                        /*Line Error*/
                        } else if (StringUtils.equalsIgnoreCase(message.get(TYPE), Scope.LINE.toString())) {

                            final List<String> lineLevelErrorList = Arrays
									.asList(message.get(MESSAGE).split(Jnjlab2bcoreConstants.SYMBOL_TILDE + Jnjlab2bcoreConstants.SYMBOL_TILDE));

							final String lineProductCode = lineLevelErrorList.get(MESSAGE);
							List<String> lineErrorList = lineErrorMessageMap.get(lineProductCode);

                            if (null == lineErrorList) {
								lineErrorList = new ArrayList<>();
							}

							/*Add the text from message to lineErrorList*/
							lineErrorList.add(lineLevelErrorList.get(0));
							lineErrorMessageMap.put(lineProductCode, lineErrorList);
						}
						else if (StringUtils.equalsIgnoreCase(message.get(TYPE), Scope.DEFAULT.toString()) && !singleDefaultAtHeader)
						{
							headerErrorMessageList.add(message.get(MESSAGE));
							singleDefaultAtHeader = true;
						}
					}
					if(incomingSapErrorMessage.equalsIgnoreCase(Jnjlab2bcoreConstants.IN_CORRECT_SHIPTO)){
                		headerErrorMessageList.add(Jnjlab2bcoreConstants.IN_CORRECT_SHIPTO);
                	}
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
	}
}
