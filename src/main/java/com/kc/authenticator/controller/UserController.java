package com.kc.authenticator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    @GetMapping("/")
    public String home() {
        return "Welcome to home route of EndUserController";
    }
}