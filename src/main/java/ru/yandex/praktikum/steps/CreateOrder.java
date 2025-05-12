package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateOrderRequest;

import static io.restassured.RestAssured.given;

public class CreateOrder {

    public ValidatableResponse createOrder(String[] colors) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setFirstName("Naruto");
        createOrderRequest.setLastName("Uchiha");
        createOrderRequest.setAddress("Konoha, 142 apt.");
        createOrderRequest.setMetroStation("4");
        createOrderRequest.setPhone("+7 800 355 35 35");
        createOrderRequest.setRentTime("5");
        createOrderRequest.setDeliveryDate("2027-06-06");
        createOrderRequest.setComment("Saske, come back to Konoha");
        createOrderRequest.setColor(colors);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(createOrderRequest)
                .when()
                .post("/api/v1/orders")
                .then();
    }
}
