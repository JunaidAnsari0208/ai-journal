package com.junaid.ai_journal.service.impl;

import com.junaid.ai_journal.service.EmailService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @RateLimiter(name = "sendEmailRateLimiter", fallbackMethod = "fallbackEmailSender")
    public ResponseEntity<String> sendEmail(String to, String subject, String body) {
        try{
            logger.info("Attempting to send mail to: {}", to);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(body);
            simpleMailMessage.setSubject(subject);

            javaMailSender.send(simpleMailMessage);

            logger.info("Email sent successfully to {}", to);
            return ResponseEntity.ok("Email sent successfully to "+to);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    public ResponseEntity<String> fallbackEmailSender(String to, String subject, String body, Throwable throwable){
        logger.warn("Rate limiter triggered. Email to {} not sent. Reason: {}", to, throwable.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many email requests, please try again later.");
    }
}
