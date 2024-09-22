package com.kc.authenticator.dto;

import com.kc.authenticator.model.Dev;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DevResponse {
    public Boolean isSuccess = true;
    public String message = "";
    public Dev developer;

    public DevResponse() {
        this.message = "No info of the developer";
        this.isSuccess = true;
    }

    public DevResponse(Dev developer) {
        this.developer = developer;
    }

    public DevResponse(Dev developer, String message) {
        this.developer = developer;
        this.message = message;
        this.isSuccess = true;
    }

    public DevResponse(Dev developer, String message, Boolean isSuccess) {
        this.developer = developer;
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
