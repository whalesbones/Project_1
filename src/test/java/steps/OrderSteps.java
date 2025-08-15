package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String token) {
        io.restassured.specification.RequestSpecification request = given()
                .header("Content-type", "application/json")
                .and()
                .body(order);

        if (token != null) {
            request.header("Authorization", token);
        }

        return request.when()
                .post(BASE_URL + "/api/orders")
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + "/api/orders")
                .then();
    }
}