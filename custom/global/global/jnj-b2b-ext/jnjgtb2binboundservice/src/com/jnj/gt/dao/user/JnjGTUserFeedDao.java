/**
 * 
 */
package com.jnj.gt.dao.user;

import java.util.List;

import com.jnj.gt.model.JnjGTIntB2bCustomerModel;
import com.jnj.gt.model.JnjGTIntUserB2bUnitModel;
import com.jnj.gt.model.JnjGTIntUserPermissionModel;
import com.jnj.gt.model.JnjGTIntUserSalesOrgModel;


/**
 * The Interface JnjGTUserFeedDao.
 * 
 * @author sakshi.kashiva
 */
public interface JnjGTUserFeedDao
{

	/**
	 * Gets the intb2bunit for email.
	 * 
	 * @param jnjGTIntB2bCustomerModel
	 *           the email
	 * @return the intb2bunit for email
	 */
	public List<JnjGTIntUserB2bUnitModel> getIntB2BUnitForEmail(final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel);

	/**
	 * Gets the intuserpermissionid for customer.
	 * 
	 * @param jnjGTIntB2bCustomerModel
	 *           the email
	 * @return the intuserpermissionid for customer
	 */
	public List<JnjGTIntUserPermissionModel> getIntUserPermissionIdForCustomer(JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel);

	/**
	 * Gets the intusersalesorg for customer.
	 * 
	 * @param email
	 *           the email
	 * @return the intusersalesorg for customer
	 */
	public List<JnjGTIntUserSalesOrgModel> getIntUserSalesOrgForCustomer(JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel);


}
