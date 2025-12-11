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
import com.jnj.gt.mapper.product.JnjGTProductListPriceMapper;
import com.jnj.gt.model.JnjGTIntListPriceAmtModel;
import com.jnj.gt.model.JnjGTIntListPriceModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTListPriceFeedService;


public class DefaultJnjGTIntProductListPriceFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntProductListPriceFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTListPriceFeedService jnjGTListPriceFeedService;

	@Autowired
	private JnjGTProductListPriceMapper jnjGTProductListPriceMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_LIST_PRICE + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final List<JnjGTIntListPriceModel> invalidIntProductListPriceRecords = (List<JnjGTIntListPriceModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntListPriceModel._TYPECODE, recordStatus, selectionDate);


		final Collection<ItemModel> invalidIntRecords = getIntermediateRecordsForCleanup(invalidIntProductListPriceRecords);
		invalidIntRecords.addAll(invalidIntProductListPriceRecords);
		jnjGTFeedService.invalidateRecords(invalidIntRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_LIST_PRICE + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN
					+ Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

	}


	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_LIST_PRICE + Logging.HYPHEN +
		 * "processIntermediaryRecords()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN + Logging.START_TIME
		 * + JnJCommonUtil.getCurrentDateTime()); }
		 *//** After cleanup, mark the valid records status to PENDING **/
		/*
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntListPriceModel._TYPECODE);
		 * 
		 * jnjGTProductListPriceMapper.processIntermediateRecords();
		 * 
		 * if (LOGGER.isDebugEnabled()) { LOGGER.debug(Logging.PRODUCT_LIST_PRICE + Logging.HYPHEN +
		 * "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN + Logging.END_TIME +
		 * JnJCommonUtil.getCurrentDateTime()); }
		 */
	}

	private Collection<ItemModel> getIntermediateRecordsForCleanup(final List<JnjGTIntListPriceModel> invalidIntListPriceRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();
		String listPriceID = null;

		for (final JnjGTIntListPriceModel intListPriceModel : invalidIntListPriceRecords)
		{
			listPriceID = intListPriceModel.getListPriceID();
			Collection<JnjGTIntListPriceAmtModel> intListPriceAmountRecords;

			/** Fetch Intermediate List Price Records. **/
			intListPriceAmountRecords = jnjGTListPriceFeedService.getListPriceAmountRecordsByListPriceId(listPriceID);

			if (intListPriceAmountRecords != null && !intListPriceAmountRecords.isEmpty())
			{
				invalidIntRecords.addAll(intListPriceAmountRecords);
			}
		}
		return invalidIntRecords;
	}
}
