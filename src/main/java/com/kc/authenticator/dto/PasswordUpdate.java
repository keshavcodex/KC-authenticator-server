package com.kc.authenticator.dto;

public class PasswordUpdate {
    String token;
    String password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String  toString() {
        final StringBuilder sb = new StringBuilder("PasswordUpdate {");
        sb.append("token='").append(token).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
