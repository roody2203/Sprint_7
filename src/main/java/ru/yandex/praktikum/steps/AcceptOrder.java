package ru.yandex.praktikum.steps;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;

import static io.restassured.RestAssured.given;

public class AcceptOrder {
    private CreateCourier createCourier = new CreateCourier();//создаем объект класса CreateCourier
    private LoginCourier loginCourier = new LoginCourier();//создаем объект класса LoginCourier

    private String login;//создаем поле логин
    private String password;//создаем поле пароль

   /* public ValidatableResponse acceptOrder(int id, int courierId) {

        createCourier.createCourierWithoutFirstName(login,password);//создали курьера
        loginCourier.loginCourier(login, password).extract().path("id");//получили id курьера


        return ;
    }*/
}
