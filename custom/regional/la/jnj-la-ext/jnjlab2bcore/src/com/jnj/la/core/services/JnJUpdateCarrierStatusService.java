/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.la.core.services;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

public interface JnJUpdateCarrierStatusService {

    void processPendingOrderEntries(final JnjIntegrationRSACronJobModel model);

}
