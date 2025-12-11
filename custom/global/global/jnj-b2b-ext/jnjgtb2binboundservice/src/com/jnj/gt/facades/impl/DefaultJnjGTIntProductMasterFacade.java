/**
 *
 */
package com.jnj.gt.facades.impl;

import de.hybris.platform.core.model.ItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.enums.RecordStatus;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.facades.JnjGTIntFacade;
import com.jnj.gt.mapper.product.JnjGTProductMasterMapper;
import com.jnj.gt.model.JnJGTIntProductPlantModel;
import com.jnj.gt.model.JnjGTIntProductDescModel;
import com.jnj.gt.model.JnjGTIntProductKitModel;
import com.jnj.gt.model.JnjGTIntProductModel;
import com.jnj.gt.model.JnjGTIntProductRegModel;
import com.jnj.gt.model.JnjGTIntProductSalesOrgModel;
import com.jnj.gt.model.JnjGTIntProductUnitModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTProductFeedService;


public class DefaultJnjGTIntProductMasterFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntProductMasterFacade.class);

	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTProductFeedService jnjGTProductFeedService;

	@Autowired
	private JnjGTProductMasterMapper jnjGTProductMasterMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final List<JnjGTIntProductModel> invalidIntProductRecords = (List<JnjGTIntProductModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntProductModel._TYPECODE, recordStatus, selectionDate);

		final Collection<ItemModel> invalidIntRecords = getIntermediateRecordsForCleanup(invalidIntProductRecords);
		invalidIntRecords.addAll(invalidIntProductRecords);
		jnjGTFeedService.invalidateRecords(invalidIntRecords);



		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void processIntermediaryRecords()
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "processIntermediaryRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		/** After cleanup, mark the valid records status to PENDING **/

		jnjGTFeedService.updateIntRecordStatus(JnjGTIntProductModel._TYPECODE);

		//jnjGTFeedService.updateIntRecordStatus(JnjGTIntProductDescModel._TYPECODE);

		//jnjGTFeedService.updateIntRecordStatus(JnjGTIntProductDescModel._TYPECODE);

		jnjGTProductMasterMapper.processIntermediateRecords();

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_MASTER + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}

	private Collection<ItemModel> getIntermediateRecordsForCleanup(final List<JnjGTIntProductModel> invalidIntProductRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();
		String materialNum = null;

		for (final JnjGTIntProductModel intProductModel : invalidIntProductRecords)
		{
			materialNum = intProductModel.getMaterialNum();

			/** Fetch Intermediate Product Kit Records. **/
			final Collection<JnjGTIntProductKitModel> intProductKitRecords = jnjGTProductFeedService
					.getIntProductKitRecordsByProductSkuCode(JnjGTIntProductKitModel._TYPECODE, materialNum,
							intProductModel.getSrcSystem());
			if (intProductKitRecords != null && !intProductKitRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductKitRecords);
			}

			/** Fetch Intermediate Product Region Records. **/
			final Collection<JnjGTIntProductRegModel> intProductRegRecords = jnjGTProductFeedService
					.getIntProductRegRecordsByProductSkuCode(JnjGTIntProductRegModel._TYPECODE, materialNum,
							intProductModel.getSrcSystem());
			if (intProductRegRecords != null && !intProductRegRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductRegRecords);
			}

			/** Fetch Intermediate Product Description Records. **/
			final Collection<JnjGTIntProductDescModel> intProductDescRecords = jnjGTProductFeedService
					.getIntProductDescRecordsByProductSkuCode(JnjGTIntProductDescModel._TYPECODE, materialNum,
							intProductModel.getSrcSystem());
			if (intProductDescRecords != null && !intProductDescRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductDescRecords);
			}

			/** Fetch Intermediate Product Unit Records. **/
			final Collection<JnjGTIntProductUnitModel> intProductUnitRecords = jnjGTProductFeedService
					.getIntProductUnitsRecordsByProductSkuCode(materialNum, null, true, intProductModel.getSrcSystem());
			if (intProductUnitRecords != null && !intProductUnitRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductUnitRecords);
			}

			/** Fetch Intermediate Product Plant Records. **/
			final Collection<JnJGTIntProductPlantModel> intProductPlantRecords = jnjGTProductFeedService
					.getIntProductPlantRecordsByProductSkuCode(JnJGTIntProductPlantModel._TYPECODE, materialNum,
							intProductModel.getSrcSystem());
			if (intProductPlantRecords != null && !intProductPlantRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductPlantRecords);
			}

			/** Fetch Intermediate Product Sales Org. Records. **/
			final Collection<JnjGTIntProductSalesOrgModel> intProductSalesOrgRecords = jnjGTProductFeedService
					.getIntProductSalesOrgRecordsByProductSkuCode(JnjGTIntProductSalesOrgModel._TYPECODE, materialNum,
							intProductModel.getSrcSystem());
			if (intProductSalesOrgRecords != null && !intProductSalesOrgRecords.isEmpty())
			{
				invalidIntRecords.addAll(intProductSalesOrgRecords);
			}
		}
		return invalidIntRecords;
	}

	/**
	 * @return the jnjGTFeedService
	 */
	public JnjGTFeedService getjnjGTFeedService()
	{
		return jnjGTFeedService;
	}

	/**
	 * @param jnjGTFeedService
	 *           the jnjGTFeedService to set
	 */
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

	/**
	 * @param jnjGTProductFeedService
	 *           the jnjGTProductFeedService to set
	 */
	public void setjnjGTProductFeedService(final JnjGTProductFeedService jnjGTProductFeedService)
	{
		this.jnjGTProductFeedService = jnjGTProductFeedService;
	}

	/**
	 * @return the jnjGTProductMasterMapper
	 */
	public JnjGTProductMasterMapper getjnjGTProductMasterMapper()
	{
		return jnjGTProductMasterMapper;
	}

	/**
	 * @param jnjGTProductMasterMapper
	 *           the jnjGTProductMasterMapper to set
	 */
	public void setjnjGTProductMasterMapper(final JnjGTProductMasterMapper jnjGTProductMasterMapper)
	{
		this.jnjGTProductMasterMapper = jnjGTProductMasterMapper;
	}
}
