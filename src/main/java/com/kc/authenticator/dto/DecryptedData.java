package com.kc.authenticator.dto;

import com.kc.authenticator.model.Token;

public class DecryptedData {
    String id;
    String token;
    Boolean isValidData;

    public DecryptedData(String id, String token) {
        this.id = id;
        this.token = token;
        isValidData = true;
    }
    public DecryptedData(Boolean isValidData) {
        this.id = null;
        this.token = null;
        this.isValidData = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getIsValidData() {
        return isValidData;
    }
    public Boolean isValidData() {
        return isValidData;
    }

    public void setIsValidData(Boolean isValidData) {
        this.isValidData = isValidData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DecryptedData {");
        sb.append("id='").append(id).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", isValidData=").append(isValidData);
        sb.append('}');
        return sb.toString();
    }
}
