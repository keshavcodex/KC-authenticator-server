package com.kc.authenticator.controller;

import com.kc.authenticator.dto.AppListResponse;
import com.kc.authenticator.dto.UserListResponse;
import com.kc.authenticator.dto.AppResponse;
import com.kc.authenticator.dto.UserResponse;
import com.kc.authenticator.model.App;
import com.kc.authenticator.model.User;
import com.kc.authenticator.services.AppService;
import com.kc.authenticator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    AppService appService;

    @Autowired
    UserService userService;

    @PostMapping("/createApp")
    public ResponseEntity<AppResponse> createApp(@RequestBody App app) {
        try {
            AppResponse response = appService.createApp(app.getDevId(), app.getAppName());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAppByDevIdAndAppName")
    public ResponseEntity<AppResponse> getAppByDevIdAndAppName(@RequestParam("devId") String devId, @RequestParam("appName") String appName) {
        try {
            AppResponse response = appService.getAppByDevIdAndAppName(devId, appName);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAllAppsByDevId")
    public ResponseEntity<AppListResponse> getAllAppsByDevId(@RequestParam("devId") String devId) {
        try {
            AppListResponse response = appService.getAllAppsByDevId(devId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppListResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getApp")
    public ResponseEntity<AppResponse> getApp(@RequestParam("id") String id) {
        try {
            AppResponse response = appService.getApp(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAllApps")
    public ResponseEntity<AppListResponse> getAllApps() {
        try {
            AppListResponse response = appService.getAllApps();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppListResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @PutMapping("/editApp")
    public ResponseEntity<AppResponse> editApp(@RequestBody App app) {
        try {
            AppResponse response = appService.editApp(app.getId(), app.getAppName());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @DeleteMapping("/deleteApp")
    public ResponseEntity<AppResponse> deleteApp(@RequestParam("id") String id) {
        try {
            AppResponse response = appService.deleteApp(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }


    //Users Services
    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createApp(@RequestBody User user) {
        try {
            UserResponse response = userService.createUser(user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getEndUser")
    public ResponseEntity<UserResponse> getUser(@RequestParam("id") String id) {
        try {
            UserResponse response = userService.getUserById(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while creating new app"));
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<UserListResponse> getAllUsers(@RequestParam("appId") String appId) {
        try {
            UserListResponse response = userService.getUsersByAppId(appId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserListResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @PutMapping("/editEndUser")
    public ResponseEntity<UserResponse> editUser(@RequestBody User user){
        try{
            UserResponse response = userService.editUser(user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e){
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while creating new app"));

        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<UserResponse> deleteUser(@RequestParam("id") String id) {
        try {
            String response = userService.deleteUser(id);
            return ResponseEntity.ok().body(new UserResponse(null, response, true));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @RequestMapping("/**")
    public ResponseEntity<AppResponse> unknownRoute() {
        return ResponseEntity.status(404).body(new AppResponse(null, "This route is unavailable in App controller.", false));
    }

}