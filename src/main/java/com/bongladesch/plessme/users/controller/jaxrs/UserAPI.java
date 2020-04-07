package com.bongladesch.plessme.users.controller.jaxrs;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.common.usecase.ILogger;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;
import com.bongladesch.plessme.users.usecase.IIdentityProvider;
import com.bongladesch.plessme.users.usecase.IMessageSender;
import com.bongladesch.plessme.users.usecase.IUserRepository;
import com.bongladesch.plessme.users.usecase.UCreateUser;
import com.bongladesch.plessme.users.usecase.UserAlreadyExistsException;
import com.bongladesch.plessme.users.usecase.UserValidationException;

import org.jboss.resteasy.annotations.cache.NoCache;

import io.quarkus.security.identity.SecurityIdentity;

/**
 * UsersAPI implements a REST API with JAX-RS to address
 * the request to the "users" component to the related
 * usecase implementations. This class also handles the
 * exceptions thrown by the usecases for invalid input.
 */
@Path("/users")
public class UserAPI {

    private SecurityIdentity identity;
    private ILogger logger;
    private IGenerator generator;
    private IUserRepository userRepository;
    private IIdentityProvider identityProvider;
    private IMessageSender messageSender;

    /**
     * Constructor for CDI.
     * @param identity request identity
     * @param logger logger used by the API and usecases
     * @param generator generator to create UUID etc.
     * @param userRepository user repository dependency
     * @param identityProvider identity provider dependency
     * @param messageSender messaging dependency
     */
    @Inject
    public UserAPI(
        SecurityIdentity identity,
        ILogger logger,
        IGenerator generator,
        IUserRepository userRepository,
        IIdentityProvider identityProvider,
        IMessageSender messageSender) {
        this.identity = identity;
        this.logger = logger;
        this.generator = generator;
        this.userRepository = userRepository;
        this.identityProvider = identityProvider;
        this.messageSender = messageSender;
    }

    /**
     * Rest API endpoint implementation for the createUser usecase.
     * @param userJSON User JSON API object with input data
     * @return A valid HTTP reponse (codes: 200 + USER_ID = success, 400|403 = failure)
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @NoCache
    public Response createUser(UserJSON userJSON) {
        // Inject dependencies to the usecase on creation
        UCreateUser createUserAccount = new UCreateUser(
            logger,
            generator,
            userRepository,
            identityProvider,
            messageSender
        );
        // Transform input object to user object
        UserBuilder userBuilder = new UserBuilder();
        User user = userBuilder.
            email(userJSON.email).
            password(userJSON.password).
            firstName(userJSON.firstName).
            lastName(userJSON.lastName).
            build();
        // Execute usecase and handle exceptions
        try {
            user = createUserAccount.create(user);
        } catch(UserAlreadyExistsException | UserValidationException e) {
            if(e instanceof UserAlreadyExistsException) {
                return Response.status(403).build();
            } else if (e instanceof UserValidationException) {
                return Response.status(400).build();
            }
        }
        return Response.ok("{\"id\":\"" + user.getId() + "\"}").build();
    }

    @GET
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response hello() {
        //Principal principal = identity.getPrincipal();
        // if(principal instanceof KeycloakPrincipal) {
        //     logger.info("Before cast");
        //     KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) identity.getPrincipal();
        //     logger.info("Keycloak");
        // } else {
        //     logger.info("Not Keycloak");
        // }
        //String idToken = principal.getKeycloakSecurityContext().getIdTokenString();
        //logger.info("Keycloak ID: " + idToken);
        logger.info("Attributes size: " + identity.getAttributes().size());
        return Response.ok("{\"hello\":\"" + identity.getPrincipal().getName() + "\"}").build();
    }
}