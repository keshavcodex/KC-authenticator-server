package com.kc.authenticator.dto;

import com.kc.authenticator.model.App;

public class AppResponse {
    public Boolean isSuccess = true;
    public String message = "";
    public App clientApp;

    public AppResponse() {
        this.message = "No info of the clientApp";
        this.isSuccess = true;
    }

    public AppResponse(App clientApp) {
        this.clientApp = clientApp;
    }

    public AppResponse(App clientApp, String message) {
        this.clientApp = clientApp;
        this.message = message;
        this.isSuccess = true;
    }

    public AppResponse(App clientApp, String message, Boolean isSuccess) {
        this.clientApp = clientApp;
        this.message = message;
        this.isSuccess = isSuccess;
    }
}
