package ru.yandex.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CancelOrderRequest;
import static io.restassured.RestAssured.given;

public class CancelOrder extends BaseApi {
    public final static String path = "/api/v1/orders/cancel";

    @Step("Send DELETE request to /api/v1/orders/cancel")
    public ValidatableResponse cancelOrder(CancelOrderRequest request) {
        return given()
                .spec(requestSpecification)
                .body(request)
                .when()
                .delete(path)
                .then();
    }
}
