package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CancelOrderRequest;
import static io.restassured.RestAssured.given;

public class CancelOrder {
    public ValidatableResponse cancelOrder(Integer track) {
        CancelOrderRequest cancelOrderRequest = new CancelOrderRequest();
        cancelOrderRequest.setTrack(track);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(cancelOrderRequest)
                .when()
                .delete("/api/v1/orders/cancel")
                .then();
    }
}
