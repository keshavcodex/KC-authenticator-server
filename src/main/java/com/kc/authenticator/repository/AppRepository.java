package com.kc.authenticator.repository;

import com.kc.authenticator.model.App;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends MongoRepository<App, String> {
    Optional<App> findByDevIdAndAppName(String devId, String appName);
    List<App> findAllByDevId(String devId);

}
