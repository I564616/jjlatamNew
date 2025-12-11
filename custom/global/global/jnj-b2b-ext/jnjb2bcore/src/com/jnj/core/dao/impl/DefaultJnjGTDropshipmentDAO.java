/**
 * 
 */
package com.jnj.core.dao.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.dao.JnJGtDropshipmentDAO;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

/**
 * @author nsinha7
 *
 */
public class DefaultJnjGTDropshipmentDAO implements JnJGtDropshipmentDAO{
	
	protected static final Logger LOGGER = Logger.getLogger(DefaultJnjGTDropshipmentDAO.class);
	
	@Autowired
	protected FlexibleSearchService flexibleSearchService;
	
	protected static final String FETCH_DROPSHIPMENT_DETAILS = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {materialId} IN (?selectedMaterialIds)";

	protected static final String FETCH_DROPSHIPMENT_DETAILS_BY_SALESORG = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {materialId} IN (?selectedMaterialIds) AND {salesOrganization}=?salesOrg";

	public FlexibleSearchService getFlexibleSearchService() {
		return flexibleSearchService;
	}
	
	@Override
	public List<JnjDropShipmentDetailsModel> getDropShipmentDetails(List<String> productCodesList) {

		final String METHOD_NAME = "getShippingDetails ()";
		LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
				+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());

		List<JnjDropShipmentDetailsModel> results = null;
		try
		{
			final String query = FETCH_DROPSHIPMENT_DETAILS;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter("selectedMaterialIds", productCodesList);
			results = flexibleSearchService.<JnjDropShipmentDetailsModel> search(fQuery).getResult();

		}
		catch (final ModelNotFoundException modelNotFound)
		{
			LOGGER.error("Error fetching Model " + JnJCommonUtil.getCurrentDateTime());
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());
		}
		return results;

	}
	@Override
	public List<JnjDropShipmentDetailsModel> getDropShipmentDetails(
			List<String> productCodesList, String salesOrg) {
		final String METHOD_NAME = "getShippingDetails ()";
		
		LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
				+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());

		List<JnjDropShipmentDetailsModel> results = null;
		try {
			final String query = FETCH_DROPSHIPMENT_DETAILS_BY_SALESORG;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
			fQuery.addQueryParameter("selectedMaterialIds", productCodesList);
			fQuery.addQueryParameter("salesOrg", salesOrg);
			results = flexibleSearchService
					.<JnjDropShipmentDetailsModel> search(fQuery).getResult();

		} catch (final ModelNotFoundException modelNotFound) {
			JnjGTCoreUtil.logErrorMessage("Get Dropshipment", METHOD_NAME,
					"No Dropshipment Details for material list: "
							+ productCodesList + " and salesOrg: " + salesOrg,
							DefaultJnjGTDropshipmentDAO.class);
			LOGGER.error("Error fetching Model " + JnJCommonUtil.getCurrentDateTime());
		}

		JnjGTCoreUtil.logInfoMessage("Get Dropshipment", METHOD_NAME,
				Logging.END_OF_METHOD, DefaultJnjGTDropshipmentDAO.class);
		
		return results;
	}

}
