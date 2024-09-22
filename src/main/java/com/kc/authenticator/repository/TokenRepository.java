package com.kc.authenticator.repository;

import com.kc.authenticator.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {

}
