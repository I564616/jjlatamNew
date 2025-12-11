/**
 * 
 */
package com.jnj.gt.service.shipment.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.gt.dao.shipment.JnjGTShipmentFeedDao;
import com.jnj.gt.model.JnjGTIntShipTrckLineModel;
import com.jnj.gt.service.shipment.JnjGTShipmentFeedService;


/**
 * The JnjGTB2BUnitFeedServiceImpl class contains all those methods which are dealing with customer related intermediate
 * model and it has definition of all the methods which are defined in the JnjGTB2BUnitFeedService interface.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public class DefaultJnjGTShipmentFeedService implements JnjGTShipmentFeedService
{
	private static final Logger LOGGER = Logger.getLogger(DefaultJnjGTShipmentFeedService.class);

	@Autowired
	private JnjGTShipmentFeedDao jnjGTShipmentFeedDao;

	/**
	 * {!{@inheritDoc}
	 */
	@Override
	public List<JnjGTIntShipTrckLineModel> getJnjGTIntShipTrckLineModel(final String correlationId, final String deliveryNum,
			final String sourceSysId)
	{
		return jnjGTShipmentFeedDao.getJnjGTIntShipTrckLineModel(correlationId, deliveryNum, sourceSysId);
	}

}
