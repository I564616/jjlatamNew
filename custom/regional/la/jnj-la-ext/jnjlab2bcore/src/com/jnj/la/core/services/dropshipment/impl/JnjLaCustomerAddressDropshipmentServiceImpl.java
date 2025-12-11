/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.core.services.dropshipment.impl;

import com.jnj.la.core.dao.dropshipment.impl.JnjLaSoldToShipToOrderTypeDAOImpl;
import com.jnj.la.core.model.JnjLaSoldToShipToSpecialCaseModel;
import com.jnj.la.core.services.dropshipment.JnjLaCustomerAddressDropshipmentService;
import org.springframework.beans.factory.annotation.Autowired;

public class JnjLaCustomerAddressDropshipmentServiceImpl implements JnjLaCustomerAddressDropshipmentService {

    @Autowired
    protected JnjLaSoldToShipToOrderTypeDAOImpl jnjLaSoldToShipToOrderTypeDAO;

    @Override
    public JnjLaSoldToShipToSpecialCaseModel getDropshipmentDetailsSpecialCase(final String soldTo, final String shipTo) {
        return jnjLaSoldToShipToOrderTypeDAO.getDropshipmentSpecialCase(soldTo, shipTo);
    }

}
