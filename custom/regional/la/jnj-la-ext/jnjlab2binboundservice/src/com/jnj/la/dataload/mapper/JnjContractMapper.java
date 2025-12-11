/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.dataload.mapper;


import com.jnj.core.model.JnjIntegrationRSACronJobModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.ContractDocumentTypeEnum;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjContractEntryModel;
import com.jnj.core.model.JnjContractEntryPriceModel;
import com.jnj.core.model.JnjContractModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.unit.JnjUnitService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.LoadContract;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants.Logging;
import com.jnj.la.core.dto.JnjContractDTO;
import com.jnj.la.core.dto.JnjContractProductDTO;
import com.jnj.la.core.model.JnJLaProductModel;
import com.jnj.la.core.services.JnJLaProductService;
import com.jnj.la.core.services.contract.JnjContractService;
import com.jnj.la.core.util.JnjLaCommonUtil;
import com.jnj.la.core.util.JnjLaInterfaceOperationArchUtility;
import com.jnj.services.MessageService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.springframework.util.ObjectUtils;

/**
 * The Class JnjContractMapper maps the ContractsDTOs to the Hybris Models.
 *
 * @author Accenture
 * @version 1.0
 */
public class JnjContractMapper
{

	/** The Constant LOG. */
	private static final Logger LOG = Logger.getLogger(JnjContractMapper.class);

	/** The Constant LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT. */
	private static final String LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT = "processContracts()";

	/** The Constant LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS. */
	private static final String LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS = "mapJnjContractsToHybris()";

	/** The Constant LOAD_CONTRACTS_MAPPER_GET_PRODUCT_LIST. */
	private static final String LOAD_CONTRACTS_MAPPER_GET_CONTRACT_ENTRY_LIST = "getContractEntryList()";

	/** The Constant LOAD_CONTRACTS_MAPPER_GET_CONTRACT_PRICE. */
	private static final String LOAD_CONTRACTS_MAPPER_GET_CONTRACT_PRICE = "getContractPrice()";

	/** The Constant LOAD_CONTRACTS_MAPPER_GET_CURRENCY. */
	private static final String LOAD_CONTRACTS_MAPPER_GET_CURRENCY = "getCurrency()";

	/** The Constant METHOD_POPULATE_IMT_CONTRACT_MODEL. */
	private static final String METHOD_POPULATE_IMT_CONTRACT_MODEL = "populateImtContractModel()";

	/** The Constant METHOD_POPULATE_IMT_CONTRACT_ENTRY_MODEL. */
	private static final String METHOD_POPULATE_IMT_CONTRACT_ENTRY_MODEL = "populateImtContractEntryModel()";

	/** The Constant LOAD_CONTRACTS_PROCESS_IMT_CONTRACT. */
	private static final String LOAD_CONTRACTS_PROCESS_IMT_CONTRACT = "processImtRecord()";

	/** The Constant LOAD_CONTRACTS_UPDATE_IMT_CONTRACT_MODEL. */
	private static final String LOAD_CONTRACTS_UPDATE_IMT_CONTRACT_MODEL = "updateImtContractModel()";

	/** The jn j product service. */
	@Autowired
	private JnJLaProductService jnjLaProductService;

	/** The model service. */
	@Autowired
	private ModelService modelService;

	/** The b2b unit service. */
	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;
	//private B2BUnitService<CompanyModel, UserModel> b2bUnitService;

	/** The jnj contract service. */
	@Autowired
	private JnjContractService jnjContractService;

	/** The base store service. */
	@Autowired
	private BaseStoreService baseStoreService;

	/** The jnj unit service. */
	@Autowired
	private JnjUnitService jnjUnitService;

	/** The jnj interface operation arch utility. */
	//@Autowired
	//private JnjInterfaceOperationArchUtility jnjInterfaceOperationArchUtility;

	@Autowired
	protected MessageService messageService;

	@Autowired
	protected JnjLaInterfaceOperationArchUtility interfaceOperationArchUtility;
	
	@Autowired
	protected ConfigurationService configurationService;


	/**
	 * @return the interfaceOperationArchUtility
	 */
	public JnjLaInterfaceOperationArchUtility getInterfaceOperationArchUtility()
	{
		return interfaceOperationArchUtility;
	}

	/** The record status. */
	protected boolean recordStatus = false;


