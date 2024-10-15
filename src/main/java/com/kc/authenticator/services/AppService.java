package com.kc.authenticator.services;

import com.kc.authenticator.dto.AppListResponse;
import com.kc.authenticator.dto.AppResponse;
import com.kc.authenticator.model.App;
import com.kc.authenticator.model.Dev;
import com.kc.authenticator.repository.AppRepository;
import com.kc.authenticator.repository.DevRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AppService {

    @Autowired
    AppRepository appRepository;

    @Autowired
    DevRepository devRepository;

    public AppResponse createApp(String devId, String appName) {
        try {

            Optional<Dev> dev = devRepository.findById(devId);
            if (!dev.isPresent()) return new AppResponse(null, "Developer not found.", false);

            // Check for existing app with the same name for the developer
            Optional<App> previousApp = appRepository.findByDevIdAndAppNameIgnoreCase(devId, appName);
            if (!previousApp.isEmpty()) {
                System.out.println(previousApp.get());
                return new AppResponse(null, "This app name is already present in this account.", false);
            }

            // Create and save new App
            App app = new App(devId, appName);
            App savedApp = appRepository.save(app);
            return new AppResponse(savedApp, "App successfully Created.");
        } catch (Exception e) {
            System.out.println(e);
            return new AppResponse(null, "App creation failed!!");

        }
    }

    public AppResponse editApp(String id, String appName) {

        Optional<App> existingApp = appRepository.findById(id);

        if (existingApp.isPresent()) {
            App app = existingApp.get();

            Optional<App> previousApp = appRepository.findByDevIdAndAppName(app.getDevId(), appName);
            if (!previousApp.isEmpty()) {
                System.out.println(previousApp.get());
                return new AppResponse(null, "This app name is already present in this account.", false);
            }

            app.setAppName(appName);
            appRepository.save(app);
            return new AppResponse(app, "App edited successfully", true);
        } else {
            return new AppResponse(null, "App not found", false);
        }
    }

    public AppListResponse getAllAppsByDevId(String devId) {
        List<App> appList = appRepository.findAllByDevId(devId);
        Collections.reverse(appList);
        return new AppListResponse(appList, "list of all apps");
    }

    public AppResponse getApp(String id) {
        Optional<App> app = appRepository.findById(id);
        if (app.isPresent()) {
            return new AppResponse(app.get(), "app found", true);
        } else {
            return new AppResponse(null, "app not found", false);
        }
    }

    public AppListResponse getAllApps() {
        List<App> appList = appRepository.findAll();
        Collections.reverse(appList);
        return new AppListResponse(appList, "list of all apps");
    }

    public AppResponse getAppByDevIdAndAppName(String devId, String appName) {
        Optional<App> app = appRepository.findByDevIdAndAppNameIgnoreCase(devId, appName);
        if (!app.isPresent()) return new AppResponse(null, "No app found!!", false);
        return new AppResponse(app.get(), "App found!!");
    }

    public AppResponse deleteApp(String id) {
        appRepository.deleteById(id);
        return new AppResponse(null, "App deleted successfully", true);
    }

}
