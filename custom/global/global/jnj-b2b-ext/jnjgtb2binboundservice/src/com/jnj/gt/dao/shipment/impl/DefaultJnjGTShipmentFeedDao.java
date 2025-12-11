/**
 * 
 */
package com.jnj.gt.dao.shipment.impl;

import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.Logging;
import com.jnj.gt.constants.Jnjgtb2binboundserviceConstants.ShipmentInbound;
import com.jnj.gt.dao.shipment.JnjGTShipmentFeedDao;
import com.jnj.gt.model.JnjGTIntShipTrckLineModel;



/**
 * The JnjGTShipmentFeedDaoImpl class contains all those methods which are dealing with shipment related intermediate
 * model and it has definition of all the methods which are defined in the JnjGTShipmentFeedDao interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTShipmentFeedDao extends AbstractItemDao implements JnjGTShipmentFeedDao
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTShipmentFeedDao.class);

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntShipTrckLineModel> getJnjGTIntShipTrckLineModel(final String correlationId, final String deliveryNum,
			final String sourceSysId)
	{
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntShipTrckLineModel()" + Logging.HYPHEN + Logging.BEGIN_OF_METHOD + Logging.HYPHEN
					+ Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		final Map queryParams = new HashMap();
		String query = ShipmentInbound.SHIP_TRCK_LINE_QUERY;
		if (StringUtils.isNotEmpty(correlationId))
		{
			query = query.concat(" Where {correlationId}=?correlationId");
			queryParams.put(JnjGTIntShipTrckLineModel.CORRELATIONID, correlationId);
		}
		if (StringUtils.isNotEmpty(deliveryNum))
		{
			query = query.concat(" Where {deliveryNum}=?deliveryNum");
			queryParams.put(JnjGTIntShipTrckLineModel.DELIVERYNUM, deliveryNum);
		}
		if (StringUtils.isNotEmpty(sourceSysId))
		{
			query = query.concat(" and {sourceSysId}=?sourceSysId");
			queryParams.put(JnjGTIntShipTrckLineModel.SOURCESYSID, sourceSysId);
		}

		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

		fQuery.addQueryParameters(queryParams);
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntShipTrckLineModel()" + Jnjb2bCoreConstants.Logging.HYPHEN + "JnjGTIntShipTrckLineModel Query "
					+ fQuery);
		}

		final List<JnjGTIntShipTrckLineModel> result = getFlexibleSearchService().<JnjGTIntShipTrckLineModel> search(fQuery)
				.getResult();
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug("getJnjGTIntShipTrckLineModel()" + Logging.HYPHEN + Logging.END_OF_METHOD + Logging.HYPHEN
					+ Logging.END_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return result;
	}





}
