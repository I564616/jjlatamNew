/**
 *
 */
package com.jnj.gt.mapper.user;

import de.hybris.platform.b2b.company.B2BCommerceB2BUserGroupService;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.enums.AccessBy;
import com.jnj.core.enums.CustomerStatus;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnjConfigModel;
import com.jnj.core.model.JnjGTTerritoryDivCustRelModel;
import com.jnj.core.model.JnjGTTerritoryDivisonModel;
import com.jnj.core.services.JnjConfigService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnjGTIntB2bCustomerModel;
import com.jnj.gt.model.JnjGTIntUserB2bUnitModel;
import com.jnj.gt.model.JnjGTIntUserPermissionModel;
import com.jnj.gt.model.JnjGTIntUserSalesOrgModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.core.services.customer.JnjGTCustomerService;
import com.jnj.core.services.territory.JnjGTTerritoryService;
import com.jnj.gt.service.user.JnjGTUserFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;

/**
 * @author sakshi.kashiva
 *
 */
public class JnjGTUserProfileDataLoadMapper extends JnjAbstractMapper
{
	private static final Logger LOGGER = Logger.getLogger(JnjGTUserProfileDataLoadMapper.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Resource(name = "GTCustomerService")
	private JnjGTCustomerService jnjGTCustomerService;

	@Autowired
	private JnjGTB2BUnitService jnjGTB2BUnitService;

	@Autowired
	private JnjGTUserFeedService jnjGTUserFeedService;

	@Autowired
	private JnjGTTerritoryService jnjGTTerritoryService;

	@Autowired
	ModelService modelService;

	@Autowired
	private B2BCommerceB2BUserGroupService b2BCommerceB2BUserGroupService;

	@Autowired
	private JnjConfigService jnjConfigService;

	@Autowired
	private CommonI18NService commonI18NService;

	/*
	 * Main Method invoking the business logic
	 */
	@Override
	public void processIntermediateRecords()
	{
		mapB2bCustomerDataToHybris();
	}

	//Method to map User Profile Data in Hybris
	public boolean mapB2bCustomerDataToHybris()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("mapUserDataToHybris()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean recordStatus = false;
		String errorMessage = null;
		final Collection<JnjGTIntB2bCustomerModel> jnjGTIntB2bCustomerModelList = (Collection<JnjGTIntB2bCustomerModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntB2bCustomerModel._TYPECODE, RecordStatus.PENDING);
		if (CollectionUtils.isNotEmpty(jnjGTIntB2bCustomerModelList))
		{
			for (final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel : jnjGTIntB2bCustomerModelList)
			{
				recordStatus = false;
				try
				{
					//Fetching the list for b2bunit for the corresponding customer
					final List<JnjGTIntUserB2bUnitModel> jnjGTIntUserB2bUnitModelList = jnjGTUserFeedService
							.getIntB2BUnitForEmail(jnjGTIntB2bCustomerModel);
					//checking if the List is Empty or not.
					if (!jnjGTIntUserB2bUnitModelList.isEmpty())
					{
						JnJB2bCustomerModel customer = new JnJB2bCustomerModel();
						customer.setUid(StringUtils.lowerCase(jnjGTIntB2bCustomerModel.getEmail()));
						//Fetching the User Model
						customer = jnjGTCustomerService.getJnJB2bCustomerModel(customer);
						//Validating if the model is created
						if (null == customer)
						{
							customer = modelService.create(JnJB2bCustomerModel.class);
							customer.setUid(jnjGTIntB2bCustomerModel.getEmail());
							customer.setEmail(jnjGTIntB2bCustomerModel.getEmail());
						}

						customer.setGatewayUserId(jnjGTIntB2bCustomerModel.getId());
						//Adding consequent B2bUnit in groups.
						final Set<PrincipalGroupModel> groups = new HashSet<PrincipalGroupModel>();
						for (final JnjGTIntUserB2bUnitModel jnjGTIntUserB2bUnitModel : jnjGTIntUserB2bUnitModelList)
						{
							final JnJB2BUnitModel b2bUnitModel = jnjGTB2BUnitService.getB2BUnitModelForUid(
									jnjGTIntUserB2bUnitModel.getCustomerNumber(),
									JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntUserB2bUnitModel.getSource()));
							if (b2bUnitModel != null)
							{
								groups.add(b2bUnitModel);
								final UserGroupModel b2bCustomerGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID(
										Jnjb2bCoreConstants.B2BUnit.B2BCUSTOMERGROUP, UserGroupModel.class);
								groups.add(b2bCustomerGroup);
								customer.setGroups(groups);

							}
							else
							{
								recordStatus = false;
								throw new BusinessException("B2BUnit Not Found With Customer Id "
										+ jnjGTIntUserB2bUnitModel.getCustomerNumber() + "For Customer Number"
										+ jnjGTIntB2bCustomerModel.getEmail());
							}
						}

						//Fetching List for Permissions for the corresponding customer
						final List<JnjGTIntUserPermissionModel> jnjGTIntUserPermissionModelList = jnjGTUserFeedService
								.getIntUserPermissionIdForCustomer(jnjGTIntB2bCustomerModel);
						//Iterating the list
						for (final JnjGTIntUserPermissionModel jnjGTIntUserPermissionModel : jnjGTIntUserPermissionModelList)
						{
							final List<JnjConfigModel> configList = jnjConfigService.getConfigModelsByIdAndKey(
									jnjGTIntUserPermissionModel.getPermissionCode(), jnjGTIntUserPermissionModel.getPermissionCode());
							if (CollectionUtils.isNotEmpty(configList))
							{
								final String groupId = configList.get(0).getValue();
								if (StringUtils.isNotEmpty(groupId))
								{
									final UserGroupModel userGroup = b2BCommerceB2BUserGroupService.getUserGroupForUID(groupId,
											UserGroupModel.class);
									if (userGroup != null)
									{
										groups.add(userGroup);
										customer.setGroups(groups);
									}
								}
								else
								{
									LOGGER.info("Permission Group Not Found With Permission Group"
											+ jnjGTIntUserPermissionModel.getPermissionCode() + "For Customer Number"
											+ jnjGTIntB2bCustomerModel.getEmail());
								}
							}
							if (jnjGTIntUserPermissionModel.getPermissionCode().equals(
									Jnjgtb2binboundserviceConstants.UserProfile.NO_CHARGE_PERMISSION))
							{
								customer.setNoCharge(Boolean.valueOf(true));
							}

						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getUserType()))
						{
							customer.setUserType(jnjGTIntB2bCustomerModel.getUserType());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getFirstName())
								&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getLastName()))
						{
							customer.setName(jnjGTIntB2bCustomerModel.getFirstName() + Jnjb2bCoreConstants.UserSearch.SPACE
									+ jnjGTIntB2bCustomerModel.getLastName());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getSource()))
						{
							if (JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()).equalsIgnoreCase(
									Jnjgtb2binboundserviceConstants.UserProfile.MDD))
							{
								customer.setMddSector(true);
								customer.setSourceId(Jnjgtb2binboundserviceConstants.UserProfile.MDD);
							}
							if (JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()).equalsIgnoreCase(
									Jnjgtb2binboundserviceConstants.UserProfile.CONSUMER))
							{
								customer.setConsumerSector(true);
								customer.setSourceId(Jnjgtb2binboundserviceConstants.UserProfile.CONSUMER);
							}
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getRecTimeStamp()))
						{
							customer.setRecTimeStamp(Date.valueOf(jnjGTIntB2bCustomerModel.getRecTimeStamp()));
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getEmail()))
						{
							customer.setEmail(jnjGTIntB2bCustomerModel.getEmail());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getSuperVisorFirstName())
								&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getSuperVisorLastName()))
						{
							customer.setSuperVisorName(jnjGTIntB2bCustomerModel.getSuperVisorFirstName()
									+ Jnjb2bCoreConstants.UserSearch.SPACE + jnjGTIntB2bCustomerModel.getSuperVisorLastName());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getSuuperVisorNumber()))
						{
							customer.setSuperVisorNumber(jnjGTIntB2bCustomerModel.getSuuperVisorNumber());
						}
						if (null != (jnjGTIntB2bCustomerModel.getRegistrationDate()))
						{
							customer.setRegistrationDate(jnjGTIntB2bCustomerModel.getRegistrationDate());
						}
						if (null != (jnjGTIntB2bCustomerModel.getAuthorizedBuyer()))
						{
							customer.setAuthorizedBuyer(jnjGTIntB2bCustomerModel.getAuthorizedBuyer());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getCompanyName()))
						{
							customer.setCompanyName(jnjGTIntB2bCustomerModel.getCompanyName());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getStatus()))
						{
							if (jnjGTIntB2bCustomerModel.getStatus().equals(
									Jnjgtb2binboundserviceConstants.UserProfile.ACTIVE_STATUS_CODE))
							{
								customer.setStatus(CustomerStatus.ACTIVE);
								customer.setActive(Boolean.valueOf(true));
							}
							else if (jnjGTIntB2bCustomerModel.getStatus().equals(
									Jnjgtb2binboundserviceConstants.UserProfile.DISABLE_STATUS_CODE))
							{
								customer.setStatus(CustomerStatus.DISABLED);
								customer.setActive(Boolean.valueOf(false));
							}
						}
						//Logic to set address in customer model.
						final AddressModel AddressModel = new AddressModel();
						AddressModel.setOwner(customer);
						AddressModel.setStreetnumber(jnjGTIntB2bCustomerModel.getAddress1());
						AddressModel.setStreetname(jnjGTIntB2bCustomerModel.getAddress2());
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getCountry()))
						{

							AddressModel.setCountry(commonI18NService.getCountry(jnjGTIntB2bCustomerModel.getCountry()));
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getPhone()))
						{
							AddressModel.setPhone1(jnjGTIntB2bCustomerModel.getPhone());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getPostalCode()))
						{
							AddressModel.setPostalcode(jnjGTIntB2bCustomerModel.getPostalCode());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getMobile()))
						{
							AddressModel.setPhone2(jnjGTIntB2bCustomerModel.getMobile());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getFax()))
						{
							AddressModel.setFax(jnjGTIntB2bCustomerModel.getFax());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getState()))
						{
							AddressModel.setDistrict(jnjGTIntB2bCustomerModel.getState());
						}
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getCity()))
						{
							AddressModel.setTown(jnjGTIntB2bCustomerModel.getCity());
						}
						AddressModel.setContactAddress(Boolean.TRUE);
						final ArrayList<AddressModel> address = new ArrayList<AddressModel>();
						address.add(AddressModel);
						customer.setAddresses(address);
						if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getWwid()))
						{
							customer.setWwid(jnjGTIntB2bCustomerModel.getWwid());
						}
						final String gateWayAccessCode = jnjGTIntB2bCustomerModel.getGateWayAccessCode();
						if (StringUtils.isNotEmpty(gateWayAccessCode))
						{
							customer.setGateWayAccessCode(jnjGTIntB2bCustomerModel.getGateWayAccessCode());
							if (gateWayAccessCode.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.UserProfile.ACCESS_BY_TERRITORY))
							{
								customer.setAccessBy(AccessBy.TERRITORIES);
							}
							else if (gateWayAccessCode.equalsIgnoreCase(Jnjgtb2binboundserviceConstants.UserProfile.ACCESS_BY_WWID))
							{
								customer.setAccessBy(AccessBy.WWID);
							}
						}
						else
						{
							customer.setAccessBy(AccessBy.NOT_SALES_REP);
						}
						final String divison = jnjGTIntB2bCustomerModel.getSalesOrg();
						final List<JnjConfigModel> divisonList = jnjConfigService.getConfigModelsByIdAndKey(
								Jnjb2bCoreConstants.Register.DIVISIONS, divison);
						if (CollectionUtils.isNotEmpty(divisonList))
						{
							customer.setDivison(divisonList.get(0).getKey());

						}
						//logic for Territory Division Model.
						final List<JnjGTIntUserSalesOrgModel> jnjGTIntUserSalesOrgModelList = jnjGTUserFeedService
								.getIntUserSalesOrgForCustomer(jnjGTIntB2bCustomerModel);
						//checking if the List is Empty.
						if (CollectionUtils.isNotEmpty(jnjGTIntUserSalesOrgModelList)
								&& JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()).equalsIgnoreCase(
										Jnjgtb2binboundserviceConstants.UserProfile.MDD))
						{
							final List<JnjGTTerritoryDivisonModel> teritoryList = new ArrayList<JnjGTTerritoryDivisonModel>();
							//Iterating the List.
							for (final JnjGTIntUserSalesOrgModel jnjGTIntUserSalesOrgModel : jnjGTIntUserSalesOrgModelList)
							{
								JnjGTTerritoryDivisonModel territoryDiv = new JnjGTTerritoryDivisonModel();
								territoryDiv = jnjGTTerritoryService.getTerritoryDivisonByUid(
										jnjGTIntUserSalesOrgModel.getSalesOrgDivCdId() + "|" + jnjGTIntUserSalesOrgModel.getOrgId(),
										JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntUserSalesOrgModel.getSource()));
								//checking if the territory model exists or not.
								if (territoryDiv == null)
								{
									territoryDiv = modelService.create(JnjGTTerritoryDivisonModel.class);
									territoryDiv.setUid(jnjGTIntUserSalesOrgModel.getSalesOrgDivCdId() + "|"
											+ jnjGTIntUserSalesOrgModel.getOrgId());
								}

								territoryDiv.setTerritoryCode(jnjGTIntUserSalesOrgModel.getOrgId());
								territoryDiv.setDivCode(jnjGTIntUserSalesOrgModel.getSalesOrgDivCdId());
								territoryDiv.setSourceSystemId(JnjGTInboundUtil.fetchValidSourceSysId(jnjGTIntUserSalesOrgModel
										.getSource()));
								if (jnjGTTerritoryService.saveTerritoryDivisonModel(territoryDiv))
								{
									teritoryList.add(territoryDiv);
								}
							}
							if (teritoryList.size() == jnjGTIntUserSalesOrgModelList.size())
							{
								for (final JnjGTTerritoryDivisonModel territory : teritoryList)
								{
									groups.add(territory);
									customer.setGroups(groups);
								}
								recordStatus = jnjGTCustomerService.saveJnjGTB2bCustomerFeed(customer);
							}
							for (final JnjGTTerritoryDivisonModel territoryDiv : teritoryList)
							{
								JnjGTTerritoryDivCustRelModel jnjGTTerritoryDivCustRelModel = new JnjGTTerritoryDivCustRelModel();
								// add the   TerritoryDivison model in the  source.
								jnjGTTerritoryDivCustRelModel.setSource(territoryDiv);
								// add the customer model in the  target.
								jnjGTTerritoryDivCustRelModel.setTarget(customer);
								final List<JnjGTTerritoryDivCustRelModel> jnjGTTerritoryDivCustRelModelList = jnjGTTerritoryService
										.getTerritoryDivCustRel(jnjGTTerritoryDivCustRelModel);
								if (CollectionUtils.isEmpty(jnjGTTerritoryDivCustRelModelList))
								{
									jnjGTTerritoryDivCustRelModel = modelService.create(JnjGTTerritoryDivCustRelModel.class);
									// add the   TerritoryDivison model in the  source.
									jnjGTTerritoryDivCustRelModel.setSource(territoryDiv);
									// add the customer model in the  target.
									jnjGTTerritoryDivCustRelModel.setTarget(customer);
								}
								if (!jnjGTTerritoryService.saveTerritoryDivisonCustModel(jnjGTTerritoryDivCustRelModel))
								{
									modelService.remove(customer);
									recordStatus = false;
								}
							}
						}
						else
						{
							recordStatus = jnjGTCustomerService.saveJnjGTB2bCustomerFeed(customer);
							if (recordStatus)
							{
								/* Start cr_119 */
								//jnjGTCustomerService.getCurrentUser().setSendActivationMail(Boolean.FALSE);
								jnjGTCustomerService.generateResetPasswordDetails(customer);
							}
						}
					}
					else
					{
						recordStatus = false;
						errorMessage = "No B2BuNIT Found For Customer With Email" + jnjGTIntB2bCustomerModel.getEmail()
								+ "With User Id" + jnjGTIntB2bCustomerModel.getId();
						throw new BusinessException(errorMessage);
					}
				}
				catch (final UnknownIdentifierException exception)
				{
					LOGGER.error(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "Unknown Identifier Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelLoadingException exception)
				{
					LOGGER.error(Logging.ORDER_TEMPLATE_FEED + Logging.HYPHEN + "mapOrderTemplateEntryToHybris()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelNotFoundException exception)
				{
					LOGGER.error(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "Model Not Found Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final ModelSavingException exception)
				{
					LOGGER.error(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "MOdel Saving Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final BusinessException exception)
				{
					LOGGER.error(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "Buisness  Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				catch (final Exception exception)
				{
					LOGGER.error(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "Thwoable Exception occurred -" + exception.getMessage(), exception);
					errorMessage = exception.getMessage();
				}
				if (recordStatus)
				{
					//Record Successfully saved to Hybris. Changing the status to 'Success'.
					if (LOGGER.isDebugEnabled())
					{
						LOGGER.debug(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
								+ "The Record with Email Id: " + jnjGTIntB2bCustomerModel.getEmail() + "User Id:"
								+ jnjGTIntB2bCustomerModel.getId() + " processed successfully. Changing the Record status to 'SUCCESS'");
					}
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2bCustomerModel, RecordStatus.SUCCESS, false, null);
				}
				else
				{
					//Update the Write Dash Board and Updating the Status of Record to "Error" if the Retry Threshold is reached.
					jnjGTFeedService.updateIntermediateRecord(jnjGTIntB2bCustomerModel, null, true, errorMessage,
							Logging.USER_PROFILE_FEED, jnjGTIntB2bCustomerModel.getEmail());
					LOGGER.info(Logging.USER_PROFILE_FEED + Logging.HYPHEN + "mapUserDataToHybris()" + Logging.HYPHEN
							+ "The Record with Email Id: " + jnjGTIntB2bCustomerModel.getEmail() + "User Id:"
							+ jnjGTIntB2bCustomerModel.getId() + " was not processed successfully.");
				}
			}
		}
		return recordStatus;
	}

	@Override
	public void processIntermediateRecords(String facadeBeanId) {
		// TODO Auto-generated method stub
		
	}
}
