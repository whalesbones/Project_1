package api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import generator.IngredientsResponse;
import generator.UserGenerator;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;

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


    private String getIngredientByType(List<Ingredient> ingredients, String type) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getType().equals(type))
                .findFirst()
                .map(Ingredient::getId)
                .orElseThrow(() -> new IllegalStateException("No ingredient of type " + type + " found!"));
    }

    @Before
    public void setUp() {
        User user = generate();
        credentials = new UserCredentials(user.getEmail(), user.getPassword());

        ValidatableResponse createResponse = userSteps.createUser(user);
        accessToken = createResponse.extract().path("accessToken");

        RestAssured.config = RestAssured.config()
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory((clazz, charset) -> {
                            ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                            return mapper;
                        }));
    }

    @Test
    public void createOrder_Success() {
        List<Ingredient> ingredients = loadAllIngredients();

        // 🔍 Логируем все ингредиенты
        System.out.println("Total ingredients loaded: " + ingredients.size());
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            System.out.println("Index " + i + ": ID=" + ing.getId() + ", Name=" + ing.getName() + ", Type=" + ing.getType());
        }

        if (ingredients.size() < 3) {
            throw new IllegalStateException("Not enough ingredients!");
        }

        // Берём один ингредиент каждого типа
        String bunId = getIngredientByType(ingredients, "bun");
        String sauceId = getIngredientByType(ingredients, "sauce");
        String mainId = getIngredientByType(ingredients, "main");

        Order order = new Order(Arrays.asList(bunId, sauceId, mainId));

        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    public void getUserOrdersSuccess() {
        List<Ingredient> ingredients = loadAllIngredients();
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients loaded!");
        }

        String ingredientId = getIngredientByType(ingredients, "bun");
        Order order = new Order(Collections.singletonList(ingredientId));

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
    public void createOrder_Success() {
        List<Ingredient> ingredients = loadAllIngredients();

        // 🔍 Логируем все ингредиенты
        System.out.println("Total ingredients loaded: " + ingredients.size());
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ing = ingredients.get(i);
            System.out.println("Index " + i + ": ID=" + ing.getId() + ", Name=" + ing.getName() + ", Type=" + ing.getType());
        }

        if (ingredients.size() < 3) {
            throw new IllegalStateException("Not enough ingredients!");
        }

        // Берём один ингредиент каждого типа
        String bunId = getIngredientByType(ingredients, "bun");
        String sauceId = getIngredientByType(ingredients, "sauce");

        Order order = new Order(Arrays.asList(bunId, sauceId));

        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }
}