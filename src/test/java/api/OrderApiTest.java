package api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import pojo.UserCredentials;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;

public class OrderApiTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();

    private final String email = "testuser" + System.currentTimeMillis() + "@yandex.ru";
    private final String name = "Test User";
    private String accessToken;

    private UserCredentials credentials;

    @Before
    public void setUp() {
        String password = "password123";
        User user = new User(email, password, name);
        credentials = new UserCredentials(email, password);

        ValidatableResponse createResponse = userSteps.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    public void createOrder_Success() {
        Order order = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        ValidatableResponse response = orderSteps.createOrder(order, accessToken);
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("order.number", notNullValue());
    }

    @Test
    public void createOrder_Unauthorized_Failure() {
        Order order = new Order(Collections.singletonList("61c0c5a71d1f82001bdaaa6d"));
        ValidatableResponse response = orderSteps.createOrder(order, null);
        response.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void getUserOrders_Success() {
        Order order = new Order(Collections.singletonList("61c0c5a71d1f82001bdaaa6d"));
        orderSteps.createOrder(order, accessToken);

        ValidatableResponse response = orderSteps.getUserOrders(accessToken);
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void getUserOrders_Unauthorized_Failure() {
        ValidatableResponse response = orderSteps.getUserOrders(null);
        response.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}