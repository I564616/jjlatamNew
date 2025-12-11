package com.jnj.core.services.surgeon.impl;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.dao.surgeon.JnjGTSurgeonDao;
import com.jnj.core.model.JnjGTSurgeonModel;
import com.jnj.core.services.surgeon.JnjGTSurgeonService;


/**
 * The Class JnjGTSurgeonServiceImpl.
 *
 * @author sakshi.kashiva
 */
public class DefaultJnjGTSurgeonService implements JnjGTSurgeonService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTSurgeonService.class);

	@Autowired
	protected ModelService modelService;

	@Autowired
	protected FlexibleSearchService flexibleSearchService;

	@Autowired
	protected JnjGTSurgeonDao jnjGTSurgeonDao;

	public ModelService getModelService() {
		return modelService;
	}

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}

	public JnjGTSurgeonDao getJnjGTSurgeonDao() {
		return jnjGTSurgeonDao;
	}

	/**
	 * Saves item model.
	 *
	 * @param itemModel
	 *           the item model
	 * @return true, if successful
	 */
	@Override
	public boolean saveSurgeon(final JnjGTSurgeonModel jnjGTSurgeonModel)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveSurgeon()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		boolean saved = false;
		try
		{
			modelService.save(jnjGTSurgeonModel);
			saved = true;
		}
		catch (final ModelSavingException modelSavingException)
		{
			throw modelSavingException;
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("saveSurgeon()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		return saved;
	}

	/**
	 * Gets the surgeon model by example.
	 *
	 * @param surgeon
	 *           the surgeon
	 * @return the surgeon model by example
	 */
	@Override
	public JnjGTSurgeonModel getJnjGTSurgeonModelByExample(final JnjGTSurgeonModel surgeon)
	{
		JnjGTSurgeonModel jnjGTSurgeonModel = null;
		try
		{
			jnjGTSurgeonModel = flexibleSearchService.getModelByExample(surgeon);
		}
		catch (final ModelNotFoundException exp)
		{

			LOGGER.debug("getJnjGTSurgeonModelByExample()" + Logging.HYPHEN + "Error Found-Model is not loaded" + Logging.END_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTSurgeonModelByExample()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return jnjGTSurgeonModel;
	}

	@Override
	public Collection<JnjGTSurgeonModel> getAllSurgeonRecords()
	{
		return jnjGTSurgeonDao.getAllSurgeonRecords();
	}

	@Override
	public SearchPageData<JnjGTSurgeonModel> getSurgeonRecords(final PageableData pageableData, final String searchPattern)
	{
		return jnjGTSurgeonDao.getSurgeonRecords(pageableData, searchPattern);
	}

}
