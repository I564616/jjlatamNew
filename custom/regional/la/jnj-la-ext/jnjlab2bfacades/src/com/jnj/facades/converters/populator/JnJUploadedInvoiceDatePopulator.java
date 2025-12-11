/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.converters.populator;

import com.jnj.core.model.JnJB2bCustomerModel;
import com.jnj.la.core.data.JnJUploadedInvoiceDateData;
import com.jnj.la.core.model.JnJUploadedInvoiceDateModel;
import de.hybris.platform.acceleratorservices.payment.cybersource.converters.populators.response.AbstractResultPopulator;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class JnJUploadedInvoiceDatePopulator extends AbstractResultPopulator<JnJUploadedInvoiceDateModel, JnJUploadedInvoiceDateData> {
    @Override
    public void populate(final JnJUploadedInvoiceDateModel model, final JnJUploadedInvoiceDateData data) throws ConversionException {
        data.setCreationDate(model.getCreationtime());
        data.setCurrentStatus(model.getCurrentStatus());
        data.setErased(model.getErased());
        data.setErrorMessage(model.getErrorMessage());
        data.setFilename(model.getFilename());
        data.setUser(getCustomerData(model.getUser()));
        data.setId(model.getPk().toString());
    }

    private static CustomerData getCustomerData(final JnJB2bCustomerModel userModel) {
        final CustomerData user = new CustomerData();
        user.setName(userModel.getName());
        user.setUid(userModel.getUid());
        return user;
    }
}
