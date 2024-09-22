package com.kc.authenticator.repository;

import com.kc.authenticator.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
