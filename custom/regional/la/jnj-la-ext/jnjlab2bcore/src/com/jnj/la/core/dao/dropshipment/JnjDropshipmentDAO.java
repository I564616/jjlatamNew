/**
 *
 */
package com.jnj.la.core.dao.dropshipment;

import java.util.List;

import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.la.core.dto.JnjDropshipmentDTO;


/**
 * @author sh11
 *
 */
public interface JnjDropshipmentDAO
{
	public JnjDropShipmentDetailsModel getDropshipmentProduct(JnjDropShipmentDetailsModel jnjDropShipmentDetailsModel);

	public JnjDropShipmentDetailsModel getDropshipmentDetailsModel(JnjDropshipmentDTO jnjDropshipmentDTO);

	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrg(String salesOrg, List<String> materialIds);
}
