package com.jnj.la.jnjlaservicepageaddon.forms;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author plahiri1
 *
 */
public class JnjLaudoFileUploadForm extends JnjFileUploadForm {

	private MultipartFile multipartFile;
	private String productCode;
	private String laudoNumber;
	private String expirationDate;
	private String countryIso;
	private boolean documentDeleted;
	
	public boolean isDocumentDeleted() {
		return this.documentDeleted;
	}

	public void setDocumentDeleted(final boolean documentDeleted) {
		this.documentDeleted = documentDeleted;
	}

	/**
	 * @return the countryIso
	 */
	public String getCountryIso() {
		return countryIso;
	}

	/**
	 * @param countryIso
	 *            the countryIso to set
	 */
	public void setCountryIso(final String countryIso) {
		this.countryIso = countryIso;
	}

	/**
	 * @return the multipartFile
	 */
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	/**
	 * @param multipartFile
	 *            the multipartFile to set
	 */
	public void setMultipartFile(final MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	/**
	 * @return the productCode
	 */
	public String getProductCode() {
		return productCode;
	}

	/**
	 * @param productCode
	 *            the productCode to set
	 */
	public void setProductCode(final String productCode) {
		this.productCode = productCode;
	}

	/**
	 * @return the laudoNumber
	 */
	public String getLaudoNumber() {
		return laudoNumber;
	}

	/**
	 * @param laudoNumber
	 *            the laudoNumber to set
	 */
	public void setLaudoNumber(final String laudoNumber) {
		this.laudoNumber = laudoNumber;
	}

	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(final String expirationDate) {
		this.expirationDate = expirationDate;
	}

}
