package com.junaid.ai_journal.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
