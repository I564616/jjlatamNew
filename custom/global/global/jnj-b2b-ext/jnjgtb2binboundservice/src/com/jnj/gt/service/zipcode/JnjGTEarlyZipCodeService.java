/**
 * 
 */
package com.jnj.gt.service.zipcode;

import de.hybris.platform.core.model.c2l.RegionModel;

import java.io.File;

import com.jnj.core.model.JnjGTEarlyZipCodesModel;
import com.jnj.exceptions.SystemException;


/**
 * The JnjGTEarlyZipCodeService interface contains the declaration of all the method of the JnjGTEarlyZipCodeServiceImpl
 * class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTEarlyZipCodeService
{

	/**
	 * Fetch file from external url by getting the url value from the property file and hit it to get the file in
	 * response.
	 * 
	 * @param isCallForTextFile
	 *           the is call for text file
	 * @return the file
	 */
	public File fetchFileFromExternalUrl(final boolean isCallForTextFile) throws SystemException;

	/**
	 * Gets the model by state and postal code.
	 * 
	 * @param areaName
	 *           the area name
	 * @param postalCode
	 *           the postal code
	 * @param regionModel
	 *           the region model
	 * @return the model by state and postal code
	 */
	public JnjGTEarlyZipCodesModel getModelByStateAndPostalCode(final String areaName, String postalCode, RegionModel regionModel);
}
