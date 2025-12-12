/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 * @author prodri92
 */

package com.jnj.facades.invoice.impl;

import com.jnj.facades.data.JnJLaInvoiceHistoryData;
import com.jnj.la.core.services.JnjLaInvoiceService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.facades.constants.Jnjb2bFacadesConstants;
import com.jnj.facades.data.JnJInvoiceOrderData;
import com.jnj.facades.invoice.JnjLatamInvoiceFacade;
import com.jnj.facades.order.JnjLatamInvoiceDocMapper;
import com.jnj.facades.order.impl.JnjLatamInvoiceDocMapperImpl;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.outboundservice.invoice.ElectronicBillingResponse;

public class JnjLatamInvoiceFacadeImpl implements JnjLatamInvoiceFacade
{
	private static final Logger LOGGER = Logger.getLogger(JnjLatamInvoiceFacadeImpl.class);

	@Autowired
	private JnjLaInvoiceService jnjInvoiceService;

	@Autowired
	private JnjLatamInvoiceDocMapper jnjLatamInvoiceDocMapper;

	@Autowired
	private Converter<JnJInvoiceOrderModel, JnJInvoiceOrderData> jnJInvoiceOrderDataConverter;

	@Autowired
	private Converter<JnJInvoiceOrderModel, JnJLaInvoiceHistoryData> jnjLaInvoiceHistoryDataConverter;

	@Override
	public List<JnJInvoiceOrderData> getInvoices(final String salesOrder) throws BusinessException
	{
		List<JnJInvoiceOrderModel> invoiceModelList;
		final List<JnJInvoiceOrderData> invoiceDataList = new ArrayList<>();
		if (null != salesOrder)
		{
			invoiceModelList = jnjInvoiceService.getInvoicebyOrderCode(salesOrder);
			for (final JnJInvoiceOrderModel jnJInvoiceOrderModel : invoiceModelList)
			{
				invoiceDataList.add(jnJInvoiceOrderDataConverter.convert(jnJInvoiceOrderModel, new JnJInvoiceOrderData()));
			}
		}
		else
		{
			throw new BusinessException("Empty SAP Order No received. Returning Null");

		}
		return invoiceDataList;

	}

	@Override
	public File getInvoiceDocFile(final String fileType, final String invoiceId) throws BusinessException, IntegrationException
	{
		final String methodName = "getInvoiceDocFile";
		LOGGER.info("inside latam invoice facade method");

		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, methodName, Logging.BEGIN_OF_METHOD,
				JnjLatamInvoiceDocMapperImpl.class);
		File invoiceDocFile = null;
		URL website = null;
		ReadableByteChannel rbc = null;
		FileOutputStream fos = null;

