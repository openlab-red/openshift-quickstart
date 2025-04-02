package io.redhat.quickstart.resource;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MessageResourceTest {

    @Test
    public void testPingEndpoint() {
        given()
          .when().get("/api/ping")
          .then()
             .statusCode(200)
             .body("message", is("pong"));
    }

    @Test
    public void testAddMessage() {
        given()
            .when()
            .post("/api/add/HelloQuarkus")   // Updated to use path parameter
            .then()
            .statusCode(201);
    }
}