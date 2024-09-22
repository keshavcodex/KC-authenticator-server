package com.kc.authenticator.repository;

import com.kc.authenticator.model.EndUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<EndUser, String> {
}
