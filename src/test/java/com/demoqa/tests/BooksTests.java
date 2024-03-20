package com.demoqa.tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;


public class BooksTests extends TestBase {

    @Test
    void successfulLoginWithApiAndUiTest() {
        // Make Api request to get token, id ...
        String body = "{\"userName\":\"test123456\",\"password\":\"Test123456@\"}"; // BAD PRACTICE

        Response response = given()
                .log().uri()
                .log().method()
                .log().body()
                .contentType(JSON)
                .body(body)
                .when()
                .post("/Account/v1/Login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();

        String userId = response.path("userId"),
                token = response.path("token"),
                expires = response.path("expires");

        // Put data to browser cookies
        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie("userID", userId));
        getWebDriver().manage().addCookie(new Cookie("token", token));
        getWebDriver().manage().addCookie(new Cookie("expires", expires));

        open("/books");
        $("#userName-value").shouldHave(text("test123456"));
    }
}
