package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    private ValidatableResponse response;

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

    @Step("Send POST request to /api/v1/courier")
    public ValidatableResponse sendPostRequestForCreateCourier(String login, String password, String firstName) {
        response = createCourier.createCourier(login, password, firstName);
        return response;
    }

    @Step("Send POST request to /api/v1/courier without first name")
    public ValidatableResponse sendPostRequestForCreateCourierWithoutFirstName(String login, String password) {
        response = createCourier.createCourierWithoutFirstName(login, password);
        return response;
    }

    @Step("Compare response status code with extends status code")
    public  void compareStatusCode(ValidatableResponse response, int statusCode){
        response.statusCode(statusCode);
    }

    @Step("Compare response body with 'ok'")
    public  void compareBody(ValidatableResponse response, String result, boolean extendsBody) {
        response.body(result, is(extendsBody));
    }

    @Step("Compare response body message")
    public  void compareBodyMessage(ValidatableResponse response, String result, String extendsBody) {
        response.body(result, is(extendsBody));
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
    @DisplayName("Check response status code create courier")
    public void checkStatusCodeCreatureCourierShouldReturnStatusCode201Test() { //проверка статус кода ответа создания курьера
        //создаем рандомные логин, пароль и имя
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        //создаем курьера
        response = sendPostRequestForCreateCourier(login, password, firstName);
        //проверяем статус код
        compareStatusCode(response, 201);
    }

    @Test
    @DisplayName("Check response body create courier")
    public void checkBodyCreatureCourierShouldReturnOkTrueTest() {//проверка тела ответа создания курьера
        //создаем рандомные логин, пароль и имя
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        //создаем курьера
        response = sendPostRequestForCreateCourier(login, password, firstName);
        //проверяем тело ответа
        compareBody(response, "ok", true);//проверяем тело ответа
    }

    @Test
    @DisplayName("Check response status code create two identical courier")
    public void checkCantCreateTwoIdenticalCouriersStatusCodeShouldReturn409Test() {//проверка статус кода ответа создания двух идентичных курьеров
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        //создаем двух курьеров с идентичными данными
        ValidatableResponse firstCourier = sendPostRequestForCreateCourier(login, password, firstName);
        response = sendPostRequestForCreateCourier(login, password, firstName);
        //проверяем статус код
        compareStatusCode(response, 409);// проверяем статус-код
    }

    @Test
    @DisplayName("Check response body create two identical courier")
    public void checkCantCreateTwoIdenticalCouriersBodyShouldReturnMessageTest() {//проверка тела ответа создания двух идентичных курьеров
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        String extendsMessage = "Этот логин уже используется. Попробуйте другой.";
        //создаем двух курьеров с идентичными данными
        response = sendPostRequestForCreateCourier(login, password, firstName);
        response = sendPostRequestForCreateCourier(login, password, firstName);
                compareBodyMessage(response, "message", extendsMessage);//Проверка тела ответа(по докам Этот логин уже используется)
    }

    @Test
    @DisplayName("Check response status code can't create courier without login")
    public void checkCantCreateCourierWithoutLoginStatusCodeShouldReturn400Test() {//проверка статус кода ответа создания курьера без логина
        //создаем логин, пароль и имя курьера
        login = "";//логин-пустое поле
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        delete = false;
        //создаем курьера без логина
        response = sendPostRequestForCreateCourier(login, password, firstName);
        //проверяем статус код
        compareStatusCode(response, 400);// проверяем статус-код
    }

    @Test
    @DisplayName("Check response body can't create courier without login")
    public void checkCantCreateCourierWithoutLoginBodyShouldReturnMessageTest() {//проверка тела ответа создания курьера без логина
        //создаем логин, пароль и имя курьера
        login = "";//логин-пустое поле
        password = generateRandomPassword();
        firstName = generateRandomFirstName();
        String extendsMessage = "Недостаточно данных для создания учетной записи";
        delete = false;
        //создаем курьера без логина
        response = sendPostRequestForCreateCourier(login, password, firstName);
        compareBodyMessage(response, "message", extendsMessage);// проверяем тело ответа
    }

    @Test
    @DisplayName("Check response status code can't create courier without password")
    public void checkCantCreateCourierWithoutPasswordStatusCodeShouldReturn400Test() {//проверка статус кода ответа создания курьера без пароля
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = "";//пароль-пустое поле
        firstName = generateRandomFirstName();
        delete = false;
        //создаем курьера без пароля
        response = sendPostRequestForCreateCourier(login, password, firstName);
        //проверяем статус код
        compareStatusCode(response, 400);// проверяем статус-код
    }

    @Test
    @DisplayName("Check response body can't create courier without password")
    public void checkCantCreateCourierWithoutPasswordBodyShouldReturnMessageTest() {//проверка тела ответа создания курьера без пароля
        //создаем логин, пароль и имя курьера
        login = generateRandomLogin();
        password = "";//пароль-пустое поле
        firstName = generateRandomFirstName();
        String extendsMessage = "Недостаточно данных для создания учетной записи";
        delete = false;
        //создаем курьера без логина
        response = sendPostRequestForCreateCourier(login, password, firstName);
        compareBodyMessage(response, "message", extendsMessage);// проверяем тело ответа
    }

    @Test
    @DisplayName("Check response status code create courier without first Name")
    public void checkCreateCourierWithoutFirstNameShouldReturnStatusCode201Test() {//проверка статус кода ответа при создании курьера без имени
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //Создаем курьера
        response = sendPostRequestForCreateCourierWithoutFirstName(login, password);
        compareStatusCode(response, 201);// проверяем статус-код
    }

    @Test
    @DisplayName("Check response body create courier without first Name")
    public void checkCreateCourierWithoutFirstNameShouldReturnOkTrueTest() {//проверка тела ответа при создании курьера без имени
        //создаем рандомные логин и пароль
        login = generateRandomLogin();
        password = generateRandomPassword();
        //Создаем курьера
        response = sendPostRequestForCreateCourierWithoutFirstName(login, password);
        compareBody(response, "ok", true);//проверяем тело ответа
    }

    @After
    public void teardown() {
        //удаляем созданного курьера
        if (delete) {
            Integer id = loginCourier.loginCourier(login, password).extract().path("id");
            deleteCourier.deleteCourier(id);
        }
    }


}