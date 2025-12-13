package com.jnj.gt.core.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;

public class OAuthClientChecker {

    private String clientPrefix = "CLIENT_";

    public boolean check(Authentication authentication, String requiredClient) {

        // Authentication must be JWT-based
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            return false; // equivalent to voter abstain → not granted
        }

        Jwt jwt = jwtAuth.getToken();

        // Try to extract clientId from common claim names
        String clientId = getClientIdFromJwt(jwt);

        if (clientId == null) {
            return false;
        }

        String expected = clientPrefix + clientId.toUpperCase();

        return expected.equalsIgnoreCase(requiredClient);
    }

    private String getClientIdFromJwt(Jwt jwt) {
        // Most common claim name
        if (jwt.hasClaim("client_id")) {
            return jwt.getClaimAsString("client_id");
        }

        // Sometimes Spring Authorization Server uses "cid"
        if (jwt.hasClaim("cid")) {
            return jwt.getClaimAsString("cid");
        }

        // Sometimes OAuth2 providers use "azp"
        if (jwt.hasClaim("azp")) {
            return jwt.getClaimAsString("azp");
        }

        return null;
    }

    public String getClientPrefix() {
        return clientPrefix;
    }

    public void setClientPrefix(String clientPrefix) {
        this.clientPrefix = clientPrefix;
    }
}
