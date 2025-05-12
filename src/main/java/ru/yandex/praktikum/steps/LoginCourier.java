package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.LoginCourierRequest;

import static io.restassured.RestAssured.given;

public class LoginCourier {
    public ValidatableResponse loginCourier(String login, String password) {
        LoginCourierRequest loginCourierRequest = new LoginCourierRequest();
        loginCourierRequest.setLogin(login);
        loginCourierRequest.setPassword(password);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(loginCourierRequest)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
}
