/**
 * Copyright: Copyright © 2019
 * This file contains trade secrets of Johnson & Johnson. No part may be reproduced or transmitted in any
 * form by any means or for any purpose without the express written permission of Johnson & Johnson.
 */
package com.jnj.restservice.logic.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.jnj.restservice.logic.Response;


/**
 * To retrieve the token from the rest response
 */
public class AWSTokenHandler implements IResponseHandler {


    @Override
    public <T> void handle(final jakarta.ws.rs.core.Response rawResponse, Response<T> restResponse, final Class<T> genericType) {
        JsonNode responseNode = rawResponse.readEntity(JsonNode.class);

        JsonNode messageNode = responseNode.path("token");

        if(messageNode!=null && !(messageNode instanceof MissingNode) ) {
            String token =null;
            if(messageNode!=null  && !(messageNode instanceof MissingNode)){
                 token= messageNode.asText();
                restResponse.setToken(token);
            }

        }
    }



    @Override

    public <T> boolean canHandle(final jakarta.ws.rs.core.Response rawResponse,final Response<T> restResponse) {
         return (rawResponse.getStatus()== 201  );
    }

}
