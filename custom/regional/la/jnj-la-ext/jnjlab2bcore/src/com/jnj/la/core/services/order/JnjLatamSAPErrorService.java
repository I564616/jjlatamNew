/**
 *
 */
package com.jnj.la.core.services.order;

import com.jnj.la.core.model.JnjSAPErrorTranslationTableModel;


/**
 * @author shriya.tiwari
 * 
 */
public interface JnjLatamSAPErrorService
{
	JnjSAPErrorTranslationTableModel getAllErrorDetails(final String key);

	JnjSAPErrorTranslationTableModel getDefaultError();

}
