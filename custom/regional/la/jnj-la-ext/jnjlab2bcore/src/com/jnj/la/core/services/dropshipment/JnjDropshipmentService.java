/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2013
 * All rights reserved.
 */
package com.jnj.la.core.services.dropshipment;

import java.util.List;

import com.jnj.core.model.JnjDropShipmentDetailsModel;
import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.dto.JnjDropshipmentDTO;



public interface JnjDropshipmentService
{
	public boolean insertUpdateDropShipmentDetails(JnjDropshipmentDTO jnjDropshipmentProductDTO) throws BusinessException;

	public boolean deleteDropshipmentProductDetails(JnjDropshipmentDTO jnjDropshipmentProductDTO) throws BusinessException;

	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrg(String salesOrg);

	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsByMaterialId(List<String> materialIds);

	public List<JnjDropShipmentDetailsModel> getJnjDropshipmentDetailsBySalesOrgAndMaterialId(String salesOrg,
			List<String> materialIds);
}
