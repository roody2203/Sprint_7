package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.LoginCourierRequest;

import static io.restassured.RestAssured.given;

public class LoginCourier extends BaseApi {
    public final static String path = "/api/v1/courier/login";

    @Step("Send POST request to /api/v1/courier/login")
    public ValidatableResponse loginCourier(LoginCourierRequest request) {
                return given()
                        .spec(requestSpecification)
                        .body(request)
                        .when()
                        .post(path)
                        .then();
    }
}
