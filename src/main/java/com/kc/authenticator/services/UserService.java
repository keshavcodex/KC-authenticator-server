package com.kc.authenticator.services;

import com.kc.authenticator.dto.DecryptedData;
import com.kc.authenticator.dto.UserResponse;
import com.kc.authenticator.dto.UserListResponse;
import com.kc.authenticator.model.App;
import com.kc.authenticator.model.User;
import com.kc.authenticator.model.Token;
import com.kc.authenticator.model.User;
import com.kc.authenticator.repository.AppRepository;
import com.kc.authenticator.repository.UserRepository;
import com.kc.authenticator.utility.StringCheck;
import com.kc.authenticator.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Value("${app.tokenDuration}")
    private Integer tokenDuration;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AppRepository appRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    TimeUtility timeUtility;

    @Autowired
    EmailService emailService;

    @Autowired
    StringCheck stringCheck;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserResponse getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().removePassword();
            return new UserResponse(user.get(), "User found");
        }
        return new UserResponse(null, "User not found!!");
    }

    public UserListResponse getUsersByAppId(String appId) {
        List<User> userList = userRepository.findAllByAppId(appId);
        Collections.reverse(userList);
        return new UserListResponse(userList, "list of all users");
    }

    public UserResponse save(User user) {
        try {
            User newUser = userRepository.save(user);
            Optional<App> appEntity = appRepository.findById(user.getAppId());
            if (appEntity.isPresent()) {
                App app = appEntity.get();
                app.setUserCount(app.getUserCount() + 1);
                appRepository.save(app);
            }
            return new UserResponse(newUser, "User created successfully");
        } catch (Exception e) {
            System.out.println(e);
            return new UserResponse(null, "Email already registered on this app!!", false);
        }
    }

    public UserResponse createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    public UserResponse editUser(User user) {
        try {
            Optional<User> existingUserEntity = userRepository.findById(user.getId());
            if (existingUserEntity.isPresent()) {
                User existingUser = existingUserEntity.get();
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setPhone(user.getPhone());
                userRepository.save(existingUser);
                existingUser.removePassword();
                return new UserResponse(existingUser, "User edited successfully");
            }
            return new UserResponse(null, "User not found!!");
        } catch (Exception e) {
            System.out.println(e);
            return new UserResponse(null, "This email is already registered");
        }
    }

    public String deleteUser(String id) {
        Optional<User> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            Optional<App> appEntity = appRepository.findById(user.getAppId());
            if (appEntity.isPresent()) {
                App app = appEntity.get();
                app.setUserCount(app.getUserCount() - 1);
                appRepository.save(app);
                userRepository.deleteById(id);
                return "User deleted successfully";
            } else {
                userRepository.deleteById(id);
                return "user not connected to any app, if user present then deleted successfully";
            }
        }
        return "User id not available";
    }

    public UserResponse loginUser(User loginUser) {
        Optional<User> userEntity = userRepository.findByAppIdAndEmail(loginUser.getAppId(), loginUser.getEmail());
        if (userEntity.isPresent()) {
            User foundUser = userEntity.get();
            if (passwordEncoder.matches(loginUser.getPassword(), foundUser.getPassword())) {
                foundUser.removePassword();
                return new UserResponse(foundUser, "Logged in successfully", true);
            } else {
                return new UserResponse(null, "Password is incorrect");
            }
        }
        return new UserResponse(null, "no user found");
    }

    public Boolean isExistingUser(User user) {
        Optional<User> userEntity = userRepository.findByAppIdAndEmail(user.getAppId(), user.getEmail());
        return userEntity.isPresent();
    }

    public UserResponse sendPasswordResetLink(String appId, String email, String frontendUrl) {
        try {
//          Search the email if exist
            Optional<User> userEntity = userRepository.findByAppIdAndEmail(appId, email);
            if (userEntity.isEmpty()) {
                return new UserResponse(null, "Email does not exist.");
            }
            User user = userEntity.get();
//          then take the id add token and generate url
            String encryptedData = tokenService.generateEncryptedIdAndToken(user.getId());
//          then send url to email
            String resetUrl = frontendUrl + "?token=" + encryptedData;
            String subject = "KC Authenticator - Password reset request";
            String year = String.valueOf(java.time.Year.now().getValue());
            String lastMinute = timeUtility.getTime(tokenDuration);
            String body = String.format("<html>" + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" + "<div style='width: 100%%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);'>" + "<div style='background-color: #4CAF50; color: white; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;'>" + "<h1 style='margin: 0;'>KC Authenticator</h1>" + "<p style='font-size: 16px; margin-top: 5px;'>Your Security is Our Priority</p>" + "</div>" + "<div style='padding: 30px; color: #333; line-height: 1.6;'>" + "<h2 style='color: #4caf50; text-align: center; font-size: 23px; font-weight: 600;'>Password Reset Request</h2>" + "<h3>Hello %s,</h3>" + "<p>We have received a request to reset your password. To proceed, please click the button below to reset your password. Please note that this link will remain valid until %s.</p>" + "<div style='text-align: center;'>" + "<a href='%s' style='display: inline-block; padding: 10px 20px; font-size: 18px; font-weight: 500; color: white; background-color: #4caf50; text-decoration: none; border-radius: 5px;'>Reset</a>" + "</div>" + "<p>If you did not request this change, please ignore this email.</p>" + "<p>Thank you for being a valued user of KC Authenticator!</p>" + "<p>With warm regards,<br />The KC Authenticator Team</p>" + "</div>" + "<div style='padding: 20px; text-align: center; background-color: #f1f1f1; color: #777; font-size: 14px;'>" + "<p>&copy; %s KC Authenticator. All rights reserved.</p>" + "</div>" + "</div>" + "</body>" + "</html>", user.getFirstName(), lastMinute, resetUrl, year);
            emailService.sendEmail(user.getEmail(), subject, body);
            // Build a success response
            return new UserResponse(null, "Password reset link sent to your email.", true);
        } catch (Exception e) {
            // Handle error
            System.out.println(e);
            return new UserResponse(null, "Unable to send reset link.");
        }
    }

    public UserResponse updatePassword(String encryptedData, String password) {
//        decryptText
//        then check if token is still present in token collection in mongodb
//        then take the id and update password
//        return acknowledgement
        try {
            DecryptedData decryptedData = tokenService.decryptIdAndToken(encryptedData);
            System.out.println(decryptedData + ", " + password);
            if (!decryptedData.isValidData()) return new UserResponse(null, "wrong token provided by user!!");
            if (stringCheck.hasSpecialCharacter(decryptedData.getId()) || stringCheck.hasSpecialCharacter(decryptedData.getToken()))
                return new UserResponse(null, "modified token provided by user!!");
            Optional<Token> token = tokenService.getTokenAndDeleteToken(decryptedData.getId());
            if (token.isEmpty()) return new UserResponse(null, "Link has expired please try again.");
            String referenceId = token.get().getReferenceId();
            Optional<User> userEntity = userRepository.findById(referenceId);
            if (userEntity.isEmpty()) return new UserResponse(null, "User information not found try again!");
            User user = userEntity.get();
            user.setPassword(passwordEncoder.encode(password));
            User response = userRepository.save(user);
            response.removePassword();
            String subject = "KC Authenticator - Password changed";
            String year = String.valueOf(java.time.Year.now().getValue());
            String body = String.format("<html>" + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" + "<div style='width: 100%%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);'>" + "<div style='background-color: #4CAF50; color: white; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;'>" + "<h1 style='margin: 0;'>KC Authenticator</h1>" + "<p style='font-size: 16px; margin-top: 5px;'>Your Security is Our Priority</p>" + "</div>" + "<div style='padding: 30px; color: #333; line-height: 1.6;'>" + "<h2 style='color: #4caf50; text-align: center; font-size: 23px; font-weight: 600;'>Password Changed</h2>" + "<h3>Hello %s,</h3>" + "<p>Your password has been successfully changed. If you did not make this change, please reset your password immediately to secure your account.</p>" + "<p>If you need assistance, please reach out to our support team.</p>" + "<p>Thank you for being a valued user of KC Authenticator!</p>" + "<p>With warm regards,<br />The KC Authenticator Team</p>" + "</div>" + "<div style='padding: 20px; text-align: center; background-color: #f1f1f1; color: #777; font-size: 14px;'>" + "<p>&copy; %s KC Authenticator. All rights reserved.</p>" + "</div>" + "</div>" + "</body>" + "</html>", user.getFirstName(), year);
            emailService.sendEmail(user.getEmail(), subject, body);
            return new UserResponse(response, "Password updated successfully.");
        } catch (Exception e) {
            System.out.println(e);
            return new UserResponse(null, "password updation failed.", false);
        }
    }


}
