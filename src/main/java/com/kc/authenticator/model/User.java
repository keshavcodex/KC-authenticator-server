package com.kc.authenticator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "EndUser")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String appId;

    @Indexed(unique = true)
    private String email;
    private String password;
}
