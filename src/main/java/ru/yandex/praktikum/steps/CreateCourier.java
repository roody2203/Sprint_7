package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import ru.yandex.praktikum.dto.CreateCourierRequest;

import static io.restassured.RestAssured.given;

public class CreateCourier {

    public ValidatableResponse createCourier(String login, String password, String firstName) {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(login);
        createCourierRequest.setPassword(password);
        createCourierRequest.setFirstName(firstName);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier")
                .then();
    }


    public ValidatableResponse createCourierWithoutFirstName(String login, String password) {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(login);
        createCourierRequest.setPassword(password);

        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://qa-scooter.praktikum-services.ru/")
                .body(createCourierRequest)
                .when()
                .post("/api/v1/courier")
                .then();
    }

}
