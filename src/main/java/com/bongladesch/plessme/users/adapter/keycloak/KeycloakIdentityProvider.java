package com.bongladesch.plessme.users.adapter.keycloak;

import java.util.Arrays;
import java.util.Collections;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.usecase.IIdentityProvider;

/**
 * KeycloakIdentityProvider is a Keycloak identity provider implementation of the plessme identity
 * provider interface.
 */
@ApplicationScoped
public class KeycloakIdentityProvider implements IIdentityProvider {

  /**
   * Keycloak API rest client implementation is injected by CDI. Class fields must be package
   * private due to implementation quarkus.
   */
  @Inject
  @ConfigProperty(name = "plessme.keycloak.url")
  String url;

  @Inject
  @ConfigProperty(name = "plessme.keycloak.realm")
  String realm;

  @Inject
  @ConfigProperty(name = "plessme.keycloak.client.id")
  String clientId;

  @Inject
  @ConfigProperty(name = "plessme.keycloak.client.secret")
  String clientSecret;

  @Inject ILogger logger;

  /**
   * Create an enabled user in Keycloak for OAuth2 implementation from a valid user entity. Realm
   * role "user" will be assigned to the created Keycloak user.
   *
   * @param user Valid user object to create a Keycloak user from.
   */
  @Override
  public boolean createUser(User user) {
    // Init Keycloak client
    logger.debug("Initiate Keycloak connection to URL " + url + " on realm " + realm);
    Keycloak keycloak =
        KeycloakBuilder.builder()
            .serverUrl(url)
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    // Define user data
    UserRepresentation userRepresentation = userToUserRepresentation(user);
    // Create user in Keycloak
    logger.debug("Create enabled user on Keycloak realm " + realm + " with ID " + user.getId());
    Response response = null;
    try {
      UsersResource usersResource = keycloak.realm(realm).users();
      response = usersResource.create(userRepresentation);
    } catch (javax.ws.rs.ProcessingException e) {
      return false;
    }
    // Add "user" role to new created user
    userRepresentation.setRealmRoles(Arrays.asList("user"));
    String userId = CreatedResponseUtil.getCreatedId(response);
    addUserRole(keycloak, userId);
    return true;
  }

  /**
   * Transforms an User object to UserRepresentation object including credentails.
   *
   * @param user User object to create UserRepresentation from.
   * @return Created UserRepresentation object.
   */
  private UserRepresentation userToUserRepresentation(User user) {
    // Create user representation object
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setEnabled(true);
    userRepresentation.setEmailVerified(false);
    userRepresentation.setEmail(user.getEmail());
    userRepresentation.setUsername(user.getEmail());
    userRepresentation.setFirstName(user.getFirstName());
    userRepresentation.setLastName(user.getLastName());
    userRepresentation.setAttributes(Collections.singletonMap("id", Arrays.asList(user.getId())));
    // Create password as credentials object
    CredentialRepresentation password = new CredentialRepresentation();
    password.setTemporary(false);
    password.setType(CredentialRepresentation.PASSWORD);
    password.setValue(user.getPassword());
    // Add credentials object to user representation
    userRepresentation.setCredentials(Arrays.asList(password));
    return userRepresentation;
  }

  /**
   * Add the realm role "user" to the user with provided ID.
   *
   * @param keycloak Keycloak client instance
   * @param userId ID of the user to add the role "user"
   */
  private void addUserRole(Keycloak keycloak, String userId) {
    RealmResource realmResource = keycloak.realm(realm);
    UsersResource usersRessource = realmResource.users();
    RoleRepresentation userRealmRole = realmResource.roles().get("user").toRepresentation();
    UserResource userResource = usersRessource.get(userId);
    userResource.roles().realmLevel().add(Arrays.asList(userRealmRole));
  }
}
