/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.dropshipment.impl;

import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import com.jnj.la.core.dto.JnjDropshipmentDTO;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jnj.core.constants.Jnjb2bCoreConstants.Logging;
import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.core.services.impl.DefaultJnjGTDropshipmentService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.dao.dropshipment.JnjDropshipmentDAO;
import com.jnj.la.core.services.dropshipment.JnjDropshipmentService;


/**
 *
 *
 */
public class JnjDropshipmentServiceImpl extends DefaultJnjGTDropshipmentService implements JnjDropshipmentService
{

	@Autowired
	protected ModelService modelService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	protected JnjDropshipmentDAO jnjlaDropshipmentDAO;


	private static final String DROPSHIPMENT_SERVICE = "Dropshipment Service";
	private static final String METHOD_UPSERT_DROP_SHIPMENT_DETAILS = "insertUpdateDropShipmentDetails()";
	private static final String METHOD_DELETE_DROP_SHIPMENT_DETAILS = "deleteDropshipmentDetails()";

	public boolean insertUpdateDropShipmentDetails(final JnjDropshipmentDTO jnjDropshipmentDTO)
			throws BusinessException
	{
		boolean recordStatus = false;

		JnjGTCoreUtil.logInfoMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
				METHOD_UPSERT_DROP_SHIPMENT_DETAILS, Logging.BEGIN_OF_METHOD , JnjDropshipmentServiceImpl.class);

		// Check if drop shipment info already exists
		JnjDropShipmentDetailsModel jnjDropShipmentDetailsFromDB = getDropshipment(jnjDropshipmentDTO);

