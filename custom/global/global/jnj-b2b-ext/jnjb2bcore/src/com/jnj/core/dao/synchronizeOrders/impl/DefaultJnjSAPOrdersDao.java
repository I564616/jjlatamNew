/**
 * 
 */
package com.jnj.core.dao.synchronizeOrders.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.synchronizeOrders.JnjSAPOrdersDao;
import com.jnj.core.model.JnjDeliveryScheduleModel;
import com.jnj.core.model.JnjOrdEntStsMappingModel;
import com.jnj.core.model.JnjOrdStsMappingModel;
import com.jnj.core.util.JnJCommonUtil;


/**
 * @author akash.rawat
 * 
 */
public class DefaultJnjSAPOrdersDao implements JnjSAPOrdersDao
{
	protected static final Logger LOG = Logger.getLogger(DefaultJnjSAPOrdersDao.class);
	protected static final int PROCESSING_STATUS = 0;
	protected FlexibleSearchService flexibleSearchService;

	@Override
	public OrderEntryModel getExistingOrderEntryByEntryNumber(final String entryNumber, final String orderNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT DISTINCT {").append(ItemModel.PK).append("} FROM {").append(OrderEntryModel._TYPECODE)
				.append(" AS ent JOIN ").append(OrderModel._TYPECODE).append(" AS ord ON {ent:order} = {ord:pk}} WHERE {ent:")
				.append(OrderEntryModel.ENTRYNUMBER).append("} = ?entryNumber").append(" AND {ord:")
				.append(OrderModel.SAPORDERNUMBER).append("} = ?sapOrderNumber");

		try
		{
			queryParams.put(OrderEntryModel.ENTRYNUMBER, Integer.valueOf(entryNumber));
			queryParams.put(OrderModel.SAPORDERNUMBER, Integer.valueOf(orderNumber));
		}
		catch (final NumberFormatException exc)
		{
			LOG.error("Exception during converting value of: " + entryNumber + exc.getMessage());
			exc.printStackTrace();
		}

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<OrderEntryModel> result = getFlexibleSearchService().<OrderEntryModel> search(flexibleSearchQuery).getResult();
		return (result.size() > 0 ? result.get(0) : null);
	}


	@Override
	public JnjDeliveryScheduleModel getExistingScheduleLineByLineNumber(final String lineNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT DISTINCT {").append(ItemModel.PK).append("} FROM {").append(JnjDeliveryScheduleModel._TYPECODE)
				.append("} WHERE {").append(JnjDeliveryScheduleModel.LINENUMBER).append("} = ?lineNumber");

		queryParams.put(JnjDeliveryScheduleModel.LINENUMBER, lineNumber);
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<JnjDeliveryScheduleModel> result = getFlexibleSearchService().<JnjDeliveryScheduleModel> search(
				flexibleSearchQuery).getResult();
		return (result.size() > 0 ? result.get(0) : null);
	}

	@Override
	public OrderModel getExistingOrderBySAPOrderNumber(final String sapOrderNumber)
	{
		final StringBuilder searchQuery = new StringBuilder();
		final Map queryParams = new HashMap();
		searchQuery.append("SELECT DISTINCT {").append(ItemModel.PK).append("} FROM {").append(OrderModel._TYPECODE)
				.append("} WHERE {").append(OrderModel.SAPORDERNUMBER).append("} = ?sapOrderNumber");
		queryParams.put(OrderModel.SAPORDERNUMBER, sapOrderNumber);

		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(searchQuery);
		flexibleSearchQuery.addQueryParameters(queryParams);
		final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(flexibleSearchQuery).getResult();
		return (result.size() > 0 ? result.get(0) : null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<JnjOrdStsMappingModel> getOrderStatus(final String overAllStatus, final String rejectionStatus,
			final String creditStatus, final String deliveryStatus, final String invoiceStatus)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final StringBuilder searchQuery = new StringBuilder();

		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjOrdStsMappingModel._TYPECODE)
				.append("} WHERE {").append(JnjOrdStsMappingModel.OVERALLSTATUS)
				.append((overAllStatus == null) ? "} IS NULL" : "} = ?overAllStatus").append(" AND {")
				.append(JnjOrdStsMappingModel.REJECTIONSTATUS)
				.append((rejectionStatus == null) ? "} IS NULL" : "} = ?rejectionStatus").append(" AND {")
				.append(JnjOrdStsMappingModel.CREDITSTATUS).append((creditStatus == null) ? "} IS NULL" : "} = ?creditStatus")
				.append(" AND {").append(JnjOrdStsMappingModel.DELIVERYSTATUS)
				.append((deliveryStatus == null) ? "} IS NULL" : "} = ?deliveryStatus").append(" AND {")
				.append(JnjOrdStsMappingModel.INVOICESTATUS).append((invoiceStatus == null) ? "} IS NULL" : "} = ?invoiceStatus");

		final Map queryParams = new HashMap();

		if (overAllStatus != null)
		{
			queryParams.put(JnjOrdStsMappingModel.OVERALLSTATUS, overAllStatus);
		}

		if (rejectionStatus != null)
		{
			queryParams.put(JnjOrdStsMappingModel.REJECTIONSTATUS, rejectionStatus);
		}

		if (creditStatus != null)
		{
			queryParams.put(JnjOrdStsMappingModel.CREDITSTATUS, creditStatus);
		}
		if (deliveryStatus != null)
		{
			queryParams.put(JnjOrdStsMappingModel.DELIVERYSTATUS, deliveryStatus);
		}
		if (invoiceStatus != null)
		{
			queryParams.put(JnjOrdStsMappingModel.INVOICESTATUS, invoiceStatus);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Staus Model Query " + fQuery);
		}

		final List<JnjOrdStsMappingModel> result = getFlexibleSearchService().<JnjOrdStsMappingModel> search(fQuery).getResult();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (!result.isEmpty())
		{
			return result;
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public List<JnjOrdEntStsMappingModel> getOrderEntryStatus(final String overAllStatus, final String rejectionStatus,
			final String deliveryStatus, final String invoiceStatus, final String gtsHold)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		final StringBuilder searchQuery = new StringBuilder();

		searchQuery.append("SELECT {").append(ItemModel.PK).append("} FROM {").append(JnjOrdEntStsMappingModel._TYPECODE)
				.append("} WHERE {").append(JnjOrdEntStsMappingModel.OVERALLSTATUS)
				.append((overAllStatus == null) ? "} IS NULL" : "} = ?overAllStatus").append(" AND {")
				.append(JnjOrdEntStsMappingModel.REJECTIONSTATUS)
				.append((rejectionStatus == null) ? "} IS NULL" : "} = ?rejectionStatus").append(" AND {")
				.append(JnjOrdEntStsMappingModel.DELIVERYSTATUS)
				.append((deliveryStatus == null) ? "} IS NULL" : "} = ?deliveryStatus").append(" AND {")
				.append(JnjOrdEntStsMappingModel.INVOICESTATUS).append((invoiceStatus == null) ? "} IS NULL" : "} = ?invoiceStatus")
				.append(" AND {").append(JnjOrdEntStsMappingModel.GTSHOLD).append((gtsHold == null) ? "} IS NULL" : "} = ?GTSHold");

		final Map queryParams = new HashMap();

		if (overAllStatus != null)
		{
			queryParams.put(JnjOrdEntStsMappingModel.OVERALLSTATUS, overAllStatus);
		}

		if (rejectionStatus != null)
		{
			queryParams.put(JnjOrdEntStsMappingModel.REJECTIONSTATUS, rejectionStatus);
		}

		if (deliveryStatus != null)
		{
			queryParams.put(JnjOrdEntStsMappingModel.DELIVERYSTATUS, deliveryStatus);
		}
		if (invoiceStatus != null)
		{
			queryParams.put(JnjOrdEntStsMappingModel.INVOICESTATUS, invoiceStatus);
		}
		if (gtsHold != null)
		{
			queryParams.put(JnjOrdEntStsMappingModel.GTSHOLD, gtsHold);
		}


		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(searchQuery);
		fQuery.addQueryParameters(queryParams);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + " Order Entry Status Model Query " + fQuery);
		}

		final List<JnjOrdEntStsMappingModel> jnjOrderEntryStatusModelList = getFlexibleSearchService()
				.<JnjOrdEntStsMappingModel> search(fQuery).getResult();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("getOrderEntryStatus()" + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}

		if (!jnjOrderEntryStatusModelList.isEmpty())
		{
			return jnjOrderEntryStatusModelList;
		}
		else
		{
			return null;
		}
	}

	@Override
	public UnitModel getUnitOfMeasurement(final UnitModel unit) throws ModelNotFoundException, IllegalArgumentException
	{
		return getFlexibleSearchService().getModelByExample(unit);
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
