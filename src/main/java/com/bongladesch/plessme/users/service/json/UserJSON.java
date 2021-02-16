package com.bongladesch.plessme.users.service.json;

import com.bongladesch.plessme.users.entity.User;

/** UserJSON is a JSON representation of the "user" object to handle incomming userdata. */
public class UserJSON {
  private String id;
  private Long created;
  private String email;
  private String password;
  private String firstName;
  private String lastName;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getCreated() {
    return this.created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public static UserJSON fromUser(User user) {
    UserJSON userJSON = new UserJSON();
    userJSON.setId(user.getId());
    userJSON.setCreated(user.getCreated());
    userJSON.setEmail(user.getEmail());
    userJSON.setPassword(user.getPassword());
    userJSON.setFirstName(user.getFirstName());
    userJSON.setLastName(user.getLastName());
    return userJSON;
  }
}
