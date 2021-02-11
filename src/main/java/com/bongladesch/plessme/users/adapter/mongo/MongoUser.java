package com.bongladesch.plessme.users.adapter.mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;

import io.quarkus.mongodb.panache.MongoEntity;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;

/** MongoUser is a user representation object for Mongo Panache extension. */
@MongoEntity(collection = "user")
public class MongoUser extends PanacheMongoEntityBase {

  @BsonId public String id;

  @BsonProperty("email")
  public String email;

  @BsonProperty("firstname")
  public String firstName;

  @BsonProperty("lastname")
  public String lastName;

  @BsonProperty("created")
  public Long created;

  /** Public default constructor for (de-)serialization. */
  public MongoUser() {}

  /**
   * Public constructor to create a MongoUser from an User object.
   *
   * @param user incomming user data
   */
  public MongoUser(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.created = user.getCreated();
  }

  /**
   * Transform this MongoUser object to an User object.
   *
   * @return user object from MongoUser
   */
  public User toUser() {
    UserBuilder builder =
        new UserBuilder()
            .id(this.id)
            .created(this.created)
            .email(this.email)
            .firstName(this.firstName)
            .lastName(this.lastName);
    return builder.build();
  }

  /**
   * Find an user in Mongo by e-mail.
   *
   * @param email e-mail of user
   * @return User as MongoUser object
   */
  public static MongoUser findByEmail(String email) {
    return find("email", email).firstResult();
  }
}
