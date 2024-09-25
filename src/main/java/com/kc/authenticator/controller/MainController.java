package com.kc.authenticator.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
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
