package com.bongladesch.plessme.users.adapter.keycloak;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.usecase.IIdentityProvider;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 * KeycloakIdentityProvider is a Keycloak identity provider
 * implementation of the plessme identity provider interface.
 */
@ApplicationScoped
public class KeycloakIdentityProvider implements IIdentityProvider {

    /**
     * Keycloak API rest client implementation is injected by CDI.
     * Class fields must be package private due to implementation quarkus.
     */
    @Inject
    @RestClient
    KeycloakRestClient keycloakRestClient;

    @Inject
    @ConfigProperty(name = "plessme.keycloak.realm")
    String realm;

    @Inject
    @ConfigProperty(name = "plessme.keycloak.client.id") 
    String clientId;

    @Inject
    @ConfigProperty(name = "plessme.keycloak.client.secret")
    String clientSecret;

    @Inject
    ILogger logger;

    /**
     * Create an enabled user in Keycloak for OAuth2 implementation by given user data.
     * @param user User object to create a Keycloak user from.
     */
    @Override
    public void createUser(User user) {
        logger.debug("Request OIDC access token from Keycloak to create user with email: " + user.getEmail());
        KeycloakTokenResponse tokenResponse = keycloakRestClient.getToken(realm, "client_credentials", clientId, clientSecret);
        logger.debug("Create user from user data in Keycloak");
        keycloakRestClient.createUser(realm, "Bearer " + tokenResponse.access_token, new KeycloakUserRepresentation(user));
    }
}