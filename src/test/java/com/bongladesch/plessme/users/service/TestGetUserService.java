package com.bongladesch.plessme.users.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Test implementation for usecase "GetUser". */
@QuarkusTest
@Tag("integration")
public class TestGetUserService {

    /** Test getUser from OIDC user info */
    @Test
    @TestSecurity(
            user = "tester@gmail.com",
            roles = {"user"},
            authorizationEnabled = false)
    public void testGetUser() {
        given().when().get("/users").then().statusCode(200).body(is("{\"id\":\"UUID\"}"));
    }
}
