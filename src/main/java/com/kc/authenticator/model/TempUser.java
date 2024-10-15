package com.kc.authenticator.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "tempUser")
public class TempUser extends User {

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public TempUser() {

    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public TempUser(User user){
        this.setAppId(user.getAppId());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPhone(user.getPhone());
        this.setEmail(user.getEmail());
        this.setPassword(user.getPassword());
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(60);
        this.setExpiresAt(expiresAt);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TempUser{");
        sb.append("id='").append(id).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append("expiresAt=").append(expiresAt);
        sb.append('}');
        return sb.toString();
    }
}
