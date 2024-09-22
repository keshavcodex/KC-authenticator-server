package com.kc.authenticator.services;

import com.kc.authenticator.dto.AppListResponse;
import com.kc.authenticator.dto.AppResponse;
import com.kc.authenticator.model.App;
import com.kc.authenticator.model.Dev;
import com.kc.authenticator.repository.AppRepository;
import com.kc.authenticator.repository.DevRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            Optional<App> previousApp = appRepository.findByDevIdAndAppName(devId, appName);
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

    public AppListResponse getAllAppsByDevId(String devId) {
        List<App> appList = appRepository.findAllByDevId(devId);
        return new AppListResponse(appList, "list of all apps");
    }

    public AppListResponse getAllApps() {
        List<App> appList = appRepository.findAll();
        return new AppListResponse(appList, "list of all apps");
    }

    public AppResponse getAppByDevIdAndAppName(String devId, String appName) {
        Optional<App> app = appRepository.findByDevIdAndAppName(devId, appName);
        if (!app.isPresent())
            return new AppResponse(null, "No app found!!", false);
        return new AppResponse(app.get(), "App found!!");
    }

    public AppResponse deleteApp(String id) {
        appRepository.deleteById(id);
        return new AppResponse(null, "App deleted successfully", true);
    }

}
