/**
 * 
 */
package com.jnj.core.dto;

/**
 * @author nsinha7
 *
 */
public class JnjGTSerialResponseData {
	
	private String inputSerialNumber;
	private String gtin;
	private String batchNumber;
	private String expiryYear;
	private String expiryMonth;
	private String expiryDay;
	private String serialNumber;
	private String status;
	private String reason;
	private String reasonCode;
	private String LPN;
	
	/**
	 * @return the inputSerialNumber
	 */
	public String getInputSerialNumber() {
		return inputSerialNumber;
	}
	/**
	 * @param inputSerialNumber the inputSerialNumber to set
	 */
	public void setInputSerialNumber(String inputSerialNumber) {
		this.inputSerialNumber = inputSerialNumber;
	}
	/**
	 * @return the serialNumber
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	/**
	 * @return the gtin
	 */
	public String getGtin() {
		return gtin;
	}
	/**
	 * @param gtin the gtin to set
	 */
	public void setGtin(String gtin) {
		this.gtin = gtin;
	}
	/**
	 * @return the batchNumber
	 */
	public String getBatchNumber() {
		return batchNumber;
	}
	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}
	/**
	 * @return the expiryYear
	 */
	public String getExpiryYear() {
		return expiryYear;
	}
	/**
	 * @param expiryYear the expiryYear to set
	 */
	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	/**
	 * @return the expiryMonth
	 */
	public String getExpiryMonth() {
		return expiryMonth;
	}
	/**
	 * @param expiryMonth the expiryMonth to set
	 */
	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	/**
	 * @return the expiryDay
	 */
	public String getExpiryDay() {
		return expiryDay;
	}
	/**
	 * @param expiryDays the expiryDays to set
	 */
	public void setExpiryDay(String expiryDay) {
		this.expiryDay = expiryDay;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the reasonCode
	 */
	public String getReasonCode() {
		return reasonCode;
	}
	/**
	 * @param reasonCode the reasonCode to set
	 */
	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
	/**
	 * @return the lPN
	 */
	public String getLPN() {
		return LPN;
	}
	/**
	 * @param lPN the lPN to set
	 */
	public void setLPN(String lPN) {
		LPN = lPN;
	}
	

}
