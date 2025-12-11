/**
 * 
 */
package com.jnj.gt.mapper.zipcode;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.jnj.exceptions.SystemException;


/**
 * The JnjGTEarlyZipCodeMapper interface contains the declaration of all the method of the JnjGTEarlyZipCodeMapperImpl
 * class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTEarlyZipCodeMapper
{
	/**
	 * Fetch the early zip code data by hitting the url and read the content of the file & save data in hybris database.
	 */
	public void fetchAndSaveDataInHybris() throws SystemException, IOException, FileNotFoundException;
}
