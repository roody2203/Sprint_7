package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.dto.CreateCourierRequest;
import ru.yandex.praktikum.dto.LoginCourierRequest;
import ru.yandex.praktikum.steps.CreateCourier;
import ru.yandex.praktikum.steps.DeleteCourier;
import ru.yandex.praktikum.steps.LoginCourier;


import static org.hamcrest.Matchers.is;

public class CreateCourierTests {
    private CreateCourier createCourier;
    private LoginCourier loginCourier;
    private DeleteCourier deleteCourier;

    private String login;//создаем поле логин
    private String password;//создаем поле пароль
    private String firstName;//создаем поле имя

    boolean delete = true;

    @Step("Generate random login")
    public String generateRandomLogin() {
        return RandomStringUtils.randomAlphabetic(7);
    }

    @Step("Generate random password")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Generate random first name")
    public String generateRandomFirstName() {
        return RandomStringUtils.randomAlphabetic(6);
    }


    @Step("Compare response status code with expected status code")
    public  void compareStatusCode(ValidatableResponse response, int statusCode){
        response.statusCode(statusCode);
    }

    @Step("Compare response body with expected boolean")
    public  void compareBody(ValidatableResponse response, String result, boolean expectedBody) {
        response.body(result, is(expectedBody));
    }

    @Step("Compare response body message with expected string")
    public  void compareBodyMessage(ValidatableResponse response, String result, String expectedBody) {
        response.body(result, is(expectedBody));
    }

    @Before
    public void init() { // метод для логирования запроса и ответа при ошибке
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());

        createCourier = new CreateCourier();//создаем объект класса CreateCourier
        loginCourier = new LoginCourier();//создаем объект класса LoginCourier(нужен для удаления курьера в аннотации After)
        deleteCourier = new DeleteCourier();
    }

    @Test
    @DisplayName("Check create courier")
    public void createCourierTest() { //проверка статус кода ответа создания курьера
        //создаем рандомные логин, пароль и имя
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();

        //создаем курьера
        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName(firstName);
        ValidatableResponse response = createCourier.createCourier(request);

        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_CREATED);

        //проверяем тело ответа
        compareBody(response, "ok", true);
    }

    @Test
    @DisplayName("Check can't create two identical courier")
    public void checkCantCreateTwoIdenticalCouriersTest() {//проверка создания двух идентичных курьеров
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();

        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName(firstName);

        //создаем двух курьеров с идентичными данными
        ValidatableResponse firstResponse = createCourier.createCourier(request);
        ValidatableResponse secondResponse = createCourier.createCourier(request);
        //проверяем статус код
        compareStatusCode(secondResponse, HttpStatus.SC_CONFLICT);
        //Проверка тела ответа
        compareBodyMessage(secondResponse, "message", "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Check can't create courier without login")
    public void checkCantCreateCourierWithoutLoginTest() {//проверка создания курьера без логина
        //создаем логин, пароль и имя курьера
        login = "";//логин-пустое поле
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        delete = false;

        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName(firstName);
        //создаем курьера без логина
        ValidatableResponse response = createCourier.createCourier(request);
        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        // проверяем тело ответа
        compareBodyMessage(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check can't create courier without password")
    public void checkCantCreateCourierWithoutPasswordTest() {//проверка создания курьера без пароля
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = "";//пароль-пустое поле
        firstName = generateRandomFirstName();
        delete = false;

        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        request.setFirstName(firstName);
        //создаем курьера без пароля
        ValidatableResponse response = createCourier.createCourier(request);
        //проверяем статус код
        compareStatusCode(response, HttpStatus.SC_BAD_REQUEST);
        // проверяем тело ответа
        compareBodyMessage(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check create courier without first Name")
    public void checkCreateCourierWithoutFirstNameTest() {//проверка при создании курьера без имени
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();

        CreateCourierRequest request = new CreateCourierRequest();
        request.setLogin(login);
        request.setPassword(password);
        //Создаем курьера
        ValidatableResponse response = createCourier.createCourier(request);
        // проверяем статус-код
        compareStatusCode(response, HttpStatus.SC_CREATED);
        //проверяем тело ответа
        compareBody(response, "ok", true);
    }

    @After
    public void teardown() {
        //удаляем созданного курьера
        if (delete) {
            LoginCourierRequest request = new LoginCourierRequest();
            request.setLogin(login);
            request.setPassword(password);
            Integer id = loginCourier.loginCourier(request).extract().path("id");
            deleteCourier.deleteCourier(id);
        }
    }
}