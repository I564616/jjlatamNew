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
import com.jnj.gt.mapper.product.JnjGTProductCPSIAMapper;
import com.jnj.gt.model.JnjGTIntCpscContactDetailModel;
import com.jnj.gt.model.JnjGTIntCpscDetailsModel;
import com.jnj.gt.model.JnjGTIntCpscTestDetailModel;
import com.jnj.gt.service.common.JnjGTFeedService;
import com.jnj.gt.service.product.JnjGTCpscFeedService;


public class DefaultJnjGTIntCpsiaFacade extends JnjGTIntFacade
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTIntCpsiaFacade.class);
	@Autowired
	private JnjGTFeedService jnjGTFeedService;

	@Autowired
	private JnjGTCpscFeedService jnjGTCpscFeedService;

	@Autowired
	private JnjGTProductCPSIAMapper jnjGTProductCPSIAMapper;

	@Override
	public void cleanInvalidRecords(final RecordStatus recordStatus, final Date selectionDate)
	{
		final List<JnjGTIntCpscDetailsModel> invalidIntCpscRecords = (List<JnjGTIntCpscDetailsModel>) jnjGTFeedService
				.getRecordsByStatus(JnjGTIntCpscDetailsModel._TYPECODE, recordStatus, selectionDate);

		final Collection<ItemModel> invalidIntRecords = getIntermediateRecordsForCleanup(invalidIntCpscRecords);
		invalidIntRecords.addAll(invalidIntCpscRecords);
		jnjGTFeedService.invalidateRecords(invalidIntRecords);

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(Logging.PRODUCT_LOCAL + Logging.HYPHEN + "cleanInvalidRecords()" + Logging.HYPHEN + Logging.END_OF_METHOD
					+ Logging.HYPHEN + Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
	}

	@Override
	public void processIntermediaryRecords()
	{
		/*
		 * jnjGTFeedService.updateIntRecordStatus(JnjGTIntCpscDetailsModel._TYPECODE);
		 * jnjGTProductCPSIAMapper.processIntermediateRecords();
		 */
	}

	private Collection<ItemModel> getIntermediateRecordsForCleanup(final List<JnjGTIntCpscDetailsModel> invalidIntCpscRecords)
	{
		final Collection<ItemModel> invalidIntRecords = new ArrayList<>();
		String productSkuCode = null;
		String lotNumber = null;

		for (final JnjGTIntCpscDetailsModel intCpscModel : invalidIntCpscRecords)
		{
			productSkuCode = intCpscModel.getProductSkuCode();
			lotNumber = intCpscModel.getLotNumber();
			/** Fetch Intermediate CPSC Contact Records. **/
			final Collection<JnjGTIntCpscContactDetailModel> intCpscContactRecords = jnjGTCpscFeedService
					.getCpscContactDetailByProductCodeAndLotNumber(productSkuCode, lotNumber);
			if (intCpscContactRecords != null && !intCpscContactRecords.isEmpty())
			{
				invalidIntRecords.addAll(intCpscContactRecords);
			}

			/** Fetch Intermediate Cpsc test Records. **/
			final Collection<JnjGTIntCpscTestDetailModel> intCpscTestRecords = jnjGTCpscFeedService
					.getCpscTestDetailByProductCodeAndLotNumber(productSkuCode, lotNumber);
			if (intCpscTestRecords != null && !intCpscTestRecords.isEmpty())
			{
				invalidIntRecords.addAll(intCpscTestRecords);
			}
		}
		return invalidIntRecords;
	}
}
