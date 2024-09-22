package com.kc.authenticator.repository;

import com.kc.authenticator.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OTPRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByDevEmail(String email);
    Optional<OTP> findByDevEmailAndOtp(String email, String otp);
}
