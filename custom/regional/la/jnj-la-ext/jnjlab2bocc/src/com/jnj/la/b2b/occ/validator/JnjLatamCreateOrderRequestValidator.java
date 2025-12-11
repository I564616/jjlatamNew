/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */

package com.jnj.la.b2b.occ.validator;


import java.util.List;

import jakarta.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.jnj.core.model.JnJB2BUnitModel;
import com.jnj.core.services.b2bunit.JnjGTB2BUnitService;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.facades.vtex.order.JnjLatamVtexOrderFacade;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderRequestWsDTO;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;

import de.hybris.platform.core.model.user.AddressModel;



/**
 * The Class JnjLatamCreateOrderRequestValidator.
 *
 */
public class JnjLatamCreateOrderRequestValidator extends JnjLatamOrderRequestValidator
        implements Validator
{

	private static final Logger LOG = LoggerFactory.getLogger(JnjLatamCreateOrderRequestValidator.class);
    private static final String SHIP_TO_ACCOUNT = "shipToAccount";

    private static final String ORDER_REQUEST_SHIPTO_INVALID = "Ship To Account is not valid";

    private static final String ORDER_REQUEST_ENTRIES_REQUIRED =
			 "Entries is required to proceed";
    
    private static final String ORDER_REQUEST_SHIPTO_REQUIRED =
            "Ship To Account should be present to proceed";
    
    @Resource(name = "jnjB2BUnitService")
    private JnjGTB2BUnitService jnjGTB2BUnitService;
    
	@Resource(name = "jnjLatamVtexOrderFacade")
	private JnjLatamVtexOrderFacade jnjLatamVtexOrderFacade;

    
    @Override
    public boolean supports(final Class clazz)
    {
		 return JnjLatamOrderRequestWsDTO.class.equals(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors)
    {
		 final JnjLatamOrderRequestWsDTO request = (JnjLatamOrderRequestWsDTO) target;
         validateCartReferenceNumber(errors, request);
         validatePONumber(errors, request);
         validateSoldToAccount(errors, request);
         validateShipToAccount(errors, request);
         validateOrderType(errors, request);
         validateEntries(errors, request);
    }


    /**
     * This method is used to validate the Order Types
     *
     * @param errors the errors to be used
     * @param request the request to be used
     */
    private void validateOrderType(final Errors errors,
			 final JnjLatamOrderRequestWsDTO request)
	 {
        final String orderType = StringUtils.trim(request.getOrderType());
        if (StringUtils.isEmpty(orderType))
        {
            errors.rejectValue(ORDER_TYPE, ORDER_REQUEST_ORDER_TYPE_REQUIRED);
        }else
        {
            List<String> orderTypeList = JnJCommonUtil.getValues(API_ALLOWED_ORDER_TYPES, Jnjlab2bcoreConstants.CONST_COMMA);
            if(!orderTypeList.contains(request.getOrderType()))
            {
                errors.rejectValue(ORDER_TYPE, INVALID_ORDER_TYPE);
            }
        }
    }

    /**
     * This method is used to validate the Bill To/Ship To Accounts
     *
     * @param errors the errors to be used
     * @param request the request to be used
     */
    private void validateShipToAccount(final Errors errors,
			 final JnjLatamOrderRequestWsDTO request)
	 {
    	 if (StringUtils.isEmpty(request.getShipToAccount())) {
             errors.rejectValue(SHIP_TO_ACCOUNT, ORDER_REQUEST_SHIPTO_REQUIRED);
         }else {
        	final JnJB2BUnitModel soldToAccount = jnjLatamVtexOrderFacade.getB2bUnitForAccountId(request.getSoldToAccount());
	    	if (!isValidShipTo(soldToAccount,request.getShipToAccount()))
	        {
	            errors.rejectValue(SHIP_TO_ACCOUNT, ORDER_REQUEST_SHIPTO_INVALID);
	        }
         }
	 }
     
   
    /**
     * Checks if is valid ship to.
     *
     * @param soldToAccount the sold to account
     * @param shipToAccount the ship to account
     * @return true, if is valid ship to
     */
    private boolean isValidShipTo(final JnJB2BUnitModel soldToAccount, final String shipToAccount) {
      final List<AddressModel> shippingAddresses = jnjGTB2BUnitService.getShippingAddresses(soldToAccount);
      return shippingAddresses.stream().map(AddressModel::getJnJAddressId).anyMatch(p -> StringUtils.equalsIgnoreCase(p, shipToAccount));
    }
    
    
    /**
     * @param errors the errors to be used
     * @param request the request to be used
     */
	 private void validateEntries(final Errors errors, final JnjLatamOrderRequestWsDTO request)
	 {
        if (CollectionUtils.isEmpty(request.getEntries())) {
            errors.rejectValue(ENTRIES, ORDER_REQUEST_ENTRIES_REQUIRED);
        } else {
            try {
                if (CollectionUtils.isNotEmpty(request.getEntries())) {
                    invokeOrderEntryValidator(errors, request);
                }
            } finally {
                //
            }
        }
    }
    
}  
