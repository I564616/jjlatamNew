/**
 * 
 */
package com.jnj.gt.dao.shipment;

import java.util.List;

import com.jnj.gt.model.JnjGTIntShipTrckLineModel;



/**
 * The JnjGTShipmentFeedDao interface contains all those methods which are dealing with shipment related intermediate
 * model and it has declaration of all the methods which are defined in the JnjGTShipmentFeedDaoImpl class.
 * 
 * @author Accenture
 * @version 1.0
 * 
 */
public interface JnjGTShipmentFeedDao
{

	/**
	 * Gets the jnj na int ship trck line model.
	 * 
	 * @param correlationId
	 *           the correlation id
	 * @param deliveryNum
	 *           the delivery num
	 * @param sourceSysId
	 *           the source sys id
	 * @return the jnj na int ship trck line model
	 */
	public List<JnjGTIntShipTrckLineModel> getJnjGTIntShipTrckLineModel(final String correlationId, final String deliveryNum,
			final String sourceSysId);

}
