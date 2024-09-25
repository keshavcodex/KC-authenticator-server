package com.kc.authenticator.controller;

import com.kc.authenticator.dto.AppListResponse;
import com.kc.authenticator.dto.AppResponse;
import com.kc.authenticator.model.App;
import com.kc.authenticator.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/app")
public class AppController {

    @Autowired
    AppService clientAppService;

    @PostMapping("/createApp")
    public ResponseEntity<AppResponse> createApp(@RequestBody App body) {
        try {
            AppResponse response = clientAppService.createApp(body.getDevId(), body.getAppName());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAppByDevIdAndAppName")
    public ResponseEntity<AppResponse> getAppByDevIdAndAppName(@RequestParam("devId") String devId, @RequestParam("appName") String appName) {
        try {
            AppResponse response = clientAppService.getAppByDevIdAndAppName(devId, appName);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAllAppsByDevId")
    public ResponseEntity<AppListResponse> getAllAppsByDevId(@RequestParam("devId") String devId) {
        try {
            AppListResponse response = clientAppService.getAllAppsByDevId(devId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppListResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @GetMapping("/getAllApps")
    public ResponseEntity<AppListResponse> getAllApps() {
        try {
            AppListResponse response = clientAppService.getAllApps();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppListResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @DeleteMapping("/deleteApp")
    public ResponseEntity<AppResponse> deleteApp(@RequestParam("id") String id) {
        try {
            AppResponse response = clientAppService.deleteApp(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new AppResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @RequestMapping("/**")
    public ResponseEntity<AppResponse> unknownRoute() {
        return ResponseEntity.status(404).body(new AppResponse(null, "This route is unavailable in App controller.", false));
    }

}