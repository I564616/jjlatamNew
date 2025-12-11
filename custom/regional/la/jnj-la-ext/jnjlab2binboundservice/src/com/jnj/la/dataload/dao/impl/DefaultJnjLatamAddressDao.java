/*
 * Copyright: Copyright ©  2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.dataload.dao.impl;

import com.jnj.core.constants.Jnjb2bCoreConstants;
import com.jnj.core.dao.impl.DefaultJnJAddressDao;
import com.jnj.core.util.JnJCommonUtil;
import com.jnj.core.util.JnjGTCoreUtil;
import com.jnj.la.core.constants.Jnjlab2bcoreConstants;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultJnjLatamAddressDao extends DefaultJnJAddressDao {

    public static final Class currentClass = DefaultJnjLatamAddressDao.class;

    @Override
    public List<AddressModel> getAddressByIdandOnwerType(final String jnjId, final boolean ignoreleadingZeros,
                                                         final boolean checkActiveFlag){
        final String methodName = "getAddressByIdandOnwerType()";
        final List<AddressModel> addressList = new ArrayList<>();

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPSERT_CUSTOMER_NAME, methodName + Jnjlab2bcoreConstants.Logging.BEGIN_OF_METHOD,
            JnJCommonUtil.getCurrentDateTime(), currentClass);

        final String addressQueryLeadingZeros = "select {pk} from {address} where {owner} in ({{ select {unit:pk} from "
            + "{jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner}}"
            + "where REGEXP_LIKE({add:jnjaddressId},'^[0]*"
            + jnjId
            + "$')}}) and REGEXP_LIKE({JNJADDRESSID},'^[0]*" + jnjId + "$')";

        final String addressQuery = "select {pk} from {address} where {owner} in ({{ select {unit:pk} from {jnjb2bunit as unit  join address as add on {unit:pk} = {add:owner}}  "
            + "where {add:jnjaddressId}=?jnjAddressId}}) and {jnjaddressId}=?jnjAddressId";

        try{
            String query;
            ServicesUtil.validateParameterNotNull(jnjId, "Id must not be null");
            final Map queryParams = new HashMap();
            if (ignoreleadingZeros){
                query = addressQueryLeadingZeros;
            }else{
                query = addressQuery;
                queryParams.put("jnjAddressId", jnjId);
            }
            if (checkActiveFlag){
                query = query + " AND {active}=?activeFlag";
                queryParams.put("activeFlag", "1");
            }
            final FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
            fQuery.addQueryParameters(queryParams);

            JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPSERT_CUSTOMER_NAME, methodName +
                Jnjb2bCoreConstants.Logging.HYPHEN + "Get addresses query: " + fQuery, JnJCommonUtil.getCurrentDateTime(), currentClass);

            final List<AddressModel> results = getSessionService().executeInLocalView(new SessionExecutionBody(){
                @Override
                public List<AddressModel> execute(){
                    return getFlexibleSearchService().<AddressModel> search(fQuery).getResult();
                }
            }, getUserService().getAdminUser());
            if (!results.isEmpty()){
                for (final AddressModel address : results){
                    addressList.add(address);
                }
            }
        }catch (final ModelNotFoundException modelNotFound){
            LOGGER.error(Jnjb2bCoreConstants.Logging.UPSERT_CUSTOMER_NAME + " - " + methodName + "model not found for given key -" + jnjId
                + JnJCommonUtil.getCurrentDateTime());
        }

        JnjGTCoreUtil.logDebugMessage(Jnjlab2bcoreConstants.Logging.UPSERT_CUSTOMER_NAME, methodName +
            Jnjb2bCoreConstants.Logging.END_OF_METHOD, JnJCommonUtil.getCurrentDateTime(), currentClass);

        return addressList;
    }
}