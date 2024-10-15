package com.kc.authenticator.repository;

import com.kc.authenticator.model.TempUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempUserRepository extends MongoRepository<TempUser, String> {
    TempUser findByEmail(String email);
    TempUser findByAppIdAndEmail(String appId, String email);
}
