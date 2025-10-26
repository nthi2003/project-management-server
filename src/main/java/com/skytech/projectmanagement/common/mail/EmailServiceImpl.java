package com.skytech.projectmanagement.common.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Yêu cầu đặt lại mật khẩu Project Management");

            message.setText("Mã token đặt lại mật khẩu của bạn là: " + token
                    + "\nToken này sẽ hết hạn trong 1 giờ.");

            mailSender.send(message);
            log.info("Đã gửi email reset password tới: {}", toEmail);
        } catch (Exception e) {
            log.error("Lỗi khi gửi email tới {}: {}", toEmail, e.getMessage());
        }
    }

}
