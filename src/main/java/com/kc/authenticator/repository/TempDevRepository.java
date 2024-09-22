package com.kc.authenticator.repository;

import com.kc.authenticator.model.TempDev;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TempDevRepository extends MongoRepository<TempDev, String> {
    TempDev findByDevEmail(String email);
}
