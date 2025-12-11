/**
 * 
 */
package com.jnj.core.dto;

/**
 * @author nsinha7
 *
 */
public class JnjGTSplitOrderInfo {
	private String shipTo="";
	private String soldTo="";
	private String principal = "";
	private String docorderType = "";
	private String salesOrganizantion = "";
	private String forbiddenFlag = "";
	private String sector = "";
	private String noProductError = "";
	/**
	 * @return the shipTo
	 */
	public String getShipTo() {
		return shipTo;
	}
	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}
	/**
	 * @return the soldTo
	 */
	public String getSoldTo() {
		return soldTo;
	}
	/**
	 * @param soldTo the soldTo to set
	 */
	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}
	/**
	 * @return the principal
	 */
	public String getPrincipal() {
		return principal;
	}
	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	/**
	 * @return the docorderType
	 */
	public String getDocorderType() {
		return docorderType;
	}
	/**
	 * @param docorderType the docorderType to set
	 */
	public void setDocorderType(String docorderType) {
		this.docorderType = docorderType;
	}
	/**
	 * @return the salesOrganizantion
	 */
	public String getSalesOrganizantion() {
		return salesOrganizantion;
	}
	/**
	 * @param salesOrganizantion the salesOrganizantion to set
	 */
	public void setSalesOrganizantion(String salesOrganizantion) {
		this.salesOrganizantion = salesOrganizantion;
	}
	/**
	 * @return the forbiddenFlag
	 */
	public String getForbiddenFlag() {
		return forbiddenFlag;
	}
	/**
	 * @param forbiddenFlag the forbiddenFlag to set
	 */
	public void setForbiddenFlag(String forbiddenFlag) {
		this.forbiddenFlag = forbiddenFlag;
	}
	/**
	 * @return the sector
	 */
	public String getSector() {
		return sector;
	}
	/**
	 * @param sector the sector to set
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}
	/**
	 * @return the noProductError
	 */
	public String getNoProductError() {
		return noProductError;
	}
	/**
	 * @param noProductError the noProductError to set
	 */
	public void setNoProductError(String noProductError) {
		this.noProductError = noProductError;
	}
	
	@Override
	public boolean equals(final Object object)
	{
		boolean result = false;
		if (object == null || object.getClass() != getClass())
		{
			result = false;
		}
		else
		{
			final JnjGTSplitOrderInfo splitOrderInfo = (JnjGTSplitOrderInfo) object;
			if (this.principal.equals(splitOrderInfo.getPrincipal())
					&& this.docorderType.equals(splitOrderInfo.getDocorderType())
					&& this.forbiddenFlag.equals(splitOrderInfo.getForbiddenFlag()) 
					&& this.sector.equals(splitOrderInfo.getSector())
					&& this.noProductError.equals(splitOrderInfo.getNoProductError()))
			{
				result = true;
			}
		}
		return result;
	}

	/**
	 * The hashCode method is over ridden with shipper, principal and order type of SplitOrderInfo class.
	 */
	@Override
	public int hashCode()
	{
		int hash = 1;

		if (this.principal != null && this.principal.length() > 0)
		{
			hash = hash * this.principal.hashCode();
		}
		if (this.docorderType != null && this.docorderType.length() > 0)
		{
			hash = hash * this.docorderType.hashCode();
		}
		if (this.forbiddenFlag != null && this.forbiddenFlag.length() > 0)
		{
			hash = hash * this.forbiddenFlag.hashCode();
		}
		if (this.sector != null && this.sector.length() > 0)
		{
			hash = hash * this.sector.hashCode();
		}
		if (this.noProductError != null && this.noProductError.length() > 0)
		{
			hash = hash * this.noProductError.hashCode();
		}

		return hash;
	}

}
