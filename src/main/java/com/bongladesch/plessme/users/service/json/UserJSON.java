package com.bongladesch.plessme.users.service.json;

/** UserJSON is a JSON representation of the "user" object to handle incomming userdata. */
public class UserJSON {
  private String email;
  private String password;
  private String firstName;
  private String lastName;

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
}
