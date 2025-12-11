/**
 * 
 */
package com.jnj.gt.service.product.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTProductLotModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.service.product.JnjGTLotMasterFeedService;


/**
 * The Service layer class associated with Product Lot Master Feed.
 * 
 * @author t.e.sharma
 */
public class DefaultJnjGTLotMasterFeedService implements JnjGTLotMasterFeedService
{
	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductFeedService.class);

	/** Instance of <code>JnJProductService</code>. */
	private JnJProductService jnJProductService;

	/** Instance of <code>ModelService</code>. */
	private ModelService modelService;


	@Autowired
	private FlexibleSearchService flexibleSearchService;

	/**
	 * {@inheritDoc}
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
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveItems(final Collection<? extends ItemModel> items) throws BusinessException
	{
		try
		{
			getModelService().saveAll(items);
		}
		catch (final ModelSavingException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}


	public JnjGTProductLotModel getProductLotByExample(final JnjGTProductLotModel productLot)
	{
		JnjGTProductLotModel jnjGTProductLotModel = null;
		try
		{
			jnjGTProductLotModel = flexibleSearchService.getModelByExample(productLot);
		}
		catch (final ModelNotFoundException exception)
		{
			// In case of Lot master feed this message is req for info only 
			LOGGER.info("Lot Master not Found,Creating a New Model" + exception.getMessage());
		}
		return jnjGTProductLotModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemModel createNewItem(final Class modelClass)
	{
		return getModelService().create(modelClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JnJProductModel getProductByCode(final String code)
	{
		if (getJnJProductService().getProductCodeOrEAN(code) instanceof JnJProductModel)
		{
			return (JnJProductModel) getJnJProductService().getProductCodeOrEAN(code);
		}
		return null;
	}

	@Override
	public void removeItems(final Collection<? extends ItemModel> items) throws BusinessException
	{
		try
		{
			getModelService().removeAll(items);
		}
		catch (final ModelRemovalException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * @return the jnJProductService
	 */
	public JnJProductService getJnJProductService()
	{
		return jnJProductService;
	}

	/**
	 * @param jnJProductService
	 *           the jnJProductService to set
	 */
	public void setJnJProductService(final JnJProductService jnJProductService)
	{
		this.jnJProductService = jnJProductService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


}
