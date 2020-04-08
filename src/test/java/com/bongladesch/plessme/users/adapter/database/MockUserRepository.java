package com.bongladesch.plessme.users.adapter.database;

import com.bongladesch.plessme.users.adapter.mongo.MongoUserRepository;
import com.bongladesch.plessme.users.entity.User;
import com.bongladesch.plessme.users.entity.User.UserBuilder;
import io.quarkus.test.Mock;
import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockUserRepository extends MongoUserRepository {

    private boolean alreadyExists;

    public MockUserRepository() {
        super(null);
        alreadyExists = false;
    }

    public void alreadyExists() {
        this.alreadyExists = true;
    }

    public void notExists() {
        this.alreadyExists = false;
    }

    @Override
    public void create(User user) {}

    @Override
    public User findByEmail(String email) {
        if (alreadyExists) {
            UserBuilder builder = new UserBuilder();
            builder.email("me@test.com").password("password").firstName("my").lastName("name");
            return builder.build();
        } else {
            return null;
        }
    }
}
