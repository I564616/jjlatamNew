/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.la.b2b.occ.controllers;

import de.hybris.platform.commerceservices.search.pagedata.PaginationData;
import de.hybris.platform.commercewebservicescommons.dto.search.pagedata.PaginationWsDTO;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.webservicescommons.dto.error.ErrorWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetLevelHelper;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.collect.Lists;

@Controller
public class JnjLatamB2BBaseController {

    protected static final String DEFAULT_PAGE_SIZE = "20";
    protected static final String DEFAULT_CURRENT_PAGE = "0";
    protected static final String DEFAULT_FIELD_SET = FieldSetLevelHelper.DEFAULT_LEVEL;
    protected static final String HEADER_TOTAL_COUNT = "X-Total-Count";
    protected static final String API_COMPATIBILITY_B2B_CHANNELS = "api.compatibility.b2b.channels";
    private static final Logger LOG = Logger.getLogger(JnjLatamB2BBaseController.class);
    @Resource(name = "dataMapper")
    private DataMapper dataMapper;

    protected static String logParam(final String paramName, final long paramValue)
    {
        return paramName + " = " + paramValue;
    }

    protected static String logParam(final String paramName, final Long paramValue)
    {
        return paramName + " = " + paramValue;
    }

    protected static String logParam(final String paramName, final String paramValue)
    {
        return paramName + " = " + logValue(paramValue);
    }

    protected static String logValue(final String paramValue)
    {
        return "'" + sanitize(paramValue) + "'";
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(
            { ModelNotFoundException.class })
    public ErrorListWsDTO handleModelNotFoundException(final Exception ex)
    {
        LOG.info("Handling Exception for this request - " + ex.getClass().getSimpleName() + " - " + sanitize(ex.getMessage()));
        if (LOG.isDebugEnabled())
        {
            LOG.debug(ex);
        }

        return handleErrorInternal(UnknownIdentifierException.class.getSimpleName(), ex.getMessage());
    }

    protected ErrorListWsDTO handleErrorInternal(final String type, final String message)
    {
        final ErrorListWsDTO errorListDto = new ErrorListWsDTO();
        final ErrorWsDTO error = new ErrorWsDTO();
        error.setType(type.replace("Exception", "Error"));
        error.setMessage(sanitize(message));
        errorListDto.setErrors(Lists.newArrayList(error));
        return errorListDto;
    }

    protected void validate(final Object object, final String objectName, final Validator validator)
    {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        if (errors.hasErrors())
        {
            throw new WebserviceValidationException(errors);
        }
    }

    /**
     * Adds pagination field to the 'fields' parameter
     *
     * @param fields
     * @return fields with pagination
     */
    protected String addPaginationField(final String fields)
    {
        String fieldsWithPagination = fields;

        if (StringUtils.isNotBlank(fieldsWithPagination))
        {
            fieldsWithPagination += ",";
        }
        fieldsWithPagination += "pagination";

        return fieldsWithPagination;
    }

    protected void setTotalCountHeader(final HttpServletResponse response, final PaginationWsDTO paginationDto)
    {
        if (paginationDto != null && paginationDto.getTotalResults() != null)
        {
            response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(paginationDto.getTotalResults()));
        }
    }

    protected void setTotalCountHeader(final HttpServletResponse response, final PaginationData paginationDto)
    {
        if (paginationDto != null)
        {
            response.setHeader(HEADER_TOTAL_COUNT, String.valueOf(paginationDto.getTotalNumberOfResults()));
        }
    }

    protected static String sanitize(final String input)
    {
        return YSanitizer.sanitize(input);
    }

    protected DataMapper getDataMapper()
    {
        return dataMapper;
    }

    protected void setDataMapper(final DataMapper dataMapper)
    {
        this.dataMapper = dataMapper;
    }
}
