/**
 *
 */
package com.jnj.gt.dao.b2bunit;

import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.model.JnjGTIntPartnerFuncModel;
import com.jnj.gt.model.JnjGTIntSalesOrgModel;


/**
 * The JnjGTB2BUnitFeedDao interface contains all those methods which are dealing with customer related intermediate
 * model and it has declaration of all the methods which are defined in the JnjGTB2BUnitFeedDaoImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTB2BUnitFeedDao
{

	/**
	 * Gets the JnjGTIntB2BUnitModel on the basis of record status and indicator.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @param indicator
	 *           the indicator
	 * @return the jnj na int b2 b unit model
	 */
	public List<JnjGTIntB2BUnitModel> getJnjGTIntB2BUnitModel(final RecordStatus recordStatus, final String indicator);

	/**
	 * Gets the JnjGTIntSalesOrgModel on the basis of Customer Number.
	 * 
	 * @param customerNumber
	 *           the customer number
	 * @return the JnjGTIntSalesOrgModel
	 */
	public List<JnjGTIntSalesOrgModel> getJnjGTIntSalesOrgModel(final String customerNumber);

	/**
	 * Gets the jnj na int partner func model on the basis of customer number.
	 * 
	 * @param partnerFunction
	 *           the partner function
	 * @return the jnj na int partner func model
	 */
	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModel(final String customerNumber);

	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelByPartnerCustomerNumber(final String partnerCustomerNumber);

	/**
	 * Gets the jnj na int affiliation model on the basis of recordStatus and customer number.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @param customerNumber
	 *           the customer number
	 * @return the jnj na int affiliationt model
	 */
	public List<JnjGTIntAffiliationModel> getJnjGTIntAffiliationModel(final RecordStatus recordStatus, final String customerNumber);

	public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelForBillTo(final RecordStatus recordStatus,
			final String indicator);

	/*
	 * public List<JnjGTIntPartnerFuncModel> getJnjGTIntPartnerFuncModelForShipTo(final RecordStatus recordStatus, final
	 * String indicator);
	 */
}
