package com.kc.authenticator.services;

import com.kc.authenticator.model.OTP;
import com.kc.authenticator.repository.OTPRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Value("${app.otpDuration}")
    private Integer otpDuration;

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private EmailService emailService;

    public void generateOTP(String email) {
        String otp = new DecimalFormat("000000").format(new Random().nextInt(1000000));
        Optional<OTP> existingOtp = otpRepository.findByDevEmail(email);

        OTP otpEntity;
        if (existingOtp.isPresent()) {
            otpEntity = existingOtp.get();
            otpEntity.setOtp(otp);
            otpEntity.resetExpires(otpDuration);
        } else {
            otpEntity = new OTP(otp, email, otpDuration);
        }
        otpRepository.save(otpEntity);
        //Sending the generated email
        try {
            String subject = "KC Authenticator - OTP for Authentication";
            String year = String.valueOf(java.time.Year.now().getValue());
            String body = String.format(
                    "<html>" +
                            "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;'>" +
                            "<div style='width: 100%%; max-width: 600px; margin: 20px auto; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);'>" +
                            "<div style='background-color: #4CAF50; color: white; padding: 20px; border-top-left-radius: 8px; border-top-right-radius: 8px; text-align: center;'>" +
                            "<h1 style='margin: 0;'>KC Authenticator</h1>" +
                            "<p style='font-size: 16px; margin-top: 5px;'>Your Security is Our Priority</p>" +
                            "</div>" +
                            "<div style='padding: 20px;'>" +
                            "<h2 style='color: #333; text-align: center;'>Your One-Time Password (OTP)</h2>" +
                            "<p style='font-size: 24px; font-weight: bold; color: #48c84c; text-align: center;'>%s</p>" +
                            "<p style='line-height: 1.5; color: #555;'>Please use this OTP to complete your authentication process. If you did not request this, please ignore this email.</p>" +
                            "<br>" +
                            "<p style='text-align: center;'>Thank you for using KC Authenticator.</p>" +
                            "<p style='text-align: center;'>Best Regards,<br>KC Authenticator Team</p>" +
                            "</div>" +
                            "<div style='background-color: #f4f4f4; padding: 10px; text-align: center; border-bottom-left-radius: 8px; border-bottom-right-radius: 8px;'>" +
                            "<p style='color: #777; font-size: 12px;'>© %s KC Authenticator. All rights reserved.</p>" +
                            "</div>" +
                            "</div>" +
                            "</body>" +
                            "</html>", otp, year
            );
            emailService.sendEmail(email, subject, body);
        } catch (MessagingException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    public Boolean validateOTP(String email, String otp) {
        OTP otpEntity = otpRepository.findByDevEmailAndOtp(email, otp).orElse(null);

        return otpEntity != null;
    }

    public void deleteOTP(String email) {
        OTP otpEntity = otpRepository.findByDevEmail(email).orElse(null);

        if (otpEntity != null) {
            otpRepository.delete(otpEntity);
        }
    }
}
