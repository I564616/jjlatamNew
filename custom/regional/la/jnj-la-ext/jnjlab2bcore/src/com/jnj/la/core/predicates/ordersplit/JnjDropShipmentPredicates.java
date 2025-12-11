/*
 * Copyright: Copyright ©  2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */

package com.jnj.la.core.predicates.ordersplit;

import com.jnj.core.model.JnjDropShipmentDetailsModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import java.util.function.Predicate;

public class JnjDropShipmentPredicates {

    public static Predicate<JnjDropShipmentDetailsModel> sameSoldToAndShipTo(String soldTo, String shipTo) {
        return drop -> drop.getSoldTo() != null && drop.getSoldTo().equals(soldTo) && drop.getShipTo() != null
            && drop.getShipTo().equals(shipTo);
    }

    public static Predicate<JnjDropShipmentDetailsModel> sameSoldToOnlyAndEmptyShipTo(final String soldTo) {
        return drop -> (drop.getSoldTo() != null && drop.getSoldTo().equals(soldTo))
            && (null == drop.getShipTo() || drop.getShipTo().equals(Strings.EMPTY));
    }

    public static Predicate<JnjDropShipmentDetailsModel> sameShipToOnly(final String shipTo) {
        return drop -> drop.getShipTo() != null && drop.getShipTo().equals(shipTo);
    }

    public static Predicate<JnjDropShipmentDetailsModel> emptySoldTo() {
        return drop -> drop.getSoldTo() == null || drop.getSoldTo().equals(Strings.EMPTY);
    }

    public static Predicate<JnjDropShipmentDetailsModel> sameIsoCode(final String isoCode){
        return drop -> drop.getSalesOrganization() != null && drop.getSalesOrganization().startsWith(isoCode);
    }

    public static Predicate<JnjDropShipmentDetailsModel> sameDestinationCountry(final String destinationCountry){
        return drop -> StringUtils.isNotEmpty(drop.getDestinationCountry()) && drop.getDestinationCountry().equals(destinationCountry);
    }

    public static Predicate<JnjDropShipmentDetailsModel> sameMaterialIdAndEmptyDestinationCountry(final String destinationCountry, final String materialId){
        return drop -> StringUtils.isEmpty(drop.getDestinationCountry()) && drop.getMaterialId().equals(materialId);
    }

}
