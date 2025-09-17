package api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import steps.UserSteps;
import static generator.UserGenerator.*;
import static org.hamcrest.Matchers.*;

public class UserProfileTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        // Подготовка: создаём пользователя
        user = generate();
        ValidatableResponse response = userSteps.createUser(user);
        response.assertThat().statusCode(200);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void updateUser_Success() {
        // Действие: обновляем имя
        String newName = "NewName_" + System.currentTimeMillis();
        User updatedUser = new User(user.getEmail(), user.getPassword(), newName);
        ValidatableResponse updateResponse = userSteps.updateUser(updatedUser, accessToken);
        updateResponse.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }

    @Test
    public void updateUser_Unauthorized_Failure() {
        // Действие: пытаемся обновить без токена
        String newName = "NewName_" + System.currentTimeMillis();
        User updatedUser = new User(user.getEmail(), user.getPassword(), newName);
        ValidatableResponse response = userSteps.updateUser(updatedUser, null);
        response.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", notNullValue());
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}