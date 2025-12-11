/**
 *
 */
package com.jnj.core.services.b2bunit;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.model.JnjGTSalesOrgCustomerModel;


/**
 * The JnjGTB2BUnitService interface contains the declaration of all the methods of the JnjGTB2BUnitServiceImpl class.
 * 
 * @author sumit.y.kumar
 */
public interface JnjGTB2BUnitService extends B2BUnitService<B2BUnitModel, B2BCustomerModel>
{

	/**
	 * Save item model.
	 * 
	 * @param itemModel
	 *           the item model
	 * @return true, if successful
	 */
	public boolean saveItemModel(final ItemModel itemModel);

	/**
	 * Gets the sales org customer model.
	 * 
	 * @param salesOrg
	 *           the sales org
	 * @param distribChannel
	 *           the distrib channel
	 * @param division
	 *           the division
	 * @param sourceSysId
	 *           the source sys id
	 * @return the sales org customer model
	 */
	public JnjGTSalesOrgCustomerModel getSalesOrgCustomerModel(final String salesOrg, final String distribChannel,
			final String division, String sourceSysId);

	/**
	 * Gets the b2 b unit model for uid by using customer number and source sys id.
	 * 
	 * @param customerNumber
	 *           the customer number
	 * @param sourceSysId
	 *           the source sys id
	 * @return the b2 b unit model for uid
	 */
	public JnJB2BUnitModel getB2BUnitModelForUid(final String customerNumber, final String sourceSysId);

	/**
	 * This method fetches the current B2B Unit for the logged in user.
	 * 
	 * @return JnJB2BUnitModel
	 * @author sanchit.a.kumar
	 */
	public JnJB2BUnitModel getCurrentB2BUnit();

	/**
	 * Checks if the current selected Session customer is an International Affiliate or not, based on the Indicator set
	 * in it.
	 * 
	 * @return boolean
	 */
	public boolean isCustomerInternationalAff();

	/**
	 * This is used to get UPG Code for given division.
	 * 
	 * @param b2bUnit
	 *           the b2b unit
	 * @param division
	 *           the division
	 * @return String
	 */
	public String getUPGCode(final JnJB2BUnitModel b2bUnit, String division);

	/**
	 * Gets the contact address for current User's B2BUnuit.
	 * @param b2bUnit YTODO
	 * 
	 * @return the contact address for current B2BUnit
	 */
	public AddressModel getContactAddress(JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the shipping addresses for current B2bUnit.
	 * @param b2bUnit YTODO
	 * 
	 * @return List AddressModel where shipping flag is true
	 */
	public List<AddressModel> getShippingAddresses(JnJB2BUnitModel b2bUnit);
	public List<AddressModel> getBillingAddresses(JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the shipping address.
	 * @param b2bUnit YTODO
	 * 
	 * @return the shipping address
	 */
	public AddressModel getShippingAddress(JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the billing address.
	 * @param b2bUnit YTODO
	 * 
	 * @return the billing address
	 */
	public AddressModel getBillingAddress(JnJB2BUnitModel b2bUnit);

	/**
	 * Gets the all drop ship accounts.
	 * 
	 * @return the all drop ship accounts
	 */
	public Collection<AddressModel> getAllDropShipAccounts();

	/**
	 * Gets the available order type for the current Unit and logged in user.
	 * 
	 * @return orderTypes List
	 */
	public Set<String> getOrderTypesForAccount();

	/**
	 * Checks if is cSC user.
	 * 
	 * @return true, if is cSC user
	 */
	public boolean isCSCUser();

	/**
	 * Checks if is customer distributor.
	 * 
	 * @return true, if is customer distributor
	 */
	public boolean isCustomerDistributor();

	/**
	 * @param b2bUnitModel
	 * @return
	 */
	public List<JnJB2BUnitModel> getB2bUnitWithSourceSystemId(JnJB2BUnitModel b2bUnitModel);
	
	/**
	 * This method is used to get available order types for the user
	 * @param b2bUnitList
	 * @return
	 */
	public Set<String> getAvailableOrderTypes(List<B2BUnitData> b2bUnitList);
	//3088
	public List<AddressModel> getSearchShippingAddress(String searchItem);
	
	public List<AddressModel> getSearchBillingAddress(String searchItem);
}
