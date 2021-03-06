package com.bongladesch.plessme.users.service;

import io.quarkus.oidc.UserInfo;

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

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.cache.NoCache;

import com.bongladesch.plessme.common.usecase.IGenerator;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;
import com.bongladesch.plessme.users.service.json.UserJSON;
import com.bongladesch.plessme.users.usecase.IIdentityProvider;
import com.bongladesch.plessme.users.usecase.IUserRepository;
import com.bongladesch.plessme.users.usecase.UCreateUser;
import com.bongladesch.plessme.users.usecase.UGetUser;
import com.bongladesch.plessme.users.usecase.UserAlreadyExistsException;
import com.bongladesch.plessme.users.usecase.UserValidationException;

/**
 * UsersAPI implements a REST API with JAX-RS to address the requests to the "users" component to
 * the related usecase implementations. This class also handles the exceptions thrown by the
 * usecases for invalid input.
 */
@Path("/users")
public class UserService {

  private UserInfo userInfo;
  private Logger logger;
  private IGenerator generator;
  private IUserRepository userRepository;
  private IIdentityProvider identityProvider;

  /**
   * Constructor for CDI.
   *
   * @param userInfo user data of identity
   * @param logger logger used by the service implementation
   * @param generator generator to create UUID etc.
   * @param userRepository user repository dependency
   * @param identityProvider identity provider dependency
   */
  @Inject
  public UserService(
      UserInfo userInfo,
      Logger logger,
      IGenerator generator,
      IUserRepository userRepository,
      IIdentityProvider identityProvider) {
    this.userInfo = userInfo;
    this.logger = logger;
    this.generator = generator;
    this.userRepository = userRepository;
    this.identityProvider = identityProvider;
  }

  /**
   * Rest API endpoint implementation for the createUser usecase.
   *
   * @param userJSON User JSON API object with input data
   * @return A valid HTTP reponse (codes: 200 + USER_ID = success, 400|403 = failure)
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @PermitAll
  @NoCache
  public Response createUser(UserJSON userJSON) {
    logger.debug("Received HTTP request to createUser with input:");
    logger.debug(userJSON);
    // Inject dependencies to the usecase on creation
    UCreateUser createUserAccount = new UCreateUser(generator, userRepository, identityProvider);
    // Transform input object to user object
    UserBuilder userBuilder = new UserBuilder();
    User user =
        userBuilder
            .email(userJSON.getEmail())
            .password(userJSON.getPassword())
            .firstName(userJSON.getFirstName())
            .lastName(userJSON.getLastName())
            .build();
    // Execute usecase and handle exceptions
    try {
      logger.debug("Create user object from request input");
      user = createUserAccount.create(user);
    } catch (UserAlreadyExistsException uaee) {
      logger.debug(uaee);
      return Response.status(403).build();
    } catch (UserValidationException uve) {
      logger.debug(uve);
      return Response.status(400).build();
    }
    return Response.status(201).entity("{\"id\":\"" + user.getId() + "\"}").build();
  }

  @GET
  @RolesAllowed("user")
  @Produces(MediaType.APPLICATION_JSON)
  @NoCache
  public Response getUser() {
    String id = userInfo.getString("plessmeid");
    logger.debug("Received HTTP request to getUser with input" + id);
    // Inject dependencies to the usecase on creation
    UGetUser getUser = new UGetUser(userRepository);
    // Search for user on repository
    User user = getUser.getUser(id);
    if (user == null) {
      logger.debug("User with ID " + id + " not found.");
      return Response.status(404).build();
    }
    return Response.ok(UserJSON.fromUser(user)).build();
  }
}
