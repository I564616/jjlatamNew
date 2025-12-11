/**
 *
 */
package com.jnj.la.core.services.order.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.daos.JnjLatamSAPErrMessageDao;
import com.jnj.la.core.model.JnjSAPErrorTranslationTableModel;
import com.jnj.la.core.services.order.JnjLatamSAPErrorService;


/**
 * @author shriya.tiwari
 *
 */
public class JnjLatamSAPErrorServiceImpl implements JnjLatamSAPErrorService

{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjLatamSAPErrorServiceImpl.class);

	@Autowired
	private JnjLatamSAPErrMessageDao jnjSAPErrMessageDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.services.JnjSAPErrorService#getAllErrorDetails(java.lang.String)
	 */
	@Override
	public JnjSAPErrorTranslationTableModel getAllErrorDetails(final String key)
	{
		final String METHOD_NAME = "getAllErrorDetails()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel = null;

		try
		{
			jnjSAPErrorTranslationTableModel = jnjSAPErrMessageDao.getAllErrorDetails(key);
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + "Cannot find the Translated SAP Message for key["
					+ key + "]. Exception occured with message - " + businessException);
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + "Cannot find the Translated SAP Message for key["
					+ key + "]. Exception occured with message - " + businessException.getLocalizedMessage());

		}
		return jnjSAPErrorTranslationTableModel;
	}



	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.facades.services.JnjSAPErrorService#getDefaultError()
	 */
	@Override
	public JnjSAPErrorTranslationTableModel getDefaultError()
	{
		final String METHOD_NAME = "getDefaultError()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel = null;
		try
		{
			jnjSAPErrorTranslationTableModel = jnjSAPErrMessageDao.getDefaultError();
		}
		catch (final BusinessException businessException)
		{
			LOGGER.error(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "Cannot find Default Translated SAP Message. Exception occred with message - " + businessException);
		}
		return jnjSAPErrorTranslationTableModel;
	}
}
