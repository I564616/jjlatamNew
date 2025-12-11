/*
 * This code contains copyright information which is the proprietary property
 * of JNJ Companies Ltd. No part of this code may be reproduced, stored or
 * transmitted in any form without the prior written permission of JNJ Companies Ltd.
 * Copyright (C) JNJ Companies Ltd 2021
 * All rights reserved.
 */

package com.jnj.la.browseandsearch.controllers.cms;

import com.jnj.b2b.storefront.controllers.ControllerConstants;
import com.jnj.b2b.storefront.controllers.cms.ProductReferencesComponentController;
import com.jnj.la.browseandsearch.controllers.JnjlabrowseandsearchaddonControllerConstants;

import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import org.apache.commons.lang3.StringUtils;

public class JnjLatamProductReferencesComponentController extends ProductReferencesComponentController {

    @Override
    protected String getView(ProductReferencesComponentModel component) {
               return JnjlabrowseandsearchaddonControllerConstants.ADDON_PREFIX + ControllerConstants.Views.Cms.ComponentPrefix+StringUtils.lowerCase(getTypeCode(component));
    }
}
