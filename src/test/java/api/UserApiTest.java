package api;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserCredentials;
import steps.UserSteps;

import static org.hamcrest.Matchers.*;

public class UserApiTest {

    private final UserSteps userSteps = new UserSteps();
    private final String email = "testuser" + System.currentTimeMillis() + "@yandex.ru";
    private final String password = "password123";
    private final String name = "Test User";
    private String accessToken;

    private User user;
    private UserCredentials credentials;

    @Before
    public void setUp() {
        user = new User(email, password, name);
        credentials = new UserCredentials(email, password);
    }

    @Test
    public void createUser_Success() {
        ValidatableResponse response = userSteps.createUser(user);
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(email))
                .and()
                .body("user.name", equalTo(name));

        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void createUser_EmailExists_Failure() {
        // Сначала создаём пользователя
        userSteps.createUser(user).assertThat().statusCode(200);

        // Повторная регистрация
        ValidatableResponse response = userSteps.createUser(user);
        response.assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    public void login_Success() {
        userSteps.createUser(user);
        ValidatableResponse response = userSteps.login(credentials);
        response.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(email));

        accessToken = response.extract().path("accessToken");
    }

    @Test
    public void login_WrongPassword_Failure() {
        userSteps.createUser(user);
        UserCredentials wrongCreds = new UserCredentials(email, "wrongpass");
        ValidatableResponse response = userSteps.login(wrongCreds);
        response.assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    public void updateUser_Success() {
        ValidatableResponse createResponse = userSteps.createUser(user);
        accessToken = createResponse.extract().path("accessToken");

        User updatedUser = new User(email, password, "New Name");
        ValidatableResponse updateResponse = userSteps.updateUser(updatedUser, accessToken);
        updateResponse.assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.name", equalTo("New Name"));
    }

    @Test
    public void updateUser_Unauthorized_Failure() {
        User updatedUser = new User(email, password, "New Name");
        ValidatableResponse response = userSteps.updateUser(updatedUser, null);
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