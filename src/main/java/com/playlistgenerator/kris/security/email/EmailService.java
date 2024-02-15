package com.playlistgenerator.kris.security.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void send(String receiverEmail, String message, String emailObject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(message, true);
            helper.setTo(receiverEmail);
            helper.setSubject(emailObject);
            helper.setFrom("playlistgeneratorr@gmail.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }
    public String buildConfirmationEmail(String userName, String activationLink) {
        String emailContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "</head>"
                + "<body style='font-family: Arial, sans-serif; padding: 20px; background-color: #f8f9fa;'>"
                + "<div style='max-width: 600px; margin: auto; padding: 20px; background-color: white; border: 1px solid #ddd;'>"
                + "<h1 style='color: #000000; text-align: center;'>Playlist party generator | Activate Your Account</h1>"
                + "<p>Hello " + userName + ",</p>"
                + "<p>Thank you for registering in our website. Please click the button below to activate your account:</p>"
                + "<a href='" + activationLink + "' style='background-color: #007bff; color: white; padding: 10px 20px;"
                + "text-align: center; text-decoration: none; display: inline-block; font-size: 16px;"
                + "border: none; cursor: pointer; border-radius: 0.25rem;'>Activate Account</a>"
                + "<p style='margin-top: 20px;'>If you did not request this, please ignore this email.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        return emailContent;
    }

    public String buildPasswordRecoveryEmail(String userName, String recoveryLink) {
        String emailContent = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "</head>"
                + "<body style='font-family: Arial, sans-serif; padding: 20px; background-color: #f8f9fa;'>"
                + "<div style='max-width: 600px; margin: auto; padding: 20px; background-color: white; border: 1px solid #ddd;'>"
                + "<h1 style='color: #000000; text-align: center;'>Playlist party generator | Reset password</h1>"
                + "<p>Hello " + userName + ",</p>"
                + "<p>Please click the button below to reset your password:</p>"
                + "<a href='" + recoveryLink + "' style='background-color: #007bff; color: white; padding: 10px 20px;"
                + "text-align: center; text-decoration: none; display: inline-block; font-size: 16px;"
                + "border: none; cursor: pointer; border-radius: 0.25rem;'>Reset password</a>"
                + "<p style='margin-top: 20px;'>If you did not request this, please ignore this email.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        return emailContent;
    }
}
