/**
 *
 */
package com.jnj.la.core.dao.dropshipment.impl;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.la.core.dao.dropshipment.JnjDropshipmentDAO;



/**
 * @author sh11
 *
 */
public class JnjDropshipmentDAOImpl implements JnjDropshipmentDAO
{
	/** The flexible search service. */

	@Autowired
	private FlexibleSearchService flexibleSearchService;


	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getLogger(JnjDropshipmentDAOImpl.class);

	private static final String FETCH_DROPSHIPMENT_DETAILS = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {materialId}=?materialId and {salesOrganization}=?salesOrganization"
			+ " and {shipper}=?shipper and {principal}=?principal and {documentType}=?documentType and {destinationCountry}=?destinationCountry";

	private static final String QUERY_DROPSHIPMENT = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {id}=?id";
	private static final String QUERY_DROPSHIPMENT_SALESORG = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {salesOrganization}=?salesOrganization";
	private static final String QUERY_DROPSHIPMENT_MATERIALID = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {materialId}=?materialId";
	private static final String QUERY_DROPSHIPMENT_SALESORG_MATERIALID = "SELECT {pk} FROM {JnjDropShipmentDetails} WHERE {salesOrganization}=?salesOrganization AND {materialId} IN (?materialId)";
	private static final String PARAM_DEST_COUNTRY_NULL = " and {destinationCountry} IS NULL ";
	private static final String PARAM_DEST_COUNTRY_NOT_NULL = " and {destinationCountry}=?destinationCountry ";
	private static final String PARAM_SHIPPER_NULL = " and {shipper} IS NULL ";
	private static final String PARAM_SHIPPER_NOT_NULL = " and {shipper}=?shipper ";
	private static final String QUERY_INT_DROPSHIPMENT = "SELECT {pk} FROM {JnjIntDropShipmentDetails} WHERE {recordStatus}=?recordStatus";
	private static final String QUERY_INT_DROPSHIPMENT_ID = "SELECT {pk} FROM {JnjIntDropShipmentDetails} WHERE {id}=?id";

	public JnjDropShipmentDetailsModel getDropshipmentProduct(final JnjDropShipmentDetailsModel jnjDropShipmentDetailsModel)
	{


		final String METHOD_NAME = "getDropshipmentProduct ()";
		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		List<JnjDropShipmentDetailsModel> results = null;
		JnjDropShipmentDetailsModel jnjDrpShpmntDtailsModel = null;
		try
		{
			final String query = FETCH_DROPSHIPMENT_DETAILS;
			final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);

			fQuery.addQueryParameter("materialId", jnjDropShipmentDetailsModel.getMaterialId());
			fQuery.addQueryParameter("salesOrganization", jnjDropShipmentDetailsModel.getSalesOrganization());
			fQuery.addQueryParameter("shipper", jnjDropShipmentDetailsModel.getShipper());
			fQuery.addQueryParameter("principal", jnjDropShipmentDetailsModel.getPrincipal());
			fQuery.addQueryParameter("documentType", jnjDropShipmentDetailsModel.getDocumentType());
			fQuery.addQueryParameter("destinationCountry", jnjDropShipmentDetailsModel.getDestinationCountry());

			results = flexibleSearchService.<JnjDropShipmentDetailsModel> search(fQuery).getResult();

		}
		catch (final Exception ex)
		{
			LOGGER.error("[ Error while searching dropshipment: " + ex);
		}

		if (LOGGER.isDebugEnabled())
		{
			LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.END_OF_METHOD
					+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME
					+ JnJCommonUtil.getCurrentDateTime());
		}

		if (results != null && !results.isEmpty())
		{
			jnjDrpShpmntDtailsModel = results.get(0);
		}

		return jnjDrpShpmntDtailsModel;
	}

	public JnjDropShipmentDetailsModel getDropshipmentDetailsModel(final JnjDropshipmentDTO jnjDropshipmentDTO)
	{
		final String METHOD_NAME = "getDropshipmentDetailsModel()";
		LOGGER.debug(METHOD_NAME + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
				+ Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME + JnJCommonUtil.getCurrentDateTime());


		List<JnjDropShipmentDetailsModel> results = null;
		JnjDropShipmentDetailsModel jnjDrpShpmntDtailsModel = null;
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(QUERY_DROPSHIPMENT);
		fQuery.addQueryParameter("id", jnjDropshipmentDTO.getEcareId());

		try
		{
			results = flexibleSearchService.<JnjDropShipmentDetailsModel> search(fQuery).getResult();
		}
		catch (final Exception ex)
		{
			JnjGTCoreUtil.logErrorMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB + Jnjlab2bcoreConstants.Logging.HYPHEN,
					METHOD_NAME,"Error while searching Drop Shipment with ECARE ID: " + jnjDropshipmentDTO.getEcareId() + ex.getMessage(),
					JnjDropshipmentDAOImpl.class);
		}

		if (results != null && !results.isEmpty())
		{
			jnjDrpShpmntDtailsModel = results.get(0);
		}

		JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.LOAD_DROP_SHIPMENT_JOB + Jnjlab2bcoreConstants.Logging.HYPHEN,
				Jnjlab2bcoreConstants.Logging.END_OF_METHOD + Jnjlab2bcoreConstants.Logging.HYPHEN + METHOD_NAME, Jnjlab2bcoreConstants.Logging.HYPHEN + JnJCommonUtil.getCurrentDateTime(),
				JnjDropshipmentDAOImpl.class);

		return jnjDrpShpmntDtailsModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.dao.JnjDropshipmentDAO#getJnjDropshipmentDetailsBySalesOrg(java.lang.String)
	 */
	@Override
	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrg(final String salesOrganization,
			final List<String> materialIds)
	{
		List<JnjDropShipmentDetailsModel> results = null;
		FlexibleSearchQuery fQuery = null;

		if (salesOrganization != null && !salesOrganization.isEmpty())
		{
			if (materialIds != null && !materialIds.isEmpty())
			{
				fQuery = new FlexibleSearchQuery(QUERY_DROPSHIPMENT_SALESORG_MATERIALID);
				fQuery.addQueryParameter("salesOrganization", salesOrganization);
				fQuery.addQueryParameter("materialId", materialIds);
			}
			else
			{
				fQuery = new FlexibleSearchQuery(QUERY_DROPSHIPMENT_SALESORG);
				fQuery.addQueryParameter("salesOrganization", salesOrganization);
			}
		}
		else
		{
			fQuery = new FlexibleSearchQuery(QUERY_DROPSHIPMENT_MATERIALID);
			fQuery.addQueryParameter("materialId", materialIds);
		}

		try
		{
			results = flexibleSearchService.<JnjDropShipmentDetailsModel> search(fQuery).getResult();
		}
		catch (final Exception ex)
		{
			LOGGER.error("[ Error while searching Intermediate dropshipment: " + ex);
		}
		return results;
	}
}
