package steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import pojo.Order;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.given;

public class OrderSteps {



    public OrderSteps() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return createOrder(order, null);
    }

    public static Response getIngredientsWithoutAuth() {
        return given()
                .header("Content-Type", "application/json")
                .get("/api/ingredients");
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order, String token) {
       RequestSpecification request = given()
                .header("Content-type", "application/json")
                .and()
                .body(order);

        if (token != null) {
            request.header("Authorization", token);
        }

        return request.when()
                .post("/api/orders")
                .then();
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get( "/api/orders")
                .then();
    }
}