package com.bongladesch.plessme.users.adapter.keycloak;

/**
 * KeycloakCredentialRepresentation represents a password JSON API object for Keycloak users.
 */
public class KeycloakCredentialRepresentation {
    public String type;
    public String temporary;
    public String value;

    /**
     * Constructor to create a KeycloakCredentialRepresentation from a password string.
     * @param password incomming password to generate credentials from
     */
    public KeycloakCredentialRepresentation(String password) {
        this.type = "password";
        this.temporary = "false";
        this.value = password;
    }
}