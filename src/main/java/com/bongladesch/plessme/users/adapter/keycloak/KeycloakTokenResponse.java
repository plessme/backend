package com.bongladesch.plessme.users.adapter.keycloak;

/**
* KeycloakTokenResponse represents a OIDC token request response JSON API object from Keycloak API.
*/
public class KeycloakTokenResponse {
    public String access_token;
    public int expires_in;
    public int refresh_expires_in;
    public String refresh_token;
    public String token_type;
    public int not_before_policy;
    public String session_state;
    public String scope;
}
