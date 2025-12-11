/**
 *
 */
package com.jnj.la.core.daos;

import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.model.JnjSAPErrorTranslationTableModel;


/**
 * The Interface JnjSAPErrMessageDao.
 * 
 * @author shriya.tiwari
 */
public interface JnjLatamSAPErrMessageDao
{

	/**
	 * Gets the all error details.
	 * 
	 * @param key
	 *           the key
	 * @return the all error details
	 * @throws BusinessException
	 *            the business exception
	 */
	JnjSAPErrorTranslationTableModel getAllErrorDetails(final String key) throws BusinessException;

	/**
	 * Gets the default error.
	 * 
	 * @return the default error
	 * @throws BusinessException
	 */
	JnjSAPErrorTranslationTableModel getDefaultError() throws BusinessException;
}
