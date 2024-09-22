package com.kc.authenticator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public String home() {
        return "Welcome to home route of authenticator application";
    }

    @RequestMapping(value = "*")
    public String unknownRoutes() {
        return "This route is unavailable path='/something'. Error 404";
    }
}
