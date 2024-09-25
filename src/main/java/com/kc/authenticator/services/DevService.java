package com.kc.authenticator.services;

import com.kc.authenticator.dto.DecryptedData;
import com.kc.authenticator.model.Dev;
import com.kc.authenticator.dto.DevResponse;
import com.kc.authenticator.dto.DevListResponse;
import com.kc.authenticator.model.Token;
import com.kc.authenticator.repository.DevRepository;
import com.kc.authenticator.utility.StringCheck;
import com.kc.authenticator.utility.TimeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DevService {

    @Value("${app.tokenDuration}")
    private Integer tokenDuration;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TimeUtility timeUtility;

    @Autowired
    private StringCheck stringCheck;

    @Autowired
    private final DevRepository devRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private EmailService emailService;

    public DevService(DevRepository devRepository) {
        this.devRepository = devRepository;
    }

    public Dev saveDeveloper(Dev developer) {
        return devRepository.save(developer);
    }

    public DevResponse loginDeveloper(String email, String password) {
        // Find the developer by email
        Dev developer = devRepository.findByDevEmail(email);

        if (developer == null) {
            return new DevResponse(null, "Email does not exist", false);
        }

        // Check if the provided password matches the stored hashed password
        if (passwordEncoder.matches(password, developer.getPassword())) {
            developer.removePassword();
            return new DevResponse(developer, "Logged in successfully", true);
        } else {
            return new DevResponse(null, "Password is incorrect", false);
        }
    }

    public DevListResponse getAllDevelopers() {
        List<Dev> developers = devRepository.findAll();

        // Remove passwords from each developer for security
        developers.forEach(Dev::removePassword);

        if (developers.isEmpty()) {
            return new DevListResponse(null, "No developers found", false);
        } else {
            return new DevListResponse(developers, "Developers retrieved successfully", true);
        }
    }

    public DevResponse getDeveloperById(String id) {
        // Fetch the developer using the repository
        Optional<Dev> developerOptional = devRepository.findById(id);

        if (developerOptional.isPresent()) {
            Dev developer = developerOptional.get().removePassword();
//            developer.removePassword(); // Remove password for security
            return new DevResponse(developer, "Developer retrieved successfully", true);
        } else {
            return new DevResponse(null, "Developer not found", false);
        }
    }

    public Dev getDevById(String id) {
        Optional<Dev> developerOptional = devRepository.findById(id);

        if (developerOptional.isPresent()) {
            Dev developer = developerOptional.get().removePassword();  // Remove password for security
            return developer;
        } else {
            return null;
        }
    }

    public DevResponse deleteDeveloperById(String id) {
        try {
            devRepository.deleteById(id);
            return new DevResponse(null, "Developer deleted successfully", true);
        } catch (Exception e) {
            return new DevResponse(null, "Failed to delete developer. Developer might not exist.", false);
        }
    }

    public DevResponse editDeveloper(Dev developer) {
        try {
            // Find the developer by ID
            Optional<Dev> existingDevOptional = devRepository.findById(developer.getId());

            if (existingDevOptional.isPresent()) {
                Dev existingDev = existingDevOptional.get();

                // Update the fields (you can add more fields as needed)
                existingDev.setFirstName(developer.getFirstName());
                existingDev.setLastName(developer.getLastName());
                existingDev.setPhone(developer.getPhone());

                // Save the updated developer
                devRepository.save(existingDev);
                existingDev.removePassword(); // Remove password for security in the response

                return new DevResponse(existingDev, "Developer updated successfully", true);
            } else {
                return new DevResponse(null, "Developer not found", false);
            }
        } catch (Exception e) {
            return new DevResponse(null, "Failed to update developer: " + e.getMessage(), false);
        }
    }

    public DevResponse sendPasswordResetLink(String email, String frontendUrl) {
        try {
//          Search the email if exist
            Dev dev = devRepository.findByDevEmail(email);
            if (dev == null) {
                return new DevResponse(null, "Email does not exist.", false);
            }
//          then take the id add token and generate url
            String encryptedData = tokenService.generateEncryptedIdAndToken(dev.getId());
//          then send url to email
            String resetUrl = frontendUrl + "?token=" + encryptedData;
            String subject = "KC Authenticator - Password reset request";
            String year = String.valueOf(java.time.Year.now().getValue());
            String lastMinute = timeUtility.getTime(tokenDuration);
            String body = String.format("<html>" + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" + "<div style='width: 100%%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);'>" + "<div style='background-color: #4CAF50; color: white; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;'>" + "<h1 style='margin: 0;'>KC Authenticator</h1>" + "<p style='font-size: 16px; margin-top: 5px;'>Your Security is Our Priority</p>" + "</div>" + "<div style='padding: 30px; color: #333; line-height: 1.6;'>" + "<h2 style='color: #4caf50; text-align: center; font-size: 23px; font-weight: 600;'>Password Reset Request</h2>" + "<h3>Hello %s,</h3>" + "<p>We have received a request to reset your password. To proceed, please click the button below to reset your password. Please note that this link will remain valid until %s.</p>" + "<div style='text-align: center;'>" + "<a href='%s' style='display: inline-block; padding: 10px 20px; font-size: 18px; font-weight: 500; color: white; background-color: #4caf50; text-decoration: none; border-radius: 5px;'>Reset</a>" + "</div>" + "<p>If you did not request this change, please ignore this email.</p>" + "<p>Thank you for being a valued user of KC Authenticator!</p>" + "<p>With warm regards,<br />The KC Authenticator Team</p>" + "</div>" + "<div style='padding: 20px; text-align: center; background-color: #f1f1f1; color: #777; font-size: 14px;'>" + "<p>&copy; %s KC Authenticator. All rights reserved.</p>" + "</div>" + "</div>" + "</body>" + "</html>", dev.getFirstName(), lastMinute, resetUrl, year);
            emailService.sendEmail(dev.getDevEmail(), subject, body);
            // Build a success response
            return new DevResponse(null, "Password reset link sent to your email.", true);
        } catch (Exception e) {
            // Handle error
            System.out.println(e);
            return new DevResponse(null, "Unable to send reset link.", false);
        }
    }

    public DevResponse updatePassword(String encryptedData, String password) {
//        decryptText
//        then check if token is still present in token collection in mongodb
//        then take the id and update password
//        return acknowledgement
        try {
            DecryptedData decryptedData = tokenService.decryptIdAndToken(encryptedData);
            if (!decryptedData.isValidData()) return new DevResponse(null, "wrong token provided by user!!", false);
            if (stringCheck.hasSpecialCharacter(decryptedData.getId()) || stringCheck.hasSpecialCharacter(decryptedData.getToken()))
                return new DevResponse(null, "modified token provided by user!!", false);
            Optional<Token> token = tokenService.getTokenAndDeleteToken(decryptedData.getId());
            if (!token.isPresent()) return new DevResponse(null, "Link has expired please try again.", false);
            String referenceId = token.get().getReferenceId();
            Dev dev = getDevById(referenceId);
            dev.setPassword(passwordEncoder.encode(password));
            Dev response = saveDeveloper(dev);
            response.removePassword();
            String subject = "KC Authenticator - Password changed";
            String year = String.valueOf(java.time.Year.now().getValue());
            String body = String.format("<html>" + "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" + "<div style='width: 100%%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);'>" + "<div style='background-color: #4CAF50; color: white; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;'>" + "<h1 style='margin: 0;'>KC Authenticator</h1>" + "<p style='font-size: 16px; margin-top: 5px;'>Your Security is Our Priority</p>" + "</div>" + "<div style='padding: 30px; color: #333; line-height: 1.6;'>" + "<h2 style='color: #4caf50; text-align: center; font-size: 23px; font-weight: 600;'>Password Changed</h2>" + "<h3>Hello %s,</h3>" + "<p>Your password has been successfully changed. If you did not make this change, please reset your password immediately to secure your account.</p>" + "<p>If you need assistance, please reach out to our support team.</p>" + "<p>Thank you for being a valued user of KC Authenticator!</p>" + "<p>With warm regards,<br />The KC Authenticator Team</p>" + "</div>" + "<div style='padding: 20px; text-align: center; background-color: #f1f1f1; color: #777; font-size: 14px;'>" + "<p>&copy; %s KC Authenticator. All rights reserved.</p>" + "</div>" + "</div>" + "</body>" + "</html>", dev.getFirstName(), year);
            emailService.sendEmail(dev.getDevEmail(), subject, body);
            return new DevResponse(response, "Password updated successfully.");
        } catch (Exception e) {
            System.out.println(e);
            return new DevResponse(null, "password updation failed.", false);
        }
    }

    public Dev getDevByEmail(String email) {
        return devRepository.findByDevEmail(email);
    }
}