package api;

import generator.IngredientsResponse;
import generator.UserGenerator;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Ingredient;
import pojo.Order;
import pojo.User;
import pojo.UserCredentials;
import steps.OrderSteps;
import steps.UserSteps;
import static generator.UserGenerator.*;
import static generator.generateOrder.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderApiTest {

    private final UserSteps userSteps = new UserSteps();
    private final OrderSteps orderSteps = new OrderSteps();
    private final UserGenerator userGenerator = new UserGenerator();
    private User user;
    private IngredientsResponse ingredientsResponse;
    private String accessToken;

    private UserCredentials credentials;

    private static List<Ingredient> allIngredients;

    public static List<Ingredient> loadAllIngredients() {
        if (allIngredients == null) {
            Response response = OrderSteps.getIngredientsWithoutAuth();
            IngredientsResponse ingredientsResponse = response.as(IngredientsResponse.class);
            allIngredients = ingredientsResponse.getData();
        }
        return allIngredients;
    }

    @Before
    public void setUp() {
        User user = generate();
        credentials = new UserCredentials(user.getEmail(), user.getPassword());

        ValidatableResponse createResponse = userSteps.createUser(user);
        accessToken = createResponse.extract().path("accessToken");
    }

    @Test
    public void createOrder_Success() {
        // Получаем ингредиенты динамически
        List<Ingredient> ingredients = loadAllIngredients();
        if (ingredients.size() < 2) {
            throw new IllegalStateException("Not enough ingredients!");
        }

        Order order = new Order(Arrays.asList(
                ingredients.get(0).getId(),
                ingredients.get(1).getId()
        ));

        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue())
                .body("order.ingredients", notNullValue())
                .body("order.status", equalTo("created"))
                .body("order.name", notNullValue())
                .body("order.createdAt", notNullValue())
                .body("order.updatedAt", notNullValue());
    }



    @Test
    public void getUserOrdersSuccess() {
        Order order = new Order(Collections.singletonList("61c0c5a71d1f82001bdaaa6d"));
        orderSteps.createOrder(order, accessToken);
        orderSteps.getUserOrders(accessToken)
        .assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("orders", hasSize(greaterThanOrEqualTo(1)));
    }

    @Test
    public void createOrderUnauthorizedFailure() {
        Order order = new Order(Collections.singletonList("xxxx"));
        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(500);
    }

    @After
    public void tearDown() {
        userSteps.deleteUser(accessToken);

    }
}