		try
		{
			jnjDropShipmentDetailsFromDB = getJnjDropShipmentDetailsModel(jnjDropshipmentDTO, jnjDropShipmentDetailsFromDB);
			modelService.save(jnjDropShipmentDetailsFromDB);
			recordStatus = true;
			JnjGTCoreUtil.logDebugMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
					METHOD_UPSERT_DROP_SHIPMENT_DETAILS,"Successfully saved Drop Shipment Model with ECARE ID: " + jnjDropshipmentDTO.getEcareId(),
					JnjDropshipmentServiceImpl.class);
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
					METHOD_UPSERT_DROP_SHIPMENT_DETAILS," Database error occurred while saving Drop Shipment Model with ECARE ID: "
							+ jnjDropshipmentDTO.getEcareId() + exception, JnjDropshipmentServiceImpl.class);
			recordStatus = false;
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN, METHOD_UPSERT_DROP_SHIPMENT_DETAILS,
					" Error occurred while saving Drop Shipment Model with ECARE ID: " + jnjDropshipmentDTO.getEcareId()
							+ exception, JnjDropshipmentServiceImpl.class);
			recordStatus = false;
		}

		JnjGTCoreUtil.logInfoMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
				METHOD_UPSERT_DROP_SHIPMENT_DETAILS, Logging.END_OF_METHOD , JnjDropshipmentServiceImpl.class);

		return recordStatus;
	}

	private JnjDropShipmentDetailsModel getJnjDropShipmentDetailsModel(JnjDropshipmentDTO jnjDropshipmentDTO,
																	   JnjDropShipmentDetailsModel jnjDropShipmentDetailsFromDB) throws BusinessException
	{
		if (null != jnjDropShipmentDetailsFromDB)
        {
			jnjDropShipmentDetailsFromDB = updateDropshipmentDetails(jnjDropShipmentDetailsFromDB, jnjDropshipmentDTO);
        }
        else
        {
			jnjDropShipmentDetailsFromDB = createJnjDropShipmentDetailsModel(jnjDropshipmentDTO);
        }
		return jnjDropShipmentDetailsFromDB;
	}

	private JnjDropShipmentDetailsModel createJnjDropShipmentDetailsModel(JnjDropshipmentDTO jnjDropshipmentDTO) {
		Date date = Calendar.getInstance().getTime();
		JnjDropShipmentDetailsModel jnjDropShipmentDetailsFromDB;
		jnjDropShipmentDetailsFromDB = modelService.create(JnjDropShipmentDetailsModel.class);
		jnjDropShipmentDetailsFromDB.setId(jnjDropshipmentDTO.getEcareId());
		jnjDropShipmentDetailsFromDB.setShipperMD(jnjDropshipmentDTO.getShipperMd());
		jnjDropShipmentDetailsFromDB.setShipTo(jnjDropshipmentDTO.getShipTo());
		jnjDropShipmentDetailsFromDB.setSoldTo(jnjDropshipmentDTO.getSoldTo());
		jnjDropShipmentDetailsFromDB.setTimeCreated(date);
		jnjDropShipmentDetailsFromDB.setPrincipal(jnjDropshipmentDTO.getPrincipal());
		jnjDropShipmentDetailsFromDB.setDestinationCountry(jnjDropshipmentDTO.getDestCountry());
		jnjDropShipmentDetailsFromDB.setDocumentType(jnjDropshipmentDTO.getDocType());
		jnjDropShipmentDetailsFromDB.setMaterialId(jnjDropshipmentDTO.getMaterialNum());
		jnjDropShipmentDetailsFromDB.setShipper(jnjDropshipmentDTO.getShipper());
		jnjDropShipmentDetailsFromDB.setSalesOrganization(jnjDropshipmentDTO.getSalesOrg());
		jnjDropShipmentDetailsFromDB.setTimeModified(date);
		return jnjDropShipmentDetailsFromDB;
	}

	private JnjDropShipmentDetailsModel updateDropshipmentDetails(JnjDropShipmentDetailsModel jnjDropShipmentDetailsFromDB,
																  JnjDropshipmentDTO jnjDropshipmentDTO) {
		Date date = Calendar.getInstance().getTime();
		jnjDropShipmentDetailsFromDB.setId(jnjDropshipmentDTO.getEcareId());
		jnjDropShipmentDetailsFromDB.setShipperMD(jnjDropshipmentDTO.getShipperMd());
		jnjDropShipmentDetailsFromDB.setShipTo(jnjDropshipmentDTO.getShipTo());
		jnjDropShipmentDetailsFromDB.setSoldTo(jnjDropshipmentDTO.getSoldTo());
		jnjDropShipmentDetailsFromDB.setPrincipal(jnjDropshipmentDTO.getPrincipal());
		jnjDropShipmentDetailsFromDB.setDestinationCountry(jnjDropshipmentDTO.getDestCountry());
		jnjDropShipmentDetailsFromDB.setDocumentType(jnjDropshipmentDTO.getDocType());
		jnjDropShipmentDetailsFromDB.setMaterialId(jnjDropshipmentDTO.getMaterialNum());
		jnjDropShipmentDetailsFromDB.setShipper(jnjDropshipmentDTO.getShipper());
		jnjDropShipmentDetailsFromDB.setSalesOrganization(jnjDropshipmentDTO.getSalesOrg());
		jnjDropShipmentDetailsFromDB.setTimeModified(date);
		return jnjDropShipmentDetailsFromDB;
	}

	public boolean deleteDropshipmentProductDetails(final JnjDropshipmentDTO jnjDropshipmentDTO)
			throws BusinessException
	{

		boolean recordStatus = false;
		JnjGTCoreUtil.logDebugMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
				METHOD_DELETE_DROP_SHIPMENT_DETAILS, Logging.BEGIN_OF_METHOD + JnJCommonUtil.getCurrentDateTime() ,
				JnjDropshipmentServiceImpl.class);
		try
		{

			final JnjDropShipmentDetailsModel drpshpmntModelFromDB = getDropshipment(jnjDropshipmentDTO);

			if (null != drpshpmntModelFromDB)
			{
				modelService.remove(drpshpmntModelFromDB);
				recordStatus = true;
				JnjGTCoreUtil.logInfoMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN,
						METHOD_DELETE_DROP_SHIPMENT_DETAILS, "Successfully Deleted Drop Shipment Model with Ecare Id: " + drpshpmntModelFromDB.getId(),
						JnjDropshipmentServiceImpl.class);
			}
			else
			{
				JnjGTCoreUtil.logErrorMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN, METHOD_DELETE_DROP_SHIPMENT_DETAILS,
						" Drop Shipment model not found for deletion: " + jnjDropshipmentDTO.getEcareId()
						, JnjDropshipmentServiceImpl.class);
			}
		}
		catch (final ModelSavingException exception)
		{
			JnjGTCoreUtil.logErrorMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN, METHOD_DELETE_DROP_SHIPMENT_DETAILS,
					" Database error occurred while deleting Drop Shipment Model with ECARE ID:  " + jnjDropshipmentDTO.getEcareId()
					+ exception.getMessage(), JnjDropshipmentServiceImpl.class);
		}
		catch (final Exception exception)
		{
			JnjGTCoreUtil.logErrorMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN, METHOD_DELETE_DROP_SHIPMENT_DETAILS,
					" Error occurred while deleting Drop Shipment Model with ECARE ID:  " + jnjDropshipmentDTO.getEcareId() + exception.getMessage()
					, JnjDropshipmentServiceImpl.class);
		}

		JnjGTCoreUtil.logInfoMessage(DROPSHIPMENT_SERVICE + Jnjlab2bcoreConstants.Logging.HYPHEN, METHOD_DELETE_DROP_SHIPMENT_DETAILS,
				Logging.END_OF_METHOD, JnjDropshipmentServiceImpl.class);

		return recordStatus;
	}

	public JnjDropShipmentDetailsModel getDropshipment(final JnjDropshipmentDTO jnjDropshipmentDTO)
	{
		JnjDropShipmentDetailsModel drpshpmntModelFromDB = null;

		if (jnjDropshipmentDTO != null)
		{
			drpshpmntModelFromDB = jnjlaDropshipmentDAO.getDropshipmentDetailsModel(jnjDropshipmentDTO);
		}
		return drpshpmntModelFromDB;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.services.JnjDropshipmentService#getDropshipmentDetailsBySalesOrg(java.lang.String)
	 */
	@Override
	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrg(final String salesOrganization)
	{

		return getJnjDropshipmentDetailsBySalesOrgAndMaterialId(salesOrganization, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.jnj.core.services.JnjDropshipmentService#getJnjDropshipmentDetailsByMaterialId(java.util.List)
	 */
	@Override
	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsByMaterialId(final List<String> materialIds)
	{
		return getJnjDropshipmentDetailsBySalesOrgAndMaterialId(null, materialIds);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.jnj.core.services.JnjDropshipmentService#getJnjDropshipmentDetailsBySalesOrgAndMaterialId(java.lang.String,
	 * java.util.List)
	 */
	@Override
	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrgAndMaterialId(final String salesOrganization,
			final List<String> materialIds)
	{
		return jnjlaDropshipmentDAO.getJnjDropshipmentDetailsBySalesOrg(salesOrganization, materialIds);
	}

}
