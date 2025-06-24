package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateCourierRequest;

import static io.restassured.RestAssured.given;

public class CreateCourier extends BaseApi {
    public final static String path = "/api/v1/courier";

    @Step("Send POST request to /api/v1/courier")
    public ValidatableResponse createCourier(CreateCourierRequest request) {

        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .post(path)
                .then();
    }
}
