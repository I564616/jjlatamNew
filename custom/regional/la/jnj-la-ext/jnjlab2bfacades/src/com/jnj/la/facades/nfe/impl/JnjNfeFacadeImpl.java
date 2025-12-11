/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.facades.nfe.impl;

import de.hybris.platform.util.Config;

import java.io.File;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.services.JnjInvoiceService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.la.facade.nfe.JnjNfeFacade;
import com.jnj.la.facades.nfe.mapper.JnjNfeMapper;
import com.jnj.outboundservice.nfe.ElectronicNotaFiscalResponse;
import com.jnj.outboundservice.nfe.ReceiveElectronicNotaFiscalFromHybrisWrapper;
import com.jnj.outboundservice.nfe.TNfeProc;




/**
 * The Facade Class for NFe to get and process the NFe Data.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjNfeFacadeImpl implements JnjNfeFacade
{

	/** The Constant LOG. */
	private static final Logger LOGGER = Logger.getLogger(JnjNfeFacadeImpl.class);
	private static final String METHOD_GET_NFE_FILE = "getNfeFile()";
	private static final String METHOD_EXTRACT_NFE_FILE = "extractXmlFile()";

	/** The jnj invoice service. */
	@Autowired
	private JnjInvoiceService jnjInvoiceService;

	/** The jnj nfe mapper. */
	@Autowired
	private JnjNfeMapper jnjNfeMapper;

	/**
	 * Gets the NFe XML data from SAP and returns the NFe XML File.
	 *
	 * @param invDocNo
	 *           the inv doc no
	 * @return the nfe file
	 * @throws IntegrationException
	 * @throws BusinessException
	 * @throws JAXBException
	 */
	@Override
	public File getNfeFile(final String invDocNo)
			throws BusinessException, IntegrationException, IllegalArgumentException, JAXBException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_GET_NFE_FILE, Logging.BEGIN_OF_METHOD, JnjNfeFacadeImpl.class);

		File xmlFile = null;
		final ReceiveElectronicNotaFiscalFromHybrisWrapper receiveElectronicNotaFiscalFromHybrisWrapper = new ReceiveElectronicNotaFiscalFromHybrisWrapper();

		final JnJInvoiceOrderModel jnJInvoiceOrderModel = jnjInvoiceService.getInvoicebyCode(invDocNo);

		LOGGER.info(Logging.QUERY_NFE + Logging.HYPHEN + METHOD_GET_NFE_FILE + Logging.HYPHEN
				+ "Searching Hybris for Invoice Associated with InvoiceID: " + invDocNo);

		final ElectronicNotaFiscalResponse electronicNotaFiscalResponse;
		if (null != jnJInvoiceOrderModel)
		{

			electronicNotaFiscalResponse = jnjNfeMapper.mapNfeRequestData(jnJInvoiceOrderModel,
					receiveElectronicNotaFiscalFromHybrisWrapper);

			//Create xml file from the response with the required values
			xmlFile = extractXmlFile(electronicNotaFiscalResponse);
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_GET_NFE_FILE,
					"Cannot Find Invoice Associated with InvoiceID: " + invDocNo + "Initiating Business Exception",
					JnjNfeFacadeImpl.class);

			throw new BusinessException("Cannot Find Invoice Associated with InvoiceID: " + invDocNo);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_GET_NFE_FILE, Logging.END_OF_METHOD, JnjNfeFacadeImpl.class);

		return xmlFile;
	}



	/**
	 * This methods takes out TNfeProc element from ElectronicNotaFiscalResponse and makes an XML.
	 *
	 * @param electronicNotaFiscalResponse
	 *           the electronic nota fiscal response
	 * @return XML File
	 */
	private File extractXmlFile(final ElectronicNotaFiscalResponse electronicNotaFiscalResponse)
			throws JAXBException, BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_GET_NFE_FILE, Logging.END_OF_METHOD, JnjNfeFacadeImpl.class);


		final File file;

		if (null != electronicNotaFiscalResponse)
		{
			final TNfeProc tNfeProc = electronicNotaFiscalResponse.getNfeProc();
			JAXBContext jAXBContext;
			Marshaller marshaller;
			final String nfeFilePath = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT_TEMP)
					+ Config.getParameter(Jnjb2bCoreConstants.Nfe.NFE_TEMP_FILE_LOCATION_KEY);
			final String nfeFileName = Jnjb2bCoreConstants.Nfe.NFE_TEMP_FILE_NAME;

			file = new File(FilenameUtils.getFullPath(nfeFilePath + nfeFileName), FilenameUtils.getName(nfeFilePath + nfeFileName));

			// Not null check for TNfeProc object
			if (null != tNfeProc)
			{
				jAXBContext = JAXBContext.newInstance(TNfeProc.class);
				marshaller = jAXBContext.createMarshaller();
				marshaller.marshal(
						new JAXBElement(new QName("http://www.portalfiscal.inf.br/nfe", "TNfeProc"), TNfeProc.class, tNfeProc), file);
			}
			else
			{
				JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_EXTRACT_NFE_FILE,
						"TNfeProc in ElectronicNotaFiscalResponse is null. Initiating Business Exception", JnjNfeFacadeImpl.class);

				throw new BusinessException("TNfeProc in ElectronicNotaFiscalResponse is null.");
			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_EXTRACT_NFE_FILE,
					"ElectronicNotaFiscalResponse is null. Initiating Business Exception", JnjNfeFacadeImpl.class);

			throw new BusinessException("ElectronicNotaFiscalResponse is null");
		}
		JnjGTCoreUtil.logDebugMessage(Logging.QUERY_NFE, METHOD_EXTRACT_NFE_FILE, Logging.END_OF_METHOD, JnjNfeFacadeImpl.class);
		return file;
	}


}
