package com.jnj.facades.order;

import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import java.util.Locale;
import java.io.File;
import java.util.List;
import java.util.Map;
import com.jnj.exceptions.BusinessException;
import com.jnj.exceptions.IntegrationException;
import com.jnj.exceptions.SystemException;
import com.jnj.facades.data.JnjGTSurgeonData;
import com.jnj.facades.order.JnjOrderFacade;
import com.jnj.core.dto.JnjGTDisputeInquiryDto;
import com.jnj.core.dto.JnjGTOrderHistoryForm;


public interface JnjGTOrderFacade extends JnjOrderFacade
{
	public boolean isPONumUsed(String poNumber);

	/**
	 * Retrieves Paged Order History Data using <code>JnjGTOrderService</code> based on the criteria selected
	 * 
	 * @param pageableData
	 * @param form
	 * @return SearchPageData<OrderModel>
	 */
	public SearchPageData<OrderHistoryData> getPagedOrderHistoryForStatuses(final PageableData pageableData,
			final JnjGTOrderHistoryForm form);

	/**
	 * Submits Order Change Request on PO Number change.
	 * 
	 * @param order
	 * @return boolean
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	public boolean submitPOOrderChangeRequest(String orderCode, String updatedPoNumber) throws SystemException,
			IntegrationException;

	/**
	 * Submits Order Change Request on Surgeon change.
	 * 
	 * @param order
	 * @return boolean
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	public String submitSurgeonOrderChangeRequest(String orderCode, String selectedSurgeonId) throws SystemException,
			IntegrationException;

	/**
	 * Retrieves and converts Surgeon data to be populated in Surgeon Update.
	 * 
	 * @return Collection<String>
	 */
	public List<JnjGTSurgeonData> getSurgeonData();

	/**
	 * Fetches accounts data associated with the current session user.
	 * 
	 * @return String
	 */
	public Map<String, String> getAccountsMap();

	/**
	 * Returns current B2B uid unit of the session user.
	 * 
	 * @return String
	 */
	public String getCurrentB2bUnitId();

	/**
	 * Indicates if the current user in session a Sales Representative user.
	 * 
	 * @return boolean
	 */
	public boolean isSalesRepUserInSession();

	/**
	 * Publishes the JnjGTDisputeInquirEvent to trigger the mail for submission of Dispute Order Inquiry form.
	 */
	public boolean sendDisputeorderInquiry(JnjGTDisputeInquiryDto dto, final String orderCode);

	/**
	 * Retrieves Surgeon information associated with the respective order, and if found converts it into data bean.
	 * 
	 * @param orderCode
	 * @return JnjGTSurgeryInfoData
	 */
	public JnjGTSurgeonData getSurgeonInformation(final String orderCode);

	/**
	 * Makes an out-bound call to download and display Proof Delivery Document to the user.
	 * 
	 * @param trackingId
	 * @param shipDate
	 */
	public File getDeliveryProof(final String trackingId, final String shipDate);

	/**
	 * Updates batch content Indicator.
	 * 
	 * @param orderCode
	 * @return true, if successful
	 */
	public boolean updateBatchContentInd(String orderCode);

	/**
	 * The isCmodRgaCall method is used to get the bytes array which is coming from the CMOD interface.
	 * 
	 * @param orderNumber
	 * @return byte array in map
	 */
	public Map<String, byte[]> isCmodRgaCall(final String orderNumber, final boolean isCmod) throws BusinessException,
			IntegrationException;

	/**
	 * Gets the surgeon data.
	 * 
	 * @param searchPattern
	 *           the search pattern
	 * @param loadMoreCounter
	 * @return the surgeon data
	 */
	public SearchPageData<JnjGTSurgeonData> getSurgeonData(final String searchPattern, int loadMoreCounter);
	
	
	/**
	 * @param orderCode
	 * @param ignoreRestriction
	 * @return
	 */
	public OrderData getOrderDetailsForCode(String orderCode, boolean ignoreRestriction);

	// GTR -1688 - Invoice PDF/EXCEL Logo
	public String getImagePathForOrderandInvoiceDownloads(String mediaUid);
	
	/**
	 * Submits Order Change Request on Surgeon change.
	 * 
	 * @param order
	 * @return boolean
	 * @throws IntegrationException
	 * @throws SystemException
	 */
	public String submitSurgeonOrderChangeRequest(String orderCode, String selectedSurgeonId, String name, String hospitalID)
			throws SystemException, IntegrationException;
	
	public OrderData getPriceInquiryOrderDetailsForCode(String hybrisOrderNo, String jnjOrderNo);
	public String getJnjOrderCreditLimitMsg(String code , Locale loc);

}
