package com.kc.authenticator.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "tempDev")
public class TempDev extends Dev {

    @Indexed(name = "expiresAt", expireAfterSeconds = 0)
    private LocalDateTime expiresAt;

    public TempDev() {

    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public TempDev(Dev dev){
        this.setFirstName(dev.getFirstName());
        this.setLastName(dev.getLastName());
        this.setPhone(dev.getPhone());
        this.setDevEmail(dev.getDevEmail());
        this.setPassword(dev.getPassword());
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(60);
        this.setExpiresAt(expiresAt);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TempDev{");
        sb.append("id='").append(id).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", devEmail='").append(devEmail).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append("expiresAt=").append(expiresAt);
        sb.append('}');
        return sb.toString();
    }
}
