package com.kc.authenticator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Developer")
public class Dev {
    @Id
    String id;
    String firstName;
    String lastName;
    String phone;

    @Indexed(unique = true)
    String devEmail;
    String password;

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getDevEmail() {
        return devEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDevEmail(String devEmail) {
        this.devEmail = devEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Dev() {
    }

    public Dev(Dev dev) {
        this.setFirstName(dev.getFirstName());
        this.setLastName(dev.getLastName());
        this.setPhone(dev.getPhone());
        this.setDevEmail(dev.getDevEmail());
        this.setPassword(dev.getPassword());
    }

    public Dev(String firstName, String lastName, String devEmail) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setDevEmail(devEmail);
    }

    public Dev removePassword() {
        this.setPassword("");
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Dev{");
        sb.append("id='").append(id).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", devEmail='").append(devEmail).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
