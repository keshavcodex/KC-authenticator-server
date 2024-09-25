package com.kc.authenticator.repository;

import com.kc.authenticator.model.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OTPRepository extends MongoRepository<OTP, String> {
    Optional<OTP> findByReferenceId(String referenceId);
    Optional<OTP> findByReferenceIdAndOtp(String referenceId, String otp);
}
