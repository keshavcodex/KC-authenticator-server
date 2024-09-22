package com.kc.authenticator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Apps")
public class App {
    @Id
    private String id;
    private String devId;
    private String appName;

    public String getId() {
        return id;
    }

    public App() {
    }

    public App(String devId, String appName) {
        this.devId = devId;
        this.appName = appName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientApp {");
        sb.append("id='").append(id).append('\'');
        sb.append(", devId=").append(devId);
        sb.append(", appName='").append(appName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
