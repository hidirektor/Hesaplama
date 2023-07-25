package me.t3sl4.hydraulic.Util;

public class User {
    private String username;
    private String fullName;

    public User(String username, String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }
}