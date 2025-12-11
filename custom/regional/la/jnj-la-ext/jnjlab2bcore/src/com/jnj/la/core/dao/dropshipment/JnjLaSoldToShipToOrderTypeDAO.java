/**
 * Copyright: Copyright © 2017
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 *
 * @author aadrian2
 */

package com.jnj.la.core.dao.dropshipment;

import com.jnj.la.core.model.JnjLaSoldToShipToSpecialCaseModel;

public interface JnjLaSoldToShipToOrderTypeDAO {
    public JnjLaSoldToShipToSpecialCaseModel getDropshipmentSpecialCase (final String soldTo, final String shipTo);
}
