package com.junaid.ai_journal.service;

import com.junaid.ai_journal.payload.dto.JournalReport;
import org.springframework.http.ResponseEntity;

public interface EmailService {
    void sendEmail(String to, String userName ,JournalReport report);
}
