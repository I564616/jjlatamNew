package com.jnj.la.b2b.occ.validator;

import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrdersListWsDTO;
import com.jnj.la.b2b.webservicescommons.dto.JnjLatamOrdersWsDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class JnjLatamOrderStatusRequestValidator implements Validator
{
    protected static final String ORDERSLIST = "ordersList";
    protected static final String CODE = "code";
    private static final String ORDER_STATUS_LIST_REQUIRED =
            "OrdersList is required to proceed";
    private static final String CODE_REQUIRED =
            "Code field is required";

    @Override
    public void validate(final Object target, final Errors errors)
    {
        JnjLatamOrdersListWsDTO request = (JnjLatamOrdersListWsDTO) target;
        if (CollectionUtils.isEmpty(request.getOrdersList()))
        {
            errors.rejectValue(ORDERSLIST, ORDER_STATUS_LIST_REQUIRED);
        }else if (CollectionUtils.isNotEmpty(request.getOrdersList())) {
                validateOrderCode(errors, request);
            }
    }

    /**
     *
     * @param errors the error to be used
     * @param request the request to be used
     */
    protected void validateOrderCode(final Errors errors,
                                             final JnjLatamOrdersListWsDTO request)
    {
        int counter = 0;
        for (JnjLatamOrdersWsDTO entry : request.getOrdersList())
        {
            errors.pushNestedPath("ordersList[" + counter + "]");
            if (StringUtils.isBlank(entry.getCode()))
            {
                errors.rejectValue(CODE,CODE_REQUIRED);
            }
            counter++;
            errors.popNestedPath();
        }
    }

    @Override
    public boolean supports(final Class clazz)
    {
        return JnjLatamOrdersListWsDTO.class.equals(clazz);
    }

}
