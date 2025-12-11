/**
 * 
 */
package com.jnj.b2b.jnjglobalordertemplate.forms;

import java.util.List;
import java.util.Map;

/**
 * Form class responsible to display/capture data attributes for/from Dispute Item inquiry.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTDisputeItemInquiryForm
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
	 * Total dispute amount captured.
	 */
	private String totalDisputedAmount;

	/**
	 * Expected price value for the disputed item.
	 */
	private String expectedPrice;

	/**
	 * Contract Number with associated order.
	 */
	private String contractNumber;

	/**
	 * Product code if order is being short shipped.
	 */
	private String shortShippedProductCode;

	/**
	 * Ordered quantity for the short shipped product.
	 */
	private String shortShippedOrderedQuantity;

	/**
	 * Received quantity for the short shipped product.
	 */
	private String shortShippedReceivedQuantity;

	/**
	 * Product code if order is being over shipped.
	 */
	private String overShippedProductCode;

	/**
	 * Ordered quantity for the over shipped product.
	 */
	private String overShippedOrderedQuantity;

	/**
	 * Received quantity for the over shipped product.
	 */
	private String overShippedReceivedQuantity;

	/**
	 * Indicates if product replacement required.
	 */
	private boolean replacementRequired;

	/**
	 * Lot Numbers.
	 */
	private String lotNumbers;

	/**
	 * Indicates if shipped products to be retained.
	 * 
	 */
	private boolean keepProductsShipped;

	/**
	 * New purchase order number for the associated order.
	 */
	private String newPurchaseOrderNumber;
	
	
	private String disputeInvoiceNumber;
	
	private String pricingProductCode;
	
	public String getPricingProductCode() {
		return pricingProductCode;
	}

	public void setPricingProductCode(String pricingProductCode) {
		this.pricingProductCode = pricingProductCode;
	}

	public String[] getUnorderedPricingProductsInfo() {
		return unorderedPricingProductsInfo;
	}

	public void setUnorderedPricingProductsInfo(String[] unorderedPricingProductsInfo) {
		this.unorderedPricingProductsInfo = unorderedPricingProductsInfo;
	}

	public String[] getUnorderedShortShippedProductsInfo() {
		return unorderedShortShippedProductsInfo;
	}

	public void setUnorderedShortShippedProductsInfo(String[] unorderedShortShippedProductsInfo) {
		this.unorderedShortShippedProductsInfo = unorderedShortShippedProductsInfo;
	}

	public String[] getUnorderedOverShippedProductsInfo() {
		return unorderedOverShippedProductsInfo;
	}

	public void setUnorderedOverShippedProductsInfo(String[] unorderedOverShippedProductsInfo) {
		this.unorderedOverShippedProductsInfo = unorderedOverShippedProductsInfo;
	}

	public Map<String, List<String>> getProductsAndQuantityPrice() {
		return productsAndQuantityPrice;
	}

	public void setProductsAndQuantityPrice(Map<String, List<String>> productsAndQuantityPrice) {
		this.productsAndQuantityPrice = productsAndQuantityPrice;
	}

	private String[] unorderedPricingProductsInfo;
	
	private String[] unorderedShortShippedProductsInfo;
	
	private String[] unorderedOverShippedProductsInfo;
	
	private Map<String, List<String>> productsAndQuantityPrice;
	
	
	
	

	public String getDisputeInvoiceNumber() {
		return disputeInvoiceNumber;
	}

	public void setDisputeInvoiceNumber(String disputeInvoiceNumber) {
		this.disputeInvoiceNumber = disputeInvoiceNumber;
	}

	public String[] getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String[] invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public String[] getQuantity() {
		return quantity;
	}

	public void setQuantity(String[] quantity) {
		this.quantity = quantity;
	}

	public String getDisputedPrice() {
		return disputedPrice;
	}

	public void setDisputedPrice(String disputedPrice) {
		this.disputedPrice = disputedPrice;
	}

	/**
	 * @return the disputeInvoiceNumber
	 */
	private String[] invoiceNumber;
	
	private String[] quantity;
	
	private String disputedPrice;
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
	 * @return the totalDisputedAmount
	 */
	public String getTotalDisputedAmount()
	{
		return totalDisputedAmount;
	}

	/**
	 * @param totalDisputedAmount
	 *           the totalDisputedAmount to set
	 */
	public void setTotalDisputedAmount(final String totalDisputedAmount)
	{
		this.totalDisputedAmount = totalDisputedAmount;
	}

	/**
	 * @return the expectedPrice
	 */
	public String getExpectedPrice()
	{
		return expectedPrice;
	}

	/**
	 * @param expectedPrice
	 *           the expectedPrice to set
	 */
	public void setExpectedPrice(final String expectedPrice)
	{
		this.expectedPrice = expectedPrice;
	}

	/**
	 * @return the contractNumber
	 */
	public String getContractNumber()
	{
		return contractNumber;
	}

	/**
	 * @param contractNumber
	 *           the contractNumber to set
	 */
	public void setContractNumber(final String contractNumber)
	{
		this.contractNumber = contractNumber;
	}

	/**
	 * @return the shortShippedProductCode
	 */
	public String getShortShippedProductCode()
	{
		return shortShippedProductCode;
	}

	/**
	 * @param shortShippedProductCode
	 *           the shortShippedProductCode to set
	 */
	public void setShortShippedProductCode(final String shortShippedProductCode)
	{
		this.shortShippedProductCode = shortShippedProductCode;
	}

	/**
	 * @return the shortShippedOrderedQuantity
	 */
	public String getShortShippedOrderedQuantity()
	{
		return shortShippedOrderedQuantity;
	}

	/**
	 * @param shortShippedOrderedQuantity
	 *           the shortShippedOrderedQuantity to set
	 */
	public void setShortShippedOrderedQuantity(final String shortShippedOrderedQuantity)
	{
		this.shortShippedOrderedQuantity = shortShippedOrderedQuantity;
	}

	/**
	 * @return the shortShippedReceivedQuantity
	 */
	public String getShortShippedReceivedQuantity()
	{
		return shortShippedReceivedQuantity;
	}

	/**
	 * @param shortShippedReceivedQuantity
	 *           the shortShippedReceivedQuantity to set
	 */
	public void setShortShippedReceivedQuantity(final String shortShippedReceivedQuantity)
	{
		this.shortShippedReceivedQuantity = shortShippedReceivedQuantity;
	}

	/**
	 * @return the overShippedProductCode
	 */
	public String getOverShippedProductCode()
	{
		return overShippedProductCode;
	}

	/**
	 * @param overShippedProductCode
	 *           the overShippedProductCode to set
	 */
	public void setOverShippedProductCode(final String overShippedProductCode)
	{
		this.overShippedProductCode = overShippedProductCode;
	}

	/**
	 * @return the overShippedOrderedQuantity
	 */
	public String getOverShippedOrderedQuantity()
	{
		return overShippedOrderedQuantity;
	}

	/**
	 * @param overShippedOrderedQuantity
	 *           the overShippedOrderedQuantity to set
	 */
	public void setOverShippedOrderedQuantity(final String overShippedOrderedQuantity)
	{
		this.overShippedOrderedQuantity = overShippedOrderedQuantity;
	}

	/**
	 * @return the overShippedReceivedQuantity
	 */
	public String getOverShippedReceivedQuantity()
	{
		return overShippedReceivedQuantity;
	}

	/**
	 * @param overShippedReceivedQuantity
	 *           the overShippedReceivedQuantity to set
	 */
	public void setOverShippedReceivedQuantity(final String overShippedReceivedQuantity)
	{
		this.overShippedReceivedQuantity = overShippedReceivedQuantity;
	}

	/**
	 * @return the replacementRequired
	 */
	public boolean isReplacementRequired()
	{
		return replacementRequired;
	}

	/**
	 * @param replacementRequired
	 *           the replacementRequired to set
	 */
	public void setReplacementRequired(final boolean replacementRequired)
	{
		this.replacementRequired = replacementRequired;
	}

	/**
	 * @return the lotNumbers
	 */
	public String getLotNumbers()
	{
		return lotNumbers;
	}

	/**
	 * @param lotNumbers
	 *           the lotNumbers to set
	 */
	public void setLotNumbers(final String lotNumbers)
	{
		this.lotNumbers = lotNumbers;
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
	 * @return the newPurchaseOrderNumber
	 */
	public String getNewPurchaseOrderNumber()
	{
		return newPurchaseOrderNumber;
	}

	/**
	 * @param newPurchaseOrderNumber
	 *           the newPurchaseOrderNumber to set
	 */
	public void setNewPurchaseOrderNumber(final String newPurchaseOrderNumber)
	{
		this.newPurchaseOrderNumber = newPurchaseOrderNumber;
	}

}
