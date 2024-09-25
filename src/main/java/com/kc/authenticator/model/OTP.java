package com.kc.authenticator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "otp")
public class OTP {

    @Id
    private String id;
    private String referenceId;
    private String otp;
    private String email;

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public OTP(){}
    public OTP(String otp, String referenceId, Integer minutes) {
        this.otp = otp;
        this.referenceId = referenceId;
        this.expiresAt = LocalDateTime.now().plusMinutes(minutes);
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
    public String getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    public void resetExpires(Integer minutes) {
        this.expiresAt = LocalDateTime.now().plusMinutes(minutes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OTP{");
        sb.append("id='").append(id).append('\'');
        sb.append(", referenceId='").append(referenceId).append('\'');
        sb.append(", otp='").append(otp).append('\'');
        sb.append(", expiresAt=").append(expiresAt);
        sb.append('}');
        return sb.toString();
    }
}
