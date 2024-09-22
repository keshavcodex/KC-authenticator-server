package com.kc.authenticator.dto;

import com.kc.authenticator.model.App;
import java.util.List;

public class AppListResponse {
    public Boolean isSuccess = true;
    public Integer count = 0;
    public String message = "";
    public List<App> apps;

    public AppListResponse() {
        this.message = "No info of the apps";
        this.isSuccess = true;
    }

    public AppListResponse(List<App> apps) {
        this.apps = apps;
        this.count = apps.size();
    }

    public AppListResponse(List<App> apps, String message) {
        this.apps = apps;
        this.message = message;
        this.isSuccess = true;
        this.count = apps.size();
    }

    public AppListResponse(List<App> apps, String message, Boolean isSuccess) {
        this.apps = apps;
        this.message = message;
        this.isSuccess = isSuccess;
        this.count = apps.size();
    }
}
