package models;

public class UserSession {

    private static UserSession instance;
    
    private User user;

    private UserSession(User user) {
        this.user = user;
    }

    public static void startSession(User user) {
        instance = new UserSession(user);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }

    public User getUser() {
        return user;
    }
}
