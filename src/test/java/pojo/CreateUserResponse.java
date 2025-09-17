// src/test/java/pojo/CreateUserResponse.java
package pojo;

public class CreateUserResponse {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken; // ✅ добавлено

    // Геттеры
    public boolean isSuccess() { return success; }
    public User getUser() { return user; }
    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; } // ✅

    // Сеттеры
    public void setSuccess(boolean success) { this.success = success; }
    public void setUser(User user) { this.user = user; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; } // ✅

    // equals() и hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserResponse that = (CreateUserResponse) o;
        return success == that.success &&
                java.util.Objects.equals(user, that.user) &&
                java.util.Objects.equals(accessToken, that.accessToken) &&
                java.util.Objects.equals(refreshToken, that.refreshToken); // ✅
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(success, user, accessToken, refreshToken); // ✅
    }
}