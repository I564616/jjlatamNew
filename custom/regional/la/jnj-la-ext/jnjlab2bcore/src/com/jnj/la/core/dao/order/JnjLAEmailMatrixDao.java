/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.dao.order;

import de.hybris.platform.basecommerce.enums.OrderEntryStatus;

public interface JnjLAEmailMatrixDao {

    boolean sendEmailByStatuses(OrderEntryStatus previous, OrderEntryStatus current);

}