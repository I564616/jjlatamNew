/**
 *
 */
package com.jnj.la.core.daos.impl;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.daos.JnjLatamSAPErrMessageDao;
import com.jnj.la.core.model.JnjSAPErrorTranslationTableModel;


/**
 * @author shriya.tiwari
 *
 */
public class JnjLatamSAPErrMessageDaoImpl implements JnjLatamSAPErrMessageDao
{
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjLatamSAPErrMessageDaoImpl.class);

	private static final String FIND_ERROR_MESSAGE = " SELECT {pk} FROM {JnjSAPErrorTranslationTable} WHERE {KEY}=(?key) ";

	private static final String FIND_DEFAULT_ERROR_MESSAGE = " SELECT {pk} FROM {JnjSAPErrorTranslationTable} WHERE {KEY}='default'";


	@Override
	public JnjSAPErrorTranslationTableModel getAllErrorDetails(final String key) throws BusinessException
	{
		final String METHOD_NAME = "getAllErrorDetails()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		final Map parameters = new HashMap();
		parameters.put("key", key);
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_ERROR_MESSAGE);
		fQuery.addQueryParameters(parameters);
		JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel = null;
		try
		{
			jnjSAPErrorTranslationTableModel = flexibleSearchService.searchUnique(fQuery);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.error(modelNotFoundException);
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "ModelNotFoundException occured. Cascading to Business Exception. ");
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());

		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{

			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "AmbiguousIdentifierException occured. Cascading to Business Exception. " + ambiguousIdentifierException);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + "Exception occured. Cascading to Business Exception. ",
					exception);
			throw new BusinessException(exception.getLocalizedMessage());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnjSAPErrorTranslationTableModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.dao.JnjSAPErrMessageDao#getDefaultError()
	 */
	@Override
	public JnjSAPErrorTranslationTableModel getDefaultError() throws BusinessException
	{
		final String METHOD_NAME = "getDefaultError()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.BEGIN_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(FIND_DEFAULT_ERROR_MESSAGE);
		JnjSAPErrorTranslationTableModel jnjSAPErrorTranslationTableModel = null;
		try
		{
			jnjSAPErrorTranslationTableModel = flexibleSearchService.searchUnique(fQuery);
		}
		catch (final ModelNotFoundException modelNotFoundException)
		{
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "ModelNotFoundException occured. Cascading to Business Exception. ", modelNotFoundException);
			throw new BusinessException(modelNotFoundException.getLocalizedMessage());

		}
		catch (final AmbiguousIdentifierException ambiguousIdentifierException)
		{
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME
					+ "AmbiguousIdentifierException occured. Cascading to Business Exception. ", ambiguousIdentifierException);
			throw new BusinessException(ambiguousIdentifierException.getLocalizedMessage());
		}
		catch (final Exception exception)
		{
			LOGGER.warn(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + "Exception occured. Cascading to Business Exception. ",
					exception);
			throw new BusinessException(exception.getLocalizedMessage());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.SUBMIT_ORDER + Logging.HYPHEN + METHOD_NAME + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + System.currentTimeMillis());
		}
		return jnjSAPErrorTranslationTableModel;
	}

}
