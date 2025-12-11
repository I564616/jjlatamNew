/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import com.jnj.restservice.logic.Response;

/**
 * Response handler for Token verification
 */
public class AWSTokenVerificationHandler implements IResponseHandler {

    @Override
    public <T> void handle(final jakarta.ws.rs.core.Response rawResponse, Response<T> restResponse, final Class<T> genericType) {
        if(rawResponse.getStatus()== 204 ){
        	restResponse.setValidToken(true);
        }
       
    }


    @Override
    public <T> boolean canHandle(final jakarta.ws.rs.core.Response rawResponse, final Response<T> restResponse) {
         return (rawResponse != null && rawResponse.getStatus()== 204 );
    }

}
