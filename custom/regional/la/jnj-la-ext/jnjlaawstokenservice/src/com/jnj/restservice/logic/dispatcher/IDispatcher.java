/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.dispatcher;

import com.jnj.restservice.exception.RestServiceException;
import com.jnj.restservice.logic.Request;
import com.jnj.restservice.logic.Response;


/**
 * @author Ravindran George
 *
 * 
 */
public interface IDispatcher<T> {
    public Response<T> dispatch(Request restRequest) throws RestServiceException;
}
