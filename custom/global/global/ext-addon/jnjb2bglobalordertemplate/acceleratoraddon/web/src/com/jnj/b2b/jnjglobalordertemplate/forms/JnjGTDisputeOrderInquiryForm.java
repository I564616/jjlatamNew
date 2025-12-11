/**
 * 
 */
package com.jnj.b2b.jnjglobalordertemplate.forms;

import de.hybris.platform.commercefacades.user.data.AddressData;

import org.springframework.web.multipart.MultipartFile;


/**
 * Form class responsible to display/capture data attributes for/from Dispute Order inquiry.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeOrderInquiryForm
{
	/**
	 * Associated Order code.
	 */
	private String orderCode;

	/**
	 * Account number which relates with the dispute inquiry.
	 */
	private String accountNumber;

	/**
	 * Purchase Order number of the order for which dispute is raised.
	 */
	private String purchaseOrderNumber;

	/**
	 * Unordered products information which consists of concatenated:
	 * <ul>
	 * <li>Product code</li>,
	 * <li>Lot Numbers (comma separated), and</li>
	 * <li>Quantity</li>
	 * </ul>
	 */
	private String[] unorderedProductsInfo;

	/**
	 * Indicates if shipped products would be retained.
	 */
	boolean keepProductsShipped;

	/**
	 * New Purcahse Order Number entered.
	 */
	private String newPONumber;

	/**
	 * Array of Disputed fees reasons.
	 */
	private String[] disputedFees;

	/**
	 * Correct Purchase Order Number entered.
	 */
	private String correctPurchaseOrderNumber;

	/**
	 * Ship to address data.
	 */
	private AddressData shipToAddress;

	/**
	 * Correct to address data.
	 */
	private AddressData correctAddress;

	/**
	 * Tax exempt certificate file upload.
	 */
	private CommonsMultipartFile taxExemptCertificate;

	/**
	 * Indicates if incorrect address dispute is checked.
	 */
	private boolean inCorrectAddressDispute;

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber()
	{
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *           the accountNumber to set
	 */
	public void setAccountNumber(final String accountNumber)
	{
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the purchaseOrderNumber
	 */
	public String getPurchaseOrderNumber()
	{
		return purchaseOrderNumber;
	}

	/**
	 * @param purchaseOrderNumber
	 *           the purchaseOrderNumber to set
	 */
	public void setPurchaseOrderNumber(final String purchaseOrderNumber)
	{
		this.purchaseOrderNumber = purchaseOrderNumber;
	}

	/**
	 * @return the keepProductsShipped
	 */
	public boolean isKeepProductsShipped()
	{
		return keepProductsShipped;
	}

	/**
	 * @param keepProductsShipped
	 *           the keepProductsShipped to set
	 */
	public void setKeepProductsShipped(final boolean keepProductsShipped)
	{
		this.keepProductsShipped = keepProductsShipped;
	}

	/**
	 * @return the newPONumber
	 */
	public String getNewPONumber()
	{
		return newPONumber;
	}

	/**
	 * @param newPONumber
	 *           the newPONumber to set
	 */
	public void setNewPONumber(final String newPONumber)
	{
		this.newPONumber = newPONumber;
	}

	/**
	 * @return the disputedFees
	 */
	public String[] getDisputedFees()
	{
		return disputedFees;
	}

	/**
	 * @param disputedFees
	 *           the disputedFees to set
	 */
	public void setDisputedFees(final String[] disputedFees)
	{
		this.disputedFees = disputedFees;
	}

	/**
	 * @return the correctPurchaseOrderNumber
	 */
	public String getCorrectPurchaseOrderNumber()
	{
		return correctPurchaseOrderNumber;
	}

	/**
	 * @param correctPurchaseOrderNumber
	 *           the correctPurchaseOrderNumber to set
	 */
	public void setCorrectPurchaseOrderNumber(final String correctPurchaseOrderNumber)
	{
		this.correctPurchaseOrderNumber = correctPurchaseOrderNumber;
	}

	/**
	 * @return the shipToAddress
	 */
	public AddressData getShipToAddress()
	{
		return shipToAddress;
	}

	/**
	 * @param shipToAddress
	 *           the shipToAddress to set
	 */
	public void setShipToAddress(final AddressData shipToAddress)
	{
		this.shipToAddress = shipToAddress;
	}

	/**
	 * @return the correctAddress
	 */
	public AddressData getCorrectAddress()
	{
		return correctAddress;
	}

	/**
	 * @param correctAddress
	 *           the correctAddress to set
	 */
	public void setCorrectAddress(final AddressData correctAddress)
	{
		this.correctAddress = correctAddress;
	}

	/**
	 * @return the taxExemptCertificate
	 */
	public CommonsMultipartFile getTaxExemptCertificate()
	{
		return taxExemptCertificate;
	}

	/**
	 * @param taxExemptCertificate
	 *           the taxExemptCertificate to set
	 */
	public void setTaxExemptCertificate(final CommonsMultipartFile taxExemptCertificate)
	{
		this.taxExemptCertificate = taxExemptCertificate;
	}

	/**
	 * @return the unorderedProductsInfo
	 */
	public String[] getUnorderedProductsInfo()
	{
		return unorderedProductsInfo;
	}

	/**
	 * @param unorderedProductsInfo
	 *           the unorderedProductsInfo to set
	 */
	public void setUnorderedProductsInfo(final String[] unorderedProductsInfo)
	{
		this.unorderedProductsInfo = unorderedProductsInfo;
	}

	/**
	 * @return the inCorrectAddressDispute
	 */
	public boolean isInCorrectAddressDispute()
	{
		return inCorrectAddressDispute;
	}

	/**
	 * @param inCorrectAddressDispute
	 *           the inCorrectAddressDispute to set
	 */
	public void setInCorrectAddressDispute(final boolean inCorrectAddressDispute)
	{
		this.inCorrectAddressDispute = inCorrectAddressDispute;
	}

}
