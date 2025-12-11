/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.core.dao.dropshipment.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.model.JnjLaSoldToShipToSpecialCaseModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Autowired;

public class JnjLaSoldToShipToOrderTypeDAOImpl {
    private static final Class currentClass = JnjLaSoldToShipToOrderTypeDAOImpl.class;

    protected static final String FETCH_DROPSHIPMENT_DETAIL_BY_CUSTOMER_ADDRESS =
        "SELECT {pk} FROM {JnjLaSoldToShipToSpecialCase} WHERE {soldTo}=?soldTo AND {shipTo}=?shipTo";

    @Autowired
    protected FlexibleSearchService flexibleSearchService;

    public JnjLaSoldToShipToSpecialCaseModel getDropshipmentSpecialCase (final String soldTo, final String shipTo){
        final String methodName = "getDropshipmentSpecialCase()";

        JnjGTCoreUtil.logDebugMessage("Get dropshipment info", methodName,
            Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.BEGIN_OF_METHOD
                + Jnjb2bCoreConstants.Logging.HYPHEN + Jnjb2bCoreConstants.Logging.START_TIME
                + JnJCommonUtil.getCurrentDateTime(), currentClass);

        JnjLaSoldToShipToSpecialCaseModel result = null;
        try{
            final String query = FETCH_DROPSHIPMENT_DETAIL_BY_CUSTOMER_ADDRESS;
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameter("soldTo", soldTo);
            fQuery.addQueryParameter("shipTo", shipTo);
            result = flexibleSearchService.<JnjLaSoldToShipToSpecialCaseModel>searchUnique(fQuery);
        }catch (final ModelNotFoundException modelNotFound) {
            JnjGTCoreUtil.logErrorMessage("Get dropshipment", methodName,
                "No dropshipment detail for customer with sold to: " + soldTo + " and shipTo: " + shipTo, currentClass);
            JnjGTCoreUtil.logErrorMessage("Get dropshipment", methodName, "Error fetching Model ", modelNotFound, currentClass);
        }
        JnjGTCoreUtil.logInfoMessage("Get Dropshipment", methodName, Jnjb2bCoreConstants.Logging.END_OF_METHOD, currentClass);

        return result;
    }
}
