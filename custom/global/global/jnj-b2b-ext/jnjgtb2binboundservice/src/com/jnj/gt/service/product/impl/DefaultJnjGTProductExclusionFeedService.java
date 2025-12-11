/**
 * 
 */
package com.jnj.gt.service.product.impl;

import de.hybris.platform.b2bacceleratorservices.company.CompanyB2BCommerceService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjProductExclusionClassType;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.JnJProductService;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.dao.product.JnjGTProductExclusionFeedDao;
import com.jnj.gt.model.JnjGTIntProductExclusionModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductExclusionFeedService;


/**
 * The Service layer class associated with Product Exclusion Feed.
 * 
 */
public class DefaultJnjGTProductExclusionFeedService implements JnjGTProductExclusionFeedService
{
	/**
	 * Constant LOGGER.
	 */
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTProductExclusionFeedService.class);

	/**
	 * Instance of <code>CompanyB2BCommerceService</code>
	 */
	@Autowired
	private CompanyB2BCommerceService companyB2BCommerceService;

	/**
	 * Instance of <code>JnJProductService</code>
	 */
	@Autowired
	private JnJProductService jnjProductService;

	/**
	 * Instance of <code>ModelService</code>
	 */
	@Autowired
	private ModelService modelService;

	/**
	 * Instance of <code>JnjGTProductExclusionFeedDao</code>
	 */
	@Autowired
	private JnjGTProductExclusionFeedDao jnjGTProductExclusionFeedDao;

	/**
	 * Instance of <code>FlexibleSearchService</code>
	 */
	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * {!{@inheritDoc}
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
	 * {!{@inheritDoc}
	 */
	@Override
	public <T> T createNewItem(final Class modelClass)
	{
		return getModelService().create(modelClass);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public Collection<JnjGTIntProductExclusionModel> getProdExclusionRecords(final JnjProductExclusionClassType classType,
			final String characteristicValue, final String materialNumber)
	{
		return getJnjGTProductExclusionFeedDao().getProdExclusionRecords(classType, characteristicValue, materialNumber);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<String> getB2BUnitCodesUsingMaterialNumber(final String materialNumber)
	{
		return getJnjGTProductExclusionFeedDao().getB2BUnitCodesUsingMaterialNumber(materialNumber);
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public void removeAllProductExclusionRecords() throws BusinessException
	{
		//Check if this returns ALL records i.e. all class types
		final Collection<JnjGTIntProductExclusionModel> productExclusions = (Collection<JnjGTIntProductExclusionModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntProductExclusionModel._TYPECODE, RecordStatus.PENDING);
		if (!productExclusions.isEmpty())
		{
			try
			{
				getModelService().removeAll(productExclusions);
			}
			catch (final ModelRemovalException e)
			{
				throw new BusinessException(e.getMessage());
			}
		}
	}

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public Collection<JnJB2BUnitModel> getCustomersForGroup(final String groupNumber)
	{
		return getJnjGTProductExclusionFeedDao().getCustomersForGroup(groupNumber);
	}

	public CompanyB2BCommerceService getCompanyB2BCommerceService()
	{
		return companyB2BCommerceService;
	}

	public JnJProductService getJnJProductService()
	{
		return jnjProductService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public JnjGTProductExclusionFeedDao getJnjGTProductExclusionFeedDao()
	{
		return jnjGTProductExclusionFeedDao;
	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

}
