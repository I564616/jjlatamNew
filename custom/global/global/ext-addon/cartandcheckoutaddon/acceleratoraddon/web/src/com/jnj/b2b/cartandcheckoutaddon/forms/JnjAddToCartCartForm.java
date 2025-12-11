/**
 * 
 */
package com.jnj.b2b.cartandcheckoutaddon.forms;

import java.util.List;

/**
 * @author raghav.kumar
 * 
 */
public class JnjAddToCartCartForm
{
//AAOL-2405 added 
	private String productId;
	private String qty;

	private String hybrisLineItemNo;
	private List<String> proposedItemNo;//AAOL-6378
	

	private String originalItemNo;
	private String zzFlag;
	private String zzsubstMat;
	private String zzsubstQty;
 
	//AAOL-2405 end
	
	/**
	 * @return the productId
	 */
	public String getProductId()
	{
		return productId;
	}

	/**
	 * @param productId
	 *           the productId to set
	 */
	public void setProductId(final String productId)
	{
		this.productId = productId;
	}

	/**
	 * @return the qty
	 */
	public String getQty()
	{
		return qty;
	}

	/**
	 * @param qty
	 *           the qty to set
	 */
	public void setQty(final String qty)
	{
		this.qty = qty;
	}

	/**
	 * @return the proposedItemNo
	 */
	public List<String> getProposedItemNo() {
		return proposedItemNo;
	}

	public void setProposedItemNo(List<String> proposedItemNo) {
		this.proposedItemNo = proposedItemNo;
	}

	/**
	 * @return the originalItemNo
	 */
	public String getOriginalItemNo() {
		return originalItemNo;
	}

	/**
	 * @param originalItemNo the originalItemNo to set
	 */
	public void setOriginalItemNo(String originalItemNo) {
		this.originalItemNo = originalItemNo;
	}

	public String getHybrisLineItemNo() {
		return hybrisLineItemNo;
	}

	public void setHybrisLineItemNo(String hybrisLineItemNo) {
		this.hybrisLineItemNo = hybrisLineItemNo;
	}

	public String getZzFlag() {
		return zzFlag;
	}

	public void setZzFlag(String zzFlag) {
		this.zzFlag = zzFlag;
	}

	public String getZzsubstMat() {
		return zzsubstMat;
	}

	public void setZzsubstMat(String zzsubstMat) {
		this.zzsubstMat = zzsubstMat;
	}

	public String getZzsubstQty() {
		return zzsubstQty;
	}

	public void setZzsubstQty(String zzsubstQty) {
		this.zzsubstQty = zzsubstQty;
	}
 


}
