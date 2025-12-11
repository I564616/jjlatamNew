/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import jakarta.annotation.Resource;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fedex.ws.track.v8.ClientDetail;
import com.fedex.ws.track.v8.Contact;
import com.fedex.ws.track.v8.ContactAndAddress;
import com.fedex.ws.track.v8.Localization;
import com.fedex.ws.track.v8.NotificationSeverityType;
import com.fedex.ws.track.v8.ObjectFactory;
import com.fedex.ws.track.v8.QualifiedTrackingNumber;
import com.fedex.ws.track.v8.SignatureProofOfDeliveryImageType;
import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterReply;
import com.fedex.ws.track.v8.SignatureProofOfDeliveryLetterRequest;
import com.fedex.ws.track.v8.TransactionDetail;
import com.fedex.ws.track.v8.VersionId;
import com.fedex.ws.track.v8.WebAuthenticationCredential;
import com.fedex.ws.track.v8.WebAuthenticationDetail;
import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.gt.outbound.mapper.JnjGTProofOfDeliveryMapper;
import com.jnj.gt.outbound.services.JnjGTProofOfDeliveryService;



/**
 * The JnjGTProofOfDeliveryMapperImpl class contains the definition of all the method of the JnjGTProofOfDeliveryMapper
 * interface.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTProofOfDeliveryMapper implements JnjGTProofOfDeliveryMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProofOfDeliveryMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	private JnjGTProofOfDeliveryService jnjGTProofOfDeliveryService;

	@Resource(name = "productService")
	JnJGTProductService jnJGTProductService;
	
	@Autowired
	protected JnJCommonUtil jnjCommonUtil;

	public JnjGTProofOfDeliveryService getJnjGTProofOfDeliveryService() {
		return jnjGTProofOfDeliveryService;
	}

	public JnJGTProductService getjnJGTProductService() {
		return jnJGTProductService;
	}

	/**
	 * {!{@inheritDoc}
	 * 
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	@Override
	public File mapProofOfDeliveryRequestResponse(final String trackingNumber, final String personName, final String companyName,
			final String shipDate) throws IntegrationException, SystemException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "mapProofOfDeliveryRequestResponse()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		File file = null;
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(jnjCommonUtil.getTimeStampFormat());
		final SimpleDateFormat dateFormatForShipDate = new SimpleDateFormat(
				Config.getParameter(Jnjgtb2boutboundserviceConstants.RESPONSE_DATE_FORMAT));
		final SignatureProofOfDeliveryLetterRequest request = new SignatureProofOfDeliveryLetterRequest();
		// set the client details
		request.setClientDetail(createClientDetail());
		// set the authentication details
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		// set the version id information
		final VersionId versionId = new VersionId();
		versionId.setServiceId(Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.SERVICE_ID));
		versionId.setMajor(Integer.parseInt(Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.MAJOR)));
		versionId.setIntermediate(Integer.parseInt(Config
				.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.INTERMEDIATE)));
		versionId.setMinor(Integer.parseInt(Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.MINOR)));
		request.setVersion(versionId);
		// set the transaction details
		final TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId(simpleDateFormat.format(new Date())); //This is a reference field for the customer.  Any value can be used and will be provided in the return.
		//transactionDetail.setCustomerTransactionId("36450712113");
		final Localization localization = new Localization();
		localization.setLanguageCode(Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.LANGUAGE_CODE));
		transactionDetail.setLocalization(localization);
		request.setTransactionDetail(transactionDetail);
		// set the tracking information
		final QualifiedTrackingNumber trackNumber = new QualifiedTrackingNumber();

		final GregorianCalendar gregorianCalendar = new GregorianCalendar();
		XMLGregorianCalendar date;
		try
		{
			//final Date formattedDate = dateFormatForShipDate.parse("2014-05-20");
			final Date formattedDate = dateFormatForShipDate.parse(shipDate);
			gregorianCalendar.setTime(formattedDate);
			date = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
			date.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			trackNumber.setShipDate(date);
		}
		catch (final DatatypeConfigurationException exception)
		{
			LOGGER.error(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "mapProofOfDeliveryRequestResponse()" + Logging.HYPHEN
					+ "DatatypeConfiguration Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the JnjGTProofOfDeliveryMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}
		catch (final ParseException exception)
		{
			LOGGER.error(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "mapProofOfDeliveryRequestResponse()" + Logging.HYPHEN
					+ "Parse  Exception Occured " + exception.getMessage(), exception);
			throw new SystemException("System Exception throw from the JnjGTProofOfDeliveryMapperImpl class",
					MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
		}


		trackNumber.setTrackingNumber(trackingNumber);
		request.setQualifiedTrackingNumber(trackNumber);
		// set the contact and address information
		final ContactAndAddress consigneeContactAndAddress = new ContactAndAddress();
		final Contact consigneeContact = new Contact();
		consigneeContact.setPersonName(personName);
		consigneeContact.setCompanyName(companyName);
		// consigneeContact.setPhoneNumber("1234567890");
		consigneeContactAndAddress.setContact(consigneeContact);
		request.setConsignee(consigneeContactAndAddress);
		request.setLetterFormat(SignatureProofOfDeliveryImageType.PDF);

		final SignatureProofOfDeliveryLetterReply reply = jnjGTProofOfDeliveryService
				.retrieveSignatureProofOfDeliveryLetter(request);
		//  Get the file content from the response and set it in fiel
		if (isResponseOk(reply.getHighestSeverity()))
		{
			try
			{
				file = saveLetterToFile(reply.getLetter(), trackingNumber);
			}
			catch (final Exception exception)
			{
				LOGGER.error(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "mapProofOfDeliveryRequestResponse()" + Logging.HYPHEN
						+ "Exception Occured " + exception.getMessage(), exception);
				throw new SystemException("System Exception throw from the JnjGTProofOfDeliveryMapperImpl class",
						MessageCode.SYSTEM_EXCEPTION, Severity.SYSTEM_EXCEPTION);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "mapProofOfDeliveryRequestResponse()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return file;
	}

	/**
	 * Checks if is response ok.
	 * 
	 * @param notificationSeverityType
	 *           the notification severity type
	 * @return true, if is response ok
	 */
	protected static boolean isResponseOk(final NotificationSeverityType notificationSeverityType)
	{
		if (notificationSeverityType == null)
		{
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING)
				|| notificationSeverityType.equals(NotificationSeverityType.NOTE)
				|| notificationSeverityType.equals(NotificationSeverityType.SUCCESS))
		{
			return true;
		}
		return false;
	}

	/**
	 * Save letter to file.
	 * 
	 * @param letter
	 *           the letter
	 * @param trackingNumber
	 *           the tracking number
	 * @return the file
	 * @throws Exception
	 *            the exception
	 */
	protected static File saveLetterToFile(final byte[] letter, final String trackingNumber) throws Exception
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "saveLetterToFile()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final String letterLocation = Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.FILE_LOCATION);
		final String letterFileName = new String(letterLocation
				.concat(Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.SERVICE_ID)).concat(trackingNumber)
				.concat(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.FILE_EXTENSION));
		final File letterFile = new File(letterFileName);

		final FileOutputStream fos = new FileOutputStream(letterFile);
		fos.write(letter);
		fos.close();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "saveLetterToFile()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return letterFile;
	}

	/**
	 * Creates the client detail.
	 * 
	 * @return the client detail
	 */
	protected static ClientDetail createClientDetail()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "createClientDetail()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final ClientDetail clientDetail = new ClientDetail();
		final String accountNumber = Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.ACCOUNT_NUMBER);
		final String meterNumber = Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.METER_NUMBER);
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "createClientDetail()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return clientDetail;
	}

	/**
	 * Creates the web authentication detail.
	 * 
	 * @return the web authentication detail
	 */
	protected static WebAuthenticationDetail createWebAuthenticationDetail()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "createWebAuthenticationDetail()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final WebAuthenticationDetail webAuthenticationDetail = new WebAuthenticationDetail();
		final WebAuthenticationCredential wac = new WebAuthenticationCredential();
		final String key = Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.KEY);
		final String password = Config.getParameter(Jnjgtb2boutboundserviceConstants.ProofOfDelivery.PASSWORD);
		wac.setKey(key);
		wac.setPassword(password);
		webAuthenticationDetail.setUserCredential(wac);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PROOF_OF_DELIVERY + Logging.HYPHEN + "createWebAuthenticationDetail()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return webAuthenticationDetail;
	}

}
