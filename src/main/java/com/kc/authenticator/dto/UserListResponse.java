package com.kc.authenticator.dto;

import com.kc.authenticator.model.User;
import java.util.List;

public class UserListResponse {
    public Boolean isSuccess = true;
    public Integer count = 0;
    public String message = "";
    public List<User> users;

    public UserListResponse() {
        this.message = "No info of the users";
        this.isSuccess = true;
    }

    public UserListResponse(List<User> users) {
        this.users = users;
        this.count = users.size();
    }

    public UserListResponse(List<User> users, String message) {
        this.users = users;
        this.message = message;
        this.isSuccess = true;
        this.count = users.size();
    }

    public UserListResponse(List<User> users, String message, Boolean isSuccess) {
        this.users = users;
        this.message = message;
        this.isSuccess = isSuccess;
        this.count = users.size();
    }
}
