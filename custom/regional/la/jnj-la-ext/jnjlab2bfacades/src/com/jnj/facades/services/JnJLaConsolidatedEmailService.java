/*
 * Copyright: Copyright © 2018
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.facades.services;

import com.jnj.core.model.JnjIntegrationRSACronJobModel;

import java.util.List;

public interface JnJLaConsolidatedEmailService {

    void processPendingEmails(final JnjIntegrationRSACronJobModel model, final String store);

    List<String> getExclusionOrderStatuses();

}