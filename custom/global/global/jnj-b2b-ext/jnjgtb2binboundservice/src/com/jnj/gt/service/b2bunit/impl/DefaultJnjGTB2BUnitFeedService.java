/**
 *
 */
package com.jnj.gt.service.b2bunit.impl;

import  com.jnj.gt.dao.b2bunit.JnjGTB2BUnitFeedDao;
import  com.jnj.gt.service.b2bunit.JnjGTB2BUnitFeedService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.model.JnjGTIntPartnerFuncModel;
import com.jnj.gt.model.JnjGTIntSalesOrgModel;


/**
 * The JnjGTB2BUnitFeedServiceImpl class contains all those methods which are dealing with customer related intermediate
 * model and it has definition of all the methods which are defined in the JnjGTB2BUnitFeedService interface.
 * 
 * @author sumit.y.kumar
 * 
 */
public class DefaultJnjGTB2BUnitFeedService implements JnjGTB2BUnitFeedService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTB2BUnitFeedService.class);

	@Autowired
	private JnjGTB2BUnitFeedDao jnjGTB2BUnitFeedDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnjGTIntB2BUnitModel> getJnjGTIntB2BUnitModel(final RecordStatus recordStatus, final String indicator)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntB2BUnitModel(recordStatus, indicator);
		//return jnjGTB2BUnitFeedDao.getJnjGTIntB2BUnitModel(RecordStatus.ERROR, indicator);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnjGTIntSalesOrgModel> getJnjGTIntSalesOrgModel(final String customerNumber)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntSalesOrgModel(customerNumber);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModel(final String customerNumber)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntPartnerFuncModel(customerNumber);
	}

	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelByPartnerCustomerNumber(final String partnerCustomerNumber)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntPartnerFuncModelByPartnerCustomerNumber(partnerCustomerNumber);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntAffiliationModel> getJnjGTIntAffiliationModel(final RecordStatus recordStatus, final String customerNumber)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntAffiliationModel(recordStatus, customerNumber);
		//return jnjGTB2BUnitFeedDao.getJnjGTIntAffiliationModel(RecordStatus.ERROR, customerNumber);
	}

	@Override
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelForBillTo(final RecordStatus recordStatus,
			final String indicator)
	{
		return jnjGTB2BUnitFeedDao.getJnjGTIntPartnerFuncModelForBillTo(recordStatus, indicator);
	}
}
