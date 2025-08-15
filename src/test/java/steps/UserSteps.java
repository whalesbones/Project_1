package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.User;
import pojo.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserSteps {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register")
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(UserCredentials credentials) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(credentials)
                .when()
                .post(BASE_URL + "/api/auth/login")
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateUser(User user, String token) {
        io.restassured.specification.RequestSpecification request = given()
                .header("Content-type", "application/json")
                .and()
                .body(user);

        if (token != null) {
            request.header("Authorization", token);
        }

        return request.when()
                .patch(BASE_URL + "/api/auth/user")
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + "/api/auth/user")
                .then();
    }
}