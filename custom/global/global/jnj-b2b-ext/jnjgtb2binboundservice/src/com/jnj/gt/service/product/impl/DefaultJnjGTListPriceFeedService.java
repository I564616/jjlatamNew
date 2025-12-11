/**
 * 
 */
package com.jnj.gt.service.product.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjGTSourceSysId;
import com.jnj.core.model.JnjGTVariantProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.dao.product.JnjGTListPriceFeedDao;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;
import com.jnj.gt.service.product.JnjGTListPriceFeedService;
import com.jnj.gt.util.JnjGTInboundUtil;


/**
 * @author dheeraj.e.sharma
 * 
 */
public class DefaultJnjGTListPriceFeedService implements JnjGTListPriceFeedService
{
	/**
	 * Instance of <code>ModelService</code>
	 */
	@Autowired
	private ModelService modelService;

	/**
	 * Instance of <code>JnjGTProductFeedDao</code>
	 */
	@Autowired
	private JnjGTListPriceFeedDao jnjGTListPriceFeedDao;

	@Autowired
	private de.hybris.platform.catalog.CatalogVersionService catalogVersionService;

	@Autowired
	private ProductService productService;


	@Override
	public void saveItem(final ItemModel item) throws BusinessException
	{
		try
		{
			modelService.save(item);
		}
		catch (final ModelSavingException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void saveItems(final Collection<? extends ItemModel> items) throws BusinessException
	{
		try
		{
			modelService.saveAll(items);
		}
		catch (final ModelSavingException e)
		{
			throw new BusinessException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTListPriceFeedService#createNewItem(java.lang.Class)
	 */
	@Override
	public <T> T createNewItem(final Class modelClass)
	{
		return getModelService().create(modelClass);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jnj.core.services.product.jnjGTListPriceFeedService#getJnjGTVariantProductsByUomAndBaseCode(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Collection<JnjGTVariantProductModel> getProductByUom(final String productSkuCode, final String priceUomCode,
			final String srcSysId)
	{
		return getJnjGTListPriceFeedDao().getProductByUom(productSkuCode, priceUomCode, srcSysId,
				getStgCatalogVersionByProduct(srcSysId).getPk().toString());
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @return the jnjGTListPriceFeedDao
	 */
	public JnjGTListPriceFeedDao getJnjGTListPriceFeedDao()
	{
		return jnjGTListPriceFeedDao;
	}

	/**
	 * @param jnjGTListPriceFeedDao
	 *           the jnjGTListPriceFeedDao to set
	 */
	public void setJnjGTListPriceFeedDao(final JnjGTListPriceFeedDao jnjGTListPriceFeedDao)
	{
		this.jnjGTListPriceFeedDao = jnjGTListPriceFeedDao;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jnj.core.services.product.jnjGTListPriceFeedService#getListPriceAmountRecordsByListPriceId()
	 */
	@Override
	public Collection<JnjGTIntListPriceAmtModel> getListPriceAmountRecordsByListPriceId(final String listPriceID)
	{

		return jnjGTListPriceFeedDao.getListPriceAmountRecordsByListPriceId(listPriceID);
	}

	/**
	 * Fetches the STG Catalog version by product.
	 */
	@Override
	public CatalogVersionModel getStgCatalogVersionByProduct(final String srcSystemID)
	{
		String catalogID = Jnjgtb2binboundserviceConstants.Product.CONSUMER_USA_CATALOG_ID;
		if (isMDD(srcSystemID))
		{
			catalogID = Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID;
		}
		return catalogVersionService.getCatalogVersion(catalogID, Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
	}

	private boolean isMDD(final String srcSystemID)
	{
		return JnjGTSourceSysId.MDD.toString().equals(JnjGTInboundUtil.fetchValidSourceSysId(srcSystemID));
	}

}
