/**
 *
 */
package com.jnj.la.b2b.cartandcheckoutaddon.forms;

import org.springframework.web.multipart.MultipartFile;


/**
 * @author Manoj.K.Panda
 *
 */
public class JnjMockDocumentTransferForm
{
	private MultipartFile fileToUpload;
	private String Company;

	/**
	 * @return the fileToUpload
	 */
	public MultipartFile getFileToUpload()
	{
		return fileToUpload;
	}

	/**
	 * @param fileToUpload
	 *           the fileToUpload to set
	 */
	public void setFileToUpload(final MultipartFile fileToUpload)
	{
		this.fileToUpload = fileToUpload;
	}

	/**
	 * @return the company
	 */
	public String getCompany()
	{
		return Company;
	}

	/**
	 * @param company
	 *           the company to set
	 */
	public void setCompany(final String company)
	{
		Company = company;
	}
}
