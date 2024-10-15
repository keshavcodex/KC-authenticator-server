package com.kc.authenticator.repository;

import com.kc.authenticator.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findAllByAppId(String appId);
    Optional<User> findByAppIdAndEmail(String appId, String email);
    Optional<User> findByEmail(String email);
}
