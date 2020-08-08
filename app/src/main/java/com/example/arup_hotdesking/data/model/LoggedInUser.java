package com.example.arup_hotdesking.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String email;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.email = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}