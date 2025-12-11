/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services;

import com.jnj.la.core.enums.JnJEmailPeriodicity;
import de.hybris.platform.core.model.order.OrderModel;

public interface JnjLAOrderEmailMatrixService {

    boolean mustSendEmail(final OrderModel order, final JnJEmailPeriodicity periodicity);

}