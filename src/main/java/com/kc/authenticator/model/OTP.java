package com.kc.authenticator.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Document(collection = "otp")
public class OTP {

    @Value("${app.tokenDuration}")
    private Integer tokenDuration;

    @Id
    private String id;
    private String otp;

    @Indexed(unique = true)
    private String devEmail;

    private String email;

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public OTP(String otp, String devEmail, Integer plusMinutes) {
        this.otp = otp;
        this.devEmail = devEmail;
        this.expiresAt = LocalDateTime.now().plusMinutes(plusMinutes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getDevEmail() {
        return devEmail;
    }

    public void setDevEmail(String devEmail) {
        this.devEmail = devEmail;
    }

    public String getEmail() {
        return devEmail;
    }

    public void setEmail(String devEmail) {
        this.devEmail = devEmail;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    public void resetExpires(Integer plusMinutes) {
        this.expiresAt = LocalDateTime.now().plusMinutes(plusMinutes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OTP{");
        sb.append("id='").append(id).append('\'');
        sb.append(", otp='").append(otp).append('\'');
        sb.append(", email='").append(devEmail).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
