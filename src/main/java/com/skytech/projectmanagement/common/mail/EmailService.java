package com.skytech.projectmanagement.common.mail;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String token);
}
