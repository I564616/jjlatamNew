/**
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.b2b.jnjglobalresources.download.views;

import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.jnj.facades.data.JnjGTCpscContactData;
import com.jnj.facades.data.JnjGTCpsiaData;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.utils.CommonUtil;
import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Class responsible to create PDF view for CPSIA
 * 
 * @author Accenture
 * 
 */
public class JnjGTCpsiaCertificatePdfView extends AbstractPdfView
{
	private static final String TESTING = "testing";
	private static final String MANUFACTURER_DATE = "manufacturerDate";
	private static final String DESCRIPTION = "description";
	private static final String UPC_NUMBER = "upcNumber";
	private static final String LOT_NUMBER = "lotNumber";
	private static final String PRODUCT_CODE = "productCode";
	private static final String PRODUCT_NAME = "productName";
	private static final String MODIFIED_DATE = "modifiedDate";
	private static final String TEST_RESULT_HOLDER_DATA = "testResultHolderData";
	private static final String IMPORTER_RECORD_DATA = "importerRecordData";
	private static final String COMPANY_CERTIFYING_DATA = "companyCertifyingData";
	private static final String MANUFACTURER_DATA = "manufacturerData";
	private static final String _03 = "03";
	private static final String _02 = "02";
	private static final String _01 = "01";
	private static final Logger LOG = Logger.getLogger(JnjGTCpsiaCertificatePdfView.class);
	private static final String RESOURCES_CPSIA = "Resources - CPSIA";
	private static final String CPSI_DATA = "cpsiData";

	/**
	 * This method generates the PDF doc
	 */
	@Override
	protected void buildPdfDocument(final Map<String, Object> arg0, final Document document, final PdfWriter arg2,
			final HttpServletRequest arg3, final HttpServletResponse arg4) throws Exception
	{
		final String METHOD_NAME = "buildPdfDocument()";
		final JnjGTCpsiaData jnjGTCpsiaData = (JnjGTCpsiaData) arg0.get(CPSI_DATA);
		final String htmlStringForPDF = generateVMForCPSIACertificate(jnjGTCpsiaData);
		arg4.setHeader("Content-Disposition", "attachment; filename=CPSIACertificate.pdf");
		try
		{
			final File file = File.createTempFile("certificate", ".pdf");
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			final HTMLWorker htmlWorker = new HTMLWorker(document);
			htmlWorker.parse(new StringReader(htmlStringForPDF));
			document.close();
		}
		catch (final Exception exception)
		{
			LOG.error(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + "Exception in creating PDF :: " + exception);
		}
	}

	/**
	 * This method fetches the VM template for the CPSIA PDF download and returns HTML String
	 * 
	 * @param jnjGTCpsiaData
	 * 
	 * @return HTML String
	 */
	protected String generateVMForCPSIACertificate(final JnjGTCpsiaData jnjGTCpsiaData)
	{
		final String METHOD_NAME = "generateVMForCPSIACertificate";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		final VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");

		LOG.info(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + "Getting the vm file..");

		final String path = getClass().getResource(Config.getParameter("cpsia.path")).getPath();
		LOG.info(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + "Found CPSIA Path as:  " + path);

		velocityEngine.setProperty("file.resource.loader.path", path.substring(0, path.lastIndexOf("/")));
		LOG.info(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + "file.resource.loader.path :  "
				+ path.substring(0, path.lastIndexOf("/")));
		velocityEngine.setProperty("file.resource.loader.cache", Boolean.TRUE);
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");

		LOG.info(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + "Initializing Velocity Engine");
		velocityEngine.init();

		LOG.info(RESOURCES_CPSIA + Logging.HYPHEN + METHOD_NAME + "Retrieving template from Velocity Engine");
		final Template template = velocityEngine.getTemplate("cpsiaCertificate.vm");

		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "VM File fetched :: " + template, LOG);
		final VelocityContext context = new VelocityContext();
		populateContext(jnjGTCpsiaData, context);
		final StringWriter writer = new StringWriter();
		template.merge(context, writer);
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "VM File merged with string!", LOG);

		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOG);
		return String.valueOf(writer);
	}

	/**
	 * This method populate the context for the VM file
	 * 
	 * @param jnjGTCpsiaData
	 * @param context
	 */
	protected void populateContext(final JnjGTCpsiaData jnjGTCpsiaData, final VelocityContext context)
	{
		final String METHOD_NAME = "populateContext";
		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.BEGIN_OF_METHOD, LOG);

		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Going to set values in the context", LOG);
		context.put(PRODUCT_NAME, jnjGTCpsiaData.getProductName());
		context.put(PRODUCT_CODE, jnjGTCpsiaData.getProductCode());
		context.put(LOT_NUMBER, jnjGTCpsiaData.getLotNumber());
		context.put(UPC_NUMBER, jnjGTCpsiaData.getUpc());
		context.put(DESCRIPTION, jnjGTCpsiaData.getDescription());
		context.put(MANUFACTURER_DATE, jnjGTCpsiaData.getMfdDate());
		/** Address data **/
		addAddressesToContext(jnjGTCpsiaData, context);
		/** Testing Report info **/
		context.put(TESTING, jnjGTCpsiaData.getCpscTestDetailList());
		context.put(MODIFIED_DATE, jnjGTCpsiaData.getModifiedDate());
		CommonUtil.logDebugMessage(RESOURCES_CPSIA, METHOD_NAME, "Values have been set in the context!", LOG);

		CommonUtil.logMethodStartOrEnd(RESOURCES_CPSIA, METHOD_NAME, Logging.END_OF_METHOD, LOG);
	}

	/**
	 * This method adds various address to the context based on the address type
	 * 
	 * @param jnjGTCpsiaData
	 * @param context
	 */
	protected void addAddressesToContext(final JnjGTCpsiaData jnjGTCpsiaData, final VelocityContext context)
	{
		final List<JnjGTCpscContactData> manufacturerData = new ArrayList<JnjGTCpscContactData>();
		final List<JnjGTCpscContactData> companyCertifyingData = new ArrayList<JnjGTCpscContactData>();
		final List<JnjGTCpscContactData> importerRecordData = new ArrayList<JnjGTCpscContactData>();
		final List<JnjGTCpscContactData> testResultHolderData = new ArrayList<JnjGTCpscContactData>();
		/** Iterating over the CPSIA contact data **/
		for (final JnjGTCpscContactData addressData : jnjGTCpsiaData.getCpscContactDataList())
		{
			if (_01.equalsIgnoreCase(addressData.getAddressType()))
			{
				manufacturerData.add(addressData);
			}
			else if (_02.equalsIgnoreCase(addressData.getAddressType()))
			{
				companyCertifyingData.add(addressData);
			}
			else if (_03.equalsIgnoreCase(addressData.getAddressType()))
			{
				importerRecordData.add(addressData);
			}
			else
			{
				testResultHolderData.add(addressData);
			}
		}
		context.put(MANUFACTURER_DATA, manufacturerData);
		context.put(COMPANY_CERTIFYING_DATA, companyCertifyingData);
		context.put(IMPORTER_RECORD_DATA, importerRecordData);
		context.put(TEST_RESULT_HOLDER_DATA, testResultHolderData);
	}
}
