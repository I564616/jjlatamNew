/**
 * 
 */
package com.jnj.core.services.company;

import de.hybris.platform.b2b.model.B2BCustomerModel;
//import de.hybris.platform.b2bacceleratorservices.company.B2BCommerceUserService;
import de.hybris.platform.b2b.company.B2BCommerceUserService;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;

import java.util.List;

import com.jnj.core.dto.JnjGTPageableData;
import com.jnj.core.model.JnJB2bCustomerModel;


/**
 * @author komal.sehgal
 * 
 */
public interface JnjGTB2BCommerceUserService extends B2BCommerceUserService
{

	/**
	 * @param pageableData
	 * @return
	 */
	public SearchPageData<B2BCustomerModel> searchCustomers(JnjGTPageableData pageableData);

	/**
	 * Retrieves division (s) of the user based on the <code>AccessBy</code> value. If AccessBy is:
	 * 
	 * <ul>
	 * <li>WWID - <code>division</code> from user is returned</li>
	 * <li>TERRITORIES - territory divisions are returned from user.</li>
	 * </ul>
	 * 
	 * @param customer
	 * @return List<String>
	 */
	public List<String> getUserDivisions(final JnJB2bCustomerModel customer);

	public String generateTokenForApprovedEmail(JnJB2bCustomerModel customer);
	/**
	 * @param customer
	 * @return String
	 */
	public String generateTemporaryPwdForEmail(JnJB2bCustomerModel customer);
	
	/**
	 * This method returns the userSector
	 * @return String
	 */
	public String getCurrentUserSector();

}
