package com.kc.authenticator.dto;

import com.kc.authenticator.model.User;

public class UserResponse {
    public Boolean isSuccess = true;
    public String message = "";
    public User user;

    public UserResponse() {
        this.message = "No info of the User";
        this.isSuccess = true;
    }

    public UserResponse(User user) {
        this.user = user;
    }

    public UserResponse(User user, String message) {
        this.user = user;
        this.message = message;
        this.isSuccess = user != null;
    }

    public UserResponse(User user, String message, Boolean isSuccess) {
        this.user = user;
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
