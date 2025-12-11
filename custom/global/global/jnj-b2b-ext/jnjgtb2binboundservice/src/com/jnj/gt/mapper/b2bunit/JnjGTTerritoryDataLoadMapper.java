/**
 * 
 */
package com.jnj.gt.mapper.b2bunit;

import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjGTTerritoryDivAddressesModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntTerritoryModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * @author komal.sehgal
 * 
 */
public class JnjGTTerritoryDataLoadMapper extends JnjAbstractMapper
{

	private static final Logger LOGGER = Logger.getLogger(JnjGTTerritoryDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	ModelService modelService;

	@Autowired
	private JnjGTTerritoryService jnjGtTerritoryService;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;


	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjGTSalesAlignmentDataLoadMapper jnjGTSalesAlignmentDataLoadMapper;

	@Override
	public void processIntermediateRecords()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		removeAllTerritoryDivison();
		mapTerritoryDataForUser();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	/**
	 * 
	 */
	private void removeAllTerritoryDivison()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "removeAllTerritoryDivison()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjGTTerritoryDivisonModel> jnjGTTerritoryModelList = jnjGtTerritoryService
				.getAllJnjGTTerritoryDivModel(Jnjb2bCoreConstants.SalesAlignment.CONSUMER);
		if (!jnjGTTerritoryModelList.isEmpty())
		{
			for (final JnjGTTerritoryDivisonModel jnjGTTerritoryModel : jnjGTTerritoryModelList)
			{

				jnjGTSalesAlignmentDataLoadMapper.removeTerriToryDivsion(false, jnjGTTerritoryModel);
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "removeAllTerritoryDivison()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method will map the Intermediate records For Territory To Hybris
	 */
	public boolean mapTerritoryDataForUser()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		String errorMessage = null;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntTerritoryModel> jnjGTIntTerritoryList = (List<JnjGTIntTerritoryModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntTerritoryModel._TYPECODE, RecordStatus.PENDING);
		//If the list is not empty process  the records
		if (!jnjGTIntTerritoryList.isEmpty())
		{
			for (final JnjGTIntTerritoryModel jnjGTIntTerritory : jnjGTIntTerritoryList)
			{
				recordStatus = false;
				try
				{
					//Checking the B2BUnit exist for the incoming Sold To Number.
					final JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(
							jnjGTIntTerritory.getSoldToNumber(),
							JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntTerritory.getSourceSystemId()));
					//Checking whether the User exist for the Incoming WWID 
					JnJB2bCustomerModel JnJB2bCustomerModel = new JnJB2bCustomerModel();
					JnJB2bCustomerModel.setWwid(jnjGTIntTerritory.getWwid());
					JnJB2bCustomerModel = jnjGTCustomerService.getJnJB2bCustomerModel(JnJB2bCustomerModel);
					if (!jnjGTIntTerritory.getTerritoryCode().equalsIgnoreCase(
							Config.getParameter(Jnjgtb2binboundserviceConstants.Territory.TERRITORY_HASH_CODE)))
					{
						if (JnJB2BUnitModel != null && JnJB2bCustomerModel != null)
						{
							recordStatus = createTerritoryDivisonModel(jnjGTIntTerritory, JnJB2BUnitModel, JnJB2bCustomerModel);
						}
						else
						{
							recordStatus = false;
							if (JnJB2BUnitModel == null)
							{
								errorMessage = "Customer Not Found With UID '" + jnjGTIntTerritory.getSoldToNumber()
										+ "' Not found For TerritotryCode '" + jnjGTIntTerritory.getTerritoryCode() + "'";
							}
							else if (JnJB2bCustomerModel == null)
							{
								errorMessage = "User Not Found With Wwid '" + jnjGTIntTerritory.getWwid()
										+ "' Not found For TerritotryCode '" + jnjGTIntTerritory.getTerritoryCode() + "'";
							}
							LOGGER.error(errorMessage);
						}
					}
					else
					{
						if (JnJB2bCustomerModel != null)
						{
							recordStatus = createTerritoryDivisonModel(jnjGTIntTerritory, JnJB2BUnitModel, JnJB2bCustomerModel);
						}
						else
						{
							recordStatus = false;
							errorMessage = "User Not Found With Wwid '" + jnjGTIntTerritory.getWwid()
									+ "' Not found For TerritotryCode '" + jnjGTIntTerritory.getTerritoryCode() + "'";
							LOGGER.error(errorMessage);
						}
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Throwable throwable)
				{
					LOGGER.error(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + throwable.getMessage(), throwable);
					errorMessage = throwable.getMessage();
				}
				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
								+ "The Record with Territory Id " + jnjGTIntTerritory.getTerritoryCode()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntTerritory, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntTerritory, null, true, errorMessage,
							Logging.SALES_ALIGNMENT_FEED, jnjGTIntTerritory.getTerritoryCode());
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDataForUser()" + Logging.HYPHEN
								+ "The Record with Territory  Id" + jnjGTIntTerritory.getTerritoryCode()
								+ " was not processed successfully.");
					}
				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return recordStatus;
	}

	/**
	 * @param jnjGTIntTerritory
	 * @param JnJB2BUnitModel
	 * @param JnJB2bCustomerModel
	 * @return
	 */
	private boolean createTerritoryDivisonModel(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnJB2BUnitModel JnJB2BUnitModel, final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		boolean recordStatus;
		//Check whether the Model exist for a given Territory Code.
		JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
				jnjGTIntTerritory.getTerritoryCode() + Jnjgtb2binboundserviceConstants.PIPE_STRING
						+ Config.getParameter(Jnjgtb2binboundserviceConstants.Territory.CONSUMER_DIVISON),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntTerritory.getSourceSystemId()));
		// If the model is null create a new model
		if (jnjGTTerritoryDivisonModel == null)
		{
			jnjGTTerritoryDivisonModel = modelService.create(JnjGTTerritoryDivisonModel.class);
		}
		//Map the intermediate data in  TerritoryDivison model.
		recordStatus = mapTerritoryDivisonModel(jnjGTIntTerritory, jnjGTTerritoryDivisonModel, JnJB2BUnitModel, JnJB2bCustomerModel);
		return recordStatus;
	}

	/**
	 * @param jnjGTIntTerritory
	 * @param jnjGTTerritoryDivisonModel
	 *           This method will map the TerritoryDivion Model From the Intermediate TerritoryModel
	 * @param JnJB2bCustomerModel
	 * @param JnJB2BUnitModel
	 */
	private boolean mapTerritoryDivisonModel(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel, final JnJB2BUnitModel JnJB2BUnitModel,
			final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		boolean recordStatus;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//Setting the Uid For the Territory
		if (StringUtils.isNotEmpty(jnjGTIntTerritory.getTerritoryCode()))
		{
			jnjGTTerritoryDivisonModel.setUid(jnjGTIntTerritory.getTerritoryCode() + Jnjgtb2binboundserviceConstants.PIPE_STRING
					+ Config.getParameter(Jnjgtb2binboundserviceConstants.Territory.CONSUMER_DIVISON));
			jnjGTTerritoryDivisonModel.setTerritoryCode(jnjGTIntTerritory.getTerritoryCode());
		}

		//Mapping the Source id 
		if (StringUtils.isNotEmpty(jnjGTIntTerritory.getSourceSystemId()))
		{
			jnjGTTerritoryDivisonModel.setSourceSystemId(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntTerritory
					.getSourceSystemId()));
		}

		//Marking the Model as validated.
		jnjGTTerritoryDivisonModel.setInvalidated(Boolean.FALSE);
		final Set<JnjGTTerritoryDivAddressesModel> existingAdrreses = jnjGTTerritoryDivisonModel.getAddresses();
		final Set<JnjGTTerritoryDivAddressesModel> newTerritoryDivAdrreses = new HashSet<JnjGTTerritoryDivAddressesModel>();
		if (CollectionUtils.isNotEmpty(existingAdrreses))
		{
			boolean shipToNumberExists = false;
			final Set<JnjGTTerritoryDivAddressesModel> existingTerritoryDivAdrreses = new HashSet<JnjGTTerritoryDivAddressesModel>();
			existingTerritoryDivAdrreses.addAll(existingAdrreses);

			/***
			 * Iterate over existing Territory division addresses and check if there exist any record with current
			 * shipToNumber as address code already.
			 ***/
			for (final JnjGTTerritoryDivAddressesModel territoryDivAddressesModel : existingTerritoryDivAdrreses)
			{
				if (territoryDivAddressesModel.getAddressCode().equals(jnjGTIntTerritory.getShipToNumber()))
				{
					shipToNumberExists = true;
					break;
				}
			}

			/*** If current shipToNumber does not exists, create a new one and add to existing collection. ***/
			if (!shipToNumberExists)
			{
				final JnjGTTerritoryDivAddressesModel newTerritoryDivisionAddress = modelService
						.create(JnjGTTerritoryDivAddressesModel.class);
				newTerritoryDivisionAddress.setAddressCode(jnjGTIntTerritory.getShipToNumber());
				newTerritoryDivAdrreses.add(newTerritoryDivisionAddress);
			}

			/*** Add all new addresses into existing collection. ***/
			existingTerritoryDivAdrreses.addAll(newTerritoryDivAdrreses);

			/*** Replace existing address collection with the updated ones. ***/
			jnjGTTerritoryDivisonModel.setAddresses(existingTerritoryDivAdrreses);
		}
		else
		{
			/*** If no existing records found for addresses, create a new ones and set via new collection. ***/
			final JnjGTTerritoryDivAddressesModel territoryDivisionAddress = modelService
					.create(JnjGTTerritoryDivAddressesModel.class);
			territoryDivisionAddress.setAddressCode(jnjGTIntTerritory.getShipToNumber());
			newTerritoryDivAdrreses.add(territoryDivisionAddress);
			jnjGTTerritoryDivisonModel.setAddresses(newTerritoryDivAdrreses);
		}

		final Set<String> billingAddresses = (Set<String>) jnjGTTerritoryDivisonModel.getBillingAddresses();
		if (CollectionUtils.isNotEmpty(billingAddresses))
		{
			final Set<String> newbillingAddresses = new HashSet<String>();
			newbillingAddresses.addAll(billingAddresses);
			if (!newbillingAddresses.contains(jnjGTIntTerritory.getBillToNumber()))
			{
				newbillingAddresses.add(jnjGTIntTerritory.getBillToNumber());
			}
			jnjGTTerritoryDivisonModel.setBillingAddresses(newbillingAddresses);
		}
		else
		{
			final Set<String> newBillingAdrreses = new HashSet<String>();
			newBillingAdrreses.add(jnjGTIntTerritory.getBillToNumber());
			jnjGTTerritoryDivisonModel.setBillingAddresses(newBillingAdrreses);
		}
		recordStatus = jnjGtTerritoryService.saveTerritoryDivisonModel(jnjGTTerritoryDivisonModel);
		if (JnJB2BUnitModel != null)
		{
			recordStatus = mapSoldToNumberForTerritory(jnjGTIntTerritory, jnjGTTerritoryDivisonModel, JnJB2BUnitModel);
		}
		if (JnJB2bCustomerModel != null)
		{
			recordStatus = mapWwidForTerritory(jnjGTIntTerritory, jnjGTTerritoryDivisonModel, JnJB2bCustomerModel);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.TERRITORY_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * @param jnjGTIntTerritory
	 * @param jnjGTTerritoryDivisonModel
	 * @param JnJB2bCustomerModel
	 * @return
	 */
	private boolean mapWwidForTerritory(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel, final JnJB2bCustomerModel JnJB2bCustomerModel)
	{
		final boolean recordStatus;
		//Map the intermediate data in  TerritoryDivCustRel model.		
		JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel = new JnjGTTerritoryDivCustRelModel();
		// add the   TerritoryDivison model in the  source.
		jnjGTTerritoryDivCustRel.setSource(jnjGTTerritoryDivisonModel);
		// add the   B2BUnit  model in the  target.
		jnjGTTerritoryDivCustRel.setTarget(JnJB2bCustomerModel);
		//Load the existing Model  
		final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelList = jnjGtTerritoryService
				.getTerritoryDivCustRel(jnjGTTerritoryDivCustRel);
		if (jnjGTTerritoryDivCustRelList.isEmpty())
		{
			jnjGTTerritoryDivCustRel = modelService.create(JnjGTTerritoryDivCustRelModel.class);
		}
		recordStatus = mapTerritoryDivCustRelModelForUser(jnjGTIntTerritory, JnJB2bCustomerModel, jnjGTTerritoryDivisonModel,
				jnjGTTerritoryDivCustRel);
		return recordStatus;
	}

	/**
	 * @param jnjGTIntTerritory
	 * @param JnJB2bCustomerModel
	 * @param jnjGTTerritoryDivisonModel
	 * @param jnjGTTerritoryDivCustRel
	 */
	private boolean mapTerritoryDivCustRelModelForUser(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnJB2bCustomerModel JnJB2bCustomerModel, final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel,
			final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel)
	{
		boolean recordStatus;
		// add the   TerritoryDivison model in the  source.
		jnjGTTerritoryDivCustRel.setSource(jnjGTTerritoryDivisonModel);
		// add the   B2BUnit  model in the  target.
		jnjGTTerritoryDivCustRel.setTarget(JnJB2bCustomerModel);
		jnjGTTerritoryDivCustRel.setInvalidated(Boolean.FALSE);
		//Save the TerritoryDivisonModel
		jnjGtTerritoryService.saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRel);
		//Adding the TerritoryDivisonModel in the groups  of the B2BCustomer.
		if (!JnJB2bCustomerModel.getGroups().contains(jnjGTTerritoryDivisonModel))
		{
			final HashSet<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
			groups.addAll(JnJB2bCustomerModel.getGroups());
			groups.add(jnjGTTerritoryDivisonModel);
			JnJB2bCustomerModel.setGroups(groups);
		}
		recordStatus = jnjGTCustomerService.saveJnjB2bCustomer(JnJB2bCustomerModel);
		return recordStatus;

	}


	/**
	 * @param jnjGTIntTerritory
	 * @param jnjGTTerritoryDivisonModel
	 * @param JnJB2BUnitModel
	 *           This method will load the relation of the Incoming Sold TO Number To the B2BUnit
	 */
	private boolean mapSoldToNumberForTerritory(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel, final JnJB2BUnitModel JnJB2BUnitModel)
	{
		boolean recordStatus;
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		//Map the intermediate data in  TerritoryDivCustRel model.		
		JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
		// add the   TerritoryDivison model in the  source.
		jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
		// add the   B2BUnit  model in the  target.
		jnjGTTerritoryDivCustRelModel.setTarget(JnJB2BUnitModel);

		//Load the existing Model  
		final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
				.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
		//If the model Load create  a new Model and map the model from the Intermediate Data
		if (jnjGTTerritoryDivCustRelModelList.isEmpty())
		{
			jnjGTTerritoryDivCustRelModel = modelService.create(JnjGTTerritoryDivCustRelModel.class);

		}
		recordStatus = mapTerritoryDivCustRelModelForCustomer(jnjGTIntTerritory, JnJB2BUnitModel, jnjGTTerritoryDivisonModel,
				jnjGTTerritoryDivCustRelModel);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * @param jnjGTIntTerritory
	 * @param JnJB2BUnitModel
	 * @param jnjGTTerritoryDivisonModel
	 * @param jnjGTTerritoryDivCustRelModel
	 */
	private boolean mapTerritoryDivCustRelModelForCustomer(final JnjGTIntTerritoryModel jnjGTIntTerritory,
			final JnJB2BUnitModel JnJB2BUnitModel, final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel,
			final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel)
	{
		boolean recordStatus;
		// add the   TerritoryDivison model in the  source.
		jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
		// add the   B2BUnit  model in the  target.
		jnjGTTerritoryDivCustRelModel.setTarget(JnJB2BUnitModel);

		jnjGTTerritoryDivCustRelModel.setInvalidated(Boolean.FALSE);
		//Save the TerritoryDivisonModel
		recordStatus = jnjGtTerritoryService.saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRelModel);
		if (!JnJB2BUnitModel.getGroups().contains(jnjGTTerritoryDivisonModel))
		{
			final HashSet<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
			groups.addAll(JnJB2BUnitModel.getGroups());
			groups.add(jnjGTTerritoryDivisonModel);
			JnJB2BUnitModel.setGroups(groups);
		}
		recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
		return recordStatus;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
