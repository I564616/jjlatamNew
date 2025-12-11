package com.jnj.la.core.jalo;

import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCopyContext;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncWorker;
import de.hybris.platform.catalog.jalo.synchronization.ItemCopyCreator;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import com.jnj.core.jalo.JnJProduct;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.util.JnjGTCoreUtil;
import org.apache.log4j.Logger;


public class JnjProductCatalogsSyncJob extends GeneratedJnjProductCatalogsSyncJob
{
	private static final Logger LOG = Logger.getLogger(JnjProductCatalogsSyncJob.class);
	
	@Override
	protected CatalogVersionSyncCopyContext createCopyContext(final CatalogVersionSyncCronJob cj,
			final CatalogVersionSyncWorker worker)
	{
		// replace copy context to hook into it
		return new MyCopyContext(this, cj, worker);
	}


	private static class MyCopyContext extends CatalogVersionSyncCopyContext
	{
		private Country country = null;

		public MyCopyContext(final CatalogVersionSyncJob job, final CatalogVersionSyncCronJob cronjob,
				final CatalogVersionSyncWorker worker)
		{
			super(job, cronjob, worker);
			if (job instanceof JnjProductCatalogsSyncJob)
			{
				final JnjProductCatalogsSyncJob jnjSynJob = (JnjProductCatalogsSyncJob) job;
				country = jnjSynJob.getCountry();
			}
		}

		/*
		 * Called each time a item has been copied to target catalog successfully
		 */
		@Override
		protected void finishedCopying(final ItemCopyCreator icc)

		{
			super.finishedCopying(icc);
			ModelService modelService = null;
			// ... place custom code here ...
			final Item src = icc.getSourceItem(); // source version item
			final Item tgt = icc.getTargetItem(); // target version item
			JnJProductModel targetJnjProduct = null;

			if (src instanceof JnJProduct)
			{
				try
				{
					modelService = (ModelService) Registry.getApplicationContext().getBean("modelService");
					final JnJLaProduct srcJnjProduct = (JnJLaProduct) src;
					final JnJLaProduct jnJProduct = (JnJLaProduct) tgt;
					targetJnjProduct = modelService.get(jnJProduct);
					targetJnjProduct.setEcommerceFlag(Boolean.FALSE);				
					
					if (CollectionUtils.isNotEmpty(srcJnjProduct.getSalesOrgList()))
					{
						LOG.info("Count of Salesorg : "+srcJnjProduct.getSalesOrgList().size());
						productSalesOrgLoop: for (final JnJProductSalesOrg jnJProductSalesOrg : srcJnjProduct.getSalesOrgList())
						{
							final String salesOrgName = jnJProductSalesOrg.getSalesOrg();
							if (StringUtils.isEmpty(salesOrgName))
							{
								continue productSalesOrgLoop;
							}

							final String salesOrgCountry = salesOrgName.substring(0, 2);
							
							/* The active ProductSalesOrg is the Master */
							if (null != jnJProductSalesOrg.isActive() && jnJProductSalesOrg.isActive().booleanValue())
							{
								if (salesOrgCountry.equalsIgnoreCase(country.getIsocode()) && ((JnJLaProduct)src).getSector().equals(jnJProductSalesOrg.getSector()))
								{
									LOG.info("Sector "+ jnJProductSalesOrg.getSector());
									
									JnjGTCoreUtil.logInfoMessage("Jnj Product Catalog Sync Job for country " + salesOrgCountry,
											"finishedCopying()",
											"SalesOrg match found for sector " + jnJProductSalesOrg.getSector() ,
											JnjProductCatalogsSyncJob.class);

									targetJnjProduct.setNumeratorDUOM(jnJProductSalesOrg.getNumeratorDUOM());
									targetJnjProduct.setNumeratorSUOM(jnJProductSalesOrg.getNumeratorSUOM());
									targetJnjProduct.setProductStatusCode(jnJProductSalesOrg.getStatus());																								
									
									targetJnjProduct.setEcommerceFlag(jnJProductSalesOrg.isEcommerceFlag());									
								
									if (Objects.nonNull(jnJProductSalesOrg.getSalesUnitOfMeasure())) {									
										targetJnjProduct
												.setUnit((UnitModel) modelService.get(jnJProductSalesOrg
														.getSalesUnitOfMeasure()));
									}
									if (Objects.nonNull(jnJProductSalesOrg.getDeliveryUnitOfMeasure())) {										
										targetJnjProduct
												.setDeliveryUnitOfMeasure((UnitModel) modelService.get(jnJProductSalesOrg
														.getDeliveryUnitOfMeasure()));
									}									
									
									targetJnjProduct.setDisContinue(jnJProductSalesOrg.isDisContinue());
									targetJnjProduct.setColdChainProduct(jnJProductSalesOrg.isColdChainProduct());
									targetJnjProduct.setOfflineDate(jnJProductSalesOrg.getOfflineDate());
									modelService.save(targetJnjProduct);
									break productSalesOrgLoop;
								}
							}
						} //End of productSalesOrgLoop
					}
				}
				catch (final Exception exception)
				{
					JnjGTCoreUtil.logErrorMessage("Jnj Product Catalog Sync Joc", "finishedCopying",
							"Exception Occured while Copying Custom Attributes from Master To Local Product Sync Job", exception,
							JnjProductCatalogsSyncJob.class);
				}
			}
		}
	}
}
