package com.kc.authenticator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Token")
public class Token {

    @Id
    String id;
    String referenceId;
    String token;

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public Token() {
    }

    public Token(String[] keys) {
        if (keys.length >= 2) {
            this.id = keys[0];
            this.token = keys[1];
        }
    }

    public Token(String referenceId, String token, Integer plusMinutes) {
        this.referenceId = referenceId;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusMinutes(plusMinutes);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("decryptedData {");
        sb.append("id='").append(id).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", referenceId='").append(referenceId).append('\'');
        sb.append('}');
        return sb.toString();
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

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
