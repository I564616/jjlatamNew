/**
 * 
 */
package com.jnj.gt.service.product.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductCpscDetailModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.dao.product.JnjGTCpscFeedDao;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;
import com.jnj.gt.service.product.JnjGTCpscFeedService;


/**
 * The Service layer class associated with Product CPSIA Feed.
 */

public class DefaultJnjGTCpscFeedService implements JnjGTCpscFeedService
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTCpscFeedService.class);

	/**
	 * The instance of <code>ModelService</code>.
	 */
	@Autowired
	private ModelService modelService;

	/**
	 * The instance of <code>jnjGTCpscFeedDao</code>.
	 */
	@Autowired
	private JnjGTCpscFeedDao jnjGTCpscFeedDao;

	/**
	 * The instance of <code>FlexibleSearchService</code>.
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTCpscFeedService#saveItem(de.hybris.platform.core.model.ItemModel)
	 */
	@Override
	public void saveItem(final ItemModel item) throws BusinessException
	{
		try
		{
			getModelService().save(item);
		}
		catch (final ModelSavingException e)
		{
			LOGGER.error("Encountered an ERROR while saving a model " + e.getMessage());
			throw new BusinessException(e.getMessage());

		}
	}


	@Override
	public void removeItem(final ItemModel item) throws BusinessException
	{
		try
		{
			getModelService().remove(item);
		}
		catch (final ModelRemovalException e)
		{
			LOGGER.error("Encountered an ERROR while Removing a model " + e.getMessage());
			throw new BusinessException(e.getMessage());

		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTCpscFeedService#createNewItem(java.lang.Class)
	 */
	@Override
	public ItemModel createNewItem(final Class modelClass)
	{
		return getModelService().create(modelClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTCpscFeedService#getProductByCodeAndLotNumber(java.lang.String)
	 */
	@Override
	public JnJProductModel getProductByCode(final String productCode)
	{
		return getjnjGTCpscFeedDao().getProductByCode(productCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntCpscContactDetailModel> getCpscContactDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber)
	{
		return getjnjGTCpscFeedDao().getCpscContactDetailByProductCodeAndLotNumber(productSkuCode, lotNumber);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntCpscTestDetailModel> getCpscTestDetailByProductCodeAndLotNumber(final String productSkuCode,
			final String lotNumber)
	{
		return getjnjGTCpscFeedDao().getCpscTestDetailByProductCodeAndLotNumber(productSkuCode, lotNumber);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTCpscFeedService#cleanExistingCPSCRecords()
	 */
	@Override
	public void cleanExistingCPSCRecords()
	{
		final Collection<JnjGTProductCpscDetailModel> existingCpscRecords = getjnjGTCpscFeedDao().getExistingCpscDetails();
		if (existingCpscRecords != null && !existingCpscRecords.isEmpty())
		{
			try
			{
				getModelService().removeAll(existingCpscRecords);
			}

			catch (final ModelRemovalException e)
			{
				LOGGER.error("Deletion/Removal of records from table 'JNJ_PRODUCT_CPSC' has caused an error: " + e.getMessage());
			}
		}
	}

	/**
	 * Gets the model service.
	 * 
	 * @return the model service
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Gets the flexible search service.
	 * 
	 * @return the flexible search service
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * Gets the jnj na cpsc feed dao.
	 * 
	 * @return the jnj na cpsc feed dao
	 */
	public JnjGTCpscFeedDao getjnjGTCpscFeedDao()
	{
		return jnjGTCpscFeedDao;
	}
}
