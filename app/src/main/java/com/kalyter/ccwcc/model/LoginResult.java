package com.kalyter.ccwcc.model;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class LoginResult {
    private User user;
    private Token token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
