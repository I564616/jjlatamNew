/**
 * 
 */
package com.jnj.gt.job;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.Date;
import jakarta.annotation.Resource;
import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.core.services.JnJGTProductService;
import com.jnj.core.model.JnJProductModel;
import com.jnj.core.model.JnjGTVariantProductModel;


/**
 * Cron job responsible for updating and setting Delivery & Sales GTIN on daily basis to remove any inconsistent and
 * obsolete data for MDD products. If a delivery/sales GTIN for has been obsolete for a product, i.e. had been offline
 * then a run is passed through all eligible (i.e. having shipUnitInd set) and online variants to decide new Delivery
 * GTIN based on the greatest Package level code.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class JnjGTUpdateDeliverySalesGtinJob extends AbstractJobPerformable<CronJobModel>
{
	/**
	 * The Constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTUpdateDeliverySalesGtinJob.class);

	/**
	 * Private instance of <code>JnJGTProductService</code>
	 */
	@Resource(name = "productService")
	protected JnJGTProductService jnJGTProductService;

	/**
	 * Private instance of <code>CatalogVersionService</code>
	 */
	@Autowired
	protected CatalogVersionService catalogVersionService;


	/**
	 * Private instance of <code>CategoryService</code>
	 */
	@Autowired
	protected CategoryService categoryService;

	/**
	 * Private instance of <code>ModelService</code>
	 */
	@Autowired
	protected ModelService modelService;

	protected final int greatestPackglevelCode = 0;

	@Override
	public PerformResult perform(final CronJobModel cronJob)
	{
		LOGGER.info("START OF 'UPDATE DELIVERY/SALES GTIN JOB'" + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + "perform()");

		final String targetCatalogVersion = Config
				.getParameter(Jnjb2bCoreConstants.Product.DELIVERY_SALES_GTIN_UPDATE_TARGET_VERSION);
		if (targetCatalogVersion == null)
		{
			LOGGER.error("Could NOT find target Catalog Version for the GTIN update cron job, exiting the process.");
		}

		final CatalogVersionModel mddCatalogVersion = catalogVersionService.getCatalogVersion(
				Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID, targetCatalogVersion);

		CategoryModel mddRootCategory = null;
		try
		{
			mddRootCategory = categoryService.getCategoryForCode(mddCatalogVersion,
					Jnjgtb2binboundserviceConstants.Product.ROOT_CATEGORY_ID);
		}
		catch (final UnknownIdentifierException | AmbiguousIdentifierException exception)
		{
			LOGGER.error("COULD NOT FIND THE MDD ROOT CATEGORY BASED ON MDD-STAGED CATALOG VERSION: " + exception.getMessage());
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		final Collection<ProductModel> mddProducts = jnJGTProductService.getProductsForCategory(mddRootCategory);
		if (CollectionUtils.isEmpty(mddProducts))
		{
			LOGGER.info("NO PRODUCTS FOUND WITH THE MDD ROOT CATEGORY BASED ON MDD - " + targetCatalogVersion + "CATALOG VERSION");
		}

		final Date currentTimeStamp = new Date();

		for (final ProductModel product : mddProducts)
		{
			try
			{
				if (product instanceof JnJProductModel)
				{
					final JnJProductModel jnjGTProduct = (JnJProductModel) product;
					final Collection<JnjGTVariantProductModel> productVariants = jnJGTProductService
							.getVariantsOrderedByPkgLvlCode(jnjGTProduct.getPk().toString());

					boolean deliveryGtinFound = false;
					boolean salesGtinFound = false;
					for (final JnjGTVariantProductModel variant : productVariants)
					{
						/***
						 * If delivery GTIN has been already found and iterating item is the existing one, then set delivery
						 * GTIN off for this old variant.
						 ***/
						if (deliveryGtinFound && variant.getDeliveryGtinInd() != null && variant.getDeliveryGtinInd().booleanValue())
						{
							variant.setDeliveryGtinInd(Boolean.FALSE);
						}
						else if (!deliveryGtinFound
								&& variant.getShipUnitInd() != null
								&& Boolean.TRUE.equals(variant.getShipUnitInd())
								&& ArticleApprovalStatus.APPROVED.equals(variant.getApprovalStatus())
								&& (variant.getOnlineDate() == null || (variant.getOnlineDate() != null && variant.getOnlineDate()
										.before(currentTimeStamp)))
								&& (variant.getOfflineDate() == null || (variant.getOfflineDate() != null && variant.getOfflineDate()
										.after(currentTimeStamp))))
						{
							deliveryGtinFound = true;

							/***
							 * If existing Delivery GTIN already satisfies the criteria, therefore no change in the delivery
							 * GTIN.
							 ***/
							if (variant.getDeliveryGtinInd() != null && variant.getDeliveryGtinInd().booleanValue())
							{
								LOGGER.info("Existing Delivery GTIN with product code [" + variant.getCode()
										+ "] already satisfies the eligible criteria, hence no change required in Delivery GTIN."
										+ variant.getCode());
							}
							else
							{
								variant.setDeliveryGtinInd(Boolean.TRUE);
								LOGGER.info("Variant with product code: " + variant.getCode() + "has been set as the Delivery GTIN");
							}
						}


						/***
						 * If Sales GTIN has been already found and iterating item is the existing one, then set delivery GTIN
						 * off for this old variant.
						 ***/
						if (salesGtinFound && variant.getSalesGtinInd() != null && variant.getSalesGtinInd().booleanValue())
						{
							variant.setSalesGtinInd(Boolean.FALSE);
						}
						else if (!salesGtinFound
								&& variant.getSellUnitInd() != null
								&& Boolean.TRUE.equals(variant.getSellUnitInd())
								&& ArticleApprovalStatus.APPROVED.equals(variant.getApprovalStatus())
								&& (variant.getOnlineDate() == null || (variant.getOnlineDate() != null && variant.getOnlineDate()
										.before(currentTimeStamp)))
								&& (variant.getOfflineDate() == null || (variant.getOfflineDate() != null && variant.getOfflineDate()
										.after(currentTimeStamp))))
						{
							salesGtinFound = true;

							/***
							 * If existing Sales GTIN already satisfies the criteria, therefore no change in the Sales GTIN.
							 ***/
							if (variant.getSalesGtinInd() != null && variant.getSalesGtinInd().booleanValue())
							{
								LOGGER.info("Existing Sales GTIN with product code [" + variant.getCode()
										+ "] already satisfies the eligible criteria, hence no change required in Sales GTIN."
										+ variant.getCode());
							}
							else
							{
								variant.setSalesGtinInd(Boolean.TRUE);
								LOGGER.info("Variant with product code: " + variant.getCode() + "has been set as the Sales GTIN");
							}
						}
					}

					//final JnjGTVariantProductModel currentDelivertGtinVariant = jnJGTProductService.getDeliveryGTIN(jnjGTProduct);
					//final JnjGTVariantProductModel currentSalestGtinVariant = jnJGTProductService.getSalesGTIN(jnjGTProduct);

					if (!deliveryGtinFound)
					{
						LOGGER.info("No delivery GTIN update available for the product with code: " + jnjGTProduct.getCode());
					}
					if (!salesGtinFound)
					{
						LOGGER.info("No Sales GTIN update available for the product with code: " + jnjGTProduct.getCode());
					}

					saveUpdatedGTINVariants(productVariants, jnjGTProduct.getCode());
				}
			}
			catch (final Exception exception)
			{
				LOGGER.error(exception.getMessage());
			}
		}

		LOGGER.info("END OF 'UPDATE DELIVERY/SALES GTIN JOB'" + Logging.END_OF_METHOD + Logging.HYPHEN + "perform()");

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}


	/**
	 * Saves collection of all updated variants for a product.
	 * 
	 * @param variants
	 * @param productCode
	 */
	protected void saveUpdatedGTINVariants(final Collection<JnjGTVariantProductModel> variants, final String productCode)
	{
		LOGGER.info("saveUpdatedGTINVariants()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD);
		try
		{
			modelService.saveAll(variants);
		}
		catch (final ModelSavingException exception)
		{
			LOGGER.error("Exception while saving Delivery/Sales GTIN Variants for the Base product having code: " + productCode
					+ ". Exception Message: " + exception.getMessage());
		}
		LOGGER.info("saveUpdatedGTINVariants()" + Logging.HYPHEN + Logging.END_OF_METHOD);
	}
}
