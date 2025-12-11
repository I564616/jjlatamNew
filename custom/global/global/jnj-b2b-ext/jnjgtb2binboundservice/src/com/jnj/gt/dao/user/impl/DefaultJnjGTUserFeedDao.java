/**
 * 
 */
package com.jnj.gt.dao.user.impl;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.dao.user.JnjGTUserFeedDao;
import com.jnj.gt.model.JnjGTIntB2bCustomerModel;
import com.jnj.gt.model.JnjGTIntUserB2bUnitModel;
import com.jnj.gt.model.JnjGTIntUserPermissionModel;
import com.jnj.gt.model.JnjGTIntUserSalesOrgModel;
import com.jnj.gt.util.JnjGTInboundUtil;



/**
 * The Class JnjNaUserFeedDaoImpl implements the interface JnjGTUserFeedDao and give the business logic for different
 * methods.
 * 
 * @author sakshi.kashiva
 */
public class DefaultJnjGTUserFeedDao extends AbstractItemDao implements JnjGTUserFeedDao
{



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
		List<JnjGTIntUserB2bUnitModel> intUserB2BUnitModelList = new ArrayList<JnjGTIntUserB2bUnitModel>();
		try
		{
			FlexibleSearchQuery fQuery = null;
			final Map queryParams = new HashMap();
			if (Jnjgtb2binboundserviceConstants.Product.MDD_SRC_SYS.equalsIgnoreCase(JnjGTInboundUtil
					.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()))
					&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getId()))
			{
				queryParams.put("userId", jnjGTIntB2bCustomerModel.getId());
				fQuery = new FlexibleSearchQuery("select {pk} from  {JnjGTIntUserB2bUnit}  where {userId}=?userId");
			}
			else if (Jnjgtb2binboundserviceConstants.Product.CONSUMER_SRC_SYS.equalsIgnoreCase(JnjGTInboundUtil
					.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()))
					&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getEmail()))
			{
				queryParams.put("email", jnjGTIntB2bCustomerModel.getEmail());
				fQuery = new FlexibleSearchQuery("select {pk} from  {JnjGTIntUserB2bUnit}  where {email}=?email");
			}

			fQuery.addQueryParameters(queryParams);
			final SearchResult<JnjGTIntUserB2bUnitModel> result = getFlexibleSearchService().search(fQuery);
			intUserB2BUnitModelList = result.getResult();
		}
		catch (final ModelNotFoundException e)
		{
			intUserB2BUnitModelList = null;
		}
		return intUserB2BUnitModelList;
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
		List<JnjGTIntUserPermissionModel> jnjGTIntUserPermissionModelList = new ArrayList<JnjGTIntUserPermissionModel>();
		try
		{
			FlexibleSearchQuery fQuery = null;
			final Map queryParams = new HashMap();
			if (Jnjgtb2binboundserviceConstants.Product.MDD_SRC_SYS.equalsIgnoreCase(JnjGTInboundUtil
					.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()))
					&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getId()))
			{
				queryParams.put("userId", jnjGTIntB2bCustomerModel.getId());
				fQuery = new FlexibleSearchQuery("select {pk} from  {JnjGTIntUserPermission}  where {userId}=?userId");
			}
			else if (Jnjgtb2binboundserviceConstants.Product.CONSUMER_SRC_SYS.equalsIgnoreCase(JnjGTInboundUtil
					.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()))
					&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getEmail()))
			{
				queryParams.put("email", jnjGTIntB2bCustomerModel.getEmail());
				fQuery = new FlexibleSearchQuery("select {pk} from  {JnjGTIntUserPermission}  where {email}=?email");
			}
			fQuery.addQueryParameters(queryParams);
			final SearchResult<JnjGTIntUserPermissionModel> result = getFlexibleSearchService().search(fQuery);
			jnjGTIntUserPermissionModelList = result.getResult();

		}
		catch (final ModelNotFoundException e)
		{
			jnjGTIntUserPermissionModelList = null;
		}
		return jnjGTIntUserPermissionModelList;
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
		List<JnjGTIntUserSalesOrgModel> jnjGTIntUserSalesOrgModelList = null;
		FlexibleSearchQuery fQuery = null;
		try
		{
			if (Jnjgtb2binboundserviceConstants.Product.MDD_SRC_SYS.equalsIgnoreCase(JnjGTInboundUtil
					.fetchValidSourceSysId(jnjGTIntB2bCustomerModel.getSource()))
					&& StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getId()))
			{
				final Map queryParams = new HashMap();
				if (StringUtils.isNotEmpty(jnjGTIntB2bCustomerModel.getId()))
				{
					queryParams.put("userId", jnjGTIntB2bCustomerModel.getId());
					fQuery = new FlexibleSearchQuery("select {pk} from  {JnjGTIntUserSalesOrg}  where {userId}=?userId");
				}
				fQuery.addQueryParameters(queryParams);
				final SearchResult<JnjGTIntUserSalesOrgModel> result = getFlexibleSearchService().search(fQuery);
				jnjGTIntUserSalesOrgModelList = result.getResult();
			}
		}
		catch (final ModelNotFoundException e)
		{
			jnjGTIntUserSalesOrgModelList = null;
		}
		return jnjGTIntUserSalesOrgModelList;
	}
}
