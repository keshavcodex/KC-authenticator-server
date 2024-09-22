package com.kc.authenticator.repository;

import com.kc.authenticator.model.Dev;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DevRepository extends MongoRepository<Dev, String> {
    Dev findByDevEmail(String email);
}
