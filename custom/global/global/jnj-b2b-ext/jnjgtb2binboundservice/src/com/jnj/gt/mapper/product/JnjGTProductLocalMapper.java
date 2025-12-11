package com.jnj.gt.mapper.product;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.util.Config;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.JnjGTModStatus;
import com.jnj.core.enums.RecordStatus;
import com.jnj.core.model.JnJProductModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants;
import com.jnj.gt.mapper.JnjAbstractMapper;
import com.jnj.gt.model.JnJGTIntProductLocalModel;
import com.jnj.gt.model.JnJGTIntProductLocalPlantModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;


/**
 * The Mapper class responsible to process all <code>JnjGTIntProductModel</code> records, based on the intermediate
 * records from Local feed to Update MDD <code>JnJProductModel</code>. Logic includes processing, mapping and
 * establishing relationships of <code>JnjGTIntProductLocalModel</code> with associations.
 *
 * Version 0.1 [Author: akash.rawat]
 *
 */
public class JnjGTProductLocalMapper extends JnjAbstractMapper
{
	/**
	 * The constant Instance of <code>Logger</code>.
	 */
	private static final Logger LOGGER = Logger.getLogger(JnjGTProductLocalMapper.class);

	/**
	 * Private instance of <code>jnjGTFeedService</code>
	 */
	private JnjGTFeedService jnjGTFeedService;

	/**
	 * Private instance of <code>jnjGTProductFeedService</code>
	 */
	private JnjGTProductFeedService jnjGTProductFeedService;

	/**
	 * Private instance of <code>CatalogVersionService</code>
	 */
	@Autowired
	private CatalogVersionService catalogVersionService;

	/**
	 * <code>CatalogVersionModel</code> instance for MDD catalog.
	 */
	private CatalogVersionModel mddCatalogVersion;

	/**
	 * Processes all <code>JnJNAIntProductLocalModel</code> records and persist them after processing as updated
	 * <code>JnJProductModel</code>.
	 */
	@Override
	public void processIntermediateRecords()
	{
		try
		{
			getMddCatalogVersion();
		}
		catch (final BusinessException exception)
		{
			LOGGER.error("PODUCT LOCAL FEED PROCESSING COULD NOT BE PROCESSED: " + exception.getMessage());
			return;
		}

		processProductLocalRecords();
	}

	/**
	 * Processes valid intermediate records from <code>JnJNAIntProductLocal</code>.
	 */
	private void processProductLocalRecords()
	{
		final String mddActiveProdStatusCodes = Config
				.getParameter(Jnjgtb2binboundserviceConstants.Product.MDD_ACTIVE_PROD_STATUS_CODES_KEY);
		final String mddDiscontinuedProdStatusCodes = Config
				.getParameter(Jnjgtb2binboundserviceConstants.Product.MDD_DISCONTINUED_PROD_STATUS_CODES_KEY);

		final Collection<JnJGTIntProductLocalModel> intProductLocalModels = (Collection<JnJGTIntProductLocalModel>) getjnjGTFeedService()
				.getRecordsByStatus(JnJGTIntProductLocalModel._TYPECODE, RecordStatus.PENDING);

		for (final JnJGTIntProductLocalModel intProductLocalModel : intProductLocalModels)
		{
			try
			{
				final JnJProductModel productModel = getjnjGTProductFeedService().getProductByCode(
						intProductLocalModel.getProductSkuCode(), mddCatalogVersion);

				if (productModel == null)
				{
					final String errorMessage = "Could NOT find a Product corresponding to the PRODUCT SKU CODE: "
							+ intProductLocalModel.getProductSkuCode();
					throw new BusinessException(errorMessage);
				}

				productModel.setDChainStatusEffectiveDate(intProductLocalModel.getDChainStatusEffectiveDate());
				final String statusCode = intProductLocalModel.getDChainSpecStatus();
				productModel.setModStatus((mddActiveProdStatusCodes.contains(statusCode)) ? JnjGTModStatus.ACTIVE
						: ((mddDiscontinuedProdStatusCodes.contains(statusCode)) ? JnjGTModStatus.DISCONTINUED : null));
				productModel.setInFieldInd(intProductLocalModel.getInFieldInd());

				final List<JnJGTIntProductLocalPlantModel> intProductLocalPlantRecords = (List<JnJGTIntProductLocalPlantModel>) getjnjGTProductFeedService()
						.getIntProdPlantLocalRecords(intProductLocalModel.getProductSkuCode(), intProductLocalModel.getSrcSystem());

				if (CollectionUtils.isEmpty(intProductLocalPlantRecords))
				{
					LOGGER.info("NO INTERMEDIATE PRODUCT PLANT LOCAL RECORDS FOR PROD. SKU: "
							+ intProductLocalModel.getProductSkuCode() + ", AND SOURCE SYS: " + intProductLocalModel.getSrcSystem());
				}
				else
				{
					final int size = intProductLocalPlantRecords.size();
					final JnJGTIntProductLocalPlantModel intProductLocalPlantModel = intProductLocalPlantRecords.get(size - 1);
					productModel.setBatchMgmt((intProductLocalPlantModel.getBatchMgmt() != null) ? intProductLocalPlantModel
							.getBatchMgmt() : Boolean.FALSE);
				}

				getjnjGTProductFeedService().saveItem(productModel);
				getjnjGTFeedService().updateIntermediateRecord(intProductLocalModel, RecordStatus.SUCCESS, false, null);
			}
			catch (final BusinessException e)
			{
				LOGGER.error("PRODUCT LOCAL FEED PROCESSING for Intermediate record with PRODUCT SKU CODE: "
						+ intProductLocalModel.getProductSkuCode() + ", could NOT be processed. Error: " + e.getMessage());
				getjnjGTFeedService().updateIntermediateRecord(intProductLocalModel, RecordStatus.PENDING, true, e.getMessage());
			}
			catch (final Exception exception)
			{
				LOGGER.error(
						"PRODUCT LOCAL FEED PROCESSING for Intermediate record with PRODUCT SKU CODE: "
								+ intProductLocalModel.getProductSkuCode() + ", could NOT be processed", exception);
				getjnjGTFeedService().updateIntermediateRecord(intProductLocalModel, RecordStatus.PENDING, true,
						exception.getMessage());
			}
		}
	}

	/**
	 * Fetches the MDD and CONSUMER product Catalog version from the active store front along with the root Category.
	 *
	 * @throws BusinessException
	 *            : If either MDD and Consumer Catalog versions are found in the active storefront, or Multiple Root
	 *            category is found.
	 */
	private void getMddCatalogVersion() throws BusinessException
	{
		mddCatalogVersion = catalogVersionService.getCatalogVersion(Jnjgtb2binboundserviceConstants.Product.MDD_CATALOG_ID,
				Jnjgtb2binboundserviceConstants.Product.STAGED_CATALOG_VERSION);
	}

	/**
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	public void setjnjGTFeedService(final JnjGTFeedService jnjGTFeedService)
	{
		this.jnjGTFeedService = jnjGTFeedService;
	}

	/**
	 * @return the jnjGTProductFeedService
	 */
	public JnjGTProductFeedService getjnjGTProductFeedService()
	{
		return jnjGTProductFeedService;
	}

	public void setjnjGTProductFeedService(final JnjGTProductFeedService jnjGTProductFeedService)
	{
		this.jnjGTProductFeedService = jnjGTProductFeedService;
	}

	@Override
	public void processIntermediateRecords(String facadeBeanId) {
		// TODO Auto-generated method stub
		
	}

}
