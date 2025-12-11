/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import com.jnj.restservice.logic.Response;

public interface IResponseHandler {
    public <T> void handle(jakarta.ws.rs.core.Response rawResponse, Response<T> restResponse, Class<T> genericType);
    public <T> boolean canHandle(jakarta.ws.rs.core.Response rawResponse, Response<T> restResponse);
}
