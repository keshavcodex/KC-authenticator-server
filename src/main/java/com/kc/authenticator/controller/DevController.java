package com.kc.authenticator.controller;

import com.kc.authenticator.dto.DevListResponse;
import com.kc.authenticator.dto.DevResponse;
import com.kc.authenticator.dto.PasswordResetRequest;
import com.kc.authenticator.dto.PasswordUpdate;
import com.kc.authenticator.model.*;
import com.kc.authenticator.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/dev")
public class DevController {

    @Autowired
    private DevService devService;

    @Autowired
    private TempDevService tempDevService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService otpService;

    @GetMapping("/")
    public String home() {
        return "Welcome to home route of DevController";
    }

    @PostMapping("/login")
    public ResponseEntity<DevResponse> login(@RequestBody Dev dev) {
        try {
            DevResponse response = devService.loginDeveloper(dev.getDevEmail(), dev.getPassword());
            return ResponseEntity.accepted().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while loggin", false));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<DevResponse> signUp(@RequestBody Dev dev) {
        try {
            Dev existingDev = devService.getDevByEmail(dev.getDevEmail());
            if (existingDev != null) {
                return ResponseEntity.accepted().body(new DevResponse(null, "Developer already registered!", false));
            }
            TempDev tempDev = new TempDev(dev);
            tempDev = tempDevService.saveTempDev(tempDev);
            OTP generatedOtp = otpService.generateOTP(tempDev.getId(), tempDev.getDevEmail());
            Dev response = tempDev.removePassword();
            return ResponseEntity.ok(new DevResponse(response, "OTP send successfully", true));
        } catch (Exception e) {
            // Log the exception (logging framework would be used in a real application)
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Dev registration failed! " + e, false));
        }
    }

    @PostMapping("/validate-signup")
    public ResponseEntity<DevResponse> validateSignUp(@RequestBody OTP request) {
        boolean isValid = otpService.validateOTP(request.getReferenceId(), request.getOtp());

        TempDev tempDev = tempDevService.getById(request.getReferenceId());
        if (isValid && tempDev != null) {
            Dev dev = new Dev(tempDev);
            devService.saveDeveloper(dev);
            tempDevService.deleteDev(tempDev.getId());
            otpService.deleteOTP(request.getReferenceId());
            dev = dev.removePassword();
            return ResponseEntity.ok(new DevResponse(dev, "Developer Registration Successful."));
        } else {
            return ResponseEntity.accepted().body(new DevResponse(null, "Invalid OTP or OTP has expired", false));
        }
    }

    @GetMapping("/getDeveloper/{id}")
    public ResponseEntity<DevResponse> getDeveloperById(@PathVariable String id) {
        try {
            DevResponse response = devService.getDeveloperById(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while getting developer by id", false));
        }
    }

    @GetMapping("/getAllDevelopers")
    public ResponseEntity<DevListResponse> getAllDevelopers() {
        DevListResponse response = devService.getAllDevelopers();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/deleteDeveloper/{id}")
    public ResponseEntity<DevResponse> deleteDeveloperById(@PathVariable String id) {
        try {
            DevResponse response = devService.deleteDeveloperById(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while getting developer by id", false));
        }
    }

    @PutMapping("/editDeveloper")
    public ResponseEntity<DevResponse> editDeveloper(@RequestBody Dev dev) {
        try {
            DevResponse response = devService.editDeveloper(dev);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while getting developer by id", false));
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<DevResponse> resendOtp(@RequestParam("referenceId") String referenceId) {
        try {
            DevResponse response =  otpService.resendOtp(referenceId);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while getting developer by id", false));
        }
    }

    @PostMapping("/password-reset-link")
    public ResponseEntity<DevResponse> sendPasswordResetLink(@RequestBody PasswordResetRequest req) {
        try {
            DevResponse response = devService.sendPasswordResetLink(req.getEmail(), req.getFrontendUrl());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while sending password reset link to mail", false));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<DevResponse> updatePassword(@RequestBody PasswordUpdate req) {
        try {
            DevResponse response = devService.updatePassword(req.getToken(), req.getPassword());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new DevResponse(null, "Internal Server error while sending updating password", false));
        }
    }

    @RequestMapping("/**")
    public ResponseEntity<DevResponse> unknownRoute() {
        return ResponseEntity.status(404).body(new DevResponse(null, "This route is unavailable in developer controller.", false));
    }

}
