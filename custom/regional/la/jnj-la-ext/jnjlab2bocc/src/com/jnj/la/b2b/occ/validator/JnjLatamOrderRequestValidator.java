/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2023
 * All rights reserved.
 */
package com.jnj.la.b2b.occ.validator;

import com.jnj.core.service.vtex.order.JnjLatamVtexOrderService;
import de.hybris.platform.b2b.company.B2BCommerceUnitService;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderEntriesRequestWsDTO;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrderRequestWsDTO;

import jakarta.annotation.Resource;

/**
 * The Class JnjLatamOrderRequestValidator.
 *
 */
public class JnjLatamOrderRequestValidator implements Validator
{

    protected static final String GIVEN_ORDER_TYPE_IS_INVALID_OR_NOT_ALLOWED =
            "Given Order Type is invalid or not allowed";
    private static final String ORDER_REQUEST_PURCHASE_ORDER_NUMBER_REQUIRED =
            "Purchase Order Number should be present to proceed";
    private static final String PURCHASE_ORDER_NUMBER = "purchaseOrderNumber";
    private static final String FIELD_WITH_NAME_REQUIRED = "field.withName.required";
    private static final String DEFAULT_MESSAGE_FIELD_IS_REQUIRED = "Field {0} is required";
    protected static final String SPINE_SALES_REP_UCN = "spineSalesRepUCN";
    protected static final String REASON_CODE = "reasonCode";
    protected static final String ORDER_TYPE = "orderType";
    private static final String SOLD_TO_ACCOUNT = "soldToAccount";
    protected static final String ENTRIES = "entries";
    private static final String ORDER_REQUEST_ENTRIES_REQUIRED =
            "At least one entry should be present to proceed";
    private static final String ORDER_REQUEST_SOLDTO_REQUIRED =
            "Sold To Account should be present to proceed";
    protected static final String ORDER_REQUEST_ORDER_TYPE_REQUIRED =
            "Order Type should be present to proceed";
    protected static final String ORDER_REQUEST_ZNC_REASON_CODE_REQUIRED =
            "Reason Code is required to process ZNC typed orders";
    private static final String ORDER_REQUEST_SOLDTO_INVALID = "Sold To Account is not valid";
    protected static final String ORDER_REQUEST_HETROGENOUS_DIVISIONS =
            "There are Heterogenous Divisions available for the Products";
    protected static final String ORDER_REQUEST_SALES_REP_UCN_REQUIRED =
            "Sales Rep UCN should be present to proceed";
    protected static final String DATE_FORMAT = "MM/dd/yyyy";
    protected static final String REGEX = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
    protected static final String REQUESTED_DELIVERY_DATE_INVALID_FORMAT =
            "Date should be in MM/dd/yyyy format";
	private static final String VTEX_CART_REFERENCE_NUMBER = "cartReferenceNumber";
    private static final String NOT_POSSIBLE_TO_CREATE_WITH_THIS_CARTREFERENCENUMBER = "Not possible to create more than 1 order with the same cartReferenceNumber";

    protected static final String INVALID_ORDER_TYPE =
            "Invalid order type";
    public static final String API_ALLOWED_ORDER_TYPES = "api.allowed.order.types";

    private B2BCommerceUnitService b2BCommerceUnitService;

    private UserService userService;

	protected Validator jnjLatamOrderEntriesRequestValidator;

    private ConfigurationService configurationService;

    @Resource(name = "jnjLatamVtexOrderService")
    private JnjLatamVtexOrderService jnjLatamVtexOrderService;
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
         validateEntries(errors, request);
    }


	 /**
	  * @param errors
	  *           the errors to be used
	  * @param request
	  *           the request to be used
	  */
	 protected void validateCartReferenceNumber(final Errors errors, final JnjLatamOrderRequestWsDTO request)
	 {
		 if (StringUtils.isEmpty(request.getCartReferenceNumber()))
		 {
			 errors.rejectValue(VTEX_CART_REFERENCE_NUMBER, FIELD_WITH_NAME_REQUIRED, new String[]
			 { VTEX_CART_REFERENCE_NUMBER }, DEFAULT_MESSAGE_FIELD_IS_REQUIRED);
		 } else if(jnjLatamVtexOrderService.getOrdersWithCartRefNum(request.getCartReferenceNumber()).isPresent())
         {
             errors.rejectValue(VTEX_CART_REFERENCE_NUMBER,NOT_POSSIBLE_TO_CREATE_WITH_THIS_CARTREFERENCENUMBER);
         }
	 }



    /**
     * @param errors the errors to be used
     * @param request the request to be used
     */
    protected void validateSoldToAccount(final Errors errors,
			 final JnjLatamOrderRequestWsDTO request)
	 {
        if (StringUtils.isEmpty(request.getSoldToAccount())) {
            errors.rejectValue(SOLD_TO_ACCOUNT, ORDER_REQUEST_SOLDTO_REQUIRED);
        } else {
            if (getB2BUnitModelForAccount(request.getSoldToAccount()) == null) {
                errors.rejectValue(SOLD_TO_ACCOUNT, ORDER_REQUEST_SOLDTO_INVALID);
            }
        }
    }

    /**
     * @param account the account to be used
     * @return B2BUnitModel
     */
    protected B2BUnitModel getB2BUnitModelForAccount(final String account)
    {
        return (b2BCommerceUnitService.getUnitForUid(account));
    }

    /**
     * @param errors the errors to be used
     * @param request the request to be used
     */
	 protected void validatePONumber(final Errors errors, final JnjLatamOrderRequestWsDTO request)
	 {
        final String purchaseOrderNumber = StringUtils.trim(request.getPurchaseOrderNumber());
        if (StringUtils.isEmpty(purchaseOrderNumber))
        {
            errors.rejectValue(PURCHASE_ORDER_NUMBER, ORDER_REQUEST_PURCHASE_ORDER_NUMBER_REQUIRED);
        }
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

    /**
     * @param errors the errors to be used
     * @param request the request to be used
     */
    protected void invokeOrderEntryValidator(final Errors errors,
			 final JnjLatamOrderRequestWsDTO request)
	 {
        int counter = 0;
		  for (final JnjLatamOrderEntriesRequestWsDTO entry : request.getEntries())
		  {
            errors.pushNestedPath("entries[" + counter + "]");
				ValidationUtils.invokeValidator(this.jnjLatamOrderEntriesRequestValidator, entry, errors);
            counter++;
            errors.popNestedPath();
        }
    }

	 protected B2BCommerceUnitService getB2BCommerceUnitService()
	 {
		 return b2BCommerceUnitService;
	 }

	 public void setB2BCommerceUnitService(final B2BCommerceUnitService b2bCommerceUnitService)
	 {
		 b2BCommerceUnitService = b2bCommerceUnitService;
	 }

	 protected UserService getUserService()
	 {
		 return userService;
	 }

	 public void setUserService(final UserService userService)
	 {
		 this.userService = userService;
	 }

	 protected Validator getJnjLatamOrderEntriesRequestValidator()
	 {
		 return jnjLatamOrderEntriesRequestValidator;
	 }

	 public void setJnjLatamOrderEntriesRequestValidator(final Validator jnjLatamOrderEntriesRequestValidator)
	 {
		 this.jnjLatamOrderEntriesRequestValidator = jnjLatamOrderEntriesRequestValidator;
	 }

	 protected ConfigurationService getConfigurationService()
	 {
		 return configurationService;
	 }
	 public void setConfigurationService(final ConfigurationService configurationService)
	 {
		 this.configurationService = configurationService;
	 }

}