		final ElectronicBillingResponse electronicBillingResponse = jnjLatamInvoiceDocMapper.getInvoiceDocMapper(fileType,
				invoiceId);
		if (hasUrl(electronicBillingResponse)) {
			LOGGER.info("URL from webservice----------" + electronicBillingResponse.getElectronicBillingRequestOut().getURL());
			final String sharedFolder = Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_ROOT)
					+ Config.getParameter(Jnjb2bCoreConstants.FEED_FILEPATH_OUTGOING)
					+ Config.getParameter(Jnjb2bCoreConstants.UploadFile.SFTP_SOURCE_ORDER_FOLDER);
			final List<String> urlList = electronicBillingResponse.getElectronicBillingRequestOut().getURL();
			final String urlOpenText = Config.getParameter(Jnjb2bCoreConstants.INVOICE_OPEN_TEXT_URL);
			final String hostOpenText = Config.getParameter(Jnjb2bCoreConstants.INVOICE_OPEN_TEXT_HOST);
			String newURLOpentext = "";
			invoiceDocFile = new File(sharedFolder,
					FilenameUtils.getName(invoiceId + Jnjb2bFacadesConstants.Logging.DOT_STRING + fileType));
			LOGGER.info("size of the URL List" + urlList.size());
			for (final String url : urlList)
			{
				try
				{


					newURLOpentext = parseCdataValue(url.replace(hostOpenText, urlOpenText));
					LOGGER.info(Jnjb2bFacadesConstants.Logging.GET_INVOICE_DOCUMENT + Logging.HYPHEN + "getInvoiceDocFile()"
							+ Logging.HYPHEN + " Final OpenText URL: ##############" + newURLOpentext);
					website = URI.create(newURLOpentext).toURL();
					rbc = Channels.newChannel(website.openStream());
					fos = new FileOutputStream(invoiceDocFile);
					fos.getChannel().transferFrom(rbc, 0, Jnjlab2bcoreConstants.BUFFER_SIZE);
				}
				catch (final FileNotFoundException fileNOteFoundException)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocFile()",
							"File Not Found Exception Occured" + fileNOteFoundException.getMessage(), JnjLatamInvoiceFacadeImpl.class);
					LOGGER.error("error while calling getInvoiceDocFile() " + fileNOteFoundException);
					throw new BusinessException("File Not Found Exception Occured", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
				catch (final MalformedURLException malformedURLException)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocFile()",
							"Mal Formed URL Exception Occured" + malformedURLException.getMessage(), JnjLatamInvoiceFacadeImpl.class);
					LOGGER.error("error while calling getInvoiceDocFile() " + malformedURLException);
					throw new BusinessException("Mal Formed URL Exception Occured", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
				catch (final IOException ioException)
				{
					JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocFile()",
							"Input Out Exception Occured" + ioException.getMessage(), JnjLatamInvoiceFacadeImpl.class);
					LOGGER.error("error while calling getInvoiceDocFile() " + ioException);
					throw new BusinessException("Input Out Exception Occured ", MessageCode.BUSINESS_EXCEPTION,
							Severity.BUSINESS_EXCEPTION);
				}
				finally
				{
					try
					{
						if (rbc != null)
						{
							rbc.close();
						}

					}
					catch (final IOException e)
					{
						JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, methodName,
								" Error while closing ReadableByteChannel", e, JnjLatamInvoiceFacadeImpl.class);
					}
					finally
					{
						try
						{
							if (fos != null)
							{
								fos.close();
							}
						}
						catch (final IOException e)
						{
							JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, methodName,
									" Error while closing FileOutputStream", e, JnjLatamInvoiceFacadeImpl.class);
						}
					}
				}
			}
		} else if (hasError(electronicBillingResponse)) { // code for logging the error message which comes from the SAP response
			JnjGTCoreUtil.logErrorMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocFile()",
					"Received error message from SAP and the error message is "
							+ electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue(),
					JnjLatamInvoiceFacadeImpl.class);

			throw new BusinessException(electronicBillingResponse.getElectronicBillingRequestOut().getERROR().getValue(),
					MessageCode.BUSINESS_EXCEPTION, Severity.BUSINESS_EXCEPTION);
		}

		JnjGTCoreUtil.logDebugMessage(Logging.GET_INVOICE_DOCUMENT, "getInvoiceDocFile()", Logging.END_OF_METHOD,
				JnjLatamInvoiceDocMapperImpl.class);
		return invoiceDocFile;
	}

	private static boolean hasUrl(ElectronicBillingResponse response) {
		return response != null &&
			response.getElectronicBillingRequestOut() != null &&
			response.getElectronicBillingRequestOut().getURL() != null &&
			!response.getElectronicBillingRequestOut().getURL().isEmpty();
	}

	private static boolean hasError(ElectronicBillingResponse response) {
		return response != null &&
			response.getElectronicBillingRequestOut() != null &&
			response.getElectronicBillingRequestOut().getERROR() != null &&
			StringUtils.isNotEmpty(response.getElectronicBillingRequestOut().getERROR().getValue());
	}

	@Override
	public Map<String, List<String>> uploadInvoiceFile(final MultipartFile[] submitOrderFileArray,
			final Map<String, String> fileStatusMap, final String salesOrder, final String poNum,
			final List<JnJInvoiceOrderData> invoiceList) throws BusinessException
	{
		return null;
	}

	@Override
	public Map<File, String> downloadInvoiceFile(final String salesOrder, final String fileName) throws BusinessException
	{
		return null;
	}

	@Override
	public SearchPageData<JnJLaInvoiceHistoryData> getInvoiceOrderData(String searchBy, String searchText, String fromDate, String toDate, Integer pageSize, Integer currentPage, String sortColumn, String sortDirection) {
		final SearchPageData<JnJInvoiceOrderModel> invoiceOrderModels = jnjInvoiceService.getInvoiceOrderData(searchBy, searchText, fromDate, toDate, pageSize, currentPage, sortColumn, sortDirection);
		return invoiceSearchPageToData(invoiceOrderModels);
	}

	@Override
	public SearchPageData<JnJLaInvoiceHistoryData> getInvoiceOrderData(String searchBy, String searchText, String fromDate, String toDate) {
		final SearchPageData<JnJInvoiceOrderModel> invoiceOrderModels = jnjInvoiceService.getInvoiceOrderData(searchBy, searchText, fromDate, toDate);
		return invoiceSearchPageToData(invoiceOrderModels);
	}

	@Override
	public List<JnJInvoiceOrderModel> getInvoicesByOrders(final List<OrderModel> order) {
		return jnjInvoiceService.getInvoicesByOrders(order);
	}

	private SearchPageData<JnJLaInvoiceHistoryData> invoiceSearchPageToData(SearchPageData<JnJInvoiceOrderModel> invoiceOrderModels) {
		final SearchPageData<JnJLaInvoiceHistoryData> invoiceOrderDatas = new SearchPageData<>();

		if (invoiceOrderModels != null) {
			invoiceOrderDatas.setResults(jnjLaInvoiceHistoryDataConverter.convertAll(invoiceOrderModels.getResults()));
			invoiceOrderDatas.setSorts(invoiceOrderModels.getSorts());
			invoiceOrderDatas.setPagination(invoiceOrderModels.getPagination());
		}

		return invoiceOrderDatas;
	}

	private String parseCdataValue(final String value)
	{

		return (value.replace("<![CDATA[", "")).replace("]]>", "");
	}


}
