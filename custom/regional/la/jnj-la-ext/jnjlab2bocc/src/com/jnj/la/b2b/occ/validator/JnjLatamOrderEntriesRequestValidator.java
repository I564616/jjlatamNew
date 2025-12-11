/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */

package com.jnj.la.b2b.occ.validator;

import com.jnj.exceptions.BusinessException;
import com.jnj.la.core.services.JnJLaProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderEntriesRequestWsDTO;


/**
 * The Class JnjLatamOrderEntriesRequestValidator.
 *
 */
public class JnjLatamOrderEntriesRequestValidator implements Validator
{
    private static final String ORDER_REQUEST_MATERIAL_NUMBER_INVALID =
            "The material {0} doesn't exist or invalid";
    private static final String ORDER_REQUEST_QUANTITY_POSITIVE_INTEGER =
            "Quantity should be a positive integer";
    private static final String ORDER_REQUEST_QUANTITY_FIELD_REQUIRED =
            "Quantity field is required";
    private static final int MIN_QUANTITY = 1;
    private static final String QUANTITY = "quantity";
    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final String MATERIAL_NUMBER = "materialNumber";
    private static final String ORDER_REQUEST_MATERIAL_NUMBER_INVALID_DEFAULT =
            "The Material Number is invalid for this field";
    private static final String ORDER_REQUEST_MATERIAL_NUMBER_REQUIRED =
            "Material Number field is required";
    private JnJLaProductService jnjLaProductService;


    @Override
    public boolean supports(final Class clazz)
    {
		 return JnjLatamOrderEntriesRequestWsDTO.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors)
    {

        final JnjLatamOrderEntriesRequestWsDTO request = (JnjLatamOrderEntriesRequestWsDTO) target;

        if (StringUtils.isEmpty(request.getMaterialNumber()))
        {
            errors.rejectValue(MATERIAL_NUMBER, ORDER_REQUEST_MATERIAL_NUMBER_REQUIRED);
        } else {
            try {
                jnjLaProductService.getProductForCatalogId(request.getMaterialNumber());
            } catch (BusinessException e)
            {
                errors.rejectValue(MATERIAL_NUMBER, ORDER_REQUEST_MATERIAL_NUMBER_INVALID_DEFAULT,
                        new String[] {request.getMaterialNumber()},
                        ORDER_REQUEST_MATERIAL_NUMBER_INVALID);
            }
        }

        if (request.getQuantity() == null)
        {
            errors.rejectValue(QUANTITY, ORDER_REQUEST_QUANTITY_FIELD_REQUIRED);
        } else if (request.getQuantity() < MIN_QUANTITY)
        {
            errors.rejectValue(QUANTITY, ORDER_REQUEST_QUANTITY_POSITIVE_INTEGER);
        }

    }

    public JnJLaProductService getJnjLaProductService()
    {
        return jnjLaProductService;
    }

    public void setJnjLaProductService(final JnJLaProductService jnjLaProductService)
    {
        this.jnjLaProductService = jnjLaProductService;
    }

}
