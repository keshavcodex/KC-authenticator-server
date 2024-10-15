package com.kc.authenticator.controller;

import com.kc.authenticator.dto.UserListResponse;
import com.kc.authenticator.dto.UserResponse;
import com.kc.authenticator.dto.PasswordResetRequest;
import com.kc.authenticator.dto.PasswordUpdate;
import com.kc.authenticator.model.User;
import com.kc.authenticator.model.OTP;
import com.kc.authenticator.model.TempUser;
import com.kc.authenticator.services.OTPService;
import com.kc.authenticator.services.TempUserService;
import com.kc.authenticator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    TempUserService tempUserService;

    @Autowired
    OTPService otpService;

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody User user) {
        try {
            UserResponse response = userService.loginUser(user);
            return ResponseEntity.accepted().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while loggin", false));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody User user) {
        try {
            Boolean isExistingUser = userService.isExistingUser(user);
            if (isExistingUser) {
                return ResponseEntity.accepted().body(new UserResponse(null, "User already registered!"));
            }
            TempUser tempUser = new TempUser(user);
            String referenceId = tempUserService.saveTempUser(tempUser).getId();
            OTP generatedOtp = otpService.generateOTP(referenceId, tempUser.getEmail());
            tempUser.setId(referenceId);
            tempUser.removePassword();
            return ResponseEntity.ok(new UserResponse(tempUser, "OTP send successfully", true));
        } catch (Exception e) {
            // Log the exception (logging framework would be used in a real application)
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "User registration failed! " + e, false));
        }
    }

    @PostMapping("/validate-signup")
    public ResponseEntity<UserResponse> validateSignUp(@RequestBody OTP request) {
        boolean isValid = otpService.validateOTP(request.getReferenceId(), request.getOtp());

        TempUser tempUser = tempUserService.getById(request.getReferenceId());
        if (isValid && tempUser != null) {
            User user = new User(tempUser);
            userService.save(user);
            tempUserService.deleteUser(tempUser.getId());
            otpService.deleteOTP(request.getReferenceId());
            user.removePassword();
            return ResponseEntity.ok(new UserResponse(user, "User Registration Successful."));
        } else {
            return ResponseEntity.accepted().body(new UserResponse(null, "Invalid OTP or OTP has expired", false));
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponse> getUserById(@RequestParam("id") String id) {
        try {
            UserResponse response = userService.getUserById(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while getting User by id", false));
        }
    }

    @PutMapping("/editUser")
    public ResponseEntity<UserResponse> editUser(@RequestBody User user) {
        try {
            UserResponse response = userService.editUser(user);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while getting User by id", false));
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<UserResponse> deleteUser(@RequestParam("id") String id) {
        try {
            String response = userService.deleteUser(id);
            return ResponseEntity.ok().body(new UserResponse(null, response, true));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while creating new app", false));
        }
    }

    @PostMapping("/password-reset-link")
    public ResponseEntity<UserResponse> sendPasswordResetLink(@RequestBody PasswordResetRequest req) {
        try {
            UserResponse response = userService.sendPasswordResetLink(req.getAppId(), req.getEmail(), req.getFrontendUrl());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while sending password reset link to mail", false));
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<UserResponse> updatePassword(@RequestBody PasswordUpdate req) {
        try {
            UserResponse response = userService.updatePassword(req.getToken(), req.getPassword());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while sending updating password", false));
        }
    }
    /*
            @GetMapping("/getAllUsers")
            public ResponseEntity<UserListResponse> getAllUsers() {
                UserListResponse response = userService.getAllUsers();
                return ResponseEntity.ok().body(response);
            }

            @PostMapping("/resend-otp")
            public ResponseEntity<UserResponse> resendOtp(@RequestParam("referenceId") String referenceId) {
                try {
                    UserResponse response =  otpService.resendOtp(referenceId);
                    return ResponseEntity.ok().body(response);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body(new UserResponse(null, "Internal Server error while getting User by id", false));
                }
            }


        */
    @RequestMapping("/**")
    public String home() {
        return "This route is not available in EndUserController";
    }
}