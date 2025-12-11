/**
 *
 */
package com.jnj.gt.mapper.b2bunit;


import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Calendar;
import java.util.Date;
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
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivProdMappingModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntSalesAlignmentCustModel;
import com.jnj.gt.model.JnjGTIntSalesAlignmentOrgModel;
import com.jnj.gt.model.JnjGTIntSalesAlignmentProductModel;
import com.jnj.gt.model.JnjGTIntSalesAlignmentUserModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.gt.util.JnjGTInboundUtil;




/**
 * Class to Map Data from intermediate model to hybris model for Sales Alignment.
 * 
 * @author komal.sehgal
 * 
 */
public class JnjGTSalesAlignmentDataLoadMapper extends JnjAbstractMapper
{

	private static final Logger LOGGER = Logger.getLogger(JnjGTSalesAlignmentDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTTerritoryService jnjGtTerritoryService;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	@Autowired
	ModelService modelService;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;


	//Method to invoke the main logic.
	@Override
	public void processIntermediateRecords()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		mapSalesAlignmentDataForOrg();
		mapSalesAlignmentDataForCustomer();
		mapSalesAlignmentDataForUser();
		mapSalesAlignmentDataforProduct();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "processIntermediateRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	/**
	 * This method maps Data for Territory and Division.
	 */
	public boolean mapSalesAlignmentDataForOrg()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		String errorMessage = null;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntSalesAlignmentOrgModel> jnjGTIntSorgOrgList = (List<JnjGTIntSalesAlignmentOrgModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntSalesAlignmentOrgModel._TYPECODE, RecordStatus.PENDING);
		if (!jnjGTIntSorgOrgList.isEmpty())
		{
			for (final JnjGTIntSalesAlignmentOrgModel jnjGTIntSorgOrg : jnjGTIntSorgOrgList)
			{
				recordStatus = false;
				try
				{
					if (StringUtils.isEmpty(jnjGTIntSorgOrg.getDelIndicator())
							|| (StringUtils.isNotEmpty(jnjGTIntSorgOrg.getDelIndicator()) && jnjGTIntSorgOrg.getDelIndicator()
									.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_NO)))
					{

						JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
								getTerritoryDivID(jnjGTIntSorgOrg.getOrgId(), jnjGTIntSorgOrg.getSalesOrgDivCd()),
								JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgOrg.getSourceSystemId()));
						if (jnjGTTerritoryDivisonModel == null)
						{
							jnjGTTerritoryDivisonModel = modelService.create(JnjGTTerritoryDivisonModel.class);
						}
						//Map the intermediate data in  TerritoryDivison model.
						jnjGTTerritoryDivisonModel = mapTerritoryDivisonModel(jnjGTIntSorgOrg, jnjGTTerritoryDivisonModel);
						recordStatus = jnjGtTerritoryService.saveTerritoryDivisonModel(jnjGTTerritoryDivisonModel);
					}
					else if (jnjGTIntSorgOrg.getDelIndicator().equalsIgnoreCase(
							Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_YES))
					{
						//Calling the  method for removal of the Territory Divison and its relations
						recordStatus = removeTerriToryDivison(jnjGTIntSorgOrg);
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelRemovalException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "MOdel Removal Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntSorgOrg.getOrgId() + "|"
								+ jnjGTIntSorgOrg.getSalesOrgDivCd() + " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgOrg, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgOrg, null, true, errorMessage, Logging.SALES_ALIGNMENT_FEED,
							getTerritoryDivID(jnjGTIntSorgOrg.getOrgId(), jnjGTIntSorgOrg.getSalesOrgDivCd()));
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id " + jnjGTIntSorgOrg.getOrgId() + "|"
								+ jnjGTIntSorgOrg.getSalesOrgDivCd() + " was not processed successfully.");
					}

				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * This method maps removes the Territory Divison if the DelIndicator is true
	 */

	private boolean removeTerriToryDivison(final JnjGTIntSalesAlignmentOrgModel jnjGTIntSorgOrg)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "removeTerriToryDivisonWithDelInicatorTrue()"
					+ Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		//Getting the Territory Divison Model
		final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
				getTerritoryDivID(jnjGTIntSorgOrg.getOrgId(), jnjGTIntSorgOrg.getSalesOrgDivCd()),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgOrg.getSourceSystemId()));
		// if model is not null
		if (jnjGTTerritoryDivisonModel != null)
		{
			recordStatus = removeTerriToryDivsion(recordStatus, jnjGTTerritoryDivisonModel);
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * @param recordStatus
	 * @param jnjGTTerritoryDivisonModel
	 * @return
	 */
	public boolean removeTerriToryDivsion(boolean recordStatus, final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{
		//Getting the All Territory Divison Cust Model
		final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
		jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
		final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
				.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
		recordStatus = removeTerritoryCustModel(recordStatus, jnjGTTerritoryDivisonModel, jnjGTTerritoryDivCustRelModelList);
		//Delete the Territory Divison model  model
		recordStatus = jnjGtTerritoryService.removeTerritoryDivisonModel(jnjGTTerritoryDivisonModel);
		return recordStatus;
	}




	private JnjGTTerritoryDivisonModel mapTerritoryDivisonModel(final JnjGTIntSalesAlignmentOrgModel jnjGTIntSorgOrg,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgOrg.getOrgId()) && StringUtils.isNotEmpty(jnjGTIntSorgOrg.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setUid(getTerritoryDivID(jnjGTIntSorgOrg.getOrgId(), jnjGTIntSorgOrg.getSalesOrgDivCd()));
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgOrg.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setDivCode(jnjGTIntSorgOrg.getSalesOrgDivCd());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgOrg.getOrgId()))
		{
			jnjGTTerritoryDivisonModel.setTerritoryCode(jnjGTIntSorgOrg.getOrgId());
		}
		if (jnjGTIntSorgOrg.getOrgEffectiveDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEffectiveDate(jnjGTIntSorgOrg.getOrgEffectiveDate());
		}
		else
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			jnjGTTerritoryDivisonModel.setEffectiveDate(calendar.getTime());
		}
		if (jnjGTIntSorgOrg.getOrgEndDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEndDate(jnjGTIntSorgOrg.getOrgEndDate());
		}
		else
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			jnjGTTerritoryDivisonModel.setEffectiveDate(calendar.getTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgOrg.getSourceSystemId()))
		{
			jnjGTTerritoryDivisonModel
					.setSourceSystemId(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgOrg.getSourceSystemId()));
		}
		if (jnjGTIntSorgOrg.getOrgEndDate() != null && jnjGTIntSorgOrg.getOrgEffectiveDate() != null)
		{
			/* iF THE Model is in the date range make it active othwerwise it is dormant */
			if (JnjGTCoreUtil.compareDates(jnjGTIntSorgOrg.getOrgEffectiveDate(), jnjGTIntSorgOrg.getOrgEffectiveDate()))
			{
				jnjGTTerritoryDivisonModel.setInvalidated(Boolean.FALSE);

			}
			else
			{
				jnjGTTerritoryDivisonModel.setInvalidated(Boolean.TRUE);

			}
		}
		else
		{
			jnjGTTerritoryDivisonModel.setInvalidated(Boolean.FALSE);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTTerritoryDivisonModel;
	}



	private JnjGTTerritoryDivisonModel mapTerritoryDivisonModelFromCust(final JnjGTIntSalesAlignmentCustModel jnjGTIntSorgCust,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgCust.getOrgId()) && StringUtils.isNotEmpty(jnjGTIntSorgCust.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setUid(getTerritoryDivID(jnjGTIntSorgCust.getOrgId(), jnjGTIntSorgCust.getSalesOrgDivCd()));
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgCust.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setDivCode(jnjGTIntSorgCust.getSalesOrgDivCd());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgCust.getOrgId()))
		{
			jnjGTTerritoryDivisonModel.setTerritoryCode(jnjGTIntSorgCust.getOrgId());
		}
		if (jnjGTIntSorgCust.getAlnAfflnEffectiveDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEffectiveDate(jnjGTIntSorgCust.getAlnAfflnEffectiveDate());
		}
		if (jnjGTIntSorgCust.getAlnAfflnEndDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEndDate(jnjGTIntSorgCust.getAlnAfflnEndDate());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgCust.getSourceSystemId()))
		{
			jnjGTTerritoryDivisonModel
					.setSourceSystemId(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgCust.getSourceSystemId()));
		}
		jnjGTTerritoryDivisonModel.setInvalidated(Boolean.FALSE);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTTerritoryDivisonModel;
	}




	/**
	 * This method maps Data for Territory and Division for Consumer to B2BUnit
	 */
	public boolean mapSalesAlignmentDataForCustomer()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForCustomer()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntSalesAlignmentCustModel> jnjGTIntSorgCustList = (List<JnjGTIntSalesAlignmentCustModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntSalesAlignmentCustModel._TYPECODE, RecordStatus.PENDING);
		boolean recordStatus = false;
		String errorMessage = null;
		if (!jnjGTIntSorgCustList.isEmpty())
		{
			for (final JnjGTIntSalesAlignmentCustModel jnjGTIntSorgCust : jnjGTIntSorgCustList)
			{
				recordStatus = false;
				try
				{
					if (StringUtils.isEmpty(jnjGTIntSorgCust.getDelIndiactor())
							|| (StringUtils.isNotEmpty(jnjGTIntSorgCust.getDelIndiactor()) && jnjGTIntSorgCust.getDelIndiactor()
									.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_NO)))
					{
						// Check whether the CustomerAccount in the incoming Unit exist or not.
						final JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(
								jnjGTIntSorgCust.getUniCustomerNumber(),
								JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgCust.getSourceSystemId()));
						// Check whether the  TerritoryDivisonModel exist or not  in the incoming Unit exist or not.
						if (JnJB2BUnitModel != null)
						{
							JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
									getTerritoryDivID(jnjGTIntSorgCust.getOrgId(), jnjGTIntSorgCust.getSalesOrgDivCd()),
									JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgCust.getSourceSystemId()));
							// if the model  doesn't exit create a new TerritoryDivisonModel
							if (jnjGTTerritoryDivisonModel == null)
							{
								jnjGTTerritoryDivisonModel = modelService.create(JnjGTTerritoryDivisonModel.class);
								//Map the intermediate data in  TerritoryDivison model.
								jnjGTTerritoryDivisonModel = mapTerritoryDivisonModelFromCust(jnjGTIntSorgCust,
										jnjGTTerritoryDivisonModel);
								//Save the TerritoryDivisonModel
								recordStatus = jnjGtTerritoryService.saveTerritoryDivisonModel(jnjGTTerritoryDivisonModel);
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
							//If the model doesn't Load create  a new Model
							if (jnjGTTerritoryDivCustRelModelList.isEmpty())
							{
								jnjGTTerritoryDivCustRelModel = modelService.create(JnjGTTerritoryDivCustRelModel.class);
								recordStatus = mapTerritoryDivCustRelModel(jnjGTIntSorgCust, JnJB2BUnitModel, jnjGTTerritoryDivisonModel,
										jnjGTTerritoryDivCustRelModel);
							}
							else
							{
								for (final JnjGTTerritoryDivCustRelModel jnjTerritoryCustRelModel : jnjGTTerritoryDivCustRelModelList)
								{
									recordStatus = mapTerritoryDivCustRelModel(jnjGTIntSorgCust, JnJB2BUnitModel,
											jnjGTTerritoryDivisonModel, jnjTerritoryCustRelModel);
								}

							}
						}
						else
						{
							errorMessage = "B2BUnit not founf for this customer Number";
						}

					}

					else if (jnjGTIntSorgCust.getDelIndiactor().equalsIgnoreCase(
							Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_YES))
					{
						//Calling the  method for removal of the Territory Cust Divison and its relations
						recordStatus = removeTerriToryDivisonCustomerMapping(jnjGTIntSorgCust);
						if (!recordStatus)
						{
							errorMessage = "B2BUnit not founf for this customer Number";
						}
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelRemovalException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "MOdel Removal Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntSorgCust.getOrgId() + "|"
								+ jnjGTIntSorgCust.getSalesOrgDivCd()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgCust, RecordStatus.SUCCESS, false, null);
				}
				else
				{


					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgCust, null, true, errorMessage,
							Logging.SALES_ALIGNMENT_FEED,
							getTerritoryDivID(jnjGTIntSorgCust.getOrgId(), jnjGTIntSorgCust.getSalesOrgDivCd()));
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.

						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForOrg()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id " + jnjGTIntSorgCust.getOrgId() + "|"
								+ jnjGTIntSorgCust.getSalesOrgDivCd() + " was not processed successfully.");
					}
				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		return recordStatus;
	}

	/**
	 * This method will remove handle the Territory Divison Cust Model with del inicator True.
	 * 
	 * @param JnjGTIntSalesAlignmentCustModel
	 * @return
	 */
	private boolean removeTerriToryDivisonCustomerMapping(final JnjGTIntSalesAlignmentCustModel jnjGTIntSorgCust)
	{

		boolean recordStatus = false;
		final JnJB2BUnitModel JnJB2BUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(jnjGTIntSorgCust.getUniCustomerNumber(),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgCust.getSourceSystemId()));
		final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
				getTerritoryDivID(jnjGTIntSorgCust.getOrgId(), jnjGTIntSorgCust.getSalesOrgDivCd()),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgCust.getSourceSystemId()));
		if (JnJB2BUnitModel != null && jnjGTTerritoryDivisonModel != null)
		{
			//Map the intermediate data in  TerritoryDivCustRel model.
			final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
			// add the   TerritoryDivison model in the  source.
			jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
			// add the   B2BUnit  model in the  target.
			jnjGTTerritoryDivCustRelModel.setTarget(JnJB2BUnitModel);
			//Load the existing Model
			final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
					.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
			//
			recordStatus = removeTerritoryCustModel(recordStatus, jnjGTTerritoryDivisonModel, jnjGTTerritoryDivCustRelModelList);
		}
		return recordStatus;
	}

	/**
	 * This method will remove the relationship of the Cust Model From the B2bUnit and Users
	 * 
	 * @param recordStatus
	 * @param jnjGTTerritoryDivisonModel
	 * @param jnjGTTerritoryDivCustRelModelList
	 * @return
	 */
	private boolean removeTerritoryCustModel(boolean recordStatus, final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel,
			final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList)
	{

		for (final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRel : jnjGTTerritoryDivCustRelModelList)
		{
			// Remove the  Territory Divison model from the  B2BUnit or User groups
			final PrincipalModel pricipalModel = jnjGTTerritoryDivCustRel.getTarget();
			final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
			groups.addAll(pricipalModel.getGroups());
			groups.remove(jnjGTTerritoryDivisonModel);
			pricipalModel.setGroups(groups);
			jnjGTB2BUnitService.saveItemModel(pricipalModel);
		}
		recordStatus = jnjGtTerritoryService.removeTerritoryDivisonCustRelModel(jnjGTTerritoryDivCustRelModelList);
		return recordStatus;
	}

	/**
	 * This method concatenates the Both the parameter with a pipe.
	 * 
	 * @param orgId
	 * @param salesOrgDivCd
	 * @return
	 */
	private String getTerritoryDivID(final String orgId, final String salesOrgDivCd)
	{
		return (orgId + Jnjgtb2binboundserviceConstants.PIPE_STRING + salesOrgDivCd);
	}

	/**
	 * @param jnjGTIntSorgCust
	 * @param JnJB2BUnitModel
	 * @param jnjGTTerritoryDivisonModel
	 * @param jnjGTTerritoryDivCustRelModel
	 * @return
	 */
	private boolean mapTerritoryDivCustRelModel(final JnjGTIntSalesAlignmentCustModel jnjGTIntSorgCust,
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
		Date endDate = jnjGTIntSorgCust.getAlnAfflnEndDate();
		Date effectiveDate = jnjGTIntSorgCust.getAlnAfflnEffectiveDate();
		if (endDate == null)
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			endDate = calendar.getTime();
			jnjGTTerritoryDivCustRelModel.setEndDate(endDate);
		}
		else
		{
			jnjGTTerritoryDivCustRelModel.setEndDate(jnjGTIntSorgCust.getAlnAfflnEndDate());
		}
		if (effectiveDate == null)
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			effectiveDate = calendar.getTime();
			jnjGTTerritoryDivCustRelModel.setEffectiveDate(effectiveDate);
		}
		else
		{
			jnjGTTerritoryDivCustRelModel.setEffectiveDate(jnjGTIntSorgCust.getAlnAfflnEffectiveDate());
		}
		if (JnjGTCoreUtil.compareDates(effectiveDate, endDate))
		{
			//Checking whether the Group Exist in the Existing Collection of the B2BUnit
			if (!JnJB2BUnitModel.getGroups().contains(jnjGTTerritoryDivisonModel))
			{
				//Adding the TerritoryDivisonModel in the groups  of the B2BUnit.
				final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
				groups.addAll(JnJB2BUnitModel.getGroups());
				groups.add(jnjGTTerritoryDivisonModel);
				JnJB2BUnitModel.setGroups(groups);
			}
			recordStatus = jnjGTB2BUnitService.saveItemModel(JnJB2BUnitModel);
		}
		return recordStatus;
	}





	/**
	 * This method maps Data for Territory and Division to User.
	 */
	public boolean mapSalesAlignmentDataForUser()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		// we want to keep the  status as "PENDING" until we get the correct value in access by field i.e. WWID so once the access by value is changes
		// then the record status will change to success and create a proper relation with territory division customer model.
		// This flag is used to avoid the status change and also not to increase the write attempts of the current record.
		boolean isUserAccessByValid = false;
		String errorMessage = null;
		// Fetch all those records whose record status is pending.
		final List<JnjGTIntSalesAlignmentUserModel> jnjGTIntSorgUserList = (List<JnjGTIntSalesAlignmentUserModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntSalesAlignmentUserModel._TYPECODE, RecordStatus.PENDING);

		if (!jnjGTIntSorgUserList.isEmpty())
		{
			for (final JnjGTIntSalesAlignmentUserModel jnjGTIntSorgUser : jnjGTIntSorgUserList)
			{
				recordStatus = false;
				isUserAccessByValid = false;
				try
				{
					if (StringUtils.isEmpty(jnjGTIntSorgUser.getDelIndiactor())
							|| (StringUtils.isNotEmpty(jnjGTIntSorgUser.getDelIndiactor()) && jnjGTIntSorgUser.getDelIndiactor()
									.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_NO)))
					{
						if (null != jnjGTIntSorgUser.getWwid())
						{
							// Check whether the User Exist with the WWID for the Incoming Record.
							final JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
							customer.setWwid(jnjGTIntSorgUser.getWwid());
							final List<JnJB2bCustomerModel> JnJB2bCustomerModelList = jnjGTCustomerService
									.getJnJB2bCustomerModels(customer);
							if (CollectionUtils.isNotEmpty(JnJB2bCustomerModelList))
							{
								for (final JnJB2bCustomerModel JnJB2bCustomerModel : JnJB2bCustomerModelList)
								{
									// Check whether the  TerritoryDivisonModel exist or not  in the incoming Unit exist or not.
									if (JnJB2bCustomerModel != null
											&& JnJB2bCustomerModel.getAccessBy() != null
											&& JnJB2bCustomerModel.getAccessBy().name()
													.equalsIgnoreCase(Jnjb2bCoreConstants.SalesAlignment.ACCESS_BY_WWID)
											&& JnJB2bCustomerModel.getDivison() != null
											&& JnJB2bCustomerModel.getDivison().equalsIgnoreCase(jnjGTIntSorgUser.getSalesOrgDivCd()))
									{
										JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService
												.getTerritoryDivisonByUid(
														getTerritoryDivID(jnjGTIntSorgUser.getOrgId(), jnjGTIntSorgUser.getSalesOrgDivCd()),
														JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgUser.getSourceSystemId()));
										// if the model  doesn't exit create a new TerritoryDivisonModel
										if (jnjGTTerritoryDivisonModel == null)
										{
											jnjGTTerritoryDivisonModel = modelService.create(JnjGTTerritoryDivisonModel.class);
										}
										//Map the intermediate data in  TerritoryDivison model.
										jnjGTTerritoryDivisonModel = mapTerritoryDivisonModelFromUser(jnjGTIntSorgUser,
												jnjGTTerritoryDivisonModel);
										//Save the TerritoryDivisonModel
										jnjGtTerritoryService.saveTerritoryDivisonModel(jnjGTTerritoryDivisonModel);

										//Map the intermediate data in  TerritoryDivCustRel model.
										JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
										// add the   TerritoryDivison model in the  source.
										jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
										// add the   B2BUnit  model in the  target.

										jnjGTTerritoryDivCustRelModel.setTarget(JnJB2bCustomerModel);
										//Load the existing Model
										final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
												.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
										//If the model doesn't Load create  a new Model
										if (jnjGTTerritoryDivCustRelModelList.isEmpty())
										{
											jnjGTTerritoryDivCustRelModel = modelService.create(JnjGTTerritoryDivCustRelModel.class);
											recordStatus = mapTerritoryDivisonCustRelModelForUser(jnjGTIntSorgUser, JnJB2bCustomerModel,
													jnjGTTerritoryDivisonModel, jnjGTTerritoryDivCustRelModel);
										}
										else
										{
											for (final JnjGTTerritoryDivCustRelModel jnjTerritoryCustRelModel : jnjGTTerritoryDivCustRelModelList)
											{
												recordStatus = mapTerritoryDivisonCustRelModelForUser(jnjGTIntSorgUser, JnJB2bCustomerModel,
														jnjGTTerritoryDivisonModel, jnjTerritoryCustRelModel);
											}
										}

									}
									else
									{
										if (JnJB2bCustomerModel.getAccessBy() != null
												&& !JnJB2bCustomerModel.getAccessBy().name()
														.equalsIgnoreCase(Jnjb2bCoreConstants.SalesAlignment.ACCESS_BY_WWID))
										{
											errorMessage = "Record Failed Because User Exist But It Doesnt Have Access By WWID"
													+ jnjGTIntSorgUser.getWwid();
											isUserAccessByValid = true;
										}
										else
										{
											errorMessage = "Record Failed Because User Exist But It Doesnt Have Divison With Incoming Divison"
													+ jnjGTIntSorgUser.getWwid();
											isUserAccessByValid = true;
										}
										// We put the WWID as null so that Customer User territory relation ship is not created.
										jnjGTIntSorgUser.setWwid(null);
										createTerritoryDivisonWithNullWwid(false, jnjGTIntSorgUser);
										LOGGER.error(errorMessage);
									}
								}
							}
							else
							{

								recordStatus = createTerritoryDivisonWithNullWwid(recordStatus, jnjGTIntSorgUser);

							}

						}
						else
						{
							recordStatus = createTerritoryDivisonWithNullWwid(recordStatus, jnjGTIntSorgUser);

						}
					}
					else if (jnjGTIntSorgUser.getDelIndiactor() != null
							&& jnjGTIntSorgUser.getDelIndiactor().equalsIgnoreCase(
									Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_YES))
					{
						recordStatus = removeTerriToryDivisonWithUserMapping(jnjGTIntSorgUser);
					}
				}

				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelRemovalException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
							+ "MOdel Removal Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
							+ "Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}

				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id  " + jnjGTIntSorgUser.getOrgId() + "|"
								+ jnjGTIntSorgUser.getSalesOrgDivCd()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgUser, RecordStatus.SUCCESS, false, null);
				}
				else if (!isUserAccessByValid)
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntSorgUser, null, true, errorMessage,
							Logging.SALES_ALIGNMENT_FEED,
							getTerritoryDivID(jnjGTIntSorgUser.getOrgId(), jnjGTIntSorgUser.getSalesOrgDivCd()));
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataForUser()" + Logging.HYPHEN
								+ "The Record with Territory and Div Id " + jnjGTIntSorgUser.getOrgId() + "|"
								+ jnjGTIntSorgUser.getSalesOrgDivCd() + " was not processed successfully.");
					}

				}

			}//End Of For LOOP
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentData()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * @param recordStatus
	 * @param jnjGTIntSorgUser
	 * @return
	 */
	private boolean createTerritoryDivisonWithNullWwid(boolean recordStatus, final JnjGTIntSalesAlignmentUserModel jnjGTIntSorgUser)
	{
		JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
				getTerritoryDivID(jnjGTIntSorgUser.getOrgId(), jnjGTIntSorgUser.getSalesOrgDivCd()),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgUser.getSourceSystemId()));
		// if the model  doesn't exit create a new TerritoryDivisonModel
		if (jnjGTTerritoryDivisonModel == null)
		{
			jnjGTTerritoryDivisonModel = modelService.create(JnjGTTerritoryDivisonModel.class);
		}
		//Map the intermediate data in  TerritoryDivison model.
		jnjGTTerritoryDivisonModel = mapTerritoryDivisonModelFromUser(jnjGTIntSorgUser, jnjGTTerritoryDivisonModel);
		//Save the TerritoryDivisonModel
		recordStatus = jnjGtTerritoryService.saveTerritoryDivisonModel(jnjGTTerritoryDivisonModel);

		//Load the existing Model
		final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
				.getTerritoryDivCustRelWithNullTarget(jnjGTTerritoryDivisonModel.getPk(), jnjGTIntSorgUser.getWwid(),
						jnjGTIntSorgUser.getUniCustomerNumber());

		//If the model doesn't Load create  a new Model
		if (jnjGTTerritoryDivCustRelModelList.isEmpty())
		{
			final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = modelService
					.create(JnjGTTerritoryDivCustRelModel.class);
			recordStatus = mapTerritoryDivisonCustRelModelForUser(jnjGTIntSorgUser, null, jnjGTTerritoryDivisonModel,
					jnjGTTerritoryDivCustRelModel);
		}
		else
		{
			for (final JnjGTTerritoryDivCustRelModel jnjTerritoryCustRelModel : jnjGTTerritoryDivCustRelModelList)
			{
				recordStatus = mapTerritoryDivisonCustRelModelForUser(jnjGTIntSorgUser, null, jnjGTTerritoryDivisonModel,
						jnjTerritoryCustRelModel);
			}
		}
		return recordStatus;
	}

	/**
	 * This method will remove handle the Territory Divison Cust Model with del inicator True.
	 * 
	 * @param JnjGTIntSalesAlignmentCustModel
	 * @return
	 */
	private boolean removeTerriToryDivisonWithUserMapping(final JnjGTIntSalesAlignmentUserModel jnjGTIntSorgUser)
	{

		boolean recordStatus = false;
		final JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
		customer.setWwid(jnjGTIntSorgUser.getWwid());
		final List<JnJB2bCustomerModel> customerList = jnjGTCustomerService.getJnJB2bCustomerModels(customer);
		if (CollectionUtils.isNotEmpty(customerList))
		{
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
					getTerritoryDivID(jnjGTIntSorgUser.getOrgId(), jnjGTIntSorgUser.getSalesOrgDivCd()),
					JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgUser.getSourceSystemId()));
			for (final JnJB2bCustomerModel JnJB2bCustomerModel : customerList)
			{
				if (jnjGTTerritoryDivisonModel != null)
				{
					//Map the intermediate data in  TerritoryDivCustRel model.
					final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
					// add the   TerritoryDivison model in the  source.
					jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
					// add the   B2BUnit  model in the  target.
					jnjGTTerritoryDivCustRelModel.setTarget(JnJB2bCustomerModel);
					//Load the existing Model
					final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGtTerritoryService
							.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
					//
					recordStatus = removeTerritoryCustModel(recordStatus, jnjGTTerritoryDivisonModel,
							jnjGTTerritoryDivCustRelModelList);
				}
			}
		}
		else
		{
			LOGGER.info("User doesnt exsit with wwid" + jnjGTIntSorgUser.getWwid());
		}
		return recordStatus;
	}


	/**
	 * @param jnjGTIntSorgUser
	 * @param JnJB2bCustomerModel
	 * @param jnjGTTerritoryDivisonModel
	 * @param jnjGTTerritoryDivCustRelModel
	 */
	private boolean mapTerritoryDivisonCustRelModelForUser(final JnjGTIntSalesAlignmentUserModel jnjGTIntSorgUser,
			final JnJB2bCustomerModel JnJB2bCustomerModel, final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel,
			final JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel)
	{

		boolean recordStatus;
		// add the   TerritoryDivison model in the  source.
		jnjGTTerritoryDivCustRelModel.setSource(jnjGTTerritoryDivisonModel);
		// add the   B2BUnit  model in the  target.
		jnjGTTerritoryDivCustRelModel.setTarget(JnJB2bCustomerModel);
		//Set the effective and End Dates
		jnjGTTerritoryDivCustRelModel.setEffectiveDate(jnjGTIntSorgUser.getAlnAfflnEffectiveDate());
		jnjGTTerritoryDivCustRelModel.setEndDate(jnjGTIntSorgUser.getAlnAfflnEndDate());
		// Populating the org type code in relation model.
		jnjGTTerritoryDivCustRelModel.setOrgTypeCd(jnjGTIntSorgUser.getOrgTypeCd());
		//Setting the Customer in the JnjTerritoryDivison Model In case of the User.
		jnjGTTerritoryDivCustRelModel
				.setUniqueCustomer(jnjGTB2BUnitService.getB2BUnitModelForUid(jnjGTIntSorgUser.getUniCustomerNumber(),
						JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgUser.getSourceSystemId())));
		if (StringUtils.isNotEmpty(jnjGTIntSorgUser.getWwid()))
		{
			jnjGTTerritoryDivCustRelModel.setWwid(jnjGTIntSorgUser.getWwid());
		}
		jnjGTTerritoryDivCustRelModel.setInvalidated(Boolean.FALSE);
		if (jnjGTIntSorgUser.getPrimRepInd() != null
				&& jnjGTIntSorgUser.getPrimRepInd().equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_YES))
		{
			//Setting the  primarySalesRep  Boolean  for  JnjTerritoryDivison Model In case of the User.
			jnjGTTerritoryDivCustRelModel.setPrimarySalesRep(Boolean.valueOf(true));
		}
		else if (jnjGTIntSorgUser.getPrimRepInd() != null
				&& jnjGTIntSorgUser.getPrimRepInd().equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_NO))
		{
			//Setting the  primarySalesRep  Boolean  for  JnjTerritoryDivison Model In case of the User.
			jnjGTTerritoryDivCustRelModel.setPrimarySalesRep(Boolean.valueOf(false));
		}
		//Save the TerritoryDivisonModel
		recordStatus = jnjGtTerritoryService.saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRelModel);
		Date endDate = jnjGTIntSorgUser.getAlnAfflnEndDate();
		Date effectiveDate = jnjGTIntSorgUser.getAlnAfflnEffectiveDate();
		if (endDate == null)
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			endDate = calendar.getTime();
		}
		if (effectiveDate == null)
		{
			final Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			effectiveDate = calendar.getTime();
		}

		if (JnjGTCoreUtil.compareDates(effectiveDate, endDate))
		{
			if (JnJB2bCustomerModel != null)
			{
				//Checking whether the Territory Divison Model exist in the groups or not.
				if (!JnJB2bCustomerModel.getGroups().contains(jnjGTTerritoryDivisonModel))
				{
					//Adding the TerritoryDivisonModel in the groups  of the User.
					final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
					groups.addAll(JnJB2bCustomerModel.getGroups());
					groups.add(jnjGTTerritoryDivisonModel);
					JnJB2bCustomerModel.setGroups(groups);
				}
				recordStatus = jnjGTCustomerService.saveJnjGTB2bCustomer(JnJB2bCustomerModel);
			}
		}

		return recordStatus;
	}

	private JnjGTTerritoryDivisonModel mapTerritoryDivisonModelFromUser(final JnjGTIntSalesAlignmentUserModel jnjGTIntSorgUser,
			final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgUser.getOrgId()) && StringUtils.isNotEmpty(jnjGTIntSorgUser.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setUid(getTerritoryDivID(jnjGTIntSorgUser.getOrgId(), jnjGTIntSorgUser.getSalesOrgDivCd()));
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgUser.getSalesOrgDivCd()))
		{
			jnjGTTerritoryDivisonModel.setDivCode(jnjGTIntSorgUser.getSalesOrgDivCd());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgUser.getOrgId()))
		{
			jnjGTTerritoryDivisonModel.setTerritoryCode(jnjGTIntSorgUser.getOrgId());
		}
		if (jnjGTIntSorgUser.getAlnAfflnEffectiveDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEffectiveDate(jnjGTIntSorgUser.getAlnAfflnEffectiveDate());
		}
		if (jnjGTIntSorgUser.getAlnAfflnEndDate() != null)
		{
			jnjGTTerritoryDivisonModel.setEndDate(jnjGTIntSorgUser.getAlnAfflnEndDate());
		}
		if (StringUtils.isNotEmpty(jnjGTIntSorgUser.getSourceSystemId()))
		{
			jnjGTTerritoryDivisonModel
					.setSourceSystemId(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSorgUser.getSourceSystemId()));
		}
		jnjGTTerritoryDivisonModel.setInvalidated(Boolean.FALSE);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapTerritoryDivisonModel()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTTerritoryDivisonModel;
	}


	//Method to map Sales Alignment Data in Product.
	public boolean mapSalesAlignmentDataforProduct()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final List<JnjGTIntSalesAlignmentProductModel> jnjGTIntSalesAlignmentProductMapModelList = (List<JnjGTIntSalesAlignmentProductModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntSalesAlignmentProductModel._TYPECODE, RecordStatus.PENDING);
		boolean recordStatus = false;
		String errorMessage = null;
		if (!jnjGTIntSalesAlignmentProductMapModelList.isEmpty())
		{
			for (final JnjGTIntSalesAlignmentProductModel jnjGTIntSalesAlignmentProductMapModel : jnjGTIntSalesAlignmentProductMapModelList)
			{
				recordStatus = false;
				try
				{
					if (StringUtils.isEmpty(jnjGTIntSalesAlignmentProductMapModel.getDelIndiactor())
							|| StringUtils.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_NO,
									jnjGTIntSalesAlignmentProductMapModel.getDelIndiactor()))
					{

						final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
								getTerritoryDivID(jnjGTIntSalesAlignmentProductMapModel.getOrgId(),
										jnjGTIntSalesAlignmentProductMapModel.getSalesOrgDivCd()),
								JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSalesAlignmentProductMapModel.getSourceSysId()));
						if (null != jnjGTTerritoryDivisonModel)
						{
							//setting data in territory product model.
							JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel = new JnjGTTerritoryDivProdMappingModel();
							jnjGTTerritoryDivProdMappingModel.setTerritoryDivison(jnjGTTerritoryDivisonModel);
							jnjGTTerritoryDivProdMappingModel.setMajorGroupCode(jnjGTIntSalesAlignmentProductMapModel
									.getFranMajorProdGrpCd());
							//Checking whether the model exit or not
							jnjGTTerritoryDivProdMappingModel = jnjGtTerritoryService
									.getJnjGTTerritoryDivProdMappingModel(jnjGTTerritoryDivProdMappingModel);
							//if not create a new model.
							if (jnjGTTerritoryDivProdMappingModel == null)
							{
								jnjGTTerritoryDivProdMappingModel = modelService.create(JnjGTTerritoryDivProdMappingModel.class);
								jnjGTTerritoryDivProdMappingModel.setTerritoryDivison(jnjGTTerritoryDivisonModel);
								jnjGTTerritoryDivProdMappingModel.setMajorGroupCode(jnjGTIntSalesAlignmentProductMapModel
										.getFranMajorProdGrpCd());
							}
							//Save the new model.
							recordStatus = jnjGtTerritoryService.saveTerritoryDivisonProdModel(jnjGTTerritoryDivProdMappingModel);
						}
					}
					else if (jnjGTIntSalesAlignmentProductMapModel.getDelIndiactor().equalsIgnoreCase(
							Jnjgtb2binboundserviceConstants.SalesAlignment.FLAG_YES))
					{

						recordStatus = removeSalesAlignmentProductMapping(jnjGTIntSalesAlignmentProductMapModel);
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelRemovalException exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
							+ "MOdel Removal Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()"
								+ Logging.HYPHEN + "The Record with Territory and Div Id  "
								+ jnjGTIntSalesAlignmentProductMapModel.getOrgId() + "|"
								+ jnjGTIntSalesAlignmentProductMapModel.getSalesOrgDivCd()
								+ " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService
							.updateIntermediateRecord(jnjGTIntSalesAlignmentProductMapModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(
							jnjGTIntSalesAlignmentProductMapModel,
							null,
							true,
							errorMessage,
							Logging.SALES_ALIGNMENT_FEED,
							getTerritoryDivID(jnjGTIntSalesAlignmentProductMapModel.getOrgId(),
									jnjGTIntSalesAlignmentProductMapModel.getSalesOrgDivCd()));
					if (LOGGER.isDebugEnabled())
					{
						//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
						LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()"
								+ Logging.HYPHEN + "The Record with Territory and Div Id "
								+ jnjGTIntSalesAlignmentProductMapModel.getOrgId() + "|"
								+ jnjGTIntSalesAlignmentProductMapModel.getSalesOrgDivCd() + " was not processed successfully.");
					}

				}
			}
		}
		else
		{
			if (LOGGER.isDebugEnabled())
			{
				LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
						+ "No JnjGTIntSorgOrgMap Records with status 'PENDING' exists. Exiting the write process.");
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SALES_ALIGNMENT_FEED + Logging.HYPHEN + "mapSalesAlignmentDataforProduct()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return recordStatus;
	}

	/**
	 * This method is used for removing the Product Mapping from JnjGTIntSalesAlignmentProductModel if the Delete
	 * Indicator is true
	 * 
	 * @param jnjGTIntSalesAlignmentProductMapModel
	 * @return
	 */
	private boolean removeSalesAlignmentProductMapping(
			final JnjGTIntSalesAlignmentProductModel jnjGTIntSalesAlignmentProductMapModel)
	{
		boolean recordStatus = false;
		final JnjGTTerritoryDivisonModel jnjGTTerritoryDivisonModel = jnjGtTerritoryService.getTerritoryDivisonByUid(
				getTerritoryDivID(jnjGTIntSalesAlignmentProductMapModel.getOrgId(),
						jnjGTIntSalesAlignmentProductMapModel.getSalesOrgDivCd()),
				JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntSalesAlignmentProductMapModel.getSourceSysId()));
		JnjGTTerritoryDivProdMappingModel jnjGTTerritoryDivProdMappingModel = new JnjGTTerritoryDivProdMappingModel();
		jnjGTTerritoryDivProdMappingModel.setTerritoryDivison(jnjGTTerritoryDivisonModel);
		//Checking whether the model exit or not
		jnjGTTerritoryDivProdMappingModel = jnjGtTerritoryService
				.getJnjGTTerritoryDivProdMappingModel(jnjGTTerritoryDivProdMappingModel);
		if (jnjGTTerritoryDivProdMappingModel != null)
		{
			recordStatus = jnjGtTerritoryService.removeJnjGTTerritoryDivProdMappingModel(jnjGTTerritoryDivProdMappingModel);
		}
		return recordStatus;
	}

	@Override
	public void processIntermediateRecords(final String facadeBeanId)
	{
		// TODO Auto-generated method stub

	}

}