	/**
	 * Process contract. The method persists the DTO to the respective HYBRIS data models incorporating certain business
	 * logic.
	 *
	 */
	public void processContracts(final List<JnjContractDTO> listOfContracts, final JnjIntegrationRSACronJobModel cronjobmodel)
	{
		String recordStatusMessage = null;

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				"Begin of Processing Contracts from JnjImtContractModel with status [PENDING]", JnjContractMapper.class);

		//final int DEFAULT_RETRY_COUNT = 3;

		if ((null != listOfContracts) && !listOfContracts.isEmpty())
		{
			LOG.info("No of Contract fetched from RSA "+listOfContracts.size());
			//Processing Each JnjImtContractModel to JnjContractModel
			for (final JnjContractDTO jnjContractDTO : listOfContracts)
			{
				try
				{
					//Processing each Record
					recordStatus = processDTORecord(jnjContractDTO);


					if (recordStatus)
					{

						LOG.info("Record Processed Successfully for ---------" + jnjContractDTO.getCustomerNum());
						setLastsuccessfulDate(jnjContractDTO.getLastUpdatedDate(), cronjobmodel,
								"customer###" + jnjContractDTO.getCustomerNum());

					}
				}
				catch (final ParseException parseException)
				{
					LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT
							+ Logging.HYPHEN + "Error Occurred While Saving Data to Hybris.Dropping the record with Contract ID: ["
							+ jnjContractDTO.geteCCContractNum() + "] " + Logging.HYPHEN + parseException);
					recordStatus = false;
					recordStatusMessage = parseException.getMessage();
				}
				catch (final BusinessException businessException)
				{
					LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT
							+ Logging.HYPHEN + "Error Occurred While Saving Data to Hybris.Dropping the record with Contract ID: ["
							+ jnjContractDTO.geteCCContractNum() + "] " + Logging.HYPHEN + businessException);
					recordStatus = false;
					recordStatusMessage = businessException.getMessage();
				}
				catch (final Exception exception)
				{
					LOG.error(
							Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT + Logging.HYPHEN
									+ "Error Occurred While Saving Contract to Actual Models. Dropping the record with Contract ID: ["
									+ jnjContractDTO.geteCCContractNum() + "] " + Logging.HYPHEN + exception);
					recordStatus = false;
					recordStatusMessage = "Exception Occurred While Saving Contract to Actual Models. Dropping the record with Contract ID: ["
							+ jnjContractDTO.geteCCContractNum() + "] ";
				}

			}
		}
		else
		{
			JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
					"No JnjImtContractModel Records with status [PENDING] exists in Hybris. Exiting the write process.",
					JnjContractMapper.class);
		}
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				"End of Processing Contracts from JnjImtContractModel with status [PENDING]", JnjContractMapper.class);

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				Logging.END_OF_METHOD, JnjContractMapper.class);
	}


	/**
	 * Gets the jnjb2bunit.
	 *
	 * @param unitUid
	 *           the unit uid
	 * @return the jn j b2 b unit
	 */
	private JnJB2BUnitModel getJnJB2BUnit(final String unitUid)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);
		final JnJB2BUnitModel jnJB2BUnitModel = (JnJB2BUnitModel) jnjGTB2BUnitService.getUnitForUid(unitUid);
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_PROCESS_CONTRACT,
				Logging.END_OF_METHOD, JnjContractMapper.class);
		return jnJB2BUnitModel;

	}

	/**
	 * Process imt record.
	 *
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @return true, if successful
	 * @throws ParseException
	 *            the parse exception
	 * @throws BusinessException
	 *            the business exception
	 */
	private boolean processDTORecord(final JnjContractDTO jnjContractDTO) throws ParseException, BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_PROCESS_IMT_CONTRACT,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);

		recordStatus = false;
		JnjContractModel jnjContractModel;
		
		final String contractDocType = jnjContractDTO.getDocumentType();
		final String contractOrderReason = configurationService.getConfiguration().getString(Jnjlab2bcoreConstants.LoadContract.LOAD_CONTRACT_ORDER_REASONS_DOC+contractDocType);
		if(contractOrderReason != null && contractOrderReason.contains(jnjContractDTO.getContractOrderReason()))
		{
				JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_PROCESS_IMT_CONTRACT,
						"Contract with ID: " + jnjContractDTO.geteCCContractNum() + " has required Order Reason and Document Type",
						JnjContractMapper.class);
				jnjContractModel = jnjContractService.getContractDetailsById(jnjContractDTO.geteCCContractNum());

				if (null != jnjContractModel)
				{
					LOG.info("Contract with ID: exist in hybris "+jnjContractDTO.geteCCContractNum());
					JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_PROCESS_IMT_CONTRACT,
							"Contract with ID: " + jnjContractDTO.geteCCContractNum() + " exist in Hybris", JnjContractMapper.class);
					recordStatus = mapJnjContractsToHybris(jnjContractModel, jnjContractDTO);
				}
				else
				{
					JnjGTCoreUtil.logDebugMessage(
							Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_PROCESS_IMT_CONTRACT, "Contract with ID: "
									+ jnjContractDTO.geteCCContractNum() + " does not exist in Hybris, Creating New Contract.",
							JnjContractMapper.class);
					jnjContractModel = modelService.create(JnjContractModel.class);
					recordStatus = mapJnjContractsToHybris(jnjContractModel, jnjContractDTO);
					LOG.info("Contract with ID: does not exist in hybris "+jnjContractDTO.geteCCContractNum());
				}
		}
		else
		{
			LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_PROCESS_IMT_CONTRACT + Logging.HYPHEN
					+ " Document type " + jnjContractDTO.getDocumentType() + " and Order reason "
					+ jnjContractDTO.getContractOrderReason() + " are not accepted in the platform. ");
			recordStatus = false;
			throw new BusinessException(" Document type " + jnjContractDTO.getDocumentType() + " and Order reason "
					+ jnjContractDTO.getContractOrderReason() + " are not accepted in the platform. ");

		}
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_PROCESS_IMT_CONTRACT, Logging.END_OF_METHOD,
				JnjContractMapper.class);
		return recordStatus;
	}


	/**
	 * Map jnj contracts to hybris.
	 *
	 * @param jnjContractModel
	 *           the jnj contract model
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @return the jnj contract model
	 * @throws ParseException
	 *            the parse exception
	 * @throws BusinessException
	 *            the business exception
	 */
	@SuppressWarnings("deprecation")
	public boolean mapJnjContractsToHybris(final JnjContractModel jnjContractModel, final JnjContractDTO jnjContractDTO)
			throws ParseException, BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);
		recordStatus = false;
					
		if (StringUtils.isNotEmpty(jnjContractDTO.getCustomerNum()))
		{
			final JnJB2BUnitModel jnJB2BUnitModel = getJnJB2BUnit(jnjContractDTO.getCustomerNum());
			LOG.info("B2bunit from db "+jnJB2BUnitModel.getUid());
			if (null != jnJB2BUnitModel)
			{
				JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS,
						"B2bUnit with ID: " + jnjContractDTO.getCustomerNum() + " exists in Hybris.", JnjContractMapper.class);

				final SimpleDateFormat simpleStartDateFormat = new SimpleDateFormat(
						Jnjlab2bcoreConstants.LoadContract.LOAD_CONTRACT_START_DATE_FORMAT);

				Set<B2BUnitModel> finalAccountList = new HashSet<>();
				
				if (CollectionUtils.isNotEmpty(jnjContractModel
						.getAccounts())) {
					for (B2BUnitModel account : jnjContractModel
							.getAccounts()) {
						finalAccountList.add(account);
						
					}
				}				
				
				finalAccountList.add(jnJB2BUnitModel);
				jnjContractModel.setUnit(jnJB2BUnitModel);			
				jnjContractModel.setAccounts(finalAccountList);					

				jnjContractModel.setECCContractNum(jnjContractDTO.geteCCContractNum());
				jnjContractModel.setTenderNum(jnjContractDTO.getTenderNum());
				LOG.info("jnjContractModel.getStartDate() :: " + jnjContractModel.getStartDate());
				if (null != jnjContractDTO.getStartDate())
				{
					final Date startDate = simpleStartDateFormat.parse(jnjContractDTO.getStartDate());
					jnjContractModel.setStartDate(startDate);
				}
				LOG.info("jnjContractModel.getEndDate() :: " + jnjContractModel.getEndDate());
				if (null != jnjContractDTO.getEndDate())
				{
					final Date endDate = simpleStartDateFormat.parse(jnjContractDTO.getEndDate());
					jnjContractModel.setEndDate(endDate);
				}
				jnjContractModel.setContractOrderReason(jnjContractDTO.getContractOrderReason());

				if (jnjContractDTO.getDocumentType().equals(ContractDocumentTypeEnum.ZCQ.toString()))
				{
					jnjContractModel.setDocumentType(ContractDocumentTypeEnum.ZCQ);
				}
				else if (jnjContractDTO.getDocumentType().equals(ContractDocumentTypeEnum.ZCV.toString()))
				{
					jnjContractModel.setDocumentType(ContractDocumentTypeEnum.ZCV);

				}
				else if (jnjContractDTO.getDocumentType().equals(ContractDocumentTypeEnum.ZCI.toString()))
				{
					jnjContractModel.setDocumentType(ContractDocumentTypeEnum.ZCI);
				}
				jnjContractModel.setIndirectCustomer(jnjContractDTO.getIndirectCustomer());

				if (null != jnjContractDTO.getTotalAmount())
				{
					jnjContractModel.setTotalAmount(Double.valueOf(jnjContractDTO.getTotalAmount()));
				}
				if (null != jnjContractDTO.getBalanceAmount())
				{
					jnjContractModel.setBalanceAmount(Double.valueOf(jnjContractDTO.getBalanceAmount()));
				}
				//CP024 Changes: Setting Activation Flag Depending Upon the Logic
				if (StringUtils.isNotEmpty(jnjContractDTO.getStatus()))
				{
					jnjContractModel.setStatus(jnjContractDTO.getStatus());

					final Calendar todaysDate = Calendar.getInstance();
					// reset hour, minutes, seconds and millis
					todaysDate.set(Calendar.HOUR_OF_DAY, 0);
					todaysDate.set(Calendar.MINUTE, 0);
					todaysDate.set(Calendar.SECOND, 0);
					todaysDate.set(Calendar.MILLISECOND, 0);

					if (null != jnjContractModel.getEndDate())
					{
						if ((StringUtils.equalsIgnoreCase(jnjContractModel.getStatus(), LoadContract.LOAD_CONTRACT_STATUS_A)
								|| StringUtils.equalsIgnoreCase(jnjContractModel.getStatus(), LoadContract.LOAD_CONTRACT_STATUS_B))
								&& (jnjContractModel.getEndDate().after(todaysDate.getTime())
										|| jnjContractModel.getEndDate().equals(todaysDate.getTime()))
								&& (jnjContractModel.getStartDate().before(todaysDate.getTime())
										|| jnjContractModel.getStartDate().equals(todaysDate.getTime())))
						{
							jnjContractModel.setActive(Boolean.TRUE);
						}
						else if (StringUtils.equalsIgnoreCase(jnjContractModel.getStatus(), LoadContract.LOAD_CONTRACT_STATUS_C)
								|| (jnjContractModel.getEndDate().before(todaysDate.getTime())
										|| jnjContractModel.getStartDate().after(todaysDate.getTime())))
						{
							jnjContractModel.setActive(Boolean.FALSE);
						}
					}
					else
					{
						LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
								+ "Customer " + jnjContractDTO.getCustomerNum() + " is not in the platform.");
						recordStatus = false;
						throw new BusinessException(
								"No End Date associated with the contract with ID [" + jnjContractModel.getECCContractNum() + "]");
					}
				}
				else
				{
					LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
							+ "Customer " + jnjContractDTO.getCustomerNum() + " is not in the platform.");
					recordStatus = false;
					throw new BusinessException(
							"No Header Status associated with the contract with ID [" + jnjContractModel.getECCContractNum() + "]");
				}
				jnjContractModel.setJnjContractEntries(getContractEntryList(jnjContractDTO, jnjContractModel));
				recordStatus = jnjContractService.saveContract(jnjContractModel);
			}
			else
			{
				LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
						+ "Customer " + jnjContractDTO.getCustomerNum() + " is not in the platform.");
				recordStatus = false;
				throw new BusinessException("Customer " + jnjContractDTO.getCustomerNum() + " is not in the platform.");
			}
		}
		else
		{
			LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
					+ Logging.HYPHEN + "Received Null Customer No. in the feed record. Dropping the record.");
			recordStatus = false;
			throw new BusinessException("Received Null Customer No. in the feed record.");
		}
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS,
				Logging.END_OF_METHOD, JnjContractMapper.class);
		return recordStatus;
	}


	/**
	 * Gets the product list.
	 *
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @return the product list
	 * @throws BusinessException
	 *            the business exception
	 */
	private List<JnjContractEntryModel> getContractEntryList(final JnjContractDTO jnjContractDTO, final JnjContractModel jnjContractModel) throws BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_ENTRY_LIST,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);

		final List<JnjContractProductDTO> jnjContractProductDTOList = jnjContractDTO.getProductList();
		final List<JnjContractEntryModel> contractEntryModelList = new ArrayList<>();
		for (final JnjContractProductDTO jnjContractEntryDto : jnjContractProductDTOList)
		{
			final String productCode = jnjContractEntryDto.getMaterialNo();
			final JnJB2BUnitModel jnjB2BUnit = getJnJB2BUnit(jnjContractDTO.getCustomerNum());
			final String productSector = jnjLaProductService.getProductSector(productCode);
			final String customerSalesOrgCountry = JnjLaCommonUtil.getSalesOrgCustomerForProductSector(productSector, jnjB2BUnit);
			JnJProductModel jnJProductModel;
			if (customerSalesOrgCountry != null)
			{
				jnJProductModel = jnjLaProductService.getProduct(customerSalesOrgCountry, productCode);
			}
			else
			{
				JnjGTCoreUtil.logWarnMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_ENTRY_LIST,
						"Coundn't find a valid sales org from sector:" + productSector + " in customer: "
								+ jnjContractDTO.getCustomerNum()
								+ ". Falling back to determine the product's catalog version with the customer's country. This might cause problems for customers which buy from more than one store, such as AR. The current country for the customer is: "
								+ jnjB2BUnit.getCountry().getIsocode(),
						JnjContractMapper.class);
				jnJProductModel = jnjLaProductService.getProduct(jnjB2BUnit, jnjContractEntryDto.getMaterialNo());
			}

			final JnJLaProductModel jnjLaProductModel = (JnJLaProductModel) jnJProductModel;
			List<JnjContractEntryModel> contractEntries = Collections.emptyList();
			JnjContractEntryModel jnjContractEntryModel = null;
			if (CollectionUtils.isNotEmpty(jnjContractModel.getJnjContractEntries())) {
				contractEntries = jnjContractModel.getJnjContractEntries();
			}
			if (null != jnjLaProductModel)
				setContractData(jnjContractDTO, contractEntryModelList, jnjContractEntryDto, jnjLaProductModel, contractEntries, jnjContractEntryModel);
			else
			{
				LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
						+ Logging.HYPHEN + "Product with ID: " + jnjContractEntryDto.getMaterialNo() + " not peresent in Hybris");
				throw new BusinessException("Product with ID: " + jnjContractEntryDto.getMaterialNo() + " not peresent in Hybris");
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_ENTRY_LIST,
				Logging.END_OF_METHOD, JnjContractMapper.class);
		return contractEntryModelList;
	}

	private void setContractData(JnjContractDTO jnjContractDTO, List<JnjContractEntryModel> contractEntryModelList, JnjContractProductDTO jnjContractEntryDto, JnJLaProductModel jnjLaProductModel, List<JnjContractEntryModel> contractEntries, JnjContractEntryModel jnjContractEntryModel) throws BusinessException {
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_ENTRY_LIST,
				"Product With ID: " + jnjContractEntryDto.getMaterialNo() + " found in Hybris.", JnjContractMapper.class);

		boolean existingProduct = false;
		for (JnjContractEntryModel jnjContractEntry : contractEntries) {
			if (!ObjectUtils.isEmpty(jnjContractEntry.getProduct()) && StringUtils.isNotEmpty(jnjContractEntry.getProduct().getCode()) &&
					jnjContractEntry.getProduct().getCode().equals(jnjLaProductModel.getCode())) {
				jnjContractEntryModel = jnjContractEntry;
				existingProduct= true;
			}
		}
		if (jnjContractEntryModel == null) {
			jnjContractEntryModel = modelService.create(JnjContractEntryModel.class);
		}
		jnjContractEntryModel.setECCContractNum(jnjContractDTO.geteCCContractNum());
		jnjContractEntryModel.setProduct(jnjLaProductModel);
		jnjContractEntryModel.setStatus(jnjContractEntryDto.getStatus());
		if (null != jnjContractEntryDto.getTenderLineNum())
		{
			jnjContractEntryModel.setTenderLineNum(jnjContractEntryDto.getTenderLineNum());
		}
		jnjContractEntryModel.setContractPrice(getContractPrice(jnjContractDTO, jnjContractEntryDto));
		if (null != jnjContractEntryDto.getLineNum())
		{
			jnjContractEntryModel.setLineNum(Integer.valueOf((int) (Double.parseDouble(jnjContractEntryDto.getLineNum()))));
		}
		try
		{
			if (null != jnjContractEntryDto.getContractQty())
			{
				jnjContractEntryModel
						.setContractQty(Integer.valueOf((int) (Double.parseDouble(jnjContractEntryDto.getContractQty()))));
			}
			if (null != jnjContractEntryDto.getContractBalanceQty())
			{
				  jnjContractEntryModel.setContractBalanceQty(
						Integer.valueOf((int) (Double.parseDouble(jnjContractEntryDto.getContractBalanceQty()))));
			}
		}
		catch (final NumberFormatException numberFormatException)
		{
			LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_MAP_JNJ_CONTRACTS_TO_HYBRIS
					+ Logging.HYPHEN + "NumberFormatException Occurred:");
			throw new BusinessException("NumberFormatException Occurred:" + Logging.HYPHEN + numberFormatException);
		}
		if (existingProduct) {
			modelService.save(jnjContractEntryModel);
		}
		contractEntryModelList.add(jnjContractEntryModel);
	}


	/**
	 * Gets the contract price.
	 *
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @param jnjImtContractEntryModel
	 *           the jnj imt contract entry model
	 * @return the contract price
	 * @throws BusinessException
	 *            the business exception
	 */
	private JnjContractEntryPriceModel getContractPrice(final JnjContractDTO jnjImtContractModel,
			final JnjContractProductDTO jnjImtContractEntryModel) throws BusinessException
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_PRICE,
				Logging.BEGIN_OF_METHOD, JnjContractMapper.class);
		final JnjContractEntryPriceModel jnjContractEntryPriceModel = modelService.create(JnjContractEntryPriceModel.class);
		if (null != jnjImtContractEntryModel)
		{
			if (null != jnjImtContractEntryModel.getNetPrice())
			{
				jnjContractEntryPriceModel.setPrice(Double.valueOf(jnjImtContractEntryModel.getNetPrice()));
			}
			jnjContractEntryPriceModel.setCurrency(getCurrency(jnjImtContractModel));
			if (StringUtils.isNotEmpty(jnjImtContractEntryModel.getContractUnit()))
			{
				final UnitModel contractUom = jnjUnitService.getUnit(jnjImtContractEntryModel.getContractUnit());
				if (null != contractUom)
				{
					jnjContractEntryPriceModel.setUnit(contractUom);
				}
				else
				{
					LOG.error(Logging.LOAD_CONTRACTS_INTERFACE + Logging.HYPHEN + LOAD_CONTRACTS_MAPPER_GET_CONTRACT_PRICE
							+ Logging.HYPHEN + "Unit with Code: " + jnjImtContractEntryModel.getContractUnit()
							+ "not peresent in Hybris. Skipping Record");
					throw new BusinessException(
							"Unit with Code: " + jnjImtContractEntryModel.getContractUnit() + "not peresent in Hybris. Skipping Record");
				}
			}
		}
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CONTRACT_PRICE,
				Logging.END_OF_METHOD, JnjContractMapper.class);
		return jnjContractEntryPriceModel;
	}

	public void setLastsuccessfulDate(final String lastsuccessfulDate, final JnjIntegrationRSACronJobModel cronjobmodel, final String data)
	{

		LOG.info("Contract Data processed successfully for --------" + data);

		getInterfaceOperationArchUtility().setLastSuccesfulStartTimeForJob(lastsuccessfulDate, cronjobmodel);

	}

	/**
	 * Gets the currency.
	 *
	 * @param jnjImtContractModel
	 *           the jnj imt contract model
	 * @return the currency
	 */
	private CurrencyModel getCurrency(final JnjContractDTO jnjImtContractModel)
	{
		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CURRENCY, Logging.BEGIN_OF_METHOD,
				JnjContractMapper.class);

		final String countryIso = getJnJB2BUnit(jnjImtContractModel.getCustomerNum()).getCountry().getIsocode();
		final String baseStoreID = JnjLaCommonUtil.getIdByCountry(countryIso) + Jnjlab2bcoreConstants.BASE_STORE;
		final CurrencyModel currencyModel = baseStoreService.getBaseStoreForUid(baseStoreID).getDefaultCurrency();

		JnjGTCoreUtil.logDebugMessage(Logging.LOAD_CONTRACTS_INTERFACE, LOAD_CONTRACTS_MAPPER_GET_CURRENCY, Logging.END_OF_METHOD,
				JnjContractMapper.class);
		return currencyModel;

	}
}
