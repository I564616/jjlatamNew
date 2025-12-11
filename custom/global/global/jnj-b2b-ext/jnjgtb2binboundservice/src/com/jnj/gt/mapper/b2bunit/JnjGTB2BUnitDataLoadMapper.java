/**
 *
 */
package com.jnj.gt.mapper.b2bunit;

import de.hybris.platform.core.enums.Gender;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.europe1.enums.UserPriceGroup;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.commons.MessageCode;
import com.jnj.commons.Severity;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.ClassOfTradeGroupModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjGTSalesOrgCustomerModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.B2BUnit;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitLocalModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.model.JnjGTIntPartnerFuncModel;
import com.jnj.gt.model.JnjGTIntSalesOrgModel;
import com.jnj.core.services.address.JnjGTAddressService;
import com.jnj.gt.service.b2bunit.JnjGTB2BUnitFeedService;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;



/**
 * The JnjGTB2BUnitDataLoadMapper class contains all those methods which map the customer related intermediate tables
 * data with Hybris B2B model(JnJB2BUnitModel).
 * 
 * @author sumit.y.kumar
 * 
 */
public class JnjGTB2BUnitDataLoadMapper extends JnjAbstractMapper
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTB2BUnitDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTB2BUnitFeedService jnjGTB2BUnitFeedService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private CommonI18NService commonI18NService;

	@Autowired
	@Resource(name = "jnjGTAddressService")
	private JnjGTAddressService jnjGTAddressService;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private UserService userService;

	/**
	 * {!@inheritDoc}
	 * 
	 */
	@Override
	public void processIntermediateRecords()
	{
		System.out.println("JnjGTB2BUnitDataLoadMapper processIntermediateRecords() START");
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Call the private method for sold to records.
		mapCustomerDataForSoldTo();
		// Call the private method for ship to records.
		mapCustomerDataForShipTo();
		// Call the private method for bill to records.
		mapCustomerDataForBillTo();
		// Call the private method for pay from records.
		//mapCustomerDataForPayFrom();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Map customer data for those records for which Sold To flag is true.
	 * 
	 * @return true, if successful
	 */
	public boolean mapCustomerDataForSoldTo()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		boolean recordStatus = false;
		Set<AddressModel> newAdressCollection = null;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntB2BUnitModel> jnjGTIntB2BUnitModelList = jnjGTB2BUnitFeedService.getJnjGTIntB2BUnitModel(
				RecordStatus.PENDING, B2BUnit.SOLD_TO_INDC);
		if (!jnjGTIntB2BUnitModelList.isEmpty())
		{
			for (final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel : jnjGTIntB2BUnitModelList)
			{
				try
				{
					recordStatus = false;
					if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getSourceSysId())
							&& StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getUid()))
					{
						// Call To fetch the source system id
						final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2BUnitModel.getSourceSysId());
						if (StringUtils.isNotEmpty(sourceSysId))
						{
							JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(jnjGTIntB2BUnitModel.getUid(),
									sourceSysId);
							if (JnJB2BUnitModel == null)
							{
								JnJB2BUnitModel = modelService.create(JnJB2BUnitModel.class);
								//Setting the source system id
								JnJB2BUnitModel.setSourceSysId(sourceSysId);
								//Setting the Id
								JnJB2BUnitModel.setUid(jnjGTIntB2BUnitModel.getUid());
								//Setting the default parent b2b unit for every unit.
								final Set<PrincipalGroupModel> principalGrpModelSet = new HashSet<PrincipalGroupModel>();
								principalGrpModelSet.add(jnjGTB2BUnitService
										.getUnitForUid(Jnjb2bCoreConstants.UserSearch.GLOBAL_ACCOUNT_ID));
								JnJB2BUnitModel.setGroups(principalGrpModelSet);
								newAdressCollection = new HashSet<AddressModel>();
							}
							else
							{
								newAdressCollection = new HashSet<AddressModel>(JnJB2BUnitModel.getAddresses());
							}

							//Map the intermediate data in B2BUnit model.
							JnJB2BUnitModel = createB2BUnitByJnjGTB2BUnit(jnjGTIntB2BUnitModel, JnJB2BUnitModel);
							//Mapping Address Model From Intermediate Table
							final AddressModel addressModel = extractAdressFromIntB2BUnitModel(jnjGTIntB2BUnitModel, JnJB2BUnitModel);

							// Contact Address is set in JnJB2BUnitModel.
							JnJB2BUnitModel.setContactAddress(addressModel);
							// In case of MDD we are supposed to set shipping address as well.


							if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId))
							{
								JnJB2BUnitModel.setShippingAddress(addressModel);
							}


							newAdressCollection.add(addressModel);
							// To populate those addresses for which Jnj Address Id comes in Customer Table after the creation of empty address through Affiliation or Partner Function Table.
							final List<AddressModel> updatedEmptyAddressModels = jnjGTAddressService.getAddressByIdandSourceSysId(
									jnjGTIntB2BUnitModel.getUid(), sourceSysId);
							if (CollectionUtils.isNotEmpty(updatedEmptyAddressModels))
							{
								for (final AddressModel updatedEmptyAddressModel : updatedEmptyAddressModels)
								{
									if (updatedEmptyAddressModel instanceof AddressModel)
									{
										final AddressModel AddressModel = updatedEmptyAddressModel;
										// populate the empty address model with current b2b unit address data.
										mapIntAddressDataWithAddressModel(AddressModel, jnjGTIntB2BUnitModel);
										jnjGTB2BUnitService.saveItemModel(AddressModel);
									}
								}
							}

							// check for the source system id if its consumer then enter in the if block
							/*
							 * if (JnjGTSourceSysId.CONSUMER.toString().equals(sourceSysId)) { // Mapped the Sales Org Info in
							 * the B2BUnit Model. recordStatus = mapSalesOrgWithB2BUnit(JnJB2BUnitModel); // Mapped the Partner
							 * Function Info in the B2BUnit Model. recordStatus = mapPartnerFuncWithB2BUnit(JnJB2BUnitModel,
							 * newAdressCollection, addressModel); }
							 */// else check for the mdd if its mdd then enter in the below mentioned else if block
							if (JnjGTSourceSysId.MDD.toString().equals(sourceSysId))
							{
								// Call the below method if we want to call from Customer master cronjob by passing the isCronJob flag as false.
								/*
								 * recordStatus = mapAffiliationDataWithB2BUnit(JnJB2BUnitModel, newAdressCollection,
								 * addressModel, false, Logging.CUSTOMER_MASTER_FEED);
								 */
								recordStatus = mapPartnerFuncWithB2BUnit(JnJB2BUnitModel, newAdressCollection, addressModel);
							}
							JnJB2BUnitModel.setAddresses(newAdressCollection);
							// Save the JnJB2BUnitModel model.
							recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
							if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getCustStatusCde())
									&& "0".equals(jnjGTIntB2BUnitModel.getCustStatusCde()))
							{
								JnJB2BUnitModel.setActive(Boolean.FALSE);
								recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
							}
							else
							{
								JnJB2BUnitModel.setActive(Boolean.TRUE);
								recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
							}
						}
						else
						{
							errorMessage = "Source system id is empty or null";
						}
					}
					else
					{
						errorMessage = "Uid or source system id is empty or null";
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final BusinessException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
							+ "Business Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, null, true, errorMessage,
							Logging.CUSTOMER_MASTER_FEED, jnjGTIntB2BUnitModel.getUid());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " was not processed successfully and error message is: " + errorMessage);
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CUSTOMER_MASTER_FEED
						+ Logging.HYPHEN
						+ "mapCustomerDataForSoldTo()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Sold To'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return recordStatus;
	}

	/**
	 * Map customer data for those records for which ship To flag is true.
	 * 
	 * @return true, if successful
	 */
	public boolean mapCustomerDataForShipTo()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		boolean recordStatus = false;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntB2BUnitModel> JnjGTIntB2BUnitModelList = jnjGTB2BUnitFeedService.getJnjGTIntB2BUnitModel(
				RecordStatus.PENDING, B2BUnit.SHIP_TO_INDC);
		if (!JnjGTIntB2BUnitModelList.isEmpty())
		{
			for (final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel : JnjGTIntB2BUnitModelList)
			{
				try
				{
					recordStatus = false;
					// Call To fetch the source system id
					final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2BUnitModel.getSourceSysId());
					final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(
							jnjGTIntB2BUnitModel.getUid(), sourceSysId);

					if (!addressModelList.isEmpty())
					{
						for (final AddressModel newAddressModel : addressModelList)
						{
							if (newAddressModel instanceof AddressModel)
							{
								final AddressModel AddressModel = newAddressModel;
								mapIntAddressDataWithAddressModel(AddressModel, jnjGTIntB2BUnitModel);
								populatesPartnerInfo(AddressModel, B2BUnit.SHIP_TO_INDC);
								recordStatus = jnjGTB2BUnitService.saveItemModel(newAddressModel);
							}
						}
					}
					else
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
									+ "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid());
						}
						errorMessage = "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid();
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, null, true, errorMessage,
							Logging.CUSTOMER_MASTER_FEED, jnjGTIntB2BUnitModel.getUid());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " was not processed successfully.");
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CUSTOMER_MASTER_FEED
						+ Logging.HYPHEN
						+ "mapCustomerDataForShipTo()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Ship To'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForShipTo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Map customer data for those records for which Bill To flag is true.
	 * 
	 * @return true, if successful
	 */
	public boolean mapCustomerDataForBillTo()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		boolean recordStatus = false;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntB2BUnitModel> JnjGTIntB2BUnitModelList = jnjGTB2BUnitFeedService.getJnjGTIntB2BUnitModel(
				RecordStatus.PENDING, B2BUnit.BILL_TO_INDC);
		if (!JnjGTIntB2BUnitModelList.isEmpty())
		{
			for (final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel : JnjGTIntB2BUnitModelList)
			{
				try
				{
					recordStatus = false;
					final JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(jnjGTIntB2BUnitModel.getUid(),
							jnjGTIntB2BUnitModel.getSourceSysId());
					// Call To fetch the source system id
					final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2BUnitModel.getSourceSysId());
					/*
					 * final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(
					 * jnjGTIntB2BUnitModel.getUid(), sourceSysId);
					 */
					final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(
							jnjGTIntB2BUnitModel.getUid(), sourceSysId);

					if (!addressModelList.isEmpty())
					{
						for (final AddressModel newAddressModel : addressModelList)
						{
							if (newAddressModel instanceof AddressModel)
							{
								final AddressModel AddressModel = newAddressModel;
								mapIntAddressDataWithAddressModel(AddressModel, jnjGTIntB2BUnitModel);
								populatesPartnerInfo(AddressModel, B2BUnit.BILL_TO_INDC);
								recordStatus = jnjGTB2BUnitService.saveItemModel(newAddressModel);
							}
						}
					}
					else
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
									+ "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid());
						}
						errorMessage = "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid();
						//Suparno code to save billing address
						final Collection<AddressModel> newAdressCollection = new ArrayList<AddressModel>();
						System.out.println("Will create a new Billing Address for this Customer Number -"
								+ jnjGTIntB2BUnitModel.getUid());
						final AddressModel addressModel = modelService.create(AddressModel.class);
						final AddressModel newAddressModel = mapIntAddressDataWithAddressModel(addressModel, jnjGTIntB2BUnitModel);
						final List<JnjGTIntPartnerFuncModel> jnjGTIntPartFuncModelList = jnjGTB2BUnitFeedService
								.getJnjGTIntPartnerFuncModel(jnjGTIntB2BUnitModel.getUid());
						List<JnjGTIntPartnerFuncModel> jnjGTIntPartFuncModelListByPartnerCustomerNumberList = null;

						if (jnjGTIntPartFuncModelList.isEmpty() == true)
						{
							jnjGTIntPartFuncModelListByPartnerCustomerNumberList = jnjGTB2BUnitFeedService
									.getJnjGTIntPartnerFuncModelByPartnerCustomerNumber(jnjGTIntB2BUnitModel.getUid());
							if (jnjGTIntPartFuncModelListByPartnerCustomerNumberList != null
									&& jnjGTIntPartFuncModelListByPartnerCustomerNumberList.isEmpty() != true)
							{
								for (final JnjGTIntPartnerFuncModel jnjGTIntPartnerFuncModel : jnjGTIntPartFuncModelListByPartnerCustomerNumberList)
								{
									if (StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartCustNo())
											&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartFuncName()))
									{
										if (jnjGTIntPartnerFuncModel != null
												&& jnjGTIntPartnerFuncModel.getPartFuncName().equalsIgnoreCase("Bill-to Primary"))
										{
											//newAddressModel.setPrimaryBillTo(Boolean.TRUE);
										}
										else
										{
											//newAddressModel.setPrimaryBillTo(Boolean.FALSE);
										}
										/*
										 * populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel,
										 * jnjGTIntPartnerFuncModel.getPartCustNo(), jnjGTIntPartnerFuncModel.getPartFuncCode(),
										 * false);
										 */
										populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, newAddressModel,
												jnjGTIntPartnerFuncModel, false);

									}
								}
							}
						}
						else
						{
							for (final JnjGTIntPartnerFuncModel jnjGTIntPartnerFuncModel : jnjGTIntPartFuncModelList)
							{
								if (StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartCustNo())
										&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartFuncCode()))
								{
									/*
									 * populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel,
									 * jnjGTIntPartnerFuncModel.getPartCustNo(), jnjGTIntPartnerFuncModel.getPartFuncCode(),
									 * false);
									 */
									populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel, jnjGTIntPartnerFuncModel,
											false);

								}
							}
						}
						//populatesPartnerInfo(newAddressModel, B2BUnit.BILL_TO_INDC);
						//recordStatus = jnjGTB2BUnitService.saveItemModel(newAddressModel);
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
							+ "Model Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, null, true, errorMessage,
							Logging.CUSTOMER_MASTER_FEED, jnjGTIntB2BUnitModel.getUid());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " was not processed successfully.");
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CUSTOMER_MASTER_FEED
						+ Logging.HYPHEN
						+ "mapCustomerDataForBillTo()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Bill To'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForBillTo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Map customer data for those records for which Pay From flag is true.
	 * 
	 * @return true, if successful
	 */
	public boolean mapCustomerDataForPayFrom()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		boolean recordStatus = false;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntB2BUnitModel> JnjGTIntB2BUnitModelList = jnjGTB2BUnitFeedService.getJnjGTIntB2BUnitModel(
				RecordStatus.PENDING, B2BUnit.PAY_FROM_INDC);
		if (!JnjGTIntB2BUnitModelList.isEmpty())
		{
			for (final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel : JnjGTIntB2BUnitModelList)
			{
				try
				{
					recordStatus = false;
					// Call To fetch the source system id
					final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2BUnitModel.getSourceSysId());
					final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(
							jnjGTIntB2BUnitModel.getUid(), sourceSysId);

					if (!addressModelList.isEmpty())
					{
						for (final AddressModel newAddressModel : addressModelList)
						{
							if (newAddressModel instanceof AddressModel)
							{
								final AddressModel AddressModel = newAddressModel;
								mapIntAddressDataWithAddressModel(AddressModel, jnjGTIntB2BUnitModel);
								populatesPartnerInfo(AddressModel, B2BUnit.PAY_FROM_INDC);
								recordStatus = jnjGTB2BUnitService.saveItemModel(newAddressModel);
							}
						}
					}
					else
					{
						if (LOGGER.isDebugEnabled())
						{
							LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
									+ "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid());
						}
						errorMessage = "AddressModel list is empty for this Customer Number -" + jnjGTIntB2BUnitModel.getUid();
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitModel, null, true, errorMessage,
							Logging.CUSTOMER_MASTER_FEED, jnjGTIntB2BUnitModel.getUid());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntB2BUnitModel.getUid()
								+ " was not processed successfully.");
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CUSTOMER_MASTER_FEED
						+ Logging.HYPHEN
						+ "mapCustomerDataForPayFrom()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Pay From'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapCustomerDataForPayFrom()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Creates the b2b unit by jnj na b2b unit.
	 * 
	 * @param jnjGTIntB2BUnitModel
	 *           the jnj na int b2 b unit model
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @return the jnj na b2 b unit model
	 */
	private final JnJB2BUnitModel createB2BUnitByJnjGTB2BUnit(final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel,
			final JnJB2BUnitModel JnJB2BUnitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "createB2BUnitByJnjGTB2BUnit()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		// To do We will put the name in all the three Locale
		if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getCustName1()))
		{
			JnJB2BUnitModel.setName(getB2BUnitName(jnjGTIntB2BUnitModel.getCustName1(), jnjGTIntB2BUnitModel.getCustName2()));
			JnJB2BUnitModel.setLocName(getB2BUnitName(jnjGTIntB2BUnitModel.getCustName1(), jnjGTIntB2BUnitModel.getCustName2()));
		}
		if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getShortCustName1()))
		{
			JnJB2BUnitModel.setShortCustName(getB2BUnitName(jnjGTIntB2BUnitModel.getShortCustName1(),
					jnjGTIntB2BUnitModel.getShortCustName2()));
		}
		if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getTradeCode()))
		{
			JnJB2BUnitModel.setClassOfTrade(jnjGTIntB2BUnitModel.getTradeCode());
			// Fetch the list of config model and get the value from the first element if the list is not empty or null.
			final List<JnjConfigModel> jnjConfigModelList = jnjConfigService.getConfigModelsByIdAndKey(B2BUnit.CLASS_OF_TRADE_ID,
					jnjGTIntB2BUnitModel.getTradeCode());
			if (CollectionUtils.isNotEmpty(jnjConfigModelList))
			{
				final String indicator = jnjConfigModelList.get(0).getValue();
				JnJB2BUnitModel.setIndicator(indicator);
				// set COT as per the indicator.
				final UserGroupModel userGroupModel = userService.getUserGroupForUID(indicator);
				if (null != userGroupModel && userGroupModel instanceof ClassOfTradeGroupModel)
				{
					final ClassOfTradeGroupModel cotGrp = (ClassOfTradeGroupModel) userGroupModel;
					final Set<PrincipalGroupModel> allGroups = new HashSet<PrincipalGroupModel>(JnJB2BUnitModel.getGroups());
					allGroups.add(cotGrp);
					JnJB2BUnitModel.setGroups(allGroups);
				}
			}
		}
		JnJB2BUnitModel.setTradeCodeName(jnjGTIntB2BUnitModel.getTradeName());
		JnJB2BUnitModel.setSalesRepLocNo(jnjGTIntB2BUnitModel.getSalesRepLocNo());
		JnJB2BUnitModel.setGlobalLocNo(jnjGTIntB2BUnitModel.getGlobalLocNo());
		//JnJB2BUnitModel.setTaxId(jnjGTIntB2BUnitModel.getTaxId());
		//JnJB2BUnitModel.setSendMethod(jnjGTIntB2BUnitModel.getSendMethod());
		//JnJB2BUnitModel.setExpCustomer(jnjGTIntB2BUnitModel.getExpCustomer());
		JnJB2BUnitModel.setCountry(getCountry(jnjGTIntB2BUnitModel));

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "createB2BUnitByJnjGTB2BUnit()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return JnJB2BUnitModel;
	}

	/**
	 * Gets the b2b unit name.
	 * 
	 * @param firstName
	 *           the first name
	 * @param secondName
	 *           the second name
	 * @return the b2b unit name
	 */
	private String getB2BUnitName(final String firstName, final String secondName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getB2BUnitName()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String finalName = StringUtils.EMPTY;

		if (StringUtils.isNotEmpty(firstName))
		{
			finalName = firstName;
		}
		if (StringUtils.isNotEmpty(secondName))
		{
			finalName = finalName.concat(" ").concat(secondName);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getB2BUnitName()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return finalName.trim();
	}

	/**
	 * This method is used to create address model for customer number.
	 * 
	 * @param jnjGTIntB2BUnitModel
	 *           the jnj na int b2 b unit model
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @return addressModel AddressModel
	 * @throws BusinessException
	 *            the business exception
	 */
	private AddressModel extractAdressFromIntB2BUnitModel(final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel,
			final JnJB2BUnitModel JnJB2BUnitModel) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "extractAdressFromIntB2BUnitModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		AddressModel AddressModel = null;
		try
		{
			if (null != JnJB2BUnitModel.getPk())
			{
				AddressModel = jnjGTAddressService.getAddressForJnjAddressId(JnJB2BUnitModel, jnjGTIntB2BUnitModel.getUid());
			}
			if (AddressModel == null)
			{
				AddressModel = modelService.create(AddressModel.class);
				AddressModel.setSourceSysId(JnJB2BUnitModel.getSourceSysId());
			}

			AddressModel = mapIntAddressDataWithAddressModel(AddressModel, jnjGTIntB2BUnitModel);
			AddressModel.setContactAddress(Boolean.TRUE);
			System.out.println("Groups for JnJB2BUnitModel :: " + JnJB2BUnitModel.getGroups());
			if (JnJB2BUnitModel.getGroups() == null)
			{
				final Set<PrincipalGroupModel> principalGroups = new HashSet<PrincipalGroupModel>();
				JnJB2BUnitModel.setGroups(principalGroups);
			}
			else
			{
				final Set<PrincipalGroupModel> principalGroups = new HashSet<PrincipalGroupModel>();
				JnJB2BUnitModel.setGroups(principalGroups);
			}
			AddressModel.setOwner(JnJB2BUnitModel);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "extractAdressFromIntB2BUnitModel()" + Logging.HYPHEN
					+ "model not found for given key -" + modelNotFoundException.getMessage(), modelNotFoundException);
			throw new BusinessException(modelNotFoundException.getMessage(), MessageCode.BUSINESS_EXCEPTION,
					Severity.BUSINESS_EXCEPTION);
		}
		catch (final UnknownIdentifierException unknownIdentifierException)
		{
			LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "extractAdressFromIntB2BUnitModel()" + Logging.HYPHEN
					+ "ISO Code not matched found for given key -" + unknownIdentifierException.getMessage(),
					unknownIdentifierException);
			throw new BusinessException(unknownIdentifierException.getMessage(), MessageCode.BUSINESS_EXCEPTION,
					Severity.BUSINESS_EXCEPTION);
		}
		try
		{

			//AddressModel.setAppartment("TestApartment");
			//AddressModel.setBuilding("TestBuilding");
			//AddressModel.setCellphone("TestCellPhone");
			final CountryModel countryModel = new CountryModel();
			countryModel.setName("United kingdom of Belgium", Locale.ENGLISH);
			countryModel.setIsocode("BET2");
			//AddressModel.setCountry(countryModel);
			//AddressModel.setGlobalLocNo("TestGlobalLocNo");
			//AddressModel.setDistrict("TestDistrict");
			//AddressModel.setFax("TestFax");
			AddressModel.setDepartment("IT");
			final RegionModel regionModel = new RegionModel();
			//Locale loc = new Locale();
			regionModel.setName("TestRegionBrussels", Locale.ENGLISH);
			regionModel.setIsocode("BR2");
			regionModel.setCountry(countryModel);
			//AddressModel.setRegion(regionModel);
			//final List<CommentModel> commentModelList = new ArrayList<CommentModel>();
			//final CommentModel cm1 = new CommentModel();
			//cm1.setText("test comment 1");
			//final CommentModel cm2 = new CommentModel();
			//cm2.setText("test comment 2");
			//commentModelList.add(cm1);
			//commentModelList.add(cm2);
			//AddressModel.setComments(commentModelList);
			//AddressModel.setJnjGTState("TestState");
			//AddressModel.setPublicKey("TestPublicKey");
			//AddressModel.setUnloadingAddress(new Boolean(false));
			//AddressModel.setCompany("TestCognizant");
			//AddressModel.setDateofbirth(new Date());
			//AddressModel.setDateOfBirth(new Date());
			//AddressModel.setContactAddress(new Boolean(false));
			//AddressModel.setDuplicate(Boolean.FALSE);
			AddressModel.setEmail("test@test.com");
			//AddressModel.setShippingAddress(Boolean.TRUE);
			AddressModel.setGender(Gender.MALE);
			//AddressModel.setOriginal(AddressModel);
			//AddressModel.setJnjGTState("TestState");
			//AddressModel.setVisibleInAddressBook(Boolean.TRUE);
			final TitleModel titleModel = new TitleModel();
			titleModel.setCode("test_mr2");
			titleModel.setName("test_mr2", Locale.ENGLISH);
			//AddressModel.setTitle(titleModel);
			//AddressModel.setOwner(AddressModel);

			System.out.println(AddressModel.getAppartment() + " " + AddressModel.getBuilding() + " " + AddressModel.getCellphone()
					+ " " + AddressModel.getCountyName() + " " + AddressModel.getDistrict() + " " + AddressModel.getFax() + " "
					+ AddressModel.getDepartment() + " " + AddressModel.getGlobalLocNo() + " " + AddressModel.getJnJAddressId() + " "
					+ AddressModel.getJnjGTState() + " " + AddressModel.getPublicKey() + " " + AddressModel.getSourceSysId() + " "
					+ AddressModel.getComments() + " " + AddressModel.getTitle() + " "
					+ AddressModel.getDeliveryAddresss2CartToOrderCronJob() + " "
					+ AddressModel.getPaymentAddresss2CartToOrderCronJob());

			AddressModel.toString();
			jnjGTB2BUnitService.saveItemModel(AddressModel);
		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "extractAdressFromIntB2BUnitModel()" + Logging.HYPHEN
					+ "Model Not saved into data base -" + modelSavingException.getMessage(), modelSavingException);
			throw new BusinessException(modelSavingException.getMessage(), MessageCode.BUSINESS_EXCEPTION,
					Severity.BUSINESS_EXCEPTION);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "extractAdressFromIntB2BUnitModel()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return AddressModel;
	}

	/**
	 * Map sales org with JnjGTB2BUnit Model.
	 * 
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @return true, if successful
	 */
	private boolean mapSalesOrgWithB2BUnit(final JnJB2BUnitModel JnJB2BUnitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgWithB2BUnit()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isSalesOrgDataMapped = false;
		JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel = null;
		Set<JnjGTSalesOrgCustomerModel> jnjGTSalesOrgCustomerModels = null;
		final List<JnjGTIntSalesOrgModel> jnjGTIntSalesOrgModelList = jnjGTB2BUnitFeedService
				.getJnjGTIntSalesOrgModel(JnJB2BUnitModel.getUid());
		// Iterate the intermediate model list one by one.
		for (final JnjGTIntSalesOrgModel jnjGTIntSalesOrgModel : jnjGTIntSalesOrgModelList)
		{
			boolean isModelExisted = false;
			if (StringUtils.isNotEmpty(jnjGTIntSalesOrgModel.getSourceSysId())
					&& StringUtils.isNotEmpty(jnjGTIntSalesOrgModel.getSalesOrg())
					&& StringUtils.isNotEmpty(jnjGTIntSalesOrgModel.getDistChannel())
					&& StringUtils.isNotEmpty(jnjGTIntSalesOrgModel.getDivision()))
			{
				// Call To fetch the source system id
				final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSalesOrgModel.getSourceSysId());

				// check for the Sales Org Customers list if it is not empty.
				if (CollectionUtils.isNotEmpty(JnJB2BUnitModel.getSalesOrgCustomers()))
				{
					for (final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustModel : JnJB2BUnitModel.getSalesOrgCustomers())
					{
						// Check if the division is already exist in the b2b unit model.
						if (jnjGTIntSalesOrgModel.getDivision().equals(jnjGTSalesOrgCustModel.getDivision())
								&& jnjGTIntSalesOrgModel.getSalesOrg().equals(jnjGTSalesOrgCustModel.getSalesOrg())
								&& jnjGTIntSalesOrgModel.getDistChannel().equals(jnjGTSalesOrgCustModel.getDistributionChannel())
								&& sourceSysId.equals(jnjGTSalesOrgCustModel.getSourceSysId()))
						{
							// set the user price group by using the customer group and price list value from the intermediate tables.
							setUserPriceGrp(jnjGTIntSalesOrgModel.getPriceList(), jnjGTIntSalesOrgModel.getCustomerGroup(),
									jnjGTSalesOrgCustModel, JnJB2BUnitModel);
							isModelExisted = true;
							break;
						}
					}
					if (!isModelExisted)
					{
						jnjGTSalesOrgCustomerModel = modelService.create(JnjGTSalesOrgCustomerModel.class);
						// call the private method to map the value from intermediate model to hybris model.
						mapSalesOrgCustModelFromIntSalesOrgModel(jnjGTSalesOrgCustomerModel, jnjGTIntSalesOrgModel, JnJB2BUnitModel);
						jnjGTSalesOrgCustomerModels = new HashSet<JnjGTSalesOrgCustomerModel>(JnJB2BUnitModel.getSalesOrgCustomers());
						jnjGTSalesOrgCustomerModels.add(jnjGTSalesOrgCustomerModel);
						JnJB2BUnitModel.setSalesOrgCustomers(jnjGTSalesOrgCustomerModels);
					}
				}// check for the Sales Org Customers list if it is empty then create the list
				else
				{
					jnjGTSalesOrgCustomerModel = modelService.create(JnjGTSalesOrgCustomerModel.class);
					jnjGTSalesOrgCustomerModels = new HashSet<JnjGTSalesOrgCustomerModel>();
					// call the private method to map the value from intermediate model to hybris model.
					mapSalesOrgCustModelFromIntSalesOrgModel(jnjGTSalesOrgCustomerModel, jnjGTIntSalesOrgModel, JnJB2BUnitModel);
					jnjGTSalesOrgCustomerModels.add(jnjGTSalesOrgCustomerModel);
					JnJB2BUnitModel.setSalesOrgCustomers(jnjGTSalesOrgCustomerModels);
				}
			}
		}// if block end
		isSalesOrgDataMapped = true;

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgWithB2BUnit()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isSalesOrgDataMapped;
	}

	/**
	 * Map partner func with b2 b unit by using JnJB2BUnitModel.
	 * 
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @param newAdressCollection
	 *           the new adress collection
	 * @param addressModel
	 *           the address model
	 * @return true, if successful
	 */
	private boolean mapPartnerFuncWithB2BUnit(final JnJB2BUnitModel JnJB2BUnitModel,
			final Collection<AddressModel> newAdressCollection, final AddressModel addressModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapPartnerFuncWithB2BUnit()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean isPartnerFuncDataMapped = false;
		// Fetch all those intermediates record which have customer number equals to Uid of JnJB2BUnitModel.
		final List<JnjGTIntPartnerFuncModel> jnjGTIntPartFuncModelList = jnjGTB2BUnitFeedService
				.getJnjGTIntPartnerFuncModel(JnJB2BUnitModel.getUid());

		for (final JnjGTIntPartnerFuncModel jnjGTIntPartnerFuncModel : jnjGTIntPartFuncModelList)
		{
			if (StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartCustNo())
					&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getPartFuncCode()))
			{
				/*
				 * populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel,
				 * jnjGTIntPartnerFuncModel.getPartCustNo(), jnjGTIntPartnerFuncModel.getPartFuncCode(), false);
				 */
				populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel, jnjGTIntPartnerFuncModel, false);

			}
		}
		isPartnerFuncDataMapped = true;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapPartnerFuncWithB2BUnit()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return isPartnerFuncDataMapped;
	}

	/**
	 * Map customer data for those records for which Sold To flag is true.
	 * 
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @param newAdressCollection
	 *           the new adress collection
	 * @param addressModel
	 *           the address model
	 * @param isCronJobCall
	 *           the is cron job call
	 * @return true, if successful
	 */
	private boolean mapAffiliationDataWithB2BUnit(JnJB2BUnitModel JnJB2BUnitModel, Collection<AddressModel> newAdressCollection,
			final AddressModel addressModel, final boolean isCronJobCall, final String feedName)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		String errorMessage = null;
		boolean recordStatus = false;
		List<JnjGTIntAffiliationModel> jnjGTIntAffiliationModelList = null;

		if (isCronJobCall)
		{
			jnjGTIntAffiliationModelList = jnjGTB2BUnitFeedService.getJnjGTIntAffiliationModel(RecordStatus.PENDING, null);
		}
		else
		{
			// Fetch all those records whose record status is pending and customer number equal to uid of JnJB2BUnitModel.
			jnjGTIntAffiliationModelList = jnjGTB2BUnitFeedService.getJnjGTIntAffiliationModel(RecordStatus.PENDING,
					JnJB2BUnitModel.getUid());
		}

		// Check the list is not empty
		if (!jnjGTIntAffiliationModelList.isEmpty())
		{

			for (final JnjGTIntAffiliationModel jnjGTIntAffiliationModel : jnjGTIntAffiliationModelList)
			{
				try
				{
					recordStatus = false;
					if (isCronJobCall)
					{
						if (StringUtils.isNotEmpty(jnjGTIntAffiliationModel.getSourceSysId())
								&& StringUtils.isNotEmpty(jnjGTIntAffiliationModel.getParentCustNo()))
						{
							// Call To fetch the source system id
							final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntAffiliationModel.getSourceSysId());
							JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(jnjGTIntAffiliationModel.getParentCustNo(),
									sourceSysId);
							if (JnJB2BUnitModel == null)
							{
								errorMessage = "B2BUnit is not existed for the particular id "
										+ jnjGTIntAffiliationModel.getParentCustNo() + "for the Affiliation Feed";
							}
							else
							{
								newAdressCollection = new HashSet<AddressModel>(JnJB2BUnitModel.getAddresses());
							}
						}
						else
						{
							errorMessage = "Source System Id or Parent Customer No. is empty or null";
						}
					}
					if (null != JnJB2BUnitModel && StringUtils.isNotEmpty(jnjGTIntAffiliationModel.getChildCustNo()))
					{

						populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel,
								jnjGTIntAffiliationModel.getChildCustNo(), jnjGTIntAffiliationModel.getPartnerTypeCode(),
								jnjGTIntAffiliationModel.getDelIndicator() != null ? jnjGTIntAffiliationModel.getDelIndicator()
										.booleanValue() : false);

						/*
						 * populatesAddressInfo(JnJB2BUnitModel, newAdressCollection, addressModel, jnjGTIntAffiliationModel,
						 * jnjGTIntAffiliationModel.getDelIndicator() != null ? jnjGTIntAffiliationModel.getDelIndicator()
						 * .booleanValue() : false);
						 */
						// if the isCronJobCall is true then only execute below statement.
						if (isCronJobCall)
						{
							JnJB2BUnitModel.setAddresses(newAdressCollection);
							// Save the JnJB2BUnitModel model.
							recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
						}
						recordStatus = true;
					}
					else if (StringUtils.isEmpty(errorMessage))
					{
						errorMessage = "B2BUnit Model or Child Customer No. is empty or null";
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntAffiliationModel.getParentCustNo()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntAffiliationModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntAffiliationModel, null, true, errorMessage, feedName,
							jnjGTIntAffiliationModel.getParentCustNo());

					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(feedName + Logging.HYPHEN + "mapAffiliationDataWithB2BUnit()" + Logging.HYPHEN
								+ "The Record with Customer Number: " + jnjGTIntAffiliationModel.getParentCustNo()
								+ " was not processed successfully.");
					}
				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(feedName
						+ Logging.HYPHEN
						+ "mapCustomerDataForSoldTo()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Sold To'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(feedName + Logging.HYPHEN + "mapCustomerDataForSoldTo()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Populates partner info in addressModel by getting from the JnjGTIntPartnerFuncModel.
	 * 
	 * @param addressModel
	 *           the address model
	 * @param code
	 *           the code
	 */
	private void populatesPartnerInfo(final AddressModel addressModel, final String code)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesPartnerInfo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (StringUtils.isNotEmpty(code) && Config.getParameter(B2BUnit.MDD_SHIP_TO_INDC).equalsIgnoreCase(code))
		{
			addressModel.setShippingAddress(Boolean.TRUE);
		}
		/*
		 * else if (StringUtils.isNotEmpty(code) &&
		 * (Config.getParameter(B2BUnit.CONS_PAY_FROM_INDC).equalsIgnoreCase(code) || Config.getParameter(
		 * B2BUnit.MDD_PAY_FROM_INDC).equalsIgnoreCase(code))) { addressModel.setPayFromAddress(Boolean.TRUE); }
		 */
		else if (StringUtils.isNotEmpty(code) && Config.getParameter(B2BUnit.MDD_BILL_TO_INDC).equalsIgnoreCase(code))
		{
			addressModel.setBillingAddress(Boolean.TRUE);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesPartnerInfo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	private void populatesPartnerInfo(final AddressModel addressModel, final JnjGTIntPartnerFuncModel jnjGTIntPartnerFuncModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesPartnerInfo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final String code = jnjGTIntPartnerFuncModel.getPartFuncCode();

		if (StringUtils.isNotEmpty(code) && Config.getParameter(B2BUnit.MDD_SHIP_TO_INDC).equalsIgnoreCase(code))
		{
			if (jnjGTIntPartnerFuncModel.getCustomerNumber() != null
					&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getCustomerNumber()))
			{
				addressModel.setShippingAddress(Boolean.TRUE);
			}
		}
		/*
		 * else if (StringUtils.isNotEmpty(code) &&
		 * (Config.getParameter(B2BUnit.CONS_PAY_FROM_INDC).equalsIgnoreCase(code) || Config.getParameter(
		 * B2BUnit.MDD_PAY_FROM_INDC).equalsIgnoreCase(code))) { addressModel.setPayFromAddress(Boolean.TRUE); }
		 */
		else if (StringUtils.isNotEmpty(code) && Config.getParameter(B2BUnit.MDD_BILL_TO_INDC).equalsIgnoreCase(code))
		{
			if (jnjGTIntPartnerFuncModel.getCustomerNumber() != null
					&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getCustomerNumber()))
			{
				addressModel.setBillingAddress(Boolean.TRUE);
			}
		}
		else if (StringUtils.isNotEmpty(code) && code.equalsIgnoreCase("Bill-to Primary"))
		{
			if (jnjGTIntPartnerFuncModel.getCustomerNumber() != null
					&& StringUtils.isNotEmpty(jnjGTIntPartnerFuncModel.getCustomerNumber()))
			{
				System.out.println("It's the primary Bill To");
				addressModel.setBillingAddress(Boolean.TRUE);
			}
		}
		/*
		 * else { final Set<PrincipalGroupModel> principalGroups = new HashSet<PrincipalGroupModel>();
		 * JnJB2BUnitModel.setGroups(principalGroups); } AddressModel.setOwner(JnJB2BUnitModel);
		 */
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesPartnerInfo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Populates address info by using the JnJB2BUnitModel, jnjAddressid & code.
	 * 
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * @param newAdressCollection
	 *           the new adress collection
	 * @param addressModel
	 *           the address model
	 * @param jnjAddressId
	 *           the jnj address id
	 * @param code
	 *           the code
	 */



	private void populatesAddressInfo(final JnJB2BUnitModel JnJB2BUnitModel, final Collection<AddressModel> newAdressCollection,
			final AddressModel addressModel, final JnjGTIntPartnerFuncModel jnjGTIntPartnerFuncModel, final boolean delInd)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesAddressInfo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		AddressModel newAddressModel = null;
		AddressModel existAddressModel = null;
		final String jnjAddressId = jnjGTIntPartnerFuncModel.getPartCustNo();
		final String shipToNumber = jnjGTIntPartnerFuncModel.getCustomerNumber();
		final String code = jnjGTIntPartnerFuncModel.getPartFuncCode();
		if (JnJB2BUnitModel != null)
		{
			existAddressModel = jnjGTAddressService.getAddressForJnjAddressId(JnJB2BUnitModel, jnjAddressId);
		}
		else
		{
			existAddressModel = jnjGTAddressService.getAddressById(jnjAddressId);
		}
		// Checking whether the new address is in the existing address of the B2B Unit and exist in the database
		if (existAddressModel != null)
		{
			// If delIndicator comes as true then remove that model from the collection list and also from the hybris data base.
			if (delInd)
			{
				if (newAdressCollection.contains(existAddressModel))
				{
					newAdressCollection.remove(existAddressModel);
				}
				modelService.remove(existAddressModel);
			}
			else
			{
				// If true the add the new address in the B2B final Unit with shipping/billing/payer from flag setting true

				final AddressModel shipToAddress = jnjGTAddressService.getAddressById(shipToNumber);
				populatesPartnerInfo(existAddressModel, jnjGTIntPartnerFuncModel);
				if (existAddressModel.getBillingAddress() == Boolean.TRUE && existAddressModel.getShippingAddress() == Boolean.FALSE)
				{
					existAddressModel.setOwner(shipToAddress);
				}
				else if (existAddressModel.getBillingAddress() == Boolean.FALSE
						&& existAddressModel.getShippingAddress() == Boolean.TRUE)
				{
					existAddressModel.setOwner(JnJB2BUnitModel);
				}
				modelService.save(existAddressModel);
				newAdressCollection.add(existAddressModel);
			}

		}
		else if (null != addressModel && addressModel.getJnJAddressId().equals(jnjAddressId))
		{
			// If delIndicator comes as true then remove that model from the collection list and also from the hybris data base.
			if (delInd)
			{
				if (newAdressCollection.contains(addressModel))
				{
					newAdressCollection.remove(addressModel);
				}
				modelService.remove(addressModel);
			}
			else
			{
				final AddressModel shipToAddress = jnjGTAddressService.getAddressById(shipToNumber);
				populatesPartnerInfo(addressModel, jnjGTIntPartnerFuncModel);
				addressModel.setOwner(shipToAddress);
				modelService.save(addressModel);
			}
		}
		else if (!delInd)
		{
			final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(jnjAddressId,
					JnJB2BUnitModel.getSourceSysId());

			if (!addressModelList.isEmpty() && addressModelList.get(0) instanceof AddressModel)
			{
				newAddressModel = modelService.clone(addressModelList.get(0));
			}
			else
			{
				//
				// If False create an new address Model and add the new final address in the final B2B Unit with shipping/billing/payer from flag setting true
				newAddressModel = modelService.create(AddressModel.class);
				newAddressModel.setSourceSysId(JnJB2BUnitModel.getSourceSysId());
				newAddressModel.setJnJAddressId(jnjAddressId);
			}
			newAddressModel.setOwner(JnJB2BUnitModel);
			populatesPartnerInfo(newAddressModel, jnjGTIntPartnerFuncModel);
			modelService.save(newAddressModel);
			newAdressCollection.add(newAddressModel);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesAddressInfo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}



	private void populatesAddressInfo(final JnJB2BUnitModel JnJB2BUnitModel, final Collection<AddressModel> newAdressCollection,
			final AddressModel addressModel, final String jnjAddressId, final String code, final boolean delInd)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesAddressInfo()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		AddressModel newAddressModel = null;
		//final String jnjAddressId = jnjGTIntPartnerFuncModel.getPartCustNo();
		//final String code = jnjGTIntPartnerFuncModel.getPartFuncCode();
		final AddressModel existAddressModel = jnjGTAddressService.getAddressForJnjAddressId(JnJB2BUnitModel, jnjAddressId);
		// Checking whether the new address is in the existing address of the B2B Unit and exist in the database
		if (existAddressModel != null)
		{
			// If delIndicator comes as true then remove that model from the collection list and also from the hybris data base.
			if (delInd)
			{
				if (newAdressCollection.contains(existAddressModel))
				{
					newAdressCollection.remove(existAddressModel);
				}
				modelService.remove(existAddressModel);
			}
			else
			{
				// If true the add the new address in the B2B final Unit with shipping/billing/payer from flag setting true
				existAddressModel.setOwner(JnJB2BUnitModel);
				populatesPartnerInfo(existAddressModel, code);
				modelService.save(existAddressModel);
				newAdressCollection.add(existAddressModel);
			}

		}
		else if (null != addressModel && addressModel.getJnJAddressId().equals(jnjAddressId))
		{
			// If delIndicator comes as true then remove that model from the collection list and also from the hybris data base.
			if (delInd)
			{
				if (newAdressCollection.contains(addressModel))
				{
					newAdressCollection.remove(addressModel);
				}
				modelService.remove(addressModel);
			}
			else
			{
				populatesPartnerInfo(addressModel, code);
				modelService.save(addressModel);
			}
		}
		else if (!delInd)
		{
			final List<AddressModel> addressModelList = jnjGTAddressService.getAddressByIdandSourceSysId(jnjAddressId,
					JnJB2BUnitModel.getSourceSysId());

			if (!addressModelList.isEmpty() && addressModelList.get(0) instanceof AddressModel)
			{
				newAddressModel = modelService.clone(addressModelList.get(0));
			}
			else
			{
				//
				// If False create an new address Model and add the new final address in the final B2B Unit with shipping/billing/payer from flag setting true
				newAddressModel = modelService.create(AddressModel.class);
				newAddressModel.setSourceSysId(JnJB2BUnitModel.getSourceSysId());
				newAddressModel.setJnJAddressId(jnjAddressId);
			}
			newAddressModel.setOwner(JnJB2BUnitModel);
			populatesPartnerInfo(newAddressModel, code);
			modelService.save(newAddressModel);
			newAdressCollection.add(newAddressModel);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "populatesAddressInfo()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}


	/**
	 * Map int address data with address model.
	 * 
	 * @param AddressModel
	 *           the jnj na address model
	 * @param jnjGTIntB2BUnitModel
	 *           the jnj na int b2 b unit model
	 * @throws BusinessException
	 */
	private AddressModel mapIntAddressDataWithAddressModel(final AddressModel AddressModel,
			final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel) throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapIntAddressDataWithAddressModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		AddressModel.setLine3(jnjGTIntB2BUnitModel.getAddressLine3());
		//System.out.println(AddressModel.getLine3());
		AddressModel.setLine4(jnjGTIntB2BUnitModel.getAddressLine4());
		//System.out.println(AddressModel.getLine4());
		AddressModel.setCountyName(jnjGTIntB2BUnitModel.getCountyName());
		//System.out.println(AddressModel.getCountyName());
		AddressModel.setPoBoxPostalCode(jnjGTIntB2BUnitModel.getPoBoxPostalCode());
		//System.out.println(AddressModel.getPoBoxPostalCode());
		AddressModel.setPostalExtCode(jnjGTIntB2BUnitModel.getPostalExtCode());
		//System.out.println(AddressModel.getPostalExtCode());
		AddressModel.setJnJAddressId(jnjGTIntB2BUnitModel.getUid());
		//System.out.println(AddressModel.getJnJAddressId());
		//AddressModel.setLine1(jnjGTIntB2BUnitModel.getAddressLine1());
		//System.out.println(AddressModel.getLine1());
		AddressModel.setLine2(jnjGTIntB2BUnitModel.getAddressLine2());
		//System.out.println(AddressModel.getLine2());
		AddressModel.setTown(jnjGTIntB2BUnitModel.getTown());
		AddressModel.setPobox(jnjGTIntB2BUnitModel.getPoBoxNumber());
		AddressModel.setPostalcode(jnjGTIntB2BUnitModel.getPostalCode());
		AddressModel.setEmail(jnjGTIntB2BUnitModel.getEmail());
		AddressModel.setFirstname(jnjGTIntB2BUnitModel.getFirstName());
		AddressModel.setMiddlename(jnjGTIntB2BUnitModel.getMiddleName());
		AddressModel.setLastname(jnjGTIntB2BUnitModel.getLastName());
		AddressModel.setCompany(jnjGTIntB2BUnitModel.getCustName1());
		AddressModel.setPostalcode(jnjGTIntB2BUnitModel.getPostalCode());
		final CountryModel countryModel = getCountry(jnjGTIntB2BUnitModel);
		AddressModel.setCountry(countryModel);
		//System.out.println(AddressModel.getCountry().getName());
		AddressModel.setRegion(getRegion(jnjGTIntB2BUnitModel, countryModel));
		//System.out.println(AddressModel.getRegion().getName());
		if (BooleanUtils.isTrue(jnjGTIntB2BUnitModel.getShipToAddress()))
		{
			AddressModel.setShippingAddress(Boolean.TRUE);
			AddressModel.setBillingAddress(Boolean.FALSE);
		}
		if (BooleanUtils.isTrue(jnjGTIntB2BUnitModel.getBillToAddress()))
		{
			AddressModel.setShippingAddress(Boolean.FALSE);
			AddressModel.setBillingAddress(Boolean.TRUE);

		}
		if (BooleanUtils.isTrue(jnjGTIntB2BUnitModel.getPayFromAddress()))
		{
			AddressModel.setPayFromAddress(Boolean.TRUE);
		}
		/*
		 * if (BooleanUtils.isTrue(jnjGTIntB2BUnitModel.getSoldToAddress())) {
		 * AddressModel.setSoldToAddress(Boolean.TRUE); }
		 */
		if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getCustStatusCde()) && "0".equals(jnjGTIntB2BUnitModel.getCustStatusCde()))
		{
			AddressModel.setActive(false);
		}
		else
		{
			AddressModel.setActive(true);
		}
		//System.out.println(AddressModel.isActive());
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapIntAddressDataWithAddressModel()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return AddressModel;
	}

	/**
	 * Map affiliation data through cron job.
	 * 
	 * @return true, if successful
	 */
	public boolean mapAffiliationDataThroughCronJob()
	{
		System.out.println("JnjGTB2BUnitDataLoadMapper mapAffiliationDataThroughCronJob() START");
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapAffiliationDataThroughCronJob()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		// Call the below method if we want to call from Affiliation cronjob by passing the isCronJob flag as true.
		recordStatus = mapAffiliationDataWithB2BUnit(null, null, null, true, Logging.CUSTOMER_LOCAL_FEED);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapAffiliationDataThroughCronJob()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Map customer data for those records for which Sold To flag is true.
	 * 
	 * @return true, if successful
	 */
	public boolean mapCustomerLocalDataWithB2BUnit()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		String errorMessage = null;
		boolean recordStatus = false;

		JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel = null;
		Set<JnjGTSalesOrgCustomerModel> jnjGTSalesOrgCustomerModels = null;
		// Fetch all those records whose record status is pending.
		final Collection<JnjGTIntB2BUnitLocalModel> jnjGTIntB2BUnitLocalModelList = (Collection<JnjGTIntB2BUnitLocalModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntB2BUnitLocalModel._TYPECODE, RecordStatus.PENDING);
		if (!jnjGTIntB2BUnitLocalModelList.isEmpty())
		{
			for (final JnjGTIntB2BUnitLocalModel jnjGTIntB2BUnitLocalModel : jnjGTIntB2BUnitLocalModelList)
			{
				boolean isModelExisted = false;
				try
				{
					recordStatus = false;
					if (StringUtils.isNotEmpty(jnjGTIntB2BUnitLocalModel.getSourceSysId())
							&& StringUtils.isNotEmpty(jnjGTIntB2BUnitLocalModel.getCustomerNumber())
							&& StringUtils.isNotEmpty(jnjGTIntB2BUnitLocalModel.getDivision()))
					{
						// Call To fetch the source system id
						final String sourceSysId = JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2BUnitLocalModel.getSourceSysId());
						final JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(
								jnjGTIntB2BUnitLocalModel.getCustomerNumber(), sourceSysId);
						if (JnJB2BUnitModel == null)
						{
							errorMessage = "B2BUnit is not existed for the particular id "
									+ jnjGTIntB2BUnitLocalModel.getCustomerNumber() + "for the Customer Local Feed";
						}
						else
						{
							// check for the Sales Org Customers list if it is not empty.
							if (CollectionUtils.isNotEmpty(JnJB2BUnitModel.getSalesOrgCustomers()))
							{
								for (final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustModel : JnJB2BUnitModel.getSalesOrgCustomers())
								{
									// Check if the division is already exist in the b2b unit model.
									if (jnjGTIntB2BUnitLocalModel.getDivision().equals(jnjGTSalesOrgCustModel.getDivision())
											&& sourceSysId.equals(jnjGTSalesOrgCustModel.getSourceSysId()))
									{
										// Set the User Price group by using price book ,class of trade value.
										setUserPriceGrpUsingPriceBook(jnjGTIntB2BUnitLocalModel.getPriceBook(),
												JnJB2BUnitModel.getIndicator(), jnjGTSalesOrgCustModel);
										isModelExisted = true;
										break;
									}
								}
								if (!isModelExisted)
								{
									jnjGTSalesOrgCustomerModel = modelService.create(JnjGTSalesOrgCustomerModel.class);
									// call the private method to map the value from intermediate model to hybris model.
									mapSalesOrgCustModelFromIntB2BUnitLocalModel(jnjGTSalesOrgCustomerModel, jnjGTIntB2BUnitLocalModel,
											JnJB2BUnitModel.getIndicator(), sourceSysId);
									// To avoid Unsupported Operation Exception
									modelService.refresh(JnJB2BUnitModel);
									jnjGTSalesOrgCustomerModels = new HashSet<JnjGTSalesOrgCustomerModel>(
											JnJB2BUnitModel.getSalesOrgCustomers());
									jnjGTSalesOrgCustomerModels.add(jnjGTSalesOrgCustomerModel);
									JnJB2BUnitModel.setSalesOrgCustomers(jnjGTSalesOrgCustomerModels);
								}
							}// check for the Sales Org Customers list if it is not empty then create the list and add the model in it and set the list in the JnJB2BUnitModel.
							else
							{
								jnjGTSalesOrgCustomerModel = modelService.create(JnjGTSalesOrgCustomerModel.class);
								jnjGTSalesOrgCustomerModels = new HashSet<JnjGTSalesOrgCustomerModel>();
								// call the private method to map the value from intermediate model to hybris model.
								mapSalesOrgCustModelFromIntB2BUnitLocalModel(jnjGTSalesOrgCustomerModel, jnjGTIntB2BUnitLocalModel,
										JnJB2BUnitModel.getIndicator(), sourceSysId);
								jnjGTSalesOrgCustomerModels.add(jnjGTSalesOrgCustomerModel);
								JnJB2BUnitModel.setSalesOrgCustomers(jnjGTSalesOrgCustomerModels);
							}
							// Save the JnJB2BUnitModel model.
							recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
						}// if block end
					}
					else
					{
						errorMessage = "Source System Id or Customer Number or Division is null or empty";
					}

				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
					recordStatus = false;
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
							+ "Throwable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
					recordStatus = false;
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()"
								+ Logging.HYPHEN + "The Record with Customer Number: " + jnjGTIntB2BUnitLocalModel.getCustomerNumber()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitLocalModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2BUnitLocalModel, null, true, errorMessage,
							Logging.CUSTOMER_LOCAL_FEED, jnjGTIntB2BUnitLocalModel.getCustomerNumber());
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()"
								+ Logging.HYPHEN + "The Record with Customer Number: " + jnjGTIntB2BUnitLocalModel.getCustomerNumber()
								+ " was not processed successfully.");
					}
				}
			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED
						+ Logging.HYPHEN
						+ "mapCustomerLocalDataWithB2BUnit()"
						+ Logging.HYPHEN
						+ "No jnjGTIntB2BUnitModel Records with status 'PENDING' exists in Hybris for 'Sold To'. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_LOCAL_FEED + Logging.HYPHEN + "mapCustomerLocalDataWithB2BUnit()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * Map sales org cust model from intermediate sales org model.
	 * 
	 * @param jnjGTSalesOrgCustomerModel
	 *           the jnj na sales org customer model
	 * @param jnjGTIntSalesOrgModel
	 *           the jnj na int sales org model
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 */
	private void mapSalesOrgCustModelFromIntSalesOrgModel(final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel,
			final JnjGTIntSalesOrgModel jnjGTIntSalesOrgModel, final JnJB2BUnitModel JnJB2BUnitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgCustModelFromIntSalesOrgModel()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		jnjGTSalesOrgCustomerModel.setSalesOrg(jnjGTIntSalesOrgModel.getSalesOrg());
		jnjGTSalesOrgCustomerModel.setDistributionChannel(jnjGTIntSalesOrgModel.getDistChannel());
		jnjGTSalesOrgCustomerModel.setDivision(jnjGTIntSalesOrgModel.getDivision());
		jnjGTSalesOrgCustomerModel.setSourceSysId(JnJB2BUnitModel.getSourceSysId());
		// set the user price group by using the customer group and price list value from the intermediate tables.
		setUserPriceGrp(jnjGTIntSalesOrgModel.getPriceList(), jnjGTIntSalesOrgModel.getCustomerGroup(), jnjGTSalesOrgCustomerModel,
				JnJB2BUnitModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgCustModelFromIntSalesOrgModel()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Map sales org cust model from intermediate B2BUnit Local model.
	 * 
	 * @param jnjGTSalesOrgCustomerModel
	 *           the jnj na sales org customer model
	 * @param jnjGTIntB2BUnitLocalModel
	 *           the jnj na int b2 b unit local model
	 * @param indicator
	 *           the indicator
	 */
	private void mapSalesOrgCustModelFromIntB2BUnitLocalModel(final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel,
			final JnjGTIntB2BUnitLocalModel jnjGTIntB2BUnitLocalModel, final String indicator, final String sourceSysId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgCustModelFromIntSalesOrgModel()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		jnjGTSalesOrgCustomerModel.setDivision(jnjGTIntB2BUnitLocalModel.getDivision());
		jnjGTSalesOrgCustomerModel.setSourceSysId(sourceSysId);
		// Set the User Price group by using price book ,class of trade value.
		setUserPriceGrpUsingPriceBook(jnjGTIntB2BUnitLocalModel.getPriceBook(), indicator, jnjGTSalesOrgCustomerModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "mapSalesOrgCustModelFromIntSalesOrgModel()"
					+ Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Sets the user price grp.
	 * 
	 * @param priceList
	 *           the price list
	 * @param customerGrp
	 *           the customer grp
	 * @param jnjGTSalesOrgCustomerModel
	 *           the jnj na sales org customer model
	 * @param JnJB2BUnitModel
	 *           the jnj na b2 b unit model
	 * 
	 */
	private void setUserPriceGrp(final String priceList, final String customerGrp,
			final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel, final JnJB2BUnitModel JnJB2BUnitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "setUserPriceGrp()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		// check for empty and null then enter inside if block.
		if (StringUtils.isNotEmpty(customerGrp))
		{
			jnjGTSalesOrgCustomerModel.setCustomerGroup(customerGrp);
			JnJB2BUnitModel.setCustomerGroup(customerGrp);
		}
		String userPriceGroup = priceList;
		if (StringUtils.isEmpty(userPriceGroup)
				|| JnJCommonUtil.getValue(Jnjgtb2binboundserviceConstants.PRICE_LIST_VALUE).equalsIgnoreCase(userPriceGroup))
		{
			userPriceGroup = JnJCommonUtil.getValue(Jnjgtb2binboundserviceConstants.PRICE_LIST_VALUE);
			if (StringUtils.isNotEmpty(customerGrp))
			{
				userPriceGroup = userPriceGroup.concat(Jnjgtb2binboundserviceConstants.PIPE_STRING).concat(customerGrp);
			}
		}
		final UserPriceGroup userPriceGrp = UserPriceGroup.valueOf(userPriceGroup);
		jnjGTSalesOrgCustomerModel.setUserPriceGroup(userPriceGrp);
		JnJB2BUnitModel.setUserPriceGroup(userPriceGrp);

		modelService.save(jnjGTSalesOrgCustomerModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "setUserPriceGrp()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	/**
	 * Sets the user price group using price book.
	 * 
	 * @param priceBook
	 *           the price book
	 * @param indicator
	 *           the indicator
	 * @param jnjGTSalesOrgCustomerModel
	 *           the jnj na sales org customer model
	 */
	private void setUserPriceGrpUsingPriceBook(final String priceBook, final String indicator,
			final JnjGTSalesOrgCustomerModel jnjGTSalesOrgCustomerModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "setUserPriceGrpUsingPriceBook()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// check for the empty and null
		if (StringUtils.isNotEmpty(indicator))
		{
			String userPriceGroup = indicator;
			if (StringUtils.isNotEmpty(priceBook))
			{
				userPriceGroup = indicator.concat(Jnjgtb2binboundserviceConstants.PIPE_STRING).concat(priceBook);
			}
			final UserPriceGroup userPriceGrp = UserPriceGroup.valueOf(userPriceGroup);
			jnjGTSalesOrgCustomerModel.setUserPriceGroup(userPriceGrp);
		}
		modelService.save(jnjGTSalesOrgCustomerModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "setUserPriceGrpUsingPriceBook()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * Returns the applicable country model value.
	 * 
	 * @param jnjGTIntB2BUnitModel
	 *           the jnjGTIntB2BUnitModel
	 */
	private CountryModel getCountry(final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getCountry()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		CountryModel countryModel = null;
		if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getIsoCode()))
		{
			try
			{
				countryModel = commonI18NService.getCountry(jnjGTIntB2BUnitModel.getIsoCode());
			}
			catch (final Exception exception)
			{
				LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getCountry()" + Logging.HYPHEN + "Exception occurred -"
						+ exception.getMessage(), exception);
				countryModel = modelService.create(CountryModel.class);
				countryModel.setIsocode(jnjGTIntB2BUnitModel.getIsoCode());
				countryModel.setName(jnjGTIntB2BUnitModel.getCountryName());
				modelService.save(countryModel);
			}
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getCountry()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return countryModel;
	}

	/**
	 * Returns the applicable region model value.
	 * 
	 * @param jnjGTIntB2BUnitModel
	 *           the jnjGTIntB2BUnitModel
	 * @param countryModel
	 *           the country model
	 * @throws BusinessException
	 *            the business exception
	 */
	private RegionModel getRegion(final JnjGTIntB2BUnitModel jnjGTIntB2BUnitModel, final CountryModel countryModel)
			throws BusinessException
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getRegion()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		RegionModel regionModel = null;
		if (countryModel != null && StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getRegion()))
		{
			try
			{
				regionModel = commonI18NService.getRegion(countryModel, jnjGTIntB2BUnitModel.getRegion());
			}
			catch (final Exception exception)
			{
				LOGGER.error(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getRegion()" + Logging.HYPHEN + "Exception occurred -"
						+ exception.getMessage(), exception);
				regionModel = modelService.create(RegionModel.class);
				regionModel.setIsocode(jnjGTIntB2BUnitModel.getRegion());
				regionModel.setName(jnjGTIntB2BUnitModel.getStateName());
				regionModel.setCountry(countryModel);
				modelService.save(regionModel);
			}
		}
		else if (StringUtils.isNotEmpty(jnjGTIntB2BUnitModel.getRegion()))
		{
			throw new BusinessException("Country code is null but region code is not null", MessageCode.BUSINESS_EXCEPTION,
					Severity.BUSINESS_EXCEPTION);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.CUSTOMER_MASTER_FEED + Logging.HYPHEN + "getRegion()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return regionModel;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
