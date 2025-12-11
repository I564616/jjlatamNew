/**
 *
 */
package com.jnj.core.services.b2bunit.impl;

import de.hybris.platform.b2b.services.impl.DefaultB2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;

import jakarta.annotation.Resource;
//import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTOrderTypeComparator;
import com.jnj.core.dao.address.JnjGTAddressDao;
//import com.jnj.core.constants.Jnjb2bCoreConstants;
//import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.b2bunit.JnjGTB2BUnitDao;
import com.jnj.core.data.JnjGTDivisonData;
import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.enums.JnjGTUserTypes;
import com.jnj.core.enums.JnjOrderTypesEnum;
import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTSalesOrgCustomerModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.services.customer.JnjGTCustomerService;




/**
 * The JnjGTB2BUnitServiceImpl class contains the definition of all the method of the JnjGTB2BUnitService interface.
 *
 * @author sumit.y.kumar
 *
 */
public class DefaultJnjGTB2BUnitService extends DefaultB2BUnitService implements JnjGTB2BUnitService
{

	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTB2BUnitService.class);

	/*@Autowired
	protected CompanyB2BCommerceService companyB2BCommerceService;*/
	@Autowired
	private JnjGTAddressDao jnjGTAddressDao;

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected JnjGTB2BUnitDao jnjGTB2BUnitDao;

	@Autowired
	FlexibleSearchService flexibleSearchService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected SessionService sessionService;

	@Resource(name = "GTCustomerService")
	protected JnjGTCustomerService jnjGTCustomerService;

	
	/*public CompanyB2BCommerceService getCompanyB2BCommerceService() {
		return companyB2BCommerceService;
	}*/

	public ModelService getModelService() {
		return modelService;
	}

	public JnjGTB2BUnitDao getJnjGTB2BUnitDao() {
		return jnjGTB2BUnitDao;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public UserService getUserService() {
		return userService;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public JnjGTCustomerService getJnjGTCustomerService() {
		return jnjGTCustomerService;
	}

	/**
	 * {!{@inheritDoc}
	 */

	@Override
	public boolean saveItemModel(final ItemModel itemModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveItemModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean success = false;
		try
		{
			modelService.saveAll(itemModel);
			modelService.refresh(itemModel);
			if(itemModel.getOwner() == null) {
			ItemModel itemModel1 = modelService.get(itemModel);
			itemModel1.setOwner(itemModel);
			modelService.saveAll(itemModel1);
			modelService.refresh(itemModel1);
			}
			success = true;

		}
		catch (final ModelSavingException modelSavingException)
		{
			LOGGER.error(
					"saveItemModel()" + Logging.HYPHEN + "model is not saved into hybris database - "
							+ modelSavingException.getMessage(), modelSavingException);
			throw modelSavingException;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveItemModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return success;

	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public JnjGTSalesOrgCustomerModel getSalesOrgCustomerModel(final String salesOrg, final String distribChannel,
			final String division, final String sourceSysId)
	{
		return jnjGTB2BUnitDao.getSalesOrgCustomerModel(salesOrg, distribChannel, division, sourceSysId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnJB2BUnitModel getB2BUnitModelForUid(final String customerNumber, final String sourceSysId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getB2BUnitModelForUid()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		JnJB2BUnitModel JnJB2BUnitModel = null;

		try
		{
			if (StringUtils.isNotEmpty(customerNumber) && StringUtils.isNotEmpty(sourceSysId))
			{
				JnJB2BUnitModel = new JnJB2BUnitModel();
				if (LOGGER.isDebugEnabled())
				{
					LOGGER.debug("getB2BUnitModelForUid()" + Logging.HYPHEN + "CustomerNumber " + customerNumber + "Source Sys id "
							+ sourceSysId);
				}
				JnJB2BUnitModel.setUid(customerNumber);
				JnJB2BUnitModel.setSourceSysId(sourceSysId);

				// Invoking the Flexible Search Service to get the Load Translation Model on passing the Customer product number.
				JnJB2BUnitModel = flexibleSearchService.getModelByExample(JnJB2BUnitModel);
			}
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error("getB2BUnitModelForUid()" + Logging.HYPHEN + "model is not found in hybris database for customer number - "
					+ customerNumber + " and source system id " + sourceSysId + " - " + modelNotFoundException.getMessage());
			// Because for the given customer number and source system id we don't have model in hybris database.
			JnJB2BUnitModel = null;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getB2BUnitModelForUid()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return JnJB2BUnitModel;
	}

	/**
	 * This method fetches the current B2B Unit for the logged in user.
	 *
	 * @author sanchit.a.kumar
	 * @return JnJB2BUnitModel
	 */
	@Override
	public JnJB2BUnitModel getCurrentB2BUnit()
	{
		LOGGER.debug("calling getCurrentB2BUnit()");
		JnJB2BUnitModel currentB2bUnit = sessionService.getAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT);
		if (null == currentB2bUnit)
		{
			LOGGER.debug("Not able to find current unit in session, loading from current user");
			if(userService.getCurrentUser() instanceof JnJB2bCustomerModel) {
				currentB2bUnit = ((JnJB2bCustomerModel) userService.getCurrentUser()).getCurrentB2BUnit();
			}
			sessionService.setAttribute(Jnjb2bCoreConstants.CURRENT_B2BUNIT, currentB2bUnit);
		}
		return currentB2bUnit;
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public boolean isCustomerInternationalAff()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getCurrentB2BUnit()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		boolean isCustomerInternationalAffiliates = false;
		// Get the current b2bunit.
		final JnJB2BUnitModel b2bUnitModel = getCurrentB2BUnit();
		// Check if the indicator value is same as international affiliation key, if yes enter inside if block.
	if(b2bUnitModel.getIndicator()!=null){	
		if (Config.getParameter(Jnjb2bCoreConstants.B2BUnit.INTERNATIONAL_AFFILIATION).equals(b2bUnitModel.getIndicator()))
			{
			isCustomerInternationalAffiliates = true;
			}
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUPGCodeForDivision()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return isCustomerInternationalAffiliates;
	}

	/**
	 * The getUPGCodeForDivision method is used to get the user price group from the b2bunit.
	 *
	 * @param b2bUnit
	 * @param division
	 *
	 * @return userPriceGroup
	 */
	@Override
	public String getUPGCode(JnJB2BUnitModel b2bUnit, final String division)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUPGCode" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		String userPriceGroup = null;

		// if b2bunit is null then get the current b2b unit
		if (null == b2bUnit)
		{
			b2bUnit = getCurrentB2BUnit();
		}

		// From session get the current site i.e. MDD/CONS
		final String currentSite = sessionService.getAttribute(Jnjb2bCoreConstants.SITE_NAME);

		// If site is MDD, check for division as empty or null
		if (currentSite.equals(Jnjb2bCoreConstants.MDD) && StringUtils.isNotEmpty(division))
		{
			final Collection<JnjGTSalesOrgCustomerModel> salesOrgList = b2bUnit.getSalesOrgCustomers();
			// Check for empty or null on list.
			if (CollectionUtils.isNotEmpty(salesOrgList))
			{
				for (final JnjGTSalesOrgCustomerModel JnjGTSalesOrgCustomerModel : salesOrgList)
				{
					// By Using division, find out the JnjGTSalesOrgCustomerModel to fetch the User Price Group.
					if (null != JnjGTSalesOrgCustomerModel && JnjGTSalesOrgCustomerModel.getDivision().equalsIgnoreCase(division)
							&& null != JnjGTSalesOrgCustomerModel.getUserPriceGroup())
					{

						userPriceGroup = JnjGTSalesOrgCustomerModel.getUserPriceGroup().toString();
					}
				}
			}
		}
		// If site is CONS, return the userPriceGroup code
		else if (currentSite.equals(Jnjb2bCoreConstants.CONS))
		{
			userPriceGroup = String.valueOf(b2bUnit.getUserPriceGroup());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getUPGCode()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return userPriceGroup;
	}

	@Override
	public AddressModel getContactAddress(final JnJB2BUnitModel b2bUnit)
	{
		final JnJB2BUnitModel currentB2BUnit = b2bUnit != null ? b2bUnit : getCurrentB2BUnit();

		return null != currentB2BUnit.getContactAddress() && currentB2BUnit.getContactAddress().isActive() ? currentB2BUnit
				.getContactAddress() : null;
	}

	@Override
	public List<AddressModel> getShippingAddresses(final JnJB2BUnitModel b2bUnit)
	{
		final JnJB2BUnitModel unitModel = b2bUnit != null ? b2bUnit : getCurrentB2BUnit();
		List<AddressModel> shippingAddressList = null;
		if (null != unitModel)
		{
			shippingAddressList = new ArrayList<AddressModel>();
			for (final AddressModel address : unitModel.getAddresses())
			{
				if (address.isActive() && address.getShippingAddress().booleanValue())
				{
					shippingAddressList.add(address);
				}
			}
		}
		return shippingAddressList;
	}

	@Override
	public AddressModel getShippingAddress(final JnJB2BUnitModel b2bUnit)
	{
		final JnJB2BUnitModel b2bUnitModel = b2bUnit != null ? b2bUnit : getCurrentB2BUnit();
		AddressModel shippingAddress = null;
		if (null != b2bUnitModel.getShippingAddress() && b2bUnitModel.getShippingAddress().isActive())
		{
			shippingAddress = b2bUnitModel.getShippingAddress();
		}
		else if (CollectionUtils.isNotEmpty(getShippingAddresses(b2bUnitModel)))
		{
			shippingAddress = getShippingAddresses(b2bUnitModel).get(0);
		}
		else if (null != getContactAddress(b2bUnitModel))
		{
			shippingAddress = getContactAddress(b2bUnitModel);
		}
		return shippingAddress;
	}

	@Override
	public AddressModel getBillingAddress(final JnJB2BUnitModel b2bUnit)
	{
		final JnJB2BUnitModel b2bUnitModel = b2bUnit != null ? b2bUnit : getCurrentB2BUnit();

		AddressModel shippingAddress = null;
		if (null != b2bUnitModel && null != b2bUnitModel.getBillingAddress())
		{
			shippingAddress = b2bUnitModel.getBillingAddress();
		}
		else if (null != b2bUnitModel && CollectionUtils.isNotEmpty(b2bUnitModel.getBillingAddresses()))
		{
			shippingAddress = b2bUnitModel.getBillingAddresses().iterator().next();
		}
		else if (null != getContactAddress(b2bUnitModel))
		{
			shippingAddress = getContactAddress(b2bUnitModel);
		}
		return shippingAddress;
	}

	@Override
	public Collection<AddressModel> getAllDropShipAccounts()
	{
		return getCurrentB2BUnit().getDropShipAddresses();
	}

	@Override
	public Set<String> getOrderTypesForAccount()
	{
		final Set<String> availableOrderTypes = new HashSet<String>();
		final List noChargeOrderAccNum = JnJCommonUtil.getValues(Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_NO_CHARGE_ORDERS,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List deliveredOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_DELIVERED_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List replenishOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_REPLENISH_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List internationalOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_INTERNATIONAL_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);

		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();
		final JnJB2BUnitModel currentB2BUnit = getCurrentB2BUnit();

		final JnjGTDivisonData divisionData = jnjGTCustomerService.getPopulatedDivisionData(null);
		/* Fetching order types defined by Class of Trade OR associated division - Starts */
		if (CollectionUtils.isNotEmpty(internationalOrderAccNum)
				&& internationalOrderAccNum.contains(currentB2BUnit.getClassOfTrade()))
		{
			availableOrderTypes.add(JnjOrderTypesEnum.ZEX.getCode());
			if (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman())
			{
				availableOrderTypes.add(JnjOrderTypesEnum.ZDEL.getCode());
			}
		}
		if (CollectionUtils.isNotEmpty(noChargeOrderAccNum) && noChargeOrderAccNum.contains(currentB2BUnit.getClassOfTrade())
				&& BooleanUtils.isTrue(currentCustomer.getNoCharge()) && BooleanUtils.isTrue(currentB2BUnit.getNoCharge()))
		{
			availableOrderTypes.add(JnjOrderTypesEnum.ZNC.getCode());
		}
		if (CollectionUtils.isNotEmpty(deliveredOrderAccNum) && !deliveredOrderAccNum.contains(currentB2BUnit.getClassOfTrade())
				&& (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman()))
		{
			availableOrderTypes.add(JnjOrderTypesEnum.ZDEL.getCode());
		}

		if (CollectionUtils.isNotEmpty(replenishOrderAccNum) && replenishOrderAccNum.contains(currentB2BUnit.getClassOfTrade())
				&& (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman()))
		{
			availableOrderTypes.add(JnjOrderTypesEnum.ZKB.getCode());
		}
		/* Fetching order types defined by Class of Trade - End */

		/* Fetching order types defined by AccType - Starts */
		final String accType = currentB2BUnit.getIndicator();
		final JnjGTUserTypes userType = sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE);
		if (null != userType && null != accType)
		{
			final List<String> orderTypesByType = JnJCommonUtil.getValues(
					accType + Jnjb2bCoreConstants.SYMBOl_DOT + userType.getCode(), Jnjb2bCoreConstants.SYMBOl_COMMA);
			if (CollectionUtils.isNotEmpty(orderTypesByType))
			{
				availableOrderTypes.addAll(orderTypesByType);
			}
		}
		/* Fetching order types defined by AccType - End */
		//AAOL-2429 and AAOL-2433 changes
		if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.NO_CHARGE_INTERNAL_USER)== Boolean.TRUE) {
			availableOrderTypes.removeAll(availableOrderTypes);
			availableOrderTypes.add(JnjOrderTypesEnum.ZNC.getCode());
		}

		/*fetching order types for Consignment orer types*/
		if(null!=currentCustomer && (null==currentCustomer.getConsignmentEntryOrder()||true==currentCustomer.getConsignmentEntryOrder()))
		{
			// ADD by surabhi 
			availableOrderTypes.add(JnjOrderTypesEnum.ZOR.getCode());
			//end
			final List<String> orderTypesConsignment = JnJCommonUtil.getValues("VIEW_AND_PLACE_ORDER", Jnjb2bCoreConstants.SYMBOl_COMMA);
			if (CollectionUtils.isNotEmpty(orderTypesConsignment))
			{
				availableOrderTypes.addAll(orderTypesConsignment);
			}
		}
		/*fetching order types for Consignment orer types*/

		/* Sorting the available order types Soumitra AAOL-2701 */
		/* Converting set to list */
		final List<String> availableOrderTypesAsList = new ArrayList<String>(availableOrderTypes);
		final JnjGTOrderTypeComparator jnjGTOrderTypeComparator = new JnjGTOrderTypeComparator();
		// Sort the list
		Collections.sort(availableOrderTypesAsList, jnjGTOrderTypeComparator);
		/* Converting list to set */
		Set<String> sortedAvailableOrderTypes = new LinkedHashSet<String>(availableOrderTypesAsList);
		
		return sortedAvailableOrderTypes;
	}

	@Override
	public boolean isCustomerDistributor()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("isCustomerDistributor()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final String distCustName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_DISTRIBUTOR);
		boolean isDistributor = false;
		final String indicator = getCurrentB2BUnit().getIndicator();
		if (distCustName.equalsIgnoreCase(indicator))
		{
			isDistributor = true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("isCustomerDistributor()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return isDistributor;
	}

	@Override
	public boolean isCSCUser()
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("isCSCUser()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		final String cscCustName = JnJCommonUtil.getValue(Jnjb2bCoreConstants.B2BUnit.B2B_ACC_TYPE_CSC);
		boolean isCSCCustomer = false;
		final String indicator = getCurrentB2BUnit().getIndicator();
		if (cscCustName.equalsIgnoreCase(indicator))
		{
			isCSCCustomer = true;
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("isCSCUser()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return isCSCCustomer;
	}

	/**
	 * @param b2bUnitModel
	 * @return
	 */
	@Override
	public List<JnJB2BUnitModel> getB2bUnitWithSourceSystemId(final JnJB2BUnitModel b2bUnitModel)
	{
		final List<JnJB2BUnitModel> JnJB2BUnitModelList = flexibleSearchService.getModelsByExample(b2bUnitModel);
		return JnJB2BUnitModelList;
	}
	
	public JnJB2BUnitModel getB2BUnitByUid(String uid) {
		JnJB2BUnitModel jnjB2BUnitModel = jnjGTB2BUnitDao.getJnJB2BUnitByUid(uid);
		return jnjB2BUnitModel;
	}

	@Override
	public List<AddressModel> getBillingAddresses(JnJB2BUnitModel b2bUnit) {
		final JnJB2BUnitModel unitModel = b2bUnit != null ? b2bUnit : getCurrentB2BUnit();
		List<AddressModel> billingAddressList = null;
		if (null != unitModel)
		{
			billingAddressList = new ArrayList<AddressModel>();
			for (final AddressModel address : unitModel.getAddresses())
			{
				if (address.isActive() && address.getBillingAddress().booleanValue())
				{
					billingAddressList.add(address);
				}
			}
		}
		return billingAddressList;
	}

	
	@Override
	public Set<String> getAvailableOrderTypes(List<B2BUnitData> b2bUnitList)
	{
		List<JnJB2BUnitModel> b2bDetailList = new ArrayList<JnJB2BUnitModel>();
		final Set<String> availableOrderTypes = new HashSet<String>();
		
		final List<String> noChargeOrderAccNum = JnJCommonUtil.getValues(Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_NO_CHARGE_ORDERS,
				Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> deliveredOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_DELIVERED_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> replenishOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_REPLENISH_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);
		final List<String> internationalOrderAccNum = JnJCommonUtil.getValues(
				Jnjb2bCoreConstants.Cart.SALES_REP_ACC_NUM_FOR_INTERNATIONAL_ORDERS, Jnjb2bCoreConstants.SYMBOl_COMMA);

		final JnJB2bCustomerModel currentCustomer = (JnJB2bCustomerModel) userService.getCurrentUser();

		final JnjGTDivisonData divisionData = jnjGTCustomerService.getPopulatedDivisionData(null);

		if(b2bUnitList!=null && !(b2bUnitList.isEmpty())){
			b2bDetailList = getB2bUnitDetailList(b2bUnitList);
		}
		
		if(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE)!=null){
			
			if(JnjGTUserTypes.PORTAL_ADMIN.equals(sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE))){
				availableOrderTypes.add(JnjOrderTypesEnum.ZOR.getCode());
			}
		}
		
		
		if(b2bDetailList!=null && !(b2bDetailList.isEmpty())){
			for (JnJB2BUnitModel b2bUnit:b2bDetailList){
				
				
				/* Fetching order types defined by Class of Trade OR associated division - Starts */
				if (CollectionUtils.isNotEmpty(internationalOrderAccNum)
						&& internationalOrderAccNum.contains(b2bUnit.getClassOfTrade()))
				{
					availableOrderTypes.add(JnjOrderTypesEnum.ZEX.getCode());
					if (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman())
					{
						availableOrderTypes.add(JnjOrderTypesEnum.ZDEL.getCode());
					}
				}
				if (CollectionUtils.isNotEmpty(noChargeOrderAccNum) && noChargeOrderAccNum.contains(b2bUnit.getClassOfTrade())
						&& BooleanUtils.isTrue(currentCustomer.getNoCharge()) && BooleanUtils.isTrue(b2bUnit.getNoCharge()))
				{
					availableOrderTypes.add(JnjOrderTypesEnum.ZNC.getCode());
				}
				if (CollectionUtils.isNotEmpty(deliveredOrderAccNum) && !deliveredOrderAccNum.contains(b2bUnit.getClassOfTrade())
						&& (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman()))
				{
					availableOrderTypes.add(JnjOrderTypesEnum.ZDEL.getCode());
				}

				if (CollectionUtils.isNotEmpty(replenishOrderAccNum) && replenishOrderAccNum.contains(b2bUnit.getClassOfTrade())
						&& (divisionData.isIsMitek() || divisionData.isIsSpine() || divisionData.isIsCodman()))
				{
					availableOrderTypes.add(JnjOrderTypesEnum.ZKB.getCode());
				}
				
				final String accType = b2bUnit.getIndicator();
				final JnjGTUserTypes userType = sessionService.getAttribute(Jnjb2bCoreConstants.Login.USER_TYPE);
				if (null != userType && null != accType)
				{
					final List<String> orderTypesByType = JnJCommonUtil.getValues(
							accType + Jnjb2bCoreConstants.SYMBOl_DOT + userType.getCode(), Jnjb2bCoreConstants.SYMBOl_COMMA);
					if (CollectionUtils.isNotEmpty(orderTypesByType))
					{
						availableOrderTypes.addAll(orderTypesByType);
					}
				}
				/* Fetching order types defined by AccType - End */
				//AAOL-2429 and AAOL-2433 changes
				if (sessionService.getAttribute(Jnjb2bCoreConstants.Login.NO_CHARGE_INTERNAL_USER)== Boolean.TRUE) {
					availableOrderTypes.removeAll(availableOrderTypes);
					availableOrderTypes.add(JnjOrderTypesEnum.ZNC.getCode());
				}
			}
			
		}
		
		return availableOrderTypes;
	}
	
	
	/**
	 * This method returns List of JnJB2BUnitModel.
	 * @param b2bUnitList
	 * @return
	 */
	private List<JnJB2BUnitModel> getB2bUnitDetailList(List<B2BUnitData> b2bUnitList) {
		
		List<JnJB2BUnitModel> b2bDetailsList = new ArrayList<JnJB2BUnitModel>();
		
		for(B2BUnitData b2bUnitData:b2bUnitList){
			JnJB2BUnitModel b2bUnitdetail = jnjGTB2BUnitDao.getB2BUnitByUid(b2bUnitData.getUid());
			b2bDetailsList.add(b2bUnitdetail);
		}
		return b2bDetailsList;
	}
	//3088
	@Override
	public List<AddressModel> getSearchShippingAddress(final String searchitem)
	{
		final JnJB2BUnitModel unitModel = getCurrentB2BUnit();
		List<AddressModel> shippingAddressList = null;

		if (null != unitModel)
		{
			shippingAddressList = new ArrayList<AddressModel>();
			if (jnjGTAddressDao != null)
			{
				shippingAddressList = jnjGTAddressDao.getSearchShippingAddress(searchitem, unitModel.getUid());
			}

		}
		return shippingAddressList;


	}
	//3088
		@Override
		public List<AddressModel> getSearchBillingAddress(final String searchitem)
		{
			final JnJB2BUnitModel unitModel = getCurrentB2BUnit();
			List<AddressModel> billingAddressList = null;

			if (null != unitModel)
			{
				billingAddressList = new ArrayList<AddressModel>();
				if (jnjGTAddressDao != null)
				{
					billingAddressList = jnjGTAddressDao.getSearchBillingAddress(searchitem, unitModel.getUid());
				}

			}
			return billingAddressList;


		}
}
