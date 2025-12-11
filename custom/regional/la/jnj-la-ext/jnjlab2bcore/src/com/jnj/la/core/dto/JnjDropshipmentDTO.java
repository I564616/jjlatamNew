/**
 *
 */
package com.jnj.la.core.dto;


public class JnjDropshipmentDTO
{
	private String materialNum;
	private String operation;
	private String lastUpdatedDate;
	private String docType;
	private String salesOrg;
	private String shipper;
	private String principal;
	private String destCountry;
	private String ecareId;
	private String soldTo;
	private String shipTo;
	private String shipperMd;

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getDestCountry() {
		return destCountry;
	}

	public void setDestCountry(String destCountry) {
		this.destCountry = destCountry;
	}

	public String getEcareId() {
		return ecareId;
	}

	public void setEcareId(String ecareId) {
		this.ecareId = ecareId;
	}

	public String getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(String soldTo) {
		this.soldTo = soldTo;
	}

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public String getShipperMd() {
		return shipperMd;
	}

	public void setShipperMd(String shipperMd) {
		this.shipperMd = shipperMd;
	}
}
