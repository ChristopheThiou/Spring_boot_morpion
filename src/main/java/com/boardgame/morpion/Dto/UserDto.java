package com.boardgame.morpion.dto;

public class UserDto {
    private String userId;
    private String username;

    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}