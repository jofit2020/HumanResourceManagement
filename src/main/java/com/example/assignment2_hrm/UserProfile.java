package com.example.assignment2_hrm;

public class UserProfile {

    // Role Constants
    public static final String ROLE_ADMIN = "APP_ADMIN";
    public static final String ROLE_USER = "APP_USER";

    // Properties
    private String userId;
    private String email;
    private String username;
    private String password;
    private boolean isActive;
    private String fullName;
    private String role;

    // Constructor
    public UserProfile(String userId, String email, String username, String password, boolean isActive, String fullName, String role) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Helper Methods
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(this.role);
    }

    public boolean isUser() {
        return ROLE_USER.equals(this.role);
    }
}
