package com.junaid.ai_journal.service;

import org.springframework.http.ResponseEntity;

public interface EmailService {
    ResponseEntity<String> sendEmail(String to, String subject, String body);
}
