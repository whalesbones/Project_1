package api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserCredentials;
import steps.UserSteps;
import static generator.UserGenerator.*;
import static org.hamcrest.Matchers.*;

public class UserLoginTest {

    private final UserSteps userSteps = new UserSteps();
    private String accessToken;
    private User user;

    @Before
    public void setUp() {
        // Подготовка: создаём пользователя перед тестами
        user = generate();
        ValidatableResponse response = userSteps.createUser(user);
        response.assertThat().statusCode(200);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void login_Success() {
        // Действие: логинимся
        UserCredentials credentials = new UserCredentials(user.getEmail(), user.getPassword());
        ValidatableResponse response = userSteps.login(credentials);
        response.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()));
    }

    @Test
    public void login_WrongPassword_Failure() {
        // Действие: логинимся с неправильным паролем
        String wrongPassword = "wrongpass_" + System.currentTimeMillis();
        UserCredentials wrongCreds = new UserCredentials(user.getEmail(), wrongPassword);
        ValidatableResponse response = userSteps.login(wrongCreds);
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