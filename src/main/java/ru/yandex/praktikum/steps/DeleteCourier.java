package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class DeleteCourier extends BaseApi {
    public final static String path = "/api/v1/courier/{id}";

    @Step("Send DELETE request to /api/v1/courier/{id}")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(requestSpecification)
                .pathParam("id", id)
                .when()
                .delete(path)
                .then();
    }
}
