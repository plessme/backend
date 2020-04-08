package com.bongladesch.plessme.users.entity;

/**
* This class represents a user account of the PlessMe system. A user account owns a whole
* collection of documents. He is able to handle these documents by uploading or downloading after
* scrolling through all documents.
*/
public class User {
    private String id;
    private Long created;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    /**
    * Default constructor to create a new user by his name.
    *
    * @param email Email of the user account
    * @param password Password of the user account
    * @param firstName FirstName of the user account
    * @param lastName LastName of the user account
    */
    private User(
            final String id,
            final Long created,
            final String email,
            final String password,
            final String firstName,
            final String lastName) {
        this.id = id;
        this.created = created;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /** Inner public class to implement a build pattern for the user object. */
    public static class UserBuilder {
        private String id;
        private Long created;
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        /** Default constructor for builder. */
        public UserBuilder() {}

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        /**
        * Set created timestamp
        *
        * @param created timestamp
        * @return the builder
        */
        public UserBuilder created(Long created) {
            this.created = created;
            return this;
        }

        /**
        * Set email address.
        *
        * @param email email address
        * @return the builder
        */
        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        /**
        * Set password.
        *
        * @param password users password
        * @return the builder
        */
        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        /**
        * Set users first name.
        *
        * @param firstName users first name
        * @return the builder
        */
        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        /**
        * Set users last name.
        *
        * @param lastName users last name
        * @return the builder
        */
        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        /**
        * Create a User object with set attributes.
        *
        * @return the created user object
        */
        public User build() {
            return new User(
                    this.id, this.created, this.email, this.password, this.firstName, this.lastName);
        }
    }

    /**
    * Get the system ID of an user.
    *
    * @return The ID of the user
    */
    public String getId() {
        return this.id;
    }

    /**
    * Get the creation timestamp of the user.
    *
    * @return Creation timestamp
    */
    public Long getCreated() {
        return this.created;
    }

    /**
    * Get the E-Mail adress of an user account.
    *
    * @return E-Mail address of the user account
    */
    public String getEmail() {
        return this.email;
    }

    /**
    * Get the users password.
    *
    * @return users password
    */
    public String getPassword() {
        return this.password;
    }

    /**
    * Get the first name of a user.
    *
    * @return The first name of the user
    */
    public String getFirstName() {
        return this.firstName;
    }

    /**
    * Get the last name of a user.
    *
    * @return The last name of the user
    */
    public String getLastName() {
        return this.lastName;
    }
}
