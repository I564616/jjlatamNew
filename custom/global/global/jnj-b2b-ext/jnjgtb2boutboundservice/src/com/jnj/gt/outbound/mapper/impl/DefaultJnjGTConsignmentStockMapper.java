/**
 * 
 */
package com.jnj.gt.outbound.mapper.impl;

import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.xml.bind.JAXBElement;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.BATCHIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.CUSTOMERIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ConsignmentIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.MATERIALOUT;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.MATNRIN;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ObjectFactory;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockInput;
import com.jnj.hcswmd01.mu007_epic_viewconsstock_v1.viewconsignmentstockwebservice.ViewConsignmentStockOutput;
import com.jnj.gt.constants.Jnjgtb2boutboundserviceConstants.Logging;
import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.data.JnjGTInventoryReportResponseData;
import com.jnj.core.model.JnJProductModel;
import com.jnj.gt.outbound.mapper.JnjGTConsignmentStockMapper;
import com.jnj.gt.outbound.services.JnjGTConsignmentStockService;


/**
 * This class represents the mapper layer for the consignment stock flow.
 * 
 * @author Accenture
 * @version 1.0
 */
public class DefaultJnjGTConsignmentStockMapper implements JnjGTConsignmentStockMapper
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTConsignmentStockMapper.class);
	public static final ObjectFactory objectFactory = new ObjectFactory();

	@Autowired
	JnjGTConsignmentStockService jnjGTConsignmentStockService;

	@Resource(name = "productService")
	JnJGTProductService productService;

	public JnjGTConsignmentStockService getJnjGTConsignmentStockService() {
		return jnjGTConsignmentStockService;
	}

	public JnJGTProductService getProductService() {
		return productService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.outbound.mapper.JnjGTConsignmentStockMapper#mapConsignmentStockRequestResponse(com.jnj.core.dto.
	 * JnjGTPageableData)
	 */
	@Override
	public List<JnjGTInventoryReportResponseData> mapConsignmentStockRequestResponse(final JnjGTPageableData jnjGTPageableData)
			throws IntegrationException, SystemException
	{
		final String METHOD_NAME = "mapConsignmentStockRequestResponse()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONSIGNMENT_STOCK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjGTInventoryReportResponseData> responseList = new ArrayList<JnjGTInventoryReportResponseData>();
		JnjGTInventoryReportResponseData jnjGTInventoryReportResponseData = null;


		final ViewConsignmentStockInput viewConsignmentStockInput = new ViewConsignmentStockInput();

		final ConsignmentIN consignmentIN = new ConsignmentIN();

		consignmentIN.setCONSIGNATCUST("");

		final JAXBElement<String> returnAtCust = objectFactory.createConsignmentINRETURNATCUST("");
		consignmentIN.setRETURNATCUST(returnAtCust);

		final JAXBElement<String> zeroFlag = jnjGTPageableData.isSearchFlag() ? objectFactory.createConsignmentINZEROFLAG("X")
				: objectFactory.createConsignmentINZEROFLAG("");

		consignmentIN.setZEROFLAG(zeroFlag);
		consignmentIN.getCUSTOMERIN().addAll(fetchCustomerINData(jnjGTPageableData.getSearchParamsList()));

		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchBy()))
		{
			consignmentIN.getMATNRIN().addAll(fetchMatnrINData(jnjGTPageableData.getSearchBy()));
		}
		if (StringUtils.isNotEmpty(jnjGTPageableData.getSearchText()))
		{
			consignmentIN.getBATCHIN().addAll(fetchBATCHINData(jnjGTPageableData.getSearchText()));
		}
		viewConsignmentStockInput.setConsignmentIN(consignmentIN);
		final JAXBElement<String> transactionID = objectFactory.createViewConsignmentStockInputTransactionID("");
		viewConsignmentStockInput.setTransactionID(transactionID);

		final ViewConsignmentStockOutput viewConsignmentStockOutput = jnjGTConsignmentStockService
				.viewConsignmentStocks(viewConsignmentStockInput);
		if (null != viewConsignmentStockOutput && null != viewConsignmentStockOutput.getConsignmentOUT()
				&& CollectionUtils.isNotEmpty(viewConsignmentStockOutput.getConsignmentOUT().getMATERIALOUT()))
		{
			for (final MATERIALOUT materialout : viewConsignmentStockOutput.getConsignmentOUT().getMATERIALOUT())
			{
				if (StringUtils.isNotEmpty(materialout.getMATNUMBER()))
				{
					jnjGTInventoryReportResponseData = new JnjGTInventoryReportResponseData();
					jnjGTInventoryReportResponseData.setUcnNumber(materialout.getCUSTNUMBER());
					jnjGTInventoryReportResponseData.setProductCode(materialout.getMATNUMBER());
					
					
					
					JnJProductModel productModel;
					try
					{
						productModel = (JnJProductModel) productService.getProductForCode(materialout.getMATNUMBER());
						jnjGTInventoryReportResponseData.setDescription(productModel.getName());
						jnjGTInventoryReportResponseData.setProductURL(productService.getProductUrl(productModel));
						
						
					}
					catch (final UnknownIdentifierException e)
					{
						if (viewConsignmentStockOutput.getConsignmentOUT().getMATERIALOUT().size() >= 2)
						{
							LOGGER.error("The following product with code :" + materialout.getMATNUMBER()
									+ " %d was not present in the hybris database", e);
						}
						else
						{
							throw new IntegrationException();
						}
					}
					jnjGTInventoryReportResponseData.setLotNumber(materialout.getBATCHNUMBER());
					jnjGTInventoryReportResponseData.setUnrestricted(materialout.getUNRSTRCTDSTOCK());
					jnjGTInventoryReportResponseData.setRestricted(materialout.getRSTRCTDSTOCK());
					jnjGTInventoryReportResponseData.setQualityStock(materialout.getQUALITYSTOCK());
					jnjGTInventoryReportResponseData.setTotalQty(materialout.getTOTALSTOCK());
					jnjGTInventoryReportResponseData.setUnit(materialout.getUOM());
					
					
					responseList.add(jnjGTInventoryReportResponseData);
				}
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CONSIGNMENT_STOCK + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return responseList;
	}

	/**
	 * @param searchText
	 * @return List<BATCHIN>
	 */
	protected List<BATCHIN> fetchBATCHINData(final String searchText)
	{
		final List<BATCHIN> batchList = new ArrayList<BATCHIN>();
		final BATCHIN batchin = new BATCHIN();
		batchin.setHIGH("");
		batchin.setSIGN("I");
		batchin.setOPTION("EQ");
		batchin.setLOW(searchText);
		batchList.add(batchin);
		return batchList;
	}

	/**
	 * @param searchBy
	 * @return List<MATNRIN>
	 */
	protected List<MATNRIN> fetchMatnrINData(final String searchBy)
	{
		final List<MATNRIN> materialList = new ArrayList<MATNRIN>();
		final MATNRIN materialIN = new MATNRIN();
		materialIN.setHIGH("");
		materialIN.setSIGN("I");
		materialIN.setOPTION("EQ");
		materialIN.setLOW(searchBy);
		materialList.add(materialIN);
		return materialList;
	}

	
	/**
	 * @param searchParamsList
	 * @return List<CUSTOMERIN>
	 */
	protected List<CUSTOMERIN> fetchCustomerINData(final List<String> searchParamsList)
	{

		final List<CUSTOMERIN> customerList = new ArrayList<CUSTOMERIN>();
		for (final String accountNumber : searchParamsList)
		{
			final CUSTOMERIN customerIN = new CUSTOMERIN();
			customerIN.setHIGH("");
			customerIN.setSIGN("I");
			customerIN.setOPTION("EQ");
			customerIN.setLOW(accountNumber);

			customerList.add(customerIN);
		}
		return customerList;
	}
}
