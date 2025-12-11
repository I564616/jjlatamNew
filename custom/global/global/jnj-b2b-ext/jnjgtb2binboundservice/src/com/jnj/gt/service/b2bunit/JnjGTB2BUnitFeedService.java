/**
 *
 */
package com.jnj.gt.service.b2bunit;

import java.util.List;

import com.jnj.core.enums.RecordStatus;
import com.jnj.gt.model.JnjGTIntAffiliationModel;
import com.jnj.gt.model.JnjGTIntB2BUnitModel;
import com.jnj.gt.model.JnjGTIntPartnerFuncModel;
import com.jnj.gt.model.JnjGTIntSalesOrgModel;



/**
 * The JnjGTB2BUnitFeedService interface contains all those methods which are dealing with customer related intermediate
 * model and it has declaration of all the methods which are defined in the JnjGTB2BUnitFeedServiceImpl class.
 * 
 * @author sumit.y.kumar
 * 
 */
public interface JnjGTB2BUnitFeedService
{

	/**
	 * Gets the jnj na int b2 b unit model on the basis of record status and indicator.
	 * 
	 * @param recordStatus
	 *           the record status
	 * @return the jnj na int b2 b unit model
	 */
	public List<JnjGTIntB2BUnitModel> getJnjGTIntB2BUnitModel(final RecordStatus recordStatus, final String indicator);

	/**
	 * Gets the jnj na sales org cust model on the basis of customer number.
	 * 
	 * @param customerNumber
	 *           the customer number
	 * @return the jnj na sales org cust model
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
	 * Gets the jnj na int affiliation model on the basis of record status and customer number.
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


}
