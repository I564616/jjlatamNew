/*
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.la.core.daos.impl;

import com.jnj.core.model.JnJInvoiceOrderModel;
import com.jnj.la.core.daos.JnjBrInvoiceDao;

@SuppressWarnings("unused")
public class DefaultJnjBrInvoiceDao extends DefaultJnjLaInvoiceDao implements JnjBrInvoiceDao {

    protected String getInvoiceNumberField(){
        return JnJInvoiceOrderModel.NFNUMBER;
    }

}