package com.kc.authenticator.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Apps")
public class App {

    @Id
    private String id;
    private String devId;
    private String appName;
    private Integer userCount = 0;

    @CreatedDate
    @Field("createdAt")
    private Date createdAt;

    @LastModifiedDate
    @Field("updatedAt")
    private Date updatedAt;

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public App() {
    }

    public App(String devId, String appName) {
        this.devId = devId;
        this.appName = appName;
    }

    // Getters and Setters
    public String getId() {
        return id;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString method
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("App {");
        sb.append("id='").append(id).append('\'');
        sb.append(", devId='").append(devId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", userCount='").append(userCount).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append('}');
        return sb.toString();
    }
}
