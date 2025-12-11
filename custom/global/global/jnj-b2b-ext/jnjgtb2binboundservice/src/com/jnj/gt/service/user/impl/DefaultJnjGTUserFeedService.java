/**
 * 
 */
package com.jnj.gt.service.user.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.gt.dao.user.JnjGTUserFeedDao;
import com.jnj.gt.model.JnjGTIntB2bCustomerModel;
import com.jnj.gt.model.JnjGTIntUserB2bUnitModel;
import com.jnj.gt.model.JnjGTIntUserPermissionModel;
import com.jnj.gt.model.JnjGTIntUserSalesOrgModel;
import com.jnj.gt.service.user.JnjGTUserFeedService;


/**
 * The Class JnjGTUserFeedServiceImpl.
 * 
 * @author sakshi.kashiva
 */
public class DefaultJnjGTUserFeedService implements JnjGTUserFeedService
{


	/** The jnj na user feed dao. */
	@Autowired
	JnjGTUserFeedDao jnjGTUserFeedDao;

	/**
	 * Gets the intb2bunit for email.
	 * 
	 * @param email
	 *           the email
	 * @return the intb2bunit for email
	 */
	@Override
	public List<JnjGTIntUserB2bUnitModel> getIntB2BUnitForEmail(final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel)
	{
		return jnjGTUserFeedDao.getIntB2BUnitForEmail(jnjGTIntB2bCustomerModel);
	}

	/**
	 * Gets the intuserpermissionid for customer.
	 * 
	 * @param email
	 *           the email
	 * @return the intuserpermissionid for customer
	 */
	@Override
	public List<JnjGTIntUserPermissionModel> getIntUserPermissionIdForCustomer(
			final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel)
	{
		return jnjGTUserFeedDao.getIntUserPermissionIdForCustomer(jnjGTIntB2bCustomerModel);
	}

	/**
	 * Gets the intusersalesorg for customer.
	 * 
	 * @param email
	 *           the email
	 * @return the intusersalesorg for customer
	 */
	@Override
	public List<JnjGTIntUserSalesOrgModel> getIntUserSalesOrgForCustomer(final JnjGTIntB2bCustomerModel jnjGTIntB2bCustomerModel)
	{
		return jnjGTUserFeedDao.getIntUserSalesOrgForCustomer(jnjGTIntB2bCustomerModel);
	}




}
